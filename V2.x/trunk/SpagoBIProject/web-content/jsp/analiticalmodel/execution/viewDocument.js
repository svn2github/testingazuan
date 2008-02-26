
Ext.onReady(function() {

	var popout_ViewPoint = Ext.get('popout_ViewPoint');
    popout_ViewPoint.enableDisplayMode("block");
    popout_ViewPoint.toggle(true);
  
	var popout_Parameters = Ext.get('popout_Parameters');
    popout_Parameters.enableDisplayMode("block");
    popout_Parameters.toggle(true);
  
	var popout_SubObject = Ext.get('popout_SubObject');
    popout_SubObject.enableDisplayMode("block");  
    popout_SubObject.toggle(true);
 
 	var popout_SnapSphot = Ext.get('popout_SnapSphot');
    popout_SnapSphot.enableDisplayMode("block");  
    popout_SnapSphot.toggle(true);
   
    var win;
  
    Ext.get('toggle_Parameters').on('click', function(){
      
        popout_Parameters.toggle(true);
 
    });

    Ext.get('toggle_ViewPoint').on('click', function(){
      
        popout_ViewPoint.toggle(true);
 
    });

    Ext.get('toggle_SubObject').on('click', function(){
      
        popout_SubObject.toggle(true);
 
    });  
    Ext.get('toggle_SnapSphot').on('click', function(){
      
        popout_SnapSphot.toggle(true);
 
    });         

    Ext.get('v_save_button').on('click', function(){

        if(!win){
            win = new Ext.Window({
                id: 'finestra',
                layout:'fit',
                width:500,
                height:300,
                closeAction:'hide',
                plain: true,
                buttons: [{
                    text:'Submit',
                    handler: function(){
                        win.hide();
                    }
                },{
                    text: 'Close',
                    handler: function(){
                        win.hide();
                    }
                }]                
            });
        }
        win.show(this);
		win.load({
		    url: readUrl,
		    callback: function(){    
		    	Ext.get('b_save').on('click', function(){
		    		var textField=Ext.get('v_name');
					alert(textField.dom.value);
					win.hide();
    			});
    		},
		    discardUrl: false,
		    nocache: false,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: false
		});  
 		      
    });  
    

	    var documentPanel = new Ext.Panel({
	        collapsible:false,
	        renderTo: 'documentContainer',
	        height:'auto',
	        width :'auto',
	        autoWidth :true,
	        border :false,
	        bodyBorder :false,
	        border :false,
	        header :false
	    });  
    
         documentPanel.show(this);
		documentPanel.load({
		    url: 'https://localhost:8443/SpagoBI/Wiki.pdf',
		    discardUrl: false,
		    nocache: false,
		    text: "Sto caricando ...",
		    timeout: 30,
		    scripts: false
		});   
});