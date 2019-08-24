package gutzufusss.gui;

import gutzufusss.Main;
import gutzufusss.util.Config;
import gutzufusss.util.Logger;

public class GUIModel {
	private Logger logger;
	private Config config;
	private Main controller; // for communication with the main programm
	private GUIController guiCtrl;

	public GUIModel(Logger logger, Config config, Main m) {
		this.config = config;
		this.logger = logger;
		controller = m;
		guiCtrl = new GUIController(logger, this);
	}
	
	public void userBrowsePath() {
		// TODO: handle
	}
	
	public void updateLogLevel(String selectedItem) {
		int newLogLvl = Integer.parseInt(selectedItem.split(":")[0]);
		config.curConf.logLevel = newLogLvl;
	}
}
