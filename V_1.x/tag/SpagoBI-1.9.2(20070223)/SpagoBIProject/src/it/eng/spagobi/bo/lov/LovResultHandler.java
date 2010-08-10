/**
 * 
 */
package it.eng.spagobi.bo.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;

/**
 * @author Gioia
 *
 */
public class LovResultHandler {
	private SourceBean lovResultSB = null;
	
	public LovResultHandler(String lovResult) throws SourceBeanException {
		lovResultSB= SourceBean.fromXMLString(lovResult);
	}
	
	public String  getValueColumn() {
		String valueColumns = null;
		SourceBean valCol = (SourceBean)lovResultSB.getAttribute("VALUE-COLUMN");
		if(valCol != null) valueColumns = valCol.getCharacters();
		return valueColumns;
	}
	
	public String  getDescriptionColumn() {
		String descriptionColumns = null;
		SourceBean valCol = (SourceBean)lovResultSB.getAttribute("DESCRIPTION-COLUMN");
		if(valCol != null) descriptionColumns = valCol.getCharacters();
		
		// just for back compatibility purpose
		if(descriptionColumns == null) descriptionColumns = getValueColumn();
		
		return descriptionColumns;
	}
	
	public String getVisibleColumns() {
		String visibleColumns = null;
		SourceBean valCol = (SourceBean)lovResultSB.getAttribute("VISIBLE-COLUMNS");
		if(valCol != null) visibleColumns = valCol.getCharacters();
		return visibleColumns;
	}
		
	public List getVisibleColumnsAsList () {		
		String visibleColumns = getVisibleColumns();
		
		if (visibleColumns == null || visibleColumns.trim().equals("")) return new ArrayList ();
		
		StringTokenizer strToken = new StringTokenizer(visibleColumns, ",");
		List columns = new ArrayList();
		while (strToken.hasMoreTokens()) {
			String val = strToken.nextToken().trim();
			columns.add(val);
		}
		return columns;
	}
	
	public List getRows() {
		return lovResultSB.getAttributeAsList("ROW");
	}
	
	public SourceBean getRow(String value) {
		return (SourceBean)lovResultSB.getFilteredSourceBeanAttribute("ROW", getValueColumn(), value);
	}
	
	public boolean isSingleValue() {
		return (getRows().size() == 1);
	}
	
	public List getValues() {
		List values = new ArrayList();
		List rows = getRows();
		for(int i = 0; i < rows.size(); i++) {
			SourceBean row = (SourceBean)rows.get(i);
			values.add(row.getAttribute(getValueColumn()));
		}
		return values;
	}
	
	public boolean containsValue(String value) {
		List values = getValues();
		for(int i = 0; i < values.size(); i++)
			if(values.get(i).toString().equalsIgnoreCase(value)) return true;
		
		return false;
	}
	
	public SourceBean getLovResultSB() {
		return lovResultSB;
	}
}
