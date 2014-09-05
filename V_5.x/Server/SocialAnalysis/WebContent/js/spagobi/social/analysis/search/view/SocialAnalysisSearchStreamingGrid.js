/** SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

/**
 * 
 * Search Form
 * 
 *     
 *  @author
 *  Giorgio Federici (giorgio.federici@eng.it)
 */


Ext.define('Sbi.social.analysis.search.view.SocialAnalysisSearchStreamingGrid', {
	extend: 'Ext.grid.Panel',
	
	title: 'Continuos scanning',
	titleAlign: 'center',
	flex: 1,

	config:
	{

	},
	
	

	constructor : function(config) {
		this.initConfig(config||{});
		
		this.store = Ext.create('Sbi.social.analysis.search.store.StreamingSearchStore', { });
		
		this.callParent(arguments);
	},

	initComponent: function() {
		Ext.apply(this, {
			columns: [
		        {
		            text: 'ID',
		            width: 100,
		            dataIndex: 'searchID'
		        },
		        {
		            text: 'Label',
		            width: 100,
		            dataIndex: 'label'
		        },
		        {
		            text: 'Keywords',
		            width: 100,
		            dataIndex: 'keywords'
		        },
		        {
		            text: 'Last Activation',
		            width: 100,
		            dataIndex: 'lastActivationTime',
		            renderer : Ext.util.Format.dateRenderer('m/d/Y H:i')
		        },
		        {
		            text: 'Accounts to monitor',
		            width: 200,
		            dataIndex: 'accounts',
		        },
		        {
		            text: 'Resources to monitor',
		            width: 200,
		            dataIndex: 'links',
		        },
		        {
		        	xtype: 'actioncolumn',
		            text: 'Start/Stop',
		            width: 100,
		            dataIndex: 'loading',
		            align: 'center',
		            getClass: function(value, metadata, record)
		            {
		            	var searchLoading = record.get('loading');

		            	if(!searchLoading)
		            	{
		            	    return 'x-streaming-start'; 
		            	} else {
		            	    return 'x-streaming-stop';               
		            	}
		            },
		            handler: function(grid, rowIndex, colIndex) {
	                    var rec = grid.getStore().getAt(rowIndex);
	                    var loadingValue = rec.get('loading');
	                    
	                    if(loadingValue)
                    	{
	                    	//stop code
                    	}
	                    else
                    	{
	                    	//start code
	                    	//grid.getStore().save();
	                    	Ext.Msg.show({
	                    	     title:'Confirm',
	                    	     msg: 'Starting this Stream will stop the other one active. Are you sure?',
	                    	     buttons: Ext.Msg.YESNO,
	                    	     icon: Ext.Msg.QUESTION,
	                    	     fn: function(btn, text){
                                     if (btn == 'yes'){
                                    	 Ext.Ajax.request({
             	                            url : 'restful-services/streamingSearch',
             	                            method:'POST', 
             	                            params : {
             	                                searchID: Ext.encode(rec.get('searchID')),
             	                                keywords: Ext.encode(rec.get('keywords'))
             	                            },
             	                            scope : this
                                    	 }); 
                                     
                                     }
                                     if (btn == 'no'){
                                    	 //do nothing
                                     }
                                 },
	                    	     icon: Ext.Msg.QUESTION
	                    	});
//	                    	
                    	}
		            }
	            },
		        {
		            xtype: 'actioncolumn',
		            text: 'Delete',
		            icon: 'img/delete.png',
		            align: 'center'
		        },
		        {
		        	xtype: 'actioncolumn',
		            text: 'Analyse',
		            width: 100,
		            dataIndex: 'loading',
		            align: 'center',
		            icon: 'img/analysis.png',
		            handler: function(grid, rowIndex, colIndex) {
	                    var rec = grid.getStore().getAt(rowIndex);
		            	window.location.href = "summary.jsp?searchID="+rec.get('searchID');
	                }
		        }
		    ]}),
		
		this.callParent();
	}

});

