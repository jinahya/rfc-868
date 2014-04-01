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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 *
 * @author Jin Kwon
 */
public final class Rfc868Bases {


    public static final int BASE_YEAR = 1900;


    public static final String BASE_TIME_ZONE_ID = "GMT";


    private static final LocalDate BASE_LOCAL_DATE
            = LocalDate.of(BASE_YEAR, 1, 1);


    private static final LocalTime BASE_LOCAL_TIME = LocalTime.of(0, 0);


    public static final ZoneId BASE_ZONE_ID = ZoneId.of(BASE_TIME_ZONE_ID);


    public static final ZonedDateTime BASE_ZONED_DATE_TIME
            = ZonedDateTime.of(BASE_LOCAL_DATE, BASE_LOCAL_TIME, BASE_ZONE_ID);


    public static final Instant BASE_INSTANT
            = ZonedDateTime.of(BASE_LOCAL_DATE, BASE_LOCAL_TIME, BASE_ZONE_ID)
            .toInstant();


    private static final TemporalAmount YEARS_FROM_EPOCH
            = Period.ofYears(1970 - 1900);


    private static final Duration DURATION_BETWEEN_EPOCH_AND_BASE
            = Duration.between(BASE_ZONED_DATE_TIME.plus(YEARS_FROM_EPOCH),
                               BASE_ZONED_DATE_TIME);


    public static final long BASE_NANOS_FROM_EPOCH
            = DURATION_BETWEEN_EPOCH_AND_BASE.get(ChronoUnit.NANOS);


    public static final long BASE_SECONDS_FROM_EPOCH
            = DURATION_BETWEEN_EPOCH_AND_BASE.get(ChronoUnit.SECONDS);


    public static final TimeZone BASE_TIME_ZONE
            = TimeZone.getTimeZone(BASE_TIME_ZONE_ID);


    public static final long BASE_MILLIS_FROM_EPOCH;


    static {
        final Calendar calendar = new GregorianCalendar(BASE_TIME_ZONE);
        calendar.clear();
        calendar.set(Calendar.YEAR, BASE_YEAR);
        BASE_MILLIS_FROM_EPOCH = calendar.getTimeInMillis();
    }


    private Rfc868Bases() {

        super();
    }


}

