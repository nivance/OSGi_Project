package nivance.jpa.cassandra;
//package nivance.jpa.cassandra;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.datastax.driver.core.BoundStatement;
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.ResultSet;
//import com.datastax.driver.core.Row;
//import com.datastax.driver.core.Session;
//import nivance.db.cassandra.entity.MessageIDCounter;
//import nivance.jpa.cassandra.core.KeyspaceManager;
//import nivance.jpa.cassandra.dao.SimpleCassandraDAO;
//import nivance.jpa.cassandra.exception.CQLGenException;
//import nivance.jpa.cassandra.mapping.CQLStatement;
//import nivance.jpa.cassandra.mapping.CounterStatement;
//
//public class TestCounterTable {
//	
//	Cluster cluster = null;
//	Session session  = null;
//	MessageIDCounter obj = new MessageIDCounter();
//	@Before
//	public void before(){
//		cluster = Cluster.builder().addContactPoint("localhost").build();
//		try {
//			session=cluster.connect("lottery");
//			long start=System.currentTimeMillis();
//			SimpleCassandraDAO dao=new SimpleCassandraDAO(MessageIDCounter.class,session);
//			boolean bret=dao.CreateTable();
//			System.out.println("execute :"+bret+":1:" + (System.currentTimeMillis() - start));
//			//init data
//			obj.setMessageid("msg1");
//			obj.setOrderno("orderno1");
//			obj.setRofailed(1L);
//			obj.setRosuccess(1L);
//			obj.setVofailed(1L);
//			obj.setVosuccess(1L);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@After
//	public void after(){
//		cluster.close();
//	}
//	
//	@Test
//	public void countIncrementTest(){
//		try {
//			CounterStatement cs = new CounterStatement();
//			CQLStatement counterIncrement = cs.increment(MessageIDCounter.class);
//			System.out.println(counterIncrement);
//			counterIncrement.prepare(session);
//			
//			BoundStatement bound = counterIncrement.getStatement(obj);
//			ResultSet result=session.execute(bound);
//			System.out.println("result="+result+"::"+result.isFullyFetched()+":one="+result.one());
//			result= session.execute("select * from msgidcounter;");
//			for(Row row : result.all()){
//				System.out.println(row);
//			}
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void countDecrement(){
//		try {
//			CounterStatement cs = new CounterStatement();
//			CQLStatement counterDecrement = cs.decrement(MessageIDCounter.class);
//			System.out.println(counterDecrement);
//			counterDecrement.prepare(session);
//			
//			BoundStatement bound = counterDecrement.getStatement(obj);
//			ResultSet result=session.execute(bound);
//			System.out.println("result="+result+"::"+result.isFullyFetched()+":one="+result.one());
//			result= session.execute("select * from msgidcounter;");
//			for(Row row : result.all()){
//				System.out.println(row);
//			}
//		} catch (CQLGenException e) {
//			e.printStackTrace();
//		}
//	}
//}
