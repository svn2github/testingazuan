package it.eng.spagobi;

import java.util.ArrayList;
import java.util.List;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.javaClassLovs.IJavaClassLov;

public class AllEmpClass implements IJavaClassLov {

	public String getValues(IEngUserProfile profile) {
		StringBuffer buf = new StringBuffer();
		buf.append("<rows>");
		buf.append("<row id=\'8\' name=\'Kim\' storeName=\'Store 9\' postitle=\'Store Manager\' depDescr=\'Store Management\' storeid=\'9\' positionid=\'11\' departmentid=\'11\' />");
		buf.append("<row id=\'15\' name=\'Walter\' storeName=\'Store 4\' postitle=\'Store Manager\' depDescr=\'Store Management\' storeid=\'4\' positionid=\'11\' departmentid=\'11\' />");
		buf.append("</rows>");
		return buf.toString();
	}

	public List getNamesOfProfileAttributeRequired() {
		List required = new ArrayList();
		return required;
	}


}
