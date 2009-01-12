Utils = {};

Utils.version = "1.0.0";

Utils.getVersion = function(){
  return this.version;
}

Utils.svgNS = 'http://www.w3.org/2000/svg';

Utils.find = function(a, pname, pvalue) {
  var result;
  for(i = 0; i < a.length; i++) {
    if(a[i][pname] === pvalue) {
      result = a[i];
      break;
    }
  }
  
  return result;
};


Utils.apply = function(o, c, defaults){
    if(defaults){
        // no "this" reference for friendly out of scope calls
        Utils.apply(o, defaults);
    }
    
    if(o && c && typeof c == 'object'){
        for(var p in c){
            o[p] = c[p];
        }
    }
    
    return o;
};

Utils.applyAttributes = function(o, c, defaults){
    if(defaults){
        // no "this" reference for friendly out of scope calls
        Utils.applyAttributes(o, defaults);
    }
    
    var str = '';
    
    if(o && c && typeof c == 'object'){
        for(var p in c){
            str += p + ": " + c[p] + "\n";
            o.setAttributeNS(null, p, c[p]);
        }
    }
    
    //alert(str);
    return o;
};

Utils.toStr = function(o) {
    var str = "";
			
		if(o === 'undefined') return 'undefined';
			
		str += "Type: [" + typeof(o) + "]\n------------------------\n";
			
	  for(p in o) {
	   str += p + ": " +  o[p] + "\n";
	  }
  
    return str;
};
		
Utils.dump = function(o) {
		alert(this.toStr(o));
};
