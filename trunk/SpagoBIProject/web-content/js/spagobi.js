
function FCKeditor_OnComplete( editorInstance ) {
        completeName = editorInstance.Name;
        instanceName = completeName.substring(13, completeName.length);
        var functocall = 'initializeNotes' + instanceName +'()';
        eval(functocall);
}


function findPos(obj) {
       curleft = curtop = 0;
	   if (obj.offsetParent) {
		    curleft = obj.offsetLeft
		    curtop = obj.offsetTop
		    while (obj = obj.offsetParent) {
			     curleft += obj.offsetLeft
			     curtop += obj.offsetTop
		    }
	   }
	   return [curleft,curtop];
 }
 




// START PORLTET JAVASCRIPT INTIIALIZER
// When you wnat to exec a javascript when the page is loaded you simply set
// the onload attribute on body tag.
// Unfortunately with portlet you cannot because is the portal that write the body tag.
// for this reason you have to set into the portlet html code the js function window.onload=name function.
// Unfortunately if you have more than a portlet in a page the last one ovverrides 
// the window.onload set made by the previous one.
// The SpagoBI solution is to define a global object for the page. The global object
// is defined into this javascript which is linked by the html code of the portelt.
// In this way the page has only one object instance. The object is call "SbiJsInitializer".
// the base class for the object is "SbiJsInitializerClass".
// If you want that each portlet of the page executes a js funciont when the page is loaded 
// you need to add a function to the global object into the html page of the portlet. Example:
//
// SbiJsInitializer.myfunct = myfunctName;
// function myfunctName() {
//    do something
// }
// 
// Then for each portelt set the window.onload as below:
//
// window.onload = SbiJsInitializer.initialize;

 
function SbiJsInitializerClass() {

  this.initialize = Initialize;

  function Initialize() {
      for(prop in SbiJsInitializer)  {
        if(prop != "initialize") {
           eval("SbiJsInitializer."+prop+"()");
        }
      } 
  }
  
}

 try{
 	 SbiJsInitializer.prototype;
 } catch (err) {
 	 var SbiJsInitializer = new SbiJsInitializerClass();
 }

// END PORLTET JAVASCRIPT INTIIALIZER

// START IFRAMES NAVIGATOR INITIALIZER
function IFramesNavigator() {

  this.nestedFrames = new Array();
  
  this.addFrame = AddFrame;
  
  function AddFrame(nestedFrame, label) {
    aFrameWithLabel = new Array(nestedFrame, label);
    this.nestedFrames[this.nestedFrames.length] = aFrameWithLabel;
  }
  
  this.removeFrames = RemoveFrames;
  
  function RemoveFrames(index) {
    aFrameWithLabel = this.nestedFrames[index];
    aFrame = aFrameWithLabel[0];
    //PER IE
    //alert(aFrame.contentDocument.getElementById('1'));
    
    //PER FIREFOX
    //aFrame.src = aFrame.contentDocument.getElementById('1');
    
    //aFrame.src = "http://www.libero.it";
    
    initialLength = this.nestedFrames.length;
    for (i = 1; i < initialLength - index; i++) {
      this.nestedFrames.pop();
    }
    window.history.go(-(initialLength - 1 - index));
  }
  
  this.getFramesNumber = GetFramesNumber;
  
  function GetFramesNumber() {
    return this.nestedFrames.length;
  }
  
  this.getFrame = GetFrame;
  
  function GetFrame(index) {
    return this.nestedFrames[index];
  }

}

try {
  iframesNavigator.prototype;
} catch (err) {
  var iframesNavigator = new IFramesNavigator();
}
// END IFRAMES NAVIGATOR INITIALIZER
