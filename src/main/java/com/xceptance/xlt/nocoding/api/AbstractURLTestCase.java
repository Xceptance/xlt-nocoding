package com.xceptance.xlt.nocoding.api;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.tests.AbstractTestCase;
import com.xceptance.xlt.nocoding.parser.MockParser;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.scriptItem.NoCodingScriptItem;

public abstract class AbstractURLTestCase extends AbstractTestCase
{
    private Parser parser;

    private List<NoCodingScriptItem> itemList;

    @Before
    public void initialize()
    {
        this.parser = new MockParser();
        itemList = parser.parse();
    }

    @Test
    public void executeTest() throws Throwable
    {
        for (final NoCodingScriptItem item : itemList)
        {
            // TODO Log Eintrag?
            item.executeAction();
        }
    }
}
