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

import it.eng.qbe.datasource.HibernateDataSource;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.WizardConstants;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.SessionFactory;

import sun.misc.BASE64Encoder;

public class Utils {

	/**
	 * This method is responsible to get Session Factories associated with datamart models, session factories
	 * are cached in Application Container
	 * @param dm: The datamart model
	 * @param application: Spago Application Container
	 * @return: the session Factory associated with a given datamart
	 */
	public static SessionFactory getSessionFactory(DataMartModel dm, ApplicationContainer application){
		Logger.debug(Utils.class, "getSessionFactory: start method getSessionFactory");
		if (application.getAttribute(dm.getPath()) != null){
			Logger.debug(Utils.class, "getSessionFactory: return session factory contained into application container");
			return (SessionFactory)application.getAttribute(dm.getPath());
		}else{
			Logger.debug(Utils.class, "getSessionFactory: session factory not contained into application container," +
					"it's necessary to create a new session factory");
			SessionFactory sf = dm.createSessionFactory();
			Logger.debug(Utils.class, "getSessionFactory: session factory created: " + sf);
			application.setAttribute(dm.getPath(), sf);
			Logger.debug(Utils.class, "getSessionFactory: session factory stored into application context: " + sf);
			return sf;
		}
		
	}
	
	public static IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	public static List getViewJarFiles(HibernateDataSource dataSource){
		try{
			IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
			return dataMartModelRetriever.getViewJarFiles(dataSource.getPath(), dataSource.getDialect());
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
	}
	
	/**
	 * This method is responsible to get Propertis  associated with datamart models, Properties objects
	 * are cached in Application Container
	 * @param dm: The datamart model
	 * @param application: Spago Application Container
	 * @return the Properties object associated with datamart
	 */
	public static Properties getLabelProperties(DataMartModel dm, ApplicationContainer application) {
		
		Properties props = new Properties();
		
		HibernateDataSource dataSource = dm.getDataSource();
		try{
			String propEntryInApplicationContext = dataSource.getPath()+"_labels";
			if (application.getAttribute(propEntryInApplicationContext) != null){
				props =  (Properties)application.getAttribute(propEntryInApplicationContext);
			}else{
				File dmJarFile = dataSource.getJarFile();
				JarFile jf = new JarFile(dmJarFile);
			
				props = LocaleUtils.getLabelProperties(jf);
				
				List views = getViewJarFiles(dataSource);
				Iterator it = views.iterator();
				while(it.hasNext()) {
					File viewJarFile = (File)it.next();
					jf = new JarFile(viewJarFile);
					Properties tmpProps = LocaleUtils.getLabelProperties(jf);
					props.putAll(tmpProps);
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			Logger.error(Utils.class, e);
		}
		return props;
	
	}
	
	/**
	 * This method is responsible to get Propertis  associated with datamart models for the given locale, Properties objects
	 * are cached in Application Container
	 * @param dm: The datamart model
	 * @param application: Spago Application Container
	 * @param loc: locale
	 * @return the Properties object associated with datamart
	 */ 
	public static Properties getLabelProperties(DataMartModel dm, ApplicationContainer application, Locale loc) {
		
		Properties props = new Properties();
		
		HibernateDataSource dataSource = dm.getDataSource();
		try{
			String propEntryInApplicationContext = dataSource.getPath()+"_labels_"+loc.getLanguage();
			if (application.getAttribute(propEntryInApplicationContext) != null){
				props =  (Properties)application.getAttribute(dataSource.getPath()+"_labels_"+loc.getLanguage());
			}else{
				File dmJarFile = dataSource.getJarFile();
				JarFile jf = new JarFile(dmJarFile);
			
				props = LocaleUtils.getLabelProperties(jf, loc);
				
				if (props.isEmpty()) {
					return getLabelProperties(dm, application);
				} else {
					List views = getViewJarFiles(dataSource);
					Iterator it = views.iterator();
					while(it.hasNext()) {
						File viewJarFile = (File)it.next();
						jf = new JarFile(viewJarFile);
						Properties tmpProps = LocaleUtils.getLabelProperties(jf, loc);
						if(tmpProps.isEmpty()) tmpProps = LocaleUtils.getLabelProperties(jf);
						props.putAll(tmpProps);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			Logger.error(Utils.class, e);
		}
		return props;
	
	}

	/**
	 * @param requestContainer
	 * @param dmModel
	 * @param className
	 * @return
	 */
	public static String getLabelForClass(RequestContainer requestContainer, DataMartModel dmModel,  String className){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		
		String res =(String)prop.get("class." + className);
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return className;
	}
	
	public static String getLabelForForeignKey(RequestContainer requestContainer, DataMartModel dmModel,  String classForeignKeyID){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		
		String res =(String)prop.get("relation." + classForeignKeyID);
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
	public static String getLabelForField(RequestContainer requestContainer, DataMartModel dmModel,  String completeFieldName){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		String res =(String)prop.get("field." + completeFieldName); 
		if ((res != null) && (res.trim().length() > 0))
			return res;
		else
			return completeFieldName;
	}
	
	/**
	 * @param request
	 * @return Spago Request Container
	 */
	public static RequestContainer getRequestContainer(HttpServletRequest request){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		
		if (qbeMode.equalsIgnoreCase("WEB")){
			return RequestContainerAccess.getRequestContainer(request);
		}else{
			return RequestContainerPortletAccess.getRequestContainer(request);
		}
	}
	
	/**
	 * @param requestContainer
	 * @param dmModel
	 * @param wizObj
	 * @param completeFieldName
	 * @return
	 */
	public static String getLabelForQueryField(RequestContainer requestContainer, DataMartModel dmModel, ISingleDataMartWizardObject wizObj,  String completeFieldName){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		Locale loc = LocaleUtils.getLocale(requestContainer, qbeMode);
		Properties prop = Utils.getLabelProperties(dmModel, ApplicationContainer.getInstance(), loc);
		
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
		
		String classLabel = Utils.getLabelForClass(requestContainer, dmModel, className);
		
		String realFieldName = fieldNameWithoutOperators.substring(dotIndexOf + 1);
		String res =(String)prop.get("field." + realFieldName); 
		if ((res != null) && (res.trim().length() > 0))
			return prefix + classLabel+ "." + res + postFix;
		else
			return prefix + classLabel+ "." + fieldNameWithoutOperators + postFix;
	}
	

	/**
	 * Update the QBE_LAST_UPDATE_TIMESTAMP in session container
	 * @param reqContainer
	 */
	public static void updateLastUpdateTimeStamp(RequestContainer reqContainer){
			String str = String.valueOf(System.currentTimeMillis());
			Logger.debug(Utils.class,"Last Update Timestamp ["+str+"]");
			reqContainer.getSessionContainer().setAttribute("QBE_LAST_UPDATE_TIMESTAMP", str);
	}
		
	public static String getReportServletContextAddress(){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		String path = null;
		if (qbeMode.equalsIgnoreCase("WEB")){
			path = "..";
		}else{
			PortletRequest portletRequest = it.eng.spago.util.PortletUtilities.getPortletRequest();
			Logger.debug(Utils.class, 
					"getReportServletContextAddress portlet request obtained: " + portletRequest);
			path ="http://"+portletRequest.getServerName()+ ":"+portletRequest.getServerPort() + portletRequest.getContextPath(); 
			Logger.debug(Utils.class, 
					"getReportServletContextAddress using context path: " + path);
		}
		
		return path;
		//return "http://"+portletRequest.getServerName()+ ":"+portletRequest.getServerPort() +"/spagobi"; 
	}
	
	public static String getAbsoluteReportServletContext(RequestContainer requestContainer){
		String qbeMode = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MODE.mode");
		//		 Retrieve Locale
		String path = null;
		if (qbeMode.equalsIgnoreCase("WEB")){
			String contextPath = (String)requestContainer.getAttribute("HTTP_REQUEST_CONTEXT_PATH");
			path = "http://"+requestContainer.getAttribute("host")+ contextPath;
		}else{
			PortletRequest portletRequest = it.eng.spago.util.PortletUtilities.getPortletRequest();
			Logger.debug(Utils.class, 
					"getReportServletContextAddress portlet request obtained: " + portletRequest);
			path ="http://"+portletRequest.getServerName()+ ":"+portletRequest.getServerPort() + portletRequest.getContextPath(); 
			Logger.debug(Utils.class, 
					"getReportServletContextAddress using context path: " + path);
		}
		
		return path;
		//return "http://"+portletRequest.getServerName()+ ":"+portletRequest.getServerPort() +"/spagobi"; 
	}
	
	public static String[] getJndiDsDialectFromModel(DataMartModel dm){
		
		String[] result = new String[2];
		
		//ALTRIMENTI CERCO I PARAMETRI DI CONFIGURAZIONE SUL FILE hibconn.properies
		URL hibConnPropertiesUrl = JarUtils.getResourceFromJarFile(dm.getJarFile(), "hibconn.properties") ;
		if (hibConnPropertiesUrl != null){
			Properties prop = new Properties();
			try{
				prop.load(hibConnPropertiesUrl.openStream());
			}catch (IOException ioe) {
				ioe.printStackTrace();
			}
			result[0] = prop.getProperty("hibernate.connection.datasource");
			result[1] = prop.getProperty("hibernate.dialect");		
		}
		
		return result;
	}
	
	public static List getAllJndiDS(){				
		List listResult = new ArrayList();
		try{
			Context ctx = new InitialContext();
			NamingEnumeration list = ctx.listBindings("java://comp/env/jdbc");
			
			while (list.hasMore()){
				NameClassPair item = (NameClassPair)list.next();
				String cl = item.getClassName();
				String name = item.getName();
				//System.out.println(cl+" - "+name);
				listResult.add("jdbc/"+name);
			}
		 }catch (Exception e) {
			e.printStackTrace();
		}
		
		return listResult;
	}
	

	
	public static String hashMD5(String original) throws EMFInternalError {
		 byte[] stringByteArray = new byte[original.length()];
		 try {
			 stringByteArray = original.getBytes("UTF-8");
	        } // try
	        catch (UnsupportedEncodingException uee) {
	            throw new EMFInternalError(EMFErrorSeverity.ERROR, "Autenticazione fallita", uee);
	        } // catch (UnsupportedEncodingException uee)
	        MessageDigest algorithm = null;
	        try {
	            algorithm = MessageDigest.getInstance("SHA-1");
	        } // try
	        catch (NoSuchAlgorithmException nsae) {
	            throw new EMFInternalError(EMFErrorSeverity.ERROR, "Autenticazione fallita", nsae);
	        } // catch (NoSuchAlgorithmException nsae)
	        algorithm.reset();
	        algorithm.update(stringByteArray);
	        byte[] digestedString = algorithm.digest();
	        return new BASE64Encoder().encodeBuffer(digestedString);
	}
        
   
	public static boolean isUserAble(IEngUserProfile userProfile, String func){
		try{
			Collection userFuncs = userProfile.getFunctionalities();
			return userFuncs.contains(func) || userFuncs.contains(func.toUpperCase());
		}catch (EMFInternalError e) {
			return false;
		}
	}
   
	
	public static ISingleDataMartWizardObject getWizardObject(SessionContainer sessionContainer){
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		String qbeQueryMode = (String)sessionContainer.getAttribute(WizardConstants.QUERY_MODE);

		if (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(WizardConstants.SUBQUERY_MODE)){
			   String subQueryField = (String)sessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
			   //ISingleDataMartWizardObject newWizObject =  aWizardObject.getSubQueryOnField(subQueryField);
			   ISingleDataMartWizardObject newWizObject =  aWizardObject.getSelectedSubquery();
			   return newWizObject;
		}
		
		return aWizardObject;
	}
	
	public static ISingleDataMartWizardObject getMainWizardObject(SessionContainer sessionContainer){
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		
		return aWizardObject;
	}
	
	public static boolean isSubQueryModeActive(SessionContainer sessionContainer){
		
		String qbeQueryMode = (String)sessionContainer.getAttribute(WizardConstants.QUERY_MODE);

		if (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(WizardConstants.SUBQUERY_MODE)){
			return true;
		}else{
			return false;
		}
		
		
	}
	/**
	 * Estrae dal file formula.xml solo i campi calcolati relativi alle entita' che 
	 * ho estratto nella query
	 * @param aWizardObject
	 * @return
	 */
	public static List getCalculatedFields(ISingleDataMartWizardObject aWizardObject, DataMartModel dmModel) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f =  null;
		try{
			
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			String formulaFile = dmModel.getJarFile().getParent() + "/formula.xml";
			f = new File(formulaFile);
			if (!f.exists()){
				return new ArrayList();
			}
			saxReader = new SAXReader();
			formulaFileDocument = saxReader.read(f);
			
			
			Iterator it = aWizardObject.getEntityClasses().iterator();
			EntityClass ec = null;
			String ecName = null;
			String xPath = null;
			Node domNode = null;
			CalculatedField cfield = null;
			while(it.hasNext()){
				ec =(EntityClass) it.next();
				ecName = ec.getClassName(); 
				xPath = "/FORMULAS/FORMULA[@onEntity = '"+ecName+"' and @mode='auto']";
				
				List nodeList = formulaFileDocument.selectNodes(xPath);
				
				for (Iterator it2 = nodeList.iterator(); it2.hasNext();){
					domNode = (Node)it2.next();
					cfield = new CalculatedField();
					cfield.setEntityName(domNode.valueOf("@onEntity"));
					cfield.setId(domNode.valueOf("@id"));
					cfield.setFldLabel(domNode.valueOf("@resultingFieldName"));
					cfield.setScript(domNode.valueOf("@script"));
					cfield.setMappings(domNode.valueOf("@mappings"));
					cfield.setClassNameInQuery(cfield.getEntityName());
					cfield.setFldCompleteNameInQuery(cfield.getEntityName() + "." + cfield.getId());
					cfield.setInExport(domNode.valueOf("@inExport"));
					calcuatedFields.add(cfield);
				}
			}
			
		}
		return calcuatedFields;
		}catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}finally{
			f = null;
			saxReader = null;
			formulaFileDocument = null;
		}
		
	}
	
	public static CalculatedField getCalculatedField(String cFieldId, String formulaFileParentPath) throws Exception{
		File f = null;
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		try{
			String formulaFile = formulaFileParentPath + "/formula.xml";
			
			f = new File(formulaFile);
			if (!f.exists()){
				return null;
			}
			saxReader = new SAXReader();
			formulaFileDocument = saxReader.read(f);
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
			f = null;
			saxReader = null;
			formulaFileDocument = null;
		}
	}
	
	
	public static List getCalculatedFields(String entitiesList, String formulaFileParentPath) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f = null;
		try{
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			String formulaFile = formulaFileParentPath + "/formula.xml";
			f = new File(formulaFile);
			if (!f.exists()){
				return new ArrayList();
			}
			saxReader = new SAXReader();
			formulaFileDocument = saxReader.read(f);
			
			
			String[] entitiesName = entitiesList.split(";");
			String ecName = null;
			String xPath = null;
			Node domNode = null;
			CalculatedField cfield = null;
			for (int i=0; i < entitiesName.length; i++){
				
				ecName = entitiesName[i];
				xPath = "/FORMULAS/FORMULA[@onEntity = '"+ecName+"' and @mode='auto']";
				
				List nodeList = formulaFileDocument.selectNodes(xPath);
				
				for (Iterator it2 = nodeList.iterator(); it2.hasNext();){
					domNode = (Node)it2.next();
					cfield = new CalculatedField();
					cfield.setId(domNode.valueOf("@id"));
					cfield.setEntityName(domNode.valueOf("@onEntity"));
					cfield.setFldLabel(domNode.valueOf("@resultingFieldName"));
					cfield.setScript(domNode.valueOf("@script"));
					cfield.setMappings(domNode.valueOf("@mappings"));
					cfield.setInExport(domNode.valueOf("@inExport"));
					cfield.setClassNameInQuery(cfield.getEntityName());
					cfield.setFldCompleteNameInQuery(cfield.getEntityName() + "." + cfield.getId());
					calcuatedFields.add(cfield);
				}
			}
			
		}
		return calcuatedFields;
		}catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}finally{
			f = null;
			saxReader = null;
			formulaFileDocument = null;
		}
		
	}
	
	
	public static List getManualCalculatedFieldsForEntity(String ecName, String formulaFileParentPath) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f = null;
		try{
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			String formulaFile = formulaFileParentPath + "/formula.xml";
			
			f = new File(formulaFile);
			if (!f.exists()){
				return new ArrayList();
			}
			saxReader = new SAXReader();
			formulaFileDocument = saxReader.read(f);
			
			
	
			String xPath = null;
			Node domNode = null;
			CalculatedField cfield = null;
			
				
				
				xPath = "/FORMULAS/FORMULA[@onEntity = '"+ecName+"' and @mode='manual']";
				
				List nodeList = formulaFileDocument.selectNodes(xPath);
				
				for (Iterator it2 = nodeList.iterator(); it2.hasNext();){
					domNode = (Node)it2.next();
					cfield = new CalculatedField();
					cfield.setId(domNode.valueOf("@id"));
					cfield.setEntityName(domNode.valueOf("@onEntity"));
					cfield.setFldLabel(domNode.valueOf("@resultingFieldName"));
					cfield.setScript(domNode.valueOf("@script"));
					cfield.setMappings(domNode.valueOf("@mappings"));
					cfield.setMappings(domNode.valueOf("@inExport"));
					calcuatedFields.add(cfield);
				}
			
			
		}
		return calcuatedFields;
		}catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}finally{
			f = null;
			saxReader = null;
			formulaFileDocument = null;
		}
		
	}
	
	
	
	
	public static Integer findPositionOf(ISingleDataMartWizardObject aWizardObject, String completeName){
		if (aWizardObject.getSelectClause() != null){
			List l = aWizardObject.getSelectClause().getSelectFields();
			ISelectField selField = null;
			String selFieldcompleteName = null;
			for (int i=0; i < l.size(); i++){
				selField = (ISelectField)l.get(i);
				selFieldcompleteName = selField.getFieldCompleteName();
				if ((selFieldcompleteName != null) && (selFieldcompleteName.equals(completeName))){
					return new Integer(i);
				}
			}
		}
		
		return new Integer(-1);
	}
	
	
	public static String getOrderedFieldList(ISingleDataMartWizardObject wizObject){
		StringBuffer sb = new StringBuffer();
		if (wizObject.getSelectClause() != null){
			List l = wizObject.getSelectClause().getSelectFields();
			
			for (Iterator it = l.iterator(); it.hasNext();){
				sb.append(((ISelectField)it.next()).getFieldCompleteName());
				if (it.hasNext())
					sb.append(";");
			}
		}
		
		
		return sb.toString();
	}
	
	public static String getSelectedEntitiesAsString(ISingleDataMartWizardObject aWizardObject) throws Exception{
		Iterator it = aWizardObject.getEntityClasses().iterator();
		EntityClass ec = null;
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()){
			ec = (EntityClass)it.next();
			sb.append(ec.getClassName());
			if (it.hasNext()){
				sb.append(";");
			}
		}
		return sb.toString();
	}
	
	public static String asJavaClassIdentifier(String identifier){
		return capitalize(asJavaIdentifier(identifier));
	}
	public static String asJavaPropertyIdentifier(String identifier){
		return unCapitalize(asJavaIdentifier(identifier));
	}
	public static String asJavaIdentifier(String identifier) {
		
		StringBuffer sb = new StringBuffer();
		String originalIdentifier = identifier;
		
		if (identifier.equalsIgnoreCase(identifier.toUpperCase())){
			originalIdentifier = identifier.toLowerCase();
		}
			
			StringTokenizer st = new StringTokenizer(originalIdentifier, "_ ", false);
			boolean isFirstToken = false;
			while (st.hasMoreTokens()){
				if (!isFirstToken){
					sb.append(capitalize(st.nextToken()));
				}else{
					sb.append(st.nextToken());
					isFirstToken = true;
				}
			}	
			
		return sb.toString();
	}
	
	public static String capitalize(String value) {
        if (value == null) {
            return null;
        }

        java.util.StringTokenizer tokenizer = new StringTokenizer(value, " ");
        StringBuffer result = new StringBuffer();

        while (tokenizer.hasMoreTokens()) {
            StringBuffer word = new StringBuffer(tokenizer.nextToken());

            // upper case first character
            word.replace(0, 1, word.substring(0, 1).toUpperCase());

            if (!tokenizer.hasMoreTokens()) {
                result.append(word);
            } else {
                result.append(word + " ");
            }
        }

        return result.toString();
    }
	
	public static String unCapitalize(String value) {
        if (value == null) {
            return null;
        }

        java.util.StringTokenizer tokenizer = new StringTokenizer(value, " ");
        StringBuffer result = new StringBuffer();

        while (tokenizer.hasMoreTokens()) {
            StringBuffer word = new StringBuffer(tokenizer.nextToken());

            // upper case first character
            word.replace(0, 1, word.substring(0, 1).toLowerCase());

            if (!tokenizer.hasMoreTokens()) {
                result.append(word);
            } else {
                result.append(word + " ");
            }
        }

        return result.toString();
    }
	public static String packageAsDir(String packageName){
		String dir = packageName.replace('.', File.separatorChar);
		return dir;
	}
	
	//	 Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }
}
