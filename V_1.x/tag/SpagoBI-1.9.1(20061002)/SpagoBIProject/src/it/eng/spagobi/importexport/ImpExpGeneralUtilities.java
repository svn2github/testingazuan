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
package it.eng.spagobi.importexport;

import it.eng.spago.error.EMFUserError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Implements general utilities for import / export purposes
 */
public class ImpExpGeneralUtilities {

	/**
	 * Delete a folder and its contents
	 * @param dir The java file object of the directory 
	 * @return the result of the operation 
	 */ 
	public static boolean deleteDir(File dir) {
		 if(dir.isDirectory()) {
			 String[] children = dir.list();
			 for (int i=0; i<children.length; i++) {
				 boolean success = deleteDir(new File(dir, children[i]));
				 if (!success) {
					 return false;
				 }
			 }
		 }
		 return dir.delete();
	 }
	
	
	
}
