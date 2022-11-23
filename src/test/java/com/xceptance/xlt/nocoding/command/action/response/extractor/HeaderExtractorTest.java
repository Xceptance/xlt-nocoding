package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests for {@link HeaderExtractor}
 *
 * @author ckeiner
 */
public class HeaderExtractorTest extends ExtractorTest
{

    public HeaderExtractorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies that {@link HeaderExtractor} can extract a header
     *
     * @throws Throwable
     */
    @Test
    public void testHeaderExtractor() throws Throwable
    {
        final AbstractExtractor extractor = new HeaderExtractor("Set-Cookie");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieName2 + "=" + mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieName3 + "=" + mockObjects.cookieValue3, result.get(2));
    }

    /**
     * Verifies that {@link HeaderExtractor} can extract a header when its a variable
     *
     * @throws Throwable
     */
    @Test
    public void testHeaderExtractorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = "Set-Cookie";
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new HeaderExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieName2 + "=" + mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieName3 + "=" + mockObjects.cookieValue3, result.get(2));
    }

    /**
     * Verifies the {@link HeaderExtractor} finds nothing if the name of the header is not found
     *
     * @throws Throwable
     */
    @Test
    public void testHeaderExtractorNoResult() throws Throwable
    {
        final AbstractExtractor extractor = new HeaderExtractor("Dont-Set-Cookie");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(0, result.size());
    }

    /**
     * Verifies the {@link HeaderExtractor} can extract a part of a header value using an additional regex and matching
     * group.
     *
     * @throws Throwable
     */
    @Test
    public void testHeaderExtractorWithValueRegexAndGroup() throws Throwable
    {
        final AbstractExtractor extractor = new HeaderExtractor("Set-Cookie", "([-a-z])=(\\d+)", "2");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieValue3, result.get(2));
    }

    /**
     * Verifies the {@link HeaderExtractor} can extract a part of a header value when regex and matching group are
     * variables.
     *
     * @throws Throwable
     */
    @Test
    public void testHeaderExtractorWithVariablesForRegexAndGroup() throws Throwable
    {
        final String regexVariableName = "variable_1";
        final String groupVariableName = "variable_2";
        context.getVariables().store(regexVariableName, "([-a-z])=(\\d+)");
        context.getVariables().store(groupVariableName, "2");
        final AbstractExtractor extractor = new HeaderExtractor("Set-Cookie", "${" + regexVariableName + "}",
                                                                "${" + groupVariableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieValue3, result.get(2));
    }
}
