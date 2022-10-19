import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * This RequestManager class is used when we need to submit new request, edit existing request,
 * approve or reject a request, assign request, update request status
 * 
 * It connects with database and Main, SubmitRequest, EditRequest GUI
 * 
 * 
 */
public class RequestManager {
	private User user;
	private Connection requestManagerConn;

	/**
	 * Constructor for RequestManager with current user info
	 * @param user current user 
	 */
	public RequestManager(User user) {
		this.user = user;
		requestManagerConn = DatabaseDriver.getConnection();
	}

	public User getUser() {
		return user;
	}

	/**
	 * This method is to get all requests from database
	 * and return a RequestBoard object
	 * @return RequestBoard that contains info of all the requests in the database
	 */
	public RequestBoard getAllRequestBoard() {
		ArrayList<Request> allRequests = new ArrayList<>();
		String query = "SELECT * from request";
		try {
			Statement statement = requestManagerConn.createStatement();
			ResultSet rst = statement.executeQuery(query);
			while (rst.next()){
				Integer requestID = rst.getInt("request_id");
				String type = getField(rst.getInt("type_id"), "type", "analysis_type");
				String requester = getUserName(rst.getInt("requester_id"));
				String submitDate = rst.getString("submit_date");
				String acceptDate = rst.getString("accept_date");
				String dueDate = rst.getString("due_date");
				Integer tatRequest = rst.getInt("tat_request");
				String status = getField(rst.getInt("requeststatus_id"), "status", "request_status");
				String assignee = getUserName(rst.getInt("assignee_id"));
				String summary = rst.getString("summary");
				Integer tatFinal = rst.getInt("tat_Final");
				String log = rst.getString("log");
				allRequests.add(new Request(requestID, type, requester, submitDate, tatRequest,
						summary, status, assignee, acceptDate, dueDate, tatFinal, log));
			}
			return new RequestBoard(allRequests);
		} catch (SQLException e) {
			e.printStackTrace();
			return new RequestBoard(allRequests);
		}
	}

