package gutzufusss.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gutzufusss.gui.*;
import gutzufusss.util.Logger;

public class GUIController implements ActionListener {
	private GUIView view;
	private GUIModel model;
	
	private Logger logger;
	
	public GUIController(Logger logger, GUIModel model) {
		this.logger = logger;
		this.model = model;
		view = new GUIView(logger, this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
