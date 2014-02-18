package mp1;

import org.gstreamer.Caps;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.Element;
import org.gstreamer.State;
import org.gstreamer.elements.FileSink;

import common.GStreamerLoader;

public class AudioRecorderModel {
	
	private Pipeline pipe;
	/**
	 * Opens up an audio pipeline from recorder to file
	 */
	
	private Element mic;
	private Element audioresample;
	private Element audioconvert;
	private Element encoder;
	private Element caps;
	private String format;
	private String samplingRate;
	private String sampleSize;
	private String numberOfChannels;
	FileSink fileSink;
	
	public void setFormat(String format){
		this.format = format;
	}
	public void setSamplingRate(String samplingRate){
		this.samplingRate=samplingRate;
	}
	public void setSampleSize(String sampleSize){
		this.sampleSize=sampleSize;
	}
	public void setNumberOfChannels(String numberOfChannels){
		this.numberOfChannels=numberOfChannels;
	}
	
	public AudioRecorderModel(String micName, String audioresampleName, String audioconvertName, String encoderName, String location){
		this.mic = ElementFactory.make(micName, "mic");
		this.audioresample =  ElementFactory.make(audioresampleName, "audioresample");
		this.audioconvert =  ElementFactory.make(audioconvertName, "audioconvert");
		this.encoder = ElementFactory.make(encoderName, "encoder");
		fileSink = new FileSink("fileSink");
		fileSink.setLocation(location);
	}
	
	public AudioRecorderModel(String location){
		mic = ElementFactory.make("osxaudiosrc", "mic");
		audioresample =  ElementFactory.make("audioresample", "audioresample");
		audioconvert =  ElementFactory.make("audioconvert", "audioconvert");
		encoder = ElementFactory.make("wavenc", "encoder");
		fileSink = new FileSink("fileSink");
		fileSink.setLocation(location);
	}
	
	public void setCaps(){
		this.caps = ElementFactory.make("capsfilter", "caps");
		
		String capsString = this.format+",rate="+this.samplingRate+",channels="+this.numberOfChannels+",width="+this.audioresample+",depth="+this.audioresample;;
		this.caps.setCaps(Caps.fromString(capsString));
		
	}
	
	private void run(){
		pipe = new Pipeline("pipeline");
		this.setCaps();
		pipe.addMany(mic,audioresample,audioconvert,encoder,fileSink);
		Element.linkMany(mic,audioresample,audioconvert, encoder,fileSink);
		
		pipe.setState(State.PLAYING);		
		Gst.main();
		pipe.setState(State.NULL);
	}
	
	
	public static void main(String[] args) {
		GStreamerLoader.loadGStreamer();
		args = Gst.init("AudioRecorder", args);
		AudioRecorderModel ar = new AudioRecorderModel("/Users/masterjildo/Desktop/audiothing.wav");
		ar.setFormat("audio-alaw");
		ar.setNumberOfChannels("1");
		ar.setSampleSize("16");
		ar.setSamplingRate("44100");
		ar.run();
	}

}
