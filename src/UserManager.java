import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The UserManager class is used when we need to get user info, create new user, or update existing user info
 * It creates a connection from DatabaseDriver, and connects to LoginGUI
 * 
 *
 */
public class UserManager {
	private Connection userManagerConn;
	
	/**
	 * Constructor for UserManager
	 */
	public UserManager() {
		userManagerConn = DatabaseDriver.getConnection();
	}

	/**
	 * This method is for login use
	 * Check if the user is an active user to login into the system by verifying the email and password
	 * @param email the input user email
	 * @param password the input user password
	 * @return true/false - if the input matches data in the database or not
	 */
	public boolean checkUser(String email, String password) {
		String query = "SELECT * FROM user WHERE email = ? AND password = ? ";
		try {
			PreparedStatement ps = userManagerConn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rst = ps.executeQuery();
			int count = 0;
			while (rst.next()) {
				count++;
			}
			return count == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method is for registration use, to check if the input email address has been used or not
	 * If there is already an entry with the email provided in the database, then return true
	 * @param email the input user email
	 * @return true/false - if the input matches data in the database or not
	 */
	public boolean checkUser(String email) {
		String query = "SELECT * FROM user WHERE email = ?";
		try {
			PreparedStatement ps = userManagerConn.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rst = ps.executeQuery();
			int count = 0;
			boolean isActive = false;
			while (rst.next()) {
				count++;
				isActive = rst.getInt("active") == 1;
			}
			return count == 1 && isActive;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * This method will create a new user and insert this user's info into database
	 * All the information will be pull from user input
	 * @param firstName first name of the user
	 * @param lastName last name of the user
	 * @param email email address of the user
	 * @param roleID if user register, the role will be set as 1-requester
	 * @param department department of the user
	 * @param active when user register, it will be set as 1-active
	 * @param password user password
	 */
	public void createUser(String firstName, String lastName, String email,
			int roleID, String department, int active, String password) {
		String query = "INSERT INTO user (first_name, last_name, email, role_id, department, active, password) VALUES(?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = userManagerConn.prepareStatement(query);
			//ps.setInt(1, userID);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, email);
			ps.setInt(4, roleID);
			ps.setString(5, department);
			ps.setInt(6, active);
			ps.setString(7, password);
			System.out.println(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns a user object that associates with the input email.
	 * It will only be called after the method checkUser.
	 * @param email user email address
	 * @return user a user object that associate with the input email
	 */
	public User getCurrentUser(String email) {
		String query = "SELECT * from user WHERE email='" + email + "'";
		try {
			Statement statement = userManagerConn.createStatement();
			ResultSet rst = statement.executeQuery(query);
			rst.next();
			int id = rst.getInt("id");
			String firstName = rst.getString("first_name");
			String lastName = rst.getString("last_name");
			int roleID = rst.getInt("role_id");
			String department = rst.getString("department");
			boolean active = rst.getInt("active") == 1;
			String password = rst.getString("password");
			return new User(id, firstName, lastName, email, roleID, department, active, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
