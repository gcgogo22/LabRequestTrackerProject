
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * this class stores the remaining 8 unit tests
 * We decided to have two separate unit test files because the database host we are currently using is a free one
 * and only allows a certain number of connection calls, separating the unit tests would reduce the number of connection calls
 * 
 *
 */
public class UnitTestPartTwo {
	User user = new User(20009, "TestAccount", "test", "test",
			1, "test", true, "test");
	RequestManager rm = new RequestManager(user);
	UserManager um = new UserManager();
	Request request1 = new Request(1, "Simulation", "Alex", "2020-04-16", 20, "This request was submitted recently",
			"Approved", "Jason", "2020-04-17", "2020-06-10", 16, "This is a test log");
	Request request2 = new Request(2, "Simulation", "Jason", "2019-07-15", 30, "This request was submitted last year",
			"Approved", "Alex", "2019-07-16", "2019-08-10", 26, "This is another test log");

	/**
	 * test checkUser(String email, String password) in the UserManager class
	 */
	@Test
	public void testCheckUser1() {
		//boolean checkUser(String email, String password)
		assertTrue(um.checkUser("test", "test"));
		assertFalse(um.checkUser("random", "wrongpw"));
	}

	/**
	 * test checkUser(String email) in the UserManager class
	 */
	@Test
	public void testCheckUser2() {
		//boolean checkUser(String email);
		assertTrue(um.checkUser("test"));
		assertFalse(um.checkUser("random"));
	}

	/**
	 * test getCurrentUser method in the UserManager class
	 */
	@Test
	public void testGetCurrentUser() {
		User testUser = um.getCurrentUser("test");
		int userID = testUser.getUserID();
		String firstName = testUser.getFirstName();
		String lastName = testUser.getLastName();
		String email = testUser.getEmail();

		User expectedUser = user;
		int expectedUserID = user.getUserID();
		String expectedFirstName = user.getFirstName();
		String expectedLastName = user.getLastName();
		String expectedEmail = user.getEmail();

		assertEquals(expectedUserID, userID);
		assertEquals(expectedFirstName, firstName);
		assertEquals(expectedLastName, lastName);
		assertEquals(expectedEmail, email);
	}


	/**
	 * test getRequestFromLastDays method in the RequestManager class
	 * should only return request1 as it is submitted less than 60 days ago
	 */
	@Test
	public void testGetRequestFromLastDays1() {
		ArrayList<Request> requests = new ArrayList<>();
		requests.add(request1);
		requests.add(request2);
		ArrayList<Request> result = rm.getRequestFromLastNDays(requests, 60);
		ArrayList<Request> expectedResult = new ArrayList<>();
		expectedResult.add(request1);
		assertArrayEquals(expectedResult.toArray(), result.toArray());
	}

	/**
	 * test getRequestFromLastDays method in the RequestManager class
	 * should return both request1 and request2 as they are both submitted less than 500 days ago
	 */
	@Test
	public void testGetRequestFromLastDays2() {
		ArrayList<Request> requests = new ArrayList<>();
		requests.add(request1);
		requests.add(request2);
		ArrayList<Request> result = rm.getRequestFromLastNDays(requests, 500);
		ArrayList<Request> expectedResult = new ArrayList<>();
		expectedResult.add(request1);
		expectedResult.add(request2);
		assertArrayEquals(expectedResult.toArray(), result.toArray());
	}


	/**
	 * test getComparator in RequestManager class by comparing requestID
	 * returns -1 when comparing by requestID because request1's requestID is 1 and request2's requestID is 2
	 */
	@Test
	public void testGetComparator1() {
		int result = rm.getComparator("requestID").compare(request1, request2);
		int expectedResult = -1;
		assertEquals(expectedResult, result);
	}


	/**
	 * test getComparator in RequestManager class by comparing type
	 * returns 0 when comparing by type because request1's type is the same with request2's type
	 */
	@Test
	public void testGetComparator2() {
		int result = rm.getComparator("type").compare(request1, request2);
		int expectedResult = 0;
		assertEquals(expectedResult, result);
	}

	/**
	 * test getComparator in RequestManager class by comparing submitDate
	 * returns 1 when comparing by submitDate because request1's submitDate is more recent than request2's submitDate
	 */
	@Test
	public void testGetComparator3() {
		int result = rm.getComparator("submitDate").compare(request1, request2);
		int expectedResult = 1;
		assertEquals(expectedResult, result);
	}


}
