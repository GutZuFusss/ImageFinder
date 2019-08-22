package gutzufusss.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Logger {
	private final String LOG_PATH = "logs/{TS}.log".replaceFirst("{TS}", getTimestamp(true));

	public static final int LVL_INFO = 1;
	public static final int LVL_WARN = 2;
	public static final int LVL_ERROR = 4;
	public static final int LVL_FATAL = 8;
	public static final int LVL_DEBUG = 16;

	private File logFile;

	public Logger() {
		logFile = new File(LOG_PATH);
		try {
			if(!logFile.getParentFile().exists())
				logFile.getParentFile().createNewFile();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// now it is safe to create the current's session logfile
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(int lvl, String msg, boolean printCallerMethod) {
		String calledFrom = Thread.currentThread().getStackTrace()[1].getClassName()
				+ (printCallerMethod ? "::" + Thread.currentThread().getStackTrace()[1].getMethodName() : "");

	}

	private String getTimestamp(boolean logger) {
		if(logger)
			return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
		else
			return LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	private String getTimestamp() { return getTimestamp(false); }
}
