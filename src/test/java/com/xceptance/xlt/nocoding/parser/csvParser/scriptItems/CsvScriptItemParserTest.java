package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVRecord;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;

public abstract class CsvScriptItemParserTest
{
    protected Iterable<CSVRecord> buildRecord(final String input) throws IOException
    {
        final StringReader in = new StringReader(input);
        return CsvConstants.CSV_FORMAT.withFirstRecordAsHeader().parse(in);
    }
}
