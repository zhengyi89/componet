package com.zbjdl.common.httpclient;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


/**
 * 
 * 
 * 实现 HttpClientWrapper 接口的类
 * 
 */
public class HttpClientUtil implements HttpClientWrapper {
	// 线程安全的 httpClient
	private HttpClient client = new HttpClient(
			new MultiThreadedHttpConnectionManager());

	// 线程安全的 MAP 把cookie 信息设置为全局 类似用浏览器 在每个请求之间保持cookie的传递
	private Map<String, String> cookies = new HashMap<String, String>();
	
	List<Header> headers = Collections.synchronizedList(new ArrayList<Header>());
	//来缓存协议头信息
	
	// 空构造的是不带认证的 HttpClient 操作
	public HttpClientUtil() {
		super();
	}

	// 带username 和password 参数的 需要认证的 不乱是basic 还是DIGEST （摘要） 认证都是一样
	// 这是httpclient 封装方便的之处
	public HttpClientUtil(String userName, String passWord) {
		client.getState().setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(userName, passWord));
	}

	/**
	 * 清除cookie 信息的方法实现
	 */
	@Override
	public void clearCookie() {
		client.getState().clearCookies();
	}

	/**
	 * 保证每次的cookie信息都保持最新
	 * 
	 * @param cookies1
	 */

	private void setCookies(Cookie[] cookies1) {
		for (Cookie cookie : cookies1) {
			this.cookies.put(cookie.getName(), cookie.getValue());
		}
	}

	@Override
	public String doRequest(MethodType method, String url,
			Map<String, String> params, String charset) throws HttpException,
			IOException {
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		doRequest(new HttpResponseCallBack() {
			@Override
			public void processResponse(InputStream in) throws IOException {
				int b = -1;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				out.flush();
				out.close();
				in.close();
			}
		}, method, url, params, charset);

		return out.toString(charset);
	}

	/**
	 * 关闭请求过程中的网路连接
	 * 
	 * @param method
	 */
	private void closeConnection(HttpMethod method) {
		method.releaseConnection();
	}

	/***
	 * 处理返回流的请求 主要是针对下载的情况 利用的回调的形式在资源关闭之前 让用户操作流
	 * 
	 */
	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, Map<String, String> params, String charset)
			throws HttpException, IOException {

		HttpMethod httpMethod = null;
		switch (method) {
		// 处理get请求
		case GET:
			httpMethod = this.doGet(url, params, charset);
			break;

		// 处理post请求
		case POST:
			httpMethod = this.doPost(url, params, charset);
			break;
		// 处理option请求
		case OPTION:
			httpMethod = this.doOption(url, params, charset);
			break;
		// 处理put请求
		case PUT:
			httpMethod = this.doPut(url, params, charset);
			break;
		case TRACE:
			httpMethod = this.doTrace(url, params, charset);
			break;
		case DELETE:
			httpMethod = this.doDelete(url, params, charset);
			break;
		default:

		}
		InputStream is = httpMethod.getResponseBodyAsStream();
		callback.processResponse(is);
		is.close();
		this.closeConnection(httpMethod);
	}

	@Override
	public String doRequest(MethodType method, String url, String charset)
			throws HttpException, IOException {

		return this.doRequest(method, url, null, charset);
	}

	@Override
	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, String charset) throws HttpException, IOException {

		this.doRequest(callback, method, url, null, charset);
	}

	/**
	 * 主要是设置些 http 头的信息 由于对http 头的了解是一件比较难的事 因此在这预先设置好 http 头的信息
	 * 
	 * @return list
	 */
	private List<Header> getHeaders() {
       
		headers.add(new Header("Accept-Language", "zh-CN"));
		headers.add(new Header("Accept-Encoding", " gzip, deflate"));
		//headers.add(new Header("If-Modified-Since",
				//"Thu, 29 Jul 2004 02:24:49 GMT"));
		//headers.add(new Header("If-None-Match", "'3014d-1d31-41085ff1'"));
		headers
				.add(new Header(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash,"
								+ " application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
		headers
				.add(new Header("User-Agent",
						" Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.2)"));
		headers.add(new Header("Connection", " Keep-Alive"));

		return headers;
	}
	@Override
	public void addHttpHeader(Map<String, String> headers1) {
		 headers = getHeaders();
  		 Set<String> names = headers1.keySet();
  		 for(String name : names){
  			for(Header header:headers){
  				if(header.getName().equals(name)){
  					header.setValue(headers1.get(name));
  				}
  			}
  			headers.add(new Header(name,headers1.get(name)));
  		 }
	}

	/**
	 * post 请求时 把传进来的参数 进行封装下
	 * 
	 * @param params
	 * @return NameValuePair[]
	 */
	private NameValuePair[] postParams(Map<String, String> params) {
		if (params == null || params.isEmpty())
			return new NameValuePair[0];

		Set<String> paramNames = params.keySet();
		int i = 0;
		NameValuePair[] nameValuePairs = new NameValuePair[paramNames.size()];
		for (String paramName : paramNames) {
			i++;
			NameValuePair nameValuePair = new NameValuePair(paramName, params
					.get(paramName));
			nameValuePairs[i - 1] = nameValuePair;
		}

		return nameValuePairs;
	}

	/***
	 * get 请求时 如果带有参数 则用这方法 生成带参数的url
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private String createNewUrl(String url, Map<String, String> params) {

		if (params == null || params.isEmpty())
			return url;

		Set<String> names = params.keySet();
		int i = 0;
		for (String name : names) {
			i++;
			String value = params.get(name);
			if (i == 1) {
				url += "?" + name + "=" + value;
			} else {
				url += "&" + name + "=" + value;
			}
		}
		return url;
	}

	/**
	 * 把map对象的的 cookie 信息变成 http 协议头里符合cookie的字符串
	 * 
	 * @param cookies
	 * @return String
	 */
	private String cookieStr(Map<String, String> cookies) {
		if (cookies == null || cookies.isEmpty())
			return "";

		String cookieStr = "";
		int i = 0;
		Set<String> cookieNames = cookies.keySet();
		for (String cookie : cookieNames) {
			i++;
			if (i == 1) {
				cookieStr = cookieStr + cookie + "=" + cookies.get(cookie);
			} else {
				cookieStr = cookieStr + ";" + cookie + "="
						+ cookies.get(cookie);
			}
		}
		return cookieStr;
	}

	/**
	 * 处理get 请求
	 * 
	 * @param url
	 * @param params
	 * @param cookies
	 * @param charset
	 * @return HttpMethod 主要是考虑到结果集有多种
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doGet(String url, Map<String, String> params,
			String charset) throws HttpException, IOException {
		// 构造新的url
		String newUrl = createNewUrl(url, params);

		// 构造get 请求
		GetMethod get = new GetMethod(newUrl);
		// 设置cookie信息
		get.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		get.setRequestHeader("Cookie", this.cookieStr(cookies));
		// 设置协议头的参数的编码集
		client.getParams().setContentCharset(charset);
		// 设置协议头
		client.getHostConfiguration().getParams().setParameter(
				"http.default-headers", headers);

		// 发送get请求 异常直接抛出
		executeHttpMethod(get);
		// System.out.println(get.getResponseBodyAsStream());
		return get;
	}

	/**
	 * post 请求 由于post和put 请求HttpClient 不支持自动重定向 所以我们的手动的重定向的页面
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return HttpMethod
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doPost(String url, Map<String, String> params,
			String charset) throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		// 设置协议头的参数的编码集
		client.getParams().setContentCharset(charset);
		// 设置协议头
		client.getHostConfiguration().getParams().setParameter(
				"http.default-headers", headers);
		// 设置post请求的的参数
		post.setRequestBody(this.postParams(params));
		// 设置cookie信息
		post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		post.setRequestHeader("Cookie", this.cookieStr(cookies));
		int status = executeHttpMethod(post);
		if (isRedirected(status)) {

			// 获取返回过来的 URL信息
			Header locationHeader = post.getResponseHeader("location");
			// 由于 HttpClient 的post put 的请求不支持 自动重定向 所以我们要自己 手动重定向
			// 而get 等其他的 都是自动重定向
			String newUrl = "";
			if (locationHeader != null) {
				// 从协议头里获取需要重定向的url
				newUrl = locationHeader.getValue();

			} else {
				// 如果没有则返回默认的页面
				newUrl = "/";
			}
			// 构造get请求 并传入newurl
			GetMethod get = new GetMethod(newUrl);
			// 发送get 请求 手动重定向
			get.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			get.setRequestHeader("Cookie", this.cookieStr(cookies));
			executeHttpMethod(get);
			return get;
		} else {
			return post;
		}

	}

	/**
	 * 处理是否重定向
	 * 
	 * @param status
	 * @return
	 */
	private boolean isRedirected(int status) {
		return status == HttpStatus.SC_MOVED_TEMPORARILY
				|| status == HttpStatus.SC_MOVED_PERMANENTLY
				|| status == HttpStatus.SC_TEMPORARY_REDIRECT
				|| status == HttpStatus.SC_USE_PROXY
				|| status == HttpStatus.SC_NOT_MODIFIED
				|| status == HttpStatus.SC_SEE_OTHER;
	}

	/**
	 * 处理method请求 处理完之后把cookie信息都更新一遍 保持在各个请求之间和服务器的cookie信息一致
	 * 
	 * @param httpMethod
	 * @return status
	 * @throws HttpException
	 * @throws IOException
	 */
	private int executeHttpMethod(HttpMethod httpMethod) throws HttpException,
			IOException {
		int status = this.client.executeMethod(httpMethod);
		this.setCookies(client.getState().getCookies());
		return status;
	}

	/**
	 * 处理option 请求
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doOption(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * 处理delete 请求
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doDelete(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * 处理put 请求
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doPut(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * 处理trace 请求
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doTrace(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	public void addCookie(Cookie cookie) {
		client.getState().addCookie(cookie);
		Cookie[] cookies = client.getState().getCookies();
		this.setCookies(cookies);
	}

	public void addCookies(Cookie[] cookies1) {
		client.getState().addCookies(cookies1);
		Cookie[] cookies = client.getState().getCookies();
		this.setCookies(cookies);
	}

	/**
	 * Sets the socket timeout (<tt>SO_TIMEOUT</tt>) in milliseconds which is the
	 * timeout for waiting for data
	 *
	 * @param newTimeoutInMilliseconds Timeout in milliseconds
	 */
	public void setTimeout(int newTimeoutInMilliseconds) {
		client.getHttpConnectionManager().getParams().setSoTimeout(newTimeoutInMilliseconds);
	}

	/**
	 * Sets the timeout until a connection is etablished
	 *
	 * @param newTimeoutInMilliseconds Timeout in milliseconds
	 */
	public void setConnectionTimeout(int newTimeoutInMilliseconds) {
		client.getHttpConnectionManager().getParams().setConnectionTimeout(newTimeoutInMilliseconds);
	}

}