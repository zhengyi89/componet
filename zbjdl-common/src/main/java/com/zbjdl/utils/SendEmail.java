package com.zbjdl.utils;
//package com.hengbao.utils;
//
//import java.util.Date;
//
//import com.hengbao.date.HengbaoDateUtil;
//import com.sendEmail.Service;
//import com.sendEmail.ServiceSoap;
//
//public class SendEmail {
//	
//	public static boolean sendEmail(String toEmail,String subject,String msg,String fromEmail,String password) {
//		boolean success = true;
//		Service service = new Service();
//		ServiceSoap soap = service.getServiceSoap();
//		String emailMsg = soap.sendMail(toEmail, subject, msg, fromEmail,password, "", "", true);
//		if (!emailMsg.equals("1")) {
//			success = false;
//		}
//		return success;
//	}
//	
//	public static void main(String[] args) {
//		StringBuilder msg = new StringBuilder("<b>亲爱的用户：</b><br/>您好！您在");
//		msg.append(HengbaoDateUtil.getTimeWithUnit(new Date()));
//		msg.append("提交了用户注册信息，请点击下面的链接激活注册信息。");
//		String url = "www.baidu.com";
//		msg.append("<a href=\"http://www.baidu.com\">");
//		msg.append(url);
//		msg.append("</a>");
//		msg.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问) 为了保证您帐号的安全，该链接有效期为24小时，并且点击一次后失效！ ");
//		String subject = "-用户注册";
//		sendEmail("@163.com", subject, msg.toString(),"kf@hengbao.com","123zxc");
//	}
//}
