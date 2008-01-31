

<%@page import="it.eng.qbe.model.*"%>
<%@page import="it.eng.qbe.wizard.*"%>
<%@page import="it.eng.qbe.query.*"%>

<%@page import="it.eng.spagobi.qbe.commons.constants.*"%>
<%@page import="it.eng.spago.configuration.*"%>
<%@ page import="it.eng.spago.base.*"%>

<%@ page import="it.eng.qbe.utility.*"%>
<%@ page import="it.eng.qbe.locale.*"%>
<%@ page import="it.eng.qbe.log.*"%>
<%@ page import="it.eng.qbe.model.io.*"%>
<%@ page import="it.eng.qbe.urlgenerator.*"%>
<%@page import="it.eng.qbe.conf.*"%>

<%@ page import="java.util.*"%>


<%@ taglib uri="/WEB-INF/tlds/commons/qctl.tld" prefix="qbe" %>
<%@ taglib uri="/WEB-INF/tlds/jstl-1.1.2/c.tld" prefix="c" %>

<qbe:url type="resource" var="qbeCommonsScripts" ref="../js/commons/qbe_commons.js"/>
<script type="text/javascript" src='${qbeCommonsScripts}'/></script>

<%  
	RequestContainer requestContainer = null;
	ResponseContainer responseContainer = null;
	SessionContainer sessionContainer = null;
	
	SourceBean aServiceRequest = null; // deprecated
	SourceBean serviceRequest = null;
	
	SourceBean aServiceResponse = null; // deprecated
	SourceBean serviceResponse = null;
	
	DataMartModel datamartModel = null;
	ISingleDataMartWizardObject datamartWizard = null;
	IQuery query = null;
	
	IQbeUrlGenerator qbeUrl = null;
	
	String bundle = null;
	IQbeMessageHelper qbeMsg = null;
	
	Map functionalities = null;
	
	
	
	
	requestContainer = QbeEngineConf.getInstance().isWebModalityActive()? 
					   RequestContainerAccess.getRequestContainer(request):
					   RequestContainerPortletAccess.getRequestContainer(request);
						
	responseContainer = QbeEngineConf.getInstance().isWebModalityActive()? 
						ResponseContainerAccess.getResponseContainer(request):
						ResponseContainerPortletAccess.getResponseContainer(request);
						
	sessionContainer = requestContainer.getSessionContainer();
	
	aServiceRequest = requestContainer.getServiceRequest();
	serviceRequest = aServiceRequest;
	
	aServiceResponse = responseContainer.getServiceResponse();
	serviceResponse = aServiceResponse;
	
		
	datamartModel = (DataMartModel)sessionContainer.getAttribute(QbeConstants.DATAMART_MODEL); 
	datamartWizard = (ISingleDataMartWizardObject)sessionContainer.getAttribute(QbeConstants.DATAMART_WIZARD);
	query = (datamartWizard != null)? datamartWizard.getQuery(): null;
	
	qbeUrl = QbeEngineConf.getInstance().isWebModalityActive()? 
			 new WebQbeUrlGenerator():
			 new PortletQbeUrlGenerator();
			 
	bundle = QbeEngineConf.getInstance().getBundle();
	qbeMsg =  QbeEngineConf.getInstance().getQbeMessageHelper();
	
	
	functionalities = (Map)sessionContainer.getAttribute("FUNCTIONALITIES");
%>
   




	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/spagobi.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/jsr168.css")%>" type="text/css"/>
	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/external.css")%>" type="text/css"/>
	<link rel="styleSheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../css/dtree.css")%>" type="text/css" />	
	
	<%-- Necessary for window Javascript library --%>
	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/prototype.js")%>'></script>
  	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/effects.js")%>'></script>
  	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/window.js")%>'></script>
  	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/debug.js")%>'></script>
  	<script type="text/javascript" src='<%=qbeUrl.conformStaticResourceLink(request,"../js/application.js")%>'/></script>
  	
   
  	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../themes/default.css")%>" type="text/css"/>	 
  	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../themes/alert.css")%>" type="text/css"/>	 
  	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../themes/alert_lite.css")%>" type="text/css" />
  	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../themes/mac_os_x.css")%>" type="text/css"/>	
  	<link rel="stylesheet" href ="<%=qbeUrl.conformStaticResourceLink(request,"../themes/debug.css")%>" type="text/css"/>	 
  
    
	
	<script>
		document.onselectstart = function() { return true; }
	</script>
	
