package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.actionItems;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVRecord;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;

/**
 * Extracts the information of a {@link Request} from a {@link CSVRecord}.
 * 
 * @author ckeiner
 */
public class RequestParser implements AbstractActionItemParser
{
    /**
     * Extracts the information needed for a {@link Request} from the {@link CSVRecord}
     * 
     * @param record
     *            The {@link CSVRecord} with the action item
     * @return The request defined by the CSVRecord
     */
    public Request parse(final CSVRecord record)
    {
        // Initialize variables
        String url = null;
        String method = null;
        List<NameValuePair> parameters = null;
        String encoded = null;

        // Get url
        if (record.isMapped(CsvConstants.URL))
        {
            url = record.get(CsvConstants.URL);
            // Remove quotation marks at the beginning and end of the url
            if (url.startsWith("\"") && url.endsWith("\""))
            {
                url = url.substring(1, url.length() - 1);
            }
        }
        // Get the method
        if (record.isMapped(CsvConstants.METHOD))
        {
            method = record.get(CsvConstants.METHOD);
        }
        // Get the parameters
        if (record.isMapped(CsvConstants.PARAMETERS))
        {
            parameters = readParameters(record.get(CsvConstants.PARAMETERS));
        }
        // Get encoded
        if (record.isMapped(CsvConstants.ENCODED))
        {
            encoded = record.get(CsvConstants.ENCODED);
        }

        // Build the request
        final Request request = new Request(url);
        request.setHttpMethod(method);
        request.setParameters(parameters);
        request.setEncodeBody(encoded);
        request.setEncodeParameters(encoded);

        // Return the request
        return request;
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

}
