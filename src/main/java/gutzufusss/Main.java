package gutzufusss;
import java.sql.SQLException;

import gutzufusss.util.ImageManipulation;
import gutzufusss.util.Logger;
import gutzufusss.wrapper.OCRWrapper;
import gutzufusss.wrapper.SQLWrapper;

public class Main {
	private Logger logger;
	private ImageDBController imgDB;
	private ConfigDBController configDB;
	private OCRWrapper ocrWrapper;
	private ImageManipulation imgManipulator;

	public Main() {
		logger = new Logger();
		imgDB = new ImageDBController(logger);
		configDB = new ConfigDBController(logger);
		ocrWrapper = new OCRWrapper(this, imgDB);
		imgManipulator = new ImageManipulation(this);
		
		// database & table checks
		try {
			SQLWrapper.checkDB(logger);
			imgDB.tableCheck();
			configDB.tableCheck();
		} catch(SQLException e) {
			getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
		}

		getLogger().log(Logger.LVL_INFO, "ImageFinder initialized successfully!");

		ocrWrapper.scanDirectory("test_images");
	}

	public OCRWrapper getOCR() { return ocrWrapper; }

	public ImageManipulation getIMGManipulator() { return imgManipulator; }

	public Logger getLogger() { return logger; }

	public static void main(String[] args) { // entry point
		new Main();
	}
}
