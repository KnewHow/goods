package www.zhiyun.com.user.web.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;


import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class TraveldreamUtils {

	/**
	 * 获取四位验证码
	 * @return
	 */
	public static String getCode(){
		String code="";
		for(int i=0;i<4;i++){
			code = code+(int)(Math.random()*10);
		}
		return code;
	}
	
	public void sendMail(String subject,String content,String to){
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("baseEmail.properties"));
		} catch (IOException e) {
			throw new RuntimeException("发邮件工具错误1");
		}
		
		String host = props.getProperty("host");//获取主机名
		String from = props.getProperty("from");//获取发件人
		String username = props.getProperty("username");//获取用户名
		String password = props.getProperty("password");//获取密码
		//获取发邮件的session
		Session session = MailUtils.createSession(host, username, password);
		
		Mail mail = new Mail(from, to, subject, content);
//System.out.println(host+","+from+","+username+","+password+","+to+","+content+","+subject);
		try {
			//发邮件
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException("发邮件工具错误2");
		} catch (IOException e) {
			throw new RuntimeException("发邮件工具错误3");
		}
	}
	
}
