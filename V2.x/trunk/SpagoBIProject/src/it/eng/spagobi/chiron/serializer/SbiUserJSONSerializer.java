package it.eng.spagobi.chiron.serializer;

import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.profiling.bean.SbiExtUserRoles;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author franceschini
 *
 */
public class SbiUserJSONSerializer implements Serializer {
	private static Logger logger = Logger.getLogger(SbiUserJSONSerializer.class);
	
	public static final String USER_ID = "userId";
	public static final String FULL_NAME = "fullName";
	public static final String ID = "id";

	public Object serialize(Object o, Locale locale)
			throws SerializationException {
		logger.debug("IN");
		JSONObject result = new JSONObject();

		if ( !(o instanceof SbiUser) ) {
			throw new SerializationException("SbiUserJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
		}
		
		try {

			SbiUser sbiUser = (SbiUser)o;
			result.put(ID, sbiUser.getId());
			result.put(USER_ID, sbiUser.getUserId());
			result.put(FULL_NAME, sbiUser.getFullName());
			
			//roles
			Set<SbiExtUserRoles> userRoles = sbiUser.getSbiExtUserRoleses();
			Iterator itRoles = userRoles.iterator();
			JSONArray rolesJSON = new JSONArray();
			rolesJSON.put("roles");

			while(itRoles.hasNext()){
				JSONObject jsonRole = new JSONObject();
				SbiExtUserRoles extRole = (SbiExtUserRoles)itRoles.next();
				Integer roleId= extRole.getId().getExtRoleId();
				Role role = DAOFactory.getRoleDAO().loadByID(roleId);
				jsonRole.put("name", role.getName());
				jsonRole.put("id", role.getId());
				jsonRole.put("description", role.getDescription());
				rolesJSON.put(jsonRole);
			}	
			result.put("userRoles", rolesJSON);
			
			//attributes
			Set<SbiUserAttributes> userAttributes = sbiUser.getSbiUserAttributeses();
			Iterator itAttrs = userAttributes.iterator();
			JSONArray attrsJSON = new JSONArray();
			attrsJSON.put("attributes");

			while(itAttrs.hasNext()){
				JSONObject jsonAttr = new JSONObject();
				SbiUserAttributes userAttr = (SbiUserAttributes)itRoles.next();
				String attrName= userAttr.getSbiAttribute().getAttributeName();
				jsonAttr.put("name", attrName);
				jsonAttr.put("id", userAttr.getSbiAttribute().getAttributeId());
				jsonAttr.put("value", userAttr.getAttributeValue());
				attrsJSON.put(jsonAttr);
			}	
			result.put("userAttributes", attrsJSON);
		} catch (Throwable t) {
			throw new SerializationException("An error occurred while serializing object: " + o, t);
		} finally {
			logger.debug("OUT");
		}
		return result;
	}

}
