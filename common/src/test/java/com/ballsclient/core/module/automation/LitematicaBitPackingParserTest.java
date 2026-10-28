package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.automation.model.LitematicaBitPackingParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Litematica Continuous Bit-Packing Across 64-Bit Boundaries Test")
class LitematicaBitPackingParserTest {

    public static int readEntry(long[] words, int index, int bitsPerBlock) {
        return LitematicaBitPackingParser.readEntry(words, index, bitsPerBlock);
    }

    public static void writeEntry(long[] words, int index, int bitsPerBlock, int value) {
        LitematicaBitPackingParser.writeEntry(words, index, bitsPerBlock, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 9, 11, 13, 16})
    @DisplayName("Round-trip encode and decode across 64-bit boundaries for variable bit widths")
    void testContinuousBitPackingRoundTrip(int bitsPerBlock) {
        int totalEntries = 500;
        int maxVal = (1 << bitsPerBlock) - 1;
        int totalBits = totalEntries * bitsPerBlock;
        int wordCount = (totalBits + 63) / 64;
        long[] words = new long[wordCount];

        int[] expectedValues = new int[totalEntries];
        Random rnd = new Random(42 + bitsPerBlock);

        for (int i = 0; i < totalEntries; i++) {
            expectedValues[i] = rnd.nextInt(maxVal + 1);
            writeEntry(words, i, bitsPerBlock, expectedValues[i]);
        }

        for (int i = 0; i < totalEntries; i++) {
            int decoded = readEntry(words, i, bitsPerBlock);
            assertThat(decoded)
                    .as("Entry %d mismatch for %d bits per block", i, bitsPerBlock)
                    .isEqualTo(expectedValues[i]);
        }
    }

    @Test
    @DisplayName("Explicit test of boundary-spanning entry at bit offset 60-64")
    void testBoundarySpanningEdgeCase() {
        int bitsPerBlock = 5;
        long[] words = new long[2];
        // Entry at index 12: bitIndex = 12 * 5 = 60.
        // It spans bits 60, 61, 62, 63 (word 0) and bit 0 (word 1).
        int testValue = 0b10111; // 23 in decimal
        writeEntry(words, 12, bitsPerBlock, testValue);

        int result = readEntry(words, 12, bitsPerBlock);
        assertThat(result).isEqualTo(testValue);
    }

    @Test
    @DisplayName("Pack and unpack array round-trip")
    void testPackAndUnpackHelper() {
        int bitsPerBlock = 7;
        int total = 120;
        int[] original = new int[total];
        for (int i = 0; i < total; i++) {
            original[i] = i % 128;
        }

        long[] packed = LitematicaBitPackingParser.pack(original, bitsPerBlock);
        int[] unpacked = LitematicaBitPackingParser.unpack(packed, total, bitsPerBlock);

        assertThat(unpacked).containsExactly(original);
    }

    @Test
    @DisplayName("Bits per block calculation matches minimum 2-bit invariant")
    void testCalculateBitsPerBlock() {
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(1)).isEqualTo(2);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(2)).isEqualTo(2);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(4)).isEqualTo(2);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(5)).isEqualTo(3);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(8)).isEqualTo(3);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(9)).isEqualTo(4);
        assertThat(LitematicaBitPackingParser.calculateBitsPerBlock(256)).isEqualTo(8);
    }
}
