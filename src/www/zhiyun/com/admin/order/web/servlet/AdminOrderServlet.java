package www.zhiyun.com.admin.order.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.order.domain.Order;
import www.zhiyun.com.order.service.OrderService;
import www.zhiyun.com.pager.PageBean;
import www.zhiyun.com.user.domain.User;

import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	
	/**
	 * 获取url请求路径
	 * 
	 * @param request
	 * @return
	 */
	private String getUrl(HttpServletRequest request) {
		/*
		 * getRequestURI：/goods/OrderServlet getQueryString:method=xx&pid=xxx
		 * 但是我们这里的url要去除里面的参数pc，因为pc需要页面自己给出
		 */
		String url = request.getRequestURI() + "?" + request.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 获取页面中传来的pc
	 * 
	 * @param request
	 * @return
	 */
	private int getPc(HttpServletRequest request) {
		int pc = 1;
		String param = request.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {// 如果获取到的pc不为空，就使用页面的pc
			pc = Integer.parseInt(param);
		}

		return pc;
	}
	
	/**
	 * 查询所有订单方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 思路 1、获取页面传递过来的pc和session中的user 2、向办法得到url设置给pageBean对象
		 * 3、保存pageBean，然后转发到/jsps/order/list.jsp
		 */
		int pc = getPc(request);
		String url = getUrl(request);
		PageBean<Order> pb = orderService.findAll(pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	/**
	 * 按订单状态取消订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 思路 1、获取页面传递过来的pc和session中的user 2、向办法得到url设置给pageBean对象
		 * 3、保存pageBean，然后转发到/jsps/order/list.jsp
		 */
		int pc = getPc(request);
		String url = getUrl(request);
		int status = Integer.parseInt(request.getParameter("status"));
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 加载订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid = request.getParameter("oid");
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		String btn = request.getParameter("btn");
		request.setAttribute("btn", btn);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.getStatus(oid);
		if (status != 1) {// 如果订单状态不为1，无法取消订单，提示错误信息
			request.setAttribute("code", "error");
			request.setAttribute("msg", "订单状态不对，不能取消订单！");
			return "f:/adminjsps/msg.jsp";
		}
		// 如果状态正确就取消订单
		orderService.updateStatus(oid, 5);
		request.setAttribute("code", "success");
		request.setAttribute("msg", "取消订单成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * 发货
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.getStatus(oid);
		if (status != 2) {// 如果订单状态不为1，无法取消订单，提示错误信息
			request.setAttribute("code", "error");
			request.setAttribute("msg", "订单状态不对，不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		// 如果状态正确就取消订单
		orderService.updateStatus(oid, 3);
		request.setAttribute("code", "success");
		request.setAttribute("msg", "发货成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	
	


}
