package it.eng.spagobi.managers;

import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.lov.FixedListDetail;
import it.eng.spagobi.bo.lov.JavaClassDetail;
import it.eng.spagobi.bo.lov.LovResultHandler;
import it.eng.spagobi.bo.lov.QueryDetail;
import it.eng.spagobi.bo.lov.ScriptDetail;
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
			// the lov is a query 
			if(lov.getITypeCd().equals("QUERY")) {
				QueryDetail queryDet = QueryDetail.fromXML(lovProvider);
				String visibleColumns = queryDet.getVisibleColumns();
				String invisibleColumns = queryDet.getInvisibleColumns();
				fillNames(names, visibleColumns, invisibleColumns);	
			}
			// the lov is a fixed list 
			if(lov.getITypeCd().equals("FIX_LOV")) {
				FixedListDetail fixListDet = FixedListDetail.fromXML(lovProvider);
				String visibleColumns = fixListDet.getVisibleColumns();
				String invisibleColumns = fixListDet.getInvisibleColumns();
				fillNames(names, visibleColumns, invisibleColumns);	
			}
			// the lov is a script 
			if(lov.getITypeCd().equals("SCRIPT")) {
				ScriptDetail scriptDetail = ScriptDetail.fromXML(lovProvider);
				String lovResult = scriptDetail.getLovResult();
				LovResultHandler lrh = new LovResultHandler(lovResult);
				String visibleColumns = lrh.getVisibleColumns();
				String invisibleColumns = lrh.getInvisibleColumns();
				fillNames(names, visibleColumns, invisibleColumns);	
			}
			// the lov is a java class 
			if(lov.getITypeCd().equals("JAVA_CLASS")) {
				JavaClassDetail jcd = JavaClassDetail.fromXML(lovProvider);
				String lovResult = jcd.getLovResult();
				LovResultHandler lrh = new LovResultHandler(lovResult);
				String visibleColumns = lrh.getVisibleColumns();
				String invisibleColumns = lrh.getInvisibleColumns();
				fillNames(names, visibleColumns, invisibleColumns);	
			}
		} catch (Exception e) {
			names = new ArrayList();
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "getAllColumnsNames", "Error while recovering column names " + e);
		}
		return names;
	}
	
	
	/**
	 * Fill a list with the names of the visible and invisible columns
	 * @param names List to fill
	 * @param visCols String of visible column names separated by coma
	 * @param invisCols String of invisible column names separated by coma
	 */
	private void fillNames(List names, String visCols, String invisCols) {
		String[] visColumns = visCols.split(",");
	    for(int i = 0; i < visColumns.length; i++) {
	    	String visibleColumn = visColumns[i].trim();
	    	names.add(visibleColumn);
	    }
		if(invisCols != null) {
		    String[] invisColumns = invisCols.split(",");
		    for(int i = 0; i < invisColumns.length; i++) {
		    	String invisibleColumn = invisColumns[i].trim();
		    	names.add(invisibleColumn);
		    }
		}
	}
	
	
}
