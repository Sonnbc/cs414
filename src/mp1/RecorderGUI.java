package mp1;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.gstreamer.Gst;

import common.GStreamerLoader;


class GUIValues {
	public static final int DEFAULT_RESOLUTION_X = 640;
  public static final int DEFAULT_RESOLUTION_Y = 480;
  public static final int DEFAULT_FRAME_RATE = 40;
 
	public int resolutionX, resolutionY, frameRate;
	public GUIValues() {
		this(DEFAULT_RESOLUTION_X, DEFAULT_RESOLUTION_Y, DEFAULT_FRAME_RATE);
	}
	public GUIValues(int x, int y, int f) {
		resolutionX = x;
		resolutionY = y;
		frameRate = f;
	}
}

public class RecorderGUI extends JFrame
{
	class MyPanel extends JPanel implements ActionListener{
    private static final long serialVersionUID = 1L;
    
        
    private JLabel resolutionXLabel;
    private JLabel resolutionYLabel;
    private JLabel frameRateLabel;
    
    private static final String resolutionXString = "Horizontal resolution: ";
    private static final String resolutionYString = "Vertical resolution: ";
    private static final String frameRateString = "Frame rate:";
    
    private JFormattedTextField resolutionXField;
    private JFormattedTextField resolutionYField;
    private JFormattedTextField frameRateField;
    
    private NumberFormat resolutionXFormat;
    private NumberFormat resolutionYFormat;
    private NumberFormat frameRateFormat;
    
    protected JButton startButton;
    
    public MyPanel() {
    		super(new BorderLayout());
    		setUpFormats();
    		
    		resolutionXLabel = new JLabel(resolutionXString);
    		resolutionYLabel = new JLabel(resolutionYString);
    		frameRateLabel = new JLabel(frameRateString);
    		
    		resolutionXField = new JFormattedTextField(resolutionXFormat);
    		resolutionXField.setValue(GUIValues.DEFAULT_RESOLUTION_X);
    		resolutionXField.setColumns(10);
    		
    		resolutionYField = new JFormattedTextField(resolutionYFormat);
    		resolutionYField.setValue(GUIValues.DEFAULT_RESOLUTION_Y);
    		resolutionYField.setColumns(10);
    		
    		frameRateField = new JFormattedTextField(frameRateFormat);
    		frameRateField.setValue(GUIValues.DEFAULT_FRAME_RATE);
    		frameRateField.setColumns(10);
    		
    		resolutionXLabel.setLabelFor(resolutionXField);
    		resolutionYLabel.setLabelFor(resolutionYField);
    		frameRateLabel.setLabelFor(frameRateField);
    		
    		JPanel labelPane = new JPanel(new GridLayout(0,1));
    		labelPane.add(resolutionXLabel);
    		labelPane.add(resolutionYLabel);
    		labelPane.add(frameRateLabel);
    		
    		JPanel fieldPane = new JPanel(new GridLayout(0,1));
    		fieldPane.add(resolutionXField);
    		fieldPane.add(resolutionYField);
    		fieldPane.add(frameRateField);
    		
    		ImageIcon startButtonIcon = new ImageIcon("res/right.gif");
        startButton = new JButton("Start", startButtonIcon);
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        startButton.setToolTipText("Click this button to start recording");
    		
    		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
        add(startButton, BorderLayout.PAGE_END);
    }
    
    private void setUpFormats() {
      resolutionXFormat = NumberFormat.getNumberInstance();
      resolutionYFormat = NumberFormat.getNumberInstance();
      frameRateFormat = NumberFormat.getNumberInstance();
    }
    
    public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	  }

    public void actionPerformed(ActionEvent e)
    {
    	 if ("start".equals(e.getActionCommand())) {
    		 
    		 /*SwingUtilities.invokeLater(new Runnable() {
          public void run()
          {*/
          	int x = ((Number)resolutionXField.getValue()).intValue();
       		 	int y = ((Number)resolutionYField.getValue()).intValue();
       		 	int f = ((Number)frameRateField.getValue()).intValue();
       		 	GUIValues v = new GUIValues(x, y, f);
						
       		 	Recorder.run(v);
          //}			 
    		 //});
    		 
    	 }
    }
	}
	
  private static final long serialVersionUID = 1L;

	public RecorderGUI() {
		setTitle("Recorder GUI");
		setLocation(10,200);
		
		getContentPane().add(new MyPanel());
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		GStreamerLoader.loadGStreamer();
    args = Gst.init("RecorderGUI", args);
    
    RecorderGUI gui = new RecorderGUI();
    gui.setVisible(true);
	}
}
