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


Sbi.chart.SpagoBIChart = function(config) {	
	
	this.store = config.store;
	if(this.store) {
		this.store.on('load', this.onStoreLoad, this);
		this.store.on('metachange', this.onStoreMetaChange, this);
	}
	
	Sbi.chart.SpagoBIChart.superclass.constructor.call(this, config);
};

Ext.extend(Sbi.chart.SpagoBIChart, Ext.FlashComponent, {
    
	store: null
	, storeMeta: null
	
	, CHART_BASE_URL: '/SpagoBIConsoleEngine/swf/spagobichart/'
	, CHART_SWF: {
		speedometer: 'speedometer.swf'
		, livelines: 'livelines.swf'
		, multileds: 'multileds.swf'
	}
	, CHART_DEFAULT_CONFIG: {
		speedometer: {
			paramWidth: 100
			, paramHeight: 100
			, minValue: 0
			, maxValue: 100
			, lowValue: 33
			, highValue: 66
			, field: 'EffortIndex'
		}, livelines: {
			rangeMinValue: 0
			, rangeMaxValue: 120 
			, stepY: 40
			, domainValueNumber: 18
			, title:'SpagoBI Liveline'
		} , multileds: {
			title:'SpagoBI Multileds'
			, fields: Ext.util.JSON.encode([
			    {header: 'Effort Idx',name:'EffortIndex', rangeMaxValue: 100, secondIntervalUb: 66, firstIntervalUb: 10, rangeMinValue: 0}, 
				{header: 'Compet.',name:'Competitiveness', rangeMaxValue: 100, secondIntervalUb: 66, firstIntervalUb: 33, rangeMinValue: 0}, 
				{header: 'Cost Opt.',name:'CostOptimization', rangeMaxValue: 100, secondIntervalUb: 66, firstIntervalUb: 33, rangeMinValue: 0},
				{header: 'Health',name:'Health', rangeMaxValue: 100, secondIntervalUb: 66, firstIntervalUb: 33, rangeMinValue: 0}
			])
			
		}
		
	}
	
    // -- public methods -------------------------------------------------------------------
    
   
    
    
    // -- private methods ------------------------------------------------------------------
    
	, initComponent : function(){
		Sbi.chart.SpagoBIChart.superclass.initComponent.call(this);
    	if(!this.url){
        	this.url = this.CHART_BASE_URL + this.CHART_SWF[this.chartType];
    	}
    	   	
    	this.autoScroll = true;
    	
    	this.flashVars = this.CHART_DEFAULT_CONFIG[this.chartType];
    	
	}

	,  onRender : function(ct, position){
		
		this.flashVars.paramWidth = ct.getWidth();
		this.flashVars.paramHeight = ct.getHeight();
				
		Sbi.chart.SpagoBIChart.superclass.onRender.call(this, ct, position);
	}
	
	, onStoreLoad: function(s, records, option) {
		
		if(this.chartType === 'livelines')  {
			this.livlinesLoad(s, records, option);
		} else if(this.chartType === 'multileds')  {
			this.multiledsLoad(s, records, option);
		} else if(this.chartType === 'speedometer')  {
			this.speedometerLoad(s, records, option);
		} else {
			alert('Unsupported: ' + this.chartType);
			return;
		}
	}
		
	, multiledsLoad: function(s, records, option) {
		var data = {};
		var rec = this.store.getAt(0);
		if(rec) {
			var fields = this.storeMeta.fields;
			for(var i = 0, l = fields.length, f; i < l; i++) {
				f = fields[i];
				if( (typeof f) === 'string') {
					f = {name: f};
				}
				var alias = f.header || f.name;
				if(alias === 'recNo') continue;
				data[alias] = rec.get(f.name);				
			}
			if(this.swf.loadData) {
				this.swf.loadData(data);
			}
		}
	}
	
	, speedometerLoad: function(s, records, option) {
		//alert('speedometerLoad');
		if(this.swf.setValue) {
			var value;
			var rec = this.store.getAt(0);
			if(rec) {
				//alert('fAlias: ' + this.flashVars.field);
				var fName = s.getFieldNameByAlias(this.flashVars.field);
				//alert('fName: ' + fName);
				value = rec.get(fName);
				//alert('value: ' + value);
			}
			this.swf.setValue(value);
		}
	}
	
	, livlinesLoad: function(s, records, option) {
		var data = {};
		var rec = this.store.getAt(0);
		if(rec) {
			var fields = this.storeMeta.fields;
			for(var i = 0, l = fields.length, f; i < l; i++) {
				f = fields[i];
				if( (typeof f) === 'string') {
					f = {name: f};
				}
				var alias = f.header || f.name;
				if(alias === 'recNo') continue;
				data[alias] = rec.get(f.name);				
			}
			if(this.swf.loadData) {
				this.swf.loadData(data);
			}
		}
	}
	
	, onStoreMetaChange: function(s, m) {
		this.storeMeta = m;
	}
    
});
