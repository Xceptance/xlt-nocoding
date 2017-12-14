package com.xceptance.xlt.nocoding.parser.csvParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

public class CsvParser_Apache extends Parser
{

    public CsvParser_Apache(final String pathToFile)
    {
        super(pathToFile);
    }

    @Override
    public List<ScriptItem> parse() throws Exception
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();

        final BufferedReader br = createBufferedReaderFromFile(getFile());
        final CSVParser parser = createCSVParser(br, CsvConstants.CSV_FORMAT);
        final Map<String, Integer> headerMap = parser.getHeaderMap();
        for (final String headerField : headerMap.keySet())
        {
            if (!CsvConstants.isPermittedHeaderField(headerField))
            {
                throw new IllegalArgumentException(MessageFormat.format("Unsupported or misspelled header field: {0}", headerField));
            }
        }
        final List<CSVRecord> csvRecords = new ArrayList<CSVRecord>(parser.getRecords());

        // Iterate over each record, one record is a scriptItem
        for (final CSVRecord csvRecord : csvRecords)
        {
            // Set current to either CsvConstants.TYPE or CsvConstants.TYPE_DEFAULT
            if (csvRecord.isConsistent())
            {
                final String current = csvRecord.isMapped(CsvConstants.TYPE) ? csvRecord.get(CsvConstants.TYPE) : CsvConstants.TYPE_DEFAULT;
                switch (current)
                {
                    case CsvConstants.TYPE_ACTION:

                        break;

                    case CsvConstants.TYPE_XHR_ACTION:

                        break;

                    case CsvConstants.TYPE_STATIC:

                        break;

                    default:
                        break;
                }
            }
            else
            {
                // throw new CsvMappingException(parser, "Not consistent", schema)
            }
        }

        return scriptItems;

    }

    private BufferedReader createBufferedReaderFromFile(final File file)
    {
        try
        {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        }
        catch (UnsupportedEncodingException | FileNotFoundException e)
        {
            throw new IllegalArgumentException("Failed to create a BufferedReader, because:" + e.getMessage());
        }
    }

    private CSVParser createCSVParser(final BufferedReader br, final CSVFormat csvFormat)
    {

        CSVParser parser = null;
        try
        {
            parser = new CSVParser(br, csvFormat);
        }
        catch (final IOException e)
        {
            throw new IllegalArgumentException("Failed to create a CSVParser, because:" + e.getMessage());
        }
        return parser;
    }

}
