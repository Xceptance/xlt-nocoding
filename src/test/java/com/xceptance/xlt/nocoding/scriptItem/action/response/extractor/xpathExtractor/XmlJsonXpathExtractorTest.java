package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.ExtractorTest;
import com.xceptance.xlt.nocoding.util.MockWebResponse;

public class XmlJsonXpathExtractorTest extends ExtractorTest
{

    String urlString = "http://www.xceptance.com";

    URL url;

    WebResponse jsonResponse;

    String jsonType = "text/json";

    String jsonContent = "{" + "  \"geodata\": [" + "    {" + "      \"id\": \"1\"," + "      \"name\": \"Julie Sherman\","
                         + "      \"gender\" : \"female\"," + "      \"latitude\" : \"37.33774833333334\","
                         + "      \"longitude\" : \"-121.88670166666667\"" + "    }," + "    {" + "      \"id\": \"2\","
                         + "      \"name\": \"Johnny Depp\"," + "      \"gender\" : \"male\"," + "      \"latitude\" : \"37.336453\","
                         + "      \"longitude\" : \"-121.884985\"" + "    }" + "  ]" + "}";

    WebResponse xmlResponse;

    String xmlType = "text/xml";

    String xmlContent = "<data>" + " <employee>" + "   <name>John</name>" + "   <title>Manager</title>" + " </employee>" + " <employee>"
                        + "   <name>Sara</name>" + "   <title>Clerk</title>" + " </employee>" + "</data>";

    WebResponse unknownTypeResponse;

    WebResponse maliciousJsonContentResponse;

    /**
     * {@link #jsonContent} without ending }
     */
    String maliciousJsonContent = "{" + "  \"geodata\": [" + "    {" + "      \"id\": \"1\"," + "      \"name\": \"Julie Sherman\","
                                  + "      \"gender\" : \"female\"," + "      \"latitude\" : \"37.33774833333334\","
                                  + "      \"longitude\" : \"-121.88670166666667\"" + "    }," + "    {" + "      \"id\": \"2\","
                                  + "      \"name\": \"Johnny Depp\"," + "      \"gender\" : \"male\","
                                  + "      \"latitude\" : \"37.336453\"," + "      \"longitude\" : \"-121.884985\"" + "    }" + "  ]";

    WebResponse maliciousXmlContentResponse;

    /**
     * {@link #xmlContent} without ending data tag
     */
    String maliciousXmlContent = "<data>" + " <employee>" + "   <name>John</name>" + "   <title>Manager</title>" + " </employee>"
                                 + " <employee>" + "   <name>Sara</name>" + "   <title>Clerk</title>" + " </employee>";

    @Before
    public void setup() throws MalformedURLException
    {
        url = new URL(urlString);

        jsonResponse = new MockWebResponse(jsonContent, url, jsonType);

        xmlResponse = new MockWebResponse(xmlContent, url, xmlType);

        unknownTypeResponse = new MockWebResponse("", url, "unknown");

        maliciousJsonContentResponse = new MockWebResponse(maliciousJsonContent, url, jsonType);

        maliciousXmlContentResponse = new MockWebResponse(maliciousXmlContent, url, xmlType);
    }

    /**
     * Verifies you can get Json Content
     */
    @Test
    public void testGetByXPathWithJson()
    {
        context.setWebResponse(jsonResponse);
        final XmlJsonXpathExtractor xpathResponse = new XmlJsonXpathExtractor("//latitude");
        xpathResponse.execute(context);
        final List<String> list = xpathResponse.getResult();
        final String lat0 = list.get(0);
        final String lat1 = list.get(1);
        Assert.assertEquals("37.33774833333334", lat0);
        Assert.assertEquals("37.336453", lat1);
    }

    /**
     * Verifies you can get XML Content
     */
    @Test
    public void testGetByXPathWithXML()
    {
        context.setWebResponse(xmlResponse);
        final XmlJsonXpathExtractor xpathResponse = new XmlJsonXpathExtractor("//title");
        xpathResponse.execute(context);
        final List<String> list = xpathResponse.getResult();
        final String tit0 = list.get(0);
        final String tit1 = list.get(1);
        Assert.assertEquals("Manager", tit0);
        Assert.assertEquals("Clerk", tit1);
    }

    /**
     * Verifies an error is thrown when the Json Content is faulty
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetByXPathWithMaliciousJson()
    {
        context.setWebResponse(maliciousJsonContentResponse);
        final XmlJsonXpathExtractor xpathResponse = new XmlJsonXpathExtractor("//latitude");
        xpathResponse.execute(context);
    }

    /**
     * Verifies an error is thrown when the Xml Content is faulty
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetByXPathWithMaliciousXml()
    {
        context.setWebResponse(maliciousXmlContentResponse);
        final XmlJsonXpathExtractor xpathResponse = new XmlJsonXpathExtractor("//title");
        xpathResponse.execute(context);
    }

}
