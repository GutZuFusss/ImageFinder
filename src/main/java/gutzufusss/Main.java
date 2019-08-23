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
		
		try {
			if(imgDB.startUpCheck() == false)
				Thread.sleep(Integer.MAX_VALUE); // i don't even know under what circumstances this could ever happen... probably the program trying to create the database file without sufficient permissions. i know this is shit btw but i could not care less, except about the length this comment already has. ultra wide screen ftw!
		} catch(InterruptedException | SQLException e) {
			if(e instanceof InterruptedException)
				getLogger().log(Logger.LVL_ERROR, "Aaaaaawww maaaaaaaannn!");
			else if(e instanceof SQLException)
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
