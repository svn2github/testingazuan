package it.eng.spagobi.plugins.ireport;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import it.businesslogic.ireport.util.I18n;
import it.eng.spagobi.plugins.ireport.gui.ConfigurationFrame;
import it.eng.spagobi.plugins.ireport.gui.LoginFrame;
import it.eng.spagobi.plugins.ireport.gui.TreeFrame;


public class Plugin extends it.businesslogic.ireport.plugin.IReportPlugin {
	
	public static final Dimension buttonsDimensions = new Dimension(120,40);
	public static final int margin = 15;
	
	private static Plugin mainInstance = null;
	
	private Document document = null;
	
	private PluginResourcesManager resourcesManager = null;
	
	
	public static void main (String [] args){
		Plugin plugin = new Plugin();
		plugin.configure();
	}
	
	
	/**
	 * This constructor gets System.getProperty("user.home") to set the default configuration folder.
	 * 
	 * The initial tempFolder is set from the config folder. In the following it will be read from the config file.
	 * This folder is used to store all the files which will be downloaded from server
	 *  
	 */
    public Plugin() {
    	
    	mainInstance = this;
    	resourcesManager = PluginResourcesManager.getMainInstance();
    	
    }
    
    /**
     * This method is called every time the plugin is selected from the iReport Plugin menu
     *  
     * First, it sets the LookAndFeel.
     *  
     * Then reads the configuration file to get information about server location and tempo folder.
     * If no config files are available, it opens a ConfigurationFrame passing it the default values 
     * for thesconfigFile.exists()e parameters.
     * 
     * Then it checks if there is a user aready logged
     *   
     */
    public void call() {
    	
   	
    	//sets the LookAndFeel
    	setLookAndFeel();
    	
    	// checks if there is the Config file and if the information in it are correct
       	if (checkConfigFile()) {
       		
        	// checks if there is already a user logged 
       		checkUser();
       		    		       		
       	} else {
       		
       		// open the configuration frame
       		
   			ConfigurationFrame configurationFrame = ConfigurationFrame.getMainInstance();
   			configurationFrame.setAction("call");
   			configurationFrame.setTextFields(resourcesManager.getServerLocation(),resourcesManager.getTempFolder());
   			   
    		configurationFrame.showFrame();
       		
     	}
 
    }
    
    /**
     * This method is called when the plugin configuration button on plugin list is selected. 
     *  
     * It sets the LookAndFeel.
     *  
     * Then it reads the configuration file to get information about server location and temporary folder
     * and open the ConfigurationFrame.
     *    
     */
 
    public void configure() {
    	
    	//set the LookAndFeel
    	setLookAndFeel();
    	
    	// check the Config file
    	checkConfigFile();
       	
    	// open the configuration frame
    	ConfigurationFrame configurationFrame = ConfigurationFrame.getMainInstance();
   		configurationFrame.setAction("configure");
   		configurationFrame.setTextFields(resourcesManager.getServerLocation(),resourcesManager.getTempFolder());
   	
   		configurationFrame.showFrame();
    	
    }

    
    /**
     * This method sets the look and feel
     *
     */
    
    private void setLookAndFeel(){
    	    	
    	LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();
       	try {
    		
			UIManager.setLookAndFeel(lafi[0].getClassName());
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			
			
		} catch (ClassNotFoundException e) {
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			
		} catch (InstantiationException e) {
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			
		} catch (IllegalAccessException e) {
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			
		} catch (UnsupportedLookAndFeelException e) {
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			
		}
    	
    }
    
    /**
     * This method checks if the config file is present in the plugin folder
     * 
     * If the file is present it will try to get the required information from it and will eventually check if these information are correct
     * 
     * @return true if the server location and the temp folder have been updated correctly getting the required information from the existing 
     * 		config file  
     *   
     */
    
