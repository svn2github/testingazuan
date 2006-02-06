
package it.eng.qbe.javascript;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.IQbeUrlGenerator;
import it.eng.qbe.utility.Logger;
import it.eng.qbe.utility.PortletQbeUrlGenerator;
import it.eng.qbe.utility.RelationField;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;

/**
 * @author Andrea Zoppello
 * 
 * This is an utility class to generate the Javascript code in JSP
 * to handle the tree for field Selection, field Condition, field that are right value of a join
 * 
 */
public class GenerateJavaScriptMenu {

	
	private DataMartModel dataMartModel = null;
	
	private HttpServletRequest httpRequest = null; 
	private IQbeUrlGenerator qbeUrlGenerator = null;
	
	
	/**
	 * @param dataMartModel : The DatamartModel object reperesenting the datamart we're working on
	 * @param httpRequest : the httpRequest Object
	 */
	public GenerateJavaScriptMenu(DataMartModel dataMartModel, HttpServletRequest httpRequest){
		this.dataMartModel = dataMartModel;
		this.httpRequest = httpRequest;
		
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");   
		
		if (qbeMode.equalsIgnoreCase("WEB")){
			qbeUrlGenerator = new it.eng.qbe.utility.WebQbeUrlGenerator();
		} else if  (qbeMode.equalsIgnoreCase("PORTLET")){
			qbeUrlGenerator = new PortletQbeUrlGenerator();
		}
		
	}
	
	/**
	 * @deprecated 
	 * @return the String with the Javascript code for Class Selection Tree
	 */
	public final String writeTreeForClassSelection(){
		StringBuffer aStringBuffer = new StringBuffer();
		
		String nomeAlberoJavaScript = "classTree";
		aStringBuffer.append("<script type=\"text/javascript\">");
		aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
		
		int nodeCounter = 0;
		nodeCounter = writeWholeHibernateModelTree(aStringBuffer, nomeAlberoJavaScript, "Select a Class", -1, nodeCounter, false, new SelectClassForWizardURLGenerator(qbeUrlGenerator, httpRequest));
		
		
		aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
		aStringBuffer.append("</script>");
		return aStringBuffer.toString();

	}
	
	
	/**
	 * @deprecated
	 * @return the String with the Javascript code that constitute the application menu ( Only in Web version )
	 */
	public final String writeApplicationMenuTree(){
			StringBuffer aStringBuffer = new StringBuffer();

			
			String nomeAlberoJavaScript = "repTree";
			aStringBuffer.append("<script type=\"text/javascript\">");
			aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
			aStringBuffer.append(
					nomeAlberoJavaScript +
					".add(0,-1,'Menu','','','Menu'," +
					" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
					",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
					");");
			
			
			int nodeCounter = 0;
			//nodeCounter = writeWholeHibernateModelTree(aStringBuffer, nomeAlberoJavaScript, "Exploring Repository", 0, nodeCounter, true, new ExploreClassContentURLGenerator());
			
			nodeCounter = writeWizardSubTree(aStringBuffer, nomeAlberoJavaScript, "Available Wizards", 0 , nodeCounter);
			aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
			aStringBuffer.append("</script>");
			return aStringBuffer.toString();
	
	}
	
	/**
	 * @return the String with the Javascript code that constitute the tree for Field Selection
	 */
	public final String selTree(){
		StringBuffer aStringBuffer = new StringBuffer();

		
		String nomeAlberoJavaScript = "selTree";
		aStringBuffer.append("<script type=\"text/javascript\">");
		aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
		aStringBuffer.append(nomeAlberoJavaScript +
						".add(0,-1,'"+dataMartModel.getName()+"','','','"+dataMartModel.getName()+"'," +
						" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
						",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
						");");
		
		
		int nodeCounter = 0;
		nodeCounter = writeSelectionTree(aStringBuffer, nomeAlberoJavaScript, "Exploring Repository", 0, nodeCounter, true);
		
		
		aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
		aStringBuffer.append("</script>");
		return aStringBuffer.toString();
	}
	
