/*

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

*/

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