package com.xceptance.xlt.nocoding.util.variableResolver;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

    public VariableResolver()
    {
        this(GeneralDataProvider.getInstance());
    }

    public String resolveString(final String toResolve, final PropertyManager propertyManager)
    {
        return resolveStringInAGoodWayHopefully(toResolve, propertyManager);
        // return resolveStringNew(toResolve, propertyManager, 0, new Stack<Integer>());
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

    public String resolveStringInAGoodWayHopefully(final String toResolve, final PropertyManager propertyManager)
    {
        /**
         * When true, this ignores everything, until another ' apppears
         */
        Boolean ignore = false;
        String output = "";
        // iterate over the whole String
        for (int i = 0; i < toResolve.length(); i++)
        {
            // Save the current character
            final char current = toResolve.charAt(i);

            if (current == '\'')
            {
                // invert the value of ignore
                ignore = Boolean.logicalXor(ignore, false);
            }
            else if (!ignore && current == '$')
            {
                // check for variable if there is another symbol
                if (toResolve.length() > i + 1 && toResolve.charAt(i + 1) == '{')
                {
                    // VARIABLE HANDLER
                    // Raise i by two ( +1 -> {, +2 character after { )
                    i = i + 2;
                    // Search for variable in the new string and resolve it
                    final Pair<String, Integer> resolvedPair = doRecursion(toResolve.substring(i), propertyManager);
                    // Add the resolved variable to output
                    output += resolvedPair.getLeft();
                    // And raise i by the length of the variable name
                    i += resolvedPair.getRight();

                }
                // happy path
                else
                {
                    output += current;
                }
            }
            else
            {
                output += current;
            }
        }
        return output;
    }

    private Pair<String, Integer> doRecursion(final String toResolve, final PropertyManager propertyManager)
    {
        /**
         * When true, this ignores everything, until another ' apppears
         */
        Boolean ignore = false;
        String output = "";
        int length = 0;
        // iterate over the whole String
        for (int i = 0; i < toResolve.length(); i++)
        {
            // Save the current character
            final char current = toResolve.charAt(i);

            if (current == '\'')
            {
                // invert the value of ignore
                ignore = Boolean.logicalXor(ignore, false);
            }
            else if (!ignore && current == '$')
            {
                // check for variable if there is another symbol
                if (toResolve.length() > i + 1 && toResolve.charAt(i + 1) == '{')
                {
                    // Raise i by two ( +1 -> {, +2 character after { )
                    i = i + 2;
                    final Pair<String, Integer> resolvedPair = doRecursion(toResolve.substring(i), propertyManager);
                    // Add the resolved variable to output
                    output += resolvedPair.getLeft();
                    // And raise i by the length of the variable name
                    i += resolvedPair.getRight();
                }
                // happy path
                else
                {
                    output += current;
                }
            }
            // We found a possible end
            else if (current == '}')
            {
                // Since we are in this function, we did find a variable sign,
                length = i;
                String resolvedValue = propertyManager.getDataStorage().searchFor(output);
                // if we didn't find it, let beanshell handle the variable
                if (resolvedValue == null && !output.equals("{"))
                {
                    try
                    {
                        final Object beanShellEval = interpreter.eval(output);
                        // if beanshell found something, we save it as a string
                        if (beanShellEval != null)
                        {
                            resolvedValue = beanShellEval.toString();
                        }
                        // BeanSheall doesn't know the variable, therefore we want the plain text
                        else
                        {
                            // So we simply add ${ and } again.
                            resolvedValue = "${" + output + "}";
                        }
                    }
                    catch (final EvalError e)
                    {
                        throw new RuntimeException("Evaluation Error: ", e);
                    }
                }
                // This fixes Text${'{'}
                else if (resolvedValue == null)
                {
                    resolvedValue = output;
                }

                output = resolvedValue;
                // we found a variable, therefore we are done
                break;
            }
            else
            {
                output += current;
            }
            length = i;
        }

        // handle missing ending curly brace aka }
        if (output.equals(toResolve))
        {
            output = "${" + output;
        }

        final Pair<String, Integer> resolvedPair = new ImmutablePair<String, Integer>(output, length);
        return resolvedPair;
    }

}
