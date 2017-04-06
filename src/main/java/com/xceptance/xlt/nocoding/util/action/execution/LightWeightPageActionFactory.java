package com.xceptance.xlt.nocoding.util.action.execution;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.actions.Downloader;
import com.xceptance.xlt.nocoding.actions.LightWeightPageAction;
import com.xceptance.xlt.nocoding.actions.ModifiedAbstractHtmlPageAction;
import com.xceptance.xlt.nocoding.actions.ModifiedAbstractLightWeightPageAction;
import com.xceptance.xlt.nocoding.actions.XhrLightWeightPageAction;
import com.xceptance.xlt.nocoding.util.NoCodingPropAdmin;
import com.xceptance.xlt.nocoding.util.ParameterUtils;
import com.xceptance.xlt.nocoding.util.action.validation.URLActionDataExecutableResultFactory;

/**
 * Factory Class <br>
 * Produces {@link ModifiedAbstractHtmlPageAction}s, depending on the type of request. <br>
 * Cases:
 * <ul>
 * <li>Request is not a XmlHttpRequest -> use {@link #createPageAction(String, WebRequest)}
 * <li>Request is a XmlHttpRequest -> use {@link #createXhrPageAction(String, WebRequest)}
 * </ul>
 * The response is NOT parsed into the dom. <br>
 * See the execution model of {@link ModifiedAbstractLightWeightPageAction}
 *
 * @author matthias mitterreiter
 */
public class LightWeightPageActionFactory extends URLActionDataExecutionableFactory
{
    private NoCodingPropAdmin propAdmin;

    private LightWeightPageAction previousAction;

    private final URLActionDataExecutableResultFactory resultFactory;

    /**
     * @param properties
     *            {@link XltProperties} for {@link WebClient} configuration.
     */
    public LightWeightPageActionFactory(final NoCodingPropAdmin propAdmin)
    {
        super();
        setPropertiesAdmin(propAdmin);
        resultFactory = new URLActionDataExecutableResultFactory();
        XltLogger.runTimeLogger.debug("Creating new Instance");
    }

    private void setPropertiesAdmin(final NoCodingPropAdmin propAdmin)
    {
        ParameterUtils.isNotNull(propAdmin, "NoCodingPropAdmin");
        this.propAdmin = propAdmin;
    }

    @Override
    public URLActionDataExecutionable createPageAction(final String name, final WebRequest request)
    {
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");

        LightWeightPageAction action;

        if (previousAction == null)
        {
            action = new LightWeightPageAction(name, request, resultFactory);
            previousAction = action;
            action.setDownloader(createDownloader());
            configureWebClient((XltWebClient) action.getWebClient());
        }
        else
        {
            action = new LightWeightPageAction(previousAction, name, request, createDownloader(), resultFactory);
        }
        previousAction = action;
        return action;
    }

    private Downloader createDownloader()
    {
        final Boolean userAgentUID = propAdmin.getPropertyByKey(NoCodingPropAdmin.USERAGENTUID, false);
        final int threadCount = propAdmin.getPropertyByKey(NoCodingPropAdmin.DOWNLOADTHREADS, 1);

        final Downloader downloader = new Downloader((XltWebClient) previousAction.getWebClient(), threadCount, userAgentUID);

        return downloader;

    }

    private void configureWebClient(final XltWebClient webClient)
    {
        propAdmin.configWebClient(webClient);
    }

    @Override
    public URLActionDataExecutionable createXhrPageAction(final String name, final WebRequest request)
    {
        ParameterUtils.isNotNull(name, "name");
        ParameterUtils.isNotNull(request, "WebRequest");

        if (previousAction == null)
        {
            throw new IllegalArgumentException("Xhr action cannot be the first action");
        }
        final XhrLightWeightPageAction xhrAction = new XhrLightWeightPageAction(previousAction, name, request, createDownloader(),
                                                                                resultFactory);

        previousAction = xhrAction;
        return xhrAction;
    }

    public NoCodingPropAdmin getPropAdmin()
    {
        return propAdmin;
    }

    public URLActionDataExecutableResultFactory getResultFactory()
    {
        return resultFactory;
    }
}
