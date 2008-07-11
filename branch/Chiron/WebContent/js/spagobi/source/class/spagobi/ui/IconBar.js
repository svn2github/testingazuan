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




qx.Class.define("spagobi.ui.IconBar", {
	extend : qx.ui.layout.VerticalBoxLayout,
  	
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
	
	members :  {
		
		_defaultBackgroudColor: 'white',
		_focusedBackgroudColor: '#DEFF83',
		_checkedButton: undefined,
	    
	    addButton: function(buttonConfig) {
	    	//this._addButton(name,image,callback, context,tooltip);
	    	//this._addButton(buttonConfig.name,buttonConfig.image, buttonConfig.handler, buttonConfig.context,buttonConfig.tooltip);
	    	this._addAtom(buttonConfig.name,buttonConfig.image, buttonConfig.handler, buttonConfig.context,buttonConfig.tooltip);
	 	},
	 	/*
	 	_addButton: function(name,image,callback, context,tooltip) {	    
			var btn = new qx.ui.form.Button('',image);
			btn.setUserData('name', name);
			btn.setUserData('checked', false);
			var tt = new qx.ui.popup.ToolTip(tooltip);
			btn.setToolTip(tt);
			tt.setShowInterval(20);
			var border = new qx.ui.core.Border(0);
			btn.setBorder(border);
			btn.addEventListener("execute", callback, context);
			btn.addEventListener("execute", this.attack, this);//callback, context);
			btn.addEventListener("mouseover",this._onmouseover);
			btn.addEventListener("mouseout", this._onmouseout);
			
			this.add(btn);
			return btn;
	 	},
	 	
	 	check : function(e){
	 	
	 	    e.getTarget().checked = true;
	 		e.getTarget().setBackgroundColor('#DEFF83');
	 		this._checkedButton = e.getTarget();
	 	},
	 	
	 	uncheck : function(){ 
	 	
	 	  this._checkedButton.setBackgroundColor(null);
	 	  this._checkedButton.checked = false;
	 	  
	 	
	 	},
	 	
	 	attack: function(e){
	 	
	 	if (!this._checkedButton) {
	 		this.check(e);
	 		
	 	} else {
	 	
	 	  this.uncheck();
	 	  this.check(e);
 	   }
	    
       },
       
       _onmouseover: function(e) {	 		
	 		if (e.getTarget().checked == false) {
	 			e.getTarget().setBackgroundColor('white');
	 		}
	 	},
	 	
	   _onmouseout: function(e) {
	        if (e.getTarget().checked == false) {
	 		e.getTarget().setBackgroundColor(null);
	 		}
	 	},
	 	*/
	 	_addAtom: function(name,image,callback, context,tooltip) {	    
			var atom = new qx.ui.basic.Atom('', image);
			atom.setUserData('name', name);
			atom.setUserData('checked', false);
			var tt = new qx.ui.popup.ToolTip(tooltip);
			atom.setToolTip(tt);
			tt.setShowInterval(20);
			//atom.setBackgroundColor('white');
			atom.addEventListener("mouseover",this._onmouseover);
    		atom.addEventListener("mouseout", this._onmouseout);
    		atom.addEventListener("mousedown", callback, context);
    		atom.addEventListener("mousedown",this.attack, this);///*{if ("mousedown"){ this.atom.setChecked(true);*/callback/*}}*/, context);
    		atom.addEventListener("keydown",  this._onkeydown);
    		atom.addEventListener("keypress", this._onkeypress);
			
			this.add(atom);
	 	},
	 	
	 	
	 check : function(e){
	 	 
	 	    e.getTarget().setUserData('checked', true);
	 		e.getTarget().setBackgroundColor('#DEFF83');
	 		this._checkedButton = e.getTarget();
	 	},
	 	
	 	uncheck : function(){ 
	 	
	 	  this._checkedButton.setBackgroundColor(null);
	 	  this._checkedButton.setUserData('checked', false);
	 	  
	 	
	 	},
	 	
	 	attack: function(e){
	 	
	 	if (!this._checkedButton) {
	 		this.check(e);
	 		
	 	} else {
	 	
	 	  this.uncheck();
	 	  this.check(e);
	 	  
	 	  
	 	}
	 	
	 },
	 	
	 	
	 	 _onmouseover: function(e) {	 		
	 		if (e.getTarget().getUserData('checked') == false) {
	 			e.getTarget().setBackgroundColor('gray');
	 		}
	 	},
	 	
	   _onmouseout: function(e) {
	        if (e.getTarget().getUserData('checked') == false) {
	 		e.getTarget().setBackgroundColor(null);
	 		}
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
			e.getTarget().setBackgroundColor('gray');
	 	}
	 	
	}
	
});
