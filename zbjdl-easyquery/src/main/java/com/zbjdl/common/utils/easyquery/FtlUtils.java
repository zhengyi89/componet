//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.views.freemarker.ScopesHashModel;

public class FtlUtils {
    private static Configuration fileFtlcfg = new Configuration();
    private static Configuration stringFtlCfg = new Configuration();
    private static StringFtlTemplateLoader stringTemplateLoader = new StringFtlTemplateLoader();
    private static ClassTemplateLoader classTemplateLoader = new ClassTemplateLoader(FtlUtils.class, "/easyquerytemplate");
    private static String DEFAULT_ENCODING = "utf-8";
    private static Map defaultRootContext = new HashMap();
    private static Map<String, StringBuffer> staticResources = new HashMap();

    public FtlUtils() {
    }

    static String generateHtml(Map root, String templateName) throws IOException, TemplateException {
        String templatePath = templateName + ".ftl";
        Template template = fileFtlcfg.getTemplate(templatePath);
        Writer out = new CharArrayWriter();
        root.putAll(defaultRootContext);
        template.process(root, out);
        String html = out.toString();
        return html;
    }

    public static String generateStaticHtml(String fileName) throws IOException {
        String templatePath = "easyquerytemplate/" + fileName;
        if (staticResources.containsKey(templatePath)) {
            return ((StringBuffer)staticResources.get(templatePath)).toString();
        } else {
            InputStream template = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
            Reader reader = new InputStreamReader(template, "utf-8");
            StringBuffer sb = new StringBuffer();

            try {
                BufferedReader br = new BufferedReader(reader);
                String tmp = null;

                while((tmp = br.readLine()) != null) {
                    sb.append(tmp);
                    sb.append("\r");
                }

                staticResources.put(templatePath, sb);
                String var7 = sb.toString();
                return var7;
            } finally {
                IOUtils.closeQuietly(template);
                IOUtils.closeQuietly(reader);
            }
        }
    }

    public static String parseFtl(ScopesHashModel root, String str) {
        stringTemplateLoader.addTemplate(str.hashCode() + "", str);

        try {
            Template template = stringFtlCfg.getTemplate(str.hashCode() + "");
            Writer out = new CharArrayWriter();
            root.putAll(defaultRootContext);
            template.process(root, out);
            return out.toString();
        } catch (Exception var4) {
            throw new EasyQueryException("parseFtl fail!", var4);
        }
    }

    public static void main(String[] args) throws IOException, TemplateException {
        Template template = fileFtlcfg.getTemplate("a.ftl");
        StringWriter writer = new StringWriter();
        Map root = new HashMap();
        root.putAll(defaultRootContext);
        root.put("user", "xxxx");
        template.process(root, writer);
        System.out.println(writer.toString());
    }

    static {
        fileFtlcfg.setDefaultEncoding(DEFAULT_ENCODING);
        fileFtlcfg.setEncoding(Locale.CHINA, DEFAULT_ENCODING);
        fileFtlcfg.setTemplateLoader(classTemplateLoader);
        fileFtlcfg.setNumberFormat("0");
        stringFtlCfg.setDefaultEncoding(DEFAULT_ENCODING);
        stringFtlCfg.setEncoding(Locale.CHINA, DEFAULT_ENCODING);
        stringFtlCfg.setTemplateLoader(stringTemplateLoader);
        stringFtlCfg.setNumberFormat("0");
        BeansWrapper beansWrapper = BeansWrapper.getDefaultInstance();
        defaultRootContext.put("statics", beansWrapper.getStaticModels());
        defaultRootContext.put("enums", beansWrapper.getEnumModels());
    }
}
