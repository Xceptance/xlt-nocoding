package com.xceptance.xlt.common.tests;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.nocoding.api.AbstractNocodingTestCase;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.Downloader;

/**
 * Here, the important stuff is done:
 * <ul>
 * <li>for every {@link URLActionData} object from the {@link #actions} List, that was prepared in
 * {@link AbstractURLTestCase @Before}:
 * <ul>
 * <li>create a {@link WebRequest} from the {@link URLActionData}, depending on the type of request.
 * <li>execute the {@link WebRequest} via {@link URLActionDataExecutionable}.
 * <li>receive the result {@link URLActionDataExecutionableResult}
 * <li>handle and validate the {@link URLActionDataExecutionableResult} via {@link URLActionDataResponseHandler}.
 * </ul>
 * <li>static requests are treated differently. They go to the {@link Downloader} and cannot be validated. Therefore
 * before executing an action, we have to take a look onto the next actions in the list to see whether they are static
 * requests and must be added to the Downloader.
 * </ul>
 *
 * @author matthias mitterreiter
 * @deprecated Use {@link AbstractNocodingTestCase} instead.
 */
@Deprecated
public class URLTestCase extends AbstractNocodingTestCase
{
}
