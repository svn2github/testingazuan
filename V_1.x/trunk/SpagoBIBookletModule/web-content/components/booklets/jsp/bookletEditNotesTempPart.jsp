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

<%@ page import="it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				java.util.Map,
				java.util.HashMap,
				java.util.Set,
				it.eng.spago.base.SourceBean,
				java.util.HashMap,
				it.eng.spagobi.constants.SpagoBIConstants" %>

<%
	SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute(BookletsConstants.BOOKLET_COLLABORATION_MODULE); 
	String pathConfBook = (String)moduleResponse.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
	String indexPart = (String)moduleResponse.getAttribute(BookletsConstants.BOOKLET_PART_INDEX);
	String activityKey = (String)moduleResponse.getAttribute(SpagoBIConstants.ACTIVITYKEY);
	Map imageurl = (Map)moduleResponse.getAttribute("mapImageUrls");
    String notes = (String)moduleResponse.getAttribute("notes");
    Iterator iterImgs = null;
    
    // add parameters to back url
    Map backUrlPars = new HashMap();
    backUrlPars.put("PAGE", "WorkflowToDoListPage");
    backUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	String backUrl = urlBuilder.getUrl(request, backUrlPars);
	
	// add parameters to save url
	Map saveNoteUrlPars = new HashMap();
	saveNoteUrlPars.put("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
	saveNoteUrlPars.put("OPERATION", BookletsConstants.OPERATION_SAVE_NOTE);
	saveNoteUrlPars.put(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
	saveNoteUrlPars.put(BookletsConstants.BOOKLET_PART_INDEX, indexPart);
	saveNoteUrlPars.put(SpagoBIConstants.ACTIVITYKEY, activityKey);
	saveNoteUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	String saveNoteUrl = urlBuilder.getUrl(request, saveNoteUrlPars);
	      
	// add parameter to close url
	Map closeNoteUrlPars = new HashMap();
	closeNoteUrlPars.put("PAGE", "CompleteOrRejectActivityPage");
	closeNoteUrlPars.put("CompletedActivity", "TRUE");
	closeNoteUrlPars.put(SpagoBIConstants.ACTIVITYKEY, activityKey);
	closeNoteUrlPars.put(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	String closeNoteUrl = urlBuilder.getUrl(request, closeNoteUrlPars);
	      
%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "book.editnotes"  bundle="component_booklets_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/back.png")%>' 
      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
			</a>
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('formNotes').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "book.save"  bundle="component_booklets_messages"/>' />
			</a>
		</td>

		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=closeNoteUrl%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.closeDiscussion" bundle="component_booklets_messages" />' 
      				 src='<%= urlBuilder.getResourceLink(request, "/components/booklets/img/closeNotes32.png")%>' 
      				 alt='<spagobi:message key = "book.closeDiscussion"  bundle="component_booklets_messages"/>' />
			</a>
		</td>
		
	</tr>
</table>



<br/>

	
<spagobi:error/>	


	
	<script>
			
			var tabs = new Array(<%=(imageurl.size() + 1)%>);
			<%
				iterImgs = imageurl.keySet().iterator();
				int prog = 0;
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
			%>
					tabs[<%=prog%>]="tab<%=nameImg%>";
			<%
				prog ++;
				}
			%>
			tabs[<%=prog%>]="tabNote";
			
			
			var divs = new Array(<%=(imageurl.size() + 1)%>);
			<%
				iterImgs = imageurl.keySet().iterator();
				prog = 0;
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
			%>
					divs[<%=prog%>]="div<%=nameImg%>";
			<%
				prog ++;
				}
			%>
			divs[<%=prog%>]="divNote";
			
			
			var selectedName = "Note"
			
			
		function changeTab(name) {
			for(i=0; i<divs.length; i++) {
			    completeTabName = tabs[i];
				completeDivName = divs[i];
				divobj = document.getElementById(completeDivName);
				tabobj = document.getElementById(completeTabName);
				divName = completeDivName.substring(3);
				tabName = completeTabName.substring(3);
				if(divName==name){
					divobj.style.display='inline';
				} else {
				  divobj.style.display='none';
				}
				if(tabName==name){
					tabobj.className='tab selected';
				} else {
				  tabobj.className='tab';
				}
			}
		}
	
	</script>
	
	
	
	<!-- ************************ START BUILT TABS ************** -->
	
	<div style='width:100%;' class='UITabs'>
		<div class="first-tab-level" style="background-color:#f8f8f8">
			<div style="overflow: hidden; width:  100%">
				
			<%
				iterImgs = imageurl.keySet().iterator();
				while(iterImgs.hasNext()){
					String nameImg = (String)iterImgs.next();
					String linkClass = "tab";
			%>
		
				<div id='tab<%=nameImg%>' class='<%=linkClass%>'>
					<a href="javascript:changeTab('<%=nameImg%>')" style="color:black;"> <%=nameImg%> </a>
				</div>
		
			<%	
				}
			%>
						
				<div id='tabNote' class='tab selected'>
					<a href="javascript:changeTab('Note')" style="color:black;">Note</a>
				</div>	
				
			</div>
		</div>
	</div>
	
	<!-- ************************ END BUILT TABS ************** -->
	
		
		
		
		
		
		
	

	<!-- ************************ START BUILT DIVS IMAGE AND NOTE ************** -->	
		
	<div style="width:100%;background-color:#f8f8f8;border:1 solid black;">
	<br/>	
	<%
		iterImgs = imageurl.keySet().iterator();
		while(iterImgs.hasNext()){
			String nameImg = (String)iterImgs.next();
			String url = (String)imageurl.get(nameImg);
	%>
		<div style="display:none;" name="div<%=nameImg%>" id="div<%=nameImg%>">
			<center>
				<img src="<%=url%>" />
			</center>
			<br/>
			<br/>
		</div>
	<%
		}
	%>
	    <div style="display:inline;" name="divNote" id="divNote" >
			<div name="notesdiv" id="notesdiv" >
				<form method="POST" id="formNotes" action="<%=saveNoteUrl%>" >
				<center>
					<b><spagobi:message key = "book.notes"  bundle="component_booklets_messages"/></b>
					<br/>
					<textarea name="notes" style="width:1000px;height:350px;"><%=notes%></textarea>
				<center>
				</form>
			</div>
			<br/>
			<br/>
			
		
		</div>
	
	
	<!-- ************************ START BUILT DIVS IMAGE AND NOTE ************** -->	
	
	</div>











