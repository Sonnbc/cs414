package mp1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

import org.gstreamer.lowlevel.BaseSrcAPI.Seek;
import org.gstreamer.query.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.SeekType;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.event.SeekEvent;
import org.gstreamer.swing.VideoComponent;

import common.GStreamerLoader;

public class VideoPlayer {

	private String filePath = null;
	private JTextField pathField;
	private PlayBin2 pipe;
	private Element source;
	private Element decoder;
	private Element sink;
	private boolean playing = false;
	
	
	private JButton createButton(String iconPath){
		JButton button = new JButton(new ImageIcon(iconPath));
		button.setBackground(Color.black);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		Border emptyBorder = BorderFactory.createEmptyBorder();
		button.setBorder(emptyBorder);
		button.setFocusPainted(false);
		return button;
	}
	
	private void makePipe(){
		source  = ElementFactory.make("filesrc", "file_source");
		source.set("location", filePath);
		decoder = ElementFactory.make("wavpackparse", "wav-decoder");
		Element converter = ElementFactory.make("audioconvert","converter");
		sink = ElementFactory.make("alsasink","alsa-output");
		pipe.addMany(source,decoder,converter ,sink);
		Element.linkMany(source,decoder,converter, sink);
		
	}
	
	public void run() {

			
				VideoComponent videoComponent = new VideoComponent();
				sink = videoComponent.getElement();
				//setup frame
				JFrame frame = new JFrame("VideoPlayer");
				//setup buttons
				JButton start = createButton("res/startButton.png");
				JButton pause = createButton("res/pauseButton.png");
				JButton rewind = createButton("res/rewindButton.png");
				JButton fastForward = createButton("res/forwardButton.png");
				
				JPanel buttonPanel = new JPanel(new GridLayout(0,4));
				buttonPanel.setBackground(Color.black);
				buttonPanel.add(rewind);
				buttonPanel.add(start);
				buttonPanel.add(pause);
				buttonPanel.add(fastForward);
				//setup text field
				JLabel pathLabel = new JLabel("File Location:");
				pathField = new JTextField(GUIValues.DEFAULT_LOCATION);
				pathField.setColumns(10);
				pathLabel.setLabelFor(pathField);
				JPanel fieldPanel = new JPanel(new GridLayout(0,2));
				fieldPanel.add(pathLabel);
				fieldPanel.add(pathField);
				//add panels to frame
				frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
				frame.getContentPane().add(fieldPanel, BorderLayout.PAGE_START);
				frame.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
				frame.setPreferredSize(new Dimension(640, 600));
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
				
				
				
				start.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String path =  pathField.getText();
						if (filePath == null || !filePath.equals(path)){
							if (playing) pipe.setState(State.PAUSED);
							//makePipe();
							pipe = new PlayBin2("pipeline");
							filePath = path;
							pipe.setVideoSink(sink);
							pipe.setInputFile(new File(filePath));
							pipe.setState(State.PLAYING);
							playing = true;
							
						}
						pipe.setState(State.PLAYING);
					}
				});
				pause.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						pipe.setState(State.PAUSED);
						playing = false;
					}
				});
				rewind.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						long position;
						position = pipe.queryPosition(Format.TIME);
						SeekEvent seek = new SeekEvent(1.0,Format.TIME, 1 << 0 , SeekType.SET, position - 2000000000, SeekType.NONE, -1);
						pipe.sendEvent(seek);
					}
				});
				fastForward.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						long position;
						position = pipe.queryPosition(Format.TIME);
						SeekEvent seek = new SeekEvent(1.0,Format.TIME, 1 << 0 , SeekType.SET, position + 2000000000, SeekType.NONE, -1);
						pipe.sendEvent(seek);
					}
				});


			
		
		Gst.main();
		pipe.setState(State.NULL);
	}
	
	public static void main(String[] args) {
		GStreamerLoader.loadGStreamer();
		args = Gst.init("VideoPlayerGUI", args);
		VideoPlayer vp = new VideoPlayer();
		vp.run();
	}
}