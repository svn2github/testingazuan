/**
 * This file is released under the LGPL license
 * See the LICENSE_SpagoBI-IReportPlugin.txt file for the complete text of the LGPL license
 * 
 */
package it.eng.spagobi.plugins.ireport.gui;


import it.businesslogic.ireport.util.I18n;
import it.eng.spagobi.plugins.ireport.MyNode;
import it.eng.spagobi.plugins.ireport.Plugin;
import it.eng.spagobi.plugins.ireport.PluginResourcesManager;
import it.eng.spagobi.plugins.ireport.UserProfile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import it.eng.spagobi.plugins.ireport.ServerRequest;

public class TreeFrame extends JDialog implements ActionListener, TreeSelectionListener, TreeExpansionListener, KeyListener {

	  private static TreeFrame mainInstance = null;
	
	  private PluginResourcesManager resourcesManager= null;
	  
	  private JTree tree = null;
	  private DefaultMutableTreeNode rootNode = null;
	  private DefaultTreeModel treeModel = null; 
	  
	  private JPanel buttonsTopPane = null;
	  private JPanel buttonsMiddlePane = null;
	  private JPanel buttonsBottomPane = null;
	  private JPanel verticalButtonsPane = null;
	  private JPanel navigatorButtonsPane = null;
	  private JPanel horizontalButtonsPane = null;
	  private JScrollPane scrollPane = null;
	  private JSplitPane splitPane = null;
	  private JPanel leftBottomSidePane = null;
	  private JPanel leftSidePane = null;
	  private JPanel mainPane = null;
	  
	  private JButton expandAllButton = null;
	  private JButton collapseAllButton = null;
	  private JButton matchButton = null;
	  private JButton checkINButton = null;
	  private JButton checkOUTButton = null;
	  private JButton changeUserButton = null;
	  private JButton hideButton = null;
	  private JLabel currentUserLabel = null;
	  private JTextField addressTextField = null;
	  private JButton addressButton = null;
	  
	  private DefaultMutableTreeNode body = null;
	  
	  
  /**
   * This method returns the mainInstance which is referenced to the 
   * last created instance of the TreeFrame. 
   * If mainInstance is null it will create a new instance of this class
   *	
   */
	    
	public static TreeFrame getMainInstance(){
		
		if (mainInstance==null) {
			
			mainInstance = new TreeFrame();
			
		}

		return mainInstance;
		
	}
	
	public TreeFrame(){
		super(PluginResourcesManager.getMainInstance().getMainFrame());
		
		resourcesManager = PluginResourcesManager.getMainInstance();
		mainInstance = this;

		createAndShowGUI();
        
	}
	
	/**
    * This method creates the GUI and shows it.  
    */
	private void createAndShowGUI() {
            
        setSize(new Dimension(500, 400)); 
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	setContentPane(getMainPane());
		setTitle("SpagoBI - " + resourcesManager.getLanguageResource("DisplayTitle"));
                
    }

	/* The following methods create and return the JPanel and all the widgets needed to 
	 * realize the frame with the following structure:
	 * 
	 * mainPane
	 * 		leftSidePane
	 * 			scrollPane
	 * 				jTree
	 * 			horizontalButtonPane
	 * 				
	 * 		verticalButtonsPane
	 * 			buttonsTopPane
	 * 					checkINButton
	 * 					checkOUTButton
	 * 			buttonsMiddlePane
	 * 					currentUserLabe
	 * 					changeUserButton
	 * 			buttonsBottomPane
	 * 					hideButton
	 */ 
	
