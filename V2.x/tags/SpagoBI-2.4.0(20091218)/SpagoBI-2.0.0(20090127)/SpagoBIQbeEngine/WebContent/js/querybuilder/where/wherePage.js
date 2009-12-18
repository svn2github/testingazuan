
	
	var valueForFieldId = '';
	var fieldId = '';
	
	function selectFieldForConditionCallBack( fieldUniqueName ) {
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL';
			
			var inputFieldUniqueName = document.getElementById("FIELD_UNIQUE_NAME");
			inputFieldUniqueName.value = fieldUniqueName;	
			
			var inputNextActionAfterSaveCondition1 = document.getElementById('nextActionAfterSaveCondition');
			var inputNextPublisherAfterSaveCondition1 = document.getElementById('nextPublisherAfterSaveCondition');
			
			inputNextActionAfterSaveCondition1.value='PUBLISH_ACTION';
			inputNextPublisherAfterSaveCondition1.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';						
				
		
		
			formUpdCond1.submit();
			return true;
	}
	
	function selectFieldForJoinCallBack( fieldUniqueName ){
			
			
			var valueForField = document.getElementById(valueForFieldId);					 
			valueForField.value = fieldUniqueName;
			
			div = document.getElementById("divTreeSelectJoin");
			div.style.display = 'none';
			
			// devo aggiungere la chiamata ad un action che mi vada ad aggiungere la classe al where
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL_RIGHT';
		  	
		  	/*
		  	var inputSCompleteName = document.getElementById("S_COMPLETE_FIELD_NAME");
		  	inputSCompleteName.value = completeFieldName;	
		  	
		  	var inputSClassName = document.getElementById("S_CLASS_NAME");	
		  	inputSClassName.value = className;
		  	
		  	var inputSHibType = document.getElementById("S_HIB_TYPE");
			inputSHibType.value = hibType;	
			*/			
			
			var inputFieldUniqueName = document.getElementById("FIELD_UNIQUE_NAME");
			inputFieldUniqueName.value = fieldUniqueName;	
			
			var inputFieldID = document.getElementById("Parameter");
			inputFieldID.name = 'FIELDID';
			inputFieldID.value = fieldId;
				
			
			var inputNextActionAfterSaveCondition1 = document.getElementById('nextActionAfterSaveCondition');
			var inputNextPublisherAfterSaveCondition1 = document.getElementById('nextPublisherAfterSaveCondition');
						
			inputNextActionAfterSaveCondition1.value='PUBLISH_ACTION';
			inputNextPublisherAfterSaveCondition1.value='SELECT_FIELDS_FOR_CONDITION_PUBLISHER';						
			
		
			formUpdCond1.submit();
			return true;			
		}
		
		
		//function selectFieldForJoinWithParentCallBack(completeFieldName, className, hibType){
		function selectFieldForJoinWithParentCallBack( fieldUniqueName ){
			
			
			var valueForField = document.getElementById(valueForFieldId);
					 
			//valueForField.value = completeFieldName;
			valueForField.value = fieldUniqueName;
			
			div = document.getElementById("divTreeSelectJoin");
			div.style.display = 'none';
			
			
			
			var formUpdCond1 = document.getElementById('formUpdateConditions');
			
			var fUpdCondJoinParentInput = document.getElementById('fUpdCondJoinParent');
			fUpdCondJoinParentInput.value='TRUE';
			
			var inputUpdCondMsg =document.getElementById("updCondMsg");
			inputUpdCondMsg.value='UPD_SEL_RIGHT';
		  	
		  	/*
		  	var inputSCompleteName = document.getElementById("S_COMPLETE_FIELD_NAME");
		  	inputSCompleteName.value = completeFieldName;	
		  	
		  	var inputSClassName = document.getElementById("S_CLASS_NAME");	
		  	inputSClassName.value = className;
		  	
		  	var inputSHibType = document.getElementById("S_HIB_TYPE");
			inputSHibType.value = hibType;			
			*/	
			
			var inputFieldUniqueName = document.getElementById("FIELD_UNIQUE_NAME");
			inputFieldUniqueName.value = fieldUniqueName;	
			
			
			var inputFieldID = document.getElementById("Parameter");
			inputFieldID.name = 'FIELDID';
			inputFieldID.value = fieldId;
			
			
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