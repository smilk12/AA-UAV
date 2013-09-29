package il.co.server;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;

/**
 * ServerGui is a class that implements the drone server User Interface.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
@SuppressWarnings("serial")
public class ServerGui extends JFrame {

	//.: View variables.
	private JPanel contentPane;
	public  JTextArea textArea;
	private JMenuItem mntmStartServer;
	private JMenuItem mntmProperties;
	private JMenuItem mntmAboutUs;	
	private JLabel AccX;
	private JLabel AccY;
	private JLabel AccZ;
	private JLabel gpsLat;
	private JLabel gpsLon;
	private JLabel gpsAlt;
	private JLabel gpsCity;
	private JMenuItem mntmStopServer;
	private Thread server;
	private JLabel yaw;
	private JLabel roll;
	private JLabel pitch;
	private JLabel battaryLevel;
	private JLabel battaryVoltage;
	private JLabel battaryStatus;
	//===========================

	//**************************** ..:: main ::.. ***************************//
	/**
	 * Launch the drone server application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGui frame = new ServerGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//************************************************************************//   

	//************************** ..:: ServerGui ::.. *************************//
	/**
	 * Create the frame by running the initComponents and createEvents methods.
	 */
	public ServerGui() {
		initComponents();
		createEvents();         
	}
	//************************************************************************//   

	//******************** ..:: Initialize Components ::.. *******************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize all of the view variables.
	 * 
	 * @since           1.0
	 */	    
	private void initComponents(){
		setTitle("ArDrone Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 783, 583);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmStartServer = new JMenuItem("Start Server");

		mnFile.add(mntmStartServer);

		mntmStopServer = new JMenuItem("Stop Server");
		mnFile.add(mntmStopServer);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		mntmProperties = new JMenuItem("Properties");
		mnEdit.add(mntmProperties);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		mntmAboutUs = new JMenuItem("About Us");
		mnAbout.add(mntmAboutUs);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, SystemColor.textHighlightText, SystemColor.textHighlight, SystemColor.controlDkShadow, SystemColor.scrollbar));

		JLabel lblServerActivity = new JLabel("Server Activity");

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Accelerometer", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "GPS", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Compass", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblXroll = new JLabel("X (Roll):");

		roll = new JLabel("0.0");

		JLabel lblYpitch = new JLabel("Y (Pitch):");

		pitch = new JLabel("0.0");

		JLabel lblZyaw = new JLabel("Z (Yaw):");

		yaw = new JLabel(" 0.0");

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(ServerGui.class.getResource("/il/co/icons/compass.png")));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
										.addComponent(lblZyaw)
										.addGap(18)
										.addComponent(yaw))
										.addGroup(gl_panel_2.createSequentialGroup()
												.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
														.addComponent(lblYpitch)
														.addComponent(lblXroll))
														.addGap(18)
														.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
																.addComponent(roll)
																.addComponent(pitch))))
																.addPreferredGap(ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
																.addComponent(label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
				);
		gl_panel_2.setVerticalGroup(
				gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblXroll)
												.addComponent(roll))
												.addGap(3)
												.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblYpitch)
														.addComponent(pitch))
														.addGap(3)
														.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
																.addComponent(lblZyaw)
																.addComponent(yaw)))
																.addComponent(label, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
																.addContainerGap())
				);
		panel_2.setLayout(gl_panel_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Phone Battary", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblLevel = new JLabel("Level:");

		battaryLevel = new JLabel("0.0");

		JLabel lblVoltage = new JLabel("Voltage:");

		battaryVoltage = new JLabel("0.0");

		JLabel lblStatus = new JLabel("Status:");

		battaryStatus = new JLabel("Unknown");

		JProgressBar progressBar = new JProgressBar();

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ServerGui.class.getResource("/il/co/icons/BatteryDis.png")));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addComponent(lblVoltage)
								.addComponent(lblLevel)
								.addComponent(lblStatus))
								.addGap(28)
								.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
												.addComponent(battaryStatus)
												.addGroup(gl_panel_3.createSequentialGroup()
														.addComponent(battaryLevel)
														.addGap(18)
														.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
														.addComponent(battaryVoltage))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
														.addGap(13))
				);
		gl_panel_3.setVerticalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_3.createSequentialGroup()
														.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
																.addComponent(lblLevel)
																.addComponent(battaryLevel))
																.addGap(3)
																.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
																		.addComponent(lblVoltage)
																		.addComponent(battaryVoltage)))
																		.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																		.addGap(3)
																		.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
																				.addComponent(lblStatus)
																				.addComponent(battaryStatus)))
																				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																				.addContainerGap())
				);
		panel_3.setLayout(gl_panel_3);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 482, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(panel, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
												.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
												.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
												.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)))
												.addComponent(lblServerActivity))
												.addContainerGap(13, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(lblServerActivity)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 437, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(9)
										.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(9)
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(56, Short.MAX_VALUE))
				);
		JLabel lblLatitude = new JLabel("Latitude :");
		JLabel lblLongitude = new JLabel("Longitude :");
		JLabel lblAltitude = new JLabel("altitude :");

		JLabel lblCity = new JLabel("City :");

		gpsCity = new JLabel("Unknown");

		gpsAlt = new JLabel("0.0");

		gpsLon = new JLabel("0.0");

		gpsLat = new JLabel("0.0");

		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(ServerGui.class.getResource("/il/co/icons/satellite.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addComponent(lblLatitude)
										.addGap(18)
										.addComponent(gpsLat))
										.addGroup(gl_panel_1.createSequentialGroup()
												.addComponent(lblLongitude)
												.addGap(10)
												.addComponent(gpsLon))
												.addGroup(gl_panel_1.createSequentialGroup()
														.addComponent(lblAltitude)
														.addGap(21)
														.addComponent(gpsAlt))
														.addGroup(gl_panel_1.createSequentialGroup()
																.addComponent(lblCity)
																.addGap(38)
																.addComponent(gpsCity)))
																.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
																.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
				);
		gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(label_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addComponent(lblLatitude)
												.addComponent(gpsLat))
												.addGap(3)
												.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
														.addComponent(lblLongitude)
														.addComponent(gpsLon))
														.addGap(3)
														.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																.addComponent(lblAltitude)
																.addComponent(gpsAlt))
																.addGap(2)
																.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblCity)
																		.addComponent(gpsCity))))
																		.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		panel_1.setLayout(gl_panel_1);
		JLabel lblX = new JLabel("X :");

		AccX = new JLabel("0.0");
		JLabel lblZ = new JLabel("Z :");

		AccZ = new JLabel(" 0.0");
		JLabel lblY = new JLabel("Y :");

		AccY = new JLabel("0.0");

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(ServerGui.class.getResource("/il/co/icons/Accelerometer.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblX)
										.addGap(10)
										.addComponent(AccX))
										.addGroup(gl_panel.createSequentialGroup()
												.addComponent(lblY)
												.addGap(10)
												.addComponent(AccY))
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(lblZ)
														.addGap(7)
														.addComponent(AccZ)))
														.addPreferredGap(ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
														.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblX)
												.addComponent(AccX))
												.addGap(3)
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblY)
														.addComponent(AccY))
														.addGap(3)
														.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
																.addComponent(lblZ)
																.addComponent(AccZ))))
																.addContainerGap())
				);
		panel.setLayout(gl_panel);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setForeground(SystemColor.window);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(SystemColor.desktop);
		scrollPane.setViewportView(textArea);
		contentPane.setLayout(gl_contentPane);
	}
	//************************************************************************//   

	//********************* ..:: Create Events ::.. **************************// 
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to start the frame events that runs in background.
	 * This method start the server thread. 
	 * This method handle a button click event that changes the activity view.   
	 * 
	 * 
	 * @since           1.0
	 */	    
	private void createEvents(){
		mntmStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					server = new server(textArea,AccX,AccY,AccZ,gpsLat,gpsLon,gpsAlt,gpsCity,yaw,roll,pitch,battaryLevel,battaryVoltage,battaryStatus);				
					server.start();
				}catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		mntmStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
	}
	//************************************************************************//   
}
