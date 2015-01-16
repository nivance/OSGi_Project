package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.util.UUID;
//
//import com.datastax.driver.core.BoundStatement;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.mapping.CQLStatement;
//import nivance.jpa.cassandra.mapping.InsertStatement;
//
//public class TestInsertStatement {
//
//	public static void main(String[] args) {
//		Cluster cluster = Cluster.builder().addContactPoint("192.168.24.21").build();
//		Session session = cluster.connect("test");
//
//		try {
//			CQLStatement insert = new InsertStatement().insertIfNotExist(CoreBO.class, false).prepare(session);
//			int count = 10;
//			long start;
//			BoundStatement[] bss = new BoundStatement[count];
//			for (int i = 0; i < count; i++) {
//				CoreBO bo = new CoreBO();
//				bo.setMerchantid("666666");
//				bo.setMessageid(UUID.randomUUID().toString());
//				bo.setCommand("1000");
//				bo.setLtype("slto");
//				bss[i] = insert.getStatement(bo);
//			}
//			start = System.currentTimeMillis();
//
//			for (int i = 0; i < count; i++) {
//				session.execute(bss[i]);
//			}
//
//			// result.one();
//			System.out.println("execute OKOK:1:" + (System.currentTimeMillis() - start));
//
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//
//		cluster.close();
//	}
//}
