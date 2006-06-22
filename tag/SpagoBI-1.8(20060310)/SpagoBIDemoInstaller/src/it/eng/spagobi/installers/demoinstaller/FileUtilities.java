package it.eng.spagobi.installers.demoinstaller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileUtilities {

	
	public static void explode(String destdir, String pathfile) throws Exception {
		File war = new File(pathfile);
		String warname = war.getName();
		warname = warname.substring(0, warname.lastIndexOf("."));
		File destDir = new File(destdir);
		destDir.mkdir();
		unzip(war, destDir);
	}
	

	public static void copy(String destdir, String pathfile) throws Exception {
 		File filein = new File(pathfile);
 		String name = filein.getName();
 		File destDir = new File(destdir);
 		destDir.mkdirs();
		File fileout = new File(destdir+"/"+name);
	    FileInputStream fileinstr = new FileInputStream(filein);
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
	    	path = newDirectory.getPath() + File.separator + entry.getName();
	    	file = new File(path);

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
	        
	        
	private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte [] b = new byte[1024];
		int len = 0;
		while ( (len=in.read(b))!= -1 ) {
		     out.write(b,0,len);
		}
	}

	
	
}
