package scater.common;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class SendMail {

	public SendMail() {  
    }  
  
    public static void main(String[] args) throws MessagingException {  
        sendText("905033683@qq.com",6);  
        System.out.println("发送邮件成功！");  
    }  
  
    public static void sendText(String emailAddress,int id) throws MessagingException {  
        SimpleEmail email = new SimpleEmail();  
        email.setTLS(true);  
        email.setHostName("smtp.163.com");  
        email.setAuthentication("hehuihuifly@163.com", "jswh123@"); // 用户名和密码  
        try {  
            email.addTo(emailAddress); // 接收方  
            email.setFrom("hehuihuifly@163.com"); // 发送方  
            email.setSubject("测试邮件"); // 标题  
            email.setCharset("UTF-8");   
            email.buildMimeMessage();   
            //设置内容的字符集为UTF-8,先buildMimeMessage才能设置内容文本   
            email.getMimeMessage().setText("请点击该链接进行激活页面http://localhost:8080/CookBookDesign/active.html?id="+id,"UTF-8");   
            email.sendMimeMessage();     
        } catch (EmailException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    public static void sendPassword(String emailAddress,String password) throws MessagingException{
    	SimpleEmail email = new SimpleEmail();  
        email.setTLS(true);  
        email.setHostName("smtp.163.com");  
        email.setAuthentication("hehuihuifly@163.com", "jswh123@"); // 用户名和密码  
        try {  
            email.addTo(emailAddress); // 接收方  
            email.setFrom("hehuihuifly@163.com"); // 发送方  
            email.setSubject("测试邮件"); // 标题  
            email.setCharset("UTF-8");   
            email.buildMimeMessage();   
            //设置内容的字符集为UTF-8,先buildMimeMessage才能设置内容文本   
            email.getMimeMessage().setText("您的密码是："+password,"UTF-8");   
            email.sendMimeMessage();     
        } catch (EmailException e) {  
            e.printStackTrace();  
        } 
    }
}
