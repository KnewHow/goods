package www.zhiyun.com.cart.domain;

import java.math.BigDecimal;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.user.domain.User;


/**
 * 购物车条目对象的实体层
 * 
 * @author 1
 * 
 */
public class CartItem {

	private String cartItemId;// 主键
	private int quantity;// 数量
	private Book book;// 所属的图书
	private User user;// 所属的用户

	// 添加一个小计的方法
	public double getSubtotal() {
		//使用java为小数提供的方法，可以避免计算的误差
		BigDecimal b1 = new BigDecimal(book.getCurrPrice() + "");
		BigDecimal b2 = new BigDecimal(quantity+"");
		BigDecimal b3 = b1.multiply(b2);
		return b3.doubleValue();
	}

	public CartItem(String cartItemId, int quantity, Book book, User user) {
		super();
		this.cartItemId = cartItemId;
		this.quantity = quantity;
		this.book = book;
		this.user = user;
	}

	public CartItem() {
		super();
	}

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", quantity=" + quantity
				+ ", book=" + book + ", user=" + user + "]";
	}

}
