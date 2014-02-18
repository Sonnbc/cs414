package mp1;

import java.awt.BorderLayout;
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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.SeekType;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.event.SeekEvent;
import org.gstreamer.swing.VideoComponent;

import common.GStreamerLoader;

public class VideoPlayer {
	public static void main(String[] args) {
		GStreamerLoader.loadGStreamer();
		args = Gst.init("VideoPlayer", args);
		final PlayBin2 playbin = new PlayBin2("VideoPlayer");
		playbin.setInputFile(new File(
				"Amazing Race All-Stars - Meet the Teams.mp4"));

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				VideoComponent videoComponent = new VideoComponent();
				playbin.setVideoSink(videoComponent.getElement());
				
				
				JFrame frame = new JFrame("VideoPlayer");
				ImageIcon startButtonIcon = new ImageIcon("Silver_Play_Button.jpg");
				JButton start = new JButton("Start", startButtonIcon);
				JButton pause = new JButton("Pause");
				JButton rewind = new JButton("Rewind");
				JButton fastForward = new JButton ("Fast Forward");
				frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
				frame.getContentPane().add(start, BorderLayout.PAGE_END);
				frame.getContentPane().add(pause, BorderLayout.PAGE_START);
				frame.getContentPane().add(rewind, BorderLayout.LINE_START);
				frame.getContentPane().add(fastForward, BorderLayout.LINE_END);
				frame.setPreferredSize(new Dimension(640, 480));
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
				start.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						playbin.setState(State.PLAYING);
					}
				});
				pause.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						playbin.setState(State.PAUSED);
					}
				});
				rewind.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						long position;
						position = playbin.queryPosition(Format.TIME);
						SeekEvent seek = new SeekEvent(1.0,Format.TIME, 1 << 0 , SeekType.SET, position - 2000000000, SeekType.NONE, -1);
						playbin.sendEvent(seek);
					}
				});
				fastForward.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						long position;
						position = playbin.queryPosition(Format.TIME);
						SeekEvent seek = new SeekEvent(1.0,Format.TIME, 1 << 0 , SeekType.SET, position + 2000000000, SeekType.NONE, -1);
						playbin.sendEvent(seek);
					}
				});


			}
		});
		Gst.main();
		playbin.setState(State.NULL);
	}
}