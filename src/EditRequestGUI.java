import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class represents the GUI for updating specific request
 * It shows up after "Update chosen request" was clicked in the pull-down menu of "Actions".
 * 
 *
 */
public class EditRequestGUI implements GUI {
	private JFrame frmEditRequest;
	private JTextArea taRequestSummary;
	private JTextField tfTatRequest;
	private JTextArea taEditComment;
	private JComboBox cbRequestType;
	private JComboBox cbRequestStatus;
	private JComboBox cbAssignee;

	private Request currentRequest;
	private RequestManager requestManager;
	private User currentUser;

	/**
	 * Constructor for EditRequestGUI
	 * @param requestManager the requestManager that calls for this Edit request form
	 * @param currentRequest the request pass in for edit
	 */
	public EditRequestGUI(RequestManager requestManager, Request currentRequest) {
		this.requestManager = requestManager;
		this.currentRequest = currentRequest;
		currentUser = requestManager.getUser();
		initialize();
	}

	/**
	 * Initialize the contents of the edit request frame.
	 */
	public void initialize() {
		//predefine some fonts that can be used throughout the UI components
		Font boldLabelFont = new Font("Tahoma", Font.BOLD, 12);
		Font largeFont = new Font("Tahoma", Font.BOLD, 14);

		frmEditRequest = new JFrame();
		frmEditRequest.setTitle("Edit Request");
		frmEditRequest.setBounds(200, 200, 1000, 500);
		frmEditRequest.getContentPane().setLayout(null);
		frmEditRequest.setVisible(true);

		JLabel lblRequestType = new JLabel("Please select request Type:");
		lblRequestType.setFont(boldLabelFont);
		lblRequestType.setBounds(10, 11, 227, 27);
		frmEditRequest.getContentPane().add(lblRequestType);

		cbRequestType = new JComboBox();
		cbRequestType.setFont(largeFont);
		cbRequestType.setModel(new DefaultComboBoxModel(requestManager.getComboBoxFields(true, "analysis_type", "type", 1, "")));
		cbRequestType.setSelectedItem(currentRequest.getType());
		cbRequestType.setMaximumRowCount(10);
		cbRequestType.setBounds(262, 11, 200, 27);
		frmEditRequest.getContentPane().add(cbRequestType);

		JLabel lblPleaseAddTurnaroundtime = new JLabel("Please add turn-around-time request:");
		lblPleaseAddTurnaroundtime.setFont(boldLabelFont);
		lblPleaseAddTurnaroundtime.setBounds(10, 63, 241, 27);
		frmEditRequest.getContentPane().add(lblPleaseAddTurnaroundtime);

		tfTatRequest = new JTextField();
		tfTatRequest.setBounds(261, 63, 76, 28);
		frmEditRequest.getContentPane().add(tfTatRequest);
		tfTatRequest.setColumns(10);
		tfTatRequest.setText(Integer.toString(currentRequest.getTatRequest()));

		JLabel lblNewLabel = new JLabel("Please add request Summary:");
		lblNewLabel.setFont(boldLabelFont);
		lblNewLabel.setBounds(10, 112, 227, 27);
		frmEditRequest.getContentPane().add(lblNewLabel);

		JScrollPane requestSummary = new JScrollPane();
		requestSummary.setBounds(10, 149, 500, 227);
		frmEditRequest.getContentPane().add(requestSummary);

		taRequestSummary = new JTextArea();
		requestSummary.setViewportView(taRequestSummary);
		taRequestSummary.setColumns(10);
		taRequestSummary.setText(currentRequest.getSummary());

		JLabel lblRequestStatus = new JLabel("Request Status:");
		lblRequestStatus.setFont(boldLabelFont);
		lblRequestStatus.setBounds(566, 12, 124, 27);
		frmEditRequest.getContentPane().add(lblRequestStatus);

		cbRequestStatus = new JComboBox();
		cbRequestStatus.setModel(new DefaultComboBoxModel(requestManager.getComboBoxFields(true, "request_status", "status", 1, "")));
		cbRequestStatus.setMaximumRowCount(10);
		cbRequestStatus.setFont(largeFont);
		cbRequestStatus.setBounds(718, 11, 214, 27);
		frmEditRequest.getContentPane().add(cbRequestStatus);
		cbRequestStatus.setSelectedItem(currentRequest.getStatus());
		if  (currentUser.getRoleID() == 1) {
			cbRequestStatus.setEnabled(false);		//Only manager and analyst are authorized to edit this field
		}

		JLabel lblAssignee = new JLabel("Assignee:");
		lblAssignee.setFont(boldLabelFont);
		lblAssignee.setBounds(566, 63, 124, 27);
		frmEditRequest.getContentPane().add(lblAssignee);

		cbAssignee = new JComboBox();
		cbAssignee.setModel(new DefaultComboBoxModel(requestManager.getComboBoxFields(false, "user", "first_name, last_name", 2, "role_id=3")));
		cbAssignee.setMaximumRowCount(10);
		cbAssignee.setFont(largeFont);
		cbAssignee.setBounds(718, 62, 214, 27);
		frmEditRequest.getContentPane().add(cbAssignee);
		cbAssignee.setSelectedItem(currentRequest.getAssignee());
		if  (currentUser.getRoleID() == 1) {
			cbAssignee.setEnabled(false);		//Only manager and analyst are authorized to edit this field
		}

		JLabel lblEditComments = new JLabel("Edit Comment:");
		lblEditComments.setFont(boldLabelFont);
		lblEditComments.setBounds(566, 112, 227, 27);
		frmEditRequest.getContentPane().add(lblEditComments);

		JScrollPane editComment = new JScrollPane();
		editComment.setBounds(566, 149, 408, 225);
		frmEditRequest.getContentPane().add(editComment);

		taEditComment = new JTextArea();
		editComment.setViewportView(taEditComment);
		taEditComment.setColumns(10);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(largeFont);
		btnSubmit.setBounds(129, 387, 259, 49);
		frmEditRequest.getContentPane().add(btnSubmit);
		
		//ActionListener for "Submit" bottom click
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (inputIsValid()) {
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					LocalDate today = LocalDate.now();
					
					//Get data from the GUI
					int requestID = currentRequest.getRequestID();
					int typeID = cbRequestType.getSelectedIndex();
					int tatRequest = Integer.parseInt(tfTatRequest.getText());
					String summary = taRequestSummary.getText();
					int requestStatusID = cbRequestStatus.getSelectedIndex();
					String requestStatus = (String) cbRequestStatus.getSelectedItem();
					String previousStatus = currentRequest.getStatus();
					String acceptDate = currentRequest.getAcceptDate();
					String dueDate = currentRequest.getDueDate();
					int tatFinal = currentRequest.getTatFinal();
					int assigneeID = cbAssignee.getSelectedIndex();    //Important! Analyst user ID must starts from 0
					String previousAssignee = currentRequest.getAssignee();
					String assigneeName = (String) cbAssignee.getSelectedItem();
					String log = currentRequest.getLog();
					
					//Set data for the currentRequest object
					currentRequest.setType((String) cbRequestType.getSelectedItem());					
					currentRequest.setTatRequest(tatRequest);
					currentRequest.setSummary(summary);
					currentRequest.setStatus(requestStatus);
					if (requestStatus.equals("Accepted")) {
						acceptDate = today.toString();
						dueDate = today.plusDays(tatRequest).toString();
						currentRequest.setAcceptDate(acceptDate);
						currentRequest.setDueDate(dueDate);
					} else if (requestStatus.equals("Reported")) {
						LocalDate acceptedDate = LocalDate.parse(currentRequest.getAcceptDate());
						tatFinal = (int) ChronoUnit.DAYS.between(acceptedDate, today);
						currentRequest.setTatFinal(tatFinal);
					}
					currentRequest.setAssignee((String) cbAssignee.getSelectedItem());	
					
					if (!requestStatus.equals(previousStatus)) {
						log = log + timestamp.toString().substring(0, 19) + " " + requestManager.getUser().getFullName() + " changed status: " + requestStatus + "\n";
					}
					if (!assigneeName.equals(previousAssignee)) {
						System.out.println(assigneeName);
						System.out.println(previousAssignee);
						log = log + timestamp.toString().substring(0, 19) + " " + requestManager.getUser().getFullName() + " assigned to: " + assigneeName + "\n";
					}
					if (!taEditComment.getText().isEmpty()) {
						log = log + timestamp.toString().substring(0, 19) + " " + requestManager.getUser().getFullName() + ": " + taEditComment.getText() + "\n";
					}
					currentRequest.setLog(log);
					
					//Update data in database
					requestManager.updateRequest(requestID, typeID, tatRequest, summary, requestStatusID, acceptDate, dueDate, tatFinal, assigneeID, log);

					//display success message and close the form
					JOptionPane.showMessageDialog(null, "Edit request successful! Please refresh the table.");
					frmEditRequest.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Please input corrent information.");

				}
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(largeFont);
		btnCancel.setBounds(588, 387, 259, 49);
		frmEditRequest.getContentPane().add(btnCancel);
		
		//ActionListener for "Cancel" bottom click
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmEditRequest.dispose();
			}
		});

	}

	/**
	 * This is a helper method for checking the input from the request is valid or not
	 * @return true/false - if the input is valid or not
	 */
	private boolean inputIsValid() {
		if (tfTatRequest.getText().isEmpty()) {
			return false;
		} else {
			try {
				Integer.parseInt(tfTatRequest.getText());
			} catch (Exception e) {
				return false;
			}
		}
		boolean invalid = cbRequestType.getSelectedIndex() == 0 || taRequestSummary.getText().isEmpty();
		return !invalid;
	}
}
