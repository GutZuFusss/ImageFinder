package gutzufusss.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GlobalUtil {

	private GlobalUtil() { } // no object of diz class plz
	
	private String getTimestamp(boolean logger) {
		DateTimeFormatter formatter = null;
		if(logger)
			formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH-mm-ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
		else
			formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());

		return formatter.format(Instant.now());
	}

	private String getTimestamp() { return getTimestamp(false); }
}
