package gutzufusss.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

import gutzufusss.gui.*;
import gutzufusss.util.Config;
import gutzufusss.util.Logger;

public class GUIController implements ActionListener, ItemListener, ChangeListener {
	private GUIView view;
	private GUIModel model;
	
	private Logger logger;
	
	public GUIController(Logger logger, GUIModel model) {
		this.logger = logger;
		this.model = model;
		view = new GUIView(logger, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "Browse...":
			model.userBrowsePath();
			break;
		case "Start scanning":
			model.startScanning(view.dirPathTF.getText());
			break;
		/*case LVL_ERROR:
			break;*/
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().toString().contains("loggingLevel") && e.getStateChange() == ItemEvent.SELECTED)
			model.updateLogLevel(e.getItem().toString());
		else if(e.getSource().toString().contains("debuggingActive"))
			model.updateDebuggingActive();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().toString().contains("critConf"))
			model.updateCritConf((JSpinner)e.getSource()).getValue());
	}

	public void setDirPath(String p) { view.dirPathTF.setText(p); }

	public Config getConfig() { return model.getConfig(); }
}
