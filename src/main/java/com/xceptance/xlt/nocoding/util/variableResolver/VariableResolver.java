package com.xceptance.xlt.nocoding.util.variableResolver;

import java.util.Date;
import java.util.Stack;
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

    public String resolveString(final String toResolve, final PropertyManager propertyManager)
    {
        return resolveStringNew(toResolve, propertyManager);
    }

    /**
     * Resolves string recursively from the inside to the outside
     * 
     * @param toResolve
     *            The String that you want to resolve
     * @param propertyManager
     *            The propertyManager with the global data storage inside
     * @return String - The resolved String
     * @throws EvalError
     */
    public String resolveStringOld(final String toResolve, final PropertyManager propertyManager)
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

    static int recursionLevel = 0;

    static Stack<Integer> recursiveLength = new Stack<Integer>();

    public String resolveStringNew(final String toResolve, final PropertyManager propertyManager)
    {
        final String toResolve_varDynamic = toResolve;
        String output = "";

        for (int i = 0; i < toResolve_varDynamic.length(); i++)
        {
            final char charAtPosition = toResolve_varDynamic.charAt(i);
            // can there be a variable? is there a variable sign? is this followed by curly braces?
            if (toResolve_varDynamic.length() >= i + 2 && charAtPosition == '$' && toResolve_varDynamic.charAt(i + 1) == '{')
            {
                // recursion
                recursionLevel += 1;
                System.out.println("Incrementing recursion to " + recursionLevel);
                output += resolveStringNew(toResolve_varDynamic.substring(i + 2), propertyManager);
                // since we found a variable, we are finished in this loop
                recursionLevel -= 1;
                System.out.println("Decrementing recursion to " + recursionLevel);
                i += recursiveLength.pop() + 2;

            }
            // did we encounter $ and { before }?
            else if (charAtPosition == '}' && recursionLevel != 0)
            {
                // We found a variable, so we need to save the length of the variable string
                recursiveLength.push(i);
                // Since this is a variable, we search for it in the dataStorage
                if (propertyManager.getDataStorage().searchFor(output) != null)
                {
                    // resolve the value by overwriting output
                    output = propertyManager.getDataStorage().getVariableByKey(output);
                }
                // if we didn't find it, we use beanshell
                else
                {
                    try
                    {
                        final Object beanShellEval = interpreter.eval(output);
                        // if beanshell found something, we save it as a string
                        if (beanShellEval != null)
                        {
                            // resolve the value by overwriting output
                            output = beanShellEval.toString();
                        }
                    }
                    catch (final EvalError e)
                    {
                        throw new RuntimeException("Evaluation Error: ", e);
                    }
                }
                // and want to return
                break;
            }
            else
            {
                // normal case
                output += charAtPosition;
            }
        }

        return output;
    }
}
