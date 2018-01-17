package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;

/**
 * The class for getting the accepted extensions and the associated {@link Parser} for the extension
 * 
 * @author ckeiner
 */
public class ParserFactory
{
    private static ParserFactory factoryInstance;

    private final Map<String, Parser> extensionsMap;

    private ParserFactory()
    {
        extensionsMap = new HashMap<>();
        final CsvParser csvParser = new CsvParser();
        final YamlParser yamlParser = new YamlParser();
        for (final String extension : csvParser.getExtensions())
        {
            extensionsMap.put(extension, csvParser);
        }
        for (final String extension : yamlParser.getExtensions())
        {
            extensionsMap.put(extension, yamlParser);
        }
    }

    public static synchronized ParserFactory getInstance()
    {
        if (factoryInstance == null)
        {
            factoryInstance = new ParserFactory();
        }
        return factoryInstance;
    }

    /**
     * Returns list of extensions
     * 
     * @return
     */
    public List<String> getExtensions()
    {
        return new ArrayList<String>(extensionsMap.keySet());
    }

    /**
     * Checks the parser for its file extensions and then creates the parser is the extension fits
     * 
     * @param file
     * @return The Parser associated with the extension of <code>file</code>
     */
    public Parser getParser(final String file)
    {
        // Extract the extension of the file, that is the last position of the . +1, so we cut out the dot
        final String extension = file.substring(file.lastIndexOf('.') + 1);
        // Get the Parser instance from the extensionsMap
        final Parser parser = extensionsMap.get(extension);
        // If the parser is null, the extensions could not be found
        if (parser == null)
        {
            throw new IllegalArgumentException("Could not find appropriate parser for the extension " + extension + " with the path "
                                               + file);
        }
        XltLogger.runTimeLogger.debug("Using " + parser.getClass().getSimpleName() + " for " + file);
        return parser;
    }

}
