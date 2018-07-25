package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserFactory;
import com.xceptance.xlt.nocoding.parser.csv.CsvParser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;
import com.xceptance.xlt.nocoding.util.FileFinderUtils;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.ObjectUtils;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Executes a xlt-nocoding test case by parsing the file specified in the classpath or the properties and executing the
 * parsed commands.
 *
 * @author ckeiner
 */
public abstract class AbstractNocodingTestCase extends AbstractTestCase
{
    /**
     * Cache of all parsed data.
     */
    private static final Map<String, List<Command>> DATA_CACHE = new HashMap<>();

    /**
     * The parser to use for parsing
     */
    private Parser parser;

    /**
     * The list of {@link Command}s
     */
    private List<Command> itemList;

    /**
     * The {@link Context} of all <code>ScriptItems</code>
     */
    private Context<?> context;

    public Parser getParser()
    {
        return parser;
    }

    public List<Command> getItemList()
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
     * @throws Exception
     */
    @Before
    public void initialize() throws Exception
    {
        // Instantiate the properties
        final XltProperties properties = XltProperties.getInstance();
        // Get the mode and create the corresponding Context
        String mode = properties.getProperty(NoCodingPropertyAdmin.MODE);
        // If mode isn't defined, set it to the default mode
        if (mode == null)
        {
            XltLogger.runTimeLogger.info("No mode supplied, assuming default mode: " + NoCodingPropertyAdmin.MODE_DEFAULT);
            mode = NoCodingPropertyAdmin.MODE_DEFAULT;
        }
        switch (mode)
        {
            case NoCodingPropertyAdmin.LIGHTWEIGHT:
                context = new LightWeightContext(properties);
                break;

            case NoCodingPropertyAdmin.DOM:
                context = new DomContext(properties);
                break;

            default:
                // Mode is neither of the two options, therefore throw an error
                throw new IllegalStateException("Mode must be " + NoCodingPropertyAdmin.LIGHTWEIGHT + " or " + NoCodingPropertyAdmin.DOM
                                                + " but is " + mode);
        }
        // Get the possible filePaths
        final List<String> filepaths = getAllPossibleFilepaths();
        // Find the path that returns an existing File
        final String filepath = FileFinderUtils.getExistingFilepath(filepaths);
        // Create the appropriate parser
        parser = getParserFor(filepath);
        // Get or parse the file
        itemList = getOrParse(filepath);
    }

    /**
     * Gets all possible filepaths, that is: Classpath and the provided directory in the properties
     *
     * @return A list of Strings, that contain all file paths
     */
    protected List<String> getAllPossibleFilepaths()
    {
        final List<String> filepaths = new ArrayList<>(2);
        // Get the fileName from the property files
        String fileName = context.getPropertyByKey(NoCodingPropertyAdmin.FILENAME);
        // If the fileName is empty
        if (StringUtils.isBlank(fileName))
        {
            // Get the class and convert the class to a simple name
            fileName = getClass().getSimpleName();
        }
        // Get the directory to the classpath
        String directory = getDirectoryToClasspath();
        filepaths.add(directory + fileName);
        // Get the directory from the properties
        directory = getDirectoryFromProperties();
        filepaths.add(directory + fileName);
        // Return all filepaths
        return filepaths;
    }

    /**
     * Gets the directory from the file paths, ending with {@link File#separatorChar}
     *
     * @return String that describes the path to the directory defined in the properties
     */
    protected String getDirectoryFromProperties()
    {
        // Get the directory from the properties
        String directory = context.getPropertyByKey(NoCodingPropertyAdmin.DIRECTORY);
        // If the directory is not defined in the properties, assume default directory
        if (directory == null)
        {
            directory = NoCodingPropertyAdmin.DIRECTORY_DEFAULT;
        }
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
     * @return String that describes the path to the classpath directory
     */
    protected String getDirectoryToClasspath()
    {
        // Get the classname with .class after it
        final String classString = getClass().getSimpleName() + ".class";
        // Get the URL to the class and extract its path
        final String path = getClass().getResource(classString).toString();
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
     * Creates the correct parser depending on the file extension. For example, it creates a {@link YamlParser} for
     * yml/yaml files and a {@link CsvParser} for CSV files.
     *
     * @param path
     *            The path to the file with the file extension
     * @return The {@link Parser} that should be used for the specified file.
     */
    protected Parser getParserFor(final String path)
    {
        return ParserFactory.getInstance().getParser(path);
    }

    /**
     * Gets the list of <code>ScriptItem</code>s either by parsing them or from the cache. <br>
     * On the first execution, it gets the list from the parser and saves it in {@link #DATA_CACHE}. Then, it gets the
     * list out of a deep copy from the {@link #DATA_CACHE}.
     *
     * @param filePath
     *            The path to the file that is to be parsed
     * @return A list of <code>ScriptItem</code>s generated from the provided file
     * @throws Exception
     */
    protected List<Command> getOrParse(final String filePath) throws Exception
    {
        synchronized (DATA_CACHE)
        {
            List<Command> result = DATA_CACHE.get(filePath);
            if (result == null)
            {
                XltLogger.runTimeLogger.debug("Parsing file...");
                result = parser.parse(filePath);
                // Put a clone of the parsed data in the cache
                DATA_CACHE.put(filePath, ObjectUtils.cloneObject(result));
            }
            // Otherwise, clone the objects, so we do not have problems with concurrency
            else
            {
                result = ObjectUtils.cloneObject(DATA_CACHE.get(filePath));
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
        XltLogger.runTimeLogger.info("Starting Testcase : " + toString());
        // If there are ScriptItems in the itemList
        if (getItemList() != null && !getItemList().isEmpty())
        {
            int index = 0;
            // Execute every item
            for (final Command item : itemList)
            {
                XltLogger.runTimeLogger.info("Starting ScriptItem : " + item.toString());
                if (item instanceof Action)
                {
                    context.setActionIndex(index++);
                }
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
