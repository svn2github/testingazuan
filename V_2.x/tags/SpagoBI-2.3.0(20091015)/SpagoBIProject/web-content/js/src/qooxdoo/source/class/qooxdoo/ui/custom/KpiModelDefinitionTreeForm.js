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
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it), 
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 */

/**
* This class defines the KPI Model Definition Form. 
* 
*/

qx.Class.define("qooxdoo.ui.custom.KpiModelDefinitionTreeForm", {
	extend: qooxdoo.ui.custom.FunctionalClassDummy,
	
	/**
	 * 
	 */
	
	construct : function() { 
				
		this.base(arguments);
		
	},
	
	members:
	{
		createRightSideForm: function(){	//function over-riding
			var form = new qooxdoo.ui.custom.KpiModelDefinitionDetailForm();
			this.getRightPart()._add(form);
			this.getRightPart().setUserData('form',form);
		},
		
		showFormData: function(){			//function over-riding
			
			this.clearAll();
			var nodeData = {};
			
			
			 if(this._tree.getSelectedItem() == this._tree.getRoot()){
  				nodeData.tree = this._tree.getRoot(); // Remember change this._tree to this._tree.getRoot()
  			 }
  		     else{
  		     
  		     	nodeData = this._tree.getNodeData();
  		     	
  		     	// loadTreeData() of DataService.js should have the form fields
  		     	// This is because nodeData gets data from atom.getUserData('data') inside tree.getNodeData()...
  		     	//... which gets from atom.setUserData('data', config.data) of tree.addNode() ...
  		     	//... which from loadTreeMeta() of DataService.js
  		     	//... whose 'data' property is set from loadTreeData() of DataService.js
  		     	
  		     	/*
  		     	//to be removed when actual data is available
  		     	if( nodeData.data.typename == undefined){
  		     		var def_nodeData = {
							id: '171',
		                    code: 'INDICATORI CSP',
		                    name: 'INDICATORI CSP',
		                    description: 'INDICATORI CSP',
		                    typename: 'GQM root',
		                    typedescription :'null',
		                    kpiname : 'Numero Alarmi CSP'
						   };
  		     		nodeData.data = qooxdoo.commons.CoreUtils.apply(def_nodeData, nodeData.data);
  		     	}
  		     	//qooxdoo.commons.CoreUtils.dump(nodeData.data);
  		     	*/
  		     }
			 var f = this.getRightPart().getUserData('form');
			 var o = [];
	    		for(var prop in f.dataMappings){
	    			o[prop] = nodeData.data;
	    		}	
	    	 f.setData(o);
			 //f.setData(nodeData.data);
			 
			/*
			var nodeData = {
							id: '171',
		                    code: 'INDICATORI CSP',
		                    name: 'INDICATORI CSP',
		                    description: 'INDICATORI CSP',
		                    typename: 'GQM root',
		                    typedescription :'null',
		                    kpiname : 'Numero Alarmi CSP'
						   };
			var f = this.getRightPart().getUserData('form');
			
			//same as in selectDataObject() of MasterDetailsPage
			var o = [];
    		for(var prop in f.dataMappings){
    			o[prop] = nodeData;
    		}	
    		f.setData(o);
    		*/
		},
		
		clearAll: function(e){
			//alert("hi");
			var nodeData = {
					//id: '171',
                    code: '',
                    name: '',
                    description: '',
                    typename: '',
                    typedescription :'',
                    kpiname : ''
				   };
			var f = this.getRightPart().getUserData('form');
			var o = [];
    		for(var prop in f.dataMappings){
    			o[prop] = nodeData;
    		}	
    		f.setData(o);
		},
		
		createNode: function(e){
			
			var f = this.getRightPart().getUserData('form');
			
			//same as in Save function below
			var temp_engine = f.getData();
    		var engine = {};
    		//qooxdoo.commons.CoreUtils.dump(temp_engine);
    		for(prop in temp_engine){				// parse the group-boxes
    			for(sub_prop in temp_engine[prop]){	// parse the form inside groupbox
    				engine[sub_prop] = temp_engine[prop][sub_prop];
    			}
    		}
    		//qooxdoo.commons.CoreUtils.dump(engine);
			var parent = this._tree.getSelectedItem();
			var nodeObject = {};
			nodeObject.name = engine['name'];
				//nodeObject.parent = this._tree.getUserData(parent.getLabel());	//nodeid = label .. to be changed
			nodeObject.parent = parent.getLabel();
			nodeObject.data = engine;
			
			var treeNode = this._tree.addNode(nodeObject);
			this._tree.select(treeNode.getUserData('node'));
			/*
			var o = [];
    		for(var prop in f.dataMappings){
    			o[prop] = nodeData;
    		}	
    		f.getData();
    		*/
		},
		
		save: function (e) {
			var f = this.getRightPart().getUserData('form');
			
			// Same as in Save function in MasterDetails page
			var temp_engine = f.getData();
			//qooxdoo.commons.CoreUtils.dump(temp_engine);
    		var engine = {};
    		
    		for(prop in temp_engine){				// parse the group-boxes
    			for(sub_prop in temp_engine[prop]){	// parse the form inside groupbox
    				engine[sub_prop] = temp_engine[prop][sub_prop];
    			}
    		}
    		
    		qooxdoo.commons.CoreUtils.dump(engine);
    		//this.clearAll();
		}	
	}	

});