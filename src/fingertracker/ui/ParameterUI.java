package fingertracker.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fingertracker.Param;


public class ParameterUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JLabel label;
	
	public ParameterUI(Param<?> param) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		label = new JLabel(param.getName());
		add(label);
	}
}
