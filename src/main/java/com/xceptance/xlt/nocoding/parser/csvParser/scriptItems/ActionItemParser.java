package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVRecord;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;

/**
 * The class for parsing an action item.
 * 
 * @author ckeiner
 */
public class ActionItemParser
{

    /**
     * Parses the action item to a list of {@link ScriptItem}s.
     * 
     * @param record
     *            The {@link CSVRecord} with the the action item
     * @return The Action defined by the CSVRecord
     */
    public Action parse(final CSVRecord record)
    {
        // Initialize all needed variables
        String name = null;
        String url = null;
        String method = null;
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        String responsecode = null;
        AbstractExtractor extractor = null;
        AbstractValidationMethod textValidator = null;
        String encoded = null;
        final List<AbstractResponseStore> responseStores = new ArrayList<AbstractResponseStore>();
        boolean hasXpath = false;
        boolean hasRegexp = false;

        // Build an iterator over the headers
        final Iterator<String> headerIterator = record.toMap().keySet().iterator();
        // While there are headers
        while (headerIterator.hasNext())
        {
            // Get the next header
            final String header = headerIterator.next();
            // Verify that the header is permitted or one of the regexp/xpath getters
            if (!CsvConstants.isPermittedHeaderField(header))
            {
                if (!(header.contains(CsvConstants.REGEXP_GETTER_PREFIX) || header.contains(CsvConstants.XPATH_GETTER_PREFIX)))
                    throw new IllegalArgumentException(header + "isn't an allowed header!");
            }
            // Get the value
            final String value = record.get(header);
            // If the value is null or empty, discard it
            if (value == null || value.isEmpty())
            {
                continue;
            }

            // Differentiate between the headers
            switch (header)
            {
                case CsvConstants.TYPE:
                    break;
                case CsvConstants.NAME:
                    name = value;
                    break;
                case CsvConstants.URL:
                    url = value;
                    final String quotationMark = "\"";
                    // Remove quotation marks at the beginning and end of the url
                    if (url.startsWith(quotationMark) && url.endsWith(quotationMark))
                    {
                        url = url.substring(1, url.length() - 1);
                    }
                    break;
                case CsvConstants.METHOD:
                    method = value;
                    break;
                case CsvConstants.PARAMETERS:
                    parameters = readParameters(value);
                    break;
                case CsvConstants.RESPONSECODE:
                    responsecode = value;
                    break;
                case CsvConstants.XPATH:
                    if (!hasRegexp)
                    {
                        extractor = new XpathExtractor(value);
                        hasXpath = true;
                        break;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Cannot use Xpath and Regexp together!");
                    }
                case CsvConstants.REGEXP:
                    if (!hasXpath)
                    {
                        extractor = new RegexpExtractor(value);
                        hasRegexp = true;
                        break;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Cannot use Xpath and Regexp together!");
                    }
                case CsvConstants.TEXT:
                    textValidator = new MatchesValidator(value);
                    break;
                case CsvConstants.ENCODED:
                    encoded = value;
                    break;

                default:
                    // If the fieldName contains either REGEXP_GETTER_PREFIX and XPath isn't used
                    if (header.contains(CsvConstants.REGEXP_GETTER_PREFIX) && !hasXpath)
                    {
                        if (!hasXpath)
                        {
                            responseStores.add(handleStore(header, value));
                            hasRegexp = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Cannot use Xpath and Regexp together!");
                        }
                    }
                    // If the fieldName contains either XPATH_GETTER_PREFIX and Regexp isn't used
                    else if (header.contains(CsvConstants.XPATH_GETTER_PREFIX) && !hasRegexp)
                    {
                        // Create a response store
                        if (!hasRegexp)
                        {
                            responseStores.add(handleStore(header, value));
                            hasXpath = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Cannot use Xpath and Regexp together!");
                        }
                    }
                    // In every other case, throw an error
                    else
                    {
                        // Throw an exception
                        throw new IllegalArgumentException("Allowed header but not implemented: " + header);
                    }
            }
        }

        // Build an action with the data
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        // Build the request
        final Request request = new Request(url);
        request.setHttpMethod(method);
        request.setParameters(parameters);
        request.setEncodeBody(encoded);
        request.setEncodeParameters(encoded);
        // Add the request to the actionItems
        actionItems.add(request);

        // Build the response
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();

        // HttpcodeValidator
        if (responsecode != null)
        {
            responseItems.add(new HttpcodeValidator(responsecode));
        }
        // Validator
        if (extractor != null)
        {
            final String validationName = "Validate " + name;
            final Validator validator = new Validator(validationName, extractor, textValidator);
            responseItems.add(validator);
        }
        // ResponseStore
        if (responseStores != null && !responseStores.isEmpty())
        {
            responseItems.addAll(responseStores);
        }
        // Create response if there are responseItems
        if (responseItems != null && !responseItems.isEmpty())
        {
            actionItems.add(new Response(responseItems));
        }

        // Build the action
        final Action action = new Action(name, actionItems);
        // Return the action
        return action;
    }

