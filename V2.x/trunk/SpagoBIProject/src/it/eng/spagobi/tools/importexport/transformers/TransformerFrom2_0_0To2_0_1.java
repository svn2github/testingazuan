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
package it.eng.spagobi.tools.importexport.transformers;

import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.importexport.ITransformer;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class TransformerFrom2_0_0To2_0_1 implements ITransformer {

	static private Logger logger = Logger.getLogger(TransformerFrom2_0_0To2_0_1.class);
	
	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		logger.debug("IN");
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			logger.error("Error while unzipping 2.0.0 exported archive", e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);
		// compress archive
		try {
			content = TransformersUtilities.createExportArchive(pathImpTmpFolder, archiveName);
		} catch (Exception e) {
			logger.error("Error while creating creating the export archive", e);	
		}
		// delete tmp dir content
		File tmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(tmpDir);
		logger.debug("OUT");
		return content;
	}
	
	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		logger.debug("IN");
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "";
			Statement stmt = conn.createStatement();
			sql =  "ALTER TABLE SBI_DATA_SET ADD COLUMN NUM_ROWS BOOLEAN DEFAULT FALSE";
			stmt.execute(sql);
			sql =  "UPDATE SBI_DATA_SET SET NUM_ROWS=FALSE";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE SBI_DATA_SET ADD COLUMN LANGUAGE_SCRIPT VARCHAR(50) DEFAULT 'groovy'";
			stmt.execute(sql);
			sql =  "UPDATE SBI_DATA_SET SET LANGUAGE_SCRIPT='groovy' where OBJECT_TYPE = 'SbiScriptDataSet'";
			stmt.executeUpdate(sql);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			logger.error("Error while changing database", e);	
		} finally {
			logger.debug("OUT");
		}
	}

}
