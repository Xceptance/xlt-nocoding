package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebResponse;

import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * Extracts an element from the {@link WebResponse} in the {@link Context}. The extraction happens in
 * {@link AbstractExtractor#execute(Context)}. To get the result, you need to use {@link AbstractExtractor#getResult()}.
 * However, you should not reuse an instance twice.
 *
 * @author ckeiner
 */
public abstract class AbstractExtractor implements Serializable
{

    /**
     * The expression to use for the extraction
     */
    protected String extractionExpression;

    /**
     * The result of the extraction
     */
    private final List<String> result;

    /**
     * Sets {@link #extractionExpression} and creates an ArrayList for {@link #result}.
     *
     * @param extractionExpression
     *            The {@link #extractionExpression} to use for extracting
     */
    public AbstractExtractor(final String extractionExpression)
    {
        this.extractionExpression = extractionExpression;
        result = new ArrayList<>(1);
    }

    /**
     * Executes the extractor. Looks into the {@link WebResponse} located in {@link Context}. Then sets the extracted
     * expression via {@link #addResult(String)}.
     *
     * @param context
     *            The {@link Context} to use
     */
    public abstract void execute(Context<?> context);

    /**
     * Adds a string to the result list.
     *
     * @param result
     *            The string to be added to the result.
     */
    public void addResult(final String result)
    {
        getResult().add(result);
    }

    /**
     * Adds all strings in the list to the result list.
     *
     * @param resultList
     *            The list of strings to be added to the result.
     */
    public void addResult(final List<String> resultList)
    {
        getResult().addAll(resultList);
    }

    /**
     * Resolves {@link #extractionExpression}.
     *
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}.
     */
    protected void resolveValues(final Context<?> context)
    {
        extractionExpression = context.resolveString(extractionExpression);
    }

    public List<String> getResult()
    {
        return result;
    }

    public String getExtractionExpression()
    {
        return extractionExpression;
    }

    public void setExtractionExpression(final String extractionExpression)
    {
        this.extractionExpression = extractionExpression;
    }

}
