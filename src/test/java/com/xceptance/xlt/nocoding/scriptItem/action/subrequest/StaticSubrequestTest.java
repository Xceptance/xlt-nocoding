package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

public class StaticSubrequestTest
{

    public Context context;

    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    @Test
    public void textConstructor()
    {
        final String url = "http://www.xceptance.net";
        final List<String> urls = new ArrayList<String>();
        urls.add(url);
        final StaticSubrequest subrequest = new StaticSubrequest(urls);
        subrequest.getUrls().forEach((subrequestUrl) -> {
            Assert.assertEquals(url, subrequestUrl);
        });
    }

    @Test
    public void testExecute() throws Exception
    {
        final String url = "http://www.xceptance.net";
        final List<String> urls = new ArrayList<String>();
        urls.add(url);
        final StaticSubrequest subrequest = new StaticSubrequest(urls);
        subrequest.execute(context);
    }

}
