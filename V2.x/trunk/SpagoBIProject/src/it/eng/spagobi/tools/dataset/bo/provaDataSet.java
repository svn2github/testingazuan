package it.eng.spagobi.tools.dataset.bo;

import it.eng.spago.security.IEngUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class provaDataSet implements IJavaClassDataSet {


	public String getValues(IEngUserProfile profile, HashMap map) {
	
		String result = "<ROWS>";
		result += "<ROW VALUE=\"";
		int i = 2*100;
		result += new Integer (i).toString() +"\"/>";
		result += "</ROWS>";
		return result;
		
	}

	public List getNamesOfProfileAttributeRequired(){
		List a=new ArrayList();
		a.add("month");
		return a;
	}
	
	
}
