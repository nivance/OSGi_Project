package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.util.Iterator;
//
//import com.datastax.driver.core.BoundStatement;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Row;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.mapping.SelectStatement;
//import nivance.jpa.cassandra.util.BeanPropertyMapper;
//
//public class TestSelectStatement {
//
//	public static void main(String[] args) {
//		Cluster cluster = Cluster.builder().addContactPoint("192.168.24.21").build();
//		Session session = cluster.connect("test");
//		try {
//			SelectStatement select = new SelectStatement();
//			select.findAll(CoreBO.class).prepare(session);
//			// System.out.println("mm=" + mm);
//
//			int count = 1;
//			long start;
//			CoreBO bos[] = new CoreBO[count];
//			for (int i = 0; i < count; i++) {
//				bos[i] = new CoreBO();
//				CoreBO bo = bos[i];
//				bo.setMerchantid("666666");
//				bo.setMessageid("ea7e0c82-b961-452a-b49e-24623630efbe");
//			}
//			start = System.currentTimeMillis();
//
//			BoundStatement[] bss = new BoundStatement[count];
//			for (int i = 0; i < count; i++) {
//				bss[i] = select.getStatement(bos[i]);
//				bss[i].setFetchSize(100);
//			}
//			start = System.currentTimeMillis();
//
//			for (int i = 0; i < count; i++) {
//				ResultSet result = session.execute(bss[i]);
//				Row row = result.one();
//				if (row != null) {
//					CoreBO bo = BeanPropertyMapper.ObjectFrom(select.parseFromRow(row), CoreBO.class);
//					System.out.println("bo==" + bo);
//				}
//				Iterator<Row> it = result.iterator();
//				while (it.hasNext()) {
//					row = it.next();
//					System.out.println("MORE ROW::" + row);
//				}
//			}
//
//			// result.one();
//			System.out.println("execute OKOK:1:" + (System.currentTimeMillis() - start));
//
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//		cluster.close();
//
//	}
//}
