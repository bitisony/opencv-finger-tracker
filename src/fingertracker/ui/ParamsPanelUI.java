package fingertracker.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import fingertracker.Param;

public class ParamsPanelUI extends JPanel {
	private static final long serialVersionUID = 1L;

	public ParamsPanelUI(ArrayList<Param<?>> params) {
		super();
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
		setLayout(new GridBagLayout());
		for (Param<?> p : params) {
			add(ParamUI.getUI(p), gbc);
		}
	}
}
