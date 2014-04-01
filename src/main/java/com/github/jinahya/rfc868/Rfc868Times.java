/*
 * Copyright 2014 Jin Kwon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.rfc868;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Objects;


/**
 *
 * @author Jin Kwon
 */
public final class Rfc868Times {


    /**
     *
     * @param now
     *
     * @return
     */
    public static Duration get(final Temporal now) {

        return Duration.between(Rfc868Constants.BASE_INSTANT,
                                Objects.requireNonNull(now, "null now"));
    }


    /**
     *
     * @param now
     *
     * @return
     */
    public static long get(final long now) {

        return (now - Rfc868Constants.BASE_MILLIS) / 1000L;
    }


    /**
     *
     * @param now
     * @param output
     * @param offset
     *
     * @return
     *
     * @see #get(long)
     */
    public static long put(final long now, final byte[] output, int offset) {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        if (false && offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ") < 0");
        }

        if (false && offset + 4 >= output.length) {
            throw new IllegalArgumentException(
                    "offset(" + offset + ") + 4 >= output.length("
                    + output.length + ")");
        }

        final long time = get(now);

        output[offset++] = (byte) ((time >> 0x18) & 0xFF);
        output[offset++] = (byte) ((time >> 0x10) & 0xFF);
        output[offset++] = (byte) ((time >> 0x08) & 0xFF);
        output[offset++] = (byte) (time & 0xFF);

        return time;
    }


    public static long get(final byte[] input, int offset) {

        return ((input[offset++] & 0xFF) << 24)
               | ((input[offset++] & 0xFF) << 16)
               | ((input[offset++] & 0xFF) << 8)
               | (input[offset++] & 0xFF);
    }


    /**
     *
     * @param now
     * @param output
     *
     * @return
     *
     * @see #get(long)
     */
    public static long put(final long now, final ByteBuffer output) {

        if (output == null) {
            throw new NullPointerException("null buffer");
        }

        if (false && output.remaining() < 4) {
            throw new IllegalArgumentException(
                    "output.remaining(" + output.remaining() + ") < 4");
        }

        final long time = get(now);

        output.put((byte) ((time >> 0x18) & 0xFF));
        output.put((byte) ((time >> 0x10) & 0xFF));
        output.put((byte) ((time >> 0x08) & 0xFF));
        output.put((byte) (time & 0xFF));

        return time;
    }


    public static long get(final ByteBuffer input) {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return ((input.get() & 0xFF) << 24)
               | ((input.get() & 0xFF) << 16)
               | ((input.get() & 0xFF) << 8)
               | (input.get() & 0xFF);
    }


    public static long put(final long now, final OutputStream output)
            throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        final long time = get(now);

        output.write((int) ((time >> 24) & 0xFF));
        output.write((int) ((time >> 0x10) & 0xFF));
        output.write((int) ((time >> 0x08) & 0xFF));
        output.write((int) (time & 0xFF));

        return time;
    }


    public static long get(final InputStream input) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return (input.read() << 24)
               | (input.read() << 16)
               | (input.read() << 8)
               | input.read();
    }


    private Rfc868Times() {

        super();
    }


}

