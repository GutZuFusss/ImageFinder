package gutzufusss.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gutzufusss.gui.*;

public class GUIController implements ActionListener {
	private GUIView view;
	private GUIModel model;
	
	public GUIController(GUIView view) {
		this.view = view;
		model = new GUIModel();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
