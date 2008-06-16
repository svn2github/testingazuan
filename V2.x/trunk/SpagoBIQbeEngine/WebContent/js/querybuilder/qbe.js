/**

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

**/

/**
  * Qbe main script file
  * by Andrea Gioia
  */
 
// reference local blank image
Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';

 
// create namespace
Ext.namespace('it.eng.spagobi.engines.qbe');
 
// create application
it.eng.spagobi.engines.qbe.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
    var tabs = [];
    var tabPanel; 
    var query;

 
    // public space
    return {
        // public properties, e.g. strings to translate
        
        // public methods
        init: function() {
            Ext.QuickTips.init();
            
            it.eng.spagobi.engines.qbe.locale.module.init();
            it.eng.spagobi.engines.qbe.locale.module.applyLocale();
           
            tabs[0] = getQueryBuilderPanel(query);
            tabs[1] = getQueryResultsPanel(); 
            //tabs[2] = createGridNodi();          
            
            // Main (Tabbed) Panel            
            tabPanel = new Ext.TabPanel({
          		region:'center',
          		deferredRender:false,
          		autoScroll: true, 
          		margins:'0 4 4 0',
          		activeTab:0,
          		items: tabs        
            });     
                        
            // Configure viewport
            var viewport = new Ext.Viewport({
              layout:'border',
              items:[tabPanel]}); 
                   
        },
        
        setQuery: function(q) {
        	query = q;
        },
        
        activateTab:function() {
        	tabPanel.activate(tabs[1]);
        }
    };
}(); // end of app
 
// end of file
