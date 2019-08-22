import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLWrapper {
	private static final String DB_PATH = "db/img_finder_data.db";
	private static final int QUERY_TIMEOUT = 30; 

	private static Connection connection = null;
	private static Statement statement = null;

	// constructor is private to prevent instantiation because all methods in this class will be static anyways
	private SQLWrapper() { }

	public static ResultSet execSQL(String query) {
		createConAndStateIfNeeded();
		ResultSet resultOfQuery = null;
		try {
			resultOfQuery = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace(); // TODO: log as always...
		}

		return resultOfQuery;
	}

	// START_MISC_FUNCTIONS
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
