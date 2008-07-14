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
* This class defines the Left Vertical Bar of the the GUI
*/

qx.Class.define("spagobi.ui.IconBar", {
	extend : qx.ui.layout.VerticalBoxLayout,

	/** 
	*  Call to the constructor with the main Config object as its argument which contains the following  properties
	*  defaultBackgroudColor -> This is the default background color of the button. 
	*  focusedBackgroudColor -> This is the color of the background when a particular button is focussed.
	*  @param config (qx.ui.object)
	*  
	*/  	
    construct : function( config ) {   
	    this.base(arguments);
	    this.setSpacing(0);
	    if(config) {
	    	if(config.defaultBackgroudColor) {
		    	this._defaultBackgroudColor = config.defaultBackgroudColor;
		    	this.setBackgroundColor(this._defaultBackgroudColor);
	    	}
	    	
	    	if(config.focusedBackgroudColor) {
		    	this._focusedBackgroudColor = config.focusedBackgroudColor;
	    	}
		    
		    if(config.buttons) {
			    for(var i = 0; i < config.buttons.length; i++) {
			    	this.addButton( config.buttons[i] );
			    }
		    }		    
	    }
	},
	
	/**
	* Global members of the class
	*  _focusedBackgroudColor
	*  _defaultBackgroudColor
	*  _checkedButton
	*/
	members :  {
		
		_defaultBackgroudColor: 'white',
		_focusedBackgroudColor: '#DEFF83',
		_checkedButton: undefined,
	   
		/**	  
		* This function adds the button on the vertical bar (the butons are actually atoms) by calling another function _addAtom().
		* The argument to this function is the complex object namely buttonConfig which has the folowing properties
		* name, image, handler (function),  context (scope object), tooltip, defaultBackgroudColor, focusedBackgroudColor. 
		* All these properties are explained in brief at the function definition.<b> 
		* @param buttonConfig (qx.ui.Object)
		*/    
	    addButton: function(buttonConfig) {
	    		    
	    	/**
	    	*  Call to the funtion which adds the atoms to the bar
	    	*/
	    	
	    	this._addAtom(buttonConfig.name,buttonConfig.image, buttonConfig.handler, buttonConfig.context,buttonConfig.tooltip);
	 	},
	 	
	 	
	 	/**
	 	*  Definition of the funtion that adds atoms to the vertical bar with the folowing aruments.
	 	*  @param name (qx.ui.basic.Atom) -> the name of the button by which it is identified
	 	*  @param image (qx.ui.basic.Atom) -> The image of the button, as how the button will look.
	 	*  @param handler (function) -> handler is the callback which will be executed when the button is clicked.
	 	*  @param context (qx.ui.object) -> Context is the scope to which the callback function will be executed 
	 	*  @param tooltip (new qx.ui.popup)->This is the tooltip which is a text assciated with each button and is visible when the mouse goes over any button. 
	 	*  
	 	*/	 	
	 	_addAtom: function(name, image, handler, context, tooltip) {	    
			var atom = new qx.ui.basic.Atom('', image);
			atom.setUserData('name', name);
			atom.setUserData('checked', false);
			var tt = new qx.ui.popup.ToolTip(tooltip);
			atom.setToolTip(tt);
			tt.setShowInterval(20);
			atom.addEventListener("mouseover",this._onmouseover);
    		atom.addEventListener("mouseout", this._onmouseout);
    		atom.addEventListener("mousedown", handler, context);
    		atom.addEventListener("mousedown",this.select, this);
    		atom.addEventListener("keydown",  this._onkeydown);
    		atom.addEventListener("keypress", this._onkeypress);
			
			this.add(atom);
	 	},
	 	
		 /**
		  *   This function checks the atom presently clicked
		  *   and sets the backdround color to the color specified 
		  *   and also retains the value of the event handler it was triggered  by.
		  *   @param e (qx.event.type.event)e is the current instance of the event listener.
		  */	
		 check : function(e){
		 	 
		 	    e.getTarget().setUserData('checked', true);
		 		e.getTarget().setBackgroundColor('#DEFF83');
		 		this._checkedButton = e.getTarget();
		 },
	 
		/**
		 *   This function unchecks the atom presently clicked
		 *   and sets the backdround color to null 
		 *   and also changes the value of the "checked" property to false of 
		 *   the previously checked button.
		 */	
	 	uncheck : function(){ 
	 	
	 	  this._checkedButton.setBackgroundColor(null);
	 	  this._checkedButton.setUserData('checked', false);
	 	  
	 	
	 	},
	 	
		/**
		 *   This function calls the check and uncheck functions defined above. 
		 *   It calls the function check when ever the value of the Global variable
		 *   "_checkedButton" is undefined pasing the parameter "e" which is the event
		 *   listener of the current button being clicked as the argument  
		 *   otherwise calls the uncheck function
		 *   @param e (qx.event.type.event)e is the current instance of the event listener.
		 */	
	 	select: function(e){	 	
		 	if (!this._checkedButton) {
		 		this.check(e);		 		
		 	} else {		 	
		 	  this.uncheck();
		 	  this.check(e);		 	  
		 	}
	 	
		},
	 	
		/* *
		 *   This function is executed when ever the user moves the mouse over a button. 
		 *   When a user moves the mouse over any button, this function first checks
		 *   whether the current button is checked already or not.
		 *   If checked, it does not do anything but if the current button is not checked  
		 *   it changes the background color of the button to white.
		 *   @param e (qx.event.type.event)e is the current instance of the event listener.
		 */		  	
	 	 _onmouseover: function(e) {	 		
	 		if (e.getTarget().getUserData('checked') == false) {
	 			e.getTarget().setBackgroundColor('white');
	 		}
	 	},
	
		/* *
		 *   This function is executed when ever the user moves out the mouse from a button. 
		 *   When a user moves the mouse out the mouse, this function first checks
		 *   whether the current button is checked already or not.
		 *   If checked, it does not do anything but if the current button is not checked  
		 *   it changes the background color of the button from white to null.
		 *   @param e (qx.event.type.event) e is the current instance of the event listener.
		 */	
		_onmouseout: function(e) {
	        if (e.getTarget().getUserData('checked') == false) {
	 		e.getTarget().setBackgroundColor(null);
	 		}
	 	},
	 	
		/* *
		 *  This function is called whenever a user presses any key from the keyboard.
		 *  @param e (qx.event.type.event) e is the current instance of the event listener.
		 *
		 */	
	 	_onkeydown: function(e) {},
	 
		/* *
		 *  This function is called whenever a user presses any key from the keyboard.
		 *  @param e (qx.event.type.event) e is the current instance of the event listener.
		 *
		 */	 	
	 	_onkeypress: function(e) {
	 		
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

		    var vChild =
		        (vPrevious
		         ? (e.getTarget().isFirstChild()
		            ? e.getTarget().getParent().getLastChild()
		            : e.getTarget().getPreviousSibling())
		         : (e.getTarget().isLastChild()
		            ? e.getTarget().getParent().getFirstChild()
		            : e.getTarget().getNextSibling()));
		
		    // focus next/previous button
		    vChild.setFocused(true);
			vChild.setBackgroundColor('#DEFF83');
			e.getTarget().setBackgroundColor('white');
	 	}
	 	
	}
	
});
