package nivance.mybatis.gens.plugin.mybatis3;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getAliasedEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getSelectListPhrase;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class Annotated2JavaPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	
	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<Method> methods = interfaze.getMethods();
		//------------------------------把自带的annotion标签替换-------------------------
		for(Method method : methods){
			FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());
			StringBuilder annotaionStr = new StringBuilder();
			if("deleteByPrimaryKey".equalsIgnoreCase(method.getName())){
				annotaionStr.append("@DeleteProvider(type=");
			}else if("insert".equalsIgnoreCase(method.getName())){
				annotaionStr.append("@InsertProvider(type=");
			}else if("selectByPrimaryKey".equalsIgnoreCase(method.getName())){
				annotaionStr.append("@SelectProvider(type=");
			}else if("updateByPrimaryKey".equalsIgnoreCase(method.getName())){
				annotaionStr.append("@UpdateProvider(type=");
			}else{
				continue;
			}
			method.getAnnotations().clear();
			annotaionStr.append(fqjt.getShortName());
			annotaionStr.append(".class, method=\"");
			annotaionStr.append(method.getName());
			annotaionStr.append("\")");
			method.addAnnotation(annotaionStr.toString());
		}
		//------------------------------添加表名参数-------------------------
		for(Method method : methods){
			//只有applyWhere 不用加表名
			if(!"applyWhere".equals(method.getName())){
				List<Parameter> parameters = method.getParameters();
				for(Parameter p : parameters){
					if(p.getAnnotations().size()>0){
						continue;
					}
					if("example".equals(p.getName())){
						p.addAnnotation("@Param(\"example\")");
					}else{
						p.addAnnotation("@Param(\"record\")");
					}
				}
				method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "table","@Param(\"table\")"));
			}
		}
		return true;
	}
	
	@Override
	public boolean providerUpdateByExampleSelectiveMethodGenerated(
			Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$

        importedTypes.add(new FullyQualifiedJavaType("java.util.Map")); //$NON-NLS-1$
        
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
        
        FullyQualifiedJavaType record =
            introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");", //$NON-NLS-1$
                record.getShortName(), record.getShortName()));

        FullyQualifiedJavaType example =
            new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", //$NON-NLS-1$
                example.getShortName(), example.getShortName()));

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        
        method.addBodyLine(String.format("UPDATE(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", //$NON-NLS-1$
                    getGetterMethodName(introspectedColumn.getJavaProperty(),
                            introspectedColumn.getFullyQualifiedJavaType())));
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getParameterClause(introspectedColumn));
            sb.insert(2, "record."); //$NON-NLS-1$
            
            method.addBodyLine(String.format("SET(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)),
                    sb.toString()));
                
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }

            method.addBodyLine(""); //$NON-NLS-1$
        }
        
        method.addBodyLine("applyWhere(example, true);"); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	
	@Override
	public boolean providerUpdateByExampleWithoutBLOBsMethodGenerated(
			Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
		method.setName(introspectedTable.getUpdateByExampleStatementId());
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$

        importedTypes.add(new FullyQualifiedJavaType("java.util.Map")); //$NON-NLS-1$
        
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        
        method.addBodyLine(String.format("UPDATE(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getParameterClause(introspectedColumn));
            sb.insert(2, "record."); //$NON-NLS-1$
            
            method.addBodyLine(String.format("SET(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)),
                    sb.toString()));
        }
        
        method.addBodyLine(""); //$NON-NLS-1$
        
        FullyQualifiedJavaType example =
            new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", //$NON-NLS-1$
                example.getShortName(), example.getShortName()));
        
        method.addBodyLine("applyWhere(example, true);"); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	@Override
	public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(
			Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
		method.setName(introspectedTable.getSelectByExampleStatementId());
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        
        FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");",example.getShortName(), example.getShortName()));
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        boolean distinctCheck = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
            if (distinctCheck) {
                method.addBodyLine("if (example != null && example.isDistinct()) {"); //$NON-NLS-1$
                method.addBodyLine(String.format("SELECT_DISTINCT(\"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("} else {"); //$NON-NLS-1$
                method.addBodyLine(String.format("SELECT(\"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getSelectListPhrase(introspectedColumn))));
                method.addBodyLine("}"); //$NON-NLS-1$
            } else {
                method.addBodyLine(String.format("SELECT(\"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getSelectListPhrase(introspectedColumn))));
            }
            
            distinctCheck = false;
        }

        method.addBodyLine(String.format("FROM(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("applyWhere(example, false);"); //$NON-NLS-1$
        
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("if (example != null && example.getOrderByClause() != null) {"); //$NON-NLS-1$
        method.addBodyLine("ORDER_BY(example.getOrderByClause());"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	
	@Override
	public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(
			Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.WHERE"); //$NON-NLS-1$

        FullyQualifiedJavaType record = introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
//        method.addParameter(new Parameter(record, "record")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
        method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");",record.getShortName(), record.getShortName()));
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        
        method.addBodyLine(String.format("UPDATE(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", //$NON-NLS-1$
                    getGetterMethodName(introspectedColumn.getJavaProperty(),
                            introspectedColumn.getFullyQualifiedJavaType())));
            }

            method.addBodyLine(String.format("SET(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
                
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }

            method.addBodyLine(""); //$NON-NLS-1$
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            method.addBodyLine(String.format("WHERE(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn)));
        }
        
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	
	@Override
	public boolean providerDeleteByExampleMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");",example.getShortName(), example.getShortName()));
        
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        method.addBodyLine(String.format("DELETE_FROM(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("applyWhere(example, false);"); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	
	@Override
	public boolean providerInsertSelectiveMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.VALUES"); //$NON-NLS-1$

        FullyQualifiedJavaType record = introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addParameter(new Parameter(fqjt, "record")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
        method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");",record.getShortName(), record.getShortName()));
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        
        context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);

        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        method.addBodyLine(String.format("INSERT_INTO(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));

        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }
            method.addBodyLine(""); //$NON-NLS-1$
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", //$NON-NLS-1$
                    getGetterMethodName(introspectedColumn.getJavaProperty(),
                            introspectedColumn.getFullyQualifiedJavaType())));
            }
            method.addBodyLine(String.format("VALUES(\"%s\", \"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }
        }
        
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }

	@Override
	public boolean providerCountByExampleMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//清空原来的内容
		method.getBodyLines().clear();
		method.getParameters().clear();
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(fqjt);
//        Method method = new Method( introspectedTable.getCountByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
        method.addBodyLine("String table = (String) parameter.get(\"table\");");
        FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");",example.getShortName(), example.getShortName()));
            
        context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);
        
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        method.addBodyLine("SELECT(\"count(*)\");"); //$NON-NLS-1$
        method.addBodyLine(String.format("FROM(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("applyWhere(example, false);"); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
        return false;
    }
	
	@Override
	public boolean providerGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//将@insert.... 修改为@insertProvider....标签
		initInsertProvider(topLevelClass, introspectedTable);
		initDeleteByPkProvider(topLevelClass, introspectedTable);
		initUpdateByPk(topLevelClass, introspectedTable);
		initSelectByPKProvider(topLevelClass, introspectedTable);
		
		List<Method> methods = topLevelClass.getMethods();
		for(Method method : methods){
			List<String> lines = method.getBodyLines();
			for(int i=0;i<lines.size();i++){
				String line = lines.get(i);
				if(StringUtils.contains(line, "FROM(")){
					line = "FROM(table);";
				}else if(StringUtils.contains(line, "DELETE_FROM(")){
					line = "DELETE_FROM(table);";
				}else if(StringUtils.contains(line, "INSERT_INTO(")){
					line = "INSERT_INTO(table);";
				}else if(StringUtils.contains(line, "UPDATE(")){
					line = "UPDATE(table);";
				}else{
					continue;
				}
				lines.set(i,line);
//				method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "table"));
			}
		}
		return super.providerGenerated(topLevelClass, introspectedTable);
	}
	
	
	private void initInsertProvider(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
		Set<String> staticImports = new TreeSet<String>();
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
		staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO"); //$NON-NLS-1$
		staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
		staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.VALUES"); //$NON-NLS-1$

		FullyQualifiedJavaType record = introspectedTable.getRules().calculateAllFieldsClass();
		importedTypes.add(record);
		Method method = new Method(introspectedTable.getInsertStatementId());
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), "parameter")); //$NON-NLS-1$
//		method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");", //$NON-NLS-1$
//	                record.getShortName(), record.getShortName()));
		FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
