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
	Sbi.chart.SpagoBIChart.superclass.constructor.call(this, config);
};

Ext.extend(Sbi.chart.SpagoBIChart, Ext.FlashComponent, {
    
	CHART_BASE_URL: '/SpagoBIConsoleEngine/swf/spagobichart/'
	, CHART_SWF: {
		rotate: 'rotation.swf'
		, liveline: 'liveline.swf'
	}
	, CHART_DEFAULT_CONFIG: {
		rotate: {
			paramWidth: 100
			, paramHeight: 100
			, minValue: -100
			, maxValue: 100
			, lowValue: -50
			, highValue: 50
		}, liveline: {
			minYValue: 0
			, maxYValue: 180 
			, stepNumValue: 10
			, refreshRate: 15000
			, xColName: 'name'
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
		
        this.testFn.defer(2000, this);
	}
	
	, testFn: function() {
    	this.swf.setValue(35);
    }
    
});
