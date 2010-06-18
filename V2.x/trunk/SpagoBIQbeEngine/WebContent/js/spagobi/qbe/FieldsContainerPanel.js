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
  * - Davide Zerbetto (davide.zerbetto@eng.it)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.FieldsContainerPanel = function(config) {
	
	var defaultSettings = {
	};
	
	if (Sbi.settings && Sbi.settings.qbe && Sbi.settings.qbe.fieldsContainerPanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.qbe.fieldsContainerPanel);
	}
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.init(c);
			
	Ext.apply(c, {
        store: this.store
        , cm: this.cm
        , enableDragDrop: true
        , ddGroup: 'crosstabDesignerDDGroup'
	    , layout: 'fit'
	    , viewConfig: {
	    	forceFit: true
	    }
        , listeners: {
			render: function(grid) { // hide the grid header
				grid.getView().el.select('.x-grid3-header').setStyle('display', 'none');
    		}                
		}
        , type: 'fieldsContainerPanel'
	});	
	
	// constructor
    Sbi.qbe.FieldsContainerPanel.superclass.constructor.call(this, c);
    
    this.on('render', this.initDropTarget, this);
    
};

Ext.extend(Sbi.qbe.FieldsContainerPanel, Ext.grid.GridPanel, {
	
	init: function(c) {
	
		this.store =  new Ext.data.SimpleStore({
	        fields: ['id', 'alias', 'iconCls', 'nature']
		});
	
        this.template = new Ext.Template( // see Ext.Button.buttonTemplate and Button's onRender method
        		// margin auto in order to have button center alignment
                '<table style="margin-left: auto; margin-right: auto;" id="{4}" cellspacing="0" class="x-btn {3}"><tbody class="{1}">',
                '<tr><td class="x-btn-tl"><i>&#160;</i></td><td class="x-btn-tc"></td><td class="x-btn-tr"><i>&#160;</i></td></tr>',
                '<tr><td class="x-btn-ml"><i>&#160;</i></td><td class="x-btn-mc"><em class="{2}" unselectable="on"><button type="{0}" class=" x-btn-text {5}">{6}</button></em></td><td class="x-btn-mr"><i>&#160;</i></td></tr>',
                '<tr><td class="x-btn-bl"><i>&#160;</i></td><td class="x-btn-bc"></td><td class="x-btn-br"><i>&#160;</i></td></tr>',
                '</tbody></table>');
        this.template.compile();
		
	    var fieldColumn = new Ext.grid.Column({
	    	header:  ''
	    	, dataIndex: 'alias'
	    	, hideable: false
	    	, hidden: false	
	    	, sortable: false
	   	    , renderer : function(value, metaData, record, rowIndex, colIndex, store){
	        	return this.template.apply(
	        			['button', 'x-btn-small x-btn-icon-small-left', '', 'x-btn-text-icon', Ext.id(), record.data.iconCls, record.data.alias]		
	        	);
	    	}
	        , scope: this
	    });
	    this.cm = new Ext.grid.ColumnModel([fieldColumn]);
	}
	
	, initDropTarget: function() {
		this.removeListener('render', this.initDropTarget, this);
		var dropTarget = new Sbi.widgets.GenericDropTarget(this, {
			ddGroup: 'crosstabDesignerDDGroup'
			, onFieldDrop: this.onFieldDrop
		});
	}

	, onFieldDrop: function(ddSource) {
		if (ddSource.grid && ddSource.grid.type && ddSource.grid.type === 'queryFieldsPanel') {
			// dragging from QueryFieldsPanel
			var rows = ddSource.dragData.selections;
			this.store.add(rows);
		} else if (ddSource.grid && ddSource.grid.type && ddSource.grid.type === 'fieldsContainerPanel') {
			// dragging from FieldsContainerPanel
			var rows = ddSource.dragData.selections;
			ddSource.grid.store.remove(rows);
			this.store.add(rows);
		}
	}

});