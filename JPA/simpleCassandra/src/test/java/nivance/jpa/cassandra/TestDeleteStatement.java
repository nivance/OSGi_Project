package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import com.datastax.driver.core.BoundStatement;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.mapping.DeleteStatement;
//
//public class TestDeleteStatement {
//
//	public static void main(String[] args) {
//
//		try {
//			Cluster cluster = Cluster.builder().addContactPoint("192.168.24.31").build();
//			Session session=cluster.connect("lottery");
//
//			DeleteStatement delete = new DeleteStatement();
//			
//			delete.deleteIfExist(CoreBO.class).prepare(session);
//			int count = 1;
//			long start;
//			CoreBO bos[] = new CoreBO[count];
//			for (int i = 0; i < count; i++) {
//				bos[i] = new CoreBO();
//				CoreBO bo = bos[i];
//				bo.setMerchantid("666666");
//				bo.setMessageid("f17242db-8644-4f9e-b080-0fddbe9d9c2c");
//			}
//			start = System.currentTimeMillis();
//			BoundStatement[] bss = new BoundStatement[count];
//			for (int i = 0; i < count; i++) {
//				bss[i] = delete.getStatement(bos[i]);
//			}
//			start = System.currentTimeMillis();
//
//			for (int i = 0; i < count; i++) {
//				ResultSet result=session.execute(bss[i]);
//				System.out.println("result="+result+"::"+result.isFullyFetched()+":one="+result.one());
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
