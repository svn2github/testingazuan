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

<%@ page import="javax.portlet.PortletURL,
				it.eng.spago.navigation.LightNavigationManager,
				it.eng.spagobi.booklets.constants.BookletsConstants,
				java.util.List,
				java.util.Iterator,
				java.util.Map,
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
    
	PortletURL backUrl = renderResponse.createActionURL();
    PortletURL saveNoteUrl = renderResponse.createActionURL();
    PortletURL closeNoteUrl = renderResponse.createActionURL();
	
    // add parameters to back url
	backUrl.setParameter("PAGE", "WorkflowToDoListPage");
	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_RESET, "true");
	
	// add parameters to save url
	saveNoteUrl.setParameter("PAGE", BookletsConstants.BOOKLET_COLLABORATION_PAGE);
	saveNoteUrl.setParameter("OPERATION", BookletsConstants.OPERATION_SAVE_NOTE);
	saveNoteUrl.setParameter(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
	saveNoteUrl.setParameter(BookletsConstants.BOOKLET_PART_INDEX, indexPart);
	saveNoteUrl.setParameter(SpagoBIConstants.ACTIVITYKEY, activityKey);
	saveNoteUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	      
	// add parameter to close url
	closeNoteUrl.setParameter("PAGE", "CompleteOrRejectActivityPage");
	closeNoteUrl.setParameter("CompletedActivity", "TRUE");
	closeNoteUrl.setParameter(SpagoBIConstants.ACTIVITYKEY, activityKey);
	closeNoteUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");	
	    
	    
%>




<table class='header-table-portlet-section'>
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' style='vertical-align:middle;padding-left:5px;'>
			<spagobi:message key = "book.editnotes"  bundle="component_booklets_messages"/>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.back" bundle="component_booklets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/back.png")%>' 
      				 alt='<spagobi:message key = "book.back"  bundle="component_booklets_messages"/>' />
			</a>
		</td>
		
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href="javascript:document.getElementById('formNotes').submit();"> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.save" bundle="component_booklets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/save32.png")%>' 
      				 alt='<spagobi:message key = "book.save"  bundle="component_booklets_messages"/>' />
			</a>
		</td>

		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%=closeNoteUrl.toString()%>'> 
      			<img class='header-button-image-portlet-section' 
      				 title='<spagobi:message key = "book.closeDiscussion" bundle="component_booklets_messages" />' 
      				 src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/components/booklets/img/closeNotes32.png")%>' 
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
				<form method="POST" id="formNotes" action="<%=saveNoteUrl.toString()%>" >
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











