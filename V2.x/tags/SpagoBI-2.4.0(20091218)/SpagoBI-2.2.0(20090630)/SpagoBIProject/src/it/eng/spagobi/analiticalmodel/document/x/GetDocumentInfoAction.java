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
import it.eng.spagobi.chiron.serializer.SerializationException;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zerbetto Davide
 */
public class GetDocumentInfoAction extends AbstractSpagoBIAction {
	
	public static final String SERVICE_NAME = "GET_DOCUMENT_INFO_ACTION";
	
	// logger component
	private static Logger logger = Logger.getLogger(GetDocumentInfoAction.class);
	
	public void doService() {
		logger.debug("IN");
		
		try {
			String label = this.getAttributeAsString("label");
			BIObject obj = null;
			boolean found = false;
			boolean canSee = false;
			try {
				obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
				found = obj != null;
				if (obj != null) {
					canSee = ObjectsAccessVerifier.canSee(obj, this.getUserProfile());
				}
			} catch (Exception e) {
				logger.error("Error while recovering info about document with label [" + label + "]", e);
				throw new SpagoBIServiceException(SERVICE_NAME, "Error while recovering info about document with label [" + label + "]", e);
			}
			HttpServletRequest httpRequest = getHttpRequest();
			MessageBuilder m = new MessageBuilder();
			Locale locale = m.getLocale(httpRequest);
			try {
				JSONObject result = new JSONObject();
				if (!found) {
					result.put("found", false);
				} else {
					result.put("found", true);
					if (!canSee) {
						result.put("canSee", false);
					} else {
						JSONObject document = (JSONObject) SerializerFactory.getSerializer("application/json").serialize( obj ,locale);
						result.put("document", document);
						result.put("canSee", true);
					}
				}
				writeBackToClient( new JSONSuccess( result ) );
			} catch (IOException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Impossible to write back the responce to the client", e);
			} catch (SerializationException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects", e);
			} catch (JSONException e) {
				throw new SpagoBIServiceException(SERVICE_NAME, "Cannot serialize objects into a JSON object", e);
			}

		} finally {
			logger.debug("OUT");
		}
	}

}
