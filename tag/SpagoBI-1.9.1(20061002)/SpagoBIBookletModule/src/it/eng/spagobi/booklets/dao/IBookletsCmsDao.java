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
/*
 * Created on 13-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.booklets.dao;

import it.eng.spagobi.booklets.bo.ConfiguredBIDocument;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Defines all the methods needed for access contents of booklets into CMS Repository 
 */
public interface IBookletsCmsDao {

	/**
	 * Creates the cms node that holds the booklet configuration and returns the path of the node.
	 * @param pathBiObject
	 * @return the path of the booklet template node
	 */
	public String createNewConfigurationNode(String pathBiObject);
	
	public List getConfiguredDocumentList(String pathBooklet);
	
	public void addConfiguredDocument(String pathBooklet, ConfiguredBIDocument doc);
	
	public void deleteConfiguredDocument(String pathBooklet, String conDocIdentifier);
	
	public ConfiguredBIDocument getConfigureDocument(String pathBooklet, String conDocIdentifier);
	
	public void storeBookletTemplate(String pathBooklet, String templateFileName, byte[] templateContent);
	
	public String getBookletTemplateFileName(String pathBooklet);
	
	public InputStream getBookletTemplateContent(String pathBooklet);
	
	public void storeBookletProcessDefinition(String pathBooklet, String pdFileName, byte[] pdFileContent);
	
	public String getBookletProcessDefinitionFileName(String pathBooklet);
	
	public InputStream getBookletProcessDefinitionContent(String pathBooklet);
	
	public void createStructureForTemplate(String pathBooklet, int numTempParts);
	
	public void storeTemplateImage(String pathBooklet, byte[] image, String docLogicalName, int indexTempPart);
	
	public Map getImagesOfTemplatePart(String pathBooklet, String indPart);
	
	public byte[] getNotesTemplatePart(String pathBooklet, String indPart);
	
	public void storeNote(String pathBooklet, String indPart, byte[] noteContent);
	
	public void storeCurrentPresentationContent(String pathBooklet, byte[] docContent);
	
	public void storeCurrentPresentationContent(String pathBooklet, InputStream docContentIS);
	
	public byte[] getCurrentPresentationContent(String pathBooklet);
	
	public void versionPresentation(String pathBooklet, byte[] presContent, boolean approved);
	
	public List getPresentationVersions(String pathBooklet);
	
	public void deletePresentationVersion(String pathBooklet, String verName);
	
	public byte[] getPresentationVersionContent(String pathBooklet, String verName);
	
	public String getBookletName(String pathBooklet);
	
	public String getBiobjectPath(String pathBooklet);
	
	//public void saveWorkflowConfiguration(String pathBooklet, WorkflowConfiguration workConf);
	
	//public WorkflowConfiguration getWorkflowConfiguration(String pathBooklet);
}