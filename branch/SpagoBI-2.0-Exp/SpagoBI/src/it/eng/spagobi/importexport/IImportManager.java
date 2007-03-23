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

import java.util.List;

public interface IImportManager {

	/**
	 * Prepare the environment for the import procedure
	 * @param pathImpTmpFold The path of the temporary import folder
	 * @param archiveName the name of the compress exported file
	 * @param archiveContent the bytes of the compress exported file 
	 */
	public void prepareImport(String pathImportTmpFold, String archiveName, byte[] archiveContent) throws EMFUserError;
	
	/**
	 * Gets the SpagoBI version of the exported file
	 * @return The SpagoBI version of the exported file
	 */
	public String getExportVersion();
	
	/**
	 * Gets the current SpagobI version
	 * @return The current SpagoBI version
	 */
	public String getCurrentVersion();
	
	/**
	 * Imports the exported objects
	 */
	public void importObjects() throws EMFUserError;
	
	/**
	 * Gets the list of all exported roles
	 * @return The list of exported roles
	 */
	public List getExportedRoles() throws EMFUserError;
	
	/**
	 * Gets the list of all exported engines
	 * @return The list of exported engines
	 */
	public List getExportedEngines() throws EMFUserError;
	
	/**
	 * Gets the list of exported connections
	 * @return List of the exported connections
	 */
	public List getExportedConnections() throws EMFUserError;
	
	/**
	 * Commits all changes made on exported and current databases
	 * @return String, the path of the log file
	 */
	public ImportResultInfo commitAllChanges() throws EMFUserError;
	
    /**
     * Check the existance of the exported metadata into the current system metadata
     * and insert their associations into the association object MeatadataAssociation
     */
	public void checkExistingMetadata() throws EMFUserError;
	
	/**
	 * Ends the import procedure
	 */
	public void stopImport();
	
    /**
     * Gets the object which contains the association between exported metadata 
     * and the current system metadata
     * @return MetadataAssociation the object which contains the association 
     * between exported metadata and the current system metadata
     */
	public MetadataAssociations getMetadataAssociation();

}
