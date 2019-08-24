package gutzufusss.util;

import gutzufusss.ConfigDBController;

public class Config {
	/* ################ config variables ################ */
	public class ConfigVariables {
		public String		configName;
		public boolean		debug;
		public int			logLevel;
		public boolean			logAutoScroll;
		public int			critConf;

		public boolean		flGrayscale;
		public boolean		flBinary;
		public boolean		flSmooth;
		public boolean		flBorder;
		public boolean		flSWT;
		public boolean		flContrast;
	}
	public ConfigVariables curConfig = new ConfigVariables(); // live vars
	public ConfigVariables defConfig = new ConfigVariables(); // default vars
	/* ################ config variables ################ */

	private Logger logger;
	private ConfigDBController configDB;

	public Config(Logger logger) {
		this.logger = logger;
		configDB = new ConfigDBController(this);
		
		setDefaultConf();
		initConfigVars();
	}
	
	public ConfigDBController getConfigDB() { return configDB; }
	
	public void setDefaultConf () {
		defConfig.configName	= "default_config";
		defConfig.debug			= /*false*/true;
		defConfig.logLevel		= Logger./*LVL_INFO*/LVL_DEBUG;
		defConfig.logAutoScroll = true;
		defConfig.critConf		= 55;

		defConfig.flGrayscale	= true;
		defConfig.flBinary		= false;
		defConfig.flSmooth		= true;
		defConfig.flBorder		= true;
		defConfig.flSWT			= false;
		defConfig.flContrast	= false;
	}

	private void initConfigVars() {
		if(configDB.loadConfig("default_config"))
			return;

		logger.log(Logger.LVL_WARN, "Could not load configuration, using standard values.");

		curConfig = defConfig;
	}
}
