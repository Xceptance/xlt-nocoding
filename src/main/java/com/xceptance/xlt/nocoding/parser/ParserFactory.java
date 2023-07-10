/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.csv.CsvParser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * The class for getting the accepted extensions and the associated {@link Parser} for the extension
 *
 * @author ckeiner
 */
public class ParserFactory
{
    /**
     * The singleton instance of <code>ParserFactory</code>
     */
    private static ParserFactory factoryInstance;

    /**
     * The map, that maps the extension to a concrete parser
     */
    private final Map<String, Parser> extensionsMap;

    /**
     * Creates a new instance of <code>ParserFactory</code> and fills {@link #extensionsMap} with the known Parsers and
     * its extensions
     */
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

    /**
     * Either creates an instance of <code>ParserFactory</code> if {@link #factoryInstance} is <code>null</code> or
     * returns <code>factoryInstance</code>.
     *
     * @return the singleton instance of <code>ParserFactory</code>
     */
    public static synchronized ParserFactory getInstance()
    {
        if (factoryInstance == null)
        {
            factoryInstance = new ParserFactory();
        }
        return factoryInstance;
    }

    /**
     * Returns a list of all known file extensions for the parser, therefore it returns the {@link Map#keySet()} of
     * {@link #extensionsMap}.
     *
     * @return A list with the content of {@link Map#keySet()} of {@link #extensionsMap}.
     */
    public List<String> getExtensions()
    {
        return new ArrayList<>(extensionsMap.keySet());
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
