

function executeDoc(spagobiContext
					,documentId
					,documentLabel
					,executionRole
					,parametersStr
					,parametersMap
					,displayToolbar
					,displaySliders
					,iframeStyle
					,theme
					,authenticationTicket) {

	var url=spagobiContext+'/servlet/AdapterHTTP?PAGE=ExecuteBIObjectPage&NEW_SESSION=true&MODALITY=SINGLE_OBJECT_EXECUTION_MODALITY';

	if (documentId==null && documentLabel==null) {
  		alert('documentId e documentLabel nulli');
  		return;
	}

	if (documentId != null) {
			url+='&OBJECT_ID=' + documentId;
	} 
	else {
			url+='&OBJECT_LABEL=' + documentLabel;
	}

	if (parametersStr != null) {
			url+='&PARAMETERS=' + parametersStr;
	}

	// Get the parameterMap
	if (parametersMap != null) {
			for(prop in parametersMap) {
			 if(typeof parametersMap[prop]=='string'){
				url+='&'+prop+'='+parametersMap[prop];
			 }
			 if(typeof parametersMap[prop]=='number'){
					url+='&'+prop+'='+parametersMap[prop];			 
			}			 
		}

	}

	if (executionRole != null) url+='&ROLE='+executionRole;
	if (displayToolbar != null) url+='&TOOLBAR_VISIBLE='+displayToolbar;
	if (displaySliders != null) url+='&SLIDERS_VISIBLE='+displaySliders;
	if (theme != null)	url+='&theme='+theme;
	if (authenticationTicket != null) url+='&auth_ticket='+authenticationTicket;


	// once finished the url build the HTML
	
	if(iframeStyle==null){
		iframeStyle="";
	}
	
	var htmlCode='<iframe id="frame" src="'+url+'" style="'+iframeStyle+'" width="500" height="500"></iframe>'

	return htmlCode;
}