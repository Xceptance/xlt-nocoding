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
        // Set replacement to our toResolve string
        String replacement = toResolve;
        final Matcher matcher = parameterPattern.matcher(toResolve);

        while (matcher.find())
        {
            final String foundVariable = matcher.group();
            // Remove ${ and }
            final String resolvedVariable = foundVariable.substring(2, foundVariable.length() - 1);
            // Search in the storage for the variable
            String resolvedValue = propertyManager.getDataStorage().searchFor(resolvedVariable);
            // if we didn't find it, let beanshell handle the variable
            if (resolvedValue == null)
            {
                try
                {
                    final Object beanShellEval = interpreter.eval(resolvedVariable);
                    // if beanshell found something, we save it as a string
                    if (beanShellEval != null)
                    {
                        resolvedValue = beanShellEval.toString();
                    }
                }
                catch (final EvalError e)
                {
                    if (!e.getClass().equals(NullPointerException.class.getClass()))
                    {
                        throw new RuntimeException("variable", e);
                    }
                }
            }

            // Replace the resolved value
            if (foundVariable != null && resolvedValue != null)
            {
                replacement = toResolve.replace(foundVariable, resolvedValue);
                // Finally resolve other placeholders
                replacement = resolveString(replacement, propertyManager);
            }
        }

        return replacement;
    }
}
