package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.xceptance.xlt.nocoding.parser.csvParser.CsvParser;
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
    private Context<?> context;

    public Parser getParser()
    {
        return parser;
    }

    public List<ScriptItem> getItemList()
    {
        return itemList;
    }

    public Context<?> getContext()
    {
        return context;
    }

    /**
     * Prepares a test case by parsing the contents of the file to {@link #itemList}
     * 
     * @throws IOException
     *             is thrown when the file is not found or the parser encounters an error
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
        // Get the possible filePaths
        final List<String> filepaths = getAllPossibleFilepaths();
        // Find the path that returns an existing File
        final String filepath = getExistingFilepath(filepaths);
        // Create the appropriate parser
        this.parser = decideParser(filepath);
        // Get or parse the file
        itemList = getOrParse();
    }

    /**
     * Gets all possible filepaths, that is: Classpath and the provided directory in the properties
     * 
     * @return A list of Strings, that contain all file paths
     */
    protected List<String> getAllPossibleFilepaths()
    {
        final List<String> filepaths = new ArrayList<String>(2);
        // Get the fileName from the property files
        String fileName = context.getPropertyByKey(NoCodingPropertyAdmin.FILENAME);
        // If the fileName is empty
        if (StringUtils.isBlank(fileName))
        {
            // Get the class and convert the class to a simple name
            fileName = getClass().getSimpleName();
        }
        String directory = getDirectoryToClasspath();
        filepaths.add(directory + fileName);
        directory = getDirectoryFromProperties();
        filepaths.add(directory + fileName);
        return filepaths;
    }

    /**
     * Gets the directory from the file paths, ending with {@link File#separatorChar}
     * 
     * @return String that describes the path to the file
     */
    protected String getDirectoryFromProperties()
    {
        // Get the directory from the properties
        String directory = context.getPropertyByKey(NoCodingPropertyAdmin.DIRECTORY);
        // If it doesn't end with the File.separatorChar
        if (!directory.endsWith(String.valueOf(File.separatorChar)))
        {
            // Add the separatorChar
            directory = directory + File.separatorChar;
        }
        // Return the directory
        return directory;
    }

    /**
     * Gets the directory from the classpath, ending with {@link File#separatorChar}
     * 
     * @return
     */
    protected String getDirectoryToClasspath()
    {
        // Get the classname with .class after it
        final String classString = getClass().getSimpleName() + ".class";
        // Get the URL to the class and extract its path
        final String path = getClass().getResource(classString).getPath();
        // Remove the classname with .class
        String directory = path.substring(0, path.length() - classString.length());

        // If the directory does not end with the File.separatorChar
        if (!directory.endsWith(String.valueOf(File.separatorChar)))
        {
            // Add the separatorChar
            directory = directory + File.separatorChar;
        }
        // Return the directory
        return directory;
    }

    /**
     * Goes through all provided file paths and tries to find a path to an existing file. If the extension is missing, it
     * adds yml, yaml and csv and searches for the file in that order.
     * 
     * @param possiblePaths
     *            All possible filepaths to look into, with or without file extension
     * @return The path to a found file
     */
    protected String getExistingFilepath(final List<String> possiblePaths)
    {
        String correctPath = null;
        for (final String filepath : possiblePaths)
        {
            // Get the extensions
            final String fileExtension = FilenameUtils.getExtension(filepath);
            if (!fileExtension.isEmpty() && new File(filepath).isFile())
            {
                correctPath = filepath;
                break;
            }

            // If the file extension is empty, try to add yml, yaml and csv
            else if (fileExtension.isEmpty())
            {
                // Check for YAML files first
                final String[] yamlPaths =
                    {
                      filepath + ".yml", filepath + ".yaml"
                    };

                for (final String yamlPath : yamlPaths)
                {
                    // If the new path is a file, create the parser and quit the loop
                    if (new File(yamlPath).isFile())
                    {
                        correctPath = yamlPath;
                        break;
                    }
                }
                // If a yaml file has been found, the pathToFile isn't null anymore, therefore break out of the loop
                if (correctPath != null)
                {
                    break;
                }

                // Check for CSV file second
                final String csvPath = filepath + ".csv";
                if (new File(csvPath).isFile())
                {
                    correctPath = csvPath;
                    break;
                }
            }
            else
            {
                throw new IllegalArgumentException("Illegal file type: " + "\"" + fileExtension + "\"" + "\n"
                                                   + "Supported types: '.yaml' | '.yml'\n");// or '.csv'" + "\n");
            }
        }
        return correctPath;
    }

    /**
     * Creates the correct parser depending on the file extension. For example creates a {@link YamlParser} for yml/yaml
     * files and a {@link CsvParser} for csv files.
     * 
     * @param path
     *            The path to the file with the file extension
     * @return The {@link Parser} that should be used for the specified file.
     */
    protected Parser decideParser(final String path)
    {
        Parser parser = null;
        // Get the extensions
        final String fileExtension = FilenameUtils.getExtension(path);
        if (!fileExtension.isEmpty() && new File(path).isFile())
        {
            // If it is yml or yaml
            if (fileExtension.equalsIgnoreCase("yml") || fileExtension.equalsIgnoreCase("yaml"))
            {
                parser = new YamlParser(path);
            }
            else if (fileExtension.equalsIgnoreCase("csv"))
            {
                parser = new CsvParser(path);
            }
            else
            {
                throw new IllegalArgumentException("Unknown file extension " + fileExtension);
            }
        }
        // If the file extension is empty, try to add yml, yaml and csv
        else if (fileExtension.isEmpty())
        {
            throw new IllegalArgumentException("Illegal file: " + "\"" + path + "\"" + "\n"
                                               + "Supported types: '.yaml', '.yml' or '.csv'\n");
        }

        // If the parser still is null, no file was found
        if (parser == null)
        {
            // No file with a supported extension was found
            throw new IllegalArgumentException("Failed to find a script file for file path: " + path);
        }
        return parser;
    }

    /**
     * Gets the list of <code>ScriptItem</code>s either by parsing them or from the cache. <br>
     * On the first execution, it gets the list from the parser and saves it in {@link #DATA_CACHE}. Then, it gets the list
     * out of the {@link #DATA_CACHE}.
     * 
     * @return A list of <code>ScriptItem</code>s generated from the file located at {@link #getDirectoryFromProperties()}
     * @throws IOException
     *             is thrown, for example, when the file is not found, or the parser encounters an error
     */
    protected List<ScriptItem> getOrParse() throws IOException
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

    /**
     * Executes the test case
     * 
     * @throws Throwable
     *             Most Throwable that happen during execution
     */
    @Test
    public void execute() throws Throwable
    {
        XltLogger.runTimeLogger.info("Starting Testcase : " + this.toString());
        // If there are ScriptItems in the itemList
        if (getItemList() != null && !getItemList().isEmpty())
        {
            // Execute every item
            for (final ScriptItem item : itemList)
            {
                XltLogger.runTimeLogger.info("Starting ScriptItem : " + item.toString());
                // TODO count zu index umbenennen -> storeItem interferiert
                context.setScriptItemCount(itemList.indexOf(item));
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

}
