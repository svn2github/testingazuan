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
<%@page import="it.eng.spago.navigation.LightNavigationManager"%>
<%@page import="it.eng.spagobi.constants.SpagoBIConstants"%>
<%@page import="it.eng.spagobi.scheduler.to.JobInfo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="it.eng.spagobi.bo.BIObject"%>
<%@page import="it.eng.spagobi.bo.BIObjectParameter"%>
<%@page import="it.eng.spago.base.SessionContainer"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.bo.dao.DAOFactory"%>
<%@page import="it.eng.spagobi.bo.dao.IBIObjectDAO"%>
<%@page import="it.eng.spagobi.bo.dao.IParameterDAO"%>
<%@page import="it.eng.spagobi.bo.Role"%>
<%@page import="java.util.HashMap"%>

<%  
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("JobManagementModule"); 

	SessionContainer permSession = aSessionContainer.getPermanentContainer();
	IEngUserProfile userProfile = (IEngUserProfile) permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);

	JobInfo jobInfo = (JobInfo)aSessionContainer.getAttribute(SpagoBIConstants.JOB_INFO);   
	List jobBiobjects = jobInfo.getBiobjects();
	
	Map backUrlPars = new HashMap();
	backUrlPars.put("LIGHT_NAVIGATOR_BACK_TO", "1");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
	
	Map formUrlPars = new HashMap();
	formUrlPars.put("PAGE", "JobManagementPage");
	formUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
	String formUrl = urlBuilder.getUrl(request, formUrlPars);   
	   
	String splitter = ";";
%>


<!-- ********************** SCRIPT FOR DOCUMENT SELECTION WINDOW **************************** -->

<%@page import="it.eng.spagobi.utilities.ChannelUtilities"%>
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
			winDS = new Window('winDSId', {className: "alphacube", title: "", width:550, height:400, destroyOnClose: true});
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

<!-- ********************** SCRIPT FOR TABS **************************** -->
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
	
	    biobidstr = ''; 
		checkBiObjs = document.getElementsByName('biobject');
		for(i=0; i<checkBiObjs.length; i++) {
		    checkBiObj = checkBiObjs[i];
			if(checkBiObj.checked){
			    if(biobidstr!='') {
			    	biobidstr = biobidstr + ',';
			    }
				biobidstr = biobidstr + checkBiObj.value;
			}
		}
		$('selected_biobject_ids').value = biobidstr;
		
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


<!-- ********************** SCRIPT FOR PARAMETER LOOKUP **************************** -->
<script>

	var winLRL = null;
	var parfieldName = '';

	function getLovList(idObj, idPar, urlNamePar) {

		$('loadingdiv').style.display='inline';
		
		rolefield = $('role_par_'+idObj+'_'+urlNamePar);
		parfieldName = 'par_'+idObj+'_'+urlNamePar;
		role = rolefield.value;
		if(role==null) {
			role = rolefield.options[rolefield.selectedIndex].value;
		}
		url = "<%=ChannelUtilities.getSpagoBIContextName(request)%>/servlet/AdapterHTTP";
	    pars = "NEW_SESSION=TRUE&PAGE=LovLookupAjaxPage";
	    pars +="&roleName="+role;
	    pars += "&parameterId="+idPar;
	    pars += "&parameterFieldName="+parfieldName;
	    pars += "&<%=LightNavigationManager.LIGHT_NAVIGATOR_DISABLED%>=TRUE";
		new Ajax.Request(url,
       			{
           			method: 'post',
           			parameters: pars,
       				onSuccess: function(transport){
                           			response = transport.responseText || "";
                           			displayList(response);
                       			},
       				onFailure: showError
       			}
   		);
	}
		
	function displayList(html) {
		winLRL = new Window('winLRLId', {className: "alphacube", title: "", width:650, height:400, destroyOnClose: true});
      	winLRL.setDestroyOnClose();
      	winLRL.setHTMLContent(html);
      	winLRL.showCenter(true);
	}
		
	function showError()  {
		alert('Error while getting values');
	}
	
	    
	observerLRLshow = { 
		onShow: function(eventName, win) {
			if (win == winLRL) {
			    $('loadingdiv').style.display='none';
			    parfield = document.getElementById(parfieldName);
			    parfieldval = parfield.value;
			    parfieldvalues = parfieldval.split(';');
			    checks = document.getElementsByName('rowcheck');
			    for(i=0; i<checks.length; i++) {
			    	check = checks[i];
			    	for(j=0; j<parfieldvalues.length; j++) {
			    		value = parfieldvalues[j];
			    		if(check.value == value) {
			    			check.checked = true;
			    		}
			    	}
			    }
			}
		}
	}
		
	observerLRLclose = { 
		onClose: function(eventName, win) {
			if (win == winLRL) {
			    valstring = '';
			    parfield = document.getElementById(parfieldName);
			    checks = document.getElementsByName('rowcheck');
			    for(i=0; i<checks.length; i++) {
			    	check = checks[i];
			    	if(check.checked) {
			    		val = check.value;
			    		valstring = valstring + val + ';';
			    	}
			    }
			    if(valstring.length > 0) {
			    	valstring = valstring.substring(0, (valstring.length -1) );
			    }
			    if(valstring.length > 0) {
			    	parfield.value = valstring;
			    }
			}
		}
	}
	
	observerLRLdestroy = { 
		onDestroy: function(eventName, win) { 
			if (win == winLRL) { 
				winLRL = null; 
			} 
	 	} 
	}

	Windows.addObserver(observerLRLshow);
	Windows.addObserver(observerLRLclose);
	Windows.addObserver(observerLRLdestroy);

