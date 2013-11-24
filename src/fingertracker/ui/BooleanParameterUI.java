package fingertracker.ui;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fingertracker.BooleanParameter;
import fingertracker.ParameterListener;

public class BooleanParameterUI extends ParameterUI {
	private static final long serialVersionUID = 1L;

	private final JCheckBox checkBox;
	
	public BooleanParameterUI(final BooleanParameter param) {
		super(param);
		checkBox = new JCheckBox();
		add(checkBox);
		
		checkBox.setSelected(param.getValue());
		checkBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				checkBox.removeChangeListener(this);
				param.setValue(checkBox.isSelected());
				checkBox.addChangeListener(this);
			}
		});
		
		param.addListener(new ParameterListener<Boolean>() {
			@Override
			public void valueChanged(Boolean value) {
				checkBox.setSelected(value);
			}
		});
	}
}
