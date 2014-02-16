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

import common.GStreamerLoader;

public class Recorder
{
	private static Pipeline pipe;
	
	public static void run(GUIValues v) {
		pipe = new Pipeline("pipeline"); 

    final Element camera = ElementFactory.make("qtkitvideosrc", "camera"); 
    final Element videoFilter = ElementFactory.make("capsfilter", "videofilter"); 
    videoFilter.setCaps(Caps.fromString(
    		"video/x-raw-yuv, framerate=" + v.frameRate + "/1, " +
    		"width=" + v.resolutionX + ", height=" + v.resolutionY));
        
    final Tee tee = new Tee("tee");
    pipe.addMany(camera, videoFilter, tee);
    Element.linkMany(camera, videoFilter, tee);
    //Element.linkMany(camera, videoFilter);
    System.out.println("go here 1");
    
    Thread liveThread = new Thread() {
      public void run() {
      	Element liveSink = ElementFactory.make("osxvideosink", "livesink"); 
        Queue liveQueue = new Queue("livequeue");
        
        System.out.println("go here 2");
        pipe.addMany(liveQueue, liveSink);
        Element.linkMany(tee, liveQueue, liveSink);
        //Element.linkMany(videoFilter, liveSink);
        System.out.println("go here 4");
      }  
    };
  
    liveThread.start();
    /*SwingUtilities.invokeLater(new Runnable() { 
      public void run() {  
        Element liveSink = ElementFactory.make("osxvideosink", "livesink"); 
        Queue liveQueue = new Queue("livequeue");

        pipe.addMany(liveQueue, liveSink);
        Element.linkMany(tee, liveQueue, liveSink);
        //Element.linkMany(videoFilter, liveSink);
        System.out.println("go here 2");
        pipe.setState(State.PLAYING);
        System.out.println("go here 4");
      } 
    });*/ 
    
    System.out.println("go here 3");
		Element mediaEncoder = ElementFactory.make("jpegenc", "encoder");
		Element mediaMuxer = ElementFactory.make("avimux", "muxer");
		FileSink fileSink = new FileSink("fileSink");
		fileSink.setLocation("/Users/son/Downloads/b.avi");
		Queue storageQueue = new Queue("storagequeue");
		
		pipe.addMany(storageQueue, mediaEncoder, mediaMuxer, fileSink);
		Element.linkMany(tee, storageQueue, mediaEncoder, mediaMuxer, fileSink);
		
		pipe.setState(State.PLAYING);
    Gst.main();
    pipe.setState(State.NULL);
	}
	
	public static void main(String[] args) { 
		GStreamerLoader.loadGStreamer();
    args = Gst.init("Recorder", args);
		run(new GUIValues());
	}
}