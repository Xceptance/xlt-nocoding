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
package com.xceptance.xlt.nocoding.util;

import java.net.URL;

import org.htmlunit.StringWebResponse;

public class MockWebResponse extends StringWebResponse
{

    private static final long serialVersionUID = -6805954103312051680L;

    private String contentType;

    private String content;

    public MockWebResponse(final String content, final URL url, final String contentType)
    {
        super(content, url);
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public String getContentAsString()
    {
        return content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }
}
