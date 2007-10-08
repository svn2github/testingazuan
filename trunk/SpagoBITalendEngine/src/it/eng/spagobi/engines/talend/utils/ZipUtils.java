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
package it.eng.spagobi.engines.talend.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Andrea Gioia
 * 
 */
public class ZipUtils {

	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	
	public static void unzip(ZipFile zipFile, File destDir) {
		
		Enumeration entries;
		
		try {
			
			entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (!entry.isDirectory()) {
					File destFile = new File(destDir,entry.getName());
					File destFileDir = destFile.getParentFile();
					if(!destFileDir.exists()) {
						System.err.println("Extracting directory: " + entry.getName().substring(0, entry.getName().lastIndexOf('/')));
						destFileDir.mkdirs();
					}
					
					System.err.println("Extracting file: " + entry.getName());
					copyInputStream(zipFile.getInputStream(entry),
							new BufferedOutputStream(new FileOutputStream(
									new File(destDir,entry.getName()))));
				}				
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}
	
	public static void unzipSkipFirstLevel(ZipFile zipFile, File destDir) {
		
		Enumeration entries = null;;
		
		try {
		entries = zipFile.entries();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
	
			
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (!entry.isDirectory()) {
					String destFileStr = entry.getName();
					
					destFileStr = (destFileStr.indexOf('/') > 0)
									? destFileStr.substring(destFileStr.indexOf('/')) 
									: null;
					if(destFileStr == null) continue;
					File destFile = new File(destDir, destFileStr);
					File destFileDir = destFile.getParentFile();
					if(!destFileDir.exists()) {
						System.err.println("Extracting directory: " + entry.getName().substring(0, entry.getName().lastIndexOf('/')));
						destFileDir.mkdirs();
					}
					
					System.err.println("Extracting file: " + entry.getName());
					copyInputStream(zipFile.getInputStream(entry),
							new BufferedOutputStream(new FileOutputStream(
									new File(destDir, destFileStr))));
				}				
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}
	
	public static String[] getDirectoryNameByLevel(ZipFile zipFile, int levelNo) {
		
		Set names = new HashSet();
		Enumeration entries;
		
		try {
			
			entries = zipFile.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (!entry.isDirectory()) {
					String fileName = entry.getName();
					String[] components = fileName.split("/");
					
					if(components.length == (levelNo+1)) {
						String dirNam = components[components.length-2];
						names.add(dirNam);
					}
					
					System.out.println(entry.getName());
				}				
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return null;
		}
		
		return (String[])names.toArray(new String[0]);
	}
	
	
	
	
	

}
