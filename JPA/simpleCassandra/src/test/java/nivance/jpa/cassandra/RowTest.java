package nivance.jpa.cassandra;

import java.nio.ByteBuffer;
import java.util.List;

import nivance.simplecass.cassandra.core.CassandraClusterFactory;
import nivance.simplecass.cassandra.core.KeyspaceManager;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.collect.Lists;

public class RowTest {

	public static void main(String[] args) {
		CassandraClusterFactory cluster = null;
		try {
			cluster = new CassandraClusterFactory();
			// cluster.setContactPoints("192.168.24.21");
			cluster.initCluster();
			List<Class<?>> daos = Lists.newArrayList();
			KeyspaceManager sltoKm = new KeyspaceManager(cluster, "lottery","SimpleStrategy","1", ConsistencyLevel.QUORUM);
			Session session = sltoKm.getSession();
			ResultSet rs = session.execute("select * from testbean;");
			for(Row row : rs.all()){
				List<String>list = getPropertyValue(row, "ors");
				System.out.println(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cluster.destroy();
	}
	
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	public static  <T> T getPropertyValue(Row row,String columnName) {
		ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
		int columnIndex = columnDefinitions.getIndexOf(columnName);
		if (row.isNull(columnIndex)) {
			return null;
		}
		DataType columnType = columnDefinitions.getType(columnIndex);
		if (columnType.isCollection()) {
			List<DataType> typeArguments = columnType.getTypeArguments();
			switch (columnType.getName()) {
			case SET:
				return (T)  row.getSet(columnIndex, typeArguments.get(0).asJavaClass());
			case MAP:
				return (T) row.getMap(columnIndex, typeArguments.get(0).asJavaClass(), typeArguments.get(1).asJavaClass());
			case LIST:
				return (T) row.getList(columnIndex, typeArguments.get(0).asJavaClass());
			}
		}
		ByteBuffer bytes = row.getBytesUnsafe(columnIndex);
		return (T) columnType.deserialize(bytes);
	}
}
