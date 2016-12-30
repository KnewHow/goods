package www.zhiyun.com.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import www.zhiyun.com.category.domain.Category;

import com.sun.jmx.snmp.daemon.CommunicationException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 分类的持久层
 * @author 1
 *
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	/**
	 * 添加分类，这个方法既可以添加一级分类，也可以添加二级分类
	 * 一级分类和二级分类的唯一区别就是有木有父分类。在数据库里面体现为pid
	 * @param category
	 * @throws SQLException 
	 */
	public void add(Category category) throws SQLException{  
		String sql = "insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		String pid = null;
		if(category.getParent()!=null){//说明是二级分类
			pid = category.getParent().getCid();//把pid设置为父分类ID
		}
		Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql,params);
	}
	
	/**
	 * 查询不带二级分类的一级分类
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findParents() throws SQLException{
		String sql = "select *from t_category where pid is null order by orderBy";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> categoryList = toListCategory(mapList);
		return categoryList;
		
	}
	
	/**
	 * 根据一级分类查询二级分类的个数
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public int findChildrenCntByParent(String pid) throws SQLException{
		String sql = "select count(*) from t_category where pid =?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),pid);
		return number==null ? 0 :number.intValue();
	}
	
	/**
	 *根据cid删除分类 
	 * @param cid
	 * @throws SQLException 
	 */
	public void delete(String cid) throws SQLException{
		String sql = "delete from t_category where cid=?";
		qr.update(sql,cid);
	}
	
	/**
	 * 编辑一级分类或者二级分类方法
	 * 一级分类和二级分类的主要区别在于pid是否为null
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException{
		String sql = "update t_category set cname=?,pid=?,`desc`=? where cid=?";
		/*
		 * 因为一级分类的pid为空，即没有category.getParent()
		 * 如果不这样，就会导致空指针异常
		 */
		String pid = null;
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(),pid,category.getDesc(),category.getCid()};
		qr.update(sql,params);
	}
	
	/**
	 * 加载一级分类或者二级分类方法
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException{
		String sql = "select *from t_category where cid=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(),cid);
		return toCategory(map);
	}
	/**
	 * 查询所有的分类
	 * @return
	 */
	public List<Category> findAll(){
		try{
			String sql = "select * from t_category where pid is null order by orderBy";
			//使用map集合，是为了防止pid这个数据的丢失
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
			//获取所有的一级分类
			List<Category> parents = toListCategory(mapList);
			//在为每个一级分类，添加二级分类
			for(Category parent:parents){
				//获取二级分类
				List<Category> children = findByParent(parent.getCid());
				//把二级分类，添加到一级分类下
				parent.setChildren(children);
			}
			return parents;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 写一个私有方法，该方法可以把一个map集合变成category对象
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String,Object> map){
		Category category = CommonUtils.toBean(map, Category.class);
		//获取pid值
		String pid = (String) map.get("pid");
		if(pid!=null){//如果pid不等于null，表示其不是一级分类，我们要为其添加父分类
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		} 
		return category;
	}
	
	/**
	 * 写一个私有方法，把list<Map>变成List<Category>
	 * @param mapList
	 * @return
	 */
	private List<Category> toListCategory(List<Map<String,Object>> mapList){
		//对list集合进行遍历，然后在调用上面已经写好的，对单个集合转成对象的方法
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String,Object> map:mapList){
			categoryList.add(toCategory(map));
		}
		return categoryList;
	}
	
	/**
	 * 
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql = "select *from t_category where pid=? order by orderBy";
		//获取所有的二级分类
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),pid);
		return toListCategory(mapList);
		
	}

}
