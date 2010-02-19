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
			height: 170,
			dataset: 'testStore'
	        , widgetConfig: {
	           	type: 'chart.ext.line'
	        	, xField: 'name'
	            , yField: 'visits'
	        }
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.chartWidget) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.chartWidget);
		}
		
		var c = Ext.apply(defaultSettings, config || {});		
		if(c.dataset) {
			if((typeof c.dataset) === 'string' ) {
				c.storeName = c.dataset;
			} else {
				c.storeConfig = c.dataset;
			}
			delete c.dataset;
		}		
		Ext.apply(this, c);

		// constructor
		Sbi.console.ChartWidget.superclass.constructor.call(this, c);
		
};

Ext.extend(Sbi.console.ChartWidget, Sbi.console.Widget, {
    
	
	store: null
	, storeName: null
	, storeConfig: null
	
	, widgetConfig: null
	, chart: null
	
	, LINE_CHART: 'chart.ext.line'
	, BAR_CHART: 'chart.ext.bar'
	, PIE_CHART: 'chart.ext.pie'
	
    //  -- public methods ---------------------------------------------------------
    
   
    
    //  -- private methods ---------------------------------------------------------
	
	

	, onRender: function(ct, position) {
		Sbi.console.ChartWidget.superclass.onRender.call(this, ct, position);
		
		if(!this.store) {
			if(this.storeName) {
				this.store = this.getStore(this.storeName);
			} else if(this.storeConfig) {
				alert('ChartWidget: sorry unable to create a private dataset from config');
			} else {
				return;
			}
		}

		if(this.store.proxy) {
			this.store.load({
				params: {}, 
				callback: function(){}, 
				scope: this, 
				add: false
			});
		}
		
		this.chart = null;
		this.chart = this.createChart(this.widgetConfig);
			
		this.items.each( function(item) {
			this.items.remove(item);
	        item.destroy();           
	    }, this);   
		
		if(this.chart !== null) {
			this.add(this.chart);
			this.doLayout();
		}
	}
    
	, createChart: function(chartConfig) {
		var chart = null;
		
		chartConfig = chartConfig || {};
		
		var chartType = chartConfig.type;
		if(chartConfig.type) {
			delete chartConfig.type;
		}
		
		if(chartType === this.LINE_CHART) {
			chart = this.createLineChart(chartConfig);
		} else if(chartType === this.BAR_CHART) {
			chart = this.createBarChart(chartConfig);
		} else if(chartType === this.PIE_CHART){
			chart = this.createPieChart(chartConfig);
		} else {
			Sbi.exception.ExceptionHandler.showErrorMessage('Chart type [' + chartType + '] not supported by [ChartWidget]');
		}
		
		return chart;
	}
	
	, createLineChart: function(chartConfig) {
		
		var c = Ext.apply({}, chartConfig, {
			xtype: 'linechart',
	        store: this.store,
	        //xField: 'name',
	        //yField: 'visits',
			listeners: {
				itemclick: function(o){
					//var rec = this.store.getAt(o.index);
					//alert('Item Selected', 'You chose ' + rec.get('name'));
				}
			}
		});
		
		return new Ext.Panel({
	        layout:'fit'
	        , height: this.height
	        , items: c
	    });
	}
	
	, createBarChart: function(chartConfig) {
		
		var c = Ext.apply({}, chartConfig, {
			xtype: 'columnchart',
			store: this.store,
            //xField: 'column-2',
            //yField: 'column-3',
			listeners: {
				itemclick: function(o){
					//alert(this.store + ' - ' + this.getStore('testConsoleChart'));
					//var rec = this.store.getAt(o.index);
					//alert('Item Selected', 'You chose ' + rec.get('column-2'));
				}
			}
		});
		
		return new Ext.Panel({
			layout:'fit'
		    , height: this.height	
		    , items: c
		});		
	}
	
	, createPieChart: function(chartConfig) {
		var c = Ext.apply({}, chartConfig, {
			 store: this.store,
	         xtype: 'piechart',
	         //dataField: 'column-3',
	         //categoryField: 'column-2',
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
		});
		
		return new Ext.Panel({
			layout:'fit'	
		    , height: 170
			, items: c
		});
	}
    
});