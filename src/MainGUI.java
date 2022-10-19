import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the main GUI that showing the information of all requests
 * It pops up after successful user login
 * 
 *
 */
public class MainGUI implements GUI {
	private JFrame mainFrame;
	private JTable table;  //this table shows all request info that pulls from database
	private JTextField textFieldAddComment;
	private JTextPane txtpnRequestDiscription;
	private JTextPane txtpnRequestLog ;

	private User currentUser;
	private RequestManager requestManager;
	private RequestBoard allRequestBoard;
	private RequestBoard currentBoard;

	/**
	 * Launch the Main GUI.
	 */
	public MainGUI(User user) {
		currentUser = user;
		requestManager = new RequestManager(user);
		allRequestBoard = requestManager.getAllRequestBoard();
		currentBoard = allRequestBoard;
		initialize();
	}

	/**
	 * Initialize the contents of the main GUI.
	 */
	public void initialize() {
		//predefine default font that can be used throughout the UI components
		Font defaultFont = new Font("Tahoma", Font.PLAIN, 12);

		mainFrame = new JFrame();
		mainFrame.setTitle("Lab Request Tracker");
		mainFrame.setBounds(100, 100, 1600, 900);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);

		JScrollPane requestDescription = new JScrollPane();
		requestDescription.setBounds(10, 584, 716, 245);
		mainFrame.getContentPane().add(requestDescription);

		txtpnRequestDiscription = new JTextPane();
		requestDescription.setViewportView(txtpnRequestDiscription);
		txtpnRequestDiscription.setFont(new Font("Tahoma", Font.BOLD, 14));

		JScrollPane requestLog = new JScrollPane();
		requestLog.setBounds(731, 585, 843, 204);
		mainFrame.getContentPane().add(requestLog);

		txtpnRequestLog = new JTextPane();
		requestLog.setViewportView(txtpnRequestLog);
		txtpnRequestLog.setFont(defaultFont);

		JScrollPane requestTable = new JScrollPane();
		requestTable.setBounds(10, 11, 1564, 530);
		mainFrame.getContentPane().add(requestTable);


		//JTable coding is here:
		table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(RequestBoard.COLOMNS);
		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredColumnWidth();		//Set preferred column width for better display
		setTableValue(allRequestBoard.getData());	//initialize the table with the info of all requests

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		requestTable.setViewportView(table);
		ListSelectionModel lineSelectionModel = table.getSelectionModel();

