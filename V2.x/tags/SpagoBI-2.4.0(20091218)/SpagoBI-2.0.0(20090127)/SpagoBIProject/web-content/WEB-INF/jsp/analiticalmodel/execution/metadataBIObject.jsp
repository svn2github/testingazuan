<!--
SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
-->

<%@ include file="/WEB-INF/jsp/commons/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter,				 
				 it.eng.spagobi.commons.dao.DAOFactory,			
				 java.util.List,			 
				 it.eng.spagobi.commons.bo.Domain,
				 java.util.Iterator,
				 it.eng.spagobi.engines.config.bo.Engine,			
				 it.eng.spago.base.SourceBean,	
				 it.eng.spagobi.monitoring.metadata.SbiAudit,		
				 java.util.Date"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.eng.spagobi.tools.datasource.bo.DataSource"%>
<%@page import="it.eng.spagobi.monitoring.dao.AuditManager"%>

<%
	// GET RESPONSE OBJECTS
	String docLabel = "";
	String docName = "";
	String docType = "";
	String engineName = "";
	String driverName = "";
	String dataSource = "";
	
	String docDescr = "";
	String docObjective = ""; 
	String docLongDescr = ""; 
	String docImage = ""; 
	Double docRating = new Double (0); 
	String keywords = ""; 
	
	String docLanguage = ""; 
	String creationDate = ""; 
	String creationUser = "";
	String templateDimension = "";
	
	String lastModifDate = ""; 
	String lastModifUser = ""; 
	
	String lastAccesDate = "";
	String lastExecTime = "";
	Double mediumExecTime = new Double(0);	
	String lastExecUser = "";	
	
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("MetadataBIObjectModule"); 
	BIObject obj = (BIObject) moduleResponse.getAttribute("biObj");
	Integer docId = obj.getId();
	docLabel = obj.getLabel();
	docName = obj.getName();
	Integer docTypeId = obj.getBiObjectTypeID();
	Domain d = DAOFactory.getDomainDAO().loadDomainById(docTypeId);
	if (d != null) docType = d.getValueName();
	Engine engine = obj.getEngine();	
	if (engine != null) engineName = engine.getName();

	List param = DAOFactory.getBIObjectDAO().getBIObjectParameters(obj);
	if (param != null && !param.isEmpty()) {
		Iterator it = param.iterator();
	    while (it.hasNext()){
	    	BIObjectParameter part = (BIObjectParameter)it.next();
		   String par = part.getLabel();
		   driverName = driverName + par +" ; ";
	   }	
	}
	Integer dsID = obj.getDataSourceId();
	if (dsID != null){ DataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByID(dsID);
	if (ds!=null) dataSource = ds.getLabel();}
	
	docDescr = (obj.getDescription()!= null ? obj.getDescription() : "");
	docObjective = (obj.getObjectve()!= null ? obj.getObjectve() : ""); 
	docLongDescr = (obj.getExtendedDescription()!= null ? obj.getExtendedDescription() : ""); 
	Double temp =  DAOFactory.getBIObjectRatingDAO().calculateBIObjectRating(obj);
	docRating = ( temp != null ? temp :new Double (0) );
	keywords = (obj.getKeywords()!= null ? obj.getKeywords() : ""); 
	
	docLanguage = (obj.getLanguage()!= null ? obj.getLanguage() : ""); 
	
	creationDate = (obj.getCreationDate() != null ? obj.getCreationDate().toString() : ""); 
	creationUser = (obj.getCreationUser()!= null ? obj.getCreationUser() : ""); 
	
	ObjTemplate currTemplate = obj.getActiveTemplate();
	if (currTemplate !=null){
	templateDimension = currTemplate.getDimension();
	lastModifDate = currTemplate.getCreationDate().toString(); 
	lastModifUser = currTemplate.getCreationUser(); 
	}
	
    SbiAudit sba = AuditManager.getInstance().getLastExecution(docId);
    if (sba != null && sba.getUserName() != null) lastExecUser = sba.getUserName();
    Date lastExecDate = sba.getExecutionStartTime();
    if (lastExecDate != null )lastExecTime = lastExecDate.toString();
    AuditManager sb = AuditManager.getInstance();
    if (sb != null) mediumExecTime = sb.getMediumExecTime(docId);
%>


<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'General Data',
        collapsible:true,
        collapsed : false,
        renderTo: 'container1',
        contentEl : 'generalData'
    });
});
</script>
<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Technical Data',
        collapsible:true,
        collapsed : true,
        renderTo: 'container2',
        contentEl : 'technicalData'
    });
});
</script>
<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Business Questions',
        collapsible:true,
        collapsed : true,
        renderTo: 'container3',
        contentEl : 'businessQuestions'
    });
});
</script>
<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Categories',
        collapsible:true,
        collapsed : true,
        renderTo: 'container4',
        contentEl : 'categories'
    });
});
</script>
		<div id="container1"> </div>	

		<div id="container2"> </div>	
		
		<div id="container3"> </div>	

		<div id="container4"> </div>
	

