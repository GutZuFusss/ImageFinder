package gutzufusss;

import java.sql.ResultSet;
import java.sql.SQLException;

import gutzufusss.util.Config;
import gutzufusss.util.Logger;
import gutzufusss.wrapper.SQLWrapper;

public class ConfigDBController extends SQLWrapper {
	public static final String TABLE_CONF = "config_data";
	
	private Config config;

	public ConfigDBController(Config config) {
		this.config = config;
	}

	@Override
	public void tableCheck() throws SQLException {
		logger.log(Logger.LVL_DEBUG, "Attempting to create table: " + TABLE_CONF);
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_CONF + "';").next()) {
			logger.log(Logger.LVL_DEBUG, "Found SQL table " + TABLE_CONF);
			return;
		}
		
		logger.log(Logger.LVL_DEBUG, "Attempting to create table: " + TABLE_CONF);
		
		// create our config table
		execSQL("CREATE TABLE " + TABLE_CONF + " " +
				"(config_name		VARCHAR PRIMARY KEY," +	// name of the configuration
				" debug 			BOOLEAN," +				// enables/disables debugging mode
				" log_lvl			INTEGER," +				// logging level
				" crit_conf			INTEGER)");				// critical confidence treshold
		
		// insert a default entry
		execSQL("INSERT INTO " + TABLE_CONF + "(config_name, debug, log_lvl, crit_conf) VALUES (" +
				"'" +	"default_config"				+ "', " +
						config.defConfig.debug			+ ", " +
						config.defConfig.logLevel			+ ", " +
						config.defConfig.critConf			+ ");");
		

		logger.log(Logger.LVL_INFO, "SQL table '" + TABLE_CONF + "' was generated.");
	}
	
	public boolean loadConfig(String configName) { // TODO: continue haaaah
		//ResultSet result = execQuerry("SELECT * FROM " + TABLE_CONF + " WHERE config_name='" + configName + "';");
		return false;
	}
}
