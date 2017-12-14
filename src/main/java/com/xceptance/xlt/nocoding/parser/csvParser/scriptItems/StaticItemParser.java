package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class StaticItemParser
{

    public AbstractSubrequest parse(final JsonNode node)
    {
        final Iterator<String> fieldNames = node.fieldNames();
        final List<String> urls = new ArrayList<String>();
        String encoded = null;
        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();
            if (!(node.get(fieldName) instanceof NullNode) && !node.get(fieldName).isNull())
            {
                switch (fieldName)
                {
                    case CsvConstants.URL:
                        String url = ParserUtils.readValue(node, fieldName);
                        url = url.trim();
                        final String quotationMark = "\"";
                        if (url.startsWith(quotationMark) && url.endsWith(quotationMark))
                        {
                            url = url.substring(1, url.length() - 1);
                        }
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
        return new StaticSubrequest(urls);
    }

}
