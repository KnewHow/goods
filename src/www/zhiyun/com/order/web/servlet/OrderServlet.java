package www.zhiyun.com.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.jdbc.odbc.OdbcDef;
import www.zhiyun.com.cart.domain.CartItem;
import www.zhiyun.com.cart.service.CartItemService;
import www.zhiyun.com.order.domain.Order;
import www.zhiyun.com.order.domain.OrderItem;
import www.zhiyun.com.order.service.OrderService;
import www.zhiyun.com.pager.PageBean;
import www.zhiyun.com.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {

	private OrderService orderService = new OrderService();
	private CartItemService cartItemService = new CartItemService();

	/**
	 * 我的订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 思路 1、获取页面传递过来的pc和session中的user 2、向办法得到url设置给pageBean对象
		 * 3、保存pageBean，然后转发到/jsps/order/list.jsp
		 */
		int pc = getPc(request);
		String url = getUrl(request);
		User user = (User) request.getSession().getAttribute("session_user");
		PageBean<Order> pb = orderService.myOrder(user.getUid(), pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";
	}

	/**
	 * 创建订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 在Servlet里面最重要的事情就是对Order数据进行封装，但是怎么获取相关的信息呢 这个需要购物车里面传购物车里面的购物车条目的ID
		 * 1、获取从购物车里面传来的购物车条目的IDs 2、获取购物车条目的List集合 3、用条目来完成对Order的封装
		 * 4、遍历购物车条目，对OrderItem进行封装
		 */
		String cartItemIds = request.getParameter("cartItemIds");
//System.out.println(cartItemIds);
		List<CartItem> cartItemList = cartItemService
				.loadCartItems(cartItemIds);
		Order order = new Order();
		order.setOid(CommonUtils.uuid());// 设置order的主键
		order.setOrdertime(String.format("%tF %<tT", new Date()));// 设置订单时间
		order.setAddress(request.getParameter("address"));// 设置收货地址
		order.setStatus(1);// 设置状态为未付款

		BigDecimal total = new BigDecimal("0");
		for (CartItem cartItem : cartItemList) {
			total = total.add(new BigDecimal(cartItem.getSubtotal() + ""));
		}
		order.setTotal(total.doubleValue());// 设置总计
		User owner = (User) request.getSession().getAttribute("session_user");
		order.setUser(owner);// 设置所属用户

		// 对订单条目进行设置
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem : cartItemList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		// 在数据库里面创建订单
		orderService.createOrder(order);
		// 订单已经生成了，所以要把购物车里面的数据库给删了
		cartItemService.batchDelete(cartItemIds);

		request.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}

	/**
	 * 加载订单
	 * 
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
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 取消订单
	 * 
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
			request.setAttribute("msg", "您的状态不对，不能取消订单！");
			return "f:/jsps/msg.jsp";
		}
		// 如果状态正确就取消订单
		orderService.updateStatus(oid, 5);
		request.setAttribute("code", "success");
		request.setAttribute("msg", "取消订单成功");
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 确认收货
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.getStatus(oid);
		if (status != 3) {// 如果订单状态不为3，无法确认收货，提示错误信息
			request.setAttribute("code", "error");
			request.setAttribute("msg", "您的状态不对，不能确认收货！");
			return "f:/jsps/msg.jsp";
		}
		// 如果状态正确就取消订单
		orderService.updateStatus(oid, 4);
		request.setAttribute("code", "success");
		request.setAttribute("msg", "交易成功！");
		return "f:/jsps/msg.jsp";

	}

	/**
	 * 支付准备
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String paymentPre(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/pay.jsp";

	}

	/**
	 * 支付方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Properties props = new Properties();
		// 加载payment.properties配置文件
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("payment.properties"));

		// 给定13个参数
		String p0_Cmd = "Buy";// 业务类型
		String p1_MerId = props.getProperty("p1_MerId");// 商户编号，类似于银行卡号
		String p2_Order = request.getParameter("oid"); // 商品编号
		String p3_Amt = "0.01";// 用户支付金额
		String p4_Cur = "CNY";// 支付币种：人民币
		String p5_Pid = "";// 商品名称。如果参数不给值，可以给一个空的字符串
		String p6_Pcat = "";// 商品种类
		String p7_Pdesc = "";// 商品描述
		String p8_Url = props.getProperty("p8_Url");// 易宝回调使用的地址路径
		String p9_SAF = "";// 送货地址
		String pa_MP = "";// 商品扩展信息；
		String pd_FrpId = request.getParameter("yh");// 支付通道
		String pr_NeedResponse = "1";// 应答机制，默认为1

		//获取配置文件里面的秘钥
		String keyValue = props.getProperty("keyValue");
		// 使用13个参数加上秘钥获取hmac
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur,
				p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId,
				pr_NeedResponse, keyValue);
		//使用sb来链接参数
		StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
		//添加参数
		sb.append("?").append("p0_Cmd=").append(p0_Cmd);
		sb.append("&").append("p1_MerId=").append(p1_MerId);
		sb.append("&").append("p2_Order=").append(p2_Order);
		sb.append("&").append("p3_Amt=").append(p3_Amt);
		sb.append("&").append("p4_Cur=").append(p4_Cur);
		sb.append("&").append("p5_Pid=").append(p5_Pid);
		sb.append("&").append("p6_Pcat=").append(p6_Pcat);
		sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
		sb.append("&").append("p8_Url=").append(p8_Url);
		sb.append("&").append("p9_SAF=").append(p9_SAF);
		sb.append("&").append("pa_MP=").append(pa_MP);
		sb.append("&").append("pd_FrpId=").append(pd_FrpId);
		sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
		sb.append("&").append("hmac=").append(hmac);
		response.sendRedirect(sb.toString());
		return null;

	}
	
	/**
	 * 支付方法的回调
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*首先回调有两种方法，一个是易宝引导的重定向会商城页面
		 * 第二个是点对点和服务器进行链接。第一种方式如果用户在付完款以后直接关闭浏览器
		 * 没有重定向回商城，那么就无法修改自己的订单状态，很不安全。第二种方式是很安全的。但是没有
		 * 固定的IP地址做不了。区别这两种支付方式的的方法是判断易宝给的参数
		 * 
		 */
		
		
		//获取易宝反馈的12个参数
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		//这个表示的是使用点对点还是重定向。如果r9_BType是1表示重定向，为2表点对点
		String r9_BType = request.getParameter("r9_BType");
		String hmac = request.getParameter("hmac");
		
		//获取配置文件里面的keyValue秘钥
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		
		String keyValue = props.getProperty("keyValue");
		//对hmac进行校验
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		
		if(!bool){//如果bool为false，表明错误
			request.setAttribute("code", "error");
			request.setAttribute("msg", "标签不正确，支付失败！");
			return "f:/jsps/msg.jsp";
		}
		
		if(r1_Code.equals("1")){//表示支付成功
			orderService.updateStatus(r6_Order, 2);
			if(r9_BType.equals("1")){//如果是重定向的方法
				request.setAttribute("code", "success");
				request.setAttribute("msg", "恭喜支付成功！");
				return "f:/jsps/msg.jsp";
			}else if(r9_BType.equals("2")){//使用点对点的方式进行返回
				response.getWriter().print("success");
			}
		}
		return null;
		
	}

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
}
