package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import com.datastax.driver.core.BoundStatement;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.mapping.UpdateStatement;
//
//public class TestUpdateStatement {
//
//	public static void main(String[] args) {
//
//		try {
//			Cluster cluster = Cluster.builder().addContactPoint("192.168.24.21").build();
//			Session session=cluster.connect("test");
//
//			UpdateStatement update = new UpdateStatement();
//			update.update(CoreBO.class).prepare(session);
//			int count = 1;
//			long start;
//			CoreBO bos[] = new CoreBO[count];
//			for (int i = 0; i < count; i++) {
//				bos[i] = new CoreBO();
//				CoreBO bo = bos[i];
//				bo.setMerchantid("666666");
//				bo.setMessageid("974d3453-1681-4238-9d12-71b7c6a134e9");
//				bo.setCommand("22000");
//				bo.setLtype("pk3d");
//			}
//			start = System.currentTimeMillis();
//
//			BoundStatement[] bss = new BoundStatement[count];
//			for (int i = 0; i < count; i++) {
//				bss[i] = update.getStatement(bos[i]);
//			}
//			start = System.currentTimeMillis();
//
//			for (int i = 0; i < count; i++) {
//				ResultSet result=session.execute(bss[i]);
//				System.out.println("result="+result+":one="+result.one());
//			}
//			
//			// result.one();
//			System.out.println("execute OKOK:1:" + (System.currentTimeMillis() - start));
//
//			cluster.close();
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//
//	}
//}
