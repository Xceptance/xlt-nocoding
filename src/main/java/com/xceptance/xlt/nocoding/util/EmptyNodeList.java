package com.xceptance.xlt.nocoding.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An always empty {@link NodeList}.
 */
public class EmptyNodeList implements NodeList
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index)
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength()
    {
        return 0;
    }
}
