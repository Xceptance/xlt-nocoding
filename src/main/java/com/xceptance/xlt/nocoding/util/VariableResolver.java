package com.xceptance.xlt.nocoding.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xceptance.xlt.api.data.GeneralDataProvider;

import bsh.EvalError;
import bsh.Interpreter;

public class VariableResolver
{

    // TODO brauch ich nicht, siehe xlt -> (general)dataProvider
    public static String dataDirectory = "/config/data/default";

    public static Interpreter interpreter = new Interpreter();

    // TODO GeneralDataProvider einbinden
    // public static DataProvider dataProvider = new DataProvider();

    static
    {
        interpreter = new Interpreter();
        try
        {
            interpreter.set("NOW", String.valueOf(System.currentTimeMillis()));
            interpreter.set("RANDOM", new ParameterInterpreterRandom());
            interpreter.set("DATE", new Date());
            interpreter.set("DATA", GeneralDataProvider.getInstance());
        }
        catch (final EvalError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * The pattern for finding variables
     */
    private final static Pattern parameterPattern = Pattern.compile("\\$\\{[^\\{\\}]*\\}");

    /**
     * Resolves string recursively from the inside to the outside
     * 
     * @param toResolve
     *            The String that you want to resolve
     * @param propertyManager
     *            The propertyManager with the global data storage inside
     * @return The resolved String
     * @throws EvalError
     */
    public static String resolveString(final String toResolve, final PropertyManager propertyManager)
    {
        // set replacement to our toResolve string
        String replacement = toResolve;
        final Matcher matcher = parameterPattern.matcher(toResolve);
        // TODO Das hier muss rekursiv werden, auch $Random, BeanShell kann rekursiv sein
        while (matcher.find())
        {
            final String foundVariable = matcher.group();
            // Remove ${ and }
            final String resolvedTarget = foundVariable.substring(2, foundVariable.length() - 1);
            // Is this a DATA. or RANDOM. expression

            // try to find it in the storage
            String resolvedValue = propertyManager.getDataStorage().searchFor(resolvedTarget);
            // if we didn't find it, ask beanshell
            if (resolvedValue == null)
            {
                try
                {
                    resolvedValue = (String) interpreter.eval(resolvedTarget);
                }
                catch (final EvalError e)
                {
                    // TODO fancy things in here pls
                    throw new RuntimeException("variabl", e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // finally replace it
            if (foundVariable != null && resolvedValue != null)
            {
                replacement = toResolve.replace(foundVariable, resolvedValue);
            }
            // Finally resolve other placeholders
            replacement = resolveString(replacement, propertyManager);
        }

        return replacement;
    }
}
