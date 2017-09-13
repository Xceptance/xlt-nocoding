package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

public class Subrequests
{
    private final List<AbstractSubrequest> subrequests;

    public Subrequests(final List<AbstractSubrequest> subrequests)
    {
        this.subrequests = subrequests;
    }

    public void execute()
    {
        for (final AbstractSubrequest abstractSubrequest : subrequests)
        {
            abstractSubrequest.execute();
        }
    }
}