    private boolean checkConfigFile(){
    	        
		try{
			
			SAXReader reader = new SAXReader();
			document = reader.read(resourcesManager.getConfigFolder() + File.separator + resourcesManager.getConfigFile());
			
			return getInfoFromConfigDocument();
			
		}	catch(DocumentException de){
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + resourcesManager.getLanguageResource("ConfigFileNotFound"));
			return false;
				 											
		} 
    	
    }
    
    /**
     * This method gets the desired information from the Dom4j Document generated parsing the config file 
     * Then it checks that the obtained values are correct
     * 
     * It update the serverLocation and tempFolder fields only if the read values are accepted  
     * 
     * @return true if the information are correct
     */
    private boolean getInfoFromConfigDocument(){
    	
    	try{
    		
    		String serverLocationTemp = document.selectSingleNode( "/configuration/serverLocation" ).getText();
        	String tempFolderTemp =  document.selectSingleNode( "/configuration/tempFolder" ).getText();
        	
        	if (checkConfigInformation(serverLocationTemp, tempFolderTemp)) {
        		
        		resourcesManager.setServerLocation(serverLocationTemp);
        		resourcesManager.setTempFolder(tempFolderTemp);
        		
        		return true;
        		
        	} else return false;
        	
    	} catch (NullPointerException e){
    		
    		resourcesManager.logOnConsole("SpagoBI Plugin - " + resourcesManager.getLanguageResource("ConfigFileNotCorrect"));
    		
    		return false;
    		
    	}
    }
    
    /**
     * 
     * This methods checks if the input parameters representing the server location and 
     * the temporary folder contain correct values
     * 
     * @param serverLocation 	Server location
     * @param tempFolder 		Temporary folder 
     * 
     * @return true if the parameters are valide, false otherwise
     * 
     */
    
    private boolean checkConfigInformation(String serverLocation, String tempFolder){
    			
    	
    	if (serverLocation.equals("")){
    	
    		resourcesManager.logOnConsole("SpagoBI Plugin - " + resourcesManager.getLanguageResource("ServerLocationNotCorrect") +  ": '" + serverLocation+"'");
    		return false;
    		
    	} else if (!checkAndSetFolder(tempFolder)){
    		
    		resourcesManager.logOnConsole("SpagoBI Plugin - " + resourcesManager.getLanguageResource("TempFolderNotCorrect") +  ": '" + tempFolder+"'");
    		return false;
    		
    	}
    	
    	return true;
    	
   }
    
    
	    
    /**
     * 
     * This method updates the local fields serverLocation and tempFolder getting the new values as input parameters.
     * Then creates a new config file storing these information 
     *
     * The input parameters are first evaluated with the checkConfigInformation(String, String) method
     *  
     * @param serverLocationTemp	
     * @param tempFolderTemp
     * 
     * @return true if the input parameters could be correct values and if the config file write operation is processed correctly 
     */
    
    
    public boolean updateConfiguration(String serverLocationTemp, String tempFolderTemp){
    // aggiornare anche la mappa dei nodi
    	
    	if (checkConfigInformation(serverLocationTemp, tempFolderTemp)) {
    		if (writeToFile(createConfigDocument(serverLocationTemp,tempFolderTemp) , resourcesManager.getConfigFolder(), resourcesManager.getConfigFile())) {
    			
    			resourcesManager.setServerLocation(serverLocationTemp);
        		resourcesManager.setTempFolder(tempFolderTemp);
        		return true;
        		
    		} else return false; 
    		
    	} else return false;
    	
    	
    }
	    
    
    /**
     * This method creates the config document containing information about server location and temp folder 
     * 
     * @return dom4J Document  
     * 
     */
    
	private Document createConfigDocument(String ServerLocation, String tempFolder) {
		   	
	        Document document = DocumentHelper.createDocument();
	        Element root = document.addElement( "configuration" );

	        Element server = root.addElement("serverLocation");
	            server.addAttribute("name", "Server Location");
	            server.addText(ServerLocation);
	            
	        Element temp = root.addElement("tempFolder");
	            temp.addAttribute("name", "Temp Folder");
	            temp.addText(tempFolder);

	        return document;
	    }
	    
	
	/**
	 * This method writes on file the dom4j document read as input.
	 *
	 *	@param document 	Dom4j Document to store on the file
	 *	@param pathName 	folder where the file will be stored
	 *	@param fileName 	name of the file (This file should have the following extension: .xml)
	 * 
	 *  return false if an exception occurred and the write operation failed, true otherwise
	 *  
	 */
	private boolean  writeToFile(Document document, String pathName, String fileName){
						  
		resourcesManager.logOnConsole("Il file di configurazione è in "+pathName);
			// check if the folder name is correct and creates it if it dosn't exist
			if (!checkAndSetFolder(pathName)) return false;
			
			XMLWriter writer;
			        
	        try {
				
				writer = new XMLWriter(new FileWriter(new File(pathName+File.separator + fileName)));
				writer.write(document);
			    writer.close();
			        
			} catch (IOException e) {
				
				resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
				
				return false;
			}        
			
			return true;
			
					
	    }
		
	
	/**
	 * This method checks if there is already a user logged by reading the UserProfile information
	 * 
	 * If no users are available it opens the Login Frame
	 * 
	 * Otherwise it show the hidden Tree Frame 
	 *
	 */
    
	public void checkUser() {
				
       	if ((UserProfile.getUsername().equals(""))&&(UserProfile.getPassword().equals(""))){
       	      		
       		LoginFrame.getMainInstance().showFrame();
       		
    	} else {
    		
    	 	// check if there is an open file and if it matches an element in the tree 
        	File file = resourcesManager.getIReportFile();
        	if (file!=null) {
        		if (TreeFrame.getMainInstance().matchFileToNode(file)!=null) {
        			
        			String fullNameFile = file.getParent();
        			
        			if (!fullNameFile.startsWith(resourcesManager.getTempFolder())) return;
        			
        			else{
        				
        				fullNameFile = fullNameFile.substring(resourcesManager.getTempFolder().length()+1);
        				
        			}
        			
        			TreeFrame.getMainInstance().showNode(fullNameFile);
        		}
        			
        	}
     	
    		TreeFrame.getMainInstance().showFrame();
    		
    	}
       	
	}
	
	/**
     * Return the main instance of the Plugin. This method assume that there is only
     * one Plugin instanced per JVM
     * 
     */
	
	public static Plugin getMainInstance(){
					
		return mainInstance;
		  
	}
	
	/**
	 * 
	 * This method evaluates if the input String corresponds to an existing directory. 
	 *  
	 * @return true if the directory already exists or has been correctrly created, false if the path is not correct or if a
	 * 	security Exception occurred.
	 */
	
	private boolean checkAndSetFolder(String pathName) {
		
		File dir = new File(pathName);
		  if (dir.exists()) {
              if (!dir.isDirectory() ) {
              	
              	resourcesManager.logOnConsole("SpagoBI Plugin - "+resourcesManager.getLanguageResource("FolderNameNotCorrect") + ": '"+pathName+"'");
              	return false;
              	
              }
          }
         
          else {
          	
              boolean retVal = dir.mkdirs();
              if (retVal == false) {
              	
              	resourcesManager.logOnConsole("SpagoBI Plugin - "+resourcesManager.getLanguageResource("FolderCannotBeCreated") + ": '"+pathName+"'");
      			return false;
              	
              }	         
					
		}
		
		return true;
		
	}
	
}