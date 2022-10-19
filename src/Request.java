/**
 * Request class represents all the information of the request in the database
 * 
 *
 */
public class Request {
	private int requestID;
	private String type;
	private String requester;
	private String submitDate;
	private int tatRequest;		//TAT: Turn-Around-Time
	private String summary;
	private String Status;
	private String assignee;
	private String acceptDate;
	private String dueDate;
	private int tatFinal;
	private String log;

	/**
	 * constructor for Request
	 * @param requestID request ID
	 * @param type different type of analysis for the request, predefined in database
	 * @param requester requester full name
	 * @param submitDate request submit date in format "yyyy-mm-dd"
	 * @param tatRequest turn-around-time request input by requester
	 * @param summary request summary input by requester
	 * @param status status of the request, predefined in database
	 * @param assignee assignee full name
	 * @param acceptDate request accepted date in format "yyyy-mm-dd"
	 * @param dueDate request due date in format "yyyy-mm-dd"
	 * @param tatFinal the final turn-around-time of the request, calculate after request close
	 * @param log the log of the request
	 */
	public Request(int requestID, String type, String requester, String submitDate, int tatRequest, String summary,
			String status, String assignee, String acceptDate, String dueDate, int tatFinal, String log) {
		this.requestID = requestID;
		this.type = type;
		this.requester = requester;
		this.submitDate = submitDate;
		this.tatRequest = tatRequest;
		this.summary = summary;
		this.Status = status;
		this.assignee = assignee;
		this.acceptDate = acceptDate;
		this.dueDate = dueDate;
		this.tatFinal = tatFinal;
		this.log = log;
	}


	public int getRequestID() {
		return requestID;
	}


	public String getSubmitDate() {
		return submitDate;
	}


	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}


	public String getAcceptDate() {
		return acceptDate;
	}


	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}


	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


	public String getLog() {
		return log;
	}


	public void setLog(String log) {
		this.log = log;
	}


	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}


	public int getTatRequest() {
		return tatRequest;
	}


	public void setTatRequest(int turnAroundTimeRequest) {
		this.tatRequest = turnAroundTimeRequest;
	}


	public int getTatFinal() {
		return tatFinal;
	}


	public void setTatFinal(int turnAroundTime) {
		this.tatFinal = turnAroundTime;
	}



	public String getType() {
		return type;
	}


	public void setType(String requestType) {
		this.type = requestType;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getRequester() {
		return requester;
	}


	public void setRequester(String requester) {
		this.requester = requester;
	}


	public String getAssignee() {
		return assignee;
	}


	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}


	public String getStatus() {
		return Status;
	}


	public void setStatus(String Status) {
		this.Status = Status;
	}
	
	/**
	 * This method returns a string array that will be shown on the request board as one row of data
	 * All the field sequence is predefined in RequestBoard
	 * @return a string array represents all the data to show on RequestBoard
	 */
	public String[] toRowDataOnRequestBoard() {
		String[] data = new String[RequestBoard.COLOMNS.length];
		data[0] = Integer.toString(getRequestID());
		data[1] = getType();
		data[2] = getRequester();
		data[3] = getSubmitDate();
		data[4] = getAcceptDate();
		data[5] = getDueDate();
		data[6] = Integer.toString(getTatRequest());
		data[7] = Integer.toString(getTatFinal());
		data[8] = getStatus();
		data[9] = getAssignee();
		data[10] = getSummary();		
		return data;
	}
}
