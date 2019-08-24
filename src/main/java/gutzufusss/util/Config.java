package gutzufusss.util;

import gutzufusss.ConfigDBController;

public class Config {
	/* ################ config variables ################ */
	public class ConfigVariables {
		public String		configName;
		public boolean		debug;
		public int			logLevel;
		public int			critConf;
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
	
	public void setDefaultConf (){
		defConfig.configName		= "default_config";
		defConfig.debug			= false;
		defConfig.logLevel		= Logger.LVL_INFO;
		defConfig.critConf		= 55;
	}

	private void initConfigVars() {
		if(configDB.loadConfig("default_config"))
			return;

		logger.log(Logger.LVL_WARN, "Could not load configuration, using standard values.");

		curConfig = defConfig;
	}
}
