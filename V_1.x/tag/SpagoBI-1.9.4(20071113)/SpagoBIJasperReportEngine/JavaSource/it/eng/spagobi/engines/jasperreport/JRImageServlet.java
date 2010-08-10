/**
 * 
 * LICENSE: see COPYING file. 
 * 
 */
package it.eng.spagobi.engines.jasperreport;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class JRImageServlet extends HttpServlet {

	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		String mapName = request.getParameter("mapname");
		Map imagesMap = (Map)session.getAttribute(mapName);
		if(imagesMap != null){
			String imageName = request.getParameter("image");
			if (imageName != null) {
				byte[] imageData = (byte[])imagesMap.get(imageName);
				imagesMap.remove(imageName);
				if(imagesMap.isEmpty()){
					session.removeAttribute(mapName);
				}
				response.setContentLength(imageData.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(imageData, 0, imageData.length);
				ouputStream.flush();
				ouputStream.close();
			}
		}
	}

}
