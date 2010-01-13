package it.eng.spagobi.profiling.services;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.x.AbstractSpagoBIAction;
import it.eng.spagobi.analiticalmodel.document.x.SaveMetadataAction;
import it.eng.spagobi.chiron.serializer.SerializerFactory;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.messages.MessageBuilder;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bo.UserBO;
import it.eng.spagobi.profiling.dao.ISbiUserDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageUserAction extends AbstractSpagoBIAction {
	// logger component
	private static Logger logger = Logger.getLogger(SaveMetadataAction.class);
	private final String MESSAGE_DET = "MESSAGE_DET";
	// type of service
	private final String USERS_LIST = "USERS_LIST";
	private final String USER_DETAIL = "USER_DETAIL";
	private final String USER_INSERT = "USER_INSERT";
	private final String USER_UPDATE = "USER_UPDATE";
	private final String USER_DELETE = "USER_DELETE";

	// USER detail
	private final String ID = "ID";
	private final String USER_ID = "USER_ID";
	private final String FULL_NAME = "FULL_NAME";
	private final String PASSWORD = "PASSWORD";
	private final String CONFIRM_PASSWORD = "CONFIRM_PASSWORD";
	
	private final String ROLES = "ROLES";
	private final String ATTRIBUTES = "ATTRIBUTES";


	@Override
	public void doService() {
		logger.debug("IN");
		ISbiUserDAO userDao;
		try {
			userDao = DAOFactory.getSbiUserDAO();
		} catch (EMFUserError e1) {
			throw new SpagoBIServiceException(SERVICE_NAME,	"Please enter user name");
		}
		HttpServletRequest httpRequest = getHttpRequest();
		MessageBuilder m = new MessageBuilder();
		Locale locale = m.getLocale(httpRequest);

		String serviceType = this.getAttributeAsString(MESSAGE_DET);

		if (serviceType != null && serviceType.equalsIgnoreCase(USERS_LIST)) {
			try {
				ArrayList<UserBO> users = userDao.loadUsers();

				JSONArray usersJSON = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(users,	locale);
				JSONObject usersResponseJSON = createJSONResponseUsers(usersJSON);

				writeBackToClient(new JSONSuccess(usersResponseJSON));

			} catch (Throwable e) {
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving users", e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(USER_DETAIL)) {
			Integer id = getAttributeAsInteger(ID);
			try {
				SbiUser user = userDao.loadSbiUserById(id);

				JSONObject userJSON = (JSONObject) SerializerFactory.getSerializer("application/json").serialize(user,locale);

				writeBackToClient(new JSONSuccess(userJSON));

			} catch (Throwable e) {
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving user detail",
						e);
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(USER_INSERT)) {
			String userId = getAttributeAsString(USER_ID);
			String fullName = getAttributeAsString(FULL_NAME);
			String password = getAttributeAsString(PASSWORD);
			String confirmPwd = getAttributeAsString(CONFIRM_PASSWORD);
			if (userId != null) {
				if(password == null || confirmPwd == null || !password.equals(confirmPwd)){
					throw new SpagoBIServiceException(SERVICE_NAME,	"Please check password!");
				}
				SbiUser user = new SbiUser();
				user.setUserId(userId);
				user.setFullName(fullName);
				user.setPassword(password);
				try {
					Integer id = userDao.saveSbiUser(user);

					writeBackToClient( new JSONAcknowledge("Operazion succeded") );

				} catch (Throwable e) {
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception occurred while saving new user",
							e);
				}
			}else{
				throw new SpagoBIServiceException(SERVICE_NAME,	"Please enter user name");
			}
		} else if (serviceType != null	&& serviceType.equalsIgnoreCase(USER_DELETE)) {
			Integer id = getAttributeAsInteger(ID);
			try {
				userDao.deleteSbiUserById(id);

				writeBackToClient( new JSONAcknowledge("Operazion succeded") );

			} catch (Throwable e) {
				throw new SpagoBIServiceException(SERVICE_NAME,
						"Exception occurred while retrieving user to delete",
						e);
			}
		}else if (serviceType != null	&& serviceType.equalsIgnoreCase(USER_UPDATE)) {
			Integer id = getAttributeAsInteger(ID);
			String userId = getAttributeAsString(USER_ID);
			String fullName = getAttributeAsString(FULL_NAME);
			String password = getAttributeAsString(PASSWORD);
			String confirmPwd = getAttributeAsString(CONFIRM_PASSWORD);
			
			ArrayList<String> roles = (ArrayList<String>)getAttributeAsStringList(ROLES);//roles ID
			JSONArray attributesJSON = getAttributeAsJSONArray(ATTRIBUTES);//attributes ID-value
			
			if (userId != null) {
				if(password == null || confirmPwd == null || !password.equals(confirmPwd)){
					throw new SpagoBIServiceException(SERVICE_NAME,	"Please check password!");
				}
				try {
					HashMap<Integer, String> attrList = deserializeJSONArray(attributesJSON);
					try {
						userDao.fullUpdateSbiUser(id, password, fullName, roles, attrList);

						writeBackToClient( new JSONAcknowledge("Operazion succeded") );

					} catch (Throwable e) {
						throw new SpagoBIServiceException(SERVICE_NAME,
								"Exception occurred while saving new user",
								e);
					}
				
				} catch (JSONException e) {
					throw new SpagoBIServiceException(SERVICE_NAME,
							"Exception occurred while updating user",
							e);
				}

			}else{
				throw new SpagoBIServiceException(SERVICE_NAME,	"Please enter user name");
			}
		}
		logger.debug("OUT");

	}

	/**
	 * Creates a json array with children users informations
	 * 
	 * @param rows
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONResponseUsers(JSONArray rows)
			throws JSONException {
		JSONObject results;

		results = new JSONObject();
		results.put("title", "Users");
		results.put("samples", rows);
		return results;
	}
	
	private HashMap<Integer, String> deserializeJSONArray(JSONArray rows) throws JSONException{
		HashMap<Integer, String> toReturn = new HashMap<Integer, String>();
		for(int i=0; i< rows.length(); i++){
			JSONObject obj = (JSONObject)rows.get(i);
			Iterator it = obj.keys();
			while(it.hasNext()){
				String key = (String)it.next();
				String value =(String)obj.get(key);
				toReturn.put(Integer.valueOf(key.trim()), value);
			}
		}	
		return toReturn;
	}

}
