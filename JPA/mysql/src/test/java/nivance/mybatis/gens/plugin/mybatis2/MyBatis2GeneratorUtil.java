package nivance.mybatis.gens.plugin.mybatis2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import nivance.mybatis.gens.core.FtlClassInfo;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class MyBatis2GeneratorUtil {
	
	public static void main(String[] args) throws Exception {
//		String xmlName = "generatorConfigOracle.xml";
		String xmlName = "generatorConfigMysql.xml";
		String tmpSource = "src/test/java";
		String basePackage = "nivance.dbpimpl.sql";
		testGenerateMyBatis2(xmlName,tmpSource,basePackage);
	}

	public static void testGenerateMyBatis2(String xmlName,String tmpSource,String basePackage) throws Exception {
		List<String> warnings = new ArrayList<String>();
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(MyBatis2GeneratorUtil.class
				.getResourceAsStream(xmlName));
		DefaultShellCallback shellCallback = new DefaultShellCallback(true);
		try {
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,shellCallback, warnings);
			myBatisGenerator.generate(null);
			List<Context> configs = config.getContexts();
			String targetSource = "src/gens/java";
			List<FtlClassInfo> clazzinfos = new ArrayList<FtlClassInfo>();
			for(Context context : configs){
				targetSource = context.getJavaModelGeneratorConfiguration().getTargetProject();
				genIpojoDaoAndGetTableInfo(context,clazzinfos,basePackage);
			}
			genSpringXml(clazzinfos, tmpSource, "Mybatis2IpojoDAOXml.ftl", new File(targetSource));
			genSqlMapXml(clazzinfos, tmpSource, "Mybatis2SqlMapXml.ftl", new File(targetSource));
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static void genIpojoDaoAndGetTableInfo(Context config,List<FtlClassInfo> clazzinfos,String targetPackage) {
		String daoTargetSource = config.getJavaClientGeneratorConfiguration().getTargetPackage();
		List<TableConfiguration> list = config.getTableConfigurations();
		for (TableConfiguration table : list) {
			FtlClassInfo classinfo = new FtlClassInfo();
			classinfo.setDomainClass(table.getDomainObjectName());
			classinfo.setDomainObject(StringUtils.uncapitalize(table.getDomainObjectName()));
			classinfo.setPackageName(StringUtils.substringBeforeLast(daoTargetSource, "."));
			classinfo.setTablename(table.getTableName().toUpperCase());
			clazzinfos.add(classinfo);
//			File ipojoDaoDir = getDirectory(targetSource, targetPackage,"ipojodao");
//			genFtl(classinfo, tmpSource, "Mybatis2IpojoDAO.ftl",ipojoDaoDir, "IpojoDao");
		}
	}

	public static File getDirectory(String targetSource, String targetPackage,
			String domain) {
		File project = new File(targetSource);
		// File project = new File(targetProject + File.separatorChar
		// +targetSource);
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			sb.append(File.separatorChar);
		}
		st = new StringTokenizer(domain, "."); //$NON-NLS-1$
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			sb.append(File.separatorChar);
		}
		File directory = new File(project, sb.toString());
		if (!directory.isDirectory()) {
			boolean rc = directory.mkdirs();
			if (!rc) {
				throw new RuntimeException("can't create "
						+ directory.getAbsolutePath());
			}
		}
		return directory;
	}

	public static void genFtl(FtlClassInfo clazzinfo, String tempSource,
			String tmplname, File daoDir, String end) {
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
	
	public static void genSpringXml(List<FtlClassInfo> clazzinfos, String tempSource,
			String tmplname, File daoDir) {
		try {
			freemarker.template.Configuration cfg = new freemarker.template.Configuration();
			cfg.setDirectoryForTemplateLoading(new File(tempSource));
			DefaultObjectWrapper ow = new DefaultObjectWrapper();
			cfg.setObjectWrapper(ow);
			Template temp = cfg.getTemplate(tmplname);
			/* Create a data-model */
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("clazzinfos", clazzinfos);
			File dst = new File(daoDir, "SpringContext-osgiServer.xml");
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
	
	public static void genSqlMapXml(List<FtlClassInfo> clazzinfos, String tempSource,
			String tmplname, File daoDir) {
		try {
			freemarker.template.Configuration cfg = new freemarker.template.Configuration();
			cfg.setDirectoryForTemplateLoading(new File(tempSource));
			DefaultObjectWrapper ow = new DefaultObjectWrapper();
			cfg.setObjectWrapper(ow);
			Template temp = cfg.getTemplate(tmplname);
			/* Create a data-model */
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("clazzinfos", clazzinfos);
			File dst = new File(daoDir, "sql-map-config.xml");
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

}
