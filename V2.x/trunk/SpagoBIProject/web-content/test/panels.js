/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
    var p1 = new Ext.Panel({
        bodyBorder : true,
        collapsible:true,
        renderTo: 'container_1',
        width:'auto'
    });
    p1.show(this);
		p1.load({
		    url: 'http://localhost:8080/SpagoBI/test/panel_1.html',
		    discardUrl: false,
		    nocache: false,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: true
		});   
    
    var p2 = new Ext.Panel({
        id:'pannello_2',
        bodyBorder : true,
        collapsible:true,
        renderTo: 'container_2',
        width:'auto'
    });
    p2.show(this);
		p2.load({
		    url: 'http://localhost:8080/SpagoBI/test/panel_2.html',
		    discardUrl: false,
		    nocache: false,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: true
		}); 
    
    var p3 = new Ext.Panel({
        bodyBorder : true,
        collapsible:true,
        renderTo: 'container_3',
        width:'auto'
    });
    p3.show(this);
		p3.load({
		    url: 'http://localhost:8080/SpagoBI/test/panel_3.html',
		    discardUrl: false,
		    nocache: false,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: true
		});  
    
    
    var bottone_refresh=Ext.get('refresh');
    bottone_refresh.on('click', function(){
		  p2.load({
		    url: 'http://localhost:8080/SpagoBI/test/panel_2.html',
		    discardUrl: true,
		    nocache: true,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: true
		  }); 
 
    });
               
});
