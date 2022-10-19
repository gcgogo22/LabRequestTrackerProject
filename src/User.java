/**
 * User class represents each user that has access to the system
 * 
 */
public class User {

	private int userID;
	private String firstName;
	private String lastName;
	private String email;
	private int roleID;
	private String department;
	private boolean active;
	private String password;

	/**
	 * Constructor for User object
	 * @param userID user ID
	 * @param firstName user first name
	 * @param lastName user last name
	 * @param email user email address
	 * @param roleID 1-Requester, 2-Manager, 3-Analyst
	 * @param department user department
	 * @param active 0-inactive, 1-active 
	 * @param password user password
	 */
	public User(int userID, String firstName, String lastName, String email,
			int roleID, String department, boolean active, String password) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roleID = roleID;
		this.department = department;
		this.active = active;
		this.password = password;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getUserID() {
		return userID;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getRoleID() {
		return roleID;
	}


	public void setRole(int roleID) {
		this.roleID = roleID;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * This method returns full name of the user
	 * @return first name + last name
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	//only for debug use
	public String toString() {
		return userID + firstName + lastName + email + roleID + department + active;
	}
}
