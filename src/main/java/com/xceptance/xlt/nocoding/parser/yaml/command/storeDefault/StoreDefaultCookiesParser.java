package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.util.NameValuePair;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing default cookies.
 *
 * @author ckeiner
 */
public class StoreDefaultCookiesParser extends AbstractStoreDefaultSubItemsParser
{

    /**
     * Parses the cookies list item to a list of {@link AbstractStoreDefaultItem}s which consists of multiple
     * {@link StoreDefaultCookie}.
     *
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param defaultCookiesNode
     *            The {@link Node} the default cookies start at
     * @return A list of <code>StoreDefault</code>s with the parsed default cookies.
     */
    @Override
    public List<AbstractStoreDefaultItem> parse(final Mark context, final Node defaultCookiesNode)
    {
        // Create list of defaultItems
        final List<AbstractStoreDefaultItem> defaultItems = new ArrayList<>();

        if (defaultCookiesNode instanceof ScalarNode)
        {
            final String value = YamlParserUtils.transformScalarNodeToString(context, defaultCookiesNode);
            if (value.equals(Constants.DELETE))
            {
                defaultItems.add(new StoreDefaultCookie(Constants.COOKIES, Constants.DELETE));
            }
            else
            {
                throw new ParserException("Node", context, " contains " + value + " but it must either be an array or " + Constants.DELETE,
                                          defaultCookiesNode.getStartMark());
            }
        }
        else
        {
            // Parse the ArrayNode as NameValuePair
            final List<NameValuePair> cookies = YamlParserUtils.getSequenceNodeAsNameValuePair(context, defaultCookiesNode);
            for (final NameValuePair cookie : cookies)
            {
                // Create a StoreDefaultHeader for every header key value pair
                defaultItems.add(new StoreDefaultCookie(cookie.getName(), cookie.getValue()));
            }
        }
        // Return all default items
        return defaultItems;
    }

}
