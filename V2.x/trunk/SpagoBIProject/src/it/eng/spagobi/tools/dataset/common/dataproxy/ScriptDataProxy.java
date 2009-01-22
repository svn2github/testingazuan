/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagobi.tools.dataset.common.dataproxy;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.behaviouralmodel.lov.handlers.ScriptManager;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ScriptDataProxy extends AbstractDataProxy {

	String script;

	private static transient Logger logger = Logger.getLogger(ScriptDataProxy.class);


	public ScriptDataProxy() {

	}

	public ScriptDataProxy(String script) {
		setScript( script );
	}


	public Object load(String statement) throws EMFUserError {
		if(statement != null) {
			setScript(statement);
		}
		return load();
	}
	

	public Object load() throws EMFUserError {
		String data = null;
		try {
			data = ScriptManager.runScript(script);

			// check if the result must be converted into the right xml sintax
			boolean toconvert = checkSintax(data);
			if(toconvert) { 
				data = convertResult(data);
			}
		} catch (SourceBeanException e) {
			logger.error("SourceBeanException",e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("script languaga not proper");
			EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 9216);						
			throw userError;
		}
		logger.debug("OUT");

		return data;
	}

	private boolean checkSintax(String result) {

		logger.debug("IN");
		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";

		boolean toconvert = false;
		try{
			SourceBean source = SourceBean.fromXMLString(result);
			if(!source.getName().equalsIgnoreCase("ROWS")) {
				toconvert = true;
			} else {
				List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
				if( (rowsList==null) || (rowsList.size()==0) ) {
					toconvert = true;
				} else {
					// TODO this part can be moved to the import transformer
					// RESOLVES RETROCOMPATIBILITY PROBLEMS
					// finds the name of the first attribute of the rows if exists 
					String defaultName = "";
					SourceBean rowSB = (SourceBean) rowsList.get(0);
					List attributes = rowSB.getContainedAttributes();
					if (attributes != null && attributes.size() > 0) {
						SourceBeanAttribute attribute = (SourceBeanAttribute) attributes.get(0);
						defaultName = attribute.getKey();
					}
					// if a value column is specified, it is considered
					SourceBean valueColumnSB = (SourceBean) source.getAttribute("VALUE-COLUMN");
					if (valueColumnSB != null) {
						String valueColumn = valueColumnSB.getCharacters();
						if (valueColumn != null) {
							valueColumnName = valueColumn;
						}
					} else {
						valueColumnName = defaultName;
					}
					SourceBean visibleColumnsSB = (SourceBean) source.getAttribute("VISIBLE-COLUMNS");
					if (visibleColumnsSB != null) {
						String allcolumns = visibleColumnsSB.getCharacters();
						if (allcolumns != null) {
							String[] columns = allcolumns.split(",");
							visibleColumnNames = Arrays.asList(columns);
						}
					} else {
						String[] columns = new String[] {defaultName};
						visibleColumnNames = Arrays.asList(columns);
					}
					SourceBean descriptionColumnSB = (SourceBean) source.getAttribute("DESCRIPTION-COLUMN");
					if (descriptionColumnSB != null) {
						String descriptionColumn = descriptionColumnSB.getCharacters();
						if (descriptionColumn != null) {
							descriptionColumnName = descriptionColumn;
						}
					} else {
						descriptionColumnName = defaultName;
					}
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("the result of the dataset is not formatted with the right structure so it will be wrapped inside an xml envelope",e);
			toconvert = true;
		}
		logger.debug("OUT");
		return toconvert;
	}

	private String convertResult(String result) {

		logger.debug("IN");

		List visibleColumnNames = null;
		String valueColumnName = "";
		String descriptionColumnName = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result +"\"/>");
		sb.append("</ROWS>");
		descriptionColumnName = "VALUE";
		valueColumnName = "VALUE";
		String [] visibleColumnNamesArray = new String [] {"VALUE"};
		visibleColumnNames = Arrays.asList(visibleColumnNamesArray);

		logger.debug("OUT");
		return sb.toString();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}


}
