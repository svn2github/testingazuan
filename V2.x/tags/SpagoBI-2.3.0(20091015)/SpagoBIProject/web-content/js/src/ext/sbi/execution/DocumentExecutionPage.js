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

Ext.ns("Sbi.execution");

Sbi.execution.DocumentExecutionPage = function(config, doc) {
	
	// apply defaults values
	config = Ext.apply({
		// no defaults
	}, config || {});
	
	// check mandatory values
	// ...
		
	// declare exploited services
	var params = {LIGHT_NAVIGATOR_DISABLED: 'TRUE', SBI_EXECUTION_ID: null};
	this.services = new Array();
	this.services['getUrlForExecutionService'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'GET_URL_FOR_EXECUTION_ACTION'
		, baseParams: params
	});
	this.services['showSendToForm'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SHOW_SEND_TO_FORM'
		, baseParams: params
	});
	
	this.services['saveIntoPersonalFolder'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'SAVE_PERSONAL_FOLDER'
		, baseParams: params
	});
	
	this.services['toPdf'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_PDF'
		, baseParams: params
	});
	
	this.services['toChartPdf'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_CHART_PDF'
		, baseParams: params
	});
	
	this.services['toChartJpg'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_CHART_JPG'
		, baseParams: params
	});
	
	this.services['exportDataStore'] = Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_RESULT_ACTION'
		, baseParams: params
	});

	
	// add events
    this.addEvents('beforetoolbarinit', 'beforesynchronize', 'moveprevrequest', 'loadurlfailure', 'crossnavigation', 'beforerefresh','collapse3');
    
    this.toolbarHiddenPreference = config.toolbarHidden!== undefined ? config.toolbarHidden : false;
	this.shortcutsHiddenPreference = config.shortcutsHidden !== undefined ? config.shortcutsHidden : false;
    
    this.init(config, doc);    
    
    this.shortcutsPanel.on('applyviewpoint', this.parametersPanel.applyViewPoint, this.parametersPanel);
    this.shortcutsPanel.on('viewpointexecutionrequest', function(v) {
    	this.southPanel.collapse();
    	this.northPanel.collapse();
    	this.parametersPanel.applyViewPoint(v);
		this.refreshExecution();
    }, this);
    
    this.shortcutsPanel.on('subobjectexecutionrequest', function (subObjectId) {
    	this.southPanel.collapse();
    	this.northPanel.collapse();
		this.executionInstance.SBI_SUBOBJECT_ID = subObjectId;
		var formState = this.parametersPanel.getFormState();
		var formStateStr = Sbi.commons.JSON.encode( formState );
		this.executionInstance.PARAMETERS = formStateStr;
		this.synchronize(this.executionInstance, false);
	}, this);
	
    this.shortcutsPanel.on('snapshotexcutionrequest', function (snapshotId) {
    	this.southPanel.collapse();
    	this.northPanel.collapse();
		this.executionInstance.SBI_SNAPSHOT_ID = snapshotId;
		this.synchronize(this.executionInstance);
	}, this);
    
	var c = Ext.apply({}, config, {
		layout: 'border'
		, tbar: this.toolbar
		, items: [this.miframe, this.southPanel, this.northPanel]
	});	    
	
	// constructor
    Sbi.execution.DocumentExecutionPage.superclass.constructor.call(this, c);
	
};

