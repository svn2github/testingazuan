/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.security;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;


/**
 * Graphical Interface useful to produce a Public/Private Keys pair. (DSA Alghoritm, 1024 bits)
 */
public class KeyGenerator extends JDialog implements ActionListener, KeyListener {
		
	private JPanel mainPane = null;	
	private JButton generateKeyButton = null;
	private JButton abortButton = null;
	private JButton folderChooserButton = null;
	private JTextField keyNameTextField = null;
	private JTextField parentFolderTextField = null;
	private JLabel keyNameLabel = null;
	private JLabel parentFolderLabel = null;
	private JFileChooser fileChooser = null;
	private Dimension buttonDimension = new Dimension(100,40);
	
	/**
	 * Start point
	 * @param args Eventual execution arguments
	 */
	public static void main (String[] args) {
		setLookAndFeel();
		KeyGenerator kg = new KeyGenerator();
	}
	
	
	/**
	 * Constructor
	 */
	public KeyGenerator() {
		createAndShowGUI();
	}
	
	/**
	 * Initializes and shows the Gtaphical Interface
	 */
	private void createAndShowGUI(){
		setSize(640, 210);
		setResizable(false);
		setContentPane(getMainPane());
		setTitle("SpagoBI - Keys Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Set the look and feel of the Graphical Inteface
	 */
	static private void setLookAndFeel(){
    	LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();
       	try {
			UIManager.setLookAndFeel(lafi[0].getClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (ClassNotFoundException e) {
			System.out.println("SpagoBI Keys Generator - " + e.toString());
		} catch (InstantiationException e) {
			System.out.println("SpagoBI Keys Generator - " + e.toString());
		} catch (IllegalAccessException e) {
			System.out.println("SpagoBI Keys Generator - " + e.toString());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("SpagoBI Keys Generator - " + e.toString());
		}
    }
    
	
	/**
	 * Creates the text field for the key name
	 * @return JTextField, the text field for the key name
	 */
	private JTextField getKeyNameTextField(){
		if ( keyNameTextField == null) {
	 		keyNameTextField = new JTextField();
			keyNameTextField.setLocation(new java.awt.Point(210,30));
			keyNameTextField.setSize(new java.awt.Dimension(350,30));
			Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			keyNameTextField.setBorder(new CompoundBorder(outer, inner));
		}
		return keyNameTextField;
	}
	
	
	
	/**
	 * Creates the text filed for the path of the parent directory
	 * @return JTextField, the text filed for the path of the parent directory
	 */
	private JTextField getParentFolderTextField(){
		if ( parentFolderTextField == null) {
			parentFolderTextField = new JTextField();
			parentFolderTextField.setLocation(new java.awt.Point(210,70));
			parentFolderTextField.setSize(new java.awt.Dimension(350,30));
			Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			parentFolderTextField.setBorder(new CompoundBorder(outer, inner));
		}
		return parentFolderTextField;
	}

	
	
	/**
	 * Creates the label for the name field
	 * @return JLabel, the label for the name field
	 */
	private JLabel getKeyNameLabel() {
		if (keyNameLabel == null) {
			keyNameLabel = new JLabel("Key Prefix",JLabel.CENTER);
			keyNameLabel.setLocation(new java.awt.Point(20, 30));
			keyNameLabel.setSize(new java.awt.Dimension(170, 30));
			keyNameLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return keyNameLabel;
	}

	
	/**
	 * Creates the label for the path parent field
	 * @return JLabel, the label for the path parent field
	 */
	private JLabel getParentFolderLabel() {
		if (parentFolderLabel == null) {
			parentFolderLabel = new JLabel("Save Folder",JLabel.CENTER);
			parentFolderLabel.setLocation(new java.awt.Point(20, 70));
			parentFolderLabel.setSize(new java.awt.Dimension(170, 30));
			parentFolderLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return parentFolderLabel;
	}

	
	/**
	 * Creates the button for the choice of the parent folder 
	 * @return JButton, the button for the choice of the parent folder 
	 */
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
	
	
	/**
	 * Creates the button for the generation of the key pair 
	 * @return JButton, the button for the generation of the key pair 
	 */
	private JButton getGenerateKeyButton() {
		if (generateKeyButton == null) {
			generateKeyButton = new JButton();
			generateKeyButton.setText("Generate Key");
			generateKeyButton.setActionCommand("GenerateKey");
			generateKeyButton.addKeyListener(this);
			generateKeyButton.addActionListener(this);
			generateKeyButton.setLocation(new Point(180,130));
			generateKeyButton.setSize(buttonDimension);
			generateKeyButton.setBorder(BorderFactory.createRaisedBevelBorder());
			generateKeyButton.registerKeyboardAction(this,"GenerateKey",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
		}
		return generateKeyButton;
	}

	
	/**
	 * Creates the button for abort operation
	 * @return JButton, the button for abort operation 
	 */
	private JButton getAbortButton() {
		if (abortButton == null) {
			abortButton = new JButton();
			abortButton.setText("Abort");
			abortButton.setActionCommand("Abort");
			abortButton.addKeyListener(this);
			abortButton.addActionListener(this);
			abortButton.setLocation(new Point(360,130));
			abortButton.setSize(buttonDimension);
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.registerKeyboardAction(this,"Abort",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
			abortButton.registerKeyboardAction(this,"Abort",KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}
		return abortButton;
	}

	
	
	/**
	 * Creates the File chooser for the selection of the parent folder
	 * @return JFileChooser, the File chooser for the selection of the parent folder
	 */
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
		    fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return fileChooser;
	}
	

	
	/**
	 * Creates the main Panel of the Interface
	 * @return JPanel, the main Panel of the Interface
	 */
	private JPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new JPanel();
			mainPane.setLayout(null);
			mainPane.add(getParentFolderLabel(), null);
			mainPane.add(getKeyNameLabel(), null);
			mainPane.add(getGenerateKeyButton(), null);
			mainPane.add(getAbortButton(), null);
			mainPane.add(getFolderChooserButton(), null);
			mainPane.add(getKeyNameTextField(), null);
			mainPane.add(getParentFolderTextField(), null);
		}
		return mainPane;
	}
	
	
	/**
	 * Manages the action performed by interface components
	 */
	public void actionPerformed(ActionEvent e) {	
		if (e.getActionCommand().equals("Abort")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("GenerateKey")) {
			if (generateKey()) System.exit(0);				
		} else if (e.getActionCommand().equals("FolderChooser")){
            int returnVal = getFileChooser().showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = getFileChooser().getSelectedFile();
                getParentFolderTextField().setText(file.getPath());
            } 
		}
	}
	

    /**
     * Manages the key pressed event
     */
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyChar()==KeyEvent.VK_ENTER))
	    	 ((JButton)e.getComponent()).setBorder(BorderFactory.createLoweredBevelBorder());
	}

	
	/**
	 * Manages the key released event
	 */
	public void keyReleased(KeyEvent e) {
		getGenerateKeyButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getAbortButton().setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	
	/**
	 * Manages the key typed event
	 */
	public void keyTyped(KeyEvent e) {
	}
	
	
	/**
	 * Generate the key pair and put it into the save folder chosen by the user 
	 * @return boolean, operation executed or not
	 */
	private boolean generateKey(){
        String folder = this.getParentFolderTextField().getText();
	    try {
	        if ((folder.equals(""))&&(this.getKeyNameTextField().getText().equals(""))) {
	        	javax.swing.JOptionPane.showMessageDialog(this, "Key Name and Parent Folder required");
	        	return false;
	        } else if (folder.equals("")) {
	        	javax.swing.JOptionPane.showMessageDialog(this, "Parent Folder required");
	        	return false;
	        } else if (this.getKeyNameTextField().getText().equals("")){
	        	javax.swing.JOptionPane.showMessageDialog(this, "Key Name required");
	        	return false;
	        }
	        String algorithm = "DSA";  // or RSA, DH, etc.
	        // Generate a 1024-bit Digital Signature Algorithm (DSA) key pair
	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
	        keyGen.initialize(1024);
	        KeyPair keypair = keyGen.genKeyPair();
	        PrivateKey privateKey = keypair.getPrivate();
	        PublicKey publicKey = keypair.getPublic();
	        // Get the bytes of the public and private keys
	        byte[] privateKeyBytes = privateKey.getEncoded();
	        byte[] publicKeyBytes = publicKey.getEncoded();
	        String privateKeyFileName = this.getKeyNameTextField().getText() + "_privatekey";
	        FileOutputStream fos = new FileOutputStream(new File(folder+File.separator+privateKeyFileName));
	        fos.write(privateKeyBytes);	        
	        fos.flush();
	        fos.close();
	        String publicKeyFileName = this.getKeyNameTextField().getText() + "_publickey";        
	        fos = new FileOutputStream(new File(folder+File.separator+publicKeyFileName));
	        fos.write(publicKeyBytes);	        
	        fos.flush();
	        fos.close();  
	        javax.swing.JOptionPane.showMessageDialog(this, "done");
	        return true;	        
	    } catch (NoSuchAlgorithmException e) {
	    	javax.swing.JOptionPane.showMessageDialog(this, e.toString());
	    	return false;
	    } catch (IOException e) {
	    	javax.swing.JOptionPane.showMessageDialog(this, e.toString());
	    	return false;
		}
	}
	
}