		//Data table - row selection listener
		lineSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (! lineSelectionModel.isSelectionEmpty()) {
					int selectedRow = lineSelectionModel.getMinSelectionIndex();
					Request selectedRequest = allRequestBoard.getRequests().get(selectedRow);
					txtpnRequestDiscription.setText(requestManager.getRequestDescription(selectedRequest));
					txtpnRequestLog.setText(selectedRequest.getLog());
				}
			}
		});

		textFieldAddComment = new JTextField();
		textFieldAddComment.setBounds(731, 794, 706, 35);
		mainFrame.getContentPane().add(textFieldAddComment);
		textFieldAddComment.setColumns(10);

		JButton btnAddComment = new JButton("Add Comment");
		btnAddComment.setBounds(1442, 794, 132, 35);
		mainFrame.getContentPane().add(btnAddComment);

		JLabel lblRequestDescription = new JLabel("Request Description:");
		lblRequestDescription.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblRequestDescription.setBounds(10, 552, 314, 27);
		mainFrame.getContentPane().add(lblRequestDescription);

		JLabel lblRequestLog = new JLabel("Request Log:");
		lblRequestLog.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblRequestLog.setBounds(731, 552, 314, 27);
		mainFrame.getContentPane().add(lblRequestLog);

		//ActionListener for "Add Comment" button
		btnAddComment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (! lineSelectionModel.isSelectionEmpty()) {
					int selectedRow = lineSelectionModel.getMinSelectionIndex();
					Request selectedRequest = currentBoard.getRequests().get(selectedRow);
					String currentLog = selectedRequest.getLog();
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					currentLog = currentLog + timestamp.toString().substring(0, 19) + " " + currentUser.getFullName()
					+": " + textFieldAddComment.getText() + "\n";
					selectedRequest.setLog(currentLog);
					txtpnRequestLog.setText(currentLog);
					requestManager.updateLog(selectedRequest, currentLog);
					textFieldAddComment.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Please select a request!");
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		JMenu menuActions = new JMenu("Actions");
		menuActions.setFont(defaultFont);
		menuBar.add(menuActions);

		JMenuItem actionsMenuItem_1 = new JMenuItem("Submit new request");
		actionsMenuItem_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuActions.add(actionsMenuItem_1);
		
		//create a SubmitRequestGUI
		actionsMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							SubmitRequestGUI submitRequest = new SubmitRequestGUI(requestManager);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		JMenuItem actionsMenuItem_2 = new JMenuItem("Edit chosen request");
		actionsMenuItem_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuActions.add(actionsMenuItem_2);
		
		//create a EditRequestGUI after checking row selected
		actionsMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (! lineSelectionModel.isSelectionEmpty()) {
					int selectedRow = lineSelectionModel.getMinSelectionIndex();
					Request selectedRequest = currentBoard.getRequests().get(selectedRow);
					if ((currentUser.getRoleID() == 1) && (! currentUser.getFullName().equals(selectedRequest.getRequester()))) {
						JOptionPane.showMessageDialog(null, "You can only edit your own requests.");
					} else if ((currentUser.getRoleID() == 1) && (selectedRequest.getStatus().equals("Reported") ||
							selectedRequest.getStatus().equals("Rejected")) ) {
						JOptionPane.showMessageDialog(null, "You cannot edit request that's been closed.");
					} else {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									EditRequestGUI editRequest = new EditRequestGUI(requestManager, selectedRequest);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select a request!");
				}
			}
		});

		JMenu manuQuickDisplay = new JMenu("Quick-Display");
		manuQuickDisplay.setFont(defaultFont);
		menuBar.add(manuQuickDisplay);

		JMenuItem quickDisplayItem_1 = new JMenuItem("Refresh");
		quickDisplayItem_1.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_1);
		
		//ActionLister for "Refresh" item chosen
		quickDisplayItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem quickDisplayItem_2 = new JMenuItem("All Requests");
		quickDisplayItem_2.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_2);
		
		//ActionLister for "All requests" item chosen
		quickDisplayItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allRequestBoard = requestManager.getAllRequestBoard();	//talk to the database
				currentBoard = allRequestBoard;
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem quickDisplayItem_3 = new JMenuItem("Open requests");
		quickDisplayItem_3.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_3);
		
		//ActionLister for "Open requests" item chosen
		quickDisplayItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pull the info of all the open requests (not Rejected or Reported) with RequestBoard
				ArrayList<Request> openRequest = new ArrayList<>();
				for (Request request: allRequestBoard.getRequests()) {
					String status = request.getStatus();
					if (! (status.equals("Rejected") || status.equals("Reported"))) {
						openRequest.add(request);
					}
				}
				currentBoard = new RequestBoard(openRequest);
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem quickDisplayItem_4 = new JMenuItem("Last 7 days");
		quickDisplayItem_4.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_4);
		
		//ActionLister for "Last 7 days" item chosen
		quickDisplayItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pull the info of requests submitted in the last 7 day with RequestBoard
				currentBoard = new RequestBoard(requestManager.getRequestFromLastNDays(allRequestBoard.getRequests(), 7));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem quickDisplayItem_5 = new JMenuItem("Last 14 days");
		quickDisplayItem_5.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_5);
		
		//ActionLister for "Last 14 days" item chosen
		quickDisplayItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pull the info of requests submitted in the last 14 day with RequestBoard
				currentBoard = new RequestBoard(requestManager.getRequestFromLastNDays(allRequestBoard.getRequests(), 14));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem quickDisplayItem_6 = new JMenuItem("Last 30 days");
		quickDisplayItem_6.setFont(defaultFont);
		manuQuickDisplay.add(quickDisplayItem_6);
		
		//ActionLister for "Last 30 days" item chosen
		quickDisplayItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//pull the info of requests submitted in the last 30 day with RequestBoard
				currentBoard = new RequestBoard(requestManager.getRequestFromLastNDays(allRequestBoard.getRequests(), 30));
				setTableValue(currentBoard.getData());
			}
		});

		JMenu menuSort = new JMenu("Sort");
		menuSort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		menuBar.add(menuSort);

		JMenuItem sortItem_1 = new JMenuItem("By request ID");
		sortItem_1.setFont(defaultFont);
		menuSort.add(sortItem_1);
		
		//ActionLister for "By request ID" item chosen
		sortItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(currentBoard.getRequests(), requestManager.getComparator("requestID"));
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem sortItem_2 = new JMenuItem("By request type");
		sortItem_2.setFont(defaultFont);
		menuSort.add(sortItem_2);
		
		//ActionLister for "By request type" item chosen
		sortItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(currentBoard.getRequests(), requestManager.getComparator("type"));
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem sortItem_3 = new JMenuItem("By submit date");
		sortItem_3.setFont(defaultFont);
		menuSort.add(sortItem_3);
		
		//ActionLister for "By submit date" item chosen
		sortItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(currentBoard.getRequests(), requestManager.getComparator("submitDate"));
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem sortItem_4 = new JMenuItem("By request status");
		sortItem_4.setFont(defaultFont);
		menuSort.add(sortItem_4);
		
		//ActionLister for "By request status" item chosen
		sortItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(currentBoard.getRequests(), requestManager.getComparator("status"));
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});

		JMenuItem sortItem_5 = new JMenuItem("By assignee");
		sortItem_5.setFont(defaultFont);
		menuSort.add(sortItem_5);
		
		//ActionLister for "By assignee" item chosen
		sortItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(currentBoard.getRequests(), requestManager.getComparator("assignee"));
				currentBoard.setData(currentBoard.updateTableData(currentBoard.getRequests()));
				setTableValue(currentBoard.getData());
			}
		});
	}

	/**
	 * This helper method updates the table with the input data
	 * @param data: 2D string array that should match the requestBoard columns
	 */
	private void setTableValue (String[][] data) {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for (int i = 0; i < data.length; i++) {
			tableModel.addRow(data[i]);
		}
		table.setModel(tableModel);
		tableModel.fireTableDataChanged();
	}

	/**
	 * This helper method is used to set preferred column width for the table display
	 */
	private void setPreferredColumnWidth() {
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(150);
		columnModel.getColumn(2).setPreferredWidth(120);
		columnModel.getColumn(3).setPreferredWidth(80);
		columnModel.getColumn(4).setPreferredWidth(80);
		columnModel.getColumn(5).setPreferredWidth(80);
		columnModel.getColumn(6).setPreferredWidth(85);
		columnModel.getColumn(7).setPreferredWidth(85);
		columnModel.getColumn(8).setPreferredWidth(150);
		columnModel.getColumn(9).setPreferredWidth(120);
		columnModel.getColumn(10).setPreferredWidth(550);
	}
}
