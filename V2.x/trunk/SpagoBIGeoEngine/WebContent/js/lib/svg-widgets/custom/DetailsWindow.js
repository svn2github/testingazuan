/**
 * The constructor arguments are the following:
 * 
 * 1. id (String):
 *    unique id that represents a window. The outermost newly created group 
 *    of the Window geometry gets this id. Furthermore, this id is passed to 
 *    callBack functions when triggering Window events.
 *    
 * 2.  parentId (String or object):  
 *     an existing group id or node reference (<g/> or <svg/> element) where the 
 *     new Window object will be appended. This group does not need to be empty. 
 *     The new Window group is appended to this group as last child. If the 
 *     group id does not exist, the script creates a new empty parent group and 
 *     appends it to the root element. 
 *    
 * 3.  width (number):
 *     width of the Window in viewBox coordinates 
 *
 * 4.  height (number):
 *     height of the Window (incl. title and status bar) in viewBox coordinates 
 *
 * 5.  transX (number):
 *     the position of the left edge of the window in viewBox coordinates 
 *
 * 6.  transY (number):
 *     the position of the upper edge of the window in viewBox coordinates 
 *
 * 7.  moveable (boolean, true|false):
 *     indicates whether the Window may be moved or not 
 *
 * 8.  constrXmin (number):
 *     the left constraint, the constraints define the area where the Window can 
 *     be moved within 
 * 
 * 9.  constrYmin (number):
 *     the upper constraint 
 *     
 * 10. constrXmax (number):
 *     the right constraint 
 *     
 * 11. constrYmax (number):
 *     the lower constraint 
 * 
 * 12. showContent (boolean):
 *     value may hold true or false, indicates whether the Window content should 
 *     be visible or hidden during window movements 
 *     
 * 13. placeholderStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the 
 *     placeholder rectangle; this is the style of the placeholder rectangle 
 *     that is displayed instead of the window content if showContent is set to 
 *     true; could include CSS classes; 
 *     example: 
 *     var winPlaceholderStyles = {"fill":"none","stroke":"dimgray",
 *     "stroke-width":1.5}; 
 *      
 * 14. windowStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the Window 
 *     rectangle; could include CSS classes; 
 *     example: 
 *     var windowStyles = {"fill":"aliceblue","stroke":"dimgray",
 *     "stroke-width":1}; 
 *     
 * 15. margin (number):
 *     a number in viewBox coordinates describing a margin. 
 *     Used e.g. for placing text and buttons in statusBar or titleBar 
 *
 * 16. titleBarVisible (boolean, true|false):
 *     indicates whether the Window should have a title bar 
 *     
 * 17. statusBarVisible (boolean, true|false):
 *     indicates whether the Window should have a status bar  
 *     
 * 18. titleText (String or undefined):
 *     a string specifying the Window title text 
 *     
 * 19. statusText (String or undefined):
 *     a string or undefined value specifying the Window status text 
 *     
 * 20. closeButton (boolean, true|false):
 *     indicates whether the Window should have a closeButton. 
 *     The script loooks for an existing symbol with the id "closeButton" or 
 *     creates a new closeButton in the <defs/> section if a symbol with this id
 *     does not exist. Please note that a closed Window still exists and can be 
 *     opened again using the method .open() 
 *
 * 21. minimizeButton (boolean, true|false):
 *     indicates whether the Window should have a minimizeButton. 
 *     The script loooks for an existing symbol with the id "minimizeButton" or 
 *     creates a new minimizeButton in the <defs/> section if a symbol with this
 *     id does not exist 
 *     
 * 22. maximizeButton (boolean, true|false):
 *     indicates whether the Window should have a maximizeButton. 
 *     The script loooks for an existing symbol with the id "maximizeButton" or 
 *     creates a new maximizeButton in the <defs/> section if a symbol with this 
 *     id does not exist 
 *     
 * 23. titlebarStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the titlebar 
 *     rectangle; could include CSS classes; 
 *     example: 
 *     var titlebarStyles = {"fill":"gainsboro","stroke":"dimgray",
 *     "stroke-width":1}; 
 *
 * 24. titlebarHeight (number):
 *     a number value specifiying the titleBar height in viewBox coordinates 
 *
 * 25. statusbarStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the statusbar 
 *     rectangle; could include CSS classes; 
 *     example: var statusbarStyles = {"fill":"aliceblue","stroke":"dimgray",
 *      "stroke-width":1}; 
 *       
 * 26  statusbarHeight (number):
 *     a number value specifiying the statusBar height in viewBox coordinates 
 * 27. titletextStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the titlebar 
 *     text; could include CSS classes; should at least include a "font-size" 
 *     attribute; 
 *     example: var titletextStyles = {"font-family":"Arial,Helvetica",
 *      "font-size":14,"fill":"dimgray"}; 
 *       
 * 28. tatustextStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the statusbar 
 *     text; could include CSS classes; should at least include a "font-size" 
 *     attribute; 
 *     example: 
 *     var statustextStyles = {"font-family":"Arial,Helvetica",
 *     "font-size":10,"fill":"dimgray"}; 
 *     
 * 29. buttonStyles (Array of literals with presentation attributes):
 *     an array literal containing the presentation attributes of the buttons; 
 *     could include CSS classes; should at least include a "fill", "stroke" 
 *     and "stroke-width" attribute; 
 *     example: 
 *     var buttonStyles = {"fill":"gainsboro","stroke":"dimgray",
 *      "stroke-width":1}; 
 *      
 * 30. functionToCall (function, object or undefined):
 *     a callBack function that is called after a window event occurs. 
 *     The parameters for the callBack functions are as follows: 
 *      - id of the Window, eventType (string). 
 *     In case of an object, the method .windowStatusChanged(id,evtType) is 
 *     called. In case of a undefined value, no callBack function is executed. 
 *     "evtType" may hold the following values: 
 *      -  minimized, maximized, closed, opened, removed, moved, movedTo, resized, 
 *         moveStart, moveEnd and created
 *     For some event types, the callBack function is executed with a slight 
 *     delay (200ms) to allow the Window to change state before executing the 
 *     callback function. The events created and resized can be used to trigger 
 *     the creation or update of the window decoration.  
  
  
  
 */   