<%  if ( QbeEngineConf.getInstance().isWebModalityActive() ) { %>
		<script language=JavaScript>
			var currentSubQueryFieldId = '';
			var errMsg = null;
			
			
			function exitSubQueryMode(){
				var url = '../servlet/AdapterHTTP';
				var pars = 'ACTION_NAME=EXIT_SUBQUERY_ACTION&SAVE=' + saveSubQuery;
		
				
				
								
				var myAjax = new Ajax.Request( url, 
												   { method: 'post', 
												     parameters: pars, 
												     onComplete: showResponse });


			}

			function showResponse(originalRequest) {
				var valueForFieldOfSubqueryId = 'VALUE_FOR_FIELD_'+ currentSubQueryFieldId;
				
				
				if(saveSubQuery == true) {					
					$(valueForFieldOfSubqueryId).value='$subquery_'+currentSubQueryFieldId+'$';
				}
				
				
				if(errMsg != null && errMsg.length > 0) {
					$(valueForFieldOfSubqueryId).title = errMsg;
					$(valueForFieldOfSubqueryId).style.backgroundColor = '#FF6666';					
				} else {
					$(valueForFieldOfSubqueryId).style.backgroundColor = '#FFFFFF';
					$(valueForFieldOfSubqueryId).title = '';
				}
				
				return;
			}
			
		 	
			function ajxCreateViewFromCurrentQuery(){
				
				var mViewName = prompt("Create View", "View Name");
				 
				if ((mViewName != null) && ( Trim(mViewName) != '')) {
					var url = '../servlet/AdapterHTTP';
					var pars = 'ACTION_NAME=CREATE_VIEW_ACTION&VIEW_NAME='+mViewName;
		
					var myAjax = new Ajax.Request( url, 
												   { method: 'post', 
												     parameters: pars, 
												     onComplete: ajxCreateViewFromCurrentQueryCallBack });
				}else{
					alert(" Specify a View ID");
					return;
				}
			}
			
			
			
			function ajxCreateViewFromCurrentQueryCallBack(originalRequest){
				var ajxResponse = originalRequest.responseText;
				if (ajxResponse == 'OK'){
					alert(' Created View ');
				}else{
					alert(ajxResponse);
				}
			}
			
			// Persist action and submit formExport
			function ajxPersistTemporaryQueryAction(){
				
				 
				
				var url = '../servlet/AdapterHTTP';
				var pars = 'ACTION_NAME=PERSIST_TEMPORARY_QUERY_ACTION';
		
				var myAjax = new Ajax.Request( url, 
												   { method: 'post', 
												     parameters: pars, 
												     onComplete: ajxPersistTemporaryQueryActionCallBack });
			}
			 
			function ajxPersistTemporaryQueryActionCallBack(originalRequest){
				
			
				//alert("responseText ["+ originalRequest.responseText +"]");
				
				$('_savedObjectId').value=originalRequest.responseText;
				$('formExport').submit();
				 
			}
			// end  Persist action and submit formExport
			
			
			function ajxPersistTemporaryQueryActionAndSubmitExportIFrame(){
				
				//alert("Persist temporary this query ");
				 
				
				var url = '../servlet/AdapterHTTP';
				var pars = 'ACTION_NAME=PERSIST_TEMPORARY_QUERY_ACTION';
		
				var myAjax = new Ajax.Request( url, 
												   { method: 'post', 
												     parameters: pars, 
												     onComplete: ajxPersistTemporaryQueryActionAndSubmitExportIFrameCallBack });
			}
			 
			function ajxPersistTemporaryQueryActionAndSubmitExportIFrameCallBack(originalRequest){
				
			
				//alert("responseText ["+ originalRequest.responseText +"]");
				
				$('_savedObjectIdIframe').value=originalRequest.responseText;
				$('formExportIframe').submit();
				 
			}
			
			
			
			
		
		</script>	
<%  } %>







