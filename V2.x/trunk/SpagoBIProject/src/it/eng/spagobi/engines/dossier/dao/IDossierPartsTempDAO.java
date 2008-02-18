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

import it.eng.spago.error.EMFInternalError;

import java.util.Map;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public interface IDossierPartsTempDAO {
	
	public void storeImage(Integer dossierId, byte[] image, String docLogicalName, int pageNum, Long workflowProcessId) throws EMFInternalError;
	
	public Map getImagesOfDossierPart(Integer dossierId, int pageNum, Long workflowProcessId) throws EMFInternalError;
	
	public byte[] getNotesOfDossierPart(Integer dossierId, int pageNum, Long workflowProcessId) throws EMFInternalError;
	
	public void storeNote(Integer dossierId, int pageNum, byte[] noteContent, Long workflowProcessId) throws EMFInternalError;

}
