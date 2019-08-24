package gutzufusss;
import java.sql.SQLException;

import gutzufusss.util.Config;
import gutzufusss.util.ImageManipulation;
import gutzufusss.util.Logger;
import gutzufusss.wrapper.OCRWrapper;
import gutzufusss.wrapper.SQLWrapper;

public class Main {
	private Logger logger;
	private Config config;
	private ImageDBController imgDB;
	private OCRWrapper ocrWrapper;
	private ImageManipulation imgManipulator;

	public Main() {
		logger = new Logger();
		config = new Config(logger);
		imgDB = new ImageDBController();
		imgManipulator = new ImageManipulation(logger);

		SQLWrapper.setLogger(logger);
		logger.setConfig(config);

		// database & table checks
		try {
			SQLWrapper.checkDB();
			imgDB.tableCheck();
			config.getConfigDB().tableCheck();
		} catch(SQLException e) {
			logger.log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
		}

		new GUI(logger, config, this);

		logger.log(Logger.LVL_INFO, "ImageFinder initialized completely successfully!");

		//ocrWrapper.scanDirectory("test_images");
	}

	public OCRWrapper getOCR() { return ocrWrapper; }

	public ImageDBController getImgDB() { return imgDB; }

	public ImageManipulation getIMGManipulator() { return imgManipulator; }

	public static void main(String[] args) { // entry point
		new Main();
	}
}
