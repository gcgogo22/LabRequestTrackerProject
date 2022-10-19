import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.sql.Timestamp;
import javax.swing.JTextArea;

/**
 * This class represents the GUI for submitting a new request
 * It shows up after "Submit new request" was clicked in the pull-down menu of "Actions".
 * 
 *
 */

public class SubmitRequestGUI implements GUI {

	private JFrame frmSummitNewRequest;
	private JTextArea taRequestSummary;
	private JTextField tfTatRequest;
	private JComboBox cbRequestType;

	private RequestManager requestManager;
	private User currentUser;
	
	/**
	 * Constructor for SubmitRequestGUI
	 * @param requestManager the requestManager that calls for this submit request form
	 */
	public SubmitRequestGUI(RequestManager requestManager) {
		this.requestManager = requestManager;
		initialize();
	}

	/**
	 * Initialize the contents of the submit request frame.
	 */
	public void initialize() {
		//predefine some fonts that can be used throughout the UI components
		Font boldLabelFont = new Font("Tahoma", Font.BOLD, 12);
		Font largeFont = new Font("Tahoma", Font.BOLD, 14);
		frmSummitNewRequest = new JFrame();
		frmSummitNewRequest.setTitle("Submit New Request");
		frmSummitNewRequest.setBounds(200, 200, 1000, 500);
		frmSummitNewRequest.getContentPane().setLayout(null);
		frmSummitNewRequest.setVisible(true);

		JScrollPane requestSummary = new JScrollPane();
		requestSummary.setBounds(10, 149, 964, 227);
		frmSummitNewRequest.getContentPane().add(requestSummary);

		taRequestSummary = new JTextArea();
		requestSummary.setViewportView(taRequestSummary);
		taRequestSummary.setColumns(10);

		JLabel lblNewLabel = new JLabel("Please add request Summary:");
		lblNewLabel.setFont(boldLabelFont);
		lblNewLabel.setBounds(10, 112, 227, 27);
		frmSummitNewRequest.getContentPane().add(lblNewLabel);


		cbRequestType = new JComboBox();
		cbRequestType.setFont(largeFont);
		cbRequestType.setModel(new DefaultComboBoxModel(requestManager.getComboBoxFields(true, "analysis_type", "type", 1, "")));
		cbRequestType.setMaximumRowCount(10);
		cbRequestType.setBounds(262, 11, 302, 27);
		frmSummitNewRequest.getContentPane().add(cbRequestType);

		JLabel lblRequestType = new JLabel("Please select request Type:");
		lblRequestType.setFont(boldLabelFont);
		lblRequestType.setBounds(10, 11, 227, 27);
		frmSummitNewRequest.getContentPane().add(lblRequestType);

		JLabel lblPleaseAddTurnaroundtime = new JLabel("Please add turn-around-time request:");
		lblPleaseAddTurnaroundtime.setFont(boldLabelFont);
		lblPleaseAddTurnaroundtime.setBounds(10, 63, 241, 27);
		frmSummitNewRequest.getContentPane().add(lblPleaseAddTurnaroundtime);

		tfTatRequest = new JTextField();
		tfTatRequest.setBounds(261, 63, 76, 28);
		frmSummitNewRequest.getContentPane().add(tfTatRequest);
		tfTatRequest.setColumns(10);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(largeFont);
		btnSubmit.setBounds(129, 387, 259, 49);
		frmSummitNewRequest.getContentPane().add(btnSubmit);
		
		//ActionListener for "Submit" bottom click
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (inputIsValid()) {
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String submitTime[] = timestamp.toString().substring(0, 19).split("\\s");
					int requestID = requestManager.getAllRequestCount() + 1;
					int typeID = cbRequestType.getSelectedIndex();
					int requesterID = requestManager.getUser().getUserID();
					String submitDate = submitTime[0];
					int tatRequest = Integer.parseInt(tfTatRequest.getText());
					String summary = taRequestSummary.getText();
					int requestStatusID = 1;
					String acceptDate = "0000-00-00";
					String dueDate = "0000-00-00";		
					int tatFinal = -1;	
					int assigneeID = 0;
					String log = timestamp.toString().substring(0, 19) + " " + requestManager.getUserName(requesterID) + " Request Submitted\n";
					requestManager.insertRequest(requestID, typeID, requesterID, submitDate, tatRequest, summary,
							requestStatusID, acceptDate, dueDate, tatFinal, assigneeID, log);
					JOptionPane.showMessageDialog(null, "Request Submit successful! Your reqeust ID is " + requestID + ".\n"
							+ "Please click \"All request\" in \"Quick-display\" menu to see your new submission.");
					frmSummitNewRequest.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Please input corrent information.");

				}
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(largeFont);
		btnCancel.setBounds(588, 387, 259, 49);
		frmSummitNewRequest.getContentPane().add(btnCancel);
		//ActionListener for "Cancel" bottom click
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSummitNewRequest.dispose();
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
