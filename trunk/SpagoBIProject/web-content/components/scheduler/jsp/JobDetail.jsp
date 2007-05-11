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

<%@ include file="/jsp/portlet_base.jsp"%>

<%@page import="it.eng.spago.base.SourceBean"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.scheduler.to.JobInfo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spagobi.bo.BIObjectParameter"%>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("JobManagementModule"); 
	   
	JobInfo jobInfo = (JobInfo)aSessionContainer.getAttribute(SpagoBIConstants.JOB_INFO);   
	List jobBiobjects = jobInfo.getBiobjects();
	
	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("LIGHT_NAVIGATOR_BACK_TO", "1");
	   
	PortletURL formUrl = renderResponse.createActionURL();
	formUrl.setParameter("PAGE", "JobManagementPage");
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	   
	String splitter = ";";
   
	String linkProto = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/prototype.js");
	String linkProtoWin = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/window.js");
	String linkProtoEff = urlBuilder.getResourceLink(request, "/js/prototype/javascripts/effects.js");
	String linkProtoDefThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/default.css");
	String linkProtoAlphaThem = urlBuilder.getResourceLink(request, "/js/prototype/themes/alphacube.css");
   
%>


<script type="text/javascript" src="<%=linkProto%>"></script>
<script type="text/javascript" src="<%=linkProtoWin%>"></script>
<script type="text/javascript" src="<%=linkProtoEff%>"></script>
<link href="<%=linkProtoDefThem%>" rel="stylesheet" type="text/css"/>
<link href="<%=linkProtoAlphaThem%>" rel="stylesheet" type="text/css"/> 



<script>
	var docselwinopen = false;
	var winDS = null;
	
	function opencloseDocumentSelectionWin() {
		if(!docselwinopen){
			docselwinopen = true;
			openDocumentSelectionWin();
		}
	}
	
	function openDocumentSelectionWin(){
		if(winDS==null) {
			winDS = new Window('winDSId', {className: "alphacube", title: "", width:550, height:450, destroyOnClose: true});
	      	winDS.setContent('selectiondocumentdiv', false, false);
	      	winDS.showCenter(true);
	    } else {
	      	winDS.showCenter(true);
	    }
	}
	
	observerWDS = { 
		onClose: function(eventName, win) {
			if (win == winDS) {
				docselwinopen = false;
			}
		}
	}
	
	observerWDS1 = { 
		onDestroy: function(eventName, win) { 
			if (win == winDS) { 
				$('selectiondocumentcontainerdiv').appendChild($('selectiondocumentdiv')); 
				$('selectiondocumentdiv').style.display='none';
				winDS = null; 
				Windows.removeObserver(this); 
			} 
		 } 
	}
	

	Windows.addObserver(observerWDS);
	Windows.addObserver(observerWDS1);
	
</script>


<script>
	tabOpened = ""; 
	
	function changeTab(biobjid) {
	  	if(tabOpened==biobjid) {
      		return;
    	}
		document.getElementById('areabiobj'+biobjid).style.display="inline";
		document.getElementById('areabiobj'+tabOpened).style.display="none";
		document.getElementById('tabbiobj'+biobjid).className="tab selected";
		document.getElementById('tabbiobj'+tabOpened).className="tab";
		tabOpened = biobjid;
	}
	
	function fillParamCall() {
		if(winDS!=null){
			winDS.destroy();
		}
		document.getElementById('formmsg').value='MESSAGE_DOCUMENTS_SELECTED';
		document.getElementById('jobdetailform').submit();
	}
	
	function saveCall() {
		document.getElementById('formmsg').value='MESSAGE_SAVE_JOB';
		document.getElementById('jobdetailform').submit();
	}
	
</script>


<form id="jobdetailform" method="post" action="<%=formUrl%>" >
	<input id="formmsg" type="hidden" name="MESSAGEDET" value="" />
	<input id="splitterparameter" type="hidden" name="splitter" value="<%=splitter%>" />


<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "scheduler.jobDetail"  bundle="component_scheduler_messages"/>		
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "scheduler.back" bundle="component_scheduler_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/back.png")%>' 
      				 alt='<spagobi:message key = "scheduler.back"  bundle="component_scheduler_messages"/>' />
			</a>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:saveCall()'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "scheduler.save" bundle="component_scheduler_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/save.png")%>' 
      				 alt='<spagobi:message key = "scheduler.save"  bundle="component_scheduler_messages"/>' />
			</a>
		</td>
	</tr>
</table>

<br/>

<div class="div_detail_area_forms" >
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "scheduler.jobName"  bundle="component_scheduler_messages"/>
		</span>
	</div>
	<%
		String readonly  = "";
		String jobName = jobInfo.getJobName();
		if(jobName!=null) {
			jobName = jobName.trim();
			if(!jobName.equals(""))
				readonly = " readonly ";
		}
	%>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" name="jobname" 
	      	   size="50" value="<%=jobInfo.getJobName()%>"  <%=readonly%> >
	    &nbsp;*
	</div>
	<div class='div_detail_label'>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "scheduler.jobDescription"  bundle="component_scheduler_messages"/>
		</span>
	</div>
	<div class='div_detail_form'>
		<input class='portlet-form-input-field' type="text" 
			   name="jobdescription" size="50" value="<%=jobInfo.getJobDescription()%>" >
	</div>
</div>

<br/>




<spagobi:error/>




