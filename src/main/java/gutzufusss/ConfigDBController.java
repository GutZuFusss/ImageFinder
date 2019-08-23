package gutzufusss;

import java.sql.SQLException;

import gutzufusss.util.Logger;
import gutzufusss.wrapper.SQLWrapper;

public class ConfigDBController extends SQLWrapper {

	public ConfigDBController(Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean startUpCheck() throws SQLException {
		// TODO: diz
		return false;
	}

}
