package common;

import org.gstreamer.Element;
import org.gstreamer.Pipeline;

public class ElementChain {
	public Pipeline pipe;
	public Element head;
	public Element tail;
	public ElementChain(Pipeline p) {
		pipe = p;
	}
	public ElementChain(Pipeline p, Object...elements) {
		pipe = p;
		addMany(elements);
	}
	public void addElement(Element element) {
		pipe.add(element);
		if (head == null) {
			head = element;
		}
		else {
			Element.linkMany(tail, element);
		}
		tail = element;
	}
	
	public void addElementChain(ElementChain ec) {
		if (ec == null) {
			return;
		}
		if (head == null) {
			head = ec.head;
		}
		else {
			Element.linkMany(tail, ec.head);
		}
		tail = ec.tail;
	}
	
	public void addMany(Object...elements) {
		for (Object e : elements) {
			if (e == null) {
				continue;
			}
			if (e instanceof Element) {
				addElement((Element) e);
			}
			else if (e instanceof ElementChain) {
				addElementChain((ElementChain)e );
			}
		}
	}
	
	public void link(ElementChain other) {
		Element.linkMany(tail, other.head);
	}
}