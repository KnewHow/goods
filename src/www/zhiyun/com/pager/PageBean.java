package www.zhiyun.com.pager;

import java.util.List;

/**
 * 分页的实体层
 * 
 * @author 1
 * 
 */
public class PageBean<T> {
	private int tr;// 总记录数，totalRecode
	private int pc;// 当前页数pageCode
	private int ps;// 每页记录数 pageSize
	private String url;// 请求的路径
	private List<T> beanList;
//  private int tp;//总页数，但是我们不需要让它变成成员变量，因为他可以算出来
	public PageBean(int tr, int pc, int ps, String url, List<T> beanList) {
		super();
		this.tr = tr;
		this.pc = pc;
		this.ps = ps;
		this.url = url;
		this.beanList = beanList;
	}

	public int getTp() {
		int tp = tr / ps;
		return tr % ps == 0 ? tp : tp + 1;
	}

	public PageBean() {
		super();
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}

}