	private JButton getCheckINButton(){
		
		if (checkINButton == null) {
			
			// create Check IN and Check Out buttons
        	checkINButton = new JButton(resourcesManager.getLanguageResource("checkINButton"));
        	checkINButton.setActionCommand("Check IN");
        	checkINButton.addKeyListener(this);
        	checkINButton.addActionListener(this);        	
        	checkINButton.setLocation(new Point(10,10));
        	checkINButton.setSize(Plugin.buttonsDimensions);
        	checkINButton.setEnabled(false);
        	checkINButton.setBorder(BorderFactory.createRaisedBevelBorder());
        	checkINButton.registerKeyboardAction(this,"Check IN",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
        	checkINButton.registerKeyboardAction(this,"Check IN",KeyStroke.getKeyStroke(117,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}

		return checkINButton;
		
	}
	
	private JButton getCheckOUTButton(){
		
		if (checkOUTButton == null) {
			
	       	checkOUTButton= new JButton(resourcesManager.getLanguageResource("checkOUTButton"));
        	checkOUTButton.setActionCommand("Check OUT");
        	checkOUTButton.addKeyListener(this);
        	checkOUTButton.addActionListener(this);
        	checkOUTButton.setLocation(new Point(10,60));
        	checkOUTButton.setSize(Plugin.buttonsDimensions);
        	checkOUTButton.setEnabled(false);
        	checkOUTButton.setBorder(BorderFactory.createRaisedBevelBorder());
        	checkOUTButton.registerKeyboardAction(this,"Check OUT",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
        	checkOUTButton.registerKeyboardAction(this,"Check OUT",KeyStroke.getKeyStroke(116,0),JButton.WHEN_IN_FOCUSED_WINDOW);

			
		}

		return checkOUTButton;
		
	}
	
	private JButton getHideButton(){
		
		if (hideButton == null) {
			
			hideButton = new JButton(resourcesManager.getLanguageResource("hideButton"));
			hideButton.setActionCommand("Hide");
			hideButton.addKeyListener(this);
			hideButton.addActionListener(this);
			hideButton.setLocation(new Point(10,10));
			hideButton.setSize(Plugin.buttonsDimensions);
			hideButton.setEnabled(true);
			hideButton.setBorder(BorderFactory.createRaisedBevelBorder());
			hideButton.registerKeyboardAction(this,"Hide",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
			hideButton.registerKeyboardAction(this,"Hide",KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JButton.WHEN_IN_FOCUSED_WINDOW);
			
		}

		return hideButton;
		
	}
	
	private JButton getChangeUserButton(){
		
		if (changeUserButton == null) {
			
        	changeUserButton = new JButton(resourcesManager.getLanguageResource("changeUserButton"));
        	changeUserButton.setActionCommand("Change User");
        	changeUserButton.addKeyListener(this);
        	changeUserButton.addActionListener(this);
        	changeUserButton.setLocation(10,60);
        	changeUserButton.setSize(Plugin.buttonsDimensions);
        	changeUserButton.setBorder(BorderFactory.createRaisedBevelBorder());
        	changeUserButton.registerKeyboardAction(this,"Change User",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
        	changeUserButton.registerKeyboardAction(this,"Change User",KeyStroke.getKeyStroke(112,0),JButton.WHEN_IN_FOCUSED_WINDOW);
			
		}

		return changeUserButton;
		
	}
	
	private JLabel getCurrentUserLabel(){
		
		if (hideButton == null) {
			
        	currentUserLabel = new JLabel(UserProfile.getUsername(),JLabel.CENTER);
        	currentUserLabel.setLocation(new Point(10,10));
        	currentUserLabel.setSize(Plugin.buttonsDimensions);
        
        		TitledBorder outer = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),resourcesManager.getLanguageResource("currentUserLabel"));
        		outer.setTitleJustification(TitledBorder.CENTER);
        		outer.setTitlePosition(TitledBorder.DEFAULT_POSITION);
        
    		Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
    		currentUserLabel.setBorder(new CompoundBorder(outer, inner));
			
		} else {
	
			TitledBorder outer = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),resourcesManager.getLanguageResource("currentUserLabel"));
			outer.setTitleJustification(TitledBorder.CENTER);
			outer.setTitlePosition(TitledBorder.DEFAULT_POSITION);
    
    		Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
    		currentUserLabel.setBorder(new CompoundBorder(outer, inner));
		
		}
			
		return currentUserLabel;
		
	}
	
	
	
	
	private JPanel getButtonsTopPane(){
		
		if (buttonsTopPane == null) {
			
			buttonsTopPane = new JPanel();
	        buttonsTopPane.setLayout(null);
	        buttonsTopPane.setMaximumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	        buttonsTopPane.setMinimumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	        buttonsTopPane.setPreferredSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	        
			buttonsTopPane.add(getCheckINButton());
	        buttonsTopPane.add(getCheckOUTButton());		}

		return buttonsTopPane;
		
	}

	private JButton getMatchButton(){
	
		if (matchButton == null) {
			
			matchButton = new JButton(resourcesManager.getLanguageResource("matchButton"));
	    	matchButton.setActionCommand("Find File");
	    	matchButton.addKeyListener(this);
	    	matchButton.addActionListener(this);
	    	matchButton.setLocation(new Point(10,10));
	    	matchButton.setSize(Plugin.buttonsDimensions);
	    	matchButton.setEnabled(true);
	    	matchButton.setBorder(BorderFactory.createRaisedBevelBorder());
	    	matchButton.registerKeyboardAction(this,"Find File",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
	    	matchButton.registerKeyboardAction(this,"Find File",KeyStroke.getKeyStroke(120,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}

	return matchButton;
	
	}
	
	private JButton getCollapseAllButton(){
		
		if (collapseAllButton== null) {
			
			collapseAllButton = new JButton(resourcesManager.getLanguageResource("collapseAllButton"));
			collapseAllButton.setActionCommand("Collapse All");
        	collapseAllButton.addKeyListener(this);
        	collapseAllButton.addActionListener(this);
        	collapseAllButton.setLocation(new Point(10,10));
        	collapseAllButton.setSize(Plugin.buttonsDimensions);
        	collapseAllButton.setEnabled(false);
        	collapseAllButton.setFocusable(true);
        	collapseAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
        	collapseAllButton.registerKeyboardAction(this,"Collapse All",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
        	collapseAllButton.registerKeyboardAction(this,"Collapse All",KeyStroke.getKeyStroke(109,0),JButton.WHEN_IN_FOCUSED_WINDOW);
		}

		return collapseAllButton;
		
	}

	private JButton getExpandAllButton(){
	
		if (expandAllButton == null) {
			
			expandAllButton = new JButton(resourcesManager.getLanguageResource("expandAllButton"));
			expandAllButton.setActionCommand("Expand All");
			expandAllButton.addKeyListener(this);
			expandAllButton.addActionListener(this);
			expandAllButton.setLocation(new Point(10,10));
			expandAllButton.setSize(Plugin.buttonsDimensions);
	    	expandAllButton.setEnabled(false);
	    	expandAllButton.setFocusable(true);
	    	expandAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
	    	expandAllButton.registerKeyboardAction(this,"Expand All",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
	    	expandAllButton.registerKeyboardAction(this,"Expand All",KeyStroke.getKeyStroke(107,0),JButton.WHEN_IN_FOCUSED_WINDOW);	
		}

	return expandAllButton;
	
	}
	
	private JPanel getButtonsMiddlePane(){
		
		if (buttonsMiddlePane == null) {
			
	       	buttonsMiddlePane = new JPanel();
	       	buttonsMiddlePane.setLayout(null);
	       
	       	buttonsMiddlePane.setMaximumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	       	buttonsMiddlePane.setMinimumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	       	buttonsMiddlePane.setPreferredSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,120));
	                    
	        buttonsMiddlePane.add(getCurrentUserLabel());
	        buttonsMiddlePane.add(getChangeUserButton());
		}

		return buttonsMiddlePane;
		
	}
	
	private JPanel getButtonsBottomPane(){
		
		if (buttonsBottomPane == null) {
			
	        buttonsBottomPane = new JPanel();
	        buttonsBottomPane.setLayout(null);        

	        buttonsBottomPane.setMaximumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,60));
	        buttonsBottomPane.setMinimumSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,60));
	        buttonsBottomPane.setPreferredSize(new Dimension((int)Plugin.buttonsDimensions.getWidth()+20,60));
	        buttonsBottomPane.add(getHideButton());

		}
		return buttonsBottomPane;
	
	}		
     
	private JPanel getVerticalButtonsPane(){
		
		if (verticalButtonsPane == null) {
			
			verticalButtonsPane = new JPanel();
			verticalButtonsPane.setLayout(new BoxLayout(verticalButtonsPane, BoxLayout.Y_AXIS));
			verticalButtonsPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	        
			verticalButtonsPane.add(getButtonsTopPane());
			verticalButtonsPane.add(Box.createVerticalGlue());
	        verticalButtonsPane.add(getButtonsMiddlePane());
	        verticalButtonsPane.add(Box.createVerticalGlue());
	        verticalButtonsPane.add(getButtonsBottomPane());
	    		
		}
		
		return verticalButtonsPane;
		
	}

	private JScrollPane getScrollPane() {

		if (scrollPane == null) {
			
	        scrollPane = new JScrollPane(getJTree());
			scrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	scrollPane.setMinimumSize(new Dimension(100, 200)); 
	    	scrollPane.setPreferredSize(new Dimension(100,200));
			
		}
		
		return scrollPane;

	}			
	

	
	
	private JPanel getHorizontalButtonsPane(){
		
		if (horizontalButtonsPane== null) {
			
			horizontalButtonsPane = new JPanel();
			horizontalButtonsPane.setLayout(new BoxLayout(horizontalButtonsPane, BoxLayout.X_AXIS));
			horizontalButtonsPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			
			horizontalButtonsPane.add(Box.createHorizontalGlue());
			//horizontalButtonsPane.add(getMatchButton());
			//horizontalButtonsPane.add(Box.createRigidArea(new Dimension(10,30)));
			horizontalButtonsPane.add(getCollapseAllButton());
			horizontalButtonsPane.add(Box.createRigidArea(new Dimension(10,30)));
			horizontalButtonsPane.add(getExpandAllButton());
			horizontalButtonsPane.add(Box.createHorizontalGlue());	
			
		}
		
		return horizontalButtonsPane;
		
	}
	
	
	private JButton getAddressButton(){
		
		if (addressButton== null) {
			
			addressButton = new JButton("GO");
			addressButton.setActionCommand("Go to address");
			addressButton.addKeyListener(this);
			addressButton.addActionListener(this);
			addressButton.setEnabled(true);
			addressButton.setBorder(BorderFactory.createRaisedBevelBorder());
			addressButton.registerKeyboardAction(this,"GO",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JButton.WHEN_FOCUSED);
			addressButton.registerKeyboardAction(this,"GO",KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),JButton.WHEN_IN_FOCUSED_WINDOW);	
		}

		return addressButton;
		
	}
	
