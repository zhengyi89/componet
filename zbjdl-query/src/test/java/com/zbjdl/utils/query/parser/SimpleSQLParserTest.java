
package com.zbjdl.utils.query.parser;

import junit.framework.Assert;

import org.junit.Test;

import com.zbjdl.utils.query.parser.SimpleSQLParser;
import com.zbjdl.utils.query.parser.SimpleSQLStatement;

public class SimpleSQLParserTest {

	@Test
	public void test1() {
		String sql = "select a from tableb  where(1=1) and e in(select id from user where name=xx group by aa order by id)  group  by e  order by d with ur";
		SimpleSQLStatement statement = SimpleSQLParser.parse(sql);
		Assert.assertEquals("select a from tableb where(1=1) and e in(select id from user where name=xx group by aa order by id) group  by e order by d with ur", statement.getSQL());
		Assert.assertEquals("select a", statement.getSelect());
		Assert.assertEquals("from tableb", statement.getFrom());
		Assert.assertEquals("where(1=1) and e in(select id from user where name=xx group by aa order by id)", statement.getWhere());
		Assert.assertEquals("group  by e", statement.getGroupBy());
		Assert.assertEquals("order by d", statement.getOrderBy());
		Assert.assertEquals("with ur", statement.getWithUr());

		Assert.assertEquals("order by name desc,createtime asc,id desc", SimpleSQLParser.parseOrderBy("name,createtime,id", "false,true,false"));
	}

}
