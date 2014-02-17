package common;

import java.util.Iterator;
import java.util.List;

import org.gstreamer.Element;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;

public class Common
{
	/* Iterate through all the elements in the pipeline and display their
	 * caps. Useful for debugging and general information about the pipeline */
	public static void printPipeline(Pipeline p) {
		
		List<Element> elements = p.getElements();
		
		if (elements.size() > 0) {
			Iterator<Element> elemiter = elements.iterator();
			Element e = null;
			while (elemiter.hasNext()) {
				e = (Element) elemiter.next();

				List<Pad> pads = e.getPads();
				
				if (pads.size() > 0) {
					Iterator<Pad> paditer = pads.iterator();
					Pad pad = null;
					while (paditer.hasNext()) {
						pad = (Pad) paditer.next();
						System.out.print(e + " " + pad.getDirection()); 
						System.out.println("\t" + pad.getCaps());
					}
				}
			}
		}
	}
}