	private JTextField getAddressTextField(){
		
		if (addressTextField== null) {
			
			addressTextField = new JTextField();
			Border outer = BorderFactory.createRaisedBevelBorder();
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			addressTextField.setBorder(new CompoundBorder(outer, inner));
			addressTextField.setPreferredSize(new Dimension(100,15));
		}

		return addressTextField;
		
	}
	
	
	private JPanel getNavigatorButtonsPane(){
		
		if (navigatorButtonsPane== null) {
			
			navigatorButtonsPane = new JPanel();
			navigatorButtonsPane.setLayout(new BoxLayout(navigatorButtonsPane, BoxLayout.X_AXIS));
			navigatorButtonsPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			
			navigatorButtonsPane.add(Box.createHorizontalGlue());
			navigatorButtonsPane.add(getAddressTextField());
			navigatorButtonsPane.add(Box.createRigidArea(new Dimension(10,10)));
			navigatorButtonsPane.add(getAddressButton());
			navigatorButtonsPane.add(Box.createHorizontalGlue());	
			
		}
		
		return navigatorButtonsPane;
		
	}
			
	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			
			splitPane = new JSplitPane();
			splitPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
			splitPane.setOneTouchExpandable(true);
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(JSplitPane.CENTER_ALIGNMENT);
			splitPane.setDividerSize(10);
			
