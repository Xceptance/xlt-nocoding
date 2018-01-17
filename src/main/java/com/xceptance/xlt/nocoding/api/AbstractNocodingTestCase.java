package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import com.xceptance.xlt.nocoding.parser.ParserFactory;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
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
     *             Thrown when the file is not found or the parser encounters an error
     */
    @Before
    public void initialize() throws IOException
    {
        // Instantiate the properties
        final XltProperties properties = XltProperties.getInstance();
        // Get the mode and create the corresponding Context
        String mode = properties.getProperty(NoCodingPropertyAdmin.MODE);
        // If mode isn't defined, set it to Lightweight
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
                throw new IllegalStateException("Mode must be " + NoCodingPropertyAdmin.LIGHTWEIGHT + " or " + NoCodingPropertyAdmin.DOM
                                                + " but is " + mode);
        }
        // Get the possible filePaths
        final List<String> filepaths = getAllPossibleFilepaths();
        // Find the path that returns an existing File
        final String filepath = getExistingFilepath(filepaths);
        // Create the appropriate parser
        this.parser = getParserFor(filepath);
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
        final List<String> filepaths = new ArrayList<String>(2);
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
        // If the directory is not defined in the properties, assume
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
     * Goes through all provided file paths and tries to find a path to an existing file. If the extension is missing, it
     * adds all known extensions via {@link ParserFactory#getExtensions()} and searches for the file.
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
            if (!fileExtension.isEmpty())
            {
                // Check if the file exists
                if (existsFileFor(filepath))
                {
                    // If it does, this is the correct path
                    correctPath = filepath;
                    // Exit the loop
                    break;
                }
            }

            // If the file extension is empty, try to add all known extensions
            else if (fileExtension.isEmpty())
            {
                // Get all possible extensions
                final List<String> extensions = ParserFactory.getInstance().getExtensions();
                // Try to add each extension and stop when a file is found
                for (final String extension : extensions)
                {
                    // Add .extension to the path
                    final String fullPath = filepath + "." + extension;
                    // Check if it exists
                    if (existsFileFor(fullPath))
                    {
                        correctPath = fullPath;
                        break;
                    }
                }
                // If a file has been found, the correctPath isn't null anymore, therefore break out of the outer loop
                if (correctPath != null)
                {
                    break;
                }
            }
        }
        // Since we cannot say for sure if we found an existing file, throw an error if correctPath is still null
        if (correctPath == null)
        {
            String allFiles = "";
            for (final String path : possiblePaths)
            {
                allFiles += path + "\n";
            }
            // Remove last \n
            allFiles = allFiles.substring(0, allFiles.length() - 1);
            String extensions = "";
            for (final String extension : ParserFactory.getInstance().getExtensions())
            {
                extensions += extension + ", ";
            }
            // Remove comma
            extensions = extensions.substring(0, extensions.length() - 2);

            final String errorMessage = "No script file found in any of these locations:\n" + allFiles
                                        + " with the following extensions added if none was supplied: " + extensions;
            throw new IllegalArgumentException(errorMessage);
        }
        return correctPath;
    }

    /**
     * Checks if at the location of <code>fullPath</code> anything can be found.<br>
     * At first, it creates a file at the path and if it exists, the function returns true. If no File exists, it creates a
     * URL and tries to open the Stream. If the Stream can be opened, then we can read from there and return true.<br>
     * 
     * @param fullPath
     * @return True if either a File or URL can read from the provided <code>fullPath</code><br>
     */
    protected boolean existsFileFor(final String fullPath)
    {
        boolean exists = false;

        // Check if a file exists at the location
        if (new File(fullPath).exists())
        {
            exists = true;
        }
        // If no file could be found
        else
        {
            try
            {
                // Try to build a URL out of fullPath
                final URL file = new URL(fullPath);
                if (file != null)
                {
                    // Try to open the stream
                    file.openStream();
                    // We only execute this line, if the stream could be opened
                    exists = true;
                }
            }
            // Catch MalformedURLException and every other IO exception that occurs when opening the Stream, thus telling us, that
            // we cannot find something at the location
            catch (final IOException e)
            {
                exists = false;
            }
        }
        // Return the value of exists
        return exists;
    }

    /**
     * Creates the correct parser depending on the file extension. For example, it creates a {@link YamlParser} for yml/yaml
     * files and a {@link CsvParser} for CSV files.
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
     * On the first execution, it gets the list from the parser and saves it in {@link #DATA_CACHE}. Then, it gets the list
     * out of the {@link #DATA_CACHE}.
     * 
     * @param filePath
     *            The path to the file that is to be parsed
     * @return A list of <code>ScriptItem</code>s generated from the provided file
     * @throws IOException
     *             Thrown when the parser encounters an error
     */
    protected List<ScriptItem> getOrParse(final String filePath) throws IOException
    {
        synchronized (DATA_CACHE)
        {
            List<ScriptItem> result = DATA_CACHE.get(filePath);
            if (result == null)
            {
                result = parser.parse(filePath);
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
            int index = 0;
            // Execute every item
            for (final ScriptItem item : itemList)
            {
                XltLogger.runTimeLogger.info("Starting ScriptItem : " + item.toString());
                if (item instanceof Action)
                {
                    context.setScriptItemIndex(index++);
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
