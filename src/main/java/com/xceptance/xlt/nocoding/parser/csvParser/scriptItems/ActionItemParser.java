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
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.XpathExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ActionItemParser
{

    public Action parse(final JsonNode node)
    {
        final Iterator<String> fieldNames = node.fieldNames();
        String name = null;
        String url = null;
        String method = null;
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        String responsecode = null;
        AbstractExtractor extractor = null;
        AbstractValidationMethod textValidator = null;
        String encoded = null;
        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();
            final String value = ParserUtils.readValue(node, fieldName);
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
                        url = url.trim();
                        final String quotationMark = "\"";
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
                        break;
                }
            }
        }

        final Request request = new Request(url);
        request.setHttpMethod(method);
        request.setParameters(parameters);
        request.setEncodeBody(encoded);
        request.setEncodeParameters(encoded);
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        responseItems.add(new HttpcodeValidator(responsecode));
        final Validator validator = new Validator(null, extractor, textValidator);
        responseItems.add(validator);
        final Response response = new Response();

        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        actionItems.add(request);
        actionItems.add(response);
        final LightWeightAction action = new LightWeightAction(name, actionItems);
        return action;
    }

    private List<NameValuePair> readParameters(final String parameterString)
    {
        final List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        final StringTokenizer tokenizer = new StringTokenizer(parameterString, "&");
        while (tokenizer.hasMoreTokens())
        {
            final String parameter = tokenizer.nextToken();

            // the future pair
            String name = null;
            String value = null;

            // Get index of =
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
            else
            {
                name = parameter;
            }
            if (name != null)
            {
                parameterList.add(new NameValuePair(name, value));
            }
        }
        return parameterList;
    }

}