	/**
	 * @return the String with the Javascript code that constitute the tree for Field Condition
	 */
	public final String condTree(ISingleDataMartWizardObject dataMartWizardObject, String selectionTree){
		
		StringBuffer aStringBuffer = new StringBuffer();
		
		String nomeAlberoJavaScript = "condTree";
		aStringBuffer.append("<script type=\"text/javascript\">");
		aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
		aStringBuffer.append(nomeAlberoJavaScript +
				".add(0,-1," +
				"'"+dataMartModel.getName()+"'," +
				"''," +
				"''," +
				"'"+dataMartModel.getName()+"'," +
				"'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				");");
		
		
		int nodeCounter = 0;
		nodeCounter = writeConditionTree(dataMartWizardObject, selectionTree, aStringBuffer, nomeAlberoJavaScript, "Exploring Repository", 0, nodeCounter, true);
		
		
		aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
		aStringBuffer.append("</script>");
		return aStringBuffer.toString();
	}
	
	
	/**
	 * @return the String with the Javascript code that constitute the tree for Field that are right value of a join condition
	 */
	public final String condTreeSelectJoin(ISingleDataMartWizardObject dataMartWizardObject, String selectionTree){
		
		//
		StringBuffer aStringBuffer = new StringBuffer();

		
		String nomeAlberoJavaScript = "condTreeSelectJoin";
		aStringBuffer.append("<script type=\"text/javascript\">");
		aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
		aStringBuffer.append(nomeAlberoJavaScript +
				".add(0,-1," +
				"'"+dataMartModel.getName()+"'," +
				"''," +
				"''," +
				"'"+dataMartModel.getName()+"'," +
				"'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				");");
		
		
		int nodeCounter = 0;
		nodeCounter = writeConditionTreeSelectJoin(dataMartWizardObject, selectionTree, aStringBuffer, nomeAlberoJavaScript, "Exploring Repository", 0, nodeCounter, true);
		
		
		aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
		aStringBuffer.append("</script>");
		return aStringBuffer.toString();
	}
	
	
	/**
	 * Thi is the start funztion for generating the tree for Field that are right value of a join condition
	 * @param dataMartWizardObject The object representing the datamart
	 * @param aStringBuffer The StringBuffer were javascript code will be append
	 * @param nomeAlberoJavaScript the identifier of the tree in javascript
	 * @param labelSottoAlberoFunzioni a Label
	 * @param rootNode the index of the root note
	 * @param nodeCounter the counter of total node
	 * @param generateFieldsNode if true the fiells of Entitities of the datamart will be generated
	 * @return the node counter the counter of the node
	 */
	public final int writeConditionTreeSelectJoin(ISingleDataMartWizardObject dataMartWizardObject, String selectionTree, StringBuffer aStringBuffer, String nomeAlberoJavaScript, String labelSottoAlberoFunzioni, int rootNode, int nodeCounter, boolean generateFieldsNode){
		
		if ("LIGHT".equalsIgnoreCase(selectionTree)) {
		
		ApplicationContainer application = ApplicationContainer.getInstance();
		SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
		
		List list  = dataMartWizardObject.getEntityClasses();
		
		
		EntityClass ec = null;
		
		String classCompleteName = "";
	
		
		for (Iterator it = list.iterator(); it.hasNext(); ){
			ec  = (EntityClass)it.next();
			
			classCompleteName = (String)ec.getClassName();
			
			nodeCounter = writeTreeReachableFromClass(aStringBuffer, nomeAlberoJavaScript, classCompleteName, rootNode, nodeCounter, null, new SelectFieldForJoinUrlGenerator(classCompleteName, qbeUrlGenerator, httpRequest));
		}		 
		
		return nodeCounter;
		
		} else {
			
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
			
			Map aMap = sf.getAllClassMetadata();
		
			Object o = null;
		
			String classCompleteName = "";
	
		
			for (Iterator it = aMap.keySet().iterator(); it.hasNext(); ){
				o  = it.next();
			
				classCompleteName = (String)o;
			
				nodeCounter = writeTreeReachableFromClass(aStringBuffer,nomeAlberoJavaScript, classCompleteName, rootNode, nodeCounter, null, new SelectFieldForJoinUrlGenerator(classCompleteName, qbeUrlGenerator, httpRequest));
			}		 
		
			return nodeCounter;
			
		}
	}
	
	
	/**
	 * Thi is the start funztion for generating the tree for Field Condition Selection
	 * @param dataMartWizardObject The object representing the datamart
	 * @param aStringBuffer The StringBuffer were javascript code will be append
	 * @param nomeAlberoJavaScript the identifier of the tree in javascript
	 * @param labelSottoAlberoFunzioni a Label
	 * @param rootNode the index of the root note
	 * @param nodeCounter the counter of total node
	 * @param generateFieldsNode if true the fiells of Entitities of the datamart will be generated
	 * @return the node counter the counter of the node
	 */
	public final int writeConditionTree(ISingleDataMartWizardObject dataMartWizardObject, String selectionTree, StringBuffer aStringBuffer, String nomeAlberoJavaScript, String labelSottoAlberoFunzioni, int rootNode, int nodeCounter, boolean generateFieldsNode){
		
		if ((selectionTree == null )||("LIGHT".equalsIgnoreCase(selectionTree))) {
			
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
		
			List list  = dataMartWizardObject.getEntityClasses();
		
			EntityClass ec = null;
		
			String classCompleteName = "";
	
		
			for (Iterator it = list.iterator(); it.hasNext(); ){
				ec  = (EntityClass)it.next();
			
				classCompleteName = (String)ec.getClassName();
			
				nodeCounter = writeTreeReachableFromClass(aStringBuffer, nomeAlberoJavaScript, classCompleteName, rootNode, nodeCounter, null, new SelectFieldForConditionURLGenerator(classCompleteName, qbeUrlGenerator, httpRequest));
			}		 
		
			return nodeCounter;
	
		} else {
			
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
			
			Map aMap = sf.getAllClassMetadata();
		
			Object o = null;
		
			String classCompleteName = "";
	
		
			for (Iterator it = aMap.keySet().iterator(); it.hasNext(); ){
				o  = it.next();
			
				classCompleteName = (String)o;
			
				nodeCounter = writeTreeReachableFromClass(aStringBuffer,nomeAlberoJavaScript, classCompleteName, rootNode, nodeCounter, null, new SelectFieldForConditionURLGenerator(classCompleteName, qbeUrlGenerator, httpRequest));
			}		 
		
			return nodeCounter;
			
		}
	}
	
