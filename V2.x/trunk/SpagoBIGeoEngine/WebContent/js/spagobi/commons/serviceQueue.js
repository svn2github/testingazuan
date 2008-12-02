/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
 
/**
  * Object's name - short description
  * 
  * Object documentation ...
  * 
  * by Andrea Gioia (andrea.gioia@eng.it)
  */

Sbi.commons.ServiceQueue = function(config) {
	
	this.serviceQueue = [];
	
	Ext.apply(this, config);
	
	this.addEvents();	
	
	// constructor
    Sbi.commons.ServiceQueue.superclass.constructor.call(this);
};

Ext.extend(Sbi.commons.ServiceQueue, Ext.util.Observable, {
    
    // static contens and methods definitions
   
   
    // public methods
    add : function(serviceConfig) {
    	this.serviceQueue.push( serviceConfig );
    }
    
    , run : function() {
    	this.runNext();
    }
    
    , runNext : function(serviceResponse, serviceConfig) {
    	var nextServiceConfig = serviceQueue.pop();
    	nextServiceConfig.success = this.runNext;
    	
    	Ext.Ajax.request( nextServiceConfig );  
    	
    	//this.fireEvent("requestcomplete", this, response, options);
        //Ext.callback(options.success, options.scope, [response, options]);   
    }
});