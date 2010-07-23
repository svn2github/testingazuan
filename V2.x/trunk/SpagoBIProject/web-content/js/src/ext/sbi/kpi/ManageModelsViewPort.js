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
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Chiara Chiarelli
 */
Ext.ns("Sbi.kpi");

Sbi.kpi.ManageModelsViewPort = function(config) { 
	
	var conf = config;
	
	//DRAW center element
	this.manageModels = new Sbi.kpi.ManageModels(conf);
	//DRAW west element
    this.modelsGrid = new Sbi.kpi.ManageModelsGrid(conf);
   //DRAW east element
    this.manageKpis = new Sbi.kpi.ManageKpis(conf);
	
	
	
	var viewport = {
		layout: 'border'
		, items: [
	         {
	           region: 'west',
	           collapseMode:'mini',
	           width: 300,
	           items:[this.modelsGrid]
	          },
		    {
		       region: 'center',
		       width: 300,
		       collapseMode:'mini',
		       items: [this.manageModels]
		    }, {
		        region: 'east',
		        split: true,
		        width: 700,
		        collapseMode:'mini',
		        items:[this.manageKpis]
		    }
		]

	};

	var c = Ext.apply({}, config || {}, viewport);

	Sbi.kpi.ManageModelsViewPort.superclass.constructor.call(this, c);	 		
}

Ext.extend(Sbi.kpi.ManageModelsViewPort, Ext.Viewport, {
	modelsGrid: null,
	manageModels: null,
	manageKpis: null
});
