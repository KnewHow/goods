package www.zhiyun.com.book.service;

import java.sql.SQLException;

import www.zhiyun.com.book.dao.BookDao;
import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.pager.PageBean;

/**
 * book对象的业务层
 * @author 1
 *
 */
public class BookService {

	private BookDao bookDao = new BookDao();
	
	/**
	 * 根据分类来查找数目
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid,int pc){
		return bookDao.findByCategory(cid, pc);
	}
	
	/**
	 * 按书名来查询图书
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname,int pc){
		return bookDao.findByBname(bname, pc);
	}
	
	/**
	 * 根据作者来查询图书
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author,int pc){
		return bookDao.findByAuthor(author, pc);
	}
	
	/**
	 * 根据出版社来查询图书
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press,int pc){
		return bookDao.findByPress(press, pc);
	}
	
	/**
	 * 多条件组合查询
	 * @param form
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book form,int pc){
		return bookDao.findByCombination(form, pc);
	}
	
	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		return bookDao.findByBid(bid);
	}
	
	/**
	 * 按二级分类查找图书数量
	 * @param cid
	 * @return
	 */
	public int findBookCountByCategory(String cid){
		try {
			return bookDao.findBookCountByCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * 添加图书方法
	 * @param book
	 */
	public void add(Book book) {
		try {
			bookDao.add(book);
		} catch (SQLException e) {
			throw new RuntimeException("图书添加异常！");
		}
	}
	
	/**
	 * 编辑图书
	 * @param book
	 */
	public void edit(Book book) {
		try {
			bookDao.edit(book);
		} catch (SQLException e) {
			throw new RuntimeException("图书修改异常！");
		}
	}
	
	/**
	 * 删除图书
	 * @param bid
	 */
	public void delete(String bid) {
		try {
			bookDao.delete(bid);
		} catch (SQLException e) {
			throw new RuntimeException("图书修改异常！");
		}
	}
}
