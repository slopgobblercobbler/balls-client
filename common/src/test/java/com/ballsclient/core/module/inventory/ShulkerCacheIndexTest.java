package com.ballsclient.core.module.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Shulker Box NBT Cache & Search Index Test")
class ShulkerCacheIndexTest {

    public record CachedItem(String id, int count, String displayName) {}

    /**
     * Deterministic hex digest of shulker box 27-slot contents.
     */
    public static String computeShulkerContentsHash(List<CachedItem> contents) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (CachedItem item : contents) {
                if (item != null) {
                    String row = item.id() + ":" + item.count() + ":" + (item.displayName() != null ? item.displayName() : "");
                    digest.update(row.getBytes(StandardCharsets.UTF_8));
                }
            }
            byte[] hash = digest.digest();
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Headless LRU Shulker Cache with eviction tracking.
     */
    public static class ShulkerLRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxEntries;
        private final List<K> evictedKeys = new ArrayList<>();

        public ShulkerLRUCache(int maxEntries) {
            super(maxEntries, 0.75f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            if (size() > maxEntries) {
                evictedKeys.add(eldest.getKey());
                return true;
            }
            return false;
        }

        public List<K> getEvictedKeys() {
            return Collections.unmodifiableList(evictedKeys);
        }
    }

    /**
     * Multi-field item search index across cached shulker boxes.
     */
    public static class ShulkerSearchIndex {
        private final Map<String, List<CachedItem>> cache = new HashMap<>();

        public void indexShulker(String shulkerBoxId, List<CachedItem> items) {
            cache.put(shulkerBoxId, items);
        }

        public List<String> searchShulkers(String query) {
            String lowerQuery = query.toLowerCase();
            List<String> matchingShulkers = new ArrayList<>();

            for (Map.Entry<String, List<CachedItem>> entry : cache.entrySet()) {
                boolean matchFound = false;
                for (CachedItem item : entry.getValue()) {
                    if (item.id().toLowerCase().contains(lowerQuery) ||
                            (item.displayName() != null && item.displayName().toLowerCase().contains(lowerQuery))) {
                        matchFound = true;
                        break;
                    }
                }
                if (matchFound) {
                    matchingShulkers.add(entry.getKey());
                }
            }
            return matchingShulkers;
        }
    }

    @Test
    @DisplayName("Shulker Index: Deterministic hex digest matches for identical contents")
    void testDeterministicHexDigest() {
        List<CachedItem> itemsA = List.of(
                new CachedItem("minecraft:netherite_ingot", 64, null),
                new CachedItem("minecraft:diamond_block", 32, "Vault Reserve")
        );
        List<CachedItem> itemsB = List.of(
                new CachedItem("minecraft:netherite_ingot", 64, null),
                new CachedItem("minecraft:diamond_block", 32, "Vault Reserve")
        );

        String hashA = computeShulkerContentsHash(itemsA);
        String hashB = computeShulkerContentsHash(itemsB);

        assertThat(hashA)
                .as("Identical contents must yield identical SHA-256 hex digests")
                .isEqualTo(hashB);
        assertThat(hashA).hasSize(64);
    }

    @Test
    @DisplayName("Shulker Index: Modified item count alters content hash")
    void testHashChangesOnModification() {
        List<CachedItem> original = List.of(new CachedItem("minecraft:diamond", 64, null));
        List<CachedItem> modified = List.of(new CachedItem("minecraft:diamond", 63, null));

        String hashA = computeShulkerContentsHash(original);
        String hashB = computeShulkerContentsHash(modified);

        assertThat(hashA).isNotEqualTo(hashB);
    }

    @Test
    @DisplayName("Shulker Cache: LRU eviction strictly caps maximum entries and evicts eldest")
    void testLRUEviction() {
        ShulkerLRUCache<String, String> lru = new ShulkerLRUCache<>(3);
        lru.put("box-1", "hash-1");
        lru.put("box-2", "hash-2");
        lru.put("box-3", "hash-3");

        // Access box-1 to mark it as recently used
        lru.get("box-1");

        // Insert 4th element: box-2 is now the eldest and should be evicted
        lru.put("box-4", "hash-4");

        assertThat(lru.size()).isEqualTo(3);
        assertThat(lru.containsKey("box-2")).isFalse();
        assertThat(lru.containsKey("box-1")).isTrue();
        assertThat(lru.containsKey("box-3")).isTrue();
        assertThat(lru.containsKey("box-4")).isTrue();
        assertThat(lru.getEvictedKeys()).containsExactly("box-2");
    }

    @Test
    @DisplayName("Shulker Search: Substring and displayName query returns matching shulkers")
    void testSearchIndexQuery() {
        ShulkerSearchIndex index = new ShulkerSearchIndex();
        index.indexShulker("red-box", List.of(
                new CachedItem("minecraft:redstone", 64, null),
                new CachedItem("minecraft:repeater", 16, null)
        ));
        index.indexShulker("blue-box", List.of(
                new CachedItem("minecraft:lapis_lazuli", 64, null),
                new CachedItem("minecraft:enchanted_golden_apple", 8, "God Apple")
        ));
        index.indexShulker("black-box", List.of(
                new CachedItem("minecraft:obsidian", 64, null)
        ));

        List<String> redstoneResults = index.searchShulkers("redstone");
        assertThat(redstoneResults).containsExactly("red-box");

        List<String> appleResults = index.searchShulkers("god apple");
        assertThat(appleResults).containsExactly("blue-box");

        List<String> nonExistent = index.searchShulkers("elytra");
        assertThat(nonExistent).isEmpty();
    }
}
