package mp1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioRecorderViewControlller {
	
	public class AudioRecorderView extends JFrame{
		
	}
	public class MyPanel extends JPanel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public class AudioRecorderController {
		
	}

	public static void main(String args[]){
		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel textLabel = new JLabel("Audio Player");
		textLabel.setPreferredSize(new Dimension(300,100));
		frame.getContentPane().add(textLabel,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
