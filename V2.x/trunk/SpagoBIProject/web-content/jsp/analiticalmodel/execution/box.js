/**
  * SpagoBI Core - box component
  * by Davide Zerbetto
  */

function createBox(title, content, renderTo) {
    var p = new Ext.Panel({
    	title: title,
        collapsible:false,
        frame: true,
        renderTo: renderTo,
        contentEl: content
    });
    p.show();
};


function createToggledBox(title, content, renderTo, toggler, toggled) {
	Ext.onReady(function() {
	    var p = new Ext.Panel({
	    	title: title,
	        collapsible:false,
	        frame: true,
	        renderTo: renderTo,
	        contentEl: content
	    });
	    var visibile;
	    if (!toggled) {
	    	p.hide();
	    	visibile = false;
	    } else {
	    	p.show();
	    	visibile = true;
	    }
	    Ext.get(toggler).on('click', function() {
	        if (!visibile) {
	        	p.show();
	        	visibile = true;
	        } else {
	        	p.hide();
	        	visibile = false;
	        }
	    });
	});	
}