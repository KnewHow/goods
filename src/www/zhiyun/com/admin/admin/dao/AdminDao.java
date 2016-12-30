package www.zhiyun.com.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import www.zhiyun.com.admin.admin.domain.Admin;

import cn.itcast.jdbc.TxQueryRunner;

/**
 * Admin对象的持久层
 * @author Administrator
 *
 */
public class AdminDao {
	QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 根据登录名和密码查找用户
	 * @param adminName
	 * @param AdminPwd
	 * @return
	 * @throws SQLException 
	 */
	public Admin findAdminByNameAndPassword(String adminname,String adminpwd) throws SQLException{
		String sql = "select *from t_admin where adminname=? and adminpwd=?";
		Admin admin = qr.query(sql, new BeanHandler<Admin>(Admin.class),adminname,adminpwd);
		return admin;
	}
	
	/**
	 * 设置登录的起始时间
	 * @param adminId
	 * @param starttime
	 * @throws SQLException 
	 */
	public void setStarttime(String adminId,String starttime) throws SQLException{
		String sql = "update t_admin set starttime=? where adminId=?";
		qr.update(sql,starttime,adminId);
	}
	
	public void setEndtime(String adminId,String endtime) throws SQLException{
		String sql = "update t_admin set endtime=? where adminId=?";
		qr.update(sql,endtime,adminId);
	}
}
