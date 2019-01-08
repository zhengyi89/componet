
package com.zbjdl.utils.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zbjdl.utils.query.QueryParam;
import com.zbjdl.utils.query.QueryResult;
import com.zbjdl.utils.query.QueryService;

/**    
 */
public class QueryTest {

	@Test
	public void test1() throws ParseException {
		ApplicationContext context = new ClassPathXmlApplicationContext("queryContext.xml");
		QueryService service = (QueryService)context.getBean("queryService");
		
		Map map = new HashMap();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = format.parse("2011-12-02 00:00:00");
		Date endDate = format.parse("2011-12-09 00:00:00");
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		QueryParam qp = new QueryParam();
		qp.setCurrentPage(1);
		qp.setMaxSize(2);
		qp.setStartIndex(0);
		qp.setParams(map);
		
		QueryResult result = service.query("queryTestWithNoPage", qp);
		System.out.println("total: " + result.getTotalCount());
		System.out.println("datas: " + result.getData());
		
		QueryParam qp2 = new QueryParam();
		qp2.setCurrentPage(2);
		qp2.setMaxSize(2);
		qp2.setStartIndex(0);
		qp2.setParams(map);
		
		QueryResult result2 = service.query("queryTestWithNoPage", qp2);
		System.out.println("total2: " + result2.getTotalCount());
		System.out.println("datas2: " + result2.getData());
	}
}
