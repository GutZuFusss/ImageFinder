import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLWrapper {
	public static Connection connection = null;
	public static Statement statement = null;

	// constructor is private to prevent instantiation because all methods in this class will be static anyways
	private SQLWrapper() { }

	
	// START_MISC_FUNCTIONS
	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:db/img_finder_data.db");
	}
	// END_MISC_FUNCTIONS
}
