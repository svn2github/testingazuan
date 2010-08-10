function hideMenu(event) {
	divM = document.getElementById('divmenuFunct');
    yup = parseInt(divM.style.top) - parseInt(document.documentElement.scrollTop);
    ydown = parseInt(divM.style.top) + parseInt(divM.offsetHeight) - parseInt(document.documentElement.scrollTop);
    xleft = parseInt(divM.style.left);
    xright = parseInt(divM.style.left) + parseInt(divM.offsetWidth);
    if( (event.clientY<=(yup+2)) || (event.clientY>=ydown) || (event.clientX<=(xleft+2)) || (event.clientX>=xright) ) { 
		divM.style.display = 'none' ;
	}	
}	

function showMenu(event, divM) {
	divM.style.left = '' + (event.clientX-5) + 'px';
	divM.style.top = '' + (event.clientY + document.documentElement.scrollTop - 5)  + 'px' ;
	divM.style.display = 'inline' ;
}