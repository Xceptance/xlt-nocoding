package com.xceptance.xlt.nocoding.util.bsh;

import org.junit.Test;

public class ParameterInterpreterRandomTest
{

    @Test
    public void testConstructor()
    {
        @SuppressWarnings("unused")
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
    }

    @Test
    public void testEmail()
    {
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
        final String email = pir.Email();
        System.err.println(email);
    }
}
