package mp1;

import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.Element;
import org.gstreamer.State;
import org.gstreamer.elements.FileSink;

import common.GStreamerLoader;

public class AudioRecorder {
	private static Pipeline pipe;
	public static void main(String[] args) {

		GStreamerLoader.loadGStreamer();
		args = Gst.init("AudioRecorder", args);
		pipe = new Pipeline("pipeline");
		final Element mic = ElementFactory.make("osxaudiosrc", "mic");
		final Element audioresample =  ElementFactory.make("audioresample", "audioresample");
		final Element audioconvert =  ElementFactory.make("audioconvert", "audioconvert");
		final Element encoder = ElementFactory.make("wavenc", "encoder");
		FileSink fileSink = new FileSink("fileSink");
		fileSink.setLocation("/Users/son/Downloads/audiothing.wav");
		pipe.addMany(mic,audioresample,audioconvert,encoder,fileSink);
		Element.linkMany(mic,audioresample,audioconvert, encoder,fileSink);
		pipe.setState(State.PLAYING);
		Gst.main();
		pipe.setState(State.NULL);
	}

}
