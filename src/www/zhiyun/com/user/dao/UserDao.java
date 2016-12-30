package www.zhiyun.com.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import www.zhiyun.com.user.domain.User;

import cn.itcast.jdbc.TxQueryRunner;
/**
 * 持久层
 * @author 1
 *
 */
public class UserDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * ajax校验用户名是否存在
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try{
			String sql = "select count(*) from t_user where loginname=?";
			Number num = (Number) qr.query(sql, new ScalarHandler(),loginname);
			return num.intValue()==0;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ajax校验邮箱是否唯一
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try{
			String sql = "select count(*) from t_user where email=?";
			Number num = (Number) qr.query(sql, new ScalarHandler(),email);
			return num.intValue()==0;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void addUser(User user){
		try{
			String sql = "insert into t_user values(?,?,?,?,?,?)";
			Object[] params ={user.getUid(),user.getLoginname(),
					user.getLoginpass(),user.getEmail(),
					user.getStatus(),user.getActivationCode()};
			qr.update(sql,params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过激活码来查询用户
	 * @param activationCode
	 * @return
	 */
	public User findUserByActivationCode(String activationCode){
		try{
			String sql = "select *from t_user where activationCode=?";
			return qr.query(sql, new BeanHandler<User>(User.class),activationCode);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改用户状态
	 * @param uid
	 * @param status
	 */
	public void updateStatus(String uid,boolean status){
		try{
			String sql = "update t_user set status=? where uid=?";
			qr.update(sql,status,uid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据用户名来查找用户
	 * @param loginname
	 * @return
	 */
	public User findByLoginname(String loginname){
		try{
			String sql = "select *from t_user where loginname=?";
			return qr.query(sql, new BeanHandler<User>(User.class),loginname);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改密码
	 * @param loginpass
	 * @param uid
	 */
	public void updateLoginpass(String newLoginpass,String uid){
		try{
			String sql = "update t_user set loginpass=? where uid=?";
			qr.update(sql,newLoginpass,uid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	
	public User findByUid(String uid){
		try{
			String sql = "select *from t_user where uid=?";
			return qr.query(sql, new BeanHandler<User>(User.class),uid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按照邮箱来查询用户数量
	 * @param email
	 * @return
	 */
	public int findCountByEmail(String email) {
		try {
			String sql = "select count(*) from t_user where email=?";
			Number num = (Number) qr.query(sql, new ScalarHandler(),email);
			int sum = num.intValue();
			return sum;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 按照邮箱来查询用户
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email)  {
		try {
			String sql = "select *from t_user where email=?";
			return qr.query(sql, new BeanHandler<User>(User.class),email);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * 
	 * 重新设置新的密码
	 * @param uid
	 * @param newPassword
	 * @throws SQLException
	 */
	public void updatePassword(String uid, String newPassword)
			throws SQLException {
		String sql = "update t_user set loginpass=? where uid =?";
		this.qr.update(sql, new Object[] { newPassword, uid });
	}
	
	
}
