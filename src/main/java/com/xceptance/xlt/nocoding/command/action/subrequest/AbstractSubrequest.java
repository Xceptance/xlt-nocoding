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
package com.xceptance.xlt.nocoding.command.action.subrequest;

import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * The abstract class for every subrequest.
 *
 * @author ckeiner
 */
public abstract class AbstractSubrequest extends AbstractActionSubItem
{

    /**
     * Executes the subrequest.
     *
     * @param context
     *            The {@link Context} for this subrequest
     */
    @Override
    public abstract void execute(Context<?> context) throws Exception;

    /**
     * Fills default data of an item.
     *
     * @param context
     *            The {@link Context} for this subrequest
     */
    public abstract void fillDefaultData(Context<?> context);

}
