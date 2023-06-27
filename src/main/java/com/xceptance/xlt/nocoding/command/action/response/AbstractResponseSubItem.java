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
package com.xceptance.xlt.nocoding.command.action.response;

import java.io.Serializable;

import org.htmlunit.WebResponse;

import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * The interface for every response item. A response item is either an item that stores something or an item that
 * validates the {@link WebResponse} of a HTTP request.
 *
 * @author ckeiner
 */
public abstract class AbstractResponseSubItem implements Serializable
{
    /**
     * Executes the item by either storing or validating something in the {@link WebResponse}
     *
     * @param context
     *            The {@link Context} with the last {@link WebResponse} and {@link DataStorage}
     */
    public abstract void execute(final Context<?> context);
}