Ext.extend(Sbi.execution.DocumentExecutionPage, Ext.Panel, {
    
    // static contents and methods definitions
	services: null
	, executionInstance: null
	
	, toolbar: null
	, miframe : null
	, parametersPanel: null
    , shortcutsPanel: null
    , southPanel: null
    , northPanel: null
   
	// ----------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------
    
    
    , synchronize: function( executionInstance, synchronizeSliders ) {
		
		if(this.fireEvent('beforesynchronize', this, executionInstance, this.executionInstance) !== false){
			this.executionInstance = executionInstance;
			this.synchronizeToolbar( executionInstance );
			
			if(synchronizeSliders === undefined || synchronizeSliders === true) {
				/*
				this.parametersPanel.on(
						'synchronize',
						function (parametersPanel) {
							if(executionInstance.PARAMETERS !== undefined) {
								var parameters = Ext.util.JSON.decode( executionInstance.PARAMETERS );
								var state = Ext.urlEncode(parameters);		
								this.parametersPanel.setFormState(state);
							}
						}
						, this
				);
				*/
				this.parametersPanel.synchronize(executionInstance);
				this.shortcutsPanel.synchronize(executionInstance);
			}
			
			Ext.Ajax.request({
		        url: this.services['getUrlForExecutionService'],
		        params: executionInstance,
		        callback : function(options , success, response){
		  	  		if(success) {   
			      		if(response !== undefined && response.responseText !== undefined) {
			      			var content = Ext.util.JSON.decode( response.responseText );
			      			if(content !== undefined) {
			      				if(content.errors !== undefined && content.errors.length > 0) {
			      					this.fireEvent('loadurlfailure', content.errors);
			      				} else {
			      					this.miframe.getFrame().setSrc( content.url );
			      					//this.add(this.miframe);
			      				}
			      			} 
			      		} else {
			      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			      		}
		  	  		} else { 
			  	  		if(response !== undefined && response.responseText !== undefined) {
			      			var content = Ext.util.JSON.decode( response.responseText );
			      			if(content !== undefined && content.errors !== undefined) {
			      				this.fireEvent('loadurlfailure', content.errors);
			      			} 
			      		} else {
			      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
			      		}
		  	  		}
		        },
		        scope: this,
				  failure: Sbi.exception.ExceptionHandler.handleFailure      
		   });
		}
	}

	, synchronizeToolbar: function( executionInstance ){
		
		// if toolbar is hidden, do nothing
		if (this.toolbarHiddenPreference) 
			return;
		
		this.toolbar.items.each( function(item) {
			this.toolbar.items.remove(item);
            item.destroy();           
        }, this); 
		
		this.fireEvent('beforetoolbarinit', this, this.toolbar);
		
		this.toolbar.addFill();
		
		if (executionInstance.isPossibleToComeBackToParametersPage == undefined || executionInstance.isPossibleToComeBackToParametersPage === true) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-back' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.back')
				, scope: this
				, handler : function() {this.fireEvent('moveprevrequest');}
			}));
		}
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-refresh' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.refresh')
		    , scope: this
		    , handler : function() {
					this.southPanel.collapse();
					this.northPanel.collapse();
					this.refreshExecution();
			}			
		}));

		if(Sbi.user.ismodeweb){
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-expand' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.expand')
			    , scope: this
			    , handler : function() {
						this.fireEvent('collapse3');
				}			
			}));
		}
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-rating' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.rating')
		    , scope: this
		    , handler : this.rateExecution	
		}));
		
		this.toolbar.addButton(new Ext.Toolbar.Button({
			iconCls: 'icon-print' 
			, tooltip: LN('sbi.execution.executionpage.toolbar.print')
		    , scope: this
		    , handler : this.printExecution
		}));
		
		if (Sbi.user.functionalities.contains('SendMailFunctionality') && !executionInstance.SBI_SNAPSHOT_ID
				&& executionInstance.document.typeCode == 'REPORT') {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-sendMail' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.send')
		     	, scope: this
		    	, handler : this.sendExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SaveIntoFolderFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveIntoPersonalFolder' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.save')
		     	, scope: this
		    	, handler : this.saveExecution
			}));
		}

		if (Sbi.user.functionalities.contains('SaveRememberMeFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-saveRememberMe'
				, tooltip: LN('sbi.execution.executionpage.toolbar.bookmark')
		     	, scope: this
		    	, handler :this.bookmarkExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SeeNotesFunctionality') && !executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-notes' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.annotate')
		     	, scope: this
		    	, handler : this.annotateExecution
			}));
		}
		
		if (Sbi.user.functionalities.contains('SeeMetadataFunctionality') && !this.executionInstance.SBI_SNAPSHOT_ID) {
			this.toolbar.addButton(new Ext.Toolbar.Button({
				iconCls: 'icon-metadata' 
				, tooltip: LN('sbi.execution.executionpage.toolbar.metadata')
		     	, scope: this
		    	, handler : this.metaExecution
			}));
		}
		
		if(executionInstance.document.exporters){
			if ( executionInstance.document.typeCode == 'KPI' && executionInstance.document.exporters.contains('PDF')) {
				this.toolbar.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-pdf' 
					, tooltip: LN('sbi.execution.PdfExport')
			     	, scope: this
			    	, handler : this.pdfExecution
				}));
			}else if( executionInstance.document.typeCode == 'REPORT') {
					var menuItems = new Array();
					
					for(i=0;i<executionInstance.document.exporters.length ;i++){
						
						if (executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportReportExecution('PDF'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(executionInstance.document.exporters[i]=='XLS'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XlsExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xls' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportReportExecution('XLS'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='RTF'){
							menuItems.push(   new Ext.menu.Item({
	                            id:  Ext.id()
	                            , text: LN('sbi.execution.rtfExport')
	                            , group: 'group_2'
	                            , iconCls: 'icon-rtf' 
						     	, scope: this
								 , width: 15
						    	, handler : function() { this.exportReportExecution('RTF'); }
								, href: ''   
	                        })	
	                        );
						}else if(executionInstance.document.exporters[i]=='DOC'){
							menuItems.push(   new Ext.menu.Item({
	                            id:  Ext.id()
	                            , text: LN('sbi.execution.docExport')
	                            , group: 'group_2'
	                            , iconCls: 'icon-rtf' 
						     	, scope: this
								 , width: 15
						    	, handler : function() { this.exportReportExecution('DOC'); }
								, href: ''   
	                        })	
	                        );
						}else if(executionInstance.document.exporters[i]=='CSV'){
							menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.CsvExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-csv' 
									     	, scope: this
									   , width: 15
									    	, handler : function() { this.exportReportExecution('CSV'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='XML'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XmlExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xml' 
									     	, scope: this
									      , width: 15
									    	, handler : function() { this.exportReportExecution('XML'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='JPG'){
						menuItems.push(   new Ext.menu.Item({
				                            id: Ext.id()
				                            , text: LN('sbi.execution.JpgExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-jpg' 
									     	, scope: this
									     , width: 15
									    	, handler : function() { this.exportReportExecution('JPG'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='TXT'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.txtExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-txt' 
									     	, scope: this
									     	 , width: 15
									    	, handler : function() { this.exportReportExecution('TXT'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='PPT'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.pptExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-ppt' 
									     	, scope: this
									      , width: 15
									    	, handler : function() { this.exportReportExecution('PPT'); }
											, href: ''   
				                        })	
				                        ); 
						}
				    }   
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(executionInstance.document.exporters.length > 0){
						this.toolbar.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}else if( executionInstance.document.typeCode == 'OLAP') {
					var menuItems = new Array();
					
					for(i=0;i<executionInstance.document.exporters.length ;i++){
						
						if (executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportOlapExecution('PDF'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(executionInstance.document.exporters[i]=='XLS'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XlsExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xls' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportOlapExecution('XLS'); }
											, href: ''   
				                        })	
				                        ); 
						}
				    }   
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(executionInstance.document.exporters.length > 0){
						this.toolbar.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}
			else if ( executionInstance.document.typeCode == 'DASH') {
				this.toolbar.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-pdf' 
					, tooltip: LN('sbi.execution.PdfExport')
			     	, scope: this
			    	, handler :  function() { this.exportChartExecution('PDF'); }
					, href: ''  
				}));
			}
			else if ( executionInstance.document.typeCode == 'DATAMART') {
			
					var menuItems = new Array();
					
					for(i=0;i<executionInstance.document.exporters.length ;i++){
						
						if (executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportQbEExecution('application/pdf'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(executionInstance.document.exporters[i]=='XLS'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.XlsExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-xls' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportQbEExecution('application/vnd.ms-excel'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='RTF'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.rtfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-rtf' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportQbEExecution('application/rtf'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='CSV'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.CsvExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-csv' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportQbEExecution('text/csv'); }
											, href: ''   
				                        })	
				                        ); 
						}else if(executionInstance.document.exporters[i]=='JRXML'){
						menuItems.push(   new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.jrxmlExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-jrxml' 
									     	, scope: this
											 , width: 15
									    	, handler : function() { this.exportQbEExecution('text/jrxml'); }
											, href: ''   
				                        })	
				                        ); 
						}
						
				    }   
					var menu0 = new Ext.menu.Menu({
					id: 'basicMenu_0',
					items: menuItems    
					});	
					
					if(executionInstance.document.exporters.length > 0){
						this.toolbar.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			}	else if ( executionInstance.document.typeCode == 'MAP') {
			
					var menuItems = new Array();
					
					for(i=0;i<executionInstance.document.exporters.length ;i++){
						
						if (executionInstance.document.exporters[i]=='PDF'){
						menuItems.push(	new Ext.menu.Item({
				                            id:  Ext.id()
				                            , text: LN('sbi.execution.PdfExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-pdf' 
									     	, scope: this
									        , width: 15
									    	, handler : function() { this.exportGeoExecution('pdf'); }
											, href: ''   
				                        })	 
				                       ); 
						}else if(executionInstance.document.exporters[i]=='JPG'){
						menuItems.push(   new Ext.menu.Item({
				                            id: Ext.id()
				                            , text: LN('sbi.execution.JpgExport')
				                            , group: 'group_2'
				                            , iconCls: 'icon-jpg' 
									     	, scope: this
									     , width: 15
									    	, handler : function() { this.exportGeoExecution('jpeg'); }
											, href: ''   
				                        })	
				                        ); 
						}
			
			  		}   
					var menu0 = new Ext.menu.Menu({
						id: 'basicMenu_0',
						items: menuItems    
					});	
					
					if(executionInstance.document.exporters.length > 0){
						this.toolbar.add(
									new Ext.Toolbar.MenuButton({
										id: Ext.id()
							            , tooltip: 'Exporters'
										, path: 'Exporters'	
										, iconCls: 'icon-export' 	
							            , menu: menu0
							            , width: 15
							            , cls: 'x-btn-menubutton x-btn-text-icon bmenu '
							        })					    				        				
						);	
					}
			
			
			/*
				this.toolbar.addButton(new Ext.Toolbar.Button({
					iconCls: 'icon-jpg' 
					, tooltip: LN('sbi.execution.JpgExport')
			     	, scope: this
			    	, handler :  function() { this.exportGeoExecution('jpeg'); }
					, href: ''  
				}));*/
			}	
		}
	}


	, refreshExecution: function() {
		
		/*
		var mustSynchronize = false;
		if (this.executionInstance.SBI_SUBOBJECT_ID !== undefined) {
			delete this.executionInstance.SBI_SUBOBJECT_ID;
			mustSynchronize = true;
		}
		if (this.executionInstance.SBI_SNAPSHOT_ID !== undefined) {
			delete this.executionInstance.SBI_SNAPSHOT_ID;
			mustSynchronize = true;
		}
		
		var formState = this.parametersPanel.getFormState();
		var formStateStr = Sbi.commons.JSON.encode( formState );
		
		if(this.fireEvent('beforerefresh', this, this.executionInstance, formState) !== false){
			if(mustSynchronize || formStateStr !== this.executionInstance.PARAMETERS) { // todo: if(parametersPanel.isDirty())		
				this.executionInstance.PARAMETERS = formStateStr;
				this.synchronize( this.executionInstance, false );
			} else {
				this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
			}
		}
		*/
		
		var formState = this.parametersPanel.getFormState();
		var formStateStr = Sbi.commons.JSON.encode( formState );
		
		if(this.fireEvent('beforerefresh', this, this.executionInstance, formState) !== false){
			if(formStateStr !== this.executionInstance.PARAMETERS) { // todo: if(parametersPanel.isDirty())		
				this.executionInstance.PARAMETERS = formStateStr;
				this.synchronize( this.executionInstance, false );
			} else {
				this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
			}
		}
	}
	
	, rateExecution: function() {
		this.win_rating = new Sbi.execution.toolbar.RatingWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_rating.show();
	}
	
	, printExecution: function() {
		this.miframe.getFrame().print();
	}
	
	, sendExecution: function () {
		var sendToIframeUrl = this.services['showSendToForm'] 
		        + '&objlabel=' + this.executionInstance.OBJECT_LABEL
		        + '&objid=' + this.executionInstance.OBJECT_ID
				+ '&' + Sbi.commons.Format.toStringOldSyntax(this.parametersPanel.getFormState());
		this.win_sendTo = new Sbi.execution.toolbar.SendToWindow({'url': sendToIframeUrl});
		this.win_sendTo.show();
	}
	
	, saveExecution: function () {
		Ext.Ajax.request({
	          url: this.services['saveIntoPersonalFolder'],
	          params: {documentId: this.executionInstance.OBJECT_ID},
	          callback : function(options , success, response){
	    	  	if (success && response !== undefined) {   
		      		if(response.responseText !== undefined) {
		      			var responseText = response.responseText;
		      			var iconSaveToPF;
		      			var message;
		      			if (responseText=="sbi.execution.stpf.ok") {
		      				message = LN('sbi.execution.stpf.ok');
		      				iconSaveToPF = Ext.MessageBox.INFO;
		      			}
		      			if (responseText=="sbi.execution.stpf.alreadyPresent") {
		      				message = LN('sbi.execution.stpf.alreadyPresent');
		      				iconSaveToPF = Ext.MessageBox.WARNING;
		      			}
		      			if (responseText=="sbi.execution.stpf.error") {
		      				message = LN('sbi.execution.stpf.error');
		      				iconSaveToPF = Ext.MessageBox.ERROR;
		      			}

		      			var messageBox = Ext.MessageBox.show({
		      				title: 'Status',
		      				msg: message,
		      				modal: false,
		      				buttons: Ext.MessageBox.OK,
		      				width:300,
		      				icon: iconSaveToPF,
		      				animEl: 'root-menu'        			
		      			});
		      		} else {
		      			Sbi.exception.ExceptionHandler.showErrorMessage('Server response is empty', 'Service Error');
		      		}
	    	  	}
	          },
	          scope: this,
	  		  failure: Sbi.exception.ExceptionHandler.handleFailure      
	     });
	}
	
	, bookmarkExecution: function () {
		this.win_saveRememberMe = new Sbi.execution.toolbar.SaveRememberMeWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_saveRememberMe.show();
	}
	
	, annotateExecution: function () {
		this.win_notes = new Sbi.execution.toolbar.NotesWindow({'SBI_EXECUTION_ID': this.executionInstance.SBI_EXECUTION_ID});
		this.win_notes.show();
	}
	
	, metaExecution: function () {
		this.win_metadata = new Sbi.execution.toolbar.MetadataWindow({'OBJECT_ID': this.executionInstance.OBJECT_ID});
		this.win_metadata.show();
	}
	
	, pdfExecution: function () {
		var urlExporter = this.services['toPdf'] + '&OBJECT_ID=' + this.executionInstance.OBJECT_ID;
		//alert(urlExporter);
		window.open(urlExporter,'name','height=750,width=1000');
	}		
	
	,exportReportExecution: function (exportType) {
		var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var startIndex =docurl.indexOf('?')+1;
	    var endIndex = docurl.length;
	    var baseUrl = docurl.substring(0,startIndex);
	    var docurlPar = docurl.substring(startIndex,endIndex);
	    var parurl = Ext.urlDecode(docurlPar);
	    parurl.outputType = exportType;
	    parurl = Ext.urlEncode(parurl);
	    var endUrl = baseUrl +parurl;
		window.open(endUrl,'name','height=750,width=1000');
	}
	
	,exportOlapExecution: function (exportType) {
		var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	    baseUrl = baseUrl.substring(0,baseUrl.lastIndexOf('/')+1) + "Print?";
	 
	    var docurlPar = "cube=01&type=";
	    if (exportType == "PDF") {docurlPar += "1";}
	    else if (exportType == "XLS"){ docurlPar += "0"};
	   
	    var endUrl = baseUrl + docurlPar;
	    //alert ("endUrl: " + endUrl);
	    
		window.open(endUrl,'name','height=750,width=1000');
	}
	
	, exportChartExecution: function (exportType) {		
		var urlExporter = "";
	    
		if (exportType == "PDF")  {
			urlExporter = this.services['toChartPdf'] + '&OBJECT_ID=' + this.executionInstance.OBJECT_ID ;
			urlExporter+= '&SBI_EXECUTION_ID=' + this.executionInstance.SBI_EXECUTION_ID + "&outputType=PDF";
		}
		window.open(urlExporter,'name','height=750,width=1000');
	}
	
	, exportQbEExecution: function (exportType) {	
	    var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	 
	    var docurlPar = "ACTION_NAME=EXPORT_RESULT_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&user_id=-1&MIME_TYPE="+exportType+"&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT";
	   
	    var endUrl = baseUrl + docurlPar;
	   // alert ("endUrl: " + endUrl);
	    
		window.open(endUrl,'name','height=750,width=1000');
	}
	
	, exportGeoExecution: function (exportType) {	
	    var mf = this.miframe;
		var frame = mf.getFrame();
	    var docurl = frame.getDocumentURI();
	    var baseUrl = docurl.substring(0,docurl.indexOf('?')+1);   
	    if (baseUrl=="") baseUrl = docurl;
	 
	    //var docurlPar = "ACTION_NAME=DRAW_MAP_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&user_id=-1&outputFormat=jpeg&inline=false";
	    var docurlPar = "ACTION_NAME=DRAW_MAP_ACTION&SBI_EXECUTION_ID="+this.executionInstance.SBI_EXECUTION_ID+"&user_id=-1&outputFormat="+exportType+"&inline=false";
	    var endUrl = baseUrl + docurlPar;
	   // alert ("endUrl: " + endUrl);
	    
		window.open(endUrl,'name','height=750,width=1000');
	}

	
	// ----------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------
	
	, init: function( config, doc ) {
		this.initToolbar(config);
		this.initNorthPanel(config);
		this.initCenterPanel(config, doc);
		this.initSouthPanel(config, doc);
	}
	
	, initToolbar: function( config ) {
		
		if (this.toolbarHiddenPreference) 
			return;
			
		this.toolbar = new Ext.Toolbar({
			items: ['']
		});
		
		this.toolbar.on('render', function() {
			
		}, this);
	}
	
	, initNorthPanel: function( config ) {
		Ext.apply(config, {pageNumber: 3}); // this let the ParametersPanel know that it is on execution page
		this.parametersPanel = new Sbi.execution.ParametersPanel(config);
		
		this.northPanel = new Ext.Panel({
				region:'north'
				, border: false
				, frame: false
				, collapsible: true
				, collapsed: true
				, hideCollapseTool: true
				, titleCollapse: true
				, collapseMode: 'mini'
				, split: true
				, autoScroll: true
				, height: 130
				, layout: 'fit'
				, items: [this.parametersPanel]
		});
	}
	
	, initCenterPanel: function( config, doc ) {
		this.miframe = new Ext.ux.ManagedIframePanel({
			region:'center'
	        , frameConfig : {
				// setting an initial iframe height in IE, to fix resize problem
				autoCreate : Ext.isIE ? {style: 'height:500'} : { },
				disableMessaging : false
	        }
			, defaultSrc: 'about:blank'
	        , loadMask  : true
	        //, fitToParent: true  // not valid in a layout
	        , disableMessaging :false
	        , listeners  : {
	        		
	        	'message:subobjectsaved': {
	        		fn: function(srcFrame, message) {
		        		this.shortcutsPanel.synchronizeSubobjects(this.executionInstance);
			        }
	        		, scope: this
	        	}
	        
	        	, 'message:crossnavigation' : {
	        		fn: function(srcFrame, message){
	                	var config = {
	                		document: {'label': message.data.label}
	            			, preferences: {
	                			parameters: message.data.parameters
	                			, subobject: {'name': message.data.subobject}
	                		}
	            	    };
	                	// workaround for document composition with a svg map on IE: when clicking on the map, this message is thrown
	                	// but we must invoke execCrossNavigation defined for document composition
	                	if (Ext.isIE && this.executionInstance.document.typeCode == 'DOCUMENT_COMPOSITE') {
	                		srcFrame.dom.contentWindow.execCrossNavigation(message.data.windowName, message.data.label, message.data.parameters);
	                	} else {
	                		this.fireEvent('crossnavigation', config);
	                	}
	        		}
	        		, scope: this
	            }
	        }
	    });
		
		if(doc.refreshSeconds !== undefined && doc.refreshSeconds > 0){
			this.refr = function(seconds) {
						this.miframe.getFrame().setSrc( null ); // refresh the iframe with the latest url
						this.refr.defer(seconds*1000, this,[seconds]);
					}
			this.refr.defer(doc.refreshSeconds*1000, this,[doc.refreshSeconds]);
		}
		
		
		this.miframe.on('documentloaded', function() {
			this.miframe.iframe.execScript("parent = document;", true);
			var scriptFn = 	"parent.execCrossNavigation = function(d,l,p,s) {" +
							"	sendMessage({'label': l, parameters: p, windowName: d, subobject: s},'crossnavigation');" +
							"};";
			this.miframe.iframe.execScript(scriptFn, true);
			this.miframe.iframe.execScript("uiType = 'ext';", true);
			
			// iframe resize when iframe content is reloaded
			if (Ext.isIE) {
				var aFrame = this.miframe.getFrame();
				aFrame.dom.style.height = this.miframe.getSize().height - 6;
			}

		}, this);
		
		this.miframe.on('resize', function() {
			if (Ext.isIE) {
				var aFrame = this.miframe.getFrame();
				aFrame.dom.style.height = this.miframe.getSize().height - 6;
			}

		}, this);
	}
	
	, initSouthPanel: function( config, doc ) {
		this.shortcutsPanel = new Sbi.execution.ShortcutsPanel(config, doc);
		
		var shortcutsHidden = (!Sbi.user.functionalities.contains('SeeViewpointsFunctionality') 
								&& !Sbi.user.functionalities.contains('SeeSnapshotsFunctionality') 
								&& !Sbi.user.functionalities.contains('SeeSubobjectsFunctionality'))
								||
								this.shortcutsHiddenPreference;
		
		var southPanelHeight = 
			(Sbi.settings && Sbi.settings.execution && Sbi.settings.execution.shortcutsPanel && Sbi.settings.execution.shortcutsPanel.height) 
			? Sbi.settings.execution.shortcutsPanel.height : 280;
		
		this.southPanel = new Ext.Panel({
			region:'south'
			, border: false
			, frame: false
			, collapsible: true
			, collapsed: true
			, hideCollapseTool: true
			, titleCollapse: true
			, collapseMode: 'mini'
			, split: true
			, autoScroll: true
			, height: southPanelHeight
			, layout: 'fit'
			, items: [this.shortcutsPanel]
			, hidden: shortcutsHidden
	    });
	}
});
