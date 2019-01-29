package test.common;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.zbjdl.date.ZbjdlDateUtil;


public class MainTest {
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/applicationContext-common.xml");
		context.start();
		
		RedisTemplate redisTemplate = (RedisTemplate)context.getBean("redisTemplate");
		
		ValueOperations<String, String> valueops = redisTemplate
				.opsForValue();
		valueops.set("test", "hahaha");
		
//		redisTemplate.opsForHash().put("mapkey", "key1", 1);
//		redisTemplate.opsForHash().put("mapkey", "key2", 2);
		
		Date now = new Date();
//		Thread.sleep(1000);
		Date now1 = new Date();
		
		System.out.println(ZbjdlDateUtil.compareDateTime(now1, now));
		
		logger.info("记录日志测试");
	}
}
