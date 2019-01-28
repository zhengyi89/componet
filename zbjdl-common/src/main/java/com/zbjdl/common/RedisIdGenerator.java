package com.zbjdl.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zbjdl.date.HengbaoDateUtil;
import com.zbjdl.utils.HengBaoUtil;
@Component("redisIdGenerator")
public class RedisIdGenerator {
	
	private final String ID_FORMAT="000000";
	private final String BATCH_FORMAT = "00000";
//	private final String ACCOUNTING_KEY_PREFIX = "ACCOUNTING";
//	private final String REQUEST_KEY_PREFIX = "REQUEST";
//	private final String GATEWAY_KEY_PREFIX = "GATEWAY";
	
//	private  final String  KAIHU_KEY_PREFIX="KAIHU";
//	private  final String  FANGKUAN_KEY_PREFIX="FANGKUAN";
//	private  final String  HUANKUAN_KEY_PREFIX="HUANKUAN";
//	private  final String  TIXIAN_KEY_PREFIX="TIXIAN";
 	private  final String  CONTRACT_KEY_PREFIX="CONTRACT";
//	private  final String  COUPON_KEY_PREFIX="COUPON";
//	
//	private  final String  KAIHU_FORMAT="00000";
//	private  final String  FANGKUAN_FORMAT="00000";
//	private  final String  HUANKUAN_FORMAT="00000";
//	private  final String  TIXIAN_FORMAT="00000";
 	private  final String  CONTRACT_FORMAT="00000";
//	private  final String  COUPON_FORMAT="00000";
 	
	private final String THREE_FORMAT = "000";
	private final String FOUR_FORMAT="0000";
	private final String FIVE_FORMAT = "00000";
	private  final String  SIX_FORMAT="000000";
	private  final String  SEVEN_FORMAT="0000000";
	
	private final String ACCOUNT_KEY_PREFIX = "ACCOUNT";
	private final String ACCOUNTING_KEY_PREFIX = "ACCOUNTING";
	private final String REQUEST_KEY_PREFIX = "REQUEST";
	private final String GATEWAY_KEY_PREFIX = "GATEWAY";
	private final String BATCH_KEY_PREFIX = "BATCH";
	private final String FLOW_KEY_PREFIX = "FLOW";
	private final String ORDER_KEY_PREFIX = "ORDER";
	private final String PAY_KEY_PREFIX = "PAY";
	private final String PAY_KEY_PREFIX_16 = "PAY16";
	private final String DRAWING = "DRAW";
	private final String MERCHANT = "MER";
	private final String REFUND = "RE";
	
	private final String BOC_GATEWAY_KEY_PREFIX = "BOC_GATEWAY";
	private final String BOC_REFUND_KEY_PREFIX = " BOC_REFUND";
	
	private final String CMBC_BATCH_GATEWAY_KEY_PREFIX = "CMBC_BATCH_GATEWAY";
	
	
	//恒丰相关的：
	private  final  String PLAT_CODE = "1014";
	
