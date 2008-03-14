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

<%@ include file="/jsp/commons/portlet_base.jsp"%>


<STYLE>
	
	.div_form_container {
    	border: 1px solid #cccccc;
    	background-color:#fafafa;
    	float: left;
    	margin: 5px;
    	font-size: 10pt;
		font-weight: normal;
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
	
	.div_form_label_large {	
		float: left;
		width:300px;
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

<div class="div_form_container">
	<div class="div_form_margin" >
		<div class="div_form_row" >
			<div class='div_form_label'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.docConf.viewPoint.name" />
				</span>
			</div>
			<div class='div_form_field'>
				<input class='portlet-form-input-field' type="text" style='width:230px;' 
						name="nameVP" id="nameVP" value="" maxlength="20"/>
			</div>
		</div>
		<div class="div_form_row" >
			<div class='div_form_label'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.docConf.viewPoint.description" />
				</span>
			</div>
			<div class='div_form_field'>
				<input class='portlet-form-input-field' type="text" style='width:230px;' 
					name="descVP" id="descVP" value="" maxlength="160"/>
			</div>
		</div>
		<div class="div_form_row" >
			<div class='div_form_label'>
				<span class='portlet-form-field-label'>
					<spagobi:message key = "SBIDev.docConf.viewPoint.scope" />
				</span>
			</div>
			<div class='div_form_field'>
				<select id="scopeVP" name="scopeVP" >
					<option value=""/>
					<option value="Public"  /><spagobi:message key = "SBIDev.docConf.viewPoint.scopePublic" />
					<option value="Private" /><spagobi:message key = "SBIDev.docConf.viewPoint.scopePrivate" />
				</select>
			</div>
		</div>
	</div>
</div>

<div>
	<div>
		<a href="javascript:saveViewpoint(document.getElementById('nameVP'),document.getElementById('descVP'),document.getElementById('scopeVP'))">
			<img width="20px" height="20px" 
	  	   		src='<%=urlBuilder.getResourceLink(request, "/img/save.png")%>' 
	  	        name='saveViewPoint' 
	  	        alt='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", "messages", request)%>' 
	            title='<%=msgBuilder.getMessage("SBIDev.docConf.viewPoint.saveButt", "messages", request)%>' />
		</a>
 	</div>
</div>

<%@ include file="/jsp/commons/footer.jsp"%>