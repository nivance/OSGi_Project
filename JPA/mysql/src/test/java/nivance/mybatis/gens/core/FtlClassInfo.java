package nivance.mybatis.gens.core;


public class FtlClassInfo {
	String domainObject;
	String domainClass;
	String packageName;
	String tablename;
	
	public String getDomainObject() {
		return domainObject;
	}
	public void setDomainObject(String domainObject) {
		this.domainObject = domainObject;
	}
	public String getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(String domainClass) {
		this.domainClass = domainClass;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
}
