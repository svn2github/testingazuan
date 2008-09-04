<%-- 

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

--%>

  
<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java" %>


<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>


<%--
	IQbeUrlGenerator qbeUrl = new WebQbeUrlGenerator();
--%>

<qbe:page>

	
	
	
	<qbe:url type="resource" var="spagobiCSS" ref="../css/spagobi.css"/>
	<link rel="styleSheet" href ="${spagobiCSS}" type="text/css" />
	
	<qbe:url type="resource" var="jsr168CSS" ref="../css/jsr168.css"/>
	<link rel="styleSheet" href ="${jsr168CSS}" type="text/css" />
	
	<qbe:url type="resource" var="externalCSS" ref="../css/external.css"/>
	<link rel="styleSheet" href ="${externalCSS}" type="text/css" />
	
	<qbe:url type="resource" var="dtreeCSS" ref="../css/dtree.css"/>
	<link rel="styleSheet" href ="${dtreeCSS}" type="text/css" />

	<qbe:url type="resource" var="qbeCommonsScripts" ref="../js/commons/qbe_commons.js"/>
	<script type="text/javascript" src='${qbeCommonsScripts}'/></script>
	
	<qbe:url type="resource" var="pageJavaScripts" ref="../js/querybuilder/select/selectPage.js"/>
	<script type="text/javascript" src='${pageJavaScripts}'/></script>
	
	<qbe:url type="resource" var="dtreeScripts" ref="../js/dtree.js"/>
	<script type="text/javascript" src='${dtreeScripts}'/></script>
	
	
	<qbe:message key="QBE.Title.Selection" var="pageName" />	
	<qbe:url type="action" var="updateSelectionClause"/>



	<qbe:page-content>
		
		<%@include file="/jsp/commons/titlebar.jspf" %>
		<%@include file="/jsp/testata.jsp" %>	
		
		

		<div class='div_background_no_img'>
		
			<table width="100%">  
				<tr>
					<td width="2%">&nbsp;</td>	   	
				   	<td width="29%">&nbsp;</td>
				   	<td width="29%">&nbsp;</td>
				   	<td width="30%">&nbsp;</td>
				</tr>
				
				<tr>
					<td></td>
					
					<td valign="top">						
						<%@include file="tree.jspf" %>							
					</td>
						
					<td>
						<qbe:newtree actionName="SELECT_FIELD_FOR_SELECT_ACTION" actionType="action" modality="full"/>
					</td>
						  		
					<td width="47%" valign="top">	
						
						<%@include file="fields.jspf" %>
						
					</td>
				</tr>
				
				<tr>
					<td>&nbsp;</td>
			   	</tr>
			</table>
			
		</div>
			
		<!--	  				 									 					
		<tiles:insertTemplate path="operators_window.jspf"/>
		-->
		
		<%@include file="operators_window.jspf" %>
	 	
		    		   			
		<script type="text/javascript">
			changeTabBkg();
		</script>

	</qbe:page-content>

</qbe:page>


<qbe:audit/>
 
 
