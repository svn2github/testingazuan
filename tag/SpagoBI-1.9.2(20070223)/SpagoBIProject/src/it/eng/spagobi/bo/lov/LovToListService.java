/**
 * 
 */
package it.eng.spagobi.bo.lov;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.commons.DelegatedBasicListService;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

/**
 * @author Gioia
 *
 */
public class LovToListService {
	private String lovResult = null;
	
	public LovToListService(String lovResult) {
		this.lovResult = lovResult;
	}
		
	public ListIFace getLovAsListService() throws Exception {
		ListIFace list = null;	
				
		LovResultHandler lovResultHandler = new LovResultHandler( getLovResult() );
		SourceBean lovResultSB = lovResultHandler.getLovResultSB();
		
		PaginatorIFace paginator = new GenericPaginator();
		
		int numRows = 10;
		try{
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			String lookupnumRows = (String)spagoconfig.getAttribute("SPAGOBI.LOOKUP.numberRows");
			if(lookupnumRows!=null) {
				numRows = Integer.parseInt(lookupnumRows);
			}
		} catch(Exception e) {
			numRows = 10;
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "getListServiceBaseConfig", "Error while recovering number rows for " +
					            "lookup from configuration, usign default 10", e);
		}
		paginator.setPageSize(numRows);
		
		List rows = null;
		
		if (lovResultSB != null) {
			rows = lovResultSB.getAttributeAsList("ROW");
			for (int i = 0; i < rows.size(); i++)
				paginator.addRow(rows.get(i));
		}
		list = new GenericList();
		list.setPaginator(paginator);
		
		return list;
	}

	
	public SourceBean getListServiceBaseConfig(String title) throws Exception {
		SourceBean config = null;
				 
		LovResultHandler lovResultHandler = new LovResultHandler( getLovResult() );
				
		List visibleColumns = lovResultHandler.getVisibleColumnsAsList ();					
		String valueColumn = lovResultHandler.getValueColumn();
		
		String moduleConfigStr = "";
		moduleConfigStr += "<CONFIG rows=\"10\" title=\"" + title + "\">";
		moduleConfigStr += "	<KEYS>";
		moduleConfigStr += "		<OBJECT key='"+ valueColumn +"'/>";
		moduleConfigStr += "	</KEYS>";
		moduleConfigStr += "	<QUERIES/>";
		moduleConfigStr += "</CONFIG>";
		config = SourceBean.fromXMLString(moduleConfigStr);
		
		SourceBean columnsSB = createColumnsSB(visibleColumns);
		config.setAttribute(columnsSB);
		
		return config;
	}
	
	private SourceBean createColumnsSB(List columns) throws SourceBeanException {
		if (columns == null || columns.size() == 0) return new SourceBean("COLUMNS");
		String columnsStr = "<COLUMNS>";
		for (int i = 0; i < columns.size(); i++) {
			columnsStr += "	<COLUMN name=\"" + columns.get(i).toString() + "\" />";
		}
		columnsStr += "</COLUMNS>";
		SourceBean columnsSB = SourceBean.fromXMLString(columnsStr);
		return columnsSB;
	}
	
	
	public String getLovResult() {
		return lovResult;
	}

	public void setLovResult(String lovResult) {
		this.lovResult = lovResult;
	}
	

}
