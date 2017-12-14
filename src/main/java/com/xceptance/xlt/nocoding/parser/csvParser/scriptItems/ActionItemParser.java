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

public class ActionItemParser extends AbstractScriptItemParser
{

    @Override
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
                    case CsvConstants.URL:
                        url = ParserUtils.readValue(node, fieldName);
                    case CsvConstants.METHOD:
                        method = ParserUtils.readValue(node, fieldName);
                    case CsvConstants.PARAMETERS:
                        parameters = readParameters(node);
                    case CsvConstants.RESPONSECODE:
                        responsecode = ParserUtils.readValue(node, fieldName);
                    case CsvConstants.XPATH:
                        extractor = readExtractor(node);
                    case CsvConstants.REGEXP:
                        extractor = readExtractor(node);
                    case CsvConstants.TEXT:
                        textValidator = readValidationMethod(node);
                    case CsvConstants.ENCODED:
                        encoded = ParserUtils.readValue(node, fieldName);

                    default:
                        break;
                }
            }
        }

        final Request request = new Request();
        request.setUrl(url);
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
