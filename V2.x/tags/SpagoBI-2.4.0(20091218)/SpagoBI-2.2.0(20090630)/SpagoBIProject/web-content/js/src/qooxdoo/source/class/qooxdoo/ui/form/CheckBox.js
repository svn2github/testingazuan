qx.Class.define("qooxdoo.ui.form.CheckBox",
{
  	extend : qooxdoo.ui.form.InputField,

  	/*
  	*****************************************************************************
     CONSTRUCTOR
  	*****************************************************************************
  	*/
  	/*Input Filed of type CheckBox A[X] B[] C[X] D[] E[] */

	construct : function(config) {
    	this.base(arguments, config);
  	},

  	members :
  	{
  		atom : [],
		label_text: [],
		check_box: [],
  	
  		/**
		 * Function to get the data of the checkbox list.
		 * <p> The function returns an array of strings corrosponding to the labels of the 
		 * checkboxes which are selected.
		 * @return Array of selected checkbox labels 
		 */
		getData: function() {
			alert('getDataCheckBox');
			var temp = [];
			for(i=0,j=0; i<this.atom.length; i++){
				if(this.atom[i].getUserData('field').getChecked() == true ){
					temp[j] = this.atom[i].getUserData('label').getValue();
					j++;
				}
			}
			return temp;
		},
  	
  		/**
		 * Function to set the data of the checkbox list.
		 * <p> The function sets those checkboxes to true whose labels are provided in the parameter.
		 * <p> Note that the existing selected checkboxes remain checked and are not reset.
		 * 
		 * @param value Array of strings containing the label of checkboxes which need to be set.
		 */
		setData: function(data) {
			alert('setDataCheckBox');
			for(j=0; j<this.atom.length; j++){
				this.atom[j].getUserData('field').setChecked(false);
			}	
			var flag = false;

			for(i=0; i<data.length; i++){
				for(j=0; j<this.atom.length; j++){
					if( data[i] == this.atom[j].getUserData('label').getContent() ){	//getValue
						this.atom[j].getUserData('field').setChecked(true);
						flag = true;
						break;
					}
				}
				if(flag == false){
					alert('Checkbox Label '+ data[i] + ' is not valid');
				}
				else
					flag = false;
				
				
			}
		}
  		
  		, _createField: function(config) {
        	
			var defaultConfig = {
	    		checked: false,
	        	top: 0,
	        	left: 0,
	        	items: [],
	        	listeners: [],
	        	columns: 1
	    	};
	    	config = qooxdoo.commons.CoreUtils.apply(defaultConfig, config);
			
			this.atom = [];
			this.label_text = [];
			this.check_box = [];
			var grid = new qx.ui.layout.Grid();
			this._field = new qx.ui.container.Composite(grid);
		    
			
			var cols = config.columns; 
			var rows = Math.ceil(config.items.length/config.columns);				
			var index = 0;

			for(i=0,k=0;i<rows ; i++){
				grid.setRowHeight(i, 30);		

				for(j=0; j<cols && k<config.items.length; j++,k++){
					grid.setColumnWidth(j, 50);	

					this.label_text[index] = new qx.ui.basic.Label(config.items[k]);
		    		this.check_box[index]  = new qx.ui.form.CheckBox();	

	   				var hBox = new qx.ui.layout.HBox();
	   				hBox.setSpacing(2);
	   				this.atom[index] = new qx.ui.container.Composite();
	   				this.atom[index].setLayout(hBox);
	   				
	   				this.atom[index].add(this.check_box[index]);
	   				this.atom[index].add(this.label_text[index].set( {alignY:"middle",alignX:"left"}));
	   				
	   				this.atom[index].setUserData('label', this.label_text[index]);
	    			this.atom[index].setUserData('field', this.check_box[index]);		

	   				this._field.add(this.atom[index],{row:i,column:j});
	   				index++;	
				}
		 	}  	            					
  		}
  	}
});