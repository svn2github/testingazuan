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
package it.eng.spagobi.qbe.commons.presentation.tag;

import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.qbe.tree.DTree;
import it.eng.spagobi.qbe.tree.IQbeTree;
import it.eng.spagobi.qbe.tree.QbeTreeBuilder;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeEntityFilter;
import it.eng.spagobi.qbe.tree.filter.IQbeTreeFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityEntityFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeAccessModalityFieldFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeOrderEntityFilter;
import it.eng.spagobi.qbe.tree.filter.QbeTreeSelectEntityFilter;
import it.eng.spagobi.qbe.tree.urlgenerator.IQbeTreeUrlGenerator;
import it.eng.spagobi.qbe.tree.urlgenerator.QbeTreeUrlGenerator;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class NewTreeQbeTag extends QbeTagSupport {
	
	private String actionName;
	private String actionType; // action | page | url | javascript
	private String modality;
	
	public int doStartTag() throws JspException {
		
		try {
			
			String subQueryPrefix = null;
		   	if ( getQuery().isSubqueryModeActive() ){
					String subQueryFieldId = (String)getSessionContainer().getAttribute(QbeConstants.SUBQUERY_FIELD);
					subQueryPrefix =  getQuery().getSubQueryIdForSubQueryOnField(subQueryFieldId);
		   	}
		   	
		   	IQbeTreeUrlGenerator urlGenerator = 
				new QbeTreeUrlGenerator(actionName, actionType, getQbeUrlGenerator(), getRequest());
			
		   	
		   	IQbeTreeEntityFilter entityFilter = new QbeTreeAccessModalityEntityFilter( new QbeTreeOrderEntityFilter() );
		   	if(modality.equalsIgnoreCase("light")) {
		   		entityFilter = new QbeTreeOrderEntityFilter (
		   						new QbeTreeSelectEntityFilter ( 
		   						 new QbeTreeAccessModalityEntityFilter(), getQuery() ) );
		   	} else {
		   		entityFilter = new QbeTreeOrderEntityFilter(
		   						new QbeTreeAccessModalityEntityFilter() );
		   	}
		   	IQbeTreeFieldFilter fieldFilter = new QbeTreeAccessModalityFieldFilter();		   	
		   	QbeTreeFilter treeFilter = new  QbeTreeFilter(entityFilter, fieldFilter);
		   		
			QbeTreeBuilder qbeBuilder = new QbeTreeBuilder(DTree.class.getName(), urlGenerator, treeFilter);
			
		   	
		   	List trees = qbeBuilder.getQbeTrees(getDatamartModel());
		   	StringBuffer script = new StringBuffer();
		   	for(int i = 0; i < trees.size(); i++) {
		   		IQbeTree tree = (IQbeTree)trees.get(i);
		   		script.append(tree.getTreeConstructorScript());
		   		script.append("\n\n\n");
		   	}
	    	  
	    	pageContext.getOut().print( script.toString() );
	      } catch (Exception ex) {
	    	 ex.printStackTrace();
	         throw new JspTagException(getClass().getName() + ": " + ex.getMessage());
	      }
	      return SKIP_BODY;
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}

