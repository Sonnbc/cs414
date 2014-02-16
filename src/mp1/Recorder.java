package mp1;

import javax.swing.SwingUtilities;

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
	public static void main(String[] args) { 
		GStreamerLoader.loadGStreamer();
    args = Gst.init("SwingVideoTest", args); 
    pipe = new Pipeline("pipeline"); 

    final Element camera = ElementFactory.make("qtkitvideosrc", "camera"); 
    final Element videoFilter = ElementFactory.make("capsfilter", "videofilter"); 
    videoFilter.setCaps(Caps.fromString("video/x-raw-yuv, width=640, height=480")); 
    
    final Tee tee = new Tee("tee");
    pipe.addMany(camera, videoFilter, tee);
    Element.linkMany(camera, videoFilter, tee);
    SwingUtilities.invokeLater(new Runnable() { 
      public void run() {  
        Element liveSink = ElementFactory.make("osxvideosink", "livesink"); 
        Queue liveQueue = new Queue("livequeue");

        pipe.addMany(liveQueue, liveSink);
        Element.linkMany(tee, liveQueue, liveSink);
      } 
    }); 
    
    SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			Element mediaEncoder = ElementFactory.make("jpegenc", "encoder");
    			Element mediaMuxer = ElementFactory.make("avimux", "muxer");
    			FileSink fileSink = new FileSink("fileSink");
    			fileSink.setLocation("/Users/son/Downloads/b.avi");
    			Queue storageQueue = new Queue("storagequeue");
    			
    			pipe.addMany(storageQueue, mediaEncoder, mediaMuxer, fileSink);
    			Element.linkMany(tee, storageQueue, mediaEncoder, mediaMuxer, fileSink);
    			
    			pipe.setState(State.PLAYING);
    		}
    });
    
    Gst.main();
    pipe.setState(State.NULL);
}}