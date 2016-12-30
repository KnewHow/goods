package www.zhiyun.com.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.book.service.BookService;
import www.zhiyun.com.pager.PageBean;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
/**
 * book对象的web层
 * @author 1
 *
 */
public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	/**
	 * 按分类来查找图书
	 */
	public String findByCategory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取页面的pc
		 * 2、获取页面的cid
		 * 3、使用BookService进行来获取pageBean对象
		 * 4、补充pageBean对象的url属性
		 * 5、把信息保存进request域中
		 * 6、转发到list.jsp
		 */
		
		//获取pc对象
		int pc = getPc(request);
		//获取cid
		String cid = request.getParameter("cid");
		//获取url
		String url = getUlr(request);
		//获取pageBean对象
		PageBean<Book> pageBean = bookService.findByCategory(cid, pc);
		//设置url
		pageBean.setUrl(url);
		//保存进入request域
		request.setAttribute("pb", pageBean);
		//转发到相应的页面
		return "f:/jsps/book/list.jsp";
		
		
		
	}
	
	/**
	 * 按作者来查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取页面的pc
		 * 2、获取页面的cid
		 * 3、使用BookService进行来获取pageBean对象
		 * 4、补充pageBean对象的url属性
		 * 5、把信息保存进request域中
		 * 6、转发到list.jsp
		 */
		
		//获取pc对象
		int pc = getPc(request);
		//获取cid
		String author = request.getParameter("author");
		//获取url
		String url = getUlr(request);
		//获取pageBean对象
		PageBean<Book> pageBean = bookService.findByAuthor(author, pc);
		//设置url
		pageBean.setUrl(url);
		//保存进入request域
		request.setAttribute("pb", pageBean);
		//转发到相应的页面
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按出版社来查询图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取页面的pc
		 * 2、获取页面的cid
		 * 3、使用BookService进行来获取pageBean对象
		 * 4、补充pageBean对象的url属性
		 * 5、把信息保存进request域中
		 * 6、转发到list.jsp
		 */
		
		//获取pc对象
		int pc = getPc(request);
		//获取cid
		String press = request.getParameter("press");
		//获取url
		String url = getUlr(request);
		//获取pageBean对象
		PageBean<Book> pageBean = bookService.findByPress(press, pc);
		//设置url
		pageBean.setUrl(url);
		//保存进入request域
		request.setAttribute("pb", pageBean);
		//转发到相应的页面
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按书名来查询图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取页面的pc
		 * 2、获取页面的cid
		 * 3、使用BookService进行来获取pageBean对象
		 * 4、补充pageBean对象的url属性
		 * 5、把信息保存进request域中
		 * 6、转发到list.jsp
		 */
		
		//获取pc对象
		int pc = getPc(request);
		//获取cid
		String bname = request.getParameter("bname");
		//获取url
		String url = getUlr(request);
		//获取pageBean对象
		PageBean<Book> pageBean = bookService.findByBname(bname, pc);
		//设置url
		pageBean.setUrl(url);
		//保存进入request域
		request.setAttribute("pb", pageBean);
		//转发到相应的页面
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 组合条件查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*思路：
		 * 1、获取页面的pc
		 * 2、获取页面的cid
		 * 3、使用BookService进行来获取pageBean对象
		 * 4、补充pageBean对象的url属性
		 * 5、把信息保存进request域中
		 * 6、转发到list.jsp
		 */
		
		//获取pc对象
		int pc = getPc(request);
		//获取cid
		Book form = CommonUtils.toBean(request.getParameterMap(), Book.class);
		//获取url
		String url = getUlr(request);
		//获取pageBean对象
		PageBean<Book> pageBean = bookService.findByCombination(form, pc);
		//设置url
		pageBean.setUrl(url);
		//保存进入request域
		request.setAttribute("pb", pageBean);
		//转发到相应的页面
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 加载图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	
	
	
	
	/**
	 * 获取所需的页码
	 * @param request
	 * @return
	 */
	private int getPc(HttpServletRequest request){
		int pc = 1;
		String param = request.getParameter("pc");
		if(param!=null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
		
		return pc;
	}
	
	/**
	 * 获取url
	 * @param request
	 * @return
	 */
	private String getUlr(HttpServletRequest request){
		/*获取url的思路
		 * http://localhost:8080/goods/BookServlet?method=findByCategory&pc=xxx
		 * 以下是两个方法所获取对应的路径
		 * request.getRequestUrl:/goods/BookSer
		 * request.getQueryString:method=findByCategory&pc=xxx
		 */
		String url = request.getRequestURI()+"?"+request.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url = url.substring(0,index);//http://localhost:8080/goods/BookServlet?method=findByCategory
		}
		return url;
	}
	
}
