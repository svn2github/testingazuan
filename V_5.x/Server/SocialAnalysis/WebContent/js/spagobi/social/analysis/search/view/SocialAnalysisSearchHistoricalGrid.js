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
		            align: 'center'
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
		        }
		    ]}),
		
		this.callParent();
	}

});

