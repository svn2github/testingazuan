/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Ceneselli" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  

/**
 * Object name
 * 
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
 * Authors
 * 		Marco Cortella  (marco.cortella@eng.it)
 * 		
 */

//fix for IE BUG Array.isArray not supported
(function () {
    var toString = Object.prototype.toString,
         strArray = Array.toString();
 
    Array.isArray = Array.isArray || function (obj) {
        return typeof obj == "object" && (toString.call(obj) == "[object Array]" || ("constructor" in obj && String(obj.constructor) == strArray));
    }
})();

Ext.ns("Sbi.tools.dataset");

Sbi.tools.dataset.FileDatasetPanel = function(config) {
	
	
	var defaultSettings =  {
	        labelWidth: 75, 
	        frame:false,
	        defaultType: 'textfield'        	
		};


	var c = Ext.apply(defaultSettings, config || {});
	Ext.apply(this, c);

	var panelItems;
	panelItems = this.initUploadForm(panelItems,config);
	

	c = {
			items: [
			        panelItems        
			       ]
		};





	Sbi.tools.dataset.FileDatasetPanel.superclass.constructor.call(this, c);	 		
	
};

Ext.extend(Sbi.tools.dataset.FileDatasetPanel, Ext.Panel, {
	
	
	initUploadForm : function(items,config){
		if (this.isOwner == undefined) this.isOwner = true;
		
		//XLS Options Panel
		this.skipRowsField = new Ext.form.NumberField({
			fieldLabel : LN('sbi.ds.file.xsl.skiprows'),
			allowBlank : true,
			name: 'skipRows',
			width: 100,
			readOnly: !this.isOwner || false
		});
		
		this.limitRowsField = new Ext.form.NumberField({
			fieldLabel : LN('sbi.ds.file.xsl.limitrows'),
			allowBlank : true,
			name: 'limitRows',
			width: 100,
			readOnly: !this.isOwner || false
		});
		
		this.sheetNumberField = new Ext.form.NumberField({
			fieldLabel : LN('sbi.ds.file.xsl.sheetnumber'),
			allowBlank : true,
			name: 'xslSheetNumber',
			width: 100,
			readOnly: !this.isOwner || false
		});		
		
		
		this.xlsOptionsPanel = new Ext.Panel({
			  margins: '50 50 50 50',
	          labelAlign: 'left',
	          bodyStyle:'padding:5px',
	          layout: 'form',
	          width: 500,
			  labelWidth: 150,
	          items: [ this.skipRowsField, this.limitRowsField, this.sheetNumberField  ]
		});
		this.xlsOptionsPanel.setVisible(false);

		
		
		//CSV Options Panel
		//not used now because CSV Reading library supports both Windows and Unix approach for EoL
		this.csvEndOfLineCombo = new Ext.form.ComboBox({
			name : 'csvEndOfLine',
			store: new Ext.data.ArrayStore({
		        fields: [
		            'csvEndOfLineName',
		            'csvEndOfLineValue'
		        ],
		        data: [['Windows CR LF', '\\r\\n'], ['Unix, Mac Os X LF', '\\n']]
		    }),
			width : 150,
			fieldLabel : 'End of line Character',
			displayField : 'csvEndOfLineName', 
			valueField : 'csvEndOfLineValue', 
			typeAhead : true,
			forceSelection : true,
			mode : 'local',
			triggerAction : 'all',
			selectOnFocus : true, 
			editable : false,
			allowBlank : false, 
			validationEvent : false,
			readOnly: !this.isOwner || false
		});	
		
		
		this.csvQuoteCombo = new Ext.form.ComboBox({
			name : 'csvQuote',
			store: new Ext.data.ArrayStore({
		        fields: [
		            'csvQuoteName',
		            'csvQuoteValue'
		        ],
		        data: [['"', '"'], ['\'', '\'']]
		    }),
			width : 150,
			fieldLabel : LN('sbi.ds.file.csv.quote'),
			displayField : 'csvQuoteName', 
			valueField : 'csvQuoteValue', 
			typeAhead : true,
			forceSelection : true,
			mode : 'local',
			triggerAction : 'all',
			selectOnFocus : true, 
			editable : false,
			allowBlank : false, 
			validationEvent : false,
			readOnly: !this.isOwner || false
		});	
		
		this.csvDelimiterCombo = new Ext.form.ComboBox({
			name : 'csvDelimiter',
			store: new Ext.data.ArrayStore({
		        fields: [
		            'csvDelimiterName',
		            'csvDelimiterValue'
		        ],
		        data: [[';', ';'], [',', ','], ['\\t', '\\t'], ['\|', '\|']]
		    }),
			width : 150,
			fieldLabel : LN('sbi.ds.file.csv.delimiter'),
			displayField : 'csvDelimiterName', 
			valueField : 'csvDelimiterValue', 
			typeAhead : true,
			forceSelection : true,
			mode : 'local',
			triggerAction : 'all',
			selectOnFocus : true, 
			editable : false,
			allowBlank : false, 
			validationEvent : false,
			readOnly: !this.isOwner || false
		});	
		
		
		this.csvOptionsPanel = new Ext.Panel({
			  margins: '50 50 50 50',
	          labelAlign: 'left',
	          bodyStyle:'padding:5px',
	          layout: 'form',
	          width: 500,
			  labelWidth: 150,
	          items: [ this.csvDelimiterCombo, this.csvQuoteCombo]
		});
		this.csvOptionsPanel.setVisible(false);

		
		
		//Upload file fields
		this.fileTypeCombo = new Ext.form.ComboBox({
			name : 'fileType',
			store: new Ext.data.ArrayStore({
		        fields: [
		            'fileTypeName',
		            'fileTypeValue'
		        ],
		        data: [['CSV', 'CSV'], ['Excel 2003', 'XLS']]
		    }),
			width : (this.fromWizard)? 'auto' : 150,
			fieldLabel : LN('sbi.ds.file.type'),
			displayField : 'fileTypeName', 
			valueField : 'fileTypeValue', 
			typeAhead : true, forceSelection : true,
			mode : 'local',
			triggerAction : 'all',
			selectOnFocus : true, 
			editable : false,
			allowBlank : false, 
			validationEvent : false,
			readOnly: !this.isOwner || false
		});		
		this.fileTypeCombo.addListener('select',this.activateFileTypePanel, this);

		this.fileNameField = new Ext.form.DisplayField({
			fieldLabel : LN('sbi.ds.fileName'),
			width:  300,
			allowBlank : false,
			id: 'fileNameField',
			name: 'fileName',
			readOnly:true,
			hidden: !this.isOwner || false
		});
		
		this.uploadField = new Ext.form.TextField({
			inputType : 'file',
			fieldLabel : LN('sbi.generic.upload'),
			allowBlank : false,
			width: (this.fromWizard)? 500 : 300,
			id: 'fileUploadField',
			name: 'fileUpload',
			hidden: !this.isOwner || false
		});
		
		this.noChecks = new  Ext.form.Checkbox({
			fieldLabel : LN('sbi.ds.skip.checks'),
			checked: false,
			id: 'noChecks',
			name: 'SKIP_CHECKS'
		});
		
		this.uploadButton = new Ext.Button({
	        text: LN('sbi.ds.file.upload.button'),
	        id: 'fileUploadButton',
	        hidden: !this.isOwner || false
	    });
		
		//Main Panel
		this.fileUploadFormPanel = new Ext.Panel({
		  margins: '50 50 50 50',
          labelAlign: 'left',
          bodyStyle:'padding:5px',
//          layout: 'form',
		  defaultType: 'textfield',
		  fileUpload: true,
		  id: 'fileUploadPanel',
		  items: [this.fileNameField, this.uploadField, this.noChecks,this.uploadButton, this.fileTypeCombo, this.csvOptionsPanel, this.xlsOptionsPanel]

		});
		if (!this.fromWizard) {
			this.fileUploadFormPanel.layout = 'form';
		}
		return this.fileUploadFormPanel;

	}	

	//Listeners
	,activateFileTypePanel : function(combo, record, index) {
		if (Array.isArray(record)) record = record[0];
		var fileTypeSelected = record.get('fileTypeValue');
		if (fileTypeSelected != null && fileTypeSelected == 'CSV') {
			this.csvOptionsPanel.setVisible(true);
			this.xlsOptionsPanel.setVisible(false);
		} else if (fileTypeSelected != null && fileTypeSelected == 'XLS') {
			this.csvOptionsPanel.setVisible(false);
			this.xlsOptionsPanel.setVisible(true);
		}
	}
	
	,initialActivateFileTypePanel: function(fileTypeSelected){
		if (fileTypeSelected != null && fileTypeSelected == ''){
			this.csvOptionsPanel.setVisible(false);
			this.xlsOptionsPanel.setVisible(false);
		}
		else if (fileTypeSelected != null && fileTypeSelected == 'CSV') {
			this.csvOptionsPanel.setVisible(true);
			this.xlsOptionsPanel.setVisible(false);
		} else if (fileTypeSelected != null && fileTypeSelected == 'XLS') {
			this.csvOptionsPanel.setVisible(false);
			this.xlsOptionsPanel.setVisible(true);
		}
	}
	
	//Public Methods
	, setFormState: function(formState) {
		this.fileNameField.setValue(formState.fileName);
		if (formState.csvDelimiter != null){
			this.csvDelimiterCombo.setValue(formState.csvDelimiter);
		}
		if (formState.csvQuote != null){
			this.csvQuoteCombo.setValue(formState.csvQuote);
		}
		if (formState.fileType != null){
			this.fileTypeCombo.setValue(formState.fileType);
			this.initialActivateFileTypePanel(formState.fileType);
		}
		if (formState.skipRows != null){
			this.skipRowsField.setValue(formState.skipRows);
		}
		if (formState.limitRows != null){
			this.limitRowsField.setValue(formState.limitRows);
		}
		if (formState.xslSheetNumber != null){
			this.sheetNumberField.setValue(formState.xslSheetNumber);	
		}
	}
	
	, getFormState: function() {
		var formState = {};
		
		formState.fileName = this.fileNameField.getValue();
		formState.csvDelimiter = this.csvDelimiterCombo.getValue();
		formState.csvQuote = this.csvQuoteCombo.getValue();
		formState.fileType = this.fileTypeCombo.getValue();
		formState.skipRows = this.skipRowsField.getValue();
		formState.limitRows = this.limitRowsField.getValue();
		formState.xslSheetNumber = this.sheetNumberField.getValue();

		return formState;
	}

	
});