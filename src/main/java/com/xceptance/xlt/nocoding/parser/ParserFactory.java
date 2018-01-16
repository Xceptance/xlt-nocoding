package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.xceptance.xlt.nocoding.parser.csvParser.CsvParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;

public class ParserFactory
{
    private static ParserFactory factoryInstance;

    public static ParserFactory getInstance()
    {
        synchronized (factoryInstance)
        {
            if (factoryInstance == null)
            {
                factoryInstance = new ParserFactory();
            }
        }
        return factoryInstance;
    }

    /**
     * Returns list of extensions without a dot
     * 
     * @return
     */
    public List<String> getExtensions()
    {
        final List<String> extensions = new ArrayList<String>();
        extensions.addAll(CsvParser.FILE_EXTENSION);
        extensions.addAll(YamlParser.FILE_EXTENSION);
        return extensions;
    }

    public Parser getParser(final String file)
    {
        Parser parser = null;
        final String extension = FilenameUtils.getExtension(file);
        if (CsvParser.FILE_EXTENSION.contains(extension))
        {
            parser = new CsvParser(file);
        }
        else if (YamlParser.FILE_EXTENSION.contains(extension))
        {
            parser = new YamlParser(file);
        }

        if (parser == null)
        {
            throw new IllegalArgumentException("Could not find appropriate parser for " + file);
        }
        return parser;
    }

}
