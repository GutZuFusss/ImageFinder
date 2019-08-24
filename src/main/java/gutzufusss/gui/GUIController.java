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
import javax.swing.JList;
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
			model.updateDebuggingActive(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flGrayscale"))
			model.updateFlGrayscale(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flBinary"))
			model.updateFlBinary(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flSmooth"))
			model.updateFlSmooth(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flBorder"))
			model.updateFlBorder(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flSWT"))
			model.updateFlSWT(e.getStateChange() == 1);
		else if(e.getSource().toString().contains("flContrast"))
			model.updateFlContrast(e.getStateChange() == 1);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().toString().contains("critConf"))
			model.updateCritConf((int)((JSpinner)e.getSource()).getValue());
	}

	public void handleAutoScroll() { view.listLog.ensureIndexIsVisible(view.listLog.getModel().getSize() - 1); }

	public void setDirPath(String p) { view.dirPathTF.setText(p); }

	public Config getConfig() { return model.getConfig(); }
}
