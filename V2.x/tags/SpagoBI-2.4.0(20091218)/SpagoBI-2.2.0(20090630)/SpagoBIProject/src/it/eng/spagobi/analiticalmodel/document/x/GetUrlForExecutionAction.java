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

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.Snapshot;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.dao.ISnapshotDAO;
import it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO;
import it.eng.spagobi.analiticalmodel.document.handlers.ExecutionInstance;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zerbetto Davide
 */
public class GetUrlForExecutionAction extends AbstractSpagoBIAction {

	public static final String SERVICE_NAME = "GET_URL_FOR_EXECUTION_ACTION";

	public static final String SBI_SUBOBJECT_ID = "SBI_SUBOBJECT_ID";
	public static final String SBI_SNAPSHOT_ID = "SBI_SNAPSHOT_ID";
	public static final String PARAMETERS = "PARAMETERS";

	// logger component
	private static Logger logger = Logger.getLogger(GetUrlForExecutionAction.class);

	protected ExecutionInstance executionInstance = null;
	protected UserProfile userProfile = null;

	public void doService() {
		logger.debug("IN");
		try {
			// retrieving execution instance from session, no need to check if user is able to execute the current document
			executionInstance = getContext().getExecutionInstance( ExecutionInstance.class.getName() );
			userProfile = (UserProfile) this.getUserProfile();
			Integer subObjectId = this.getAttributeAsInteger( SBI_SUBOBJECT_ID );
			Integer snapshotId = this.getAttributeAsInteger( SBI_SNAPSHOT_ID );
			JSONObject response = null;
			if (snapshotId != null) {
				response = handleSnapshotExecution(snapshotId);
			} else if (subObjectId != null) {
				response = handleSubObjectExecution(subObjectId);
			} else {
				response = handleNormalExecution();
			}
			try {
				writeBackToClient( new JSONSuccess( response ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			}
		} finally {
			logger.debug("OUT");
		}
	}

	private JSONObject handleSnapshotExecution(Integer snapshotId) {
		logger.debug("IN");
		JSONObject response = new JSONObject();
		try {
			// we are not executing a subobject, so delete subobject if existing
			executionInstance.setSubObject(null);
			ISnapshotDAO dao = null;
			
			try {
				dao = DAOFactory.getSnapshotDAO();
			} catch (EMFUserError e) {
				logger.error("Error while istantiating DAO", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot access database", e);
			}

			Snapshot snapshot = null;
			try {
				snapshot = dao.loadSnapshot(snapshotId);
			} catch (EMFUserError e) {
				logger.error("Snapshot with id = " + snapshotId + " not found", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Scheduled execution not found", e);
			}
			
			BIObject obj = executionInstance.getBIObject();
			if (obj.getId().equals(snapshot.getBiobjId())) {
				executionInstance.setSnapshot(snapshot);
				String url = executionInstance.getSnapshotUrl();
				try {
					response.put("url", url);
				} catch (JSONException e) {
					throw new SpagoBIServiceException("Cannot serialize the url [" + url + "] to the client", e);
				}
			} else {
				throw new SpagoBIServiceException(SERVICE_NAME, "Required scheduled execution is not relevant to current document");
			}
		} finally {
			logger.debug("OUT");
		}
		return response;
	}

	protected JSONObject handleSubObjectExecution(Integer subObjectId) {
		logger.debug("IN");
		JSONObject response = new JSONObject();
		try {
			// we are not executing a snapshot, so delete snapshot if existing
			executionInstance.setSnapshot(null);
			ISubObjectDAO dao = null;
			try {
				dao = DAOFactory.getSubObjectDAO();
			} catch (EMFUserError e) {
				logger.error("Error while istantiating DAO", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot access database", e);
			}

			SubObject subObject = null;
			try {
				subObject = dao.getSubObject(subObjectId);
			} catch (EMFUserError e) {
				logger.error("SubObject with id = " + subObjectId + " not found", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Customized view not found", e);
			}

			Locale locale=this.getLocale();
			
			BIObject obj = executionInstance.getBIObject();
			if (obj.getId().equals(subObject.getBiobjId())) {
				boolean canExecuteSubObject = false;
				if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
					canExecuteSubObject = true;
				} else {
					if (subObject.getIsPublic() || subObject.getOwner().equals(userProfile.getUserId().toString())) {
						canExecuteSubObject = true;
					}
				}
				if (canExecuteSubObject) {
					executionInstance.setSubObject(subObject);
					String url = executionInstance.getSubObjectUrl(locale);
					try {
						response.put("url", url);
					} catch (JSONException e) {
						throw new SpagoBIServiceException("Cannot serialize the url [" + url + "] to the client", e);
					}
				} else {
					throw new SpagoBIServiceException(SERVICE_NAME, "User cannot execute required customized view");
				}
			} else {
				throw new SpagoBIServiceException(SERVICE_NAME, "Required subobject is not relevant to current document");
			}
		} catch (EMFInternalError e) {
			throw new SpagoBIServiceException(SERVICE_NAME, "An internal error has occured", e);
		} finally {
			logger.debug("OUT");
		}
		return response;
	}

	protected JSONObject handleNormalExecution() {
		logger.debug("IN");
		JSONObject response = new JSONObject();
		try {
			// we are not executing a subobject or a snapshot, so delete subobject/snapshot if existing
			executionInstance.setSubObject(null);
			executionInstance.setSnapshot(null);
			JSONObject executionInstanceJSON = this.getAttributeAsJSONObject( PARAMETERS );
			executionInstance.refreshParametersValues(executionInstanceJSON, false);

			Locale locale=this.getLocale();

			List errors = null;
			try {
				errors = executionInstance.getParametersErrors();
			} catch (Exception e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot evaluate errors on parameters validation", e);
			}
			if ( errors != null && errors.size() > 0) {
				// there are errors on parameters validation, send errors' descriptions to the client
				JSONArray errorsArray = new JSONArray();
				Iterator errorsIt = errors.iterator();
				while (errorsIt.hasNext()) {
					EMFUserError error = (EMFUserError) errorsIt.next();
					errorsArray.put(error.getDescription());
				}
				try {
					response.put("errors", errorsArray);
				} catch (JSONException e) {
					throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize errors to the client", e);
				}
			} else {
				// there are no errors, we can proceed, so calculate the execution url and send it back to the client
				String url = executionInstance.getExecutionUrl(locale);

				try {
					response.put("url", url);
				} catch (JSONException e) {
					throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize the url [" + url + "] to the client", e);
				}
			}
		} finally {
			logger.debug("OUT");
		}
		return response;
	}

}
