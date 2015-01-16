package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import nivance.bean.MapBean;
//import nivance.db.cassandra.entity.MessageIDCounter;
//import nivance.jpa.cassandra.core.CassandraClusterFactory;
//import nivance.jpa.cassandra.core.KeyspaceManager;
//import nivance.jpa.cassandra.dao.SimpleCassandraDAO;
//
//public class ClusterMainTest {
//
//	public static void main(String[] args) {
//		CassandraClusterFactory cluster = null;
//		try {
//			MapBean mb = new MapBean();
//			mb.setValueByName("ltype", "SLTO");
//			mb.setValueByName("messageid", "abc");
//			mb.setValueByName("merchantid", "666666");
//			cluster = new CassandraClusterFactory();
////			cluster.setContactPoints("192.168.24.21");
//			cluster.initCluster();
//			KeyspaceManager sltoKm = new KeyspaceManager(cluster, "sltoKeyspace",1);
////			sltoKm.warmup(CoreBO.class);
//			SimpleCassandraDAO dao=new SimpleCassandraDAO(MessageIDCounter.class,sltoKm);
//			
//			System.out.println(dao.insert(mb));
//			System.out.println(dao.findOne(mb).getValueByName("ltype"));
//			mb.setValueByName("ltype", "PCK3");
//			System.out.println(dao.update(mb));
//			System.out.println(dao.findOne(mb).getValueByName("ltype"));
//			System.out.println(dao.exists(mb));
//			System.out.println(dao.delete(mb));
//			System.out.println(dao.exists(mb));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				cluster.destroy();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
