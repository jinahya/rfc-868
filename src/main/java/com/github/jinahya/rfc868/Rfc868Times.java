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
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 *
 * @author Jin Kwon
 */
public final class Rfc868Times {


    /**
     * Returns the duration between the {@code base} and given {@code now}.
     *
     * @param now the {@code Temporal} representing {@code now}.
     *
     * @return the duration between the {@code base} and given {@code now}.
     *
     * @see Rfc868Constants#BASE_INSTANT
     */
    public static Duration get(final Temporal now) {

        return Duration.between(Rfc868Constants.BASE_INSTANT,
                                Objects.requireNonNull(now, "null now"));
    }


    public static Supplier<Duration> get(final Supplier<Temporal> now) {

        if (now == null) {
            throw new NullPointerException("null now");
        }

        return () -> get(now.get());
    }


    public static Function<Duration, Duration> get(
            final Supplier<Temporal> now, final Consumer<Duration> consumer) {

        return (d) -> {
            consumer.accept(get(now.get()));
            return d;
        };
    }


    /**
     * Returns the time in seconds since midnight on January first 1900.
     *
     * @param now the milliseconds from {@code epoch} representing current time.
     *
     * @return the seconds since midnight on January first 1990.
     */
    public static long get(final long now) {

        return (now - Rfc868Constants.BASE_MILLIS) / 1000L;
    }


    /**
     * Writes the time to specified byte array.
     *
     * @param now the current time in milliseconds from the {@code epoch}.
     * @param output the byte array to which the time is written
     * @param offset the offset in the byte array.
     *
     * @return the time written to {@code output}.
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


    /**
     * Read the time written on given byte array.
     *
     * @param input the byte array to read.
     * @param offset the offset in the array.
     *
     * @return the time read.
     */
    public static long get(final byte[] input, int offset) {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return (((input[offset++] & 0xFF) << 24)
                | ((input[offset++] & 0xFF) << 16)
                | ((input[offset++] & 0xFF) << 8)
                | (input[offset++] & 0xFF))
               & 0xFFFFFFFFL;
    }


    /**
     *
     * @param now
     * @param output
     *
     * @return the time written.
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

        return (((input.get() & 0xFF) << 24)
                | ((input.get() & 0xFF) << 16)
                | ((input.get() & 0xFF) << 8)
                | (input.get() & 0xFF))
               & 0xFFFFFFFFL;
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


    /**
     *
     * @param now the time in milliseconds from the {@code epoch} represents
     * current time.
     * @param output the {@code Socket} to which the time is written.
     *
     * @return the time written.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #put(long, java.io.OutputStream)
     */
    public static long put(final long now, final Socket output)
            throws IOException {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        return put(now, output.getOutputStream());
    }


    public static long get(final InputStream input) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return ((input.read() << 24)
                | (input.read() << 16)
                | (input.read() << 8)
                | input.read())
               & 0xFFFFFFFFL;
    }


    /**
     *
     * @param input the {@code Socket} to which the time written.
     *
     * @return the time written.
     *
     * @throws IOException if an I/O error occurs
     *
     * @see #get(java.io.InputStream)
     */
    public static long get(final Socket input) throws IOException {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return get(input.getInputStream());
    }


    /**
     * Write the time value to specified {@code DatagramPacket}.
     *
     * @param now the time in milliseconds since the {@code epoch}.
     * @param output the {@code DatagramPacket} to which the time is written.
     *
     * @return the time written.
     *
     * @see DatagramPacket#getData()
     * @see DatagramPacket#getOffset()
     * @see #put(long, byte[], int)
     */
    public static long put(final long now, final DatagramPacket output) {

        if (output == null) {
            throw new NullPointerException("null output");
        }

        return put(now, output.getData(), output.getOffset());
    }


    /**
     * Read the time value from given {@code DatagramPacket}.
     *
     * @param input the {@code DatagramPacket} from which the time is read.
     *
     * @return the time value read form the {@code input}.
     *
     * @see DatagramPacket#getData()
     * @see DatagramPacket#getOffset()
     * @see #get(byte[], int)
     */
    public static long get(final DatagramPacket input) {

        if (input == null) {
            throw new NullPointerException("null input");
        }

        return get(input.getData(), input.getOffset());
    }


    /**
     * Creates a new instance.
     */
    private Rfc868Times() {

        super();
    }


}

