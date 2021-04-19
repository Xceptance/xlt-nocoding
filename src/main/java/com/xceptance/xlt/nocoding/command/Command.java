package com.xceptance.xlt.nocoding.command;

import java.io.Serializable;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Defines the behavior of a scriptItem. A script item is either an "Action", "Store", or default definitions.
 */
public interface Command extends Serializable
{
    /**
     * The method, which executes the ScriptItem.
     *
     * @param context
     *            The current {@link Context}
     * @throws Throwable
     */
    public void execute(Context<?> context) throws Throwable;
}
