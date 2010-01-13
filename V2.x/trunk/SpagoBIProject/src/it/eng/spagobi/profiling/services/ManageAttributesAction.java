/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.profiling.services;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.profiling.bean.SbiAttribute;
import it.eng.spagobi.profiling.dao.ISbiAttributeDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageAttributesAction extends AbstractSpagoBIAction{
	
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String ATTR_LIST = "ATTR_LIST";
	private final String ATTR_INSERT = "ATTR_INSERT";
	private final String ATTR_DELETE = "ATTR_DELETE";
	
	private final String ID = "ID";
	private final String NAME = "NAME";
	private final String DESCRIPTION = "DESCRIPTION";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3524157303709604995L;
	// logger component
	private static Logger logger = Logger.getLogger(ManageAttributesAction.class);
	
	@Override
	public void doService() {
		logger.debug("IN");
		ISbiAttributeDAO attrDao;
		try {
			attrDao = DAOFactory.getSbiAttributeDAO();
		} catch (EMFUserError e1) {
			logger.error(e1.getMessage(), e1);
			throw new SpagoBIServiceException(SERVICE_NAME,	"Error occurred");
		}
		HttpServletRequest httpRequest = getHttpRequest();
		MessageBuilder m = new MessageBuilder();
		Locale locale = m.getLocale(httpRequest);

		String serviceType = this.getAttributeAsString(MESSAGE_DET);
		logger.debug("Service type "+serviceType);
		if (serviceType != null && serviceType.equalsIgnoreCase(ATTR_LIST)) {
			try {
				ArrayList<SbiAttribute> attributes = (ArrayList<SbiAttribute>)attrDao.loadSbiAttributes();
				logger.debug("Loaded attributes list");
				JSONArray attributesJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(attributes,	locale);
				JSONObject attributesResponseJSON = createJSONResponseAttributes(attributesJSON);

				writeBackToClient(new JSONSuccess(attributesResponseJSON));

			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving attributes", e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(ATTR_INSERT)) {
			String name = getAttributeAsString(NAME);
			String description = getAttributeAsString(DESCRIPTION);

			if (name != null) {
				SbiAttribute attribute = new SbiAttribute();
				attribute.setDescription(description);
				attribute.setAttributeName(name);
				
				try {
					attrDao.saveSbiAttribute(attribute);
					logger.debug("New attribute inserted");
					writeBackToClient( new JSONAcknowledge("Operazion succeded") );

				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception occurred while saving new attribute",
							e);
				}
			}else{
				logger.error("Missing attribute name");
				throw new SpagoBIServiceException(SERVICE_NAME,	"Please enter attribute name");
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(ATTR_DELETE)) {
			Integer id = getAttributeAsInteger(ID);
			try {
				attrDao.deleteSbiAttributeById(id);
				logger.debug("Attribute deleted");
				writeBackToClient( new JSONAcknowledge("Operazion succeded") );

			} catch (Throwable e) {
				logger.error("Exception occurred while deleting attribute", e);
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while deleting attribute",
						e);
			}
		}
		logger.debug("OUT");
		
	}
	/**
	 * Creates a json array with children attributes informations
	 * 
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponseAttributes(JSONArray rows)
			throws JSONException {
		JSONObject results;

		results = new JSONObject();
		results.put("title", "Attributes");
		results.put("samples", rows);
		return results;
	}
}
