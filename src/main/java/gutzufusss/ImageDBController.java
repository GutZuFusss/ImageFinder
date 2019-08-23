package gutzufusss;

import java.sql.SQLException;

import gutzufusss.wrapper.SQLWrapper;

public class ImageDBController extends SQLWrapper {

	public ImageDBController() { }

	@Override
	public boolean startUpCheck() throws SQLException {
		return false;
	}

}
