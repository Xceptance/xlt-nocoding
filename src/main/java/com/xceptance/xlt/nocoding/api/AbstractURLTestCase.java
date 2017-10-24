package com.xceptance.xlt.nocoding.api;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.WebClientConfigurator;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * Executes a xlt-nocoding test case by parsing the file and then executing it
 * 
 * @author ckeiner
 */
public abstract class AbstractURLTestCase extends AbstractTestCase
{

    /**
     * The parser we use
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
        // Instantiate storage
        final DataStorage globalStore = new DataStorage();
        // TODO Remove this
        // Store host in it
        globalStore.storeVariable("host", "https://localhost:8443");
        // Instantiate the PropertyManager
        context = new Context(XltProperties.getInstance(), globalStore);
        // TODO write in constructor?
        // Load the default configuration
        context.getDataStorage().loadDefaultConfig();

        // Configure the webclient so it uses javascript, etc.
        context.configureWebClient();

        final String dataDirectory = context.getPropertyByKey(WebClientConfigurator.DIRECTORY);
        final String fileName = context.getPropertyByKey(WebClientConfigurator.FILENAME);
        final String pathToFile = dataDirectory + File.separatorChar + fileName;
        System.out.println(pathToFile);

        // this.parser = new MockParser();
        // this.parser = new YamlParser("./config/data/TLLogin.yml");
        // TODO ask why 404
        // this.parser = new YamlParser("./config/data/hellosuite.yml");
        // this.parser = new YamlParser("./config/data/TLExampleSubSelection.yml");
        // this.parser = new YamlParser("./config/data/TLRegister.yml");
        // this.parser = new com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser("./config/data/TLOrder.yml");
        this.parser = new com.xceptance.xlt.nocoding.parser.YamlParser(pathToFile);
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
                // TODO Log Eintrag!
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

}
