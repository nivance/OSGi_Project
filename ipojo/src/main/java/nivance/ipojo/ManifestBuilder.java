package nivance.ipojo;

import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.felix.ipojo.manipulator.store.builder.DefaultManifestBuilder;

public class ManifestBuilder extends DefaultManifestBuilder {

	private List<String> jarlibList;

	public ManifestBuilder(List<String> jarlibList) {
		super();
		this.jarlibList = jarlibList;
	}

	@Override
	public Manifest build(Manifest original) {
		Manifest ret = super.build(original);
		checkIgnoreImport(ret);
		checkSetBuildPath(ret);
		
		Attributes attrs = ret.getMainAttributes();
		String ipojoComponents = (String) attrs.getValue("iPOJO-Components");
		System.out.println("ipojoComponents::"+ipojoComponents);
		return ret;
	}

	public void checkSetBuildPath(Manifest ret) {
		Attributes attrs = ret.getMainAttributes();
		String importLib = (String) attrs.getValue("Import-Lib");
		String bundleCP = (String) attrs.getValue("Bundle-ClassPath");
		if (importLib != null && jarlibList != null) {
			if (bundleCP == null) {
				bundleCP = ".";
			}
			String libstarts[] = importLib.split(",");
			for (int i = 0; i < jarlibList.size(); i++) {
				int j=0;
				for(j=0;j<libstarts.length;j++){
					if(jarlibList.get(i).startsWith(libstarts[j])){
						break;
					}
				}
				if(j>=libstarts.length){
					jarlibList.remove(i);i--;
				}
			}
			String orgCPs[] = bundleCP.split(",");
			for (int i = 0; i < orgCPs.length; i++) {
				if (!jarlibList.contains(orgCPs[i])) {
					jarlibList.add(orgCPs[i]);
				}
			}
			StringBuffer buff = new StringBuffer();
			for (String str : jarlibList) {
				if (buff.length() > 0) {
					buff.append(", ");
				}
				buff.append(str);
			}
			attrs.putValue("Bundle-ClassPath", buff.toString());

		}

	}

	public void checkIgnoreImport(Manifest ret) {
		Attributes attrs = ret.getMainAttributes();
		String importIgnore = (String) attrs.getValue("Import-Ignore");
		String imports = (String) attrs.getValue("Import-Package");
		System.out.println("imports==" + imports + "::importIgnore=" + importIgnore);
		if (importIgnore != null) {
			String orgimport[] = imports.split(", ");
			for (String ignore : importIgnore.split(",")) {
				for (int i = 0; i < orgimport.length; i++) {
					if (orgimport[i] != null && orgimport[i].trim().startsWith(ignore)) {
						orgimport[i] = null;
					}
				}
			}
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < orgimport.length; i++) {
				if (orgimport[i] != null) {
					if (buff.length() > 0) {
						buff.append(", ");
					}
					buff.append(orgimport[i].trim());
				}
			}
			System.out.println("change Import-Package:" + buff.toString());
			attrs.putValue("Import-Package", buff.toString());
		}
	}

}
