/* *

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

* */

/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 
 */
 
 qx.Class.define("qooxdoo.ui.DistributionList",
{
  //extend : qx.legacy.ui.splitpane.VerticalSplitPane,
  extend : qx.ui.window.Window,
  
  construct : function(type, data)
  {
	 
	
	if (type.getColumn() <= 1){
	 return ;
	}
  	else {
  		
  		this.base(arguments,null, "qx/icon/Oxygen/16/apps/office-calendar.png");
	    this.setLayout(new qx.ui.layout.VBox(10));
	    this.setShowStatusbar(true);
		this.setStatus("Details loaded");
		this.addListener("resize", function(){ this.center();}, this);
		this.open();
		this.setModal(true);
		
		if (type.getColumn() == 2){
			
			this.setCaption("Details Window");
			var textfield1 = qooxdoo.commons.WidgetUtils.createInputTextField({
        		type: 'text',
        		dataIndex: 'name',
        		text: 'Name',
        		labelwidth: 300,
        		mandatory: false	
        	});
			
			(textfield1.getUserData('field').getChildren()[0]).setValue(data.name);
			 this.add(textfield1);

			var textfield2 = qooxdoo.commons.WidgetUtils.createInputTextField({
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Description',
        		labelwidth: 300,
        		mandatory: false
        	});
			
			(textfield2.getUserData('field').getChildren()[0]).setValue(data.description);
			this.add(textfield2);
			
			var container = new qx.ui.container.Composite(new qx.ui.layout.HBox);
			container.getLayout().setAlignX("center");
			var centralLabel = new qx.ui.basic.Label("Documents Related");
			container.add(centralLabel);
			this.add(container);
			
			var records = qooxdoo.app.data.DataService.loadDistributionSublistRecords();
			this.config.dataset = records;
			var window_table = new qooxdoo.ui.table.Table(this, this.config);
			window_table.getSelectionModel().removeListener("changeSelection",window_table._onChangeSelection, window_table);
			this.add(window_table);
			
	}
		else {
			this.setCaption("Emails Window");
			
			var container1 = new qx.ui.container.Composite(new qx.ui.layout.HBox(5));
			container1.getLayout().setAlignX("left");
			var Label1 = new qx.ui.basic.Label("Subscription List:");
			container1.add(Label1);
		//	alert(data.name);
			var Label2 = new qx.ui.basic.Label(data.name);
			container1.add(Label2);	
			this.add(container1);
			var label3 = new qx.ui.basic.Label("Insert email in order to subscribe: ");
			this.add(label3);
			this.textfield3 = qooxdoo.commons.WidgetUtils.createInputTextField({
        		type: 'text',
        		dataIndex: 'description',
        		text: 'Email',
        		labelwidth: 50,
        		mandatory: false
        	});
			
			this.add(this.textfield3);
			
			var container2 = new qx.ui.container.Composite(new qx.ui.layout.HBox(20));
			
			var saveButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/save.png"));
		    var saveToolTip = new qx.ui.tooltip.ToolTip("Save");
		    saveButton.setToolTip(saveToolTip);
		    saveButton.addListener("execute", this._onSave, this);
		    container2.add(saveButton);
		    var deleteButton = new qx.ui.toolbar.Button("", qx.util.AliasManager.getInstance().resolve("spagobi/img/spagobi/test/delete.png"));
		    var deleteToolTip = new qx.ui.tooltip.ToolTip("Delete");
		    deleteButton.setToolTip(deleteToolTip);
		    deleteButton.addListener("execute", this._onDelete, this);
		    container2.add(deleteButton);
		    this.add(container2);
			
		}
  	 }
  },
  
  members :
  {
	  config : {},
	  textfield3 : undefined,
	  
	   _onDelete: function(e) {
	    	alert('delete');
	    }
	    
	  , _onSave: function(e) {
		  var d = this.textfield3.getUserData('field').getChildren()[0];
	    	alert(d.getValue());
	    }
	  
  }
 
});  