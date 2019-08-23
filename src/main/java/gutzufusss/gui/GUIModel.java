package gutzufusss.gui;

import gutzufusss.Main;
import gutzufusss.util.Logger;

public class GUIModel {
	private Main controller; // for communication with the main programm
	private GUIController guiCtrl;
	private Logger logger;

	public GUIModel(Logger logger, Main m) {
		this.logger = logger;
		controller = m;
		guiCtrl = new GUIController(logger, this);
	}
}
