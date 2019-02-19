package com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault;

import java.util.List;

import org.yaml.snakeyaml.nodes.Node;

import com.xceptance.xlt.nocoding.command.storeDefault.AbstractStoreDefaultItem;

/**
 * Abstract class that defines the method for parsing default items.
 *
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultSubItemsParser
{
    /**
     * Parses the default item at the specified {@link Node}.
     *
     * @param defaultItemNode
     *            The <code>Node</code> the default item starts at
     * @return {@link AbstractStoreDefaultItem}
     */
    public abstract List<AbstractStoreDefaultItem> parse(Node defaultItemNode);
}
