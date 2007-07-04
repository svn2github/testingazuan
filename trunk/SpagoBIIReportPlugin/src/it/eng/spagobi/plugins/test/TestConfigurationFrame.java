/**
 * This file is released under the LGPL license
 * See the LICENSE_SpagoBI-IReportPlugin.txt file for the complete text of the LGPL license
 * 
 */
package it.eng.spagobi.plugins.test;

import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.JButton;

import it.eng.spagobi.plugins.ireport.Plugin;
import it.eng.spagobi.plugins.ireport.gui.ConfigurationFrame;
import it.eng.spagobi.plugins.ireport.gui.LoginFrame;
import junit.framework.TestCase;

public class TestConfigurationFrame extends TestCase {

	
	private ConfigurationFrame configurationFrame = null;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		configurationFrame = new ConfigurationFrame();
		new Plugin();
		
		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		LoginFrame.getMainInstance().setVisible(false);
		configurationFrame.setVisible(false);
		
	}
	/*
	 * Test method for 'it.eng.spagobi.plugins.ireport.gui.ConfigurationFrame.actionPerformed(ActionEvent)'
	 */
	public void testActionPerformed_AbortOn_Call() {
				
		ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "Abort");
		configurationFrame.setAction("call");
		
		configurationFrame.actionPerformed(actionEvent);
		// visualizza il messaggio di avviso per dire che server configurare prima
		assertFalse(configurationFrame.isVisible());
		
		
	}
	
	public void testActionPerformed_ConfigOn_Call() {

		ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "Config");
		configurationFrame.setAction("call");
		configurationFrame.setTextFields("test","test");
		configurationFrame.setVisible(true);
		
		configurationFrame.actionPerformed(actionEvent);
		assertFalse(configurationFrame.isVisible());
		assertTrue(LoginFrame.getMainInstance().isVisible());
		
		
		
	}
	
	public void testActionPerformed_AbortOn_Configure() {

		ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "Abort");
		configurationFrame.setAction("configure");
				
		configurationFrame.actionPerformed(actionEvent);
		assertFalse(configurationFrame.isVisible());
		
	}
	
	public void testActionPerformed_ConfigOn_Configure() {

		ActionEvent actionEvent = new ActionEvent(new JButton(), 1, "Config");
		configurationFrame.setAction("configure");
		configurationFrame.setTextFields("test","test");
		
		configurationFrame.actionPerformed(actionEvent);
		assertFalse(configurationFrame.isVisible());

		
	}

}
