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
	
	public ConfigVariables defConf = new ConfigVariables();
	
	private Logger logger;
	private ConfigDBController configDB;

	public Config(Logger logger) {
		this.logger = logger;
		configDB = new ConfigDBController(this);
		
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
		// TODO: call function in configDB that attempts to load the config or else just set curConf = defConf
		
		curConf = defConf;
	}
}
