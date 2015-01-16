package nivance.mybatis.gens.plugin.mybatis3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nivance.mybatis.gens.core.FtlClassInfo;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class DaoExamplePlugin extends PluginAdapter {

	public DaoExamplePlugin() {
		super();
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		StringBuffer sb = new StringBuffer();
		List<IntrospectedColumn> ics= introspectedTable.getAllColumns();
		for(IntrospectedColumn ic : ics){
			String fieldName = StringUtils.capitalize(MyBatis3GeneratorUtil.TableField2JavaField(ic.getActualColumnName()));
			String getterName = "get" + fieldName;
			String andEqualTo = "and" + fieldName + "EqualTo";
			sb.append("\t\t\t\t").append("if(record." + getterName + "()!=null){").append("\n");
			sb.append("\t\t\t\t").append("criteria." + andEqualTo + "(record." + getterName + "());").append("\n");
			sb.append("\t\t\t\t").append("}").append("\n");
		}
		List<FtlClassInfo> clazzinfos = new ArrayList<FtlClassInfo>();
		TableConfiguration tableConfig = introspectedTable.getTableConfiguration();
		String daoTargetSource = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
		genDao(tableConfig,daoTargetSource, clazzinfos, MyBatis3GeneratorUtil.basePackage, MyBatis3GeneratorUtil.tmpSource,sb.toString());
		return true;
	}

	private static void genDao(TableConfiguration tableConfig,String daoTargetSource,List<FtlClassInfo> clazzinfos,String targetPackage,String tmpSource,String exampleBody) {
		FtlClassInfo classinfo = new FtlClassInfo();
		classinfo.setDomainClass(tableConfig.getDomainObjectName());
		classinfo.setDomainObject(StringUtils.uncapitalize(tableConfig.getDomainObjectName()));
		classinfo.setPackageName(StringUtils.substringBeforeLast(daoTargetSource, "."));
		classinfo.setTablename(tableConfig.getTableName().toUpperCase());
		clazzinfos.add(classinfo);
		File daoDir = MyBatis3GeneratorUtil.getDirectory("src/gens/java", StringUtils.substringBeforeLast(daoTargetSource, "."),"dao");
//		genFtl(classinfo, tmpSource, "Mybatis3Dao.ftl",daoDir, "Dao",exampleBody);
//		genFtl(classinfo, tmpSource, "Mybatis3DynamicTableDao.ftl",daoDir, "Dao",exampleBody);
		genFtl(classinfo, tmpSource, "Mybatis3StaticTableDao.ftl",daoDir, "Dao",exampleBody);
	}
	
	public static void genFtl(FtlClassInfo clazzinfo, String tempSource,
			String tmplname, File daoDir, String end,String exampleBody) {
		try {
			freemarker.template.Configuration cfg = new freemarker.template.Configuration();
			cfg.setDirectoryForTemplateLoading(new File(tempSource));
			DefaultObjectWrapper ow = new DefaultObjectWrapper();
			cfg.setObjectWrapper(ow);
			Template temp = cfg.getTemplate(tmplname);
			/* Create a data-model */
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("domainClazz", clazzinfo.getDomainClass());
			root.put("domainObject", clazzinfo.getDomainObject());
			root.put(
					"packageName",
					clazzinfo.getPackageName().replaceAll(
							"\\" + File.separator, "."));
			root.put("exampleBody", exampleBody);
			File dst = new File(daoDir, clazzinfo.getDomainClass() + end
					+ ".java");
			System.out.println(dst.getAbsolutePath());
			dst.getParentFile().mkdirs();
			FileOutputStream fout = new FileOutputStream(dst);
			Writer out = new OutputStreamWriter(fout);
			temp.process(root, out);
			out.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean validate(List<String> arg0) {
		System.out.println("validate:" + arg0);
		return true;
	}

	public static void main(String[] args) {
		System.out.println(JavaBeansUtil.getCamelCaseString("result_count",
				true));
	}
}
