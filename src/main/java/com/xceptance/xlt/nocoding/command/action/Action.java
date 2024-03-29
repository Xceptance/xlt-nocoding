/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.command.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.InvalidArgumentException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.ActionSubItemUtil;
import com.xceptance.xlt.nocoding.util.WebAction;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * <code>Action</code> is the data model for the "Action" type of the execution.<br>
 * Therefore, it consists of a {@link #name} and list of {@link AbstractActionSubItem}s.
 *
 * @author ckeiner
 */
public class Action implements Command
{
    /**
     * The name of the action
     */
    protected String name;

    /**
     * The list of actionItems
     */
    protected final List<AbstractActionSubItem> actionItems;

    /**
     * Creates an instance of {@link Action} that sets {@link #actionItems} to an ArrayList of size 1.
     */
    public Action()
    {
        actionItems = new ArrayList<>(1);
    }

    /**
     * Creates an instance of {@link Action} that sets {@link #name} and {@link #actionItems}.
     *
     * @param name
     *            The name of the action
     * @param actionItems
     *            A list of {@link AbstractActionSubItem}s
     */
    public Action(final String name, final List<AbstractActionSubItem> actionItems)
    {
        this.name = name;
        this.actionItems = actionItems;
    }

    public List<AbstractActionSubItem> getActionItems()
    {
        return actionItems;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Fills in the default data for name, request, response if it isn't specified and verifies there is only one
     * request, response and in the correct order. Finally, it adds the default static subrequests.
     *
     * @param context
     *            The {@link Context} with the {@link DataStorage}.
     */
    protected void fillDefaultData(final Context<?> context)
    {
        if (name == null || name.isEmpty())
        {
            setName(ActionSubItemUtil.getDefaultName(context, "Action"));
        }
        ActionSubItemUtil.assertOrder(actionItems);
        ActionSubItemUtil.fillDefaultData(actionItems, context);

        // Add default static requests
        if (context.getDefaultStatics() != null && !context.getDefaultStatics().getItems().isEmpty())
        {
            actionItems.add(new StaticSubrequest(context.getDefaultStatics().getItems()));
            XltLogger.runTimeLogger.debug("Added default static subrequests to Action " + name);
        }
    }

    /**
     * Tries to resolve all variables of non-null attributes
     *
     * @param context
     *            The {@link Context} with the {@link VariableResolver}
     * @throws InvalidArgumentException
     */
    protected void resolveValues(final Context<?> context)
    {
        if (getName() != null)
        {
            setName(context.resolveString(getName()));
        }
    }

    /**
     * Executes the {@link Action} by building a {@link WebAction} with the {@link #actionItems}. The
     * <code>WebAction</code> then executes the <code>actionItems</code>. In the end, the loaded page gets appended to
     * the result browser.
     *
     * @throws Throwable
     *             if a Throwable occurs during the execution or when the page is appended to the result browser
     */
    @Override
    public void execute(final Context<?> context) throws Throwable
    {
        // Fill default data
        fillDefaultData(context);
        // Resolve values
        resolveValues(context);

        // Create the WebAction with the data of this action
        final WebAction webAction = new WebAction(name, context, getActionItems());

        // Try to execute it
        try
        {
            // Execute the requests, responses and subrequests via xlt api
            webAction.run();
        }
        catch (final Exception | Error e)
        {
            final String errorMessage = "Execution Step '" + getName() + "' failed: " + e.getMessage();
            if (e instanceof Exception)
            {
                throw new Exception(errorMessage, e);
            }
            else
            {
                throw new Error(errorMessage, e);

            }
        }
        // And always append the page to the result browser
        finally
        {
            // Append the page to the result browser
            if (context.getWebResponse() != null)
            {
                context.appendToResultBrowser();
            }
        }
    }

}
