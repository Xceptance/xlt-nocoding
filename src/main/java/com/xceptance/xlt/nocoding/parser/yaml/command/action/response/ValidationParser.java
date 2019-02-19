package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor.ExtractorParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.validator.ValidatorParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing validations.
 *
 * @author ckeiner
 */
public class ValidationParser
{

    /**
     * Parses the validation items in the response block to a list of {@link Validator}.
     *
     * @param validationsNode
     *            The {@link Node} with the validation item
     * @return A list of <code>Validator</code>
     */
    public List<Validator> parse(final Node validationsNode)
    {

        // Verify that an array was used and not an object
        if (!(validationsNode instanceof SequenceNode))
        {
            throw new ParserException("Node at", validationsNode.getStartMark(),
                                      " is " + validationsNode.getNodeId().toString() + " but needs to be an array", null);
        }

        // Initialize variables
        final List<Validator> validatorList = new ArrayList<>();

        final List<Node> validators = ((SequenceNode) validationsNode).getValue();
        validators.forEach(validatorItem -> {
            final Validator validator = parseSingleValidator(validatorItem);
            validatorList.add(validator);
            // Print a debug statement
            XltLogger.runTimeLogger.debug("Added " + validator.getValidationName() + " to Validations.");
        });
        // Return all validations
        return validatorList;
    }

    protected Validator parseSingleValidator(final Node validationNodeWrapper)
    {
        // Verify that an object was used
        if (!(validationNodeWrapper instanceof MappingNode))
        {
            throw new ParserException("Node at", validationNodeWrapper.getStartMark(),
                                      " is " + validationNodeWrapper.getNodeId().toString() + " but needs to be an object", null);
        }
        String validationName = "";
        AbstractExtractor extractor = null;
        AbstractValidator validationMethod = null;

        final List<NodeTuple> validationNode = ((MappingNode) validationNodeWrapper).getValue();
        for (final NodeTuple validation : validationNode)
        {
            // Get the name of the validation
            validationName = YamlParserUtils.transformScalarNodeToString(validation.getKeyNode());
            final Node validationContent = validation.getValueNode();
            // Verify the validation is an object
            if (!(validationContent instanceof MappingNode))
            {
                throw new ParserException("Node at", validationContent.getStartMark(),
                                          " is " + validationContent.getNodeId().toString() + " but needs to be an object", null);
            }
            final List<NodeTuple> singleValidationContentItems = ((MappingNode) validationContent).getValue();
            for (final NodeTuple contentItem : singleValidationContentItems)
            {
                final String contentKey = YamlParserUtils.transformScalarNodeToString(contentItem.getKeyNode());
                // If it is an extraction, parse the extraction
                if (Constants.isPermittedExtraction(contentKey))
                {
                    // Verify, that no extractor was parsed already
                    if (extractor != null)
                    {
                        throw new ParserException("Node at", contentItem.getKeyNode().getStartMark(),
                                                  " defines a second extractor but only one definition is allowed.", null);
                    }
                    // Parse the extractor
                    extractor = new ExtractorParser(contentKey).parse(singleValidationContentItems);
                    XltLogger.runTimeLogger.debug("Extraction Mode is " + extractor);
                }
                // If it is a validation method, parse the validation method
                else if (Constants.isPermittedValidationMethod(contentKey))
                {
                    // Verify, that an extractor was parsed already
                    if (extractor == null)
                    {
                        throw new ParserException("Node at", contentItem.getKeyNode().getStartMark(),
                                                  " defines a validation method but cannot be parsed before an extractor is defined.",
                                                  null);
                    }
                    // Verify, that no validation was parsed already
                    if (validationMethod != null)
                    {
                        throw new ParserException("Node at", contentItem.getKeyNode().getStartMark(),
                                                  " defines a second validation method but only one can be defined.", null);
                    }
                    // Parse the validation method
                    validationMethod = new ValidatorParser(contentKey).parse(contentItem.getValueNode());
                    XltLogger.runTimeLogger.debug("Validation Method is " + validationMethod);
                }
                // If it is not Group OR Group and extractor is null, throw an error
                else if (!Constants.GROUP.equals(contentKey) || extractor == null)
                {
                    throw new ParserException("Node at", validationContent.getStartMark(), " defines an unknown item.", null);
                }
            }
        }

        return new Validator(validationName, extractor, validationMethod);
    }

}
