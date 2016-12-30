package www.zhiyun.com.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.category.domain.Category;
import www.zhiyun.com.pager.Expression;
import www.zhiyun.com.pager.PageBean;
import www.zhiyun.com.pager.PageConstants;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * book的持久层
 * @author 1
 *
 */
public class BookDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PageBean<Book> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		/*思路
		 * 1、得到ps
		 * 2、得到tr
		 * 3、得到beanList
		 * 4、创建PageBean,封装数据
		 * 
		 */
		
		//1、获取ps
		int ps = PageConstants.BOOK_PAGE_SIZE;
		
		//2、获取总记录数tr，但是我们要通过Expression来创建where子句
		StringBuilder whereSql = new StringBuilder(" where 1=1");//创建where子句的前段
		List<Object> params = new ArrayList<Object>();//创建集合来存储参数的值
		
		//对exprList进行遍历，把whereSql补全
		for(Expression expr:exprList){
			whereSql.append(" and").append(" ").append(expr.getName()).
			append(" ").append(expr.getOperator()).append(" ");
			/*
			 * 为什么没有直接连接参数呢，因为有的条件是 is null，后面是不接参数的
			 * 所以下一步我们进行判断，如果是is null 我们就不连接参数
			 * 如果不是 is null ，我们就把参数保存进params中
			 */
			if(!expr.getOperator().equals("is null")){//如果条件不是 is null
				whereSql.append("?");
				params.add(expr.getValue());//把参数保存
			}
		}
		//3、获取总记录数
		String sql = "select count(*) from t_book "+whereSql;
		Number num = (Number) qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = num.intValue();
		
		//获取beanList
		sql = "select *from t_book"+whereSql+" order by orderBy limit ?,?";
		params.add((pc-1) * ps);
		params.add(ps);
		List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class),params.toArray());
		
		//创建pageBean，并设置参数,但是不设置url，这个交给Servlet来给出
		PageBean<Book> pb = new PageBean<Book>();
		
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}
	
	/**
	 * 按分类来查询图书
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid,int pc){
		try {
			List<Expression> exprList = new ArrayList<Expression>();
			exprList.add(new Expression("cid", "=", cid));
			return findByCriteria(exprList, pc);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据书名来查询图书
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname,int pc){
		try {
			List<Expression> exprList = new ArrayList<Expression>();
			exprList.add(new Expression("bname", "like", "%"+bname+"%"));
			return findByCriteria(exprList, pc);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 按作者查询
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author,int pc){
		try {
			List<Expression> exprList = new ArrayList<Expression>();
			exprList.add(new Expression("author", "like", "%"+author+"%"));
			return findByCriteria(exprList, pc);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 按出版社查询
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String author,int pc){
		try {
			List<Expression> exprList = new ArrayList<Expression>();
			exprList.add(new Expression("press", "like", "%"+author+"%"));
			return findByCriteria(exprList, pc);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 组合查询
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book criteria,int pc){
		try {
			List<Expression> exprList = new ArrayList<Expression>();
			exprList.add(new Expression("bname", "like", "%"+criteria.getBname()+"%"));
			exprList.add(new Expression("author", "like", "%"+criteria.getAuthor()+"%"));
			exprList.add(new Expression("press", "like", "%"+criteria.getPress()+"%"));
			return findByCriteria(exprList, pc);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 按bid来查询图书
	 * @param bid
	 * @return
	 */
	public Book findByBid(String bid){
		try {
			//使用多表查询，查询图书父分类的id
			String sql = "select *from t_book b,t_category c where b.cid = c.cid and bid=?";
			Map<String,Object> map = qr.query(sql, new MapHandler(),bid);
			//获取book里面除了cid以外的值
			Book book = CommonUtils.toBean(map, Book.class);
			//获取book表中被落下的cid
			Category category = CommonUtils.toBean(map, Category.class);
			String pid = (String) map.get("pid");
			if(pid!=null){
				Category parent = new Category();
				parent.setCid(pid);
				category.setParent(parent);
			}
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据二级分类来查找图书
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int findBookCountByCategory(String cid) throws SQLException{
		
		String sql = "select count(*) from t_book where cid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),cid);
		return number==null ? 0:number.intValue();
	}

	/**
	 * 添加图书方法
	 * @param book
	 * @throws SQLException 
	 */
	public void add(Book book) throws SQLException {
		String sql = "insert into t_book(bid,bname,author,price,currPrice,discount," +
				"press,publishtime,edition,pageNum,wordNum,printtime,booksize,paper," +
				"cid,image_w,image_b) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),book.getPrice(),
				book.getCurrPrice(),book.getDiscount(),book.getPress(),book.getPublishtime(),
				book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),book.getImage_w(),
				book.getImage_b()};
		qr.update(sql,params);
	}
	
	
	/**
	 * 修改图书
	 * @param book
	 * @throws SQLException 
	 */
	public void edit(Book book) throws SQLException{
		String sql = "update t_book set bname=?,author=?,price=?," +
				"currPrice=?,discount=?," +
				"press=?,publishtime=?,edition=?,pageNum=?," +
				"wordNum=?,printtime=?,booksize=?,paper=?,cid=? where bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),book.getPrice(),
				book.getCurrPrice(),book.getDiscount(),book.getPress(),book.getPublishtime(),
				book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),book.getBid()};
		qr.update(sql,params);
	}
	
	
	/**
	 * 删除图书方法
	 * @param bid
	 * @throws SQLException 
	 */
	public void delete(String bid) throws SQLException{
		String sql = "delete from t_book where bid=?";
		qr.update(sql,bid);
	}
	
	
	
}
