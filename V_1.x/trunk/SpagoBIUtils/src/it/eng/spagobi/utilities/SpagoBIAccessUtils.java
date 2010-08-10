/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

 */

package it.eng.spagobi.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;



/**
 * This class has been created to provide SpagoBI Access Utils, in order to
 * customize operations with clients.
 * 
 * @author zoppello
 */
public class SpagoBIAccessUtils {
	
	/**
	 * Starting from the base url and the path as input, gets content
	 * information (response body).
	 * 
	 * @param spagoBIBaseUrl The SpagoBI input base URL
	 * @param path The input path
	 * @return The output response body
	 */
	public byte[] getContent(String spagoBIBaseUrl, String path){
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] parameters = { new NameValuePair("jcrPath", path),
	    		                       new NameValuePair("operation", "getContent") };
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(parameters);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
          } catch (IOException e) {
            System.err.println("Failed to download file.");
            e.printStackTrace();
          } finally {
            // Release the connection.
          	httppost.releaseConnection();
          }
          return responseBody;	    
	}
	
	
	/**
	 * Gets the content of a subObject of the principal object
	 * 
	 * @param spagoBIBaseUrl The SpagoBI service URL
	 * @param path The cms path
	 * @param nameSubObject the name of the subObject
	 * @return The output response body
	 */
	public byte[] getSubObjectContent(String spagoBIBaseUrl, String path, String nameSubObj, String user) {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] data = { new NameValuePair("jcrPath", path),
	    						 new NameValuePair("operation", "getSubObjectContent"),
	    						 new NameValuePair("nameSubObject", nameSubObj),
	    						 new NameValuePair("user", user)};
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(data);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
          } catch (IOException e) {
            System.err.println("Failed to download file.");
            e.printStackTrace();
          } finally {
            // Release the connection.
          	httppost.releaseConnection();
          }
          return responseBody;	    
	}
	
	
	
	
	/**
	 * Save the content of a subObject into SpagoBI Repository
	 * 
	 * @param spagoBIBaseUrl The SpagoBI service URL
	 * @param path The cms path
	 * @param nameSubObject the name of the subObject
	 * @param visibility if true the object is public, else if false is visible only to current user
	 * @param content byte array of the suobject content
	 * @return The output response body
	 */
	public byte[] saveSubObject(String spagoBIBaseUrl, String path, String nameSubObj, 
			                    String description, String user, boolean visibility,
			                    String content) throws GenericSavingException {
		HttpClient client = new HttpClient();
		String visibilityStr = "false";
		if(visibility)
			visibilityStr = "true";
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] data = { new NameValuePair("jcrPath", path),
	    						 new NameValuePair("operation", "saveSubObject"),
	    						 new NameValuePair("nameSubObject", nameSubObj),
	    						 new NameValuePair("publicVisibility", visibilityStr),
	    						 new NameValuePair("content", content),
	    						 new NameValuePair("user", user),
	    						 new NameValuePair("description", description)};
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(data);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
              
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
       
        } catch (IOException e) {
            System.err.println("Failed to upload file.");
            e.printStackTrace();
            throw new GenericSavingException("Communication Exception");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        return responseBody;	  
	}
	
	/**
	 * Save the object template into SpagoBI Repository
	 * 
	 * @param spagoBIBaseUrl: The SpagoBI service URL
	 * @param path: The cms path of the document
	 * @param content: the template content
	 * @param templateName: the template name
	 * @return The output response body
	 */
	public byte[] saveObjectTemplate(String spagoBIBaseUrl, String path, String templateName, String content) 
								throws GenericSavingException {
		HttpClient client = new HttpClient();
	    PostMethod httppost = new PostMethod(spagoBIBaseUrl);
	    NameValuePair[] data = { new NameValuePair("jcrPath", path),
	    						 new NameValuePair("templateName", templateName),
	    						 new NameValuePair("operation", "saveObjectTemplate"),
	    						 new NameValuePair("content", content)};
	    //	  Provide custom retry handler is necessary
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httppost.setMethodRetryHandler(retryhandler);
	    httppost.setRequestBody(data);
        byte[] responseBody = null; 
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httppost);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httppost.getStatusLine());
              
            }
            // Read the response body.
             responseBody  = httppost.getResponseBody();
       
        } catch (IOException e) {
            System.err.println("Failed to upload file.");
            e.printStackTrace();
            throw new GenericSavingException("Communication Exception");
        } finally {
            // Release the connection.
          	httppost.releaseConnection();
        }
        return responseBody;	  
	}
	
	
	public void unzip(File repository_zip, File newDirectory) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(repository_zip);
	    Enumeration entries = zipFile.entries();
	    ZipEntry entry = null;
	    String name = null;
	    String path = null;
	    File file = null;
	    FileOutputStream fileout = null;
	    BufferedOutputStream bufout = null;
	    InputStream in = null;
	    while(entries.hasMoreElements()) {
	    	entry = (ZipEntry) entries.nextElement();
	    	name = entry.getName();
	    	path = newDirectory.getPath() + File.separator + name;
	    	file = new File(path);

	    	// if file already exists, deletes it	    	
	    	if (file.exists() && file.isFile()) deleteDirectory(file);
	    	
	    	if(!entry.isDirectory()) {
	    		file = file.getParentFile();
	    		file.mkdirs();
	    		fileout = new FileOutputStream(newDirectory.getPath() + File.separator + entry.getName());
		    	bufout = new BufferedOutputStream(fileout); 
		    	in = zipFile.getInputStream(entry);
		    	copyInputStream(in, bufout);
		    	bufout.flush();
		    	in.close();
		    	bufout.close();
	    	} else {
	    		file.mkdirs();
	    	}
	    }
	    zipFile.close();
	}   

	
	private void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte [] b = new byte[8 * 1024];
		int len = 0;
		while ( (len=in.read(b))!= -1 ) {
		     out.write(b,0,len);
		}
	}
	
	public boolean deleteDirectory(String pathdest) {
		File directory = new File(pathdest);
		return deleteDirectory(directory);
	}
	
	public boolean deleteDirectory(File directory) {
		try {
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isFile()) {
						boolean deletion = file.delete();
						//if (!deletion)return false;
					} else
						deleteDirectory(file.getAbsolutePath());
				}
			}
			boolean deletion = directory.delete();
			//if (!deletion)	return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean deleteFile(String fileName, String path) {
		try {
			File toDelete = new File(path + File.separatorChar + fileName);
			if (toDelete.exists()) toDelete.delete();
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	/**
	 * Given an <code>InputStream</code> as input, gets the correspondent bytes array.
	 * @param is The input straeam 
	 * @return An array of bytes obtained from the input stream.
	 */
	public byte[] getByteArrayFromInputStream(InputStream is) {
	
		try {
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(
					baos);
	
			int c = 0;
			byte[] b = new byte[1024];
			while ((c = is.read(b)) != -1) {
				if (c == 1024)
					bos.write(b);
				else
					bos.write(b, 0, c);
			}
			bos.flush();
			byte[] ret = baos.toByteArray();
			bos.close();
			return ret;
		} catch (IOException ioe) {
			System.err.println("Exception: " + ioe);
            ioe.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Given an <code>InputStream</code> as input flushs the content into an OutputStream 
	 * and then close the input and output stream.
	 * @param is The input stream 
	 * @param os The output stream
	 * @param closeStreams, if true close both stream 
	 */
	public void flushFromInputStreamToOutputStream(InputStream is, OutputStream os, boolean closeStreams) {
		try{	
			int c = 0;
			byte[] b = new byte[1024];
			while ((c = is.read(b)) != -1) {
				if (c == 1024)
					os.write(b);
				else
					os.write(b, 0, c);
			}
			os.flush();
		} catch (IOException ioe) {
			System.err.println("Exception: " + ioe);
            ioe.printStackTrace();
		} finally {
			if (closeStreams) {
				try {
					if (os != null) os.close();
					if (is != null) is.close();
				} catch (IOException e) {
					System.err.println("Error closing streams: " + e);
		            e.printStackTrace();
				}
				
			}
		}
	}

}
