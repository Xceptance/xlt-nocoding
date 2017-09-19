package com.xceptance.xlt.nocoding.util;

import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;

public class DefaultValue
{
    /**
     * Request Block
     */
    public static HttpMethod METHOD = HttpMethod.GET;

    public static boolean ENCODE_PARAMETERS = false;

    public static boolean IS_XHR = false;

    public static boolean ENCODE_BODY = false;

    /**
     * Response Block
     */
    public static int HTTPCODE = 200;

    /**
     * Subrequest Block
     */

    /**
     * Other default values
     */
    public static Map<String, String> defaultValues = new HashMap<String, String>();

}
