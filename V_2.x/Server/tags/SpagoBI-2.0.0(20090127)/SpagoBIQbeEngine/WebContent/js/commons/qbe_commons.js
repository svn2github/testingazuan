function getCurrentPageName() {
	var currentPageName = null;
	var currentPageNameElement = document.getElementById('currentScreen');
		
	if (currentPageNameElement != null){
		 if (currentPageNameElement.childNodes[0] != null){
		  	currentPageName = currentPageNameElement.childNodes[0].nodeValue;
		 }
	}
	return currentPageName;
}


function updateConditions(divTxt) {
	var currentScreenTxt = getCurrentPageName();
				
	var formUpdCond = document.getElementById('formUpdateConditions');
									
	var inputNextActionAfterSaveCondition = document.getElementById('nextActionAfterSaveCondition');
	var inputNextPublisherAfterSaveCondition = document.getElementById('nextPublisherAfterSaveCondition');
	var inputParameter = document.getElementById('Parameter');
	var inputUpdCondMsg = document.getElementById('updCondMsg');
	
	//alert("inputNextActionAfterSaveCondition"+inputNextActionAfterSaveCondition);
	//alert("inputNextPublisherAfterSaveCondition"+inputNextPublisherAfterSaveCondition);
				
	if (divTxt == 'FULL_TREE'){
	
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';
		inputParameter.name='SELECTION_TREE';
		inputParameter.value='FULL';
		inputUpdCondMsg.value="UPD_TREE_SEL";
		
		formUpdCond.submit();
		return true;
	}
	
	
	if (divTxt == 'LIGHT_TREE'){
	
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';
		inputParameter.name="SELECTION_TREE";
		inputParameter.value="LIGHT";
		inputUpdCondMsg.value="UPD_TREE_SEL";
		
		formUpdCond.submit();
		return true;
	}
					   
	if (divTxt == 'DIV_FIELD_SELECTION'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_SELECTION_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_FIELD_CONDITION'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_FIELD_ORDER_BY'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_ORDERBY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_FIELD_GROUP_BY'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_GROUPBY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_RESUME_QUERY'){
		inputNextActionAfterSaveCondition.value='COMPOSE_RESUME_QUERY_ACTION';
		formUpdCond.submit();
		return true
	}

	if (divTxt == 'DIV_EXEC'){
		inputNextActionAfterSaveCondition.value='EXECUTE_QUERY_AND_SAVE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_EXPORT'){
		inputNextActionAfterSaveCondition.value='EXPORT_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	
	if (divTxt == 'DIV_SAVE_QUERY'){

		inputNextActionAfterSaveCondition.value='EXECUTE_QUERY_AND_SAVE_FROM_SAVE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_GEO'){
		inputNextActionAfterSaveCondition.value='GENERATE_GEO_TEMPLATE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_GEO_VIEWER'){
		inputNextActionAfterSaveCondition.value='GENERATE_GEO_TEMPLATE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_SAVE_SUBQUERY'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='EXIT_FROM_SUBQUERY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}
}

function updateResumeQuery(divTxt) {	
	var currentScreenTxt = getCurrentPageName();
				
	var formUpdCond = document.getElementById('formUpdateConditions');
		
	var expertSelectTextArea = document.getElementById('expertSelectTextArea');
	var inputTA = document.getElementById('expertDisplayedForUpdate');
	inputTA.value = expertSelectTextArea.value;
																										
	var inputNextActionAfterSaveCondition = document.getElementById('nextActionAfterSaveCondition');
	var inputNextPublisherAfterSaveCondition = document.getElementById('nextPublisherAfterSaveCondition');
			
	if (divTxt == 'DIV_FIELD_SELECTION'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_SELECTION_PUBLISHER';
		formUpdCond.submit();
		return true;
	
	}

	if (divTxt == 'DIV_FIELD_CONDITION'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_FIELD_ORDER_BY'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_ORDERBY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_FIELD_GROUP_BY'){
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='SELECT_FIELDS_FOR_GROUPBY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_RESUME_QUERY'){
		inputNextActionAfterSaveCondition.value='COMPOSE_RESUME_QUERY_ACTION';
		formUpdCond.submit();
		return true;
	}

	if (divTxt == 'DIV_EXEC'){
		inputNextActionAfterSaveCondition.value='EXECUTE_QUERY_AND_SAVE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_EXPORT'){
		inputNextActionAfterSaveCondition.value='EXPORT_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	
	if (divTxt == 'DIV_SAVE_QUERY'){

		inputNextActionAfterSaveCondition.value='EXECUTE_QUERY_AND_SAVE_FROM_SAVE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_GEO'){
		inputNextActionAfterSaveCondition.value='GENERATE_GEO_TEMPLATE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_GEO_VIEWER'){
		inputNextActionAfterSaveCondition.value='GENERATE_GEO_TEMPLATE_ACTION';
		formUpdCond.submit();
		return true;
	}
	
	if (divTxt == 'DIV_SAVE_SUBQUERY'){
		
		inputNextActionAfterSaveCondition.value='PUBLISH_ACTION';
		inputNextPublisherAfterSaveCondition.value='EXIT_FROM_SUBQUERY_PUBLISHER';
		formUpdCond.submit();
		return true;
	}
}

function vediSchermo(msg, divTxt) {
			
	var currentScreenTxt = getCurrentPageName();
	
				
	var formUpdCond = document.getElementById('formUpdateConditions');
	
	
	if ((currentScreenTxt == 'DIV_FIELD_CONDITION') && (formUpdCond != null)){					
		updateConditions(divTxt);
	} else if ((currentScreenTxt == 'DIV_RESUME_QUERY_SELECT_OK') && (formUpdCond != null)){
		updateResumeQuery(divTxt);		
	}else{	

			
		var frmGoSelectionF = document.getElementById('frmGoSelection');
		var frmGoConditionF = document.getElementById('frmGoCondition');
		var frmGoOrderByF = document.getElementById('frmGoOrderBy');
		var frmGoGroupByF = document.getElementById('frmGoGroupBy');
		var frmComposeQueryF = document.getElementById('frmComposeQuery');
		var frmExecuteQueryF = document.getElementById('frmExecuteQuery');
		var frmExportResultF = document.getElementById('frmExportResult');
		var frmSaveQueryF = document.getElementById('frmSaveQuery');
		var frmGeo = document.getElementById('frmGeo');
		var frmGeoViewer = document.getElementById('frmGeoViewer');				
		var frmSaveSubQuery = document.getElementById('frmSaveSubQuery');
		
		
	
		if (divTxt == 'DIV_FIELD_SELECTION'){
			frmGoSelectionF.submit();
			return true;
		}
		
		if (divTxt == 'DIV_FIELD_CONDITION'){
			frmGoConditionF.submit();
			return true;
		}
		
		if (divTxt == 'DIV_FIELD_ORDER_BY'){
			frmGoOrderByF.submit();
			return true;
		}
		
		if (divTxt == 'DIV_FIELD_GROUP_BY'){
			frmGoGroupByF.submit();
			return true;
		}
		
		if (divTxt == 'DIV_RESUME_QUERY'){
			frmComposeQueryF.submit();
			return true;
		
		}
		
		if (divTxt == 'DIV_EXEC'){
			frmExecuteQueryF.submit();
			return true;				
		}
		
		if (divTxt == 'DIV_EXPORT'){
			frmExportResultF.submit();
			return true;				
		}
		
		
		if (divTxt == 'DIV_SAVE_QUERY'){
		
			frmSaveQueryF.submit();
			return true;
		}	
		
		if (divTxt == 'DIV_GEO'){
			frmGeo.submit();
			return true;
		}
		
		if (divTxt == 'DIV_GEO_VIEWER'){
			frmGeoViewer.submit();
			return true;
		}
		
		if (divTxt == 'DIV_SAVE_SUBQUERY'){
				parent.saveSubQuery = true;
				frmSaveSubQuery.submit()
				return true;
			}
	}//end else
}

function changeTabBkg(){
	
		var spanCurrentScreeToChangeColor = document.getElementById("currentScreen");
		
		if (spanCurrentScreeToChangeColor != null){
			var screenTrimmed = Trim(spanCurrentScreeToChangeColor.childNodes[0].nodeValue);
		
			if(screenTrimmed == "DIV_RESUME_QUERY_SELECT_OK")  screenTrimmed = 'DIV_RESUME_QUERY';
		
			var tempTd = document.getElementById(screenTrimmed);
        
        	tempTd.className = 'tab selected';
        }
	
	}