	private  final String SZPA_GATEWAY = "SZPA_GATEWAY";
	
	
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	public String createAccountingNo(){
		Date date = new Date();
		String minuteStr = HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(ACCOUNTING_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(ACCOUNTING_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number,ID_FORMAT);
	}
	
	
	public String createCouponId(){
		Date date = new Date();
		String minuteStr = HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(ACCOUNTING_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(ACCOUNTING_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number,ID_FORMAT);
	}
	
	/**
	 * 中国银行6位支付请求流水号(超过后重复)
	 * @return
	 */
	public String createBocGatewayNo(){
		Date date = new Date();
		String dayStr = HengbaoDateUtil. getDayTime(date);
		Long number = redisTemplate.opsForValue().increment(BOC_GATEWAY_KEY_PREFIX + dayStr, 1l);
		redisTemplate.expire(BOC_GATEWAY_KEY_PREFIX + dayStr, 1, TimeUnit.DAYS);
		
		return dayStr + HengBaoUtil.formatDecimal(number,SIX_FORMAT);
	}
	
	/**
	 * 中国银行6位退款流水号(超过后会重复)
	 */
	public String createBocRefundNo(){
		Date date = new Date();
		String dayStr = HengbaoDateUtil. getDayTime(date);
		Long number = redisTemplate.opsForValue().increment(BOC_REFUND_KEY_PREFIX + dayStr, 1l);
		redisTemplate.expire(BOC_REFUND_KEY_PREFIX + dayStr, 1, TimeUnit.DAYS);
		
		return dayStr + HengBaoUtil.formatDecimal(number,SIX_FORMAT);
	}
	
	/**
	 * 民生银行每天三位批次号（超过后会重复）
	 * @return
	 */
	public String createCMBCBatchGatewayNo(){
		Date date = new Date();
		String dayStr = HengbaoDateUtil. getDayTime(date);
		Long number = redisTemplate.opsForValue().increment(CMBC_BATCH_GATEWAY_KEY_PREFIX + dayStr, 1l);
		redisTemplate.expire(CMBC_BATCH_GATEWAY_KEY_PREFIX + dayStr, 1, TimeUnit.DAYS);
		
		return dayStr + HengBaoUtil.formatDecimal(number,THREE_FORMAT);
	}
	
	/*
	*//**
	 * 单笔请求号
	 * @return
	 *//*
	public String createRequestNo(){
		Date date = new Date();
		String secondStr = HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(REQUEST_KEY_PREFIX + secondStr, 1l);
		redisTemplate.expire(REQUEST_KEY_PREFIX + secondStr, 1, TimeUnit.SECONDS);
		
		return secondStr + HengBaoUtil.formatDecimal(number,NO_FORMAT);
	}
	
	*//**
	 * 批次请求号
	 * @return
	 *//*
	public String createBatchRequestNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(REQUEST_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(REQUEST_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number,BATCH_FORMAT);
	}
	
	*//**
	 * 网关单笔请求号
	 * @return
	 *//*
	public String createGwPayFlowNo(){
		Date date = new Date();
		String secondStr= HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(GATEWAY_KEY_PREFIX + secondStr, 1l);
		redisTemplate.expire(GATEWAY_KEY_PREFIX + secondStr, number, TimeUnit.SECONDS);
		String gwPayFlowNo = secondStr + HengBaoUtil.formatDecimal(number,NO_FORMAT);
		return gwPayFlowNo;
	}
	*/
	/**
	 * 网关批次请求号
	 * @return
	 */
	public String createGwBatchPayNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(GATEWAY_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(GATEWAY_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number,BATCH_FORMAT);
	}
//	
//	/**
//	 * 提现流水号
//	 * @return
//	 */
//	public String tiXianFlowNo(){
//		Date date = new Date();
//		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
//		Long number = redisTemplate.opsForValue().increment(TIXIAN_KEY_PREFIX + minuteStr, 1l);
//		redisTemplate.expire(TIXIAN_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
//		
//		return minuteStr + HengBaoUtil.formatDecimal(number,TIXIAN_FORMAT);
//	}
//	/**
//	 * 放款流水号
//	 * @return
//	 */
//	public String fangKuanFlowNo(){
//		Date date = new Date();
//		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
//		Long number = redisTemplate.opsForValue().increment(FANGKUAN_KEY_PREFIX + minuteStr, 1l);
//		redisTemplate.expire(FANGKUAN_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
//		
//		return minuteStr + HengBaoUtil.formatDecimal(number,FANGKUAN_FORMAT);
//	}
//	/**
//	 * 还款流水号
//	 * @return
//	 */
//	public String huanKuanFlowNo(){
//		Date date = new Date();
//		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
//		Long number = redisTemplate.opsForValue().increment(HUANKUAN_KEY_PREFIX + minuteStr, 1l);
//		redisTemplate.expire(HUANKUAN_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
//		
//		return minuteStr + HengBaoUtil.formatDecimal(number,HUANKUAN_FORMAT);
//	}
//	/**
//	 * 开户流水号
//	 * @return
//	 */
//	public String kaiHuFlowNo(){
//		Date date = new Date();
//		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
//		Long number = redisTemplate.opsForValue().increment(KAIHU_KEY_PREFIX + minuteStr, 1l);
//		redisTemplate.expire(KAIHU_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
//		
//		return minuteStr + HengBaoUtil.formatDecimal(number,KAIHU_FORMAT);
//	}
	/**
	 * 开户流水号
	 * @return
	 */
	public String  contractNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment( CONTRACT_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire( CONTRACT_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number, CONTRACT_FORMAT);
	}
//	
//	/**
//	 * 红包交易流水号
//	 * @return
//	 */
//	public String couponFlowNo(){
//		Date date = new Date();
//		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
//		Long number = redisTemplate.opsForValue().increment( COUPON_KEY_PREFIX + minuteStr, 1l);
//		redisTemplate.expire( COUPON_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
//		
//		return minuteStr + HengBaoUtil.formatDecimal(number, COUPON_FORMAT);
//	}
	
	
	/**
	 * 生成账户号(每秒钟十万)
	 * @return
	 */
	public String createAccountId(){
		Date date = new Date();
		String secondStr = HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(ACCOUNT_KEY_PREFIX + secondStr, 1l);
		redisTemplate.expire(ACCOUNT_KEY_PREFIX + secondStr, 1, TimeUnit.SECONDS);
		
		return secondStr + HengBaoUtil.formatDecimal(number, FOUR_FORMAT);
	}
	
	/**
	 * 生成16位序号
	 * @return
	 */
	public String createPay16Id(){
		Date date = new Date();
		String secondStr = HengbaoDateUtil.getSecondTime(date).substring(2);
		Long number = redisTemplate.opsForValue().increment(PAY_KEY_PREFIX_16 + secondStr, 1l);
		redisTemplate.expire(PAY_KEY_PREFIX_16 + secondStr, 1, TimeUnit.SECONDS);
		
		return secondStr + HengBaoUtil.formatDecimal(number, FOUR_FORMAT);
	}
	
	/**
	 * 单笔请求号(每分钟百万)
	 * @return
	 */
	public String createRequestNo(){
		Date date = new Date();
		String minuteStr = HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(REQUEST_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(REQUEST_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number, FIVE_FORMAT);
	}
	
	/**
	 * 批次请求号(每分钟十万)
	 * @return
	 */
	public String createBatchRequestNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(BATCH_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(BATCH_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return minuteStr + HengBaoUtil.formatDecimal(number, FIVE_FORMAT);
	}
	
	
	/**
	 * 批次支付号(每秒钟一万)
	 * @return
	 */
	public String createBatchPayNo(){
		Date date = new Date();
		String secondStr= HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(GATEWAY_KEY_PREFIX + secondStr, 1l);
		redisTemplate.expire(GATEWAY_KEY_PREFIX + secondStr, 5, TimeUnit.SECONDS);
		
		return "b" +  secondStr + HengBaoUtil.formatDecimal(number, FOUR_FORMAT);
	}
	
	/**
	 * 网关单笔请求号（每秒钟十万）
	 * @return
	 */
	public String createGwPayFlowNo(){
		Date date = new Date();
		String secondStr= HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(GATEWAY_KEY_PREFIX + secondStr, 1l);
		redisTemplate.expire(GATEWAY_KEY_PREFIX + secondStr, 5, TimeUnit.SECONDS);
		
		return "gw" +  secondStr + HengBaoUtil.formatDecimal(number, FIVE_FORMAT);
	}
	
	/**
	 * 订单流水号
	 * @return
	 */
	public String createOrderNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(ORDER_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(ORDER_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return "o" + minuteStr + HengBaoUtil.formatDecimal(number,FIVE_FORMAT);
	}
	
	/**
	 * 交易流水号
	 * @return
	 */
	public String createTransFlowNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(FLOW_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(FLOW_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return "t" + minuteStr + HengBaoUtil.formatDecimal(number,SEVEN_FORMAT);
	}
	
	
	/**
	 * 支付流水号
	 * @return
	 */
	public String createPayFlowNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(PAY_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(PAY_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return "p" + minuteStr + HengBaoUtil.formatDecimal(number,SIX_FORMAT);
	}
	public String createCmbcOutPayFileNo() {
		return "";
	}
	
	public String createCmbcInnerPayFileNo() {
		return "";
	}
	/**
	 * 付款文件支付订单号
	 * @return
	 */
	public String createPayOrderNo()
	{
		Date date = new Date();
		String minuteStr = new DateTime(date).toString("YYMMdd");
		Long number = redisTemplate.opsForValue().increment(PAY_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(PAY_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return  minuteStr + HengBaoUtil.formatDecimal(number,FOUR_FORMAT);
	}
	
	/**
	 * 提款流水号
	 * @return
	 */
	public String createDrawingNo()
	{
		Date date = new Date();
		String minuteStr = HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(DRAWING + minuteStr, 1l);
		redisTemplate.expire(DRAWING + minuteStr, 1, TimeUnit.MINUTES);
		
		return  minuteStr + HengBaoUtil.formatDecimal(number,FOUR_FORMAT);
	}
	/**
	 * 提款流水号
	 * @return
	 */
	public String createMerchantCode()
	{
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(MERCHANT + minuteStr, 1l);
		redisTemplate.expire(MERCHANT + minuteStr, 1, TimeUnit.MINUTES);
		
		return  MERCHANT + minuteStr;
	}
	/**
	 * 退款流水号
	 * @return
	 */
	public String createRefundFlowNo(){
		Date date = new Date();
		String minuteStr= HengbaoDateUtil.getMinuteTime(date);
		Long number = redisTemplate.opsForValue().increment(FLOW_KEY_PREFIX + minuteStr, 1l);
		redisTemplate.expire(FLOW_KEY_PREFIX + minuteStr, 1, TimeUnit.MINUTES);
		
		return REFUND + minuteStr + HengBaoUtil.formatDecimal(number,SEVEN_FORMAT);
	}
	
	/**
	 * 生成恒丰的批次号，对应batch_payment_info表中的 batch_request_no
	 * @return
	 */
	public  String createHfBatchNo(){
		Date date = new Date();
		String dayStr = new DateTime(date).toString("YYMMdd");
		Long number = redisTemplate.opsForValue().increment(dayStr + PLAT_CODE, 1l);
		redisTemplate.expire(dayStr + PLAT_CODE, 1, TimeUnit.DAYS);
		return  dayStr + PLAT_CODE + HengBaoUtil.formatDecimal(number,SIX_FORMAT) + new DateTime(date).toString("mmss");
	}
	
	public String createSzpaGatewayNo(){
		Date date = new Date();
		String secondStr = HengbaoDateUtil.getSecondTime(date);
		Long number = redisTemplate.opsForValue().increment(SZPA_GATEWAY + secondStr, 1l);
		redisTemplate.expire(SZPA_GATEWAY + secondStr, 1, TimeUnit.SECONDS);
		return secondStr + HengBaoUtil.formatDecimal(number, SIX_FORMAT);
	}
}
