/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.ZONE
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
  * CalculatedFieldWizard - short description
  * 
  * Object documentation ...
  * 
  * by Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.qbe");

Sbi.qbe.CalculatedFieldWizard = function(config) {	
	
	var c = Ext.apply({}, config || {}, {
		title: 'Expression wizard ...'
		, width: 500
		, height: 250
		, hasBuddy: false		
	});
	
	Ext.apply(this, c);
	
	
	this.initMainPanel(c);	
	
	// constructor
	Sbi.widgets.SaveWindow.superclass.constructor.call(this, {
		layout: 'fit',
		width: this.width,
		height: this.height,
		closeAction:'hide',
		plain: true,
		title: this.title,
		buttonAlign : 'center',
	    buttons: [{
			text: 'Save',
		    handler: function(){
	    		this.fireEvent('apply', this, this.getFormState(), this.targetRecord);
            	this.hide();
            	//alert(sendMessage || 'sendMessage not defined');
        	}
        	, scope: this
	    },{
		    text: 'Cancel',
		    handler: function(){
            	this.hide();
        	}
        	, scope: this
		}],
		items: [this.mainPanel]
    });
	
	if(c.hasBuddy === 'true') {
		this.buddy = new Sbi.commons.ComponentBuddy({
    		buddy : this
    	});
	}
	
	this.addEvents('apply');    
};

