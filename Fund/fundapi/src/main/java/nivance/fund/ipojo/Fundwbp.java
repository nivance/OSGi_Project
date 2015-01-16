package nivance.fund.ipojo;

import org.apache.felix.ipojo.api.HandlerConfiguration;
import org.apache.felix.ipojo.metadata.Attribute;
import org.apache.felix.ipojo.metadata.Element;

public class Fundwbp implements HandlerConfiguration {

	public static final String NAME = "wbp";

	public static final String NAMESPACE = "org.apache.felix.ipojo.whiteboard";

	private String arrival;

	private String departure;

	private String filter;

	public Fundwbp onArrival(String method) {
		arrival = method;
		return this;
	}

	public Fundwbp onDeparture(String method) {
		departure = method;
		return this;
	}

	public Fundwbp setFilter(String fil) {
		filter = fil;
		return this;
	}

	public Element getElement() {
		ensureValidity();
		Element element = new Element(NAME, NAMESPACE);
		element.addAttribute(new Attribute("onArrival", arrival));
		element.addAttribute(new Attribute("onDeparture", departure));
		element.addAttribute(new Attribute("filter", filter));
		return element;
	}

	private void ensureValidity() {
		if (arrival == null) {
			throw new IllegalStateException("The whiteboard pattern configuration must have a onArrival method");
		}
		if (departure == null) {
			throw new IllegalStateException("The whiteboard pattern configuration must have a onDeparture method");
		}
		if (filter == null) {
			throw new IllegalStateException("The whiteboard pattern configuration must have a filter");
		}
	}
}