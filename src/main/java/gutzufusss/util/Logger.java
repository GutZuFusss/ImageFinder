package gutzufusss.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Logger {
	private final String LOGS_PATH = "logs/{TS}.log".replaceFirst("{TS}", getTimestamp(true));

	public static final int LVL_INFO = 1;
	public static final int LVL_WARN = 2;
	public static final int LVL_ERROR = 4;
	public static final int LVL_FATAL = 8;
	public static final int LVL_DEBUG = 16;

	public Logger() {
		File log = new File(LOGS_PATH);
		try {
			if(!log.getParentFile().exists())
				log.getParentFile().createNewFile();
		} catch(IOException e) {
			// TODO Auto-generated catch block
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
