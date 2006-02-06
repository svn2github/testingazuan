<%@ page import="java.io.*,
				 java.util.*,
				 javax.servlet.ServletOutputStream" %>

<%
	Map imagesMap = (Map)session.getAttribute("IMAGES_MAP");

	if (imagesMap != null)
	{
	String imageName = request.getParameter("image");
		if (imageName != null) {
			byte[] imageData = (byte[])imagesMap.get(imageName);
			response.setContentLength(imageData.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(imageData, 0, imageData.length);
			ouputStream.flush();
			ouputStream.close();
			imagesMap.remove(imageName);
		}
	}
%>
