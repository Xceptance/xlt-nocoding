package com.xceptance.xlt.nocoding.command.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.util.context.Context;

public class StaticSubrequestTest extends AbstractContextTest
{

    public StaticSubrequestTest(final Context<?> context)
    {
        super(context);
    }

    @Test
    public void textConstructor()
    {
        final String url = "http://www.xceptance.net";
        final List<String> urls = new ArrayList<>();
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
        final List<String> urls = new ArrayList<>();
        urls.add(url);
        final StaticSubrequest subrequest = new StaticSubrequest(urls);
        subrequest.execute(context);
    }

}
