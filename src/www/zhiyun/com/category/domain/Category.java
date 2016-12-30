package www.zhiyun.com.category.domain;

import java.util.List;

/**
 * 分类的实体层
 * @author 1
 *
 */
public class Category {

	/*
	 * 其实这个类是一级分类和二级分类共用的一个类。在数据库表中也是相互对应的
	 * 子父类之间自身相关联
	 */
	private String cid;//分类的id，主键
	private String cname;//分类名称
	private Category parent;//该类的父分类
	private String desc;//对分类的描述
	private List<Category> children;//分类的子分类
	public Category(String cid, String cname, Category parent, String desc,
			List<Category> children) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.parent = parent;
		this.desc = desc;
		this.children = children;
	}
	public Category() {
		super();
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", parent="
				+ parent + ", desc=" + desc + ", children=" + children + "]";
	}
}
