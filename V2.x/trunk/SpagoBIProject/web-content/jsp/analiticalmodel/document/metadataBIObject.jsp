<!--
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
-->

<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter,
				 it.eng.spagobi.analiticalmodel.document.service.DetailBIObjectModule,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.dao.IParameterDAO,
				 it.eng.spagobi.commons.dao.DAOFactory,
				 it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter,
				 java.util.List,
				 it.eng.spagobi.commons.constants.ObjectsTreeConstants,
				 it.eng.spagobi.commons.constants.AdmintoolsConstants,
				 it.eng.spagobi.commons.bo.Domain,
				 java.util.Iterator,
				 it.eng.spagobi.engines.config.bo.Engine,
				 it.eng.spagobi.commons.utilities.SpagoBITracer,
				 it.eng.spago.navigation.LightNavigationManager,
				 it.eng.spago.base.SourceBean,
				 java.util.TreeMap,
				 java.util.Collection,
				 java.util.ArrayList,
				 java.util.Date,
				 it.eng.spagobi.commons.utilities.GeneralUtilities,
				 it.eng.spagobi.commons.utilities.ChannelUtilities,
				 java.util.Map,
				 java.util.HashMap,
				 it.eng.spagobi.commons.bo.Subreport,
				 it.eng.spagobi.events.EventsManager,
				 it.eng.spagobi.events.bo.EventLog,
				 it.eng.spago.configuration.ConfigSingleton,
				 it.eng.spagobi.monitoring.metadata.SbiAudit,
				  it.eng.spagobi.monitoring.dao.IAuditDAO,
				 it.eng.spago.util.StringUtils,
				 it.eng.spagobi.analiticalmodel.document.dao.BIObjectDAOHibImpl,
				 it.eng.spago.security.IEngUserProfile" %>
<%@page import="it.eng.spagobi.engines.config.bo.Engine"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO"%>
<%@page import="it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.eng.spagobi.tools.datasource.bo.DataSource"%>
<%@page import="it.eng.spagobi.monitoring.dao.AuditManager"%>

<%
	// GET RESPONSE OBJECTS
	
    SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("MetadataBIObjectModule"); 
	BIObject obj = (BIObject) moduleResponse.getAttribute("biObj");
	Integer docId = obj.getId();
	String docLabel = obj.getLabel();
	String docName = obj.getName();
	Integer docTypeId = obj.getBiObjectTypeID();
	Domain d = DAOFactory.getDomainDAO().loadDomainById(docTypeId);
	String docType = d.getValueName();
	Engine engine = obj.getEngine();	
	String engineName = engine.getName();
	String driverName = engine.getDriverName();
	Integer dsID = obj.getDataSourceId();
	DataSource ds = DAOFactory.getDataSourceDAO().loadDataSourceByID(dsID);
	String dataSource = ds.getLabel();
	
	String docDescr = obj.getDescription();
	String docObjective = ""; 
	String docLongDescr = ""; 
	String docImage = ""; 
	String docRating = ""; 
	
	String docLanguage = ""; 
	String templateDimension = ""; 
	String creationDate = ""; 
	String creationUser = ""; 
	String lastModifDate = ""; 
	String lastModifUser = ""; 
	
	String lastAccesDate = "";
	String lastExecTime = "";
	String mediumExecTime = "";	
	String lastExecUser = "";
	
	ObjTemplate currTemplate = obj.getActiveTemplate();
	Integer prog = currTemplate.getProg();

    SbiAudit sba = AuditManager.getInstance().getLastExecution(docId);
    if (lastExecUser != null ) lastExecUser = sba.getUserName();
    Date lastExecDate = sba.getExecutionStartTime();
    if (lastExecDate != null )lastExecTime = lastExecDate.toString();


%>


<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'General Data',
        collapsible:true,
        collapsed : true,
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
		
		<!-- DOC IMAGE -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left"><spagobi:message key = "metadata.docImage" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docImage%>
			</td>
		</tr>	
		
		<!-- DOC RATING -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left"><spagobi:message key = "metadata.docRating" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docRating%>
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
		
		<!-- DOC NAME -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docName" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=docName%>
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
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=prog.toString()%>
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
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=driverName%>
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
		
		<!-- LAST ACCESS DATE -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left"><spagobi:message key = "metadata.docLastAccess" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=lastAccesDate%>
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
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=mediumExecTime%>
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
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp; 
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
	

<%@ include file="/jsp/commons/footer.jsp"%>
