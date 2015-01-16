package nivance.impl;

import nivance.dbpapi.shell.DBPShell;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;

@Component(immediate = true)
@Instantiate
@Provides(specifications = DBPShell.class)
public class DBPShellImpl implements DBPShell {

	@ServiceProperty(name = "osgi.command.scope")
	String scope = "dbpapi";

	@ServiceProperty(name = "osgi.command.function")
	String[] function = new String[] { "version", "showkeys", "showinfo",
			"setinfo", "status", "testinsert" };

	BundleContext context;

	public DBPShellImpl(BundleContext context) {
		super();
		this.context = context;
	}

	@Validate
	public void setProvider() {

	}

	@Override
	public void version() {
		System.out.println("version = " + context.getBundle().getVersion());
	}

	@Override
	public void showkeys() {
		System.out
				.println("keys: = " + DBPPropertyConfigurer.getPropertyKeys());
	}

	@Override
	public void showinfo(String[] keys) {
		for (String key : keys) {
			Object object = DBPPropertyConfigurer.getContextProperty(key);
			if (object != null) {
				System.out.println(key + "'s value is : " + object);
			} else {
				System.out.println(key + "'s value is null ");
			}
		}
	}

	@Override
	public void setinfo(String key, String value) {
//		if (key != null && key.equals("mysql")) {
//			if (StringUtils.lowerCase(value).matches(
//					"[a-z]+:[a-z]+://(\\d+.){4}:(\\d+)/\\w+@\\w+:\\w+")) {
//				ComboPooledDataSource dataSource = (ComboPooledDataSource) SpringContextUtil
//						.getBean("dataSource");
//				String[] values = value.split("@");
//				dataSource.setJdbcUrl(values[0]);
//				String[] info = values[1].split(":");
//				try {
//					dataSource.softReset(info[0], info[1]);
//				} catch (SQLException e) {
//					// e.printStackTrace();
//				}
//				DBPPropertyConfigurer.setContextProperty("mysql.jdbc.url",
//						dataSource.getJdbcUrl());
//				DBPPropertyConfigurer.setContextProperty("mysql.jdbc.username",
//						info[0]);
//				DBPPropertyConfigurer.setContextProperty("mysql.jdbc.password",
//						info[1]);
//				System.out.println("new info of mysql is active ");
//			} else {
//				System.err
//						.println("command should be like {dbpapi:setinfo mysql jdbc:mysql://192.168.3.159:3306/para@user:passwd}");
//			}
//		} else if (key != null && key.equals("cassandra")) {
//			System.out.println("cassandra is not supported. ");
//		} else {
//			System.err.println("not support key{" + key
//					+ "}, just support key {mysql}");
//		}
	}

	@Override
	public void status() {
//		CassandraTemplate template = (CassandraTemplate) SpringContextUtil
//				.getBean("cassandraTemplate");
//		ComboPooledDataSource dataSource = (ComboPooledDataSource) SpringContextUtil
//				.getBean("dataSource");
//		try {
//			String jdbcurl = dataSource.getJdbcUrl();
//			String user = dataSource.getUser();
//			System.out.println("mysql info :" + jdbcurl + "@" + user);
//			String active = "active", fail = "fail";
//			boolean isclose = dataSource.getConnection().isClosed();
//			System.out.println("the status of mysql is "
//					+ (!isclose ? active : fail));
//		} catch (SQLException e) {
//			// e.printStackTrace();
//		}
//		List<RingMember> members = (List<RingMember>) template.cqlOps().describeRing();
//		Object contactPoints = DBPPropertyConfigurer
//				.getContextProperty("cassandra.contactPoints");
//		Object port = DBPPropertyConfigurer
//				.getContextProperty("cassandra.port");
//		System.out.println("the config cassandra nodes are {" + contactPoints
//				+ "}@" + port);
//		StringBuilder sb = new StringBuilder();
//		for (RingMember member : members) {
//			sb.append(member.address).append(",");
//		}
//		System.out.println("the using cassandra nodes are {"
//				+ sb.delete(sb.length() - 1, sb.length()) + "}@" + port);
	}

	@Override
	public void testinsert(String messageid) {
//		CoreMessageidUnique cou = new CoreMessageidUnique();
//		cou.setMessageid(messageid);
//		MessageidUniqueCassDao cassDao = (MessageidUniqueCassDao) SpringContextUtil
//				.getBean("messageidUniqueCassDaoImpl");
//		cou.setLtype("loto");
//		try {
//			cassDao.insert(cou);
//		} catch (JPAException e) {
//			e.printStackTrace();
//		}
	}
}
