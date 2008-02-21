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

/**
 * Defines all the methods needed for access contents of dossier template.<br>
 * Dossier template is a zip file containing the presentation template (.ppt), <br>
 * the process definition file (.xml file) and dossier configuration file (.sbidossier file).<br>
 * The first operation you must perform is to unzipped the template using the <code>init</code> method: <br>
 * this method returns the path of the temporary folder where template was unzipped:<br>
 * this path is built as <b>BASE_TEMP_FOLDER + DOSSIER_ID + UUID</b> where:<br>
 * BASE_TEMP_FOLDER is the temporary base folder configured in <b>dossier.xml</b>;<br>
 * DOSSIER_ID is the document id;<br>
 * UUID is a time based random string.<br>
 * You have to keep this path in order to invoke other methods (those methods work on this folder).
 * The <code>storeTemplate</code> method builds the new zip template file and saves it into the database.
 * Then you can invoke <code>clean</code> method in order to delete temporary folder.
 * 
 * @author Zerbetto (davide.zerbetto@eng.it) 
 */
public interface IDossierDAO {

	public String init(BIObject dossier);
	
	public void clean(String pathTempFolder);
	
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

}