package com.zbjdl.common.utils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 生成唯一序号的辅助工具类
 */
public class UIDGenerator {
	
	public static final int NUMBER_LENGTH_SIX = 6;
	public static final int NUMBER_LENGTH_SEVEN = 7;
	public static final int NUMBER_LENGTH_EIGHT = 8;
	
	private static final int[] DEFAULT_CONFOUNDER = {3, 6, 7, 1, 8, 9, 5, 2};
	private static final int[] SIX_CONFOUNDER = {1,3,5,7,2,9,4,6,8};
	private static final int[] SEVEN_CONFOUNDER = {6, 7, 1, 8, 9, 5, 2};
	
	private static final String NUMBER_FORMAT_SIX="000000";
	private static final String NUMBER_FORMAT_SEVEN="0000000";
	private static final String NUMBER_FORMAT_EIGHT="00000000";
	/**
	 * 获取UUID
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	/**
	 * 把一个输入整数置换为另一个数
	 * @param num
	 * @return
	 */
	private static long confuse(long num, int[] confounder) {
		String tempStr = num+"";
		int length = confounder.length;
		int numLength = tempStr.length();
		
		//检查输入数值是否过大
		if(length<numLength){
			throw new RuntimeException("confounder length must greater then number length, "+length +" : "+ numLength);
		}
		
		String output = "";
		char[] input = tempStr.toCharArray();
		
		int confounderIndex = Integer.parseInt(input[input.length-1]+"")%length;
		int paddingLength = length-numLength;
		
		for(int i=0; i<paddingLength; i++){
			confounderIndex = (confounderIndex+1)%length;
			output = output+(confounder[confounderIndex]%10);
		}
		
		for(int i=0; i<numLength; i++){
			confounderIndex = (confounderIndex+1)%length;
			output = output+((Integer.parseInt(input[i]+"")+confounder[confounderIndex])%10);
		}
		try{
			return Long.parseLong(output);
		}catch(Exception e){
			throw new RuntimeException("confuse number overflow : "+output);
		}
    }
	/**
	 * 把一个输入整数置换为另一个数
	 * @param num
	 * @return
	 */
	private static long confuse(long num,int length, int[] confounder) {
		String tempStr = num+"";
		int numLength = tempStr.length();
		
		//检查输入数值是否过大
		if(length<numLength){
			throw new RuntimeException("confounder length must greater then number length, "+length +" : "+ numLength);
		}
		
		String output = "";
		char[] input = tempStr.toCharArray();
		
		int confounderIndex = Integer.parseInt(input[input.length-1]+"")%length;
		int paddingLength = length-numLength;
		
		for(int i=0; i<paddingLength; i++){
			confounderIndex = (confounderIndex+1)%8;
			output = output+(confounder[confounderIndex]%10);
		}
		
		for(int i=0; i<numLength; i++){
			confounderIndex = (confounderIndex+1)%8;
			output = output+((Integer.parseInt(input[i]+"")+confounder[confounderIndex])%10);
		}
		try{
			return Long.parseLong(output);
		}catch(Exception e){
			throw new RuntimeException("confuse number overflow : "+output);
		}
	}
	
	
	/**
	 * 生成业务流水号
	 * @param sequence 序列号
	 * @param bizFlag 业务标识
	 * @return
	 */
	public static String generateBizUID(long sequence, String bizFlag){
		return generateBizUID(sequence, bizFlag, new Date());
	}
	
	/**
	 * 生成业务流水号
	 * @param sequence 序列号
	 * @param bizFlag 业务标识
	 * @param date 业务发生时间
	 * @return
	 */
	public static String generateBizUID(long sequence, String bizFlag, Date date){

		String dateStr = FormatUtils.formatDate(date, "yyMMdd", null);
		return bizFlag+dateStr+confuse(sequence,DEFAULT_CONFOUNDER);
	}
	
	/**
	 * 根据传入的参数的不同，生成不同的序列号。
	 * 生成的字符串格格式（顺序）为： bizFlag+date+8位数字
	 *  
	 * @param sequence		序列号
	 * 							传入的序列号最大为8位，自增 ID 	
	 * @param numberLength	组成UID的数字部分的长度
	 * @param bizFlag 		业务标识，可以为空
	 * @param date 			业务发生时间
	 * 							可以为空。如果date为空，则生成的序列号中包含时间字符串，时间字符串的格式为“dateFormatPattern”。
	 * @param dateFormatPattern	时间格式字符串，可以为空，默认为"yyMMdd"格式
	 * @param isConfused	是否对生成的序列号进行混淆 
	 * 					
	 * @return
	 */
	public static String next(long sequence,int numberLength, String bizFlag, Date date,String dateFormatPattern ,boolean isConfused){
		StringBuffer buffer = new StringBuffer();
		if(!CheckUtils.isEmpty(bizFlag)){
			buffer.append(bizFlag);
		}
		if(date!=null){
			if(CheckUtils.isEmpty(dateFormatPattern)){
				dateFormatPattern = "yyMMdd";
			}
			String dateStr = FormatUtils.formatDate(date, dateFormatPattern, null);
			buffer.append(dateStr);
		}
		if(isConfused){
			long newSequence = 0;
			String numberFarmat = NUMBER_FORMAT_EIGHT;
			if(numberLength == UIDGenerator.NUMBER_LENGTH_EIGHT){
				newSequence = confuse(sequence,DEFAULT_CONFOUNDER);
				numberFarmat = NUMBER_FORMAT_EIGHT;
			}else if(numberLength == UIDGenerator.NUMBER_LENGTH_SEVEN){
				newSequence = confuse(sequence,SEVEN_CONFOUNDER);
				numberFarmat = NUMBER_FORMAT_SEVEN;

			}else if(numberLength == UIDGenerator.NUMBER_LENGTH_SIX){
				newSequence = confuse(sequence,6,SIX_CONFOUNDER);
				numberFarmat = NUMBER_FORMAT_SIX;
			}else{
				newSequence = confuse(sequence,DEFAULT_CONFOUNDER);
			}
			String sequenceStr = String.valueOf(newSequence);
			//为了保证固定长度,如果混淆后的数字位数少于DEFAULT_CONFOUNDER.length，则需要在前面补足
			if(numberLength>sequenceStr.length()){
				sequenceStr = formatDecimal(newSequence,numberFarmat);
			}
			buffer.append(sequenceStr);
		}else{
			buffer.append(sequence);
		}
		return buffer.toString();
	}

	/**
	 * 将传入的数字置换为另一个数字
	 * @param sequence
	 * @return
	 */
	public static String next(long sequence) {
		long newSequence = 0;
		String numberFarmat = NUMBER_FORMAT_EIGHT;

		newSequence = confuse(sequence, DEFAULT_CONFOUNDER);
		String sequenceStr = String.valueOf(newSequence);
		// 为了保证固定长度,如果混淆后的数字位数少于DEFAULT_CONFOUNDER.length，则需要在前面补足
		if (NUMBER_LENGTH_EIGHT > sequenceStr.length()) {
			sequenceStr = formatDecimal(newSequence, numberFarmat);
		}
		return sequenceStr;
	}
	/**
	 * 将数字格式化成固定长度的字符串
	 * @param decimal
	 * @param format
	 * @return
	 */
	private static final String formatDecimal(Long decimal, String format){
		DecimalFormat df = new DecimalFormat(format); 
	    return df.format(decimal);
	}
	
}