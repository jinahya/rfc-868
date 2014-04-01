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
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Objects;


/**
 *
 * @author Jin Kwon
 */
public class Rfc868Times {


//    public static long getTime(final long now) {
//
//        if (now < 0L) {
//            throw new IllegalArgumentException("now(" + now + ") < 0L");
//        }
//
//        return (now - Rfc868Constants.BASE_DATE.getTime()) / 1000L;
//    }
//
//
//    public static long getTime() {
//
//        return getTime(System.currentTimeMillis());
//    }
//
//
//    public static long getTime(final Date now) {
//
//        if (now == null) {
//            throw new NullPointerException("null now");
//        }
//
//        return getTime(now.getTime());
//    }
//
    public static long getTime(final Temporal now) {

        return Duration.between(Rfc868Bases.BASE_INSTANT,
                                Objects.requireNonNull(now, "null now"))
                .get(ChronoUnit.SECONDS);
    }


    public static long getTime(final long now) {

        return (now - Rfc868Bases.BASE_MILLIS_FROM_EPOCH) / 1000L;
    }


    private Rfc868Times() {

        super();
    }


}

