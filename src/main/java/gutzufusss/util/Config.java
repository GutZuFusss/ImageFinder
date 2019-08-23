package gutzufusss.util;

import gutzufusss.ConfigDBController;

public class Config {
	private Logger logger;
	private ConfigDBController configDB;

	public Config(Logger logger) {
		this.logger = logger;
		configDB = new ConfigDBController(this);
	}
	
	public ConfigDBController getConfigDB() { return configDB; }
	
	public final class DefaultConfig {
		public final boolean		debug		= false;
		public final int			logLevel	= Logger.LVL_INFO;
		public final int			critConf	= 55;
	}
	
	public DefaultConfig defConf = new DefaultConfig();
}
