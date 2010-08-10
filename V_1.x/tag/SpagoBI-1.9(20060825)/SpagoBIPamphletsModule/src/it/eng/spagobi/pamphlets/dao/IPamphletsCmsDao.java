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
package it.eng.spagobi.pamphlets.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.pamphlets.bo.ConfiguredBIDocument;
import it.eng.spagobi.pamphlets.bo.WorkflowConfiguration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Defines all the methods needed for access contents of pamphlets into CMS Repository 
 * @author Fiscato
 */
public interface IPamphletsCmsDao {

	public List getPamphletList(String pathBasePamp);	
	
	public void addPamphlet(String pathBase, String name);
	
	public List getConfiguredDocumentList(String pathPamphlet);
	
	public void addConfiguredDocument(String pathPamphlet, ConfiguredBIDocument doc);
	
	public void deleteConfiguredDocument(String pathPamphlet, String conDocIdentifier);
	
	public ConfiguredBIDocument getConfigureDocument(String pathPamphlet, String conDocIdentifier);
	
	public void storePamphletTemplate(String pathPamphlet, String templateFileName, byte[] templateContent);
	
	public String getPamphletTemplateFileName(String pathPamphlet);
	
	public InputStream getPamphletTemplateContent(String pathPamphlet);
	
	public void createStructureForTemplate(String pathPamphlet, int numTempParts);
	
	public void storeTemplateImage(String pathPamphlet, byte[] image, String docLogicalName, int indexTempPart);
	
	public Map getImagesOfTemplatePart(String pathPamphlet, String indPart);
	
	public byte[] getNotesTemplatePart(String pathPamphlet, String indPart);
	
	public void storeNote(String pathPamphlet, String indPart, byte[] noteContent);
	
	public String getPamphletName(String pathPamp);
	
	public void storeFinalDocument(String pathPamphlet, byte[] docContent);
	
	public byte[] getFinalDocument(String pathPamphlet);
	
	public void deletePamphlet(String pathPamphlet);
	
	public void saveWorkflowConfiguration(String pathPamphlet, WorkflowConfiguration workConf);
	
	public WorkflowConfiguration getWorkflowConfiguration(String pathPamphlet);
}