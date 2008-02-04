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

import java.util.List;

import it.eng.qbe.javascript.QbeJsTreeBuilder;
import it.eng.qbe.javascript.QbeSelectJsTreeBuilder;
import it.eng.qbe.utility.Utils;
import it.eng.spagobi.qbe.commons.constants.QbeConstants;
import it.eng.spagobi.qbe.commons.urlgenerator.IQbeTreeUrlGenerator;
import it.eng.spagobi.qbe.commons.urlgenerator.QbeTreeUrlGenerator;
import it.eng.spagobi.qbe.tree.presentation.tag.DTree;
import it.eng.spagobi.qbe.tree.presentation.tag.IQbeTree;
import it.eng.spagobi.qbe.tree.presentation.tag.QbeTreeBuilder;
import it.eng.spagobi.qbe.tree.presentation.tag.QbeTreeBuilder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class NewTreeQbeTag extends QbeTagSupport {
	
	private String type;
	
	public int doStartTag() throws JspException {
		
		try {
			IQbeTreeUrlGenerator urlGenerator = new QbeTreeUrlGenerator(getQbeUrlGenerator(), getRequest());
			QbeTreeBuilder qbeBuilder = new QbeTreeBuilder(DTree.class.getName(), urlGenerator);
									   
		   	if ( getQuery().isSubqueryModeActive() ){
					String subQueryFieldId = (String)getSessionContainer().getAttribute(QbeConstants.SUBQUERY_FIELD);
					String subQueryPrefix =  getQuery().getSubQueryIdForSubQueryOnField(subQueryFieldId);
					qbeBuilder.setClassPrefix(subQueryPrefix);
		   	}
		   	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

