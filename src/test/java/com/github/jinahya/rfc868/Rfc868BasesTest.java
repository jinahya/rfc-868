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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
public class Rfc868BasesTest {


    /**
     * logger.
     */
    private static final Logger logger
            = LoggerFactory.getLogger(Rfc868BasesTest.class);


    @Test
    public static void BASE_() {

        logger.debug("BASE_ZONED_DATE_TIME: {}",
                     Rfc868Bases.BASE_ZONED_DATE_TIME);
    }


}

