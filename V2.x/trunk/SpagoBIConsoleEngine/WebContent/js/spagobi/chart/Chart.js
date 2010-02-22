/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
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
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.chart");


Sbi.chart.Chart = function(config) {	
	Sbi.chart.Chart.superclass.constructor.call(this, config);
};

Ext.extend(Sbi.chart.Chart, Ext.FlashComponent, {
    
   
    // -- public methods -------------------------------------------------------------------
    
   
    
    
    // -- private methods ------------------------------------------------------------------
    
	initComponent : function(){
		Sbi.chart.Chart.superclass.initComponent.call(this);
    	if(!this.url){
        	this.url = Sbi.chart.Chart.CHART_URL;
    	}
    	   	
    	this.autoScroll = true;
    	
    	// not used ...
    	this.flashVars = {
    		paramWidth: 100
    		, paramHeight: 100
    		, minValue: 0
    		, maxValue: 20
    		, lowValue: 4
    		, highValue: 10
    	};
    	
    	// ...so we pass them through url
    	/*
    	var s = '?';
    	for(v in this.flashVars) {
    		this.url += s + v + '=' + this.flashVars[v];
    		s = '&';
    	}
    	*/
	}

	,  onRender : function(ct, position){
		//Sbi.chart.Chart.superclass.onRender.call(this, ct, position);
		Ext.FlashComponent.superclass.onRender.apply(this, arguments);
		
		//alert('Height: ' + ct.getHeight());
		//alert('Width: ' + ct.getWidth());
		//this.swfWidth = ct.getWidth();
	    //this.swfHeight =  ct.getHeight();
		this.flashVars.paramWidth = ct.getWidth();
		this.flashVars.paramHeight = ct.getHeight();
		
		var s = '?';
    	for(v in this.flashVars) {
    		this.url += s + v + '=' + this.flashVars[v];
    		s = '&';
    	}
		
        var params = Ext.apply({
            allowScriptAccess: 'always',
            bgcolor: this.backgroundColor,
            wmode: this.wmode
        }, this.flashParams), vars = Ext.apply({
            allowedDomain: document.location.hostname,
            elementID: this.getId(),
            eventHandler: 'Ext.FlashEventProxy.onEvent'
        }, this.flashVars);

        new swfobject.embedSWF(this.url, this.id, this.swfWidth, this.swfHeight, this.flashVersion,
            this.expressInstall ? Ext.FlashComponent.EXPRESS_INSTALL_URL : undefined, vars, params);

        this.swf = Ext.getDom(this.id);
        this.el = Ext.get(this.swf);
        
		
	}
    
});

Sbi.chart.Chart.CHART_URL = '/SpagoBIConsoleEngine/swf/rotate.swf';
//Sbi.chart.Chart.CHART_URL = 'http:/' + '/localhost:8080/SpagoBI/dashboards/rot.lzx.swf';