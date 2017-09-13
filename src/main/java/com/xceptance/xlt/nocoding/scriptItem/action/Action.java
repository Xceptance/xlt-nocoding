package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public abstract class Action extends AbstractWebAction implements ScriptItem
{

    protected final Request request;

    protected final Response response;

    protected final List<AbstractSubrequest> subrequests;

    // TODO AbstractWebAction wirklich nötig? -> Vielleicht erst zur Laufzeit erzeugen
    // Stattdessen vllt lieber WebClient explizit wo erzeugen und übergeben
    // dh in execute von LightWeight/DomAction dann das erzeugen und an xlt anbinden
    public Action(final AbstractWebAction previousAction, final String timerName, final Request request, final Response response,
        final List<AbstractSubrequest> subrequests)
    {
        super(previousAction, timerName);
        this.request = request;
        this.response = response;
        this.subrequests = subrequests;
    }

    @Override
    public void executeItem(final PropertyManager propertyManager) throws Throwable
    {
        super.run();
    }

    public Request getRequest()
    {
        return request;
    }

    public Response getResponse()
    {
        return response;
    }

    public List<AbstractSubrequest> getSubrequests()
    {
        return subrequests;
    }

}
