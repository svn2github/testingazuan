update script/spagobi.test.js file

Find and replace
"./ -> "../js/spagobi/source/

Replace line 2/3 to :
if(qxsettings["qx.resourceUri"]==undefined)qxsettings["qx.resourceUri"]="../js/spagobi/build/resource/qx";\
