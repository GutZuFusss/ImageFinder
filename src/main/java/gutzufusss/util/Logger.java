package gutzufusss.util;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

public class Logger {
	private final String LOG_PATH = "logs/" + getTimestamp(true) + ".log";

	public static final int LVL_INFO = 1;
	public static final int LVL_WARN = 2;
	public static final int LVL_ERROR = 4;
	public static final int LVL_FATAL = 8;
	public static final int LVL_DEBUG = 16;

	private File logFile;

	public Logger() {
		logFile = new File(LOG_PATH);
		if(!logFile.getParentFile().exists())
			logFile.getParentFile().mkdir();

		// now it is safe to create the current's session logfile
		try {
			if(!logFile.exists()) {
				if(logFile.createNewFile()) {
					log(LVL_INFO, "Log file has been created successfully.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace(); // kinda nonsense to log something here...
		}
		
		System.out.println(logFile.getAbsolutePath());
	}

	public void log(int lvl, String msg, boolean printCallerMethod) {
		int stackIndex = printCallerMethod ? 1 : 2; // if printCallerMethod == false we are likely coming from the overloaded function
		String calledFrom = Thread.currentThread().getStackTrace()[stackIndex].getClassName();
		if(printCallerMethod)
			calledFrom += "::" + Thread.currentThread().getStackTrace()[1].getMethodName();
		
		String logMsg = "[" + getTimestamp() + "]:" + "[" + calledFrom + "]>> " + msg;

		try {
			FileUtils.writeStringToFile(logFile, logMsg, "UTF-8");
		} catch (IOException e) {
			log(LVL_ERROR, "We seem to have some kind of log-ception here.", true);
		}
		// TODO: also print to gui once there is one
	}
	
	public void log(int lvl, String msg) { log(lvl, msg, false); }

	private String getTimestamp(boolean logger) {
		DateTimeFormatter formatter = null;
		if(logger)
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
		else
			formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());

		Instant now = Instant.now();
		return formatter.format(now);
		/*if(logger)
			return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
		else
			return LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));*/
	}

	private String getTimestamp() { return getTimestamp(false); }
}
