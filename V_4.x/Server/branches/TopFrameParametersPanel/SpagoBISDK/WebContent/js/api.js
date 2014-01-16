/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
Sbi.sdk.namespace('Sbi.sdk.api');

Sbi.sdk.apply(Sbi.sdk.api, {
	
	elId: 0
	, iframeId: null
	
	, authenticate:  function (config) {	    
		var serviceUrl = Sbi.sdk.services.getServiceUrl('authenticate', config.params);
		Sbi.sdk.jsonp.asyncRequest(serviceUrl, config.callback.fn, config.callback.scope, config.callback.args);
    }

	, executeDataSet: function (config) {	    
		var serviceUrl = Sbi.sdk.services.getServiceUrl('executedataset', config.params);
		Sbi.sdk.jsonp.asyncRequest(serviceUrl, config.callback.fn, config.callback.scope, config.callback.args);
    }

	, getDocumentUrl: function( config ) {
		var documentUrl = null;
		
		if(config.documentId === undefined && config.documentLabel === undefined) {
			alert('ERRORE: at least one beetween documentId and documentLabel attributes must be specifyed');
			return null;
		}
		
		var params = Sbi.sdk.apply({}, config.parameters || {});
		
		if(config.documentId !== undefined) params.OBJECT_ID = config.documentId;
		if(config.documentLabel !== undefined) params.OBJECT_LABEL = config.documentLabel;
		
		if (config.executionRole !== undefined) params.ROLE = config.executionRole;
		if (config.displayToolbar !== undefined) params.TOOLBAR_VISIBLE = config.displayToolbar;
		if (config.displaySliders !== undefined) params.SLIDERS_VISIBLE = config.displaySliders;
		if (config.theme !== undefined)	params.theme = config.theme;
		
		// the only available modality is with ExtJS based gui (the other previous modality (old execution gui) is deprecated)
		documentUrl = Sbi.sdk.services.getServiceUrl('executewithext', params);
		
		return documentUrl;
	}

	, getDocumentHtml: function( config ) {
		
		var documentHtml;
		var serviceUrl = this.getDocumentUrl( config );
		
		config.iframe = config.iframe || {};
		
		if(config.iframe.id === undefined) {
			config.iframe.id = 'sbi-docexec-iframe-' + this.elId;			
			this.elId = this.elId +1;
		}
		this.iframeId = config.iframe.id;
		documentHtml = '';
		documentHtml += '<iframe';
		documentHtml += ' id = "' + config.iframe.id + '" ';
		documentHtml += ' name = "' + config.iframe.id + '" ';
		documentHtml += ' src = "' + serviceUrl + '" ';
		if(config.iframe.style !== undefined) documentHtml += ' style = "' + config.iframe.style + '" ';
		if(config.iframe.width !== undefined) documentHtml += ' width = "' + config.iframe.width + '" ';
		if(config.iframe.height !== undefined) documentHtml += ' height = "' + config.iframe.height + '" ';
		documentHtml += '></iframe>';
		
		return documentHtml;
	}
	
	, injectDocument: function( config ) {
		var targetEl = config.target || document.body;
		
		
		if(typeof targetEl === 'string') {
			var elId = targetEl;
			targetEl = document.getElementById(targetEl);
			
			if(targetEl === null) {
				targetEl = document.createElement('div');
				targetEl.setAttribute('id', elId);
				if(config.width !== undefined) {
					targetEl.setAttribute('width', config.width);
				}				
				if(config.height !== undefined) {
					targetEl.setAttribute('height', config.height);
				}
				document.body.appendChild( targetEl );
			} 
		}
		
		config.iframe = config.iframe || {};
		config.iframe.width = targetEl.getAttribute('width');
		config.iframe.height = targetEl.getAttribute('height');
		
		
		targetEl.innerHTML = this.getDocumentHtml( config );
	}
	
	, getIframeId: function(){
		return this.iframeId;
	}

	,
	exportCurrentDocument : function ( outputType ) {
		var iFrameId = this.getIframeId();
		if (iFrameId == null) {
			alert('ERROR: missing identifier of the iframe containing document execution');
			return;
		}
		var iframe = document.getElementById(iFrameId);
		var spagobiWindow = iframe.contentWindow;
		spagobiWindow.Sbi.execution.ExporterUtils.exportCurrentDocument(outputType);
	}
	
});