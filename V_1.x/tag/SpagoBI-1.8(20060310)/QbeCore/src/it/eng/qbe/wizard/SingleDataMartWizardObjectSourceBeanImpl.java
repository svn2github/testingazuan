/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import it.eng.qbe.utility.Logger;
import it.eng.spago.base.SourceBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SingleDataMartWizardObjectSourceBeanImpl implements ISingleDataMartWizardObject {

	

	private ISelectClause selectClause = null;
	private IWhereClause  whereClause = null;
	private IOrderByClause orderByClause = null;
	private IGroupByClause groupByClause = null;
	
	private String finalQuery = null;
	private List entityClasses = null;
	
	private String expertQueryDisplayed = null;
	private String expertQuerySaved = null;
	
	private String owner = null;
	private boolean visibility;
	private boolean distinct;
	
	private boolean useExpertedVersion = false;
	
	private String queryId = null;
	private String description = null;
	
	public SingleDataMartWizardObjectSourceBeanImpl() {
		super();
		this.entityClasses = new ArrayList();
		//this.setQueryId("query_"+ System.currentTimeMillis());
		
	}
	
	
	
	
	public ISelectClause getSelectClause() {
		return this.selectClause;
	}
	
	public IWhereClause getWhereClause() {
		return this.whereClause;
	}
	
	public IOrderByClause getOrderByClause() {
		return this.orderByClause;
		
		
	}
	
	public IGroupByClause getGroupByClause() {
		
		return this.groupByClause;
		
	}
	
	public void setOrderByClause(IOrderByClause orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	public void setGroupByClause(IGroupByClause groupByClause) {
		this.groupByClause = groupByClause;
		
		
	}

	
	
	public void setWhereClause(IWhereClause aWhereClause) {
		this.whereClause = aWhereClause;
		
	}

	
	public void setSelectClause(ISelectClause aSelectClause) {
			this.selectClause = aSelectClause;
			
		}
	
	public void delSelectClause() {
		this.selectClause = null;
	}
	
	public void delWhereClause() {
		this.whereClause = null;
	}
	
	public void delOrderByClause() {
		this.orderByClause = null;
	}
	
	public void delGroupByClause() {
		this.groupByClause = null;
	}
	
	public String getFinalQuery() {
		return this.finalQuery;
	}
	
	public void setFinalQuery(String query) {
		this.finalQuery = query;
		
	}
	public void addEntityClass(EntityClass ec) {
		this.entityClasses.add(ec);
	}
	public List getEntityClasses() {
		return this.entityClasses;
	}
	
	public boolean containEntityClass(EntityClass parameEc) {
		
		EntityClass ec = null;
		SourceBean sb = null;
		
		for (Iterator it = this.entityClasses.iterator(); it.hasNext();){
			
			ec = (EntityClass)it.next();
			if (ec.getClassName().equalsIgnoreCase(parameEc.getClassName())
			   && ec.getClassAlias().equalsIgnoreCase(ec.getClassAlias())){
				return true;
			}
		}
		return false;
							
	}
	
	

	public void purgeNotReferredEntityClasses() {
		this.entityClasses.clear();
		
		ISelectClause selectClause = getSelectClause();
				
		EntityClass ec = null;
		ISelectField selField = null;
		if (selectClause != null){
			for (int i=0; i < selectClause.getSelectFields().size(); i++){
				selField = (ISelectField)selectClause.getSelectFields().get(i);
				ec = selField.getFieldEntityClass();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
			}
		}
		
		IWhereClause whereClause = getWhereClause();
		
		ec = null;
		
		IWhereField whereField = null;
		
		if (whereClause != null){
			for (int i=0; i < whereClause.getWhereFields().size(); i++){
				whereField = (IWhereField)whereClause.getWhereFields().get(i);
				
				ec = whereField.getFieldEntityClassForLeftCondition();
				if (!this.containEntityClass(ec)){
					this.addEntityClass(ec);
				}
				ec = whereField.getFieldEntityClassForRightCondition();
				
				if ((ec != null)&&(!this.containEntityClass(ec))){
					this.addEntityClass(ec);
				}
				
			}
		}		
		
	}
	

	public String getQueryId() {
		return queryId;
	}




	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	

	public void composeQuery(){
		this.finalQuery = null;
		StringBuffer finalQuery = new StringBuffer();
		ISelectClause aSelectClause = this.getSelectClause();
		boolean afterFirst = false;
		
		if (aSelectClause != null){
			
			finalQuery.append("select ");
			java.util.List l= aSelectClause.getSelectFields();
		 	Iterator it = l.iterator();
		 
		 	if (this.getDistinct()){
		 		finalQuery.append("distinct ");
		 	}
		 	
		 	ISelectField aSelectField = null;
		 	while (it.hasNext()){
		 		aSelectField =(ISelectField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(aSelectField.getFieldName());
		 		/*
		 		if (aSelectField.getFieldAlias() != null)
		 			finalQuery.append(" as \""+aSelectField.getFieldAlias()+ "\" ");
		 		*/
		 		afterFirst = true;
		 	}
			
		} //else{
			// return;
		//}
		
		List entityClasses = this.getEntityClasses();
		
		
		afterFirst = false;
		EntityClass ec = null;
		if (entityClasses != null){
			finalQuery.append(" from ");
			
			for (Iterator it = entityClasses.iterator(); it.hasNext();){
				ec =(EntityClass)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(ec.getClassName() + " as " + ec.getClassAlias() + " ");
		 		afterFirst = true;
			}
		}
		 
	
		IWhereClause aWhereClause = this.getWhereClause();  
		afterFirst = false;
		if (aWhereClause != null){
		 	finalQuery.append("where\n");
		 	java.util.List l= aWhereClause.getWhereFields();
		 	Iterator it = l.iterator();
		 	
		 	
		 	IWhereField aWhereField = null;
		 	String newFieldValue = null;
		 	String fieldName = null;
		 	while (it.hasNext()){
		 		aWhereField =(IWhereField)it.next();
		 		fieldName = aWhereField.getFieldName();
		 		
		 		finalQuery.append(fieldName);  
		 		finalQuery.append(" ");
		 		
		 		if (aWhereField.getFieldOperator().equalsIgnoreCase("start with")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = aWhereField.getFieldValue()+"%";
		 			aWhereField.setFieldValue(newFieldValue);
		 		}else if (aWhereField.getFieldOperator().equalsIgnoreCase("end with")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = "%"+ aWhereField.getFieldValue();
		 			aWhereField.setFieldValue(newFieldValue);
		 		}else if (aWhereField.getFieldOperator().equalsIgnoreCase("contains")){
		 			aWhereField.setFieldOperator("like");
		 			newFieldValue = "";
		 			newFieldValue = "%"+ aWhereField.getFieldValue()+"%";
		 			aWhereField.setFieldValue(newFieldValue);
		 		}
		 		finalQuery.append(aWhereField.getFieldOperator());
		 		finalQuery.append(" ");
		 		String fValue = aWhereField.getFieldValue();
		 		if ((aWhereField.getFieldEntityClassForRightCondition() == null)&&(aWhereField.getHibernateType().endsWith("StringType")))
		 			finalQuery.append("'"+ fValue + "'");
		 		else
		 			finalQuery.append( fValue );
		 		
		 		if (it.hasNext())
		 			finalQuery.append(" "+aWhereField.getNextBooleanOperator()+" ");
		 		afterFirst = true;
		 	}
		 }
		
		IGroupByClause aGroupByClause = this.getGroupByClause();  
		afterFirst = false;
		
		if (aGroupByClause != null){
			finalQuery.append(" group by ");
			java.util.List l= aGroupByClause.getGroupByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	IOrderGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IOrderGroupByField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(aOrderGroupByField.getFieldName() + " ");
		 		afterFirst = true;
		 	}
		}
		
		IOrderByClause aOrderByClause= this.getOrderByClause();  
		afterFirst = false;
		
		if (aOrderByClause != null){
			finalQuery.append(" order by ");
			java.util.List l= aOrderByClause.getOrderByFields();
		 	Iterator it = l.iterator();
		 
		 	
		 	IOrderGroupByField aOrderGroupByField = null;
		 	while (it.hasNext()){
		 		aOrderGroupByField =(IOrderGroupByField)it.next();
		 		if (afterFirst)
		 			finalQuery.append(", ");
		 		finalQuery.append(aOrderGroupByField.getFieldName());
		 		afterFirst = true;
		 	}
		}
		
		this.finalQuery = (finalQuery != null ? finalQuery.toString() : null);
		
		this.finalQuery = this.finalQuery.trim();
	}//public void composeQuery(){




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public void setEntityClasses(List entityClasses) {
		this.entityClasses = entityClasses;
	}
	
	

	public boolean isUseExpertedVersion() {
		return useExpertedVersion;
	}

	public void setUseExpertedVersion(boolean useExpertedVersion) {
		this.useExpertedVersion = useExpertedVersion;
	}




	public String getExpertQueryDisplayed() {
		return expertQueryDisplayed;
	}




	public void setExpertQueryDisplayed(String expertQueryDisplayed) {
		this.expertQueryDisplayed = expertQueryDisplayed;
	}




	public boolean getVisibility() {
		return this.visibility;
	}




	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}




	public String getOwner() {
		return this.owner;
	}




	public void setOwner(String owner) {
		this.owner = owner;
	}




	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	public boolean getDistinct() {
		return distinct;
	}




	public String getExpertQuerySaved() {
		return expertQuerySaved;
	}




	public void setExpertQuerySaved(String expertQuerySaved) {
		this.expertQuerySaved = expertQuerySaved;
	}


	/**
	 * This method extracts the name of select fields (or the alias name if present), from the expert query 
	 * 
	 * @param expertSelectFieldsList
	 * 
	 *  @return the list of the name of select fields (or the alias name if present), from the expert query, 
	 *  		null if the query is null or doesn't contain select fields 
	 */
	public List extractExpertSelectFieldsList() {
		
	String expertQueryTemp = this.getExpertQueryDisplayed();
	String seekForSelect = null;
		
		//non necessario: darebbe errrore nell'esecuzione e quindi è già gestito prima
		if (expertQueryTemp == null) return null;

		seekForSelect = expertQueryTemp.toLowerCase();
		expertQueryTemp = expertQueryTemp.trim();
		
		int checkSelect = seekForSelect.indexOf("select ");
		if (checkSelect==-1) {
			Logger.debug(SingleDataMartWizardObjectSourceBeanImpl.class,"Select fields not found");
			return null;
		}
		
						
		expertQueryTemp = expertQueryTemp.substring(7,expertQueryTemp.indexOf(" from "));
		
		boolean nextSelect = true;
		int commaIndex = -1;
		int asIndex = -1;
		int dotIndex = -1;
		String expertSelectField = null;
		List expertHeaders = new ArrayList(); 
		
		while (nextSelect) {
			commaIndex = expertQueryTemp.indexOf(",");
			if (commaIndex == -1) {
				expertSelectField = expertQueryTemp;
				nextSelect = false;
			} else {
				
				expertSelectField = expertQueryTemp.substring(0,commaIndex);
				expertQueryTemp = expertQueryTemp.substring(commaIndex+1);
			}

			asIndex = expertSelectField.indexOf(" as ");
			if (asIndex != -1){
				
				expertSelectField = expertSelectField.substring(asIndex+4);
				expertSelectField = expertSelectField.trim();
				expertHeaders.add(expertSelectField);
				continue;
				
			} else {
			
				dotIndex = expertSelectField.lastIndexOf(".");
				if (dotIndex != -1){
					
					expertSelectField = expertSelectField.substring(dotIndex+1);
					expertSelectField = expertSelectField.trim();
					expertHeaders.add(expertSelectField);
					continue;
										
				} else {										
					expertSelectField = expertSelectField.trim();
					expertHeaders.add(expertSelectField);
					continue;
				}
				
			}
				
		}
		
		return expertHeaders;
		
	}
	
	
}
