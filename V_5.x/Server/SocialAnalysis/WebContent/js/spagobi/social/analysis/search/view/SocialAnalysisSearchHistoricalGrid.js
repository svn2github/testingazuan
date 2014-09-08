/** SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

/**
 * 
 * Grid for the historical search
 * 
 *     
 *  @author
 *  Giorgio Federici (giorgio.federici@eng.it)
 */


Ext.define('Sbi.social.analysis.search.view.SocialAnalysisSearchHistoricalGrid', {
	extend: 'Ext.grid.Panel',
	
	title: 'Timely scanning',
	titleAlign: 'center',
	flex: 1,
	margin: '10 0 10 0',

	config:
	{

	},	

	constructor : function(config) {
		this.initConfig(config||{});
		
		this.store = Ext.create('Sbi.social.analysis.search.store.HistoricalSearchStore', { });
		
		var Search = Ext.ModelMgr.getModel('Sbi.social.analysis.search.model.SearchModel');
		//Search.load();
		
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
		            text: 'Frequency',
		            width: 100,
		            dataIndex: 'frequency',
		        },
		        {
		            xtype: 'actioncolumn',
		            text: 'Delete',
		            icon: 'img/delete.png',
		            align: 'center',
		            handler: function(grid, rowIndex, colIndex) {
	                    
		            	var rec = grid.getStore().getAt(rowIndex);
	              
                    	Ext.Msg.show({
                    	     title:'Confirm',
                    	     msg: 'You are deleting this search. Are you sure?',
                    	     buttons: Ext.Msg.YESNO,
                    	     icon: Ext.Msg.QUESTION,
                    	     fn: function(btn, text){
                                 if (btn == 'yes'){
                                	 Ext.Ajax.request({
         	                            url : 'restful-services/historicalSearch/deleteSearch',
         	                            method:'POST', 
         	                            params : {
         	                                searchID: Ext.encode(rec.get('searchID'))
         	                            },
         	                            scope : this,
         	                           success: function(response)
         	                           {
         	                        	  Ext.Msg.alert('Success', action.result.msg);
         	                           }
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
		        },
		        {
		        	xtype: 'actioncolumn',
		            text: 'Analyse',
		            width: 100,
		            dataIndex: 'loading',
		            align: 'center',
		            getClass: function(value, metadata, record)
		            {
		            	var searchLoading = record.get('loading');

		            	if(!searchLoading)
		            	{
		            	    return 'x-analysis-display'; 
		            	} else {
		            	    return 'x-analysis-loading';               
		            	}
		            },
		            isDisabled: function(view, rowIndex, colIndex, item, record)
		            {
		            	var searchLoading = record.get('loading');
		            	if(!searchLoading)
	            		{
		            		return false;	            		
	            		}
		            	else
	            		{
		            		return true;
	            		}
		            },
		            handler: function(grid, rowIndex, colIndex) {
	                    var rec = grid.getStore().getAt(rowIndex);
		            	window.location.href = "summary.jsp?searchID="+rec.get('searchID');
	                }		            
		        },
		        {
		        	xtype: 'actioncolumn',
		            text: 'Scheduler',
		            width: 100,
		            dataIndex: 'hasSearchScheduler',
		            align: 'center',
		            getClass: function(value, metadata, record)
		            {
		            	var searchScheduler = record.get('hasSearchScheduler');

		            	if(searchScheduler)
		            	{
		            	    return 'x-scheduler-stop-enabled'; 
		            	} else {
		            	    return 'x-scheduler-stop-disabled';               
		            	}
		            },
//		            isDisabled: function(view, rowIndex, colIndex, item, record)
//		            {
//		            	var searchScheduler = record.get('hasSearchScheduler');
//		            	if(!searchScheduler)
//	            		{
//		            		return true;	            		
//	            		}
//		            	else
//	            		{
//		            		return false;
//	            		}
//		            },
		            handler: function(grid, rowIndex, colIndex) {
	                    var rec = grid.getStore().getAt(rowIndex);
	                    var searchSchedulerValue = rec.get('hasSearchScheduler');
	                    
	                    if(!searchSchedulerValue)
                    	{
	                    	//stop code
                    	}
	                    else
                    	{
	                    	//stop scheduler code
	                    	Ext.Msg.show({
	                    	     title:'Confirm',
	                    	     msg: 'You are stopping the search scheduler. Are you sure?',
	                    	     buttons: Ext.Msg.YESNO,
	                    	     icon: Ext.Msg.QUESTION,
	                    	     fn: function(btn, text){
                                     if (btn == 'yes'){
                                    	 Ext.Ajax.request({
             	                            url : 'restful-services/historicalSearch/stopSearchScheduler',
             	                            method:'POST', 
             	                            params : {
             	                                searchID: Ext.encode(rec.get('searchID'))
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
		        }
		    ]}),
		
		this.callParent();
	}

});

