package gutzufusss.util;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

public class Logger {
	public static final int LVL_DEBUG = 1;
	public static final int LVL_FATAL = 2;
	public static final int LVL_ERROR = 4;
	public static final int LVL_WARN = 8;
	public static final int LVL_INFO = 16;

	private final String LOG_PATH = "logs/log_" + getTimestamp(true) + ".log";

	private File logFile;

	public Logger() {
		logFile = new File(LOG_PATH);
		if(!logFile.getParentFile().exists())
			logFile.getParentFile().mkdir();

		// now it is safe to create the current's session logfile
		try {
			if(!logFile.exists()) {
				if(logFile.createNewFile()) {
					log(LVL_INFO, "Log file was created.");
				}
			}
		} catch(IOException e) {
			if(e instanceof IOException) {
				log(LVL_FATAL, "Something went horribly wrong (I/O).");
				e.printStackTrace(); // kinda nonsense to log something here...
			}
		}
	}

	public void log(int lvl, String msg) {
		if(/*!DEBUG_MODE &&*/ lvl == LVL_DEBUG)
			return;

		String calledFrom = Thread.currentThread().getStackTrace()[2].getClassName(); // travel back 2 calls on the call stack
		int numDots = (int)calledFrom.chars().filter(ch -> ch == '.').count(); // unnecessary but i use lambdas way to infrequent + i like spaghetti code
		if(numDots != 0)
			calledFrom = calledFrom.split("\\.")[numDots]; // don't display the package path to the class... noone cares
		calledFrom += "::" + Thread.currentThread().getStackTrace()[2].getMethodName();

		String logMsg = "{" + getErrLvlStrin(lvl) + "}" + "[" + getTimestamp() + "]:" + "[" + calledFrom + "]>> " + msg; // prepare the message

		try {
			FileUtils.writeStringToFile(logFile, logMsg + "\n", "UTF-8", true);
		} catch(IOException e) {
			log(LVL_ERROR, "We seem to have some kind of log-ception here.");
			return;
		}

		System.out.println(logMsg);

		// TODO: also print to gui once there is one
	}

	private String getErrLvlStrin(int lvl) {
		switch(lvl){
		case LVL_INFO:
			return "INFO ";
		case LVL_WARN:
			return "WARN ";
		case LVL_ERROR:
			return "ERROR";
		case LVL_FATAL:
			return "FATAL";
		case LVL_DEBUG:
			return "DEBUG";
		default:
			log(LVL_ERROR, "Encountered unknown error level.");
			return "UNKN";
		} 
	}

	public static String getTimestamp(boolean logger) {
		DateTimeFormatter formatter = null;
		if(logger)
			formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
		else
			formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());

		return formatter.format(Instant.now());
	}

	public static String getTimestamp() { return getTimestamp(false); }
}