	/**
	 * This method is to get the total count of all requests
	 * @return the count of all the requests in the database
	 */
	public int getAllRequestCount() {
		String query = "SELECT Count(*) from request";
		try {
			Statement statement = requestManagerConn.createStatement();
			ResultSet rst = statement.executeQuery(query);
			int count = 0;
			if (rst.next()) {
				count = rst.getInt(1);
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * This method is to get certain fields from the database 
	 * The field includes type, first_name, last_name, status that associate with the unique ID.
	 * This is also a helper method for getUserName method
	 * @param id the id in the table
	 * @param fieldName field name in the table
	 * @param tableName table name
	 * @return field the field in database corresponding the to the input info
	 */
	public String getField(int id, String fieldName, String tableName) {
		String query = "SELECT " + fieldName+ " FROM "+ tableName + " WHERE ID=" + id;
		String field = "";
		try{
			Statement statement = requestManagerConn.createStatement();
			ResultSet rst = statement.executeQuery(query);
			if (rst.next()) {
				field = rst.getString(1);
			}
			return field;
		} catch (SQLException e) {
			e.printStackTrace();
			return field;
		}
	}

	/**
	 * This method is to get full user name that includes both first_name and last_name
	 * @param userID user ID
	 * @return userName the full name of t the user
	 */
	public String getUserName(int userID) {
		String firstName = getField(userID, "first_name", "user");
		String lastName = getField(userID, "last_name", "user");
		String userName = firstName + " " + lastName;
		return userName.trim();
	}

	/**
	 * This method is to get a request's type and summary as request description
	 * The return string will be presented on the MainGUI
	 * @param request input request object
	 * @return a string combines the type and summary of the request
	 */
	public String getRequestDescription(Request request) {
		return "Request Type:\n" + request.getType() +
				"\n\nSummary:\n" + request.getSummary();
	}

	/**
	 * This method is to update comment logs for each request
	 * @param request the request object that needs to be updated
	 * @param log the updated log that will be inserted into database
	 */
	public void updateLog(Request request, String log) {
		int requestID = request.getRequestID();
		String query = "UPDATE request SET log= ? WHERE request_id= ?";
		try {
			PreparedStatement ps = requestManagerConn.prepareStatement(query);
			ps.setString(1, log);
			ps.setInt(2, requestID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is to create a new request and insert it into the database.
	 * @param requestID request ID
	 * @param typeID different type of analysis for the request, predefined in database
	 * @param requesterID requester ID
	 * @param submitDate request submit date in format "yyyy-mm-dd"
	 * @param tatRequest turn-around-time request input by requester
	 * @param summary request summary input by requester
	 * @param requestStatusID status ID of the request, predefined in database
	 * @param acceptDate request accepted date in format "yyyy-mm-dd"
	 * @param dueDate request due date in format "yyyy-mm-dd"
	 * @param tatFinal the final turn-around-time of the request, calculate after request close
	 * @param assigneeID assignee ID
	 * @param log the log of the request
	 */
	public void insertRequest(int requestID, int typeID, int requesterID, String submitDate, int tatRequest, String summary,
			int requestStatusID, String acceptDate, String dueDate, int tatFinal, int assigneeID, String log) {
		String query = "INSERT INTO request VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = requestManagerConn.prepareStatement(query);
			ps.setInt(1, requestID);
			ps.setInt(2, typeID);
			ps.setInt(3, requesterID);
			ps.setString(4, submitDate);
			ps.setInt(5, tatRequest);
			ps.setString(6, summary);
			ps.setInt(7, requestStatusID);
			ps.setString(8, acceptDate);
			ps.setString(9, dueDate);
			ps.setInt(10, tatFinal);
			ps.setInt(11, assigneeID);
			ps.setString(12, log);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is to update an existing request in the database.
	 * @param requestID request ID
	 * @param typeID different type of analysis for the request, predefined in database
	 * @param tatRequest turn-around-time request input by requester
	 * @param summary request summary input by requester
	 * @param requestStatusID status ID of the request, predefined in database
	 * @param acceptDate request accepted date in format "yyyy-mm-dd"
	 * @param dueDate request due date in format "yyyy-mm-dd"
	 * @param tatFinal the final turn-around-time of the request, calculate after request close
	 * @param assigneeID assignee ID
	 * @param log the log of the request
	 */
	public void updateRequest(int requestID, int typeID, int tatRequest, String summary,
			int requestStatusID, String acceptDate, String dueDate, int tatFinal, int assigneeID, String log) {
		String query = "UPDATE request " +
				"SET type_id=?, tat_request=?, summary=?, requestStatus_id=?, accept_date=?, due_date=?, tat_final=?, assignee_id=?, log=? " +
				"WHERE request_id=?";
		try {
			PreparedStatement ps = requestManagerConn.prepareStatement(query);

			ps.setInt(1, typeID);;
			ps.setInt(2, tatRequest);
			ps.setString(3, summary);
			ps.setInt(4, requestStatusID);
			ps.setString(5, acceptDate);
			ps.setString(6, dueDate);;
			ps.setInt(7, tatFinal);
			ps.setInt(8, assigneeID);
			ps.setString(9, log);
			ps.setInt(10, requestID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is to get list of data input from certain fields, for example, the analysis type field.
	 * @param getAll get all the entrys in the table or not
	 * @param tableName table name
	 * @param columnName column name
	 * @param columnNumber how many columns of data will be returned
	 * @param condition condition used in SQL query
	 * @return
	 */
	public String[] getComboBoxFields(Boolean getAll, String tableName, String columnName, int columnNumber, String condition) {
		ArrayList<String> fieldList = new ArrayList<>();
		String query = "SELECT " + columnName + " FROM " + tableName;
		if (!getAll) {
			query = query + " WHERE " + condition + " ORDER BY id";
		}
		try {
			Statement statement = requestManagerConn.createStatement();
			ResultSet rst = statement.executeQuery(query);
			while (rst.next()) {
				String field = "";
				for (int i = 1; i <= columnNumber; i++) {
					field = field + rst.getString(i) + " ";
				}
				fieldList.add(field.trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] fields = new String[fieldList.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fieldList.get(i);
		}
		return fields;
	}

	/**
	 * This helper method is a filter that returns the list of requests that submitted in the last N days
	 * @param allRequests: an ArrayList of request that on the request board
	 * @param day the days from now
	 * @return an ArrayList of all the requests that matches the input request
	 */
	public ArrayList<Request> getRequestFromLastNDays(ArrayList<Request> allRequests, int day) {
		ArrayList<Request> result = new ArrayList<>();
		for (Request request: allRequests) {
			LocalDate submitDate = LocalDate.parse(request.getSubmitDate());
			if (((int) ChronoUnit.DAYS.between(submitDate, LocalDate.now())) <= day) {
				result.add(request);
			}
		}
		return result;
	}

	/**
	 * This helper method returns a comparator for sorting with different fields
	 * @param field: a field in the request, that the sorting will be based on.
	 * @return a comparator that will be used in the Collection.sort() method
	 */
	public Comparator<Request> getComparator(String field) {
		Comparator<Request> comparator = new Comparator<Request>() {
			public int compare(Request a, Request b) {
				if (field.equals("requestID")) {
					return a.getRequestID() - b.getRequestID();
				} else if (field.equals("type")) {
					return a.getType().compareTo(b.getType());
				} else if (field.equals("submitDate")) {
					return a.getSubmitDate().compareTo(b.getSubmitDate());
				} else if (field.equals("status")){
					return a.getStatus().compareTo(b.getStatus());
				} else if (field.equals("assignee")){
					return a.getAssignee().compareTo(b.getAssignee());
				}
				return 0;
			}
		};
		return comparator;
	}
	
	/**
	 * For debug use only
	 * Print all the elements in a string 2D array on the console
	 * @param sa a string 2D array
	 */
	public static void printStringArray(String[][] sa) {
		int row = sa.length;
		int column = sa[0].length;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				System.out.print(sa[i][j] + ",");
			}
			System.out.println();
		}
	}

	/**
	 * For debug use only
	 * Print all the elements in a string array on the console
	 * @param sa a string array
	 */
	public static void printStringArray(String[] sa) {
		int row = sa.length;
		for (int i = 0; i < row; i++) {
			System.out.print(sa[i] + ",");
		}
	}
}
