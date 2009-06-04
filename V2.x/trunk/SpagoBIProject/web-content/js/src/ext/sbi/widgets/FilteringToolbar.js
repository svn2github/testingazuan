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

Ext.ns("Sbi.widgets");

Sbi.widgets.FilteringToolbar = function(config) {	
	Sbi.widgets.FilteringToolbar.superclass.constructor.call(this, config);
};

Ext.extend(Sbi.widgets.FilteringToolbar, Ext.Toolbar, {
    
	columnNameStore: null
	, columnNameCombo: null
	
	, onRender : function(ct, position) {
	    
		Ext.PagingToolbar.superclass.onRender.call(this, ct, position);
	    
		this.addText('Il valore della colonna');
	   	    
		this.addSpacer();
		
	    this.columnNameStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : []
	    });	    
	    
	    this.columnNameCombo = this.addField(new Ext.form.ComboBox({
	        store: this.columnNameStore,
	        width: 100,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    }));
	    
	    this.store.on('metachange', function( store, meta ) {
			this.columnNameStore.removeAll();
			for(var i = 0; i < meta.fields.length; i ++) {
				if(meta.fields[i].name) {
					var r = new  this.columnNameStore.recordType({
						'value': meta.fields[i].name, 
						'label': meta.fields[i].header || meta.fields[i].name
					});
					this.columnNameStore.add([r]);
				}
			}
		}, this);
	    
	    this.addSpacer();
	    
	    this.addText('inteso come');
	    
	    this.addSpacer();
	    
	    this.typeStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : [
	                ['string', 'string']
	                , ['number', 'number']
	                , ['date', 'date']
	        ]
	    });	    
	    
	    this.typerCombo = this.addField(new Ext.form.ComboBox({
	        store: this.typeStore,
	        width: 65,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    }));
	    
	    this.addSpacer();
	    
	    this.filterStore = new Ext.data.SimpleStore({
	        fields: ['value', 'label'],
	        data : [
	                ['contain', 'contains']
	                , ['start', 'starts with']
	                , ['end', 'ends with']
	                , ['equal', '=']
	                , ['less', '<']
	                , ['lessequal', '<=']
	                , ['greater', '>']
	 	            , ['greaterequal', '>=']
	        ]
	    });	    
	    
	    this.filterCombo = this.addField(new Ext.form.ComboBox({
	        store: this.filterStore,
	        width: 80,
	        displayField:'label',
	        valueField:'value',
	        typeAhead: true,
	        triggerAction: 'all',
	        emptyText:'...',
	        selectOnFocus:true,
	        mode: 'local'
	    }));
	    
	    this.addSpacer();
	    
	    this.inputField = this.addField(new Ext.form.TextField({width: 70}));
	    
	    this.addSeparator();
	    
	    this.first = this.addButton({
	    	//text: 'x'
	        tooltip: 'apply filter'
	        //, iconCls: 'icon-filter'
	        , iconCls: 'icon-execute'
	        //, disabled: true,
	        //, handler: this.onClick.createDelegate(this, ["first"])
	    });
	    
	    this.first = this.addButton({
	    	//text: 'x'
	        tooltip: 'remove filter'
	        //, iconCls: 'icon-filter'
	        , iconCls: 'icon-remove'
	        //, disabled: true,
	        //, handler: this.onClick.createDelegate(this, ["first"])
	    });
	    
	    this.addFill();
	    
	    
	   
	    
	    
	}
});