package fingertracker.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fingertracker.NumericParameter;
import fingertracker.ParameterListener;

public class NumericParameterUI extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JSlider slider;
	private final JLabel label;
	private final JTextField text;
	
	public NumericParameterUI(final NumericParameter param) {
		super();
		label = new JLabel(param.getName());
		text = new JTextField(5);
		text.setEnabled(false);
		int n = (int)Math.ceil(param.getRange() / param.getInc());
		slider = new JSlider(0, n, 0);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(label);
		add(text);
		add(slider);
		
		param.addListener(new ParameterListener<Double>() {
			@Override
			public void valueChanged(Double value) {
				int idx = (int)Math.floor((value - param.getStart()) / param.getInc());
				slider.setValue(idx);
				text.setText(String.format("%.2f", value));
			}
		});
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int idx = slider.getValue();
				slider.removeChangeListener(this);
				param.setValue(param.getStart() + idx * param.getInc());
				slider.addChangeListener(this);
			}
		});
	}
}
