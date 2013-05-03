/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
Ext.define('app.views.WidgetPanel',{
	extend:'Ext.Panel',
	config:{
		executionInstance : null
		,resp: null
		,slider: null
		,dockedItems: []	
	},

	constructor : function(config) {
		Ext.apply(this,config);
		this.callParent(arguments);
	}

	                
    ,initialize: function (options) {
    	if(this.resp && this.resp.documentProperties && this.resp.documentProperties.style){
    		this.setStyle(this.resp.documentProperties.style);
    	}else{
    		this.setStyle("");
    	}
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
		if(this.resp && this.resp.documentProperties && this.resp.documentProperties.header){
			var header = new Ext.Panel({
				html: this.resp.documentProperties.header,
				docked: "top",
				style: {
					height: "20px",
					position: "relative"
				}
			});
			return header;
		}
		return null;
	}
	
	, buildFooter: function(){
		if(this.resp && this.resp.documentProperties && this.resp.documentProperties.footer){
			var footer = new Ext.Panel({
				docked: "bottom",
				html: this.resp.documentProperties.footer,
				style: {
					height: "20px",
					position: "relative"
				}
			});
			return footer;
		}
		return null;
	}
	
	, updateTitle: function(){
		var button = null;
		
		if(app.views.customTopToolbar){
			button = app.views.customTopToolbar.getToolbarButtonByType('title');
		}
		if(button==null && app.views.customBottomToolbar){
			button = app.views.customBottomToolbar.getToolbarButtonByType('title');
		}
		
		if(button && this.resp && this.resp.documentProperties && this.resp.documentProperties.title){
			button.toHide = false;
			var titleText = this.resp.documentProperties.title.value;
			var titleStyle = this.resp.documentProperties.title.style;
			if(titleText){
				button.setHtml(titleText);				
			}else{
				c.hide();
				button.toHide=true;
			}
			if(titleStyle){
				button.setStyle(titleStyle); 
			}
		}else{
			button.hide();
			button.toHide=true;
		}
	}
});