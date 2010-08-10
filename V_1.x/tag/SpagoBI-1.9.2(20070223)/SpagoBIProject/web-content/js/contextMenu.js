function hideMenu(event) {
	divM = document.getElementById('divmenuFunct');
	var theTop;
	if (document.documentElement && document.documentElement.scrollTop) {
		theTop = document.documentElement.scrollTop;
	}
	else {
		if (document.body){
			theTop = document.body.scrollTop;
		}
	}
    yup = parseInt(divM.style.top) - parseInt(theTop);
    ydown = parseInt(divM.style.top) + parseInt(divM.offsetHeight) - parseInt(theTop);
    xleft = parseInt(divM.style.left);
    xright = parseInt(divM.style.left) + parseInt(divM.offsetWidth);
    if( (event.clientY<=(yup+2)) || (event.clientY>=ydown) || (event.clientX<=(xleft+2)) || (event.clientX>=xright) ) { 
		divM.style.display = 'none' ;
	}	
}	

function showMenu(event, divM) {
	var theTop;
	if (document.documentElement && document.documentElement.scrollTop) {
		theTop = document.documentElement.scrollTop;
	}
	else {
		if (document.body){
			theTop = document.body.scrollTop;
		}
	}
	var theLeft;
	if (document.documentElement && document.documentElement.scrollLeft) {
		theLeft = document.documentElement.scrollLeft;
	}
	else {
		if (document.body){
			theLeft = document.body.scrollLeft;
		}
	}
	divM.style.left = '' + (event.clientX + theLeft - 5) + 'px';
	divM.style.top = '' + (event.clientY + theTop - 5)  + 'px' ;
	divM.style.display = 'inline' ;
}
