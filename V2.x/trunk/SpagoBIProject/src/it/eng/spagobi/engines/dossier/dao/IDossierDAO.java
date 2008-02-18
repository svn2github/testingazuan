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
package it.eng.spagobi.engines.dossier.dao;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.engines.dossier.bo.ConfiguredBIDocument;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Defines all the methods needed for access contents of dossier
 */
public interface IDossierDAO {

	/**
	 * Creates the cms node that holds the booklet configuration and returns the path of the node.
	 * @param pathBiObject
	 * @return the path of the booklet template node
	 */
	//public String createNewConfigurationNode(String pathBiObject);// via
	//**
	public String init(BIObject dossier);
	
	public void storeTemplate(Integer dossierId, String pathTempFolder);
	
	public Integer getDossierId(String pathTempFolder);
	
	public List getConfiguredDocumentList(String pathTempFolder);
	
	public void addConfiguredDocument(ConfiguredBIDocument doc, String pathTempFolder);
	
	public void deleteConfiguredDocument(String docLogicalName, String pathTempFolder);
	
	public ConfiguredBIDocument getConfiguredDocument(String docLogicalName, String pathTempFolder);
	
	public void storePresentationTemplateFile(String templateFileName, byte[] templateContent, String pathTempFolder);
	
	public String getPresentationTemplateFileName(String pathTempFolder);
	
	public InputStream getPresentationTemplateContent(String pathTempFolder);
	
	public void storeProcessDefinitionFile(String pdFileName, byte[] pdFileContent, String pathTempFolder);
	
	public String getProcessDefinitionFileName(String pathTempFolder); 
	
	public InputStream getProcessDefinitionContent(String pathTempFolder);
	//**
	
	//public void createStructureForTemplate(String pathBooklet, int numTempParts);// via
	
//	public String getBiobjectPath(String pathBooklet);

}