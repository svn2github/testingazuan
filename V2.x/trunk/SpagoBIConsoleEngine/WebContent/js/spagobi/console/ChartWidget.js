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
			, height: 170
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.chartWidget) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.chartWidget);
		}
		
		var c = Ext.apply(defaultSettings, config || {});		
		Ext.apply(this, c);

		// constructor
		Sbi.console.ChartWidget.superclass.constructor.call(this, c);
		
};

Ext.extend(Sbi.console.ChartWidget, Sbi.console.Widget, {
    
	store: null
	, chartType: null
	, chart: null
	
    //  -- public methods ---------------------------------------------------------
    
   
    
    //  -- private methods ---------------------------------------------------------
	
	

	, onRender: function(ct, position) {
		Sbi.console.ChartWidget.superclass.onRender.call(this, ct, position);
		
		this.store = this.getStore('testStore');
		if(!this.store) return;
		
		this.chart = null;
		this.chart = this.createChart(this);
			
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
		
		if(chartConfig.chartType === 'line') {
			chart = this.createLineChart(chartConfig);
		} else if(chartConfig.chartType === 'bar') {
			chart = this.createBarChart(chartConfig);
		} else if(chartConfig.chartType === 'pie'){
			chart = this.createPieChart(chartConfig);
		} else {
			Sbi.exception.ExceptionHandler.showErrorMessage('Chart type [' + chartConfig.chartType + '] not supported by [ChartWidget]');
		}
		
		return chart;
	}
	
	, createLineChart: function(chartConfig) {
		return new Ext.Panel({
	        layout:'fit'
	        , height: this.height
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
	}
	
	, createBarChart: function(chartConfig) {
		var s = this.getStore('testConsoleChart');
		//alert('BarChart: ' + s);
		var chart = new Ext.Panel({
		        layout:'fit'
		        , height: 170
	
		        , items: {
		            xtype: 'columnchart',
		            store: s,
		            xField: 'column-2',
		            yField: 'column-3',
					listeners: {
						itemclick: function(o){
							//alert(this.store + ' - ' + this.getStore('testConsoleChart'));
							//var rec = this.store.getAt(o.index);
							//alert('Item Selected', 'You chose ' + rec.get('column-2'));
						}
					}
		     }
		});
		s.load({
			params: {}, 
			callback: function(){}, 
			scope: this, 
			add: false
		});
		return chart;
		/*
		 
		 */
	}
	
	, createPieChart: function(chartConfig) {
		var s = this.getStore('testConsoleChart');
		return new Ext.Panel({
		        layout:'fit'	
		        , height: 170
		        
				, items: {
		            store: s,
		            xtype: 'piechart',
		            dataField: 'column-3',
		            categoryField: 'column-2',
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
    
});