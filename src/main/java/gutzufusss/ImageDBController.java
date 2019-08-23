package gutzufusss;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import gutzufusss.util.Logger;
import gutzufusss.wrapper.SQLWrapper;

public class ImageDBController extends SQLWrapper {
	public static final int 	MAX_IMG_TEXT_LEN 	= 4096;
	public static final String 	TABLE_IMG 			= "image_data";

	public ImageDBController(Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean startUpCheck() throws SQLException {
		File db = new File(DB_PATH);

		try {
			if(!db.exists()) {
				if(!db.getParentFile().exists()) // also check if we have to create the db directory
					db.getParentFile().mkdir();
				if(db.createNewFile()) {
					logger.log(Logger.LVL_INFO, "Database has been created. Attempting to create tables...");
				} else {
					logger.log(Logger.LVL_FATAL, "Could not open nor create database!!! Shutting down.");
					return false;
				}
			}
		} catch(IOException e) {
			logger.log(Logger.LVL_FATAL, "I/O error occured while creating database: " + e.getMessage());
			return false;
		}

		// okay, we are now sure our database exists, lets check for the tables
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_IMG + "';").next()) {
			logger.log(Logger.LVL_DEBUG, "Found SQL table " + TABLE_IMG);
			return true;
		}

		execSQL("CREATE TABLE " + TABLE_IMG + " " +
		        "(id 			INTEGER PRIMARY KEY AUTOINCREMENT," +				// pkey
		        " name			VARCHAR(256)," +									// max filename length is 255 (on win at least, haaaaah)
		        " abs_path		VARCHAR(1024)," +									// absolute path to the file
		        " ocr_data		VARCHAR(" + MAX_IMG_TEXT_LEN + ")," +	// text that was found in the image
		        " confidence	INTEGER)");											// how sure the ocr was about the result
		
		logger.log(Logger.LVL_INFO, "SQL table generation was successful.");

		return true;
	}

}
