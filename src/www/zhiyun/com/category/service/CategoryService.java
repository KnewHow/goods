package www.zhiyun.com.category.service;

import java.sql.SQLException;
import java.util.List;

import www.zhiyun.com.category.dao.CategoryDao;
import www.zhiyun.com.category.domain.Category;


/**
 * 分类的业务层
 * @author 1
 *
 */
public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		return categoryDao.findAll();
	}
	
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 查询不含二级分类的子分类
	 * @param category
	 */
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据cid来加载一级或者二级分类的方法
	 * @param cid
	 * @return
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 编辑一级或者二级分类的方法
	 * @param category
	 */
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	} 
	
	/**
	 * 根据一级分类查询其所属二级分类个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCntByParent(String pid){
		try {
			return categoryDao.findChildrenCntByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 删除分类
	 * @param cid
	 */
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据一级分类查找子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	
}
