package nivance.simplecass.cassandra.core;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.FrameworkUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;

import com.google.common.collect.Lists;

@Slf4j
public class ScanResolver {
	
	static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	public static List<String> scanBasePackage(String basePackageName) {
		try {
			return scan2Osgi(basePackageName);
		} catch (Exception e) {
			log.info("scan by osgi is error..::"+ basePackageName);
		}
		try {
			return scan2Local(basePackageName);
		} catch (Exception e) {
			log.info("scan by local is error..::"+ basePackageName);
		}
		throw new RuntimeException("scan basePackage is error..::"+basePackageName);
	}
	
	public static List<String> scan2Local(String basePackageName)throws IOException {
		String packageDirName = StringUtils.replace(basePackageName, ".", "/");
		String packageSearchPath = CLASSPATH_ALL_URL_PREFIX + packageDirName + "/" + DEFAULT_RESOURCE_PATTERN;
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
		List<String> classNames = Lists.newArrayList();
		Resource[] resources = resolver.getResources(packageSearchPath);
		for(Resource r : resources){
			if(r.isReadable()){
				if (r.getFilename().endsWith(".class")) {
					classNames.add(basePackageName+"."+StringUtils.substringBefore(r.getFilename(),".class"));
				}
			}
		}
		return classNames;
	}
	
	public static List<String> scan2Osgi(String basePackageName) throws IOException {
		String packageDirName = StringUtils.replace(basePackageName, ".", "/");
		String packageSearchPath = CLASSPATH_ALL_URL_PREFIX + packageDirName + "/" + DEFAULT_RESOURCE_PATTERN;
		OsgiBundleResourcePatternResolver resolver = new OsgiBundleResourcePatternResolver(FrameworkUtil.getBundle(ScanResolver.class));
		List<String> classNames = Lists.newArrayList();
		Resource[] resources = resolver.getResources(packageSearchPath);
		for(Resource r : resources){
			if(r.isReadable()){
				String path = r.getURL().getFile();
				if (path.endsWith(".class")) {
					String className = StringUtils.substringBetween(path, "/", ".class");
					className = StringUtils.replace(className, "/", ".");
					classNames.add(className);
				}
			}
		}
		return classNames;
	}

}
