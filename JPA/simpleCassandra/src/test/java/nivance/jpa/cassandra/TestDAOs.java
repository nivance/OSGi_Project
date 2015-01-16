package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import java.util.UUID;
//
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.CoreBO;
//import nivance.db.cassandra.entity.FakeCoreBO;
//import nivance.jpa.cassandra.dao.SimpleCassandraDAO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.util.BeanPropertyMapper;
//
//public class TestDAOs {
//
//	public static void main(String[] args) {
//		Cluster cluster = Cluster.builder().addContactPoint("192.168.24.31").build();
//		Session session=cluster.connect("lottery");
//		
//		try {
//			SimpleCassandraDAO sdao=new SimpleCassandraDAO(CoreBO.class,session);
//			int count=10;
//			System.out.println("truncate=="+sdao.truncate());
//
//			for (int i = 0; i < count; i++) {
//				FakeCoreBO bo = new FakeCoreBO();
//				bo.setMerchantid("666666");
//				bo.setMessageid(UUID.randomUUID().toString());
//				bo.setCommand("1000");
//				bo.setLtype("slto");
//				System.out.println("insert="+sdao.insert(BeanPropertyMapper.hashbeanFrom(bo)));
//			}
//			
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//		
//		cluster.close();
//	}
//}
