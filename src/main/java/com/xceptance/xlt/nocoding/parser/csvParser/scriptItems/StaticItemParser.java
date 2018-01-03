package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses a node to a {@link StaticSubrequest}
 * 
 * @author ckeiner
 */
public class StaticItemParser
{

    /**
     * Parses the node to a {@link StaticSubrequest}
     * 
     * @param node
     * @return
     */
    public StaticSubrequest parse(final JsonNode node)
    {
        // Initialize variables
        final List<String> urls = new ArrayList<String>();
        String encoded = null;

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = node.fieldNames();
        // While there are still fieldNames
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();
            // Ignore every field that is null
            if (!node.get(fieldName).isNull())
            {
                switch (fieldName)
                {
                    case CsvConstants.URL:
                        // Read the value and save it in url
                        String url = ParserUtils.readValue(node, fieldName).trim();
                        // Remove quotation marks in the beginning and end
                        final String quotationMark = "\"";
                        if (url.startsWith(quotationMark) && url.endsWith(quotationMark))
                        {
                            url = url.substring(1, url.length() - 1);
                        }
                        // Add the url to the list of urls
                        urls.add(url);
                        break;
                    case CsvConstants.ENCODED:
                        encoded = ParserUtils.readValue(node, fieldName);
                        break;

                    default:
                        break;
                }
            }
        }
        // Return the StaticSubrequest
        return new StaticSubrequest(urls);
    }

}
