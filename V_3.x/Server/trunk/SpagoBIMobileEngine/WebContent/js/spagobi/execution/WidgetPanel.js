/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
Ext.define('app.views.WidgetPanel',{
	extend:'Ext.Panel',
	config:{
		executionInstance : null
		,slider: null
		, dockedItems: []	
	}

	                
    ,initialize: function (options) {

    	this.callParent( arguments);
	}
	
	,
	setExecutionInstance : function (executionInstance) {
		this.executionInstance = executionInstance;
	}

	,
	getExecutionInstance : function () {
		return this.executionInstance;
	}
	, setTargetDocument: function(resp){
		var drill;
		try{
			drill = resp.config.drill;
		}catch(err){
			//for table cross navigation
			drill = resp.features.drill;
		}
		var targetDoc = drill.document;
		return targetDoc;
	}
	
	, buildHeader: function(){
		var header = new Ext.Panel({
			html: "HEADER",
			style: {
				height: "20px"
			}
		});
		return header;
	}
	
	, buildFooter: function(){
		var footer = new Ext.Panel({
			html: "FOOTER",
			style: {
				height: "20px"
			}
		});
		return footer;
	}
});