/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.x;

import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjNote;
import it.eng.spagobi.analiticalmodel.document.dao.IObjNoteDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.BIObjectNotesManager;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Zerbetto Davide
 *
 */
public class SaveNotesAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "SAVE_NOTES_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(SaveNotesAction.class);
	
	public void doService() {
		logger.debug("IN");
		try {
			// retrieving execution instance from session, no need to check if user is able to execute the current document
			ExecutionInstance executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			BIObject obj = executionInstance.getBIObject();
			BIObjectNotesManager objectNManager = new BIObjectNotesManager();
			String execIdentifier = objectNManager.getExecutionIdentifier(obj);
			String previousNotes = this.getAttributeAsString("PREVIOUS_NOTES");
			String notes = this.getAttributeAsString("NOTES");
			
			String resultStr = null;
			
			ObjNote objnote = null;
			try {
				objnote = DAOFactory.getObjNoteDAO().getExecutionNotes(obj.getId(), execIdentifier);
			} catch (Exception e) {
				logger.error("Cannot load notes for document [id: " + obj.getId() + ", label: " + obj.getLabel() + ", name: " + obj.getName() + "]", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot load notes", e);
			}
			String currentNotes = "";
			if (objnote != null) {
				logger.debug("Existing notes found with the same execution identifier");
				byte[] content = objnote.getContent();
				currentNotes = new String(content);
			}
			if (!currentNotes.equals(previousNotes)) {
				logger.debug("Notes have been modified by another user");
				resultStr = "conflict";
			} else {
				logger.debug("Saving notes...");
				try {
					saveNotes(execIdentifier, obj.getId(), notes, objnote);
					logger.debug("Notes saved");
					resultStr = "ok";
				} catch (Exception e) {
					throw new SpagoBIServiceException(SERVICE_NAME, "Error while saving notes", e);
				}
			}
			
			try {
				JSONObject result = new JSONObject();
				result.put("result", resultStr);
				writeBackToClient( new JSONSuccess( result ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			} catch (JSONException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
			}
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void saveNotes(String execIdentifier, Integer objectId, String notes, ObjNote objnote) throws Exception {
		logger.debug("IN");
		try {
			IObjNoteDAO objNoteDAO = DAOFactory.getObjNoteDAO();
			if (objnote != null) {
				objnote.setContent(notes.getBytes());
				objNoteDAO.modifyExecutionNotes(objnote);
			} else {
				objnote = new ObjNote();
				objnote.setContent(notes.getBytes());
				objnote.setExecReq(execIdentifier);
				objNoteDAO.saveExecutionNotes(objectId, objnote);
			}
		} finally {
			logger.debug("OUT");
		}
	}

}
