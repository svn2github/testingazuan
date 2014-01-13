/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
/**
  * Object name 
  * 
  * [description]
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * -  Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.commons");

Sbi.commons.Session = new (function(){
	
	this.services = new Array();
	this.services['extendSession'] = Sbi.config.contextName + '/servlet/ExtendSession';
	
	this.extend = function(){
    	Ext.Ajax.request({
	        url: this.services['extendSession']
	   });
    };
       
})();

function exportCurrentDocument(outputType, obj, executionPanel){

	var docExecPage = executionPanel.activeDocument.documentExecutionPage;
	
	var toolbar = docExecPage.toolbar;
	if(toolbar == null){		
		docExecPage.toolbarHiddenPreference= false;
		config.executionToolbarConfig = docExecPage.initialConfig.executionToolbarConfig || {};
		config.executionToolbarConfig.callFromTreeListDoc = docExecPage.initialConfig.callFromTreeListDoc;

		toolbar = new Sbi.execution.toolbar.DocumentExecutionPageToolbar(config.executionToolbarConfig);
		
		docExecPage.toolbar= toolbar;
	}
	var executionInstance = docExecPage.executionInstance;
	toolbar.controller= docExecPage;
	var exporter = new Sbi.execution.toolbar.ExportersMenu(
		{
		    toolbar: toolbar
			, executionInstance: executionInstance
		}
	);
	var docType = obj.typeCode;
	var exportUrl = exporter.getExportationUrl(outputType, docType);
	if(exportUrl != undefined){
		return exportUrl;
	}else{
		//other types of document that doesn't use export url
		//ex: CHART, WORKSHEET, NETWORK
		if(docType == 'CHART' || docType == 'DASH'){
			exporter.exportChartTo(outputType);
		}else if(docType == 'NETWORK'){
			if(outputType == 'PDF') {outputType = 'pdf'; }
			if(outputType == 'PNG') {outputType = 'png'; }
			if(outputType == 'GRAPHML') {outputType = 'graphml'; }
			
			exporter.exportNetworkTo(outputType);
		}else if(docType == 'WORKSHEET'){
			if(outputType == 'PDF'){
				outputType = 'application/pdf';
			}else if(outputType == 'XLS'){
				outputType = 'application/vnd.ms-excel';
			}else if(outputType == 'XLSX'){
				outputType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
			}
			
			exporter.exportWorksheetsTo(outputType);
		}
		
	}
	

};
