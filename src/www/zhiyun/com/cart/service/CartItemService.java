package www.zhiyun.com.cart.service;

import java.util.List;

import www.zhiyun.com.cart.dao.CartItemDao;
import www.zhiyun.com.cart.domain.CartItem;

import cn.itcast.commons.CommonUtils;

/**
 * 购物车条目对象的业务层
 * 
 * @author 1
 * 
 */
public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();

	/**
	 * 我的购物车
	 * 
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid) {
		return cartItemDao.findByUser(uid);
	}

	/**
	 * 添加我的购物车
	 * 
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		/*
		 * 思路 1、首先去数据库里面按照bid和uid来查询图书条目 如果存在，说明已经购买了类型的书，修改其数量
		 * 如果不存在，说明书的数量是新购买的，添加到数据中
		 */
		CartItem _cartitem = cartItemDao.findByUidAndBid(cartItem.getUser()
				.getUid(), cartItem.getBook().getBid());
		if(_cartitem==null){//如果为空，说明书新购买的
			//设置uuid
			cartItem.setCartItemId(CommonUtils.uuid());
			cartItemDao.addCartItem(cartItem);
		}else{//如果不为空，说明之前已经购买过此图书
			int quantity = cartItem.getQuantity()+_cartitem.getQuantity();
			//修改其数量
			cartItemDao.updateQuantity(_cartitem.getCartItemId(), quantity);
		}
	}
	
	/**
	 * 批量删除购物车里面的条目
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		cartItemDao.batchDelete(cartItemIds);
	}
	
	/**
	 * 修改数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		//首先修改属性
		cartItemDao.updateQuantity(cartItemId, quantity);
		//然后进行查询
		return cartItemDao.findByCartItemId(cartItemId);
	}
	
	public List<CartItem> loadCartItems(String cartItemIds){
		return cartItemDao.loadCartItems(cartItemIds);
	}
}
