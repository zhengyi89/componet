package com.zbjdl.utils;

public class MailUtil {

	/** 
     * 调用sendOut方法完成邮件发送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean send(String smtp,String from,String to,String subject,String content,String username,String password) {  
        Mail theMail = new Mail(smtp);
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
      
    /** 
     * 调用sendOut方法完成邮件发送,带抄送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param copyto 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean sendAndCc(String smtp,String from,String to,String copyto,String subject,String content,String username,String password) {  
        Mail theMail = new Mail(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setCopyTo(copyto)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
      
    /** 
     * 调用sendOut方法完成邮件发送,带附件 
     * @param smtp 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @param filename 附件路径 
     * @return 
     */  
    public static boolean send(String smtp,String from,String to,String subject,String content,String username,String password,String filename) {  
        Mail theMail = new Mail(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.addFileAffix(filename)) return false;   
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
      
    /** 
     * 调用sendOut方法完成邮件发送,带附件和抄送 
     * @param smtp 
     * @param from 
     * @param to 
     * @param copyto 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @param filename 
     * @return 
     */  
    public static boolean sendAndCc(String smtp,String from,String to,String copyto,String subject,String content,String username,String password,String filename) {  
        Mail theMail = new Mail(smtp);  
        theMail.setNeedAuth(true); //需要验证  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.addFileAffix(filename)) return false;   
        if(!theMail.setTo(to)) return false;  
        if(!theMail.setCopyTo(copyto)) return false;  
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    } 
}
