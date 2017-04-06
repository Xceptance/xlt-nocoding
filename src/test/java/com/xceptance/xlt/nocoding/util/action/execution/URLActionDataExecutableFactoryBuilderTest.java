package com.xceptance.xlt.nocoding.util.action.execution;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.NoCodingPropAdmin;

public class URLActionDataExecutableFactoryBuilderTest
{
    private static NoCodingPropAdmin propAdmin;

    @BeforeClass
    public static void setup()
    {
        propAdmin = new NoCodingPropAdmin(XltProperties.getInstance(), "", "");
    }

    @Test
    public void testCorrectConstructorCreation()
    {
        URLActionDataExecutionbleFactoryBuilder factoryBuilder;
        factoryBuilder = new URLActionDataExecutionbleFactoryBuilder(propAdmin, URLActionDataExecutionbleFactoryBuilder.MODE_DOM);
        Assert.assertEquals(factoryBuilder.getPropAdmin(), propAdmin);
        Assert.assertEquals(factoryBuilder.getMode(), URLActionDataExecutionbleFactoryBuilder.MODE_DOM);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterInterpreter()
    {
        @SuppressWarnings("unused")
        final URLActionDataExecutionbleFactoryBuilder factory = new URLActionDataExecutionbleFactoryBuilder(null,
                                                                                                            URLActionDataExecutionbleFactoryBuilder.MODE_DOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongConstructorParameterMode()
    {
        @SuppressWarnings("unused")
        final URLActionDataExecutionbleFactoryBuilder factory = new URLActionDataExecutionbleFactoryBuilder(propAdmin, "someMode");
    }

    @Test
    public void testCorrectDomBuildingResult()
    {
        final URLActionDataExecutionbleFactoryBuilder factory = new URLActionDataExecutionbleFactoryBuilder(propAdmin,
                                                                                                            URLActionDataExecutionbleFactoryBuilder.MODE_DOM);
        final URLActionDataExecutionableFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof HtmlPageActionFactory);
    }

    @Test
    public void testCorrectLightBuildingResult()
    {
        final URLActionDataExecutionbleFactoryBuilder factory = new URLActionDataExecutionbleFactoryBuilder(propAdmin,
                                                                                                            URLActionDataExecutionbleFactoryBuilder.MODE_LIGHT);
        final URLActionDataExecutionableFactory actionFactory = factory.buildFactory();
        Assert.assertTrue(actionFactory instanceof LightWeightPageActionFactory);
    }

}
