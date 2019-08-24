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
	public ConfigVariables curConf = new ConfigVariables(); // live vars
	public ConfigVariables defConf = new ConfigVariables(); // default vars
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
		defConf.configName		= "default_config";
		defConf.debug			= false;
		defConf.logLevel		= Logger.LVL_INFO;
		defConf.critConf		= 55;
	}

	private void initConfigVars() {
		if(configDB.loadConfig("default_config"))
			return;

		logger.log(Logger.LVL_WARN, "Could not load configuration, using standard values.");

		curConf = defConf;
	}
}