function DetailsWindow(visible) {

  // Class members

  
  // passed as parameter #13 to the constructor of Window class
  var winPlaceholderStyles = {"fill":"none","stroke":"dimgray","stroke-width":1.5};
  
  // passed as parameter #14 to the constructor of Window class
  var windowStyles = {"fill":"#fffce6","stroke":"dimgray","stroke-width":1};
  
  // passed as parameter #23 to the constructor of Window class
  var titlebarStyles = {"fill":"steelblue","stroke":"dimgray","stroke-width":1};
  
   // passed as parameter #24 to the constructor of Window class
  var titlebarHeight = 17;
  
  // passed as parameter #25 to the constructor of Window class
  var statusbarStyles = {"fill":"aliceblue","stroke":"dimgray","stroke-width":1};
  
  // passed as parameter #26 to the constructor of Window class
  var statusbarHeight = 13;
  
  // passed as parameter #27 to the constructor of Window class
  var titletextStyles = {"font-family":"Arial,Helvetica","font-size":14,"fill":"white"};
  
  // passed as parameter #28 to the constructor of Window class
  var statustextStyles = {"font-family":"Arial,Helvetica","font-size":10,"fill":"dimgray"};
  
  // passed as parameter #29 to the constructor of Window class
  var buttonStyles = {"fill":"steelblue","stroke":"white","stroke-width":2};
  
  // Call the superclass's constructor in the scope of this.
  
   
  Window.call(this, "detail",
                    "Windows",
                    210,200,649,391,  // width, height, X, Y
                    true,            // moovable
                    8,8,1092,690,    // constrXmin, constrYmin, constrXmax, constrYmax
                    true,            // showContent while mooving
                    winPlaceholderStyles,
                    windowStyles,
                    3,                // margin
                    true,             // titleBarVisible
                    false,            // statusBarVisible
                    "Detail",     // title
                    "",               // statusBar content
                    false,true,true,  // closeButton, minimizeButton, maximizeButton
                    titlebarStyles, titlebarHeight, 
                    statusbarStyles, statusbarHeight,
                    titletextStyles, statustextStyles,
                    buttonStyles,
                    this.eventHandler); 
  
  this.createContent();  
}


