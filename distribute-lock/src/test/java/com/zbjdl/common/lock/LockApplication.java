package com.zbjdl.common.lock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath*:/testContext.xml")
public class LockApplication {

	public static void main(String[] args) {
		SpringApplication.run(LockApplication.class, args);
	}
}
