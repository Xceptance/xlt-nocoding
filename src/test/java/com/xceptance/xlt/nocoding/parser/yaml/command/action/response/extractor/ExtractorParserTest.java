package com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.nodes.MappingNode;

import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserTestHelper;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing an extractor definition from YAML and creating the correct extractor instance.
 */
public class ExtractorParserTest extends AbstractParserTest
{
    @Test
    public void parseCookieExtractor() throws Exception
    {
        final String cookieName = "SID";

        final String yamlSpec = Constants.COOKIE + " : " + cookieName;

        parseAndValidateCookieExtractor(yamlSpec, cookieName, null, null);
    }

    @Test
    public void parseCookieExtractorWithRegex() throws Exception
    {
        final String cookieName = "SID";
        final String regex = ".*";

        final String yamlSpec = Constants.COOKIE + " : " + cookieName + "\n" + //
                                Constants.REGEXP + " : " + regex;

        parseAndValidateCookieExtractor(yamlSpec, cookieName, regex, null);
    }

    @Test
    public void parseCookieExtractorWithRegexAndGroup() throws Exception
    {
        final String cookieName = "SID";
        final String regex = ".*";
        final String group = "1";

        final String yamlSpec = Constants.COOKIE + " : " + cookieName + "\n" + //
                                Constants.REGEXP + " : " + regex + "\n" + //
                                Constants.GROUP + " : " + group;

        parseAndValidateCookieExtractor(yamlSpec, cookieName, regex, group);
    }

    @Test
    public void parseCookieExtractorWithRegexAndGroup2() throws Exception
    {
        final String cookieName = "SID";
        final String regex = ".*";
        final String group = "1";

        final String yamlSpec = Constants.REGEXP + " : " + regex + "\n" + //
                                Constants.GROUP + " : " + group + "\n" + //
                                Constants.COOKIE + " : " + cookieName;

        parseAndValidateCookieExtractor(yamlSpec, cookieName, regex, group);
    }

    @Test
    public void parseHeaderExtractor() throws Exception
    {
        final String headerName = "Location";

        final String yamlSpec = Constants.HEADER + " : " + headerName;

        parseAndValidateHeaderExtractor(yamlSpec, headerName, null, null);
    }

    @Test
    public void parseHeaderExtractorWithRegex() throws Exception
    {
        final String headerName = "Location";
        final String regex = ".*";

        final String yamlSpec = Constants.HEADER + " : " + headerName + "\n" + //
                                Constants.REGEXP + " : " + regex;

        parseAndValidateHeaderExtractor(yamlSpec, headerName, regex, null);
    }

    @Test
    public void parseHeaderExtractorWithRegexAndGroup() throws Exception
    {
        final String headerName = "Location";
        final String regex = ".*";
        final String group = "1";

        final String yamlSpec = Constants.HEADER + " : " + headerName + "\n" + //
                                Constants.REGEXP + " : " + regex + "\n" + //
                                Constants.GROUP + " : " + group;

        parseAndValidateHeaderExtractor(yamlSpec, headerName, regex, group);
    }

    @Test
    public void parseHeaderExtractorWithRegexAndGroup2() throws Exception
    {
        final String headerName = "Location";
        final String regex = ".*";
        final String group = "1";

        final String yamlSpec = Constants.REGEXP + " : " + regex + "\n" + //
                                Constants.GROUP + " : " + group + "\n" + //
                                Constants.HEADER + " : " + headerName;

        parseAndValidateHeaderExtractor(yamlSpec, headerName, regex, group);
    }

    @Test
    public void parseRegexpExtractor() throws Exception
    {
        final String regex = ".*";

        final String yamlSpec = Constants.REGEXP + " : " + regex;

        parseAndValidateRegexpExtractor(yamlSpec, regex, null);
    }

    @Test
    public void parseRegexpExtractorWithGroup() throws Exception
    {
        final String regex = ".*";
        final String group = "1";

        final String yamlSpec = Constants.REGEXP + " : " + regex + "\n" + //
                                Constants.GROUP + " : " + group;

        parseAndValidateRegexpExtractor(yamlSpec, regex, group);
    }

    @Test
    public void parseXpathExtractor() throws Exception
    {
        final String xpath = "//div";

        final String yamlSpec = Constants.XPATH + " : " + xpath + "\n";

        parseAndValidateXpathExtractor(yamlSpec, xpath);
    }

    // --- helper methods ---------------------------------

    private void parseAndValidateCookieExtractor(final String yaml, final String expectedCookieName, final String expectedRegex,
                                                 final String expectedGroup)
    {
        final AbstractExtractor extractor = parseAndValidateAbstractExtractor(yaml, expectedCookieName);

        // special validation for cookie extractor
        Assert.assertTrue(extractor instanceof CookieExtractor);
        final CookieExtractor cookieExtractor = (CookieExtractor) extractor;
        Assert.assertEquals(expectedRegex, cookieExtractor.getRegex());
        Assert.assertEquals(expectedGroup, cookieExtractor.getGroup());
    }

    private void parseAndValidateHeaderExtractor(final String yaml, final String expectedHeaderName, final String expectedRegex,
                                                 final String expectedGroup)
    {
        final AbstractExtractor extractor = parseAndValidateAbstractExtractor(yaml, expectedHeaderName);

        // special validation for header extractor
        Assert.assertTrue(extractor instanceof HeaderExtractor);
        final HeaderExtractor headerExtractor = (HeaderExtractor) extractor;
        Assert.assertEquals(expectedRegex, headerExtractor.getRegex());
        Assert.assertEquals(expectedGroup, headerExtractor.getGroup());
    }

    private void parseAndValidateRegexpExtractor(final String yaml, final String expectedRegex, final String expectedGroup)
    {
        final AbstractExtractor extractor = parseAndValidateAbstractExtractor(yaml, expectedRegex);

        // special validation for regexp extractor
        Assert.assertTrue(extractor instanceof RegexpExtractor);
        final RegexpExtractor regexpExtractor = (RegexpExtractor) extractor;
        Assert.assertEquals(expectedGroup, regexpExtractor.getGroup());
    }

    private void parseAndValidateXpathExtractor(final String yaml, final String xpath)
    {
        final AbstractExtractor extractor = parseAndValidateAbstractExtractor(yaml, xpath);

        // special validation for xpath extractor
        Assert.assertTrue(extractor instanceof XpathExtractor);
    }

    private AbstractExtractor parseAndValidateAbstractExtractor(final String yaml, final String extractionExpression)
    {
        // parse extractor
        final MappingNode node = (MappingNode) YamlParserTestHelper.parseToNode(yaml);
        final AbstractExtractor extractor = new ExtractorParser().parse(node.getStartMark(), node.getValue());

        // general extractor validation
        Assert.assertEquals(extractionExpression, extractor.getExtractionExpression());

        return extractor;
    }
}