Ext.extend(Sbi.qbe.CalculatedFieldWizard, Ext.Window, {
	
	hasBuddy: null
    , buddy: null
   
    , mainPanel: null 
    , westRegionPanel: null 
    , centerRegionPanel: null
    , northRegionPanel: null
    
    , detailsFormPanel: null
    , expItemsTreeRootNode: null
	, expItemsTree: null
	, expItemsPanel: null
	, expItems: null
	
	, baseExpression: ' '
	, expressionEditor: null
	, expressionEditorPanel: null
	
	, inputFields: null
	
	, targetRecord: null
	

    // --------------------------------------------------------------------------------------------
    // public methods
    // --------------------------------------------------------------------------------------------
	, getFormState : function() {      
    	
      	var formState = {};
      	
      	for(p in this.inputFields) {
      		formState[p] = this.inputFields[p].getValue();
      	}
      	
      	if(this.expressionEditor) {
      		formState.expression = this.expressionEditor.getValue();
      		formState.expression = Ext.util.Format.stripTags( formState.expression );
      		formState.expression = formState.expression.replace(/&nbsp;/g," ");
      	}
      	
      	return formState;
    }
	
	, setTargetRecord: function(record) {
		this.targetRecord = record;
		if(this.targetRecord) {
			this.inputFields.alias.setValue( record.data.alias );
			this.inputFields.type.setValue( record.data.id.type );
			this.expressionEditor.reset();
			this.expressionEditor.insertAtCursor( record.data.id.expression );
		} else {
			this.inputFields.alias.reset();
			this.inputFields.type.reset();
			this.expressionEditor.reset();
			//this.expressionEditor.insertAtCursor( ' ' );
		}
	}
	
	

	//--------------------------------------------------------------------------------------------
	//private methods
	//--------------------------------------------------------------------------------------------

	, initNorthRegionPanel: function(c) {
		this.initDetailsFormPanel(Ext.apply({}, {
			region:'north',
    		split: false,
    		frame:false,
    		border:false,
    		height: 70,
    	    bodyStyle:'padding:5px;background:#E8E8E8;border-width:1px;border-color:#D0D0D0;',
    	    style: 'padding-bottom:3px'
		}, c || {}));
		
		this.northRegionPanel = this.detailsFormPanel;
	}
	
	, initWestRegionPanel: function(c) {		
	    
	    this.initExpItemsPanel(Ext.apply({}, {
	    	region:'west',
	    	title: 'Items',
			layout: 'fit',
			split: true,
			collapsible: true,
			autoScroll: true,
		    frame: false, 
		    border: true,
		    width: 120,
		    minWidth: 120
		}, c || {}));
		
		this.westRegionPanel = this.expItemsPanel;
	}
	
	, initCenterRegionPanel: function(c) {
		this.initExpressionEditorPanel(Ext.apply({}, {
			region:'center',
		    frame: false, 
		    border: false
		}, c || {}));
		
		this.centerRegionPanel = this.expressionEditorPanel;
	}

	// details form
	, initDetailsFormPanel: function(c) {
		var items = [];
		
		if(this.inputFields === null) {
			this.inputFields = new Object();
		}
		
		this.inputFields['alias'] = new Ext.form.TextField({
    		name:'alias',
    		allowBlank:false, 
    		inputType:'text',
    		maxLength:50,
    		width:250,
    		fieldLabel:'Alias' 
    	});
    	items.push( this.inputFields['alias'] );
    	
    	var scopeComboBoxData = [
    	    ['STRING','String', 'If the expression script returns a plain text string'],
    		['HTML', 'Html', 'If the expression script returns a valid html fragment'],
    		['NUMBER', 'Number', 'If the expression script returns a number']
    	];
    	         	    		
    	var scopeComboBoxStore = new Ext.data.SimpleStore({
    		fields: ['value', 'field', 'description'],
    		data : scopeComboBoxData 
    	});    		    
    	             		    
    	this.inputFields['type'] = new Ext.form.ComboBox({
    		tpl: '<tpl for="."><div ext:qtip="{field}: {description}" class="x-combo-list-item">{field}</div></tpl>',	
    		editable  : false,
    		fieldLabel : 'Type',
    		forceSelection : true,
    		mode : 'local',
    		name : 'scope',
    		store : scopeComboBoxStore,
    		displayField:'field',
    		valueField:'value',
    		emptyText:'Select type...',
    		typeAhead: true,
    		triggerAction: 'all',
    		selectOnFocus:true
    	});
    	items.push( this.inputFields['type'] );
    	
    	this.detailsFormPanel = new Ext.form.FormPanel(
    	 Ext.apply({
    		items: items
    	 }, c || {}) 
    	);
	}
	
	
	// items tree
	, initExpItemsPanel: function(c) {
		this.expItemsTreeRootNode = new Ext.tree.TreeNode({text:'Exp. Items', iconCls:'database',expanded:true});
		
		if(this.expItems) {
			this.groupRootNodes = new Array(this.expItems.length);
			for(var i = 0, l = this.expItems.length; i < l; i++) {
				if(this.expItems[i].loader === undefined) {
					this.groupRootNodes[i] = new Ext.tree.TreeNode(Ext.apply({}, {leaf: false}, this.expItems[i]));
				} else {
					this.groupRootNodes[i] = new Ext.tree.AsyncTreeNode(Ext.apply({}, {leaf: false}, this.expItems[i]));
				}
				var itemsGroup = this[this.expItems[i].name];
				if(itemsGroup != null) {
					for(var j = 0, t = itemsGroup.length; j < t; j++) {
						var node = new Ext.tree.TreeNode(itemsGroup[j]);
						Ext.apply(node.attributes, itemsGroup[j]);
						this.groupRootNodes[i].appendChild( node );
					}
				}
			}
			
			this.expItemsTreeRootNode.appendChild(this.groupRootNodes);
		}
		
	    this.expItemsTree = new Ext.tree.TreePanel({
	        root: this.expItemsTreeRootNode,
	        enableDD:false,
	        expandable:true,
	        collapsible:true,
	        autoHeight:true ,
	        bodyBorder:false ,
	        leaf:false,
	        lines:true,
	        layout: 'fit',
	        animate:true
	     });
	    
		this.expItemsPanel = new Ext.Panel(
		 Ext.apply({
			 title: 'Items',
		 	 layout: 'fit',
		 	 items: [this.expItemsTree]
		  }, c || {}) 
		 );
		
		this.expItemsTree.addListener('click', this.expItemsTreeClick, this);		
	}
	
	, expItemsTreeClick: function(node, e) {
		if(node.attributes.value) {
	    	var text = node.attributes.value + ' ';
	    	this.expressionEditor.insertAtCursor(text) ; 
		}
	}
	
	
	
	// expression editor
	, initExpressionEditorPanel: function(c) {
		
		var buttons = {};
		buttons.clear = new Ext.Toolbar.Button({
		    text:'Clear All',
		    tooltip:'Clear all selected fields',
		    iconCls:'remove'
		});
		buttons.clear.addListener('click', function(){this.expressionEditor.clear();}, this);
		
		this.expressionEditor = new Ext.form.HtmlEditor({
    		name:'expression',
    	    frame: true,
    	    enableAlignments : false,
    	    enableColors : false,
    	    enableFont :  false,
    	    enableFontSize : false, 
    	    enableFormat : false,
    	    enableLinks :  false,
    	    enableLists : false,
    	    enableSourceEdit : false,
    	    fieldLabel:'Expression' ,
    	    	
    	    listeners:{
    	    	'render': function(editor){
    	          var tb = editor.getToolbar();
    	          tb.add(buttons.clear);
    	        },
    	        'activate': function(){
    	          active = true;
    	        },
    	        'initialize': {
    	        	fn: function(){
	    	          init = true;
	    	          this.onFirstFocus();
	    	          this.insertAtCursor(' ') ; 
	    	        } // , scope: this
    	        },
    	        'beforesync': function(){
    	          // do nothings
    	        }
    	    }
    	});
		
		this.expressionEditorPanel = new Ext.Panel(
			Ext.apply({
				layout: 'fit',
			    items: [this.expressionEditor]
			}, c || {}) 
		);
	}
	
	


	, initMainPanel: function(c) {
		
		c = c || {};
		this.initNorthRegionPanel(c.northRegionConfig || {});
		this.initWestRegionPanel(c.westRegionConfig || {});
		this.initCenterRegionPanel(c.centerRegionConfig || {});
		
		this.mainPanel = new Ext.Panel({
			layout: 'border',
		    frame: false, 
		    border: false,
		    bodyStyle:'background:#E8E8E8;',
		    style:'padding:3px;',
		    items: [this.westRegionPanel, this.centerRegionPanel, this.northRegionPanel]
		 });
    }
});