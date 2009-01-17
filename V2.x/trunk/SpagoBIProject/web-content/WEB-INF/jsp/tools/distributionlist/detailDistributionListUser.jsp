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
	<%@ include file="/jsp/commons/portlet_base.jsp"%>
	
	<%@ page         import="it.eng.spagobi.tools.distributionlist.bo.DistributionList,
							 it.eng.spagobi.tools.distributionlist.bo.Email,
	 				         it.eng.spago.navigation.LightNavigationManager,
	 				         java.util.Map,java.util.HashMap,java.util.List,
	 				         java.util.Iterator,
	 				         it.eng.spagobi.commons.bo.Domain,
	 				         it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects,
	 				         it.eng.spagobi.tools.distributionlist.service.DetailDistributionListUserModule" %>
	 				         
	<%@page import="it.eng.spagobi.commons.utilities.ChannelUtilities"%>
	
	<%
		SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailDistributionListUserModule"); 
		DistributionList dl = (DistributionList)moduleResponse.getAttribute("dlObj");
	%>
	
	<%@page import="it.eng.spagobi.analiticalmodel.document.bo.BIObject"%>

	
	<%@page import="it.eng.spagobi.commons.dao.DAOFactory"%>
<%@page import="java.util.ArrayList"%>
<div class='div_background_no_img' style='padding-top:5px;padding-left:5px;'>
	<BR>
	<table width="100%" cellspacing="0" border="0" id = "fieldsTable" >
	<tr>
	  <td>
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>
				<spagobi:message key = "SBISet.ListDL.columnName" />
			</span>
		</div>
		<%
			  String name = dl.getName();
			   if((name==null) || (name.equalsIgnoreCase("null"))  ) {
				   name = "";
			   }
		%>
		<div class='div_detail_form'>
			<input class='portlet-form-input-field' type="text" 
				   name="NAME" size="50" value="<%=name%>" readonly maxlength="50" />
		</div>
		
		<div class='div_detail_label'>
			<span class='portlet-form-field-label'>	
				<spagobi:message key = "SBISet.ListDL.columnDescr" />
			</span>
		</div>
		<div class='div_detail_form'>
		<%
			   String descr = dl.getDescr();
			   if((descr==null) || (descr.equalsIgnoreCase("null"))  ) {
			   	   descr = "";
			   }
		%>
			<input class='portlet-form-input-field' type="text" name="DESCR" 
				   size="50" value="<%= descr %>" readonly maxlength="160" />
		</div>
	
	</td><!-- CLOSE COLUMN WITH DATA FORM  -->
		
		
		<spagobi:error/>
	</tr>
	</table>   <!-- CLOSE TABLE FORM ON LEFT AND VERSION ON RIGHT  -->

	<br>
		
	<!-- LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
		<%
			List documents = dl.getDocuments();
			if(!documents.isEmpty()){
	%>		
	

	
	<table style='width:98%;margin-top:1px' id = "docTable" >

	<tr class='header-row-portlet-section'>
	
		<td class='header-title-column-portlet-section-nogrey' style='text-align:center'>
				<spagobi:message key = "SBISet.ListDL.relatedDoc" />
		</td>
	
	</tr>
	</table>
	<table style='width:98%;margin-top:1px' id = "docTable" >
	<tr>
	  <td class='portlet-section-header' style='text-align:left'>

				<spagobi:message key = "SBISet.ListDL.columnDocName" />		
	</td>				
	<td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.columnDocDescr" />			

	 </td>
		 <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.ScheduleStart" />			

	 </td>
	 	 <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.ScheduleEnd" />	
	 </td>
	 <td class='portlet-section-header' style='text-align:left'>
				<spagobi:message key = "SBISet.ListDL.ScheduleFrequence" />			

	 </td>
	
	</tr>	
	
		
		<%
			List viewedDocuments = new ArrayList();
			Iterator it2 = documents.iterator();
			while(it2.hasNext()){
				
				BIObject bo = (BIObject)it2.next();
				Integer objId = bo.getId();
				if (viewedDocuments.contains(objId)) continue;
				
				String docName = bo.getName();
				String docDescr = bo.getDescription();
				
				if((docName==null) || (docName.equalsIgnoreCase("null"))  ) {
					docName = "";
				   }
				if((docDescr==null) || (docDescr.equalsIgnoreCase("null"))  ) {
					docDescr = "";
				   }
				
				List xmls = DAOFactory.getDistributionListDAO().getXmlRelated(dl , bo.getId().intValue());
				if (! xmls.isEmpty()){
					Iterator xit = xmls.iterator();
					while (xit.hasNext()){
						String xml = (String) xit.next();						
						SourceBean sbOrig = SourceBean.fromXMLString(xml);
						
						String schedStart = (String)sbOrig.getAttribute("startDate");
						String temp1 = (String)sbOrig.getAttribute("startTime");
						String schedStartTime = temp1.substring(0,temp1.indexOf("+"));
						String schedBegin = schedStart + " " + schedStartTime ;
						
						String schedE = "";					
						String schedEndTime = "";
						if ( sbOrig.getAttribute("endDate") != null) schedE = (String)sbOrig.getAttribute("endDate");					
						if ( sbOrig.getAttribute("endTime") != null) {
							String temp = (String)sbOrig.getAttribute("endTime");
							schedEndTime = temp.substring(0,temp.indexOf("+"));	
						}
						String schedEnd = schedE + " " + schedEndTime ;
						
						String frequency = "";
						String temp = (String)sbOrig.getAttribute("chronString");
						int index = temp.indexOf("{");
						int end = temp.indexOf("}");
						String every = temp.substring(0,index);
						String content = temp.substring(index+1, end);
						
						if (every.equals("single")){
							frequency = msgBuilder.getMessage("sbi.frequency.singleEx", "messages", request)+" ";
						}
						if (every.equals("minute")){
							
							int begin = content.indexOf("=");
							String numRep = content.substring(begin+1);
							if (numRep.equals("1")){
								frequency = msgBuilder.getMessage("sbi.frequency.everyMin", "messages", request)+" ";
							}
							else {frequency = msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+numRep+" "+msgBuilder.getMessage("sbi.frequency.minutes", "messages", request)+" ";}
						}
						if (every.equals("hour")){
							
							int begin = content.indexOf("=");
							String numRep = content.substring(begin+1);
							if (numRep.equals("1")){
								frequency = msgBuilder.getMessage("sbi.frequency.everyH", "messages", request)+" ";
							}
							else
							{frequency = msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+numRep+" "+msgBuilder.getMessage("sbi.frequency.hours", "messages", request)+" ";}
						}
						if (every.equals("day")){
							
							int begin = content.indexOf("=");
							String numRep = content.substring(begin+1);
							if (numRep.equals("1")){
								frequency = msgBuilder.getMessage("sbi.frequency.everyD", "messages", request)+" ";
							}
							else
							{frequency = msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+numRep+" "+msgBuilder.getMessage("sbi.frequency.days", "messages", request)+" ";}
						}
						
						if (every.equals("week")){
							String[] params = content.split(";");
							int l = params.length ;
							String numRep = "";
							String days = "";
							for (int i =0;i<l;i++){
								String param = params[i];
								int begin = param.indexOf("=");
								
								if (param.startsWith("numRepetition")){		
									numRep = param.substring(begin+1);
								}
									
								if (param.startsWith("days")){
									days = param.substring(begin+1);
								}
							}
							days = days.replace(',',';');
							if (numRep.equals("1")){
								frequency = msgBuilder.getMessage("sbi.frequency.everyW", "messages", request)+" "+msgBuilder.getMessage("sbi.frequency.Days", "messages", request)+" "+days;
							}
							else
							{frequency = msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+numRep+" "+msgBuilder.getMessage("sbi.frequency.weeks", "messages", request)+" "+msgBuilder.getMessage("sbi.frequency.Days", "messages", request)+" "+days+" ";}
						}	
							
						if (every.equals("month")){	
							
							String[] params = content.split(";");
							String numRep = "";
							String months = "";
							String dayRep = "";
							String weeks = "";
							String days = "";
							int l = params.length ;
							for (int i =0;i<l;i++){
								String param = params[i];
								int begin = param.indexOf("=");
								
								if (param.startsWith("numRepetition")){	
									numRep = param.substring(begin+1);
								}
								if (param.startsWith("months")){
									months = param.substring(begin+1);
								}
								if (param.startsWith("dayRepetition")){
									dayRep = param.substring(begin+1);
								}
								if (param.startsWith("weeks")){
									weeks = param.substring(begin+1);
								}
								if (param.startsWith("days")){
									days = param.substring(begin+1);
								}
							}
							if (numRep.equals("0")){
								months = months.replace(',',';');
								frequency = "Months: "+months+"." ;
							}
							else if (!numRep.equals("0")){
								if (numRep.equals("1")){
									frequency = msgBuilder.getMessage("sbi.frequency.everyMonth", "messages", request)+" ";
								}
								else
								{frequency = msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+numRep+" "+ msgBuilder.getMessage("sbi.frequency.months", "messages", request)+" " ;}
							}	
							
							if (dayRep.equals("0")){
								if (weeks.equals("NONE")){
									days = days.replace(',',';');
									frequency = frequency + msgBuilder.getMessage("sbi.frequency.Days", "messages", request)+" "+days +" ";
								}	
								else if (!weeks.equals("NONE")){
									weeks = weeks.replace(',',';');
									if (weeks.equals("L")){
										frequency = frequency + msgBuilder.getMessage("sbi.frequency.Week", "messages", request)+" "+msgBuilder.getMessage("sbi.frequency.last", "messages", request)+" " ;
									}
									else if (weeks.equals("F")){
										frequency = frequency + msgBuilder.getMessage("sbi.frequency.Week", "messages", request)+" "+msgBuilder.getMessage("sbi.frequency.first", "messages", request)+" " ;
									}
									else
									{frequency = frequency + " Week: "+weeks+" " ;}
									if (!days.equals("NONE")){
										days = days.replace(',',';');
										frequency = frequency + msgBuilder.getMessage("sbi.frequency.Days", "messages", request)+" "+days +" ";
									}	
								}	
								
							}
							else if (!dayRep.equals("0")){
								days = days.replace(',',';');
								if (dayRep.equals("1")){
									frequency = frequency + msgBuilder.getMessage("sbi.frequency.everyFirstD", "messages", request)+" ";
								}
								else
								{frequency = frequency + msgBuilder.getMessage("sbi.frequency.every", "messages", request)+" "+dayRep+" "+msgBuilder.getMessage("sbi.frequency.days", "messages", request) ;}
							}
							
						}	
										
		 %>			
		 			<tr class='portlet-font'>
		 			<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>
				    	<%=docName%>
				    	</td>
				   	<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%=docDescr %>				   				
				   	</td>	
				    <td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%=schedBegin %>				   				
				   	</td>	
				   	<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%=schedEnd %>				   				
				   	</td>
					<td class='portlet-section-body' style='vertical-align:left;text-align:left;'>	
					<%=frequency %>				   				
				   	</td>
			
				   	</tr>
	<% }}   
				viewedDocuments.add(objId);	
			} %>
	
		<spagobi:error/>
								
		
	</table> 
	<% } %>
 <!-- CLOSE LIST OF DOCUMENTS RELATED TO A DISTRIBUTION LIST  -->
	
	
	</div>  

	
	<%@ include file="/jsp/commons/footer.jsp"%>
