/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2009 Engineering Ingegneria Informatica S.p.A.
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
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

Ext.ns("Sbi.browser");

Sbi.browser.SearchPanel = function(config) { 
		
	
	
	
    
	var c = Ext.apply({}, config, {
		title: LN('sbi.browser.searchpanel.title')
        , border:true
		, bodyStyle:'padding:6px 6px 6px 6px; background-color:#FFFFFF'
    	, items: [
    	          new Ext.form.Checkbox({id: 'temp'})
    	  ]
    	  //, docMeta:false
    	  //, docName:false
	});   
	
	Sbi.browser.SearchPanel.superclass.constructor.call(this, c);  
	
	this.addEvents("onsearch", "onreset");
	
	this.addListener('afterlayout', function(el){
		
		if(this.searchField) return;		
			
		this.remove('temp');
		
		this.searchField =  new Sbi.browser.SearchField({
	    	hideLabel: true 
	    	, width:220
	    });
		
		this.searchField.addListener('onsearch', this.onSearch, this);
		
		this.searchField.addListener('onreset', function(){
			this.setFormState({valueFilter: null});
			this.fireEvent('onreset', this, this.getFormState());
		}, this);
		
		this.docNameCheckbox =  new Ext.form.Checkbox({
	    	boxLabel: LN('sbi.browser.searchpanel.docname')
	    	,hideLabel: true 
	    	, width:220
	    });	
		this.docLabelCheckbox =  new Ext.form.Checkbox({
	    	boxLabel: LN('sbi.browser.searchpanel.doclabel')
	    	,hideLabel: true 
	    	, width:220
	    });	
		this.docDescrCheckbox =  new Ext.form.Checkbox({
	    	boxLabel: LN('sbi.browser.searchpanel.docdescr')
	    	,hideLabel: true 
	    	, width:220
	    });	
		this.docMetaCheckbox =  new Ext.form.Checkbox({
	    	boxLabel: LN('sbi.browser.searchpanel.docmeta')
	    	,hideLabel: true 
	    	, width:220
	    });	
		
		this.similCheckbox =  new Ext.form.Checkbox({
	    	boxLabel: LN('sbi.browser.searchpanel.similar')
	    	,hideLabel: true 
	    	, width:220
	    });	
		this.add({
	        xtype:'fieldset',
	        title: LN('sbi.browser.searchpanel.query'),
	        collapsible: false,
	        autoHeight:true,
	        //defaults: {width: 10},
	        defaultType: 'textfield',
	        items :[this.searchField]
	       });
		this.add({
	        xtype:'fieldset',
	        title: LN('sbi.browser.searchpanel.searchin'),
	        collapsible: true,
	        autoHeight:true,
	        //defaults: {width: 10},
	        defaultType: 'textfield',
	        items :[this.docNameCheckbox, this.docLabelCheckbox, this.docDescrCheckbox, this.docMetaCheckbox]
	       });
		this.add({
            xtype:'fieldset',
            title: LN('sbi.browser.searchpanel.advanced'),
            collapsible: true,
            autoHeight:true,
            //defaults: {width: 10},
            defaultType: 'textfield',
            items :[this.similCheckbox]
           });
	
		this.setFormState(c);
		
	}, this);
};

Ext.extend(Sbi.browser.SearchPanel, Ext.FormPanel, {
    
	searchField: null
	, similCheckbox: null
	, docMetaCheckbox: null
	, docDescrCheckbox: null
	, docLabelCheckbox: null
	, docNameCheckbox: null
	
	, getFormState: function() {
	
		var formState = {};
	    
		formState.valueFilter = this.searchField.getRawValue();
		formState.docName = this.docNameCheckbox.getValue();
		formState.docLabel = this.docLabelCheckbox.getValue();
		formState.docDescr = this.docDescrCheckbox.getValue();
		formState.docMeta = this.docMetaCheckbox.getValue();
		formState.similar = this.similCheckbox.getValue();
	    	   
	    return formState;
	}

	, setFormState: function(formState) {
		
		if(formState.valueFilter !== undefined) {
			this.searchField.setValue( formState.valueFilter );
		}
		
		if(formState.docName) {
			this.docNameCheckbox.setValue(formState.docName);
		} 
		
		if(formState.docLabel) {
			this.docLabelCheckbox.setValue( formState.docLabel );
		}
		
		if(formState.docDescr) {
			this.docDescrCheckbox.setValue( formState.docDescr );
		}
		if(formState.docMeta) {
			this.docMetaCheckbox.setValue( formState.docMeta );
		}
		if(formState.similar) {
			this.similCheckbox.setValue( formState.similar );
		}
	}
	
	
	, onSearch: function(field, query) {		
		this.fireEvent('onsearch', this, this.getFormState());
		
	}
    
});

Ext.reg('searchpanel', Sbi.browser.SearchPanel);