    /**
     * Converts the given string to a {@link List}<{@link NameValuePair}> according to http parameters
     * 
     * @param parameterString
     *            The string to turn into {@link List}<{@link NameValuePair}>
     * @return
     */
    private List<NameValuePair> readParameters(final String parameterString)
    {
        // Create an empty parameter list
        final List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        // Split the String at '&' and save each in tokens
        final StringTokenizer tokenizer = new StringTokenizer(parameterString, "&");
        // While we have tokens
        while (tokenizer.hasMoreTokens())
        {
            // Get the next token
            final String parameter = tokenizer.nextToken();

            // Instantiate name and value
            String name = null;
            String value = null;

            // Get index of '='
            final int pos = parameter.indexOf("=");
            // If there is an = sign
            if (pos >= 0)
            {
                // Get the name
                name = parameter.substring(0, pos);
                // If there is still something left in the parameter
                if (pos < parameter.length() - 1)
                {
                    // Save the rest to value
                    value = parameter.substring(pos + 1);
                }
            }
            // If there is no '=', save the whole parameter as name
            else
            {
                name = parameter;
            }
            // If we have a name
            if (name != null)
            {
                // Save it in the list
                parameterList.add(new NameValuePair(name, value));
            }
        }
        return parameterList;
    }

    /**
     * Creates an {@link AbstractResponseStore} depending on the fieldName
     * 
     * @param fieldName
     *            The name of the field
     * @param value
     *            The value of the field
     * @return An {@link AbstractResponseStore} with the variableName of fieldName and the proper {@link AbstractExtractor}
     *         with the extractionExpression of value
     */
    private AbstractResponseStore handleStore(final String fieldName, final String value)
    {
        // Create an empty AbstractExtractor
        AbstractExtractor storeExtractor = null;
        // If the fieldName contains CsvConstants.REGEXP_GETTER_PREFIX
        if (fieldName.contains(CsvConstants.REGEXP_GETTER_PREFIX))
        {
            // And the rest of the string is numbers
            if (fieldName.substring(CsvConstants.REGEXP_GETTER_PREFIX.length()).matches("[0-9]+"))
            {
                // Create an AbstractExtractor
                storeExtractor = new RegexpExtractor(value);
            }
            else
            {
                // Throw an error
                throw new IllegalArgumentException(fieldName + " must be " + CsvConstants.REGEXP_GETTER_PREFIX + " a number");
            }
        }
        // If the fieldName contains CsvConstants.XPATH_GETTER_PREFIX
        else if (fieldName.contains(CsvConstants.XPATH_GETTER_PREFIX))
        {
            // And the rest of the string is numbers
            if (fieldName.substring(CsvConstants.XPATH_GETTER_PREFIX.length()).matches("[0-9]+"))
            {
                // Create an AbstractExtractor
                storeExtractor = new XpathExtractor(value);
            }
            else
            {
                // Throw an error
                throw new IllegalArgumentException(fieldName + " must be " + CsvConstants.XPATH_GETTER_PREFIX + " a number");
            }
        }

        // Return the new ResponseStore
        return new ResponseStore(fieldName, storeExtractor);
    }

}
