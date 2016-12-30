package goods_test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.junit.Test;

import www.zhiyun.com.user.domain.User;
import www.zhiyun.com.user.service.UserService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class Test_1 {

	private UserService userService = new UserService();
	@Test
	public void fun1(){
		System.out.println(CommonUtils.uuid().length());
		System.out.println(CommonUtils.uuid());
	}
	
	@Test
	public void fun2(){
		UserService userService = new UserService();
		boolean bool = userService.ajaxValidateEmail("948@qq.com");
		System.out.println(bool);
	}
	
	@Test
	public void fun3() throws IOException, MessagingException{
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
		String host = props.getProperty("host");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		System.out.println(props.getProperty("host"));
		Session session = MailUtils.createSession(host, username, password);
		
		String from = props.getProperty("from");
		String to ="948170910@qq.com";
		String subject = props.getProperty("subject");
		String content = props.getProperty("content");
		content = MessageFormat.format(content, CommonUtils.uuid()+CommonUtils.uuid());
		Mail mail = new Mail(from, to, subject, content);
		MailUtils.send(session, mail);
	}
	
	@Test
	public void fun4() throws IOException, MessagingException{
//		Properties props = new Properties();
//		props.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
		String host = "smtp.163.com";
		String username = "18326600931";
		String password = "ygh19950315";
//		System.out.println(props.getProperty("host"));
		Session session = MailUtils.createSession(host, username, password);
		
		String from = "18326600931@163.com";
		String to ="948170910@qq.com";
		String subject = "这是一封测试邮件";
		String content ="这是一封测试邮件";
//		content = MessageFormat.format(content, CommonUtils.uuid()+CommonUtils.uuid());
		Mail mail = new Mail(from, to, subject, content);
		MailUtils.send(session, mail);
	}
	
	@Test
	public void fun5() throws MessagingException, IOException{
		//获取session类
				Session session = MailUtils.createSession("smtp.163.com",
						"18326600931", "ygh19950315");
				Mail mail = new Mail("18326600931@163.com",
						"948170910@qq.com","不是垃圾邮件能是什么呢？", "这里是正文");
				
				MailUtils.send(session, mail);
	}
	
	@Test
	public void fun6(){
		User user = new User();
		user.setEmail("948170910@qq.com");
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		userService.sendMail(user);
	}
	
	@Test
	public void fun7(){
		boolean bool = (false) & (false);
		System.out.println(bool);
	}
}