<div>
	<table>
		<tr> 
			<td class='titlebar_level_2_text_section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "scheduler.documentparameters"  bundle="component_scheduler_messages"/>	
			</td>
			<td class='titlebar_level_2_empty_section'>&nbsp;</td>
			<td class='titlebar_level_2_button_section'>
				<a href='javascript:opencloseDocumentSelectionWin()'> 
	      			<img class='header-button-image-portlet-section' 
	      				 title='<spagobi:message key = "scheduler.addremovedocument" bundle="component_scheduler_messages" />' 
	      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/plusminus.gif")%>' 
	      				 alt='<spagobi:message key = "scheduler.addremovedocument"  bundle="component_scheduler_messages"/>' />
				</a>
			</td>
		</td>
	</table>
	
	
	<div style='width:100%;visibility:visible;' class='UITabs'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:  100%">
	
	<%
		if(jobBiobjects.size()==0){
	%>
				<br/>
				<spagobi:message key = "scheduler.nodocumentSelected"  bundle="component_scheduler_messages"/>
				<br/>
	<%			
		} else {
			Iterator iterJobBiobjs = jobBiobjects.iterator();
	   	 	int index = 0;
	    	String tabClass = "tab selected"; 
			while(iterJobBiobjs.hasNext()) {
				BIObject biobj = (BIObject)iterJobBiobjs.next();
				String biobjName = biobj.getName();
				if(index > 0) {
					tabClass = "tab"; 
				}
				index ++;
	%>
				<div id="tabbiobj<%=biobj.getId()%>"  class='<%= tabClass%>'>
					<a href="javascript:changeTab('<%=biobj.getId()%>')" style="color:black;"> 
							<%=biobjName%>
					</a>
				</div>
	<%	
			}
		}
	%>
			</div>
		</div>
	</div>
	
	
	
	
	
	
	<%
		Iterator iterJobBiobjs = jobBiobjects.iterator();
	    int index = 0;
	    String setTabOpened = "";
	    String displaytab = "inline";
		while(iterJobBiobjs.hasNext()) {
			BIObject biobj = (BIObject)iterJobBiobjs.next();
			List pars = biobj.getBiObjectParameters();
			if(index > 0) {
				displaytab = "none";
			} else {
				setTabOpened = "<script>tabOpened = '"+biobj.getId()+"';</script>";
			}
			index ++;
	%>
	
	<%=setTabOpened%>
	<div width="100%" id="areabiobj<%=biobj.getId()%>" style="display:<%=displaytab%>;" >
	
		<%
			if( (pars==null) || (pars.size()==0) ) {
		%>		
			
      <br/>
			<div class='div_detail_area_forms' >
			   <br/>
			   <div class='div_detail_label' style="width:400px;">
		        <span class='portlet-form-field-label'>
              <spagobi:message key = "scheduler.noparameter"  bundle="component_scheduler_messages"/>
            </span>
         </div>
         <div>&nbsp;</div>
         <br/>
			</div>
			<br/>
			
		<%
      } else {
			  out.write("<br/>");
			  out.write("<div class='div_detail_area_forms' >");
				Iterator iterPars = pars.iterator();
				while(iterPars.hasNext()) {
					BIObjectParameter biobjpar = (BIObjectParameter)iterPars.next();
					String concatenatedValue = "";
					List values = biobjpar.getParameterValues();
					if(values!=null) {
						Iterator itervalues = values.iterator();
						while(itervalues.hasNext()) {
							String value = (String)itervalues.next();
							concatenatedValue += value + splitter;
						}
						if(concatenatedValue.length()>0) {
							concatenatedValue = concatenatedValue.substring(0, concatenatedValue.length() - 1);
						}
					}		
		%>
		
		<div class='div_detail_label'>
		      <span class='portlet-form-field-label'>
            <%=biobjpar.getLabel()%>
          </span>
    </div>
    <div class='div_detail_form'>
		  <input class='portlet-form-input-field' name="<%="par_"+biobj.getId()+"_"+biobjpar.getParameterUrlName()%>" type="text" value="<%=concatenatedValue%>" />
		</div>

		<%
				}
				out.write("</div>");
				out.write("<br/>");
			}
		%>
	</div>
	
	<%	
		}
	%>
	
</div>












<div id="selectiondocumentcontainerdiv">
	<div id="selectiondocumentdiv" style="display:none;width:97%;" >
		<table>
			<tr> 
				<td class='titlebar_level_2_text_section' style='vertical-align:middle;padding-left:5px;'>
					<spagobi:message key = "scheduler.documentselection"  bundle="component_scheduler_messages"/>		
				</td>
				<td class='titlebar_level_2_empty_section'>&nbsp;</td>
				<td class='titlebar_level_2_button_section'>
					<a href='javascript:fillParamCall()'> 
		      			<img class='header-button-image-portlet-section' 
		      				 title='<spagobi:message key = "scheduler.save" bundle="component_scheduler_messages" />' 
		      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/scheduler/img/save.png")%>' 
		      				 alt='<spagobi:message key = "scheduler.save"  bundle="component_scheduler_messages"/>' />
					</a>
				</td>
			</td>
		</table>
		<spagobi:treeObjects moduleName="JobManagementModule"  
				htmlGeneratorClass="it.eng.spagobi.scheduler.gui.SchedulerTreeHtmlGenerator" />
		<br/>
	</div>
</div>



</form>