			splitPane.setPreferredSize(new Dimension(300, 80));
			splitPane.setMinimumSize(new Dimension(300, 80));
			splitPane.setMaximumSize(new Dimension(300, 80));
			
			splitPane.setTopComponent(getNavigatorButtonsPane());
			splitPane.setBottomComponent(getHorizontalButtonsPane());
		}
		return splitPane;
	}

	private JPanel getLeftBottomSidePane(){
		
		if (leftBottomSidePane == null) {
			
			leftBottomSidePane = new JPanel();
			leftBottomSidePane.setLayout(new BoxLayout(leftBottomSidePane, BoxLayout.X_AXIS));
			leftBottomSidePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

			leftBottomSidePane.add(Box.createHorizontalGlue());
			leftBottomSidePane.add(getSplitPane());
		}
		
		return leftBottomSidePane;
		
		}
	
	
	private JPanel getLeftSidePane(){
	
		if (leftSidePane == null) {
			
			leftSidePane = new JPanel();
			leftSidePane.setLayout(new BoxLayout(leftSidePane, BoxLayout.Y_AXIS));
			leftSidePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	        
			leftSidePane.add(getScrollPane());
			//leftSidePane.add(getLeftBottomSidePane());
			leftSidePane.add(getHorizontalButtonsPane());
		}
		
		return leftSidePane;
	
	}
			
	private JPanel getMainPane() {

		if (mainPane == null) {
    	
			mainPane = new JPanel();
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
			mainPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			mainPane.setOpaque(true); 
			
			mainPane.add(getLeftSidePane());
			mainPane.add(Box.createRigidArea(new Dimension(10,100)));
			mainPane.add(getVerticalButtonsPane());    
			
		}
		
		return mainPane;

	}	
	
	public  JTree getJTree(){
		
		if (tree == null){

			rootNode = new DefaultMutableTreeNode("SpagoBI Radice");
		    
			treeModel = new DefaultTreeModel(rootNode);

			tree = new JTree(treeModel);
			tree.setRootVisible(false);
		    tree.setEditable(false);
		    tree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		    tree.setShowsRootHandles(false);
		    tree.addTreeSelectionListener(this);
		    tree.addTreeExpansionListener(this);
			Border outer = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			Border inner = BorderFactory.createEmptyBorder(5,5,5,5);
			tree.setBorder(new CompoundBorder(outer, inner));
		    
		}
		
		return tree;
	}
	
	public DefaultMutableTreeNode getRootNode(){
		return rootNode;
	}
	
	
	// Jtree from XML
	
	/**
	 * This method populates the JTree getting information from the XML document given as input. 
	 */ 
	public void populateTreeFromXML(Document document){
		
		   // get XML root <body>
 	   Element rootXML = (Element)document.selectSingleNode("/response/body");
 	      
 	   processElement(rootXML,rootNode);
 	   
 	   // set the first nodes visible 
 	  for (Enumeration e=body.children(); e.hasMoreElements(); ) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
			TreePath tp = new TreePath(child.getPath());
			tree.scrollPathToVisible(tp);
			tree.setSelectionPath(tp);
			
 	  }
		
 	  
 	   
	}

	
	/**
	 * This method takes the information of an xml document, starting from the given element, and creates a jtree that matches the xml structure 
	 * by calling itself recursively.
	 * 
	 * @param el	Dom4j element of the parsed xml document
	 * @param dmtn 	DefaultMutableTreeNode of the JTree
	 */
	private void processElement(Element el, DefaultMutableTreeNode parentNode) {
		 
		String tag =el.getName();
		
		DefaultMutableTreeNode currentNode;
		
		Properties attributes = new Properties();
		MyNode myNode;
		
		if (tag.equals("body")) {
			
			attributes.put("name","SpagoBI");
			myNode = new MyNode(el.getName(), attributes);
			currentNode = addObject(parentNode,myNode,true);
			body = currentNode;
			
		} else{
			
			attributes = getAttributes(el);
			myNode = new MyNode(el.getName(), attributes);
			currentNode = addObject(parentNode,myNode,false);
		}
			
		// add path to the element
		TreeNode[] tn = currentNode.getPath();
		String treePath=File.separator;
		
		for (int i =2; i<tn.length; i++ ){
			treePath += ((DefaultMutableTreeNode)tn[i]).toString()+File.separator;
		}	
		
		/*
		if(tag.equals("folder")){
		
			for (int i =2; i<tn.length; i++ ){
				treePath += ((DefaultMutableTreeNode)tn[i]).toString()+File.separator;
			}					

		} else if (tag.equals("object")){
			
			for (int i =2; i<tn.length; i++ ){
				treePath += ((MyNode)(((DefaultMutableTreeNode)tn[i]).getUserObject()) ).getAttributes().get("name")+File.separator;
			}
	
		}*/

		myNode.addAttribute("treePath",treePath);
				
		Iterator children = el.elementIterator();
			while(children.hasNext()){
				processElement((Element)children.next(), currentNode);
			}
		
	}
	
	/**
	 *	This method gets name and value of all the attributes of the dom4J Element given as input  
	 * 
	 * @param el	Element parsed from the dom4j document
	 * @return		Properties object where all the pairs (name,value) have been stored 
	 */
	private Properties getAttributes(Element el) {
		 Iterator atts = el.attributeIterator();
		 Properties hm = new Properties();
		 while(atts.hasNext()){
			 Attribute att = (Attribute) atts.next();
			 hm.put(att.getName(),att.getValue());

		 }
		 return hm;
			 		 
	}
	
	/**
	 * This method creates a new node from the child object given as input and adds it to the the node, definded as parent, of the JTree  
	 * 
	 * @param parent 	Node of the JTree to which the new node id added
	 * @param child 	Object added to the JTree as a new node
	 * @param shouldBeVisible set if the node will be shown (expanded)
	 * @return the new DefaulTreeMutableNode representing the new node just added to the JTree
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
		 	 
		 	DefaultMutableTreeNode childNode =new DefaultMutableTreeNode(child);
		 	
			if (parent == null) {
		 
		 		parent = rootNode;

		 	}
		 			 	
		 	treeModel.insertNodeInto(childNode, parent,parent.getChildCount());

		 	//	Make sure the user can see the new node.
			 	if (shouldBeVisible) {
			 		tree.scrollPathToVisible(new TreePath(childNode.getPath()));
			 	}
			return childNode;
	 }
	
	// Listener
	
	/**
	 * This method is called when a button is pressed  
	 */
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
        
		if ("Check IN".equals(command)) {
			
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        	MyNode selectedNode = (MyNode)treeNode.getUserObject();
        	selectedNode.addAttribute("operation","CHECKIN");
        	ServerRequest.getMainInstance().executeServerRequest(selectedNode.getAttributes());
            
        } else if ("Check OUT".equals(command)) {

        	MyNode selectedNode = (MyNode)((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
       	 	selectedNode.addAttribute("operation","CHECKOUT");
       	 	ServerRequest.getMainInstance().executeServerRequest(selectedNode.getAttributes());
       	 	
        } else if ("Change User".equals(command)) {
        	
        	setVisible(false);
           	LoginFrame.getMainInstance().showFrame();
        	
        } else if ("Hide".equals(command)) {
        	
        	setVisible(false);
        	
        } else if ("Expand All".equals(command)) {
        	
        	if (tree.getSelectionPath()!=null){

        		expandAll(tree.getSelectionPath(), true);
        	}
        	
        } else if ("Collapse All".equals(command)) {
        	
        	if (tree.getSelectionPath()!=null){
        		expandAll(tree.getSelectionPath(), false);
        	}
        } else if ("Find File".equals(command)) {
        	File file = resourcesManager.getIReportFile();
        	if (file!=null){
        		
        		if (matchFileToNode(file)!=null) {
        			String fullNameFile = file.getParent();
        			
        			if (!fullNameFile.startsWith(resourcesManager.getTempFolder())) return;
        			
        			else{
        				
        				fullNameFile = fullNameFile.substring(resourcesManager.getTempFolder().length()+1);
        				
        			}
        			
        			showNode(fullNameFile);
        		}
        	}  
		} else if ("Go to address".equals(command)) {
		
			if ((addressTextField.getText().equals(""))||(addressTextField.getText()==null)) return;
			showNode(addressTextField.getText());
			
    	}  
		
	}

	/**
	 * This method manages the TreeSelectionEvent given by the selection or deselection of differnts node on the JTree
	 * It allows to sets the correct status for checkInButton, checkOutButton, collapseAllButton, expandeAllButton
	 * 
	 */
	public void valueChanged(TreeSelectionEvent evt) {
		
		//Get all nodes whose selection status has changed
        TreePath[] paths = evt.getPaths();

        // Iterate through all affected nodes
        for (int i=0; i<paths.length; i++) {
            if (evt.isAddedPath(i)) {
            	
            	DefaultMutableTreeNode dmtn = ((DefaultMutableTreeNode)paths[i].getLastPathComponent());
            	if (dmtn.isRoot()) return;
            	
            	MyNode mn = (MyNode)dmtn.getUserObject();

            	if (tree.isCollapsed(new TreePath(dmtn.getPath()))) getCollapseAllButton().setEnabled(false);
            	else getCollapseAllButton().setEnabled(true);

            	if ((hasCollapsedChildren(dmtn))|| ((tree.isCollapsed(new TreePath(dmtn.getPath())))&&(!dmtn.isLeaf()))) {
            		getExpandAllButton().setEnabled(true);
				} else getExpandAllButton().setEnabled(false);
            	
            	if (mn.getTag().equals("body")) {
        			
    				getCheckINButton().setEnabled(false);
    				getCheckOUTButton().setEnabled(false);
            	} else if (mn.getTag().equals("folder")) {
    			
    				getCheckINButton().setEnabled(false);
    				getCheckOUTButton().setEnabled(false);
    				getAddressTextField().setText(mn.getAttributes().getProperty("treePath"));
    			
            	} else {
    				
    				getCheckINButton().setEnabled(true);
    				getCheckOUTButton().setEnabled(true);
    				
    				getAddressTextField().setText(mn.getAttributes().getProperty("treePath"));
    			}
    			
    			
            } else {
                // This node has been deselected
            	
            }
        }
        
		
	}
	
	/**
	 * This method is called when a node of the JTree is expanded.
	 * It's used to sets the correct status for collapseAllButton, expandeAllButton
	 */
	public void treeExpanded(TreeExpansionEvent evt) {
        JTree tree = (JTree)evt.getSource();
        TreePath path = evt.getPath();
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
        if (!dmtn.isRoot()){
        	tree.setSelectionPath(path);
        	if (hasCollapsedChildren(dmtn)){
                getExpandAllButton().setEnabled(true);
                getCollapseAllButton().setEnabled(true);
        	} else {
                getExpandAllButton().setEnabled(false);
                getCollapseAllButton().setEnabled(true);
        	}
        }
        
    }

	/**
	 * This method is called when a node of the JTree is collapsed.
	 * It's used to sets the correct status for collapseAllButton, expandeAllButton
	 */
    public void treeCollapsed(TreeExpansionEvent evt) {
    	
        JTree tree = (JTree)evt.getSource();
        TreePath path = evt.getPath();
        if (!((DefaultMutableTreeNode)path.getLastPathComponent()).isRoot()){
        	tree.setSelectionPath(path);
            getExpandAllButton().setEnabled(true);
            getCollapseAllButton().setEnabled(false);
        }
        
    }
    
   // Navigate the JTree
	
	public void showNode(String path){
		
		//DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodeMap.get(path);
		
		DefaultMutableTreeNode node = findNode(path,body);
		if (node == null) return;
		TreePath tp = new TreePath(node.getPath());

		tree.scrollPathToVisible(tp);
		tree.setSelectionPath(tp);
		
	}
	
	/**
	 * This method finds if the input file matches one of the nodes into the JTree
	 * 
	 * @return The matching node if it exists or null otherwise
	 */
	public DefaultMutableTreeNode matchFileToNode(File file){
		
		String fullNameFile = file.getParent();

		if (!fullNameFile.startsWith(resourcesManager.getTempFolder())) return null;
		
		else{
			
			fullNameFile = fullNameFile.substring(resourcesManager.getTempFolder().length()+1);
			
		}
		
		resourcesManager.logOnConsole("File da trovare (nome modificato):" + fullNameFile);
		return findNode(fullNameFile,body);
	}
	
	private DefaultMutableTreeNode findNode(String path, DefaultMutableTreeNode node){
		boolean isObject=false;
		String name;
		int res;
		
		res = path.indexOf(File.separator);
		// if path begins with a "/" it cats it
		if (res==0 ) path= path.substring(1);
		res = path.indexOf(File.separator);
		if (res>0) {
			name = path.substring(0,res);
			path=path.substring(res+1,path.length());
		} else {
			name = path;
			path = "";
		}
		
		for (Enumeration e=node.children(); e.hasMoreElements(); ) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
			if (child.toString().equals(name)) {
				
				if (path.equals("")) {
										
					return child;
					
				}
				else return findNode(path,child);
				
			} 
         }
		
		return null;
		
	}
	
	
	/**
	 * This method checks if the input file matches the current selected node into the JTree
	 * 
	 * @return The currently selected node if it matches the file or null otherwise
	 */
	
	public DefaultMutableTreeNode matchFileToSelectedNode(File file){
		
		String fullNameFile = file.getParent();
		
		if (!fullNameFile.startsWith(resourcesManager.getTempFolder())) return null;
		else{
			fullNameFile = fullNameFile.substring(resourcesManager.getTempFolder().length()+1);
		}
		DefaultMutableTreeNode fileNode = findNode(fullNameFile,body);
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		
		if ((fileNode!=null)&&(selectedNode==fileNode))
		
			return selectedNode;

		else return null;	
	}
	
	/**
	 * This method expands (if "expand"==true ) or collapse (if "expand"==false ) all the nodes which are descendant of the input node 
	 * 
	 * @param parent TreePath that identifies the node whose descendant will be collapsed or expanded 
	 * @param expand select if the process will expand (true) or collapse (false) all the nodes
	 */
    private void expandAll(TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * This method checks if the input node has some descendant which are collapsed (apart from the leaves)
     * 
     * @param node   node of the jtree 
     * @return true if this node has at least a descendant (not leaf) collapsed false otherwise 
     */
    private boolean hasCollapsedChildren(DefaultMutableTreeNode node){

    	if (node.isLeaf()) return false;
    	
    	for (Enumeration e=node.children(); e.hasMoreElements(); ) {

    		DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)e.nextElement();
            if (tree.isCollapsed(new TreePath(node.getPath()))) return true;
            else if (hasCollapsedChildren(childNode)) return true;
            
        }
    	
    	return false;
    	
    }
	
    /**
	 * This method refreshes the text on labels and buttons: it's called when the language has been changed 
	 */	
    public void updateWidgetsText(){
    	
      getExpandAllButton().setText(resourcesManager.getLanguageResource("expandAllButton"));
  	  getCollapseAllButton().setText(resourcesManager.getLanguageResource("collapseAllButton")); 
  	  getMatchButton().setText(resourcesManager.getLanguageResource("matchButton"));
  	  getCheckINButton().setText(resourcesManager.getLanguageResource("checkINButton")) ;
  	  getCheckOUTButton().setText(resourcesManager.getLanguageResource("checkOUTButton")) ;
  	  getChangeUserButton().setText(resourcesManager.getLanguageResource("changeUserButton")) ;
  	  getHideButton().setText(resourcesManager.getLanguageResource("hideButton")) ;
  	  
  	  getCurrentUserLabel();
  	  
  	  getMatchButton().setText(resourcesManager.getLanguageResource("matchButton")) ;
  	  getCollapseAllButton().setText(resourcesManager.getLanguageResource("collapseAllButton"));
  	  getExpandAllButton().setText(resourcesManager.getLanguageResource("expandAllButton")) ;
    	
    }
    
   public void showFrame(){
	    
		getMatchButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getChangeUserButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getHideButton().setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.show();
	}
	
    public void keyPressed(KeyEvent e) {
		if ((e.getKeyChar()==KeyEvent.VK_ENTER))
    	 ((JButton)e.getComponent()).setBorder(BorderFactory.createLoweredBevelBorder());
		
	}

	public void keyReleased(KeyEvent e) {
		
		getCheckINButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getCheckOUTButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getChangeUserButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getHideButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getAddressButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getMatchButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getCollapseAllButton().setBorder(BorderFactory.createRaisedBevelBorder());
		getExpandAllButton().setBorder(BorderFactory.createRaisedBevelBorder());
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
    
}