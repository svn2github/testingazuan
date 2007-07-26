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
package it.eng.spagobi.importexport.transformers;

import it.eng.spagobi.importexport.ITransformer;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class TransformerFrom1_9To1_9_1 implements ITransformer {

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
                                   "transform", "Error while decompressing 1.9 exported archive" + e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);

		// compress archive
		try {
			content = TransformersUtilities.createExportArchive(pathImpTmpFolder, archiveName);
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), 
					               "transform", "Error while creating creating the export archive " + e);	
		}
		// delete tmp dir content
		File tmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(tmpDir);
		return content;
	}
	
	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		Connection conn = null;
		try{
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			String sql = "";
			Statement stmt = conn.createStatement();
			sql =  "ALTER TABLE sbi_paruse ADD COLUMN SELECTION_TYPE VARCHAR";
			stmt.execute(sql);
			sql =  "UPDATE sbi_paruse SET SELECTION_TYPE='LIST'";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE sbi_paruse ADD COLUMN MULTIVALUE_FLAG SMALLINT";
			stmt.executeUpdate(sql);
			sql =  "UPDATE sbi_paruse SET MULTIVALUE_FLAG=0";
			stmt.executeUpdate(sql);
			sql =  "ALTER TABLE sbi_parameters ADD COLUMN FUNCTIONAL_FLAG SMALLINT";
			stmt.execute(sql);
			sql =  "UPDATE sbi_parameters SET FUNCTIONAL_FLAG=1";
			stmt.executeUpdate(sql);
			sql =  "CREATE MEMORY TABLE SBI_EVENTS_ROLES(EVENT_ID INTEGER NOT NULL, ROLE_ID INTEGER NOT NULL)";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_events_log ALTER COLUMN ID INTEGER NOT NULL";
			stmt.execute(sql);
			sql =  "ALTER TABLE sbi_events_log ADD COLUMN HANDLER VARCHAR DEFAULT 'it.eng.spagobi.events.handlers.DefaultEventPresentationHandler' NOT NULL";
			stmt.execute(sql);
			conn.commit();
			conn.close();
		} catch (Exception e) {
			SpagoBITracer.critical(ImportExportConstants.NAME_MODULE, this.getClass().getName(), "changeDatabase",
		                           "Error while changing database " + e);	
		}
	}
	

	



	
}
