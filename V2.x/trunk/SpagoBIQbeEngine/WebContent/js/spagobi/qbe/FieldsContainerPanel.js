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
	
	// constructor
    Sbi.qbe.FieldsContainerPanel.superclass.constructor.call(this, c);
    
    this.on('render', this.initDropTarget, this);
    
};

Ext.extend(Sbi.qbe.FieldsContainerPanel, Ext.Panel, {


	initDropTarget: function() {
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
			for (var i = 0; i < rows.length; i++) {
				var aRow = rows[i];
				//ddSource.grid.store.remove(aRow);
				this.add(new Ext.Panel({
					padding: 5
					//, bodyStyle: 'horizontal-align: center;'
					, items: [{
					    xtype: 'button'
						, text: aRow.data.alias // text permits html tags
						, iconCls: aRow.data.iconCls
						, style: 'margin-left: auto; margin-right: auto;'
					}]
				}));
			}
			this.doLayout();
		}
	}

});