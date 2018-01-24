/**
 *  Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.xceptance.xlt.nocoding.util.variableResolver;

import org.apache.commons.lang3.RandomStringUtils;

import com.xceptance.xlt.api.util.XltRandom;

/**
 * Provides an interface to some often used functions to get random data. Methods can be accessed by using
 * ${RANDOM.methodName(arg1, ...)}. For example:
 * <ul>
 * <li><b>${RANDOM.String(x)}</b> : a random string with length x. Contains [A-Za-z].
 * <li><b>${RANDOM.String(s, x)}</b> : a random string with length x. Contains letters from s.
 * <li><b>${RANDOM.Number(max)}</b> : returns an integer between 0 (inclusive) and max (inclusive)
 * <li><b>${RANDOM.Number(min, max)}</b> : returns an integer between min (inclusive) and max (inclusive)
 * </ul>
 */
public class ParameterInterpreterRandom
{
    public int Number(final int max)
    {
        return Number(0, max);
    }

    public int Number(final int minimum, final int maximum)
    {
        return XltRandom.nextInt(minimum, maximum);
    }

    public String String(final int length)
    {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public String String(final String characters, final int length)
    {
        return RandomStringUtils.random(length, characters);
    }

    public String DigitString(final int length)
    {
        return RandomStringUtils.randomNumeric(length);
    }

    public String Email()
    {
        return String(5) + "@" + String(5) + ".com";
    }

    public String Email(final int i)
    {
        return String(i) + "@" + String(i) + ".com";
    }

    public String Email(final int i, final int j)
    {
        return String(i) + "@" + String(j) + ".com";
    }

}