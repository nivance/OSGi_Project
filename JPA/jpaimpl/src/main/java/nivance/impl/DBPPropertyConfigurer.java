package nivance.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

@Slf4j
public class DBPPropertyConfigurer extends PropertyPlaceholderConfigurer {

	@Setter
	private String[] fileNames = {};
	private static Map<String, Object> ctxPropertiesMap = new HashMap<String, Object>();

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	public static Object getContextProperty(String name) {
		return ctxPropertiesMap.get(name);
	}

	public static boolean setContextProperty(String key, Object object) {
		if (ctxPropertiesMap.containsKey(key)
				&& ctxPropertiesMap.get(key).equals(object)) {
			return false;
		}
		ctxPropertiesMap.put(key, object);
		return true;
	}

	public static Set<String> getPropertyKeys() {
		return ctxPropertiesMap.keySet();
	}

	// @PostConstruct
	public void init() {
		Resource[] locations = new Resource[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			String filePath = getAbsolutepath(fileNames[i]);
			File file = new File(filePath);
			try {
				InputStream input = new FileInputStream(file);
				Resource location = new InputStreamResource(input);
				locations[i] = location;
			} catch (FileNotFoundException e) {
				log.warn("filePath:["+file.getAbsolutePath()+"]",e);
			}
		}
		super.setLocations(locations);
	}
	
	private String getAbsolutepath(String fileName) {
		StringBuffer path = new StringBuffer();
//		String home = System.getenv("HOME");
//		String separator = System.getProperty("file.separator");
//		path.append(home);
//		path.append(separator);
//		path.append("config");
//		path.append(separator);
		path.append(fileName);
		return path.toString();
	}

}
