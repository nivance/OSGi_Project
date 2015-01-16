package nivance.ipojo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.ipojo.manipulator.ResourceStore;
import org.apache.felix.ipojo.manipulator.ResourceVisitor;
import org.apache.felix.ipojo.metadata.Element;

public class ExtJarFileRS implements ResourceStore {

	ResourceStore rs;

	Map<String, byte[]> refreshData = new HashMap<String, byte[]>();

	ExtJarFileRS(ResourceStore rs) {
		this.rs = rs;
	}

	public void updateResouce(String key, byte[] value) {
		refreshData.put(key, value);
	}

	@Override
	public byte[] read(String path) throws IOException {
		System.out.println("read::"+path+"::"+refreshData.containsKey(path)+"::");
		
		if (refreshData.containsKey(path))
			return refreshData.get(path);
		return rs.read(path);
	}

	@Override
	public void accept(ResourceVisitor visitor) {
		rs.accept(visitor);
	}

	@Override
	public void open() throws IOException {
		rs.open();
	}

	@Override
	public void writeMetadata(Element metadata) {
		rs.writeMetadata(metadata);
	}

	@Override
	public void write(String resourcePath, byte[] resource) throws IOException {
		updateResouce(resourcePath, resource);
		rs.write(resourcePath, resource);
	}

	@Override
	public void close() throws IOException {
		rs.close();
	}

}
