package it.eng.spagobi.plugins.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import it.eng.spagobi.plugins.ireport.Plugin;
import it.eng.spagobi.plugins.ireport.PluginResourcesManager;
import it.eng.spagobi.plugins.ireport.gui.ConfigurationFrame;
import junit.framework.TestCase;

public class TestPlugin_UpdateConfiguration extends TestCase {

	private PluginResourcesManager resourcesManger = null;
	private Plugin plugin = null;
	
	private File configFileCopy = null;
	private File configFile = null;
	private String configFileCopyName = "$_config.xml";
	
	private String userHomeFolder = System.getProperty("user.home");
	private String myConfigFolder =  userHomeFolder + File.separator + "Desktop/SpagoBI";
	private String myTempFolder = myConfigFolder + File.separator+"temp";  
	
	private String myServerLocation =  "http://localhost:8080/SpagoBIiReportPluginTest/test";
	
	
	private String myTempFolderNew = userHomeFolder + File.separator + "Desktop/SpagoBI/test";  
	private String myServerLocationNew =  "http://localhost:8080/test/";
	
	private String myServerLocationError = "";
	private String myTempFolderError = "/TempTest";
	private String myConfigFolderError = "/ConfigTest";
	
	private boolean deleteConfig = false;
	
	/**
	 * The information given are correct:
	 * 1-The method under test must return the value true
	 * 2-The serverLocation have to be changed because a new correct information 
	 *   has been read into the config file  
	 * 3-The tempFolder have to be changed because a new correct information has been read into the config file	 *
	 */
	public void testUpdateConfiguration_NoError(){

		resourcesManger.setConfigFolder(myConfigFolder);
		
		assertTrue(plugin.updateConfiguration(myServerLocationNew,myTempFolderNew));
		
		assertEquals(resourcesManger.getTempFolder(),myTempFolderNew);
		assertEquals(resourcesManger.getServerLocation(),myServerLocationNew);
		
		
	}

	/**
	 * The Config Folder is not correct:
	 * 1-The method under test must return the value false
	 * 2-Error on the console
	 * 3-The filed servletLocation does not have to be changed because information are not correct
	 * 4-The filed tempFolder does not have to be changed because information are not correct 
	 * 
	 */
	public void testUpdateConfiguration_ConfigTempError(){
		createConfigFile(myTempFolder, myServerLocation, myConfigFolder,false);
		
		resourcesManger.setConfigFolder(myConfigFolderError);
				
		assertFalse(plugin.updateConfiguration(myServerLocation,myTempFolder));
		assertEquals(resourcesManger.getLog(),"SpagoBI Plugin - This folder can't be created: '/ConfigTest'");
		assertEquals(resourcesManger.getServerLocation(),myServerLocation);
		assertEquals(resourcesManger.getTempFolder(),myTempFolder);
		
		
		
	}
	
	/**
	 * The Server Location is not correct:
	 * 1-The method under test must return the value false
	 * 2-Error on the console
	 * 3-The filed servletLocation does not have to be changed because information are not correct
	 * 4-The filed tempFolder does not have to be changed because information are not correct 
	 */
	
	public void testUpdateConfiguration_ServerLocationInfoError(){

		createConfigFile(myTempFolder, myServerLocationError, myConfigFolder,false);
		
		assertFalse(plugin.updateConfiguration(myServerLocationError,myTempFolder));
		assertEquals(resourcesManger.getLog(),"SpagoBI Plugin - This folder can't be created: '/ConfigTest'");
		assertEquals(resourcesManger.getServerLocation(),myServerLocation);
		assertEquals(resourcesManger.getTempFolder(),myTempFolder);
				
	}
	
	/**
	 * The Temp Folder is not correct:
	 * 1-The method under test must return the value false
	 * 2-Error on the console
	 * 3-The filed servletLocation does not have to be changed because information are not correct
	 * 4-The filed tempFolder does not have to be changed because information are not correct 
	 */
	public void testUpdateConfiguration_TempFolderInfoError(){

		createConfigFile(myTempFolderError, myServerLocation, myConfigFolder,false);
		
		assertFalse(plugin.updateConfiguration(myServerLocation,myTempFolderError));
		assertEquals(resourcesManger.getLog(),"SpagoBI Plugin - Server Location not correct: ''");
		assertEquals(resourcesManger.getTempFolder(),myTempFolder);
		assertEquals(resourcesManger.getServerLocation(),myServerLocation);
				
	}
	
	
	/**
	 * This method sets the configuration parameters through the PluginResourcesManager 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		plugin = new Plugin();
		
		resourcesManger = PluginResourcesManager.getMainInstance();
		resourcesManger.setOperationMode("test");
		
		// set the configuration parameters 
		resourcesManger.setConfigFolder(myConfigFolder);
		resourcesManger.setTempFolder(myTempFolder);
		resourcesManger.setServerLocation(myServerLocation);
		
		configFile = new File(myConfigFolder+File.separator + resourcesManger.getConfigFile());
		configFileCopy = new File(myConfigFolder + File.separator + configFileCopyName);
		
		if (configFile.exists()) {
			configFile.renameTo(configFileCopy);
			deleteConfig = false;
		} else deleteConfig = true;
		

	}
	
	
	/**
	 * This method is called after each test and renames the copy of config file and deletes files created 
	 * for the test.    
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		configFileCopy.renameTo(configFile);
		configFileCopy.delete();
		if (deleteConfig==true) configFile.delete();
		
	}
	
	
	/**
	 * This method creates a config file containing the tempFolder and serverLocation information required 
	 * 
	 * @param myTempFolder	
	 * @param myServerLocation	
	 * @param myConfigFolder 	folder where the file is written
	 * @param insertError	if this parameter is true an error is created so that the config file couldn't be 
	 * parsed correctly
	 */
	
	private void createConfigFile(String myTempFolder, String myServerLocation, String myConfigFolder, boolean insertError){
		String error = "";
		if (insertError) error = "ERRORE";
		
	   	Document document = DocumentHelper.createDocument();
        Element root = document.addElement( error+"configuration" );

        Element server = root.addElement("serverLocation");
            server.addAttribute("name", "Server Location");
            server.addText(myServerLocation);	
  
        Element temp = root.addElement("tempFolder");
            temp.addAttribute("name", "Temp Folder");
            temp.addText(myTempFolder);
            
            
        XMLWriter writer;
	        
	    try {
	    	
	    	File dir = new File(myConfigFolder);
	    	if (!dir.exists()) dir.mkdir();
	    	
			writer = new XMLWriter(new FileWriter(new File(myConfigFolder+File.separator+resourcesManger.getConfigFile())));
			writer.write(document);
			writer.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	
        
		
	}
	
}