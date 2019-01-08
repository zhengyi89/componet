package com.zbjdl.common.httpclient;

import java.io.IOException;
import java.io.InputStream;
/**
 * 
 * 回调接口
 */
public interface HttpResponseCallBack {

	public void processResponse(InputStream responseBody) throws IOException;
}
