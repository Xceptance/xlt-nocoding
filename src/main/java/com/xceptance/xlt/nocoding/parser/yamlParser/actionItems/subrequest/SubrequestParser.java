package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.subrequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.AbstractActionItemParser;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

public class SubrequestParser extends AbstractActionItemParser
{

    /**
     * Parses the subrequest item in the action block to List<AbstractSubrequest>
     * 
     * @param node
     *            The node the item starts at
     * @return A list with all specified subrequest under that subrequest block
     * @throws IOException
     */
    @Override
    public List<AbstractActionItem> parse(final JsonNode node) throws IOException
    {
        // TODO Auto-generated method stub
        final List<AbstractSubrequest> subrequest = new ArrayList<AbstractSubrequest>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldNames = current.fieldNames();

            // the type of subrequest
            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();
                switch (fieldName)
                {
                    case Constants.XHR:
                        subrequest.add(new XhrSubrequestParser().parse(current.get(fieldName)));
                        break;

                    case Constants.STATIC:
                        // System.out.println(current.get(name));
                        final List<String> urls = new ArrayList<String>();
                        final JsonNode staticUrls = current.get(fieldName);

                        final Iterator<JsonNode> staticUrlsIterator = staticUrls.elements();
                        while (staticUrlsIterator.hasNext())
                        {
                            final String url = staticUrlsIterator.next().textValue();
                            urls.add(url);
                        }
                        // Catch empty url list
                        if (urls.isEmpty())
                        {
                            throw new IOException("No urls found");
                        }

                        subrequest.add(new StaticSubrequest(urls));
                        break;

                    default:
                        throw new IOException("No permitted subrequest item: " + fieldName);
                }

            }

        }
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        actionItems.addAll(subrequest);
        return actionItems;
    }

}
