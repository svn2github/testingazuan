/**
  * ...
  * by Antonella Giachino 
  */
  
//Ext.BLANK_IMAGE_URL = './ext-3.1/resources/images/default/s.gif';
var template;



Ext.onReady(function(){
  Ext.QuickTips.init();  
  var win;
  
  win = new Ext.Window({
    layout:'fit',
    width:500,
    height:300,
    closeAction:'hide',
    plain: true,
    title: 'ciao ciao',

    items: new Ext.Panel({
        html: this.template.toSource(),
        deferredRender:false,
        border:false
    }),
    
    buttons: [{
        text:'Submit',
        disabled:true
    },{
        text: 'Close',
        handler: function(){
            win.hide();
        }
    }]
  });

  win.show(this);
});
