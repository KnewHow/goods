package www.zhiyun.com.cart.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.cart.domain.CartItem;
import www.zhiyun.com.cart.service.CartItemService;
import www.zhiyun.com.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

@SuppressWarnings("serial")
public class CartItemServlet extends BaseServlet {

	private CartItemService cartItemService = new CartItemService();
	
	/**
	 * 我的购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("session_user");
		String uid = user.getUid();
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		request.setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 添加购物车条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路
		 * 1、封装表单数据
		 * 2、使用CartItem来来对数据进行处理
		 * 3、调用没有myCart()方法来返回到我的购物车页面
		 * 
		 */
		Map map = request.getParameterMap();
		//封装Cartitem的表单数据,也就是quantity
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		//封装Book的表单数据，也就是bid
		Book book = CommonUtils.toBean(map, Book.class);
		//获取session中的user对象
		User user = (User) request.getSession().getAttribute("session_user");
		cartItem.setBook(book);
		cartItem.setUser(user);
		cartItemService.add(cartItem);
		return myCart(request, response);
	}
	
	/**
	 * 批量删除购物车里面的条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取删除所需要的参数
		String cartItemIds = request.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(request, response);
	}
	
	public String updateQuantity(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取CartItemId和quantity参数
		String cartItemId = request.getParameter("cartItemId");
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		//修改以后返回结果
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		//把CartItem对象里面的quantity属性和subtotal属性变成JSON对象
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		//返回JSON对象
		response.getWriter().print(sb.toString()); 
		return null;
	}
	
	/**
	 * 加载被选中的购物车条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadCartItems(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1、获取参数
		 * 2、得到被选中的对象
		 * 3、保存到request域
		 * 4、转发到/jsps/cart/
		 * 
		 */
		String cartItemIds = request.getParameter("cartItemIds");
		double total = Double.parseDouble(request.getParameter("hiddenTotal"));
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		request.setAttribute("cartItemList", cartItemList);
		request.setAttribute("total", total);
		request.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
}
