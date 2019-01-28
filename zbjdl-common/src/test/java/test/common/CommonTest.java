package test.common;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zbjdl.cache.KeyValuePair;
import com.zbjdl.cache.RedisCache;
import com.zbjdl.common.RedisAccountNoCreator;
import com.zbjdl.common.RedisIdGenerator;
import com.zbjdl.common.RedisLock;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:common/applicationContext-common.xml"})
public class CommonTest {
	
	class Handler implements Runnable{

		public Handler(){
			
		}
		
		@Override
		public void run() {
			for(int i=0 ;i <1000; i++){
				String reqNo = redisIdGenerator.createRequestNo();
				if(!set.contains(reqNo)){
					set.put(reqNo, reqNo);
//					System.out.println(reqNo);
				}else{
					System.out.println("error"+reqNo);
				}
		}
	}
	}
	
	ConcurrentHashMap<String,Object> set = new ConcurrentHashMap<String,Object>();
	
	@Autowired
	private RedisAccountNoCreator accountNoCreator;
	
	@Autowired
	private RedisCache redisCache;
	
	@Autowired
	private RedisLock redisLock;
	
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	
	@Test
	public void testCreateRequestNo(){
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		int i=0;
		while(i<10){
	        executorService.execute(new Handler()); 
	        i++;
		}
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(set.size());
	}
	
	@Test
	public void testRedisLock(){
		redisLock.lockResource("abc");
	}
	
	
	public void testAccountNoCreator(){
//		String accNo = accountNoCreator.createAccountNo("afdsas", "201023", "100");
//		System.out.println(accNo);
	}
	

	public void testRedisCacheSetDict(){
		List<KeyValuePair<String, Object>> cities = Lists.newArrayList();
		KeyValuePair<String, Object> city1 = new KeyValuePair<String, Object>("001","beijing");
		KeyValuePair<String, Object> city2 = new KeyValuePair<String, Object>("002","changchun");
		cities.add(city1);
		cities.add(city2);
		
		redisCache.setDictList("city", cities);
	}
	
	
	public void testRedisCacheGetDict(){
		List<KeyValuePair<String, Object>> cities = redisCache.getDictList("city");
		
		for(KeyValuePair<String, Object> city : cities){
			System.out.println(city.getKey() + city.getValue());
		}
	}
}
