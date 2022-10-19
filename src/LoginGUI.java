import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class represents the login GUI, it shows up at the beginning when user runs app.
 * 
 *
 */
public class LoginGUI implements GUI {
	private JFrame loginFrame;
	private JPanel loginPanel;
	private JPanel registerPanel;
	private JTextField loginEmailField;
	private JPasswordField loginPasswordField;
	private JTextField registerFirstNameField;
	private JTextField registerLastNameField;
	private JTextField registerDepartmentField;
	private JTextField registerEmailField;
	private JPasswordField registerPasswordField;
	private UserManager userManager;

	/**
	 * Launch the LoginGUI application.
	 */
	public LoginGUI() {
		userManager = new UserManager();
		initialize();
	}

	/**
	 * Initialize the contents of the user login frame.
	 */
	public void initialize() {
		//Predefine some fonts that can be used throughout the UI components
		Font labelFont = new Font("Tahoma", Font.PLAIN, 16);
		Font boldLabelFont = new Font("Tahoma", Font.BOLD, 16);

		loginFrame = new JFrame();
		loginFrame.setTitle("Lab Request Tracker Login");
		loginFrame.setBounds(600, 250, 600, 500);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.getContentPane().setLayout(new CardLayout(0, 0));
		loginFrame.setVisible(true);

		loginPanel = new JPanel();
		loginFrame.getContentPane().add(loginPanel, "Login");
		loginPanel.setLayout(null);

		loginEmailField = new JTextField();
		loginEmailField.setBounds(150, 146, 333, 40);
		loginPanel.add(loginEmailField);
		loginEmailField.setColumns(10);

		loginPasswordField = new JPasswordField();
		loginPasswordField.setBounds(150, 221, 333, 40);
		loginPanel.add(loginPasswordField);

		JLabel loginEmailLabel = new JLabel("Email:");
		loginEmailLabel.setFont(labelFont);
		loginEmailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		loginEmailLabel.setBounds(38, 146, 91, 40);
		loginPanel.add(loginEmailLabel);

		JLabel loginPasswordLabel = new JLabel("Password:");
		loginPasswordLabel.setFont(labelFont);
		loginPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		loginPasswordLabel.setBounds(38, 221, 91, 40);
		loginPanel.add(loginPasswordLabel);

		JLabel loginWelcomeLabel = new JLabel("Welcome to Lab Request Tracker!");
		loginWelcomeLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		loginWelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginWelcomeLabel.setBounds(58, 28, 453, 93);
		loginPanel.add(loginWelcomeLabel);

		JButton loginLoginBtn = new JButton("Login");
		loginLoginBtn.setFont(boldLabelFont);
		loginLoginBtn.setBounds(150, 303, 144, 48);
		loginPanel.add(loginLoginBtn);

		//ActionListener for "Login" button in Login page
		loginLoginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = loginEmailField.getText();
				String password = loginPasswordField.getText();
				if (userManager.checkUser(email, password)) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								MainGUI window = new MainGUI(userManager.getCurrentUser(email));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					loginFrame.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "User or password is not correct, please try again.");
				}
			}
		});

		JButton loginRegisterBtn = new JButton("Register");
		loginRegisterBtn.setFont(boldLabelFont);
		loginRegisterBtn.setBounds(339, 303, 144, 48);
		loginPanel.add(loginRegisterBtn);

		//ActionListener for "Register" button in Login page
		loginRegisterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPanel.setVisible(true);
				loginPanel.setVisible(false);
			}
		});

		registerPanel = new JPanel();
		loginFrame.getContentPane().add(registerPanel, "Register");
		registerPanel.setLayout(null);

		registerFirstNameField = new JTextField();
		registerFirstNameField.setBounds(194, 119, 320, 35);
		registerPanel.add(registerFirstNameField);
		registerFirstNameField.setColumns(10);
		registerPanel.setVisible(false);

		JLabel registerFirstNameLabel = new JLabel("First Name:");
		registerFirstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		registerFirstNameLabel.setFont(labelFont);
		registerFirstNameLabel.setBounds(58, 119, 101, 35);
		registerPanel.add(registerFirstNameLabel);

		JLabel registerLastNameLabel = new JLabel("Last Name:");
		registerLastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		registerLastNameLabel.setFont(labelFont);
		registerLastNameLabel.setBounds(58, 165, 101, 35);
		registerPanel.add(registerLastNameLabel);

		registerLastNameField = new JTextField();
		registerLastNameField.setColumns(10);
		registerLastNameField.setBounds(194, 165, 320, 35);
		registerPanel.add(registerLastNameField);

		JLabel registerDepartmentLabel = new JLabel("Department:");
		registerDepartmentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		registerDepartmentLabel.setFont(labelFont);
		registerDepartmentLabel.setBounds(58, 211, 101, 35);
		registerPanel.add(registerDepartmentLabel);

		registerDepartmentField = new JTextField();
		registerDepartmentField.setColumns(10);
		registerDepartmentField.setBounds(194, 211, 320, 35);
		registerPanel.add(registerDepartmentField);

		JLabel registerEmailLabel = new JLabel("Email:");
		registerEmailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		registerEmailLabel.setFont(labelFont);
		registerEmailLabel.setBounds(58, 257, 101, 35);
		registerPanel.add(registerEmailLabel);

		registerEmailField = new JTextField();
		registerEmailField.setColumns(10);
		registerEmailField.setBounds(194, 257, 320, 35);
		registerPanel.add(registerEmailField);

		JLabel registerPasswordLabel = new JLabel("Password:");
		registerPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		registerPasswordLabel.setFont(labelFont);
		registerPasswordLabel.setBounds(58, 303, 101, 35);
		registerPanel.add(registerPasswordLabel);

		registerPasswordField = new JPasswordField();
		registerPasswordField.setBounds(194, 303, 320, 35);
		registerPanel.add(registerPasswordField);

		JLabel registerWelcomeLabel = new JLabel("Welcome to Lab Request Tracker!");
		registerWelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		registerWelcomeLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		registerWelcomeLabel.setBounds(58, 28, 453, 93);
		registerPanel.add(registerWelcomeLabel);

		JButton registerRegisterBtn = new JButton("Register");
		registerRegisterBtn.setFont(boldLabelFont);
		registerRegisterBtn.setBounds(104, 371, 144, 48);
		registerPanel.add(registerRegisterBtn);

		//ActionListener for "Register" button in Register page
		registerRegisterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstName = registerFirstNameField.getText();
				String lastName = registerLastNameField.getText();
				String department = registerDepartmentField.getText();
				String email = registerEmailField.getText();
				String password = registerPasswordField.getText();

				if (firstName.trim().equals("") || lastName.trim().equals("") || department.trim().equals("")
						|| email.trim().equals("") || password.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Please provide complete information.");
				} else if (userManager.checkUser(email)){
					JOptionPane.showMessageDialog(null, "Email address is already registered.");
				} else {
					int roleID = 1;		//default is 1-Requester
					int active = 1;		//default is 1-active
					userManager.createUser(firstName, lastName, email, roleID, department, active, password);
					JOptionPane.showMessageDialog(null, "Registration successful!");
					registerPanel.setVisible(false);
					loginPanel.setVisible(true);
				}
			}
		});

		JButton registerCancelBtn = new JButton("Cancel");
		registerCancelBtn.setFont(boldLabelFont);
		registerCancelBtn.setBounds(329, 371, 144, 48);
		registerPanel.add(registerCancelBtn);

		//ActionListener for "Cancel" button in Register page
		registerCancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPanel.setVisible(false);
				loginPanel.setVisible(true);
			}
		});
	}

}
