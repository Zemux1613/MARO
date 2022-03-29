package EinSehrSchoenerStartBidlschirm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected JButton startButton;
	protected JTextField tf;
	
	public NewGamePanel() {
	
		setLayout(new FlowLayout());
		
		tf = new JTextField(30);
		tf.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				 if (tf.getText().equals("Please choose an username...")) {
			        tf.setText("");
			        tf.setForeground(Color.BLACK);
				 }	
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (tf.getText().isEmpty()) {
					tf.setForeground(Color.GRAY);
					tf.setText("Please choose an username...");
		        }
			}
		});
		
		startButton = new JButton("Start!");
		
		add(new JLabel("Name: "));
		add(tf);
		
		add(startButton);

	}
}