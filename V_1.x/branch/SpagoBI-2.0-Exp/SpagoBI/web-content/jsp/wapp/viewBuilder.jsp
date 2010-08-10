<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page language="java"
         extends="it.eng.spago.dispatching.httpchannel.AbstractHttpJspPagePortlet"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         session="true" 
         import="it.eng.spago.base.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
<%@page import="org.safehaus.uuid.UUIDGenerator"%>
<%@page import="org.safehaus.uuid.UUID"%>

<%@ taglib uri="/WEB-INF/tlds/spagobiwa.tld" prefix="spagobiwa" %>

<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("ViewBuilderModule"); 
	List containerList = (List)moduleResponse.getAttribute("CONTAINERS_LIST"); 
	Integer viewHeight = (Integer)moduleResponse.getAttribute("VIEW_HEIGHT");
	Integer viewWidth = (Integer)moduleResponse.getAttribute("VIEW_WIDTH");
	String viewCode = (String)moduleResponse.getAttribute("VIEW_CODE");
	Iterator containerIter = containerList.iterator();
	
	String contextName = ChannelUtilities.getSpagoBIContextName(request);
%>


<html>
  <head>
    <title>SpagoBI Home</title>
    
    <script type="text/javascript">
	     var djConfig = {isDebug: false, debugAtAllCosts: false};
    </script>
    <script type="text/javascript" src="<%=contextName%>/js/dojo/dojo.js"></script>
    <script language="JavaScript" type="text/javascript">
    	dojo.require("dojo.widget.TabContainer");
		dojo.require("dojo.widget.LinkPane");
		dojo.require("dojo.widget.ContentPane");
		dojo.require("dojo.widget.LayoutContainer");
    	dojo.require("dojo.widget.FisheyeList");
    	dojo.hostenv.writeIncludes();
    </script>

    
    <style>
      body {
	       font-family: Arial, Helvetica, sans-serif;
	       padding: 0;
	       margin: 0;
      }
      .dojoTabPaneWrapper {
  		padding : 10px 10px 10px 10px;
	  }
    </style> 
    
  </head>



  <body>
  
	<%
	    StringBuffer jsResize = new StringBuffer();
	    jsResize.append("<script>function resizeContent"+viewCode+"(width, height) {\n");
		while(containerIter.hasNext()) {
			SourceBean containerSB = (SourceBean)containerIter.next();
			String widthcont = (String)containerSB.getAttribute("width");
			String heightcont = (String)containerSB.getAttribute("height");
			String top = (String)containerSB.getAttribute("top");
			String left = (String)containerSB.getAttribute("left");
			String idCont = (String)containerSB.getAttribute("id");
			String style = (String)containerSB.getAttribute("style");
			if(style==null) {
				style="";
			}

			List tabs = containerSB.getAttributeAsList("TAB");
			Iterator iterTab = tabs.iterator();
	%>
			<div id="mainTabContainer_<%=idCont%>" dojoType="TabContainer" style="position:absolute;width:<%=widthcont%>; height:<%=heightcont%>;left:<%=left%>;top:<%=top%>;<%=style%>" selectedTab="tab1_<%=idCont%>" >
	<%
			int prog = 1;
			while(iterTab.hasNext()) {
				SourceBean tabSB = (SourceBean)iterTab.next();
				String title = (String)tabSB.getAttribute("title");
				title = title.trim();
				if(title.startsWith("#")){
					title = title.substring(1);
					title = msgBuilder.getMessage(aRequestContainer, title);
				}
				String idTab = (String)tabSB.getAttribute("id");
				SourceBean documentSB = (SourceBean)tabSB.getAttribute("SBIDOCUMENT");
				String docLabel = (String)documentSB.getAttribute("documentLabel");
				String docParameters = (String)documentSB.getAttribute("parameters");
				String executionIdentifier = viewCode + idCont + idTab;
				String contexName = ChannelUtilities.getSpagoBIContextName(request);
				String link = contexName + "/servlet/AdapterHTTP?PAGE=DirectExecutionPage" + 
						      "&DOCUMENT_LABEL=" + docLabel + 
						      "&DOCUMENT_PARAMETERS="+docParameters +
						      "&EXECUTION_IDENTIFIER=" + executionIdentifier;
				// calculate width iframe
				String widthContStr = widthcont;
				if(widthContStr.endsWith("%")) {
					widthContStr = widthContStr.substring(0, widthContStr.length()-1);
				}
				Integer widthContInt = new Integer(widthContStr);
				int widthiframe = (viewWidth.intValue() * widthContInt.intValue()) / 100;
				widthiframe = widthiframe - 30;
				
				// calculate height iframe
				String heightContStr = heightcont;
				if(heightContStr.endsWith("%")) {
					heightContStr = heightContStr.substring(0, heightContStr.length()-1);
				}
				Integer heightContInt = new Integer(heightContStr);
				int heightframe = (viewHeight.intValue() * heightContInt.intValue()) / 100;
				heightframe = heightframe - 50;
				
				jsResize.append("var widthCont_"+viewCode+"_tag"+prog+"_"+idCont+" = ((width * "+widthContInt+")/100);\n");
				jsResize.append("var heightCont_"+viewCode+"_tag"+prog+"_"+idCont+" = ((height * "+heightContInt+")/100);\n");
				jsResize.append("widthCont_"+viewCode+"_tag"+prog+"_"+idCont+" = widthCont_"+viewCode+"_tag"+prog+"_"+idCont+" - 30;\n");
				jsResize.append("var heightCont_"+viewCode+"_tag"+prog+"_"+idCont+" = ((height * "+heightContInt+")/100);\n");
				jsResize.append("heightCont_"+viewCode+"_tag"+prog+"_"+idCont+" = heightCont_"+viewCode+"_tag"+prog+"_"+idCont+" - 50;\n");
				jsResize.append("var frame_"+viewCode+"_tag"+prog+"_"+idCont+" = document.getElementById('"+idCont+"_"+idTab+"');\n");
				jsResize.append("frame_"+viewCode+"_tag"+prog+"_"+idCont+".style.height=heightCont_"+viewCode+"_tag"+prog+"_"+idCont+" + 'px';\n");
				jsResize.append("frame_"+viewCode+"_tag"+prog+"_"+idCont+".style.width=widthCont_"+viewCode+"_tag"+prog+"_"+idCont+" + 'px'; \n");
				jsResize.append("try{\n");
				jsResize.append("	frame_"+viewCode+"_tag"+prog+"_"+idCont+".contentWindow.resizeContent(widthCont_"+viewCode+"_tag"+prog+"_"+idCont+", heightCont_"+viewCode+"_tag"+prog+"_"+idCont+"); \n");
				jsResize.append("} catch(err) {}\n");
				
	%>
				<div onValueChanged="alert('tabchange');" id="tab<%=prog%>_<%=idCont%>" dojoType="ContentPane" label="<%=title%>">
        			<iframe id="<%=idCont%>_<%=idTab%>"  style="width:<%=widthiframe%>px;height:<%=heightframe%>px;" src="<%=link%>" frameborder="0" scrolling="auto"></iframe>
        		</div>
	<%
				prog ++;
			}
	%>			
			</div>			
	<%
		}
		jsResize.append("}</script>\n");
	%>
	<%=jsResize.toString()%>
  </body>
  
</html>







