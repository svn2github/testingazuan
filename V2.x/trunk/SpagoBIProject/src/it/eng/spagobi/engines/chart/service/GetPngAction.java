package it.eng.spagobi.engines.chart;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetPngAction extends AbstractHttpAction {

	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
	throws Exception {

		freezeHttpResponse();

		HttpServletResponse response = getHttpResponse();
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("image/gif");

		HttpServletRequest req = getHttpRequest();
		String path = (String)serviceRequest.getAttribute("path");


		FileInputStream fis=new FileInputStream(path);

		int avalaible = fis.available();   // Mi informo sul num. bytes.

		for(int i=0; i<avalaible; i++) {
			out.write(fis.read()); 
		}

		fis.close();
		out.flush();	
		out.close();

		// RIMUOVO FISICAMENTE IL FILE DAL REPOSITORY
		File fileToDelete = new File(path);
		if( fileToDelete.delete() ){ 
			System.out.println("CANCELLATO");			
		}else{ 
			System.out.println(fileToDelete.getPath());
		} 
		


	}

}
