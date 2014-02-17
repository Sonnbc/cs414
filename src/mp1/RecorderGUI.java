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
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gstreamer.Gst;

import common.GStreamerLoader;


class GUIValues {
	public static final int DEFAULT_RESOLUTION_X = 640;
  public static final int DEFAULT_RESOLUTION_Y = 480;
  public static final int DEFAULT_FRAME_RATE = 20;
  public static final String DEFAULT_LOCATION = "/Users/son/Downloads/a.avi";
  public static final String DEFAULT_ENCODING = "raw";
 
	public int resolutionX = DEFAULT_RESOLUTION_X; 
	public int resolutionY = DEFAULT_RESOLUTION_Y;
	public int frameRate = DEFAULT_FRAME_RATE;
	public String location = DEFAULT_LOCATION;
	public String encoding = DEFAULT_ENCODING;
	
	public GUIValues() {
		//do nothing;
	}
	
	public GUIValues(int x, int y, int f, String s, String e) {
		resolutionX = x;
		resolutionY = y;
		frameRate = f;
		location = s;
		encoding = e;
	}
}

public class RecorderGUI extends JFrame
{
	class MyPanel extends JPanel implements ActionListener{
    private static final long serialVersionUID = 1L;
    
        
    private JLabel resolutionXLabel = new JLabel("Horizontal resolution: ");
    private JLabel resolutionYLabel = new JLabel("Vertical resolution: ");
    private JLabel frameRateLabel = new JLabel("Frame rate :");
    private JLabel locationLabel = new JLabel("Save as: ");
    private JLabel encodingLabel = new JLabel("Encoding :");
    
    private JFormattedTextField resolutionXField;
    private JFormattedTextField resolutionYField;
    private JFormattedTextField frameRateField;
    private JTextField locationField;
    private JComboBox encodingField;
    
    private NumberFormat resolutionXFormat;
    private NumberFormat resolutionYFormat;
    private NumberFormat frameRateFormat;
    
    protected JButton startButton;
    
    public MyPanel() {
    		super(new BorderLayout());
    		setUpFormats();
    		
    		resolutionXField = new JFormattedTextField(resolutionXFormat);
    		resolutionXField.setValue(GUIValues.DEFAULT_RESOLUTION_X);
    		resolutionXField.setColumns(10);
    		
    		resolutionYField = new JFormattedTextField(resolutionYFormat);
    		resolutionYField.setValue(GUIValues.DEFAULT_RESOLUTION_Y);
    		resolutionYField.setColumns(10);
    		
    		frameRateField = new JFormattedTextField(frameRateFormat);
    		frameRateField.setValue(GUIValues.DEFAULT_FRAME_RATE);
    		frameRateField.setColumns(10);
    		
    		locationField = new JTextField(GUIValues.DEFAULT_LOCATION);
    		locationField.setColumns(10);
    		
    		String[] encodingOoptions = {"raw", "mjpeg", "mpeg4"};
    		encodingField = new JComboBox(encodingOoptions);
    		encodingField.setSelectedIndex(0);
    		
    		resolutionXLabel.setLabelFor(resolutionXField);
    		resolutionYLabel.setLabelFor(resolutionYField);
    		frameRateLabel.setLabelFor(frameRateField);
    		locationLabel.setLabelFor(locationField);
    		encodingLabel.setLabelFor(encodingField);
    		
    		JPanel labelPane = new JPanel(new GridLayout(0,1));
    		labelPane.add(resolutionXLabel);
    		labelPane.add(resolutionYLabel);
    		labelPane.add(frameRateLabel);
    		labelPane.add(locationLabel);
    		labelPane.add(encodingLabel);
    		
    		JPanel fieldPane = new JPanel(new GridLayout(0,1));
    		fieldPane.add(resolutionXField);
    		fieldPane.add(resolutionYField);
    		fieldPane.add(frameRateField);
    		fieldPane.add(locationField);
    		fieldPane.add(encodingField);
    		
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

    public void actionPerformed(ActionEvent event)
    {
    	 if ("start".equals(event.getActionCommand())) {
        	int x = ((Number)resolutionXField.getValue()).intValue();
     		 	int y = ((Number)resolutionYField.getValue()).intValue();
     		 	int f = ((Number)frameRateField.getValue()).intValue();
     		 	String s = locationField.getText();
     		 	String e = (String) encodingField.getSelectedItem();
     		 	GUIValues v = new GUIValues(x, y, f, s, e);
						
       		(new Recorder()).run(v);  		 
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