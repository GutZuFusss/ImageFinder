package gutzufusss;

import javax.swing.SwingUtilities;

import gutzufusss.gui.GUIView;

public class GUI {

	public GUI(Main m) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUIView();
			}
		});
	}
}
