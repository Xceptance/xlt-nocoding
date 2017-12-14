package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
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
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
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
            if (!(node.get(fieldName) instanceof NullNode) && !node.get(fieldName).isNull())
            {
                switch (fieldName)
                {
                    case CsvConstants.TYPE:
                        // Do nothing
                        break;
                    case CsvConstants.NAME:
                        name = ParserUtils.readValue(node, fieldName);
                        break;
                    case CsvConstants.URL:
                        url = ParserUtils.readValue(node, fieldName);
                        url = url.trim();
                        final String quotationMark = "\"";
                        if (url.startsWith(quotationMark) && url.endsWith(quotationMark))
                        {
                            url = url.substring(1, url.length() - 1);
                        }
                        break;
                    case CsvConstants.METHOD:
                        method = ParserUtils.readValue(node, fieldName);
                        break;
                    case CsvConstants.PARAMETERS:
                        parameters = readParameters(node);
                        break;
                    case CsvConstants.RESPONSECODE:
                        responsecode = ParserUtils.readValue(node, fieldName);
                        break;
                    case CsvConstants.XPATH:
                        extractor = readExtractor(node);
                        break;
                    case CsvConstants.REGEXP:
                        extractor = readExtractor(node);
                        break;
                    case CsvConstants.TEXT:
                        textValidator = readValidationMethod(node);
                        break;
                    case CsvConstants.ENCODED:
                        encoded = ParserUtils.readValue(node, fieldName);
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

    private List<NameValuePair> readParameters(final JsonNode node)
    {

        return null;
    }

    private AbstractExtractor readExtractor(final JsonNode node)
    {

        return null;
    }

    private AbstractValidationMethod readValidationMethod(final JsonNode node)
    {

        return null;
    }

}
