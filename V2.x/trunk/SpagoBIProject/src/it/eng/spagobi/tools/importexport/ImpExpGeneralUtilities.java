/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.tools.importexport;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Implements general utilities for import / export purposes
 */
public class ImpExpGeneralUtilities {

    static private Logger logger = Logger.getLogger(ImpExpGeneralUtilities.class);

    /**
     * Delete a folder and its contents.
     * 
     * @param dir The java file object of the directory
     * 
     * @return the result of the operation
     */
    public static boolean deleteDir(File dir) {
	logger.debug("IN");
	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    return false;
		}
	    }
	}
	logger.debug("OUT");
	return dir.delete();
    }

}
