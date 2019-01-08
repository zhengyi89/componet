//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class StringFtlTemplateLoader implements TemplateLoader {
    private Map templates = new HashMap();

    public StringFtlTemplateLoader() {
    }

    public void addTemplate(String name, String template) {
        if (!this.templates.containsKey(name)) {
            this.templates.put(name, template);
        }

    }

    public void closeTemplateSource(Object templateSource) throws IOException {
    }

    public Object findTemplateSource(String name) throws IOException {
        return this.templates.get(name);
    }

    public long getLastModified(Object templateSource) {
        return 0L;
    }

    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return new StringReader((String)templateSource);
    }
}
