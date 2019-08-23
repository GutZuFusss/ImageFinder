package gutzufusss.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class GUIView extends JFrame {
	private JLabel testLabel; 
	private JButton testButton;

	public GUIView() {
		initializeGUI();
		this.setVisible(true);
	}
	
	private void initializeGUI() {
		this.setBounds(900, 500, 700, 500);
		testLabel = new JLabel("SCHNAPS NUR AUF EIS, NUR VON BLUNTS WERD ICH BREIT");
		testLabel.setHorizontalAlignment(JLabel.CENTER);
        testButton = new JButton("Heyaaa");
        testButton.addActionListener(new GUIController(this));
        this.add(testLabel, BorderLayout.NORTH);
        this.add(testButton, BorderLayout.CENTER);
        this.pack();
        this.setTitle("MVC");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
