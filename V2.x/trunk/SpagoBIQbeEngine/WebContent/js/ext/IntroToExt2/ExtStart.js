Ext.onReady(function(){
  
 // second tabs built from JS
    var tabs2 = new Ext.TabPanel({
        renderTo: document.body,
        activeTab: 0,
        width:600,
        height:250,
        plain:true,
        defaults:{autoScroll: true},
        items:[{
                title: 'Normal Tab',
                html: "My content was added during construction."
            },{
                title: 'Ajax Tab 1',
                autoLoad:'http://127.0.0.1:8080/SpagoBIQbeEngine/servlet/AdapterHTTP?ACTION_NAME=SPAGO_BI_START_ACTION'
            },{
                title: 'Disabled Tab',
                disabled:true,
                html: "Can't see me cause I'm disabled"
            }
        ]
    });
    
    Ext.get('myButton').on('click', function(){
        alert("You clicked the button");
        // Basic request
		Ext.Ajax.request({
		   url: 'http://127.0.0.1:8080/SpagoBIQbeEngine/servlet/AdapterHTTP?ACTION_NAME=SPAGO_BI_START_ACTION',
		   success: someFn,
		   failure: otherFn,
		   headers: {
		       'my-header': 'foo'
		   },
		   params: { foo: 'bar' }
		});
        
    });
    
    someFn = function() {
    	alert("SUCCESS");
    }; 
    
    otherFn = function() {
    	alert("FAILURE");
    };       
   

});