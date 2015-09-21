package stubble.core;

/**
 * Copyright (c) 2012-2013 Edgar Espina
 * <p>
 * This file is part of Handlebars.java.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.jknack.handlebars.io.URLTemplateLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;


/**
 * Loads the handlebar templates from the file system.
 *
 * @author sai
 * @since 0.1.0
 */
public class FileTemplateLoader extends URLTemplateLoader {

    public FileTemplateLoader(final String templatesDir) {
        setPrefix(templatesDir);
        setSuffix(".hbs");
    }

    @Override
    protected URL getResource(final String location) {
        try {
            return Paths.get(location).toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

