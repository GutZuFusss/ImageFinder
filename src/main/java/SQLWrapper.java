import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLWrapper {
	private static final String DB_PATH = "db/img_finder_data.db";
	private static final int QUERY_TIMEOUT = 30; 
	private static final String TABLE_IMG = "image_data";

	private static Connection connection = null;
	private static Statement statement = null;

	// constructor is private to prevent instantiation because all methods in this class will be static anyways
	private SQLWrapper() { }

	public static ResultSet execQuerry(String query) {
		createConAndStateIfNeeded();
		ResultSet resultOfQuery = null;
		try {
			resultOfQuery = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace(); // TODO: log as always...
		}

		return resultOfQuery;
	}

	public static void execSQL(String sql) {
		createConAndStateIfNeeded();
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace(); // TODO: another one
		}
	}

	// START_MISC_FUNCTIONS
	public static boolean startUpCheck() throws SQLException {
		File db = new File(DB_PATH);

		try {
			if(!db.exists()) {
				if(!db.getParentFile().exists()) // also check if we have to create the db directory
					db.getParentFile().mkdir();
				if(db.createNewFile()) {
					// TODO: log successful db creation
				} else {
					return false; // TODO: important log! fatal error: database did not exist and could not be created
				}
			}
		} catch(IOException e) {
			e.printStackTrace(); // TODO: log hog rog
		}

		// okay, we are now sure our database exists, lets check for the tables
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_IMG + "';").next())
			return true;

		execSQL("CREATE TABLE " + TABLE_IMG + " " +
		        "(id 			INTEGER PRIMARY KEY AUTOINCREMENT," +	// pkey
		        " name			VARCHAR(256)," +						// max filename length is 255 (on win at least, haaaaah)
		        " abs_path		VARCHAR(1024)," +						// absolute path to the file
		        " ocr_data		VARCHAR(4096)," +						// text that was found in the image (TODO: limit ocr to 4096 so we don't overflow)
		        " confidence	INTEGER)");								// how sure the ocr was about the result

		return true;
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
	}

	private static Statement createStatement(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(QUERY_TIMEOUT);

		return statement;
	}

	private static void createConAndStateIfNeeded() {
		// check wether our connection & statement are active already
		if(isConnectionOpened()) {
			try {
				connection = createConnection();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace(); // TODO: log as always...
			}
		}
		if(isStatementOpened()) {	
			try {
				statement = createStatement(connection);
			} catch (SQLException e) {
				e.printStackTrace(); // TODO: log as always...
			}
		}
	}

	private static boolean isConnectionOpened() { return connection == null; }

	private static boolean isStatementOpened() { return statement == null; }
	// END_MISC_FUNCTIONS
}
