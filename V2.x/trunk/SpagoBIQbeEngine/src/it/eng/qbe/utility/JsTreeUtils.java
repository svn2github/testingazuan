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
package it.eng.qbe.utility;

import it.eng.qbe.bo.DatamartLabels;
import it.eng.qbe.bo.DatamartProperties;
import it.eng.qbe.conf.QbeConf;
import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;

import java.util.Locale;
import java.util.Properties;

/**
 * @author Andrea Gioia
 *
 */
public class JsTreeUtils {
	public static String getLabelForClass(DataMartModel dmModel,  String className){
		
		Locale locale = QbeEngineConf.getInstance().getLocale();		
		DatamartLabels prop = dmModel.getLabels(locale);
		
		String res =(String)prop.getLabel("class." + className);
		if ((res != null) && (res.trim().length() > 0)) {
			return res;
		} else {
			return className;
		}
	}
	
	public static String getLabelForForeignKey(DataMartModel dmModel,  String classForeignKeyID){
		Locale locale = QbeEngineConf.getInstance().getLocale();	
		DatamartLabels prop = dmModel.getLabels(locale);
		
		String res =(String)prop.getLabel("relation." + classForeignKeyID);
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return null;
	}
	
	/**
	 * Get the label for given fieldName
	 * @param requestContainer: Spago Request Container
	 * @param dmModel: The datamart Model
	 * @param completeFieldName: The field Name
	 * @return the label associated with the field name
	 */
	public static String getLabelForField(DataMartModel dmModel,  String completeFieldName){
		Locale locale = QbeEngineConf.getInstance().getLocale();	
		DatamartLabels prop = dmModel.getLabels(locale);
		
		String res =(String)prop.getLabel("field." + completeFieldName); 
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return completeFieldName;
	}
	
	/**
	 * @param requestContainer
	 * @param dmModel
	 * @param wizObj
	 * @param completeFieldName
	 * @return
	 */
	public static String getLabelForQueryField(RequestContainer requestContainer, 
			DataMartModel dmModel, ISingleDataMartWizardObject wizObj,  String completeFieldName){
		Locale locale = QbeEngineConf.getInstance().getLocale();	
		DatamartLabels prop = dmModel.getLabels(locale);
		
		
		String prefix = "";
		String postFix = "";
		String fieldNameWithoutOperators = completeFieldName.trim();
		
		if (fieldNameWithoutOperators.startsWith("distinct")) {
			prefix += "distinct ";
		}
		
		if (fieldNameWithoutOperators.startsWith("all")) {
			prefix += "all ";
		} 
		
		int indexOpenPar = completeFieldName.indexOf("(");
		int indexClosePar = completeFieldName.indexOf(")");
		
		if ((indexOpenPar >= 0) && (indexClosePar >= 0)){
			prefix += completeFieldName.substring(0, indexOpenPar+1);
			fieldNameWithoutOperators = completeFieldName.substring(indexOpenPar+1, indexClosePar).trim();
			postFix += completeFieldName.substring(indexClosePar);
			Logger.debug(Utils.class,"Prefix " + prefix);
			Logger.debug(Utils.class,"PostFix " + postFix);
			Logger.debug(Utils.class,"Field without oper " + fieldNameWithoutOperators);
			
		}
		
		
		int dotIndexOf = fieldNameWithoutOperators.indexOf(".");
		
		String classAlias = fieldNameWithoutOperators.substring(0, dotIndexOf);
		
		String className = classAlias;
		EntityClass ec = null;
		for (int i=0; i < wizObj.getEntityClasses().size(); i++){
			ec = (EntityClass)wizObj.getEntityClasses().get(i);
			if (classAlias.equalsIgnoreCase(ec.getClassAlias())){
				className = ec.getClassName();
				break;
			}
		}
		
		String classLabel = getLabelForClass(dmModel, className);
		
		String realFieldName = fieldNameWithoutOperators.substring(dotIndexOf + 1);
		String res =(String)prop.getLabel("field." + realFieldName); 
		if ((res != null) && (res.trim().length() > 0))
			return prefix + classLabel+ "." + res + postFix;
		else
			return prefix + classLabel+ "." + fieldNameWithoutOperators + postFix;
	}
}
