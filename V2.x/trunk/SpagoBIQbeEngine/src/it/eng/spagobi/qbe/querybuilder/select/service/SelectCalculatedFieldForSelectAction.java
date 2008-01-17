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
package it.eng.spagobi.qbe.querybuilder.select.service;

import java.io.File;

import it.eng.qbe.javascript.QbeJsTreeNodeId;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;


/**
 * @author Andrea Zoppello
 * 
 * This Action is responsible to put a field in Select  Clause of the object ISingleDataMartWizardObject in session
 */
public class SelectCalculatedFieldForSelectAction extends AbstractQbeEngineAction {
	
	// valid input parameter names
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String CFIELD_ID = "CFIELD_ID";
	public static final String CFIELD_COMPLETE_NAME = "CFIELD_COMPLETE_NAME";

	
	public void service(SourceBean request, SourceBean response) {	
				
		String className = (String)request.getAttribute(CLASS_NAME);
		String cFieldId = (String)request.getAttribute(CFIELD_ID);
		String cFieldNameInQuery = (String)request.getAttribute(CFIELD_COMPLETE_NAME);
		
		
		
		
		
		String subQueryPrefix = null;
		if (isSubqueryModeActive()){
			subQueryPrefix = getMainDataMartWizard().getSubQueryIdForSubQueryOnField( getSubqueryField() );
		}
		
		try {
			CalculatedField calculatedField = getDatamartModel().getFormula().getCalculatedField(cFieldId);
				
			calculatedField.setFldCompleteNameInQuery(cFieldNameInQuery);
			String mappings = calculatedField.getMappings();
			String[] mappingArray = mappings.split(",");
			
			
			Integer positionInteger = new Integer(-1);
			String prefix = null;
			if ( cFieldNameInQuery.indexOf(".") > 0 ){
				prefix = cFieldNameInQuery.substring(0, cFieldNameInQuery.lastIndexOf("."));
			}
			
			
			for ( int i =0; i < mappingArray.length; i++){
				String[] splitMapping = mappingArray[i].split("->");
				String requiredField = splitMapping[0];
				
				String completeRequiredFieldName = requiredField;
				if (prefix != null){
					completeRequiredFieldName = prefix + "." + requiredField;
				}
			
				calculatedField.setClassNameInQuery(className);
				positionInteger = Utils.findPositionOf(getActiveDataMartWizard(), className + "." + completeRequiredFieldName);
				
				SessionFactory sf = getDatamartModel().getDataSource().getSessionFactory();
				
				ClassMetadata cmd = sf.getClassMetadata(calculatedField.getEntityName());
				Type aHibType = cmd.getPropertyType(requiredField);
				
				if (positionInteger.intValue() == -1){
					QbeJsTreeNodeId nodeId = new QbeJsTreeNodeId(className, completeRequiredFieldName);
					if ( isSubqueryModeActive() ) {
						nodeId.setClassPrefix(subQueryPrefix);
					}
					getActiveDataMartWizard().addSelectField(nodeId.getClassName(), nodeId.getClassAlias(), nodeId.getFieldAlias(), completeRequiredFieldName, className + "." + completeRequiredFieldName,aHibType.getName(), null, null);
				}
			
			}
		
			getActiveDataMartWizard().getSelectClause().addCalculatedField(calculatedField);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		updateLastUpdateTimeStamp();
		setMainDataMartWizard( getMainDataMartWizard() );
	}
		
	public static CalculatedField getCalculatedField(String cFieldId, File formulaFile) throws Exception{
		File f = null;
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		try{
			if (!formulaFile.exists()){
				return null;
			}
			saxReader = new SAXReader();
			formulaFileDocument = saxReader.read(formulaFile);
			String xPath = "/FORMULAS/FORMULA[@id = '"+cFieldId+"']";
			
			Node cFieldNode =  formulaFileDocument.selectSingleNode(xPath);
			CalculatedField cfield = new CalculatedField();
			cfield.setId(cFieldNode.valueOf("@id"));
			cfield.setEntityName(cFieldNode.valueOf("@onEntity"));
			cfield.setFldLabel(cFieldNode.valueOf("@resultingFieldName"));
			cfield.setScript(cFieldNode.valueOf("@script"));
			cfield.setMappings(cFieldNode.valueOf("@mappings"));
			cfield.setInExport(cFieldNode.valueOf("@inExport"));
			return cfield;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			formulaFile = null;
			saxReader = null;
			formulaFileDocument = null;
		}
	}
	
}
