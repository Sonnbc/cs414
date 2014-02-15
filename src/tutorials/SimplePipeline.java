package tutorials;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;

import common.GStreamerLoader;

public class SimplePipeline {
	
  public static void main(String[] args) {
		GStreamerLoader.loadGStreamer();
		
    args = Gst.init("SimplePipeline", args);
    Pipeline pipe = new Pipeline("SimplePipeline");
    Element src = ElementFactory.make("fakesrc", "Source");
    Element sink = ElementFactory.make("fakesink", "Destination");
    pipe.addMany(src, sink);
    src.link(sink);
    pipe.setState(State.PLAYING);
    Gst.main();
    pipe.setState(State.NULL);
  }
}