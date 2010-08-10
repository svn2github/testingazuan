package it.eng.spagobi.security;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityProviderUtilities {

	/**
	 * Get all the predefined profile attributes of the user with the given unique identifier passed as String.
	 * The attributes are contained into a configuration file which contains the name 
	 * of the attribute and the test value of the attribute. The test value is used during 
	 * the test of a script that use the attribute. 
	 * 
	 * @return HashMap of the attributes. HashMap keys are profile attribute.
	 * HashMap values are test values. 
	 * 
	 */
	public static HashMap getPredefinedProfileAttributes(String userUniqueIdentifier) {
		SourceBean profileAttrsSB = getProfileAttributesSourceBean();
		HashMap attrsMap = new HashMap();
		if (profileAttrsSB == null) return attrsMap;
		SourceBean userProfileAttrsSB = (SourceBean) profileAttrsSB.getFilteredSourceBeanAttribute("USER-PROFILES.USER", "name", userUniqueIdentifier);
		if (userProfileAttrsSB == null)
			return new HashMap();
		List profileAttrs = userProfileAttrsSB.getAttributeAsList("ATTRIBUTE");
		if (profileAttrs == null || profileAttrs.size() == 0) {
			SpagoBITracer.info("SPAGOBI(ExoSecurityProvider)", SecurityProviderUtilities.class.getName(), "getPredefinedProfileAttributes()", 
				"The user with unique identifer '" + userUniqueIdentifier + 
				"' has no predefined profile attributes.");
			return attrsMap;
		}
		Iterator iterAttrs = profileAttrs.iterator();
		SourceBean attrSB = null;
		String nameattr = null;
		String attrvalue = null;
		while(iterAttrs.hasNext()) {
			attrSB = (SourceBean) iterAttrs.next();
			if (attrSB == null)
				continue;
			nameattr = attrSB.getAttribute("name").toString();
		    attrvalue = attrSB.getAttribute("valuefortest").toString();
		    attrsMap.put(nameattr, attrvalue);
		}
		SpagoBITracer.info("SPAGOBI(ExoSecurityProvider)", SecurityProviderUtilities.class.getName(), "getPredefinedProfileAttributes()", 
				"The user with unique identifer '" + userUniqueIdentifier + 
				"' has the following predefined profile attributes:\n" + attrsMap.toString());
		return attrsMap;
	}
	
	/**
	 * Get all the shared profile attributes of the users.
	 * The attributes are contained into a configuration file which contains the name 
	 * of the attribute and the test value of the attribute. The test value is used during 
	 * the test of a script that use the attribute. 
	 * 
	 * @return HashMap of the attributes. HashMap keys are profile attribute.
	 * HashMap values are test values. 
	 * 
	 */
	public static HashMap getAllSharedProfileAttributes() {
		SourceBean profileAttrsSB = getProfileAttributesSourceBean();
		HashMap attrsMap = new HashMap();
		if (profileAttrsSB == null) return attrsMap;
		List attrs = profileAttrsSB.getAttributeAsList("ATTRIBUTE");
		if(attrs==null) {
			return attrsMap;
		}
		Iterator iterAttrs = attrs.iterator();
		SourceBean attrSB = null;
		String nameattr = null;
		String attrvalue = null;
		while(iterAttrs.hasNext()) {
			attrSB = (SourceBean)iterAttrs.next();
			if(attrSB==null)
				continue;
			nameattr = (String)attrSB.getAttribute("name");
		    attrvalue = (String)attrSB.getAttribute("valuefortest");
		    attrsMap.put(nameattr, attrvalue);
		}
		return attrsMap;
	}
	
	public static SourceBean getProfileAttributesSourceBean() {
		SourceBean profileAttributesSB = (SourceBean) ConfigSingleton.getInstance().getAttribute("EXO_PORTAL_SECURITY.PROFILE_ATTRIBUTES");
		if (profileAttributesSB == null) {
			SpagoBITracer.critical("SPAGOBI(ExoSecurityProvider)", SecurityProviderUtilities.class.getName(), "getProfileAttributesSourceBean()", 
					"There is not the needed EXO_PORTAL_SECURITY.PROFILE_ATTRIBUTES attribute in the ConfigSingleton!!");
			return null;
		} else return profileAttributesSB;
		
	}
	
	public void debug(Class classErr, String nameMeth, String message){
		SpagoBITracer.debug("SPAGOBI(ExoSecurityProvider)",
	            			classErr.getName(),
	            			nameMeth,
	            			message);
	}
	public  Pattern getFilterPattern(){
		ConfigSingleton config = ConfigSingleton.getInstance();
		debug(this.getClass(), "init", "Spago configuration retrived ");
		SourceBean secFilterSB = (SourceBean)config.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
		debug(this.getClass(), "init", "source bean filter retrived " + secFilterSB);
        String rolePatternFilter = secFilterSB.getCharacters();
        debug(this.getClass(), "init", "filter string retrived " + rolePatternFilter);
        Pattern pattern = Pattern.compile(rolePatternFilter);
        debug(this.getClass(), "init", "regular expression pattern compiled " + pattern);
        return pattern;
		
	}
}
