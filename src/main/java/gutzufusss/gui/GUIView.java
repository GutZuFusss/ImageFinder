package gutzufusss.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteAquaSkin;
import org.pushingpixels.substance.api.skin.GraphiteSkin;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import com.alee.laf.WebLookAndFeel;

import javax.swing.JScrollBar;
import java.awt.Dimension;
import java.awt.Color;

@SuppressWarnings("serial")
public class GUIView extends JFrame {

	public GUIView() {
		setUpLookAndFeel();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ImageFinder");
		setResizable(false);
		setSize(new Dimension(980, 530));
		getContentPane().setSize(new Dimension(980, 530));
		getContentPane().setLayout(null);

		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(931, 260, 17, 215);
		getContentPane().add(scrollBar);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(290, 62, 95, 25);
		getContentPane().add(btnBrowse);

		JButton btnStartScanning = new JButton("Start scanning");
		btnStartScanning.setBounds(9, 62, 145, 25);
		getContentPane().add(btnStartScanning);

		JLabel lblTypeThePath = new JLabel("Type a path or click the \"Browse...\" button below.");
		lblTypeThePath.setAutoscrolls(true);
		lblTypeThePath.setBounds(12, 0, 456, 30);
		getContentPane().add(lblTypeThePath);

		JTextField textField = new JTextField();
		textField.setBounds(9, 31, 376, 22);
		getContentPane().add(textField);
		textField.setColumns(10);

		JList<String> list = new JList<String>();
		list.setBounds(17, 260, 931, 215);
		getContentPane().add(list);

		initializeGUI();
		this.setVisible(true);
	}

	private void initializeGUI() {
	}

	private void setUpLookAndFeel() {
		getContentPane().setBackground(Color.DARK_GRAY);
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		} catch (Exception e) {
			System.out.println("Substance Graphite failed to initialize");
		}
	}
}
