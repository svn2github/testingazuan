/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.utility;

import it.eng.qbe.datasource.IHibernateDataSource;
import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.query.ISelectField;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Encoder;


// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {

	
	
	/**
	 * Gets the data mart model retriever.
	 * 
	 * @return the data mart model retriever
	 * 
	 * @throws Exception the exception
	 */
	public static IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
		IDataMartModelRetriever dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
		return dataMartModelRetriever;
	}
	
	/**
	 * Gets the view jar files.
	 * 
	 * @param dataSource the data source
	 * 
	 * @return the view jar files
	 */
	public static List getViewJarFiles(IHibernateDataSource dataSource){
		try{
			IDataMartModelRetriever dataMartModelRetriever = getDataMartModelRetriever();
			return dataMartModelRetriever.getViewJarFiles(dataSource.getName());
		}catch (Exception e) {
			Logger.error(DataMartModel.class, e);
			return null;
		}
	}
	
	/*
	public static Properties getLabelProperties(DataMartModel dm, ApplicationContainer application) {
		
		return dm.getLabelProperties();
	
	}
	*/
	
	
	

	/**
	 * Update the QBE_LAST_UPDATE_TIMESTAMP in session container.
	 * 
	 * @param reqContainer the req container
	 */
	public static void updateLastUpdateTimeStamp(RequestContainer reqContainer){
			String str = String.valueOf(System.currentTimeMillis());
			Logger.debug(Utils.class,"Last Update Timestamp ["+str+"]");
			reqContainer.getSessionContainer().setAttribute("QBE_LAST_UPDATE_TIMESTAMP", str);
	}
		
	/*
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
	
	
	
	public static List getAllJndiDS(){				
		List listResult = new ArrayList();
		try{
			Context ctx = new InitialContext();
			NamingEnumeration list = ctx.listBindings("java://comp/env/jdbc");
			
			while (list.hasMore()){
				NameClassPair item = (NameClassPair)list.next();
				String cl = item.getClassName();
				String name = item.getName();
				System.out.println(cl+" - "+name);
				listResult.add("jdbc/"+name);
			}
		 }catch (Exception e) {
			e.printStackTrace();
		}
		
		return listResult;
	}
	*/

	
	/**
	 * Hash m d5.
	 * 
	 * @param original the original
	 * 
	 * @return the string
	 * 
	 * @throws EMFInternalError the EMF internal error
	 */
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
        
   
	/**
	 * Checks if is user able.
	 * 
	 * @param userProfile the user profile
	 * @param func the func
	 * 
	 * @return true, if is user able
	 */
	public static boolean isUserAble(IEngUserProfile userProfile, String func){
		try{
			Collection userFuncs = userProfile.getFunctionalities();
			return userFuncs.contains(func) || userFuncs.contains(func.toUpperCase());
		}catch (EMFInternalError e) {
			return false;
		}
	}
   
	/*
	public static ISingleDataMartWizardObject getWizardObject(SessionContainer sessionContainer){
		ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject)sessionContainer.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
		String qbeQueryMode = (String)sessionContainer.getAttribute(WizardConstants.QUERY_MODE);

		if (qbeQueryMode != null && qbeQueryMode.equalsIgnoreCase(WizardConstants.SUBQUERY_MODE)){
			   String subQueryField = (String)sessionContainer.getAttribute(WizardConstants.SUBQUERY_FIELD);
			   //ISingleDataMartWizardObject newWizObject =  aWizardObject.getSubQueryOnField(subQueryField);
			   ISingleDataMartWizardObject newWizObject =  (ISingleDataMartWizardObject)aWizardObject.getQuery().getSelectedSubquery();
			   return newWizObject;
		}
		
		return aWizardObject;
	}
	*/
	
	/*
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
	*/
	
	/**
	 * Estrae dal file formula.xml solo i campi calcolati relativi alle entita' che
	 * ho estratto nella query
	 * 
	 * @param cFieldId the c field id
	 * @param formulaFile the formula file
	 * 
	 * @return the calculated field
	 * 
	 * @throws Exception the exception
	 */
	/*
	public static List getCalculatedFields(ISingleDataMartWizardObject aWizardObject, DataMartModel dmModel) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f =  null;
		try{
			
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			f = dmModel.getFormulaFile();
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
	*/
	
	
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
	
	/*
	public static List getCalculatedFields(String entitiesList, File formulaFile) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f = null;
		try{
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			f = formulaFile;
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
	*/
	
	/*
	public static List getManualCalculatedFieldsForEntity(String ecName, DataMartModel datamartModel) throws Exception{
		SAXReader saxReader = null;
		Document formulaFileDocument = null;
		File f = null;
		try{
		String enableScript = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-ENABLE-SCRIPT.enablescript");
		
		List calcuatedFields = new ArrayList();
		if ((enableScript != null) && (enableScript.equalsIgnoreCase("true"))){
			
			f = datamartModel.getFormulaFile();
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
	
	*/
	
	/*
	public static Integer findPositionOf(IQuery query, String completeName){
		if (query.getSelectClause() != null){
			List l = query.getSelectClause().getSelectFields();
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
	*/
	
	
	/**
	 * Gets the ordered field list.
	 * 
	 * @param wizObject the wiz object
	 * 
	 * @return the ordered field list
	 */
	public static String getOrderedFieldList(ISingleDataMartWizardObject wizObject){
		StringBuffer sb = new StringBuffer();
		if (!wizObject.getQuery().isEmpty()){
			
			for (Iterator it = wizObject.getQuery().getSelectFieldsIterator(); it.hasNext();){
				sb.append(((ISelectField)it.next()).getFieldCompleteName());
				if (it.hasNext())
					sb.append(";");
			}
		}
		
		
		return sb.toString();
	}
	
	/**
	 * Gets the selected entities as string.
	 * 
	 * @param aWizardObject the a wizard object
	 * 
	 * @return the selected entities as string
	 * 
	 * @throws Exception the exception
	 */
	public static String getSelectedEntitiesAsString(ISingleDataMartWizardObject aWizardObject) throws Exception{
		Iterator it = aWizardObject.getQuery().getEntityClassesIterator();
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
	
	/**
	 * As java class identifier.
	 * 
	 * @param identifier the identifier
	 * 
	 * @return the string
	 */
	public static String asJavaClassIdentifier(String identifier){
		return capitalize(asJavaIdentifier(identifier));
	}
	
	/**
	 * As java property identifier.
	 * 
	 * @param identifier the identifier
	 * 
	 * @return the string
	 */
	public static String asJavaPropertyIdentifier(String identifier){
		return unCapitalize(asJavaIdentifier(identifier));
	}
	
	/**
	 * As java identifier.
	 * 
	 * @param identifier the identifier
	 * 
	 * @return the string
	 */
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
	
	/**
	 * Capitalize.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
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
	
	/**
	 * Un capitalize.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
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
	
	/**
	 * Package as dir.
	 * 
	 * @param packageName the package name
	 * 
	 * @return the string
	 */
	public static String packageAsDir(String packageName){
		String dir = packageName.replace('.', File.separatorChar);
		return dir;
	}
	
	//	 Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    /**
	 * Delete dir.
	 * 
	 * @param dir the dir
	 * 
	 * @return true, if successful
	 */
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
