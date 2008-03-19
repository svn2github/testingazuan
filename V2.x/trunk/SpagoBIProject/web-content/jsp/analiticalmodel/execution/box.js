/**
  * SpagoBI Core - box component
  * by Davide Zerbetto
  */
  
Ext.namespace('it.eng.spagobi.core.box');

// shorthand alias
var box = it.eng.spagobi.core.box;

box.renderTo;

// shows the panel
box.init = function(title, content, renderTo) {
	box.renderTo = renderTo;
	var p = new Ext.Panel({
		title: title,
		collapsible:false,
		frame: true,
		renderTo: renderTo,
		contentEl: content
	});
	p.show();
};

// makes the box togglable
box.toggle = function(toggler, toggled) {
	var element = Ext.get(box.renderTo);
	element.enableDisplayMode("block");
    if (!toggled) {
    		element.toggle(true);
    }
    Ext.get(toggler).on('click', function() {
        element.toggle(true);
    });
};