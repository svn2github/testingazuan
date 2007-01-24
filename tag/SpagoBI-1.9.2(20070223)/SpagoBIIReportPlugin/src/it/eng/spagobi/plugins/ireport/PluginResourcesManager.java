package it.eng.spagobi.plugins.ireport;

import it.businesslogic.ireport.gui.JMDIMenuBar;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.eng.spagobi.plugins.ireport.gui.ConfigurationFrame;
import it.eng.spagobi.plugins.ireport.gui.LoginFrame;
import it.eng.spagobi.plugins.ireport.gui.TreeFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JFrame;

/**
 * 
 * This class manages the interface with iReport (load or save file), the boundleResources for language and the main data 
 * for the plugin configuration, like config folder, temp folder, and server location   
 *     
 * @author scarel
 *
 */

public class PluginResourcesManager implements LanguageChangedListener {
	
	private static PluginResourcesManager mainInstance = new PluginResourcesManager();
	
	
	private String operationMode;
	
	// Configuration  
	private final String configFile = "config.xml";
	private  String serverLocation = "";
	private String configFolder;
	private String tempFolder; 

	// iReport 
	private String log;
	
	// languages
	private static ResourceBundle languages;
	private Locale currentLocale;
	
	
	
	/**
	 * This method returns the unique instance of this class 
	 */
	public static PluginResourcesManager getMainInstance(){
		
		if (mainInstance == null){
			
			mainInstance = new PluginResourcesManager();
			
		} 
		return mainInstance;
	}
	
	/**
	 * This constructor set the operation mode ("run" or "test") 
	 */
	private PluginResourcesManager(){
		
		readOperationMode();
		
		// set init configuration
		this.setConfigFolder(getIReportHomeDirectory() + File.separator + "plugins" + File.separator + "iReport_SpagoBI_plugin");
		this.setTempFolder(configFolder + File.separator + "temp");
		
		//set init language resources
		if ("run".equals(operationMode)){
			
			currentLocale = I18n.getCurrentLocale();
			I18n.addOnLanguageChangedListener(this);
			
		} else currentLocale = Locale.ITALIAN;
		
		setResourceBounle();
		
	}
	
	
	public void setOperationMode(String operationMode){
		this.operationMode = operationMode;
	}
	
	

	/**
	 * This methods read from the .properties file the operation mode ("run" or "test")  
	 */
	private void readOperationMode(){
		
		Properties OperationModeFile = (new Properties());
		try {
			
			OperationModeFile.load(new FileInputStream(new File("./iReport_SpagoBI_plugin_Resources/OperationMode.properties")));
			operationMode  = OperationModeFile.getProperty("OperationMode");
			
		} catch (FileNotFoundException e) {
			operationMode = "run";
			logOnConsole("SpagoBI - "+e.toString());
			
			
		} catch (IOException e) {
			operationMode = "run";
			logOnConsole("SpagoBI - "+e.toString());
			
			
		}
		
	
	}
		
	
	// iReport
	/**
	 * This methods returns the main instance of the iReport MainFrame class if the operation mode is set on "run" or an empty JFrame otherwise
	 */
	public JFrame getMainFrame(){
		
		if ("run".equals(operationMode)){
			
			return MainFrame.getMainInstance();
			
		} else {
			
			return new JFrame("Test");
			
		}
	}
	
	/**
	 * @return the iReport Home Directory
	 */
	public String getIReportHomeDirectory(){
		
		if ("run".equals(operationMode)){
			
			return MainFrame.getMainInstance().IREPORT_HOME_DIR;
			
		} else {
			
			return System.getProperty("user.home");
			
		}
	}
	
	
	/**
	 * This methods shows a message on the main console of iReport if the operation mode is set on "run" or on the System.out otherwise
	 */
	
	public void logOnConsole(String msg){
		
		if ("run".equals(operationMode)){
			
			MainFrame.getMainInstance().logOnConsole("\n"+msg+"\n");
			
		} else {
			
			log = msg;
			System.out.println("\n---"+msg+"---\n");
			
		} 
		
	}
	
