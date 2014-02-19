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
  public static final String DEFAULT_LOCATION = "/tmp/a.avi";
  public static final String DEFAULT_VIDEO_ENCODING = "mjpeg";
  public static final String DEFAULT_AUDIO_ENCODING = "alawenc";
  public static final int DEFAULT_AUDIO_RATE = 44100;
  public static final int DEFAULT_SAMPLE_SIZE = 16;
 
	public int resolutionX = DEFAULT_RESOLUTION_X; 
	public int resolutionY = DEFAULT_RESOLUTION_Y;
	public int frameRate = DEFAULT_FRAME_RATE;
	public String location = DEFAULT_LOCATION;
	public String videoEncoding = DEFAULT_VIDEO_ENCODING;
	public String audioEncoding = DEFAULT_AUDIO_ENCODING;
	public int audioRate = DEFAULT_AUDIO_RATE;
	public int sampleSize = DEFAULT_SAMPLE_SIZE;
	
	
	public GUIValues() {
		//do nothing;
	}
	
	public GUIValues(int x, int y, int f, String l, String ev, String ea, int s, int r) {
		resolutionX = x;
		resolutionY = y;
		frameRate = f;
		location = l;
		videoEncoding = ev;
		audioEncoding = ea;
		sampleSize = s;
		audioRate = r;
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
    private JLabel videoEncodingLabel = new JLabel("Video encoding :");
    private JLabel audioEncodingLabel = new JLabel("Audio encoding :");
    private JLabel sampleSizeLabel = new JLabel("Audio sample size:");
    private JLabel rateLabel = new JLabel("Audio rate:");
    
    private JFormattedTextField resolutionXField;
    private JFormattedTextField resolutionYField;
    private JFormattedTextField frameRateField;
    private JTextField locationField;
    private JComboBox videoEncodingField;
    private JComboBox audioEncodingField;
    private JFormattedTextField sampleSizeField;
    private JFormattedTextField rateField;
    
    private NumberFormat numberFormat;
    
    protected JButton startButton;
    
    public MyPanel() {
    		super(new BorderLayout());
    		numberFormat = NumberFormat.getNumberInstance();
    		
    		resolutionXField = new JFormattedTextField(numberFormat);
    		resolutionXField.setValue(GUIValues.DEFAULT_RESOLUTION_X);
    		resolutionXField.setColumns(10);
    		
    		resolutionYField = new JFormattedTextField(numberFormat);
    		resolutionYField.setValue(GUIValues.DEFAULT_RESOLUTION_Y);
    		resolutionYField.setColumns(10);
    		
    		frameRateField = new JFormattedTextField(numberFormat);
    		frameRateField.setValue(GUIValues.DEFAULT_FRAME_RATE);
    		frameRateField.setColumns(10);
    		
    		locationField = new JTextField(GUIValues.DEFAULT_LOCATION);
    		locationField.setColumns(10);
    		
    		String[] videoEncodingOptions = {"mjpeg", "mpeg4"};
    		videoEncodingField = new JComboBox(videoEncodingOptions);
    		videoEncodingField.setSelectedIndex(0);
    		
    		String[] audioEncodingOptions = {"alawenc", "mulawenc"};
    		audioEncodingField = new JComboBox(audioEncodingOptions);
    		audioEncodingField.setSelectedIndex(0);
    		
    		sampleSizeField = new JFormattedTextField(numberFormat);
    		sampleSizeField.setValue(GUIValues.DEFAULT_SAMPLE_SIZE);
    		sampleSizeField.setColumns(10);
    		
    		rateField = new JFormattedTextField(numberFormat);
    		rateField.setValue(GUIValues.DEFAULT_AUDIO_RATE);
    		rateField.setColumns(10);
    		
    		resolutionXLabel.setLabelFor(resolutionXField);
    		resolutionYLabel.setLabelFor(resolutionYField);
    		frameRateLabel.setLabelFor(frameRateField);
    		locationLabel.setLabelFor(locationField);
    		videoEncodingLabel.setLabelFor(videoEncodingField);
    		audioEncodingLabel.setLabelFor(audioEncodingField);
    		sampleSizeLabel.setLabelFor(sampleSizeField);
    		rateLabel.setLabelFor(rateField);
    		
    		JPanel labelPane = new JPanel(new GridLayout(0,1));
    		labelPane.add(resolutionXLabel);
    		labelPane.add(resolutionYLabel);
    		labelPane.add(frameRateLabel);
    		labelPane.add(locationLabel);
    		labelPane.add(videoEncodingLabel);
    		labelPane.add(audioEncodingLabel);
    		labelPane.add(sampleSizeLabel);
    		labelPane.add(rateLabel);
    		
    		JPanel fieldPane = new JPanel(new GridLayout(0,1));
    		fieldPane.add(resolutionXField);
    		fieldPane.add(resolutionYField);
    		fieldPane.add(frameRateField);
    		fieldPane.add(locationField);
    		fieldPane.add(videoEncodingField);
    		fieldPane.add(audioEncodingField);
    		fieldPane.add(sampleSizeField);
    		fieldPane.add(rateField);
    		
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
        
    public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	  }

    public void actionPerformed(ActionEvent event)
    {
    	 if ("start".equals(event.getActionCommand())) {
        	int x = ((Number)resolutionXField.getValue()).intValue();
     		 	int y = ((Number)resolutionYField.getValue()).intValue();
     		 	int f = ((Number)frameRateField.getValue()).intValue();
     		 	String l = locationField.getText();
     		 	String ev = (String) videoEncodingField.getSelectedItem();
     		 	String ea = (String) audioEncodingField.getSelectedItem();
     		 	int s = ((Number) sampleSizeField.getValue()).intValue();
     		 	int r = ((Number) rateField.getValue()).intValue();
     		 	
     		 	GUIValues v = new GUIValues(x, y, f, l, ev, ea, s, r);
						
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
