import java.awt.EventQueue;

/**
 * This is the runner class that runs the application
 * 
 *
 */
public class LabRequestTrackerRunner {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI login = new LoginGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
