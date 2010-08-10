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
<%@ page language="java"%>

<%@ page import="it.eng.spago.base.*"%>
<%@ page import="it.eng.qbe.javascript.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@ page import="it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>



<%@ include file="../jsp/qbe_base.jsp"%>

<%-- For validation --%>
<%

			Object spagoBiInfo = sessionContainer.getAttribute("spagobi"); 

			ISingleDataMartWizardObject aWizardObject = Utils.getWizardObject(sessionContainer);
			it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel) sessionContainer
					.getAttribute("dataMartModel");

			//dm.updateCurrentClassLoader();

			String msgFinal = (String) aServiceResponse
					.getAttribute("ERROR_MSG_FINAL");
			String msgExpert = (String) aServiceResponse
					.getAttribute("ERROR_MSG_EXPERT");

			boolean flagErrorsFinal = false;
			boolean flagErrorsExpert = false;

			if (msgFinal != null) {

				flagErrorsFinal = true;

			}

			if (msgExpert != null) {

				flagErrorsExpert = true;

			}

			String finalQueryString = null;
			if (aWizardObject.isUseExpertedVersion()) {
				finalQueryString = aWizardObject.getExpertQueryDisplayed();
			} else {
				finalQueryString = aWizardObject.getFinalQuery();
			}

			
			
			%>





<%if (qbeMode.equalsIgnoreCase("WEB")) {

			%>
<body>
<%}%>

<%
	if(spagoBiInfo == null) {
%>
<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<%=dm.getName()%> : <%=dm.getDescription()%> - <%=qbeMsg.getMessage(requestContainer,"QBE.Title.Savepage", bundle)%>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="../jsp/qbe_headers.jsp"%>
	</tr>
</table>
<%
	}
%>

<%@include file="../jsp/testata.jsp" %>


