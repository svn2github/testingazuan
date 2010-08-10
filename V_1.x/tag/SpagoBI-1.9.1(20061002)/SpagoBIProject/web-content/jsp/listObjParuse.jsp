<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="java.util.List,
		 		 java.util.Iterator,
		 		 it.eng.spagobi.bo.ObjParuse,
		 		 it.eng.spagobi.bo.ParameterUse,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager,
                 it.eng.spagobi.constants.SpagoBIConstants,
                 it.eng.spagobi.constants.AdmintoolsConstants,
                 it.eng.spagobi.constants.ObjectsTreeConstants,
                 it.eng.spagobi.bo.dao.DAOFactory,
                 it.eng.spagobi.bo.dao.IModalitiesValueDAO,
                 it.eng.spagobi.bo.ModalitiesValue,
                 it.eng.spagobi.bo.QueryDetail,
                 it.eng.spagobi.bo.BIObjectParameter;" %>

<%
	SourceBean moduleResponse = (SourceBean) aServiceResponse.getAttribute("ListObjParuseModule");
	BIObjectParameter objpar = (BIObjectParameter) moduleResponse.getAttribute("objParameter");
	List allParuses = (List) moduleResponse.getAttribute("allParuses");
	List objParuses = (List) moduleResponse.getAttribute("objParuses");
	List otherObjParameters = (List) moduleResponse.getAttribute("otherObjParameters");
	
	PortletURL formUrl = renderResponse.createActionURL();
   	formUrl.setParameter("PAGE", "ListObjParusePage");
   	formUrl.setParameter("MESSAGEDET", AdmintoolsConstants.DETAIL_MOD);
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "ListObjParusePage");
   	backUrl.setParameter("MESSAGEDET", "EXIT_FROM_MODULE");
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");   	
%>
<form method='POST' action='<%= formUrl.toString() %>' id='objParusesForm'>
	<input type="hidden" name="obj_par_id" value="<%=objpar.getId()%>" />
	<%
   	
	if (allParuses == null || allParuses.size() == 0) {
		%>
		<table class='header-table-portlet-section'>		
			<tr class='header-row-portlet-section'>
				<td class='header-title-column-portlet-section' 
				    style='vertical-align:middle;padding-left:5px;'>
					<spagobi:message key = "SBIDev.listObjParuses.title" /> <%=" " + objpar.getLabel()%>
				</td>
				<td class='header-empty-column-portlet-section'>&nbsp;</td>
				<td class='header-button-column-portlet-section'>
					<a href='<%= backUrl.toString() %>'> 
		      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.listObjParuses.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.listObjParuses.backButt" />' />
					</a>
				</td>
			</tr>
		</table>
		<div class='div_background_no_img' style='padding-top:5px;padding-left:5px;'>
			<div class="div_detail_area_forms" >
				<spagobi:message key = "SBIDev.listObjParuses.noParuses" />
			</div>
		</div>
		<%
	} else {
		%>
		<table class='header-table-portlet-section'>		
			<tr class='header-row-portlet-section'>
				<td class='header-title-column-portlet-section' 
				    style='vertical-align:middle;padding-left:5px;'>
					<spagobi:message key = "SBIDev.listObjParuses.title" /> <%=" " + objpar.getLabel()%>
				</td>
				<td class='header-empty-column-portlet-section'>&nbsp;</td>
				<td class='header-button-column-portlet-section'>
					<a href="javascript:document.getElementById('objParusesForm').submit()"> 
		      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.listObjParuses.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.listObjParuses.saveButt" />' /> 
					</a>
				</td>
				<td class='header-button-column-portlet-section'>
					<a href='<%= backUrl.toString() %>'> 
		      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.listObjParuses.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.listObjParuses.backButt" />' />
					</a>
				</td>
			</tr>
		</table>
		
		<div class='div_background_no_img' style='padding-top:5px;padding-left:5px;'>
		
		<div class="div_detail_area_forms_objParuse">
		
		<table style="border-spacing:5px;border-collapse:separate;">
			<tr class="portlet-form-field-label">
				<th align="center">
					<spagobi:message key = "SBIDev.listObjParuses.activeDependancies" /> 
				</th>
				<th align="left">
					<spagobi:message key = "SBIDev.listObjParuses.modality" /> 
				</th>
				<th align="left">
					<spagobi:message key = "SBIDev.listObjParuses.filterOperation" /> 
				</th>
				<th align="left">
					<spagobi:message key = "SBIDev.listObjParuses.filterColumn" /> 
				</th>
				<th align="left">
					<spagobi:message key = "SBIDev.listObjParuses.objParFather" /> 
				</th>										
			</tr>
		
		<%
	
		Iterator it = allParuses.iterator();
		while (it.hasNext()) {
			ParameterUse paruse = (ParameterUse) it.next();
			ObjParuse correlatedObjParuse = null;
			if (objParuses != null && objParuses.size() > 0) {
				Iterator objParusesIt = objParuses.iterator();
				while (objParusesIt.hasNext()) {
					ObjParuse aObjParuse = (ObjParuse) objParusesIt.next();
					if (aObjParuse.getParuseId().equals(paruse.getUseID())) {
						correlatedObjParuse = aObjParuse;
						break;
					}
				}
			}
			%>
			<tr>
				<td align="center">
					<%
					String checked = "";
					if (correlatedObjParuse != null) checked = "checked=\"checked\"";
					%>
					<input type="checkbox" name="paruse_id" value="<%=paruse.getUseID()%>" <%=checked%>/>
				</td>
				<td class="portlet-font">
					<%=paruse.getLabel() + ": " + paruse.getDescription() %>
				</td>
				<td>
					<%
					String typeFilterSelected = "";
					if (correlatedObjParuse != null) typeFilterSelected = correlatedObjParuse.getFilterOperation();
					%>
					<select name='<%=SpagoBIConstants.TYPE_FILTER + "_" + paruse.getUseID()%>'>
						<option value='<%=SpagoBIConstants.START_FILTER%>' <% if (SpagoBIConstants.START_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.startWith" />
						</option>
						<option value='<%=SpagoBIConstants.END_FILTER%>' <% if (SpagoBIConstants.END_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.endWith" />
						</option>
						<option value='<%=SpagoBIConstants.CONTAIN_FILTER%>' <% if (SpagoBIConstants.CONTAIN_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.contains" />
						</option>
						<option value='<%=SpagoBIConstants.EQUAL_FILTER%>' <% if (SpagoBIConstants.EQUAL_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.isEquals" />
						</option>
						<option value='<%=SpagoBIConstants.LESS_FILTER%>' <% if (SpagoBIConstants.LESS_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.isLessThan" />
						</option>
						<option value='<%=SpagoBIConstants.LESS_OR_EQUAL_FILTER%>' <% if (SpagoBIConstants.LESS_OR_EQUAL_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.isLessOrEqualThan" />
						</option>
						<option value='<%=SpagoBIConstants.GREATER_FILTER%>' <% if (SpagoBIConstants.GREATER_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.isGreaterThan" />
						</option>
						<option value='<%=SpagoBIConstants.GREATER_OR_EQUAL_FILTER%>' <% if (SpagoBIConstants.GREATER_OR_EQUAL_FILTER.equalsIgnoreCase(typeFilterSelected)) out.print(" selected=\"selected\""); %>>
							<spagobi:message key = "SBIListLookPage.isGreaterOrEqualThan" />
						</option>
					</select>
				</td>
				<td>
					<%
					String columnFilterSelected = "";
					if (correlatedObjParuse != null) columnFilterSelected = correlatedObjParuse.getFilterColumn();
					%>
					<select name='<%=SpagoBIConstants.COLUMN_FILTER + "_" + paruse.getUseID()%>' style='width:150px;'>
					<%
					IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
					ModalitiesValue lov = lovDAO.loadModalitiesValueByID(paruse.getIdLov());
					String lovProvider = lov.getLovProvider();
					if (lov.getITypeCd().equals("QUERY")) {
						QueryDetail queryDet = QueryDetail.fromXML(lovProvider);
						String visibleColumns = queryDet.getVisibleColumns();
					    String[] visColumns = visibleColumns.split(",");
					    for (int i = 0; i < visColumns.length; i++) {
					    	String visibleColumn = visColumns[i].trim();
					    	%>
					    	<option value='<%=visibleColumn%>' <% if (visibleColumn.equalsIgnoreCase(columnFilterSelected)) out.print(" selected=\"selected\""); %>>
					    		<%=visibleColumn%>
					    	</option>
					    	<%
					    }
						String invisibleColumns = queryDet.getInvisibleColumns();
						if (invisibleColumns != null) {
						    String[] invisColumns = invisibleColumns.split(",");
						    for (int i = 0; i < invisColumns.length; i++) {
						    	String invisibleColumn = invisColumns[i].trim();
						    	%>
						    	<option value='<%=invisibleColumn%>' <% if (invisibleColumn.equalsIgnoreCase(columnFilterSelected)) out.print(" selected=\"selected\""); %>>
						    		<%=invisibleColumn%>
						    	</option>
						    	<%
						    }
						}
					}
					%>
					</select>
				</td>
				<td>
					<%
					Integer objParFatherId = new Integer(-1);
					if (correlatedObjParuse != null) objParFatherId = correlatedObjParuse.getObjParFatherId();
					%>
					<select name='<%="OBJ_PAR_FATHER_ID" + "_" + paruse.getUseID()%>'>
						<%
					    for (int i = 0; i < otherObjParameters.size(); i++) {
					    	BIObjectParameter otherObjParameter = (BIObjectParameter) otherObjParameters.get(i);
					    	%>
					    	<option value='<%=otherObjParameter.getId()%>' <% if (otherObjParameter.getId().equals(objParFatherId)) out.print(" selected=\"selected\""); %>>
					    		<%=otherObjParameter.getLabel()%>
					    	</option>
					    	<%
					    }
						%>
					</select>
				</td>
			</tr>
			<%
		}
		%>
		</table>
		</div>
		<%
	}
%>
	</div>
</form>
