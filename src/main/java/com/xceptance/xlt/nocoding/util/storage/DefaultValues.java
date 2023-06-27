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
package com.xceptance.xlt.nocoding.util.storage;

import org.htmlunit.HttpMethod;

/**
 * Definitions for normal default values.
 *
 * @author ckeiner
 */
public class DefaultValues
{
    /**
     * Request Block
     */
    public static String METHOD = HttpMethod.GET.toString();

    public static String XHR = "false";

    public static String ENCODEPARAMETERS = "false";

    public static String ENCODEBODY = "false";

    /**
     * Response Block
     */
    public static String HTTPCODE = "200";

    /**
     * Subrequest Block
     */

}
