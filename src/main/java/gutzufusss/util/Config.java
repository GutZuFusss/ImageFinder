package gutzufusss.util;

import gutzufusss.ConfigDBController;

public class Config {
	private Logger logger;
	private ConfigDBController configDB;

	public Config(Logger logger) {
		this.logger = logger;
		configDB = new ConfigDBController(logger);
	}
	
	public ConfigDBController getConfigDB() { return configDB; }
}