<div id="generalData" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">
	
	<!-- TABLE GENERAL DATA -->		
	
	<table style="width:100%;margin-top:1px" >
		<!-- DOC NAME -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left"><spagobi:message key = "metadata.docName" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docName%>
			</td>
		</tr>	
		
		<!-- DOC DESCR -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left"><spagobi:message key = "metadata.docDescr" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docDescr%>
			</td>
		</tr>
		
		<!-- DOC LONG DESCR -->
		<tr>
		    <td class="portlet-section-header"  width="170" style="text-align:left"><spagobi:message key = "metadata.docLongDescr" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docLongDescr%>
			</td>
		</tr>
		
		<!-- DOC OBJECTIVE -->
		<tr>
		    <td class="portlet-section-header" width="170"  style="text-align:left"><spagobi:message key = "metadata.docObjective" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docObjective%>
			</td>
		</tr>
		
		<!-- DOC RATING -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left"><spagobi:message key = "metadata.docRating" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docRating.toString()%>
			</td>
		</tr>
		<spagobi:error/>
	</table> 
</div>		


<div id="technicalData" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">	
	<!-- TABLE TECHNICAL DATA -->		
	<table style="width:100%;margin-top:1px">
		<!-- DOC ID -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docId" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docId.toString()%>
			</td>
		</tr>	
		
		<!-- DOC LABEL -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docLabel" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docLabel%>
			</td>
		</tr>
		
		<!-- DOC TYPE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docType" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docType.toString()%>
			</td>
		</tr>
		
		<!-- ENGINE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docEngine" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=engineName%>
			</td>
		</tr>	
		
		<!-- LANGUAGE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docLanguage" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docLanguage%>
			</td>
		</tr>
		
		<!-- TEMPLATE DIMENSION -->
		<tr>
		    <td class="portlet-section-header"  width="140" style="text-align:left"><spagobi:message key = "metadata.docTemplDim" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=templateDimension%>
			</td>
		</tr>
		
		<!-- DATA SOURCE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docDS" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=dataSource%>
			</td>
		</tr>
		
		<!-- DRIVERS NAME -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docDriver" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">
			&nbsp;<%=driverName%>
			</td>
		</tr>
		
		<!-- CREATION DATE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docCreateDate" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=creationDate%>
			</td>
		</tr>	
		
		<!-- CREATION USER -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docCreateUser" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=creationUser%>
			</td>
		</tr>
		
		<!-- LAST MODIF DATE -->
		<tr>
		    <td class="portlet-section-header"  width="140" style="text-align:left"><spagobi:message key = "metadata.docLastModif" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=lastModifDate%>
			</td>
		</tr>
		
		<!-- LAST MODIF USER -->
		<tr>
		    <td class="portlet-section-header"  width="140" style="text-align:left"><spagobi:message key = "metadata.docLastModifUser" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=lastModifUser%>
			</td>
		</tr>
		
		<!-- LAST EXECUTION DATE/TIME DATE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docLastExecTime" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=lastExecTime%>
			</td>
		</tr>	
		
		<!-- MEDIUM EXECUTION TIME -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docMediumTime" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=mediumExecTime.doubleValue()%> &nbsp;sec
			</td>
		</tr>
		
		<!-- LAST EXECUTION USER -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"  ><spagobi:message key = "metadata.docLastExecUser" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=lastExecUser%>
			</td>
		</tr>																											
	
	<spagobi:error/>
	</table> 
</div>  

<div id="businessQuestions" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">
	
	<!-- TABLE BUSINESS QUESTIONS -->		
	<table style="width:100%;margin-top:1px" >
		<!-- Business Questions -->
		<tr>
		    <td class="portlet-section-header" width="110" style="text-align:left"><spagobi:message key ="metadata.docBusinessQ" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;
			</td>
		</tr>	
	<spagobi:error/>
	</table> 
</div> 		

<div id="categories" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">
	
	<!-- TABLE CATEGORIES -->		
	<table style="width:100%;margin-top:1px" >
		<!-- Keywords -->
		<tr>
		    <td class="portlet-section-header" width="60" style="text-align:left"><spagobi:message key = "metadata.docKeyword" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=keywords%>
			</td>
		</tr>	
		
		<!-- Folders in which it's published -->
		<tr>
		    <td class="portlet-section-header" width="60" style="text-align:left"><spagobi:message key = "metadata.docFolder" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;
			</td>
		</tr>
	<spagobi:error/>
	</table> 
</div> 	
	

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>
