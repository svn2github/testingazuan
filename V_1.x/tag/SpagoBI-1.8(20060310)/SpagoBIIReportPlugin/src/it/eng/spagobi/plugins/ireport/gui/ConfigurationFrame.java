package it.eng.spagobi.plugins.ireport.gui;


import it.businesslogic.ireport.util.I18n;
import it.eng.spagobi.plugins.ireport.Plugin;
import it.eng.spagobi.plugins.ireport.PluginResourcesManager;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;


public class ConfigurationFrame extends JDialog implements ActionListener, KeyListener {

	private static ConfigurationFrame mainInstance ;
	
	private String action="";
		
	private JPanel mainPane = null;
	
	private JButton configButton = null;
	private JButton abortButton = null;
	private JButton folderChooserButton = null;
	
	private JTextField serverLocationTextField = null;
	private JTextField tempFolderTextField = null;
	private JLabel serverLocationLabel = null;
	private JLabel tempFolderLabel = null;
	
	private JFileChooser fileChooser = null;
	
	private PluginResourcesManager resourcesManager= null;
	
	
	/**
	 * This method returns the unique instance of the LoginFrame and creates it when it's called for the first time;  
	 * 
	 */
	public static ConfigurationFrame getMainInstance() {
		
		if (mainInstance==null) {
			
			new ConfigurationFrame();
			
		}
		
		return mainInstance;
		
	}
	
	
	public ConfigurationFrame() {
		super(PluginResourcesManager.getMainInstance().getMainFrame());
		
		resourcesManager = PluginResourcesManager.getMainInstance();
		mainInstance = this;
		
		createAndShowGUI();
		
	}
	
	/**
	 * This method updates the serverLocation and the tempFoder JTextFields
	 * 
	 * @param serverLocation
	 * @param tempFolder
	 */
	public void setTextFields(String serverLocation, String tempFolder) {
		
		this.serverLocationTextField.setText(serverLocation);
		this.tempFolderTextField.setText(tempFolder);
		
	}
	
	public void setAction(String action) {
		
		this.action = action;
		
	}
	
	
	private void createAndShowGUI(){
		
		setSize(640, 210);
		setResizable(false);
		setContentPane(getMainPane());
		setTitle("SpagoBI - " + resourcesManager.getLanguageResource("ConfigurationTitle"));
	    
	}
	
	/* The following methods create and return the JPanel and all the widgets needed to 
	 * realize the JDialog with the following structure:
	 *
	 * mainPane
	 * 		serverLocationLabel
	 * 		serverLocationTextField
	 * 		tempFolderLabel
	 * 		tempFolderField
	 * 		configButton
	 * 		abortButton
	 * 		fileChooser
	 * 
	 */ 
	
	
	private JTextField getServerLocationTextField(){
	
		if ( serverLocationTextField == null) {
 
		serverLocationTextField = new JTextField();
		serverLocationTextField.setLocation(new java.awt.Point(210,30));
		serverLocationTextField.setSize(new java.awt.Dimension(350,30));
		Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
		serverLocationTextField.setBorder(new CompoundBorder(outer, inner));
		}
	
		return serverLocationTextField;
	
	}
	
	private JTextField getTempFolderTextField(){
	
		if ( tempFolderTextField == null) {
 
		tempFolderTextField = new JTextField();
		tempFolderTextField.setLocation(new java.awt.Point(210,70));
		tempFolderTextField.setSize(new java.awt.Dimension(350,30));
		Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
		tempFolderTextField.setBorder(new CompoundBorder(outer, inner));
		}
	
	return tempFolderTextField;
	
	}

