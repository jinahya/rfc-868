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


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 *
 * @author Jin Kwon
 */
public final class Rfc868Constants {


    public static final int PORT_TCP = 0x25; // 37


    public static final int PORT_UDP = PORT_TCP; // 37


    public static final int BASE_YEAR = 1900;


    public static final Month BASE_MONTH = Month.JANUARY;


    public static final int BASE_DAY_OF_MONTH = 1;


    public static final int BASE_HOUR = 0;


    public static final int BASE_MINUTE = 0;


    private static final String TIME_ZONE_ID = "GMT";


    public static final LocalDate BASE_LOCAL_DATE
            = LocalDate.of(BASE_YEAR, BASE_MONTH, BASE_DAY_OF_MONTH);


    public static final LocalTime BASE_LOCAL_TIME
            = LocalTime.of(BASE_HOUR, BASE_MINUTE);


    public static final ZoneId BASE_ZONE_ID = ZoneId.of(TIME_ZONE_ID);


    public static final Instant BASE_INSTANT
            = ZonedDateTime.of(BASE_LOCAL_DATE, BASE_LOCAL_TIME, BASE_ZONE_ID)
            .toInstant();


    public static final TimeZone BASE_TIME_ZONE
            = TimeZone.getTimeZone(TIME_ZONE_ID);


    public static final long BASE_MILLIS;


    static {
        final Calendar calendar = new GregorianCalendar(BASE_TIME_ZONE);
        calendar.clear();
        calendar.set(Calendar.YEAR, BASE_YEAR);
        BASE_MILLIS = calendar.getTimeInMillis();
    }


    private Rfc868Constants() {

        super();
    }


}

