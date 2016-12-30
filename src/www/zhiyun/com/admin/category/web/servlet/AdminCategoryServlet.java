package www.zhiyun.com.admin.category.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.book.service.BookService;
import www.zhiyun.com.category.domain.Category;
import www.zhiyun.com.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
/**
 * 管理员对分类的管理
 * @author Administrator
 *
 */
public class AdminCategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService();
	
	/**
	 * 管理员查询所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("parents", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加一级分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		parent.setCid(CommonUtils.uuid());
		categoryService.add(parent);
		return findAll(request, response);
	}
	
	
	/**
	 * 添加二级分类的准备工作
	 * 准备工作：
	 * 1、查询所有的一级分类，在下拉列表里面显示
	 * 2、获取添加二级分类所在的一级分类的cid，在下拉列表里面默认显示
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		List<Category> parents = categoryService.findParents();
		request.setAttribute("pid", pid);
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	/**
	 * 添加二级分类的方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		//设置uuid
		child.setCid(CommonUtils.uuid());
		//获取父分类id
		String pid = request.getParameter("pid");
		//创建父分类
		Category parent = new Category();
		parent.setCid(pid);
		//把父分类设置给子分类
		child.setParent(parent);
		//添加子分类
		categoryService.add(child);
		return findAll(request, response);
	}
	
	/**
	 * 编辑一级分类的准备工作，即把一级分类从数据库查找出来，在转发到edit.jsp页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		Category parent = categoryService.load(cid);
		request.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	/**
	 * 编辑一级分类的数据库编辑工作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装表单数据
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		categoryService.edit(parent);
		return findAll(request, response);
	}
	 
	/**
	 * 修改二级分类的准备工作：从数据里面查找数据，转发到edit2.jsp
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取二级分类的cid，并进行加载
		String cid = request.getParameter("cid");
		Category child = categoryService.load(cid);
		request.setAttribute("child", child);
		//获取所有的父分类
		List<Category> parents = categoryService.findParents();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	  
	/**
	 *修改二级分类的修改工作：把表单传来的修改信息对数据库进行更新
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装表单数据
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		//获取父类ID
		String pid = request.getParameter("pid");
		//创建一级分类对象，并对cid进行封装
		Category parent = new Category();
		parent.setCid(pid);
		//把一级分类的信息封装给二级分类
		child.setParent(parent);
		categoryService.edit(child);
		return findAll(request, response);
	}

	/**
	 * 删除一级分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		int cnt = categoryService.findChildrenCntByParent(cid);
		if(cnt!=0){//如果一级分类下面有二级分类，保存错误信息，不删除
			request.setAttribute("msg", "该一级分类下面还有二级分类，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}else{//如果该一级分类下面没有二级分类，就对其进行删除
			categoryService.delete(cid);
			return findAll(request, response);
		}
	}
	
	/**
	 * 删除二级分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteChild(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		//获取二级目录下面的图书数量
		int cnt = bookService.findBookCountByCategory(cid);
		if(cnt>0){//如果二级分类下面还有图书，保存错误信息，不让你删
			request.setAttribute("msg", "该分类下面还有图书，不能删除");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(request, response);
		}
	}
}
