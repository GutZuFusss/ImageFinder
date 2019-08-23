package gutzufusss.wrapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gutzufusss.Main;
import gutzufusss.util.GlobalUtil;
import gutzufusss.util.Logger;

public class SQLWrapper {
	private static final String DB_PATH = "db/img_finder_data.db";
	private static final int QUERY_TIMEOUT = 30;
	public static final String TABLE_IMG = "image_data";
	public static final int MAX_IMG_TEXT_LEN = 4096;

	private static Main controller;

	private static Connection connection = null;
	private static Statement statement = null;

	// constructor is private to prevent instantiation because all methods in this class will be static anyways
	private SQLWrapper() { }

	public static ResultSet execQuerry(String query) {
		createConAndStateIfNeeded();
		ResultSet resultOfQuery = null;
		try {
			resultOfQuery = statement.executeQuery(query);
			controller.getLogger().log(Logger.LVL_DEBUG, "Executed querry: " + query);
		} catch(SQLException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}
		
		try {
			controller.getLogger().log(Logger.LVL_DEBUG, "Results: " + resultOfQuery.getFetchSize());
		} catch (SQLException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}

		return resultOfQuery;
	}

	public static void execSQL(String sql) {
		createConAndStateIfNeeded();
		try {
			statement.executeUpdate(sql);
			controller.getLogger().log(Logger.LVL_DEBUG, "Executed sql: " + sql);
		} catch(SQLException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}
	}

	public static boolean startUpCheck() throws SQLException {
		File db = new File(DB_PATH);

		try {
			if(!db.exists()) {
				if(!db.getParentFile().exists()) // also check if we have to create the db directory
					db.getParentFile().mkdir();
				if(db.createNewFile()) {
					controller.getLogger().log(Logger.LVL_INFO, "Database has been created. Attempting to create tables...");
				} else {
					controller.getLogger().log(Logger.LVL_FATAL, "Could not open nor create database!!! Shutting down.");
					return false;
				}
			}
		} catch(IOException e) {
			controller.getLogger().log(Logger.LVL_FATAL, "I/O error occured while creating database: " + e.getMessage());
			return false;
		}

		// okay, we are now sure our database exists, lets check for the tables
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_IMG + "';").next()) {
			controller.getLogger().log(Logger.LVL_DEBUG, "Found SQL table " + TABLE_IMG);
			return true;
		}

		execSQL("CREATE TABLE " + TABLE_IMG + " " +
		        "(id 			INTEGER PRIMARY KEY AUTOINCREMENT," +				// pkey
		        " name			VARCHAR(256)," +									// max filename length is 255 (on win at least, haaaaah)
		        " abs_path		VARCHAR(1024)," +									// absolute path to the file
		        " ocr_data		VARCHAR(" + MAX_IMG_TEXT_LEN + ")," +	// text that was found in the image
		        " confidence	INTEGER)");											// how sure the ocr was about the result
		
		controller.getLogger().log(Logger.LVL_INFO, "SQL table generation was successful.");

		return true;
	}

	// START_MISC_FUNCTIONS
	public static void closeDB() {
		try {
			statement.close();
			connection.close();
		} catch(SQLException e) {
			controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}

		controller.getLogger().log(Logger.LVL_DEBUG, "Database shutdown.");

		statement = null;
		connection = null;
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		controller.getLogger().log(Logger.LVL_DEBUG, "Creating database connection to: " + "jdbc:sqlite:" + DB_PATH);
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
	}

	private static Statement createStatement(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(QUERY_TIMEOUT);

		controller.getLogger().log(Logger.LVL_DEBUG, "Created statement,  warnings: " + statement.getWarnings().getMessage());

		return statement;
	}

	private static void createConAndStateIfNeeded() {
		// check wether our connection & statement are active already
		if(isConnectionOpened()) {
			try {
				controller.getLogger().log(Logger.LVL_DEBUG, "Trying to establish connection.");
				connection = createConnection();
			} catch(ClassNotFoundException | SQLException e) {
				if(e instanceof ClassNotFoundException)
					controller.getLogger().log(Logger.LVL_ERROR, "Error while establishing a SQL conntection: " + e.getMessage());
				else if(e instanceof SQLException)
					controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
			}
		}
		if(isStatementOpened()) {	
			try {
				controller.getLogger().log(Logger.LVL_DEBUG, "Trying to create statement.");
				statement = createStatement(connection);
			} catch(SQLException e) {
				controller.getLogger().log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
			}
		}
	}

	private static boolean isConnectionOpened() { return connection == null; }

	private static boolean isStatementOpened() { return statement == null; }
	// END_MISC_FUNCTIONS
	
	public static final void setController(Main c) { controller = c; }
}
