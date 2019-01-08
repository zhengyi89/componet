
package com.zbjdl.utils.query.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zbjdl.common.utils.CheckUtils;
import com.zbjdl.common.utils.CollectionUtils;
import com.zbjdl.utils.query.QueryException;
import com.zbjdl.utils.query.SQLParamConverter;
import com.zbjdl.utils.query.utils.OgnlUtils;

/**
 * SQL表达式的解析工具，把查询组件中配置的SQL表达式转换成标准SQL语句
 */
public class SQLExperssionParser {

	private final static Logger LOGGER = LoggerFactory.getLogger(SQLExperssionParser.class);

	/**
	 * 用来匹配类似 /~ xxx ~/ 条件语句的正则表达式
	 */
	private final static String QUERYCONDITION_EXP = "/~([\\s\\S]*?)~/";

	/**
	 * 参数名称，只能由数字、字母、下划线、小数点组成
	 */
	private final static String PARAM_EXP = "[0-9,a-z,A-Z,_,\\.,\\,]+";

	/**
	 * 匹配静态参数的正则表达式
	 */
	private final static String CONSTANTPARAM_EXP = "\\["+PARAM_EXP+"\\]";

	/**
	 * 匹配动态参数的正则表达式
	 */
	private final static String VARIABLEPARAM_EXP = "\\{"+PARAM_EXP+"\\}";

	public final static Pattern sqlInjectionParrern = Pattern.compile("['\";]|(--)|\\|\\||select\\s.+\\sinto.+|update\\s.+\\sset.+|delete\\s+from.+|(ascii|chr|cast|concast|row|substr|md5|bitandnot|bitor|bitxor|bitnot|xquery|xmlquery|xmltable|xmlexists|xmlagg|xmlrow|xml2clob|xmelement)\\(.*\\)|(sysibm|syscat|sysibmadm)\\.");

	/**
	 * 解析语句中所有/~expression~/条件表达式
	 *
	 * @param sqlExp
	 * @param context
	 * @return
	 */
	private static SQLExperssionParseResult parseConditionExpression(String sqlExp, Map context) {
		Pattern p = Pattern.compile(QUERYCONDITION_EXP);
		Matcher m = p.matcher(sqlExp);
		String result = sqlExp;
		boolean hasConditions = false;
		int i=0;
		while(m.find()){
			String expression = m.group();
			String conditonStr = parseSingleConditionExpression(expression, context);
			if(conditonStr == null){
				result = m.replaceFirst("");
			}else{
				conditonStr = fillBlank(result, m, conditonStr);
				result = m.replaceFirst(conditonStr);
				hasConditions = true;
			}
        	m = p.matcher(result);
        	if(i++>9999){
	        	throw new RuntimeException("parse expression fail, exp : "+ sqlExp);
	        }
        }
		if(result.contains("/~") || result.contains("~/")){
			throw new QueryException("unrecognized expression : " + result);
		}
		return new SQLExperssionParseResult(result, hasConditions);
	}

	/**
	 * 判断是否需要给每个条件表达式前面加上空格
	 * @param sqlExp
	 * @param m
	 * @param conditonStr
	 * @return
	 */
	private static String fillBlank(String sqlExp, Matcher m, String conditonStr){
		int startIndex = m.start();
		int endIndex = m.end();
		boolean preBlank = false;
		boolean postBlank = false;
		if(startIndex>0){
			String preLetter = sqlExp.substring(startIndex-1, startIndex);
			preBlank = (" ".equals(preLetter) || "(".equals(preLetter) || conditonStr.startsWith(" ") || conditonStr.startsWith("("));
		}else{
			preBlank = true;
		}
		if(!preBlank){
			conditonStr = " "+conditonStr;
		}
		if(endIndex< sqlExp.length()){
			String postLetter = sqlExp.substring(endIndex, endIndex+1);
			postBlank = (" ".equals(postLetter) || "(".equals(postLetter) || conditonStr.endsWith(" ") || conditonStr.endsWith(")"));
		}else{
			postBlank = true;
		}
		if(!postBlank){
			conditonStr = conditonStr + " ";
		}
		return conditonStr;
	}

	/**
	 * 解析单个/~expression~/条件表达式
	 * @param expression
	 * @param context
	 * @return
	 */
	private static String parseSingleConditionExpression(String expression, Map context){
		String result = expression.substring(2, expression.length() - 2);
		//找出条件表达式
		int index = result.indexOf(":");
		if(index<=0){
			throw new QueryException("invalid sql expression : "+ expression);
		}
		String conditionChoiceExp = result.substring(0, index);
		conditionChoiceExp = conditionChoiceExp.trim();
		Object flag = OgnlUtils.executeExpression(conditionChoiceExp, context);
		if(CheckUtils.isEmpty(flag) || Boolean.FALSE.equals(flag)){
			return null;
		}
		result = result.substring(index+1);
		return result;
	}

