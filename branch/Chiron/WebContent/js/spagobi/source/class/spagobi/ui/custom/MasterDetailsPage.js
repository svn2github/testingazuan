/*

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

*/

/*
 * TODO generalize the constructor in this way ...
 * 
 * ... construct : function(records, form) {...}
 * 
 * and move the class in the parent package (i.e spagobi.ui)
 */

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */

/**
 * Class to create the page displayed on right side.
 * <p> The page contains a Filter bar, a list and a navigation bar on top and a form at the bottom.   
 */

qx.Class.define("spagobi.ui.custom.MasterDetailsPage",
{
  extend : qx.ui.splitpane.VerticalSplitPane,

  /**
   * Constructor to create the page on right side.
   * <p> It splits the given page into 2 parts: top and bottom.
   * <p> Top part - the Filter bar, list and navigation bar are added.
   * <p> Bottom part - the form (based on the button selected in vertical tool bar) is added.
   * 
   * <p>*Example*
   * <p><code>
   * var myForm = new spagobi.ui.custom.MasterDetailsPage('engine');
   * </code>
   * 
   * @param type The name for referring to the selected page. The selected values can be : 
   * (__'engine'__ / __'dataset'__ / __'datasource'__ /  __'mapmgmt'__ / __'featuremgmt'__)
   */
   	
  construct : function(type)
  {
    this.base(arguments, "1*", "2*");
    
    var listPage;
    var detailPage;
    var detailHeader;
    //var detailBody ;
    
    this.setEdge(0);
   	this.setBorder("line-left");
      		
    var records;
    var form;  	
    this._type = type;	
	if(type === 'engine') {
		records = spagobi.app.data.DataService.loadEngineRecords();
		//form = new spagobi.ui.custom.EngineDetailsForm(); 
		form = new spagobi.ui.custom.LOVDetailsForm(); 
	} else if(type === 'dataset') {
		records = spagobi.app.data.DataService.loadDatasetRecords();
		form = new spagobi.ui.custom.DatasetDetailsForm(); 
	} else if(type === 'datasource') {
		records = spagobi.app.data.DataService.loadDatasourceRecords();
		form = new spagobi.ui.custom.DatasourceDetailsForm(); 
	} else if(type == 'mapmgmt') {
		records = spagobi.app.data.DataService.loadMapRecords();
		form = new spagobi.ui.custom.MapDetailsForm(); 
	} else if(type == 'featuremgmt') {
		records = spagobi.app.data.DataService.loadFeatureRecords();
		form = new spagobi.ui.custom.FeatureDetailsForm(); 
	}
		
   	// Create list view
   	listPage = new spagobi.ui.Table(this, records );
   	//listPage = new spagobi.ui.PagedTable(this, records );
   	this.addTop( listPage );
      	
   	// Create detail view
   	detailPage = new qx.ui.pageview.buttonview.ButtonView();
    detailPage.setEdge(0);     
        
    // Create detail view toolbar
    var saveButton = new qx.ui.pageview.buttonview.Button("Save", "spagobi/img/spagobi/test/save.png");
    var deleteButton = new qx.ui.pageview.buttonview.Button("Delete", "spagobi/img/spagobi/test/delete.png");
    var createButton = new qx.ui.pageview.buttonview.Button("New", "spagobi/img/spagobi/test/create.png");                 
    detailPage.getBar().add(createButton, saveButton, deleteButton);   
        
    // Create detail view body
   	this._form = form;       	      	
   	detailHeader = new qx.ui.pageview.buttonview.Button("", "");
    detailHeader.setDisplay(false);        
    detailHeader.setChecked(true);  		
	this.detailBody = new qx.ui.pageview.buttonview.Page( detailHeader ); 
    this.detailBody.add( this._form );  		 
  	detailPage.getPane().add( this.detailBody );
  		
  	this.addBottom( detailPage );
  },

  members :
  {
    _form : undefined,
    detailBody : undefined,
    
     /**
     * Function to get the current form
     * 
     * @return form The selected form 
     */
    getForm: function() {
    	return this._form;
    },
    
    /**
     * Function to select the object
     * 
     * @param dataObject The data object
     */
    selectDataObject: function(dataObject) {
    	//alert(this._form + ' - ' + this._type + " ->\n " + this.printObject(dataObject));
    	this._form.setData(dataObject);
    },
    
    /**
     * Function to display the current form on the page
     */
    show: function() {
    	//this._form.setVisibility(false);
    	this.detailBody.remove(this._form)
    	//this._form.dispose();
    	if(this._type === 'engine') {
			//this._form = new spagobi.ui.custom.EngineDetailsForm(); 
			this._form = new spagobi.ui.custom.LOVDetailsForm(); 
		} else if(this._type === 'dataset') {
			this._form = new spagobi.ui.custom.DatasetDetailsForm(); 
		} else if(this._type === 'datasource') {
			this._form = new spagobi.ui.custom.DatasourceDetailsForm(); 
		} else if(this._type === 'mapmgmt') {
			this._form = new spagobi.ui.custom.MapDetailsForm();
		} else if(this._type === 'featuremgmt') {
			this._form = new spagobi.ui.custom.FeatureDetailsForm();
		}
		
		this.detailBody.add(this._form);
		if(!this.isVisibility()) {
			this.setVisibility(true);
		}
    },
    
    printObject: function(o) {
    	var str = '';
    	for(p in o) {
    		str += p + ': ' + o[p] + ';\n'
    	}
    	return str;
    }
  }
});