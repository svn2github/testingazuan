//BEGIN TREE

var ie = document.all?true:false;

var minWidth = 100;
var maxWidth = 200;
var hintTime = 1000;
var hintTime = 1000;
function setParameter(name, value){
	switch(name){
		case "minWidth":
			minWidth = getWidth(value)
			break;
		case "maxWidth":
			maxWidth = getWidth(value)
			if(ie){
				maxWidth += 4
			}
			break;
		case "hintTime":
			hintTime = value;
			break;
	}
}


var maxNumberOfChild = 255;
	/* 
		NOTE: if name == "" that mean treeNode is a forest, not a simple tree
	*/
	function treeNode(name, expanded, hasChild){
		this.name = name
		this.expanded = expanded

		this.child = new Array(maxNumberOfChild)
		this.childCount = 0;
		
		this.id= "";
		this.subtree = null
		
		this.addChild = addChild
		this.getHeight = getTreeHeight
		this.getHTMLTable = getHTMLTable
		this.getHTMLTablePrepare = getHTMLTablePrepare
		this.getVerticalHTMLTable = getVerticalHTMLTable
		this.getTreeWidth = getTreeWidth
		this.width = null
		this.getTdHeight = getTdHeight
		this.getExpandedCount = getExpandedCount
		this.showName = ""
		this.minWidth = -1
		
		
		if(!hasChild)
			this.hasChild = false
		else
			this.hasChild = hasChild
			
		this.linkToOriginalNode = "defaultLINK"
	}
	
	function cloneTreeNode(origNode){
		//alert("alert from cloneTreeNode"+origNode.name)
		var res = new treeNode(origNode.name, origNode.expanded, origNode.hasChild)
		if(origNode.childCount > 0){
			for(var i = 0; i < origNode.childCount; i++){
				res.addChild(cloneTreeNode(origNode.child[i]))
			}
		}	
		res.linkToOriginalNode = origNode;
		return res;
	}
	
	
	function getExpandedCount(index){
		if(this.name == ""){
			var res = 0
			for(var i = 0; i< this.childCount; i++){
				res += this.child[i].getExpandedCount(index)
			}
			return res;
		}
		if(this.name != "" && !this.subtree && verTreeCount-1 > index){			
			//this.subtree = getTree(trees1[index+1]);
			//alert(1)
			this.subtree = cloneTreeNode(verticalOriginalNodes[index+1])
		}
				
		
		if(this.childCount == 0 || this.expanded ==false){
			if(this.subtree){
				var res = this.subtree.getExpandedCount(index+1)
				return res;
			}
			else{
				var res = 1
				return res;
			}
		}
		
		var res = 1;
		if(this.subtree)
			res = this.subtree.getExpandedCount(index+1)
		
			
		for(var i= 0; i< this.childCount; i++){
			res += this.child[i].getExpandedCount(index);
			continue;
			
			if(this.name != "" && verTreeCount-1 > index && !this.child[i].subtree){
				//this.child[i].subtree = getTree(trees1[index+1]);
				//alert(2)
				this.child[i].subtree = cloneTreeNode(verticalOriginalNodes[index+1])
			}
			if(this.child[i].subtree){
					res += this.child[i].subtree.getExpandedCount(index+1)
			}else{
				if(this.child[i].childCount > 0 && this.child[i].expanded){					
					res += this.child[i].getExpandedCount(index)
				}else{
					if(this.subtree){
						res += this.subtree.getExpandedCount(index+1)
					}
					else
						res += 1;
				}
			}
		}
//		alert(this.name + res + "-4")
		return res;
	}
	
	function getTdHeight(index){
		if(this.name == ""){
			var res = 0;
			
			for(var i = 0; i< this.childCount; i++){
				if(verTreeCount-1 > index && !this.child[i].subtree){
					//this.child[i].subtree = getTree(trees1[index+1]);
					//alert(4)
					this.child[i].subtree = cloneTreeNode(verticalOriginalNodes[index+1])
				}
				if(this.child[i].subtree){
					res += this.child[i].subtree.getExpandedCount(index+1)
				}
				else
					res += 1;
			}
			
			//return this.childCount;
			
			return res;
		}
		
		var res = 1;
		if(this.name != "" && verTreeCount-1 > index && !this.subtree){
			//alert(trees1[index+1] + "trees1[index+1] - 2")
			//this.subtree = getTree(trees1[index+1]);
			//alert(5)
			this.subtree = cloneTreeNode(verticalOriginalNodes[index+1])
		}
		
		if(this.subtree){
			res = this.subtree.getExpandedCount(index+1)
		}
		
		return res
	}
	
	
	/* 
		Calculate width
	*/
var is_blocked = false;

var is_show_white = false;
	
	function getTreeWidth(level){
		//if(this.width)
			//return this.width;
		
		if(!level)
			level = 1;
	
		var str = '<span style="display: block;white-space: nowrap; padding: 0 2px;">';
		if(this.childCount > 0){
			str += '<img class="plus" src="1x1.gif" width="9" height="9" alt ="" />' 
		}
		str += this.name;
		str += "</span>"
		var res = getWidth(str)
		//if(this.expanded){
			if(this.hasChild == true && this.child[0]){			
				for(var i = 0; i< this.childCount; i++)	{
					var tmpWidth = this.child[i].getTreeWidth(level+1);
					if(tmpWidth + marginLeft > res){
						res = tmpWidth + marginLeft;
					}
				}
			}
		//}
		//this.width = res
		return res + 5;
	}
	/* add child to the current node */
	function addChild(node){
		this.child[this.childCount] = node;
		this.childCount++;
	}
/*
	Calculate Height. For vertical tree
*/
	
	function getTreeHeight(){
		var res = 0;
		var addingHeight = 1;
		if(this.name == "")
			addingHeight = 0;
		
		if(this.expanded){
			for(var i = 0; i < this.childCount; i++){
				var tmp = this.child[i].getHeight();
				if(tmp > res)
					res = tmp
			}
		}
		return res + addingHeight;
		
	}

	var lineHeight = 14;
	var globalIndex = 0;
