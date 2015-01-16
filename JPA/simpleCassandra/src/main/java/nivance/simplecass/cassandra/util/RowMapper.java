package nivance.simplecass.cassandra.util;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;

public class RowMapper {

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

	
	public static HashMap<String,Object> parseFromRow(Row row) {
		HashMap<String,Object> hb = new HashMap<String,Object>();
		Iterator<Definition> it = row.getColumnDefinitions().iterator();
		while (it.hasNext()) {
			Definition def = it.next();
			String name = def.getName();
			Object value = getPropertyValue(row, name);
			if(value!=null){
				hb.put(name, getPropertyValue(row, name));
			}
		}
		return hb;
	}
}