</script>




<!-- ********************** PAGE STYLES **************************** -->

<STYLE>
	
	.div_form_container {
    	border: 1px solid #cccccc;
    	background-color:#fafafa;
    	float: left;
	}
	
	.div_form_margin {
		margin: 5px;
		float: left;
	}
	
	.div_form_row {
		clear: both;
		padding-bottom:5px;
	}
	
	.div_form_label {	
		float: left;
		width:150px;
		margin-right:20px;
	}
	
	.div_form_field {
	}

    .div_form_message {	
		float: left;
		margin:20px;
	}
	
    .nowraptext {
    	white-space:nowrap;
    }
    
    .div_loading {
        width:20%;
    	position:absolute;
    	left:20%;
    	top:40%;
    	border:1px solid #bbbbbb;
    	background:#eeeeee;
    	padding-left:100px;padding-right:100px;
    	display:none;
    }
    
    
    
    
</STYLE>




<!-- *********************** START HTML CODE ****************************** -->


<div id='loadingdiv' class='div_loading' >
	<center>
		<br/><br/>
		<span class='portlet-form-field-label'>
			<spagobi:message key = "loading" />	
		</span>
		<br/><br/>
		<img src='<%= urlBuilder.getResourceLink(request, "/img/wapp/loading.gif")%>' />
	</center>
</div>

<form id="jobdetailform" method="post" action="<%=formUrl%>" >
	<input id="formmsg" type="hidden" name="MESSAGEDET" value="" />
	<input id="splitterparameter" type="hidden" name="splitter" value="<%=splitter%>" />
	<input id="selected_biobject_ids" type="hidden" name="selected_biobject_ids" value="" />

<!-- *********************** PAGE TITLE ****************************** -->

<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "scheduler.jobDetail"  bundle="component_scheduler_messages"/>		
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=backUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "scheduler.back" bundle="component_scheduler_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/scheduler/img/back.png")%>' 
      				 alt='<spagobi:message key = "scheduler.back"  bundle="component_scheduler_messages"/>' />
			</a>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='javascript:saveCall()'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "scheduler.save" bundle="component_scheduler_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/scheduler/img/save.png")%>' 
      				 alt='<spagobi:message key = "scheduler.save"  bundle="component_scheduler_messages"/>' />
			</a>
		</td>
	</tr>
</table>


<!-- *********************** FIRST FORM ****************************** -->

<br/>

<div class="div_form_container" >
	<div class="div_form_margin" >
		<div class="div_form_row" >
			<div class='div_form_label'>
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
			<div class='div_form_field'>
				<input class='portlet-form-input-field' type="text" name="jobname" 
			      	   size="50" value="<%=jobInfo.getJobName()%>"  <%=readonly%> >
			    &nbsp;*
			</div>
		</div>
		<div class="div_form_row" >
			<div class='div_form_label'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "scheduler.jobDescription"  bundle="component_scheduler_messages"/>
				</span>
			</div>
			<div class='div_form_field'>
				<input class='portlet-form-input-field' type="text" 
					   name="jobdescription" size="50" value="<%=jobInfo.getJobDescription()%>" >
			</div>
		</div>
	</div>