<div class='div_background_no_img'>

	<%if ((aWizardObject.getEntityClasses() != null)
					&& (aWizardObject.getEntityClasses().size() > 0)) {%>

<table width="100%">
	<tr>
		<td width="3%">&nbsp;</td>
		<td width="45%">&nbsp;</td>
		<td width="4%">&nbsp;</td>
		<td width="45%">&nbsp;</td>
		<td width="3%">&nbsp;</td>
	</tr>
	<tr>
		
		
		<td width="3%">&nbsp;</td>




		<td class="qbe-td-form"><%-- Save --%>
			<form id="formPersistQuery" name="formPersistQuery"
					action="<%=qbeUrl.getUrl(request,null) %>" method="POST"><input
					type="hidden" id="previousQueryId" name="previousQueryId"
					value="<%= (aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : "")  %>" />
				<input type="hidden" name="ACTION_NAME" value="PERSIST_QUERY_ACTION" />
				<table>
					<tr>
						<td colspan="2" >
							<span class="qbeTitle">
									<%=qbeMsg.getMessage(requestContainer, "QBE.SavePage.Savings", bundle)%>
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td width="120px" class="qbe-font" valign="middle">
							<%=qbeMsg.getMessage(requestContainer,"QBE.Resume.Query.QueryIdentifier", bundle)%>
						</td>
						<td >
								<input id="queryId" type="text" name="queryId"
												value="<%=(aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : "") %>">
						</td>
					</tr>
					<tr>
						<td width="150px" class="qbe-font" valign="middle">
							<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.QueryDescr", bundle)%>
						</td>
						<td><input type="text" name="queryDescritpion"
							value="<%=(aWizardObject.getDescription() != null ? aWizardObject.getDescription() : "") %>">
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" class="qbeTitle">
							<%=qbeMsg.getMessage(requestContainer, "QBE.Resume.Query.Visibility", bundle)%></b>
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><%if (aWizardObject.getVisibility()) {

					%> <input
							type="radio" name="visibility" value="public" checked="checked">
              <span class="qbe-font">
						<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Public", bundle)%>
							</span>
						<br>
						<input type="radio" name="visibility" value="private"> 
						<span class="qbe-font">
						<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Private", bundle)%>
						</span>
						<%} else {

					%> <input type="radio" name="visibility" value="public">
					<span class="qbe-font">
						<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Public", bundle)%>
						</span>
						<br>
						<input type="radio" name="visibility" value="private"
							checked="checked"> 
							<span class="qbe-font">
							<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Private", bundle)%>
							</span>
						<%}

				%>		</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
						    <input type="button" 
											 value="<%=qbeMsg.getMessage(requestContainer, "QBE.SaveQuery.SaveButton", bundle)%>"
							onclick="checkFormPersistQueryAndSubmit();" /></td>
					</tr>
				</table>
			</form>
		</td>
		
		
		
		<td>&nbsp;</td>
		
		
		
		<td class="qbe-td-form">
			<table width="100%">
				<tbody>
					<tr>
						<td><span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,
								"QBE.ValidateQuery.FinalQuery", bundle)%></span>
						</td>
					</tr>
					<tr>
						<td><%-- Final --%>
							<table>
								<%-- VALIDATION --%>

								<%if (!flagErrorsFinal) {

					%>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>

								<tr>
									<td width="100%">
										<span class="qbe-font">
											<%=qbeMsg.getMessage(requestContainer, "QBE.ValidateQuery.CorrectQuery", bundle)%>
										</span>
									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<td width="100%">
									<table valign="top" border='0' cellspacing='1'>
										<thead>
											<tr>
												<%List headers = aWizardObject.getSelectClause()
							.getSelectFields();
					Iterator it = headers.iterator();
					String headerName = "";
					ISelectField selField = null;
					while (it.hasNext()) {
						selField = (ISelectField) it.next();
						headerName = (selField.getFieldAlias() != null ? selField
								.getFieldAlias()
								: selField.getFieldName());

						%>
												<td class="qbe-font"><%=headerName%></td>
												<%}

				%>
											</tr>
										</thead>

									</table>

									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<%} else {

					%>

								<%String joinMsg = (String) aServiceResponse
							.getAttribute("JOIN_WARNINGS");

					if (joinMsg != null) {

						%>
								<tr>
									<td width="3%">&nbsp;</td>
									<td width="97%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><span class="qbeError"><%=qbeMsg.getMessage(requestContainer, msgFinal, bundle)
										.trim()%></span>
									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=joinMsg.trim()%></textarea></td>
								</tr>

								<tr>
									<td>&nbsp;</td>
								</tr>


								<%} else {%>
								<tr>
									<td width="3%">&nbsp;</td>
									<td width="97%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><span class="qbeError"><%=qbeMsg.getMessage(requestContainer,
										"QBE.Error.GenericError", bundle).trim()%></span>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=qbeMsg.getMessage(requestContainer, msgFinal, bundle)
										.trim()%></textarea>
									</td>

								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<%}

				%>
								<%}%>
							</table>
							<%-- VALIDATION --%></td>
						</tr>
						<tr>
							<td><span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,
								"QBE.ValidateQuery.ExpertQuery", bundle)%></span></td>
						</tr>

						<tr>
							<td><%-- Expert--%>
							<table>
								<%-- VALIDATION --%>

								<%if (!flagErrorsExpert) {

					%>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>

								<tr>
									<td width="100%">
										<span class="qbe-font">
											<%=qbeMsg.getMessage(requestContainer, "QBE.ValidateQuery.CorrectQuery", bundle)%>
									  </span>
									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<td width="100%">
									<table valign="top">
										<%Iterator it = null;
					List headers = null;
					{%>
										<thead>
											<tr>
												<%headers = aWizardObject.extractExpertSelectFieldsList();
						if (headers == null)
							headers = new ArrayList();
						it = headers.iterator();
						String headerName = "";

						while (it.hasNext()) {
							headerName = (String) it.next();

							%>

												<td class="qbe-font"><%=headerName%></td>

												<%}

					%>

											</tr>
										</thead>

										<%}%>

									</table>

									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<%} else {

					%>

								<%String joinMsg = (String) aServiceResponse
							.getAttribute("JOIN_WARNINGS");

					if (joinMsg != null) {

						%>
								<tr>
									<td width="3%">&nbsp;</td>
									<td width="97%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><span class="qbeError"><%=qbeMsg.getMessage(requestContainer,
												msgExpert, bundle).trim()%></span>
									</td>
								</tr>
								<tr>
									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=joinMsg.trim()%></textarea></td>
								</tr>

								<tr>
									<td>&nbsp;</td>
								</tr>


								<%} else {%>
								<tr>
									<td width="3%">&nbsp;</td>
									<td width="97%">&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><span class="qbeError"><%=qbeMsg.getMessage(requestContainer,
										"QBE.Error.GenericError", bundle).trim()%></span>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=qbeMsg.getMessage(requestContainer,
												msgExpert, bundle).trim()%></textarea>
									</td>

								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<%}

				%>
								<%}%>
							</table>
							<%-- VALIDATION --%></td>
					</tr>
				</tbody>
			</table>
		</td>
	
	<td width="3%">&nbsp;</td>
	</tr> 
</table>

	<%} else {%>

<table width="100%">
	<tr>
		<td width="3%">&nbsp;</td>
		<td width="94%">&nbsp;</td>
		<td width="3%">&nbsp;</td>
	</tr>

	<tr>
		<td></td>
		<td valign="top"><span class="qbeError"><%=qbeMsg.getMessage(requestContainer,
						"QBE.Warning.NoFieldSelected", bundle)%></span>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<%}%>
<%-- SAVE --%>


<%if (qbeMode.equalsIgnoreCase("WEB")) {
%>
</body>
<%}%>

<div id="divSpanCurrent"><span id="currentScreen">DIV_SAVE_QUERY</span>
</div>

<%@include file="../jsp/qbefooter.jsp"%>


</div>
