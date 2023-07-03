/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.util.resolver;

import org.junit.Test;

/**
 * Tests {@link ParameterInterpreterRandom}
 *
 * @author ckeiner
 */
public class ParameterInterpreterRandomTest
{

    /**
     * Tests the constructor
     */
    @Test
    public void testConstructor()
    {
        @SuppressWarnings("unused")
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
    }

    /**
     * Calls {@link ParameterInterpreterRandom#Email()}
     */
    @Test
    public void testEmail()
    {
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
        final String email = pir.Email();
        System.err.println(email);
    }
}
