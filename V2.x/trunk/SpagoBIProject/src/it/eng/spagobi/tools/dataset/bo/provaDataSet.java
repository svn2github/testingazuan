package it.eng.spagobi.tools.dataset.bo;

import it.eng.spago.security.IEngUserProfile;

import java.util.List;

public class provaDataSet implements IJavaClassDataSet {

	public List getNamesOfProfileAttributeRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValues(IEngUserProfile profile) {
	
		String result = "<ROWS>";
		result += "<ROW VALUE=\"";
		int i = 2*100;
		result += new Integer (i).toString() +"\"/>";
		result += "</ROWS>";
		return result;
		
	}

}
