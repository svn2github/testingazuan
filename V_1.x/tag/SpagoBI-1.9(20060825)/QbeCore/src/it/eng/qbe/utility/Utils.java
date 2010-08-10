package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
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
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.JarFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

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
	/**
	 * This method is responsible to get Propertis  associated with datamart models, Properties objects
	 * are cached in Application Container
	 * @param dm: The datamart model
	 * @param application: Spago Application Container
	 * @return the Properties object associated with datamart
	 */
	public static Properties getLabelProperties(DataMartModel dm, ApplicationContainer application) {
		
		Properties props = new Properties();
		try{
			String propEntryInApplicationContext = dm.getPath()+"_labels";
			if (application.getAttribute(propEntryInApplicationContext) != null){
				props =  (Properties)application.getAttribute(propEntryInApplicationContext);
			}else{
				File dmJarFile = dm.getJarFile();
				JarFile jf = new JarFile(dmJarFile);
			
				props = LocaleUtils.getLabelProperties(jf);
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
		try{
			String propEntryInApplicationContext = dm.getPath()+"_labels_"+loc.getLanguage();
			if (application.getAttribute(propEntryInApplicationContext) != null){
				props =  (Properties)application.getAttribute(dm.getPath()+"_labels_"+loc.getLanguage());
			}else{
				File dmJarFile = dm.getJarFile();
				JarFile jf = new JarFile(dmJarFile);
			
				props = LocaleUtils.getLabelProperties(jf, loc);
				
				if (props.isEmpty())
					return getLabelProperties(dm, application);
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
	
	public static IQbeMessageHelper getQbeMessageHelper(){
		String qbeMsgHelperClass = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MSG-HELPER.className");
		
		IQbeMessageHelper msgHelper = null;
		try{
			msgHelper = (IQbeMessageHelper)Class.forName(qbeMsgHelperClass).newInstance();
		}catch (Exception e) {
			e.printStackTrace();
			Logger.error(Utils.class, e);
		}
		return msgHelper;
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
				System.out.println(cl+" - "+name);
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
   
	
	
	
	
	
	
	
	
	
}
