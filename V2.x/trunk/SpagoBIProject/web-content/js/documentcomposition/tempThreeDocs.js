/*
 * Ext JS Library 2.0.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
 var urlDoc1 = "";
 var urlDoc2 = "";
 var urlDoc3 = "";
 var urlIframe = "";
 var nameDoc1 = "";
 var nameDoc2 = "";
 var nameDoc3 = "";


function setUrls(pUrlIframe, pUrlDoc1, pUrlDoc2, pUrlDoc3){
	urlDoc1 = pUrlDoc1;
	urlDoc2 = pUrlDoc2;
	urlDoc3 = pUrlDoc3;
	urlIframe = pUrlIframe;
}

function setDocsName(pNameDoc1, pNameDoc2, pNameDoc3){
	nameDoc1 = pNameDoc1;
	nameDoc2 = pNameDoc2;
	nameDoc3 = pNameDoc3;

}

  Ext.onReady(function() {
		       var p1 = new Ext.Panel({
				id:'p1',
		        bodyBorder : true,
		        collapsible:true,
		        renderTo: 'divIframe_DOC1'
			    });
				
			    p1.show(this);
				p1.load({
				   url:urlIframe,
				    params: {urlDoc:urlDoc1, nameDoc: nameDoc1},
				    discardUrl: false,
				    nocache: false,
				    text: "Loading ...",
				    timeout: 30,
				    scripts: true
				});   
			    
				var p2 = new Ext.Panel({
				        id:'p2',
				        bodyBorder : true,
				        collapsible:true,
				        renderTo: 'divIframe_DOC2'
				    });
				
			    p2.show(this);
				p2.load({
				   url: urlIframe,
				   params: {urlDoc: urlDoc2, nameDoc: nameDoc2},
				    discardUrl: false,
				    nocache: false,
				    text: "Loading ...",
				    timeout: 30,
				    scripts: true
				}); 
			    
			    var  p3 = new Ext.Panel({
						id:'p3',
				        bodyBorder : true,
				        collapsible:true,
				        renderTo: 'divIframe_DOC3'
				    });
			    p3.show(this);
				p3.load({
				    url: urlIframe,
				    params: {urlDoc: urlDoc3, nameDoc: nameDoc3},
				    discardUrl: false,
				    nocache: false,
				    text: "Loading ...",
				    timeout: 30,
				    scripts: true
				}); 
				
				var button_refresh=Ext.get('refresh');
			    button_refresh.on('click', function(){
				  p3.load({
				     url: urlIframe,
					    params: {urlDoc: '/SpagoBIJasperReportEngine/SpagoBIRefreshServlet?cod_uu=1&amp;DOCUMENT_LABEL=rptDettMapGE&amp;cod_uu=1'},
					    //params: {urlDoc: '/SpagoBIJasperReportEngine/SpagoBIRefreshServlet?docUrl='+urlDoc2},
					    discardUrl: true,
					    nocache: true,
					    text: "Loading ...",
					    timeout: 30,
					    scripts: true
					  }); 
			    });
			    /*
			     var iFrameRef=Ext.get('iframe_DOC1');
				 iFrameRef.on('click', function(){
				 	alert("cliccato iframe 1");
				    //document.getElementById('linktest').href="#";
				 	//document.getElementsByName('linktest').href="#";
				 	document.getElementsByName('linktest').src="#";
				 });
			    */
          
});
