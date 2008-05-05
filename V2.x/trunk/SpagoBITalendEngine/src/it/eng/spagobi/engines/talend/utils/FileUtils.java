/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.utils;

import java.io.File;

public class FileUtils {

	/**
	 * Delete directory.
	 * 
	 * @param directory the directory
	 * 
	 * @return true, if successful
	 */
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
						deleteDirectory(file);
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
	
}
