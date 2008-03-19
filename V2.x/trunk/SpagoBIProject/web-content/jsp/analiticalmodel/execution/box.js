/**
  * SpagoBI Core - box component
  * by Davide Zerbetto
  */

function createBox(title, content, renderTo) {
	Ext.onReady(function(){
	    var p = new Ext.Panel({
	    	title: title,
	        collapsible:false,
	        frame: true,
	        renderTo: renderTo,
	        contentEl: content
	    });
	    p.show();
	});
};