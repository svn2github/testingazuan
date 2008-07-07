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


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */


qx.Class.define("spagobi.ui.IconBar", {
	extend : qx.ui.layout.VerticalBoxLayout,
  	
    construct : function( config ) {   
	    this.base(arguments);
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
	
	members :  {
		
		_defaultBackgroudColor: 'white',
		_focusedBackgroudColor: '#DEFF83',
		
	    
	    addButton: function(buttonConfig) {
	    	//this._addButton(name,image,callback, context);
	    	this._addAtom(buttonConfig.name,buttonConfig.image, buttonConfig.handler, buttonConfig.context);
	 	},
	 	
	 	_addButton: function(name,image,callback, context) {	    
			var btn = new qx.ui.form.Button('',image);
			btn.addEventListener("execute", callback, context);
			this.add(btn);
			return btn;
	 	},
	 	
	 	_addAtom: function(name,image,callback, context) {	    
			var atom = new qx.ui.basic.Atom('', image);
			atom.setUserData('name', name);
			atom.setBackgroundColor('white');
			atom.addEventListener("mouseover", this._onmouseover);
    		atom.addEventListener("mouseout", this._onmouseout);
    		atom.addEventListener("mousedown", callback, context);
    		atom.addEventListener("keydown", this._onkeydown);
    		atom.addEventListener("keypress", this._onkeypress);
			
			this.add(atom);
	 	},
	 	
	 	_onmouseover: function(e) {
	 		
	 		e.getTarget().setBackgroundColor('#DEFF83');
	 	},
	 	
	 	_onmouseout: function(e) {
	 		e.getTarget().setBackgroundColor('white');
	 	},
	 	
	 	_onmousedown: function(e) {
	 		alert('_onmousedown');
	 	},
	 	
	 	_onkeydown: function(e) {},
	 	
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
