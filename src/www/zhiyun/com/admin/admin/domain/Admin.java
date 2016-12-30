package www.zhiyun.com.admin.admin.domain;
/**
 * Admin对象的实体层
 * @author Administrator
 *
 */
public class Admin {

	private String adminId;//Admin的ID
	private String adminname;//Admin的用户名
	private String adminpwd;//Admin的密码
	private String starttime;//Admin的开始的登录时间
	private String endtime;//Admin的结束时间
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getAdminpwd() {
		return adminpwd;
	}
	public void setAdminpwd(String adminpwd) {
		this.adminpwd = adminpwd;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminname=" + adminname
				+ ", adminpwd=" + adminpwd + ", starttime=" + starttime
				+ ", endtime=" + endtime + "]";
	}
	
}
