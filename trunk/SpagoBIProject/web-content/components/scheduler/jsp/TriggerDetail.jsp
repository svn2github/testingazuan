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
<%@page import="it.eng.spagobi.scheduler.to.TriggerInfo"%>

<%  
   	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("TriggerManagementModule"); 
	TriggerInfo triggerInfo = (TriggerInfo)aSessionContainer.getAttribute(SpagoBIConstants.TRIGGER_INFO);
	JobInfo jobInfo = triggerInfo.getJobInfo();
	List jobBiobjects = jobInfo.getBiobjects();
	Map saveOptions = triggerInfo.getSaveOptions();
	
	PortletURL backUrl = renderResponse.createActionURL();
	backUrl.setParameter("LIGHT_NAVIGATOR_BACK_TO", "1");

	PortletURL formUrl = renderResponse.createActionURL();
	formUrl.setParameter("PAGE", "TriggerManagementPage");
	formUrl.setParameter("MESSAGEDET", SpagoBIConstants.MESSAGE_SAVE_SCHEDULE);
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");

%>



<%@page import="it.eng.spagobi.scheduler.to.SaveInfo"%>
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dojo/dojo.js" )%>"></script>

<script type="text/javascript">
       dojo.require("dojo.widget.DropdownDatePicker");
       dojo.require("dojo.widget.DropdownTimePicker");
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
	
	function saveCall() {
		document.getElementById('triggerdetailform').submit();
	}
	
</script>



