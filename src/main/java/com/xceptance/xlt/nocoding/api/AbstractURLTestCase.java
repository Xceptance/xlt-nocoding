package com.xceptance.xlt.nocoding.api;

import java.io.File;
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
import com.xceptance.xlt.nocoding.parser.csvParser.CsvParser_Jackson;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;

/**
 * Executes a xlt-nocoding test case by parsing the file specified in the properties and executing the parsed commands
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
     * The list of script items, created by using the parser.parse()-Method
     */
    private List<ScriptItem> itemList;

    /**
     * The Context of all ScriptItems. Handles Properties, Storage, etc.
     */
    private Context context;

    /**
     * Prepares the test case by: Instantiating the Context, loading the default configuration, and parsing the definition
     * file
     */
    @Before
    public void initialize() throws Exception
    {
        // Instantiate the PropertyManager with a new DataStorage
        context = new Context(XltProperties.getInstance());
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
     * Gets the path to the file as string
     * 
     * @return The path to the file
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
     * Creates the correct parser depending on the file extension, i.e. creates the YamlParser for yml/yaml files.
     * 
     * @param pathToFile
     *            The path to the file with or without the extension.
     * @return The parser that should be used for the specified file.
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
                parser = new CsvParser_Jackson(pathToFile);
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
     * On the first execution, it gets the {@link List}<{@link ScriptItem}> from the parser and saves it in
     * {@link #DATA_CACHE}. Then, it gets the {@link List}<{@link ScriptItem}> out of the {@link #DATA_CACHE}
     * 
     * @return {@link List}<{@link ScriptItem}> defined in the file located at {@link #getFilePath()}
     * @throws Exception
     */
    public List<ScriptItem> getOrParse() throws Exception
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
