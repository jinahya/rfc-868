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


import com.google.common.collect.ObjectArrays;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
public class Rfc868TimesTest {


    /**
     * logger.
     */
    private static final Logger logger
            = LoggerFactory.getLogger(Rfc868TimesTest.class);


    private static void putAndGet(final long now, final Class<?>[] outputTypes,
                                  final Stream<Supplier<?>> outputSuppliers,
                                  final Class<?>[] inputTypes,
                                  final Stream<Supplier<?>> inputSuppliers)
            throws NoSuchMethodException, IllegalAccessException,
                   InvocationTargetException {

        final Method put = Rfc868Times.class.getMethod(
                "put", ObjectArrays.concat(Long.TYPE, outputTypes));
        final long expected = (Long) put.invoke(
                null, ObjectArrays.concat(
                        now, outputSuppliers.<Object>map(
                                (s) -> s.get()).toArray()));

        final Method get = Rfc868Times.class.getMethod("get", inputTypes);
        final long actual = (Long) get.invoke(
                null, inputSuppliers.<Object>map((s) -> s.get()).toArray());

        Assert.assertEquals(actual, expected);
    }


    private static void putAndGet(final long now, final Class<?>[] types,
                                  final Stream<Supplier<?>> outputSuppliers,
                                  final Stream<Supplier<?>> inputSuppliers)
            throws NoSuchMethodException, IllegalAccessException,
                   InvocationTargetException {

        putAndGet(now, types, outputSuppliers, types, inputSuppliers);
    }


    private static <O, I> void putAndGet(final long now,
                                         final Class<O> outputType,
                                         final Supplier<O> outputSupplier,
                                         final Class<I> inputType,
                                         final Supplier<I> inputSupplier)
            throws NoSuchMethodException, IllegalAccessException,
                   InvocationTargetException {

        final Method put = Rfc868Times.class.getMethod("put", Long.TYPE,
                                                       outputType);
        final long expected = (Long) put.invoke(null, now,
                                                outputSupplier.get());

        final Method get = Rfc868Times.class.getMethod("get", inputType);
        final long actual = (Long) get.invoke(null, inputSupplier.get());

        Assert.assertEquals(actual, expected);
    }


    private static <T> void putAndGet(final long now, final Class<T> type,
                                      final Supplier<T> outputSupplier,
                                      final Supplier<T> inputSupplier)
            throws NoSuchMethodException, IllegalAccessException,
                   InvocationTargetException {

        putAndGet(now, type, outputSupplier, type, inputSupplier);
    }


    @Test
    public static void getTime_ZonedDateTime_() {

        final ZonedDateTime now = ZonedDateTime.now();

        final Duration time = Rfc868Times.get(now);
        logger.debug("time: {}", time);
    }


    @Test
    public static void getTime_CurrentTimeMillis() {

        final long now = System.currentTimeMillis();

        final long time = Rfc868Times.get(now); // seconds from 1 Jan 1900
        logger.debug("time: {}", time);

        final Calendar calendar
                = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.clear();
        calendar.setTimeInMillis(now - (time * 1000L));

        Assert.assertEquals(calendar.get(Calendar.YEAR), 1900);
        Assert.assertEquals(calendar.get(Calendar.MONTH), Calendar.JANUARY);
        Assert.assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 1);
        Assert.assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
        Assert.assertEquals(calendar.get(Calendar.MINUTE), 0);
        Assert.assertEquals(calendar.get(Calendar.SECOND), 0);
        //Assert.assertEquals(calendar.get(Calendar.MILLISECOND), 0);

        logger.debug("calendar.time: {}", calendar.getTime());
    }


    @DataProvider
    public static Iterator<Object[]> provideByteArrayAndOffset() {

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final int length = random.nextInt(4, 128);
        final int offset = length == 4 ? 0 : random.nextInt(0, length - 4);

        return Collections.singleton(new Object[]{new byte[length], offset})
                .iterator();
    }


    @Test(dataProvider = "provideByteArrayAndOffset")
    public static void put_(final byte[] output, final int offset) {

        final long time = Rfc868Times.put(System.currentTimeMillis(), output,
                                          offset);
    }


    @Test(dataProvider = "provideByteArrayAndOffset")
    public static void get_(final byte[] output, final int offset) {

        final long time = Rfc868Times.get(output, 0);
    }


    @Test(dataProvider = "provideByteArrayAndOffset")
    public static void putAndGet(final byte[] output, final int offset)
            throws ReflectiveOperationException {

        putAndGet(offset, new Class<?>[]{byte[].class, Integer.TYPE},
                  Stream.of(() -> output, () -> offset),
                  Stream.of(() -> output, () -> offset));
    }


    @DataProvider
    public static Iterator<Object[]> provideByteBuffer() {

        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);

        return Collections.singleton(new Object[]{buffer}).iterator();
    }


    @Test(dataProvider = "provideByteBuffer")
    public static void put_(final ByteBuffer output) {

        final long time = Rfc868Times.put(System.currentTimeMillis(), output);
    }


    @Test(dataProvider = "provideByteBuffer")
    public static void get_(final ByteBuffer input) {

        final long itme = Rfc868Times.get(input);
    }


    @Test(dataProvider = "provideByteBuffer")
    public static void putAnGet(final ByteBuffer buffer)
            throws ReflectiveOperationException {

        putAndGet(System.currentTimeMillis(), ByteBuffer.class, () -> buffer,
                  () -> (ByteBuffer) buffer.flip());
    }


    @DataProvider
    public static Object[][] provideDatagramPacket() {

        final byte[] data = new byte[4];
        final DatagramPacket packet = new DatagramPacket(data, data.length);

        return new Object[][]{new Object[]{packet}};
    }


    @Test(dataProvider = "provideDatagramPacket")
    public static void put_(final DatagramPacket output) {

        Rfc868Times.put(System.currentTimeMillis(), output);
    }


    @Test(dataProvider = "provideDatagramPacket")
    public static void get_(final DatagramPacket input) {

        final long time = Rfc868Times.get(input);
        logger.debug("time in DatagramPacket: {}", time);
    }


    @Test(dataProvider = "provideDatagramPacket")
    public static void putAndGet(final DatagramPacket packet)
            throws ReflectiveOperationException {

        putAndGet(System.currentTimeMillis(), DatagramPacket.class,
                  () -> packet, () -> packet);
    }


}

