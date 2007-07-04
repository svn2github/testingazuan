/**
 * This file is released under the LGPL license
 * See the LICENSE_SpagoBI-IReportPlugin.txt file for the complete text of the LGPL license
 * 
 */
package it.eng.spagobi.plugins.test;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import it.eng.spagobi.plugins.ireport.PluginResourcesManager;
import it.eng.spagobi.plugins.ireport.ServerRequest;
import junit.framework.TestCase;



public class TestGetPostMethod extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		
	}
	
// LOGIN
// CHECK IN 
// CHECK OUT
	
	public void testGetPostMethod() {
		ServerRequest serverRequest = ServerRequest.getMainInstance();
		
		PluginResourcesManager resourcesManager = PluginResourcesManager.getMainInstance();

		
		serverRequest.setOperation("LOGIN");
		
		Properties properties = new Properties();
		properties.setProperty("username","test");
		properties.setProperty("password","test");
		
		serverRequest.setProperties(properties);
		
		PostMethod post = serverRequest.getPostMethod();		
		

		HttpClient client = new HttpClient();
		
		try {
			
			client.executeMethod(post);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		readPost(post);
		
	}
	
	private void readPost(PostMethod post){
		
		try {
			System.out.print(post.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
