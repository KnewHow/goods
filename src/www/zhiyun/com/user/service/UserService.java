package www.zhiyun.com.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import www.zhiyun.com.user.dao.UserDao;
import www.zhiyun.com.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
/**
 * 业务层
 * @author 1
 *
 */
public class UserService {

	private UserDao userDao = new UserDao();
	
	/**
	 * ajax校验用户名
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		return userDao.ajaxValidateLoginname(loginname);
	}
	
	/**
	 * ajax校验邮箱
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		return userDao.ajaxValidateEmail(email);
	}
	
	/**
	 * 向数据库里面添加用户
	 * @param user
	 */
	public void regist(User user){
		/*1、补全数据
		 * 2、进行添加
		 * 3、发邮件
		 * 
		 */
		
		//补全数据,为什么要在service里面补全数据而不在servelt补全呢，
		//因为用户可能使用安卓客户端发送数据，这样就会跳过web层 
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//向数据库添加数据
		userDao.addUser(user);
		
		//发邮件
		sendMail(user);
		
	}
	
	
	/**
	 * 发邮件
	 */
	public void sendMail(User user){
		Properties props = new Properties();
		try {
			//读取配置文件里面的信息
			props.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
			
			String host = props.getProperty("host");//获取主机名
			String username = props.getProperty("username");//获取用户名
			String password = props.getProperty("password");//获取登录密码
			String from = props.getProperty("from");//获取发件人
			String to = user.getEmail();//获取收件人
			String subject = props.getProperty("subject");//获取主题
			String content = props.getProperty("content");//获取邮件内容
			
			//补全其激活码
			content = MessageFormat.format(content, user.getActivationCode());
			
			
			Session session = MailUtils.createSession(host, username, password);
			
			Mail mail = new Mail(from, to, subject, content);
			
			//发邮件
			
			MailUtils.send(session, mail);
			
			
			
		} catch (IOException e) {
			throw new RuntimeException();
		} catch (MessagingException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 对用户进行激活
	 * @param activationCode
	 * @throws UserException 
	 */
	public void activation(String activationCode) throws UserException{
		/*思路
		 * 1、根据activationCode来查找用户，如果用户为null，抛出异常，激活码无效
		 * 2、在判断用户的状态，如果已经激活，抛出异常，已经激活
		 * 3、如果未激活，让其激活
		 */
		User user = userDao.findUserByActivationCode(activationCode);
		if(user==null){//判断该用户是不是存在
			throw new UserException("激活码无效！");
		}
		if(user.getStatus()){//判断是否已经激活
			throw new UserException("您已经激活，请不要再次激活");
		}
		
		//如果确实未激活，就激活用户
		userDao.updateStatus(user.getUid(), true);
	}
	
	/**
	 * 登录业务
	 * @param user
	 * @return
	 * @throws UserException 
	 */
	public User login(User user) throws UserException{
		User _user = userDao.findByLoginname(user.getLoginname());
		//校验用户名
		if(_user==null) throw new UserException("用户名不存在");
		//校验是否激活
		if(!_user.getStatus()) throw new UserException("您还没有激活，无法登录");
		//校验密码是否正确
		if(!_user.getLoginpass().equals(user.getLoginpass())){
			throw new UserException("密码不正确");
		}
		
		return _user;
			
		
	}
	
	public void updateLoginpass(User form,String uid) throws UserException{
		User user = userDao.findByUid(uid);
		if(!user.getLoginpass().equals(form.getLoginpass())){//如果密码不一致，说明原密码不正确
			throw new UserException("原密码不正确");
		}
		//如果密码正确，就进行修改
		userDao.updateLoginpass(form.getNewLoginpass(), uid);
	}

	public User forgetPassword(String email) throws UserException {
		/*思路：
		 * 1、在数据库里面查询邮箱，如果不存在，抛异常
		 * 2、在根据邮箱把用户查找出来。
		 */
		int num = userDao.findCountByEmail(email);
		if(num==0){
			throw new UserException("该邮箱还没有被注册");
		}else{
			return userDao.findUserByEmail(email);
		}
	}
	
	/**
	 * 找回密码之修改密码
	 * @param uid
	 * @param newPassword
	 */
	public void findPassword(String uid,String newPassword){
		try {
			userDao.updatePassword(uid, newPassword);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
}
