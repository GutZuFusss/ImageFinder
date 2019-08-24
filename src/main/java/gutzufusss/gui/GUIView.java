package gutzufusss.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import gutzufusss.util.Logger;

import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;

import javax.swing.border.TitledBorder;
import javax.swing.DefaultComboBoxModel;
import java.awt.Component;
import javax.swing.JRadioButton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class GUIView extends JFrame {
	public JTextField dirPathTF;
	
	private Logger logger;
	private GUIController guiCtrl;

	public GUIView(Logger logger, GUIController guiCtrl) {
		this.logger = logger;
		this.guiCtrl = guiCtrl;

		setUpLookAndFeel();
		initializeGUI();

		this.setVisible(true);

		logger.log(Logger.LVL_INFO, "GUI initialization worked fine (\"GUIModel->GUIController->GUIView\" chain).");
	}

	private void initializeGUI() {
		getContentPane().setFont(new Font("Monospaced", Font.PLAIN, 11));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ImageFinder");
		setResizable(false);
		setSize(new Dimension(980, 530));
		getContentPane().setSize(new Dimension(980, 530));
		setLocation(520, 300);
		getContentPane().setLayout(null);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(guiCtrl);
		btnBrowse.setBounds(290, 62, 95, 25);
		btnBrowse.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(btnBrowse);

		JButton btnStartScanning = new JButton("Start scanning");
		btnStartScanning.addActionListener(guiCtrl);
		btnStartScanning.setBounds(9, 62, 145, 25);
		btnStartScanning.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(btnStartScanning);

		JLabel lblTypeThePath = new JLabel("Type a path or click the \"Browse...\" button below.");
		lblTypeThePath.setBounds(12, 0, 334, 30);
		lblTypeThePath.setForeground(new Color(255, 255, 255));
		lblTypeThePath.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTypeThePath.setAutoscrolls(true);
		getContentPane().add(lblTypeThePath);

		dirPathTF = new JTextField();
		dirPathTF.setBounds(9, 31, 376, 22);
		getContentPane().add(dirPathTF);
		dirPathTF.setText(System.getProperty("user.home") + "\\Pictures"); // preset the tf to something nice
		dirPathTF.setColumns(10);

		JList<String> list = new JList<String>(logger.guiLogStream);
		list.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		list.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(9, 260, 955, 230);
		getContentPane().add(scrollPane);
		
		// auto scroll code, works in loop
		/*scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getValue());  
	        }
	    });*/
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 396, 98);
		panel.setBackground(Color.BLACK);
		getContentPane().add(panel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(1, 242, 973, 260);
		panel_2.setToolTipText("");
		panel_2.setForeground(Color.BLACK);
		panel_2.setName("Logger");
		panel_2.setBackground(new Color(255, 255, 0));
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logger", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(211, 211, 211)));
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(406, 0, 287, 234);
		getContentPane().add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Misc", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblLoggingLvl = new JLabel("Logging LvL:");
		lblLoggingLvl.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLoggingLvl.setBounds(4, 11, 73, 14);
		panel_1.add(lblLoggingLvl);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(guiCtrl);
		comboBox.setMaximumRowCount(6);
		String[] logLvls = new String[Logger.LVL_DEBUG + 1];
		for(int i = 0; i <= Logger.LVL_DEBUG; i++)
			logLvls[i] = Integer.toString(i) + ":" + logger.getErrLvlString(i);
		comboBox.setModel(new DefaultComboBoxModel(logLvls));
		comboBox.setSelectedIndex(guiCtrl.getConfig().curConf.logLevel);
		comboBox.setName("loggingLevel");
		comboBox.setToolTipText("The lower the number the less logging messages you will get.");
		comboBox.setBounds(79, 8, 73, 20);
		panel_1.add(comboBox);
		
		JLabel lblDebug = new JLabel("Debugging:");
		lblDebug.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDebug.setBounds(4, 41, 73, 14);
		panel_1.add(lblDebug);
		
		JRadioButton rdbtnOn = new JRadioButton("Activated");
		rdbtnOn.addItemListener(guiCtrl);
		rdbtnOn.setEnabled(guiCtrl.getConfig().curConf.debug);
		rdbtnOn.setName("debuggingActive");
		rdbtnOn.setBounds(74, 37, 78, 23);
		panel_1.add(rdbtnOn);
		
		JLabel lblNewLabel = new JLabel("Hint: Some things have tooltips. It is sometimes worth it to hover over");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(9, 98, 383, 15);
		getContentPane().add(lblNewLabel);
		
		JLabel lblCertainItemsYou = new JLabel("certain items you may not understand.");
		lblCertainItemsYou.setForeground(new Color(255, 255, 255));
		lblCertainItemsYou.setBounds(9, 111, 287, 14);
		getContentPane().add(lblCertainItemsYou);
	}

	private void setUpLookAndFeel() {
		getContentPane().setBackground(Color.DARK_GRAY);
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			logger.log(Logger.LVL_FATAL, "Failed to initialize GUI (CLAF): " + e.getMessage());
		}
	}
}
