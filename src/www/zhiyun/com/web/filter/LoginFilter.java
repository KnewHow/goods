package www.zhiyun.com.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoginFilter implements Filter {

	public void destroy() {
	}

	
	/**
	 * 过滤器的校验和放行方法
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		//获取session域里面的user对象
		Object user = req.getSession().getAttribute("session_user");
		if(user==null){//判断如果session域里面对象如果为空，就保存错误信息并且给出提示
			req.setAttribute("code", "error");
			req.setAttribute("msg", "您还没有登录，不能访问本资源！");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		}else{//如果没有问题，就放行
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
