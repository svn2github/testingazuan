
selField = "";

function dTree(objName) {
				this.config = {
					target			: null,
					folderLinks		: true,
					useSelection	: true,
					useCookies		: true,
					useLines		: true,
					useIcons		: true,
					useStatusText	: true,
					closeSameLevel	: false,
					inOrder			: false
				}
				this.icon = {
					root		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treebase.gif")%>',
					folder		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treefolder.gif")%>',
					folderOpen	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treefolderopen.gif")%>',
					node		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treepage.gif")%>',
					empty		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeempty.gif")%>',
					line		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeline.gif")%>',
					join		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treejoin.gif")%>',
					joinBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treejoinbottom.gif")%>',
					plus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeplus.gif")%>',
					plusBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeplusbottom.gif")%>',
					minus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeminus.gif")%>',
					minusBottom	: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treeminusbottom.gif")%>',
					nlPlus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treenolines_plus.gif")%>',
					nlMinus		: '<%=qbeUrl.conformStaticResourceLink(request, "/img/treenolines_minus.gif")%>'
				};
				this.obj = objName;
				this.aNodes = [];
				this.aIndent = [];
				this.root = new Node(-1);
				this.selectedNode = null;
				this.selectedFound = false;
				this.completed = false;
			}


function openDivOperatoriSelect(fieldNameForForm, event){
	div = document.getElementById("divOperatoriSelect");
	
	div.style.display = 'inline';
	div.style.top = event.clientY+ 'px';
	div.style.left = event.clientX+ 'px';

	selField = fieldNameForForm;

	return true;
}
		
function removeOperator(fieldNameForForm, event){
			
	selField = fieldNameForForm;

	var oldValue= document.getElementById(selField).value;
	var spanId = selField + '_span';
	var spanNode = document.getElementById(spanId);
	var spanOldValue = spanNode.childNodes[0].nodeValue;
	
	var cleanValue='';
	var spanCleanVlaue='';
	
	if ( oldValue.indexOf("(") > 0 &&  oldValue.indexOf(")") > 0){
		var idxOpenedPar = oldValue.indexOf("("); 
		var idxClosePar = oldValue.indexOf(")");
		//alert(" Il campo ha un operatore ");
		cleanValue=oldValue.substring(idxOpenedPar+1, idxClosePar);
	}else{
		cleanValue = oldValue;
	}
	
	if ( spanOldValue.indexOf("(") > 0 &&  spanOldValue.indexOf(")") > 0){
		var idxOpenedParS = spanOldValue.indexOf("("); 
		var idxCloseParS = spanOldValue.indexOf(")");
		
		spanCleanValue=spanOldValue.substring(idxOpenedParS+1, idxCloseParS);
	}else{
		spanCleanValue = spanOldValue;
	}
	
	
	document.getElementById(selField).value = cleanValue;
	spanNode.childNodes[0].nodeValue = spanCleanValue;
	
	var formUpdSelect = document.getElementById("formUpdateSelect");
	formUpdSelect.submit();
	return true; 
	
	
	return true;
}


function applyOperator(operator) {
	var oldValue= document.getElementById(selField).value;
	var spanId = selField + '_span';
	var spanNode = document.getElementById(spanId);
	var spanOldValue = spanNode.childNodes[0].nodeValue;
	
	var cleanValue='';
	var spanCleanVlaue='';
	
	//alert ("old Value " + oldValue);
	
	if ( oldValue.indexOf("(") > 0 &&  oldValue.indexOf(")") > 0){
		var idxOpenedPar = oldValue.indexOf("("); 
		var idxClosePar = oldValue.indexOf(")");
		//alert(" Il campo ha un operatore ");
		cleanValue=oldValue.substring(idxOpenedPar+1, idxClosePar);
	}else{
		cleanValue = oldValue;
	}
	
	
	if ( spanOldValue.indexOf("(") > 0 &&  spanOldValue.indexOf(")") > 0){
		var idxOpenedParS = spanOldValue.indexOf("("); 
		var idxCloseParS = spanOldValue.indexOf(")");
		//alert(" Lo span ha un operatore ");
		spanCleanValue=spanOldValue.substring(idxOpenedParS+1, idxCloseParS);
	}else{
		spanCleanValue = spanOldValue;
	}
	
	//alert(" CleanValue " + cleanValue);
		//alert(" Span CleanValue " + spanCleanValue);
				
	var newValue = '';
	var newSpanValue = '';
	
	if ((operator == 'distinct') || (operator == 'all')){
		
		newValue = operator + ' ' + cleanValue;
		newSpanValue = operator + ' ' + spanCleanValue;
		
	}else{
		
		newValue = operator + '(' + cleanValue + ')';
		newSpanValue = operator + '(' + spanCleanValue + ')';
	
	}
	
	
	//alert(" New Value Field " + newValue);
		//alert(" New Value Span " + newSpanValue);
	
	document.getElementById(selField).value = newValue;
	spanNode.childNodes[0].nodeValue = newSpanValue;
	
	div = document.getElementById("divOperatoriSelect");
	div.style.display = 'none';
	
	
	var formUpdSelect = document.getElementById("formUpdateSelect");
	formUpdSelect.submit();
	return; 
}


function inputAS(fieldNameForForm, aliasfieldNameForForm, event){
	
	selField = fieldNameForForm; 
	
	var aliasSelField = aliasfieldNameForForm;
	
	var spanId = aliasSelField + '_span';
	
	var spanIdSelField = selField + '_span';
	
	var spanNode = document.getElementById(spanId);
	
	var spanIdSelNodeField = document.getElementById(spanIdSelField);
	
	
	var strPrompt = spanIdSelNodeField.childNodes[0].nodeValue + ' as ? ';
	
	
	
	var strCurrValue = spanNode.childNodes[0].nodeValue;

	var trimmedStrCurrValue = Trim(strCurrValue);
	

	
	var newValue = prompt(Trim(strPrompt), trimmedStrCurrValue); 
	
	if (newValue != null){
	
		
		document.getElementById(aliasSelField).value = newValue;
	
	
		
		spanNode.childNodes[0].nodeValue = newValue;
	
	
		var formUpdSelect = document.getElementById("formUpdateSelect");
		formUpdSelect.submit();
	}
	return true;
}

function closeDiv(){
	
	div = document.getElementById("divOperatoriSelect");
	div.style.display = 'none';
	
	return; 
}