DetailsWindow.prototype = new Window(); // Set up the prototype chain.
DetailsWindow.prototype.constructor = DetailsWindow; // Set the constructor attribute to Author.

//append new content to the window main group
DetailsWindow.prototype.createContent = function() {
  
  var windowContent;
  var tabGroup;
  var tab1;
  var tab2;
  var dText;
  
  windowContent = document.createElementNS(null,"g");
  Utils.applyAttributes(windowContent, {
    'id' : 'detail'
    , 'transform' : 'translate(649,720)'
  });
    
    
  tabGroup = document.createElementNS(null,"g");
  Utils.applyAttributes(tabGroup, {
    'id' : 'detailTabGroup'
    , 'display' : 'none'
  });    
  windowContent.appendChild(tabGroup);    
    
    
   
  tab1 = document.createElementNS(null,"g");
  Utils.applyAttributes(tab1, {
    'id' : 'detailTabGroup__0_content'
  });    
  tabGroup.appendChild(tab1); 
  
  
  
  dText = document.createElementNS(null,"g");
  Utils.applyAttributes(dText, {
    'id' : 'dynamText'
    , 'class' : 'allText normalText'
  });    
  tab1.appendChild(dText);    
  
  tab2 = document.createElementNS(null,"g");
  Utils.applyAttributes(tab2, {
    'id' : 'detailTabGroup__1_content'
  });    
  tabGroup.appendChild(tab2);              


  tabGroup.appendChild(tab1);   
  tabGroup.appendChild(tab2);   


  var windowsEl = document.getElementById("Windows");
  windowsEl.appendChild(windowContent);
  
  this.appendContent("detailTabGroup", true);   
};

DetailsWindow.prototype.initTabGroups = function() {
		
			//create tabGroups
			myMapApp.TabGroups = new Array();
			
			//first a few styles
			var tabStyles = {"fill":"rgb(180,180,180)","stroke":"dimgray","stroke-width":1,"cursor":"pointer"};
		  var tabwindowStyles = {"fill":"white","stroke":"dimgray","stroke-width":1};
		  var tabtextStyles = {"font-family":"Arial,Helvetica","font-size":15,"fill":"dimgray","font-weight":"normal"};
		  var tabtextStylesCenter = {"font-family":"Arial,Helvetica","font-size":15,"fill":"dimgray","font-weight":"normal","text-anchor":"middle"};
		                
		  //setting tabTitles
		  var tabTitles = new Array("Values","Chart");
		     
		  // tabGroup instances creation
		  myMapApp.TabGroups["detailTabGroup"] = new tabgroup("detailTabGroup","detailTabGroup",5,21,200,175,20,"round","round",5,0,tabStyles,"lightgray",tabwindowStyles,tabtextStyles,tabTitles,0,false,undefined);
		      		              
		  //add new content
		  myMapApp.TabGroups["detailTabGroup"].addContent("detailTabGroup__0_content",0,true);
		  myMapApp.TabGroups["detailTabGroup"].addContent("detailTabGroup__1_content",1,true);

};

DetailsWindow.prototype.initDetailInfopanels =  function () {
			// create detail infopanels
			myMapApp.infoPanel = new Array();
			
      var dynamText = document.getElementById("dynamText");
			for(i = 0; i < kpi_names.length; i++) {
				var offset = 50 + (i*25);
				var panel = document.createElementNS(svgNS,"text");
				panel.setAttribute("id","infopanel" + (i+1));
				panel.setAttribute("x", "20");
				panel.setAttribute("y", "" + offset);
				panel.setAttribute("fill", "dimgray");
				panel.setAttribute("font-family", "Arial,Helvetica");
				panel.setAttribute("font-size", "14px");
				panel.setAttribute("startOffset", "0");
				
				var text_node = document.createTextNode(kpi_descriptions[i]);
				panel.appendChild(text_node);
				
				dynamText.appendChild(panel);
				
				myMapApp.infoPanel[i] = panel;
			}
};


DetailsWindow.prototype.eventHandler = function(id, evtType) { 
  
};

