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

Ext.ns("Sbi.console");

Sbi.console.ChartWidget = function(config) {
	
		var defaultSettings = {
			chartType: 'bar'
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.chartWidget) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.chartWidget);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		
		Ext.apply(this, c);
		
		this.initChart();
		
		c = Ext.apply(c, {  	
	      	items: [this.chart]
			//html: 'Io sono un chart widget'
		});

		// constructor
		Sbi.console.ChartWidget.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.console.ChartWidget, Sbi.console.Widget, {
    
	store: null
	, chartType: null
	, chart: null
	
    //  -- public methods ---------------------------------------------------------
    
   
    
    //  -- private methods ---------------------------------------------------------
    
    , initChart: function() {
		this.store = new Ext.data.JsonStore({
	        fields:['name', 'visits', 'views'],
	        data: [
	            {name:'Jul 07', visits: 245000, views: 3000000},
	            {name:'Aug 07', visits: 240000, views: 3500000},
	            {name:'Sep 07', visits: 355000, views: 4000000},
	            {name:'Oct 07', visits: 375000, views: 4200000},
	            {name:'Nov 07', visits: 490000, views: 4500000},
	            {name:'Dec 07', visits: 495000, views: 5800000},
	            {name:'Jan 08', visits: 520000, views: 6000000},
	            {name:'Feb 08', visits: 620000, views: 7500000}
	        ]
	    });
		
		if(this.chartType === 'bar') {
			this.chart = new Ext.Panel({
		        //title: 'ExtJS.com Visits Trend, 2007/2008 (No styling)',
		        //width:500,
		        //height:300,
		        layout:'fit'
		        , height: 170
	
		        , items: {
		            xtype: 'linechart',
		            store: this.store,
		            xField: 'name',
		            yField: 'visits',
					listeners: {
						itemclick: function(o){
							var rec = this.store.getAt(o.index);
							alert('Item Selected', 'You chose ' + rec.get('name'));
						}
					}
		        }
		    });
		} else if(this.chartType === 'column') {
			this.chart = new Ext.Panel({
		        layout:'fit'
		        , height: 170
	
		        , items: {
		            xtype: 'columnchart',
		            store: this.store,
		            xField: 'name',
		            yField: 'visits',
					listeners: {
						itemclick: function(o){
							var rec = this.store.getAt(o.index);
							alert('Item Selected', 'You chose ' + rec.get('name'));
						}
					}
		        }
		    });
		} else if (this.chartType === 'pie') {
			this.chart = new Ext.Panel({
		        layout:'fit'	
		        , height: 170
		        
				, items: {
		            store: this.store,
		            xtype: 'piechart',
		            dataField: 'visits',
		            categoryField: 'name',
		            //extra styles get applied to the chart defaults
		            extraStyle:
		            {
		                legend:
		                {
		                    display: 'bottom',
		                    padding: 5,
		                    font:
		                    {
		                        family: 'Tahoma',
		                        size: 13
		                    }
		                }
		            }
		        }
			});
		}
	}
    
    
});