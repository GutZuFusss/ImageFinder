package gutzufusss.wrapper;

import java.io.File;
import java.io.IOException;
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
	protected static Logger logger;

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
	
	public static void checkDB() {
		File db = new File(DB_PATH);

		try {
			if(!db.exists()) {
				if(!db.getParentFile().exists()) // also check if we have to create the db directory
					db.getParentFile().mkdir();
				if(db.createNewFile()) {
					logger.log(Logger.LVL_INFO, "Database has been created. Attempting to create tables...");
				} else {
					logger.log(Logger.LVL_FATAL, "Could not open nor create database!!! Shutting down.");
				}
			}
		} catch(IOException e) {
			logger.log(Logger.LVL_FATAL, "I/O error occured while creating database: " + e.getMessage());
		}
	}
	
	public abstract void tableCheck() throws SQLException;

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
	
	public static void setLogger(Logger l) { logger = l; }

	protected final Connection createConnection() throws ClassNotFoundException, SQLException {
		logger.log(Logger.LVL_DEBUG, "Creating database connection to: " + "jdbc:sqlite:" + DB_PATH);
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
	}

	protected final Statement createStatement(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(QUERY_TIMEOUT);

		logger.log(Logger.LVL_DEBUG, "Created statement, query timeout: " + statement.getQueryTimeout());

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
