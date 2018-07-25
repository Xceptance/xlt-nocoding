/**
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.xceptance.xlt.nocoding.util.resolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link VariableResolver}
 */
public class ParameterInterpreterTest
{
    private VariableResolver interpreter;

    private Context<?> context;

    /**
     * Creates a new {@link Context} and stores the {@link Context#getResolver()} in {@link #interpreter}
     */
    @Before
    public void setup()
    {
        context = new LightWeightContext(XltProperties.getInstance());
        interpreter = context.getResolver();
    }

    /**
     * Verifies if no variable is in the expression, nothing changes
     */
    @Test
    public void noParams()
    {
        final String toResolve = "No data here.";
        Assert.assertEquals(toResolve, interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies if a empty variable is in the expression, nothing changes
     */
    @Test
    public void emptyParams()
    {
        final String toResolve = "No ${} here.";
        Assert.assertEquals(toResolve, interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies if a empty variable with spaces is in the expression, nothing changes
     */
    @Test
    public void emptyParams_Spaces()
    {
        final String toResolve = "No ${  } here.";
        Assert.assertEquals(toResolve, interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies if a variable uses a function in java, the function gets called
     */
    @Test
    public void javaCall()
    {
        final String toResolve = "No ${Math.abs(-1)} here.";
        Assert.assertEquals("No 1 here.", interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies two variables get resolved
     */
    @Test
    public void twoOccurences()
    {
        final String toResolve = "No ${Math.abs(-1)} here and ${Math.abs(-2)} here.";
        Assert.assertEquals("No 1 here and 2 here.", interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies if a variable stores something, it is stored and available for a call in the same {@link String}
     */
    @Test
    public void keepState()
    {
        final String toResolve = "No ${m = Math.abs(-1)} here and ${m + Math.abs(-2)} here.";
        Assert.assertEquals("No 1 here and 3 here.", interpreter.resolveString(toResolve, context));
    }

    /**
     * Verifies if a variable stores something, it is stored and available for a call in another {@link String}
     */
    @Test
    public void stateAcrossCalls()
    {
        Assert.assertEquals("No 1 here.", interpreter.resolveString("No ${m = Math.abs(-1)} here.", context));
        Assert.assertEquals("No 2 here.", interpreter.resolveString("No ${m = m + Math.abs(-1)} here.", context));
    }

    /**
     * Verifies a variable in a variable is resolved, while the outer variable has a missing '}'.
     */
    @Test
    public void unfinishedDeclaration()
    {
        context.getVariables().store("test", "t");
        String resolved = interpreter.resolveString("${test", context);
        Assert.assertEquals("${test", resolved);
        resolved = interpreter.resolveString("${tes${test}", context);
        Assert.assertEquals("${test", resolved);
    }

    /**
     * Verifies a single variable gets resolved
     */
    @Test
    public void normalParam()
    {
        context.getVariables().store("host", "https://localhost:8443/posters/");
        final String resolved = interpreter.resolveString("${host}", context);
        Assert.assertEquals("https://localhost:8443/posters/", resolved);
    }

    /**
     * Verifies two simple variables get resolved
     */
    @Test
    public void twoNormalParam()
    {
        context.getVariables().store("host", "https://localhost:8443/posters/");
        final String resolved = interpreter.resolveString("${host}${host}", context);
        Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
    }

    /**
     * Verifies a variable in a variable gets resolved
     */
    @Test
    public void paramInParam()
    {
        context.getVariables().store("host", "https://localhost:8443/posters/");
        context.getVariables().store("blub", "s");
        final String resolved = interpreter.resolveString("${ho${blub}t}", context);
        Assert.assertEquals("https://localhost:8443/posters/", resolved);
    }

    /**
     * Verifies a variable in a variable gets resolved and a second variable, too
     */
    @Test
    public void paramInParamWithAnotherParam()
    {
        context.getVariables().store("host", "https://localhost:8443/posters/");
        context.getVariables().store("blub", "s");
        final String resolved = interpreter.resolveString("${ho${blub}t}${host}", context);
        Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
    }

    /**
     * Verifies {@link ParameterInterpreterNow} is called
     */
    @Test
    public void testBeanShellNow()
    {
        String resolved = interpreter.resolveString("${NOW.toString()}", context);
        Assert.assertNotNull(resolved);
        resolved = interpreter.resolveString("${NOW.toString()}", context);
        Assert.assertNotNull(resolved);
    }

    /**
     * Verifies {@link ParameterInterpreterRandom} is called
     */
    @Test
    public void testBeanShellRandom()
    {
        final String resolved = interpreter.resolveString("${RANDOM.Email()}", context);
        Assert.assertNotNull(resolved);
    }

    /**
     * Verifies a simple recursion does not get resolved
     */
    @Test
    public void testSimpleRecursion()
    {
        context.getVariables().store("host", "${host}");
        final String resolved = interpreter.resolveString("${host}", context);
        Assert.assertEquals("${host}", resolved);
    }

    /**
     * Verifies null cannot be stored, therefore variables mapped to null are not resolved
     */
    @Test
    public void testNullIsUnresolvableStore()
    {
        context.getVariables().store("host", null);
        final String resolved = interpreter.resolveString("${host}", context);
        Assert.assertEquals("${host}", resolved);
    }

    // ----------------------------------------------------------------------------------------
    /* Error handling */
    // ----------------------------------------------------------------------------------------

    /**
     * Verifies that a variable isn't resolved if it isn't fully defined
     */
    @Test
    public void invalidEnd()
    {
        Assert.assertEquals("Text${a", interpreter.resolveString("Text${a", context));
    }

    /**
     * Verifies that a variable isn't resolved if a whitespace is between $ and {
     */
    @Test
    public void invalidStart()
    {
        Assert.assertEquals("Text$ {a}", interpreter.resolveString("Text$ {a}", context));
    }

    /**
     * Verifies that a variable can contain '{', '}' and call a method with curly braces in it
     */
    @Test
    public void invalidMoreCurlyBraces()
    {
        Assert.assertEquals("Text{", interpreter.resolveString("Text${'{'}", context));
        Assert.assertEquals("Text}", interpreter.resolveString("Text${'}'}", context));
        Assert.assertEquals("TTe2t-TA12000",
                            interpreter.resolveString("T${java.text.MessageFormat.format(\"Te{0}t\",2)}-T${java.text.MessageFormat.format(\"A{0}{1}\",1,2)}000",
                                                      context));
    }

    /**
     * Verifies a recursion with two elements throws an error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEndlessRecursion()
    {
        context.getVariables().store("host", "${blub}");
        context.getVariables().store("blub", "${host}");
        interpreter.resolveString("${host}", context);
    }

}
