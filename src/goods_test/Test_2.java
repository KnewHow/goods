package goods_test;

import java.sql.SQLException;

import org.junit.Test;

import www.zhiyun.com.book.dao.BookDao;
import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.order.dao.OrderDao;
import www.zhiyun.com.order.domain.Order;
import www.zhiyun.com.pager.PageBean;


public class Test_2 {

//	@Test
//	public void fun1(){
//		BookDao bookDao = new BookDao();
//		PageBean<Book> pb = bookDao.findByCategory("5F79D0D246AD4216AC04E9C5FAB3199E", 10);
//		System.out.println(pb.toString());
//	}
	
	@Test
	public void fun2() throws SQLException{
		OrderDao dao = new OrderDao();
		PageBean<Order> pb = dao.findByUser("32DB3700D2564254982BC58B0E4D95BC", 3);
		System.out.println(pb.getBeanList().toString());
	}
}
