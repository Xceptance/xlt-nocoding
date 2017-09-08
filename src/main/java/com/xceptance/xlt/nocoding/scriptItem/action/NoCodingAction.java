package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.scriptItem.NoCodingScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

public abstract class NoCodingAction extends AbstractWebAction implements NoCodingScriptItem
{

    protected final Request request;

    protected final Response response;

    protected final List<AbstractSubrequest> subrequests;

    public NoCodingAction(final AbstractWebAction previousAction, final String timerName, final Request request, final Response response,
        final List<AbstractSubrequest> subrequests)
    {
        super(previousAction, timerName);
        this.request = request;
        this.response = response;
        this.subrequests = subrequests;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void executeAction() throws Throwable
    {
        super.run();
    }

}
