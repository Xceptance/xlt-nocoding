package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * Executes a xlt-nocoding test case by parsing the file specified in the properties and executing the parsed commands
 * 
 * @author ckeiner
 */
public abstract class AbstractURLTestCase extends AbstractTestCase
{

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
        context = new Context(XltProperties.getInstance(), new DataStorage());
        final String pathToFile = getFilePath();
        this.parser = decideParser(pathToFile);
        // Parse the file
        itemList = parser.parse();
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
        // for each script item, execute it
        if (getItemList() != null && !getItemList().isEmpty())
        {
            for (final ScriptItem item : itemList)
            {
                XltLogger.runTimeLogger.info("Starting ScriptItem : " + item.toString());
                item.execute(context);
            }
        }
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
        final String dataDirectory = context.getPropertyByKey(NoCodingPropertyAdmin.DIRECTORY);
        String fileName = context.getPropertyByKey(NoCodingPropertyAdmin.FILENAME);
        if (StringUtils.isBlank(fileName))
        {
            fileName = getClass().getSimpleName();
        }

        return dataDirectory + File.separatorChar + fileName;
    }

    protected Parser decideParser(String pathToFile)
    {
        Parser parser = null;
        final String fileExtension = FilenameUtils.getExtension(pathToFile);
        if (fileExtension.equalsIgnoreCase("yml") || fileExtension.equalsIgnoreCase("yaml"))
        {
            this.parser = new com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser(pathToFile + fileExtension);
        }
        else if (fileExtension.isEmpty())
        {
            // No Extension found, try yml and csv

            // check for YAML files first
            final String[] yamlPaths =
                {
                  pathToFile + ".yml", pathToFile + ".yaml"
                };

            for (final String yamlPath : yamlPaths)
            {
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
                throw new NotImplementedException("Csv not yet implemented.");
            }
        }
        else
        {
            throw new IllegalArgumentException("Illegal file type: " + "\"" + fileExtension + "\"" + "\n"
                                               + "Supported types: '.yaml' | '.yml'\n");// or '.csv'" + "\n");
        }
        if (parser == null)
        {
            // no file with a supported extension found
            throw new IllegalArgumentException("Failed to find a script file for file path: " + pathToFile);
        }
        return parser;
    }

}
