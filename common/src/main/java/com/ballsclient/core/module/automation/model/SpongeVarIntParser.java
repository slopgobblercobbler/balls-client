package com.ballsclient.core.module.automation.model;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Sponge v2/v3 LEB128 VarInt streaming parser.
 * Encodes and decodes 7-bit variable-length integers with continuation flags (bit 7).
 */
public final class SpongeVarIntParser {

    private SpongeVarIntParser() {
    }

    /**
     * Reads a single LEB128 VarInt from a ByteBuffer.
     *
     * @param buffer byte buffer containing VarInt stream
     * @return decoded integer value
     * @throws IllegalStateException    if buffer terminates before end of VarInt
     * @throws IllegalArgumentException if VarInt exceeds 5 bytes (overflow)
     */
    public static int readVarInt(ByteBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("ByteBuffer cannot be null");
        }
        int value = 0;
        int position = 0;
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            value |= (b & 0x7F) << position;
            if ((b & 0x80) == 0) {
                return value;
            }
            position += 7;
            if (position >= 35) {
                throw new IllegalArgumentException("VarInt overflow (> 5 bytes)");
            }
        }
        throw new IllegalStateException("Unexpected end of VarInt byte stream");
    }

    /**
     * Writes an LEB128 VarInt to a ByteArrayOutputStream.
     */
    public static void writeVarInt(ByteArrayOutputStream out, int value) {
        if (out == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }
        while ((value & ~0x7F) != 0) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.write(value & 0x7F);
    }

    /**
     * Writes an LEB128 VarInt to a ByteBuffer.
     */
    public static void writeVarInt(ByteBuffer buffer, int value) {
        if (buffer == null) {
            throw new IllegalArgumentException("ByteBuffer cannot be null");
        }
        while ((value & ~0x7F) != 0) {
            buffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        buffer.put((byte) (value & 0x7F));
    }

    /**
     * Encodes an array of integer values into an LEB128 byte array.
     */
    public static byte[] encodeAll(int[] values) {
        if (values == null || values.length == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(values.length * 2);
        for (int v : values) {
            writeVarInt(out, v);
        }
        return out.toByteArray();
    }

    /**
     * Decodes count VarInts from a ByteBuffer into an integer array.
     */
    public static int[] readAll(ByteBuffer buffer, int count) {
        if (buffer == null || count <= 0) {
            return new int[0];
        }
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = readVarInt(buffer);
        }
        return result;
    }
}
