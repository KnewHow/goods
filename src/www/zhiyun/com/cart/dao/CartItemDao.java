package www.zhiyun.com.cart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.cart.domain.CartItem;
import www.zhiyun.com.user.domain.User;


import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
/**
 * 购物条目的持久层
 * @author 1
 *
 */
public class CartItemDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 根据用户来查找的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem> findByUser(String uid){
		try{
			String sql = "select *from t_cartItem c, t_book b where c.bid = b.bid and uid=?";
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),uid);
			return toCartItemList(mapList);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据uid和bid来查询购物车条目
	 * @param uid
	 * @param bid
	 * @return
	 */
	public CartItem findByUidAndBid(String uid,String bid){
		try{
			String sql = "select *from t_cartItem where uid=? and bid=?";
			Map<String, Object> map = qr.query(sql, new MapHandler(),uid,bid);
			return toCartItem(map);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 修改条目的数量
	 * @param cartItemId
	 * @param quantity
	 */
	public void updateQuantity(String cartItemId,int quantity){
		try{
			String sql = "update t_cartItem set quantity=? where cartItemId=?";
			qr.update(sql,quantity,cartItemId);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 添加CartItem对象
	 * @param cartItem
	 */
	public void addCartItem(CartItem cartItem){
		try{
			String sql = "insert into t_cartItem(cartItemId,quantity,bid,uid) values(?,?,?,?)";
			System.out.println(cartItem.toString());
			Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),
					cartItem.getBook().getBid(),cartItem.getUser().getUid()};
			qr.update(sql,params);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 批量删除CartItem表中的购物车条目
	 * @param cartItemIds
	 */
	public void  batchDelete(String cartItemIds){
		try{
			/*思路
			 * 1、首先传递过来的是以逗号隔开的id数组，我们首先要进行打散
			 * 2、创建删除的where子句
			 * 3、使用qr执行之
			 * 
			 */
			String sql = "delete from t_cartItem where ";
			Object[] cartItemArray = cartItemIds.split(",");
			sql = sql+getWhereSql(cartItemArray.length);
			qr.update(sql,cartItemArray);//后面的这个参数，必须是Object类型的数组
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据CartItemId查询对象并返回
	 * @param id
	 * @return
	 */
	public CartItem findByCartItemId(String id){
		try{
			//多表查询
			String sql ="select *from t_cartItem c ,t_book b where c.bid=b.bid and c.cartItemId=?";
			Map<String,Object> map = qr.query(sql, new MapHandler(),id);
			return toCartItem(map);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 用来加载已经被选择的购物车条目
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try{
			Object[] cartItemArray = cartItemIds.split(",");
			String sql = "select * from t_cartItem c,t_book b where c.bid=b.bid and "+
					getWhereSql(cartItemArray.length);
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),cartItemArray);
			return toCartItemList(mapList);
		}catch(Exception e){
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * 构建批量删除的where子句
	 * @param len
	 * @return
	 */
	private String getWhereSql(int len){
		StringBuilder sb = new StringBuilder("cartItemId in(");
		for(int i=0;i<len;i++){
			sb.append("?");
			if(i<len-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 把map集合变成CartItem对象
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String,Object> map){
		if(map==null || map.size()==0) return null;
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	/**
	 * 把一个List的Map集合变成CartItem对象
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for(Map<String,Object> map:mapList){
			cartItemList.add(toCartItem(map));
		}
		return cartItemList;
	}
	
}
