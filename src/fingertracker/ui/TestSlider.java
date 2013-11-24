package fingertracker.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestSlider {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		final JSlider slider = new JSlider(0, 1000, 0);
		final JTextField text = new JTextField(5);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				text.setText(""+slider.getValue());
			}
		});
		panel.add(text);
		panel.add(slider);
		frame.add(panel);
		frame.setSize(400,200);
		frame.setVisible(true);
	}
}
