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
package com.xceptance.xlt.nocoding.myUtilTests.variableResolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

import bsh.EvalError;

/**
 * Test the parameter handling via beanshell.
 */
public class ParameterInterpreterTest
{
    private VariableResolver interpreter;

    private PropertyManager propertyManager;

    @Before
    public void setup()
    {
        propertyManager = new PropertyManager(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void noParams() throws EvalError
    {
        final String toResolve = "No data here.";
        Assert.assertEquals(toResolve, VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void emptyParams()
    {
        final String toResolve = "No ${} here.";
        Assert.assertEquals(toResolve, VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void emptyParams_Spaces()
    {
        final String toResolve = "No ${  } here.";
        Assert.assertEquals(toResolve, VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void javaCall()
    {
        final String toResolve = "No ${Math.abs(-1)} here.";
        Assert.assertEquals("No 1 here.", VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void twoOccurences()
    {
        final String toResolve = "No ${Math.abs(-1)} here and ${Math.abs(-2)} here.";
        Assert.assertEquals("No 1 here and 2 here.", VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void keepState()
    {
        final String toResolve = "No ${m = Math.abs(-1)} here and ${m + Math.abs(-2)} here.";
        Assert.assertEquals("No 1 here and 3 here.", VariableResolver.resolveString(toResolve, propertyManager));
    }

    @Test
    public void stateAcrossCalls()
    {
        Assert.assertEquals("No 1 here.", VariableResolver.resolveString("No ${m = Math.abs(-1)} here.", propertyManager));
        Assert.assertEquals("No 2 here.", VariableResolver.resolveString("No ${m = m + Math.abs(-1)} here.", propertyManager));
    }

    @Test
    public void prePopulated()
    {
        final String toResolve = "No ${m = Math.abs(-1)} here and ${m + Math.abs(-2)} here.";
        Assert.assertEquals("No 1 here and 3 here.", VariableResolver.resolveString(toResolve, propertyManager));
    }
    //
    // @Test
    // public void storeAndEvaluete()
    // {
    // try
    // {
    // interpreter.set("a", "A");
    // interpreter.set("b", "B");
    // }
    // catch (final EvalError e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // Assert.assertEquals("A", interpreter.processDynamicData("${a}"));
    // Assert.assertEquals("B", interpreter.processDynamicData("${b}"));
    // Assert.assertEquals("AB", interpreter.processDynamicData("${a}${b}"));
    //
    // }
    //
    // @Test
    // public void storeNVPAndEvaluete()
    // {
    // try
    // {
    // final NameValuePair nvpC = new NameValuePair("c", "C");
    // final NameValuePair nvpD = new NameValuePair("d", "D");
    // interpreter.set(nvpC);
    // interpreter.set(nvpD);
    // }
    // catch (final EvalError e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // Assert.assertEquals("C", interpreter.processDynamicData("${c}"));
    // Assert.assertEquals("D", interpreter.processDynamicData("${d}"));
    // Assert.assertEquals("CD", interpreter.processDynamicData("${c}${d}"));
    // Assert.assertEquals("C", interpreter.processDynamicData("${c}"));
    // }

    // ----------------------------------------------------------------------------------------
    /* Error handling */
    // ----------------------------------------------------------------------------------------
    @Test
    public void invalidEnd()
    {
        Assert.assertEquals("Text${a", VariableResolver.resolveString("Text${a", propertyManager));
    }

    @Test
    public void invalidStart()
    {
        Assert.assertEquals("Text$ {a}", VariableResolver.resolveString("Text$ {a}", propertyManager));
    }

    @Test
    public void invalidMoreCurlyBraces()
    {
        // Assert.assertEquals("Text{", VariableResolver.resolveString("Text${'{'}", propertyManager));
        // Assert.assertEquals("Text}", VariableResolver.resolveString("Text${'}'}", propertyManager));
        Assert.assertEquals("TTe2t-TA12000",
                            VariableResolver.resolveString("T${java.text.MessageFormat.format(\"Te{0}t\",2)}-T${java.text.MessageFormat.format(\"A{0}{1}\",1, 2)}000",
                                                           propertyManager));
    }

}
