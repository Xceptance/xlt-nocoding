package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.csvParser.JacksonCsvParser;
import com.xceptance.xlt.nocoding.parser.csvParser.apache.ApacheCsvParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Executes a xlt-nocoding test case by parsing the file specified in the properties and executing the parsed commands.
 * 
 * @author ckeiner
 */
public abstract class AbstractURLTestCase extends AbstractTestCase
{
    /**
     * Cache of all parsed data.
     */
    private static final Map<String, List<ScriptItem>> DATA_CACHE = new HashMap<>();

    /**
     * The parser to use for parsing
     */
    private Parser parser;

    /**
     * The list of {@link ScriptItem}s
     */
    private List<ScriptItem> itemList;

    /**
     * The {@link Context} of all <code>ScriptItems</code>
     */
    private Context context;

    /**
     * Prepares a test case by parsing the file
     * 
     * @throws IOException
     *             is thrown, for example, when the file is not found, or the parser encounters an error
     */
    @Before
    public void initialize() throws IOException
    {
        // Instantiate the PropertyManager with a new DataStorage
        final XltProperties properties = XltProperties.getInstance();
        // Get the mode and create the corresponding Context
        final String mode = properties.getProperty(NoCodingPropertyAdmin.MODE);
        switch (mode)
        {
            case NoCodingPropertyAdmin.LIGHTWEIGHT:
                context = new LightWeightContext(properties);
                break;

            case NoCodingPropertyAdmin.DOM:
                context = new DomContext(properties);
                break;

            default:
                throw new IllegalStateException("Mode must be " + NoCodingPropertyAdmin.LIGHTWEIGHT + " or " + NoCodingPropertyAdmin.DOM
                                                + " but is " + mode);
        }
        // Resolve the filePath to use
        final String pathToFile = getFilePath();
        // Create the appropriate parser
        this.parser = decideParser(pathToFile);
        // Get or parse the file
        itemList = getOrParse();
    }

    /**
     * Executes the test case
     * 
     * @throws Throwable
     *             Most Throwable that happen during execution
     */
    @Test
    public void executeTest() throws Throwable
    {
        XltLogger.runTimeLogger.info("Starting Testcase : " + this.toString());
        // If there are ScriptItems in the itemList
        if (getItemList() != null && !getItemList().isEmpty())
        {
            // Execute every item
            for (final ScriptItem item : itemList)
            {
                XltLogger.runTimeLogger.info("Starting ScriptItem : " + item.toString());
                item.execute(context);
            }
        }
        // If there are no ScriptItems, throw an error
        else
        {
            XltLogger.runTimeLogger.error("No Script Items were found");
            throw new IllegalArgumentException("No Script Items were found");
        }

    }

    public Parser getParser()
    {
        return parser;
    }

    public List<ScriptItem> getItemList()
    {
        return itemList;
    }

    public Context getContext()
    {
        return context;
    }

    /**
     * Gets the file path, that is the path with the name of the file. <br>
     * Uses {@value File#separatorChar} between the directory and the filename.
     * 
     * @return String that describes the path to the file
     */
    protected String getFilePath()
    {
        // Find the data directory in the property files
        final String dataDirectory = context.getPropertyByKey(NoCodingPropertyAdmin.DIRECTORY);
        // Get the fileName from the property files
        String fileName = context.getPropertyByKey(NoCodingPropertyAdmin.FILENAME);
        // If the fileName is empty
        if (StringUtils.isBlank(fileName))
        {
            // Get the class and convert the class to a simple name
            fileName = getClass().getSimpleName();
        }
        // Finally, return the path to the file
        return dataDirectory + File.separatorChar + fileName;
    }

    /**
     * Creates the correct parser depending on the file extension. For example creates a {@link YamlParser} for yml/yaml
     * files and a {@link JacksonCsvParser} for csv files.
     * 
     * @param pathToFile
     *            The path to the file with or without the extension.
     * @return The {@link Parser} that should be used for the specified file.
     */
    protected Parser decideParser(String pathToFile)
    {
        Parser parser = null;
        // Get the extension
        final String fileExtension = FilenameUtils.getExtension(pathToFile);
        // If it is yml or yaml
        if (fileExtension.equalsIgnoreCase("yml") || fileExtension.equalsIgnoreCase("yaml"))
        {
            String parserInput = pathToFile;
            // if the pathToFile does not contain the fileExtension, add it
            if (!pathToFile.contains(fileExtension))
            {
                parserInput += fileExtension;
            }
            // Create the YamlParser with the correct input
            parser = new YamlParser(parserInput);
        }
        // If the file extension is empty, try to add yml, yaml and csv
        else if (fileExtension.isEmpty())
        {
            // check for YAML files first
            final String[] yamlPaths =
                {
                  pathToFile + ".yml", pathToFile + ".yaml"
                };

            for (final String yamlPath : yamlPaths)
            {
                // If the new path is a file, create the parser and quit the loop
                if (new File(yamlPath).isFile())
                {
                    pathToFile = yamlPath;
                    parser = new YamlParser(pathToFile);
                    break;
                }
            }

            // check for CSV file second
            final String csvPath = pathToFile + ".csv";
            if (new File(csvPath).isFile())
            {
                pathToFile = csvPath;
                parser = new ApacheCsvParser(pathToFile);
                // parser = new CsvParserNocoding(pathToFile);
                // throw new NotImplementedException("Csv not yet implemented.");
            }
        }
        else
        {
            throw new IllegalArgumentException("Illegal file type: " + "\"" + fileExtension + "\"" + "\n"
                                               + "Supported types: '.yaml' | '.yml'\n");// or '.csv'" + "\n");
        }
        if (parser == null)
        {
            // No file with a supported extension was found
            throw new IllegalArgumentException("Failed to find a script file for file path: " + pathToFile);
        }
        return parser;
    }

    /**
     * Gets the list of <code>ScriptItem</code>s either by parsing them or from the cache. <br>
     * On the first execution, it gets the list from the parser and saves it in {@link #DATA_CACHE}. Then, it gets the list
     * out of the {@link #DATA_CACHE}.
     * 
     * @return A list of <code>ScriptItem</code>s generated from the file located at {@link #getFilePath()}
     * @throws IOException
     *             is thrown, for example, when the file is not found, or the parser encounters an error
     */
    public List<ScriptItem> getOrParse() throws IOException
    {
        synchronized (DATA_CACHE)
        {
            final String filePath = parser.getFile().getAbsolutePath();
            List<ScriptItem> result = DATA_CACHE.get(filePath);
            if (result == null)
            {
                result = parser.parse();
                DATA_CACHE.put(filePath, result);
            }
            return result;
        }
    }

}
