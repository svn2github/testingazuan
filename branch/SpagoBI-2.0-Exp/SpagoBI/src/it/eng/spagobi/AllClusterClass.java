package it.eng.spagobi;

import java.util.ArrayList;
import java.util.List;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.javaClassLovs.IJavaClassLov;

public class AllClusterClass implements IJavaClassLov {

	public String getValues(IEngUserProfile profile) {
		StringBuffer buf = new StringBuffer();
		buf.append("<rows>");
		buf.append("<row name=\'Cluster1\' value=\'Cluster1\' />");
		buf.append("<row name=\'Cluster7\' value=\'Cluster7\' />");
		buf.append("<row name=\'Cluster5\' value=\'Cluster5\' />");
		buf.append("<row name=\'Cluster8\' value=\'Cluster8\' />");
		buf.append("<row name=\'Cluster4\' value=\'Cluster4\' />");
		buf.append("<row name=\'Cluster9\' value=\'Cluster9\' />");
		buf.append("<row name=\'Cluster6\' value=\'Cluster6\' />");
		buf.append("</rows>");
		return buf.toString();
	}

	public List getNamesOfProfileAttributeRequired() {
		List required = new ArrayList();
		required.add("topogigio");
		required.add("zorro");
		return required;
	}


}