	private JLabel getServerLocationLabel() {

		if (serverLocationLabel == null) {

			serverLocationLabel = new JLabel(resourcesManager.getLanguageResource("serverLocationLabel"),JLabel.CENTER);
			serverLocationLabel.setLocation(new java.awt.Point(20, 30));
			serverLocationLabel.setSize(new java.awt.Dimension(170, 30));
			serverLocationLabel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));

		}

		return serverLocationLabel;

	}

	private JLabel getTempFolderLabel() {
		if (tempFolderLabel == null) {

			tempFolderLabel = new JLabel(resourcesManager.getLanguageResource("tempFolderLabel"),JLabel.CENTER);
			tempFolderLabel.setLocation(new java.awt.Point(20, 70));
			tempFolderLabel.setSize(new java.awt.Dimension(170, 30));
			tempFolderLabel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));

		}

		return tempFolderLabel;

	}

	private JButton getFolderChooserButton() {
		
		if (folderChooserButton == null) {

			folderChooserButton = new JButton();
			folderChooserButton.setText("...");
			folderChooserButton.addActionListener(this);
			folderChooserButton.setActionCommand("FolderChooser");
			folderChooserButton.setLocation(new Point(580,70));
			folderChooserButton.setSize(30,30);
			folderChooserButton.addActionListener(this);
			folderChooserButton.addKeyListener(this);
			folderChooserButton.setBorder(BorderFactory.createRaisedBevelBorder());
			folderChooserButton.registerKeyboardAction(this,"FolderChooser",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
		}

		return folderChooserButton;

	}
	
	private JButton getConfigButton() {
		if (configButton == null) {

			configButton = new JButton();
			configButton.setText(resourcesManager.getLanguageResource("configButton"));
			configButton.setActionCommand("Config");
			configButton.addKeyListener(this);
			configButton.addActionListener(this);
			configButton.setLocation(new Point(120,130));
			configButton.setSize(Plugin.buttonsDimensions);
			configButton.setBorder(BorderFactory.createRaisedBevelBorder());
			configButton.registerKeyboardAction(this,"Config",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
		}

		return configButton;

	}

	private JButton getAbortButton() {
		
		if (abortButton == null) {

			abortButton = new JButton();
			abortButton.setText(resourcesManager.getLanguageResource("abortButton"));
			abortButton.setActionCommand("Abort");
			abortButton.addKeyListener(this);
			abortButton.addActionListener(this);
			abortButton.setLocation(new Point(300,130));
			abortButton.setSize(Plugin.buttonsDimensions);
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.registerKeyboardAction(this,"Abort",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
			abortButton.registerKeyboardAction(this,"Abort",KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}

		return abortButton;

	}

	
	private JFileChooser getFileChooser() {
		
		if (fileChooser == null) {

		    fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		}

		return fileChooser;

	}
	

	private JPanel getMainPane() {
		if (mainPane == null) {
			
			mainPane = new JPanel();
			mainPane.setLayout(null);
					
			mainPane.add(getConfigButton(), null);
			mainPane.add(getAbortButton(), null);
			mainPane.add(getTempFolderLabel(), null);
			mainPane.add(getServerLocationLabel(), null);
			mainPane.add(getFolderChooserButton(), null);
			
			mainPane.add(getServerLocationTextField(), null);
			mainPane.add(getTempFolderTextField(), null);
		
		}

		return mainPane;

	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().equals("Abort")) {
			
			if (action.equals("call")){
				
				javax.swing.JOptionPane.showMessageDialog(mainInstance, resourcesManager.getLanguageResource("ConfigurationRequired"));
				getAbortButton().setBorder(BorderFactory.createRaisedBevelBorder());
				mainInstance.setVisible(false);
				
			} else if (action.equals("configure")){
				
				getAbortButton().setBorder(BorderFactory.createRaisedBevelBorder());
				mainInstance.setVisible(false);
				
			}
			
			
		} else if (e.getActionCommand().equals("Config")) {

			if (Plugin.getMainInstance().updateConfiguration(serverLocationTextField.getText(), tempFolderTextField.getText())) {
								
				if (action.equals("call")){
					
					getConfigButton().setBorder(BorderFactory.createRaisedBevelBorder());
					mainInstance.setVisible(false);
					
					Plugin.getMainInstance().checkUser();

				
				} else if (action.equals("configure")){
					
					getConfigButton().setBorder(BorderFactory.createRaisedBevelBorder());
					mainInstance.setVisible(false);
					
				}				
			} else javax.swing.JOptionPane.showMessageDialog(getMainPane(), resourcesManager.getLanguageResource("invalideInformation"));

		} else if (e.getActionCommand().equals("FolderChooser")){
			
	            int returnVal = getFileChooser().showOpenDialog(this);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = getFileChooser().getSelectedFile();
	                
	                getTempFolderTextField().setText(file.getPath());
	                
	            } 
		}
				
	}
	
	/**
	 * This method refreshes the text on labels and buttons: it's called when the language has been changed 
	 */	
	public void updateWidgetsText(){
    	
        getConfigButton().setText(resourcesManager.getLanguageResource("configButton"));
        getAbortButton().setText(resourcesManager.getLanguageResource("abortButton")); 
        getServerLocationLabel().setText(resourcesManager.getLanguageResource("serverLocationLabel"));
        getTempFolderLabel().setText(resourcesManager.getLanguageResource("tempFolderLabel")) ;
      	
      }
	
	public void showFrame(){
		
		getConfigButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getAbortButton().setBorder(BorderFactory.createRaisedBevelBorder());
		this.show();
	}
	
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyChar()==KeyEvent.VK_ENTER))
	    	 ((JButton)e.getComponent()).setBorder(BorderFactory.createLoweredBevelBorder());
		
		
	}

	public void keyReleased(KeyEvent e) {
		
		getConfigButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getAbortButton().setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
}

