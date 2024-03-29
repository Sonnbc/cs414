package tutorials;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.gstreamer.Gst;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.swing.VideoComponent;

import common.GStreamerLoader;

public class VideoPlayer {
  public static void main(String[] args) {
  		GStreamerLoader.loadGStreamer();
      args = Gst.init("VideoPlayer", args);
      final PlayBin playbin = new PlayBin("VideoPlayer");
      playbin.setInputFile(new File(args[0]));

      SwingUtilities.invokeLater(new Runnable() {

          public void run() {
              VideoComponent videoComponent = new VideoComponent();
              playbin.setVideoSink(videoComponent.getElement());

              JFrame frame = new JFrame("VideoPlayer");
              frame.getContentPane().add(videoComponent, BorderLayout.CENTER);
              frame.setPreferredSize(new Dimension(640, 480));
              frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
              frame.pack();
              frame.setVisible(true);
              playbin.setState(State.PLAYING);
          }
      });
      Gst.main();
      playbin.setState(State.NULL);
  }
}
