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

    final Element videosrc = ElementFactory.make("qtkitvideosrc", "source"); 
    final Element videofilter = ElementFactory.make("capsfilter", "filter"); 
    videofilter.setCaps(Caps.fromString("video/x-raw-yuv, width=640, height=480")); 
    
    final Tee tee = new Tee("tee");
    pipe.addMany(videosrc, videofilter, tee);
    Element.linkMany(videosrc, videofilter, tee);
    SwingUtilities.invokeLater(new Runnable() { 
      public void run() {  
        Element videosink = ElementFactory.make("osxvideosink", "sink"); 
        //pipe.addMany(videosrc, videofilter, videosink);
        Queue queue1 = new Queue("queue1");

        pipe.addMany(queue1, videosink);
        //Element.linkMany(videosrc, videofilter, videosink); 
                Element.linkMany(tee, queue1, videosink);
        // Start the pipeline processing 
        //pipe.setState(State.PLAYING); 
      } 
    }); 
    
    SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			Element mediaEncoder = ElementFactory.make("jpegenc", "encoder");
    			Element mediaMuxer = ElementFactory.make("avimux", "muxer");
    			FileSink fileSink = new FileSink("fileSink");
    			fileSink.setLocation("/Users/son/Downloads/b.avi");
    			Queue queue2 = new Queue("queue2");
    			//pipe.addMany(videosrc, videofilter, mediaEncoder, mediaMuxer, fileSink);
    			//Element.linkMany(videosrc, videofilter, mediaEncoder, mediaMuxer, fileSink);
    			
    			pipe.addMany(queue2, mediaEncoder, mediaMuxer, fileSink);
    			Element.linkMany(tee, queue2, mediaEncoder, mediaMuxer, fileSink);
    			
    			pipe.setState(State.PLAYING);
    		}
    });
    
    Gst.main();
    pipe.setState(State.NULL);
}}