import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DatabaseDriver class to connect to database hosted in the cloud and read data to the java program
 * 
 *
 */
public class DatabaseDriver {

	/**
	 * This method is to get the connection to MySQL database
	 * @return a connection to the database
	 */
	public static Connection getConnection(){
		Connection dbConnect = null;
		try {
			dbConnect = DriverManager.getConnection("jdbc:mysql://sql3.freemysqlhosting.net:3306/sql3330869",
					"sql3330869","yNa9dJitmI");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return dbConnect;
	}
}
