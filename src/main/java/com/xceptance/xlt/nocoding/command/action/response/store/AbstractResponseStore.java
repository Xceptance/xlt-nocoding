package com.xceptance.xlt.nocoding.command.action.response.store;

import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;

/**
 * The abstract class for every response item that interacts with the storage.
 *
 * @author ckeiner
 */
public abstract class AbstractResponseStore extends AbstractResponseSubItem
{
    /**
     * The name of the variable
     */
    protected final String variableName;

    /**
     * The selector for the value of the variable
     */
    protected final AbstractExtractor extractor;

    /**
     * Sets {@link #variableName} and {@link #extractor}.
     *
     * @param variableName
     *            The name of the variable
     * @param extractor
     *            The selector to use for the value
     */
    public AbstractResponseStore(final String variableName, final AbstractExtractor extractor)
    {
        this.variableName = variableName;
        this.extractor = extractor;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public AbstractExtractor getExtractor()
    {
        return extractor;
    }

}
