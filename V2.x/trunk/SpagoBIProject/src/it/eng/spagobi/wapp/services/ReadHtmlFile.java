package it.eng.spagobi.wapp.services;


import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ReadHtmlFile extends AbstractHttpAction{

	static private Logger logger = Logger.getLogger(ReadHtmlFile.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {
	    logger.debug("IN");
		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		httpResp.setContentType("text/html");
		ServletOutputStream out = httpResp.getOutputStream();

		String menuId=(String)serviceRequest.getAttribute("MENU_ID");
		logger.debug("menuId="+menuId);
		if(menuId!=null){
			Menu menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(menuId));
			String fileName=menu.getStaticPage();
			
			// check the validity of the fileName (it must not be a path)
			// TODO remove this control and write better this action, or remove the action at all
			if (fileName.contains("\\") || fileName.contains("/") || fileName.contains("..")) {
				logger.error("Menu with id = " + menu.getMenuId() + " has file name [" + fileName + "] containing file separator character!!!");
				throw new Exception("Menu file name cannot contain file separator character");
			}
			
			logger.debug("fileName="+fileName);
			String rootPath=ConfigSingleton.getRootPath();
			String filePath=rootPath+System.getProperty("file.separator")+"static_content"+System.getProperty("file.separator")+fileName;
			logger.debug("filePath="+filePath);
			FileInputStream fis=new FileInputStream(filePath);

			int avalaible = fis.available();   // Mi informo sul num. bytes.

			for(int i=0; i<avalaible; i++) {
				out.write(fis.read()); 
			}

			fis.close();
			out.flush();	
			out.close();	 	
			logger.debug("OUT");
		}
		else{
		    logger.error("missin id");
			throw new Exception("missin id");
		}
	}

}
