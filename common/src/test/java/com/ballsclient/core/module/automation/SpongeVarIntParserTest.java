package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.automation.model.SpongeVarIntParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sponge v2 LEB128 VarInt Streaming Parser Test")
class SpongeVarIntParserTest {

    public static void writeVarInt(ByteArrayOutputStream out, int value) {
        SpongeVarIntParser.writeVarInt(out, value);
    }

    public static int readVarInt(ByteBuffer buffer) {
        return SpongeVarIntParser.readVarInt(buffer);
    }

    @Test
    @DisplayName("Single-byte VarInt values (0 to 127)")
    void testSingleByteVarInt() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeVarInt(out, 0);
        writeVarInt(out, 1);
        writeVarInt(out, 127);

        ByteBuffer buf = ByteBuffer.wrap(out.toByteArray());
        assertThat(readVarInt(buf)).isEqualTo(0);
        assertThat(readVarInt(buf)).isEqualTo(1);
        assertThat(readVarInt(buf)).isEqualTo(127);
    }

    @Test
    @DisplayName("Multi-byte VarInt values (128, 300, 16384, 2097151)")
    void testMultiByteVarInt() {
        int[] testCases = {128, 255, 300, 16384, 65535, 2097151};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int v : testCases) {
            writeVarInt(out, v);
        }

        ByteBuffer buf = ByteBuffer.wrap(out.toByteArray());
        for (int expected : testCases) {
            assertThat(readVarInt(buf)).isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("Truncated byte buffer throws IllegalStateException")
    void testTruncatedBufferFailsafe() {
        // 0x80 means more bytes follow, but buffer terminates immediately
        ByteBuffer buf = ByteBuffer.wrap(new byte[]{(byte) 0x80});
        assertThatThrownBy(() -> readVarInt(buf))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unexpected end of VarInt byte stream");
    }

    @Test
    @DisplayName("VarInt exceeding 5 bytes throws IllegalArgumentException")
    void testVarIntOverflowProtection() {
        // 5 continuous bytes with 0x80 bit set followed by terminating byte
        ByteBuffer buf = ByteBuffer.wrap(new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x01
        });
        assertThatThrownBy(() -> readVarInt(buf))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("VarInt overflow");
    }

    @Test
    @DisplayName("Bulk encode and decode VarInt array round-trip")
    void testBulkEncodeDecode() {
        int[] values = {0, 1, 42, 127, 128, 255, 1000, 65536, 1000000};
        byte[] bytes = SpongeVarIntParser.encodeAll(values);
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        int[] decoded = SpongeVarIntParser.readAll(buf, values.length);

        assertThat(decoded).containsExactly(values);
    }
}
