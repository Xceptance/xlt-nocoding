package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Context;

public class CookieSelector extends AbstractSelector
{

    public CookieSelector(final String selectionExpression)
    {
        super(selectionExpression);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(final Context context)
    {
        // Resolve variables
        resolveValues(context);

        final WebResponse webResponse = context.getWebResponse();

        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the Set-Cookie header
            if (header.getName().equals("Set-Cookie"))
            {
                // And verify if this is the correct cookie by
                // grabbing the cookie name
                final int equalSignPosition = header.getValue().indexOf("=");
                final String cookieName = header.getValue().substring(0, equalSignPosition);
                // and comparing it with the input name
                if (cookieName.equals(selectionExpression))
                {
                    // Get the content of the cookie, which is until the first semicolon
                    final int semicolonPosition = header.getValue().indexOf(";");
                    // Content starts after the equal sign (position+1) and ends before the semicolon
                    final String cookieContent = header.getValue().substring(equalSignPosition + 1, semicolonPosition);
                    setResult(cookieContent);
                    break;
                }
            }
        }

    }

}
