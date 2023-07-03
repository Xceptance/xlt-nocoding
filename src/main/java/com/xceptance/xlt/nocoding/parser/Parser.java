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
package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.xceptance.xlt.nocoding.command.Command;

/**
 * Defines the behavior of all parsers.
 *
 * @author ckeiner
 */
public interface Parser
{
    /**
     * Parses the content of the file at <code>pathToFile</code> to a list of {@link Command}s
     *
     * @param pathToFile
     *            The String that describes the path to the file
     * @return The list of {@link Command}s that is described in the file
     * @throws IOException
     */
    public default List<Command> parse(final String pathToFile) throws IOException
    {
        return parse(createReader(pathToFile));
    }

    /**
     * Parses the content of the <code>reader</code> to a list of {@link Command}s
     *
     * @param reader
     *            The Reader that contains {@link Command}s in a parser dependent format
     * @return The list of {@link Command}s that is described in the reader
     * @throws IOException
     */
    public abstract List<Command> parse(final Reader reader) throws IOException;

    /**
     * Returns all acceptable extensions for this Parser
     *
     * @return A list of extensions that are associated with this Parser
     */
    public abstract List<String> getExtensions();

    /**
     * Creates a {@link Reader} based on <code>pathToFile</code>. First, it tries to create a {@link FileReader} by
     * creating a file. If a {@link FileNotFoundException} is encountered, it creates a {@link URL} from the
     * <code>pathToFile</code> and creates a {@link InputStreamReader} based on {@link URL#openStream()}.
     *
     * @param pathToFile
     *            The path to the file
     * @return A <code>Reader</code> with the information of the file
     * @throws IOException
     *             If the <code>FileReader</code> cannot be created or <code>Url.openStream()</code> fails to open the
     *             stream.
     */
    public default Reader createReader(final String pathToFile) throws IOException
    {
        Reader fileReader = null;
        // Create a file from the path
        final File file = new File(pathToFile);
        // Check if the file exists
        if (file.exists())
        {
            // If the file exists, create a FileReader
            fileReader = new FileReader(file);
        }
        // If the file does not exist
        else
        {
            // Try to create a URL out of pathToFile and create a InputStreamReader for it
            try
            {
                fileReader = new InputStreamReader(new URL(pathToFile).openStream());
            }
            // If the pathToFile is a MalformedURL throw an error
            catch (final MalformedURLException e)
            {
                throw new FileNotFoundException("Neither File nor URL found for " + pathToFile + ", because " + e.getMessage());
            }
        }
        // Return the created fileReader
        return fileReader;
    }
}
