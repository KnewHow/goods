package www.zhiyun.com.user.domain;

/**
 * 用户对象的实体层
 * @author 1
 *
 */
public class User {

	//对于数据库
	private String uid;//用户id
	private String loginname;//登录名称
	private String loginpass;//登录密码
	private String email;//邮箱
	private boolean status;//0表示已注册，但是未激活，1已经激活
	private String activationCode;//激活码
	
	//对应表单
	private String reloginpass;//确认密码
	private String verifyCode;//验证码
	private String newLoginpass;
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getNewLoginpass() {
		return newLoginpass;
	}
	public void setNewLoginpass(String newLoginpass) {
		this.newLoginpass = newLoginpass;
	}
	public User(String uid, String loginname, String loginpass, String email,
			boolean status, String activationCode, String reloginpass,
			String verifyCode, String newLoginpass) {
		super();
		this.uid = uid;
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.email = email;
		this.status = status;
		this.activationCode = activationCode;
		this.reloginpass = reloginpass;
		this.verifyCode = verifyCode;
		this.newLoginpass = newLoginpass;
	}
	public User() {
		super();
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", email=" + email + ", status=" + status
				+ ", activationCode=" + activationCode + ", reloginpass="
				+ reloginpass + ", verifyCode=" + verifyCode
				+ ", newLoginpass=" + newLoginpass + "]";
	}
	
}
