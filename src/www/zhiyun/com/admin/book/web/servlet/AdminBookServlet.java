package www.zhiyun.com.admin.book.web.servlet;



import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.book.service.BookService;
import www.zhiyun.com.category.domain.Category;
import www.zhiyun.com.category.service.CategoryService;
import www.zhiyun.com.pager.PageBean;

import net.sf.json.JSONArray;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
/**
 * 普通图书管理的Servlet
 * @author Administrator
 *
 */
public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有的图书分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * 
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	
	/**
	 * 异步根据一级分类获取二级分类，使用自己的定义的方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxGetChildren(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		List<Category> categoryList = categoryService.findChildren(pid);
		String json = toJson(categoryList);
System.out.println(json);
		response.getWriter().print(json);
		return null;
	}
	
	
	
	/**
	 * 异步根据一级分类或者二级分类，使用jar包来完成
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxGetChildren2(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		List<Category> categoryList = categoryService.findChildren(pid);
		String json = JSONArray.fromObject(categoryList).toString();
System.out.println(json);
		response.getWriter().print(json);
		return null;
	}
	
	/**
	 * 把List集合转变为JSON字符串
	 * [{"cid":"vvv","cname":"xxx"}]
	 * @param categoryList
	 * @return
	 */
	private String  toJson(List<Category> categoryList){ 
		StringBuilder sb = new StringBuilder("[");
		for(int i=0;i<categoryList.size();i++){
			sb.append(toJson(categoryList.get(i)));
			if(i<categoryList.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 将单独的Category对象变成字符串
	 * {"cid":"vvv","cname":"xxx"}
	 * @param category
	 * @return
	 */
	private String  toJson(Category category){
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	
	
	
	
	/**
	 * 添加图书之前的第一步：把所有的一级分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addBookPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
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
		return "f:/adminjsps/admin/book/list.jsp";
		
		
		
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		//加载图书
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		
		//加载图书的一级分类
		List<Category> parents = categoryService.findParents();
		request.setAttribute("parents", parents);
		
		//获取该图书的一级分类下面的二级分类
		String pid = book.getCategory().getParent().getCid();
		List<Category> children = categoryService.findChildren(pid);
		request.setAttribute("children", children);
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	
	/**
	 * 修改图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map map = request.getParameterMap();
		//封装图书对象
		Book book = CommonUtils.toBean(map, Book.class);
		//封装分类对象
		Category category = CommonUtils.toBean(map, Category.class);
//System.out.println(book.toString());
		book.setCategory(category);
		bookService.edit(book);
		request.setAttribute("msg", "修改图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * 删除图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1、获取pid
		 * 2、加载图书
		 * 3、删除图片
		 * 4、删除数据库信息
		 * 5、转发成功页面
		 */
		
		//获取pid
		String bid = request.getParameter("bid");
		//加载图书
		Book book = bookService.load(bid);
		//获取存储路径
		String savepath = this.getServletContext().getRealPath("/");
		//删除图片
		new File(savepath, book.getImage_b()).delete();
		new File(savepath, book.getImage_w()).delete();
		//删除数据库信息
		bookService.delete(bid);
		request.setAttribute("msg", "删除图书成功");
		return "f:/adminjsps/msg.jsp";
		
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
