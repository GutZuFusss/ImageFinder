import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLWrapper {
	private static final String DB_PATH = "db/img_finder_data.db";
	private static final int QUERY_TIMEOUT = 30; 
	
	private static Connection connection = null;
	private static Statement statement = null;

	// constructor is private to prevent instantiation because all methods in this class will be static anyways
	private SQLWrapper() { }
	
	public static void execSQL(String sql) throws ClassNotFoundException, SQLException {
		// check wether our connections are active already
		if(isConnectionOpened()) {
			connection = createConnection();
		}
		if(isStatementOpened() )  {	
			statement = createStatement(connection);
		}
		
		statement.executeUpdate(sql);
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
	
	private static boolean isConnectionOpened() { return connection == null; }

	private static boolean isStatementOpened() { return statement == null; }
	// END_MISC_FUNCTIONS
}
