package nivance.mybatis.gens.plugin.mybatis3;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;


public class SqlDaoSupportPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO add SqlDaoSupport
		String daoName = "StaticTableDaoSupport";
//		String daoName = "SqlDaoSupport";
		String pojo = introspectedTable.getBaseRecordType();
		String example = introspectedTable.getExampleType();
//		FullyQualifiedJavaType exampletype = new FullyQualifiedJavaType(example);
		interfaze.addSuperInterface(new FullyQualifiedJavaType(daoName+"<"
				+ pojo + "," + example + ","
				+ introspectedTable.getPrimaryKeyType() + ">"));
		interfaze.addImportedType(new FullyQualifiedJavaType(
				"com.cwl.iso.db.mysql.jpa.iface."+daoName));
//		interfaze.addImportedType(new FullyQualifiedJavaType(
//				"nivance.dbpapi.exception.JPAException"));

		// int sumByExample(D example)throws JPAException;
//		Method method = new Method();
//		method.setName("sumByExample");
//		method.setVisibility(JavaVisibility.PUBLIC);
//		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
//		method.addParameter(new Parameter(exampletype, "example"));
//		interfaze.addMethod(method);
		
//		//------------------truncate interface------------------
//		method = new Method();
//		method.setName("truncate");
//		method.setVisibility(JavaVisibility.PUBLIC);
//		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//		interfaze.addMethod(method);
//		//------------------truncate interface------------------
		
//		List<Method> methods = interfaze.getMethods();
//		for (Method m : methods) {
//			m.addException(new FullyQualifiedJavaType("JPAException"));
//		}
		return true;
	}
	
	@Override
	public boolean providerGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
//		// TODO add SqlDaoSupport
//		String daoName = "ProviderIface";
//		String pojo = introspectedTable.getBaseRecordType();
//		String example = introspectedTable.getExampleType();
//		FullyQualifiedJavaType exampletype = new FullyQualifiedJavaType(example);
//		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(daoName+"<"
//				+ pojo + "," + example + ">"));
//		topLevelClass.addImportedType(new FullyQualifiedJavaType(
//				"com.cwl.iso.db.mysql.jpa.iface."+daoName));
		return true;
	}
}
