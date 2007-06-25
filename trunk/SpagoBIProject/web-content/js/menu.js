

//***************************************************************
//******** Functions for nested menu class **********************
//***************************************************************  
 
 function openmenuNM(idmenu) {
	  	try {
	  		status = $(idmenu).style.display;
	  		menuopened=getCookie('menuopened');
	  		if(status=='inline') {
	  			$(idmenu).style.display = 'none';
	  			if(menuopened!=null) {
					newmenuopened = '';
					idmenusop = menuopened.split(",");
					for(i=0; i<idmenusop.length; i++) {
						idmenuop = idmenusop[i];
						if( (idmenuop!=idmenu) && (idmenuop!='') ) {
							newmenuopened = newmenuopened + idmenuop + ",";
						}
					}
					setCookie('menuopened',newmenuopened,365)
				}
	  		} else {
	  			$(idmenu).style.display = 'inline';
				if (menuopened==null) {
					setCookie('menuopened',(idmenu+','),365)
				} else {
		    		menuopened = menuopened + idmenu + ',';
		    		setCookie('menuopened',menuopened,365)
				}
	  		}
	  	} catch (e) {
			alert('Cannot open menu ...');
      	}
  }
	
	
 function openmenusNMFunct() {
    	menuopened=getCookie('menuopened');
    	if(menuopened!=null) {
    		idmenus = menuopened.split(',');
    		for(i=0; i<idmenus.length; i++) {
    			idmenu = idmenus[i];
    			try {
    				$(idmenu).style.display = 'inline';
    			} catch (err) {
    				/* ignore: the menu is not in this pager (maybe another tab) */
    			}
    		}
    	}
 }
  
  
//***************************************************************
//******** Functions for slide show menu class **********************
//***************************************************************  
  
  var tCloseAll = null;
  
  
  function closeAll() {
    for(i=0; i<menulevels.length; i++) {
      menulevel = menulevels[i];
      idMenu = 'menu_' + menulevel[0];
      $(idMenu).style.display='none';
    } 
  }
  
 	function overHandler(obj, level, e, idfolder) {
      window.clearTimeout(tCloseAll);
	    for(i=0; i<menulevels.length; i++) {
	      menulevel = menulevels[i];
	      if(menulevel[0] != idfolder) {
		      deepMenu = menulevel[1];
		      idMenu = 'menu_' + menulevel[0];
		      if(parseInt(deepMenu)>parseInt(level)) {
		        $(idMenu).style.display='none';
		      }
		   }   
	    } 
		obj.className = 'menuItemOver';
	}
	
	
	function outHandler(obj, e) {
		obj.className = 'menuItem';
		tCloseAll = setTimeout('closeAll()', 500);
 	}
	
	function openmenu(idfolder, e) {
	  	try {
          var idmenu = 'menu_' + idfolder;
    	  	var idmenulink = 'menu_link_' + idfolder;
    	  	var idmenulinklast = 'menu_link_last_' + idfolder;
    	  	var trlink = $(idmenulink);
    	  	if(trlink==null) return;
    	  	var tdlinklast = $(idmenulinklast);
    	  	if(tdlinklast==null) return;
    	    var postrlink = findPos(trlink);
    	    if(postrlink==null) return;
    	    var postrlinklast = findPos(tdlinklast);
    		  var wid = postrlinklast[0] - postrlink[0] + 25;
          $(idmenu).style.left= wid + postrlink[0] + 4 + 'px';
          $(idmenu).style.top= (postrlink[1]-15) + 'px';
          $(idmenu).style.display= 'inline';
      } catch (e) {

      }
	}
	
	function checkclosemenu(idfolder, e) {  
    	var idmenu = 'menu_' + idfolder;
	  	var idmenulink = 'menu_link_' + idfolder;
	  	var idmenulinklast = 'menu_link_last_' + idfolder;
		  var trlink = $(idmenulink);
	  	var tdlinklast = $(idmenulinklast);
	    var postrlink = findPos(trlink);
	    var postrlinklast = findPos(tdlinklast);
		  var wid = postrlinklast[0] - postrlink[0] + 25;
		  hei = 30;	  
      if( (e.clientY <= (postrlink[1]+3)) || (e.clientY > (postrlink[1] + hei - 3)) || (e.clientX <= (postrlink[0]+3)) ) {
            $(idmenu).style.display= 'none';
      }
	}