/*
	Create HTML for horizontal tree
	TODO: synchronize width
*/	
	var horizontalTreeHeights = new Array(/*25*/5)
	function resetHorizontalTreeHeight(){
		for(var i = 0; i< horizontalTreeHeights.length; i++){
			horizontalTreeHeights[i] = 0;
		}
	}
	resetHorizontalTreeHeight()

	
	function getShortName(name, width, img){
		if(width < maxWidth)
			return name;
		//alert(name + "; width="+width)
		
		var tmpName = name
		var str = ""
		if(img==true){
			str = '<span style="padding: 0 2px; " class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" />'
		}else{
			str = '<span style="padding: 0 2px; " class="text">'
		}
		
		if(getWidth(str+tmpName + "</span>") <= width){
			return name
		}else{
			tmpName = name.substring(name, name.length-3)
		}
		
		
		while(getWidth(str + tmpName + "...</span>")  > width){
			
			tmpName = tmpName.substring(0, tmpName.length-1)
		}
		showName = tmpName + "...";
		//alert(showName)
		return showName
	}
	
	function getHTMLTable(index, path, level, width, available){		
		
		if(!available){
			available = 1;
		}
		
		if(available == 1){
			if(this.hasChild == true){
				available = 2;
			}
		}
		if(!index){
			index = 0;
		}
		var tmpHeight = this.getHeight()
		if(tmpHeight > horizontalTreeHeights[index]){
			horizontalTreeHeights[index] = tmpHeight
		}
		
		var firstTime = false;
		if(!path){
			if(path==undefined){
				horArrayCount = 0;
			}
			path = "";
			firstTime = true;
		}
		if(!width){
			width = minWidth;
		}
		
		
		if(!level){
			level = horizontalTreeHeights[index]+1;
		}
		//alert(this.name+"::"+level)
		showName = this.name
		width = Math.min(Math.max(width, horArrayMinWidth[globalIndex]), maxWidth)
		if(this.name == ""){
			currentWidth = width
		}else{
			var currentWidth
			var extraWidth = 0
			if(ie){
				
			}else{
				extraWidth = 8
			}
			if(this.hasChild == true){				
				currentWidth = Math.max(getWidth('<span style="padding: 0 2px;" class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + this.name + "</span>"), width);
				if(currentWidth > maxWidth){
					currentWidth = maxWidth;
				}

				
				/*				
				if(currentWidth > maxWidth){
					currentWidth = maxWidth;
					tmpName = this.name.substring(0, this.name.length-3)
					while(getWidth('<span style="padding: 0 2px; " class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + tmpName + "...</span>") - extraWidth > maxWidth){
						tmpName = tmpName.substring(0, tmpName.length-1)
					}
					showName = tmpName + "...";
				}
	*/
				showName = getShortName(showName, currentWidth, true)
				
				
			}else{
				currentWidth = Math.max(getWidth('<span style="padding: 0 2px;" class="text">' + this.name + "</span>"), width);
				if(currentWidth > maxWidth){
					currentWidth = maxWidth;
					
//					tmpName = this.name.substring(0, this.name.length-3)
	//				while(getWidth('<span style="padding: 0 2px;" class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + tmpName + "...</span>") - extraWidth - 20 > maxWidth){
		//				tmpName = tmpName.substring(0, tmpName.length-1)
			//		}
				//	showName = tmpName + "...";
				}
				showName = getShortName(showName, currentWidth, false)

							
			}
		}
		
		
		var res =  ""
		path += escape(this.name) + "/"
	
		if(this.name != "" && horTreeCount-1 > index && !this.subtree){
			this.subtree = cloneTreeNode(horizontalOriginalNodes[index+1])
		}
		

		if(index + 1 == horTreeCount && this.name != ""){
			if(document.all)
				horArrayWidth[globalIndex] = currentWidth;
			else
				horArrayWidth[globalIndex] = currentWidth + 4;
			horArrayAvailability[globalIndex] = available;
			
			horArrayPath[globalIndex] = path;
			globalIndex++			
			horArrayCount++;
		}
		if(this.hasChild == true){
			if(this.name == ""){
				if(index == 0){
					res = '<table cellpadding="0" cellspacing="0" style="height: ' + (level-1)*lineHeight+ 'px;"><tr valign="top">'					
				}else{
					res = '<table cellpadding="0" cellspacing="0" class="borderbottom" style="width: ' + currentWidth+ 'px; height: ' + (level-1)*lineHeight+ 'px;><tr valign="top">'
				}
				
			}else{				
				var className = "";
				if(this.expanded)
					className = "minus";
				else
					className = "plus"
				if(ie){
					res = '<table cellpadding="0" cellspacing="0" style="height: ' + (level*lineHeight+1)+ 'px;"><tr valign="top"><td class="borderleft" style="white-space: nowrap;height: ' + (level*lineHeight+1)+ 'px;"><span style="padding: 0 2px; height: '+(level*lineHeight+1)+'px; display: block; width: ' + currentWidth + 'px;" class="text" onmousedown="return false" unselectable="on"><img class="' + className+ '" style="margin-right: 2px;" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + showName + "</span>";
				}
				else
					res = '<table cellpadding="0" cellspacing="0" style="height: ' + level*lineHeight+ 'px;"><tr valign="top"><td class="borderleft" style="white-space: nowrap;height: ' + level*lineHeight+ 'px;"><span style="padding: 0 2px; height: '+(level*lineHeight)+'px; display: block; width: ' + currentWidth + 'px;" class="text" onmousedown="return false" unselectable="on"><img class="' + className+ '" style="margin-right: 2px;" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + showName + "</span>";
				if(this.subtree){
					var nextAv = 0
					if(available == 1){
						nextAv = 1;
					}else{
						nextAv = 3;
					}
					res += this.subtree.getHTMLTable(index + 1, path, 0, currentWidth, nextAv);
				}
				res += "</td>"
			}
			
			if(this.expanded){
				if(this.name == ""){
					if(ie){
						res += '<td><table cellpadding="0" cellspacing="0" style="height: ' + ((level-1)*lineHeight) + 'px;"><tr valign="top">'
					}else{
						res += '<td><table cellpadding="0" cellspacing="0" style="height: ' + (level-1)*lineHeight + 'px;"><tr valign="top">'
						
					}
				}else
					res += '<td style="padding-top: ' + lineHeight+ 'px; height: ' + ((level-1)*lineHeight) + 'px;"><table cellpadding="0" cellspacing="0" style="height: ' + ((level-1)*lineHeight) + 'px;"><tr valign="top">'
				for(var i = 0; i< this.childCount; i++){
					res += '<td style="white-space: nowrap;height: ' + ((level-1)*lineHeight)+ 'px;">'
					var nextAv = 0;
					if(available == 1){
						nextAv = 1
					}else if(available == 2){
						nextAv = 1
					}else{
						nextAv = 3
					}					
					if(i==0){						
						if(this.name == ""){							
							res += this.child[i].getHTMLTable(index, path, level-1, currentWidth, nextAv);
						}else{
							if(this.expanded ){
								res += this.child[i].getHTMLTable(index, path, level-1, 0, nextAv);
							}else{
								res += this.child[i].getHTMLTable(index, path, level-1, currentWidth, nextAv);
							}
						}					
					}else{
						res += this.child[i].getHTMLTable(index, path, level-1, 0, nextAv);
					}
					res += "</td>"
				}
				res += "</tr></table></td>"
			}	
		}else{			
			var tmpHeight = level*lineHeight;
			if(ie){
				tmpHeight++
			}
			res = '<table cellpadding="0" cellspacing="0" style="height: ' + tmpHeight+ 'px;><tr valign="top"><td class="borderleft" style="white-space: nowrap; height: ' + tmpHeight + 'px;"><span style="padding: 0 2px; height: '+tmpHeight+'px; display: block; line-height: 15px; width: ' + currentWidth+ 'px;" class="text" onmousedown="return false" unselectable="on">' + showName + "</span>"
			if(this.subtree){
				var nextAv = 0;
				if(available == 1){
					nextAv = 1
				}else{
					nextAv = 3
				}
				
				res += this.subtree.getHTMLTable(index + 1, path, null, currentWidth, nextAv);
			}
			res += "</td>"
		}
		res += "</tr></table>"
		return res
	}

	
function getHTMLTablePrepare(index, path, level, width, available){				
		
		if(!available){
			available = 1;
		}		
		if(available == 1){
			if(this.hasChild == true){
				available = 2;
			}
		}
		if(!index){
			index = 0;
		}
		var tmpHeight = this.getHeight()
		if(tmpHeight > horizontalTreeHeights[index]){
			horizontalTreeHeights[index] = tmpHeight
		}
		
		var firstTime = false;
		if(!path){
			if(path==undefined){
				horArrayCount = 0;
			}
			path = "";
			firstTime = true;
		}
		if(!width){
			width = minWidth;
		}
		
		
		if(!level){
			level = horizontalTreeHeights[index]+1;
		}
		showName = this.name
		width = Math.min(Math.max(width, horArrayMinWidth[globalIndex]), maxWidth)
		if(this.name == ""){
			currentWidth = width
		}else{
			var currentWidth
			var extraWidth = 0
			if(ie){
				
			}else{
				extraWidth = 8
			}
			if(this.hasChild == true){
				
				currentWidth = Math.max(getWidth('<span style="padding: 0 2px;" class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + this.name + "</span>"), width);
				
				if(currentWidth > maxWidth){
					currentWidth = maxWidth;
//					tmpName = this.name.substring(0, this.name.length-3)
	//				while(getWidth('<span style="padding: 0 2px; " class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + tmpName + "...</span>") - extraWidth > maxWidth){
		//				tmpName = tmpName.substring(0, tmpName.length-1)
			//		}
//					showName = tmpName + "...";
					showName = getShortName(showName, currentWidth, true)
				}
				
			}else{
				currentWidth = Math.max(getWidth('<span style="padding: 0 2px;" class="text">' + this.name + "</span>"), width);
				if(currentWidth > maxWidth){
					currentWidth = maxWidth;
					//tmpName = this.name.substring(0, this.name.length-3)
					//while(getWidth('<span style="padding: 0 2px;" class="text"><img style="margin-right: 2px;" class="plus" src="1x1.gif" width="9" height="9" alt="" onclick="expandTree(\'' + path + '\')" />' + tmpName + "...</span>") - extraWidth - 20 > maxWidth){
						//tmpName = tmpName.substring(0, tmpName.length-1)
					//}
					//showName = tmpName + "...";
					showName = getShortName(showName, currentWidth, false)
				}				
			}
		}
		
		var res =  ""
		path += escape(this.name) + "/"
	
		if(this.name != "" && horTreeCount-1 > index && !this.subtree){
			this.subtree = cloneTreeNode(horizontalOriginalNodes[index+1])
		}
		

		if(index + 1 == horTreeCount && this.name != ""){
			if(document.all)
				horArrayWidth[globalIndex] = currentWidth;
			else
				horArrayWidth[globalIndex] = currentWidth + 4;
			horArrayAvailability[globalIndex] = available;
			
			horArrayPath[globalIndex] = path;
			globalIndex++			
			horArrayCount++;
		}
		if(this.hasChild == true){
			if(this.name != ""){			
				if(this.subtree){
					var nextAv = 0
					if(available == 1){
						nextAv = 1;
					}else{
						nextAv = 3;
					}					
					this.subtree.getHTMLTable(index + 1, path, 0, currentWidth, nextAv);
				}				
			}
			
			if(this.expanded){
				for(var i = 0; i< this.childCount; i++){
					
					var nextAv = 0;
					if(available == 1){
						nextAv = 1
					}else if(available == 2){
						nextAv = 1
					}else{
						nextAv = 3
					}					
					if(i==0){						
						if(this.name == ""){							
							
							this.child[i].getHTMLTable(index, path, level-1, currentWidth, nextAv);
						}else{
							if(this.expanded ){
								this.child[i].getHTMLTable(index, path, level-1, 0, nextAv);
							}else{
								this.child[i].getHTMLTable(index, path, level-1, currentWidth, nextAv);
							}
						}					
					}else{
						this.child[i].getHTMLTable(index, path, level-1, 0, nextAv);
					}
				}
			}	
		}else{			
			if(this.subtree){
				var nextAv = 0;
				if(available == 1){
					nextAv = 1
				}else{
					nextAv = 3
				}
				this.subtree.getHTMLTable(index + 1, path, null, currentWidth, nextAv);
			}
		}
	}	
	
	
/*
	TODO: dinamic width of these arrays
*/
	var horArrayWidth = new Array(1000)
	var horArrayAvailability = new Array(20)
	var horArrayPath = new Array(1000)
	var horArray = new Array(1000)
	var horArrayCount = 0;
	

	var verArrayAvailability = new Array(20)
	var verArray = new Array(1000)
	var verArrayCount = 0;
	var verArrayPath = new Array();

	var maxLength = 1000
	var data = new Array(maxLength)
	for(var i =  0; i< data.length; i++){
		data[i] = new Array(maxLength)
	}
/*
	for(var i = 0; i< data.length; i++){
		for(var j = 0; j< data[i].length; j++){
			data[i][j] = ""
		}
	}
*/	
	
	
	var marginLeft = 12;
	
	
	function getVerticalHTMLTable(index, path, level, width, available, thelast){
		
		if(!available){			
			available = 1;
		}
		if(available == 1){
			if(this.hasChild == true){
				available = 2;
			}
		}

		if(!index){
			index = 0;
		}
		if(!path){
			verArrayCount = 0;
//			verStartIndex = -1;
			path = "";
		}
		if(!level)
			level = 0;
		if(!thelast)
			thelast = false
		path += escape(this.name) + "/"
		if(!width){
			width = this.getTreeWidth() - 10;
			//alert(width)
			width = verticalOriginalNodes[index].getTreeWidth();
			//alert(width)
			
		}
		
		if(/*this.name != "" && */verTreeCount-1 > index && !this.subtree){
			//this.subtree = getTree(trees1[index+1]);
			//alert("" + 7 + "' index="+index + "verticalOriginalNodes[index+1].child[0].name="+verticalOriginalNodes[index+1].child[0].name)
			this.subtree = cloneTreeNode(verticalOriginalNodes[index+1])
		}
		
		if(index + 1 == verTreeCount && this.name != ""){
			verArrayPath[verArrayCount] = path;			

			verArrayAvailability[verArrayCount] = available;
			//alert(this.name + " - " + available)

			verArrayCount++;
		}
		
		
		var res = "";

		if(this.name == ""){
			for(var i = 0; i< this.childCount; i++){
				var nextAv = 0;
				if(available == 1){
					nextAv = 1;
				}else if(available == 2){
					nextAv = 1
				}else{
					nextAv = 3
				}
				var thelast1 = false
				if(i == this.childCount-1)
					thelast1 = true
				res += this.child[i].getVerticalHTMLTable(index, path, level+1, width, nextAv, thelast1)
			}
		}
		else
		{
			res += '<span style="position: relative;padding: 0 2px; display: block; cursor: default; white-space: nowrap; margin-left: ' + (level-1)*marginLeft+ 'px; width: ' + (width -(level-1)*marginLeft) + 'px; font-family: Tahoma; font-size: 11px; " onmousedown="return false" unselectable="on"><table cellpadding="0" cellspacing="0" style="width: 100%;"><tr valign="top" style="height: 19px;">'	
			if(thelast && (!this.expanded) && level > 1){
				
				if(document.all){
					var tmp = this.getTdHeight(index)
					if(tmp > 1)
						res += '<div style="position: absolute; bottom: -1px; left: -11px;height: 2px; line-height: 2px; font-size: 2px; width: 50px; background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;"></div>'				
					else
						res += '<div style="position: absolute; bottom: -1px; left: -11px;height: 2px; line-height: 2px; font-size: 2px; width: 50px; background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;"></div>'				
				}
				else{
					res += '<div style="position: absolute; bottom: 0; left: -11px;height: 2px; line-height: 2px; font-size: 2px; width: 50px; background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;"></div>'				
				}
			}
			var className = ""
			if(this.expanded)
				className = "minus";
			else
				className = "plus"
			
			if(this.subtree){
				if(this.hasChild == true){
					if(this.expanded){
						res += '<td style="background-color1: green;height: ' + 19*this.getTdHeight(index) + 'px; background-image: url(themes/default/img/bg4.gif); background-repeat: no-repeat; background-position: 15px bottom;width: 9px;padding-top: 3px;"><img class="' + className + '" src="1x1.gif" width="9" height="9" alt="" onclick="expandVerticalTree(\'' + path + '\')" /></td><td style="padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
					}else{						
						res += '<td style="height: ' + 19*this.getTdHeight(index) + 'px; background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;width: 9px;padding-top: 3px;"><img class="' + className + '" src="1x1.gif" width="9" height="9" alt="" onclick="expandVerticalTree(\'' + path + '\')" /></td><td style="padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
					}
				}else{
					res += '<td style="background-color1: blue;height: ' + 19*this.getTdHeight(index) + 'px; padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
				}
			}else{
				if(this.hasChild == true ){
					if(this.expanded)
						res += '<td style="background-color1: grey;width: 9px;padding-top: 3px;"><img class="' + className + '" src="1x1.gif" width="9" height="9" alt="" onclick="expandVerticalTree(\'' + path + '\')" /></td><td style="padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
					else
						res += '<td style="background-color1: yellow;width: 9px;padding-top: 3px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;"><img class="' + className + '" src="1x1.gif" width="9" height="9" alt="" onclick="expandVerticalTree(\'' + path + '\')" /></td><td style="padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
				}else{
					res += '<td style="background-color1: purple;padding-top: 1px;background-image: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;font-family: Tahoma; font-size: 11px;">'
				}
			}
			res += this.name ;
			res += "</td></tr></table>"
			res += "</span>"
			if(this.subtree){
				var nextAv = 0;
				if(available == 1){
					nextAv = 1
				}else{
					nextAv = 3
				}
				res = '<table cellpadding="0" cellspacing="0" style="background-image1: url(themes/default/img/bg3.gif); background-repeat: repeat-x; background-position: 0 bottom;"><tr valign="top"><td style="border-right: 1px solid gray; width: ' + width + 'px;">' + res + '</td><td>' + this.subtree.getVerticalHTMLTable(index + 1, path, null, 0, nextAv, false) + '</td></tr></table>'
			}
			if(this.expanded){
				for(var i = 0; i< this.childCount; i++){
					var nextAv = 0;
					if(available == 1){
						nextAv = 1;
					}else if(available == 2){
						nextAv = 1
					}else{
						nextAv = 3
					}
					var thelast1 = false
					if(i == this.childCount-1)
						thelast1 = true
					res += this.child[i].getVerticalHTMLTable(index, path, level+1, width, nextAv, thelast1)
				}
			}
		}
		return res
	}
	

	
	function expandVerticalTree(path, action){		
		if(!action){
			try{
				//alert("expandVerticalTree" + path)
				getMainWindow().stateChangeRequest(getId(), 0, unescape(path))
			}catch(err){}
			return;
		}
		//alert(path + "---")
		/*
		if(is_blocked == true)
			return;
		*/
			
			
		is_blocked = true;
		prevVerArrayCount = verArrayCount;
		for(var i = verArrayCount-1; i>=0; i--){			
			if(verArrayPath[i].substring(0, escape(path).length) == escape(path)){
				verStartIndex = i				
				break;
			}
		}
		
		verArrayCount = 0;
			
		var tree = node1;
/*		
		var pathArray = path.split("/")
		for(var i = 1; i< pathArray.length-1; i++){
			if(pathArray[i] == "")
				tree = tree.subtree;
			for(var j = 0; j< tree.childCount; j++)	{
				if(escape(tree.child[j].name) == pathArray[i]){
					tree = tree.child[j];
					break;
				}
			}
		}
	*/

//alert(path)
		var pathArray = path.split("/")
		
/*
		
		for(var i = 1; i< pathArray.length-1; i++){
			if(pathArray[i] == ""){
				if(!tree.subtree){
					tree.subtree = horizontalOriginalNodes[treeIndex].cloneNode();
				}
				treeIndex++;
				tree = tree.subtree;
				
			}


*/		
		var treeIndex = 0;
		for(var i = 1; i< pathArray.length-1; i++){
			//alert(pathArray[i] + " pathArray")
			if(pathArray[i] == ""){
				treeIndex++;
				if(!tree.subtree){
					//alert(8)
					tree.subtree = cloneTreeNode(verticalOriginalNodes[treeIndex]);
				}				
				tree = tree.subtree;
			}
			for(var j = 0; j< tree.childCount; j++)	{
				
				if(tree.hasChild && tree.childCount==0){
					for(var k = 0; k< tree.linkToOriginalNode.childCount; k++){						
						var newNode = new treeNode(tree.linkToOriginalNode.child[k].name, tree.linkToOriginalNode.child[k].expanded, tree.linkToOriginalNode.child[k].hasChild);
						newNode.linkToOriginalNode = tree.linkToOriginalNode.child[k]
						tree.addChild(newNode)
					}
				}

				if(escape(tree.child[j].name) == escape(pathArray[i])){
					tree = tree.child[j];
					break;
				}
			}
		}
		if(tree.hasChild && tree.childCount==0){				
			for(var k = 0; k< tree.linkToOriginalNode.childCount; k++){
				var newNode = new treeNode(tree.linkToOriginalNode.child[k].name, tree.linkToOriginalNode.child[k].expanded, tree.linkToOriginalNode.child[k].hasChild);
				newNode.linkToOriginalNode = tree.linkToOriginalNode.child[k]
				tree.addChild(newNode)
			}
		}

		
		
		
		tree.expanded = !tree.expanded;
		var is_collapse = false
		if(tree.expanded == false){
			is_collapse = true
		}
		
		if(!firstTime){
			renderVerticalTree(node1)
			shiftVerWidth = verArrayCount - prevVerArrayCount;
		
			shiftData();
			syncronizeSize();
			invalidateTable()
			if(is_collapse){				
				//invalidateTable()
				setupRowWidth();
			}		
		}
		try{
			getMainWindow().stateChanged(getId(), 0, unescape(path));
		}
		catch(err){}
		
	}
	function expandTree(path, action){	
	
		if(!action){
			try{
				getMainWindow().stateChangeRequest(getId(), 1, unescape(path))
			}catch(err){}
			return;
		}
		is_blocked = true;
		prevHorArrayCount = horArrayCount;
		for(var i = horArrayCount-1; i>=0; i--){
			if(horArrayPath[i].substring(0, escape(path).length) == escape(path)){
				horStartIndex = i
				break;
			}
		}
		
		horArrayCount = 0;
			
		var tree = node;
		var pathArray = path.split("/")
		var treeIndex = 0;
		for(var i = 1; i< pathArray.length-1; i++){
			if(pathArray[i] == ""){
				if(!tree.subtree){
					//alert("treeIndex="+treeIndex +"; horizontalOriginalNodes[treeIndex]"+horizontalOriginalNodes[treeIndex])
					//alert(path)
					tree.subtree = cloneTreeNode(horizontalOriginalNodes[treeIndex+1]);
				}
				treeIndex++;
				tree = tree.subtree;
				
			}
			for(var j = 0; j< tree.childCount; j++)	{
				
				if(tree.hasChild && tree.childCount==0){				
					for(var k = 0; k< tree.linkToOriginalNode.childCount; k++){
						var newNode = new treeNode(tree.linkToOriginalNode.child[k].name, tree.linkToOriginalNode.child[k].expanded, tree.linkToOriginalNode.child[k].hasChild);
						newNode.linkToOriginalNode = tree.linkToOriginalNode.child[k]
						tree.addChild(newNode)
					}
				}

				if(escape(tree.child[j].name) == escape(pathArray[i])){
					tree = tree.child[j];
					break;
				}
			}
		}
		if(tree.hasChild && tree.childCount==0){				
			for(var k = 0; k< tree.linkToOriginalNode.childCount; k++){
				var newNode = new treeNode(tree.linkToOriginalNode.child[k].name, tree.linkToOriginalNode.child[k].expanded, tree.linkToOriginalNode.child[k].hasChild);
				newNode.linkToOriginalNode = tree.linkToOriginalNode.child[k]
				tree.addChild(newNode)
			}
		}
		
		tree.expanded = !tree.expanded;
		var is_collapse = false;
		if(tree.expanded == false){
			is_collapse = true
		}
		
		
		if(!firstTime){
			resetHorizontalTreeHeight()
			renderTree(node, "prepare")
			//alert(horizontalTreeHeights)
			
			shiftHorWidth = horArrayCount - prevHorArrayCount;
		
			shiftData()
			syncronizeSize();
			
			invalidateTable()
			
			if(is_collapse){		
				setupRowWidth()
			}
		}
		
		try{
				getMainWindow().stateChanged(getId(), 1, unescape(path));
		}
		catch(err){}
	}
	function insertChildren(direction, pos, parentPath, children, curNode){		
        parentPath =  parentPath.split('/')
		if(!curNode){
			//alert("insertChildren: derection=" + direction + "; pos=" + pos + "; parent="+parent + "; children="+children)
			if(direction == 0){
				//curNode = node;
				curNode = horizontalOriginalNodes[pos]
				//TODO: select from horizontalOriginalNodes
			}else{
				//curNode = node
				curNode = verticalOriginalNodes[pos]
				//TODO: select from verticalOriginalNodes
			}
		}
        var parent = curNode;
		for(var i = 1; i< parentPath.length; i++){
            parentName = parentPath[i];
            for(var j = 0; j< parent.childCount; j++){
                var child = parent.child[j];
                if(parentName == child.name) {
                    parent = child;
                    break;
                }
            }
        }
		if(parent != null){			
            var n = getTree(children);
            //n.linkToOriginalNode = n
            for(var i = 0; i< n.childCount; i++){
                var newNode = new treeNode(n.child[i].name, n.child[i].expanded, n.child[i].hasChild);
                newNode.linkToOriginalNode = newNode;
                parent.addChild(newNode);
            }	
		}
	}
	
	function expand(path, dir){
		
		var tree;
		if(dir == 0){
			expandTree(path, true);
			return;
		}else{			
			expandVerticalTree(path, true)
			return;
		}
	}
	
	var horStartIndex = -1;
	var verStartIndex = -1;
	
	var prevHorArrayCount = -1;
	var prevVerArrayCount = -1;

	
	
	var shiftHorWidth = -1;
	var shiftVerWidth = -1;
	
	
	function getTree(str){
		var elements = str.split('/')
    	var stack = new Array(10)
    	var stackPointer = 0;
    	
    	var prevLevel = 0;
    	var node;
    	for(var i = elements.length-2; i>=0; i--){
    		var pair = elements[i].split(":")
    		var level = parseInt(pair[0]);
    		//alert(level)
    		
    		var hasChild = (pair[0].charAt(pair[0].length-1)=="+")?true:false;
    		//alert(hasChild)
    		var value = pair[1]
    		
    		var node = new treeNode(value, false, hasChild);
    		if(prevLevel <= level || prevLevel == 0){
    			stack[stackPointer] = new Array(level, node)
    			stackPointer++
    		}else{
    			
    			while(stackPointer > 0 && stack[stackPointer-1][0] == prevLevel){
    				node.addChild(stack[stackPointer-1][1])
    				stackPointer--;
    			}
    			stack[stackPointer] = new Array(level, node);
    			stackPointer++
    		}
    		prevLevel = level;
    	}
		node = new treeNode("", true, true)
		while(stackPointer> 0){
			node.addChild(stack[stackPointer-1][1])			
			stackPointer--;
		}    	
		return node;
	}
	function renderTree(n, action){
		if(action == "prepare"){
			globalIndex = 0;
			n.getHTMLTablePrepare();
			return 
		}
		for(var i = 0; i< horArrayAvailability.length; i++){
			horArrayAvailability[i] = 0;
		}
		
		
		var obj = document.getElementById("top");
		globalIndex = 0;
		if(ie){
			var str = n.getHTMLTable();
			obj.innerHTML = "<div style='border-right: 1px solid #808080; width: 2px;'>" + str + "</div>"			
		}else{
			var str = n.getHTMLTable()			
			var width = 0;
			for(var i = 0; i< horArrayCount; i++){
				width += horArrayWidth[i]
			}						
			obj.innerHTML = "<div style='border-right: 1px solid #808080; width: " + width + "px;'>" + str + "</div>"
		}
		syncRowWidth()
		syncronizeSize()
	}
	function renderVerticalTree(n){
		for(var i = 0; i< verArrayAvailability.length; i++){			
			verArrayAvailability[i] = 0;
		}
		
		var obj = document.getElementById("leftinner");
		if(document.all){
			obj.style.width = "0";
		}
		globalIndex = 0;
		verArrayCount = 0;
		//fix bug!!!
		obj.innerHTML = n.getVerticalHTMLTable() + "<div id='verbottomborder' style='background-image: url(themes/default/img/border2.gif); background-repeat: repeat-x;display: block; height: 2px;  position: relative; overflow: hidden; top: -2px;width: 100%;'>&nbsp;</div>";
//		alert("verArrayAvailability"+verArrayAvailability)
//		alert("horArrayAvailability"+horArrayAvailability)

		syncronizeSize()
	}
	
	function processFocus(obj, e){
		obj.value = obj.getAttribute("realValue")
		return true;
	}
	function processBlur(obj, e){
		try{
			if(obj.getAttribute("realValue") != obj.value){
				var validate = getMainWindow().validateValue (getId(), unescape(obj.getAttribute("xtree")), unescape(obj.getAttribute("ytree")), obj.value)
				if(validate == true){
					var res = getMainWindow().updateCell(getId(), unescape(obj.getAttribute("xtree")), unescape(obj.getAttribute("ytree")), obj.value)
					obj.setAttribute("realValue", obj.value)	
									
					var showValue = obj.value;
					var w = getWidth(showValue)
					var width = parseInt(obj.style.width)
					showValue = getShortText(showValue, width)
					obj.setAttribute("showValue", showValue)
				}else{
					obj.value = obj.getAttribute("showValue")
				}
			}
		}
		catch(err){}
		obj.value = obj.getAttribute("showValue")
		var i = getX(obj.getAttribute("xtree"))
		var j = getY(obj.getAttribute("ytree"))
		data[j][i] = obj.value
	}
	
	function getX(xtree){
		for(var i = 0; i< horArrayCount; i++){
			if(xtree == horArrayPath[i]){
				return i;
			}
		}
		return -1;
	}
	function getY(ytree){
		for(var i = 0; i< verArrayCount; i++){
			if(ytree == verArrayPath[i]){
				return i
			}
		}
		return -1;
	}
	
	//var focusedObj = null	
	function processChange(obj, e){
		if(obj.getAttribute("editable") == "false"){
			window.focus();	
			return false;
		}
		var key = -1;
		if(document.all){
			key = e.keyCode;
		}
		else{
			if(e.keyCode)
				key = e.keyCode;
			else
				key = e.charCode;
		}
		
		if(key == 13){
			if(obj.getAttribute("realValue") != obj.value){
				document.getElementById("forfocus").focus();
				return;
				var validate = getMainWindow().validateValue (getId(), unescape(obj.getAttribute("xtree")), unescape(obj.getAttribute("ytree")), obj.value)
				if(validate == true){
					var res = getMainWindow().updateCell(getId(), unescape(obj.getAttribute("xtree")), unescape(obj.getAttribute("ytree")), obj.value)
				}else{
					obj.value = obj.getAttribute("realValue")
				}
			}
			return true;
		}
		return true;
	}
	var defaultCellValue = "";
	function shiftData(){
		if(horStartIndex != -1){
			var width = shiftHorWidth;
			for(var j = 0; j< verArrayCount; j++){
				for(var i = width + horStartIndex+1; i< horArrayCount; i++){
					if(data[j][i-width])
						data[j][i] = data[j][i-width];
					else
						data[i][j] = defaultCellValue;
				}
				
			}
		}
		if(verStartIndex != -1){
			var width = shiftVerWidth
			for(var j = 0; j< horArrayCount; j++){
				for(var i = width + verStartIndex+1; i< verArrayCount; i++){
					if(data[i-width][j])
						data[i][j] = data[i-width][j];
					else
						data[i][j] = defaultCellValue
					
				}
				
			}
		}
	}
	
	function invalidateTable(){
//		alert("invalidateT: " + horStartIndex + " -- " + verStartIndex)
		if(horStartIndex != -1){
			var width = shiftHorWidth;
			var content = document.getElementById("content")
			var table = content.firstChild
			var tbody = table.firstChild
			if(width < 0){
				for(var i = 0; i< tbody.childNodes.length; i++){
					var tr = tbody.childNodes[i]
					for(var j = 0; j< Math.abs(width); j++){
						if(tr.childNodes[width+ horStartIndex+1]){
							tr.removeChild(tr.childNodes[width+horStartIndex+1])	
						}
					}
				}
			}else{
				for(var i = 0; i< tbody.childNodes.length; i++){
					var tr = tbody.childNodes[i]
					for(var j = 0; j< width; j++){
						var td = document.createElement("td");
						if(document.all){
							td.onmouseover = function (){hint(event, this)}
							td.onmouseout = function () {hideHint()}
						}else{
							td.setAttribute("onmouseover", "hint(event, this)")
							td.setAttribute("onmouseout", "hideHint()")							
						}
						var div = document.createElement("div")
						div.setAttribute("id", "" + i + "_" + j)
						var style = ""
							
						if(document.all){
							style = "width: " + horArrayWidth[j+horStartIndex+1] + "px;"
						}else{
							style = "width: " + (horArrayWidth[j+horStartIndex+1]-5) + "px;"
						}
						if((horArrayAvailability[j+horStartIndex+1] == true) && (verArrayAvailability[i] == true)){
							div.setAttribute("is_white", "yes")
						}
						
						//alert("index = " +(j+horStartIndex+1) + "; horArrayAvailability[index]="+horArrayAvailability[j+horStartIndex+1] + "; horArrayAvailability="+horArrayAvailability)
						
						if(is_show_white && (horArrayAvailability[j+horStartIndex+1] == true) && (verArrayAvailability[i] == true)){
							style += "background-color: #ffffff;"							
						}else{
							style += "background-color: #e8f2fe;"
						}
						div.setAttribute("class", "cell")
						div.setAttribute("style", style)
						
						if(data[i][j+horStartIndex+1]){
							showValue = data[i][j+horStartIndex+1];
						}else{
							showValue = defaultCellValue
						}
						
						w = getWidth(showValue)
						showValue = getShortText(showValue, w)
						div.appendChild(document.createTextNode(showValue))
						div.setAttribute("textWidth", w)
						if(data[i][j+horStartIndex+1])
							div.setAttribute("realValue", data[i][j+horStartIndex+1]);
						else{
							div.setAttribute("realValue", defaultCellValue);
						}
						div.setAttribute("showValue", showValue)
						div.setAttribute("onclick", "createInputElement(this)")
						div.setAttribute("xtree", horArrayPath[j+horStartIndex+1]);
						div.setAttribute("ytree", verArrayPath[i]);
						if(document.all){
							div.style.setAttribute("cssText", style)
							div.className = "cellIE"
							div.onclick = function createInputElementIE(){return createInputElement(this)}
						}
						td.appendChild(div)						
						
						if(tr.childNodes[horStartIndex + 1 + j]){
							tr.insertBefore(td, tr.childNodes[horStartIndex + 1 + j])
						}else{
							tr.insertBefore(td, null);
						}						
					}
				}
			}
			horStartIndex = -1;
		}
		
		if(verStartIndex != -1){
			
			var width = shiftVerWidth	
			var content = document.getElementById("content")
			var table = content.firstChild
			var tbody = table.firstChild
			if(width < 0){
				for(var i = 0; i< Math.abs(width); i++){
					if(tbody.childNodes[width + verStartIndex+1])
						tbody.removeChild(tbody.childNodes[width + verStartIndex+1])
				}
			}else{
				for(var i = 0; i< width; i++){
					var tr = document.createElement("tr")
					for(var j = 0; j< horArrayCount; j++){
						var td = document.createElement("td")
						if(document.all){
							td.onmouseover = function (){hint(event, this)}
							td.onmouseout = function () {hideHint()}
						}else{
							td.setAttribute("onmouseover", "hint(event, this)")
							td.setAttribute("onmouseout", "hideHint()")							
						}
						var div = document.createElement("div")
						div.setAttribute("id", "" + i + "_" + j)
						var style = ""
							
						if(document.all){
							style = "width: " + horArrayWidth[j] + "px;"
						}else{
							style = "width: " + (horArrayWidth[j]-5) + "px;"
						}
						if((horArrayAvailability[j] == true) && (verArrayAvailability[i+verStartIndex+1] == true)){
							div.setAttribute("is_white", "yes")
						}
						if(is_show_white && (horArrayAvailability[j] == true) && (verArrayAvailability[i+verStartIndex+1] == true)){
							style += "background-color: #ffffff;"
						}else{
							style += "background-color: #e8f2fe;"
						}
						div.setAttribute("class", "cell")
						div.setAttribute("style", style)
						//div.appendChild(document.createTextNode(data[i+verStartIndex+1][j]))
						if(data[i+verStartIndex+1][j]){
							showValue = data[i+verStartIndex+1][j];
						}else{
							showValue = defaultCellValue;
						}
						w = getWidth(showValue)
						showValue = getShortText(showValue, w)
						/*if(w > horArrayWidth[j]){							
							showValue = showValue.substring(0, showValue.length-4)
							while(getWidth("" + showValue + "###") > horArrayWidth[j]){
								showValue = showValue.substring(0, showValue.length-1)
							}
							showValue = "" +showValue + "###"
						}
						*/
						div.appendChild(document.createTextNode(showValue))
						div.setAttribute("textWidth", w)
						if(data[i+verStartIndex+1][j])
							div.setAttribute("realValue", data[i+verStartIndex+1][j])
						else{
							div.setAttribute("realValue", defaultCellValue)
						}
						div.setAttribute("showValue", showValue)
						div.setAttribute("onclick", "createInputElement(this)")
						div.setAttribute("xtree", horArrayPath[j]);
						div.setAttribute("ytree", verArrayPath[i+verStartIndex+1]);
						if(document.all){
							div.style.setAttribute("cssText", style)
							div.className = "cellIE"
							div.onclick = function createInputElementIE(){return createInputElement(this)}
						}
						td.appendChild(div)
						tr.appendChild(td)
					}
					if(tbody.childNodes[i+verStartIndex+1]){
						tbody.insertBefore(tr, tbody.childNodes[i+verStartIndex+1])
					}else{
						tbody.insertBefore(tr, null)
					}
				}
			}			
			verStartIndex = -1;			
		}
		is_blocked = false;
		syncronize()
	}
	var horArrayMinWidth = new Array(1000)
	for(var  i = 0; i< 1000; i++)
		horArrayMinWidth[i] = 0;
	
	function setupRowWidth(){
		//alert("from setupRowWidth")
		var content = document.getElementById("content")
		var table = content.firstChild
		var tbody = table.firstChild
		
		for(var i = 0; i< tbody.childNodes.length; i++){
			horArrayMinWidth[i] = 0;	
		}
		for(var i = 0; i< tbody.childNodes.length; i++){
			var tr = tbody.childNodes[i]
			for(var j = 0; j< tr.childNodes.length; j++){
				var td = tr.childNodes[j]
				var obj = td.firstChild;
				var ww = obj.getAttribute("textWidth")
//alert(obj.getAttribute("realValue") + "---"obj.getAttribute("textWidth"))
				if(parseInt(ww) > parseInt(horArrayMinWidth[j])){
					if(ie){
						horArrayMinWidth[j] = ww + 8
					}else{
						horArrayMinWidth[j] = ww
					}
				}
			}
		}
		renderTree(node)
	}	
	function syncRowWidth(){		
		try{
			var content = document.getElementById("content")
			var table = content.firstChild
			var tbody = table.firstChild
			
			for(var i = 0; i< tbody.childNodes.length; i++){
				var tr = tbody.childNodes[i]
				for(var j = 0; j< tr.childNodes.length; j++){
					var td = tr.childNodes[j]
					var obj = td.firstChild;
					if(document.all)
						obj.style.width = horArrayWidth[j] + "px"
					else{
						if(obj.nodeName == "DIV"){
							obj.style.width = (horArrayWidth[j] - 5) + "px"
						}
						else
							obj.style.width = horArrayWidth[j] + "px"
					}
					//var showValue = getShortText(obj.getAttribute("realValue"), horArrayWidth[j])
					//obj.setAttribute("showValue", showValue)
					//if(obj.nodeName == "DIV")
						//obj.innerHTML = showValue
					//else
						//obj.value = showValue
					
				}
			}
			var tr = tbody.childNodes[0]
			var td = tr.childNodes[0]
			var obj = td.firstChild;
			//if(!obj.getAttribute("showValue")){
				//alert("yes")
				for(var i = 0; i< tbody.childNodes.length; i++){
					var tr = tbody.childNodes[i]
					for(var j = 0; j< tr.childNodes.length; j++){
						var td = tr.childNodes[j]
						var obj = td.firstChild;
						var showValue = getShortText(obj.getAttribute("realValue"), horArrayWidth[j])
						obj.setAttribute("showValue", showValue)
						if(obj.nodeName == "DIV")
							obj.innerHTML = showValue
						else
							obj.value = showValue
						
					}
				}
//			}
		}
		catch(e){}		
		
	}
	
	
	var firstTime = true
	function updateData(){
		if(horArrayCount*verArrayCount == 1)
			setStatusBar("Displaying #" + (horArrayCount*verArrayCount) + " cell.");
		else
			setStatusBar("Displaying #" + (horArrayCount*verArrayCount) + " cells.");
		
		if(firstTime == false){
			invalidateTable();			
			setupRowWidth();
			return
		}
		horStartIndex = -1
		verStartIndex = -1
		resetHorizontalTreeHeight()
		renderTree(node, "prepare");
		//alert(horizontalTreeHeights)
		renderVerticalTree(node1);
		syncronizeSize();
		firstTime = false;
		
		var startTime = new Date();
		
		var table = document.createElement("table");
		table.setAttribute("cellPadding", "0")
		table.cellpadding = "0"
		table.setAttribute("cellSpacing", "0");
		table.cellspacing = "0"
		var style = 'border-left: 0px solid #808080;'
		if(document.all)
			table.style.setAttribute('cssText',  style)
		else
			table.setAttribute("style", style)
		var tbody = document.createElement("tbody");
		
		var tdOrig = document.createElement("td");
		for(var i = 0; i< verArrayCount; i++){
			var tr = document.createElement("tr");
			for(var j = 0; j< horArrayCount; j++){
				var td = tdOrig.cloneNode(true)

				
				if(document.all){
					td.onmouseover = function (){hint(event, this)}
					td.onmouseout = function () {hideHint()}
				}else{
					td.setAttribute("onmouseover", "hint(event, this)")
					td.setAttribute("onmouseout", "hideHint()")							
				}
				
				var div = document.createElement("div")
				div.setAttribute("id", "" + i + "_" + j)
				var style = ""
							
				if(document.all){
					style = "width: " + horArrayWidth[j] + "px;"
					style += "height: 19px;"
				}else{
					style = "width: " + (horArrayWidth[j]-5) + "px;"
					style += "height: 18px;"
				}
				if((horArrayAvailability[j] == true) && (verArrayAvailability[i] == true)){
					div.setAttribute("is_white", "yes")
				}
				if(is_show_white && (horArrayAvailability[j] == true) && (verArrayAvailability[i] == true)){
					style += "background-color: #ffffff;"
				}else{
					style += "background-color: #e8f2fe;"
				}
				
				div.setAttribute("class", "cell")
				div.setAttribute("style", style)
				//div.appendChild(document.createTextNode(data[i][j]))

				if(data[i][j])
					showValue = data[i][j];
				else{
					showValue = defaultCellValue
				}
				
				w = getWidth(showValue)
				/*
				if(w > horArrayWidth[j]){							
					showValue = showValue.substring(0, showValue.length-4)
					while(getWidth("" + showValue + "###") > horArrayWidth[j]){
						showValue = showValue.substring(0, showValue.length-1)
					}
					showValue = "" +showValue + "###"
				}
				*/
				div.setAttribute("textWidth", w)
				div.appendChild(document.createTextNode(showValue))
				
				
				div.setAttribute("showValue", showValue)
				
				
				//div.appendChild(document.createTextNode("Showed Value"))
				if(data[i][j])
					div.setAttribute("realValue", data[i][j])
				else
					div.setAttribute("realValue", defaultCellValue)
				//div.setAttribute("showValue", "showed Value")
				div.setAttribute("onclick", "createInputElement(this)")
				div.setAttribute("unselectable", "on");//for IE
				div.setAttribute("onmousedown", "return false"); //for FF
				div.setAttribute("xtree", horArrayPath[j]);
				div.setAttribute("ytree", verArrayPath[i]);
				if(document.all){
					div.style.setAttribute("cssText", style)
					div.className = "cellIE"
					div.onclick = function createInputElementIE(){return createInputElement(this)}
				}
				td.appendChild(div)
				
				tr.appendChild(td)
			}
			tbody.appendChild(tr)
		}
		table.appendChild(tbody)		
		
		var content = document.getElementById("content")
		if(content.firstChild)
			content.removeChild(content.firstChild)
		
		document.getElementById("content").appendChild(table)
		
		
		
		globalIndex = 0;
		
		
		is_blocked = false;
		syncronize()
		setupRowWidth();
		varStartIndex = -1
		horStartIndex = -1
		
		
		
		
		
		return;
	}
	
	function createInputElement(obj){
		try{
			var res = true;//window.top.canCellBeEdited(getId(), unescape(obj.getAttribute("xtree")), unescape(obj.getAttribute("ytree")))
			if(res){

				var td = obj.parentNode
				
				var input = document.createElement("input");
				input.setAttribute("value", obj.innerHTML.replace("&nbsp;", " "))
				input.setAttribute("oldValue", obj.innerHTML.replace("&nbsp;", " "))
				input.setAttribute("realValue", obj.getAttribute("realValue"))
				var width = parseInt(obj.style.width);
				if(!document.all)
					width += 5;
				var style = ""
				if(document.all)
					style = "font-family: Tahoma; font-size: 11px; padding: 0px 1px; border-right: 1px solid #808080; border-bottom: 1px solid #808080; border-top: 0; border-left: 0; display: block;margin: 0; text-align: right; width: " + (width) + "px; height: 19px; line-height: 19px;"
				else
					style = "font-family: Tahoma; font-size: 11px; padding: 2px 2px 0 2px; border-right: 1px solid #808080; border-bottom: 1px solid #808080; border-top: 0; border-left: 0; display: block;margin: 0; text-align: right; width: " + (width) + "px; height: 19px; line-height: 19px;"
				if(obj.getAttribute("is_white") == "yes"){
					input.setAttribute("is_white", "yes");
				}
				if(is_show_white && obj.getAttribute("is_white") == "yes"){
					style += "background-color: #ffffff;"
				}
				else
					style += "background-color: #e8f2fe;"
				
				input.setAttribute("showValue", obj.getAttribute("showValue"))
				input.setAttribute("textWidth", obj.getAttribute("textWidth"))
				input.setAttribute("style", style)
				input.setAttribute("onfocus", "return processFocus(this, event)");
				input.setAttribute("onkeypress", "return processChange(this, event)")
				input.setAttribute("onblur", "return processBlur(this, event)")
				
				input.setAttribute("xtree", obj.getAttribute("xtree"));
				input.setAttribute("ytree", obj.getAttribute("ytree"));
				
				if(document.all){
					input.onblur = function processBlurIE(){return processBlur(this, event)}
					input.onfocus = function processFocusIE(){return processFocus(this, event)}
					input.onkeypress = function processChangeIE(){return processChange(this, event)}
					style += "margin-bottom: -1px;margin-top: -1px;"
					input.style.setAttribute("cssText", style)
				}

				td.removeChild(obj)
				td.appendChild(input)
				
				input.focus();
				td.firstChild.focus()
			}
		}catch(e){}
	}
/*	
	function setData(x, y, value, width, height){
		for(var i = y; i< y + height; i ++){
			for(var j = x; j < x + width; j++){
				data[i][j] = value[i][j];
			}
		}
	}
	*/
 	function changeZstate(newValue){
 		is_show_white = newValue
 	}
	function cubeTableSetCellValue(x, y, newValue){ 	
		data[x][y] = newValue;
		try{
	 		var table = document.getElementById("content").firstChild
	 		var tbody = table.firstChild
	 		var tr = tbody.childNodes[x]
	 		var td = tr.childNodes[y]
	 		var obj = td.firstChild
	 		
	 		if((horArrayAvailability[y] == true) && (verArrayAvailability[x] == true)){
	 			if(is_show_white)
	 				obj.style.backgroundColor = "#ffffff";
	 			else
	 				obj.style.backgroundColor = "#e8f2fe";
	 		}
	 		var showValue = newValue
	 		var w = getWidth(showValue)	 		
	 		var width = parseInt(obj.style.width)
	 		showValue = getShortText(showValue, width)
	 		obj.setAttribute("textWidth", w)
	 		if(obj.nodeName == "DIV"){
	 			obj.innerHTML = showValue
	 		}
	 		else{
	 			obj.value = showValue
	 			obj.setAttribute("oldValue", newValue)
	 		}
	 		obj.setAttribute("showValue", showValue)
	 		obj.setAttribute("realValue", newValue)	 		
	 	}catch(e){}
	}
	
	 function setStatusBar(str){
	 	window.status = str
	 }

	 
var hintObj = null	 
var hintTimer = null
var hintTimerRunning = false;
var hintX = -1;
var hintY = -1;

function hint(event, obj){
	
	if(obj.childNodes[0].getAttribute("realValue") == obj.childNodes[0].getAttribute("showValue"))
		return;
	if(hintTimerRunning){
		clearTimeout(hintTimer)
		hintTimerRunning = false
	}
	
	hintObj = obj
	
	hintTimerRunning = true;
	hintTimer = setTimeout("showHint1()", hintTime)
	hintX = event.clientX + document.body.scrollLeft + "px";
	hintY = event.clientY + 18 + document.body.scrollTop + "px";
	
	return;	
		
	var hint = document.getElementById("hint");
	//alert(obj.innerHTML)
	
	
	
	hint.innerHTML = obj.childNodes[0].getAttribute("realValue");
	
	hint.style.left = event.clientX + document.body.scrollLeft + "px";
	hint.style.top = event.clientY + 18 + document.body.scrollTop + "px";
	hint.style.display = ""
}

function showHint1(){
	var hint = document.getElementById("hint");
	hint.innerHTML = hintObj.childNodes[0].getAttribute("realValue");
	
	hint.style.left = hintX
	hint.style.top = hintY
	hint.style.display = ""
	
}

function hideHint(){
	var hint = document.getElementById("hint");
	hint.style.display = "none"	
	if(hintTimerRunning){
		clearTimeout(hintTimer)
		hintTimerRunning = false		
	}
}

	 

function getShortText(text, w){
		if(text.length < 4){
			return text
		}
	
		var textWidth = getWidth(text)
	 	
	 	if(ie){
			if(textWidth <= w-2){
		 		return text
			}	 		
	 	}else{
			if(textWidth <= w-4){
		 		return text
			}
	 	}
		if(w<50){
			return text
		}
	 	w -= 4;
	 	while(textWidth > w){
	 		text = text.substring(0, text.length-1)
	 		textWidth = getWidth(text+"###");
	 	}
	 	return text+"###"
	 }
	 
	 
/*	
	function canCellBeEdited(id, xTree, yTree){
    		
    }
    function updateCell(id, xTree, yTree, newValue){
    	}
*/
