package www.zhiyun.com.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.order.domain.Order;
import www.zhiyun.com.order.domain.OrderItem;
import www.zhiyun.com.pager.Expression;
import www.zhiyun.com.pager.PageBean;
import www.zhiyun.com.pager.PageConstants;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {

	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 根据条件来查询进行订单的分页查询
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc)
			throws SQLException {
		/*
		 * 思路 1、获取ps,即每页记录数 2、获取tr，即总记录数 3、获取beanList
		 * 4、创建pageBean对象，对ps，pc，tr，beanList进行添加
		 */
		int ps = PageConstants.ORDER_PAGE_SIEZ;
		StringBuilder sb = new StringBuilder("where 1=1");
		List<Object> paramsList = new ArrayList<Object>();
		for (Expression expr : exprList) {
			sb.append(" and ");
			sb.append(expr.getName()).append(" ");// 添加属性名称
			sb.append(expr.getOperator()).append(" ");// 添加属性符号
			if (!expr.getOperator().equalsIgnoreCase("is null")) {
				sb.append("?");
			}
			paramsList.add(expr.getValue());
		}
		String whereSql = sb.toString();
		String sql = "select count(*) from t_order " + whereSql;
		Number num = (Number) qr.query(sql, new ScalarHandler(),
				paramsList.toArray());
		int tr = num.intValue();// 得到tr
		// 获取beanList
		sql = "select *from t_order " + whereSql
				+ " order by ordertime desc limit ?,?";
		paramsList.add((pc - 1) * ps);
		paramsList.add(ps);
		List<Order> list = qr.query(sql,
				new BeanListHandler<Order>(Order.class), paramsList.toArray());
		// 为订单里面添加订单条目
		for (Order order : list) {
			loadOrderItem(order);
		}

		PageBean<Order> pageBean = new PageBean<Order>();
		// 设置pageBean对象里面的参数
		pageBean.setBeanList(list);
		pageBean.setPc(pc);
		pageBean.setPs(ps);
		pageBean.setTr(tr);
		return pageBean;
	}

	/**
	 * 根据order对象来添加里面的订单条目
	 * 
	 * @param order
	 * @throws SQLException
	 */
	private void loadOrderItem(Order order) throws SQLException {
		String sql = "select *from t_orderitem where oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把maplist集合变成List<OrderItem>对象
	 * 
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			list.add(toOrderItem(map));
		}
		return list;
	}

	/**
	 * 把Map集合变成OrderItem对象
	 * 
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/**
	 * 按用户来查询订单
	 * 
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		Expression e1 = new Expression("uid", "=", uid);
		exprList.add(e1);
		PageBean<Order> pb = findByCriteria(exprList, pc);
		return pb;
	}
	
	
	/**
	 * 查询所有订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		PageBean<Order> pb = findByCriteria(exprList, pc);
		return pb;
	}
	
	/**
	 * 按状态来查询定
	 * @param status
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		Expression e1 = new Expression("status", "=", status+"");
		exprList.add(e1);
		PageBean<Order> pb = findByCriteria(exprList, pc);
		return pb;
	}

	/**
	 * 创建订单
	 * 
	 * @param order
	 * @throws SQLException 
	 */
	public void createOrder(Order order) throws SQLException {
		/*
		 * 思路 
		 * 1、将订单写入数据库 
		 * 2、遍历订单里面的OrderItem，在创建一个二维数组，把订单条目添加进数组，在使用qr进行批处理
		 */

		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = { order.getOid(), order.getOrdertime(),
				order.getTotal(), order.getStatus(), order.getAddress(),
				order.getUser().getUid()};
		qr.update(sql,params);
		
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		//构造订单条目对象的二维数组
		for(int i=0;i<len;i++){
			OrderItem item = order.getOrderItemList().get(i);
			objs[i] = new Object[]{item.getOrderItemId(),item.getQuantity(),
					item.getSubtotal(),item.getBook().getBid(),
					item.getBook().getBname(),item.getBook().getCurrPrice(),
					item.getBook().getImage_b(),order.getOid()};
		}
		
		qr.batch(sql, objs);//执行批处理

	}
	
	/**
	 * 根据订单的id来加载订单
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public Order load(String oid) throws SQLException{
		String sql = "select *from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		//把订单条目加载进订单
		loadOrderItem(order);
		return order;
	}
	
	/**
	 * 根据oid查询订单状态
	 * @param oid
	 * @return
	 * @throws SQLException 
	 */
	public int getStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),oid);
		return number.intValue();
	}
	
	/**
	 * 修改订单状态
	 * @param uid
	 * @param status
	 * @throws SQLException 
	 */
	public void updateStatus(String oid,int status) throws SQLException{
		String sql = "update t_order set status=? where oid=?";
		qr.update(sql,status,oid);
	}

}
