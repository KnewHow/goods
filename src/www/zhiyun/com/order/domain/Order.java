package www.zhiyun.com.order.domain;

import java.util.List;

import www.zhiyun.com.user.domain.User;


/**
 * 订单的实体层
 * @author 1
 *
 */
public class Order {

	private String oid;//主键
	private String ordertime;//下单时间
	private double total;//总计
	private int status;//1表示未付款，2表示已付款但是未收货，3表示已发货但没有确认，4表示确认收货，交易成功，5表示取消订单
	private String address;//收货地址
	private User user;//订单所属的人
	
	private List<OrderItem> orderItemList;
	public Order(String oid, String ordertime, double total, int status,
			String address, User user, List<OrderItem> orderItemList) {
		super();
		this.oid = oid;
		this.ordertime = ordertime;
		this.total = total;
		this.status = status;
		this.address = address;
		this.user = user;
		this.orderItemList = orderItemList;
	}
	public Order() {
		super();
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total="
				+ total + ", status=" + status + ", address=" + address
				+ ", user=" + user + ", orderItemList=" + orderItemList + "]";
	}
	
	
	
}
