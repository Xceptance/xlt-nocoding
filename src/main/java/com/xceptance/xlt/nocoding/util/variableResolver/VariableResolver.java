package com.xceptance.xlt.nocoding.util.variableResolver;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.nocoding.util.PropertyManager;

import bsh.EvalError;
import bsh.Interpreter;

public class VariableResolver
{
    /**
     * The pattern for finding variables
     */
    private final static Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\{[^\\{\\}]*\\}");

    public Interpreter interpreter;

    public VariableResolver(final GeneralDataProvider dataProvider)
    {
        interpreter = new Interpreter();
        try
        {
            interpreter.set("NOW", new ParameterInterpreterNow());
            interpreter.set("RANDOM", new ParameterInterpreterRandom());
            interpreter.set("DATE", new Date());
            interpreter.set("DATA", dataProvider);
        }
        catch (final EvalError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // TODO remove?
    public VariableResolver()
    {
        interpreter = new Interpreter();
        try
        {
            interpreter.set("NOW", new ParameterInterpreterNow());
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
     * Resolves string recursively from the inside to the outside
     * 
     * @param toResolve
     *            The String that you want to resolve
     * @param propertyManager
     *            The propertyManager with the global data storage inside
     * @return The resolved String
     * @throws EvalError
     */
    public String resolveString(final String toResolve, final PropertyManager propertyManager)
    {
        // Set replacement to our toResolve string
        String replacement = toResolve;
        final Matcher matcher = PARAMETER_PATTERN.matcher(toResolve);

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
                    throw new RuntimeException("Evaluation Error: ", e);
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
