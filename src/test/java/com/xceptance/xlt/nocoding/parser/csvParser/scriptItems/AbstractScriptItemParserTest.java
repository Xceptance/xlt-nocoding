package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;

public abstract class AbstractScriptItemParserTest
{
    Context context;

    @Before
    public void init()
    {
        context = new DomContext(XltProperties.getInstance());
    }

}
