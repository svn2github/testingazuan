package it.eng.spagobi.plugins.ireport.gui;

import it.businesslogic.ireport.util.I18n;
import it.eng.spagobi.plugins.ireport.Plugin;
import it.eng.spagobi.plugins.ireport.PluginResourcesManager;
import it.eng.spagobi.plugins.ireport.ServerRequest;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;

import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;


public class LoginFrame extends JDialog implements ActionListener, KeyListener {

	private JPanel mainPane = null;
	private JButton submitButton = null;
	private JButton cancelButton = null;
	private JTextField usernameTextField = null;
	private JLabel usernameLabel = null;
	private JLabel passwordLabel = null;
	private JPasswordField passwordPasswordField = null;

	private static LoginFrame mainInstance;
	private PluginResourcesManager resourcesManager= null;
	

	/**
	 * This method returns the unique instance of the LoginFrame and creates it when it's called for the first time;  
	 * 
	 */
	public static LoginFrame getMainInstance(){
		
		if (mainInstance==null) {
			
			new LoginFrame();
			
		}
		 
		return mainInstance;
		
	}	

	/**
	 * This is the default constructor
	 */
	private LoginFrame() {
		super(PluginResourcesManager.getMainInstance().getMainFrame()); 
		
		resourcesManager = PluginResourcesManager.getMainInstance();
		mainInstance = this;
	
		createAndShowGUI();
		   	
	}

	/**
	 * 
	 * This method creates and shows the GUI    
	 *
	 */
	private void createAndShowGUI() {
		
		this.setSize(400, 210);
		this.setResizable(false);
		this.setContentPane(getMainPane());
		this.setTitle("SpagoBI - " + resourcesManager.getLanguageResource("LoginTitle"));
		
						
	}
	
	/* The following methods create and return the JPanel and all the widgets needed to 
	 * realize the JDialog with the following structure:
	 *
	 * mainPane
	 * 		usernameLabel
	 * 		usernameTextField
	 * 		passwordLabel
	 * 		passwordPasswordField
	 * 		submitButton
	 * 		cancelButton
	 * 
	 */ 
	
	private JLabel getUsernameLabel(){
		
		if (usernameLabel == null) {
			
			usernameLabel = new JLabel(resourcesManager.getLanguageResource("usernameLabel"),JLabel.CENTER);
			usernameLabel.setLocation(new java.awt.Point(20,30));
			usernameLabel.setSize(new java.awt.Dimension(100,30));
			usernameLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				
		}

		return usernameLabel;
		
	}
	
	private JTextField getUsernameTextField(){
		
		if (usernameTextField==null) {
			
			usernameTextField = new JTextField();
			usernameTextField.setLocation(new java.awt.Point(130,30));
			usernameTextField.setSize(new java.awt.Dimension(240,30));
			Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			usernameTextField.setBorder(new CompoundBorder(outer, inner));
			
		}

		return usernameTextField;
		
	}
	
	private JLabel getPasswordLabel(){
		
		if (passwordLabel==null) {
			
			passwordLabel = new JLabel(resourcesManager.getLanguageResource("passwordLabel"),JLabel.CENTER);
			passwordLabel.setLocation(new java.awt.Point(20,70));
			passwordLabel.setSize(new java.awt.Dimension(100,30));
			passwordLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));			
		}

		return passwordLabel;
		
	}
	
	private JTextField getPasswordPasswordField(){
		
		if (passwordPasswordField==null) {

			passwordPasswordField = new JPasswordField();
			passwordPasswordField.setLocation(new java.awt.Point(130,70));
			passwordPasswordField.setSize(new java.awt.Dimension(240,30));
			Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			passwordPasswordField.setBorder(new CompoundBorder(outer, inner));
		}

		return passwordPasswordField;
		
	}
	
	private JButton getSubmitButton(){
		
		if (submitButton==null) {
			
			submitButton = new JButton();
			submitButton.setText(resourcesManager.getLanguageResource("submitButton"));
			submitButton.setLocation(new Point(60,130));
			submitButton.setSize(Plugin.buttonsDimensions);
			submitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			submitButton.addKeyListener(this);
			submitButton.addActionListener(this);
			submitButton.setActionCommand("submit");
			submitButton.registerKeyboardAction(this,"submit",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
		}

		return submitButton;
		
	}
	
	private JButton getCancelButton(){
		
		if (cancelButton==null) {
			
			cancelButton= new JButton();
			
			cancelButton.setText(resourcesManager.getLanguageResource("cancelButton"));
			cancelButton.setLocation(new Point(200,130));
			cancelButton.setSize(Plugin.buttonsDimensions);
			cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
			cancelButton.addKeyListener(this);
			cancelButton.addActionListener(this);
			cancelButton.setActionCommand("cancel");
			
			cancelButton.registerKeyboardAction(this,"cancel",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
			cancelButton.registerKeyboardAction(this,"cancel",KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}

		return cancelButton;
		
	}


	private JPanel getMainPane(){
		
		if (mainPane==null) {
			
			mainPane = new JPanel();
			mainPane.setLayout(null);
			
			mainPane.add(getSubmitButton(), null);
			mainPane.add(getCancelButton(), null);
			mainPane.add(getUsernameTextField(), null);
			mainPane.add(getUsernameLabel(), null);
			mainPane.add(getPasswordLabel(), null);
			mainPane.add(getPasswordPasswordField(), null);	
					
		}

		return mainPane;
		
	}
		
	/**
	 * 
	 * This method is called when the Submit Button is pressed.
	 * It calls the ServerRequest main instance to execute a LOGIN operation 
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		if ("cancel".equals(action)){

			getCancelButton().setBorder(BorderFactory.createRaisedBevelBorder());
			this.setVisible(false);
			
		} else if ("submit".equals(action)){

			Properties properties = new Properties();
			properties.put("operation","LOGIN");
			properties.put("username",usernameTextField.getText());
			properties.put("password",passwordPasswordField.getText());
			ServerRequest.getMainInstance().executeServerRequest(properties);
		}
		
	}
	
	
	
	/**
	 * This method is used to hide the Login Frame and clear the username and password TextFields 
	 */
	public void clearFrame(){
		
		mainInstance.passwordPasswordField.setText("");
		mainInstance.usernameTextField.setText("");
		mainInstance.setVisible(false);
		
	}
	
	/**
	 * This method refreshes the text on labels and buttons: it's called when the language has been changed 
	 */	
    public void updateWidgetsText(){
    	
          getSubmitButton().setText(resourcesManager.getLanguageResource("submitButton"));
    	  getCancelButton().setText(resourcesManager.getLanguageResource("cancelButton")); 
    	  getUsernameLabel().setText(resourcesManager.getLanguageResource("usernameLabel"));
    	  getPasswordLabel().setText(resourcesManager.getLanguageResource("passwordLabel")) ;
      	
      }
    
    public void showFrame(){

    	this.setVisible(true);
    	getSubmitButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getCancelButton().setBorder(BorderFactory.createRaisedBevelBorder());
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyChar()==KeyEvent.VK_ENTER)){
			((JButton)e.getComponent()).setBorder(BorderFactory.createLoweredBevelBorder());
			
			
		}
	    	
		
		
	}

	public void keyReleased(KeyEvent e) {
		getSubmitButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getCancelButton().setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
    
    
}

