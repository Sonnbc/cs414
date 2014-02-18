package mp1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.gstreamer.Buffer;
import org.gstreamer.elements.AppSink;

/**
 * 
 * @author etubil2
 *This class monitors the time it takes to compress/decompress and the compress/decompress ratio
 *To use it use it, put a tee before the encoder/decoder and a tee after it. Connect an app sink
 *to the other ends of the tees('beginning' appsink before and ending 'appsink' after)
 *then start the thread and a window should pop up. 
 */
public class Monitor implements Runnable{
	private AppSink beginning;
	private AppSink end;
	private String type;
	JLabel textLabel;
	
	/**
	 * Constructor
	 * @param beginning - beginning appsink
	 * @param end -ending appsink
	 * @param type -what type to be printed out 'compression' 'decompression'
	 */
	public Monitor(AppSink beginning, AppSink end, String type) {
		this.beginning = beginning;
		this.end = end;
		this.type = type;
		JFrame frame = new JFrame("Monitor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textLabel = new JLabel(type+" time: N/A");
		textLabel.setPreferredSize(new Dimension(300,100));
		frame.getContentPane().add(textLabel,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	/***
	 * Main body to be ran by thread
	 */
	public void run(){
		Buffer be;
		Buffer ed;
		long beginningTime;
		long endingTime;
		long processingTime;
		float compressionRatio;
		int beginningSize;
		int endingSize;
		StringBuffer buffer = new StringBuffer(64);
		
		while(true){
			be = beginning.pullBuffer();
			beginningSize = be.getSize();
			beginningTime = System.currentTimeMillis();
			ed = end.pullBuffer();
			endingTime = System.currentTimeMillis();
			endingSize = ed.getSize();
			compressionRatio = beginningSize/endingSize;
			processingTime = endingTime-beginningTime;
			buffer.append(type);
			buffer.append(type);
			buffer.append(" time: ");
			buffer.append(processingTime);
			buffer.append(type);
			buffer.append(" ");
			buffer.append(" ratio: ");
			buffer.append(compressionRatio);
			textLabel.setText(buffer.toString());
			buffer.delete(0, buffer.length());
		}
	}
	public static void main(String args[]){
		Monitor m = new Monitor(null,null,"Compression");//the two nulls would be replaced the the appsinks
		new Thread(m).start(); //this is an example
	}
	
}
