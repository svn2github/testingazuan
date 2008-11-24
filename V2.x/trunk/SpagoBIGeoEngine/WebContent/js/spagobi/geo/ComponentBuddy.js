ComponentBuddy = function(buddy, config) {
    
    this.buddy = buddy;
    
    // sub-components
    
   
    var el =  Ext.get(Ext.DomHelper.append( Ext.getBody(), {
                            tag : 'iframe',
                            frameborder : 0,
                            html : 'Inline frames are NOT enabled\/supported by your browser.',
                            src : (Ext.isIE && Ext.isSecure)? Ext.SSL_SECURE_URL: 'about:blank'}));
                    
    el.applyStyles({
      position: 'absolute',
      'background-color': 'white',
      'z-index': 1000,
      width: '400px',
			height: '400px',
      opacity: 1
      
    });
    
    ComponentBuddy.superclass.constructor.call(this, el.dom.id, true);
    
        
    this.buddy.addListener('render', function(){this.keepInTouchWithBuddy('render');}, this);
    this.buddy.addListener('show', function(){this.keepInTouchWithBuddy('show');}, this);
}




Ext.extend(ComponentBuddy, Ext.Element, {
    
    // static contens and methods definitions
    keepInTouchWithBuddy : function(eventName){
      //alert('keepInTouchWithBuddy: ' + eventName);
      
      var box = this.buddy.getBox();
      box.width += 3;
      box.height += 3;
      this.setBox( box );
    }
    
});

