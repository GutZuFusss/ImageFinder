package gutzufusss.wrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gutzufusss.util.Logger;

public abstract class SQLWrapper {
	protected 	static final String 	DB_PATH 			= "db/img_finder_data.db";
	protected 	static final int 		QUERY_TIMEOUT 		= 30;

	protected Connection connection = null;
	protected Statement statement = null;
	protected Logger logger;

	public final ResultSet execQuerry(String query) {
		createConAndStateIfNeeded();
		ResultSet resultOfQuery = null;
		try {
			resultOfQuery = statement.executeQuery(query);
			logger.log(Logger.LVL_DEBUG, "Executed querry: " + query);
		} catch(SQLException e) {
			logger.log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}
		
		try {
			logger.log(Logger.LVL_DEBUG, "Results: " + resultOfQuery.getFetchSize());
		} catch (SQLException e) {
			logger.log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}

		return resultOfQuery;
	}

	public final void execSQL(String sql) {
		createConAndStateIfNeeded();
		try {
			statement.executeUpdate(sql);
			logger.log(Logger.LVL_DEBUG, "Executed sql: " + sql);
		} catch(SQLException e) {
			logger.log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}
	}

	public abstract boolean startUpCheck() throws SQLException; /*{
		File db = new File(DB_PATH);

		try {
			if(!db.exists()) {
				if(!db.getParentFile().exists()) // also check if we have to create the db directory
					db.getParentFile().mkdir();
				if(db.createNewFile()) {
					logger.log(Logger.LVL_INFO, "Database has been created. Attempting to create tables...");
				} else {
					logger.log(Logger.LVL_FATAL, "Could not open nor create database!!! Shutting down.");
					return false;
				}
			}
		} catch(IOException e) {
			logger.log(Logger.LVL_FATAL, "I/O error occured while creating database: " + e.getMessage());
			return false;
		}

		// okay, we are now sure our database exists, lets check for the tables
		if(execQuerry("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_IMG + "';").next()) {
			logger.log(Logger.LVL_DEBUG, "Found SQL table " + TABLE_IMG);
			return true;
		}

		execSQL("CREATE TABLE " + TABLE_IMG + " " +
		        "(id 			INTEGER PRIMARY KEY AUTOINCREMENT," +				// pkey
		        " name			VARCHAR(256)," +									// max filename length is 255 (on win at least, haaaaah)
		        " abs_path		VARCHAR(1024)," +									// absolute path to the file
		        " ocr_data		VARCHAR(" + MAX_IMG_TEXT_LEN + ")," +	// text that was found in the image
		        " confidence	INTEGER)");											// how sure the ocr was about the result
		
		logger.log(Logger.LVL_INFO, "SQL table generation was successful.");

		return true;
	}*/

	// START_MISC_FUNCTIONS
	public final void closeDB() {
		try {
			statement.close();
			connection.close();
		} catch(SQLException e) {
			logger.log(Logger.LVL_ERROR, "SQL-Error: " + e.getErrorCode() + " - " + e.getMessage());
		}

		logger.log(Logger.LVL_DEBUG, "Database shutdown.");

		statement = null;
		connection = null;
	}

	protected final Connection createConnection() throws ClassNotFoundException, SQLException {
		logger.log(Logger.LVL_DEBUG, "Creating database connection to: " + "jdbc:sqlite:" + DB_PATH);
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
	}

	protected final Statement createStatement(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(QUERY_TIMEOUT);

		//logger.log(Logger.LVL_DEBUG, "Created statement,  warnings: " + statement.getWarnings().getMessage());

		return statement;
	}

	protected final void createConAndStateIfNeeded() {
		// check wether our connection & statement are active already
		if(isConnectionOpened()) {
			try {
				logger.log(Logger.LVL_DEBUG, "Trying to establish connection.");
				connection = createConnection();
			} catch(ClassNotFoundException | SQLException e) {
				if(e instanceof ClassNotFoundException)
					logger.log(Logger.LVL_ERROR, "Error while establishing a SQL conntection: " + e.getMessage());
				else if(e instanceof SQLException)
					logger.log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
			}
		}
		if(isStatementOpened()) {	
			try {
				logger.log(Logger.LVL_DEBUG, "Trying to create statement.");
				statement = createStatement(connection);
			} catch(SQLException e) {
				logger.log(Logger.LVL_ERROR, "SQL-Error: " + ((SQLException)e).getErrorCode() + " - " + e.getMessage());
			}
		}
	}

	protected final boolean isConnectionOpened() { return connection == null; }

	protected final boolean isStatementOpened() { return statement == null; }
	// END_MISC_FUNCTIONS
}
