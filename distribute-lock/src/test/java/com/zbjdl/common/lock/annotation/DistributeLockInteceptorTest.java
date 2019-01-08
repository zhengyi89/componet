package com.zbjdl.common.lock.annotation;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zbjdl.common.lock.LockApplication;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LockApplication.class)
public class DistributeLockInteceptorTest {
	@Autowired
	private AccountService accountService;
	
	@Test
	public void testUpdateBalance() throws InterruptedException {
		ScheduledExecutorService scheduledService = Executors
				.newScheduledThreadPool(10);
		for (int i = 0; i < 10; i++) {
			scheduledService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					String accountNo="Account_"+(new Random().nextInt(3));
					TransInfo transInfo = new TransInfo();
					transInfo.setAccountNo(accountNo);
					transInfo.setAmount(5.78);
					try {
						accountService.updateBalance(transInfo);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 1, 2, TimeUnit.SECONDS);
		}
		Thread.sleep(300000);
		scheduledService.shutdown();
	}

}
