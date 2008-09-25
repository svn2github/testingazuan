package it.eng.spagobi.wapp.services;


import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.wapp.bo.Menu;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ReadHtmlFile extends AbstractHttpAction{

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {

		freezeHttpResponse();
		HttpServletResponse httpResp = getHttpResponse();
		httpResp.setContentType("text/html");
		ServletOutputStream out = httpResp.getOutputStream();

		String menuId=(String)serviceRequest.getAttribute("MENU_ID");
		if(menuId!=null){
			Menu menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(menuId));
			String fileName=menu.getStaticPage();

			String rootPath=ConfigSingleton.getRootPath();
			String filePath=rootPath+System.getProperty("file.separator")+"static_content"+System.getProperty("file.separator")+fileName;

			FileInputStream fis=new FileInputStream(filePath);

			int avalaible = fis.available();   // Mi informo sul num. bytes.

			for(int i=0; i<avalaible; i++) {
				out.write(fis.read()); 
			}

			fis.close();
			out.flush();	
			out.close();	 	

		}
		else{
			throw new Exception("missin id");
		}
	}

}
