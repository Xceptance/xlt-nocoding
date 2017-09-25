package com.xceptance.xlt.nocoding.util.dataStorage;

import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;

public class DefaultValue
{
    /**
     * Request Block
     */
    public static HttpMethod METHOD = HttpMethod.GET;

    public static Boolean XHR = false;

    public static Boolean ENCODEPARAMETERS = false;

    public static Boolean ENCODEBODY = false;

    /**
     * Response Block
     */
    public static Integer HTTPCODE = 200;

    /**
     * Subrequest Block
     */

    /**
     * Other default values
     */
    public static Map<String, String> defaultValues = new HashMap<String, String>();

    public static Map<String, String> putDefault(final String key, final String value)
    {
        defaultValues.put(key, value);
        return defaultValues;
    }

}
