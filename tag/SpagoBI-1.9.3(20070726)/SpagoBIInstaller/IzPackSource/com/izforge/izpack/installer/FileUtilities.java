package com.izforge.izpack.installer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtilities {

	
	public static void explode(String destdir, String pathfile) throws Exception {
		File war = new File(pathfile);
		String warname = war.getName();
		warname = warname.substring(0, warname.lastIndexOf("."));
		File destDir = new File(destdir);
		destDir.mkdir();
		unzip(war, destDir);
	}
	
	/**
	 * Copies a file into a destination directory. If the file exists in the destination directory,
	 * it is deleted before copying the new file.
	 */
	public static void copy(String destdir, String pathfile) throws Exception {
 		File file = new File(pathfile);
 		File destDir = new File(destdir);
 		copy(destDir, file);
	}
	
	/**
	 * Copies a file into a destination directory. If the file exists in the destination directory,
	 * it is deleted before copying the new file.
	 */
	public static void copy(File destDir, File file) throws Exception {
 		String name = file.getName();
 		if (!destDir.exists()) destDir.mkdirs();
 		File oldFile = new File(destDir.getAbsolutePath() + File.separatorChar + name);
 		if (oldFile.exists()) oldFile.delete();
		File fileout = new File(destDir.getAbsolutePath() + File.separatorChar + name);
	    FileInputStream fileinstr = new FileInputStream(file);
	    FileOutputStream fileoutstr = new FileOutputStream(fileout);
	    BufferedOutputStream bufout = new BufferedOutputStream(fileoutstr); 
	    byte [] b = new byte[1024];
		int len = 0;
		while ( (len=fileinstr.read(b))!= -1 ) {
		     bufout.write(b,0,len);
		}
		bufout.flush();
		bufout.close();
		fileinstr.close();
	}
	
	/**
	 * Copy all files and directories contained in the source directory into the destionation directory. 
	 * @param destDir The String representing the destionation directory
	 * @param sourceDir The String representing the source directory 
	 * @param mkdirs Boolean: if it is true, not existing directory (in the destionation directory) are created;
	 *   if it is false, not existing directory (in the destionation directory) are not created (and files 
	 *   contained in the relevant souce directories are not copied)
	 * @throws Exception
	 */
	public static void copyDirectory(String destDirStr, String sourceDirStr, boolean mkdirs) throws Exception {
		File destDir = new File(destDirStr);
		File sourceDir = new File(sourceDirStr);
		copyDirectory(destDir, sourceDir, mkdirs);
	}
	
	/**
	 * Copy all files and directories contained in the source directory into the destionation directories. 
	 * @param destDir The destionation directory File
	 * @param sourceDir The source directory File
	 * @param mkdirs Boolean: if it is true, not existing directory (in the destionation directory) are created;
	 *   if it is false, not existing directory (in the destionation directory) are not created (and files 
	 *   contained in the relevant souce directories are not copied)
	 * @throws Exception
	 */
	public static void copyDirectory(File destDir, File sourceDir, boolean mkdirs) throws Exception {
		if (!destDir.exists() && !destDir.isDirectory()) {
			if (mkdirs) destDir.mkdirs();
			else return;
		}
		File[] containedFiles = sourceDir.listFiles();
		for (int i = 0; i < containedFiles.length; i++) {
			File aFile = containedFiles[i];
			if (aFile.isFile()) copy(destDir, aFile);
			else {
				String dirName = aFile.getName();
				File newDir = new File(destDir.getAbsolutePath() + File.separatorChar + dirName);
				copyDirectory(newDir, aFile, mkdirs);
			}
		}
	}
	   
	private static void unzip(File repository_zip, File newDirectory) throws ZipException, IOException {
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
	    	if (file.exists()) deleteDirectory(file);
	    	
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
	
	public static void extractArchiveFile(String zipFilePath, String newDirectoryPath, String archiveFileExt) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(zipFilePath);
		File newDirectory = new File(newDirectoryPath);
		extractArchiveFile(zipFile, newDirectory, archiveFileExt);
	}
	
	private static void extractArchiveFile(ZipFile zipFile, File newDirectory, String archiveFileExt) throws ZipException, IOException {
	    if (!newDirectory.exists()) newDirectory.mkdirs();
		Enumeration entries = zipFile.entries();
	    ZipEntry entry = null;
	    String name = null;
	    FileOutputStream fileout = null;
	    BufferedOutputStream bufout = null;
	    InputStream in = null;
	    while(entries.hasMoreElements()) {
	    	entry = (ZipEntry) entries.nextElement();
	    	name = entry.getName();
	    	if (!entry.isDirectory() && name.endsWith("." + archiveFileExt)) {
	    		// finds the name of the war file
	    		int index = name.lastIndexOf('/');
	    		if (index != -1) {
	    			name = name.substring(index + 1, name.length());
	    		}
	    		index = name.lastIndexOf('\\');
	    		if (index != -1) {
	    			name = name.substring(index + 1, name.length());
	    		}
	    		fileout = new FileOutputStream(newDirectory.getPath() + File.separator + name);
		    	bufout = new BufferedOutputStream(fileout); 
		    	in = zipFile.getInputStream(entry);
		    	copyInputStream(in, bufout);
		    	bufout.flush();
		    	in.close();
		    	bufout.close();
		    	break;
	    	}
	    }
	    zipFile.close();
	}
	        
	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte [] b = new byte[8 * 1024];
		int len = 0;
		while ( (len=in.read(b))!= -1 ) {
		     out.write(b,0,len);
		}
	}

	public static boolean deleteDirectory(String pathdest) {
		File directory = new File(pathdest);
		return deleteDirectory(directory);
	}
	
	public static boolean deleteDirectory(File directory) {
		try {
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isFile()) {
						boolean deletion = file.delete();
						if (!deletion)
							return false;
					} else
						deleteDirectory(file.getAbsolutePath());
				}
			}
			boolean deletion = directory.delete();
			if (!deletion)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean deleteFile(String fileName, String path) {
		try {
			File toDelete = new File(path + File.separatorChar + fileName);
			if (toDelete.exists()) toDelete.delete();
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	/*
	public static void downloadFile(String url, String fileDest) {
		JFrame frame = new JFrame("Ciao");
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(0, 0, 250, 100);
		progressBar.setVisible(true);
		frame.add(progressBar);
		progressBar.setStringPainted(true);
		frame.setVisible(true);
		
		HttpClient client = new HttpClient();
	    client.getHostConfiguration().setProxy("proxy.eng.it", 3128);
	    client.getState().setProxyCredentials(null, "proxy.eng.it",
	    new UsernamePasswordCredentials("dzerbett", "dep(.;78"));
	    GetMethod httpget = new GetMethod(url);
	    DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
	    retryhandler.setRequestSentRetryEnabled(false);
	    retryhandler.setRetryCount(3);
	    httpget.setMethodRetryHandler(retryhandler);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(httpget);
            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + httpget.getStatusLine());
            }
            // Read the response body.
            Header header = httpget.getResponseHeader("Content-Length");
            String length = header.getValue();
            Integer dimension = new Integer(length);
            InputStream is = httpget.getResponseBodyAsStream();
            File destFile = new File(fileDest);
            FileOutputStream fos = new FileOutputStream(destFile);
			byte[] buffer = new byte[8 * 1024];
			//int len;
			int count = 0;
			double readed = 0;
			while ((count = is.read(buffer)) >= 0) {
				System.out.println("Downloaded: " + count);
				fos.write(buffer);
				readed += count;
				double percentDouble = readed/dimension.intValue()*100;
				int percent = (int) percentDouble;
				progressBar.setValue(percent);
				progressBar.updateUI();
				System.out.println("Download: " + percent);
			}
			is.close();
    		fos.flush();
    		fos.close();
          } catch (IOException e) {
            System.err.println("Failed to download file.");
            e.printStackTrace();
          } finally {
            // Release the connection.
        	  httpget.releaseConnection();
          }
	}
	*/
	
	public static void replaceParametersInFile(String sourceFilePath, String destFilePath, Properties props, 
			boolean deleteSource) throws Exception {
		File sourceFile = new File(sourceFilePath);
		FileReader reader = new FileReader(sourceFile);
		StringBuffer servbuf = new StringBuffer();
		char[] buffer = new char[1024];
		int len;
		while ((len = reader.read(buffer)) >= 0) {
			servbuf.append(buffer, 0, len);
		}
		reader.close();
		if (deleteSource) sourceFile.delete();
		
		Set keys = props.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = props.getProperty(key);
			int startIndex = servbuf.indexOf(key);
			while (startIndex != -1) {
				servbuf.replace(startIndex, startIndex + key.length(), value);
				startIndex = servbuf.indexOf(key);
			}
		}
		
		File destFile = new File(destFilePath);
		FileOutputStream fos = new FileOutputStream(destFile);
		fos.write(servbuf.toString().getBytes());
		fos.flush();
		fos.close();
	}
	
	private static void zipFolder(String pathFolder, String pathBaseFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(pathFolder);
		String[] entries = folder.list();
		byte[] buffer = new byte[4096];   
		int bytes_read;
		FileInputStream in = null;
		for (int i = 0; i < entries.length; i++) {
			File f = new File(folder, entries[i]);
			if(f.isDirectory()) {
				zipFolder(pathFolder + "/" + f.getName(), pathBaseFolder, zip);
			} else {
				in = new FileInputStream(f); 
				String completeFileName = pathFolder + File.separatorChar + f.getName();
				String elementName = f.getName();
				if (completeFileName.lastIndexOf(pathBaseFolder)!=-1) {
					int index = completeFileName.lastIndexOf(pathBaseFolder);
					int len = pathBaseFolder.length();
					elementName = completeFileName.substring(index + len + 1);
				}
				ZipEntry entry = new ZipEntry( elementName );
				entry.setTime( f.lastModified() );
				int fileLength = (int)f.length();
				FileInputStream fis = new FileInputStream ( f );
				byte[] wholeFile = new byte [fileLength];
				int bytesRead = fis.read( wholeFile , 0 /* offset */ , fileLength );
				fis.close();

				zip.putNextEntry( entry );

				zip.write( wholeFile , 0, fileLength );
				zip.closeEntry();
			}
		}
	}
	
	public static void zipFolder(String pathFolder, String destFilePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(destFilePath);
		ZipOutputStream zip = new ZipOutputStream (fos);
		zip.setLevel(5);
		zip.setMethod(ZipOutputStream.DEFLATED);
		zipFolder(pathFolder, pathFolder, zip);
		zip.close();
	}
	
}
