package gutzufusss.util;

import gutzufusss.ConfigDBController;

public class Config {
	/* config variables start here */
	public class ConfigVariables {
		public String		configName;
		public boolean		debug;
		public int			logLevel;
		public int			critConf;
	}
	public ConfigVariables curConf = new ConfigVariables();
	/* config variables end here */
	
	private Logger logger;
	private ConfigDBController configDB;

	public Config(Logger logger) {
		this.logger = logger;
		configDB = new ConfigDBController(this);
		
		initConfigVars();
	}
	
	public ConfigDBController getConfigDB() { return configDB; }
	
	public final class DefaultConfig {
		public final String			configName	= "default_config";
		public final boolean		debug		= false;
		public final int			logLevel	= Logger.LVL_INFO;
		public final int			critConf	= 55;
	}
	
	public DefaultConfig defConf = new DefaultConfig();
	
	private void initConfigVars() {
		// TODO: call function in configDB that attempts to load the config
		curConf.configName =		"default_config";
		curConf.debug =				defConf.debug;
		curConf.logLevel =			defConf.logLevel;
		curConf.critConf =			defConf.critConf;
	}
}