	/**
	 * If the operation mode is set on "run", this method checks if there is an open JReportFrame and then shows a JDialog to ask 
	 * to the user if he wants to save it.
	 * 
	 * @return the file if the user chooses the "OK" button or null otherwise or if no file are opened on iReport, 
	 * if the operation mode is set on "run".
	 * If the operation mode is differnt from "run", a default file;
	 *  
	 */
	public File saveAndGetIReportFile(){
		
		if ("run".equals(operationMode)){

			JReportFrame jrf = null;
			
			if (MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() != null && MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() instanceof JReportFrame) {
				
				int res = javax.swing.JOptionPane.showConfirmDialog(TreeFrame.getMainInstance(), getLanguageResource("CheckInSave"),"",javax.swing.JOptionPane.YES_NO_OPTION);
				if (res == javax.swing.JOptionPane.OK_OPTION) {	

		            jrf = (JReportFrame)MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame();
		            
		            MainFrame.getMainInstance().save( jrf );
		            
				} else return null;
					
			} else {
				
				javax.swing.JOptionPane.showMessageDialog(TreeFrame.getMainInstance(),getLanguageResource("CheckInOpen"));
				return null;
				
			}
		
			return new File(jrf.getReport().getFilename());
	
		} else return new File("/home/scarel/Desktop/fileExampleSourceCheckIN.xml");
		 
		
	
	}
	
	
	public void closeIReportFile(){
		
		if ("run".equals(operationMode)){
			
			JReportFrame jrf = null;

			if (MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() != null && MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() instanceof JReportFrame) {
				jrf = (JReportFrame)MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame();
				jrf.doDefaultCloseAction();
				
            }
			
		} else logOnConsole("File Close");
	}
	
	/**
	 * If the operation mode is set on "run", this method checks if there is an open JReportFrame.
	 * 
	 * @return the file open on iReport or null otherwise, if the operation mode is set on "run".
	 * If the operation mode is differnt from "run", a default file; 
	 */
	public File getIReportFile(){
		if ("run".equals(operationMode)){

			JReportFrame jrf = null;

			if (MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() != null && MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame() instanceof JReportFrame) {

		            jrf = (JReportFrame)MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame();
			
		            return new File(jrf.getReport().getFilename());
			}
            
			return null;
			
		} else return new File("/home/scarel/Desktop/fileExampleSourceCheckIN.xml");
		
	}
	
	/**
	 * If the operation mode is set on "run", this method calls iReport method to open a JReportFile. Otherwise it shows a default message
	 * on the default console 
	 * 
	 * @param fileName Name of the file to be opened
	 */
	public void openIReportFile(String fileName){
		
		if ("run".equals(operationMode)){
			
			MainFrame mfr = MainFrame.getMainInstance();
			mfr.openFile(fileName);
			
		} else logOnConsole("File Open");
		
		
	}

	
	// languages	
	
	/**
	 * This method reads the .properties files containing language informations, complaint to the ResourceBoundle class
	 * 
	 * If the operation mode is set on "run", the Locale information are taken from the iReport I18n class. Otherwise the dafult 
	 * Locale is set on ITALIAN
	 *   
	 */
	public  void  setResourceBounle(){
		
		try {
			
			languages = ResourceBundle.getBundle("iReport_SpagoBI_plugin_Resources/MyResources", currentLocale);
			
			
		} catch (Exception e){
			
			logOnConsole("SpagoBI - "+e.toString());
			
		} 
		
	}
	
	public void languageChanged(LanguageChangedEvent arg0) {
		
		currentLocale = arg0.getLocale();
		logOnConsole("Cambiata la lingua: "+currentLocale);
		setResourceBounle();
		
		LoginFrame.getMainInstance().updateWidgetsText();
		ConfigurationFrame.getMainInstance().updateWidgetsText();
		TreeFrame.getMainInstance().updateWidgetsText();
	}
	
	
	public Locale getCurrentLocale(){
		return currentLocale;
	}
	
	
	public String getLanguageResource(String key){
		String res = languages.getString(key);
		if (res == null) logOnConsole("Resource cannot be found: "+key);
		return res;
		
	}

	
	// configuration
	
	public String getConfigFolder() {
		return configFolder;
	}


	public void setConfigFolder(String configFolder) {
		this.configFolder = configFolder;
	}


	public String getServerLocation() {
		return serverLocation;
	}


	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}


	public String getTempFolder() {
		return tempFolder;
	}


	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}


	public String getConfigFile() {
		return configFile;
	}

	public String getLog() {
		return log;
	}

	

}
