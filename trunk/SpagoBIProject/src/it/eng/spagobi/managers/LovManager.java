package it.eng.spagobi.managers;

import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.lov.ILovDetail;
import it.eng.spagobi.bo.lov.LovDetailFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.List;

public class LovManager {
    
	/**
	 * Returns all the names of the columns returned by the lov
	 * @param lov the lov to analize
	 * @return List of the columns name (the element of the list are Strings)
	 */
	public List getAllColumnsNames(ModalitiesValue lov) {
		List names = new ArrayList();
		try{
			String lovProvider = lov.getLovProvider();
			ILovDetail lovProvDet = LovDetailFactory.getLovFromXML(lovProvider);
			List viscols = lovProvDet.getVisibleColumnNames();
			List inviscols = lovProvDet.getInvisibleColumnNames();
			names.addAll(viscols);
			names.addAll(inviscols);
		} catch (Exception e) {
			names = new ArrayList();
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getAllColumnsNames", "Error while recovering column names " + e);
		}
		return names;
	}
	
	
	
}
