package it.eng.spagobi.plugins.ireport;

import it.eng.spagobi.plugins.ireport.gui.LoginFrame;
import it.eng.spagobi.plugins.ireport.gui.TreeFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


public class ServerRequest {

	private static ServerRequest mainInstance = null;
	
	private PostMethod post = null;
	
	private Document document = null;
		
	private Properties properties = null;
	private String faultCode, faultString, operation;
	private PluginResourcesManager resourcesManager = null;
	
	private boolean cancelFileFlag ;
	private File input;
	
	
	
	
	public static ServerRequest getMainInstance(){
		
		if (mainInstance==null) {

			mainInstance = new ServerRequest();
			
		}
		
		return mainInstance;
		
	}
	
	private ServerRequest(){
		
		resourcesManager = PluginResourcesManager.getMainInstance();
	}
	

	
/**
 * This method sends a request to the server with the post method.
 * If first attaches (key,value) or file elements 
 * Then it executes the request and wait for the server response
 * 
 * It can manage three operations:
 * 	LOGIN
 * 	CHECK IN 
 * 	CHECK OUT 
 * 
 * 
 * @param properties It contains all the pairs (<String>key, <String>value) used to distinguish the kind of operation (LOGIN, CHECKIN, CHECKOUT) 
 * or to be sent to be attached as parameters for the server  
 */
	public void executeServerRequest (Properties properties) {
		
		this.properties = properties;
		operation = properties.getProperty("operation");
		
			HttpClient client = new HttpClient();
			
			if (getPostMethod() == null) return;
			
			try {
				
				client.executeMethod(post);
										
				if (properties.get("operation").equals("LOGIN")){
					
					login();										
					
				} else if (properties.get("operation").equals("CHECKIN")){
				
					checkIN();
					
				} else if (properties.get("operation").equals("CHECKOUT")){
					
					checkOUT();
					
				}
							
				
			} catch (HttpException e) {
				
				resourcesManager.logOnConsole("\n SpagoBI Plugin - " + e.toString());
				e.printStackTrace();
				
			} catch (IOException e) {

				resourcesManager.logOnConsole("\n SpagoBI Plugin - " + e.toString());
				e.printStackTrace();
				
			} finally {
	        
	            post.releaseConnection();
	            
			}					
	
	}
	
	/**
	 * This method creates a PostMethod element and attaches all the parameters required or files
	 * 
	 * @return Post Method created if all the parameters and file have been attached correctly, false otherwise
	 */
	public PostMethod getPostMethod(){
		
		post = new PostMethod(resourcesManager.getServerLocation());
		
		boolean retVal = setPostParameter();
		if (retVal == false) return null;
	
		return post;
		
	}

	/**
	 * This method sets the parameters to be sent to the server
	 * 
	 * @return true if all the StringPart and FilePart have been attached correctly, false otherwise
	 */
	private boolean setPostParameter(){
		
		if (operation.equals("LOGIN")) {
			
			Part[] parts = {
					
				new StringPart("operation",operation),
				new StringPart("username",properties.getProperty("username")),
				new StringPart("password",properties.getProperty("password"))
				
			};
			
			post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
			
			
		} else if (operation.equals("CHECKOUT")){
			
			String path = properties.getProperty("path");
			if((path==null) || path.trim().equals("")){
				PluginResourcesManager resMan = PluginResourcesManager.getMainInstance();
				String errMsg = resMan.getLanguageResource("CheckOutFolder");
				JOptionPane.showMessageDialog(TreeFrame.getMainInstance(),errMsg);
				return false;
			}
			Part[] parts = {
					
					new StringPart("operation",operation),
					new StringPart("username",UserProfile.getUsername()),
					new StringPart("password",UserProfile.getPassword()),
					new StringPart("name",properties.getProperty("name")),
					new StringPart("path",path)
				};
						
	            post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
	        			
		} else if (operation.equals("CHECKIN")) {
			
			input = resourcesManager.saveAndGetIReportFile();
			if (input==null) return false;
			resourcesManager.logOnConsole(input.getPath());
			
			
			if (TreeFrame.getMainInstance().matchFileToSelectedNode(input)!=null){
				
				cancelFileFlag = true;
				
			} else 	{
				
				if (TreeFrame.getMainInstance().matchFileToNode(input)!=null){
					resourcesManager.logOnConsole("altro nodo");
					
					String[] option = {resourcesManager.getLanguageResource("Continue"), resourcesManager.getLanguageResource("Cancel")};
					
					int res = javax.swing.JOptionPane.showOptionDialog(TreeFrame.getMainInstance(), resourcesManager.getLanguageResource("CheckInWarning"),"SpagoBI",JOptionPane.YES_NO_OPTION,1, null, option, null);
					if (res == 0) {	

						cancelFileFlag = true;		
					
					} else if (res == 1) return false;
																	
				} else cancelFileFlag = false;

			}
		
			
			try {
				
				Part[] parts = {
						
						new StringPart("operation",operation),
						new StringPart("username",UserProfile.getUsername()),
						new StringPart("password",UserProfile.getPassword()),
						new StringPart("name",properties.getProperty("name")),
						new StringPart("path",properties.getProperty("path")),
						new FilePart(input.getName(),input),
						new StringPart("fileName",input.getName()),
						new StringPart("filePath",input.getParent())
						
					};
				
				post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
				
			} catch (FileNotFoundException e) {
				
				resourcesManager.logOnConsole(e.toString());
				
			}	
	            
			
		} 
				
	return true;	

	}
		
	
	/**
	 * This method reads the Server Response: if the login has been successfully executed then it clears the Login Frame, get the Main instance of the 
	 * tree and populate it getting the information from the XML file received from server
	 *
	 */
	
