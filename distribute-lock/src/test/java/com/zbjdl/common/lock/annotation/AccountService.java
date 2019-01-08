package com.zbjdl.common.lock.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zbjdl.common.lock.annotation.DistributeLock;

public class AccountService {
	private static final Log LOG = LogFactory.getLog(AccountService.class);

	@DistributeLock(key = "\"TransInfo_\"+args[0].accountNo",acquireTimeoutMillis = 5000 , expiryTimeMillis = 5000)
	public void updateBalance(TransInfo transInfo) {
		LOG.info("update balance for account=" + transInfo.getAccountNo()
				+ ",amount=" + transInfo.getAmount());
	}
	@DistributeLock(key = "args[0].orderNo",acquireTimeoutMillis = 3000 , expiryTimeMillis = 5000)
	public void updateTransInfo(TransInfo transInfo) {
		LOG.info("update transInfo for account=" + transInfo.getAccountNo()
				+ ",amount=" + transInfo.getAmount());
	}

}
