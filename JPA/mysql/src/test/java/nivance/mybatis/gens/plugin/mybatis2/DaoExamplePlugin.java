package nivance.mybatis.gens.plugin.mybatis2;


import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class DaoExamplePlugin extends PluginAdapter {

	public DaoExamplePlugin() {
        super();
	}
	
	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<String> lines = new ArrayList<String>();
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
			topLevelClass.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
			String fieldName = JavaBeansUtil.getCamelCaseString(introspectedColumn.getActualColumnName().toLowerCase(), true);
			String getterName = "get"+fieldName;
			String andEqualTo = "and"+fieldName+"EqualTo";
			lines.add(("if(info."+getterName+"()!=null){"));
			lines.add("criteria."+andEqualTo+"(info."+getterName+"());");
			lines.add("}");
		}
		FullyQualifiedJavaType record = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        Method method = new Method();
        method.setName("getExample");
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType exampletype = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        method.addParameter(new Parameter(record, "info")); //$NON-NLS-1$
        method.setReturnType(exampletype);
        method.addBodyLine(exampletype.getShortName()+" example = new "+exampletype.getShortName()+"();");
        method.addBodyLine("Criteria criteria = example.createCriteria();");
        method.addBodyLines(lines);
		method.addBodyLine("return example;");
		topLevelClass.addMethod(method);
		topLevelClass.addImportedType(exampletype.getPackageName()+"."+exampletype.getShortName()+"."+"Criteria");
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
	@Override
	public boolean validate(List<String> arg0) {
		System.out.println("validate:"+arg0);
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(JavaBeansUtil.getCamelCaseString("result_count", true));
	}
}
