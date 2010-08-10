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
* This class defines the Left Vertical Bar of the the GUI which extends the 
* Qooxdoo's VerticalBoxLayout class.  
*/

qx.Class.define("spagobi.ui.IconBar", {
	//extend : qx.legacy.ui.layout.VerticalBoxLayout,
	extend : qx.ui.container.Composite,

	/** 
	*  Call to the constructor with the main "Config object" as its argument which contains the following  properties:
	* <p> defaultBackgroudColor -> This is the default background color of the button. 
	* <p> focusedBackgroudColor  -> This is the color of the background when a particular button is focussed.
	* <p> These properties are optional and if the user does not provide these value then the default values will be
	* <p> _selectedBackgroudColor: 'white' and _focusedBackgroudColor: '#DEFF83',
	* <p> *Example :-* 
	* <code>
	* <p> var a : { selectedBackgroudColor: "white", focusedBackgroudColor: null};
	* <p> var b = IconBar(a);
	* <code>
	* @param config (qx.core.object)
	*  
	*/  	
	construct : function( config ) {   
	    this.base(arguments);
	    //this.setWidth(70);
	    this.set({width: 68});
		var box = new qx.ui.layout.VBox();

		this.setLayout(box);
		this.set({allowGrowX : false});
		/*.set({
        width: 68,
    //    decorator: "main",
    //    backgroundColor: "yellow",
        allowGrowX: false
      });*/
//	    var d = new qx.ui.layout.VBox();
//	    d.allowGrowX(false);
	    
	    //change..added
	    
	    
	    if(config) {
	    	if(config.selectedBackgroudColor) {
		    	this._selectedBackgroudColor = config.selectedBackgroudColor;
	    	}
	    	
	    	if(config.focusedBackgroudColor) {
		    	this._focusedBackgroudColor = config.focusedBackgroudColor;
	    	}
	    	
	    	if(config.defaultSelectedPage) {
				this._defaultSelectedPageName = config.defaultSelectedPage;
			}
		    /*
		    if(config.buttons) {
			    for(var i = 0; i < config.buttons.length; i++) {
			    	this.addButton( config.buttons[i] );
			    }
		    }
		    */		    
	    }
	   
	   //this.add(this._container); 
	
	},
	

	members :  {
		
		_selectedBackgroudColor: '#DEFF83',
		_focusedBackgroudColor: 'gray',
		_defaultSelectedPageName : undefined,
		_checkedButton        : undefined,
	    //_container: undefined,//change..added
	    
		/**	  
		* This function adds the button on the vertical bar (the butons are actually atoms).
		* The argument to this function is a complex object namely buttonConfig which has the folowing properties.
		* <p> name     -> the name of the button by which it is identified
		* <p> image    -> The image of the button, as how the button will look.
		* <p> callback -> Callback is the function which will be executed when the button is clicked.
		* <p> context  -> Context is the scope to which the callback function will be executed 
		* <p> tooltip  ->This is the tooltip which is a text assciated with each button and is visible when the mouse goes over any button. 
		*
		*  @param buttonConfig (qx.core.object)
		*/    
	    addButton: function(buttonConfig) {
	    	this._addAtom(buttonConfig.name, buttonConfig.image, buttonConfig.handler, buttonConfig.context, buttonConfig.tooltip);
	 	},
	 	
	 	
	 	/*
	 	* <p> Definition of the funtion that adds atoms to the vertical bar with the folowing aruments.
	 	* <p> @param name (qx.legacy.ui.basic.Atom) -> the name of the button by which it is identified
	 	* <p> @param image (qx.legacy.ui.basic.Atom) -> The image of the button, as how the button will look.
	 	* <p> @param callback (function) -> Callback is the function which will be executed when the button is clicked.
	 	* <p> @param context (qx.legacy.ui.object) -> Context is the scope to which the callback function will be executed 
	 	* <p> @param tooltip (new qx.legacy.ui.popup ? null)->This is the tooltip which is a text assciated with each button and is visible when the mouse goes over any button. This field is optional
	 	*  and is only applicable when the user gives the argument for this field. 
	 	*  
	 	*/
	 	
	 	_addAtom: function(name, image, callback, context, tooltip) {	    
			//var atom = new qx.legacy.ui.basic.Atom('', image);//change
			var atom = new qx.ui.basic.Atom('', image);
			
			atom.setUserData('name', name);
			atom.setUserData('checked', false);
			if (name == this._defaultSelectedPageName)
			{
				//atom.setBackgroundColor(this._selectedBackgroudColor);
				this._check(atom);
			}
			if (tooltip){
				//var tt = new qx.legacy.ui.popup.ToolTip(tooltip);//change
				var tt = new qx.ui.tooltip.ToolTip(tooltip);
				
				atom.setToolTip(tt);
				//tt.setShowInterval(20);//change
				tt.setShowTimeout(20);
			}
			
			atom.addListener("mouseover",this._onmouseover, this);
    		atom.addListener("mouseout", this._onmouseout, this);
    		atom.addListener("mousedown", callback, context);
    		atom.addListener("mousedown",this._onmousedown, this);
    		//atom.addListener("keydown",  this._onkeydown, this);
    		atom.addListener("keypress", callback, context);//not working
			atom.addListener("keypress", this._onkeypress, this);
			
			//this.add(atom);//change
			this.add(atom);
		},
	 	
		/*
		*   This function checks the atom presently clicked
		*   and sets the backdround color to the color specified 
		*   and also retains the value of the event handler it was triggered  by.
		*  
		*   @param e (qx.legacy.event.type.event)e is the current instance of the event listener.
		*/	
	 	_check : function(button){	 	 
	 	    button.setUserData('checked', true);
	 		button.setBackgroundColor(this._selectedBackgroudColor);
	 		this._checkedButton = button;
	 	},
	 
	 	/*
	 	*   This function unchecks the atom presently clicked
	 	*   and sets the backdround color to null 
	 	*   and also changes the value of the "checked" property to false of 
	 	*   the previously checked button.
	 	*/	 	
	 	_uncheck : function(){ 
	 	  this._checkedButton.setBackgroundColor(null);
	 	  this._checkedButton.setUserData('checked', false);	 	
	 	},
	 
	 	/**
	 	* This function checks the button and sets the background color to focusedBackgroudColor(see constructor).
     	* If another button is checked, this function unchecks the previously checked button
     	* and sets the background of the previously checked button to defaultBackgroudColor (see constructor) and sets the background color of the current button.
     	* This function takes an argument "button" which is of type "event".
     	* @param e (qx.legacy.event.type.event)  -> e is the current instance of the event listener.
	 	*/	 
		select: function(button){
			if (!this._checkedButton) {
		 		this._check(button);
		 		
		 	} else {		 	
		 	  this._uncheck();
		 	  this._check(button);		 	  
		 	}	 	
	 	},
	 
	 	// This function is executed when a mouse down event occurs.
	    _onmousedown: function(e) {
	    	this.select(e.getTarget());
	 	},
	 	
	/* 
	 *   This function is executed when ever the user moves the mouse over a button. 
	 *   When a user moves the mouse over any button, this function first checks
	 *   whether the current button is checked already or not.
	 *   If checked, it does not do anything but if the current button is not checked  
	 *   it changes the background color of the button to white.
	 *   @param e (qx.legacy.event.type.event)e is the current instance of the event listener.
	 */	
	  	
	 _onmouseover: function(e) {	 		
	 	if (e.getTarget().getUserData('checked') == false) {	 		
	 		e.getTarget().setBackgroundColor(this._focusedBackgroudColor);
	 	}
	 },
	
	/* 
	 *   This function is executed when ever the user moves out the mouse from a button. 
	 *   When a user moves the mouse out the mouse, this function first checks
	 *   whether the current button is checked already or not.
	 *   If checked, it does not do anything but if the current button is not checked  
	 *   it changes the background color of the button from white to null.
	 *   @param e (qx.legacy.event.type.event) e is the current instance of the event listener.
	 */	
	 	
	   _onmouseout: function(e) {
	        if (e.getTarget().getUserData('checked') == false) {
	 			e.getTarget().setBackgroundColor(null);
	 		}
	 	},
	 /* 
	 *  This function is called whenever a user presses any key from the keyboard.
	 *  @param e (qx.legacy.event.type.event) e is the current instance of the event listener.
	 *
	 */	
	 	_onkeydown: function(e) {},
	 	
	 		 
	 /* 
	 *  This function is called whenever a user presses any key from the keyboard.
	 *  @param e (qx.legacy.event.type.event) e is the current instance of the event listener.
	 *
	 */	
	 
	 	
	 	_onkeypress: function(e) {
	 		
	 		//alert(e.getTarget().getLayoutParent());
			switch(e.getKeyIdentifier()) {
            case "Up":
              var vPrevious = true;
              break;
            case "Down":
              var vPrevious = false;
              break;
            default:
              return;
          	}
			/*change-old
		    var vChild =
		        (vPrevious
		         ? (e.getTarget().isFirstChild()
		            ? e.getTarget().getParent().getLastChild()
		            : e.getTarget().getPreviousSibling())
		         : (e.getTarget().isLastChild()
		            ? e.getTarget().getParent().getFirstChild()
		            : e.getTarget().getNextSibling()));
			*/
			//change-new ..not tested
			var atom = e.getTarget();
			var container = atom.getLayoutParent();
			var vChild;
			//alert(container.getChildren());
			/*
			var vChild =
		        (vPrevious
		         ? (atom == container.getChildren()[0]
		            ? container.getChildren()[container.getChildren().length - 1]
		            : container.getChildren()[container.getChildren().indexOf(atom)-1])
		         : (atom == container.getChildren()[container.getChildren().length - 1]
		            ? container.getChildren()[0]
		            : container.getChildren()[container.getChildren().indexOf(atom)+1]));
			*/
			if(vPrevious){
				if(atom == container.getChildren()[0]){
					vChild = container.getChildren()[container.getChildren().length - 1];
					
				}
				else{
					vChild = container.getChildren()[container.getChildren().indexOf(atom)-1];
					
				}
			}
			else{
				if(atom == container.getChildren()[container.getChildren().length - 1]){
					vChild = container.getChildren()[0];
					
				}
				else{
					vChild = container.getChildren()[container.getChildren().indexOf(atom)+1];
					
				}
			}
		    // focus next/previous button
		    //vChild.setFocused(true);
		    //vChild.setBackgroundColor(this._focusedBackgroudColor);
			this.select(vChild);
			atom.setBackgroundColor(null);
			 
	 	}
	 	
	}
	
});