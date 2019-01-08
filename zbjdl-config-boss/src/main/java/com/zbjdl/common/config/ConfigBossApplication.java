package com.zbjdl.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author yejiyong
 *
 */
@Controller
@SpringBootApplication
@ImportResource({"classpath:config-boss-spring/utils-boss-appContext.xml","classpath:config-boss-spring/config-boss-query.xml"})
public class ConfigBossApplication  {
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigBossApplication.class);
    
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ConfigBossApplication.class);
        springApplication.run(args);
        logger.info("Spring boot loaded");
	}
	@RequestMapping("/")
	public String index(){
		return "redirect:config/list";
	}
}