<%  if ( !QbeEngineConf.getInstance().isWebModalityActive() ) { %>	
	<SCRIPT language=JavaScript>
			function dTree(objName) {
				this.config = {
					target			: null,
					folderLinks		: true,
					useSelection	: true,
					useCookies		: true,
					useLines		: true,
					useIcons		: true,
					useStatusText	: true,
					closeSameLevel	: false,
					inOrder			: false
				}
				this.icon = {
					root		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treebase.gif")%>',
					folder		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treefolder.gif")%>',
					folderOpen	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treefolderopen.gif")%>',
					node		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treepage.gif")%>',
					empty		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeempty.gif")%>',
					line		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeline.gif")%>',
					join		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treejoin.gif")%>',
					joinBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treejoinbottom.gif")%>',
					plus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeplus.gif")%>',
					plusBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeplusbottom.gif")%>',
					minus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeminus.gif")%>',
					minusBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeminusbottom.gif")%>',
					nlPlus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treenolines_plus.gif")%>',
					nlMinus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treenolines_minus.gif")%>'
				};
				this.obj = objName;
				this.aNodes = [];
				this.aIndent = [];
				this.root = new Node(-1);
				this.selectedNode = null;
				this.selectedFound = false;
				this.completed = false;
			};
    </SCRIPT>
<%  }%>





	<script language='javascript' src='<%=qbeUrl.conformStaticResourceLink(request,"../js/dtree.js")%>'></script>
	<script type='text/javascript'>
				
		selField = "";

		// whitespace characters
		var whitespace = " \t\n\r";		
		// Removes initial and final whitespace characters from s.
		// Global variable whitespace (see above)
		// defines which characters are considered whitespace.

	function isDisab(classname) {
		if (classname==null) return false;
		if (classname.substring(classname.length-5)=='DISAB') return true;
		return false;
	}

	function isOver(classname) {
		if (classname==null) return false;
		if (classname.substring(classname.length-4)=='OVER') 	return true;
		return false;
	}


	function goIn(obj_td) {
		if (obj_td == null) return;
		var class_name = obj_td.className;
		if (!isOver(class_name) && !isDisab(class_name)) obj_td.className = class_name+'OVER';
	}


	function goOut(obj_td) {
		if (obj_td==null) return;
		var class_name = obj_td.className;
		if (isOver(class_name)) obj_td.className = class_name.substring(0,class_name.length-4);
	}

	function stripInitialFinalWhitespace (s){ //posizione primo carattere dall'inizio diverso da blank    
	    if (s == null || s == "") return s;
    
    	var i = 0;
	    while ((i < s.length) && (whitespace.indexOf(s.charAt(i)) != -1))
    	   i++;

	    //posizione primo carattere dalla fine diverso da blank
    	var f = s.length-1;
	    while ((f > i) && (whitespace.indexOf(s.charAt(i)) != -1))
    	   f--;
	
	    return s.substring (i, f+1);
	}

	function Trim(s) {
    	return stripInitialFinalWhitespace (s);	 
	}	


	
		
	function handleDistinct(){
		var checkBoxDst = document.getElementById('checkboxDistinct');
		var inputSelectDistinct = document.getElementById('selectDistinct');
		
		inputSelectDistinct.value = checkBoxDst.checked;
		var formUpdSelect = document.getElementById("formUpdateSelect");
		formUpdSelect.submit();
			
	}
		
		
		
	function selectFieldForConditionCallBack(actionName, completeFieldName, className, hibType){
			
			//alert("Select Field For condition CallBack");
			//alert("actionName,"+actionName);
			//alert("completeFieldName,"+ completeFieldName); 
			//alert("className,"+ className); 
			//alert("hibType" + hibType);
			//alert (" --> OK");
			//alert("1");
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			//alert("1");
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL';
		  	
		  	var inputSCompleteName = document.getElementById("S_COMPLETE_FIELD_NAME");
		  	inputSCompleteName.value = completeFieldName;	
		  	
		  	var inputSClassName = document.getElementById("S_CLASS_NAME");	
		  	inputSClassName.value = className;
		  	
		  	var inputSHibType = document.getElementById("S_HIB_TYPE");
			inputSHibType.value = hibType;				
			
			var inputNextActionAfterSaveCondition1 = document.getElementById('nextActionAfterSaveCondition');
			var inputNextPublisherAfterSaveCondition1 = document.getElementById('nextPublisherAfterSaveCondition');
						
			inputNextActionAfterSaveCondition1.value='PUBLISH_ACTION';
			inputNextPublisherAfterSaveCondition1.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';						
			
			//alert("3");
			//alert(formUpdCond1);
			formUpdCond1.submit();
			return true;
		}
		
		var valueForFieldId = '';
		var fieldId = '';
		
		function selectFieldForJoinCallBack(actionName, completeFieldName, className, hibType){
			
			//alert("selectFieldForJoinCallBack"); 
			
			var valueForField = document.getElementById(valueForFieldId);
					 
			valueForField.value = completeFieldName;
			
			div = document.getElementById("divTreeSelectJoin");
			div.style.display = 'none';
			
			// devo aggiungere la chiamata ad un action che mi vada ad aggiungere la classe al where
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			//alert("1");
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL_RIGHT';
		  	
		  	var inputSCompleteName = document.getElementById("S_COMPLETE_FIELD_NAME");
		  	inputSCompleteName.value = completeFieldName;	
		  	
		  	var inputSClassName = document.getElementById("S_CLASS_NAME");	
		  	inputSClassName.value = className;
		  	
		  	var inputSHibType = document.getElementById("S_HIB_TYPE");
			inputSHibType.value = hibType;				
			
			var inputFieldID = document.getElementById("Parameter");
			inputFieldID.name = 'FIELDID';
			inputFieldID.value = fieldId;
			
			//alert("Field ID 2"+fieldId);
							
					
			
			var inputNextActionAfterSaveCondition1 = document.getElementById('nextActionAfterSaveCondition');
			var inputNextPublisherAfterSaveCondition1 = document.getElementById('nextPublisherAfterSaveCondition');
						
			inputNextActionAfterSaveCondition1.value='PUBLISH_ACTION';
			inputNextPublisherAfterSaveCondition1.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';						
			
		
			formUpdCond1.submit();
			return true;			
		}
		
		
		function selectFieldForJoinWithParentCallBack(actionName, completeFieldName, className, hibType){
			
			
			var valueForField = document.getElementById(valueForFieldId);
					 
			valueForField.value = completeFieldName;
			
			div = document.getElementById("divTreeSelectJoin");
			div.style.display = 'none';
			
			
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			var fUpdCondJoinParentInput = document.getElementById('fUpdCondJoinParent');
			fUpdCondJoinParentInput.value='TRUE';
			
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL_RIGHT';
		  	
		  	var inputSCompleteName = document.getElementById("S_COMPLETE_FIELD_NAME");
		  	inputSCompleteName.value = completeFieldName;	
		  	
		  	var inputSClassName = document.getElementById("S_CLASS_NAME");	
		  	inputSClassName.value = className;
		  	
		  	var inputSHibType = document.getElementById("S_HIB_TYPE");
			inputSHibType.value = hibType;				
			
			var inputFieldID = document.getElementById("Parameter");
			inputFieldID.name = 'FIELDID';
			inputFieldID.value = fieldId;
			
			//alert("Field ID 2"+fieldId);
							
					
			
			var inputNextActionAfterSaveCondition1 = document.getElementById('nextActionAfterSaveCondition');
			var inputNextPublisherAfterSaveCondition1 = document.getElementById('nextPublisherAfterSaveCondition');
						
			inputNextActionAfterSaveCondition1.value='PUBLISH_ACTION';
			inputNextPublisherAfterSaveCondition1.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';						
			
		
			formUpdCond1.submit();
			return true;
			
		}
		
		function openDivTreeSelectJoin(pFieldId, tvalueForFieldfieldId, event){
			//alert("Field ID 1"+pFieldId);
			
			valueForFieldId = tvalueForFieldfieldId;
			fieldId = pFieldId;
						
			div = document.getElementById("divTreeSelectJoin");
			
			var isVis = div.style.display;
			
			//alert("isVisible --> "+isVis);
			
			if (isVis=='inline') div.style.display = 'none';
			else div.style.display = 'inline';
			
			div.style.top = event.clientY + 20 + 'px';
			div.style.left = event.clientX - 200 + 'px';
			
			
		}
		
		function openDivTreeSelectJoinParentQuery(pFieldId, tvalueForFieldfieldId, event){
			//alert("openDivTreeSelectJoinParentQuery   Field ID " + pFieldId);
			
			valueForFieldId = tvalueForFieldfieldId;
			fieldId = pFieldId;
						
			div2 = document.getElementById("divTreeSelectJoinParentQuery");
			//alert(" div2 " + div2);
			//alert(" div2 html " + div2.innerHTML);
			var isVis2 = div2.style.display;
			
			//alert("isVisible --> "+isVis2);
			
			if (isVis2=='inline') div2.style.display = 'none';
			else div2.style.display = 'inline';
					
			div2.style.top = event.clientY + 20 + 'px';
			div2.style.left = event.clientX - 200 + 'px';
			
			
		}
		
		
		
	
		
		
		function submitFormUpdateSelect() {
			//alert( 'formUpdateSelect Inizio');
			var formUpdateSelectF = document.getElementById('formUpdateSelect');
			//alert( 'formUpdateSelect ' + formUpdateSelectF);
			formUpdateSelectF.submit();
			
		}
		
		
		
		
		
		function openDivExpertSelect(event){
			
			div = document.getElementById("divExpertSelect");
			
			//alert("event.clientX " + event.clientX);
			//alert("event.clientY " + event.clientY);
			
			div.style.display = 'block';
			
			div.style.top = event.clientY + 20 +  'px';
			
			div.style.left = event.clientX - 20 + 'px';
			
			return true;
		}
		
		function openDivExpertWhere(event){
			div = document.getElementById("divExpertWhere");
			
			//alert("event.clientX " + event.clientX);
			//alert("event.clientY " + event.clientY);
			
			div.style.display = 'block';
			div.style.top = event.clientY;
			div.style.left = event.clientX;
			selField = fieldNameForForm;
			return true;
		}
		
	
		function resumeFromQbe(){
		
			var inputExpertFormActionName = document.getElementById('expertFormActionName');
			
			inputExpertFormActionName.value = 'ALIGN_EXPERT_ACTION';
			
			var expertFormF = document.getElementById('expertForm');
			
			expertFormF.submit();
			
			return;
		}
		
		
		
		function saveExpertSelect(){
		
			
			var expertSelectTextArea = document.getElementById('expertSelectTextArea');
			var inputTA = document.getElementById('expertSelectTA');
			inputTA.value = expertSelectTextArea.value;
			var expertFormF = document.getElementById('expertForm');
			
			/* Chiudeva la div della expert query (da cancellare)
			var div2 = document.getElementById("divExpertSelect");
			div2.style.display = 'none';*/
				
			//alert("ExpertForm");
			expertFormF.submit();
			return;
			
		}
		
		function submitUpdatePreview(source){

			var expertSelectTextArea = document.getElementById('expertSelectTextArea');
			var inputTA = document.getElementById('formUpdateExpert_expertTA');
			inputTA.value = expertSelectTextArea.value;

			var formUpdateExpert_Source = document.getElementById('formUpdateExpert_Source');
			formUpdateExpert_Source.value = source;	

			var formUpdateExpertF = document.getElementById('formUpdateExpert');
			formUpdateExpertF.submit();
				
		}
		
		
		function submitUpdatePreviewFromQueryResult(){
			
			var formUpdateExpertF = document.getElementById('formUpdateExpertMode');
			formUpdateExpertF.submit();
						
		}
		
		
		
		
		
		
		function checkSavingBeforeBack(destUrl, currentQueryId, startTimeStamp, lastUpdateTimeStamp){
			
			//alert(destUrl);
			//alert(currentQueryId);
			//alert(startTimeStamp);
			//alert(lastUpdateTimeStamp);
			
			var msg = '<%=qbeMsg.getMessage(requestContainer, "QBE.QueryNotSavedContinue", bundle).trim() %>';
					
		
			if ((Trim(startTimeStamp) != '') && (Trim(lastUpdateTimeStamp) != '')){
			
				  if ( (Trim(startTimeStamp) != Trim(lastUpdateTimeStamp))){
				     var exitWithoutSave2 = confirm(msg);
				     if (exitWithoutSave2 == true)
						document.location = destUrl;
					else
						return;
				  } else{
				  		document.location = destUrl;
				  }
			}
			else{
			
				document.location = destUrl;
				
			}
			
		}
		
		
		function showQueryInQueryResult(event, type) {
		
			var showQueryDiv = document.getElementById(type);
			var qbeQueryDiv = document.getElementById('divQbeQuery');
			var expertQueryDiv = document.getElementById('divExpertQuery');

			var isVis = showQueryDiv.style.display;
			
			qbeQueryDiv.style.display = 'none';
			expertQueryDiv.style.display = 'none';
				
			if (isVis=='inline') showQueryDiv.style.display = 'none';
			else showQueryDiv.style.display = 'inline';		
			
			showQueryDiv.style.top = event.clientY + 10 + 'px';
			showQueryDiv.style.left = event.clientX + 10 + 'px';			
		}
		
		
		
		
				
		function checkFormPersistQueryAndSubmit(){
		
			var frmPersistQuery = document.getElementById('formPersistQuery');
			
			var inputPreviousQueryId = document.getElementById('previousQueryId');
			
			var inputQueryId = document.getElementById('queryId');
			
			
			queryIdValue = Trim(inputQueryId.value);
			inputPreviousQueryIdValue = Trim(inputPreviousQueryId.value);
			
			if (queryIdValue == ''){
				alert('<%=qbeMsg.getMessage(requestContainer, "QBE.QueryIdIsMandatory", bundle).trim() %>');
			}else{
				if (queryIdValue == inputPreviousQueryIdValue){
					var confirmOverWrite = confirm('<%=qbeMsg.getMessage(requestContainer, "QBE.ConfirmOverWrite", bundle).trim() %>');
					if (confirmOverWrite == true){
						frmPersistQuery.submit();
					}else{
						return true;
					}	
				}else{
					frmPersistQuery.submit();
				}
			}
			
		}
					
			
			
			
			
			
			
			
			
			
			
		var alertOnClose = { onClose: function(eventName, win) { exitSubQueryMode();} }
		Windows.addObserver(alertOnClose);
		var saveSubQuery;
		
			
    	function showSubqueryWin(fieldID){
    		   
    		currentSubQueryFieldId = fieldID;
    		saveSubQuery = false;
    		var urlSubQuery = "../servlet/AdapterHTTP?ACTION_NAME=START_WIZARD_SUBQUERY_ACTION&ON_FIELD_ID="+fieldID;
      		var winz = new Window(Application.getNewId(), {className: "dialog", title: "-- Subquery on " + currentSubQueryFieldId + "--", 
                                              top:70, left:100, width:850, height:400, 
                                              resizable: true, url: urlSubQuery })
			winz.setDestroyOnClose();
			
				
			winz.show(true); 						
    	}	
			
	
		
		
		
		
	</script>
	
		

<% if ( QbeEngineConf.getInstance().isWebModalityActive() ){ %> 
</head>
<%} %>
  
  
  <% 
     if( !QbeEngineConf.getInstance().isWebModalityActive() ){
        String actor = (String)aServiceRequest.getAttribute("ACTOR");
        if(actor!=null) {
        	sessionContainer.setAttribute("ACTOR", actor);
         }
     }
    
   %>
   		
		
		



