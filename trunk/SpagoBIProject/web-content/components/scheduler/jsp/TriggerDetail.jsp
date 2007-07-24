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
<%@page import="it.eng.spagobi.scheduler.to.SaveInfo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>

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



<!-- ********************** SCRIPT FOR DOJO **************************** -->

<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/js/dojo/dojo.js" )%>"></script>

<script type="text/javascript">
       dojo.require("dojo.widget.DropdownDatePicker");
       dojo.require("dojo.widget.DropdownTimePicker");
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
	
</script>


<!-- ********************** SCRIPT FOR SAVE **************************** -->

<script>

	function saveCall() {
		chronStr = getRepetitionString();	
		$('chronstring').value=chronStr;
		document.getElementById('triggerdetailform').submit();
	}

    function getRepetitionString() {
        repStr = '';
    	if($('single_repetitionKind').checked) {
    		repStr = repStr + 'single{}';
    	}
    	if($('minute_repetitionKind').checked) {
    		repStr = repStr + 'minute{';
    		rep_n = $('minute_repetition_n').options[$('minute_repetition_n').selectedIndex].value;
    		repStr = repStr + 'numRepetition='+rep_n;
    		repStr = repStr + '}';
    	}
    	if($('hour_repetitionKind').checked) {
    		repStr = repStr + 'hour{';
    		rep_n = $('hour_repetition_n').options[$('hour_repetition_n').selectedIndex].value;
    		repStr = repStr + 'numRepetition='+rep_n;
    		repStr = repStr + '}';
    	}
    	if($('day_repetitionKind').checked) {
    		repStr = repStr + 'day{';
    		rep_n = $('day_repetition_n').options[$('day_repetition_n').selectedIndex].value;
    		repStr = repStr + 'numRepetition='+rep_n;
    		repStr = repStr + '}';
    	}
    	if($('week_repetitionKind').checked) {
    		repStr = repStr + 'week{';
    		rep_n = $('week_repetition_n').options[$('week_repetition_n').selectedIndex].value;
    		repStr = repStr + 'numRepetition='+rep_n+';days=';
    		
    		if($('day_in_week_rep_sun').checked) {
    			repStr = repStr + 'SUN,';
    		}
    		if($('day_in_week_rep_mon').checked) {
    			repStr = repStr + 'MON,';
    		}
    		if($('day_in_week_rep_tue').checked) {
    			repStr = repStr + 'TUE,';
    		}
    		if($('day_in_week_rep_wed').checked) {
    			repStr = repStr + 'WED,';
    		}
    		if($('day_in_week_rep_thu').checked) {
    			repStr = repStr + 'THU,';
    		}
    		if($('day_in_week_rep_fri').checked) {
    			repStr = repStr + 'FRI,';
    		}
    		if($('day_in_week_rep_sat').checked) {
    			repStr = repStr + 'SAT,';
    		}
    		repStr = repStr + '}';
    	}
    	if($('month_repetitionKind').checked) {
    		repStr = repStr + 'month{';
    		if($('month_selection_interval').checked) {
    			rep_n = $('monthrep_n').options[$('monthrep_n').selectedIndex].value;
    			repStr = repStr + 'numRepetition='+rep_n+';';
    			repStr = repStr + 'months=NONE;';
    		}
    		if($('month_selection_checks').checked) {
    			repStr = repStr + 'numRepetition=0;';
    			repStr = repStr + 'months=';
    			if($('monthrep_jan').checked) repStr = repStr + 'JAN,';
    			if($('monthrep_feb').checked) repStr = repStr + 'FEB,';
    			if($('monthrep_mar').checked) repStr = repStr + 'MAR,';
    			if($('monthrep_apr').checked) repStr = repStr + 'APR,';
    			if($('monthrep_may').checked) repStr = repStr + 'MAY,';
    			if($('monthrep_jun').checked) repStr = repStr + 'JUN,';
    			if($('monthrep_jul').checked) repStr = repStr + 'JUL,';
    			if($('monthrep_aug').checked) repStr = repStr + 'AUG,';
    			if($('monthrep_sep').checked) repStr = repStr + 'SEP,';
    			if($('monthrep_oct').checked) repStr = repStr + 'OCT,';
    			if($('monthrep_nov').checked) repStr = repStr + 'NOV,';
    			if($('monthrep_dic').checked) repStr = repStr + 'DIC,';	
    			repStr = repStr + ';';
    		}
    		if($('dayinmonth_selection_interval').checked) {
    			rep_n = $('dayinmonthrep_n').options[$('dayinmonthrep_n').selectedIndex].value;
    			repStr = repStr + 'dayRepetition='+rep_n+';';
    			repStr = repStr + 'weeks=NONE;';
    			repStr = repStr + 'days=NONE;';
    		}
    		if($('dayinmonth_selection_checks').checked) {
    			repStr = repStr + 'dayRepetition=0;';
    			repStr = repStr + 'weeks=';
    			weekstr = '';
    			if($('dayinmonthrep_week1').checked) weekstr = weekstr + '1';
    			if($('dayinmonthrep_week2').checked) weekstr = weekstr + '2';
    			if($('dayinmonthrep_week3').checked) weekstr = weekstr + '3';
    			if($('dayinmonthrep_week4').checked) weekstr = weekstr + '4';
    			if($('dayinmonthrep_weekL').checked) weekstr = weekstr + 'L';
    			if(weekstr=='') weekstr='NONE';
    			repStr = repStr + weekstr + ';';
    			repStr = repStr + 'days=';
    			daystr = '';
    			if($('dayinmonthrep_sun').checked) daystr = daystr + 'SUN,';
    			if($('dayinmonthrep_mon').checked) daystr = daystr + 'MON,';
    			if($('dayinmonthrep_tue').checked) daystr = daystr + 'TUE,';
    			if($('dayinmonthrep_wed').checked) daystr = daystr + 'WED,';
    			if($('dayinmonthrep_thu').checked) daystr = daystr + 'THU,';
    			if($('dayinmonthrep_fri').checked) daystr = daystr + 'FRI,';
    			if($('dayinmonthrep_sat').checked) daystr = daystr + 'SAT,';
    			if(daystr=='') daystr='NONE';
    			repStr = repStr + daystr + ';';
    		}
    		repStr = repStr + '}';
    	}
    	return repStr;
    }
    
    
    
    
    function selectOption(selobj, val) {
    	opts = selobj.options;
    	indsel = 0;
    	for(i=0; i<opts.length; i++) {
    		opt = opts[i];
    		if(opt.value == val) {
    			indsel= i;
    		}
    	}
    	selobj.selectedIndex=indsel;
    }
    
    
    
    
    
    function fillFormFromRepetitionString(repStr) {

    	type = '';
    	params = '';
    	if(repStr.indexOf('{')!=-1) {
    		indFirstBra = repStr.indexOf('{');
    		type = repStr.substring(0, indFirstBra);
    		params = repStr.substring((indFirstBra+1), (repStr.length-1));
    	} else {
    		return;
    	}
    	if(type=='single') {
    		$('single_repetitionKind').checked=true;
    	}
    	if(type=='minute') {
    		$('minute_repetitionKind').checked=true;
    		indeq = params.indexOf('=');
    		numrep = params.substring(indeq+1);
    		selectOption($('minute_repetition_n'), numrep);
    		openrepetitionform('minute');
    	}
    	if(type=='hour') {
    		$('hour_repetitionKind').checked=true;
    		indeq = params.indexOf('=');
    		numrep = params.substring(indeq+1);
    		selectOption($('hour_repetition_n'), numrep);
    		openrepetitionform('hour');
    	}
    	if(type=='day') {
    		$('day_repetitionKind').checked=true;
    		indeq = params.indexOf('=');
    		numrep = params.substring(indeq+1);
    		selectOption($('day_repetition_n'), numrep);
    		openrepetitionform('day');
    	}
    	if(type=='week') {
    		$('week_repetitionKind').checked=true;
    		indeq = params.indexOf('=');
    		indsplit = params.indexOf(';');
    		ind2eq = params.indexOf('=', (indeq + 1));
    		numrep = params.substring((indeq+1), indsplit);
    		daysstr = params.substring(ind2eq+1);
    		openrepetitionform('week');
    		selectOption($('week_repetition_n'), numrep);
    		days = daysstr.split(',');
    		for(j=0; j<days.length; j++) {
    			day = days[j];
    			if((day!=null) && (day!='')) {
    				if(day=='SUN') $('day_in_week_rep_sun').checked = 'true'; 
    				if(day=='MON') $('day_in_week_rep_mon').checked = 'true'; 
    				if(day=='TUE') $('day_in_week_rep_tue').checked = 'true'; 
    				if(day=='WED') $('day_in_week_rep_wed').checked = 'true'; 
    				if(day=='THU') $('day_in_week_rep_thu').checked = 'true'; 
    				if(day=='FRI') $('day_in_week_rep_fri').checked = 'true'; 
    				if(day=='SAT') $('day_in_week_rep_sat').checked = 'true';
    			}
    		}
    	}
    	if(type=='month') {
    		$('month_repetitionKind').checked=true;
    		openrepetitionform('month');
    		parchuncks = params.split(';');
	    	for(ind=0; ind<parchuncks.length; ind++) {
	    		parchunk = parchuncks[ind];
	    		singleparchunks = parchunk.split('=');
	    		key = singleparchunks[0];
	    		value = singleparchunks[1];
	    		if(key=='dayRepetition') {
	    			if(value!='0') {
	    				$('month_selection_interval').checked = true;
	    				selectOption($('monthrep_n'), value);
	    			} 
	    		}
	    		if(key=='months'){
	    			if(value!='NONE') {
	    				$('month_selection_checks').checked = true;
	    				months = value.split(',');
	    				for(j=0; j<months.length; j++) {
    						month = months[j];
    						if((month!=null) && (month!='')) {
			    				if(month=='JAN') $('monthrep_jan').checked = 'true';
			    				if(month=='FEB') $('monthrep_feb').checked = 'true';
			    				if(month=='MAR') $('monthrep_mar').checked = 'true';
			    				if(month=='APR') $('monthrep_apr').checked = 'true';
			    				if(month=='MAY') $('monthrep_may').checked = 'true';
			    				if(month=='JUN') $('monthrep_jun').checked = 'true';
			    				if(month=='JUL') $('monthrep_jul').checked = 'true';
			    				if(month=='AUG') $('monthrep_aug').checked = 'true';
			    				if(month=='SEP') $('monthrep_sep').checked = 'true';
			    				if(month=='OCT') $('monthrep_oct').checked = 'true';
			    				if(month=='NOV') $('monthrep_nov').checked = 'true';
			    				if(month=='DIC') $('monthrep_dic').checked = 'true';
    						}
    					}
	    			}
	    		}
	    		if(key=='dayRepetition') {
	    			if(value!='0') {
	    				$('dayinmonth_selection_interval').checked = true;
	    				selectOption($('dayinmonthrep_n'), value);
	    			} else {
	    				$('dayinmonth_selection_checks').checked = true;
	    			}
	    		}
	    		if(key=='weeks'){
	    			if(value!='NONE') {
	    				$('dayinmonth_selection_checks').checked = true;
	    				if(value=='1') $('dayinmonthrep_week1').checked = 'true';
			    		if(value=='2') $('dayinmonthrep_week2').checked = 'true';
			    		if(value=='3') $('dayinmonthrep_week3').checked = 'true';
			    		if(value=='4') $('dayinmonthrep_week4').checked = 'true';
			    		if(value=='L') $('dayinmonthrep_weekL').checked = 'true';
			    	}
			    }
			    if(key=='days'){
	    			if(value!='NONE') {
	    				$('dayinmonth_selection_checks').checked = true;
	    				days = value.split(',');
	    				for(j=0; j<days.length; j++) {
    						day = days[j];
    						if((day!=null) && (day!='')) {
			    				if(day=='SUN') $('dayinmonthrep_sun').checked = 'true';
			    				if(day=='MON') $('dayinmonthrep_mon').checked = 'true';
			    				if(day=='TUE') $('dayinmonthrep_tue').checked = 'true';
			    				if(day=='WED') $('dayinmonthrep_wed').checked = 'true';
			    				if(day=='THU') $('dayinmonthrep_thu').checked = 'true';
			    				if(day=='FRI') $('dayinmonthrep_fri').checked = 'true';
			    				if(day=='SAT') $('dayinmonthrep_sat').checked = 'true';
    						}
    					}
	    			}
	    		}
    		}
    	}
    }

