package gutzufusss;

import java.sql.SQLException;

import gutzufusss.wrapper.SQLWrapper;

public class SettingsDBController extends SQLWrapper {

	public SettingsDBController() { }

	@Override
	public boolean startUpCheck() throws SQLException {
		// TODO: diz
		return false;
	}

}
