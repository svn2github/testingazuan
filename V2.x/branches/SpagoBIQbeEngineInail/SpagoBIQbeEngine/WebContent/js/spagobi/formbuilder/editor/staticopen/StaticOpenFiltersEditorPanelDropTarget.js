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
  * - name (mail)
  */

Ext.ns("Sbi.formbuilder");

Sbi.formbuilder.StaticOpenFiltersEditorPanelDropTarget = function(staticOpenFiltersEditorPanel, config) {
	
	var c = Ext.apply({
		ddGroup    : 'formbuilderDDGroup',
		copy       : false
	}, config || {});
	
	this.targetPanel = staticOpenFiltersEditorPanel;
	
	// constructor
    Sbi.formbuilder.StaticOpenFiltersEditorPanelDropTarget.superclass.constructor.call(this, this.targetPanel.getEl(), c);
};

Ext.extend(Sbi.formbuilder.StaticOpenFiltersEditorPanelDropTarget, Ext.dd.DropTarget, {
    
	targetPanel: null

    , notifyOver : function(ddSource, e, data) {
		return this.dropAllowed;
	}
	
	, notifyDrop : function(ddSource, e, data) {
		var rows = ddSource.dragData.selections;
		if(rows.length > 1 ) {
			Ext.Msg.show({
				   title:'Wrong dragged source',
				   msg: 'Select just one field please',
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.ERROR
			});
			return;
		}
		
		var openFilter = {};
		openFilter.text = rows[0].data.alias;
		openFilter.field = rows[0].data.id;
		openFilter.operator = 'EQUALS TO';
		openFilter.maxSelectedNumber = 1;

		var staticOpenFilterWindow = new Sbi.formbuilder.StaticOpenFilterWizard(openFilter, {});
		
		staticOpenFilterWindow.show();
		
		staticOpenFilterWindow.on('apply', function(openFilter) {this.targetPanel.addFilter(openFilter);} , this); 

	}
	
});