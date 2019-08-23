package gutzufusss;

import javax.swing.SwingUtilities;

import gutzufusss.gui.GUIController;
import gutzufusss.gui.GUIModel;
import gutzufusss.gui.GUIView;
import gutzufusss.util.Logger;

public class GUI {
	public GUIController guiCtrl;

	public GUI(Logger logger, Main m) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUIModel(logger, m);
			}
		});
	}
}
