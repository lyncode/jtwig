/**
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

package com.lyncode.jtwig;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.FileJtwigResource;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.StringJtwigResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

public class JtwigTemplate {
    public static JtwigTemplate fromString (String template) {
        return new JtwigTemplate(template, new JtwigConfiguration());
    }

    private final JtwigResource resource;
    private final JtwigConfiguration configuration;

    public JtwigTemplate(JtwigResource resource, JtwigConfiguration configuration) {
        this.resource = resource;
        this.configuration = configuration;
    }

    public JtwigTemplate(String content, JtwigConfiguration configuration) {
        this(new StringJtwigResource(content), configuration);
    }

    public JtwigTemplate(File file, JtwigConfiguration configuration) {
        this(new FileJtwigResource(file), configuration);
    }

    public void output (OutputStream outputStream, JtwigModelMap modelMap) throws ParseException, CompileException, RenderException {
        JtwigParser parser = new JtwigParser(configuration.parse());
        CompileContext compileContext = new CompileContext(resource, parser, configuration.compile());
        RenderContext renderContext = RenderContext.create(configuration.render(), modelMap, outputStream);

        parser
                .parse(resource)
                .compile(compileContext)
                .render(renderContext);
    }

    public String output(JtwigModelMap modelMap) throws ParseException, CompileException, RenderException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        output(outputStream, modelMap);
        return outputStream.toString();
    }

    public Renderable compile() throws ParseException, CompileException {
        JtwigParser parser = new JtwigParser(configuration.parse());
        return parser.parse(resource)
                .compile(new CompileContext(resource, parser, configuration.compile()));
    }
}
