package gutzufusss;

import java.sql.SQLException;

import gutzufusss.util.Logger;
import gutzufusss.wrapper.SQLWrapper;

public class ConfigDBController extends SQLWrapper {
	public static final String TABLE_CONF = "config_data";

	public ConfigDBController(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void tableCheck() throws SQLException {
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_CONF + "';").next()) {
			logger.log(Logger.LVL_DEBUG, "Found SQL table " + TABLE_CONF);
			return;
		}
		
		logger.log(Logger.LVL_DEBUG, "Attempting to create table: " + TABLE_CONF);

		execSQL("CREATE TABLE " + TABLE_CONF + " " + 
				"(debug 		BOOLEAN," + 			// enables/disables debugging mode
				" log_lvl		INTEGER," + 			// logger level
				" crit_conf		INTEGER)"); 			// critical confidence treshold

		logger.log(Logger.LVL_INFO, "SQL table '" + TABLE_CONF + "' was generated.");
	}
}
