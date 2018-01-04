package com.xceptance.xlt.nocoding.parser.csvParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.StaticItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.XhrItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

public class CsvParser extends Parser
{

    public CsvParser(final String pathToFile)
    {
        super(pathToFile);
    }

    @Override
    public List<ScriptItem> parse() throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        final Reader in = new FileReader(getFile());
        final CSVFormat CSV_FORMAT = CSVFormat.RFC4180.withIgnoreEmptyLines(true)
                                                      .withCommentMarker('#')
                                                      .withHeader()
                                                      .withIgnoreSurroundingSpaces(true);
        final Iterable<CSVRecord> records = CSV_FORMAT.withFirstRecordAsHeader().parse(in);

        Action lastAction = null;
        StaticSubrequest lastStatic = null;

        for (final CSVRecord record : records)
        {
            if (record.isConsistent())
            {
                String type = null;
                if (record.isMapped(CsvConstants.TYPE))
                {
                    type = record.get(CsvConstants.TYPE);
                }
                else
                {
                    type = CsvConstants.TYPE_DEFAULT;
                }

                switch (type)
                {
                    case CsvConstants.TYPE_ACTION:
                        lastStatic = null;
                        lastAction = new ActionItemParser().parse(record);
                        scriptItems.add(lastAction);
                        break;

                    case CsvConstants.TYPE_STATIC:
                        if (lastAction == null)
                        {
                            throw new IllegalArgumentException("Static Type must be defined after an Action Type");
                        }
                        if (lastStatic == null)
                        {
                            lastStatic = new StaticItemParser().parse(record);
                            lastAction.getActionItems().add(lastStatic);
                        }
                        else
                        {
                            lastStatic.getUrls().addAll(new StaticItemParser().parse(record).getUrls());
                        }
                        break;

                    case CsvConstants.TYPE_XHR_ACTION:
                        if (lastAction == null)
                        {
                            throw new IllegalArgumentException("Xhr Action Type must be defined after an Action Type");
                        }
                        lastAction.getActionItems().add(new XhrItemParser().parse(record));
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown type: " + type);
                }
            }
        }
        return scriptItems;

    }

}
