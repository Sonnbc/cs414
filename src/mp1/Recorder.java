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

public class Recorder
{
  
	private Pipeline pipe;
	
	public ElementChain makeVideoEncodingChain(GUIValues v) {
		ElementChain c = new ElementChain(pipe);
		Element videoEncoder = null;
		
		Element videorateEnforcer = ElementFactory.make("videorate", "videorateenforcer");
		videorateEnforcer.set("force-fps", v.frameRate);
		c.addElement(videorateEnforcer);
		
		if ("mpeg4".equals(v.videoEncoding)) {
  		Element colorConverter = ElementFactory.make("ffmpegcolorspace", "colorconverter");
  		c.addMany(colorConverter);
  		videoEncoder = ElementFactory.make("ffenc_mpeg4", "videoencoder");
		}
		else if ("mjpeg".equals(v.videoEncoding)) {
			videoEncoder = ElementFactory.make("jpegenc", "videoencoder");
		}
		
		c.addElement(videoEncoder);
		return c;
	}
	
	public void run(GUIValues v) {
		pipe = new Pipeline("pipeline"); 

    Element camera = ElementFactory.make("qtkitvideosrc", "camera"); 
    Element videoFilter = ElementFactory.make("capsfilter", "videofilter"); 
    videoFilter.setCaps(Caps.fromString(
    		"video/x-raw-yuv, framerate=" + v.frameRate + "/1, " +
    		"width=" + v.resolutionX + ", height=" + v.resolutionY));    
    Tee tee = new Tee("tee");
    ElementChain videoCaptureChain = new ElementChain(pipe, camera, videoFilter, tee);
    
    Element mic = ElementFactory.make("osxaudiosrc", "mic");   
    Element audioConverter = ElementFactory.make("audioconvert", "audioconverter");
    Element audioEncoder = ElementFactory.make(v.audioEncoding, "audioencoder");
    ElementChain audioCaptureChain = new ElementChain(pipe, mic, audioConverter, audioEncoder);
    
    Queue liveQueue = new Queue("livequeue");
  	Element liveSink = ElementFactory.make("osxvideosink", "livesink"); 
  	ElementChain liveChain = new ElementChain(pipe, liveQueue, liveSink);
  	
  	ElementChain videoEncodingChain = makeVideoEncodingChain(v);
  	Queue storageQueue = new Queue("storagequeue");
  	Element mediaMuxer = ElementFactory.make("avimux", "muxer");
		FileSink fileSink = new FileSink("fileSink");
		fileSink.setLocation(v.location);
		
		ElementChain storageChain = new ElementChain(pipe, 
				storageQueue, videoEncodingChain, mediaMuxer, fileSink);
		Element.linkMany(audioCaptureChain.tail, mediaMuxer);
		
    videoCaptureChain.link(liveChain);
    videoCaptureChain.link(storageChain);
				
    //System.out.println(Element.linkMany(audioCaptureChain.tail, mediaMuxer));
		Common.printPipeline(pipe);
		
    pipe.setState(State.PLAYING);
    Gst.main();
    pipe.setState(State.NULL);
	}
	
	/*public static void main(String[] args) { 
		GStreamerLoader.loadGStreamer();
    args = Gst.init("Recorder", args);
    Recorder recorder = new Recorder();
		recorder.run(new GUIValues());
	}*/
}