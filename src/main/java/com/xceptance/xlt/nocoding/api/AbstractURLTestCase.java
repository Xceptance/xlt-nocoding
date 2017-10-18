package com.xceptance.xlt.nocoding.api;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Context;
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
     * The property manager, that handles all properties
     */
    private Context context;

    /**
     * Prepares the test case by: Instantiating the PropertyManager, Loading the default configuration, and Parsing the
     * definition file
     */
    @Before
    public void initialize()
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

        // this.parser = new MockParser();
        this.parser = new YamlParser();
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
