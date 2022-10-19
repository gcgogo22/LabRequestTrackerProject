
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import java.sql.*;

/**
 * this class stores the first 8 unit tests
 * We decided to have two separate unit test files because the database host we are currently using is a free one
 * and only allows a certain number of connection calls, separating the unit tests would reduce the number of connection calls
 * 
 *
 */
public class UnitTestPartOne {
	User user = new User(20009, "TestAccount", "test", "test",
			1, "test", true, "test");
	RequestManager rm = new RequestManager(user);
	UserManager um = new UserManager();
	Request request1 = new Request(1, "Simulation", "Alex", "2020-04-01", 10, "This is a summary for testing",
			"Approved", "Jason", "2020-04-02", "2020-04-10", 6, "This is a log");

	/**
	 * test updateTableData method in RequestBoard class
	 */
	@Test
	public void testUpdateTableData() {
		ArrayList<Request> requests = new ArrayList<Request>();
		requests.add(request1);
		RequestBoard board = new RequestBoard(requests);
		String[][] result = board.updateTableData(requests);
		String[][] expectedResult = new String[][] {{"1", "Simulation", "Alex", "2020-04-01", "2020-04-02", "2020-04-10", "10", "6",
			"Approved", "Jason",  "This is a summary for testing"}};

			Assert.assertArrayEquals(expectedResult, result);

	}

	/**
	 * test toRowDataOnRequestBoard method in Request class
	 */
	@Test
	public void testToRowDataOnRequestBoard() {
		String[] data = request1.toRowDataOnRequestBoard();

		String[] expectedData = new String[] {"1", "Simulation", "Alex", "2020-04-01", "2020-04-02", "2020-04-10", "10", "6",
				"Approved", "Jason",  "This is a summary for testing"};

		Assert.assertArrayEquals(expectedData, data);
	}


	/**
	 * test getRequestCount method in RequestManager class
	 * Get the largest request_ID from database, compare to the result of getResultCount, the two should be equal.
	 */
	@Test
	public void testGetRequestCount() {
		int result = rm.getAllRequestCount();
		int expectedResult = 0;

		try {
			java.sql.Connection dbCon = DatabaseDriver.getConnection();
			Statement statement = dbCon.createStatement();
			ResultSet rst = statement.executeQuery("select max(request_ID) from request");
			int i = 0;
			if (rst.next()) {
				i = rst.getInt(1);
			}
			expectedResult = i;
			dbCon.close();
			statement.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//		int expectedResult = 16; //this number should be updated according to the number of requests in the database.
		assertEquals(expectedResult, result);
	}

	/**
	 * test getField method in RequestManager class to get type field from database
	 */
	@Test
	public void testGetTypeField() {
		String type1 = rm.getField(1, "type", "analysis_type");
		String type2 = rm.getField(2, "type", "analysis_type");
		String expectedResult1 = "Simulation";
		String expectedResult2 = "Fault Isolation";
		assertEquals(expectedResult1, type1);
		assertEquals(expectedResult2, type2);
	}

	/**
	 * test getField method in RequestManager class to get status field from database
	 */
	@Test
	public void testGetStatusField() {
		String status1 = rm.getField(1, "status", "request_status");
		String status2 = rm.getField(2, "status", "request_status");
		String expectedResult1 = "Submitted";
		String expectedResult2 = "Under_Review";
		assertEquals(expectedResult1, status1);
		assertEquals(expectedResult2, status2);

	}

	/**
	 * test getUserName method in RequestManager class that combines two field inputs from database and return one output
	 */
	@Test
	public void testGetFullName() {
		String name1 = rm.getUserName(1);
		String name2 = rm.getUserName(1000);
		String expectedResult1 = "Jason Zheng";
		String expectedResult2 = "Alex Xia";
		assertEquals(expectedResult1, name1);
		assertEquals(expectedResult2, name2);
	}


	/**
	 * test getRequestDescription method in RequestManager class
	 */
	@Test
	public void testGetRequestDescription() {
		String result = rm.getRequestDescription(request1);
		String expectedResult = "Request Type:\n" + "Simulation" + "\n\nSummary:\n" + "This is a summary for testing";
		assertEquals(expectedResult, result);
	}



	/**
	 * test getComboBoxFields method in RequestManager class
	 */
	@Test
	public void testGetComboBoxFields() {
		String[] result = rm.getComboBoxFields(false, "user", "first_name, last_name", 2, "role_id=3");
		String[] expectedResult = new String[] {"Unassigned","Jason Zheng","Elon Musk","Stefanie Cai","Hunter Zheng"};
		Assert.assertArrayEquals(expectedResult, result);
	}
}