	/**
	 * 把所有[paramName]格式的参数替换成实际参数值
	 * @param sqlExp
	 * @param context
	 */
	private static String parseConstantParamExpression(String sqlExp, Map context){
		Pattern p = Pattern.compile(CONSTANTPARAM_EXP);
		Matcher m = p.matcher(sqlExp);
	    String result = sqlExp;
	    int i=0;
	    List<Integer> paramIndex = new ArrayList<Integer>();
	    List<String> paramValues = new ArrayList<String>();
	    while(m.find()){
	    	//截取参数表达式
		    String paramStr = m.group();
		    paramStr = paramStr.substring(1, paramStr.length()-1);

		    //转换参数对象
		    Object value = parseParam(paramStr, context, true);

	    	//把对象转换为字符串
	    	String constantParamValue = SQLParamConverter.convertToString(value);
	    	int tmpIndex = m.start();
	    	paramIndex.add(tmpIndex);
	    	paramValues.add(constantParamValue);
	    	result = m.replaceFirst("");
	        m = p.matcher(result);
	        if(i++>9999){
	        	throw new RuntimeException("parse expression fail, sqlExp : "+ sqlExp);
	        }
	    }

	    int inc_index = 0;
	    for(int j=0; j<paramIndex.size(); j++){
	    	Integer index = paramIndex.get(j);
	    	String value = paramValues.get(j);
	    	result = result.substring(0, index+inc_index) + value + result.substring(index+inc_index);
	    	inc_index += value.length();
	    }
	    return result;
	}

	/**
	 * 根据 paramStr 从 context 中获取变量
	 *
	 * @param paramStr
	 * @param context
	 * @param isStaticArgs 是否为静态参数
	 * @return
	 */
	private static Object parseParam(String paramStr, Map context, boolean isStaticArgs){
		 //动态参数可以是{paramName,paramType}这样的格式，后面一个用来表示参数类型
	    String[] args = paramStr.split(",");
	    String paramName = args[0];
	    String paramType = null;
	    if(args.length>1){
			paramType = args[1];
		}
		boolean enabledFilter = true;
		if (args.length>2) {
			enabledFilter = !"disabled".equals(args[2]);
		}

	    Object value =  OgnlUtils.executeExpression(paramName, context);
	    if(value==null){
	    	throw new QueryException("param not found : ["+ paramName+"]");
	    }

	    if(value instanceof String){
			String valueStr = value.toString();

			// 防注入
			if (valueStr.indexOf(paramName + ":@") == 0) {
				valueStr = valueStr.substring(paramName.length() + 2);
				Matcher m = sqlInjectionParrern.matcher(valueStr.toLowerCase());
				if (m.find() && enabledFilter && isStaticArgs) {
					LOGGER.warn("注入信息：paramStr:{}, value:{}", paramStr, valueStr);
					throw new QueryException("请求参数中包含非法字符串：" + m.group());
				}
			}

			if (null != paramType && !"".equals(paramType.trim())) {
				//根据指定的类型进行转换
				paramType = paramType.toLowerCase();
				if(paramType.endsWith("array")){
					paramType = paramType.substring(0, paramType.length()-5);
					value = SQLParamConverter.convertArray(paramType, valueStr);
				}else{
					value = SQLParamConverter.convert(paramType, valueStr);
				}
			}
		}
	    return value;
	}

	/**
	 * 把所有{paramName}格式的参数替换成?占位符，并且把参数值加入sqlParams
	 *
	 * @param exp
	 * @param context
	 * @param sqlParams
	 */
	private static String parseVariableParamExpression(String exp, Map context, List<Object> sqlParams){
		Pattern p = Pattern.compile(VARIABLEPARAM_EXP);
		Matcher m = p.matcher(exp);
		String result = exp;
		int i=0;
		while(m.find()){
			//截取参数表达式
		    String paramStr = m.group();
		    paramStr = paramStr.substring(1, paramStr.length()-1);

		    //转换参数对象
		    Object value = parseParam(paramStr, context, false);

		    //把参数放入参数列表
		    if(CollectionUtils.isCollection(value)){
		    	List list = CollectionUtils.toList(value);
		    	String tmp = "";
		    	for(Object obj : list){
		    		sqlParams.add(obj);
		    		tmp += "?,";
		    	}
		    	if(tmp.length()==0){
		    		throw new QueryException("array param must not empty, paramName["+paramStr+"]");
		    	}
		    	tmp = tmp.substring(0, tmp.length()-1);
		    	tmp = "("+tmp+")";
		    	result = m.replaceFirst(tmp);
		    }
		    else{
		    	sqlParams.add(value);
				result = m.replaceFirst("?");
		    }

		    m = p.matcher(result);
		    if(i++>9999){
	        	throw new RuntimeException("parse expression fail, sqlExp : "+exp);
	        }
		}
		return result;
	}

	/**
	 * 解析整个SQL表达式，将其转换为一个标准的SQL语句
	 *
	 * @param sqlExp
	 * @param context
	 * @param parseVariable
	 * @return
	 */
	public static SQLExperssionParseResult parseSQLExpression(String sqlExp, Map context, boolean parseVariable){
		//把回车、换行、制表符都替换成空格
		sqlExp = sqlExp.replaceAll("\t|\r|\n", " ");
//		sqlExp = sqlExp.replaceAll("\\s", " ");

		//解析所有的条件表达式
		SQLExperssionParseResult parseResult = parseConditionExpression(sqlExp, context);

		//解析静态参数
		sqlExp = parseConstantParamExpression(parseResult.getSql(), context);

		List<Object> sqlParams = new ArrayList<Object>();
		//解析动态参数
		if(parseVariable){
			sqlExp = parseVariableParamExpression(sqlExp, context, sqlParams);
		}
		parseResult.setSql(sqlExp);
		parseResult.setSqlParams(sqlParams);
		return parseResult;
	}

}
