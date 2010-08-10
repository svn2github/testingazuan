<%@ include file="/jsp/portlet_base.jsp"%>

<%@ page import="it.eng.spagobi.bo.Check,
                 javax.portlet.PortletURL,
                 it.eng.spago.navigation.LightNavigationManager" %>
<%@ taglib uri="/WEB-INF/tlds/spagobi.tld" prefix="spagobi" %>
<%@ taglib uri="/WEB-INF/tlds/portlet.tld" prefix="portlet" %>
<portlet:defineObjects/>


<% 
    SourceBean moduleResponse = (SourceBean)aServiceResponse.getAttribute("DetailChecksModule"); 
	Check check = (Check)moduleResponse.getAttribute("checkObj");
	String modality = (String)moduleResponse.getAttribute("modality");
%>
<% 
   	PortletURL formUrl = renderResponse.createActionURL();
    formUrl.setParameter("PAGE", "DetailModalitiesChecksPage");
    formUrl.setParameter("MESSAGEDET", modality);
	formUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED, "true");
   	
   	PortletURL backUrl = renderResponse.createActionURL();
   	backUrl.setParameter("PAGE", "ListModalitiesChecksPage");
   	backUrl.setParameter(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
 %>



<!-- spagobi:error /-->

<!--table width='100%' cellspacing='0' border='0'>		
	<tr height='40'>
		<th align='center'> <spagobi:message key = "SBIDev.valConst.title" /></th>
	</tr>
</table-->

<style>
@IMPORT url("/spagobi/css/table.css");
</style>

<table class='header-table-portlet-section'>		
	<tr class='header-row-portlet-section'>
		<td class='header-title-column-portlet-section'>
			<spagobi:message key = "SBIDev.valConst.title" />
		</td>
		<td class='header-empty-column-portlet-section'>&nbsp;</td>
		<td class='header-button-column-portlet-section'>
			<a id="submit" href="javascript:document.getElementById('checkForm').submit()"> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.valConst.saveButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' alt='<spagobi:message key = "SBIDev.valConst.saveButt" />' /> 
			</a>
		</td>
		<td class='header-button-column-portlet-section'>
			<a href='<%= backUrl.toString() %>'> 
      			<img class='header-button-image-portlet-section' title='<spagobi:message key = "SBIDev.valConst.backButt" />' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='<spagobi:message key = "SBIDev.valConst.backButt" />' />
			</a>
		</td>
	</tr>
</table>

 
 <div id="checkDiv1" style='display:inline;'>
 <form method='POST' action='<%= formUrl.toString() %>' name='checkForm1' id='checkForm1'>
 <input type='hidden' value=<%=(check.getCheckId() != null ? String.valueOf(check.getCheckId().intValue()) : "-1")%> name='id' />

<table float='left' width="40%" cellspacing="0" border="0" >
  	<tr height='1'>  	
  		<td width="1px"><span>&nbsp;</span></td>
  		<td width="60px"><span>&nbsp;</span></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.labelField" /> </td>
      	<td>&nbsp;</td>
      	<td><input style='width:90%;' class='portlet-form-input-field' type="text" name="label" value="<%=check.getLabel()%>" maxlength="20">&nbsp;*</td>
    </tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.nameField" /> </td>
      	<td>&nbsp;</td>
      	<td><input style='width:90%;' class='portlet-form-input-field' type="text" name="name" value="<%=check.getName()%>" maxlength="40">&nbsp;*</td>
    </tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.descriptionField" /></td>
      	<td>&nbsp;</td>
      	<td><input style='width:90%;' class='portlet-form-input-field' type="text" name="description" value="<%=check.getDescription()%>" maxlength="160"></td>
    </tr>
    
 </table>
 
<div class='errors-object-details-div'>
	<spagobi:error/>
</div>
 
 <table clear='left' width="50%" cellspacing="0" border="0" >
 	<tr height='1'>  	
  		<td width="1px"><span>&nbsp;</span></td>
  		<td width="80px"><span>&nbsp;</span></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td width="4%"><span>&nbsp;</span></td>
  		<td width="30%"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      	<td>&nbsp;</td>
      	<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.ListConfConst.columnCheckType" /></td>
      	<td colspan="4">&nbsp;</td>
    </tr>   		
    <%  
		String actualValueCD = check.getValueTypeCd();
		java.util.List checksTypes = it.eng.spagobi.bo.dao.DAOFactory.getDomainDAO().loadListDomainsByType("CHECK"); 
		java.util.Iterator it = checksTypes.iterator();
		java.util.Iterator it1 = checksTypes.iterator();
		java.util.Iterator it2 = checksTypes.iterator();
		it.eng.spagobi.bo.Domain dom = null;
	
		String valueCD = null;
		String valueID = null;
		String fieldValue1Name = null;
		String fieldValue2Name = null;
		String valueForRaddio = null;	
		int numChecks = 0;
		while (it.hasNext()){
			dom = (it.eng.spagobi.bo.Domain)it.next();
			valueCD = dom.getValueCd();
			valueID = String.valueOf(dom.getValueId());
			valueForRaddio = valueID+";"+valueCD;
			fieldValue1Name = valueCD +"_value1";
			fieldValue2Name = valueCD +"_value2";
			String keyLabel = "";
			numChecks = numChecks +1;
			
			
     %>
  	
  	<tr height='25'>
  			<td>&nbsp;</td>
    	    <td align='right' class='portlet-form-field-label' ><%=dom.getValueName()%></td>
    		<td>&nbsp;</td>
    	    <td align='left'>
    			<input type="radio" name="checkType" value="<%=valueForRaddio%>" <%=(valueCD.equalsIgnoreCase(actualValueCD) ? "checked=checked" : "") %>/>
    		</td>
    	    <td align='left' class='portlet-form-field-label' nowrap="nowrap" >
    		     <% keyLabel = "SBIDev.valConst." + dom.getValueCd();  %>
    		     &nbsp;&nbsp;&nbsp;&nbsp;<spagobi:message key = "<%=keyLabel%>" />
    	    </td>
    	    <td align='left'>
    	        <% if (actualValueCD != null && valueCD.equalsIgnoreCase(actualValueCD) && (check != null)){ %>
    		     	<input style='width:90%;' class='portlet-form-input-field' type="text" name="<%=fieldValue1Name %>" value="<%=(check.getFirstValue() != null ? check.getFirstValue()  : "") %>" maxlength=50/>
    		     <% } else { %>
    		        <input style='width:90%;' class='portlet-form-input-field' type="text" name="<%=fieldValue1Name %>" value="" maxlength=50/>
    		     <% } %>
    	    </td>
    </tr>	
    <%if (dom.getValueName().equalsIgnoreCase("Range")){ %>
    <tr height = '25'>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
    	<td align='left' class='portlet-form-field-label'>
    		<%keyLabel = "SBIDev.valConst." + dom.getValueCd()+ ".Value2"; %>
    		&nbsp;&nbsp;&nbsp;&nbsp;<spagobi:message key = "<%=keyLabel%>" />
    	</td>
    	<td align='left'>
    		<% if (actualValueCD != null && valueCD.equalsIgnoreCase(actualValueCD) && (check != null)){ %>
    		<input style='width:90%;' class='portlet-form-input-field' type="text" name="<%=fieldValue2Name %>" value="<%=(check.getSecondValue() != null ? check.getSecondValue()  : "") %>" maxlength=50/>
    	    <% } else { %>
    	    <input style='width:90%;' class='portlet-form-input-field' type="text" name="<%=fieldValue2Name %>" value="" maxlength=50/>
    	    <% } %>
    	</td>
    </tr>
    <% } %>
    
    <%	} // end while  %>
    
    
    
    
    
    <%--tr height='30px'>
    	<td colspan='6'>&nbsp;</td>
    </tr>
    <tr>
  	    <td colspan='3'>&nbsp;</td>
   	 	<td colspan='3'>
   	 	 	<table>
  				<tr>
   	 				<td align='center'>
   	 	    			<input type='image' src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/save.png")%>' name='save' alt='save'/>
					</td>
					</form>
					<td width='50px'>&nbsp;</td>
					<td align='center'>
 	    				<a href='<%= formUrl1.toString() %>' class='portlet-menu-item' >
							<img src='<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/img/back.png")%>' alt='Back' />
						</a> 
					</td>
    			</tr>
    			<tr>
   	 				<td align='center'>
   	 					<a href="javascript:document.getElementById('checkForm').submit()" >
   	 	     				<spagobi:message key = "SBIDev.valConst.saveButt" />
   	 	    			</a> 
					</td>
					<td width='50px'>&nbsp;</td>
					<td align='center'>
 	    				<a href='<%= formUrl1.toString() %>' >
							<spagobi:message key = "SBIDev.valConst.backButt" /> 
						</a> 
					</td>
    			</tr>
			</table>
		</td>
    </tr--%>
  
</table>
</form>
</div>
<div id="checkDiv2" style='display:none;'>
<form method='POST' action='<%= formUrl.toString() %>' name='checkForm2' id='checkForm2'>
<input type='hidden' value=<%=(check.getCheckId() != null ? String.valueOf(check.getCheckId().intValue()) : "")%> name='id' />

<div class="object-details-div">
	<table class="object-details-table">
  		<tr height='25'>
      			<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.labelField" /> </td>
      			<td>&nbsp;</td>
      			<td><input class='portlet-form-input-field' type="text" name="label" value="<%=check.getLabel()%>" size='50' maxlength="20">&nbsp;*</td>
   		</tr>
  		<tr height='25'>
      			<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.nameField" /> </td>
      			<td>&nbsp;</td>
      			<td><input class='portlet-form-input-field' type="text" name="name" value="<%=check.getName()%>" size='50' maxlength="40">&nbsp;*</td>
   		 </tr>
  		<tr height='25'>
      			<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.valConst.descriptionField" /></td>
      			<td>&nbsp;</td>
      			<td><input class='portlet-form-input-field' type="text" name="description" value="<%=check.getDescription()%>" size='50' maxlength="160"></td>
    		</tr>
	</table>
</div>

<div class='errors-object-details-div'>
	<spagobi:error/>
</div>
 
 
<div class="object-details-div" style='clear:left'>

<table width="50%" cellspacing="0" border="0" clear='left'>
 	<tr height='1'>  	
  		<td width="1px"><span>&nbsp;</span></td>
  		<td width="80px"><span>&nbsp;</s></td>
  		<td width="20px"><span>&nbsp;</span></td>
  		<td width="4%"><span>&nbsp;</span></td>
  		<td width="30%"><span>&nbsp;</span></td>
  		<td><span>&nbsp;</span></td>
  	</tr>
  	<tr height='25'>
      		<td>&nbsp;</td>
      		<td align='right' class='portlet-form-field-label' ><spagobi:message key = "SBIDev.ListConfConst.columnCheckType" /></td>
      		<td colspan="4">&nbsp;</td>
   	</tr> 
</table>  		
    
   
      
    
    <%  
		actualValueCD = check.getValueTypeCd();
		// checksTypes = it.eng.spagobi.bo.dao.DAOFactory.getDomainDAO().loadListDomainsByType("CHECK"); 
		it = checksTypes.iterator();
	    dom = null;
	    
	
		valueCD = null;
		valueID = null;
		fieldValue1Name = null;
		fieldValue2Name = null;
		valueForRaddio = null;	
		int currCheck = 0;
		String divId = null;
		

	   
		%>
		 
		 <select class='portlet-form-field' name="checkType" onchange= "showFields()" >
		<%  while (it.hasNext()){
			dom = (it.eng.spagobi.bo.Domain)it.next();
			valueCD = dom.getValueCd();
			valueID = String.valueOf(dom.getValueId());
			valueForRaddio = valueID+";"+valueCD;
			fieldValue1Name = valueCD +"_value1";
			fieldValue2Name = valueCD +"_value2";
			//boolean isFirst = false;
			//if((currCheck == 0)&&(modality.equals("DETAIL_INS"))){
			//isFirst = true;
			//}
		
		
		 %>     
  	                
    				<%--option value="<%= valueForRaddio  %>" <%if(valueCD.equalsIgnoreCase(actualValueCD)||isFirst) out.print(" selected='selected' ");%>><%=dom.getValueName()%></option--%>
    				<option value="<%= valueForRaddio  %>" <%if(valueCD.equalsIgnoreCase(actualValueCD)) out.print(" selected='selected' ");%>><%=dom.getValueName()%></option>
    			    <%currCheck = currCheck +1; } // chiusura del ciclo while per il combo%>
    			     </select>  
    			     <br/>
    			     <br/>
    			       
    			       <%it1 = checksTypes.iterator();
	                     dom = null; 
	                     currCheck = 0;
    			         
    			        while (it1.hasNext()){
    			       
    			       dom = (it.eng.spagobi.bo.Domain)it1.next();
			           valueCD = dom.getValueCd();
			           valueID = String.valueOf(dom.getValueId());
			           valueForRaddio = valueID+";"+valueCD;
			           fieldValue1Name = valueCD +"_value1";
			           fieldValue2Name = valueCD +"_value2";
    			       
    			       String keyLabel = "";
			           //currCheck = currCheck +1;
			           String currDiv = String.valueOf(currCheck); 
		               divId  = currDiv ;
	                   String style = null; 
	                   //boolean isFirst = false;
			           //if((currCheck == 0)&&(modality.equals("DETAIL_INS"))){
			           //isFirst = true;
			           //}
	                   if(valueCD.equalsIgnoreCase(actualValueCD)) {
	                   style = "display:inline";}
	                   	else {
	                   	style = "display:none";
	                   	} %>
    				
    				   
    				   <div id="<%= valueForRaddio %>" style= '<%=style%>'>
    		     			<span class='portlet-form-field-label'><% keyLabel = "SBIDev.valConst." + dom.getValueCd();  %>
    		     			&nbsp;&nbsp;&nbsp;&nbsp;<spagobi:message key = "<%=keyLabel%>" /></span>
    	         		     <% if (actualValueCD != null && valueCD.equalsIgnoreCase(actualValueCD) && (check != null)){ %>
    		     			<input style='width:30%;' class='portlet-form-input-field' type="text" name="<%=fieldValue1Name %>"
						value="<%=(check.getFirstValue() != null ? check.getFirstValue()  : "") %>" size='50'  maxlength=50/>
    		     			<% } else { %>
    		        		<input style='width:30%;' class='portlet-form-input-field' type="text" name="<%=fieldValue1Name %>"
						value="" size='50' maxlength=50/>
    		     			<% } %>
    		     	   
    	    
    	    <%if (dom.getValueName().equalsIgnoreCase("Range")){ %>
    		<br/>
    		<br/>
    		
    		<span class='portlet-form-field-label'><%keyLabel = "SBIDev.valConst." + dom.getValueCd()+ ".Value2"; %>
    		&nbsp;&nbsp;&nbsp;&nbsp;<spagobi:message key = "<%=keyLabel%>" /></span>
    		
    		<% if (actualValueCD != null && valueCD.equalsIgnoreCase(actualValueCD) && (check != null)){ %>
    		<input style='width:30%;' class='portlet-form-input-field' type="text" name="<%=fieldValue2Name %>"
			value="<%=(check.getSecondValue() != null ? check.getSecondValue()  : "") %>" size='50'  maxlength=50/>
    	    <% } else { %>
    	    <input style='width:30%;' class='portlet-form-input-field' type="text" name="<%=fieldValue2Name %>" value="" size='50'  maxlength=50/>
    	    <% } %>
    	    
     <% } %>
     
    </div>
    <%	currCheck = currCheck + 1;
    } // end while  %>
    
</form>
</div>
</div>



<script type="text/javascript">
function change(container, container1) {
 	var cont = document.getElementById(container);
 	if(cont.style.display=='none'){
 	cont.style.display = 'inline';
 	} else {
 	cont.style.display = 'none';
 	}
    var cont1 = document.getElementById(container1);
 	if(cont1.style.display=='none'){
 	cont1.style.display = 'inline';
 	} else {
 	cont1.style.display = 'none';
 	}
}

function showFields(){

var cont = document.checkForm2.checkType.selectedIndex;
var cont1 = document.checkForm2.checkType[cont].value;
var selectDiv = document.getElementById(cont1);
selectDiv.style.display = 'inline';

var lenArray = document.checkForm2.checkType.options.length;



for(var i=0; i<lenArray;i++) {
var currIndex = document.checkForm2.checkType[i].value;
var currDiv = document.getElementById(currIndex);
if(currDiv != selectDiv) {
currDiv.style.display = 'none';
}

}   

}
function setForm(){
var currDiv = document.getElementById('checkDiv1');
var submit =  document.getElementById('submit');

if(currDiv.style.display == 'inline'){
var href = "javascript:document.getElementById('checkForm1').submit()";

submit.href = href;
}
else{
var  href1 = "javascript:document.getElementById('checkForm2').submit()";

submit.href = href1;
}
}

</script>

 <script>
 change ('checkDiv1', 'checkDiv2');
 setForm();
 </script>
