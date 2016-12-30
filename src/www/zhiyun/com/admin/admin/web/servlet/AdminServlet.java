package www.zhiyun.com.admin.admin.web.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.admin.admin.domain.Admin;
import www.zhiyun.com.admin.admin.service.AdminException;
import www.zhiyun.com.admin.admin.service.AdminService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
/**
 * 管理员对象的web层
 * @author Administrator
 *
 */
public class AdminServlet extends BaseServlet {

	private AdminService adminService = new AdminService();
	
	/**
	 * 管理员的登录方法
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String adminLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装表单数据
		Admin form = CommonUtils.toBean(request.getParameterMap(), Admin.class);
		
		try {
			Admin admin = adminService.login(form);
			//保存Admin对象进入
			request.getSession().setAttribute("session_admin", admin);
			return "r:/adminjsps/admin/index.jsp";
		} catch (AdminException e) {
			request.setAttribute("form", form);
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/login.jsp";
		}
	}
	
	/**
	 * 管理员的退出
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String adminQuit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Admin admin = (Admin) request.getSession().getAttribute("session_admin");
		adminService.quit(admin);
		return"f:/adminjsps/login.jsp";
	}
}