//        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", example.getShortName(), example.getShortName()));
		method.addBodyLine("String table = (String) parameter.get(\"table\");");
		 
		context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);
		
		method.addBodyLine("BEGIN();"); //$NON-NLS-1$
		method.addBodyLine(String.format("INSERT_INTO(\"%s\");", //$NON-NLS-1$
				escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
		for (IntrospectedColumn introspectedColumn : introspectedTable
				.getAllColumns()) {
			method.addBodyLine(String
					.format("VALUES(\"%s\", \"%s\");", //$NON-NLS-1$
							escapeStringForJava(getEscapedColumnName(introspectedColumn)),
							getParameterClause(introspectedColumn,"record.")));
		}
		method.addBodyLine(""); //$NON-NLS-1$
		method.addBodyLine("return SQL();"); //$NON-NLS-1$
		topLevelClass.addStaticImports(staticImports);
		topLevelClass.addImportedTypes(importedTypes);
		topLevelClass.addMethod(method);
	}
	
	private void initDeleteByPkProvider(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        FullyQualifiedJavaType key = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        importedTypes.add(key);
        Method method = new Method( introspectedTable.getDeleteByPrimaryKeyStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
//		method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");",key.getShortName(), key.getShortName()));
		method.addBodyLine("String table = (String) parameter.get(\"table\");");
        context.getCommentGenerator().addGeneralMethodComment(method,introspectedTable);
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        method.addBodyLine(String.format("DELETE_FROM(\"%s\");",  escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            method.addBodyLine(String.format("WHERE(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
        }
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
	}
	
	private void initSelectByPKProvider(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        FullyQualifiedJavaType record = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        importedTypes.add(record);
        Method method = new Method(introspectedTable.getSelectByPrimaryKeyStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addParameter(new Parameter(record, "key")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
//		method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");",record.getShortName(), record.getShortName()));
		method.addBodyLine("String table = (String) parameter.get(\"table\");");
		
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                method.addBodyLine(String.format("SELECT(\"%s\");", //$NON-NLS-1$
                    escapeStringForJava(getSelectListPhrase(introspectedColumn))));
        }
        method.addBodyLine(String.format("FROM(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            method.addBodyLine(String.format("WHERE(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
        }
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
	}
	
	private void initUpdateByPk(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.WHERE"); //$NON-NLS-1$
        FullyQualifiedJavaType record = introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        Method method = new Method(introspectedTable.getUpdateByPrimaryKeyStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
//        method.addParameter(new Parameter(fqjt, "record")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
//		method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");",record.getShortName(), record.getShortName()));
		method.addBodyLine("String table = (String) parameter.get(\"table\");");
		
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        method.addBodyLine(String.format("UPDATE(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            method.addBodyLine(String.format("SET(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
        }
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            method.addBodyLine(String.format("WHERE(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn,"record.")));
        }
        method.addBodyLine(""); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        topLevelClass.addStaticImports(staticImports);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
	}
}
