package it.eng.spagobi.exoaddins;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupHandler;
import org.exoplatform.services.organization.User;

public class Utilities {

	public static List getExoUserFiltered(List allUser, GroupHandler groupHandler) {
		List filteredUser = new ArrayList();
		try {
			ConfigSingleton conf = ConfigSingleton.getInstance();
			if(conf==null) throw new Exception("Configuration not Found");
			SourceBean secFilterSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
			if(secFilterSB==null) throw new Exception("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER tag not Found");
			String rolePatternFilter = secFilterSB.getCharacters();
	        if(rolePatternFilter==null) throw new Exception("Role filter regular expression not found");
			Pattern pattern = Pattern.compile(rolePatternFilter);
	        Matcher matcher = null;
			
			Iterator iterUser = allUser.iterator();
			Collection groups = null;
			Iterator iterGroup = null;
			while(iterUser.hasNext()) {
				User user = (User)iterUser.next();
				String userName = user.getUserName();
				groups = groupHandler.findGroupsOfUser(userName);
				iterGroup = groups.iterator();
				boolean allowed  = false;
				while(iterGroup.hasNext()) {
					Group group = (Group)iterGroup.next();
					String groupID = group.getId();
					matcher = pattern.matcher(groupID);
					if(matcher.find()){
						allowed  = true;	
						break;
					}
				}
				if(allowed) {
					filteredUser.add(user);
				}
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, Utilities.class.getName(), 
		                        "getExoUserFiltered", "Error while filter exo user list ", e);
			filteredUser = new ArrayList();
		}
		return filteredUser;
		
	}
	
}
