package www.zhiyun.com.order.service;

import java.sql.SQLException;

import www.zhiyun.com.order.dao.OrderDao;
import www.zhiyun.com.order.domain.Order;
import www.zhiyun.com.pager.PageBean;

import cn.itcast.jdbc.JdbcUtils;

public class OrderService {

	private OrderDao orderDao = new OrderDao();
	/**
	 * 我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrder(String uid,int pc){
		try {
			JdbcUtils.beginTransaction();//开启事务
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();//提交事务
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * 查询所有订单
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc){
		try {
			JdbcUtils.beginTransaction();//开启事务
			PageBean<Order> pb = orderDao.findAll(pc);
			JdbcUtils.commitTransaction();//提交事务
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * 按订单查询订单
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status,int pc){
		try {
			JdbcUtils.beginTransaction();//开启事务
			PageBean<Order> pb = orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();//提交事务
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * 创建订单
	 * @param order
	 */
	public void createOrder(Order order){
		try {
			JdbcUtils.beginTransaction();
			orderDao.createOrder(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid){
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	public int getStatus(String oid){
		try {
			return orderDao.getStatus(oid);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid,int status){
		try {
			JdbcUtils.beginTransaction();//开启事务
			orderDao.updateStatus(oid, status);
			JdbcUtils.commitTransaction();//提交事务
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}//回滚事务
			throw new RuntimeException();
		}
	}
}
