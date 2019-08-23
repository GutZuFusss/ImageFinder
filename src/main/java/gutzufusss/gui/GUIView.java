package gutzufusss.gui;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import gutzufusss.Main;

import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class GUIView extends JFrame {
	private Main controller;

	public GUIView(Main m) {
		controller = m;
		setUpLookAndFeel();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ImageFinder");
		setResizable(false);
		setSize(new Dimension(980, 530));
		getContentPane().setSize(new Dimension(980, 530));
		getContentPane().setLayout(null);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBrowse.setBounds(290, 62, 95, 25);
		getContentPane().add(btnBrowse);

		JButton btnStartScanning = new JButton("Start scanning");
		btnStartScanning.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStartScanning.setBounds(9, 62, 145, 25);
		getContentPane().add(btnStartScanning);

		JLabel lblTypeThePath = new JLabel("Type a path or click the \"Browse...\" button below.");
		lblTypeThePath.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTypeThePath.setAutoscrolls(true);
		lblTypeThePath.setBounds(12, 0, 456, 30);
		getContentPane().add(lblTypeThePath);

		JTextField textField = new JTextField();
		textField.setBounds(9, 31, 376, 22);
		getContentPane().add(textField);
		textField.setColumns(10);

		JList<String> list = new JList<String>(controller.getLogger().guiLogStream);
		list.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(9, 260, 955, 230);
		getContentPane().add(scrollPane);
		//getContentPane().add(list);

		initializeGUI();
		this.setVisible(true);
	}

	private void initializeGUI() {
	}

	private void setUpLookAndFeel() {
		getContentPane().setBackground(Color.DARK_GRAY);
		JFrame.setDefaultLookAndFeelDecorated(false);
		try {
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		} catch (Exception e) {
			System.out.println("Substance Graphite failed to initialize");
		}
	}
}
