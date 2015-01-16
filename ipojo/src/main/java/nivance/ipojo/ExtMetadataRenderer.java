package nivance.ipojo;

import java.util.HashSet;

import org.apache.felix.ipojo.manipulator.metadata.annotation.visitor.LifecycleVisitor;
import org.apache.felix.ipojo.manipulator.render.MetadataFilter;
import org.apache.felix.ipojo.manipulator.render.MetadataRenderer;
import org.apache.felix.ipojo.metadata.Attribute;
import org.apache.felix.ipojo.metadata.Element;

public class ExtMetadataRenderer extends MetadataRenderer {

	HashSet<String> pojoSet = new HashSet<String>();

	public void addPojoClass(String classname) {
		pojoSet.add(classname);
	}

	@Override
	public void addMetadataFilter(MetadataFilter filter) {
		super.addMetadataFilter(filter);
	}

	@Override
	public String render(Element element) {
		if (element.getName().equals("component") && pojoSet.contains(element.getAttribute("classname"))) {
			Element cb = new Element("callback", "");
			cb.addAttribute(new Attribute("transition", LifecycleVisitor.Transition.VALIDATE.name().toLowerCase()));
			cb.addAttribute(new Attribute("method", "__pojoValidate"));
			element.addElement(cb);
			
			cb = new Element("callback", "");
			cb.addAttribute(new Attribute("transition", LifecycleVisitor.Transition.INVALIDATE.name().toLowerCase()));
			cb.addAttribute(new Attribute("method", "__pojoInvalidate"));
			element.addElement(cb);

		}
		String result = super.render(element);
		return result;

	}

}
