package www.zhiyun.com.admin.admin.service;

import java.sql.SQLException;
import java.util.Date;

import www.zhiyun.com.admin.admin.dao.AdminDao;
import www.zhiyun.com.admin.admin.domain.Admin;


/**
 * admin对象的业务层
 * @author Administrator
 *
 */
public class AdminService {
	private AdminDao adminDao = new AdminDao();
	
	
	/**
	 * 管理员登录页面
	 * @param form
	 * @return
	 * @throws AdminException 
	 */
	public Admin login(Admin form) throws AdminException{
		try {
			Admin admin = adminDao.findAdminByNameAndPassword(form.getAdminname(), form.getAdminpwd());
			if(admin==null){
				throw new AdminException("用户名或者密码不正确");
			}
			//设置登录时间
			String starttime = String.format("%tF %<tT", new Date());
			admin.setStarttime(starttime);
			//把时间数据添加到数据库里面
			adminDao.setStarttime(admin.getAdminId(), starttime);
			return admin;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
	
	/**
	 *管理员退出业务 
	 * @param form
	 * @return
	 * @throws AdminException
	 */
	public void quit(Admin form){
		try {
			adminDao.setEndtime(form.getAdminId(),String.format("%tF %<tT", new Date()));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
