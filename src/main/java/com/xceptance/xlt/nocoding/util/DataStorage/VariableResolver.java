package com.xceptance.xlt.nocoding.util.DataStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xceptance.xlt.nocoding.util.PropertyManager;

public class VariableResolver
{

    // TODO brauch ich nicht, siehe xlt -> (general)dataProvider
    public static String dataDirectory = "/config/data/default";

    /**
     * Resolves string recursively from the inside to the outside
     * 
     * @param toResolve
     *            The String that you want to resolve
     * @param propertyManager
     *            The propertyManager with the global data storage inside
     * @return The resolved String
     */
    public static String resolveString(final String toResolve, final PropertyManager propertyManager)
    {
        // set replacement to our toResolve string
        String replacement = toResolve;
        final Matcher matcher = Pattern.compile("\\$\\{[^\\{\\}]*\\}").matcher(toResolve);
        // TODO Das hier muss rekursiv werden, auch $Random, BeanShell kann rekursiv sein
        while (matcher.find())
        {
            final String foundVariable = matcher.group();
            // Remove ${ and }
            String resolvedTarget = foundVariable.substring(2, foundVariable.length() - 1);
            // Is this a DATA. or RANDOM. expression
            if (resolvedTarget.contains("DATA.") || resolvedTarget.contains("RANDOM."))
            {
                // TODO handle this

            }
            else
            {
                // try to find it in the storage
                resolvedTarget = propertyManager.getDataStorage().searchFor(resolvedTarget);
                if (resolvedTarget == null)
                {
                    // TODO Beanshell Magic
                }
            }

            // finally replace it
            if (foundVariable != null && resolvedTarget != null)
            {
                replacement = toResolve.replace(foundVariable, resolvedTarget);
            }
            // Finally resolve other placeholders
            replacement = resolveString(replacement, propertyManager);
        }

        return replacement;
    }
}
