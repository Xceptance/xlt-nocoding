package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeightAction;
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
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Parses an action item to {@link Action}.
 * 
 * @author ckeiner
 */
public class ActionItemParser
{

    /**
     * Parses the action content in a node to an {@link Action}
     * 
     * @param node
     *            The node the actionItem is at
     * @return
     */
    public Action parse(final JsonNode node)
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

        // Get an iterator over the fieldNames
        final Iterator<String> fieldNames = node.fieldNames();
        // While there are still fieldNames available
        while (fieldNames.hasNext())
        {
            // Get the next fieldName
            final String fieldName = fieldNames.next();
            // Get the value without whitespaces in the beginning and end
            final String value = ParserUtils.readValue(node, fieldName).trim();
            // If there really is a value and it is not empty
            if (value != null && !value.isEmpty())
            {
                switch (fieldName)
                {
                    case CsvConstants.TYPE:
                        // Do nothing
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
                        extractor = new XpathExtractor(value);
                        break;
                    case CsvConstants.REGEXP:
                        extractor = new RegexpExtractor(value);
                        break;
                    case CsvConstants.TEXT:
                        textValidator = new TextValidator(value);
                        break;
                    case CsvConstants.ENCODED:
                        encoded = value;
                        break;

                    default:
                        // If the fieldName contains either REGEXP_GETTER_PREFIX or XPATH_GETTER_PREFIX
                        if (fieldName.contains(CsvConstants.REGEXP_GETTER_PREFIX) || fieldName.contains(CsvConstants.XPATH_GETTER_PREFIX))
                        {
                            // Create a response store
                            responseStores.add(handleStore(fieldName, value));
                        }
                        // In every other case, throw an error
                        else
                        {
                            // Throw an exception
                            throw new IllegalArgumentException("Unknown header: " + fieldName);
                        }
                }
            }
        }

        final Request request = new Request(url);
        request.setHttpMethod(method);
        request.setParameters(parameters);
        request.setEncodeBody(encoded);
        request.setEncodeParameters(encoded);

        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();

        if (responsecode != null)
        {
            responseItems.add(new HttpcodeValidator(responsecode));
        }
        if (extractor != null)
        {
            final Validator validator = new Validator(null, extractor, textValidator);
            responseItems.add(validator);
        }
        final Response response = new Response(responseItems);

        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        actionItems.add(request);
        actionItems.add(response);
        final LightWeightAction action = new LightWeightAction(name, actionItems);
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
