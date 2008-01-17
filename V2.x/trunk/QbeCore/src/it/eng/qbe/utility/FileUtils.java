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
package it.eng.qbe.utility;

import java.io.File;

/**
 * @author andrea gioia
 *
 */
public class FileUtils {
	public static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	public static String getQbeDataMartDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath(); //System.getProperty("user.home");
			qbeDataMartDir = baseDirStr + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
	
	public static String getQbeScriptDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-SCRIPT-DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath(); //System.getProperty("user.home");
			qbeDataMartDir = baseDirStr + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
}
