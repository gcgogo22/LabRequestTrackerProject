import java.util.ArrayList;

/**
 * This RequestBoard class is used to read necessary data from the database
 * create results for GUI to display
 * 
 *
 */
public class RequestBoard {
	//These are the columns that will be shown in the MainGUI table
	public static final String[] COLOMNS = {"ID", "Type", "Requester",
			"Submit_Date", "Accept_Date", "Due_Date", "TAT_Request", "TAT_Final",
			"Status", "Assignee", "Summary"}; 
		
	private ArrayList<Request> requests;
	private String[][] data;
	
	/**
	 * Constructor for RequestBoard object
	 * @param requests an ArrayList of request objects
	 */
	public RequestBoard(ArrayList<Request> requests) {
		this.requests = requests;
		data = updateTableData(requests);
	}
	
	/**
	 * This method returns a 2D array that represents all the data of the input requests
	 * @param requests an ArrayList of all the requests
	 * @return a 2D string array represents all the fields of the input requests
	 */
	public String[][] updateTableData(ArrayList<Request> requests) {
		String[][] data = new String[requests.size()][COLOMNS.length];
		int row = 0;
		for (Request request : requests) {
			data[row] = request.toRowDataOnRequestBoard();
			row++;
		}
		return data;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public ArrayList<Request> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
}