	/**
	 * This is the start funztion for generating the tree for Field Selection
	 * @param aStringBuffer The StringBuffer were javascript code will be append
	 * @param nomeAlberoJavaScript the identifier of the tree in javascript
	 * @param labelSottoAlberoFunzioni a Label
	 * @param rootNode the index of the root note
	 * @param nodeCounter the counter of total node
	 * @param generateFieldsNode if true the fiells of Entitities of the datamart will be generated
	 * @return the node counter the counter of the node
	 */
	public final int writeSelectionTree(StringBuffer aStringBuffer, String nomeAlberoJavaScript, String labelSottoAlberoFunzioni, int rootNode, int nodeCounter, boolean generateFieldsNode){
		
		ApplicationContainer application = ApplicationContainer.getInstance();
		SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
		Map aMap = sf.getAllClassMetadata();
		
		
		Object o = null;
		
		String classCompleteName = "";
	
		
		for (Iterator it = aMap.keySet().iterator(); it.hasNext(); ){
			o  = it.next();
			
			//Logger.debug(GenerateJavaScriptMenu.class,o.getClass().getName());
			classCompleteName = (String)o;
			
			nodeCounter = writeTreeReachableFromClass(aStringBuffer,nomeAlberoJavaScript, classCompleteName, rootNode, nodeCounter, null, new SelectFieldForSelectionURLGenerator(classCompleteName, qbeUrlGenerator, httpRequest));
		}		 
		
		return nodeCounter;
	}
	
	
	
	
	/**
	 * @param className
	 * @param fieldURLGenerator
	 * @return
	 */
	public final String writeNavigationTreeFromClass(String className, IURLGenerator fieldURLGenerator){
		return writeNavigationTreeFromClass(className, null, fieldURLGenerator);
	}
	
	
	/**
	 * @param className
	 * @param classAlias
	 * @param fieldURLGenerator
	 * @return
	 */
	public final String writeNavigationTreeFromClass( String className, String classAlias, IURLGenerator fieldURLGenerator){
		StringBuffer aStringBuffer = new StringBuffer();

		String nomeAlberoJavaScript = "selectableFieldTree";
		aStringBuffer.append("<script type=\"text/javascript\">");
		aStringBuffer.append(nomeAlberoJavaScript +"= new dTree('"+nomeAlberoJavaScript+"');");
		aStringBuffer.append(nomeAlberoJavaScript +
				".add(0,-1,'Select Fields','','','Select Fields'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/base.gif")+"'" +
				");");	
		
