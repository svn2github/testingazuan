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
<%@page import="it.eng.spagobi.kpi.threshold.bo.Threshold"%>
<%@page import="it.eng.spagobi.kpi.threshold.bo.ThresholdValue"%>
<%@page import="java.awt.Color"%>
 

<%
	// GET RESPONSE OBJECTS
	
	String kpiBeginDate = (String)aServiceResponse.getAttribute("KPI_BEGIN_DATE");
	String kpiEndDate = (String)aServiceResponse.getAttribute("KPI_END_DATE");
	if (kpiEndDate.contains("9999")) kpiEndDate = "";
	String kpiTarget = (String)aServiceResponse.getAttribute("KPI_TARGET");
	String kpiValueDescr = (String)aServiceResponse.getAttribute("KPI_VALUE_DESCR");	
	String kpiCode = (String)aServiceResponse.getAttribute("KPI_CODE");
	String kpiDescription = (String)aServiceResponse.getAttribute("KPI_DESCRIPTION");
	String kpiName = (String)aServiceResponse.getAttribute("KPI_NAME");
	String modelInstName = (String)aServiceResponse.getAttribute("MODEL_INST_NAME");
	String modelInstanceDescr = (String)aServiceResponse.getAttribute("MODEL_INST_DESCR");
	String kpiInterpretation = (String)aServiceResponse.getAttribute("KPI_INTERPRETATION");

%>


<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'KPI Overview',
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
        title: 'KPI Description',
        collapsible:true,
        collapsed : true,
        renderTo: 'container2',
        contentEl : 'description'
    });
});
</script>
<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Model Instance',
        collapsible:true,
        collapsed : true,
        renderTo: 'container3',
        contentEl : 'modelInstance'
    });
});
</script>
<script>
Ext.onReady(function(){
    var p = new Ext.Panel({
        title: 'Technical Data',
        collapsible:true,
        collapsed : true,
        renderTo: 'container4',
        contentEl : 'technicalData'
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
		    <td class="portlet-section-header" width="140" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiName" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiName%>
			</td>
		</tr>	
		
		<!-- DOC DESCR -->
		<tr>
		    <td class="portlet-section-header" width="140" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiCode" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiCode%>
			</td>
		</tr>
		
		<spagobi:error/>
	</table> 
</div>		
<div id="description" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">
	
	<!-- TABLE GENERAL DATA -->		
	
	<table style="width:100%;margin-top:1px" >
		<!-- DOC NAME -->
		<tr>			
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiInterpretation%>
			</td>
		</tr>	
		<spagobi:error/>
	</table> 
</div>	

<div id="modelInstance" class="div_background_no_img" style="padding-top:5px;padding-left:5px;">
	
	<!-- TABLE GENERAL DATA -->		
	
	<table style="width:100%;margin-top:1px" >
		<!-- DOC NAME -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.modelInstanceName" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=modelInstName%>
			</td>
		</tr>	
		
		<!-- DOC DESCR -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.modelInstanceDescr" />		
			</td>				
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=modelInstanceDescr%>
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
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiBegDate" />		
			</td>				
			<td colspan="2" class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiBeginDate%>
			</td>
		</tr>	
		
		<!-- DOC LABEL -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiEndDate" />		
			</td>				
			<td  colspan="2" class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiEndDate%>
			</td>
		</tr>
		
		<!-- DOC TYPE -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiValueDesc" />		
			</td>				
			<td colspan="2" class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiValueDescr%>
			</td>
		</tr>
		
		<!-- ENGINE -->
		<tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><spagobi:message key = "sbi.kpi.kpiValueTarget" />		
			</td>				
			<td colspan="2" class="portlet-section-body" style="vertical-align:left;text-align:left;">&nbsp;<%=kpiTarget%>
			</td>
		</tr>	
		
		<% 
		List thresholdValues = (List)aServiceResponse.getAttribute("KPI_THRESHOLDS");
		if (thresholdValues!=null && !thresholdValues.isEmpty()){
			Iterator it = thresholdValues.iterator();
			
			while(it.hasNext()){
				ThresholdValue t =(ThresholdValue)it.next();
				
				Color c =t.getColour();
				String color = "rgb("+c.getRed()+", "+c.getGreen()+", "+c.getBlue()+")" ;
				Double min = t.getMinValue();
				Double max = t.getMaxValue();
				String minMax = "";
				if (min!=null && max !=null){
				  minMax = min.toString()+"-"+max.toString();
				}else if (min!=null && max==null){
					minMax = min.toString();
				}else if (min==null && max!=null){
					minMax = max.toString();
				}
				String type = t.getThresholdType();
				String label = "Threshold " + type+ " "+ t.getLabel();
				%>
		<!-- LANGUAGE -->
		 <tr>
		    <td class="portlet-section-header" width="170" style="text-align:left;color:black;background-color:#DCDCDC;"><%=label%>	
			</td>				
			<td class="portlet-section-body" width="15" style="vertical-align:left;text-align:left;background-color:<%=color%>;">
			</td>
			<td class="portlet-section-body" style="vertical-align:left;text-align:left;" >&nbsp;<%=minMax%>
			</td>
		 </tr>
				<%
			}
		} 
	  %>
		
																										
	
	<spagobi:error/>
	</table> 
</div>  

	
	

<%@ include file="/WEB-INF/jsp/commons/footer.jsp"%>
