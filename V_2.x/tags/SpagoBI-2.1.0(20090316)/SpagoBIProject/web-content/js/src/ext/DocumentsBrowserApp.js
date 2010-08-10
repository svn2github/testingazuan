/**
  * ...
  * by Andrea Gioia
  */
  
Ext.BLANK_IMAGE_URL = '../resources/images/default/s.gif';

Ext.onReady(function(){
  Ext.QuickTips.init();              
  var browser = new Sbi.browser.DocumentsBrowser();
  var viewport = new Ext.Viewport(browser);     
});


/* 
Ext.ns("Sbi.browser");
 
// create application
Sbi.browser.app = function() {
    // do NOT access DOM from here; elements don't exist yet
 
    // private variables
     
    // private functions

 
        // public space
    return {
        // public properties, e.g. strings to translate
        
        // public methods
        init: function() {
            Ext.QuickTips.init();
            
            var browser = new Sbi.browser.DocumentsBrowser();
            
            var viewport = new Ext.Viewport(browser);           
        }
    };
}(); // end of app
 
// end of file
*/
