package www.zhiyun.com.user.web.servlet;



import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import www.zhiyun.com.user.domain.User;
import www.zhiyun.com.user.service.UserException;
import www.zhiyun.com.user.service.UserService;

import com.sun.jndi.toolkit.url.Uri;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	
	private UserService userService = new UserService();
	
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 需要完成三个步骤
		 * 1、封装表单数据
		 * 2、进行表单校验，为什么要进行校验呢，因为一些不法分子，可以把页面下载到本地
		 * 		然后删除里面js，这样就会导致问题出错。
		 * 3、使用UserService对表单数据进行注册
		 */
		
		//封装表单数据
		User userForm = CommonUtils.toBean(request.getParameterMap(), User.class);
		
		//对表单元素进行校验 
		Map<String,String> errors = validateRegist(userForm, request.getSession());
		if(errors.size()>0){//如果校验有错误
			request.setAttribute("errors", errors);//保存错误信息
			request.setAttribute("form", userForm);//保存表单信息，为了回显
			return "f:/jsps/user/regist.jsp";
		}
		
		//将表单数据发送到数据库进行处理
		userService.regist(userForm);
		//保存成功信息,转发到msg.jsp页面。
		request.setAttribute("msg", "恭喜，您已经注册成功，请马上去邮箱激活");
		request.setAttribute("code", "success");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 对表单数据进行校验
	 * 用Map来封装错误信息，用当前表单元素名称做key，用错误信息做value 
	 * @param user
	 * @param session
	 * @return
	 */
	public Map<String,String> validateRegist(User user,HttpSession session){
		//创建一个Map集合，用来封装错误信息
		Map<String,String> errors = new HashMap<String, String>();
		
		//对用户名进行校验
		String loginname = user.getLoginname();
		if(loginname==null && loginname.trim().isEmpty()){//非空校验
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()<2 && loginname.length()>20){//长度校验
			errors.put("loginname", "用户名的长度必须在2——20之间");
		}else if(!userService.ajaxValidateLoginname(loginname)){//是否已被注册
			errors.put("loginname", "该用户名已经被注册");
		}
		
		//对密码进行校验
		String loginpass = user.getLoginpass();
		if(loginpass==null && loginpass.trim().isEmpty()){//非空校验
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()<3 && loginpass.length()>20){//长度校验
			errors.put("loginpass", "密码的长度必须在3——20之间");
		}
		
		//对确认密码进行校验
		String reloginpass = user.getReloginpass();
		if(reloginpass==null && reloginpass.trim().isEmpty()){//非空校验
			errors.put("reloginpass", "确认密码不能为空");
		}else if(reloginpass.length()<2 && reloginpass.length()>20){//长度校验
			errors.put("reloginpass", "确认密码的长度必须在2——20之间");
		}else if(!reloginpass.equals(loginpass)){//两次密码校验
			errors.put("reloginpass", "两次密码输入不一致");
		}
		
		//对邮箱进行校验
		String email = user.getEmail();
		if(email==null && email.trim().isEmpty()){//非空校验
			errors.put("email", "邮箱不能为空");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){//邮箱格式校验
			errors.put("email", "邮箱格式不正确");
		}else if(!userService.ajaxValidateEmail(email)){//是否已被注册
			errors.put("email", "邮箱已经被注册");
		}
		
		//对验证码进行校验
		String verifyCode = user.getVerifyCode();
		String vCode = (String) session.getAttribute("vCode");
		if(verifyCode==null && verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空");
		}else if(!verifyCode.equalsIgnoreCase(vCode)){
			errors.put("verifyCode", "验证码不正确");
		}

		return errors;
	}
	
	/**
	 * 对用户进行激活
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String activation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取激活码
		 * 2、把激活码交给UserService进行业务处理
		 * 		如果抛出异常，就处理异常，保存错误信息，转发到/jsps/msg.jsp
		 * 		如果成功，就保存成功信息，转发到/jsps.msg.jsp
		 */
		
		//获取激活码
		String activationCode = request.getParameter("activationCode");
		//交给UserService进行业务处理
		try {
			//如果成功
			userService.activation(activationCode);
			request.setAttribute("msg", "您已经激活成功，马上去登录吧！");
			request.setAttribute("code", "success");
		} catch (UserException e) {
			//如果失败，处理异常
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 登录
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路
		 * 1、封装表单数据
		 * 2、对表单数据进行校验
		 * 3、把表单数据交给service进行数据处理
		 * 		如果成功，把用户名保存到cookie中，在把user保存进session，重定向到主页
		 * 		如果失败，保存错误信息和表单数据，转发到login.jsp
		 */
		
		//封装表单数据
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		Map<String,String> errors = validateLogin(formUser, request.getSession());
		if(errors.size()>0){//如果校验出现错，保存错误信息和表单信息，转发到login.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}
		
		//如果校验没有问题，就把数据交给service进行处理
		try {
			User user = userService.login(formUser);
			//如果没有问题，把user对象保存进session中
			request.getSession().setAttribute("session_user", user);
			//在把用户名保存进cookie中
			
			String loginname = user.getLoginname();
			loginname = URLEncoder.encode(loginname, "utf-8");
			//在创建cookie时，需要给出一个键值对，但是值可能是中文，需要进行url编码处理
			Cookie cookie = new Cookie("loginname", loginname);
			//设置cookie的最大生存周期
			cookie.setMaxAge(60*60*24*10);//保存十天
			//把cookie发送到客户端
			response.addCookie(cookie);
			//重定向到主页
			return"r:/index.jsp";
		} catch (UserException e) {//service抛出异常，保存错误信息和表单信息，转发到login.jsp
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}
	}
	
	/**
	 * 校验登录表单信息
	 * @param user
	 * @param session
	 * @return
	 */
	public Map<String,String> validateLogin(User user,HttpSession session){
		//创建一个Map集合，用来封装错误信息
		Map<String,String> errors = new HashMap<String, String>();
		
		//对用户名进行校验
		String loginname = user.getLoginname();
		if(loginname==null && loginname.trim().isEmpty()){//非空校验
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()<2 && loginname.length()>20){//长度校验
			errors.put("loginname", "用户名的长度必须在2——20之间");
		}
		
		//对密码进行校验
		String loginpass = user.getLoginpass();
		if(loginpass==null && loginpass.trim().isEmpty()){//非空校验
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()<3 && loginpass.length()>20){//长度校验
			errors.put("loginpass", "密码的长度必须在3——20之间");
		}
		
		
		//对验证码进行校验
		String verifyCode = user.getVerifyCode();
		String vCode = (String) session.getAttribute("vCode");
		if(verifyCode==null && verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空");
		}else if(!verifyCode.equalsIgnoreCase(vCode)){
			errors.put("verifyCode", "验证码不对");
		}

		return errors;
	}
	
	
	
	/**
	 * ajax校验用户名
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String loginname = request.getParameter("loginname");
		boolean bool = userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(bool);
		return null;
	}
	
	
	/**
	 * ajax校验邮箱
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		boolean bool = userService.ajaxValidateEmail(email);
		response.getWriter().print(bool);
		return null;
	}
	
	/**
	 * 异步请求校验验证码是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String verifyCode = request.getParameter("verifyCode");
		String session_vcode = (String) request.getSession().getAttribute("vCode");
		boolean bool = verifyCode.equalsIgnoreCase(session_vcode);
		response.getWriter().print(bool);
		return null;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateLoginpass(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路
		 * 1、获取session中的用户名和参数中的新旧密码，交给UserService进行处理
		 * 		如果修改密码成功，就转发到msg.jsp
		 * 		如果修改密码失败，保存错误信息，转发到pws.jsp
		 * 
		 */
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		User user = (User) request.getSession().getAttribute("session_user");
		String uid = user.getUid();
		try {
			userService.updateLoginpass(form, uid);
			//如果成功，就转发到msg.jsp
			request.setAttribute("code", "success");
			request.setAttribute("msg", "修改密码成功");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			//保存错误信息
			request.setAttribute("msg", e.getMessage());
			//保存表单信息
			request.setAttribute("form", form);
			return "f:/jsps/user/pwd.jsp";
		}
	}
	
	/**
	 * 退出功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路
		 * 1、去除session的user对象
		 * 2、重定向到login.jsp页面
		 * 
		 */
		request.getSession().removeAttribute("session_user");
		System.out.println(request.getContextPath());
		return "r:/jsps/user/login.jsp";
	}
	
	
	public String forgetPassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		if (email == null) {
			request.setAttribute("msg", "邮箱不能为空");
			return "f:/jsps/user/forgetPwd.jsp";
		} else if (!email.matches("^([a-z0-9A-Z_-]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
			request.setAttribute("msg", "邮箱格式不正确为空");
			return "f:/jsps/user/forgetPwd.jsp";
		}
		try {
			User user = userService.forgetPassword(email);
			request.getSession().setAttribute("session_user", user);
			
			//发邮件到邮箱
			
			
			Properties props = new Properties();
			//读配置文件关于邮件主题和内容的信息
			props.load(this.getClass().getClassLoader().getResourceAsStream("forget.properties"));
			String subject = props.getProperty("subject");//获取邮件主题
			String content = props.getProperty("content");//获取邮件
			String code = TraveldreamUtils.getCode();
			content = MessageFormat.format(content, code);
			//发邮件
			new TraveldreamUtils().sendMail(subject, content, email);
			request.getSession().setAttribute("code", code);
			return "f:/jsps/user/newPassword.jsp";
			
		} catch (UserException e) {
			request.setAttribute("msg", "该邮箱还没有注册");
			return "f:/jsps/user/forgetPwd.jsp";
		}
		
	}
	
	/**
	 * 用户填写新密码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String forget_resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//获取从页面传来的验证码
		String vcode = request.getParameter("vcode");
		String code = (String) request.getSession().getAttribute("code");
		if(!code.equals(vcode)){//如果二者不相同
			request.setAttribute("msg", "验证码不正确");
			return "f:/jsps/user/newPassword.jsp";
		}
		//如果验证码正确，就修改密码
		String newPassword = request.getParameter("newPassword");
		User user = (User) request.getSession().getAttribute("session_user");
		userService.findPassword(user.getUid(), newPassword);
		return quit(request, response);
	}
	
	
	
	
	
}