</script>


<!-- ********************** SCRIPT FOR REPETITION FORMS **************************** -->

<script>

	function openrepetitionform(namerepetition) {
		$('minute_repetitionDiv_lbl').style.display='none';
		$('minute_repetitionDiv_form').style.display='none';
		$('hour_repetitionDiv_lbl').style.display='none';
		$('hour_repetitionDiv_form').style.display='none';
		$('day_repetitionDiv_lbl').style.display='none';
		$('day_repetitionDiv_form').style.display='none';
		$('week_repetitionDiv_lbl').style.display='none';
		$('week_repetitionDiv_form').style.display='none';
		$('month_repetitionDiv_form').style.display='none';
		$('month_repetitionDiv_lbl').style.display='none';
		
		divlbl = document.getElementById(namerepetition+'_repetitionDiv_lbl');
		if(divlbl!=null) {
			divlbl.style.display='inline';
		}
		divform = document.getElementById(namerepetition+'_repetitionDiv_form');
		if(divform!=null) {
			divform.style.display='inline';
		}
	}

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
	
	.div_form_label_selector {	
		clear: left;
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

	<input type='hidden' value='' id='chronstring' name='chronstring' />


	<div class="div_form_container" >
		<div class="div_form_margin" >
			<div class="div_form_row" >
				<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.schedname" bundle="component_scheduler_messages" />
					</span>
				</div>
				<%
				String readonly  = "";
				String trigName = triggerInfo.getTriggerName();
				if(trigName!=null) {
					trigName = trigName.trim();
					if(!trigName.equals("")) {
						readonly = " readonly ";
					} else {
						Calendar cal = new GregorianCalendar();
					    int hour24 = cal.get(Calendar.HOUR_OF_DAY);     
					    int min = cal.get(Calendar.MINUTE);             
					    int sec = cal.get(Calendar.SECOND);             
						trigName = jobInfo.getJobName() + "_schedule_" + hour24 + "" + min + "" + sec; 
					}
				}
				%>
				<div class='div_form_field'>
					<input id="triggername" value="<%=trigName%>" type="text" name="triggername" size="35" <%=readonly%> />
				    &nbsp;*
				</div>
			</div>
			<div class="div_form_row" >	
				<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.scheddescription" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
					<input type="text" value="<%=triggerInfo.getTriggerDescription()%>" name="triggerdescription" size="35"/>
					&nbsp;
				</div>
		     </div>
		     <div class="div_form_row" >
			 	<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.startdate" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
					<input type="text" value="<%=triggerInfo.getStartDateRFC3339()%>" 
			    		   name="startdate" id="startdate"  
			       		   dojoType="dropdowndatepicker" widgetId="startDateWidget" />
						   &nbsp;*
				</div>	
			</div>
		    <div class="div_form_row" >	
		     	<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.starttime" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
					<input type="text" value="<%=triggerInfo.getStartTime()%>" 
					       name="starttime" id="starttime"  
					       dojoType="dropdowntimepicker" widgetId="startTimeWidget" />
					&nbsp;*
				</div>
			</div>
			<div class="div_form_row" >	
				<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.enddate" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
				 	<input type="text" value="<%=triggerInfo.getEndDateRFC3339()%>" 
					       name="enddate" id="enddate"  
					       dojoType="dropdowndatepicker" widgetId="endDateWidget" />
				</div>
			</div>
			<div class="div_form_row" >	
				<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.endtime" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
					<input type="text" value="<%=triggerInfo.getEndTime()%>" 
					       name="endtime" id="endtime"  
					       dojoType="dropdowntimepicker" widgetId="endTimeWidget" />
				</div>
			</div>
			
		

			<%
			String repInterv = triggerInfo.getRepeatInterval();
			if(repInterv!=null) {
			   	if(repInterv.trim().equals("0")) {
			   		repInterv = "";
			   	}
			} else {
			   	repInterv = "";
			}
			%>
			<input type="hidden" value="<%=repInterv%>" name="repeatInterval" />
			<%--

			<div class="div_form_row" >
				<div class='div_form_label'>
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.repeatinterval" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div class='div_form_field'>
				    <%
				    	String repInterv = triggerInfo.getRepeatInterval();
				        if(repInterv!=null) {
				        	if(repInterv.trim().equals("0")) {
				        		repInterv = "";
				        	}
				        } else {
				        	repInterv = "";
				        }
				    %>
					<input type="text" value="<%=repInterv%>" name="repeatInterval" size="35"/>
					&nbsp;<span style="font-size:9pt;">ms<span>
				</div>
			</div>
			--%>
			
			
			
			<!-- ******* single execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='single_repetitionKind' name='repetitionKind' value='single' 
		     		       type="radio" onclick="openrepetitionform('single')" checked='checked' />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.singleExec" bundle="component_scheduler_messages" />
					</span>
				</div>
			</div>
			
			
			
			<!-- ******* per minute execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='minute_repetitionKind' name='repetitionKind' value='minute' 
		     		       type="radio" onclick="openrepetitionform('minute')" />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.minuteExec" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div id='minute_repetitionDiv_lbl' style='display:none;' class='div_form_label'>
					&nbsp;
				</div>
				<div id='minute_repetitionDiv_form' style='display:none;' class="div_form_container" >
					<div class="div_form_margin" >
						<div class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.everyNMinutes" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='minute_repetition_n' id='minute_repetition_n' >
								<%
								for(int i=1; i<=60; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
					</div>
				</div>
				<div style='clear:left;'></div>
			</div>
			
			
			
			<!-- ******* per hour execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='hour_repetitionKind' name='repetitionKind' value='hour' 
		     		       type="radio" onclick="openrepetitionform('hour')" />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.hourExec" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div id='hour_repetitionDiv_lbl' style='display:none;' class='div_form_label'>
					&nbsp;
				</div>
				<div id='hour_repetitionDiv_form'  style='display:none;' class="div_form_container" >
					<div class="div_form_margin" >
						<div class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.everyNHours" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='hour_repetition_n' id='hour_repetition_n' >
								<%
								for(int i=1; i<=24; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
					</div>
				</div>
				<div style='clear:left;'></div>
			</div>
			
			
			
			<!-- ******* per day execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='day_repetitionKind' name='repetitionKind' value='day' 
		     		       type="radio" onclick="openrepetitionform('day')" />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.dayExec" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div id='day_repetitionDiv_lbl' style='display:none;' class='div_form_label'>
					&nbsp;
				</div>
				<div id='day_repetitionDiv_form' style='display:none;' class="div_form_container" >
					<div class="div_form_margin" >
						<div class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.everyNDays" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='day_repetition_n' id='day_repetition_n' >
								<%
								for(int i=1; i<=31; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
					</div>
				</div>
				<div style='clear:left;'></div>
			</div>
			
			
			<!-- ******* per week execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='week_repetitionKind' name='repetitionKind' value='week' 
		     		       type="radio" onclick="openrepetitionform('week')" />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.weekExec" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div id='week_repetitionDiv_lbl' class='div_form_label' style='display:none;'>
					&nbsp;
				</div>
				<div id='week_repetitionDiv_form' style='display:none;' class="div_form_container" >
					<div class="div_form_margin" >
						<!-- the following form row is hidden because quartz is not able to manage n week   -->
						<div style='display:none;' class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.everyNWeeks" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='week_repetition_n' id='week_repetition_n' >
								<%
								for(int i=1; i<=52; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
						<div class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.inDays" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<input id='day_in_week_rep_sun' type='checkbox' value='SUN'><spagobi:message key="scheduler.sun" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_mon' type='checkbox' value='MON'><spagobi:message key="scheduler.mon" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_tue' type='checkbox' value='TUE'><spagobi:message key="scheduler.tue" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_wed' type='checkbox' value='WED'><spagobi:message key="scheduler.wed" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_thu' type='checkbox' value='THU'><spagobi:message key="scheduler.thu" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_fri' type='checkbox' value='FRI'><spagobi:message key="scheduler.fri" bundle="component_scheduler_messages" />
								<input id='day_in_week_rep_sat' type='checkbox' value='SAT'><spagobi:message key="scheduler.sat" bundle="component_scheduler_messages" />
							</div>	
						</div>
					</div>
				</div>
				<div style='clear:left;'></div>
			</div>
			
			
			
			<!-- ******* per month execution **************** -->
			<div class="div_form_row" >	
		     	<div class='div_form_label_selector'>
		     		<input id='month_repetitionKind' name='repetitionKind' value='month' 
		     		       type="radio" onclick="openrepetitionform('month')" />
					<span class='portlet-form-field-label'>
						<spagobi:message key="scheduler.monthExec" bundle="component_scheduler_messages" />
					</span>
				</div>
				<div id='month_repetitionDiv_lbl' class='div_form_label' style='display:none;'>
					&nbsp;
				</div>
				<div  id='month_repetitionDiv_form' style='display:none;' class="div_form_container" >
					<div class="div_form_margin" >
						<div class="div_form_row" >
							<div class='div_form_label'>
								<input id='month_selection_interval' name='month_selection' type='radio' checked='true'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.everyNMonth" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='monthrep_n' id='monthrep_n' >
								<%
								for(int i=1; i<=12; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
						<div class="div_form_row" >
							<div class='div_form_label'>
								<input id='month_selection_checks' name='month_selection' type='radio'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.inMonths" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<input name='monthrep_jan' id='monthrep_jan' type='checkbox' value='JAN'><spagobi:message key="scheduler.jan" bundle="component_scheduler_messages" />
								<input name='monthrep_feb' id='monthrep_feb' type='checkbox' value='FEB'><spagobi:message key="scheduler.feb" bundle="component_scheduler_messages" />
								<input name='monthrep_mar' id='monthrep_mar' type='checkbox' value='MAR'><spagobi:message key="scheduler.mar" bundle="component_scheduler_messages" />
								<input name='monthrep_apr' id='monthrep_apr' type='checkbox' value='APR'><spagobi:message key="scheduler.apr" bundle="component_scheduler_messages" />
								<input name='monthrep_may' id='monthrep_may' type='checkbox' value='MAY'><spagobi:message key="scheduler.may" bundle="component_scheduler_messages" />
								<input name='monthrep_jun' id='monthrep_jun' type='checkbox' value='JUN'><spagobi:message key="scheduler.jun" bundle="component_scheduler_messages" />
								<input name='monthrep_jul' id='monthrep_jul' type='checkbox' value='JUL'><spagobi:message key="scheduler.jul" bundle="component_scheduler_messages" />
								<input name='monthrep_aug' id='monthrep_aug' type='checkbox' value='AUG'><spagobi:message key="scheduler.aug" bundle="component_scheduler_messages" />
								<input name='monthrep_sep' id='monthrep_sep' type='checkbox' value='SEP'><spagobi:message key="scheduler.sep" bundle="component_scheduler_messages" />
								<input name='monthrep_oct' id='monthrep_oct' type='checkbox' value='OCT'><spagobi:message key="scheduler.oct" bundle="component_scheduler_messages" />
								<input name='monthrep_nov' id='monthrep_nov' type='checkbox' value='NOV'><spagobi:message key="scheduler.nov" bundle="component_scheduler_messages" />
								<input name='monthrep_dic' id='monthrep_dic' type='checkbox' value='DIC'><spagobi:message key="scheduler.dic" bundle="component_scheduler_messages" />			
							</div>	
						</div>
						<br/>
						<div class="div_form_row" >
							<div class='div_form_label'>
								<input id='dayinmonth_selection_interval' name='dayinmonth_selection' type='radio' checked='true'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.theDay" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<select name='dayinmonthrep_n' id='dayinmonthrep_n' >
								<%
								for(int i=1; i<=31; i++) {
									out.write("<option value='"+i+"'>"+i+"</option>");
								}
								%>
								</select>
							</div>	
						</div>
						<div class="div_form_row" >
							<div class='div_form_label'>
							    <input id='dayinmonth_selection_checks' name='dayinmonth_selection' type='radio'>
								<span class='portlet-form-field-label'>
									<spagobi:message key="scheduler.inWeeks" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<input name='dayinmonthrep_week' id='dayinmonthrep_week1' type='radio' value='1'><spagobi:message key="scheduler.firstweek" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_week' id='dayinmonthrep_week2' type='radio' value='2'><spagobi:message key="scheduler.secondweek" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_week' id='dayinmonthrep_week3' type='radio' value='3'><spagobi:message key="scheduler.thirdweek" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_week' id='dayinmonthrep_week4' type='radio' value='4'><spagobi:message key="scheduler.fourthweek" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_week' id='dayinmonthrep_weekL' type='radio' value='L'><spagobi:message key="scheduler.lastweek" bundle="component_scheduler_messages" />
							</div>	
						</div>
						<div class="div_form_row" >
							<div class='div_form_label'>
								<span class='portlet-form-field-label'>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<spagobi:message key="scheduler.inDays" bundle="component_scheduler_messages" />
								</span>
							</div>
							<div class='div_form_field'>
								<input name='dayinmonthrep_sun' id='dayinmonthrep_sun' type='checkbox' value='SUN'><spagobi:message key="scheduler.sun" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_mon' id='dayinmonthrep_mon' type='checkbox' value='MON'><spagobi:message key="scheduler.mon" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_tue' id='dayinmonthrep_tue' type='checkbox' value='TUE'><spagobi:message key="scheduler.tue" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_wed' id='dayinmonthrep_wed' type='checkbox' value='WED'><spagobi:message key="scheduler.wed" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_thu' id='dayinmonthrep_thu' type='checkbox' value='THU'><spagobi:message key="scheduler.thu" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_fri' id='dayinmonthrep_fri' type='checkbox' value='FRI'><spagobi:message key="scheduler.fri" bundle="component_scheduler_messages" />
								<input name='dayinmonthrep_sat' id='dayinmonthrep_sat' type='checkbox' value='SAT'><spagobi:message key="scheduler.sat" bundle="component_scheduler_messages" />
							</div>	
						</div>
					</div>
				</div>
				<div style='clear:left;'></div>
			</div>
			
			
		</div>
	</div>
		     

	<div style='clear:left;'></div>
	<br/>

	
	
	<script>
		fillFormFromRepetitionString('<%=triggerInfo.getChronString()%>');
	</script>
	
	

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
		Iterator iterJobBiobjs = jobBiobjects.iterator();
	    int index = 0;
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

