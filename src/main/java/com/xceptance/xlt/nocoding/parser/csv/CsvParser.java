package com.xceptance.xlt.nocoding.parser.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVParser;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.csv.command.ActionParser;
import com.xceptance.xlt.nocoding.parser.csv.command.StaticSubrequestParser;
import com.xceptance.xlt.nocoding.parser.csv.command.XhrSubrequestParser;

/**
 * Reads a CSV file, and generates a list filled with {@link Command}s out of the CSV file.
 *
 * @author ckeiner
 */
public class CsvParser implements Parser
{

    /**
     * Parses the content of the Reader <code>reader</code> to a list of {@link Command}s
     *
     * @param reader
     *            The Reader that contains {@link Command}s in a parser dependent format
     * @return A list of {@link Command}s
     * @throws IOException
     *             if an I/O error occurs during creating the reader, parsing the file or closing the parser
     */
    @Override
    public List<Command> parse(final Reader reader) throws IOException
    {
        // Initialize variables
        final List<Command> scriptItems = new ArrayList<>();
        final ActionWrapper lastAction = new ActionWrapper();
        final StaticSubrequestWrapper lastStatic = new StaticSubrequestWrapper();

        // Create a CSVParser based on the Reader
        final CSVParser parser = new CSVParser(reader, CsvConstants.CSV_FORMAT.withFirstRecordAsHeader());
        try
        {
            validateHeader(parser.getHeaderMap().keySet());

            parser.forEach(record -> {
                // If the record is consistent, which means that all headers have a value
                if (record.isConsistent())
                {
                    // Get the type of the record
                    String type = CsvConstants.TYPE_DEFAULT;
                    if (record.isMapped(CsvConstants.TYPE))
                    {
                        type = record.get(CsvConstants.TYPE);
                    }

                    // Differentiate between Action, Static and Xhr Requests
                    switch (type)
                    {
                        case CsvConstants.TYPE_ACTION:
                            // Reset the last static subrequest
                            lastStatic.setStaticSubrequest(null);
                            // Set the new action as last action
                            lastAction.setAction(new ActionParser().parse(record));
                            // And add it to the scriptItems
                            scriptItems.add(lastAction.getAction());
                            break;

                        case CsvConstants.TYPE_STATIC:
                            // If there was no lastAction, throw an exception
                            if (lastAction.getAction() == null)
                            {
                                throw new IllegalArgumentException("Static Type must be defined after an Action Type");
                            }
                            // If there wasn't a last static request
                            if (lastStatic.getStaticSubrequest() == null)
                            {
                                // Read the static request and assign it to lastStatic
                                lastStatic.setStaticSubrequest(new StaticSubrequestParser().parse(record));
                                // Assign last static as actionItem of the lastAction
                                lastAction.getAction().getActionItems().add(lastStatic.getStaticSubrequest());
                            }
                            // If there was a last static request
                            else
                            {
                                // Simply add the url of the current static request to the lastStatic request, so they
                                // can
                                // be executed parallel
                                lastStatic.getStaticSubrequest().getUrls().addAll(new StaticSubrequestParser().parse(record).getUrls());
                            }
                            break;

                        case CsvConstants.TYPE_XHR_ACTION:
                            // Reset the last static subrequest
                            lastStatic.setStaticSubrequest(null);
                            // If there was no lastAction, throw an exception
                            if (lastAction.getAction() == null)
                            {
                                throw new IllegalArgumentException("Xhr Action Type must be defined after an Action Type");
                            }
                            // Add the Xhr Subrequest to the action items of the lastAction
                            lastAction.getAction().getActionItems().add(new XhrSubrequestParser().parse(record));
                            break;

                        default:
                            // If the type is unknown, throw an error
                            throw new IllegalArgumentException("The record has an unknown type: " + type);
                    }
                }
                else
                {
                    throw new IllegalStateException("The record does not contain an entry for each header!");
                }
            });
        }
        catch (final Throwable e)
        {
            throw new IllegalStateException("CSV Parsing failed in line " + parser.getCurrentLineNumber() + ": " + e.getMessage(), e);
        }
        finally
        {
            parser.close();
        }
        // Return the scriptItems
        return scriptItems;
    }

    /**
     * Validates the headers are permitted
     *
     * @param headers
     */
    private void validateHeader(final Set<String> headers)
    {
        // for each header
        for (final String header : headers)
        {
            // If the header is not in the permitted list
            if (!CsvConstants.isPermittedHeaderField(header))
            {
                // Verify the header either starts with the Regexp or Xpath getter prefix
                if (!(header.startsWith(CsvConstants.REGEXP_GETTER_PREFIX) || header.startsWith(CsvConstants.XPATH_GETTER_PREFIX)))
                {
                    throw new IllegalArgumentException(header + "isn't an allowed header!");
                }
            }
        }
    }

    @Override
    public List<String> getExtensions()
    {
        return Arrays.asList("csv");
    }

    public class ActionWrapper
    {
        public Action getAction()
        {
            return action;
        }

        public void setAction(final Action action)
        {
            this.action = action;
        }

        Action action;
    }

    public class StaticSubrequestWrapper
    {
        public StaticSubrequest getStaticSubrequest()
        {
            return staticSubrequest;
        }

        public void setStaticSubrequest(final StaticSubrequest staticSubrequest)
        {
            this.staticSubrequest = staticSubrequest;
        }

        StaticSubrequest staticSubrequest;
    }

}
