package gutzufusss.gui;

import java.io.File;

import javax.swing.JFileChooser;

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
		// open dialog
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select the directory you want to scan");
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedDir = fc.getSelectedFile().getAbsolutePath();
			guiCtrl.setDirPath(selectedDir);
		}
	}

	public void updateLogLevel(String selectedItem) {
		int newLogLvl = Integer.parseInt(selectedItem.split(":")[0]);
		config.curConf.logLevel = newLogLvl;
	}
	
	public void startScanning(String path) {
		if(new File(path).exists())
			controller.getOCR().scanDirectory(path);
		else
			logger.log(Logger.LVL_ERROR, "The selected directory does not seem to exist.");
	}
}
