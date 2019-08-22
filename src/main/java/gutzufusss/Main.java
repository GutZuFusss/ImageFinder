package gutzufusss;
import java.sql.SQLException;

import gutzufusss.util.ImageManipulation;
import gutzufusss.util.Logger;
import gutzufusss.wrapper.OCRWrapper;
import gutzufusss.wrapper.SQLWrapper;

public class Main {
	private OCRWrapper ocrWrapper;
	private ImageManipulation imgManipulator;
	private Logger logger;

	public Main() {
		try {
			if(SQLWrapper.startUpCheck() == false)
				Thread.sleep(Integer.MAX_VALUE); // i don't even know under what circumstances this could ever happen... probably the program trying to create the database file without sufficient permissions. i know this is shit btw but i could not care less, except about the length this comment already has. ultra wide screen ftw!
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace(); // TODO: i guess you know what to do here by now...
			}

		ocrWrapper = new OCRWrapper(this);
		imgManipulator = new ImageManipulation();
		logger = new Logger();
		
		getLogger().log(Logger.LVL_INFO, "ImageFinder initialized successfully!", true);
		
		//ocrWrapper.scanDirectory("test_images");
	}

	public OCRWrapper getOCR() { return ocrWrapper; }

	public ImageManipulation getIMGManipulator() { return imgManipulator; }

	public Logger getLogger() { return logger; }

	public static void main(String[] args) { // entry point
		new Main();
	}
}
