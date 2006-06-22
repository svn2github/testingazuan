<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page language="java"%>
<%@ page
	import="it.eng.spago.base.*,it.eng.qbe.utility.*,it.eng.qbe.javascript.*,it.eng.qbe.wizard.*"%>
<%@ page import="java.util.*"%>



<%@ include file="../jsp/qbe_base.jsp"%>

<%-- For validation --%>
<%ISingleDataMartWizardObject aWizardObject = (ISingleDataMartWizardObject) sessionContainer
					.getAttribute(WizardConstants.SINGLE_DATA_MART_WIZARD);
			it.eng.qbe.model.DataMartModel dm = (it.eng.qbe.model.DataMartModel) sessionContainer
					.getAttribute("dataMartModel");

			dm.updateCurrentClassLoader();

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


<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section' 
		    style='vertical-align:middle;padding-left:5px;'>
			<%=dm.getName()%> : <%=dm.getDescription()%> - <%=qbeMsg.getMessage(requestContainer,"QBE.Title.Savepage")%>
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<%@include file="../jsp/qbe_headers.jsp"%>
	</tr>
</table>


<%@include file="../jsp/testata.jsp" %>


<div class='div_background_no_img'>

	<%if ((aWizardObject.getEntityClasses() != null)
					&& (aWizardObject.getEntityClasses().size() > 0)) {%>

<table width="100%">
	<tr>
		<td width="3%">&nbsp;</td>
		<td width="47%">&nbsp;</td>
		<td width="47%">&nbsp;</td>
		<td width="3%">&nbsp;</td>
	</tr>
	<tr>
		<td width="3%"></td>
		<%-- Rientro  --%>
		<td width="47%"><%-- Save --%>
			<form id="formPersistQuery" name="formPersistQuery"
					action="<%=qbeUrl.getUrl(request,null) %>" method="POST"><input
					type="hidden" id="previousQueryId" name="previousQueryId"
					value="<%= (aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : "")  %>" />
				<input type="hidden" name="ACTION_NAME" value="PERSIST_QUERY_ACTION" />
				<table>
					<tr>
						<td colspan="2"><span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,
								"QBE.SavePage.Savings")%></span>
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><%=qbeMsg.getMessage(requestContainer,
								"QBE.Resume.Query.QueryIdentifier")%>
						</td>
						<td><input id="queryId" type="text" name="queryId"
							value="<%=(aWizardObject.getQueryId() != null ? aWizardObject.getQueryId() : "") %>">
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><%=qbeMsg.getMessage(requestContainer,
								"QBE.Resume.Query.QueryDescr")%>
						</td>
						<td><input type="text" name="queryDescritpion"
							value="<%=(aWizardObject.getDescription() != null ? aWizardObject.getDescription() : "") %>">
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><b><%=qbeMsg.getMessage(requestContainer,
								"QBE.Resume.Query.Visibility")%></b>
						</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><%if (aWizardObject.getVisibility()) {

					%> <input
							type="radio" name="visibility" value="public" checked="checked">
						<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Public")%>
						<br>
						<input type="radio" name="visibility" value="private"> <%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Private")%>
						<%} else {

					%> <input type="radio" name="visibility" value="public">
						<%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Public")%>
						<br>
						<input type="radio" name="visibility" value="private"
							checked="checked"> <%=qbeMsg.getMessage(requestContainer,
									"QBE.Resume.Query.Visibility.Private")%>
						<%}

				%>		</td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button" value="<%=qbeMsg.getMessage(requestContainer,
								"QBE.SaveQuery.SaveButton")%>"
							onclick="checkFormPersistQueryAndSubmit();" /></td>
					</tr>
				</table>
			</form>
		</td>
		<td width="47%">
			<table width="100%">
				<tbody>
					<tr>
						<td><span class="qbeTitle"><%=qbeMsg.getMessage(requestContainer,
								"QBE.ValidateQuery.FinalQuery")%></span>
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
									<td width="100%"><b><%=qbeMsg.getMessage(requestContainer,
									"QBE.ValidateQuery.CorrectQuery")%></b>
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
												<TH><%=headerName%></TH>
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
									<td><span class="qbeError"><%=qbeMsg.getMessage(requestContainer, msgFinal)
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
										"QBE.Error.GenericError").trim()%></span>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=qbeMsg.getMessage(requestContainer, msgFinal)
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
								"QBE.ValidateQuery.ExpertQuery")%></span></td>
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
									<td width="100%"><b><%=qbeMsg.getMessage(requestContainer,
									"QBE.ValidateQuery.CorrectQuery")%></b>
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
						it = headers.iterator();
						String headerName = "";

						while (it.hasNext()) {
							headerName = (String) it.next();

							%>

												<th><%=headerName%></th>

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
												msgExpert).trim()%></span>
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
										"QBE.Error.GenericError").trim()%></span>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td></td>
									<td><textarea id="txtAreaMsgError" readonly="true" rows="10"
										cols="80"><%=qbeMsg.getMessage(requestContainer,
												msgExpert).trim()%></textarea>
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
	</tr>
	<td width="3%">&nbsp;</td>
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
						"QBE.Warning.NoFieldSelected")%></span>
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
