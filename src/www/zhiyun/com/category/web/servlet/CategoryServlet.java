package www.zhiyun.com.category.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import www.zhiyun.com.category.domain.Category;
import www.zhiyun.com.category.service.CategoryService;

import cn.itcast.servlet.BaseServlet;
/**
 * 分类模块的web层
 * @author 1
 *
 */
public class CategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryService();
	
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		request.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}
}
