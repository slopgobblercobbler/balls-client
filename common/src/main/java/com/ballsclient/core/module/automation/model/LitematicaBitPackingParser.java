package com.ballsclient.core.module.automation.model;

/**
 * Litematica continuous bit-packing encoder and decoder.
 * Unlike vanilla PalettedContainer (which pads long words so entries never cross
 * 64-bit boundaries), the Litematica .litematic format packs entries in an unbroken,
 * continuous bitstream across word boundaries.
 */
public final class LitematicaBitPackingParser {

    private LitematicaBitPackingParser() {
    }

    /**
     * Calculates the minimum bit-width required to store a palette of given size.
     * Guaranteed to be at least 2 bits per block per Litematica specification.
     */
    public static int calculateBitsPerBlock(int paletteSize) {
        if (paletteSize <= 1) {
            return 2;
        }
        int bits = 32 - Integer.numberOfLeadingZeros(paletteSize - 1);
        return Math.max(2, bits);
    }

    /**
     * Reads a bit-packed entry at the specified sequential block index.
     * Handles continuous bit-packing across 64-bit word boundaries.
     *
     * @param words        underlying bitstream stored as long array
     * @param index        linear 0-based block index
     * @param bitsPerBlock bit-width of each entry (1 to 32)
     * @return decoded integer value
     */
    public static int readEntry(long[] words, int index, int bitsPerBlock) {
        if (words == null || words.length == 0 || bitsPerBlock <= 0) {
            return 0;
        }
        long bitIndex = (long) index * bitsPerBlock;
        int startWord = (int) (bitIndex >>> 6);
        int endWord = (int) ((bitIndex + bitsPerBlock - 1) >>> 6);
        int startOffset = (int) (bitIndex & 63);
        long mask = bitsPerBlock == 64 ? ~0L : (1L << bitsPerBlock) - 1L;

        if (startWord >= words.length) {
            return 0;
        }

        if (startWord == endWord) {
            return (int) ((words[startWord] >>> startOffset) & mask);
        } else {
            int bitsInFirstWord = 64 - startOffset;
            long lowBits = words[startWord] >>> startOffset;
            long highBits = endWord < words.length ? words[endWord] & ((1L << (bitsPerBlock - bitsInFirstWord)) - 1L) : 0L;
            return (int) (lowBits | (highBits << bitsInFirstWord));
        }
    }

    /**
     * Writes an entry at the specified sequential block index.
     * Handles continuous bit-packing across 64-bit word boundaries.
     *
     * @param words        underlying bitstream stored as long array
     * @param index        linear 0-based block index
     * @param bitsPerBlock bit-width of each entry (1 to 32)
     * @param value        integer value to encode
     */
    public static void writeEntry(long[] words, int index, int bitsPerBlock, int value) {
        if (words == null || words.length == 0 || bitsPerBlock <= 0) {
            return;
        }
        long bitIndex = (long) index * bitsPerBlock;
        int startWord = (int) (bitIndex >>> 6);
        int endWord = (int) ((bitIndex + bitsPerBlock - 1) >>> 6);
        int startOffset = (int) (bitIndex & 63);
        long mask = bitsPerBlock == 64 ? ~0L : (1L << bitsPerBlock) - 1L;
        long val = value & mask;

        if (startWord >= words.length) {
            return;
        }

        if (startWord == endWord) {
            words[startWord] &= ~(mask << startOffset);
            words[startWord] |= (val << startOffset);
        } else {
            int bitsInFirstWord = 64 - startOffset;
            words[startWord] &= ~(mask << startOffset);
            words[startWord] |= (val << startOffset);

            if (endWord < words.length) {
                int bitsInSecondWord = bitsPerBlock - bitsInFirstWord;
                long highMask = (1L << bitsInSecondWord) - 1L;
                words[endWord] &= ~highMask;
                words[endWord] |= (val >>> bitsInFirstWord);
            }
        }
    }

    /**
     * Packs an array of integers into a continuous bit-packed long array.
     */
    public static long[] pack(int[] entries, int bitsPerBlock) {
        if (entries == null || entries.length == 0 || bitsPerBlock <= 0) {
            return new long[0];
        }
        long totalBits = (long) entries.length * bitsPerBlock;
        int wordCount = (int) ((totalBits + 63) / 64);
        long[] words = new long[wordCount];
        for (int i = 0; i < entries.length; i++) {
            writeEntry(words, i, bitsPerBlock, entries[i]);
        }
        return words;
    }

    /**
     * Unpacks a continuous bit-packed long array into an array of integers.
     */
    public static int[] unpack(long[] words, int totalEntries, int bitsPerBlock) {
        if (words == null || totalEntries <= 0 || bitsPerBlock <= 0) {
            return new int[0];
        }
        int[] result = new int[totalEntries];
        for (int i = 0; i < totalEntries; i++) {
            result[i] = readEntry(words, i, bitsPerBlock);
        }
        return result;
    }
}
