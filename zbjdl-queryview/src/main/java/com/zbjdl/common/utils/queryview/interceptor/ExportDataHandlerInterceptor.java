/**
 * Copyright: Copyright (c)2018
 * Company: 八戒财云
 */
package com.zbjdl.common.utils.queryview.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zbjdl.common.utils.config.TextResource;
import com.zbjdl.common.utils.queryview.QueryForm;
import com.zbjdl.common.utils.queryview.QueryViewApplicationContextHelper;
import com.zbjdl.common.utils.queryview.tags.bean.PreparedParamBean;
import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryService;

import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 拦截/exportExcel的url，根据查询页面组件的参数，获取数据，写入Excel文件中，通过response输出到浏览器
 * @author MengXiangChao
 * @date 2016年4月21日 下午3:15:50
 */
public class ExportDataHandlerInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportDataHandlerInterceptor.class);

	private static String requestMethod = "GET";

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getRequestURI().toString().indexOf("/exportExcel") != -1) {
			this.doExport(request, response, handler);
			return false;
		}

		// Only requestMethod allow to init query content
		if (!request.getMethod().equalsIgnoreCase(requestMethod) && !"ALL".equals(requestMethod)) {
			return true;
		}

		// TODO 暂不支持
		if (request.getRequestURI().endsWith(".query")) {
			LOGGER.debug("根据 queryKey 查找是否配置了查询语句并封装返回结果，请求参数：{}", request.getParameterNames());
			return false;
		}

		return super.preHandle(request, response, handler);
	}

	public void doExport(HttpServletRequest request, HttpServletResponse response, Object handler) {
		LOGGER.info("进入导出数据方法");
		String titleRelation = request.getParameter("titleRelation").substring(0,
				request.getParameter("titleRelation").length() - 1);
		String[] tr = titleRelation.split(",");
		if(tr.length<1){//表头对应关系不够
			LOGGER.info("表头对应关系不够");
			return;
		}
		System.out.println(titleRelation);
		String showValueRelation = request.getParameter("showValueRelation");
		String[] valueIndex = null;
		if(!"".equals(showValueRelation)){
			showValueRelation = showValueRelation.substring(0,
					request.getParameter("showValueRelation").length() - 1);
			valueIndex = showValueRelation.split(",");
		}
		String formId = request.getParameter("formId");
		String queryKey = request.getParameter("queryKey");

		Map<String, Object> httpParams = new HashMap<String, Object>();
		String params = request.getParameter("httpParams");
		
		params = params.substring(1, params.length() - 1);

		for (int i = 0; i < params.split(",").length; i++) {
			String key = params.split(",")[i].split("=")[0].toString().trim();

			String value = "";
			if (params.split(",")[i].split("=").length > 1) {
				value = params.split(",")[i].split("=")[1].toString();
			}
			httpParams.put(key, value);
		}

		// 增加prepareParam
		String prepareParams  = request.getParameter("preparedParamsStr");
		prepareParams = prepareParams.substring(1, prepareParams.length() - 1);
		
		for (int i = 0; i < prepareParams.split(",").length; i++) {
			String key = prepareParams.split(",")[i].split("=")[0].toString().trim();

			String value = "";
			if (prepareParams.split(",")[i].split("=").length > 1) {
				value = prepareParams.split(",")[i].split("=")[1].toString();
			}
			httpParams.put(key, value);
		}

		// 写入文件
		long starTime = System.currentTimeMillis();
		// 文件存放路径
		String baseDir = System.getProperty("user.dir") + File.separator + "exportFile";
		if (!new File(baseDir).exists()) {
			LOGGER.info(baseDir + "不存在");
			new File(baseDir).mkdir();
			LOGGER.info(baseDir + "创建完成");
		} else {
			LOGGER.info(baseDir + "目录存在");
		}
		String fileDir = baseDir + File.separator + queryKey;
		if (!new File(fileDir).exists()) {
			LOGGER.info(baseDir + "目录下" + queryKey + "不存在");
			new File(fileDir).mkdir();
			LOGGER.info(baseDir + "目录下" + queryKey + "创建完成");
		} else {
			LOGGER.info(baseDir + "目录下" + queryKey + "目录存在");
		}
		String timeDir = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fileName = queryKey + "_" + timeDir + ".xls";
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

		try {
			WritableWorkbook book = Workbook.createWorkbook(new File(fileDir + File.separator + fileName));
			
			
			ApplicationContext applicationContext = QueryViewApplicationContextHelper.getContext();
			QueryService service = (QueryService) applicationContext.getBean("queryService");
			QueryForm queryForm = QueryForm.buildForm(formId);
			QueryParam queryParam = queryForm.createQueryParm();
			
			int step = 50000;
			int mid = 1;
			for(int L  = 1;L< 101;L++){
				queryParam.setStartIndex(mid);
				queryParam.setMaxSize((L * step) +(L-1));
				queryParam.setParams(httpParams);
				List result = service.queryList(queryKey, queryParam);
				if(result.size() == 0){
					LOGGER.info("循环输出sheet"+L);
					break;
				}
//				LOGGER.info("KKKKKKKKKKKKKKKKKKKKK:"+result.size());
				mid = (L * step) +(L-1)+1;
				WritableSheet sheetL = book.createSheet("Sheet"+L, 0);
				// 设置单元格的样式
				WritableCellFormat cellFormat = new WritableCellFormat();
				// 设置水平居中
				cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
				// 设置垂直居中
				cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				// 设置自动换行
				cellFormat.setWrap(true);
				// 设置显示的字体样式，字体，字号，是否粗体，字体颜色
				cellFormat.setFont(new WritableFont(WritableFont.createFont("Arial"), 12, WritableFont.BOLD, false,
						UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
				
				
				// 循环生成第一行表头和第二行index
				Map<String, Integer> indexSign = new HashMap<>();// 每个字段所在的列位置
				Map<String, String> valueSign = new HashMap<>();// 字段值需要转换的位置
				
				for (int i = 0; i < tr.length; i++) {
					
					String indexCont = tr[i].split(":")[0].toString();
					if (!indexCont.equals("null")) {
						Label label = new Label(i, 1, indexCont);
//					sheet1.addCell(label);
						indexSign.put(indexCont.trim(), i);
						String cont = tr[i].split(":")[1].toString();
						Label label2 = new Label(i, 0, cont, cellFormat);
						sheetL.addCell(label2);
						sheetL.setColumnView(i, 20);
					}
				}
				for (int j = 0; valueIndex!=null && j<valueIndex.length;j++) {
					String valueCont = valueIndex[j].split(":")[0].toString();
					if(!valueCont.equals("null")){
						String value = valueIndex[j].split(":")[1].toString();
						valueSign.put(valueCont.trim(), value);
					}
				}
				// 循环解析数据放入匹配的单元格
				for (int r = 0; r < result.size(); r++) {// 每一条数据
					Map<String, Object> data = (Map<String, Object>) result.get(r);
					Iterator<String> iter = indexSign.keySet().iterator();
					while (iter.hasNext()) {// 索引map
						int c = 0;
						String key = iter.next();
						Label dataLabel = null;
						Number number1 = null;
						DateTime dateTime = null;
						Iterator<String> strIndexIte = data.keySet().iterator();
						while (strIndexIte.hasNext()) {// 每个字段
							String strIndex = strIndexIte.next();
							if (key.equals(strIndex)) {
								c = indexSign.get(key);
								Object clazzNameIndex = null;
								clazzNameIndex = data.get(strIndex);
								if (clazzNameIndex instanceof BigDecimal) {// 判断BigDecimal类型组装相应的单元格
									double doValue = new BigDecimal(clazzNameIndex.toString()).doubleValue();
									number1 = new Number(c, r + 1, doValue);
									sheetL.addCell(number1);
								}else if (clazzNameIndex instanceof String) {// 判断String类型组装相应的单元格
									String strValue = null;
									if(valueSign.get(strIndex)!=null){
										//根据转换index，拿到需要展示的值
										String showValueIndex = valueSign.get(strIndex);
										TextResource textResource = new TextResource();
										strValue = textResource.getSysText(showValueIndex, clazzNameIndex.toString());
										
									}else{
										strValue = clazzNameIndex.toString();
									}
									dataLabel = new Label(c, r + 1, strValue);
									sheetL.addCell(dataLabel);
								}else if(clazzNameIndex instanceof java.sql.Timestamp){
									Date dateValue = dateTimeFormat.parse(clazzNameIndex.toString());
//									dateTime = new DateTime(c, r + 1, dateValue);
									dataLabel = new Label(c, r + 1, dateTimeFormat.format(dateValue));
									sheetL.addCell(dataLabel);
								}else if(clazzNameIndex instanceof java.sql.Date){
//								System.out.println(clazzNameIndex);
									Date dateValue = dateFormat.parse(clazzNameIndex.toString());
									dataLabel = new Label(c, r + 1, dateFormat.format(dateValue));
									sheetL.addCell(dataLabel);
								}else if (clazzNameIndex instanceof Integer) {
									if(valueSign.get(strIndex)!=null){
										String strValue = null;
										//根据转换index，拿到需要展示的值
										String showValueIndex = valueSign.get(strIndex);
										TextResource textResource = new TextResource();
										strValue = textResource.getSysText(showValueIndex, clazzNameIndex.toString());
										dataLabel = new Label(c, r + 1, strValue);
										sheetL.addCell(dataLabel);
									}else{
										int doValue = new Integer(clazzNameIndex.toString());
										number1 = new Number(c, r + 1, doValue);
										sheetL.addCell(number1);
									}
								}else if (clazzNameIndex instanceof Long) {
									int doValue = new Integer(clazzNameIndex.toString());
									number1 = new Number(c, r + 1, doValue);
									sheetL.addCell(number1);
								}
							}
						}
					}
				}
			}
			
			
			
			book.write();
			book.close();
		} catch (IOException | JXLException e) {
			// TODO Auto-generated catch block
			LOGGER.info("" + e);
			e.printStackTrace();
		} catch (ParseException e) {
			LOGGER.info("" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		LOGGER.info("用时:" + (endTime - starTime));
		LOGGER.info("文件位置：" + fileDir + File.separator + fileName);
		// TODO 下载文件
		String filePath = fileDir + File.separator;// request.getSession().getServletContext().getRealPath("/")+
		LOGGER.info("INFO:--------文件的下载路径为：" + filePath + fileName);
		response.reset();// 先清空之前的缓存信息
		response.setContentType("application/vnd.ms-excel");// 确保文件是下载的方式而不是浏览器中直接打开
		try {
			// 弹出下载对话框
			// 为了解决浏览器弹出下载框时框中的中文名称显示问题需要转换编码。
			// 为了解决火狐浏览器中文空格问题需加上\"\"来解决
			response.addHeader("Content-Disposition",
					"attachment;filename=\"" + new String(fileName.getBytes("gbk"), "ISO-8859-1") + "\"");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			LOGGER.info("ERROR:--------文件下载方法中：文件名称编码异常！");
		}
		OutputStream os = null;
		FileInputStream fis = null;
		try {
			os = response.getOutputStream();
			// 文件名称需要正确的编码，否则涉及到中文时会乱码，导致无法下载
			// 项目中路径基本不用中文，所以不需要转码。
			fis = new FileInputStream(filePath + new String(fileName.getBytes("utf-8"), "utf-8"));
			byte[] buffer = new byte[1024 * 10];
			for (int read; (read = fis.read(buffer)) != -1;) {
				os.write(buffer, 0, read);
				os.flush();
			}
			fis.close();
			os.close();
			new File(filePath + new String(fileName.getBytes("utf-8"), "utf-8")).delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.info("ERROR:--------文件下载方法中：文件【" + filePath + File.separator + fileName + "】不存在！");
		} catch (Exception e) {
			// 如遇到ClientAbortException这种客户端异常，一般是因为IE浏览器下载过程中点了取消
			// 或者是连续点击下载导致的。该异常并不影响下载功能，所以捕获之后暂不做处理。
			e.printStackTrace();
			LOGGER.info("WARN:--------文件下载方法中：捕获到ClientAbortException异常！");
		}
	}
}