<form id="triggerdetailform" method="post" action="<%=formUrl%>" >
	

	<table class='header-table-portlet-section'>
		<tr class='header-row-portlet-section'>
			<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key = "scheduler.scheduledetail"  bundle="component_scheduler_messages"/>		
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


	<div class="div_detail_area_forms_scheduler" >
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.schedname" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input id="triggername" value="<%=triggerInfo.getTriggerName()%>" type="text" name="triggername" size="35"/>
		    &nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.scheddescription" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" value="<%=triggerInfo.getTriggerDescription()%>" name="triggerdescription" size="35"/>
			&nbsp;
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.startdate" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
		 	<input type="text" value="<%=triggerInfo.getStartDate()%>" 
			       name="startdate" id="startdate"  
			       dojoType="dropdowndatepicker" widgetId="startDateWidget" />
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.starttime" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" value="<%=triggerInfo.getStartTime()%>" 
			       name="starttime" id="starttime"  
			       dojoType="dropdowntimepicker" widgetId="startTimeWidget" />
			&nbsp;*
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.enddate" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
		 	<input type="text" value="<%=triggerInfo.getEndDate()%>" 
			       name="enddate" id="enddate"  
			       dojoType="dropdowndatepicker" widgetId="endDateWidget" />
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.endtime" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" value="<%=triggerInfo.getEndTime()%>" 
			       name="endtime" id="endtime"  
			       dojoType="dropdowntimepicker" widgetId="endTimeWidget" />
		</div>
		<div class='div_detail_label_scheduler'>
			<span class='portlet-form-field-label'>
				<spagobi:message key="scheduler.repeatinterval" bundle="component_scheduler_messages" />
			</span>
		</div>
		<div class='div_detail_form'>
			<input type="text" value="<%=triggerInfo.getRepeatInterval()%>" name="repeatInterval" size="35"/>
			&nbsp;<span style="font-size:9pt;">ms<span>
		</div>
	</div>



	<br/>



	<spagobi:error/>


  
	<table>
		<tr> 
			<td class='titlebar_level_2_text_section' style='vertical-align:middle;padding-left:5px;'>
				<spagobi:message key="scheduler.saveoptions" bundle="component_scheduler_messages" />
			</td>
		</tr>
	</table>



	<div style='width:100%;visibility:visible;' class='UITabs'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:  100%">
	
	<%
		if(jobBiobjects.size()==0){
	%>
				<br/>
				<spagobi:message key = "scheduler.jobhasnodocument"  bundle="component_scheduler_messages"/>
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
		iterJobBiobjs = jobBiobjects.iterator();
	    index = 0;
	    String setTabOpened = "";
	    String displaytab = "inline";
		while(iterJobBiobjs.hasNext()) {
			BIObject biobj = (BIObject)iterJobBiobjs.next();
			if(index > 0) {
				displaytab = "none";
			} else {
				setTabOpened = "<script>tabOpened = '"+biobj.getId()+"';</script>";
			}
			index ++;
			
			
			Integer biobjid = biobj.getId();
			SaveInfo sInfo = (SaveInfo)saveOptions.get(biobjid);
	%>
	
	<%=setTabOpened%>
	
	<div width="100%" id="areabiobj<%=biobj.getId()%>" style="display:<%=displaytab%>;" >
		<br/>
		<div class="div_detail_area_forms_scheduler" >    	
        
        <input type="checkbox" name="saveassnapshot_<%=biobj.getId()%>" 
               <%if(sInfo.isSaveAsSnapshot()){out.write(" checked='checked' " );} %> />
			  <span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.saveassnap" bundle="component_scheduler_messages" />
				</span>
			  <div style="margin-left:50px;margin-top:10px;">
            <div class='div_detail_label_scheduler'>
				        <span class='portlet-form-field-label'>
					         <spagobi:message key="scheduler.storename" bundle="component_scheduler_messages" />
				        </span>
			      </div>
            <div class='div_detail_form'>
				        <input type="text" id="snaphotname" value="<%=sInfo.getSnapshotName()%>"
				               name="snapshotname_<%=biobj.getId()%>" size="35"/>
			      </div>
			      <div class='div_detail_label_scheduler'>
				        <span class='portlet-form-field-label'>
					         <spagobi:message key="scheduler.storedescr" bundle="component_scheduler_messages" />
				        </span>
			       </div>
			       <div class='div_detail_form'>
				        <input type="text" value="<%=sInfo.getSnapshotDescription()%>"
				               name="snapshotdescription_<%=biobj.getId()%>" size="35"/>
			       </div>
		      	 <div class='div_detail_label_scheduler'>
				        <span class='portlet-form-field-label'>
					         <spagobi:message key="scheduler.historylength" bundle="component_scheduler_messages" />
				        </span>
			       </div>
			       <div class='div_detail_form'>
				        <input type="text" name="snapshothistorylength_<%=biobj.getId()%>" 
				               value="<%=sInfo.getSnapshotHistoryLength()%>" size="35"/>
			       </div>		       
        </div>

				
        <br/>
		
				<input type="checkbox" name="saveasdocument_<%=biobj.getId()%>" 
				       <%if(sInfo.isSaveAsDocument()){out.write(" checked='checked' " );} %> />
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.saveasdoc" bundle="component_scheduler_messages" />
				</span>
				<div style="margin-left:50px;margin-top:10px;">
            <div class='div_detail_label_scheduler'>
				        <span class='portlet-form-field-label'>
					         <spagobi:message key="scheduler.storename" bundle="component_scheduler_messages" />
				        </span>
			      </div>
            <div class='div_detail_form'>
				        <input type="text" name="documentname_<%=biobj.getId()%>" 
				               value="<%=sInfo.getDocumentName()%>" size="35"/>
			      </div>
			      <div class='div_detail_label_scheduler'>
				        <span class='portlet-form-field-label'>
					         <spagobi:message key="scheduler.storedescr" bundle="component_scheduler_messages" />
				        </span>
			       </div>
			       <div class='div_detail_form'>
				        <input type="text" name="documentdescription_<%=biobj.getId()%>" 
				               value="<%=sInfo.getDocumentDescription()%>" size="35"/>
			       </div>   
			       
			       <spagobi:treeObjects moduleName="TriggerManagementModule"  
										htmlGeneratorClass="it.eng.spagobi.scheduler.gui.SelectFunctionalityTreeHtmlGenerator" 
										treeName="<%="tree_" + biobj.getId()%>" />
			       
        </div>
				
				
        <br/>

				<input type="checkbox" name="sendmail_<%=biobj.getId()%>" 
				       <%if(sInfo.isSendMail()){out.write(" checked='checked' " );} %>/>
				<span class='portlet-form-field-label'>
					<spagobi:message key="scheduler.sendmail" bundle="component_scheduler_messages" />
				</span>
				<div style="margin-left:50px;margin-top:10px;">
  				<div class='div_detail_label_scheduler'>
  		      <span class='portlet-form-field-label'>
  			       <spagobi:message key="scheduler.mailto" bundle="component_scheduler_messages" />
  		      </span>
  	      </div>
  	      <div class='div_detail_form'>
  		        <input type="text" name="mailtos_<%=biobj.getId()%>" 
  		               value="<%=sInfo.getMailTos()%>" size="35" />
  	      </div>
  	    </div>
  	    
  	    <br/>
  	    
		</div>
	</div>
	
	<%	
		}
	%>
	




</form>

