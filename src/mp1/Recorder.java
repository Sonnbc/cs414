package mp1;

import java.util.ArrayList;
import java.util.List;

import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.elements.AppSink;
import org.gstreamer.elements.FileSink;
import org.gstreamer.elements.Queue;
import org.gstreamer.elements.Tee;

import common.Common;
import common.ElementChain;

public class Recorder
{
  
	private Pipeline pipe;
	private AppSink appSinkRaw = (AppSink) ElementFactory.make("appsink", "appsinkraw");
	private AppSink appSinkEncoded = (AppSink) ElementFactory.make("appsink", "appsinkencoded");
	
	List<Integer> sizeList = new ArrayList<Integer>();
	List<Long> timeList = new ArrayList<Long>();
	
	public void monitor() {
		Thread rawPuller = new Thread() {
			public void run() {
				while (true) {
					Buffer b = appSinkRaw.pullBuffer();
					long t = System.nanoTime();
					int size = b.getSize();
					timeList.add(t);
					sizeList.add(size);
				}
			}
		};
		
		Thread monitor = new Thread() {
			public void run() {
				int n = 0;
				while (true) {
					Buffer b = appSinkEncoded.pullBuffer();
					while (sizeList.size() <= n) { //call to sizeList.size() is not thread-safe
						try
            {
	            Thread.sleep(0);
            }
            catch (InterruptedException e)
            {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
					} //busy wait
					
					long t1 = System.nanoTime();
					int size1 = b.getSize();
					long t0 = timeList.get(n);
					long size0 = sizeList.get(n);
					System.out.println("Compression time (us): " + (t1 - t0)/1000 + 
							"\tSize (bytes): raw: " + size0 + "\tcompressed: " + size1 + "  \tratio: " + (size1 + .0)/ size0);
					n++;
				}
			}
		};
		
		rawPuller.start();
		monitor.start();
	}
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
		
		Tee tee = new Tee("tee1");
		Queue storageQueue = new Queue("storagequeue");
		c.addMany(videoEncoder, tee, storageQueue);
		 
		ElementChain encodedSink = new ElementChain(pipe, new Queue("monitorqueue1"), appSinkEncoded);
		tee.link(encodedSink.head);
		
		return c;
	}
	
	public void run(GUIValues v) {
		pipe = new Pipeline("pipeline"); 

    Element camera = ElementFactory.make("autovideosrc", "camera"); 
    Element videoFilter = ElementFactory.make("capsfilter", "videofilter"); 
    videoFilter.setCaps(Caps.fromString(
    		"video/x-raw-yuv, framerate=" + v.frameRate + "/1, " +
    		"width=" + v.resolutionX + ", height=" + v.resolutionY));    
    Tee tee = new Tee("tee0");
    ElementChain videoCaptureChain = new ElementChain(pipe, camera, videoFilter, tee);
    
    Element mic = ElementFactory.make("autoaudiosrc", "mic");   
    System.out.println(mic.getStaticPad("src"));
    
    Element audioConverter = ElementFactory.make("audioconvert", "audioconverter");
    Element audioFilter0 = ElementFactory.make("capsfilter", "audiofilter0");
    audioFilter0.setCaps(Caps.fromString("audio/x-raw-float, rate=44100"));
    Element audioResampler = ElementFactory.make("audioresample", "audioresampler");
    Element audioFilter1 = ElementFactory.make("capsfilter", "audiofilter1");
    audioFilter1.setCaps(Caps.fromString("audio/x-raw-float, rate=" + v.audioRate));
    Element audioEncoder = ElementFactory.make(v.audioEncoding, "audioencoder");
    ElementChain audioCaptureChain = new ElementChain(pipe, mic, audioFilter0, 
    		audioResampler, audioFilter1, audioConverter, audioEncoder);
    
    Queue liveQueue = new Queue("livequeue");
  	Element liveSink = ElementFactory.make("autovideosink", "livesink"); 
  	ElementChain liveChain = new ElementChain(pipe, liveQueue, liveSink);
  	
  	Queue encodingQueue = new Queue("encodingqueue");
  	ElementChain videoEncodingChain = makeVideoEncodingChain(v);
  	Element mediaMuxer = ElementFactory.make("avimux", "muxer");
		FileSink fileSink = new FileSink("fileSink");
		fileSink.setLocation(v.location);
		ElementChain storageChain = new ElementChain(pipe, 
				encodingQueue, videoEncodingChain, mediaMuxer, fileSink);
		
		ElementChain rawChain = new ElementChain(pipe, new Queue("monitorqueue0"), appSinkRaw);
		tee.link(rawChain.head);
		
    videoCaptureChain.link(liveChain);
    videoCaptureChain.link(storageChain);
    Element.linkMany(audioCaptureChain.tail, mediaMuxer);
				
    
		Common.printPipeline(pipe);
		
    pipe.setState(State.PLAYING);
    monitor();
    Gst.main();
    pipe.setState(State.NULL);
	}
	
}