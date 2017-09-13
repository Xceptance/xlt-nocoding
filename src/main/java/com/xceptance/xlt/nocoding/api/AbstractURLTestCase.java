package com.xceptance.xlt.nocoding.api;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.parser.MockParser;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public abstract class AbstractURLTestCase extends AbstractTestCase
{
    private Parser parser;

    private List<ScriptItem> itemList;

    private PropertyManager propertyManager;

    private DataStorage globalStorage;

    @Before
    public void initialize()
    {
        this.globalStorage = new DataStorage();
        propertyManager = new PropertyManager(XltProperties.getInstance(), globalStorage);

        // this.parser = new MockParser(globalStorage);
        // TODO Think about whether we really want to use this to share the global storage
        this.parser = new MockParser();
        itemList = parser.parse();
    }

    @Test
    public void executeTest() throws Throwable
    {
        // TODO instantiate propertyManager, Store, etc

        for (final ScriptItem item : itemList)
        {
            // TODO Log Eintrag!
            // TODO übergib die propertyManager, etc entweder hier oder im parser
            item.execute(propertyManager);
        }
    }
}