</div>

<div style='clear:left;'></div>


<br/>


<!-- *********************** ERROR TAG ****************************** -->

<spagobi:error/>




<!-- *********************** TABS ****************************** -->



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
	      				 src='<%= urlBuilder.getResourceLink(request, "/components/scheduler/img/plusminus.gif")%>' 
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
	
	
<!-- *********************** TAB FORMS ****************************** -->	
	
	<%
		IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
		IParameterDAO pardao = DAOFactory.getParameterDAO();
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
		
		<div class='div_form_container' >
		   <div class='div_form_margin'>
		   		<div class='div_form_row' >
				   <div class='div_form_message nowraptext'>
				       	<span class='portlet-form-field-label'>
		        	      <spagobi:message key = "scheduler.noparameter"  bundle="component_scheduler_messages"/>
		            	</span>
		         	</div>
		         </div>
			</div>
		</div>
		<br/>
		<%
      		} else {
      	%>
			<br/>
			<div class='div_form_container' >
				<div class='div_form_margin'>
			
				<%
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
					<div class='div_form_row' >
						<div class='div_form_label'>
							<span class='portlet-form-field-label'>
				            	<%=biobjpar.getLabel()%>
				          	</span>
				    	</div>
				    	
				    	<div class='div_form_field'>
						  	<input class='portlet-form-input-field' 
						  	       id="<%="par_"+biobj.getId()+"_"+biobjpar.getParameterUrlName()%>"
						  	       name="<%="par_"+biobj.getId()+"_"+biobjpar.getParameterUrlName()%>" 
						  	       type="text" value="<%=concatenatedValue%>" />
						  	&nbsp;&nbsp;&nbsp;
						  	<%
						  		List roles = biobjdao.getCorrectRolesForExecution(biobj.getId(), userProfile);
						  		if(roles.size()>0) {
						  	%>
						  	<a style='text-decoration:none;' href="javascript:getLovList('<%=biobj.getId()%>', '<%=biobjpar.getParID()%>', '<%=biobjpar.getParameterUrlName()%>')">
						  		<img title='<spagobi:message key = "scheduler.fillparameter"  bundle="component_scheduler_messages"/>' 
      				 				src='<%= urlBuilder.getResourceLink(request, "/img/detail.gif")%>' 
      				 				alt='<spagobi:message key = "scheduler.fillparameter"  bundle="component_scheduler_messages"/>' />
						  	</a>
						  	&nbsp;&nbsp;&nbsp;
							  	<%
							  		if(roles.size()==1) {
							    %>
						  		<input type='hidden' 
						  			   id='role_par_<%=biobj.getId()%>_<%=biobjpar.getParameterUrlName()%>' 
						  			   name='role_par_<%=biobj.getId()%>_<%=biobjpar.getParameterUrlName()%>'
						  			   value='<%=roles.get(0)%>' />
							  	<%
							  		} else {
								%>
								<span class='portlet-form-field-label'>
									<spagobi:message key = "scheduler.usingrole"  bundle="component_scheduler_messages"/> 
								</span>
								&nbsp;&nbsp;&nbsp;
								<select name='role_par_<%=biobj.getId()%>_<%=biobjpar.getParameterUrlName()%>'
										id='role_par_<%=biobj.getId()%>_<%=biobjpar.getParameterUrlName()%>' >
									<% 
										Iterator iterRoles = roles.iterator(); 
										while(iterRoles.hasNext()) {
											String role = (String)iterRoles.next();
									%>
									<option value='<%=role%>'><%=role%></option>
									<%
										}
									%>
								</select>
								<%
							  		}
						  		} // if(roles.size()>0)
								%>
						</div>
					</div>
		
		
		<%
				} // end while
		%>
				</div>
			</div>
			<br/>
		<%
			} // enf if
		%>
	</div>
	
	
	
	
	
	<%	
		}
	%>
	
</div>



<!-- *********************** DIV SELECT DOCUMENT (HIDDEN) ****************************** -->



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
		      				 src='<%= urlBuilder.getResourceLink(request, "/components/scheduler/img/save.png")%>' 
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