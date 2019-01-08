
package com.zbjdl.common.mvel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.common.utils.CheckUtils;


public class MVELTemplateFactory {
	protected static Logger logger = LoggerFactory.getLogger(MVELTemplateFactory.class);
	public static final String TEMPLATE_ENCODING  = "UTF-8";
	public static final String TEMPLATE_SUFFIX = ".mvel";
	
	private Map<String, CompiledTemplate> templateCache = new HashMap<String, CompiledTemplate>();
	private String packageName;
	
	public static MVELTemplateFactory createClassPathTemplateFactory(String packageName){
		return new MVELTemplateFactory(packageName);
	}
	
	private MVELTemplateFactory(String _packageName){
		CheckUtils.notEmpty(_packageName, "_packageName");
		if(!_packageName.endsWith("/")){
			_packageName = _packageName+"/";
		}
		this.packageName = _packageName;
	}
	
	private static byte[] toByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte data[] = bytestream.toByteArray();
		bytestream.close();
		return data;
	}
	
	private CompiledTemplate getTemplate(String templateName){
		CompiledTemplate template = templateCache.get(templateName);
		if(template == null){
			template = compileTemplate(templateName);
			templateCache.put(templateName, template);
		}
		return template;
	}
	
	private CompiledTemplate compileTemplate(String templateName){
		String templateFileName = packageName+templateName+TEMPLATE_SUFFIX;
		try {
			InputStream templateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateFileName);
			String templateContent = new String(toByte(templateStream), TEMPLATE_ENCODING);
			return TemplateCompiler.compileTemplate(templateContent);
		} catch (Exception e) {
			logger.error("compile template fail : "+templateFileName +", message : "+e.getMessage());
			return null;
		}
	}
	
	public String evalTemplate(String templateName, Object ctx, Map vars){
		try{
			return TemplateRuntime.execute(getTemplate(templateName), ctx, vars).toString();
		}catch (Exception e) {
			logger.error("eval template fail : "+e.getMessage());
			return null;
		}
	}
}
