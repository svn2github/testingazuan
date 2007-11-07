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
package it.eng.spagobi.tools.importexport;

import it.eng.spago.error.EMFUserError;

import java.util.List;
import java.util.Map;

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
	
	
	/**
     * Gets the object which contains only the association set by user between exported metadata 
     * and the current system metadata
     * @return MetadataAssociation the object which contains the association, set by user, 
     * between exported metadata and the current system metadata
     */
	public UserAssociationsKeeper getUserAssociation();
	
	
	/**
	 * checks if two or more exported roles are associate to the same current role
	 * @param roleAssociations Map of association between exported roles and 
	 * roles of the portal in use
	 * @throws EMFUserError if two ore more exported roles are associate
	 * to the same current role
	 */
	public void checkRoleReferences(Map roleAssociations) throws EMFUserError;
	
	
	
	/**
	 * Get an existing object identified by the id and the class
	 * @param id The Object id
	 * @param objClass The class of the object
	 * @param tx Hibernate transaction for the current database
	 * @param session Hibernate session for the current database
	 * @return The existing hibernate object
	 */		
	public Object getExistingObject(Integer id, Class objClass);
	
	
	
	/**
	 * Get an exported object identified by the id and the class
	 * @param id The Object id
	 * @param objClass The class of the object
	 * @param tx Hibernate transaction for the exported database
	 * @param session Hibernate session for the exported database
	 * @return The existing hibernate object
	 */		
	public Object getExportedObject(Integer id, Class objClass);
	

}
