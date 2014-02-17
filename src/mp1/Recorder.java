package mp1;

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.FileSink;
import org.gstreamer.elements.Queue;
import org.gstreamer.elements.Tee;

import common.Common;
import common.ElementChain;
import common.GStreamerLoader;

public class Recorder
{
  
	private Pipeline pipe;
	
	public ElementChain makeEncodingChain(GUIValues v) {
		if ("mpeg4".equals(v.encoding)) {
  		Element colorConverter = ElementFactory.make("ffmpegcolorspace", "colorconverter");
  		Element mediaEncoder = ElementFactory.make("ffenc_mpeg4", "encoder");
  		Element mediaMuxer = ElementFactory.make("avimux", "muxer");
  		return new ElementChain(pipe, colorConverter, mediaEncoder, mediaMuxer);
  	}
		else if ("mjpeg".equals(v.encoding)) {
			Element mediaEncoder = ElementFactory.make("jpegenc", "encoder");
			Element mediaMuxer = ElementFactory.make("avimux", "muxer");
			return new ElementChain(pipe, mediaEncoder, mediaMuxer);
		}
		else if ("raw".equals(v.encoding)) {
			return null;
		}
		return null;	//ERROR case;
	}
	
	public void run(GUIValues v) {
		pipe = new Pipeline("pipeline"); 

    final Element camera = ElementFactory.make("qtkitvideosrc", "camera"); 
    final Element videoFilter = ElementFactory.make("capsfilter", "videofilter"); 
    videoFilter.setCaps(Caps.fromString(
    		"video/x-raw-yuv, framerate=" + v.frameRate + "/1, " +
    		"width=" + v.resolutionX + ", height=" + v.resolutionY));    
    final Tee tee = new Tee("tee");
    ElementChain captureChain = new ElementChain(pipe, camera, videoFilter, tee);
    
    Queue liveQueue = new Queue("livequeue");
  	Element liveSink = ElementFactory.make("osxvideosink", "livesink"); 
  	ElementChain liveChain = new ElementChain(pipe, liveQueue, liveSink);
  	
  	ElementChain encodingChain = makeEncodingChain(v);
  	Queue storageQueue = new Queue("storagequeue");
		
		FileSink fileSink = new FileSink("fileSink");
		fileSink.setLocation(v.location);
		ElementChain storageChain = new ElementChain(pipe, 
				storageQueue, encodingChain, fileSink);
		
    captureChain.link(liveChain);
    captureChain.link(storageChain);

		Common.printPipeline(pipe);
		
    pipe.setState(State.PLAYING);
    Gst.main();
    pipe.setState(State.NULL);
	}
	
	public static void main(String[] args) { 
		GStreamerLoader.loadGStreamer();
    args = Gst.init("Recorder", args);
    Recorder recorder = new Recorder();
		recorder.run(new GUIValues());
	}
}