	private void login(){
		
		if (checkServerResponse()) {
		   
			   LoginFrame.getMainInstance().clearFrame();
			   UserProfile.setUsername(properties.getProperty("username"));
			   UserProfile.setPassword(properties.getProperty("password"));
			   
			 	// create a new tree
			   TreeFrame tree = new TreeFrame(); 
			   tree.populateTreeFromXML(document);
			   TreeFrame.getMainInstance().showFrame();
			   
			 	// check if there is an open file and if it matches an element in the tree 
			   File file = resourcesManager.getIReportFile();
	        	if (file!=null) {
	        		if (TreeFrame.getMainInstance().matchFileToNode(file)!=null) 
	        			TreeFrame.getMainInstance().showNode(file.getPath());
	        			        			
	        	}		    	
			   
		} else javax.swing.JOptionPane.showMessageDialog(LoginFrame.getMainInstance(), resourcesManager.getLanguageResource("LoginError"));
			
        
	}
	
	
	/**
	 * This method checks the HTTPtrue Status Code from the Server Response and if the download of the file from the server has been positive, 
	 * then opens it from iReport   
	 */
	private void checkOUT(){
		
		if (post.getStatusCode()==200) {
			
			//	set destination file for Server Response
			Header[] header = post.getResponseHeaders("Content-Disposition");
			
			String fileNameExt= header[0].getValue();
			
			int start = fileNameExt.indexOf("\"");
			int end = fileNameExt.indexOf("\"",start+1);
			String fileName = fileNameExt.substring(start+1,end);
									
			File dir = new File(resourcesManager.getTempFolder()+File.separator+properties.getProperty("treePath"));
			if (!dir.exists()) dir.mkdirs();
			
			// setto il nome
			String tempFileName = dir.getPath() + File.separator + fileName;
			
			File tempFile = new File(tempFileName);
			String[] option = {resourcesManager.getLanguageResource("Overwrite"), resourcesManager.getLanguageResource("Cancel")};
			
			if (tempFile.exists()) {
				
				int res = javax.swing.JOptionPane.showOptionDialog(TreeFrame.getMainInstance(), resourcesManager.getLanguageResource("FileAlreadyExists"),"SpagoBI",JOptionPane.YES_NO_OPTION,1,null, option, null);
				if (res == 0) {	

					if (writeTempFile(tempFileName) == false) return;;
					
					resourcesManager.openIReportFile(tempFileName) ;
					TreeFrame.getMainInstance().setVisible(false);
			
				} else if (res == 1) return;

				
			} else {
				
				if (writeTempFile(tempFileName) == false) return;
				
				resourcesManager.openIReportFile(tempFileName);
				TreeFrame.getMainInstance().setVisible(false);
				
			}
				
			
		} else javax.swing.JOptionPane.showMessageDialog(TreeFrame.getMainInstance(),resourcesManager.getLanguageResource("ErrorCheckOUT") + post.getStatusCode());
		
		
	}
	
	
	
	/**
	 * This methods reads the server response and shows a message to confirm the success of the upload process or, otherwise, to 
	 * or to describe the error   
	 *
	 */
	private void checkIN(){
		
		if (checkServerResponse()){
		
			javax.swing.JOptionPane.showMessageDialog(TreeFrame.getMainInstance(), resourcesManager.getLanguageResource("UploadCorrect"));

			if (cancelFileFlag){
				resourcesManager.closeIReportFile();
				input.delete();
				
			}
		}	
		else 
			
			javax.swing.JOptionPane.showMessageDialog(TreeFrame.getMainInstance(), resourcesManager.getLanguageResource("LoginError"));
		
		
	}
	
	
	/**
	 * This method reads the Server response  
	 * 
	 * @return true if the server response is compliant to the required structure and if it doesn't contain an error message, false otherwise
	 */
	private boolean checkServerResponse(){
	
		try {

			SAXReader reader = new SAXReader();
			document = reader.read(post.getResponseBodyAsStream());
		   
			faultCode = document.selectSingleNode("/response/fault/faultcode").getText();
			faultString = document.selectSingleNode("/response/fault/faultstring").getText();
			
			
		   if ((faultCode.equals(""))&&(faultCode.equals(""))) {
			    
			   return true;	   			
			   
		   } else {
			   
			   resourcesManager.logOnConsole("SpagoBI Plugin - Fault Code: "+ faultCode + " Fault String: "+ faultString);   
			   return false;
			   
		   }
		   
		} catch (NullPointerException e){
    		
    		resourcesManager.logOnConsole("SpagoBI Plugin - " + resourcesManager.getLanguageResource("ServerResponseNotCorrect"));
    		return false;
    		
		} catch (DocumentException e) {

			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			return false;
			
		} catch (IOException e) {

			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			return false;
		}

	}
	
	/**
	 * This method writes in the temp folder the file received from server 
	 * 
	 * @param tempFileName Name of the file
	 * @return	true if the operation has been correctly executed, false otherwise
	 * 
	 */
	
	private boolean writeTempFile(String tempFileName){
		
		InputStream inputStream;
		OutputStream outputStream;
		
		// write the file 
		try {
						
			inputStream = post.getResponseBodyAsStream();
			outputStream = new FileOutputStream(new File(tempFileName));

			int c;
	        while ((c = inputStream.read()) != -1)
	           outputStream.write(c);
	        
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			
		} catch (IOException e) {
			
			resourcesManager.logOnConsole("SpagoBI Plugin - " + e.toString());
			return false;
		}
		return true;
		
	}
	
	public void setProperties(Properties properties){
		this.properties = properties;
	}
	
	public void setOperation(String operation){
		this.operation= operation;
	}
	
	
}