		int nodeCounter = 0;	
		nodeCounter = writeTreeReachableFromClass(aStringBuffer, nomeAlberoJavaScript, className, 0, nodeCounter, classAlias, fieldURLGenerator);
		
		
		aStringBuffer.append("document.write("+nomeAlberoJavaScript+");");
		aStringBuffer.append("</script>");
		return aStringBuffer.toString();

	}
	
	
	/**
	 * Recursive Function To write the tree of model navigable  starting by className
	 * @param aStringBuffer
	 * @param nomeAlberoJavaScript
	 * @param className
	 * @param rootNode
	 * @param nodeCounter
	 * @param prefix
	 * @param fieldUrlGenerator
	 * @return
	 */
	public final int writeTreeReachableFromClass(StringBuffer aStringBuffer, String nomeAlberoJavaScript, String className, int rootNode, int nodeCounter, String prefix, IURLGenerator fieldUrlGenerator){
		
		
		nodeCounter++;
		
		
		String classLabel = Utils.getLabelForClass(Utils.getRequestContainer(httpRequest), dataMartModel, className);
		
		/*
		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+rootNode+",'"+className+"','','','"+className+"'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
				");");
		*/
		
		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+rootNode+",'"+classLabel+"','','','"+classLabel+"'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
				");");
		
		int idxClassNode = nodeCounter;
		// Genero i field
		try{
			ApplicationContainer application = ApplicationContainer.getInstance();
			SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
			
			ClassMetadata aClassMetadata = sf.getClassMetadata(className);
			String[] metaPropertyNames = aClassMetadata.getPropertyNames();
			
			List associatedClassesArrayList = new ArrayList();
			
			Type aHibType = null;
			String urlRef = "";    	

			
			Type aType = aClassMetadata.getIdentifierType();
			
			String completeFieldName = "";
			String fieldAction = "";
			String[] keyProperties = null;
			String[] keyPropertiesType = null;
			if (aType.isComponentType()){
					
					
					String idPropertyName = aClassMetadata.getIdentifierPropertyName();
					String[] tmpKeyProperties = ((ComponentType)aType).getPropertyNames();
					
					Type[] subtypes = ((ComponentType)aType).getSubtypes();
					keyProperties = new String[tmpKeyProperties.length];
					keyPropertiesType = new String[tmpKeyProperties.length];
					for (int j=0; j < tmpKeyProperties.length; j++){
						keyProperties[j] = idPropertyName + "." + tmpKeyProperties[j];
						keyPropertiesType[j] = subtypes[j].getClass().getName();
						//Logger.debug(GenerateJavaScriptMenu.class," Type ["+ keyPropertiesType[j]+ "]");
					}
			}else{
					keyProperties = new String[1];
					keyProperties[0] = aClassMetadata.getIdentifierPropertyName();
					keyPropertiesType = new String[1];
					keyPropertiesType[0] = aType.getClass().getName();
			}
			    	
			for (int j = 0; j < keyProperties.length; j++) {

//				Logger.debug(GenerateJavaScriptMenu.class,"keyProperties[" + j + "] "
//						+ keyProperties[j]);
				nodeCounter++;
				completeFieldName = keyProperties[j];

				
				if (prefix != null) {
					completeFieldName = prefix + "." + keyProperties[j];
				}
				//
				String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName);
				fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel,  keyPropertiesType[j]);
				
				
				
				aStringBuffer.append(nomeAlberoJavaScript + ".add("
						+ nodeCounter + "," + idxClassNode + ",'"
						+ Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName) + "','"
						+ fieldAction + "','"
						+ Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, completeFieldName)
						+ "','_self'," +
						" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif")+"'" +
						",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif")+"'" +
						");");
			}
			
				
			
			for(int i=0; i < metaPropertyNames.length; i++){
			 	aHibType = (Type)aClassMetadata.getPropertyType(metaPropertyNames[i]);
			 	
			 	if (aHibType instanceof ManyToOneType){
			 		completeFieldName = metaPropertyNames[i];
			 		
			 		if (prefix != null){
			 			 completeFieldName = prefix +"." + metaPropertyNames[i];
			 		}
			 		RelationField aRelationField = new RelationField( completeFieldName, ((ManyToOneType)aHibType).getAssociatedEntityName()); 
			 		
			 		associatedClassesArrayList.add(aRelationField);																   
			 	}else{
			 		//Logger.debug(GenerateJavaScriptMenu.class," HibType Class" + aHibType.getClass());
			 		nodeCounter++;
			 		completeFieldName = metaPropertyNames[i];
			 		
			 		
			 		if (prefix != null){
			 			 completeFieldName = prefix +"." + metaPropertyNames[i];
			 		}
			 		
			 		String fldLabel = Utils.getLabelForField(Utils.getRequestContainer(httpRequest),dataMartModel, metaPropertyNames[i]);
			 		fieldAction = fieldUrlGenerator.generateURL(completeFieldName, fldLabel, aHibType.getClass().getName());
			 		
			 		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+idxClassNode+",'"
			 				+fldLabel+"','"+fieldAction+"','"
			 				+fldLabel+"','_self'," +
			 				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif")+"'" +
			 				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif")+"'" +
			 				");");
			 	}
			}
			
			Iterator associatedClassIterator = associatedClassesArrayList.iterator();
			while (associatedClassIterator.hasNext()){
				RelationField aRelationField = (RelationField)associatedClassIterator.next();
				nodeCounter = writeTreeReachableFromClass(aStringBuffer, nomeAlberoJavaScript, aRelationField.getClassName(), idxClassNode, nodeCounter, aRelationField.getFieldName(), fieldUrlGenerator);
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return nodeCounter;
	}
	
	/**
	 * Append to aStringBuffer the javascript code for submenu related to wizards section
	 * @param aStringBuffer
	 * @param nomeAlberoJavaScript
	 * @param labelSottoAlberoWizard
	 * @param rootNode
	 * @param nodeCounter
	 * @return
	 */
	public int writeWizardSubTree(StringBuffer aStringBuffer, String nomeAlberoJavaScript, String labelSottoAlberoWizard, int rootNode, int nodeCounter){
	
		String singleDataMartQueryAction ="../servlet/AdapterHTTP?ACTION_NAME=INIT_WIZARD_ACTION&PUBLISHER_NAME=SELECT_FIELDS_FOR_SELECTION_PUBLISHER";
		nodeCounter++; 
		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+rootNode+",'"+labelSottoAlberoWizard+"','','','"+labelSottoAlberoWizard+"'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Project.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Project.gif")+"'" +
				");");
		int idWizardRootNode = nodeCounter;
		nodeCounter++;
		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+idWizardRootNode+",'DataMart Query Wizard','"+singleDataMartQueryAction+"','DataMart Query Wizard','_self'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Wizard.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Wizard.gif")+"'" +
				",false);");
		return nodeCounter;
	
	}
	
	/**
	 * Append to aStringBuffer the javascript code for the relative to the exploration of hibernate repository
	 * @param aStringBuffer
	 * @param nomeAlberoJavaScript
	 * @param labelSottoAlberoFunzioni
	 * @param rootNode
	 * @param nodeCounter
	 * @param generateFieldsNode
	 * @param aURLGenerator
	 * @return
	 */
	public final int writeWholeHibernateModelTree(StringBuffer aStringBuffer, String nomeAlberoJavaScript, String labelSottoAlberoFunzioni, int rootNode, int nodeCounter, boolean generateFieldsNode, IURLGenerator aURLGenerator){
	
		nodeCounter++;
		aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+rootNode+",'"+labelSottoAlberoFunzioni+"','','','"+labelSottoAlberoFunzioni+"'," +
				" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Project.gif")+"'" +
				",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Project.gif")+"'" +
				");");
		try{
		
		ApplicationContainer application = ApplicationContainer.getInstance();
		SessionFactory sf = Utils.getSessionFactory(dataMartModel,application);
		Map aMap = sf.getAllClassMetadata();
		String key = ""; 
		int idNodeRepositoryExplorer = nodeCounter;
		
		Object o = null;
		String listAction= null;
		String packageName = "";
		String classCompleteName = "";
		int parentId = 1;
		
		for (Iterator it = aMap.keySet().iterator(); it.hasNext(); ){
			o  = it.next();
			
			//Logger.debug(GenerateJavaScriptMenu.class,o.getClass().getName());
			classCompleteName = (String)o;
			//packageName = keyClass.getPackage().getName();
			key = classCompleteName.substring(classCompleteName.lastIndexOf('.')+1);
			listAction=aURLGenerator.generateURL(classCompleteName);
			ClassMetadata meta = sf.getClassMetadata(classCompleteName);
		
			String[] metaPropertyNames = meta.getPropertyNames();
			
			nodeCounter++;
			aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+idNodeRepositoryExplorer+",'"+key+"','"+listAction+"','"+key+"','_self'," +
					" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
					",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Class.gif")+"'" +
					",false);");
					
			if (generateFieldsNode){
				// Genero la chiave
				parentId = nodeCounter;
				nodeCounter++;
				aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+parentId+",'"+meta.getIdentifierPropertyName()+"','','','"+meta.getIdentifierPropertyName()+"'," +
						" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif")+"'" +
						",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/key.gif")+"'" +
						");");
			
				for(int j=0; j < metaPropertyNames.length; j++){
					nodeCounter++;
					aStringBuffer.append(nomeAlberoJavaScript+".add("+nodeCounter+","+parentId+",'"+metaPropertyNames[j]+"','','','"+metaPropertyNames[j]+"'," +
							" '"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif")+"'" +
							",'"+qbeUrlGenerator.conformStaticResourceLink(httpRequest,"../img/Method.gif")+"'" +
							");");
				}
			}
		}		 
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return nodeCounter;
	}

	/**
	 * @return the dataMartModel
	 */
	public DataMartModel getDataMartModel() {
		return dataMartModel;
	}

	/**
	 * @param dataMartModel
	 */
	public void setDataMartModel(DataMartModel dataMartModel) {
		this.dataMartModel = dataMartModel;
	}
}
