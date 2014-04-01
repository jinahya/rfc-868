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


import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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


}

