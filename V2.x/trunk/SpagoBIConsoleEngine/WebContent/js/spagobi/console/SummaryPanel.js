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
  * - Antonella Giachino (antonella.giachino@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.SummaryPanel = function(config) {
	
		var defaultSettings = {
			layout: 'fit'
			, region: 'north'
			, height: 410
			, split: true
			//, collapseMode: 'mini'
			, collapsible: true
	        , collapseFirst: false
		};
		
		if(Sbi.settings && Sbi.settings.console && Sbi.settings.console.summaryPanel) {
			defaultSettings = Ext.apply(defaultSettings, Sbi.settings.console.summaryPanel);
		}
		
		var c = Ext.apply(defaultSettings, config || {});
		Ext.apply(this, c);
		
		var widgetPanelConfig = config.layoutManagerConfig || {};
		widgetPanelConfig.storeManager = this.storeManager;
		widgetPanelConfig.items = [];

		for(var i = 0, l1 = config.charts.length ; i < l1; i++) {
			if(config.charts[i].widgetConfig.type === 'chart.composite') {
				var compositeWidgetPanelConfig = {};
				//******** defaults configuration applied to each contained panel **********
				
				//dataset (default)				
				var defaultDataset = widgetPanelConfig.defaults.dataset;		
				
				//width (default)
				var defaultWidth =  (widgetPanelConfig.defaults.width === undefined) ? 400 : widgetPanelConfig.defaults.width;
				
				//heigt (default)
				var defaultHeight =  (widgetPanelConfig.defaults.height === undefined) ? 400: widgetPanelConfig.defaults.height;
				
				//**************** configuration about single contained panel *************
				
				//title
				compositeWidgetPanelConfig.title = (config.charts[i].widgetConfig.title.text === undefined) ? "" :  config.charts[i].widgetConfig.title.text;
				
				//colspan
				compositeWidgetPanelConfig.colspan = (config.charts[i].widgetConfig.colspan === undefined) ? 1 : config.charts[i].widgetConfig.colspan;
				
				//rowspan
				compositeWidgetPanelConfig.rowspan = (config.charts[i].widgetConfig.rowspan === undefined) ? 1 : config.charts[i].widgetConfig.rowspan;

				//dataset (single panel)
				componentDataset = config.charts[i].widgetConfig.dataset;
				//width (single panel)
				var componentWidth = config.charts[i].widgetConfig.width;								
				//height (single panel)
				var componentHeight = config.charts[i].widgetConfig.height;				
				
				
				compositeWidgetPanelConfig.storeManager = this.storeManager;
				compositeWidgetPanelConfig.items = [];
					
				for(var j = 0, l2 = config.charts[i].widgetConfig.subcharts.length ; j < l2; j++) {
				
					var configSubChart = {};
					configSubChart = config.charts[i].widgetConfig.subcharts[j];
					
					//sets the dataset; the order for getting values are: single single panel, table
					if (configSubChart.dataset === undefined){
						if (componentDataset !== undefined){
							configSubChart.dataset = componentDataset;
						}
						else{
							configSubChart.dataset = defaultDataset;
						}
					}
					
					//sets the width of single element; the order for getting values are: single widget, single panel, table
					if (configSubChart.width === undefined){
						if (componentWidth !== undefined){
							configSubChart.width = componentWidth/l2; //divides total space by the number of elements
						}
						else{
							configSubChart.width = defaultWidth/l2; //divides default total space by the number of elements
						}
					}

					//apply the colspan
					configSubChart.width = (configSubChart.width * compositeWidgetPanelConfig.colspan);
					
					//sets the height of single element; the order for getting values are: single widget, single panel, table
					if (configSubChart.height === undefined){
						if (componentHeight !== undefined){
							configSubChart.height = componentHeight; 
						}
						else{
							configSubChart.height = defaultHeight;
						}
					}
					
					//apply the colspan
					configSubChart.height = (configSubChart.height * compositeWidgetPanelConfig.rowspan);
		
					compositeWidgetPanelConfig.width = (componentWidth > configSubChart.width)? componentWidth : configSubChart.width;
					compositeWidgetPanelConfig.height = (componentHeight > configSubChart.height)? componentHeight : configSubChart.height;
				
					//compositeWidgetPanelConfig.items.push(new Sbi.console.ChartWidget(config.charts[i].widgetConfig.subcharts[j]));
					compositeWidgetPanelConfig.items.push(new Sbi.console.ChartWidget(configSubChart));
				}
				var compositeWidgetPanel = new Sbi.console.WidgetPanel(compositeWidgetPanelConfig);
				widgetPanelConfig.items.push(compositeWidgetPanel);
			} else {
				widgetPanelConfig.items.push(new Sbi.console.ChartWidget(config.charts[i]));
			}
		}
		
		var widgetPanel = new Sbi.console.WidgetPanel(widgetPanelConfig);
		
		c = Ext.apply(c, {  	
			items: [widgetPanel]
		});

		// constructor
		Sbi.console.SummaryPanel.superclass.constructor.call(this, c);
		
};

Ext.extend(Sbi.console.SummaryPanel, Ext.Panel, {
    
    services: null
    
   
    //  -- public methods ---------------------------------------------------------
    
    
    
    //  -- private methods ---------------------------------------------------------
    
    
    
    
});