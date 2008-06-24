if(!window.qxsettings)qxsettings={};
if(qxsettings["qx.resourceUri"]==undefined)qxsettings["qx.resourceUri"]="../js/spagobi/build/resource/qx";
if(qxsettings["spagobi.test.resourceUri"]==undefined)qxsettings["spagobi.test.resourceUri"]="./resource/spagobi.test";
if(qxsettings["qx.theme"]==undefined)qxsettings["qx.theme"]="qx.theme.ClassicRoyale";
if(qxsettings["qx.minLogLevel"]==undefined)qxsettings["qx.minLogLevel"]=200;
if(qxsettings["qx.logAppender"]==undefined)qxsettings["qx.logAppender"]="qx.log.appender.Native";
if(qxsettings["qx.application"]==undefined)qxsettings["qx.application"]="spagobi.test.TestApplication";
if(qxsettings["qx.version"]==undefined)qxsettings["qx.version"]="0.7.3 ";
if(qxsettings["qx.isSource"]==undefined)qxsettings["qx.isSource"]=false;
if(!window.qxvariants)qxvariants={};
qxvariants["qx.deprecationWarnings"]="off";
qxvariants["qx.debug"]="off";
qxvariants["qx.compatibility"]="on";
qxvariants["qx.aspects"]="off";



/* ID: qx.core.Bootstrap */
qx={Class:{createNamespace:function($0,
$1){var $2=$0.split(".");
var $3=window;
var $4=$2[0];
for(var $5=0,
$6=$2.length-1;$5<$6;$5++,
$4=$2[$5]){if(!$3[$4]){$3=$3[$4]={};
}else{$3=$3[$4];
}}$3[$4]=$1;
return $4;
},
define:function($0,
$1){if(!$1){var $1={statics:{}};
}this.createNamespace($0,
$1.statics);
if($1.defer){$1.defer($1.statics);
}qx.core.Bootstrap.__registry[$0]=$1.statics;
}}};
qx.Class.define("qx.core.Bootstrap",
{statics:{LOADSTART:new Date,
time:function(){return new Date().getTime();
},
since:function(){return this.time()-this.LOADSTART;
},
__registry:{}}});




/* ID: qx.lang.Core */
qx.Class.define("qx.lang.Core");
if(!Error.prototype.toString||Error.prototype.toString()=="[object Error]"){Error.prototype.toString=function(){return this.message;
};
}if(!Array.prototype.indexOf){Array.prototype.indexOf=function($0,
$1){if($1==null){$1=0;
}else if($1<0){$1=Math.max(0,
this.length+$1);
}
for(var $2=$1;$2<this.length;$2++){if(this[$2]===$0){return $2;
}}return -1;
};
}
if(!Array.prototype.lastIndexOf){Array.prototype.lastIndexOf=function($0,
$1){if($1==null){$1=this.length-1;
}else if($1<0){$1=Math.max(0,
this.length+$1);
}
for(var $2=$1;$2>=0;$2--){if(this[$2]===$0){return $2;
}}return -1;
};
}
if(!Array.prototype.forEach){Array.prototype.forEach=function($0,
$1){var $2=this.length;
for(var $3=0;$3<$2;$3++){$0.call($1,
this[$3],
$3,
this);
}};
}
if(!Array.prototype.filter){Array.prototype.filter=function($0,
$1){var $2=this.length;
var $3=[];
for(var $4=0;$4<$2;$4++){if($0.call($1,
this[$4],
$4,
this)){$3.push(this[$4]);
}}return $3;
};
}
if(!Array.prototype.map){Array.prototype.map=function($0,
$1){var $2=this.length;
var $3=[];
for(var $4=0;$4<$2;$4++){$3.push($0.call($1,
this[$4],
$4,
this));
}return $3;
};
}
if(!Array.prototype.some){Array.prototype.some=function($0,
$1){var $2=this.length;
for(var $3=0;$3<$2;$3++){if($0.call($1,
this[$3],
$3,
this)){return true;
}}return false;
};
}
if(!Array.prototype.every){Array.prototype.every=function($0,
$1){var $2=this.length;
for(var $3=0;$3<$2;$3++){if(!$0.call($1,
this[$3],
$3,
this)){return false;
}}return true;
};
}if(!String.prototype.quote){String.prototype.quote=function(){return '"'+this.replace(/\\/g,
"\\\\").replace(/\"/g,
"\\\"")+'"';
};
}




/* ID: qx.lang.Generics */
qx.Class.define("qx.lang.Generics",
{statics:{__map:{"Array":["join",
"reverse",
"sort",
"push",
"pop",
"shift",
"unshift",
"splice",
"concat",
"slice",
"indexOf",
"lastIndexOf",
"forEach",
"map",
"filter",
"some",
"every"],
"String":["quote",
"substring",
"toLowerCase",
"toUpperCase",
"charAt",
"charCodeAt",
"indexOf",
"lastIndexOf",
"toLocaleLowerCase",
"toLocaleUpperCase",
"localeCompare",
"match",
"search",
"replace",
"split",
"substr",
"concat",
"slice"]},
__wrap:function($0,
$1){return function($2){return $0.prototype[$1].apply($2,
Array.prototype.slice.call(arguments,
1));
};
},
__init:function(){var $0=qx.lang.Generics.__map;
for(var $1 in $0){var $2=window[$1];
var $3=$0[$1];
for(var $4=0,
$5=$3.length;$4<$5;$4++){var $6=$3[$4];
if(!$2[$6]){$2[$6]=qx.lang.Generics.__wrap($2,
$6);
}}}}},
defer:function($0){$0.__init();
}});




/* ID: qx.core.Setting */
qx.Class.define("qx.core.Setting",
{statics:{__settings:{},
define:function($0,
$1){if($1===undefined){throw new Error('Default value of setting "'+$0+'" must be defined!');
}
if(!this.__settings[$0]){this.__settings[$0]={};
}else if(this.__settings[$0].defaultValue!==undefined){throw new Error('Setting "'+$0+'" is already defined!');
}this.__settings[$0].defaultValue=$1;
},
get:function($0){var $1=this.__settings[$0];
if($1===undefined){throw new Error('Setting "'+$0+'" is not defined.');
}
if($1.value!==undefined){return $1.value;
}return $1.defaultValue;
},
__init:function(){if(window.qxsettings){for(var $0 in qxsettings){if(($0.split(".")).length<2){throw new Error('Malformed settings key "'+$0+'". Must be following the schema "namespace.key".');
}
if(!this.__settings[$0]){this.__settings[$0]={};
}this.__settings[$0].value=qxsettings[$0];
}window.qxsettings=undefined;
try{delete window.qxsettings;
}catch(ex){}this.__loadUrlSettings();
}},
__loadUrlSettings:function(){if(this.get("qx.allowUrlSettings")!=true){return;
}var $0=document.location.search.slice(1).split("&");
for(var $1=0;$1<$0.length;$1++){var $2=$0[$1].split(":");
if($2.length!=3||$2[0]!="qxsetting"){continue;
}var $3=$2[1];
if(!this.__settings[$3]){this.__settings[$3]={};
}this.__settings[$3].value=decodeURIComponent($2[2]);
}}},
defer:function($0){$0.define("qx.allowUrlSettings",
false);
$0.define("qx.allowUrlVariants",
false);
$0.define("qx.resourceUri",
"./resource");
$0.define("qx.isSource",
true);
$0.__init();
}});




/* ID: qx.lang.Array */
qx.Class.define("qx.lang.Array",
{statics:{fromArguments:function($0){return Array.prototype.slice.call($0,
0);
},
fromCollection:function($0){return Array.prototype.slice.call($0,
0);
},
fromShortHand:function($0){var $1=$0.length;
if($1>4||$1==0){this.error("Invalid number of arguments!");
}var $2=qx.lang.Array.copy($0);
switch($1){case 1:$2[1]=$2[2]=$2[3]=$2[0];
break;
case 2:$2[2]=$2[0];
case 3:$2[3]=$2[1];
}return $2;
},
copy:function($0){return $0.concat();
},
clone:function($0){return $0.concat();
},
getLast:function($0){return $0[$0.length-1];
},
getFirst:function($0){return $0[0];
},
insertAt:function($0,
$1,
$2){$0.splice($2,
0,
$1);
return $0;
},
insertBefore:function($0,
$1,
$2){var $3=$0.indexOf($2);
if($3==-1){$0.push($1);
}else{$0.splice($3,
0,
$1);
}return $0;
},
insertAfter:function($0,
$1,
$2){var $3=$0.indexOf($2);
if($3==-1||$3==($0.length-1)){$0.push($1);
}else{$0.splice($3+1,
0,
$1);
}return $0;
},
removeAt:function($0,
$1){return $0.splice($1,
1)[0];
},
removeAll:function($0){return $0.length=0;
},
append:function($0,
$1){{};
Array.prototype.push.apply($0,
$1);
return $0;
},
remove:function($0,
$1){var $2=$0.indexOf($1);
if($2!=-1){$0.splice($2,
1);
return $1;
}},
contains:function($0,
$1){return $0.indexOf($1)!=-1;
},
equals:function($0,
$1){if($0.length!==$1.length){return false;
}
for(var $2=0,
$3=$0.length;$2<$3;$2++){if($0[$2]!==$1[$2]){return false;
}}return true;
}}});




/* ID: qx.core.Variant */
qx.Class.define("qx.core.Variant",
{statics:{__variants:{},
__cache:{},
compilerIsSet:function(){return true;
},
define:function($0,
$1,
$2){{};
if(!this.__variants[$0]){this.__variants[$0]={};
}else{}this.__variants[$0].allowedValues=$1;
this.__variants[$0].defaultValue=$2;
},
get:function($0){var $1=this.__variants[$0];
{};
if($1.value!==undefined){return $1.value;
}return $1.defaultValue;
},
__init:function(){if(window.qxvariants){for(var $0 in qxvariants){{};
if(!this.__variants[$0]){this.__variants[$0]={};
}this.__variants[$0].value=qxvariants[$0];
}window.qxvariants=undefined;
try{delete window.qxvariants;
}catch(ex){}this.__loadUrlVariants(this.__variants);
}},
__loadUrlVariants:function(){if(qx.core.Setting.get("qx.allowUrlVariants")!=true){return;
}var $0=document.location.search.slice(1).split("&");
for(var $1=0;$1<$0.length;$1++){var $2=$0[$1].split(":");
if($2.length!=3||$2[0]!="qxvariant"){continue;
}var $3=$2[1];
if(!this.__variants[$3]){this.__variants[$3]={};
}this.__variants[$3].value=decodeURIComponent($2[2]);
}},
select:function($0,
$1){{};
for(var $2 in $1){if(this.isSet($0,
$2)){return $1[$2];
}}
if($1["default"]!==undefined){return $1["default"];
}{};
},
isSet:function($0,
$1){var $2=$0+"$"+$1;
if(this.__cache[$2]!==undefined){return this.__cache[$2];
}var $3=false;
if($1.indexOf("|")<0){$3=this.get($0)===$1;
}else{var $4=$1.split("|");
for(var $5=0,
$6=$4.length;$5<$6;$5++){if(this.get($0)===$4[$5]){$3=true;
break;
}}}this.__cache[$2]=$3;
return $3;
},
__isValidArray:function($0){return typeof $0==="object"&&$0!==null&&$0 instanceof Array;
},
__isValidObject:function($0){return typeof $0==="object"&&$0!==null&&!($0 instanceof Array);
},
__arrayContains:function($0,
$1){for(var $2=0,
$3=$0.length;$2<$3;$2++){if($0[$2]==$1){return true;
}}return false;
}},
defer:function($0){$0.define("qx.debug",
["on",
"off"],
"on");
$0.define("qx.compatibility",
["on",
"off"],
"on");
$0.define("qx.eventMonitorNoListeners",
["on",
"off"],
"off");
$0.define("qx.aspects",
["on",
"off"],
"off");
$0.define("qx.deprecationWarnings",
["on",
"off"],
"on");
$0.__init();
}});




/* ID: qx.core.Aspect */
qx.Class.define("qx.core.Aspect",
{statics:{__registry:[],
wrap:function($0,
$1,
$2){if(!qx.core.Setting.get("qx.enableAspect")){return $1;
}var $3=[];
var $4=[];
for(var $5=0;$5<this.__registry.length;$5++){var $6=this.__registry[$5];
if($0.match($6.re)&&($2==$6.type||$6.type=="*")){var $7=$6.pos;
if($7=="before"){$3.push($6.fcn);
}else{$4.push($6.fcn);
}}}
if($3.length==0&&$4.length==0){return $1;
}var $8=function(){for(var $5=0;$5<$3.length;$5++){$3[$5].call(this,
$0,
$1,
$2,
arguments);
}var $9=$1.apply(this,
arguments);
for(var $5=0;$5<$4.length;$5++){$4[$5].call(this,
$0,
$1,
$2,
arguments,
$9);
}return $9;
};
if($2!="static"){$8.self=$1.self;
$8.base=$1.base;
}$1.wrapper=$8;
return $8;
},
addAdvice:function($0,
$1,
$2,
$3){if($0!="before"&&$0!="after"){throw new Error("Unknown position: '"+$0+"'");
}this.__registry.push({pos:$0,
type:$1,
re:$2,
fcn:$3});
}},
defer:function(){qx.core.Setting.define("qx.enableAspect",
false);
}});




/* ID: qx.core.Client */
qx.Class.define("qx.core.Client",
{statics:{__init:function(){var $0=window.location.protocol==="file:";
var $1=navigator.userAgent;
var $2=navigator.vendor;
var $3=navigator.product;
var $4=navigator.platform;
var $5=false;
var $6;
var $7=null;
var $8=null;
var $9=0;
var $a=0;
var $b=0;
var $c=0;
var $d=null;
var $e=null;
var $f;
if(window.opera&&/Opera[\s\/]([0-9\.]*)/.test($1)){$7="opera";
$8=RegExp.$1;
$6="opera";
$8=$8.substring(0,
3)+"."+$8.substring(3);
$d=$1.indexOf("MSIE")!==-1?"mshtml":$1.indexOf("Mozilla")!==-1?"gecko":null;
}else if(typeof $2==="string"&&$2==="KDE"&&/KHTML\/([0-9-\.]*)/.test($1)){$7="khtml";
$6="konqueror";
$8=RegExp.$1;
}else if($1.indexOf("AppleWebKit")!=-1&&/AppleWebKit\/([^ ]+)/.test($1)){$7="webkit";
$8=RegExp.$1;
$e=$8.indexOf("+")!=-1;
var $g=RegExp("[^\\.0-9]").exec($8);
if($g){$8=$8.slice(0,
$g.index);
}
if($1.indexOf("Safari")!=-1){$6="safari";
}else if($1.indexOf("OmniWeb")!=-1){$6="omniweb";
}else if($1.indexOf("Shiira")!=-1){$6="shiira";
}else if($1.indexOf("NetNewsWire")!=-1){$6="netnewswire";
}else if($1.indexOf("RealPlayer")!=-1){$6="realplayer";
}else{$6="other webkit";
}
if($e){$6+=" (nightly)";
}}else if(window.controllers&&typeof $3==="string"&&$3==="Gecko"&&/rv\:([^\);]+)(\)|;)/.test($1)){$7="gecko";
$8=RegExp.$1;
if($1.indexOf("Firefox")!=-1){$6="firefox";
}else if($1.indexOf("Camino")!=-1){$6="camino";
}else if($1.indexOf("Galeon")!=-1){$6="galeon";
}else{$6="other gecko";
}}else if(/MSIE\s+([^\);]+)(\)|;)/.test($1)){$7="mshtml";
$8=RegExp.$1;
$6="explorer";
$5=!window.external;
}
if($8){$f=$8.split(".");
$9=$f[0]||0;
$a=$f[1]||0;
$b=$f[2]||0;
$c=$f[3]||0;
}var $h=[];
switch($7){case "gecko":$h.push("-moz-box-sizing");
break;
case "khtml":$h.push("-khtml-box-sizing");
break;
case "webkit":$h.push("-khtml-box-sizing");
$h.push("-webkit-box-sizing");
break;
case "mshtml":break;
default:break;
}$h.push("box-sizing");
var $i=document.compatMode!=="CSS1Compat";
var $j="en";
var $k=($7=="mshtml"?navigator.userLanguage:navigator.language).toLowerCase();
var $l=null;
var $m=$k.indexOf("-");
if($m!=-1){$l=$k.substr($m+1);
$k=$k.substr(0,
$m);
}var $n="none";
var $o=false;
var $p=false;
var $q=false;
var $r=false;
if($4.indexOf("Windows")!=-1||$4.indexOf("Win32")!=-1||$4.indexOf("Win64")!=-1){$o=true;
$n="win";
}else if($4.indexOf("Macintosh")!=-1||$4.indexOf("MacPPC")!=-1||$4.indexOf("MacIntel")!=-1){$p=true;
$n="mac";
}else if($4.indexOf("X11")!=-1||$4.indexOf("Linux")!=-1||$4.indexOf("BSD")!=-1){$q=true;
$n="unix";
}else{$r=true;
$n="other";
}var $s=false;
var $t=false;
var $u=false;
var $v=false;
if($7=="mshtml"){$s=true;
}if(document.implementation&&document.implementation.hasFeature){if(document.implementation.hasFeature("org.w3c.dom.svg",
"1.0")){$t=$u=true;
}}this._runsLocally=$0;
this._engineName=$7;
this._engineNameMshtml=$7==="mshtml";
this._engineNameGecko=$7==="gecko";
this._engineNameOpera=$7==="opera";
this._engineNameKhtml=$7==="khtml";
this._engineNameWebkit=$7==="webkit";
this._engineVersion=parseFloat($8);
this._engineVersionMajor=parseInt($9);
this._engineVersionMinor=parseInt($a);
this._engineVersionRevision=parseInt($b);
this._engineVersionBuild=parseInt($c);
this._engineQuirksMode=$i;
this._engineBoxSizingAttributes=$h;
this._engineEmulation=$d;
this._browserName=$6;
this._defaultLocale=$j;
this._browserPlatform=$n;
this._browserPlatformWindows=$o;
this._browserPlatformMacintosh=$p;
this._browserPlatformUnix=$q;
this._browserPlatformOther=$r;
this._browserModeHta=$5;
this._browserLocale=$k;
this._browserLocaleVariant=$l;
this._gfxVml=$s;
this._gfxSvg=$t;
this._gfxSvgBuiltin=$u;
this._gfxSvgPlugin=$v;
this._fireBugActive=(window.console&&console.log&&console.debug&&console.assert);
this._supportsTextContent=(document.documentElement.textContent!==undefined);
this._supportsInnerText=(document.documentElement.innerText!==undefined);
this._supportsXPath=!!document.evaluate;
this._supportsElementExtensions=!!window.HTMLElement;
},
getRunsLocally:function(){return this._runsLocally;
},
getEngine:function(){return this._engineName;
},
getBrowser:function(){return this._browserName;
},
getVersion:function(){return this._engineVersion;
},
getMajor:function(){return this._engineVersionMajor;
},
getMinor:function(){return this._engineVersionMinor;
},
getRevision:function(){return this._engineVersionRevision;
},
getBuild:function(){return this._engineVersionBuild;
},
getEmulation:function(){return this._engineEmulation;
},
isMshtml:function(){return this._engineNameMshtml;
},
isGecko:function(){return this._engineNameGecko;
},
isOpera:function(){return this._engineNameOpera;
},
isKhtml:function(){return this._engineNameKhtml;
},
isWebkit:function(){return this._engineNameWebkit;
},
isSafari2:function(){return this._engineNameWebkit&&(this._engineVersion<420);
},
isInQuirksMode:function(){return this._engineQuirksMode;
},
getLocale:function(){return this._browserLocale;
},
getLocaleVariant:function(){return this._browserLocaleVariant;
},
getDefaultLocale:function(){return this._defaultLocale;
},
usesDefaultLocale:function(){return this._browserLocale===this._defaultLocale;
},
getEngineBoxSizingAttributes:function(){return this._engineBoxSizingAttributes;
},
getPlatform:function(){return this._browserPlatform;
},
runsOnWindows:function(){return this._browserPlatformWindows;
},
runsOnMacintosh:function(){return this._browserPlatformMacintosh;
},
runsOnUnix:function(){return this._browserPlatformUnix;
},
supportsVml:function(){return this._gfxVml;
},
supportsSvg:function(){return this._gfxSvg;
},
usesSvgBuiltin:function(){return this._gfxSvgBuiltin;
},
usesSvgPlugin:function(){return this._gfxSvgPlugin;
},
isFireBugActive:function(){return this._fireBugActive;
},
supportsTextContent:function(){return this._supportsTextContent;
},
supportsInnerText:function(){return this._supportsInnerText;
},
getInstance:function(){return this;
}},
defer:function($0,
$1,
$2){$0.__init();
qx.core.Variant.define("qx.client",
["gecko",
"mshtml",
"opera",
"webkit",
"khtml"],
qx.core.Client.getInstance().getEngine());
}});




/* ID: qx.lang.Object */
qx.Class.define("qx.lang.Object",
{statics:{isEmpty:function($0){for(var $1 in $0){return false;
}return true;
},
hasMinLength:function($0,
$1){var $2=0;
for(var $3 in $0){if((++$2)>=$1){return true;
}}return false;
},
getLength:function($0){var $1=0;
for(var $2 in $0){$1++;
}return $1;
},
_shadowedKeys:["isPrototypeOf",
"hasOwnProperty",
"toLocaleString",
"toString",
"valueOf"],
getKeys:qx.core.Variant.select("qx.client",
{"mshtml":function($0){var $1=[];
for(var $2 in $0){$1.push($2);
}for(var $3=0,
$4=this._shadowedKeys,
$5=$4.length;$3<$5;$3++){if($0.hasOwnProperty($4[$3])){$1.push($4[$3]);
}}return $1;
},
"default":function($0){var $1=[];
for(var $2 in $0){$1.push($2);
}return $1;
}}),
getKeysAsString:function($0){var $1=qx.lang.Object.getKeys($0);
if($1.length==0){return "";
}return '"'+$1.join('\", "')+'"';
},
getValues:function($0){var $1=[];
for(var $2 in $0){$1.push($0[$2]);
}return $1;
},
mergeWith:function($0,
$1,
$2){if($2===undefined){$2=true;
}
for(var $3 in $1){if($2||$0[$3]===undefined){$0[$3]=$1[$3];
}}return $0;
},
carefullyMergeWith:function($0,
$1){qx.log.Logger.deprecatedMethodWarning(arguments.callee);
return qx.lang.Object.mergeWith($0,
$1,
false);
},
merge:function($0,
$1){var $2=arguments.length;
for(var $3=1;$3<$2;$3++){qx.lang.Object.mergeWith($0,
arguments[$3]);
}return $0;
},
copy:function($0){var $1={};
for(var $2 in $0){$1[$2]=$0[$2];
}return $1;
},
invert:function($0){var $1={};
for(var $2 in $0){$1[$0[$2].toString()]=$2;
}return $1;
},
getKeyFromValue:function($0,
$1){for(var $2 in $0){if($0[$2]===$1){return $2;
}}return null;
},
select:function($0,
$1){return $1[$0];
},
fromArray:function($0){var $1={};
for(var $2=0,
$3=$0.length;$2<$3;$2++){{};
$1[$0[$2].toString()]=true;
}return $1;
}}});




/* ID: qx.Class */
qx.Class.define("qx.Class",
{statics:{define:function($0,
$1){if(!$1){var $1={};
}if($1.include&&!($1.include instanceof Array)){$1.include=[$1.include];
}if($1.implement&&!($1.implement instanceof Array)){$1.implement=[$1.implement];
}if(!$1.hasOwnProperty("extend")&&!$1.type){$1.type="static";
}{};
var $2=this.__createClass($0,
$1.type,
$1.extend,
$1.statics,
$1.construct,
$1.destruct);
if($1.extend){if($1.properties){this.__addProperties($2,
$1.properties,
true);
}if($1.members){this.__addMembers($2,
$1.members,
true,
true);
}if($1.events){this.__addEvents($2,
$1.events,
true);
}if($1.include){for(var $3=0,
$4=$1.include.length;$3<$4;$3++){this.__addMixin($2,
$1.include[$3],
false);
}}}if($1.settings){for(var $5 in $1.settings){qx.core.Setting.define($5,
$1.settings[$5]);
}}if($1.variants){for(var $5 in $1.variants){qx.core.Variant.define($5,
$1.variants[$5].allowedValues,
$1.variants[$5].defaultValue);
}}if($1.defer){$1.defer.self=$2;
$1.defer($2,
$2.prototype,
{add:function($0,
$1){var $6={};
$6[$0]=$1;
qx.Class.__addProperties($2,
$6,
true);
}});
}if($1.implement){for(var $3=0,
$4=$1.implement.length;$3<$4;$3++){this.__addInterface($2,
$1.implement[$3]);
}}},
createNamespace:function($0,
$1){var $2=$0.split(".");
var $3=window;
var $4=$2[0];
for(var $5=0,
$6=$2.length-1;$5<$6;$5++,
$4=$2[$5]){if(!$3[$4]){$3=$3[$4]={};
}else{$3=$3[$4];
}}{};
$3[$4]=$1;
return $4;
},
isDefined:function($0){return this.getByName($0)!==undefined;
},
getTotalNumber:function(){return qx.lang.Object.getLength(this.__registry);
},
getByName:function($0){return this.__registry[$0];
},
include:function($0,
$1){{};
qx.Class.__addMixin($0,
$1,
false);
},
patch:function($0,
$1){{};
qx.Class.__addMixin($0,
$1,
true);
},
isSubClassOf:function($0,
$1){if(!$0){return false;
}
if($0==$1){return true;
}
if($0.prototype instanceof $1){return true;
}return false;
},
getPropertyDefinition:function($0,
$1){while($0){if($0.$$properties&&$0.$$properties[$1]){return $0.$$properties[$1];
}$0=$0.superclass;
}return null;
},
getByProperty:function($0,
$1){while($0){if($0.$$properties&&$0.$$properties[$1]){return $0;
}$0=$0.superclass;
}return null;
},
hasProperty:function($0,
$1){return !!this.getPropertyDefinition($0,
$1);
},
getEventType:function($0,
$1){var $0=$0.constructor;
while($0.superclass){if($0.$$events&&$0.$$events[$1]!==undefined){return $0.$$events[$1];
}$0=$0.superclass;
}return null;
},
supportsEvent:function($0,
$1){return !!this.getEventType($0,
$1);
},
hasOwnMixin:function($0,
$1){return $0.$$includes&&$0.$$includes.indexOf($1)!==-1;
},
getByMixin:function($0,
$1){var $2,
$3,
$4;
while($0){if($0.$$includes){$2=$0.$$flatIncludes;
for($3=0,
$4=$2.length;$3<$4;$3++){if($2[$3]===$1){return $0;
}}}$0=$0.superclass;
}return null;
},
getMixins:function($0){var $1=[];
while($0){if($0.$$includes){$1.push.apply($1,
$0.$$flatIncludes);
}$0=$0.superclass;
}return $1;
},
hasMixin:function($0,
$1){return !!this.getByMixin($0,
$1);
},
hasOwnInterface:function($0,
$1){return $0.$$implements&&$0.$$implements.indexOf($1)!==-1;
},
getByInterface:function($0,
$1){var $2,
$3,
$4;
while($0){if($0.$$implements){$2=$0.$$flatImplements;
for($3=0,
$4=$2.length;$3<$4;$3++){if($2[$3]===$1){return $0;
}}}$0=$0.superclass;
}return null;
},
getInterfaces:function($0){var $1=[];
while($0){if($0.$$implements){$1.push.apply($1,
$0.$$flatImplements);
}$0=$0.superclass;
}return $1;
},
hasInterface:function($0,
$1){return !!this.getByInterface($0,
$1);
},
implementsInterface:function($0,
$1){if(this.hasInterface($0,
$1)){return true;
}
try{qx.Interface.assert($0,
$1,
false);
return true;
}catch(ex){}return false;
},
getInstance:function(){if(!this.$$instance){this.$$allowconstruct=true;
this.$$instance=new this;
delete this.$$allowconstruct;
}return this.$$instance;
},
genericToString:function(){return "[Class "+this.classname+"]";
},
__registry:qx.core.Bootstrap.__registry,
__allowedKeys:null,
__staticAllowedKeys:null,
__validateConfig:function(){},
__createClass:function($0,
$1,
$2,
$3,
$4,
$5){var $6;
if(!$2&&true){$6=$3||{};
}else{$6={};
if($2){if(!$4){$4=this.__createDefaultConstructor();
}$6=this.__wrapConstructor($4,
$0,
$1);
}if($3){var $7;
for(var $8=0,
$9=qx.lang.Object.getKeys($3),
$a=$9.length;$8<$a;$8++){$7=$9[$8];
{$6[$7]=$3[$7];
};
var $b;
}}}var $c=this.createNamespace($0,
$6,
false);
$6.name=$6.classname=$0;
$6.basename=$c;
if(!$6.hasOwnProperty("toString")){$6.toString=this.genericToString;
}
if($2){var $d=$2.prototype;
var $e=this.__createEmptyFunction();
$e.prototype=$d;
var $f=new $e;
$6.prototype=$f;
$f.name=$f.classname=$0;
$f.basename=$c;
$4.base=$6.superclass=$2;
$4.self=$6.constructor=$f.constructor=$6;
if($5){{};
$6.$$destructor=$5;
}}{qx.Clazz=$6;
qx.Proto=$f||null;
qx.Super=$2||null;
};
this.__registry[$0]=$6;
return $6;
},
__addEvents:function($0,
$1,
$2){var $3,
$3;
if($0.$$events){for(var $3 in $1){$0.$$events[$3]=$1[$3];
}}else{$0.$$events=$1;
}},
__addProperties:function($0,
$1,
$2){var $3;
if($2===undefined){$2=false;
}var $4=!!$0.$$propertiesAttached;
for(var $5 in $1){$3=$1[$5];
{};
$3.name=$5;
if(!$3.refine){if($0.$$properties===undefined){$0.$$properties={};
}$0.$$properties[$5]=$3;
}if($3.init!==undefined){$0.prototype["__init$"+$5]=$3.init;
}if($3.event!==undefined){var $6={};
$6[$3.event]="qx.event.type.ChangeEvent";
this.__addEvents($0,
$6,
$2);
}if($3.inheritable){qx.core.Property.$$inheritable[$5]=true;
}if($4){qx.core.Property.attachMethods($0,
$5,
$3);
}if($3._fast){qx.core.LegacyProperty.addFastProperty($3,
$0.prototype);
}else if($3._cached){qx.core.LegacyProperty.addCachedProperty($3,
$0.prototype);
}else if($3._legacy){qx.core.LegacyProperty.addProperty($3,
$0.prototype);
}}},
__validateProperty:null,
__addMembers:function($0,
$1,
$2,
$3){var $4=$0.prototype;
var $5,
$6;
for(var $7=0,
$8=qx.lang.Object.getKeys($1),
$9=$8.length;$7<$9;$7++){$5=$8[$7];
$6=$1[$5];
{};
if($3!==false&&$6 instanceof Function){if($4[$5]){$6.base=$4[$5];
}$6.self=$0;
{};
}$4[$5]=$6;
}},
__addInterface:function($0,
$1){{};
var $2=qx.Interface.flatten([$1]);
if($0.$$implements){$0.$$implements.push($1);
$0.$$flatImplements.push.apply($0.$$flatImplements,
$2);
}else{$0.$$implements=[$1];
$0.$$flatImplements=$2;
}},
__addMixin:function($0,
$1,
$2){{};
var $3=qx.Mixin.flatten([$1]);
var $4;
for(var $5=0,
$6=$3.length;$5<$6;$5++){$4=$3[$5];
if($4.$$events){this.__addEvents($0,
$4.$$events,
$2);
}if($4.$$properties){this.__addProperties($0,
$4.$$properties,
$2);
}if($4.$$members){this.__addMembers($0,
$4.$$members,
$2,
false);
}}if($0.$$includes){$0.$$includes.push($1);
$0.$$flatIncludes.push.apply($0.$$flatIncludes,
$3);
}else{$0.$$includes=[$1];
$0.$$flatIncludes=$3;
}},
__createDefaultConstructor:function(){function $0(){arguments.callee.base.apply(this,
arguments);
}return $0;
},
__createEmptyFunction:function(){return function(){};
},
__wrapConstructor:function($0,
$1,
$2){var $3=[];
$3.push('var clazz=arguments.callee.constructor;');
{};
$3.push('if(!clazz.$$propertiesAttached)qx.core.Property.attach(clazz);');
$3.push('var retval=clazz.$$original.apply(this,arguments);');
$3.push('if(clazz.$$includes){var mixins=clazz.$$flatIncludes;');
$3.push('for(var i=0,l=mixins.length;i<l;i++){');
$3.push('if(mixins[i].$$constructor){mixins[i].$$constructor.apply(this,arguments);}}}');
$3.push('if(this.classname===',
$1,
'.classname)this.$$initialized=true;');
$3.push('return retval;');
var $4=new Function($3.join(""));
var $5;
if($2==="singleton"){$4.getInstance=this.getInstance;
}$4.$$original=$0;
$0.wrapper=$4;
return $4;
}},
defer:function($0){var $1;
}});




/* ID: qx.Mixin */
qx.Class.define("qx.Mixin",
{statics:{define:function($0,
$1){if($1){if($1.include&&!($1.include instanceof Array)){$1.include=[$1.include];
}{};
var $2=$1.statics?$1.statics:{};
for(var $3 in $2){$2[$3].mixin=$2;
}if($1.construct){$2.$$constructor=$1.construct;
}
if($1.include){$2.$$includes=$1.include;
}
if($1.properties){$2.$$properties=$1.properties;
}
if($1.members){$2.$$members=$1.members;
}
for(var $3 in $2.$$members){if($2.$$members[$3] instanceof Function){$2.$$members[$3].mixin=$2;
}}
if($1.events){$2.$$events=$1.events;
}
if($1.destruct){$2.$$destructor=$1.destruct;
}}else{var $2={};
}$2.$$type="Mixin";
$2.name=$0;
$2.toString=this.genericToString;
$2.basename=qx.Class.createNamespace($0,
$2);
this.__registry[$0]=$2;
return $2;
},
checkCompatibility:function($0){var $1=this.flatten($0);
var $2=$1.length;
if($2<2){return true;
}var $3={};
var $4={};
var $5={};
var $6;
for(var $7=0;$7<$2;$7++){$6=$1[$7];
for(var $8 in $6.events){if($5[$8]){throw new Error('Conflict between mixin "'+$6.name+'" and "'+$5[$8]+'" in member "'+$8+'"!');
}$5[$8]=$6.name;
}
for(var $8 in $6.properties){if($3[$8]){throw new Error('Conflict between mixin "'+$6.name+'" and "'+$3[$8]+'" in property "'+$8+'"!');
}$3[$8]=$6.name;
}
for(var $8 in $6.members){if($4[$8]){throw new Error('Conflict between mixin "'+$6.name+'" and "'+$4[$8]+'" in member "'+$8+'"!');
}$4[$8]=$6.name;
}}return true;
},
isCompatible:function($0,
$1){var $2=qx.Class.getMixins($1);
$2.push($0);
return qx.Mixin.checkCompatibility($2);
},
getByName:function($0){return this.__registry[$0];
},
isDefined:function($0){return this.getByName($0)!==undefined;
},
getTotalNumber:function(){return qx.lang.Object.getLength(this.__registry);
},
flatten:function($0){if(!$0){return [];
}var $1=$0.concat();
for(var $2=0,
$3=$0.length;$2<$3;$2++){if($0[$2].$$includes){$1.push.apply($1,
this.flatten($0[$2].$$includes));
}}return $1;
},
genericToString:function(){return "[Mixin "+this.name+"]";
},
__registry:{},
__allowedKeys:null,
__validateConfig:function(){}}});




/* ID: qx.Interface */
qx.Class.define("qx.Interface",
{statics:{define:function($0,
$1){if($1){if($1.extend&&!($1.extend instanceof Array)){$1.extend=[$1.extend];
}{};
var $2=$1.statics?$1.statics:{};
if($1.extend){$2.$$extends=$1.extend;
}
if($1.properties){$2.$$properties=$1.properties;
}
if($1.members){$2.$$members=$1.members;
}
if($1.events){$2.$$events=$1.events;
}}else{var $2={};
}$2.$$type="Interface";
$2.name=$0;
$2.toString=this.genericToString;
$2.basename=qx.Class.createNamespace($0,
$2);
qx.Interface.__registry[$0]=$2;
return $2;
},
getByName:function($0){return this.__registry[$0];
},
isDefined:function($0){return this.getByName($0)!==undefined;
},
getTotalNumber:function(){return qx.lang.Object.getLength(this.__registry);
},
flatten:function($0){if(!$0){return [];
}var $1=$0.concat();
for(var $2=0,
$3=$0.length;$2<$3;$2++){if($0[$2].$$extends){$1.push.apply($1,
this.flatten($0[$2].$$extends));
}}return $1;
},
assert:function($0,
$1,
$2){var $3=$1.$$members;
if($3){var $4=$0.prototype;
for(var $5 in $3){if(typeof $3[$5]==="function"){if(typeof $4[$5]!=="function"){throw new Error('Implementation of method "'+$5+'" is missing in class "'+$0.classname+'" required by interface "'+$1.name+'"');
}if($2===true&&!qx.Class.hasInterface($0,
$1)){$4[$5]=this.__wrapInterfaceMember($1,
$4[$5],
$5,
$3[$5]);
}}else{if(typeof $4[$5]===undefined){if(typeof $4[$5]!=="function"){throw new Error('Implementation of member "'+$5+'" is missing in class "'+$0.classname+'" required by interface "'+$1.name+'"');
}}}}}if($1.$$properties){for(var $5 in $1.$$properties){if(!qx.Class.hasProperty($0,
$5)){throw new Error('The property "'+$5+'" is not supported by Class "'+$0.classname+'"!');
}}}if($1.$$events){for(var $5 in $1.$$events){if(!qx.Class.supportsEvent($0,
$5)){throw new Error('The event "'+$5+'" is not supported by Class "'+$0.classname+'"!');
}}}var $6=$1.$$extends;
if($6){for(var $7=0,
$8=$6.length;$7<$8;$7++){this.assert($0,
$6[$7],
$2);
}}},
genericToString:function(){return "[Interface "+this.name+"]";
},
__registry:{},
__wrapInterfaceMember:function(){},
__allowedKeys:null,
__validateConfig:function(){}}});




/* ID: qx.locale.MTranslation */
qx.Mixin.define("qx.locale.MTranslation",
{members:{tr:function($0,
$1){var $2=qx.locale.Manager;
if($2){return $2.tr.apply($2,
arguments);
}throw new Error("To enable localization please include qx.locale.Manager into your build!");
},
trn:function($0,
$1,
$2,
$3){var $4=qx.locale.Manager;
if($4){return $4.trn.apply($4,
arguments);
}throw new Error("To enable localization please include qx.locale.Manager into your build!");
},
marktr:function($0){var $1=qx.locale.Manager;
if($1){return $1.marktr.apply($1,
arguments);
}throw new Error("To enable localization please include qx.locale.Manager into your build!");
}}});




/* ID: qx.log.MLogging */
qx.Mixin.define("qx.log.MLogging",
{members:{getLogger:function(){if(qx.log.Logger){return qx.log.Logger.getClassLogger(this.constructor);
}throw new Error("To enable logging please include qx.log.Logger into your build!");
},
debug:function($0,
$1){this.getLogger().debug($0,
this.toHashCode(),
$1);
},
info:function($0,
$1){this.getLogger().info($0,
this.toHashCode(),
$1);
},
warn:function($0,
$1){this.getLogger().warn($0,
this.toHashCode(),
$1);
},
error:function($0,
$1){this.getLogger().error($0,
this.toHashCode(),
$1);
},
printStackTrace:function(){this.getLogger().printStackTrace();
}}});




/* ID: qx.core.MUserData */
qx.Mixin.define("qx.core.MUserData",
{members:{setUserData:function($0,
$1){if(!this.__userData){this.__userData={};
}this.__userData[$0]=$1;
},
getUserData:function($0){if(!this.__userData){return null;
}return this.__userData[$0];
}},
destruct:function(){this._disposeFields("__userData");
}});




/* ID: qx.core.LegacyProperty */
qx.Class.define("qx.core.LegacyProperty",
{statics:{getSetterName:function($0){return qx.core.Property.$$method.set[$0];
},
getGetterName:function($0){return qx.core.Property.$$method.get[$0];
},
getResetterName:function($0){return qx.core.Property.$$method.reset[$0];
},
addFastProperty:function($0,
$1){var $2=$0.name;
var $3=qx.lang.String.toFirstUp($2);
var $4="_value"+$3;
var $5="get"+$3;
var $6="set"+$3;
var $7="_compute"+$3;
$1[$4]=typeof $0.defaultValue!=="undefined"?$0.defaultValue:null;
if($0.noCompute){$1[$5]=function(){return this[$4];
};
}else{$1[$5]=function(){return this[$4]==null?this[$4]=this[$7]():this[$4];
};
}$1[$5].self=$1.constructor;
if($0.setOnlyOnce){$1[$6]=function($8){this[$4]=$8;
this[$6]=null;
return $8;
};
}else{$1[$6]=function($8){return this[$4]=$8;
};
}$1[$6].self=$1.constructor;
if(!$0.noCompute){$1[$7]=function(){return null;
};
$1[$7].self=$1.constructor;
}},
addCachedProperty:function($0,
$1){var $2=$0.name;
var $3=qx.lang.String.toFirstUp($2);
var $4="_cached"+$3;
var $5="_compute"+$3;
var $6="_change"+$3;
if(typeof $0.defaultValue!=="undefined"){$1[$4]=$0.defaultValue;
}$1["get"+$3]=function(){if(this[$4]==null){this[$4]=this[$5]();
}return this[$4];
};
$1["_invalidate"+$3]=function(){if(this[$4]!=null){this[$4]=null;
if($0.addToQueueRuntime){this.addToQueueRuntime($0.name);
}}};
$1["_recompute"+$3]=function(){var $7=this[$4];
var $8=this[$5]();
if($8!=$7){this[$4]=$8;
this[$6]($8,
$7);
return true;
}return false;
};
$1[$6]=function($8,
$7){};
$1[$5]=function(){return null;
};
$1["get"+$3].self=$1.constructor;
$1["_invalidate"+$3].self=$1.constructor;
$1["_recompute"+$3].self=$1.constructor;
},
addProperty:function($0,
$1){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Legacy properties are deprecated");
if(typeof $0!=="object"){throw new Error("AddProperty: Param should be an object!");
}
if(typeof $0.name!=="string"){throw new Error("AddProperty: Malformed input parameters: name needed!");
}if($0.dispose===undefined&&($0.type=="function"||$0.type=="object")){$0.dispose=true;
}$0.method=qx.lang.String.toFirstUp($0.name);
$0.implMethod=$0.impl?qx.lang.String.toFirstUp($0.impl):$0.method;
if($0.defaultValue==undefined){$0.defaultValue=null;
}$0.allowNull=$0.allowNull!==false;
$0.allowMultipleArguments=$0.allowMultipleArguments===true;
if(typeof $0.type==="string"){$0.hasType=true;
}else if(typeof $0.type!=="undefined"){throw new Error("AddProperty: Invalid type definition for property "+$0.name+": "+$0.type);
}else{$0.hasType=false;
}
if(typeof $0.instance==="string"){$0.hasInstance=true;
}else if(typeof $0.instance!=="undefined"){throw new Error("AddProperty: Invalid instance definition for property "+$0.name+": "+$0.instance);
}else{$0.hasInstance=false;
}
if(typeof $0.classname==="string"){$0.hasClassName=true;
}else if(typeof $0.classname!=="undefined"){throw new Error("AddProperty: Invalid classname definition for property "+$0.name+": "+$0.classname);
}else{$0.hasClassName=false;
}$0.hasConvert=$0.convert!=null;
$0.hasPossibleValues=$0.possibleValues!=null;
$0.addToQueue=$0.addToQueue||false;
$0.addToQueueRuntime=$0.addToQueueRuntime||false;
$0.up=$0.name.toUpperCase();
var $2=qx.core.Property.$$store.user[$0.name]="__user$"+$0.name;
var $3="change"+$0.method;
var $4="_modify"+$0.implMethod;
var $5="_check"+$0.implMethod;
var $6=qx.core.Property.$$method;
if(!$6.set[$0.name]){$6.set[$0.name]="set"+$0.method;
$6.get[$0.name]="get"+$0.method;
$6.reset[$0.name]="reset"+$0.method;
}$1[$2]=$0.defaultValue;
$1["get"+$0.method]=function(){return this[$2];
};
$1["force"+$0.method]=function($7){return this[$2]=$7;
};
$1["reset"+$0.method]=function(){return this["set"+$0.method]($0.defaultValue);
};
if($0.type==="boolean"){$1["toggle"+$0.method]=function($7){return this["set"+$0.method](!this[$2]);
};
}
if($0.allowMultipleArguments||$0.hasConvert||$0.hasInstance||$0.hasClassName||$0.hasPossibleValues||$0.hasUnitDetection||$0.addToQueue||$0.addToQueueRuntime||$0.addToStateQueue){$1["set"+$0.method]=function($7){if($0.allowMultipleArguments&&arguments.length>1){$7=qx.lang.Array.fromArguments(arguments);
}if($0.hasConvert){try{$7=$0.convert.call(this,
$7,
$0);
}catch(ex){throw new Error("Attention! Could not convert new value for "+$0.name+": "+$7+": "+ex);
}}var $8=this[$2];
if($7===$8){return $7;
}
if(!($0.allowNull&&$7==null)){if($0.hasType&&typeof $7!==$0.type){throw new Error("Attention! The value \""+$7+"\" is an invalid value for the property \""+$0.name+"\" which must be typeof \""+$0.type+"\" but is typeof \""+typeof $7+"\"!");
}
if(qx.Class.getByName($0.instance)){if($0.hasInstance&&!($7 instanceof qx.Class.getByName($0.instance))){throw new Error("Attention! The value \""+$7+"\" is an invalid value for the property \""+$0.name+"\" which must be an instance of \""+$0.instance+"\"!");
}}else{if($0.hasInstance&&!($7 instanceof qx.OO.classes[$0.instance])){throw new Error("Attention! The value \""+$7+"\" is an invalid value for the property \""+$0.name+"\" which must be an instance of \""+$0.instance+"\"!");
}}
if($0.hasClassName&&$7.classname!=$0.classname){throw new Error("Attention! The value \""+$7+"\" is an invalid value for the property \""+$0.name+"\" which must be an object with the classname \""+$0.classname+"\"!");
}
if($0.hasPossibleValues&&$7!=null&&!qx.lang.Array.contains($0.possibleValues,
$7)){throw new Error("Failed to save value for "+$0.name+". '"+$7+"' is not a possible value!");
}}if(this[$5]){try{$7=this[$5]($7,
$0);
if($7===$8){return $7;
}}catch(ex){return this.error("Failed to check property "+$0.name,
ex);
}}this[$2]=$7;
if(this[$4]){try{this[$4]($7,
$8,
$0);
}catch(ex){return this.error("Modification of property \""+$0.name+"\" failed with exception",
ex);
}}if($0.addToQueue){this.addToQueue($0.name);
}
if($0.addToQueueRuntime){this.addToQueueRuntime($0.name);
}if($0.addToStateQueue){this.addToStateQueue();
}if(this.hasEventListeners&&this.hasEventListeners($3)){try{this.createDispatchDataEvent($3,
$7);
}catch(ex){throw new Error("Property "+$0.name+" modified: Failed to dispatch change event: "+ex);
}}return $7;
};
}else{$1["set"+$0.method]=function($7){var $8=this[$2];
if($7===$8){return $7;
}
if(!($0.allowNull&&$7==null)){if($0.hasType&&typeof $7!==$0.type){throw new Error("Attention! The value \""+$7+"\" is an invalid value for the property \""+$0.name+"\" which must be typeof \""+$0.type+"\" but is typeof \""+typeof $7+"\"!");
}}if(this[$5]){try{$7=this[$5]($7,
$0);
if($7===$8){return $7;
}}catch(ex){return this.error("Failed to check property "+$0.name,
ex);
}}this[$2]=$7;
if(this[$4]){try{this[$4]($7,
$8,
$0);
}catch(ex){var $9=new String($7).substring(0,
50);
this.error("Setting property \""+$0.name+"\" to \""+$9+"\" failed with exception",
ex);
}}if(this.hasEventListeners&&this.hasEventListeners($3)){var $a=new qx.event.type.DataEvent($3,
$7,
$8,
false);
$a.setTarget(this);
try{this.dispatchEvent($a,
true);
}catch(ex){throw new Error("Property "+$0.name+" modified: Failed to dispatch change event: "+ex);
}}return $7;
};
}$1["set"+$0.method].self=$1.constructor;
if(typeof $0.getAlias==="string"){$1[$0.getAlias]=$1["get"+$0.method];
}if(typeof $0.setAlias==="string"){$1[$0.setAlias]=$1["set"+$0.method];
}}}});




/* ID: qx.core.Property */
qx.Class.define("qx.core.Property",
{statics:{__checks:{"Boolean":'typeof value === "boolean"',
"String":'typeof value === "string"',
"NonEmptyString":'typeof value === "string" && value.length > 0',
"Number":'typeof value === "number" && isFinite(value)',
"Integer":'typeof value === "number" && isFinite(value) && value%1 === 0',
"Float":'typeof value === "number" && isFinite(value)',
"Double":'typeof value === "number" && isFinite(value)',
"Error":'value instanceof Error',
"RegExp":'value instanceof RegExp',
"Object":'value !== null && typeof value === "object"',
"Array":'value instanceof Array',
"Map":'value !== null && typeof value === "object" && !(value instanceof Array) && !(value instanceof qx.core.Object)',
"Function":'value instanceof Function',
"Date":'value instanceof Date',
"Node":'value !== null && value.nodeType !== undefined',
"Element":'value !== null && value.nodeType === 1 && value.attributes',
"Document":'value !== null && value.nodeType === 9 && value.documentElement',
"Window":'value !== null && window.document',
"Event":'value !== null && value.type !== undefined',
"Class":'value !== null && value.$$type === "Class"',
"Mixin":'value !== null && value.$$type === "Mixin"',
"Interface":'value !== null && value.$$type === "Interface"',
"Theme":'value !== null && value.$$type === "Theme"',
"Color":'typeof value === "string" && qx.util.ColorUtil.isValid(value)',
"Border":'value !== null && qx.theme.manager.Border.getInstance().isDynamic(value)',
"Font":'value !== null && qx.theme.manager.Font.getInstance().isDynamic(value)',
"Label":'value !== null && (qx.locale.Manager.getInstance().isDynamic(value) || typeof value === "string")'},
__dispose:{"Object":true,
"Array":true,
"Map":true,
"Function":true,
"Date":true,
"Node":true,
"Element":true,
"Document":true,
"Window":true,
"Event":true,
"Class":true,
"Mixin":true,
"Interface":true,
"Theme":true,
"Border":true,
"Font":true},
$$inherit:"inherit",
$$idcounter:0,
$$store:{user:{},
theme:{},
inherit:{},
init:{},
useinit:{}},
$$method:{get:{},
set:{},
reset:{},
init:{},
refresh:{},
style:{},
unstyle:{}},
$$allowedKeys:{name:"string",
dispose:"boolean",
inheritable:"boolean",
nullable:"boolean",
themeable:"boolean",
refine:"boolean",
init:null,
apply:"string",
event:"string",
check:null,
transform:"string"},
$$allowedGroupKeys:{name:"string",
group:"object",
mode:"string",
themeable:"boolean"},
$$inheritable:{},
refresh:function($0){var $1=$0.getParent();
if($1){var $2=$0.constructor;
var $3=this.$$store.inherit;
var $4=this.$$method.refresh;
var $5;
{};
while($2){$5=$2.$$properties;
if($5){for(var $6 in this.$$inheritable){if($5[$6]&&$0[$4[$6]]){{};
$0[$4[$6]]($1[$3[$6]]);
}}}$2=$2.superclass;
}}},
attach:function($0){var $1=$0.$$properties;
if($1){for(var $2 in $1){this.attachMethods($0,
$2,
$1[$2]);
}}$0.$$propertiesAttached=true;
},
attachMethods:function($0,
$1,
$2){if($2._legacy||$2._fast||$2._cached){return;
}var $3,
$4;
if($1.charAt(0)==="_"){if($1.charAt(1)==="_"){$3="__";
$4=qx.lang.String.toFirstUp($1.substring(2));
}else{$3="_";
$4=qx.lang.String.toFirstUp($1.substring(1));
}}else{$3="";
$4=qx.lang.String.toFirstUp($1);
}$2.group?this.__attachGroupMethods($0,
$2,
$3,
$4):this.__attachPropertyMethods($0,
$2,
$3,
$4);
},
__attachGroupMethods:function($0,
$1,
$2,
$3){var $4=$0.prototype;
var $5=$1.name;
var $6=$1.themeable===true;
{};
var $7=[];
var $8=[];
if($6){var $9=[];
var $a=[];
}var $b="var a=arguments[0] instanceof Array?arguments[0]:arguments;";
$7.push($b);
if($6){$9.push($b);
}
if($1.mode=="shorthand"){var $c="a=qx.lang.Array.fromShortHand(qx.lang.Array.fromArguments(a));";
$7.push($c);
if($6){$9.push($c);
}}
for(var $d=0,
$e=$1.group,
$f=$e.length;$d<$f;$d++){{};
$7.push("this.",
this.$$method.set[$e[$d]],
"(a[",
$d,
"]);");
$8.push("this.",
this.$$method.reset[$e[$d]],
"();");
if($6){{};
$9.push("this.",
this.$$method.style[$e[$d]],
"(a[",
$d,
"]);");
$a.push("this.",
this.$$method.unstyle[$e[$d]],
"();");
}}this.$$method.set[$5]=$2+"set"+$3;
$4[this.$$method.set[$5]]=new Function($7.join(""));
this.$$method.reset[$5]=$2+"reset"+$3;
$4[this.$$method.reset[$5]]=new Function($8.join(""));
if($6){this.$$method.style[$5]=$2+"style"+$3;
$4[this.$$method.style[$5]]=new Function($9.join(""));
this.$$method.unstyle[$5]=$2+"unstyle"+$3;
$4[this.$$method.unstyle[$5]]=new Function($a.join(""));
}},
__attachPropertyMethods:function($0,
$1,
$2,
$3){var $4=$0.prototype;
var $5=$1.name;
{};
if($1.dispose===undefined&&typeof $1.check==="string"){$1.dispose=this.__dispose[$1.check]||qx.Class.isDefined($1.check)||qx.Interface.isDefined($1.check);
}var $6=this.$$method;
var $7=this.$$store;
$7.user[$5]="__user$"+$5;
$7.theme[$5]="__theme$"+$5;
$7.init[$5]="__init$"+$5;
$7.inherit[$5]="__inherit$"+$5;
$7.useinit[$5]="__useinit$"+$5;
$6.get[$5]=$2+"get"+$3;
$4[$6.get[$5]]=function(){return qx.core.Property.executeOptimizedGetter(this,
$0,
$5,
"get");
};
$6.set[$5]=$2+"set"+$3;
$4[$6.set[$5]]=function($8){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"set",
arguments);
};
$6.reset[$5]=$2+"reset"+$3;
$4[$6.reset[$5]]=function(){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"reset");
};
if($1.inheritable||$1.apply||$1.event){$6.init[$5]=$2+"init"+$3;
$4[$6.init[$5]]=function($8){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"init",
arguments);
};
}
if($1.inheritable){$6.refresh[$5]=$2+"refresh"+$3;
$4[$6.refresh[$5]]=function($8){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"refresh",
arguments);
};
}
if($1.themeable){$6.style[$5]=$2+"style"+$3;
$4[$6.style[$5]]=function($8){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"style",
arguments);
};
$6.unstyle[$5]=$2+"unstyle"+$3;
$4[$6.unstyle[$5]]=function(){return qx.core.Property.executeOptimizedSetter(this,
$0,
$5,
"unstyle");
};
}
if($1.check==="Boolean"){$4[$2+"toggle"+$3]=new Function("return this."+$6.set[$5]+"(!this."+$6.get[$5]+"())");
$4[$2+"is"+$3]=new Function("return this."+$6.get[$5]+"()");
}},
__errors:{0:'Could not change or apply init value after constructing phase!',
1:'Requires exactly one argument!',
2:'Undefined value is not allowed!',
3:'Does not allow any arguments!',
4:'Null value is not allowed!',
5:'Is invalid!'},
error:function($0,
$1,
$2,
$3,
$4){var $5=$0.constructor.classname;
var $6="Error in property "+$2+" of class "+$5+" in method "+this.$$method[$3][$2]+" with incoming value '"+$4+"': ";
$0.printStackTrace();
$0.error($6+(this.__errors[$1]||"Unknown reason: "+$1));
throw new Error($6+(this.__errors[$1]||"Unknown reason: "+$1));
},
__unwrapFunctionFromCode:function($0,
$1,
$2,
$3,
$4,
$5){var $6=this.$$method[$3][$2];
{$1[$6]=new Function("value",
$4.join(""));
};
{};
if($5===undefined){return $0[$6]();
}else{return $0[$6]($5[0]);
}},
executeOptimizedGetter:function($0,
$1,
$2,
$3){var $4=$1.$$properties[$2];
var $5=$1.prototype;
var $6=[];
if($4.inheritable){$6.push('if(this.',
this.$$store.inherit[$2],
'!==undefined)');
$6.push('return this.',
this.$$store.inherit[$2],
';');
$6.push('else ');
}$6.push('if(this.',
this.$$store.user[$2],
'!==undefined)');
$6.push('return this.',
this.$$store.user[$2],
';');
if($4.themeable){$6.push('else if(this.',
this.$$store.theme[$2],
'!==undefined)');
$6.push('return this.',
this.$$store.theme[$2],
';');
}
if($4.deferredInit&&$4.init===undefined){$6.push('else if(this.',
this.$$store.init[$2],
'!==undefined)');
$6.push('return this.',
this.$$store.init[$2],
';');
}$6.push('else ');
if($4.init!==undefined){$6.push('return this.',
this.$$store.init[$2],
';');
}else if($4.inheritable||$4.nullable){$6.push('return null;');
}else{$6.push('throw new Error("Property ',
$2,
' of an instance of ',
$1.classname,
' is not (yet) ready!");');
}return this.__unwrapFunctionFromCode($0,
$5,
$2,
$3,
$6);
},
executeOptimizedSetter:function($0,
$1,
$2,
$3,
$4){var $5=$1.$$properties[$2];
var $6=$1.prototype;
var $7=[];
var $8=$3==="set"||$3==="style"||($3==="init"&&$5.init===undefined);
var $9=$3==="reset"||$3==="unstyle";
var $a=$5.apply||$5.event||$5.inheritable;
if($3==="style"||$3==="unstyle"){var $b=this.$$store.theme[$2];
}else if($3==="init"){var $b=this.$$store.init[$2];
}else{var $b=this.$$store.user[$2];
}{if(!$5.nullable||$5.check||$5.inheritable){$7.push('var prop=qx.core.Property;');
}if($3==="set"){$7.push('if(value===undefined)prop.error(this,2,"'+$2+'","'+$3+'",value);');
}};
if($8){if($5.transform){$7.push('value=this.',
$5.transform,
'(value);');
}}if($a){if($8){$7.push('if(this.',
$b,
'===value)return value;');
}else if($9){$7.push('if(this.',
$b,
'===undefined)return;');
}}if($5.inheritable){$7.push('var inherit=prop.$$inherit;');
}if($8&&(false||$3==="set")){if(!$5.nullable){$7.push('if(value===null)prop.error(this,4,"'+$2+'","'+$3+'",value);');
}if($5.check!==undefined){if($5.nullable){$7.push('if(value!==null)');
}if($5.inheritable){$7.push('if(value!==inherit)');
}$7.push('if(');
if(this.__checks[$5.check]!==undefined){$7.push('!(',
this.__checks[$5.check],
')');
}else if(qx.Class.isDefined($5.check)){$7.push('!(value instanceof ',
$5.check,
')');
}else if(qx.Interface.isDefined($5.check)){$7.push('!(value && qx.Class.hasInterface(value.constructor, ',
$5.check,
'))');
}else if(typeof $5.check==="function"){$7.push('!',
$1.classname,
'.$$properties.',
$2);
$7.push('.check.call(this, value)');
}else if(typeof $5.check==="string"){$7.push('!(',
$5.check,
')');
}else if($5.check instanceof Array){$5.checkMap=qx.lang.Object.fromArray($5.check);
$7.push($1.classname,
'.$$properties.',
$2);
$7.push('.checkMap[value]===undefined');
}else{throw new Error("Could not add check to property "+$2+" of class "+$1.classname);
}$7.push(')prop.error(this,5,"'+$2+'","'+$3+'",value);');
}}
if(!$a){if($3==="set"){$7.push('this.',
this.$$store.user[$2],
'=value;');
}else if($3==="reset"){$7.push('if(this.',
this.$$store.user[$2],
'!==undefined)');
$7.push('delete this.',
this.$$store.user[$2],
';');
}else if($3==="style"){$7.push('this.',
this.$$store.theme[$2],
'=value;');
}else if($3==="unstyle"){$7.push('if(this.',
this.$$store.theme[$2],
'!==undefined)');
$7.push('delete this.',
this.$$store.theme[$2],
';');
}else if($3==="init"&&$8){$7.push('this.',
this.$$store.init[$2],
'=value;');
}}else{if($5.inheritable){$7.push('var computed, old=this.',
this.$$store.inherit[$2],
';');
}else{$7.push('var computed, old;');
}$7.push('if(this.',
this.$$store.user[$2],
'!==undefined){');
if($3==="set"){if(!$5.inheritable){$7.push('old=this.',
this.$$store.user[$2],
';');
}$7.push('computed=this.',
this.$$store.user[$2],
'=value;');
}else if($3==="reset"){if(!$5.inheritable){$7.push('old=this.',
this.$$store.user[$2],
';');
}$7.push('delete this.',
this.$$store.user[$2],
';');
$7.push('if(this.',
this.$$store.theme[$2],
'!==undefined)');
$7.push('computed=this.',
this.$$store.theme[$2],
';');
$7.push('else if(this.',
this.$$store.init[$2],
'!==undefined){');
$7.push('computed=this.',
this.$$store.init[$2],
';');
$7.push('this.',
this.$$store.useinit[$2],
'=true;');
$7.push('}');
}else{if($5.inheritable){$7.push('computed=this.',
this.$$store.user[$2],
';');
}else{$7.push('old=computed=this.',
this.$$store.user[$2],
';');
}if($3==="style"){$7.push('this.',
this.$$store.theme[$2],
'=value;');
}else if($3==="unstyle"){$7.push('delete this.',
this.$$store.theme[$2],
';');
}else if($3==="init"&&$8){$7.push('this.',
this.$$store.init[$2],
'=value;');
}}$7.push('}');
if($5.themeable){$7.push('else if(this.',
this.$$store.theme[$2],
'!==undefined){');
if(!$5.inheritable){$7.push('old=this.',
this.$$store.theme[$2],
';');
}
if($3==="set"){$7.push('computed=this.',
this.$$store.user[$2],
'=value;');
}else if($3==="style"){$7.push('computed=this.',
this.$$store.theme[$2],
'=value;');
}else if($3==="unstyle"){$7.push('delete this.',
this.$$store.theme[$2],
';');
$7.push('if(this.',
this.$$store.init[$2],
'!==undefined){');
$7.push('computed=this.',
this.$$store.init[$2],
';');
$7.push('this.',
this.$$store.useinit[$2],
'=true;');
$7.push('}');
}else if($3==="init"){if($8){$7.push('this.',
this.$$store.init[$2],
'=value;');
}$7.push('computed=this.',
this.$$store.theme[$2],
';');
}else if($3==="refresh"){$7.push('computed=this.',
this.$$store.theme[$2],
';');
}$7.push('}');
}$7.push('else if(this.',
this.$$store.useinit[$2],
'){');
if(!$5.inheritable){$7.push('old=this.',
this.$$store.init[$2],
';');
}
if($3==="init"){if($8){$7.push('computed=this.',
this.$$store.init[$2],
'=value;');
}else{$7.push('computed=this.',
this.$$store.init[$2],
';');
}}else if($3==="set"||$3==="style"||$3==="refresh"){$7.push('delete this.',
this.$$store.useinit[$2],
';');
if($3==="set"){$7.push('computed=this.',
this.$$store.user[$2],
'=value;');
}else if($3==="style"){$7.push('computed=this.',
this.$$store.theme[$2],
'=value;');
}else if($3==="refresh"){$7.push('computed=this.',
this.$$store.init[$2],
';');
}}$7.push('}');
if($3==="set"||$3==="style"||$3==="init"){$7.push('else{');
if($3==="set"){$7.push('computed=this.',
this.$$store.user[$2],
'=value;');
}else if($3==="style"){$7.push('computed=this.',
this.$$store.theme[$2],
'=value;');
}else if($3==="init"){if($8){$7.push('computed=this.',
this.$$store.init[$2],
'=value;');
}else{$7.push('computed=this.',
this.$$store.init[$2],
';');
}$7.push('this.',
this.$$store.useinit[$2],
'=true;');
}$7.push('}');
}}
if($5.inheritable){$7.push('if(computed===undefined||computed===inherit){');
if($3==="refresh"){$7.push('computed=value;');
}else{$7.push('var pa=this.getParent();if(pa)computed=pa.',
this.$$store.inherit[$2],
';');
}$7.push('if((computed===undefined||computed===inherit)&&');
$7.push('this.',
this.$$store.init[$2],
'!==undefined&&');
$7.push('this.',
this.$$store.init[$2],
'!==inherit){');
$7.push('computed=this.',
this.$$store.init[$2],
';');
$7.push('this.',
this.$$store.useinit[$2],
'=true;');
$7.push('}else{');
$7.push('delete this.',
this.$$store.useinit[$2],
';}');
$7.push('}');
$7.push('if(old===computed)return value;');
$7.push('if(computed===inherit){');
$7.push('computed=undefined;delete this.',
this.$$store.inherit[$2],
';');
$7.push('}');
$7.push('else if(computed===undefined)');
$7.push('delete this.',
this.$$store.inherit[$2],
';');
$7.push('else this.',
this.$$store.inherit[$2],
'=computed;');
$7.push('var backup=computed;');
$7.push('if(computed===undefined)computed=null;');
$7.push('if(old===undefined)old=null;');
}else if($a){if($3!=="set"&&$3!=="style"){$7.push('if(computed===undefined)computed=null;');
}$7.push('if(old===computed)return value;');
$7.push('if(old===undefined)old=null;');
}if($a){if($5.apply){$7.push('this.',
$5.apply,
'(computed, old);');
}if($5.event){$7.push('this.createDispatchChangeEvent("',
$5.event,
'", computed, old);');
}if($5.inheritable&&$6.getChildren){$7.push('var a=this.getChildren();if(a)for(var i=0,l=a.length;i<l;i++){');
$7.push('if(a[i].',
this.$$method.refresh[$2],
')a[i].',
this.$$method.refresh[$2],
'(backup);');
$7.push('}');
}}if($8){$7.push('return value;');
}return this.__unwrapFunctionFromCode($0,
$6,
$2,
$3,
$7,
$4);
}},
settings:{"qx.propertyDebugLevel":0}});




/* ID: qx.lang.String */
qx.Class.define("qx.lang.String",
{statics:{toCamelCase:function($0){return $0.replace(/\-([a-z])/g,
function($1,
$2){return $2.toUpperCase();
});
},
trimLeft:function($0){return $0.replace(/^\s+/,
"");
},
trimRight:function($0){return $0.replace(/\s+$/,
"");
},
trim:function($0){return $0.replace(/^\s+|\s+$/g,
"");
},
startsWith:function($0,
$1){return !$0.indexOf($1);
},
startsWithAlternate:function($0,
$1){return $0.substring(0,
$1.length)===$1;
},
endsWith:function($0,
$1){return $0.lastIndexOf($1)===$0.length-$1.length;
},
endsWithAlternate:function($0,
$1){return $0.substring($0.length-$1.length,
$0.length)===$1;
},
pad:function($0,
$1,
$2,
$3){if(typeof $2==="undefined"){$2="0";
}var $4="";
for(var $5=$0.length;$5<$1;$5++){$4+=$2;
}
if($3==true){return $0+$4;
}else{return $4+$0;
}},
toFirstUp:function($0){return $0.charAt(0).toUpperCase()+$0.substr(1);
},
toFirstLower:function($0){return $0.charAt(0).toLowerCase()+$0.substr(1);
},
addListItem:function($0,
$1,
$2){if($0==$1||$0==""){return $1;
}
if($2==null){$2=",";
}var $3=$0.split($2);
if($3.indexOf($1)==-1){$3.push($1);
return $3.join($2);
}else{return $0;
}},
removeListItem:function($0,
$1,
$2){if($0==$1||$0==""){return "";
}else{if($2==null){$2=",";
}var $3=$0.split($2);
var $4=$3.indexOf($1);
if($4===-1){return $0;
}
do{$3.splice($4,
1);
}while(($4=$3.indexOf($1))!=-1);
return $3.join($2);
}},
contains:function($0,
$1){return $0.indexOf($1)!=-1;
},
format:function($0,
$1){var $2=$0;
for(var $3=0;$3<$1.length;$3++){$2=$2.replace(new RegExp("%"+($3+1),
"g"),
$1[$3]);
}return $2;
},
escapeRegexpChars:function($0){return $0.replace(/([\\\.\(\)\[\]\{\}\^\$\?\+\*])/g,
"\\$1");
},
toArray:function($0){return $0.split(/\B|\b/g);
}}});




/* ID: qx.core.Object */
qx.Class.define("qx.core.Object",
{extend:Object,
include:[qx.locale.MTranslation,
qx.log.MLogging,
qx.core.MUserData],
construct:function(){this._hashCode=qx.core.Object.__availableHashCode++;
if(this._autoDispose){this.__dbKey=qx.core.Object.__db.length;
qx.core.Object.__db.push(this);
}},
statics:{__availableHashCode:0,
__db:[],
__disposeAll:false,
$$type:"Object",
toHashCode:function($0){if($0._hashCode!=null){return $0._hashCode;
}return $0._hashCode=this.__availableHashCode++;
},
getDb:function(){return this.__db;
},
dispose:function($0){if(this.__disposed){return;
}this.__disposed=true;
this.__unload=$0||false;
var $1;
var $2,
$3=this.__db;
for(var $4=$3.length-1;$4>=0;$4--){$2=$3[$4];
if($2&&$2.__disposed===false){try{$2.dispose();
}catch(ex){{};
}}}var $5,
$4,
$6,
$7,
$8,
$9;
},
inGlobalDispose:function(){return this.__disposed||false;
},
isPageUnload:function(){return this.__unload||false;
}},
members:{_autoDispose:true,
toHashCode:function(){return this._hashCode;
},
toString:function(){if(this.classname){return "[object "+this.classname+"]";
}return "[object Object]";
},
base:function($0,
$1){if(arguments.length===1){return $0.callee.base.call(this);
}else{return $0.callee.base.apply(this,
Array.prototype.slice.call(arguments,
1));
}},
self:function($0){return $0.callee.self;
},
getDbKey:function(){return this.__dbKey;
},
set:function($0,
$1){var $2=qx.core.Property.$$method.set;
if(typeof $0==="string"){{};
return this[$2[$0]]($1);
}else{for(var $3 in $0){{};
this[$2[$3]]($0[$3]);
}return this;
}},
get:function($0){var $1=qx.core.Property.$$method.get;
{};
return this[$1[$0]]();
},
reset:function($0){var $1=qx.core.Property.$$method.reset;
{};
this[$1[$0]]();
},
__disposed:false,
getDisposed:function(){return this.__disposed;
},
isDisposed:function(){return this.__disposed;
},
dispose:function(){if(this.__disposed){return;
}this.__disposed=true;
{};
var $0=this.constructor;
var $1;
while($0.superclass){if($0.$$destructor){$0.$$destructor.call(this);
}if($0.$$includes){$1=$0.$$flatIncludes;
for(var $2=0,
$3=$1.length;$2<$3;$2++){if($1[$2].$$destructor){$1[$2].$$destructor.call(this);
}}}$0=$0.superclass;
}var $4,
$5;
},
_disposeFields:function($0){var $1;
for(var $2=0,
$3=arguments.length;$2<$3;$2++){var $1=arguments[$2];
if(this[$1]==null){continue;
}
if(!this.hasOwnProperty($1)){{};
continue;
}this[$1]=null;
}},
_disposeObjects:function($0){var $1;
for(var $2=0,
$3=arguments.length;$2<$3;$2++){var $1=arguments[$2];
if(this[$1]==null){continue;
}
if(!this.hasOwnProperty($1)){{};
continue;
}
if(!this[$1].dispose){throw new Error(this.classname+" has no own object "+$1);
}this[$1].dispose();
this[$1]=null;
}},
_disposeObjectDeep:function($0,
$1){var $0;
if(this[$0]==null){return;
}
if(!this.hasOwnProperty($0)){{};
return;
}{};
this.__disposeObjectsDeepRecurser(this[$0],
$1||0);
this[$0]=null;
},
__disposeObjectsDeepRecurser:function($0,
$1){if($0 instanceof qx.core.Object){{};
$0.dispose();
}else if($0 instanceof Array){for(var $2=0,
$3=$0.length;$2<$3;$2++){var $4=$0[$2];
if($4==null){continue;
}
if(typeof $4=="object"){if($1>0){{};
this.__disposeObjectsDeepRecurser($4,
$1-1);
}{};
$0[$2]=null;
}else if(typeof $4=="function"){{};
$0[$2]=null;
}}}else if($0 instanceof Object){for(var $5 in $0){if($0[$5]==null||!$0.hasOwnProperty($5)){continue;
}var $4=$0[$5];
if(typeof $4=="object"){if($1>0){{};
this.__disposeObjectsDeepRecurser($4,
$1-1);
}{};
$0[$5]=null;
}else if(typeof $4=="function"){{};
$0[$5]=null;
}}}}},
settings:{"qx.disposerDebugLevel":0},
destruct:function(){var $0=this.constructor;
var $1;
var $2=qx.core.Property.$$store;
var $3=$2.user;
var $4=$2.theme;
var $5=$2.inherit;
var $6=$2.useinit;
var $7=$2.init;
while($0){$1=$0.$$properties;
if($1){for(var $8 in $1){if($1[$8].dispose){this[$3[$8]]=this[$4[$8]]=this[$5[$8]]=this[$6[$8]]=this[$7[$8]]=undefined;
}}}$0=$0.superclass;
}if(this.__dbKey!=null){if(qx.core.Object.__disposeAll){qx.core.Object.__db[this.__dbKey]=null;
}else{delete qx.core.Object.__db[this.__dbKey];
}}}});




/* ID: qx.core.Log */
qx.Class.define("qx.core.Log",
{statics:{log:function($0){this._logFormatted(arguments,
"");
},
debug:function($0){this._logFormatted(arguments,
"debug");
},
info:function($0){this._logFormatted(arguments,
"info");
},
warn:function($0){this._logFormatted(arguments,
"warn");
},
error:function($0){this._logFormatted(arguments,
"error");
},
assert:function($0,
$1,
$2){if(!$0){var $3=[];
for(var $4=1;$4<arguments.length;++$4)$3.push(arguments[$4]);
this._logFormatted($3.length?$3:["Assertion Failure"],
"error");
throw $1?$1:"Assertion Failure";
}},
dir:function($0){var $1=[];
var $2=[];
for(var $3 in $0){try{$2.push([$3,
$0[$3]]);
}catch(exc){}}$2.sort(function($4,
$5){return $4[0]<$5[0]?-1:1;
});
$1.push('<table>');
for(var $6=0;$6<$2.length;++$6){var $3=$2[$6][0],
$7=$2[$6][1];
$1.push('<tr>',
'<td class="propertyNameCell"><span class="propertyName">',
this._escapeHTML($3),
'</span></td>',
'<td><span class="propertyValue">');
this._appendObject($7,
$1);
$1.push('</span></td></tr>');
}$1.push('</table>');
this._logRow($1,
"dir");
},
dirxml:function($0){var $1=[];
this._appendNode($0,
$1);
this._logRow($1,
"dirxml");
},
time:function($0){this._timeMap[$0]=(new Date()).getTime();
},
timeEnd:function($0){if($0 in this._timeMap){var $1=(new Date()).getTime()-this._timeMap[$0];
this._logFormatted([$0+":",
$1+"ms"]);
delete this._timeMap[$0];
}},
clear:function(){this._consoleLog.innerHTML="";
},
trace:function(){if(qx.dev&&qx.dev.StackTrace){var $0=qx.dev.StackTrace.getStackTrace();
this.debug("Current stack trace: ");
for(var $1=1,
$2=$0.length;$1<$2;$1++){this.debug("  - "+$0[$1]);
}}else{this.warn("Stacktraces are not support by your build!");
}},
_consoleLog:null,
_commandLine:null,
_messageQueue:[],
_timeMap:{},
_clPrefix:">>> ",
_consoleShortcuts:{log:"qx.core.Log.log",
info:"qx.core.Log.info",
debug:"qx.core.Log.debug",
warn:"qx.core.Log.warn",
error:"qx.core.Log.error",
assert:"qx.core.Log.assert",
dir:"qx.core.Log.dir",
dirxml:"qx.core.Log.dirxml",
time:"qx.core.Log.time",
timeEnd:"qx.core.Log.timeEnd",
clear:"qx.core.Log.clear"},
_focusCommandLine:function(){if(this._commandLine){this._commandLine.focus();
}},
_initializeWindow:function(){if(this._consoleWindow){return;
}if(qx.core.Setting){var $0=qx.core.Setting.get("qx.resourceUri")+"/static/log/log.html";
this._consoleWindow=window.open($0,
"win",
"width=500,height=250,dependent=yes,resizable=yes,status=no,location=no,menubar=no,toolbar=no,scrollbars=no");
}},
_onLogReady:function($0){var $1=$0.document;
this._consoleWindow=$0;
this._consoleDocument=$1;
this._consoleLog=$1.getElementById("log");
this._commandLine=$1.getElementById("commandLine");
this._onUnloadWrapped=qx.lang.Function.bind(this._onUnload,
this);
this._onResizeWrapped=qx.lang.Function.bind(this._onResize,
this);
this._onCommandLineKeyDownWrapped=qx.lang.Function.bind(this._onCommandLineKeyDown,
this);
this._addEvent(window,
"unload",
this._onUnloadWrapped);
this._addEvent($0,
"unload",
this._onUnloadWrapped);
this._addEvent($0,
"resize",
this._onResizeWrapped);
this._addEvent(this._commandLine,
"keydown",
this._onCommandLineKeyDownWrapped);
this._syncLayout();
this._flush();
},
_syncLayout:function(){this._consoleLog.style.height=(qx.bom.Viewport.getHeight(this._consoleWindow)-42)+"px";
},
_evalCommandLine:function(){var $0=this._commandLine.value;
this._commandLine.value="";
this._logRow([this._clPrefix,
$0],
"command");
var $1=/^([a-z]+)\(/;
var $2=$1.exec($0);
if($2!=null){if(this._consoleShortcuts[$2[1]]){$0=this._consoleShortcuts[$2[1]]+$0.substring($2[1].length);
}}var $3;
try{$3=eval($0);
}catch(ex){this.error(ex);
}
if($3!==undefined){this.log($3);
}},
_logRow:function($0,
$1){if(this._consoleLog){this._writeMessage($0,
$1);
}else{this._messageQueue.push([$0,
$1]);
this._initializeWindow();
}},
_flush:function(){var $0=this._messageQueue;
this._messageQueue=[];
for(var $1=0;$1<$0.length;++$1){this._writeMessage($0[$1][0],
$0[$1][1]);
}},
_writeMessage:function($0,
$1){var $2=this._consoleLog.scrollTop+this._consoleLog.offsetHeight>=this._consoleLog.scrollHeight;
this._writeRow($0,
$1);
if($2){this._consoleLog.scrollTop=this._consoleLog.scrollHeight-this._consoleLog.offsetHeight;
}},
_appendRow:function($0){this._consoleLog.appendChild($0);
},
_writeRow:function($0,
$1){var $2=this._consoleLog.ownerDocument.createElement("div");
$2.className="logRow"+($1?" logRow-"+$1:"");
$2.innerHTML=$0.join("");
this._appendRow($2);
},
_logFormatted:function($0,
$1){if(window.__firebug__&&window.console){return window.console[$1].apply(window.console,
$0);
}var $2=[];
var $3=$0[0];
var $4=0;
if(typeof ($3)!="string"){$3="";
$4=-1;
}var $5=this._parseFormat($3);
for(var $6=0;$6<$5.length;++$6){var $7=$5[$6];
if($7&&typeof ($7)=="object"){var $8=$0[++$4];
$7.appender($8,
$2);
}else this._appendText($7,
$2);
}
for(var $6=$4+1;$6<$0.length;++$6){this._appendText(" ",
$2);
var $8=$0[$6];
if(typeof ($8)=="string")this._appendText($8,
$2);
else this._appendObject($8,
$2);
}this._logRow($2,
$1);
},
_parseFormat:function($0){var $1=[];
var $2=/((^%|[^\\]%)(\d+)?(\.)([a-zA-Z]))|((^%|[^\\]%)([a-zA-Z]))/;
var $3={s:this._appendText,
d:this._appendInteger,
i:this._appendInteger,
f:this._appendFloat};
for(var $4=$2.exec($0);$4;$4=$2.exec($0)){var $5=$4[8]?$4[8]:$4[5];
var $6=$5 in $3?$3[$5]:this._appendObject;
var $7=$4[3]?parseInt($4[3]):($4[4]=="."?-1:0);
$1.push($0.substr(0,
$4[0][0]=="%"?$4.index:$4.index+1));
$1.push({appender:$6,
precision:$7});
$0=$0.substr($4.index+$4[0].length);
}$1.push($0);
return $1;
},
_escapeHTML:function($0){function $1($2){switch($2){case "<":return "&lt;";
case ">":return "&gt;";
case "&":return "&amp;";
case "'":return "&#39;";
case '"':return "&quot;";
}return "?";
}return String($0).replace(/[<>&"']/g,
$1);
},
_objectToString:function($0){try{return $0+"";
}catch(exc){return null;
}},
_appendText:function($0,
$1){$1.push(this._escapeHTML(this._objectToString($0)));
},
_appendNull:function($0,
$1){$1.push('<span class="objectBox-null">',
this._escapeHTML(this._objectToString($0)),
'</span>');
},
_appendString:function($0,
$1){$1.push('<span class="objectBox-string">&quot;',
this._escapeHTML(this._objectToString($0)),
'&quot;</span>');
},
_appendInteger:function($0,
$1){$1.push('<span class="objectBox-number">',
this._escapeHTML(this._objectToString($0)),
'</span>');
},
_appendFloat:function($0,
$1){$1.push('<span class="objectBox-number">',
this._escapeHTML(this._objectToString($0)),
'</span>');
},
_appendFunction:function($0,
$1){var $2=/function ?(.*?)\(/;
var $3=$2.exec(this._objectToString($0));
var $4=$3?$3[1]:"function";
$1.push('<span class="objectBox-function">',
this._escapeHTML($4),
'()</span>');
},
_appendObject:function($0,
$1){try{if($0==undefined)this._appendNull("undefined",
$1);
else if($0==null)this._appendNull("null",
$1);
else if(typeof $0=="string")this._appendString($0,
$1);
else if(typeof $0=="number")this._appendInteger($0,
$1);
else if($0.toString)this._appendText($0.toString(),
$1);
else if(typeof $0=="function")this._appendFunction($0,
$1);
else if($0.nodeType==1)this._appendSelector($0,
$1);
else if(typeof $0=="object")this._appendObjectFormatted($0,
$1);
else this._appendText($0,
$1);
}catch(exc){}},
_appendObjectFormatted:function($0,
$1){var $2=this._objectToString($0);
var $3=/\[object (.*?)\]/;
var $4=$3.exec($2);
$1.push('<span class="objectBox-object">',
$4?$4[1]:$2,
'</span>');
},
_appendSelector:function($0,
$1){$1.push('<span class="objectBox-selector">');
$1.push('<span class="selectorTag">',
this._escapeHTML($0.nodeName.toLowerCase()),
'</span>');
if($0.id)$1.push('<span class="selectorId">#',
this._escapeHTML($0.id),
'</span>');
if($0.className)$1.push('<span class="selectorClass">.',
this._escapeHTML($0.className),
'</span>');
$1.push('</span>');
},
_appendNode:function($0,
$1){if($0.nodeType==1){$1.push('<div class="objectBox-element">',
'&lt;<span class="nodeTag">',
$0.nodeName.toLowerCase(),
'</span>');
for(var $2=0;$2<$0.attributes.length;++$2){var $3=$0.attributes[$2];
if(!$3.specified)continue;
$1.push('&nbsp;<span class="nodeName">',
$3.nodeName.toLowerCase(),
'</span>=&quot;<span class="nodeValue">',
this._escapeHTML($3.nodeValue),
'</span>&quot;');
}
if($0.firstChild){$1.push('&gt;</div><div class="nodeChildren">');
for(var $4=$0.firstChild;$4;$4=$4.nextSibling)this._appendNode($4,
$1);
$1.push('</div><div class="objectBox-element">&lt;/<span class="nodeTag">',
$0.nodeName.toLowerCase(),
'&gt;</span></div>');
}else $1.push('/&gt;</div>');
}else if($0.nodeType==3){$1.push('<div class="nodeText">',
this._escapeHTML($0.nodeValue),
'</div>');
}},
_addEvent:function($0,
$1,
$2){if(document.all)$0.attachEvent("on"+$1,
$2);
else $0.addEventListener($1,
$2,
false);
},
_removeEvent:function($0,
$1,
$2){if(document.all)$0.detachEvent("on"+$1,
$2);
else $0.removeEventListener($1,
$2,
false);
},
_cancelEvent:function($0){if(document.all)$0.cancelBubble=true;
else $0.stopPropagation();
},
_onCommandLineKeyDown:function($0){if($0.keyCode==13)this._evalCommandLine();
else if($0.keyCode==27)this._commandLine.value="";
},
_onResize:function($0){this._syncLayout();
},
_onUnload:function($0){var $1=this._consoleWindow;
var $2=this._commandLine;
this._consoleWindow=null;
this._consoleDocument=null;
this._consoleLog=null;
this._commandLine=null;
this._removeEvent(window,
"unload",
this._onUnloadWrapped);
if($1){try{$1.close();
}catch(ex){}this._removeEvent($1,
"unload",
this._onUnloadWrapped);
this._removeEvent($1,
"resize",
this._onResizeWrapped);
}
if($2){this._removeEvent($2,
"keydown",
this._onCommandLineKeyDownWrapped);
}}}});




/* ID: qx.lang.Function */
qx.Class.define("qx.lang.Function",
{statics:{globalEval:function($0){if(window.execScript){window.execScript($0);
}else{eval.call(window,
$0);
}},
returnTrue:function(){return true;
},
returnFalse:function(){return false;
},
returnNull:function(){return null;
},
returnThis:function(){return this;
},
returnInstance:function(){if(!this._instance){this._instance=new this;
}return this._instance;
},
returnZero:function(){return 0;
},
returnNegativeIndex:function(){return -1;
},
bind:function($0,
$1,
$2){{};
if(arguments.length>2){var $3=Array.prototype.slice.call(arguments,
2);
var $4=function(){$0.context=$1;
var $5=$0.apply($1,
$3.concat(qx.lang.Array.fromArguments(arguments)));
$0.context=null;
return $5;
};
}else{var $4=function(){$0.context=$1;
var $5=$0.apply($1,
arguments);
$0.context=null;
return $5;
};
}$4.self=$0.self?$0.self.constructor:$1;
return $4;
},
bindEvent:function($0,
$1){{};
var $2=function($3){$0.context=$1;
var $4=$0.call($1,
$3||window.event);
$0.context=null;
return $4;
};
$2.self=$0.self?$0.self.constructor:$1;
return $2;
},
getCaller:function($0){return $0.caller?$0.caller.callee:$0.callee.caller;
}}});




/* ID: qx.bom.Viewport */
qx.Class.define("qx.bom.Viewport",
{statics:{getWidth:qx.core.Variant.select("qx.client",
{"opera":function($0){return ($0||window).document.body.clientWidth;
},
"webkit":function($0){return ($0||window).innerWidth;
},
"default":function($0){var $1=($0||window).document;
return $1.compatMode==="CSS1Compat"?$1.documentElement.clientWidth:$1.body.clientWidth;
}}),
getHeight:qx.core.Variant.select("qx.client",
{"opera":function($0){return ($0||window).document.body.clientHeight;
},
"webkit":function($0){return ($0||window).innerHeight;
},
"default":function($0){var $1=($0||window).document;
return $1.compatMode==="CSS1Compat"?$1.documentElement.clientHeight:$1.body.clientHeight;
}}),
getScrollLeft:qx.core.Variant.select("qx.client",
{"mshtml":function($0){var $1=($0||window).document;
return $1.documentElement.scrollLeft||$1.body.scrollLeft;
},
"default":function($0){return ($0||window).pageXOffset;
}}),
getScrollTop:qx.core.Variant.select("qx.client",
{"mshtml":function($0){var $1=($0||window).document;
return $1.documentElement.scrollTop||$1.body.scrollTop;
},
"default":function($0){return ($0||window).pageYOffset;
}})}});




/* ID: qx.Theme */
qx.Class.define("qx.Theme",
{statics:{define:function($0,
$1){if(!$1){var $1={};
}
if($1.include&&!($1.include instanceof Array)){$1.include=[$1.include];
}{};
var $2={$$type:"Theme",
name:$0,
title:$1.title,
toString:this.genericToString};
if($1.extend){$2.supertheme=$1.extend;
}$2.basename=qx.Class.createNamespace($0,
$2);
this.__convert($2,
$1);
this.__registry[$0]=$2;
if($1.include){for(var $3=0,
$4=$1.include,
$5=$4.length;$3<$5;$3++){this.include($2,
$4[$3]);
}}},
getAll:function(){return this.__registry;
},
getByName:function($0){return this.__registry[$0];
},
isDefined:function($0){return this.getByName($0)!==undefined;
},
getTotalNumber:function(){return qx.lang.Object.getLength(this.__registry);
},
genericToString:function(){return "[Theme "+this.name+"]";
},
__extractType:function($0){for(var $1=0,
$2=this.__inheritableKeys,
$3=$2.length;$1<$3;$1++){if($0[$2[$1]]){return $2[$1];
}}},
__convert:function($0,
$1){var $2=this.__extractType($1);
if($1.extend&&!$2){$2=$1.extend.type;
}$0.type=$2||"other";
if(!$2){return;
}var $3=function(){};
if($1.extend){$3.prototype=new $1.extend.$$clazz;
}var $4=$3.prototype;
var $5=$1[$2];
for(var $6 in $5){$4[$6]=$5[$6];
if($4[$6].base){{};
$4[$6].base=$1.extend;
}}$0.$$clazz=$3;
$0[$2]=new $3;
},
__registry:{},
__inheritableKeys:["colors",
"borders",
"fonts",
"icons",
"widgets",
"appearances",
"meta"],
__allowedKeys:null,
__metaKeys:null,
__validateConfig:function(){},
patch:function($0,
$1){var $2=this.__extractType($1);
if($2!==this.__extractType($0)){throw new Error("The mixins '"+$0.name+"' are not compatible '"+$1.name+"'!");
}var $3=$1[$2];
var $4=$0[$2];
for(var $5 in $3){$4[$5]=$3[$5];
}},
include:function($0,
$1){var $2=$1.type;
if($2!==$0.type){throw new Error("The mixins '"+$0.name+"' are not compatible '"+$1.name+"'!");
}var $3=$1[$2];
var $4=$0[$2];
for(var $5 in $3){if($4[$5]!==undefined){throw new Error("It is not allowed to overwrite the key '"+$5+"' of theme '"+$0.name+"' by mixin theme '"+$1.name+"'.");
}$4[$5]=$3[$5];
}}}});




/* ID: qx.core.Target */
qx.Class.define("qx.core.Target",
{extend:qx.core.Object,
construct:function(){arguments.callee.base.call(this);
},
members:{addEventListener:function($0,
$1,
$2){if(this.getDisposed()){return;
}{};
if(this.__listeners===undefined){this.__listeners={};
}
if(this.__listeners[$0]===undefined){this.__listeners[$0]={};
}var $3="event"+qx.core.Object.toHashCode($1)+($2?"$"+qx.core.Object.toHashCode($2):"");
this.__listeners[$0][$3]={handler:$1,
object:$2};
},
removeEventListener:function($0,
$1,
$2){if(this.getDisposed()){return;
}var $3=this.__listeners;
if(!$3||$3[$0]===undefined){return;
}
if(typeof $1!=="function"){throw new Error("qx.core.Target: removeEventListener("+$0+"): '"+$1+"' is not a function!");
}var $4="event"+qx.core.Object.toHashCode($1)+($2?"$"+qx.core.Object.toHashCode($2):"");
delete this.__listeners[$0][$4];
},
hasEventListeners:function($0){return this.__listeners&&this.__listeners[$0]!==undefined&&!qx.lang.Object.isEmpty(this.__listeners[$0]);
},
createDispatchEvent:function($0){if(this.hasEventListeners($0)){this.dispatchEvent(new qx.event.type.Event($0),
true);
}},
createDispatchDataEvent:function($0,
$1){if(this.hasEventListeners($0)){this.dispatchEvent(new qx.event.type.DataEvent($0,
$1),
true);
}},
createDispatchChangeEvent:function($0,
$1,
$2){if(this.hasEventListeners($0)){this.dispatchEvent(new qx.event.type.ChangeEvent($0,
$1,
$2),
true);
}},
dispatchEvent:function($0,
$1){if(this.getDisposed()){return;
}
if($0.getTarget()==null){$0.setTarget(this);
}
if($0.getCurrentTarget()==null){$0.setCurrentTarget(this);
}this._dispatchEvent($0,
$1);
var $2=$0.getDefaultPrevented();
$1&&$0.dispose();
return !$2;
},
_dispatchEvent:function($0){var $1=this.__listeners;
if($1){$0.setCurrentTarget(this);
var $2=$1[$0.getType()];
if($2){var $3,
$4;
for(var $5 in $2){$3=$2[$5].handler;
$4=$2[$5].object||this;
$3.call($4,
$0);
}}}if($0.getBubbles()&&!$0.getPropagationStopped()&&typeof (this.getParent)=="function"){var $6=this.getParent();
if($6&&!$6.getDisposed()&&$6.getEnabled()){$6._dispatchEvent($0);
}}}},
destruct:function(){this._disposeObjectDeep("__listeners",
2);
}});




/* ID: qx.event.type.Event */
qx.Class.define("qx.event.type.Event",
{extend:qx.core.Object,
construct:function($0){arguments.callee.base.call(this);
this.setType($0);
},
properties:{type:{_fast:true,
setOnlyOnce:true},
originalTarget:{_fast:true,
setOnlyOnce:true},
target:{_fast:true,
setOnlyOnce:true},
relatedTarget:{_fast:true,
setOnlyOnce:true},
currentTarget:{_fast:true},
bubbles:{_fast:true,
defaultValue:false,
noCompute:true},
propagationStopped:{_fast:true,
defaultValue:true,
noCompute:true},
defaultPrevented:{_fast:true,
defaultValue:false,
noCompute:true}},
members:{_autoDispose:false,
preventDefault:function(){this.setDefaultPrevented(true);
},
stopPropagation:function(){this.setPropagationStopped(true);
}},
destruct:function(){this._disposeFields("_valueOriginalTarget",
"_valueTarget",
"_valueRelatedTarget",
"_valueCurrentTarget");
}});




/* ID: qx.event.type.DataEvent */
qx.Class.define("qx.event.type.DataEvent",
{extend:qx.event.type.Event,
construct:function($0,
$1){arguments.callee.base.call(this,
$0);
this.setData($1);
},
properties:{propagationStopped:{_fast:true,
defaultValue:false},
data:{_fast:true}},
destruct:function(){this._disposeFields("_valueData");
}});




/* ID: qx.event.type.ChangeEvent */
qx.Class.define("qx.event.type.ChangeEvent",
{extend:qx.event.type.Event,
construct:function($0,
$1,
$2){arguments.callee.base.call(this,
$0);
this.setValue($1);
this.setOldValue($2);
},
properties:{value:{_fast:true},
oldValue:{_fast:true}},
members:{getData:function(){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Use getValue() instead!");
return this.getValue();
}},
destruct:function(){this._disposeFields("_valueValue",
"_valueOldValue");
}});




/* ID: qx.log.Filter */
qx.Class.define("qx.log.Filter",
{extend:qx.core.Object,
type:"abstract",
construct:function(){arguments.callee.base.call(this);
},
statics:{ACCEPT:1,
DENY:2,
NEUTRAL:3},
members:{decide:function($0){throw new Error("decide is abstract");
}}});




/* ID: qx.log.DefaultFilter */
qx.Class.define("qx.log.DefaultFilter",
{extend:qx.log.Filter,
construct:function(){arguments.callee.base.call(this);
},
properties:{enabled:{check:"Boolean",
init:true},
minLevel:{check:"Number",
nullable:true}},
members:{decide:function($0){var $1=qx.log.Filter;
if(!this.getEnabled()){return $1.DENY;
}else if(this.getMinLevel()==null){return $1.NEUTRAL;
}else{return ($0.level>=this.getMinLevel())?$1.ACCEPT:$1.DENY;
}}}});




/* ID: qx.log.LogEventProcessor */
qx.Class.define("qx.log.LogEventProcessor",
{extend:qx.core.Object,
type:"abstract",
construct:function(){arguments.callee.base.call(this);
},
members:{addFilter:function($0){if(this._filterArr==null){this._filterArr=[];
}this._filterArr.push($0);
},
clearFilters:function(){this._filterArr=null;
},
getHeadFilter:function(){return (this._filterArr==null||this._filterArr.length==0)?null:this._filterArr[0];
},
_getDefaultFilter:function(){var $0=this.getHeadFilter();
if(!($0 instanceof qx.log.DefaultFilter)){this.clearFilters();
$0=new qx.log.DefaultFilter();
this.addFilter($0);
}return $0;
},
setEnabled:function($0){this._getDefaultFilter().setEnabled($0);
},
setMinLevel:function($0){this._getDefaultFilter().setMinLevel($0);
},
decideLogEvent:function($0){var $1=qx.log.Filter.NEUTRAL;
if(this._filterArr!=null){for(var $2=0;$2<this._filterArr.length;$2++){var $3=this._filterArr[$2].decide($0);
if($3!=$1){return $3;
}}}return $1;
},
handleLogEvent:function($0){throw new Error("handleLogEvent is abstract");
}},
destruct:function(){this._disposeFields("_filterArr");
}});




/* ID: qx.log.appender.Abstract */
qx.Class.define("qx.log.appender.Abstract",
{extend:qx.log.LogEventProcessor,
type:"abstract",
construct:function(){arguments.callee.base.call(this);
},
properties:{useLongFormat:{check:"Boolean",
init:true}},
members:{handleLogEvent:function($0){if(this.decideLogEvent($0)!=qx.log.Filter.DENY){this.appendLogEvent($0);
}},
appendLogEvent:function($0){throw new Error("appendLogEvent is abstract");
},
formatLogEvent:function($0){var $1=qx.log.Logger;
var $2="";
var $3=new String(new Date().getTime()-qx.core.Bootstrap.LOADSTART);
while($3.length<6){$3="0"+$3;
}$2+=$3;
if(this.getUseLongFormat()){switch($0.level){case $1.LEVEL_DEBUG:$2+=" DEBUG: ";
break;
case $1.LEVEL_INFO:$2+=" INFO:  ";
break;
case $1.LEVEL_WARN:$2+=" WARN:  ";
break;
case $1.LEVEL_ERROR:$2+=" ERROR: ";
break;
case $1.LEVEL_FATAL:$2+=" FATAL: ";
break;
}}else{$2+=": ";
}var $4="";
for(var $5=0;$5<$0.indent;$5++){$4+="  ";
}$2+=$4;
if(this.getUseLongFormat()){$2+=$0.logger.getName();
if($0.instanceId!=null){$2+="["+$0.instanceId+"]";
}$2+=": ";
}if(typeof $0.message=="string"){$2+=$0.message;
}else{var $6=$0.message;
if($6==null){$2+="Object is null";
}else{$2+="--- Object: "+$6+" ---\n";
var $7=new Array();
try{for(var $8 in $6){$7.push($8);
}}catch(exc){$2+=$4+"  [not readable: "+exc+"]\n";
}$7.sort();
for(var $5=0;$5<$7.length;$5++){try{$2+=$4+"  "+$7[$5]+"="+$6[$7[$5]]+"\n";
}catch(exc){$2+=$4+"  "+$7[$5]+"=[not readable: "+exc+"]\n";
}}$2+=$4+"--- End of object ---";
}}if($0.throwable!=null){var $9=$0.throwable;
if($9.name==null){$2+=": "+$9;
}else{$2+=": "+$9.name;
}
if($9.message!=null){$2+=" - "+$9.message;
}
if($9.number!=null){$2+=" (#"+$9.number+")";
}var $a=qx.dev.StackTrace.getStackTraceFromError($9);
}
if($0.trace){var $a=$0.trace;
}
if($a&&$a.length>0){$2+="\n";
for(var $5=0;$5<$a.length;$5++){$2+="  at "+$a[$5]+"\n";
}}return $2;
}}});




/* ID: qx.log.appender.Window */
qx.Class.define("qx.log.appender.Window",
{extend:qx.log.appender.Abstract,
construct:function($0){arguments.callee.base.call(this);
this._id=qx.log.appender.Window.register(this);
this._name=$0;
if(this._name==null){var $1=window.location.href;
var $2=0;
for(var $3=0;$3<$1.length;$3++){$2=($2+$1.charCodeAt($3))%10000000;
}this._name="qx_log_"+$2;
}this._errorsPreventingAutoCloseCount=0;
this._divDataSets=[];
this._filterTextWords=[];
this._filterText="";
},
statics:{_nextId:1,
_registeredAppenders:{},
register:function($0){var $1=qx.log.appender.Window;
var $2=$1._nextId++;
$1._registeredAppenders[$2]=$0;
return $2;
},
getAppender:function($0){return qx.log.appender.Window._registeredAppenders[$0];
}},
properties:{maxMessages:{check:"Integer",
init:500},
popUnder:{check:"Boolean",
init:false},
autoCloseWithErrors:{check:"Boolean",
init:true,
apply:"_applyAutoCloseWithErrors"},
windowWidth:{check:"Integer",
init:600},
windowHeight:{check:"Integer",
init:350},
windowLeft:{check:"Integer",
nullable:true},
windowTop:{check:"Integer",
nullable:true}},
members:{openWindow:function(){if(this._inLogWindowCallback){return;
}this._inLogWindowCallback=true;
if(this._logWindow&&!this._logWindow.closed){return ;
}var $0=this.getWindowWidth();
var $1=this.getWindowHeight();
var $2=this.getWindowLeft();
if($2===null){$2=window.screen.width-$0;
}var $3=this.getWindowTop();
if($3===null){$3=window.screen.height-$1;
}var $4="toolbar=no,scrollbars=no,resizable=yes,"+"width="+$0+",height="+$1+",left="+$2+",top="+$3;
this._logWindow=window.open("",
this._name,
$4);
qx.client.Timer.once(this._openWindowCallback,
this,
200);
},
_openWindowCallback:function(){delete this._inLogWindowCallback;
if(!this._logWindow||this._logWindow.closed){if(this._popupBlockerWarning){return;
}alert("Could not open log window. Please disable your popup blocker!");
this._popupBlockerWarning=true;
return;
}this._popupBlockerWarning=false;
if(this.getPopUnder()){this._logWindow.blur();
window.focus();
}var $0=this._logWindow.document;
var $1=qx.core.Variant.isSet("qx.client",
"mshtml")?'#lines { width: 100%; height: expression((document.body.offsetHeight - 30) + "px"); }':'';
$0.open();
$0.write("<html><head><title>"+this._name+"</title></head>"+'<body onload="qx = opener.qx;" onunload="try{qx.log.WindowAppender._registeredAppenders['+this._id+']._autoCloseWindow()}catch(e){}">'+'  <style type="text/css">'+'    html, body, input, pre{ font-size: 11px; font-family: Tahoma, sans-serif; line-height : 1 }'+'    html, body{ padding: 0; margin: 0; border : 0 none; }'+'    * { box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box }'+'    #lines{ top: 30px; left: 0; right: 0; bottom: 0; position: absolute; overflow: auto; }'+'    #control { top: 0; left: 0; right: 0; padding: 4px 8px; background: #eee; border-bottom: 1px solid #ccc; height: 30px }'+'    pre { margin: 0; padding: 4px 8px; font-family: Consolas, "Bitstream Vera Sans Mono", monospace; }'+'    hr { border: 0 none; border-bottom: 1px solid #ccc; margin: 8px 0; padding: 0; height: 1px }'+$1+'  </style>'+'  <div id="control">'+'    <input id="marker" type="button" value="Add divider"/> &#160; &#160; Filter: <input name="filter" id="filter" type="text" value="'+this._filterText+'">'+'  </div>'+'  <div id="lines">'+'    <pre id="log" wrap="wrap"></pre>'+'  </div>'+'</body></html>');
$0.close();
this._logElem=$0.getElementById("log");
this._markerBtn=$0.getElementById("marker");
this._filterInput=$0.getElementById("filter");
this._logLinesDiv=$0.getElementById("lines");
var $2=this;
this._markerBtn.onclick=function(){$2._showMessageInLog("<hr/>");
};
this._filterInput.onkeyup=function(){$2.setFilterText($2._filterInput.value);
};
if(this._logEventQueue!=null){for(var $3=0;$3<this._logEventQueue.length;$3++){this.appendLogEvent(this._logEventQueue[$3]);
}this._logEventQueue.length=0;
}},
closeWindow:function(){if(this._logWindow!=null){this._logWindow.close();
this._logWindow=null;
this._logElem=null;
}},
_autoCloseWindow:function(){if(this.getAutoCloseWithErrors()||this._errorsPreventingAutoCloseCount==0){this.closeWindow();
}else{this._showMessageInLog("Log window message: <b>Note: "+this._errorsPreventingAutoCloseCount+" errors have been recorded, keeping log window open.</b>");
}},
_showMessageInLog:function($0){var $1={message:$0,
isDummyEventForMessage:true};
this.appendLogEvent($1);
},
appendLogEvent:function($0){if(!this._logWindow||this._logWindow.closed){if(!this._logEventQueue){this._logEventQueue=[];
}this._logEventQueue.push($0);
this.openWindow();
}else if(this._logElem==null){this._logEventQueue.push($0);
}else{var $1=this._logWindow.document.createElement("div");
if($0.level>=qx.log.Logger.LEVEL_ERROR){$1.style.backgroundColor="#FFEEEE";
if(!this.getAutoCloseWithErrors()){this._errorsPreventingAutoCloseCount+=1;
}}else if($0.level==qx.log.Logger.LEVEL_DEBUG){$1.style.color="gray";
}var $2;
if($0.isDummyEventForMessage){$2=$0.message;
}else{$2=qx.html.String.fromText(this.formatLogEvent($0));
}$1.innerHTML=$2;
this._logElem.appendChild($1);
var $3={txt:$2.toUpperCase(),
elem:$1};
this._divDataSets.push($3);
this._setDivVisibility($3);
while(this._logElem.childNodes.length>this.getMaxMessages()){this._logElem.removeChild(this._logElem.firstChild);
if(this._removedMessageCount==null){this._removedMessageCount=1;
}else{this._removedMessageCount++;
}}
if(this._removedMessageCount!=null){this._logElem.firstChild.innerHTML="("+this._removedMessageCount+" messages removed)";
}this._logLinesDiv.scrollTop=this._logLinesDiv.scrollHeight;
}},
setFilterText:function($0){if($0==null){$0="";
}this._filterText=$0;
$0=$0.toUpperCase();
this._filterTextWords=$0.split(" ");
for(var $1=0;$1<this._divDataSets.length;$1++){this._setDivVisibility(this._divDataSets[$1]);
}},
_setDivVisibility:function($0){var $1=true;
for(var $2=0;$1&&($2<this._filterTextWords.length);$2++){$1=$0.txt.indexOf(this._filterTextWords[$2])>=0;
}$0.elem.style["display"]=($1?"":"none");
},
_applyAutoCloseWithErrors:function($0,
$1){if(!$0&&$1){this._errorsPreventingAutoCloseCount=0;
this._showMessageInLog("Log window message: Starting error recording, any errors below this line will prevent the log window from closing");
}else if($0&&!$1){this._showMessageInLog("Log window message: Stopping error recording, discarding "+this._errorsPreventingAutoCloseCount+" errors.");
}}},
destruct:function(){try{if(this._markerBtn){this._markerBtn.onclick=null;
}
if(this._filterInput){this._filterInput.onkeyup=null;
}}catch(ex){}this._autoCloseWindow();
this._disposeFields("_markerBtn",
"_filterInput",
"_logLinesDiv",
"_logEventQueue",
"_filterTextWords",
"_divDataSets");
}});




/* ID: qx.client.Timer */
qx.Class.define("qx.client.Timer",
{extend:qx.core.Target,
construct:function($0){arguments.callee.base.call(this);
this.setEnabled(false);
if($0!=null){this.setInterval($0);
}this.__oninterval=qx.lang.Function.bind(this._oninterval,
this);
},
events:{"interval":"qx.event.type.Event"},
statics:{once:function($0,
$1,
$2){var $3=new qx.client.Timer($2);
$3.addEventListener("interval",
function($4){$3.dispose();
$0.call($1,
$4);
$1=null;
},
$1);
$3.start();
}},
properties:{enabled:{init:true,
check:"Boolean",
apply:"_applyEnabled"},
interval:{check:"Integer",
init:1000,
apply:"_applyInterval"}},
members:{__intervalHandler:null,
_applyInterval:function($0,
$1){if(this.getEnabled()){this.restart();
}},
_applyEnabled:function($0,
$1){if($1){window.clearInterval(this.__intervalHandler);
this.__intervalHandler=null;
}else if($0){this.__intervalHandler=window.setInterval(this.__oninterval,
this.getInterval());
}},
start:function(){this.setEnabled(true);
},
startWith:function($0){this.setInterval($0);
this.start();
},
stop:function(){this.setEnabled(false);
},
restart:function(){this.stop();
this.start();
},
restartWith:function($0){this.stop();
this.startWith($0);
},
_oninterval:function(){if(this.getEnabled()){this.createDispatchEvent("interval");
}}},
destruct:function(){if(this.__intervalHandler){window.clearInterval(this.__intervalHandler);
}this._disposeFields("__intervalHandler",
"__oninterval");
}});




/* ID: qx.log.appender.FireBug */
qx.Class.define("qx.log.appender.FireBug",
{extend:qx.log.appender.Abstract,
construct:function(){arguments.callee.base.call(this);
},
members:{appendLogEvent:function($0){if(typeof console!='undefined'){var $1=qx.log.Logger;
var $2=this.formatLogEvent($0);
switch($0.level){case $1.LEVEL_DEBUG:if(console.debug){console.debug($2);
}break;
case $1.LEVEL_INFO:if(console.info){console.info($2);
}break;
case $1.LEVEL_WARN:if(console.warn){console.warn($2);
}break;
default:if(console.error){console.error($2);
}break;
}if($0.level>=$1.LEVEL_WARN&&(!$0.throwable||!$0.throwable.stack)&&console.trace){console.trace();
}}}}});




/* ID: qx.log.appender.Native */
qx.Class.define("qx.log.appender.Native",
{extend:qx.log.appender.Abstract,
construct:function(){arguments.callee.base.call(this);
if(typeof console!='undefined'&&console.debug&&!console.emu){this._appender=new qx.log.appender.FireBug;
}else{this._appender=new qx.log.appender.Window;
}},
members:{appendLogEvent:function($0){if(this._appender){return this._appender.appendLogEvent($0);
}}},
destruct:function(){this._disposeObjects("_appender");
}});




/* ID: qx.log.Logger */
qx.Class.define("qx.log.Logger",
{extend:qx.log.LogEventProcessor,
construct:function($0,
$1){arguments.callee.base.call(this);
this._name=$0;
this._parentLogger=$1;
},
statics:{deprecatedMethodWarning:function($0,
$1){var $2,
$3,
$4;
},
deprecatedClassWarning:function($0,
$1){var $2,
$3;
},
getClassLogger:function($0){var $1=$0._logger;
if($1==null){var $2=$0.classname;
var $3=$2.split(".");
var $4=window;
var $5="";
var $6=qx.log.Logger.ROOT_LOGGER;
for(var $7=0;$7<$3.length-1;$7++){$4=$4[$3[$7]];
$5+=(($7!=0)?".":"")+$3[$7];
if($4._logger==null){$4._logger=new qx.log.Logger($5,
$6);
}$6=$4._logger;
}$1=new qx.log.Logger($2,
$6);
$0._logger=$1;
}return $1;
},
_indent:0,
LEVEL_ALL:0,
LEVEL_DEBUG:200,
LEVEL_INFO:500,
LEVEL_WARN:600,
LEVEL_ERROR:700,
LEVEL_FATAL:800,
LEVEL_OFF:1000,
ROOT_LOGGER:null},
members:{getName:function(){return this._name;
},
getParentLogger:function(){return this._parentLogger;
},
indent:function(){qx.log.Logger._indent++;
},
unindent:function(){qx.log.Logger._indent--;
},
addAppender:function($0){if(this._appenderArr==null){this._appenderArr=[];
}this._appenderArr.push($0);
},
removeAppender:function($0){if(this._appenderArr!=null){this._appenderArr.remove($0);
}},
removeAllAppenders:function(){this._appenderArr=null;
},
handleLogEvent:function($0){var $1=qx.log.Filter;
var $2=$1.NEUTRAL;
var $3=this;
while($2==$1.NEUTRAL&&$3!=null){$2=$3.decideLogEvent($0);
$3=$3.getParentLogger();
}
if($2!=$1.DENY){this.appendLogEvent($0);
}},
appendLogEvent:function($0){if(this._appenderArr!=null&&this._appenderArr.length!=0){for(var $1=0;$1<this._appenderArr.length;$1++){this._appenderArr[$1].handleLogEvent($0);
}}else if(this._parentLogger!=null){this._parentLogger.appendLogEvent($0);
}},
log:function($0,
$1,
$2,
$3,
$4){var $5={logger:this,
level:$0,
message:$1,
throwable:$3,
trace:$4,
indent:qx.log.Logger._indent,
instanceId:$2};
this.handleLogEvent($5);
},
debug:function($0,
$1,
$2){this.log(qx.log.Logger.LEVEL_DEBUG,
$0,
$1,
$2);
},
info:function($0,
$1,
$2){this.log(qx.log.Logger.LEVEL_INFO,
$0,
$1,
$2);
},
warn:function($0,
$1,
$2){this.log(qx.log.Logger.LEVEL_WARN,
$0,
$1,
$2);
},
error:function($0,
$1,
$2){this.log(qx.log.Logger.LEVEL_ERROR,
$0,
$1,
$2);
},
fatal:function($0,
$1,
$2){this.log(qx.log.Logger.LEVEL_FATAL,
$0,
$1,
$2);
},
measureReset:function(){if(this._totalMeasureTime!=null){this.debug("Measure reset. Total measure time: "+this._totalMeasureTime+" ms");
}this._lastMeasureTime=null;
this._totalMeasureTime=null;
},
measure:function($0,
$1,
$2){if(this._lastMeasureTime==null){$0="(measure start) "+$0;
}else{var $3=new Date().getTime()-this._lastMeasureTime;
if(this._totalMeasureTime==null){this._totalMeasureTime=0;
}this._totalMeasureTime+=$3;
$0="(passed time: "+$3+" ms) "+$0;
}this.debug($0,
$1,
$2);
this._lastMeasureTime=new Date().getTime();
},
printStackTrace:function(){var $0=qx.dev.StackTrace.getStackTrace();
qx.lang.Array.removeAt($0,
0);
this.log(qx.log.Logger.LEVEL_DEBUG,
"Current stack trace",
"",
null,
$0);
}},
settings:{"qx.logAppender":"qx.log.appender.Native",
"qx.minLogLevel":200},
defer:function($0){$0.ROOT_LOGGER=new $0("root",
null);
$0.ROOT_LOGGER.setMinLevel(qx.core.Setting.get("qx.minLogLevel"));
$0.ROOT_LOGGER.addAppender(new (qx.Class.getByName(qx.core.Setting.get("qx.logAppender"))));
},
destruct:function(){this._disposeFields("_parentLogger",
"_appenderArr");
}});




/* ID: qx.dev.StackTrace */
qx.Class.define("qx.dev.StackTrace",
{statics:{getStackTrace:qx.core.Variant.select("qx.client",
{"gecko":function(){try{throw new Error();
}catch(e){var $0=this.getStackTraceFromError(e);
qx.lang.Array.removeAt($0,
0);
var $1=this.getStackTraceFromCaller(arguments);
var $2=$1.length>$0.length?$1:$0;
for(var $3=0;$3<Math.min($1.length,
$0.length);$3++){var $4=$1[$3];
if($4.indexOf("anonymous")>=0){continue;
}var $5=$4.split(":");
if($5.length!=2){continue;
}var $6=$5[0];
var $7=$5[1];
var $8=$0[$3];
var $9=$8.split(":");
var $a=$9[0];
var $b=$9[1];
if(qx.Class.getByName($a)){var $c=$a;
}else{$c=$6;
}var $d=$c+":";
if($7){$d+=$7+":";
}$d+=$b;
$2[$3]=$d;
}return $2;
}},
"mshtml|webkit":function(){return this.getStackTraceFromCaller(arguments);
},
"opera":function(){var $0;
try{$0.bar();
}catch(e){var $1=this.getStackTraceFromError(e);
qx.lang.Array.removeAt($1,
0);
return $1;
}return [];
}}),
getStackTraceFromCaller:qx.core.Variant.select("qx.client",
{"opera":function($0){return [];
},
"default":function($0){var $1=[];
var $2=qx.lang.Function.getCaller($0);
var $3={};
while($2){var $4=this.getFunctionName($2);
$1.push($4);
try{$2=$2.caller;
}catch(e){break;
}
if(!$2){break;
}var $5=qx.core.Object.toHashCode($2);
if($3[$5]){$1.push("...");
break;
}$3[$5]=$2;
}return $1;
}}),
getStackTraceFromError:qx.core.Variant.select("qx.client",
{"gecko":function($0){if(!$0.stack){return [];
}var $1=/@(.+):(\d+)$/gm;
var $2;
var $3=[];
while(($2=$1.exec($0.stack))!=null){var $4=$2[1];
var $5=$2[2];
var $6=this.__fileNameToClassName($4);
$3.push($6+":"+$5);
}return $3;
},
"webkit":function($0){if($0.sourceURL&&$0.line){return [this.__fileNameToClassName($0.sourceURL)+":"+$0.line];
}},
"opera":function($0){if($0.message.indexOf("Backtrace:")<0){return [];
}var $1=[];
var $2=qx.lang.String.trim($0.message.split("Backtrace:")[1]);
var $3=$2.split("\n");
for(var $4=0;$4<$3.length;$4++){var $5=$3[$4].match(/\s*Line ([0-9]+) of.* (\S.*)/);
if($5&&$5.length>=2){var $6=$5[1];
var $7=this.__fileNameToClassName($5[2]);
$1.push($7+":"+$6);
}}return $1;
},
"default":function(){return [];
}}),
getFunctionName:function($0){if($0.$$original){return $0.classname+":constructor wrapper";
}
if($0.wrapper){return $0.wrapper.classname+":constructor";
}
if($0.classname){return $0.classname+":constructor";
}
if($0.mixin){for(var $1 in $0.mixin.$$members){if($0.mixin.$$members[$1]==$0){return $0.mixin.name+":"+$1;
}}for(var $1 in $0.mixin){if($0.mixin[$1]==$0){return $0.mixin.name+":"+$1;
}}}
if($0.self){var $2=$0.self.constructor;
if($2){for(var $1 in $2.prototype){if($2.prototype[$1]==$0){return $2.classname+":"+$1;
}}for(var $1 in $2){if($2[$1]==$0){return $2.classname+":"+$1;
}}}}var $3=$0.toString().match(/(function\s*\w*\(.*?\))/);
if($3&&$3.length>=1&&$3[1]){return $3[1];
}var $3=$0.toString().match(/(function\s*\(.*?\))/);
if($3&&$3.length>=1&&$3[1]){return "anonymous: "+$3[1];
}return 'anonymous';
},
__fileNameToClassName:function($0){var $1="/source/class/";
var $2=$0.indexOf($1);
var $3=($2==-1)?$0:$0.substring($2+$1.length).replace(/\//g,
".").replace(/\.js$/,
"");
return $3;
}}});




/* ID: qx.html.String */
qx.Class.define("qx.html.String",
{statics:{escape:function($0){return qx.dom.String.escapeEntities($0,
qx.html.Entity.FROM_CHARCODE);
},
unescape:function($0){return qx.dom.String.unescapeEntities($0,
qx.html.Entity.TO_CHARCODE);
},
fromText:function($0){return qx.html.String.escape($0).replace(/(  |\n)/g,
function($1){var $2={"  ":" &nbsp;",
"\n":"<br>"};
return $2[$1]||$1;
});
},
toText:function($0){return qx.html.String.unescape($0.replace(/\s+|<([^>])+>/gi,
function($1){if(/\s+/.test($1)){return " ";
}else if(/^<BR|^<br/gi.test($1)){return "\n";
}else{return "";
}}));
}}});




/* ID: qx.dom.String */
qx.Class.define("qx.dom.String",
{statics:{escapeEntities:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){var $2,
$3=[];
for(var $4=0,
$5=$0.length;$4<$5;$4++){var $6=$0.charAt($4);
var $7=$6.charCodeAt(0);
if($1[$7]){$2="&"+$1[$7]+";";
}else{if($7>0x7F){$2="&#"+$7+";";
}else{$2=$6;
}}$3[$3.length]=$2;
}return $3.join("");
},
"default":function($0,
$1){var $2,
$3="";
for(var $4=0,
$5=$0.length;$4<$5;$4++){var $6=$0.charAt($4);
var $7=$6.charCodeAt(0);
if($1[$7]){$2="&"+$1[$7]+";";
}else{if($7>0x7F){$2="&#"+$7+";";
}else{$2=$6;
}}$3+=$2;
}return $3;
}}),
unescapeEntities:function($0,
$1){return $0.replace(/&[#\w]+;/gi,
function($2){var $3=$2;
var $2=$2.substring(1,
$2.length-1);
var $4=$1[$2];
if($4){$3=String.fromCharCode($4);
}else{if($2.charAt(0)=='#'){if($2.charAt(1).toUpperCase()=='X'){$4=$2.substring(2);
if($4.match(/^[0-9A-Fa-f]+$/gi)){$3=String.fromCharCode(parseInt("0x"+$4));
}}else{$4=$2.substring(1);
if($4.match(/^\d+$/gi)){$3=String.fromCharCode(parseInt($4));
}}}}return $3;
});
},
stripTags:function($0){return $0.replace(/<\/?[^>]+>/gi,
"");
}}});




/* ID: qx.html.Entity */
qx.Class.define("qx.html.Entity",
{statics:{TO_CHARCODE:{"quot":34,
"amp":38,
"lt":60,
"gt":62,
"nbsp":160,
"iexcl":161,
"cent":162,
"pound":163,
"curren":164,
"yen":165,
"brvbar":166,
"sect":167,
"uml":168,
"copy":169,
"ordf":170,
"laquo":171,
"not":172,
"shy":173,
"reg":174,
"macr":175,
"deg":176,
"plusmn":177,
"sup2":178,
"sup3":179,
"acute":180,
"micro":181,
"para":182,
"middot":183,
"cedil":184,
"sup1":185,
"ordm":186,
"raquo":187,
"frac14":188,
"frac12":189,
"frac34":190,
"iquest":191,
"Agrave":192,
"Aacute":193,
"Acirc":194,
"Atilde":195,
"Auml":196,
"Aring":197,
"AElig":198,
"Ccedil":199,
"Egrave":200,
"Eacute":201,
"Ecirc":202,
"Euml":203,
"Igrave":204,
"Iacute":205,
"Icirc":206,
"Iuml":207,
"ETH":208,
"Ntilde":209,
"Ograve":210,
"Oacute":211,
"Ocirc":212,
"Otilde":213,
"Ouml":214,
"times":215,
"Oslash":216,
"Ugrave":217,
"Uacute":218,
"Ucirc":219,
"Uuml":220,
"Yacute":221,
"THORN":222,
"szlig":223,
"agrave":224,
"aacute":225,
"acirc":226,
"atilde":227,
"auml":228,
"aring":229,
"aelig":230,
"ccedil":231,
"egrave":232,
"eacute":233,
"ecirc":234,
"euml":235,
"igrave":236,
"iacute":237,
"icirc":238,
"iuml":239,
"eth":240,
"ntilde":241,
"ograve":242,
"oacute":243,
"ocirc":244,
"otilde":245,
"ouml":246,
"divide":247,
"oslash":248,
"ugrave":249,
"uacute":250,
"ucirc":251,
"uuml":252,
"yacute":253,
"thorn":254,
"yuml":255,
"fnof":402,
"Alpha":913,
"Beta":914,
"Gamma":915,
"Delta":916,
"Epsilon":917,
"Zeta":918,
"Eta":919,
"Theta":920,
"Iota":921,
"Kappa":922,
"Lambda":923,
"Mu":924,
"Nu":925,
"Xi":926,
"Omicron":927,
"Pi":928,
"Rho":929,
"Sigma":931,
"Tau":932,
"Upsilon":933,
"Phi":934,
"Chi":935,
"Psi":936,
"Omega":937,
"alpha":945,
"beta":946,
"gamma":947,
"delta":948,
"epsilon":949,
"zeta":950,
"eta":951,
"theta":952,
"iota":953,
"kappa":954,
"lambda":955,
"mu":956,
"nu":957,
"xi":958,
"omicron":959,
"pi":960,
"rho":961,
"sigmaf":962,
"sigma":963,
"tau":964,
"upsilon":965,
"phi":966,
"chi":967,
"psi":968,
"omega":969,
"thetasym":977,
"upsih":978,
"piv":982,
"bull":8226,
"hellip":8230,
"prime":8242,
"Prime":8243,
"oline":8254,
"frasl":8260,
"weierp":8472,
"image":8465,
"real":8476,
"trade":8482,
"alefsym":8501,
"larr":8592,
"uarr":8593,
"rarr":8594,
"darr":8595,
"harr":8596,
"crarr":8629,
"lArr":8656,
"uArr":8657,
"rArr":8658,
"dArr":8659,
"hArr":8660,
"forall":8704,
"part":8706,
"exist":8707,
"empty":8709,
"nabla":8711,
"isin":8712,
"notin":8713,
"ni":8715,
"prod":8719,
"sum":8721,
"minus":8722,
"lowast":8727,
"radic":8730,
"prop":8733,
"infin":8734,
"ang":8736,
"and":8743,
"or":8744,
"cap":8745,
"cup":8746,
"int":8747,
"there4":8756,
"sim":8764,
"cong":8773,
"asymp":8776,
"ne":8800,
"equiv":8801,
"le":8804,
"ge":8805,
"sub":8834,
"sup":8835,
"sube":8838,
"supe":8839,
"oplus":8853,
"otimes":8855,
"perp":8869,
"sdot":8901,
"lceil":8968,
"rceil":8969,
"lfloor":8970,
"rfloor":8971,
"lang":9001,
"rang":9002,
"loz":9674,
"spades":9824,
"clubs":9827,
"hearts":9829,
"diams":9830,
"OElig":338,
"oelig":339,
"Scaron":352,
"scaron":353,
"Yuml":376,
"circ":710,
"tilde":732,
"ensp":8194,
"emsp":8195,
"thinsp":8201,
"zwnj":8204,
"zwj":8205,
"lrm":8206,
"rlm":8207,
"ndash":8211,
"mdash":8212,
"lsquo":8216,
"rsquo":8217,
"sbquo":8218,
"ldquo":8220,
"rdquo":8221,
"bdquo":8222,
"dagger":8224,
"Dagger":8225,
"permil":8240,
"lsaquo":8249,
"rsaquo":8250,
"euro":8364}},
defer:function($0,
$1,
$2){$0.FROM_CHARCODE=qx.lang.Object.invert($0.TO_CHARCODE);
}});




/* ID: qx.html.EventRegistration */
qx.Class.define("qx.html.EventRegistration",
{statics:{addEventListener:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1,
$2){$0.attachEvent("on"+$1,
$2);
},
"default":function($0,
$1,
$2){$0.addEventListener($1,
$2,
false);
}}),
removeEventListener:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1,
$2){$0.detachEvent("on"+$1,
$2);
},
"default":function($0,
$1,
$2){$0.removeEventListener($1,
$2,
false);
}})}});




/* ID: qx.core.Init */
qx.Class.define("qx.core.Init",
{type:"singleton",
extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this._onloadWrapped=qx.lang.Function.bind(this._onload,
this);
this._onbeforeunloadWrapped=qx.lang.Function.bind(this._onbeforeunload,
this);
this._onunloadWrapped=qx.lang.Function.bind(this._onunload,
this);
qx.html.EventRegistration.addEventListener(window,
"load",
this._onloadWrapped);
qx.html.EventRegistration.addEventListener(window,
"beforeunload",
this._onbeforeunloadWrapped);
qx.html.EventRegistration.addEventListener(window,
"unload",
this._onunloadWrapped);
},
events:{"load":"qx.event.type.Event",
"beforeunload":"qx.event.type.Event",
"unload":"qx.event.type.Event"},
properties:{application:{nullable:true,
check:function($0){if(typeof $0=="function"){throw new Error("The application property takes an application instance as parameter "+"and no longer a class/constructor. You may have to fix your 'index.html'.");
}return $0&&qx.Class.hasInterface($0.constructor,
qx.application.IApplication);
}}},
members:{_autoDispose:false,
_onload:function($0){if(this._onloadDone){return;
}this._onloadDone=true;
this.createDispatchEvent("load");
this.debug("qooxdoo "+qx.core.Version.toString());
{this.debug("loaded "+qx.lang.Object.getLength(qx.OO.classes)+" old classes");
};
this.debug("loaded "+qx.Class.getTotalNumber()+" classes");
this.debug("loaded "+qx.Interface.getTotalNumber()+" interfaces");
this.debug("loaded "+qx.Mixin.getTotalNumber()+" mixins");
if(qx.Theme){this.debug("loaded "+qx.Theme.getTotalNumber()+" themes");
}
if(qx.locale&&qx.locale.Manager){this.debug("loaded "+qx.locale.Manager.getInstance().getAvailableLocales().length+" locales");
}var $1=qx.core.Client.getInstance();
this.debug("client: "+$1.getEngine()+"-"+$1.getMajor()+"."+$1.getMinor()+"/"+$1.getPlatform()+"/"+$1.getLocale());
this.debug("browser: "+$1.getBrowser()+"/"+($1.supportsSvg()?"svg":$1.supportsVml()?"vml":"none"));
{};
if(!this.getApplication()){var $2=qx.Class.getByName(qx.core.Setting.get("qx.application"));
if($2){this.setApplication(new $2(this));
}}
if(!this.getApplication()){return;
}this.debug("application: "+this.getApplication().classname+"["+this.getApplication().toHashCode()+"]");
var $3=new Date;
this.getApplication().main();
this.info("main runtime: "+(new Date-$3)+"ms");
},
_onbeforeunload:function($0){this.createDispatchEvent("beforeunload");
if(this.getApplication()){var $1=this.getApplication().close();
if($1!=null){$0.returnValue=$1;
}}},
_onunload:function($0){this.createDispatchEvent("unload");
if(this.getApplication()){this.getApplication().terminate();
}qx.core.Object.dispose(true);
}},
settings:{"qx.application":"qx.application.Gui"},
destruct:function(){qx.html.EventRegistration.removeEventListener(window,
"load",
this._onloadWrapped);
qx.html.EventRegistration.removeEventListener(window,
"beforeunload",
this._onbeforeunloadWrapped);
qx.html.EventRegistration.removeEventListener(window,
"unload",
this._onunloadWrapped);
},
defer:function($0,
$1,
$2){$0.getInstance();
}});




/* ID: qx.application.IApplication */
qx.Interface.define("qx.application.IApplication",
{members:{main:function(){return true;
},
close:function(){return true;
},
terminate:function(){return true;
}}});




/* ID: qx.core.Version */
qx.Class.define("qx.core.Version",
{statics:{major:0,
minor:0,
revision:0,
state:"",
svn:0,
folder:"",
toString:function(){return this.major+"."+this.minor+(this.revision==0?"":"."+this.revision)+(this.state==""?"":"-"+this.state)+(this.svn==0?"":" (r"+this.svn+")")+(this.folder==""?"":" ["+this.folder+"]");
},
__init:function(){var $0=qx.core.Setting.get("qx.version").split(" ");
var $1=$0.shift();
var $2=$0.join(" ");
if(/([0-9]+)\.([0-9]+)(\.([0-9]))?(-([a-z0-9]+))?/.test($1)){this.major=(RegExp.$1!=""?parseInt(RegExp.$1):0);
this.minor=(RegExp.$2!=""?parseInt(RegExp.$2):0);
this.revision=(RegExp.$4!=""?parseInt(RegExp.$4):0);
this.state=typeof RegExp.$6=="string"?RegExp.$6:"";
}
if(/(\(r([0-9]+)\))?(\s\[([a-zA-Z0-9_-]+)\])?/.test($2)){this.svn=(RegExp.$2!=""?parseInt(RegExp.$2):0);
this.folder=typeof RegExp.$4=="string"?RegExp.$4:"";
}}},
settings:{"qx.version":"0.0"},
defer:function($0){$0.__init();
}});




/* ID: qx.OO */
{qx.Class.define("qx.OO",
{statics:{classes:{},
defineClass:function($0,
$1,
$2){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Use qx.Class.define instead");
var $3=$0.split(".");
var $4=$3.length-1;
var $5=window;
for(var $6=0;$6<$4;$6++){if(typeof $5[$3[$6]]==="undefined"){$5[$3[$6]]={};
}$5=$5[$3[$6]];
}if(typeof $1==="undefined"){if(typeof $2!=="undefined"){throw new Error("SuperClass is undefined, but constructor was given for class: "+$0);
}qx.Clazz=$5[$3[$6]]={};
qx.Proto=null;
qx.Super=null;
}else if(typeof $2==="undefined"){qx.Clazz=$5[$3[$6]]=$1;
qx.Proto=null;
qx.Super=$1;
}else{qx.Clazz=$5[$3[$6]]=$2;
var $7=function(){};
$7.prototype=$1.prototype;
qx.Proto=$2.prototype=new $7;
qx.Super=$2.superclass=$1;
qx.Proto.classname=$2.classname=$0;
qx.Proto.constructor=$2;
}qx.OO.classes[$0]=qx.Class;
},
isAvailable:function($0){qx.log.Logger.deprecatedMethodWarning(arguments.callee);
return qx.OO.classes[$0]!=null;
},
addFastProperty:function($0){{};
return qx.core.LegacyProperty.addFastProperty($0,
qx.Proto);
},
addCachedProperty:function($0){{};
return qx.core.LegacyProperty.addCachedProperty($0,
qx.Proto);
},
addPropertyGroup:function($0){{};
return qx.Class.addPropertyGroup($0,
qx.Proto);
},
removeProperty:function($0){{};
return qx.core.LegacyProperty.removeProperty($0,
qx.Proto);
},
changeProperty:function($0){qx.log.Logger.deprecatedMethodWarning(arguments.callee);
return qx.core.LegacyProperty.addProperty($0,
qx.Proto);
},
addProperty:function($0){qx.log.Logger.deprecatedMethodWarning(arguments.callee);
return qx.core.LegacyProperty.addProperty($0,
qx.Proto);
}}});
};




/* ID: qx.theme.classic.color.Royale */
qx.Theme.define("qx.theme.classic.color.Royale",
{title:"Windows Royale",
colors:{"background":[235,
233,
237],
"border-light":"white",
"border-light-shadow":[220,
223,
228],
"border-dark":[133,
135,
140],
"border-dark-shadow":[167,
166,
170],
"effect":[254,
200,
60],
"selected":[51,
94,
168],
"text":"black",
"text-disabled":[167,
166,
170],
"text-selected":"white",
"tooltip":[255,
255,
225],
"tooltip-text":"black",
"menu":"white",
"list":"white",
"field":"white",
"button":[235,
233,
237],
"button-hover":[246,
245,
247],
"button-abandoned":[235,
233,
237],
"window-active-caption-text":[255,
255,
255],
"window-inactive-caption-text":[255,
255,
255],
"window-active-caption":[51,
94,
168],
"window-inactive-caption":[111,
161,
217],
"button-view-pane":[250,
251,
254],
"button-view-bar":[225,
238,
255],
"tab-view-pane":[250,
251,
254],
"tab-view-border":[145,
165,
189],
"tab-view-button":[225,
238,
255],
"tab-view-button-hover":[250,
251,
254],
"tab-view-button-checked":[250,
251,
254],
"radio-view-pane":[250,
251,
254],
"radio-view-bar":[225,
238,
255],
"radio-view-button-checked":[250,
251,
254],
"list-view":"white",
"list-view-border":[167,
166,
170],
"list-view-header":[242,
242,
242],
"list-view-header-border":[214,
213,
217],
"list-view-header-cell-hover":"white",
"date-chooser":"white",
"date-chooser-title":[98,
133,
186],
"table-pane":"white",
"table-header":[242,
242,
242],
"table-header-border":[214,
213,
217],
"table-header-cell":[235,
234,
219],
"table-header-cell-hover":[255,
255,
255],
"table-focus-indicator":[179,
217,
255],
"table-row-background-focused-selected":[90,
138,
211],
"table-row-background-focused":[221,
238,
255],
"table-row-background-selected":[51,
94,
168],
"table-row-background-even":[250,
248,
243],
"table-row-background-odd":[255,
255,
255],
"table-row-selected":[255,
255,
255],
"table-row":[0,
0,
0]}});




/* ID: qx.theme.classic.Border */
qx.Theme.define("qx.theme.classic.Border",
{title:"Classic",
borders:{"black":{width:1,
color:"black"},
"white":{width:1,
color:"white"},
"dark-shadow":{width:1,
color:"border-dark-shadow"},
"light-shadow":{width:1,
color:"border-light-shadow"},
"light":{width:1,
color:"border-light"},
"dark":{width:1,
color:"border-dark"},
"tooltip":{width:1,
color:"tooltip-text"},
"inset":{width:2,
color:["border-dark-shadow",
"border-light",
"border-light",
"border-dark-shadow"],
innerColor:["border-dark",
"border-light-shadow",
"border-light-shadow",
"border-dark"]},
"outset":{width:2,
color:["border-light-shadow",
"border-dark",
"border-dark",
"border-light-shadow"],
innerColor:["border-light",
"border-dark-shadow",
"border-dark-shadow",
"border-light"]},
"groove":{width:2,
color:["border-dark-shadow",
"border-light",
"border-light",
"border-dark-shadow"],
innerColor:["border-light",
"border-dark-shadow",
"border-dark-shadow",
"border-light"]},
"ridge":{width:2,
color:["border-light",
"border-dark-shadow",
"border-dark-shadow",
"border-light"],
innerColor:["border-dark-shadow",
"border-light",
"border-light",
"border-dark-shadow"]},
"inset-thin":{width:1,
color:["border-dark-shadow",
"border-light",
"border-light",
"border-dark-shadow"]},
"outset-thin":{width:1,
color:["border-light",
"border-dark-shadow",
"border-dark-shadow",
"border-light"]},
"resizer":{width:[1,
3,
3,
1],
color:["border-light",
"border-dark-shadow",
"border-dark-shadow",
"border-light"],
innerColor:["border-light-shadow",
"border-dark",
"border-dark",
"border-light-shadow"]},
"line-left":{widthLeft:1,
colorLeft:"border-dark-shadow"},
"line-right":{widthRight:1,
colorRight:"border-dark-shadow"},
"line-top":{widthTop:1,
colorTop:"border-dark-shadow"},
"line-bottom":{widthBottom:1,
colorBottom:"border-dark-shadow"},
"divider-vertical":{widthTop:1,
colorTop:"border-dark-shadow"},
"divider-horizontal":{widthLeft:1,
colorLeft:"border-dark-shadow"}}});




/* ID: qx.theme.classic.font.Default */
qx.Theme.define("qx.theme.classic.font.Default",
{title:"Classic",
fonts:{"default":{size:11,
family:["Lucida Grande",
"Tahoma",
"Verdana",
"Liberation Sans",
"Bitstream Vera Sans"]},
"bold":{size:11,
family:["Lucida Grande",
"Tahoma",
"Verdana",
"Liberation Sans",
"Bitstream Vera Sans"],
bold:true},
"large":{size:13,
family:["Lucida Grande",
"Tahoma",
"Verdana",
"Liberation Sans",
"Bitstream Vera Sans"]},
"bold-large":{size:13,
family:["Lucida Grande",
"Tahoma",
"Verdana",
"Liberation Sans",
"Bitstream Vera Sans"],
bold:true},
"monospace":{size:11,
family:["Consolas",
"Liberation Sans Mono",
"Bitstream Vera Sans Mono",
"Courier New",
"monospace"]}}});




/* ID: qx.util.manager.Value */
qx.Class.define("qx.util.manager.Value",
{type:"abstract",
extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this._registry={};
this._dynamic={};
},
members:{connect:function($0,
$1,
$2){{};
var $3="v"+$1.toHashCode()+"$"+qx.core.Object.toHashCode($0);
var $4=this._registry;
if($2!==null&&this._preprocess){$2=this._preprocess($2);
}if(this.isDynamic($2)){$4[$3]={callback:$0,
object:$1,
value:$2};
}else if($4[$3]){delete $4[$3];
}$0.call($1,
this.resolveDynamic($2)||$2);
},
resolveDynamic:function($0){return this._dynamic[$0];
},
isDynamic:function($0){return this._dynamic[$0]!==undefined;
},
_updateObjects:function(){var $0=this._registry;
var $1;
for(var $2 in $0){$1=$0[$2];
$1.callback.call($1.object,
this.resolveDynamic($1.value));
}}},
destruct:function(){this._disposeFields("_registry",
"_dynamic");
}});




/* ID: qx.io.Alias */
qx.Class.define("qx.io.Alias",
{type:"singleton",
extend:qx.util.manager.Value,
construct:function(){arguments.callee.base.call(this);
this._aliases={};
this.add("static",
qx.core.Setting.get("qx.resourceUri")+"/static");
},
members:{_preprocess:function($0){var $1=this._dynamic;
if($1[$0]===false){return $0;
}else if($1[$0]===undefined){if($0.charAt(0)==="/"||$0.charAt(0)==="."||$0.indexOf("http://")===0||$0.indexOf("https://")==="0"||$0.indexOf("file://")===0){$1[$0]=false;
return $0;
}var $2=$0.substring(0,
$0.indexOf("/"));
var $3=this._aliases[$2];
if($3!==undefined){$1[$0]=$3+$0.substring($2.length);
}}return $0;
},
add:function($0,
$1){this._aliases[$0]=$1;
var $2=this._dynamic;
var $3=this._registry;
var $4;
var $5={};
for(var $6 in $2){if($6.substring(0,
$6.indexOf("/"))===$0){$2[$6]=$1+$6.substring($0.length);
$5[$6]=true;
}}for(var $7 in $3){$4=$3[$7];
if($5[$4.value]){$4.callback.call($4.object,
$2[$4.value]);
}}},
remove:function($0){delete this._aliases[$0];
},
resolve:function($0){if($0!==null){$0=this._preprocess($0);
}return this._dynamic[$0]||$0;
}},
destruct:function(){this._disposeFields("_aliases");
}});




/* ID: qx.theme.classic.Widget */
qx.Theme.define("qx.theme.classic.Widget",
{title:"Windows",
widgets:{uri:qx.core.Setting.get("qx.resourceUri")+"/widget/Windows"}});




/* ID: qx.theme.classic.Appearance */
qx.Theme.define("qx.theme.classic.Appearance",
{title:"Classic",
appearances:{"empty":{},
"widget":{},
"image":{},
"atom":{},
"popup":{},
"cursor-dnd-move":{style:function($0){return {source:"widget/cursors/move.gif"};
}},
"cursor-dnd-copy":{style:function($0){return {source:"widget/cursors/copy.gif"};
}},
"cursor-dnd-alias":{style:function($0){return {source:"widget/cursors/alias.gif"};
}},
"cursor-dnd-nodrop":{style:function($0){return {source:"widget/cursors/nodrop.gif"};
}},
"label":{style:function($0){return {textColor:$0.disabled?"text-disabled":"undefined"};
}},
"client-document":{style:function($0){return {backgroundColor:"background",
textColor:"text",
font:"default"};
}},
"client-document-blocker":{style:function($0){return {cursor:"default",
backgroundImage:"static/image/blank.gif"};
}},
"tool-tip":{include:"popup",
style:function($0){return {backgroundColor:"tooltip",
textColor:"tooltip-text",
border:"tooltip",
padding:[1,
3,
2,
3]};
}},
"iframe":{style:function($0){return {border:"inset"};
}},
"check-box":{style:function($0){return {padding:[2,
3]};
}},
"radio-button":{include:"check-box"},
"button":{style:function($0){if($0.pressed||$0.checked||$0.abandoned){var $1="inset";
}else{var $1="outset";
}
if($0.pressed||$0.abandoned){var $2=[4,
3,
2,
5];
}else{var $2=[3,
4];
}return {backgroundColor:$0.abandoned?"button-abandoned":$0.over?"button-hover":"button",
border:$1,
padding:$2};
}},
"toolbar":{style:function($0){return {border:"outset-thin",
backgroundColor:"background"};
}},
"toolbar-part":{},
"toolbar-part-handle":{style:function($0){return {width:10};
}},
"toolbar-part-handle-line":{style:function($0){return {top:2,
left:3,
bottom:2,
width:4,
border:"outset-thin"};
}},
"toolbar-separator":{style:function($0){return {width:8};
}},
"toolbar-separator-line":{style:function($0){return {top:2,
left:3,
width:"auto",
bottom:2,
border:"divider-horizontal"};
}},
"toolbar-button":{style:function($0){if($0.pressed||$0.checked||$0.abandoned){var $1="inset-thin";
var $2=[3,
2,
1,
4];
}else if($0.over){var $1="outset-thin";
var $2=[2,
3];
}else{var $1="undefined";
var $2=[3,
4];
}return {cursor:"default",
spacing:4,
width:"auto",
border:$1,
padding:$2,
verticalChildrenAlign:"middle",
backgroundColor:$0.abandoned?"button-abandoned":"button",
backgroundImage:$0.checked&&!$0.over?"static/image/dotted_white.gif":null};
}},
"button-view":{style:function($0){return {border:"dark-shadow"};
}},
"button-view-pane":{style:function($0){return {backgroundColor:"button-view-pane",
padding:10};
}},
"button-view-page":{},
"button-view-bar":{style:function($0){var $1="undefined";
var $2="undefined";
var $3=qx.ui.core.Border;
if($0.barTop){$1=[1,
0];
$2=$3.fromConfig({bottom:[1,
"solid",
"border-dark-shadow"]});
}else if($0.barBottom){$1=[1,
0];
$2=$3.fromConfig({top:[1,
"solid",
"border-dark-shadow"]});
}else if($0.barLeft){$1=[0,
1];
$2=$3.fromConfig({right:[1,
"solid",
"border-dark-shadow"]});
}else if($0.barRight){$1=[0,
1];
$2=$3.fromConfig({left:[1,
"solid",
"border-dark-shadow"]});
}return {backgroundColor:"button-view-bar",
padding:$1||"undefined",
border:$2||"undefined"};
}},
"button-view-button":{style:function($0){var $1,
$2,
$3,
$4,
$5;
if($0.checked||$0.over){$5=new qx.ui.core.Border(1,
"solid",
"border-dark-shadow");
if($0.barTop){$5.setBottom(3,
"solid",
"effect");
$4=[3,
6,
1,
6];
}else if($0.barBottom){$5.setTop(3,
"solid",
"effect");
$4=[1,
6,
3,
6];
}else if($0.barLeft){$5.setRight(3,
"solid",
"effect");
$4=[3,
4,
3,
6];
}else{$5.setLeft(3,
"solid",
"effect");
$4=[3,
6,
3,
4];
}}else{$5="undefined";
$4=[4,
7];
}
if($0.barTop||$0.barBottom){$1=[0,
1];
$2="auto";
$3=null;
}else{$1=[1,
0];
$3="auto";
$2=null;
}return {backgroundColor:$0.checked?"button-view-pane":"undefined",
iconPosition:"top",
margin:$1,
width:$2,
height:$3,
border:$5,
padding:$4||"undefined"};
}},
"tab-view":{style:function($0){return {spacing:-1};
}},
"tab-view-bar":{},
"tab-view-pane":{style:function($0){return {backgroundColor:"tab-view-pane",
border:new qx.ui.core.Border(1,
"solid",
"tab-view-border"),
padding:10};
}},
"tab-view-page":{},
"tab-view-button":{style:function($0){var $1,
$2,
$3,
$4;
var $5,
$6,
$7,
$8;
var $9,
$a;
$5=0;
$6=0;
$a=new qx.ui.core.Border(1,
"solid",
"tab-view-border");
if($0.checked){$1=2;
$2=4;
$3=7;
$4=8;
$7=-1;
$8=-2;
$9="tab-view-button-checked";
if($0.barTop){$a.setWidthBottom(0);
$a.setTop(3,
"solid",
"effect");
}else{$a.setWidthTop(0);
$a.setBottom(3,
"solid",
"effect");
}
if($0.alignLeft){if($0.firstChild){$3=6;
$4=7;
$8=0;
}}else{if($0.lastChild){$3=8;
$4=5;
$7=0;
}}}else{$1=2;
$2=2;
$3=5;
$4=6;
$7=1;
$8=0;
$9=$0.over?"tab-view-button-hover":"tab-view-button";
if($0.barTop){$a.setWidthBottom(0);
$5=3;
$6=1;
}else{$a.setWidthTop(0);
$5=1;
$6=3;
}
if($0.alignLeft){if($0.firstChild){$3=6;
$4=5;
}}else{if($0.lastChild){$3=6;
$4=5;
$7=0;
}}}return {padding:[$1,
$4,
$2,
$3],
margin:[$5,
$7,
$6,
$8],
border:$a,
backgroundColor:$9};
}},
"radio-view":{include:"button-view"},
"radio-view-pane":{style:function($0){return {backgroundColor:"radio-view-pane"};
}},
"radio-view-page":{},
"radio-view-bar":{style:function($0){return {backgroundColor:"radio-view-bar",
padding:[1,
0],
border:$0.barTop?qx.ui.core.Border.fromConfig({bottom:[1,
"solid",
"border-dark-shadow"]}):qx.ui.core.Border.fromConfig({top:[1,
"solid",
"border-dark-shadow"]})};
}},
"radio-view-button":{style:function($0){var $1,
$2;
if($0.checked||$0.over){$1=new qx.ui.core.Border(1,
"solid",
"border-dark-shadow");
$1.setLeft(3,
"solid",
"effect");
$2=[2,
6,
2,
4];
}else{$1="undefined";
$2=[3,
7];
}return {backgroundColor:$0.checked?"radio-view-button-checked":"undefined",
iconPosition:"left",
margin:[0,
1],
width:"auto",
opacity:$0.checked?1.0:0.3,
border:$1,
padding:$2};
}},
"window":{style:function($0){return {backgroundColor:"background",
padding:1,
border:$0.maximized?"undefined":"outset"};
}},
"window-captionbar":{style:function($0){return {padding:[1,
2,
2],
verticalChildrenAlign:"middle",
backgroundColor:$0.active?"window-active-caption":"window-inactive-caption",
textColor:$0.active?"window-active-caption-text":"window-inactive-caption-text"};
}},
"window-resize-frame":{style:function($0){return {border:"dark-shadow"};
}},
"window-captionbar-icon":{style:function($0){return {marginRight:2};
}},
"window-captionbar-title":{style:function($0){return {cursor:"default",
font:"bold",
marginRight:2};
}},
"window-captionbar-button":{include:"button",
style:function($0){return {padding:$0.pressed||$0.abandoned?[2,
1,
0,
3]:[1,
2]};
}},
"window-captionbar-minimize-button":{include:"window-captionbar-button",
style:function($0){return {icon:"widget/window/minimize.gif"};
}},
"window-captionbar-restore-button":{include:"window-captionbar-button",
style:function($0){return {icon:"widget/window/restore.gif"};
}},
"window-captionbar-maximize-button":{include:"window-captionbar-button",
style:function($0){return {icon:"widget/window/maximize.gif"};
}},
"window-captionbar-close-button":{include:"window-captionbar-button",
style:function($0){return {marginLeft:2,
icon:"widget/window/close.gif"};
}},
"window-statusbar":{style:function($0){return {border:"inset-thin"};
}},
"window-statusbar-text":{style:function($0){return {padding:[1,
4]};
}},
"color-popup":{style:function($0){return {padding:4,
border:"outset",
backgroundColor:"background"};
}},
"resizer":{style:function($0){return {border:"outset"};
}},
"resizer-frame":{style:function($0){return {border:"dark-shadow"};
}},
"menu":{style:function($0){return {backgroundColor:"menu",
border:"outset",
padding:1};
}},
"menu-layout":{},
"menu-button":{style:function($0){return {spacing:2,
padding:[2,
4],
verticalChildrenAlign:"middle",
backgroundColor:$0.over?"selected":"undefined",
textColor:$0.over?"text-selected":"undefined"};
}},
"menu-button-arrow":{style:function($0){return {source:"widget/arrows/next.gif"};
}},
"menu-check-box":{include:"menu-button",
style:function($0){return {icon:$0.checked?"widget/menu/checkbox.gif":"static/image/blank.gif"};
}},
"menu-radio-button":{include:"menu-button",
style:function($0){return {icon:$0.checked?"widget/menu/radiobutton.gif":"static/image/blank.gif"};
}},
"menu-separator":{style:function($0){return {marginTop:3,
marginBottom:2,
paddingLeft:3,
paddingRight:3};
}},
"menu-separator-line":{style:function($0){return {right:0,
left:0,
height:"auto",
border:"divider-vertical"};
}},
"list":{style:function($0){return {border:"inset-thin",
backgroundColor:"list"};
}},
"list-item":{style:function($0){return {horizontalChildrenAlign:"left",
verticalChildrenAlign:"middle",
spacing:4,
padding:[3,
5],
backgroundColor:$0.selected?"selected":"undefined",
textColor:$0.selected?"text-selected":"undefined"};
}},
"text-field":{style:function($0){return {border:"inset",
padding:[1,
3],
textColor:$0.disabled?"text-disabled":"undefined",
backgroundColor:"field"};
}},
"text-area":{include:"text-field"},
"combo-box":{style:function($0){return {border:"inset",
backgroundColor:"field"};
}},
"combo-box-list":{include:"list",
style:function($0){return {border:"undefined",
overflow:"scrollY"};
}},
"combo-box-popup":{include:"list",
style:function($0){return {maxHeight:150,
border:"dark-shadow"};
}},
"combo-box-text-field":{include:"text-field",
style:function($0){return {border:"undefined",
backgroundColor:"transparent"};
}},
"combo-box-button":{include:"button",
style:function($0){return {padding:[0,
3,
0,
2],
icon:"widget/arrows/down.gif"};
}},
"combo-box-ex":{style:function($0){return {border:"inset",
backgroundColor:"field"};
}},
"combo-box-ex-list":{include:"list",
style:function($0){return {border:"undefined",
edge:0};
}},
"combo-box-ex-text-field":{include:"text-field",
style:function($0){return {border:"undefined",
minWidth:30,
width:100,
backgroundColor:"transparent"};
}},
"combo-box-ex-popup":{include:"list",
style:function($0){return {border:"resizer"};
}},
"combo-box-ex-button":{include:"combo-box-button"},
"treevirtual-focus-indicator":{include:"empty"},
"tree-element":{style:function($0){return {height:16,
verticalChildrenAlign:"middle"};
}},
"tree-element-icon":{style:function($0){return {width:16,
height:16};
}},
"tree-element-label":{include:"label",
style:function($0){return {marginLeft:3,
height:15,
padding:2,
backgroundColor:$0.selected?"selected":"undefined",
textColor:$0.disabled?"text-disabled":($0.selected?"text-selected":"undefined")};
}},
"tree-folder":{include:"tree-element"},
"tree-folder-icon":{include:"tree-element-icon"},
"tree-folder-label":{include:"tree-element-label"},
"tree":{include:"tree-folder"},
"tree-icon":{include:"tree-folder-icon"},
"tree-label":{include:"tree-folder-label"},
"list-view":{style:function($0){return {border:new qx.ui.core.Border(1,
"solid",
"list-view-border"),
backgroundColor:"list-view"};
}},
"list-view-pane":{style:function($0){return {horizontalSpacing:1};
}},
"list-view-header":{style:function($0){return {border:qx.ui.core.Border.fromConfig({bottom:[1,
"solid",
"list-view-header-border"]}),
backgroundColor:"list-view-header"};
}},
"list-view-header-cell":{style:function($0){return {padding:[2,
6],
spacing:4,
backgroundColor:$0.over?"list-view-header-cell-hover":"undefined",
paddingBottom:$0.over?0:2,
border:$0.over?new qx.ui.core.Border.fromConfig({bottom:[2,
"solid",
"effect"]}):"undefined"};
}},
"list-view-header-cell-arrow-up":{style:function($0){return {source:"widget/arrows/up.gif"};
}},
"list-view-header-cell-arrow-down":{style:function($0){return {source:"widget/arrows/down.gif"};
}},
"list-view-header-separator":{style:function($0){return {backgroundColor:"list-view-header-border",
width:1,
marginTop:1,
marginBottom:1};
}},
"list-view-content-cell":{style:function($0){return {cursor:"default",
backgroundColor:$0.selected?"selected":"undefined",
textColor:$0.selected?"text-selected":"undefined",
border:$0.lead&&
!$0.selected?
new qx.ui.core.Border.fromConfig({top:[1,
"solid",
"effect"],
bottom:[1,
"solid",
"effect"]}):"undefined",
marginTop:$0.lead&&!$0.selected?0:1,
marginBottom:$0.lead&&!$0.selected?0:1};
}},
"list-view-content-cell-image":{include:"list-view-content-cell",
style:function($0){return {paddingLeft:6,
paddingRight:6};
}},
"list-view-content-cell-text":{include:"list-view-content-cell",
style:function($0){return {overflow:"hidden",
paddingLeft:6,
paddingRight:6};
}},
"list-view-content-cell-html":{include:"list-view-content-cell-text"},
"list-view-content-cell-icon-html":{include:"list-view-content-cell-text"},
"list-view-content-cell-link":{include:"list-view-content-cell-text"},
"group-box":{style:function($0){return {backgroundColor:"background"};
}},
"group-box-legend":{style:function($0){return {location:[10,
1],
backgroundColor:"background",
paddingRight:3,
paddingLeft:4,
marginRight:10};
}},
"group-box-frame":{style:function($0){return {edge:[8,
0,
0],
padding:[12,
9],
border:"groove"};
}},
"check-box-group-box-legend":{style:function($0){return {location:[10,
1],
backgroundColor:"background",
paddingRight:3};
}},
"radio-button-group-box-legend":{include:"check-box-group-box-legend"},
"spinner":{style:function($0){return {border:"inset",
backgroundColor:"field"};
}},
"spinner-text-field":{include:"text-field",
style:function($0){return {backgroundColor:"transparent"};
}},
"spinner-button":{style:function($0){return {width:16,
backgroundColor:"background",
paddingLeft:3,
border:$0.pressed||$0.checked||$0.abandoned?"inset":"outset"};
}},
"spinner-button-up":{include:"spinner-button",
style:function($0){return {source:"widget/arrows/up_small.gif"};
}},
"spinner-button-down":{include:"spinner-button",
style:function($0){return {paddingTop:1,
source:"widget/arrows/down_small.gif"};
}},
"colorselector":{style:function($0){return {backgroundColor:"background",
border:"outset"};
}},
"datechooser-toolbar-button":{style:function($0){var $1={backgroundColor:$0.abandoned?"button-abandoned":"button",
backgroundImage:($0.checked&&!$0.over)?"static/image/dotted_white.gif":null,
spacing:4,
width:"auto",
verticalChildrenAlign:"middle"};
if($0.pressed||$0.checked||$0.abandoned){$1.border="inset-thin";
}else if($0.over){$1.border="outset-thin";
}else{$1.border="undefined";
}
if($0.pressed||$0.checked||$0.abandoned){$1.padding=[2,
0,
0,
2];
}else if($0.over){$1.padding=1;
}else{$1.padding=2;
}return $1;
}},
"datechooser-monthyear":{style:function($0){return {font:"large",
textAlign:"center",
verticalAlign:"middle"};
}},
"datechooser-datepane":{style:function($0){return {border:new qx.ui.core.Border(1,
"solid",
"gray"),
backgroundColor:"date-chooser"};
}},
"datechooser-weekday":{style:function($0){var $1=qx.ui.core.Border.fromConfig({bottom:[1,
"solid",
"gray"]});
return {border:$1,
font:"bold-large",
textAlign:"center",
textColor:$0.weekend?"date-chooser-title":"date-chooser",
backgroundColor:$0.weekend?"date-chooser":"date-chooser-title"};
}},
"datechooser-day":{style:function($0){return {textAlign:"center",
verticalAlign:"middle",
border:$0.today?"black":"undefined",
textColor:$0.selected?"text-selected":$0.otherMonth?"text-disabled":"undefined",
backgroundColor:$0.selected?"selected":"undefined",
padding:[2,
4]};
}},
"datechooser-week":{style:function($0){if($0.header){var $1=qx.ui.core.Border.fromConfig({right:[1,
"solid",
"gray"],
bottom:[1,
"solid",
"gray"]});
}else{var $1=qx.ui.core.Border.fromConfig({right:[1,
"solid",
"gray"]});
}return {textAlign:"center",
textColor:"date-chooser-title",
padding:[2,
4],
border:$1};
}},
"table-focus-statusbar":{style:function($0){return {border:qx.ui.core.Border.fromConfig({top:[1,
"solid",
"border-dark-shadow"]}),
paddingLeft:2,
paddingRight:2};
}},
"table-focus-indicator":{style:function($0){return {border:new qx.ui.core.Border(2,
"solid",
"table-focus-indicator")};
}},
"table-editor-textfield":{include:"text-field",
style:function($0){return {border:"undefined",
padding:[0,
2]};
}},
"table-pane":{style:function($0){return {backgroundColor:"table-pane"};
}},
"table-header":{style:function($0){return {border:qx.ui.core.Border.fromConfig({bottom:[1,
"solid",
"table-header-border"]}),
backgroundColor:"table-header"};
}},
"table-menubar-button":{style:function($0){if($0.pressed||$0.checked||$0.abandoned){var $1="inset-thin";
var $2=[3,
2,
1,
4];
}else if($0.over){var $1="outset-thin";
var $2=[2,
3];
}else{var $1="undefined";
var $2=[3,
4];
}return {cursor:"default",
spacing:4,
width:"auto",
border:$1,
padding:$2,
verticalChildrenAlign:"middle",
backgroundColor:$0.abandoned?"button-abandoned":"button",
icon:"widget/table/selectColumnOrder.png"};
}},
"table-header-cell":{style:function($0){var $1,
$2,
$3;
if($0.mouseover){$1=qx.ui.core.Border.fromConfig({right:[1,
"solid",
"table-header-border"],
bottom:[2,
"solid",
"effect"]});
$3=0;
$2="table-header-cell-hover";
}else{$1=qx.ui.core.Border.fromConfig({right:[1,
"solid",
"table-header-border"]});
$3=2;
$2="table-header-cell";
}return {paddingLeft:2,
paddingRight:2,
paddingBottom:$3,
spacing:2,
overflow:"hidden",
iconPosition:"right",
verticalChildrenAlign:"middle",
border:$1,
backgroundColor:$2,
icon:$0.sorted?($0.sortedAscending?"widget/table/ascending.png":"widget/table/descending.png"):null,
horizontalChildrenAlign:"left"};
}},
"splitpane":{style:function($0){return {overflow:"hidden",
splitterSize:8,
backgroundColor:"background"};
}},
"splitpane-splitter":{style:function($0){return {cursor:$0.horizontal?"col-resize":"row-resize"};
}},
"splitpane-slider":{style:function($0){return {opacity:0.5,
backgroundColor:"background"};
}},
"splitpane-knob":{style:function($0){if($0.horizontal){return {opacity:$0.dragging?0.5:1.0,
top:"50%",
left:"50%",
cursor:"col-resize",
source:"widget/splitpane/knob-horizontal.png",
marginLeft:-2,
marginTop:-7};
}else{return {opacity:$0.dragging?0.5:1.0,
top:"50%",
left:"50%",
source:"widget/splitpane/knob-vertical.png",
marginTop:-2,
marginLeft:-7,
cursor:"row-resize"};
}}},
"scrollbar-blocker":{style:function($0){return {backgroundColor:"black",
opacity:0.2};
}}}});




/* ID: qx.theme.icon.Nuvola */
qx.Theme.define("qx.theme.icon.Nuvola",
{title:"Nuvola",
icons:{uri:qx.core.Setting.get("qx.resourceUri")+"/icon/Nuvola"}});




/* ID: qx.theme.ClassicRoyale */
qx.Theme.define("qx.theme.ClassicRoyale",
{title:"Classic Royale",
meta:{color:qx.theme.classic.color.Royale,
border:qx.theme.classic.Border,
font:qx.theme.classic.font.Default,
widget:qx.theme.classic.Widget,
appearance:qx.theme.classic.Appearance,
icon:qx.theme.icon.Nuvola}});




/* ID: qx.application.Gui */
qx.Class.define("qx.application.Gui",
{extend:qx.core.Target,
implement:qx.application.IApplication,
properties:{uiReady:{check:"Boolean",
init:false}},
members:{main:function(){qx.ui.core.Widget.initScrollbarWidth();
qx.theme.manager.Meta.getInstance().initialize();
qx.event.handler.EventHandler.getInstance();
qx.ui.core.ClientDocument.getInstance();
qx.client.Timer.once(this._preload,
this,
0);
},
close:function(){},
terminate:function(){},
_preload:function(){this.debug("preloading visible images...");
this.__preloader=new qx.io.image.PreloaderSystem(qx.io.image.Manager.getInstance().getVisibleImages(),
this._preloaderDone,
this);
this.__preloader.start();
},
_preloaderDone:function(){this.setUiReady(true);
this.__preloader.dispose();
this.__preloader=null;
var $0=(new Date).valueOf();
qx.ui.core.Widget.flushGlobalQueues();
this.info("render runtime: "+(new Date-$0)+"ms");
qx.event.handler.EventHandler.getInstance().attachEvents();
qx.client.Timer.once(this._postload,
this,
100);
},
_postload:function(){this.debug("preloading hidden images...");
this.__postloader=new qx.io.image.PreloaderSystem(qx.io.image.Manager.getInstance().getHiddenImages(),
this._postloaderDone,
this);
this.__postloader.start();
},
_postloaderDone:function(){this.__postloader.dispose();
this.__postloader=null;
}}});




/* ID: qx.ui.core.Widget */
qx.Class.define("qx.ui.core.Widget",
{extend:qx.core.Target,
type:"abstract",
construct:function(){arguments.callee.base.call(this);
this._layoutChanges={};
if(qx.core.Setting.get("qx.widgetDebugId")){this._generateHtmlId();
}},
events:{"beforeAppear":"qx.event.type.Event",
"appear":"qx.event.type.Event",
"beforeDisappear":"qx.event.type.Event",
"disappear":"qx.event.type.Event",
"beforeInsertDom":"qx.event.type.Event",
"insertDom":"qx.event.type.Event",
"beforeRemoveDom":"qx.event.type.Event",
"removeDom":"qx.event.type.Event",
"create":"qx.event.type.Event",
"execute":"qx.event.type.Event",
"mouseover":"qx.event.type.MouseEvent",
"mousemove":"qx.event.type.MouseEvent",
"mouseout":"qx.event.type.MouseEvent",
"mousedown":"qx.event.type.MouseEvent",
"mouseup":"qx.event.type.MouseEvent",
"mousewheel":"qx.event.type.MouseEvent",
"click":"qx.event.type.MouseEvent",
"dblclick":"qx.event.type.MouseEvent",
"contextmenu":"qx.event.type.MouseEvent",
"keydown":"qx.event.type.KeyEvent",
"keypress":"qx.event.type.KeyEvent",
"keyinput":"qx.event.type.KeyEvent",
"keyup":"qx.event.type.KeyEvent",
"focusout":"qx.event.type.FocusEvent",
"focusin":"qx.event.type.FocusEvent",
"blur":"qx.event.type.FocusEvent",
"focus":"qx.event.type.FocusEvent",
"dragdrop":"qx.event.type.DragEvent",
"dragout":"qx.event.type.DragEvent",
"dragover":"qx.event.type.DragEvent",
"dragmove":"qx.event.type.DragEvent",
"dragstart":"qx.event.type.DragEvent",
"dragend":"qx.event.type.DragEvent"},
statics:{create:function($0,
$1){$0._appearance=$1;
return new $0;
},
SCROLLBAR_SIZE:null,
_autoFlushTimeout:null,
_initAutoFlush:function(){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._autoFlushTimeout=window.setTimeout(qx.ui.core.Widget._autoFlushHelper,
0);
}},
_removeAutoFlush:function(){if(qx.ui.core.Widget._autoFlushTimeout!=null){window.clearTimeout(qx.ui.core.Widget._autoFlushTimeout);
qx.ui.core.Widget._autoFlushTimeout=null;
}},
_autoFlushHelper:function(){qx.ui.core.Widget._autoFlushTimeout=null;
if(!qx.core.Object.inGlobalDispose()){qx.ui.core.Widget.flushGlobalQueues();
}},
flushGlobalQueues:function(){if(qx.ui.core.Widget._autoFlushTimeout!=null){qx.ui.core.Widget._removeAutoFlush();
}
if(qx.ui.core.Widget._inFlushGlobalQueues){return;
}var $0=qx.core.Init.getInstance().getApplication();
if($0.getUiReady&&!$0.getUiReady()){return;
}qx.ui.core.Widget._inFlushGlobalQueues=true;
qx.ui.core.Widget.flushGlobalWidgetQueue();
qx.ui.core.Widget.flushGlobalStateQueue();
qx.ui.core.Widget.flushGlobalElementQueue();
qx.ui.core.Widget.flushGlobalJobQueue();
qx.ui.core.Widget.flushGlobalLayoutQueue();
qx.ui.core.Widget.flushGlobalDisplayQueue();
delete qx.ui.core.Widget._inFlushGlobalQueues;
},
_globalWidgetQueue:[],
addToGlobalWidgetQueue:function($0){if(!$0._isInGlobalWidgetQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}qx.ui.core.Widget._globalWidgetQueue.push($0);
$0._isInGlobalWidgetQueue=true;
}},
removeFromGlobalWidgetQueue:function($0){if($0._isInGlobalWidgetQueue){qx.lang.Array.remove(qx.ui.core.Widget._globalWidgetQueue,
$0);
delete $0._isInGlobalWidgetQueue;
}},
flushGlobalWidgetQueue:function(){var $0=qx.ui.core.Widget._globalWidgetQueue,
$1,
$2;
while(($1=$0.length)>0){for(var $3=0;$3<$1;$3++){$2=$0[$3];
$2.flushWidgetQueue();
delete $2._isInGlobalWidgetQueue;
}$0.splice(0,
$1);
}},
_globalElementQueue:[],
addToGlobalElementQueue:function($0){if(!$0._isInGlobalElementQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}qx.ui.core.Widget._globalElementQueue.push($0);
$0._isInGlobalElementQueue=true;
}},
removeFromGlobalElementQueue:function($0){if($0._isInGlobalElementQueue){qx.lang.Array.remove(qx.ui.core.Widget._globalElementQueue,
$0);
delete $0._isInGlobalElementQueue;
}},
flushGlobalElementQueue:function(){var $0=qx.ui.core.Widget._globalElementQueue,
$1,
$2;
while(($1=$0.length)>0){for(var $3=0;$3<$1;$3++){$2=$0[$3];
$2._createElementImpl();
delete $2._isInGlobalElementQueue;
}$0.splice(0,
$1);
}},
_globalStateQueue:[],
addToGlobalStateQueue:function($0){if(!$0._isInGlobalStateQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}qx.ui.core.Widget._globalStateQueue.push($0);
$0._isInGlobalStateQueue=true;
}},
removeFromGlobalStateQueue:function($0){if($0._isInGlobalStateQueue){qx.lang.Array.remove(qx.ui.core.Widget._globalStateQueue,
$0);
delete $0._isInGlobalStateQueue;
}},
flushGlobalStateQueue:function(){var $0=qx.ui.core.Widget._globalStateQueue,
$1,
$2;
while(($1=$0.length)>0){for(var $3=0;$3<$1;$3++){$2=$0[$3];
$2._renderAppearance();
delete $2._isInGlobalStateQueue;
}$0.splice(0,
$1);
}},
_globalJobQueue:[],
addToGlobalJobQueue:function($0){if(!$0._isInGlobalJobQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}qx.ui.core.Widget._globalJobQueue.push($0);
$0._isInGlobalJobQueue=true;
}},
removeFromGlobalJobQueue:function($0){if($0._isInGlobalJobQueue){qx.lang.Array.remove(qx.ui.core.Widget._globalJobQueue,
$0);
delete $0._isInGlobalJobQueue;
}},
flushGlobalJobQueue:function(){var $0=qx.ui.core.Widget._globalJobQueue,
$1,
$2;
while(($1=$0.length)>0){for(var $3=0;$3<$1;$3++){$2=$0[$3];
$2._flushJobQueue($2._jobQueue);
delete $2._isInGlobalJobQueue;
}$0.splice(0,
$1);
}},
_globalLayoutQueue:[],
addToGlobalLayoutQueue:function($0){if(!$0._isInGlobalLayoutQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}qx.ui.core.Widget._globalLayoutQueue.push($0);
$0._isInGlobalLayoutQueue=true;
}},
removeFromGlobalLayoutQueue:function($0){if($0._isInGlobalLayoutQueue){qx.lang.Array.remove(qx.ui.core.Widget._globalLayoutQueue,
$0);
delete $0._isInGlobalLayoutQueue;
}},
flushGlobalLayoutQueue:function(){var $0=qx.ui.core.Widget._globalLayoutQueue,
$1,
$2;
while(($1=$0.length)>0){for(var $3=0;$3<$1;$3++){$2=$0[$3];
$2._flushChildrenQueue();
delete $2._isInGlobalLayoutQueue;
}$0.splice(0,
$1);
}},
_fastGlobalDisplayQueue:[],
_lazyGlobalDisplayQueues:{},
addToGlobalDisplayQueue:function($0){if(!$0._isInGlobalDisplayQueue&&$0._isDisplayable){if(qx.ui.core.Widget._autoFlushTimeout==null){qx.ui.core.Widget._initAutoFlush();
}var $1=$0.getParent();
if($1.isSeeable()){var $2=$1.toHashCode();
if(qx.ui.core.Widget._lazyGlobalDisplayQueues[$2]){qx.ui.core.Widget._lazyGlobalDisplayQueues[$2].push($0);
}else{qx.ui.core.Widget._lazyGlobalDisplayQueues[$2]=[$0];
}}else{qx.ui.core.Widget._fastGlobalDisplayQueue.push($0);
}$0._isInGlobalDisplayQueue=true;
}},
removeFromGlobalDisplayQueue:function($0){},
flushGlobalDisplayQueue:function(){var $0,
$1,
$2,
$3;
var $4=qx.ui.core.Widget._fastGlobalDisplayQueue;
var $5=qx.ui.core.Widget._lazyGlobalDisplayQueues;
for(var $6=0,
$7=$4.length;$6<$7;$6++){$2=$4[$6];
$2.getParent()._getTargetNode().appendChild($2.getElement());
}if(qx.Class.isDefined("qx.ui.basic.Inline")){for($0 in $5){$1=$5[$0];
for(var $6=0;$6<$1.length;$6++){$2=$1[$6];
if($2 instanceof qx.ui.basic.Inline){$2._beforeInsertDom();
try{document.getElementById($2.getInlineNodeId()).appendChild($2.getElement());
}catch(ex){$2.debug("Could not append to inline id: "+$2.getInlineNodeId(),
ex);
}$2._afterInsertDom();
$2._afterAppear();
qx.lang.Array.remove($1,
$2);
$6--;
delete $2._isInGlobalDisplayQueue;
}}}}for($0 in $5){$1=$5[$0];
if(document.createDocumentFragment&&$1.length>=3){$3=document.createDocumentFragment();
for(var $6=0,
$7=$1.length;$6<$7;$6++){$2=$1[$6];
$2._beforeInsertDom();
$3.appendChild($2.getElement());
}$1[0].getParent()._getTargetNode().appendChild($3);
for(var $6=0,
$7=$1.length;$6<$7;$6++){$2=$1[$6];
$2._afterInsertDom();
}}else{for(var $6=0,
$7=$1.length;$6<$7;$6++){$2=$1[$6];
$2._beforeInsertDom();
$2.getParent()._getTargetNode().appendChild($2.getElement());
$2._afterInsertDom();
}}}for($0 in $5){$1=$5[$0];
for(var $6=0,
$7=$1.length;$6<$7;$6++){$2=$1[$6];
if($2.getVisibility()){$2._afterAppear();
}delete $2._isInGlobalDisplayQueue;
}delete $5[$0];
}for(var $6=0,
$7=$4.length;$6<$7;$6++){delete $4[$6]._isInGlobalDisplayQueue;
}qx.lang.Array.removeAll($4);
},
getActiveSiblingHelperIgnore:function($0,
$1){for(var $2=0;$2<$0.length;$2++){if($1 instanceof $0[$2]){return true;
}}return false;
},
getActiveSiblingHelper:function($0,
$1,
$2,
$3,
$4){if(!$3){$3=[];
}var $5=$1.getChildren();
var $6=$4==null?$5.indexOf($0)+$2:$4==="first"?0:$5.length-1;
var $7=$5[$6];
while($7&&(!$7.getEnabled()||qx.ui.core.Widget.getActiveSiblingHelperIgnore($3,
$7))){$6+=$2;
$7=$5[$6];
if(!$7){return null;
}}return $7;
},
__initApplyMethods:function($0){var $1="_renderRuntime";
var $2="_resetRuntime";
var $3="this._style.";
var $4="=((v==null)?0:v)+'px'";
var $5="v";
var $6=["left",
"right",
"top",
"bottom",
"width",
"height",
"minWidth",
"maxWidth",
"minHeight",
"maxHeight"];
var $7=["Left",
"Right",
"Top",
"Bottom",
"Width",
"Height",
"MinWidth",
"MaxWidth",
"MinHeight",
"MaxHeight"];
var $8=$1+"Margin";
var $9=$2+"Margin";
var $a=$3+"margin";
for(var $b=0;$b<4;$b++){$0[$8+$7[$b]]=new Function($5,
$a+$7[$b]+$4);
$0[$9+$7[$b]]=new Function($a+$7[$b]+"=''");
}var $c=$1+"Padding";
var $d=$2+"Padding";
var $e=$3+"padding";
if(qx.core.Variant.isSet("qx.client",
"gecko")){for(var $b=0;$b<4;$b++){$0[$c+$7[$b]]=new Function($5,
$e+$7[$b]+$4);
$0[$d+$7[$b]]=new Function($e+$7[$b]+"=''");
}}else{for(var $b=0;$b<4;$b++){$0[$c+$7[$b]]=new Function($5,
"this.setStyleProperty('padding"+$7[$b]+"', ((v==null)?0:v)+'px')");
$0[$d+$7[$b]]=new Function("this.removeStyleProperty('padding"+$7[$b]+"')");
}}for(var $b=0;$b<$6.length;$b++){$0[$1+$7[$b]]=new Function($5,
$3+$6[$b]+$4);
$0[$2+$7[$b]]=new Function($3+$6[$b]+"=''");
}},
TYPE_NULL:0,
TYPE_PIXEL:1,
TYPE_PERCENT:2,
TYPE_AUTO:3,
TYPE_FLEX:4,
layoutPropertyTypes:{},
__initLayoutProperties:function($0){var $1=["width",
"height",
"minWidth",
"maxWidth",
"minHeight",
"maxHeight",
"left",
"right",
"top",
"bottom"];
for(var $2=0,
$3=$1.length,
$4,
$5,
$6;$2<$3;$2++){$4=$1[$2];
$5="_computed"+qx.lang.String.toFirstUp($4);
$6=$5+"Type";
$0.layoutPropertyTypes[$4]={dataType:$6,
dataParsed:$5+"Parsed",
dataValue:$5+"Value",
typePixel:$6+"Pixel",
typePercent:$6+"Percent",
typeAuto:$6+"Auto",
typeFlex:$6+"Flex",
typeNull:$6+"Null"};
}},
initScrollbarWidth:function(){var $0=document.createElement("div");
var $1=$0.style;
$1.height=$1.width="100px";
$1.overflow="scroll";
document.body.appendChild($0);
var $2=qx.html.Dimension.getScrollBarSizeRight($0);
qx.ui.core.Widget.SCROLLBAR_SIZE=$2?$2:16;
document.body.removeChild($0);
},
_idCounter:0},
properties:{enabled:{init:"inherit",
check:"Boolean",
inheritable:true,
apply:"_applyEnabled",
event:"changeEnabled"},
parent:{check:"qx.ui.core.Parent",
nullable:true,
event:"changeParent",
apply:"_applyParent"},
element:{check:"Element",
nullable:true,
apply:"_applyElement",
event:"changeElement"},
visibility:{check:"Boolean",
init:true,
apply:"_applyVisibility",
event:"changeVisibility"},
display:{check:"Boolean",
init:true,
apply:"_applyDisplay",
event:"changeDisplay"},
anonymous:{check:"Boolean",
init:false,
event:"changeAnonymous"},
horizontalAlign:{check:["left",
"center",
"right"],
themeable:true,
nullable:true},
verticalAlign:{check:["top",
"middle",
"bottom"],
themeable:true,
nullable:true},
allowStretchX:{check:"Boolean",
init:true},
allowStretchY:{check:"Boolean",
init:true},
zIndex:{check:"Number",
apply:"_applyZIndex",
event:"changeZIndex",
themeable:true,
nullable:true,
init:null},
backgroundColor:{nullable:true,
init:null,
check:"Color",
apply:"_applyBackgroundColor",
event:"changeBackgroundColor",
themeable:true},
textColor:{nullable:true,
init:"inherit",
check:"Color",
apply:"_applyTextColor",
event:"changeTextColor",
themeable:true,
inheritable:true},
border:{nullable:true,
init:null,
apply:"_applyBorder",
event:"changeBorder",
check:"Border",
themeable:true},
font:{nullable:true,
init:"inherit",
apply:"_applyFont",
check:"Font",
event:"changeFont",
themeable:true,
inheritable:true},
opacity:{check:"Number",
apply:"_applyOpacity",
themeable:true,
nullable:true,
init:null},
cursor:{check:"String",
apply:"_applyCursor",
themeable:true,
nullable:true,
init:null},
backgroundImage:{check:"String",
nullable:true,
apply:"_applyBackgroundImage",
themeable:true},
backgroundRepeat:{check:"String",
nullable:true,
apply:"_applyBackgroundRepeat",
themeable:true},
overflow:{check:["hidden",
"auto",
"scroll",
"scrollX",
"scrollY"],
nullable:true,
apply:"_applyOverflow",
event:"changeOverflow",
themeable:true,
init:null},
clipLeft:{check:"Integer",
apply:"_applyClip",
themeable:true,
nullable:true},
clipTop:{check:"Integer",
apply:"_applyClip",
themeable:true,
nullable:true},
clipWidth:{check:"Integer",
apply:"_applyClip",
themeable:true,
nullable:true},
clipHeight:{check:"Integer",
apply:"_applyClip",
themeable:true,
nullable:true},
tabIndex:{check:"Integer",
nullable:true,
init:null,
apply:"_applyTabIndex",
event:"changeTabIndex"},
hideFocus:{check:"Boolean",
init:false,
apply:"_applyHideFocus",
themeable:true},
enableElementFocus:{check:"Boolean",
init:true},
focused:{check:"Boolean",
init:false,
apply:"_applyFocused",
event:"changeFocused"},
selectable:{check:"Boolean",
init:null,
nullable:true,
apply:"_applySelectable"},
toolTip:{check:"qx.ui.popup.ToolTip",
nullable:true},
contextMenu:{check:"qx.ui.menu.Menu",
nullable:true},
capture:{check:"Boolean",
init:false,
apply:"_applyCapture",
event:"changeCapture"},
dropDataTypes:{nullable:true,
dispose:true},
command:{check:"qx.client.Command",
nullable:true,
apply:"_applyCommand"},
appearance:{check:"String",
init:"widget",
apply:"_applyAppearance",
event:"changeAppearance"},
supportsDropMethod:{check:"Function",
nullable:true,
init:null},
marginTop:{check:"Number",
apply:"_applyMarginTop",
nullable:true,
themeable:true},
marginRight:{check:"Number",
apply:"_applyMarginRight",
nullable:true,
themeable:true},
marginBottom:{check:"Number",
apply:"_applyMarginBottom",
nullable:true,
themeable:true},
marginLeft:{check:"Number",
apply:"_applyMarginLeft",
nullable:true,
themeable:true},
paddingTop:{check:"Number",
apply:"_applyPaddingTop",
nullable:true,
themeable:true},
paddingRight:{check:"Number",
apply:"_applyPaddingRight",
nullable:true,
themeable:true},
paddingBottom:{check:"Number",
apply:"_applyPaddingBottom",
nullable:true,
themeable:true},
paddingLeft:{check:"Number",
apply:"_applyPaddingLeft",
nullable:true,
themeable:true},
left:{apply:"_applyLeft",
event:"changeLeft",
nullable:true,
themeable:true,
init:null},
right:{apply:"_applyRight",
event:"changeRight",
nullable:true,
themeable:true,
init:null},
width:{apply:"_applyWidth",
event:"changeWidth",
nullable:true,
themeable:true,
init:null},
minWidth:{apply:"_applyMinWidth",
event:"changeMinWidth",
nullable:true,
themeable:true,
init:null},
maxWidth:{apply:"_applyMaxWidth",
event:"changeMaxWidth",
nullable:true,
themeable:true,
init:null},
top:{apply:"_applyTop",
event:"changeTop",
nullable:true,
themeable:true,
init:null},
bottom:{apply:"_applyBottom",
event:"changeBottom",
nullable:true,
themeable:true,
init:null},
height:{apply:"_applyHeight",
event:"changeHeight",
nullable:true,
themeable:true,
init:null},
minHeight:{apply:"_applyMinHeight",
event:"changeMinHeight",
nullable:true,
themeable:true,
init:null},
maxHeight:{apply:"_applyMaxHeight",
event:"changeMaxHeight",
nullable:true,
themeable:true,
init:null},
location:{group:["left",
"top"],
themeable:true},
dimension:{group:["width",
"height"],
themeable:true},
space:{group:["left",
"width",
"top",
"height"],
themeable:true},
edge:{group:["top",
"right",
"bottom",
"left"],
themeable:true,
mode:"shorthand"},
padding:{group:["paddingTop",
"paddingRight",
"paddingBottom",
"paddingLeft"],
mode:"shorthand",
themeable:true},
margin:{group:["marginTop",
"marginRight",
"marginBottom",
"marginLeft"],
mode:"shorthand",
themeable:true},
heights:{group:["minHeight",
"height",
"maxHeight"],
themeable:true},
widths:{group:["minWidth",
"width",
"maxWidth"],
themeable:true},
align:{group:["horizontalAlign",
"verticalAlign"],
themeable:true},
clipLocation:{group:["clipLeft",
"clipTop"]},
clipDimension:{group:["clipWidth",
"clipHeight"]},
clip:{group:["clipLeft",
"clipTop",
"clipWidth",
"clipHeight"]},
innerWidth:{_cached:true,
defaultValue:null},
innerHeight:{_cached:true,
defaultValue:null},
boxWidth:{_cached:true,
defaultValue:null},
boxHeight:{_cached:true,
defaultValue:null},
outerWidth:{_cached:true,
defaultValue:null},
outerHeight:{_cached:true,
defaultValue:null},
frameWidth:{_cached:true,
defaultValue:null,
addToQueueRuntime:true},
frameHeight:{_cached:true,
defaultValue:null,
addToQueueRuntime:true},
preferredInnerWidth:{_cached:true,
defaultValue:null,
addToQueueRuntime:true},
preferredInnerHeight:{_cached:true,
defaultValue:null,
addToQueueRuntime:true},
preferredBoxWidth:{_cached:true,
defaultValue:null},
preferredBoxHeight:{_cached:true,
defaultValue:null},
hasPercentX:{_cached:true,
defaultValue:false},
hasPercentY:{_cached:true,
defaultValue:false},
hasAutoX:{_cached:true,
defaultValue:false},
hasAutoY:{_cached:true,
defaultValue:false},
hasFlexX:{_cached:true,
defaultValue:false},
hasFlexY:{_cached:true,
defaultValue:false}},
members:{_computedLeftValue:null,
_computedLeftParsed:null,
_computedLeftType:null,
_computedLeftTypeNull:true,
_computedLeftTypePixel:false,
_computedLeftTypePercent:false,
_computedLeftTypeAuto:false,
_computedLeftTypeFlex:false,
_computedRightValue:null,
_computedRightParsed:null,
_computedRightType:null,
_computedRightTypeNull:true,
_computedRightTypePixel:false,
_computedRightTypePercent:false,
_computedRightTypeAuto:false,
_computedRightTypeFlex:false,
_computedTopValue:null,
_computedTopParsed:null,
_computedTopType:null,
_computedTopTypeNull:true,
_computedTopTypePixel:false,
_computedTopTypePercent:false,
_computedTopTypeAuto:false,
_computedTopTypeFlex:false,
_computedBottomValue:null,
_computedBottomParsed:null,
_computedBottomType:null,
_computedBottomTypeNull:true,
_computedBottomTypePixel:false,
_computedBottomTypePercent:false,
_computedBottomTypeAuto:false,
_computedBottomTypeFlex:false,
_computedWidthValue:null,
_computedWidthParsed:null,
_computedWidthType:null,
_computedWidthTypeNull:true,
_computedWidthTypePixel:false,
_computedWidthTypePercent:false,
_computedWidthTypeAuto:false,
_computedWidthTypeFlex:false,
_computedMinWidthValue:null,
_computedMinWidthParsed:null,
_computedMinWidthType:null,
_computedMinWidthTypeNull:true,
_computedMinWidthTypePixel:false,
_computedMinWidthTypePercent:false,
_computedMinWidthTypeAuto:false,
_computedMinWidthTypeFlex:false,
_computedMaxWidthValue:null,
_computedMaxWidthParsed:null,
_computedMaxWidthType:null,
_computedMaxWidthTypeNull:true,
_computedMaxWidthTypePixel:false,
_computedMaxWidthTypePercent:false,
_computedMaxWidthTypeAuto:false,
_computedMaxWidthTypeFlex:false,
_computedHeightValue:null,
_computedHeightParsed:null,
_computedHeightType:null,
_computedHeightTypeNull:true,
_computedHeightTypePixel:false,
_computedHeightTypePercent:false,
_computedHeightTypeAuto:false,
_computedHeightTypeFlex:false,
_computedMinHeightValue:null,
_computedMinHeightParsed:null,
_computedMinHeightType:null,
_computedMinHeightTypeNull:true,
_computedMinHeightTypePixel:false,
_computedMinHeightTypePercent:false,
_computedMinHeightTypeAuto:false,
_computedMinHeightTypeFlex:false,
_computedMaxHeightValue:null,
_computedMaxHeightParsed:null,
_computedMaxHeightType:null,
_computedMaxHeightTypeNull:true,
_computedMaxHeightTypePixel:false,
_computedMaxHeightTypePercent:false,
_computedMaxHeightTypeAuto:false,
_computedMaxHeightTypeFlex:false,
_applyLeft:function($0,
$1){this._unitDetectionPixelPercent("left",
$0);
this.addToQueue("left");
},
_applyRight:function($0,
$1){this._unitDetectionPixelPercent("right",
$0);
this.addToQueue("right");
},
_applyTop:function($0,
$1){this._unitDetectionPixelPercent("top",
$0);
this.addToQueue("top");
},
_applyBottom:function($0,
$1){this._unitDetectionPixelPercent("bottom",
$0);
this.addToQueue("bottom");
},
_applyWidth:function($0,
$1){this._unitDetectionPixelPercentAutoFlex("width",
$0);
this.addToQueue("width");
},
_applyMinWidth:function($0,
$1){this._unitDetectionPixelPercentAuto("minWidth",
$0);
this.addToQueue("minWidth");
},
_applyMaxWidth:function($0,
$1){this._unitDetectionPixelPercentAuto("maxWidth",
$0);
this.addToQueue("maxWidth");
},
_applyHeight:function($0,
$1){this._unitDetectionPixelPercentAutoFlex("height",
$0);
this.addToQueue("height");
},
_applyMinHeight:function($0,
$1){this._unitDetectionPixelPercentAuto("minHeight",
$0);
this.addToQueue("minHeight");
},
_applyMaxHeight:function($0,
$1){this._unitDetectionPixelPercentAuto("maxHeight",
$0);
this.addToQueue("maxHeight");
},
isMaterialized:function(){var $0=this._element;
return (this._initialLayoutDone&&this._isDisplayable&&qx.html.Style.getStyleProperty($0,
"display")!="none"&&qx.html.Style.getStyleProperty($0,
"visibility")!="hidden"&&$0.offsetWidth>0&&$0.offsetHeight>0);
},
pack:function(){this.setWidth(this.getPreferredBoxWidth());
this.setHeight(this.getPreferredBoxHeight());
},
auto:function(){this.setWidth("auto");
this.setHeight("auto");
},
getChildren:qx.lang.Function.returnNull,
getChildrenLength:qx.lang.Function.returnZero,
hasChildren:qx.lang.Function.returnFalse,
isEmpty:qx.lang.Function.returnTrue,
indexOf:qx.lang.Function.returnNegativeIndex,
contains:qx.lang.Function.returnFalse,
getVisibleChildren:qx.lang.Function.returnNull,
getVisibleChildrenLength:qx.lang.Function.returnZero,
hasVisibleChildren:qx.lang.Function.returnFalse,
isVisibleEmpty:qx.lang.Function.returnTrue,
_hasParent:false,
_isDisplayable:false,
isDisplayable:function(){return this._isDisplayable;
},
_checkParent:function($0,
$1){if(this.contains($0)){throw new Error("Could not insert myself into a child "+$0+"!");
}return $0;
},
_applyParent:function($0,
$1){if($1){var $2=$1.getChildren().indexOf(this);
this._computedWidthValue=this._computedMinWidthValue=this._computedMaxWidthValue=this._computedLeftValue=this._computedRightValue=null;
this._computedHeightValue=this._computedMinHeightValue=this._computedMaxHeightValue=this._computedTopValue=this._computedBottomValue=null;
this._cachedBoxWidth=this._cachedInnerWidth=this._cachedOuterWidth=null;
this._cachedBoxHeight=this._cachedInnerHeight=this._cachedOuterHeight=null;
qx.lang.Array.removeAt($1.getChildren(),
$2);
$1._invalidateVisibleChildren();
$1._removeChildFromChildrenQueue(this);
$1.getLayoutImpl().updateChildrenOnRemoveChild(this,
$2);
$1.addToJobQueue("removeChild");
$1._invalidatePreferredInnerDimensions();
this._oldParent=$1;
}
if($0){this._hasParent=true;
if(typeof this._insertIndex=="number"){qx.lang.Array.insertAt($0.getChildren(),
this,
this._insertIndex);
delete this._insertIndex;
}else{$0.getChildren().push(this);
}}else{this._hasParent=false;
}qx.core.Property.refresh(this);
return this._handleDisplayable("parent");
},
_applyDisplay:function($0,
$1){return this._handleDisplayable("display");
},
_handleDisplayable:function($0){var $1=this._computeDisplayable();
if(this._isDisplayable==$1&&!($1&&$0=="parent")){return true;
}this._isDisplayable=$1;
var $2=this.getParent();
if($2){$2._invalidateVisibleChildren();
$2._invalidatePreferredInnerDimensions();
}if($0&&this._oldParent&&this._oldParent._initialLayoutDone){var $3=this.getElement();
if($3){if(this.getVisibility()){this._beforeDisappear();
}this._beforeRemoveDom();
try{this._oldParent._getTargetNode().removeChild($3);
}catch(e){}this._afterRemoveDom();
if(this.getVisibility()){this._afterDisappear();
}}delete this._oldParent;
}if($1){if($2._initialLayoutDone){$2.getLayoutImpl().updateChildrenOnAddChild(this,
$2.getChildren().indexOf(this));
$2.addToJobQueue("addChild");
}this.addToLayoutChanges("initial");
this.addToCustomQueues($0);
if(this.getVisibility()){this._beforeAppear();
}if(!this._isCreated){qx.ui.core.Widget.addToGlobalElementQueue(this);
}qx.ui.core.Widget.addToGlobalStateQueue(this);
if(!qx.lang.Object.isEmpty(this._jobQueue)){qx.ui.core.Widget.addToGlobalJobQueue(this);
}
if(!qx.lang.Object.isEmpty(this._childrenQueue)){qx.ui.core.Widget.addToGlobalLayoutQueue(this);
}}else{qx.ui.core.Widget.removeFromGlobalElementQueue(this);
qx.ui.core.Widget.removeFromGlobalStateQueue(this);
qx.ui.core.Widget.removeFromGlobalJobQueue(this);
qx.ui.core.Widget.removeFromGlobalLayoutQueue(this);
this.removeFromCustomQueues($0);
if($2&&$0){if(this.getVisibility()){this._beforeDisappear();
}if($2._initialLayoutDone&&this._initialLayoutDone){$2.getLayoutImpl().updateChildrenOnRemoveChild(this,
$2.getChildren().indexOf(this));
$2.addToJobQueue("removeChild");
this._beforeRemoveDom();
var $4=this.getElement().parentNode;
if($4){$4.removeChild(this.getElement());
if($4&&$4!==$2._getTargetNode()){this.warn("Unexpected parent node: "+$4);
}}this._afterRemoveDom();
}$2._removeChildFromChildrenQueue(this);
if(this.getVisibility()){this._afterDisappear();
}}}this._handleDisplayableCustom($1,
$2,
$0);
return true;
},
addToCustomQueues:qx.lang.Function.returnTrue,
removeFromCustomQueues:qx.lang.Function.returnTrue,
_handleDisplayableCustom:qx.lang.Function.returnTrue,
_computeDisplayable:function(){return this.getDisplay()&&this.getParent()&&this.getParent()._isDisplayable?true:false;
},
_beforeAppear:function(){this.createDispatchEvent("beforeAppear");
},
_afterAppear:function(){this._isSeeable=true;
this.createDispatchEvent("appear");
},
_beforeDisappear:function(){this.removeState("over");
if(qx.Class.isDefined("qx.ui.form.Button")){this.removeState("pressed");
this.removeState("abandoned");
}this.createDispatchEvent("beforeDisappear");
},
_afterDisappear:function(){this._isSeeable=false;
this.createDispatchEvent("disappear");
},
_isSeeable:false,
isSeeable:function(){return this._isSeeable;
},
isAppearRelevant:function(){return this.getVisibility()&&this._isDisplayable;
},
_beforeInsertDom:function(){this.createDispatchEvent("beforeInsertDom");
},
_afterInsertDom:function(){this.createDispatchEvent("insertDom");
},
_beforeRemoveDom:function(){this.createDispatchEvent("beforeRemoveDom");
},
_afterRemoveDom:function(){this.createDispatchEvent("removeDom");
},
_applyVisibility:function($0,
$1){if($0){if(this._isDisplayable){this._beforeAppear();
}this.removeStyleProperty("display");
if(this._isDisplayable){this._afterAppear();
}}else{if(this._isDisplayable){this._beforeDisappear();
}this.setStyleProperty("display",
"none");
if(this._isDisplayable){this._afterDisappear();
}}},
show:function(){this.setVisibility(true);
this.setDisplay(true);
},
hide:function(){this.setVisibility(false);
},
connect:function(){this.setDisplay(true);
},
disconnect:function(){this.setDisplay(false);
},
_isCreated:false,
_getTargetNode:qx.core.Variant.select("qx.client",
{"gecko":function(){return this._element;
},
"default":function(){return this._borderElement||this._element;
}}),
addToDocument:function(){qx.ui.core.ClientDocument.getInstance().add(this);
},
isCreated:function(){return this._isCreated;
},
_createElementImpl:function(){this.setElement(this.getTopLevelWidget().getDocumentElement().createElement("div"));
},
_applyElement:function($0,
$1){this._isCreated=$0!=null;
if($1){$1.qx_Widget=null;
}
if($0){$0.qx_Widget=this;
$0.style.position="absolute";
this._element=$0;
this._style=$0.style;
this._applyStyleProperties($0);
this._applyHtmlProperties($0);
this._applyHtmlAttributes($0);
this._applyElementData($0);
this.createDispatchEvent("create");
this.addToStateQueue();
}else{this._element=this._style=null;
}},
addToJobQueue:function($0){if(this._hasParent){qx.ui.core.Widget.addToGlobalJobQueue(this);
}
if(!this._jobQueue){this._jobQueue={};
}this._jobQueue[$0]=true;
return true;
},
_flushJobQueue:function($0){try{var $1=this._jobQueue;
var $2=this.getParent();
if(!$2||qx.lang.Object.isEmpty($1)){return;
}var $3=this instanceof qx.ui.core.Parent?this.getLayoutImpl():null;
if($3){$3.updateSelfOnJobQueueFlush($1);
}}catch(ex){this.error("Flushing job queue (prechecks#1) failed",
ex);
}try{var $4=false;
var $5=$1.marginLeft||$1.marginRight;
var $6=$1.marginTop||$1.marginBottom;
var $7=$1.frameWidth;
var $8=$1.frameHeight;
var $9=($1.frameWidth||$1.preferredInnerWidth)&&this._recomputePreferredBoxWidth();
var $a=($1.frameHeight||$1.preferredInnerHeight)&&this._recomputePreferredBoxHeight();
if($9){var $b=this.getPreferredBoxWidth();
if(this._computedWidthTypeAuto){this._computedWidthValue=$b;
$1.width=true;
}
if(this._computedMinWidthTypeAuto){this._computedMinWidthValue=$b;
$1.minWidth=true;
}
if(this._computedMaxWidthTypeAuto){this._computedMaxWidthValue=$b;
$1.maxWidth=true;
}}
if($a){var $b=this.getPreferredBoxHeight();
if(this._computedHeightTypeAuto){this._computedHeightValue=$b;
$1.height=true;
}
if(this._computedMinHeightTypeAuto){this._computedMinHeightValue=$b;
$1.minHeight=true;
}
if(this._computedMaxHeightTypeAuto){this._computedMaxHeightValue=$b;
$1.maxHeight=true;
}}
if(($1.width||$1.minWidth||$1.maxWidth||$1.left||$1.right)&&this._recomputeBoxWidth()){$5=$7=true;
}
if(($1.height||$1.minHeight||$1.maxHeight||$1.top||$1.bottom)&&this._recomputeBoxHeight()){$6=$8=true;
}}catch(ex){this.error("Flushing job queue (recompute#2) failed",
ex);
}try{if(($5&&this._recomputeOuterWidth())||$9){$2._invalidatePreferredInnerWidth();
$2.getLayoutImpl().updateSelfOnChildOuterWidthChange(this);
$4=true;
}
if(($6&&this._recomputeOuterHeight())||$a){$2._invalidatePreferredInnerHeight();
$2.getLayoutImpl().updateSelfOnChildOuterHeightChange(this);
$4=true;
}
if($4){$2._flushJobQueue();
}}catch(ex){this.error("Flushing job queue (parentsignals#3) failed",
ex);
}try{$2._addChildToChildrenQueue(this);
for(var $c in $1){this._layoutChanges[$c]=true;
}}catch(ex){this.error("Flushing job queue (addjobs#4) failed",
ex);
}try{if(this instanceof qx.ui.core.Parent&&($1.paddingLeft||$1.paddingRight||$1.paddingTop||$1.paddingBottom)){var $d=this.getChildren(),
$e=$d.length;
if($1.paddingLeft){for(var $c=0;$c<$e;$c++){$d[$c].addToLayoutChanges("parentPaddingLeft");
}}
if($1.paddingRight){for(var $c=0;$c<$e;$c++){$d[$c].addToLayoutChanges("parentPaddingRight");
}}
if($1.paddingTop){for(var $c=0;$c<$e;$c++){$d[$c].addToLayoutChanges("parentPaddingTop");
}}
if($1.paddingBottom){for(var $c=0;$c<$e;$c++){$d[$c].addToLayoutChanges("parentPaddingBottom");
}}}
if($7){this._recomputeInnerWidth();
}
if($8){this._recomputeInnerHeight();
}
if(this._initialLayoutDone){if($3){$3.updateChildrenOnJobQueueFlush($1);
}}}catch(ex){this.error("Flushing job queue (childrensignals#5) failed",
ex);
}delete this._jobQueue;
},
_isWidthEssential:qx.lang.Function.returnTrue,
_isHeightEssential:qx.lang.Function.returnTrue,
_computeBoxWidthFallback:function(){return 0;
},
_computeBoxHeightFallback:function(){return 0;
},
_computeBoxWidth:function(){var $0=this.getParent().getLayoutImpl();
return Math.max(0,
qx.lang.Number.limit($0.computeChildBoxWidth(this),
this.getMinWidthValue(),
this.getMaxWidthValue()));
},
_computeBoxHeight:function(){var $0=this.getParent().getLayoutImpl();
return Math.max(0,
qx.lang.Number.limit($0.computeChildBoxHeight(this),
this.getMinHeightValue(),
this.getMaxHeightValue()));
},
_computeOuterWidth:function(){return Math.max(0,
(this.getMarginLeft()+this.getBoxWidth()+this.getMarginRight()));
},
_computeOuterHeight:function(){return Math.max(0,
(this.getMarginTop()+this.getBoxHeight()+this.getMarginBottom()));
},
_computeInnerWidth:function(){return Math.max(0,
this.getBoxWidth()-this.getFrameWidth());
},
_computeInnerHeight:function(){return Math.max(0,
this.getBoxHeight()-this.getFrameHeight());
},
getNeededWidth:function(){var $0=this.getParent().getLayoutImpl();
return Math.max(0,
$0.computeChildNeededWidth(this));
},
getNeededHeight:function(){var $0=this.getParent().getLayoutImpl();
return Math.max(0,
$0.computeChildNeededHeight(this));
},
_recomputeFlexX:function(){if(!this.getHasFlexX()){return false;
}
if(this._computedWidthTypeFlex){this._computedWidthValue=null;
this.addToLayoutChanges("width");
}return true;
},
_recomputeFlexY:function(){if(!this.getHasFlexY()){return false;
}
if(this._computedHeightTypeFlex){this._computedHeightValue=null;
this.addToLayoutChanges("height");
}return true;
},
_recomputePercentX:function(){if(!this.getHasPercentX()){return false;
}
if(this._computedWidthTypePercent){this._computedWidthValue=null;
this.addToLayoutChanges("width");
}
if(this._computedMinWidthTypePercent){this._computedMinWidthValue=null;
this.addToLayoutChanges("minWidth");
}
if(this._computedMaxWidthTypePercent){this._computedMaxWidthValue=null;
this.addToLayoutChanges("maxWidth");
}
if(this._computedLeftTypePercent){this._computedLeftValue=null;
this.addToLayoutChanges("left");
}
if(this._computedRightTypePercent){this._computedRightValue=null;
this.addToLayoutChanges("right");
}return true;
},
_recomputePercentY:function(){if(!this.getHasPercentY()){return false;
}
if(this._computedHeightTypePercent){this._computedHeightValue=null;
this.addToLayoutChanges("height");
}
if(this._computedMinHeightTypePercent){this._computedMinHeightValue=null;
this.addToLayoutChanges("minHeight");
}
if(this._computedMaxHeightTypePercent){this._computedMaxHeightValue=null;
this.addToLayoutChanges("maxHeight");
}
if(this._computedTopTypePercent){this._computedTopValue=null;
this.addToLayoutChanges("top");
}
if(this._computedBottomTypePercent){this._computedBottomValue=null;
this.addToLayoutChanges("bottom");
}return true;
},
_recomputeRangeX:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function(){if(this._computedLeftTypeNull||this._computedRightTypeNull){return false;
}this.addToLayoutChanges("width");
return true;
},
"default":function(){return !(this._computedLeftTypeNull||this._computedRightTypeNull);
}}),
_recomputeRangeY:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function(){if(this._computedTopTypeNull||this._computedBottomTypeNull){return false;
}this.addToLayoutChanges("height");
return true;
},
"default":function(){return !(this._computedTopTypeNull||this._computedBottomTypeNull);
}}),
_recomputeStretchingX:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function(){if(this.getAllowStretchX()&&this._computedWidthTypeNull){this._computedWidthValue=null;
this.addToLayoutChanges("width");
return true;
}return false;
},
"default":function(){if(this.getAllowStretchX()&&this._computedWidthTypeNull){return true;
}return false;
}}),
_recomputeStretchingY:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function(){if(this.getAllowStretchY()&&this._computedHeightTypeNull){this._computedHeightValue=null;
this.addToLayoutChanges("height");
return true;
}return false;
},
"default":function(){if(this.getAllowStretchY()&&this._computedHeightTypeNull){return true;
}return false;
}}),
_computeValuePixel:function($0){return Math.round($0);
},
_computeValuePixelLimit:function($0){return Math.max(0,
this._computeValuePixel($0));
},
_computeValuePercentX:function($0){return Math.round(this.getParent().getInnerWidthForChild(this)*$0*0.01);
},
_computeValuePercentXLimit:function($0){return Math.max(0,
this._computeValuePercentX($0));
},
_computeValuePercentY:function($0){return Math.round(this.getParent().getInnerHeightForChild(this)*$0*0.01);
},
_computeValuePercentYLimit:function($0){return Math.max(0,
this._computeValuePercentY($0));
},
getWidthValue:function(){if(this._computedWidthValue!=null){return this._computedWidthValue;
}
switch(this._computedWidthType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedWidthValue=this._computeValuePixelLimit(this._computedWidthParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedWidthValue=this._computeValuePercentXLimit(this._computedWidthParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedWidthValue=this.getPreferredBoxWidth();
case qx.ui.core.Widget.TYPE_FLEX:if(this.getParent().getLayoutImpl().computeChildrenFlexWidth===undefined){throw new Error("Widget "+this+": having horizontal flex size (width="+this.getWidth()+") but parent layout "+this.getParent()+" does not support it");
}this.getParent().getLayoutImpl().computeChildrenFlexWidth();
return this._computedWidthValue=this._computedWidthFlexValue;
}return null;
},
getMinWidthValue:function(){if(this._computedMinWidthValue!=null){return this._computedMinWidthValue;
}
switch(this._computedMinWidthType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedWidthValue=this._computeValuePixelLimit(this._computedMinWidthParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedWidthValue=this._computeValuePercentXLimit(this._computedMinWidthParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedMinWidthValue=this.getPreferredBoxWidth();
}return null;
},
getMaxWidthValue:function(){if(this._computedMaxWidthValue!=null){return this._computedMaxWidthValue;
}
switch(this._computedMaxWidthType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedWidthValue=this._computeValuePixelLimit(this._computedMaxWidthParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedWidthValue=this._computeValuePercentXLimit(this._computedMaxWidthParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedMaxWidthValue=this.getPreferredBoxWidth();
}return null;
},
getLeftValue:function(){if(this._computedLeftValue!=null){return this._computedLeftValue;
}
switch(this._computedLeftType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedLeftValue=this._computeValuePixel(this._computedLeftParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedLeftValue=this._computeValuePercentX(this._computedLeftParsed);
}return null;
},
getRightValue:function(){if(this._computedRightValue!=null){return this._computedRightValue;
}
switch(this._computedRightType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedRightValue=this._computeValuePixel(this._computedRightParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedRightValue=this._computeValuePercentX(this._computedRightParsed);
}return null;
},
getHeightValue:function(){if(this._computedHeightValue!=null){return this._computedHeightValue;
}
switch(this._computedHeightType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedHeightValue=this._computeValuePixelLimit(this._computedHeightParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedHeightValue=this._computeValuePercentYLimit(this._computedHeightParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedHeightValue=this.getPreferredBoxHeight();
case qx.ui.core.Widget.TYPE_FLEX:if(this.getParent().getLayoutImpl().computeChildrenFlexHeight===undefined){throw new Error("Widget "+this+": having vertical flex size (height="+this.getHeight()+") but parent layout "+this.getParent()+" does not support it");
}this.getParent().getLayoutImpl().computeChildrenFlexHeight();
return this._computedHeightValue=this._computedHeightFlexValue;
}return null;
},
getMinHeightValue:function(){if(this._computedMinHeightValue!=null){return this._computedMinHeightValue;
}
switch(this._computedMinHeightType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedMinHeightValue=this._computeValuePixelLimit(this._computedMinHeightParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedMinHeightValue=this._computeValuePercentYLimit(this._computedMinHeightParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedMinHeightValue=this.getPreferredBoxHeight();
}return null;
},
getMaxHeightValue:function(){if(this._computedMaxHeightValue!=null){return this._computedMaxHeightValue;
}
switch(this._computedMaxHeightType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedMaxHeightValue=this._computeValuePixelLimit(this._computedMaxHeightParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedMaxHeightValue=this._computeValuePercentYLimit(this._computedMaxHeightParsed);
case qx.ui.core.Widget.TYPE_AUTO:return this._computedMaxHeightValue=this.getPreferredBoxHeight();
}return null;
},
getTopValue:function(){if(this._computedTopValue!=null){return this._computedTopValue;
}
switch(this._computedTopType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedTopValue=this._computeValuePixel(this._computedTopParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedTopValue=this._computeValuePercentY(this._computedTopParsed);
}return null;
},
getBottomValue:function(){if(this._computedBottomValue!=null){return this._computedBottomValue;
}
switch(this._computedBottomType){case qx.ui.core.Widget.TYPE_PIXEL:return this._computedBottomValue=this._computeValuePixel(this._computedBottomParsed);
case qx.ui.core.Widget.TYPE_PERCENT:return this._computedBottomValue=this._computeValuePercentY(this._computedBottomParsed);
}return null;
},
_computeFrameWidth:function(){var $0=this._cachedBorderLeft+this.getPaddingLeft()+this.getPaddingRight()+this._cachedBorderRight;
switch(this.getOverflow()){case "scroll":case "scrollY":$0+=qx.ui.core.Widget.SCROLLBAR_SIZE;
break;
case "auto":break;
}return $0;
},
_computeFrameHeight:function(){var $0=this._cachedBorderTop+this.getPaddingTop()+this.getPaddingBottom()+this._cachedBorderBottom;
switch(this.getOverflow()){case "scroll":case "scrollX":$0+=qx.ui.core.Widget.SCROLLBAR_SIZE;
break;
case "auto":break;
}return $0;
},
_invalidateFrameDimensions:function(){this._invalidateFrameWidth();
this._invalidateFrameHeight();
},
_invalidatePreferredInnerDimensions:function(){this._invalidatePreferredInnerWidth();
this._invalidatePreferredInnerHeight();
},
_computePreferredBoxWidth:function(){try{return Math.max(0,
this.getPreferredInnerWidth()+this.getFrameWidth());
}catch(ex){this.error("_computePreferredBoxWidth failed",
ex);
}},
_computePreferredBoxHeight:function(){try{return Math.max(0,
this.getPreferredInnerHeight()+this.getFrameHeight());
}catch(ex){this.error("_computePreferredBoxHeight failed",
ex);
}},
_initialLayoutDone:false,
addToLayoutChanges:function($0){if(this._isDisplayable){this.getParent()._addChildToChildrenQueue(this);
}return this._layoutChanges[$0]=true;
},
addToQueue:function($0){this._initialLayoutDone?this.addToJobQueue($0):this.addToLayoutChanges($0);
},
addToQueueRuntime:function($0){return !this._initialLayoutDone||this.addToJobQueue($0);
},
_computeHasPercentX:function(){return (this._computedLeftTypePercent||this._computedWidthTypePercent||this._computedMinWidthTypePercent||this._computedMaxWidthTypePercent||this._computedRightTypePercent);
},
_computeHasPercentY:function(){return (this._computedTopTypePercent||this._computedHeightTypePercent||this._computedMinHeightTypePercent||this._computedMaxHeightTypePercent||this._computedBottomTypePercent);
},
_computeHasAutoX:function(){return (this._computedWidthTypeAuto||this._computedMinWidthTypeAuto||this._computedMaxWidthTypeAuto);
},
_computeHasAutoY:function(){return (this._computedHeightTypeAuto||this._computedMinHeightTypeAuto||this._computedMaxHeightTypeAuto);
},
_computeHasFlexX:function(){return this._computedWidthTypeFlex;
},
_computeHasFlexY:function(){return this._computedHeightTypeFlex;
},
_evalUnitsPixelPercentAutoFlex:function($0){switch($0){case "auto":return qx.ui.core.Widget.TYPE_AUTO;
case Infinity:case -Infinity:return qx.ui.core.Widget.TYPE_NULL;
}
switch(typeof $0){case "number":return isNaN($0)?qx.ui.core.Widget.TYPE_NULL:qx.ui.core.Widget.TYPE_PIXEL;
case "string":return $0.indexOf("%")!=-1?qx.ui.core.Widget.TYPE_PERCENT:$0.indexOf("*")!=-1?qx.ui.core.Widget.TYPE_FLEX:qx.ui.core.Widget.TYPE_NULL;
}return qx.ui.core.Widget.TYPE_NULL;
},
_evalUnitsPixelPercentAuto:function($0){switch($0){case "auto":return qx.ui.core.Widget.TYPE_AUTO;
case Infinity:case -Infinity:return qx.ui.core.Widget.TYPE_NULL;
}
switch(typeof $0){case "number":return isNaN($0)?qx.ui.core.Widget.TYPE_NULL:qx.ui.core.Widget.TYPE_PIXEL;
case "string":return $0.indexOf("%")!=-1?qx.ui.core.Widget.TYPE_PERCENT:qx.ui.core.Widget.TYPE_NULL;
}return qx.ui.core.Widget.TYPE_NULL;
},
_evalUnitsPixelPercent:function($0){switch($0){case Infinity:case -Infinity:return qx.ui.core.Widget.TYPE_NULL;
}
switch(typeof $0){case "number":return isNaN($0)?qx.ui.core.Widget.TYPE_NULL:qx.ui.core.Widget.TYPE_PIXEL;
case "string":return $0.indexOf("%")!=-1?qx.ui.core.Widget.TYPE_PERCENT:qx.ui.core.Widget.TYPE_NULL;
}return qx.ui.core.Widget.TYPE_NULL;
},
_unitDetectionPixelPercentAutoFlex:function($0,
$1){var $2=qx.ui.core.Widget.layoutPropertyTypes[$0];
var $3=$2.dataType;
var $4=$2.dataParsed;
var $5=$2.dataValue;
var $6=$2.typePixel;
var $7=$2.typePercent;
var $8=$2.typeAuto;
var $9=$2.typeFlex;
var $a=$2.typeNull;
var $b=this[$7];
var $c=this[$8];
var $d=this[$9];
switch(this[$3]=this._evalUnitsPixelPercentAutoFlex($1)){case qx.ui.core.Widget.TYPE_PIXEL:this[$6]=true;
this[$7]=this[$8]=this[$9]=this[$a]=false;
this[$4]=this[$5]=Math.round($1);
break;
case qx.ui.core.Widget.TYPE_PERCENT:this[$7]=true;
this[$6]=this[$8]=this[$9]=this[$a]=false;
this[$4]=parseFloat($1);
this[$5]=null;
break;
case qx.ui.core.Widget.TYPE_AUTO:this[$8]=true;
this[$6]=this[$7]=this[$9]=this[$a]=false;
this[$4]=this[$5]=null;
break;
case qx.ui.core.Widget.TYPE_FLEX:this[$9]=true;
this[$6]=this[$7]=this[$8]=this[$a]=false;
this[$4]=parseFloat($1);
this[$5]=null;
break;
default:this[$a]=true;
this[$6]=this[$7]=this[$8]=this[$9]=false;
this[$4]=this[$5]=null;
break;
}
if($b!=this[$7]){switch($0){case "minWidth":case "maxWidth":case "width":case "left":case "right":this._invalidateHasPercentX();
break;
case "maxHeight":case "minHeight":case "height":case "top":case "bottom":this._invalidateHasPercentY();
break;
}}if($c!=this[$8]){switch($0){case "minWidth":case "maxWidth":case "width":this._invalidateHasAutoX();
break;
case "minHeight":case "maxHeight":case "height":this._invalidateHasAutoY();
break;
}}if($d!=this[$9]){switch($0){case "width":this._invalidateHasFlexX();
break;
case "height":this._invalidateHasFlexY();
break;
}}},
_unitDetectionPixelPercentAuto:function($0,
$1){var $2=qx.ui.core.Widget.layoutPropertyTypes[$0];
var $3=$2.dataType;
var $4=$2.dataParsed;
var $5=$2.dataValue;
var $6=$2.typePixel;
var $7=$2.typePercent;
var $8=$2.typeAuto;
var $9=$2.typeNull;
var $a=this[$7];
var $b=this[$8];
switch(this[$3]=this._evalUnitsPixelPercentAuto($1)){case qx.ui.core.Widget.TYPE_PIXEL:this[$6]=true;
this[$7]=this[$8]=this[$9]=false;
this[$4]=this[$5]=Math.round($1);
break;
case qx.ui.core.Widget.TYPE_PERCENT:this[$7]=true;
this[$6]=this[$8]=this[$9]=false;
this[$4]=parseFloat($1);
this[$5]=null;
break;
case qx.ui.core.Widget.TYPE_AUTO:this[$8]=true;
this[$6]=this[$7]=this[$9]=false;
this[$4]=this[$5]=null;
break;
default:this[$9]=true;
this[$6]=this[$7]=this[$8]=false;
this[$4]=this[$5]=null;
break;
}
if($a!=this[$7]){switch($0){case "minWidth":case "maxWidth":case "width":case "left":case "right":this._invalidateHasPercentX();
break;
case "minHeight":case "maxHeight":case "height":case "top":case "bottom":this._invalidateHasPercentY();
break;
}}if($b!=this[$8]){switch($0){case "minWidth":case "maxWidth":case "width":this._invalidateHasAutoX();
break;
case "minHeight":case "maxHeight":case "height":this._invalidateHasAutoY();
break;
}}},
_unitDetectionPixelPercent:function($0,
$1){var $2=qx.ui.core.Widget.layoutPropertyTypes[$0];
var $3=$2.dataType;
var $4=$2.dataParsed;
var $5=$2.dataValue;
var $6=$2.typePixel;
var $7=$2.typePercent;
var $8=$2.typeNull;
var $9=this[$7];
switch(this[$3]=this._evalUnitsPixelPercent($1)){case qx.ui.core.Widget.TYPE_PIXEL:this[$6]=true;
this[$7]=this[$8]=false;
this[$4]=this[$5]=Math.round($1);
break;
case qx.ui.core.Widget.TYPE_PERCENT:this[$7]=true;
this[$6]=this[$8]=false;
this[$4]=parseFloat($1);
this[$5]=null;
break;
default:this[$8]=true;
this[$6]=this[$7]=false;
this[$4]=this[$5]=null;
break;
}
if($9!=this[$7]){switch($0){case "minWidth":case "maxWidth":case "width":case "left":case "right":this._invalidateHasPercentX();
break;
case "minHeight":case "maxHeight":case "height":case "top":case "bottom":this._invalidateHasPercentY();
break;
}}},
getTopLevelWidget:function(){return this._hasParent?this.getParent().getTopLevelWidget():null;
},
moveSelfBefore:function($0){this.getParent().addBefore(this,
$0);
},
moveSelfAfter:function($0){this.getParent().addAfter(this,
$0);
},
moveSelfToBegin:function(){this.getParent().addAtBegin(this);
},
moveSelfToEnd:function(){this.getParent().addAtEnd(this);
},
getPreviousSibling:function(){var $0=this.getParent();
if($0==null){return null;
}var $1=$0.getChildren();
return $1[$1.indexOf(this)-1];
},
getNextSibling:function(){var $0=this.getParent();
if($0==null){return null;
}var $1=$0.getChildren();
return $1[$1.indexOf(this)+1];
},
getPreviousVisibleSibling:function(){if(!this._hasParent){return null;
}var $0=this.getParent().getVisibleChildren();
return $0[$0.indexOf(this)-1];
},
getNextVisibleSibling:function(){if(!this._hasParent){return null;
}var $0=this.getParent().getVisibleChildren();
return $0[$0.indexOf(this)+1];
},
getPreviousActiveSibling:function($0){var $1=qx.ui.core.Widget.getActiveSiblingHelper(this,
this.getParent(),
-1,
$0,
null);
return $1?$1:this.getParent().getLastActiveChild();
},
getNextActiveSibling:function($0){var $1=qx.ui.core.Widget.getActiveSiblingHelper(this,
this.getParent(),
1,
$0,
null);
return $1?$1:this.getParent().getFirstActiveChild();
},
isFirstChild:function(){return this._hasParent&&this.getParent().getFirstChild()==this;
},
isLastChild:function(){return this._hasParent&&this.getParent().getLastChild()==this;
},
isFirstVisibleChild:function(){return this._hasParent&&this.getParent().getFirstVisibleChild()==this;
},
isLastVisibleChild:function(){return this._hasParent&&this.getParent().getLastVisibleChild()==this;
},
hasState:function($0){return this.__states&&this.__states[$0]?true:false;
},
addState:function($0){if(!this.__states){this.__states={};
}
if(!this.__states[$0]){this.__states[$0]=true;
if(this._hasParent){qx.ui.core.Widget.addToGlobalStateQueue(this);
}}},
removeState:function($0){if(this.__states&&this.__states[$0]){delete this.__states[$0];
if(this._hasParent){qx.ui.core.Widget.addToGlobalStateQueue(this);
}}},
_styleFromMap:function($0){var $1=qx.core.Property.$$method.style;
var $2=qx.core.Property.$$method.unstyle;
var $3;
var $4;
for(var $4 in $0){$3=$0[$4];
$3==="undefined"?this[$2[$4]]():this[$1[$4]]($3);
}},
_unstyleFromArray:function($0){var $1=qx.core.Property.$$method.unstyle;
var $2,
$3;
for(var $2=0,
$3=$0.length;$2<$3;$2++){this[$1[$0[$2]]]();
}},
_renderAppearance:function(){if(!this.__states){this.__states={};
}this._applyStateStyleFocus(this.__states);
var $0=this.getAppearance();
if($0){try{var $1=qx.theme.manager.Appearance.getInstance().styleFrom($0,
this.__states);
if($1){this._styleFromMap($1);
}}catch(ex){this.error("Could not apply state appearance",
ex);
}}},
_resetAppearanceThemeWrapper:function($0,
$1){var $2=this.getAppearance();
if($2){var $3=qx.theme.manager.Appearance.getInstance();
var $4=$3.styleFromTheme($1,
$2,
this.__states);
var $5=$3.styleFromTheme($0,
$2,
this.__states);
var $6=[];
for(var $7 in $4){if($5[$7]===undefined){$6.push($7);
}}this._unstyleFromArray($6);
this._styleFromMap($5);
}},
_applyStateStyleFocus:qx.core.Variant.select("qx.client",
{"mshtml":function($0){},
"gecko":function($0){if($0.focused){if(!qx.event.handler.FocusHandler.mouseFocus&&!this.getHideFocus()){this.setStyleProperty("MozOutline",
"1px dotted invert");
}}else{this.removeStyleProperty("MozOutline");
}},
"default":function($0){if($0.focused){if(!qx.event.handler.FocusHandler.mouseFocus&&!this.getHideFocus()){this.setStyleProperty("outline",
"1px dotted invert");
}}else{this.removeStyleProperty("outline");
}}}),
addToStateQueue:function(){qx.ui.core.Widget.addToGlobalStateQueue(this);
},
recursiveAddToStateQueue:function(){this.addToStateQueue();
},
_applyAppearance:function($0,
$1){if(!this.__states){this.__states={};
}var $2=qx.theme.manager.Appearance.getInstance();
if($0){var $3=$2.styleFrom($0,
this.__states)||{};
}
if($1){var $4=$2.styleFrom($1,
this.__states)||{};
var $5=[];
for(var $6 in $4){if(!$3||!($6 in $3)){$5.push($6);
}}}
if($5){this._unstyleFromArray($5);
}
if($3){this._styleFromMap($3);
}},
_recursiveAppearanceThemeUpdate:function($0,
$1){try{this._resetAppearanceThemeWrapper($0,
$1);
}catch(ex){this.error("Failed to update appearance theme",
ex);
}},
_applyElementData:function($0){},
setHtmlProperty:function($0,
$1){if(!this._htmlProperties){this._htmlProperties={};
}this._htmlProperties[$0]=$1;
if(this._isCreated&&this.getElement()[$0]!=$1){this.getElement()[$0]=$1;
}return true;
},
removeHtmlProperty:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(!this._htmlProperties){return;
}delete this._htmlProperties[$0];
if(this._isCreated){this.getElement().removeAttribute($0);
}return true;
},
"default":function($0){if(!this._htmlProperties){return;
}delete this._htmlProperties[$0];
if(this._isCreated){this.getElement().removeAttribute($0);
delete this.getElement()[$0];
}return true;
}}),
getHtmlProperty:function($0){if(!this._htmlProperties){return "";
}return this._htmlProperties[$0]||"";
},
_applyHtmlProperties:function($0){var $1=this._htmlProperties;
if($1){var $2;
for($2 in $1){$0[$2]=$1[$2];
}}},
_generateHtmlId:function(){var $0=this.classname+"."+qx.ui.core.Widget._idCounter++;
this.debug("setting autogenerated HTML id to "+$0);
this.setHtmlProperty("id",
$0);
},
setHtmlAttribute:function($0,
$1){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Use setHtmlProperty instead");
if(!this._htmlAttributes){this._htmlAttributes={};
}this._htmlAttributes[$0]=$1;
if(this._isCreated){this.getElement().setAttribute($0,
$1);
}return true;
},
removeHtmlAttribute:function($0){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Use removeHtmlProperty instead");
if(!this._htmlAttributes){return;
}delete this._htmlAttributes[$0];
if(this._isCreated){this.getElement().removeAttribute($0);
}return true;
},
getHtmlAttribute:function($0){if(!this._htmlAttributes){return "";
}return this._htmlAttributes[$0]||"";
},
_applyHtmlAttributes:function($0){var $1=this._htmlAttributes;
if($1){var $2;
for($2 in $1){$0.setAttribute($2,
$1[$2]);
}}},
getStyleProperty:function($0){if(!this._styleProperties){return "";
}return this._styleProperties[$0]||"";
},
__outerElementStyleProperties:{cursor:true,
zIndex:true,
filter:true,
display:true,
visibility:true},
setStyleProperty:function($0,
$1){if(!this._styleProperties){this._styleProperties={};
}this._styleProperties[$0]=$1;
if(this._isCreated){var $2=this.__outerElementStyleProperties[$0]?this.getElement():this._getTargetNode();
if($2){$2.style[$0]=($1==null)?"":$1;
}}},
removeStyleProperty:function($0){if(!this._styleProperties){return;
}delete this._styleProperties[$0];
if(this._isCreated){var $1=this.__outerElementStyleProperties[$0]?this.getElement():this._getTargetNode();
if($1){$1.style[$0]="";
}}},
_applyStyleProperties:function($0){var $1=this._styleProperties;
if(!$1){return;
}var $2;
var $3=$0;
var $4=this._getTargetNode();
var $0;
var $5;
for($2 in $1){$0=this.__outerElementStyleProperties[$2]?$3:$4;
$5=$1[$2];
$0.style[$2]=($5==null)?"":$5;
}},
_applyEnabled:function($0,
$1){if($0===false){this.addState("disabled");
this.removeState("over");
if(qx.Class.isDefined("qx.ui.form.Button")){this.removeState("abandoned");
this.removeState("pressed");
}}else{this.removeState("disabled");
}},
isFocusable:function(){return this.getEnabled()&&this.isSeeable()&&this.getTabIndex()>=0&&this.getTabIndex()!=null;
},
isFocusRoot:function(){return false;
},
getFocusRoot:function(){if(this._hasParent){return this.getParent().getFocusRoot();
}return null;
},
getActiveChild:function(){var $0=this.getFocusRoot();
if($0){return $0.getActiveChild();
}return null;
},
_ontabfocus:qx.lang.Function.returnTrue,
_applyFocused:function($0,
$1){if(!this.isCreated()){return;
}var $2=this.getFocusRoot();
if($2){if($0){$2.setFocusedChild(this);
this._visualizeFocus();
}else{if($2.getFocusedChild()==this){$2.setFocusedChild(null);
}this._visualizeBlur();
}}},
_applyHideFocus:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){this.setHtmlProperty("hideFocus",
$0);
},
"default":qx.lang.Function.returnTrue}),
_visualizeBlur:function(){if(this.getEnableElementFocus()&&(!this.getFocusRoot().getFocusedChild()||(this.getFocusRoot().getFocusedChild()&&this.getFocusRoot().getFocusedChild().getEnableElementFocus()))){try{this.getElement().blur();
}catch(ex){}}this.removeState("focused");
},
_visualizeFocus:function(){if(!qx.event.handler.FocusHandler.mouseFocus&&this.getEnableElementFocus()){try{this.getElement().focus();
}catch(ex){}}this.addState("focused");
},
focus:function(){delete qx.event.handler.FocusHandler.mouseFocus;
this.setFocused(true);
},
blur:function(){delete qx.event.handler.FocusHandler.mouseFocus;
this.setFocused(false);
},
_applyCapture:function($0,
$1){var $2=qx.event.handler.EventHandler.getInstance();
if($1){$2.setCaptureWidget(null);
}else if($0){$2.setCaptureWidget(this);
}},
_applyZIndex:function($0,
$1){if($0==null){this.removeStyleProperty("zIndex");
}else{this.setStyleProperty("zIndex",
$0);
}},
_applyTabIndex:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){this.setHtmlProperty("tabIndex",
$0<0?-1:1);
},
"gecko":function($0,
$1){this.setStyleProperty("MozUserFocus",
($0<0?"ignore":"normal"));
},
"default":function($0,
$1){this.setStyleProperty("userFocus",
($0<0?"ignore":"normal"));
this.setHtmlProperty("tabIndex",
$0<0?-1:1);
}}),
_applySelectable:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){},
"gecko":function($0,
$1){if($0){this.removeStyleProperty("MozUserSelect");
}else{this.setStyleProperty("MozUserSelect",
"none");
}},
"webkit":function($0,
$1){if($0){this.removeStyleProperty("WebkitUserSelect");
}else{this.setStyleProperty("WebkitUserSelect",
"none");
}},
"khtml":function($0,
$1){if($0){this.removeStyleProperty("KhtmlUserSelect");
}else{this.setStyleProperty("KhtmlUserSelect",
"none");
}},
"default":function($0,
$1){if($0){return this.removeStyleProperty("userSelect");
}else{this.setStyleProperty("userSelect",
"none");
}}}),
_applyOpacity:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){if($0==null||$0>=1||$0<0){this.removeStyleProperty("filter");
}else{this.setStyleProperty("filter",
("Alpha(Opacity="+Math.round($0*100)+")"));
}},
"default":function($0,
$1){if($0==null||$0>1){if(qx.core.Variant.isSet("qx.client",
"gecko")){this.removeStyleProperty("MozOpacity");
}else if(qx.core.Variant.isSet("qx.client",
"khtml")){this.removeStyleProperty("KhtmlOpacity");
}this.removeStyleProperty("opacity");
}else{$0=qx.lang.Number.limit($0,
0,
1);
if(qx.core.Variant.isSet("qx.client",
"gecko")){this.setStyleProperty("MozOpacity",
$0);
}else if(qx.core.Variant.isSet("qx.client",
"khtml")){this.setStyleProperty("KhtmlOpacity",
$0);
}this.setStyleProperty("opacity",
$0);
}}}),
__cursorMap:qx.core.Variant.select("qx.client",
{"mshtml":{"cursor":"hand",
"ew-resize":"e-resize",
"ns-resize":"n-resize",
"nesw-resize":"ne-resize",
"nwse-resize":"nw-resize"},
"opera":{"col-resize":"e-resize",
"row-resize":"n-resize",
"ew-resize":"e-resize",
"ns-resize":"n-resize",
"nesw-resize":"ne-resize",
"nwse-resize":"nw-resize"},
"default":{}}),
_applyCursor:function($0,
$1){if($0){this.setStyleProperty("cursor",
this.__cursorMap[$0]||$0);
}else{this.removeStyleProperty("cursor");
}},
_applyCommand:function($0,
$1){},
_applyBackgroundImage:function($0,
$1){var $2=qx.io.image.Manager.getInstance();
var $3=qx.io.Alias.getInstance();
if($1){$2.hide($1);
}
if($0){$2.show($0);
}$3.connect(this._styleBackgroundImage,
this,
$0);
},
_styleBackgroundImage:function($0){$0?this.setStyleProperty("backgroundImage",
"url("+$0+")"):this.removeStyleProperty("backgroundImage");
},
_applyBackgroundRepeat:function($0,
$1){$0?this.setStyleProperty("backgroundRepeat",
$0):this.removeStyleProperty("backgroundRepeat");
},
_applyClip:function($0,
$1){return this._compileClipString();
},
_compileClipString:function(){var $0=this.getClipLeft();
var $1=this.getClipTop();
var $2=this.getClipWidth();
var $3=this.getClipHeight();
var $4,
$5;
if($0==null){$4=($2==null?"auto":$2+"px");
$0="auto";
}else{$4=($2==null?"auto":$0+$2+"px");
$0=$0+"px";
}
if($1==null){$5=($3==null?"auto":$3+"px");
$1="auto";
}else{$5=($3==null?"auto":$1+$3+"px");
$1=$1+"px";
}return this.setStyleProperty("clip",
("rect("+$1+","+$4+","+$5+","+$0+")"));
},
_applyOverflow:qx.core.Variant.select("qx.client",
{"default":function($0,
$1){var $2=$0;
var $3="overflow";
switch($0){case "scrollX":$3="overflowX";
$2="scroll";
break;
case "scrollY":$3="overflowY";
$2="scroll";
break;
}var $4=["overflow",
"overflowX",
"overflowY"];
for(var $5=0;$5<$4.length;$5++){if($4[$5]!=$3){this.removeStyleProperty($4[$5]);
}}
switch($0){case "scrollX":this.setStyleProperty("overflowY",
"hidden");
break;
case "scrollY":this.setStyleProperty("overflowX",
"hidden");
break;
}this._renderOverflow($3,
$2,
$0,
$1);
this.addToQueue("overflow");
},
"gecko":function($0,
$1){var $2=$0;
var $3="overflow";
switch($2){case "hidden":$2="-moz-scrollbars-none";
break;
case "scrollX":$2="-moz-scrollbars-horizontal";
break;
case "scrollY":$2="-moz-scrollbars-vertical";
break;
}this._renderOverflow($3,
$2,
$0,
$1);
this.addToQueue("overflow");
},
"opera":function($0,
$1){var $2=$0;
var $3="overflow";
switch($2){case "scrollX":case "scrollY":$2="scroll";
break;
}this._renderOverflow($3,
$2,
$0,
$1);
this.addToQueue("overflow");
}}),
_renderOverflow:function($0,
$1,
$2,
$3){this.setStyleProperty($0,
$1||"");
this._invalidateFrameWidth();
this._invalidateFrameHeight();
},
getOverflowX:function(){var $0=this.getOverflow();
return $0=="scrollY"?"hidden":$0;
},
getOverflowY:function(){var $0=this.getOverflow();
return $0=="scrollX"?"hidden":$0;
},
_applyBackgroundColor:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._styleBackgroundColor,
this,
$0);
},
_styleBackgroundColor:function($0){$0?this.setStyleProperty("backgroundColor",
$0):this.removeStyleProperty("backgroundColor");
},
_applyTextColor:function($0,
$1){},
_applyFont:function($0,
$1){},
_cachedBorderTop:0,
_cachedBorderRight:0,
_cachedBorderBottom:0,
_cachedBorderLeft:0,
_applyBorder:function($0,
$1){qx.theme.manager.Border.getInstance().connect(this._queueBorder,
this,
$0);
},
__borderJobs:{top:"borderTop",
right:"borderRight",
bottom:"borderBottom",
left:"borderLeft"},
_queueBorder:function($0,
$1){if(!$1){var $2=this.__borderJobs;
for(var $3 in $2){this.addToQueue($2[$3]);
}this.__reflowBorderX($0);
this.__reflowBorderY($0);
}else{if($1==="left"||$1==="right"){this.__reflowBorderX($0);
}else{this.__reflowBorderY($0);
}this.addToQueue(this.__borderJobs[$1]);
}this.__borderObject=$0;
},
__reflowBorderX:function($0){var $1=this._cachedBorderLeft;
var $2=this._cachedBorderRight;
this._cachedBorderLeft=$0?$0.getWidthLeft():0;
this._cachedBorderRight=$0?$0.getWidthRight():0;
if(($1+$2)!=(this._cachedBorderLeft+this._cachedBorderRight)){this._invalidateFrameWidth();
}},
__reflowBorderY:function($0){var $1=this._cachedBorderTop;
var $2=this._cachedBorderBottom;
this._cachedBorderTop=$0?$0.getWidthTop():0;
this._cachedBorderBottom=$0?$0.getWidthBottom():0;
if(($1+$2)!=(this._cachedBorderTop+this._cachedBorderBottom)){this._invalidateFrameHeight();
}},
renderBorder:function($0){var $1=this.__borderObject;
if($1){if($0.borderTop){$1.renderTop(this);
}
if($0.borderRight){$1.renderRight(this);
}
if($0.borderBottom){$1.renderBottom(this);
}
if($0.borderLeft){$1.renderLeft(this);
}}else{var $2=qx.ui.core.Border;
if($0.borderTop){$2.resetTop(this);
}
if($0.borderRight){$2.resetRight(this);
}
if($0.borderBottom){$2.resetBottom(this);
}
if($0.borderLeft){$2.resetLeft(this);
}}},
prepareEnhancedBorder:qx.core.Variant.select("qx.client",
{"gecko":qx.lang.Function.returnTrue,
"default":function(){var $0=this.getElement();
var $1=this._borderElement=document.createElement("div");
var $2=$0.style;
var $3=this._innerStyle=$1.style;
if(qx.core.Variant.isSet("qx.client",
"mshtml")){}else{$3.width=$3.height="100%";
}$3.position="absolute";
for(var $4 in this._styleProperties){switch($4){case "zIndex":case "filter":case "display":break;
default:$3[$4]=$2[$4];
$2[$4]="";
}}
for(var $4 in this._htmlProperties){switch($4){case "unselectable":$1.unselectable=this._htmlProperties[$4];
}}while($0.firstChild){$1.appendChild($0.firstChild);
}$0.appendChild($1);
}}),
_applyPaddingTop:function($0,
$1){this.addToQueue("paddingTop");
this._invalidateFrameHeight();
},
_applyPaddingRight:function($0,
$1){this.addToQueue("paddingRight");
this._invalidateFrameWidth();
},
_applyPaddingBottom:function($0,
$1){this.addToQueue("paddingBottom");
this._invalidateFrameHeight();
},
_applyPaddingLeft:function($0,
$1){this.addToQueue("paddingLeft");
this._invalidateFrameWidth();
},
renderPadding:function($0){},
_applyMarginLeft:function($0,
$1){this.addToQueue("marginLeft");
},
_applyMarginRight:function($0,
$1){this.addToQueue("marginRight");
},
_applyMarginTop:function($0,
$1){this.addToQueue("marginTop");
},
_applyMarginBottom:function($0,
$1){this.addToQueue("marginBottom");
},
execute:function(){var $0=this.getCommand();
if($0){$0.execute(this);
}this.createDispatchEvent("execute");
},
_visualPropertyCheck:function(){if(!this.isCreated()){throw new Error(this.classname+": Element must be created previously!");
}},
setScrollLeft:function($0){this._visualPropertyCheck();
this._getTargetNode().scrollLeft=$0;
},
setScrollTop:function($0){this._visualPropertyCheck();
this._getTargetNode().scrollTop=$0;
},
getOffsetLeft:function(){this._visualPropertyCheck();
return qx.html.Offset.getLeft(this.getElement());
},
getOffsetTop:function(){this._visualPropertyCheck();
return qx.html.Offset.getTop(this.getElement());
},
getScrollLeft:function(){this._visualPropertyCheck();
return this._getTargetNode().scrollLeft;
},
getScrollTop:function(){this._visualPropertyCheck();
return this._getTargetNode().scrollTop;
},
getClientWidth:function(){this._visualPropertyCheck();
return this._getTargetNode().clientWidth;
},
getClientHeight:function(){this._visualPropertyCheck();
return this._getTargetNode().clientHeight;
},
getOffsetWidth:function(){this._visualPropertyCheck();
return this.getElement().offsetWidth;
},
getOffsetHeight:function(){this._visualPropertyCheck();
return this.getElement().offsetHeight;
},
getScrollWidth:function(){this._visualPropertyCheck();
return this._getTargetNode().scrollWidth;
},
getScrollHeight:function(){this._visualPropertyCheck();
return this._getTargetNode().scrollHeight;
},
scrollIntoView:function($0){this.scrollIntoViewX($0);
this.scrollIntoViewY($0);
},
scrollIntoViewX:function($0){if(!this._isCreated||!this._isDisplayable){this.warn("The function scrollIntoViewX can only be called after the widget is created!");
return false;
}return qx.html.ScrollIntoView.scrollX(this.getElement(),
$0);
},
scrollIntoViewY:function($0){if(!this._isCreated||!this._isDisplayable){this.warn("The function scrollIntoViewY can only be called after the widget is created!");
return false;
}return qx.html.ScrollIntoView.scrollY(this.getElement(),
$0);
},
supportsDrop:function($0){var $1=this.getSupportsDropMethod();
if($1!==null){return $1.call(this,
$0);
}return (this!=$0.sourceWidget);
}},
settings:{"qx.widgetQueueDebugging":false,
"qx.widgetDebugId":false},
defer:function($0,
$1){$0.__initApplyMethods($1);
if(qx.core.Variant.isSet("qx.client",
"mshtml")){$1._renderRuntimeWidth=function($2){this._style.pixelWidth=($2==null)?0:$2;
if(this._innerStyle){this._innerStyle.pixelWidth=($2==null)?0:$2-2;
}};
$1._renderRuntimeHeight=function($2){this._style.pixelHeight=($2==null)?0:$2;
if(this._innerStyle){this._innerStyle.pixelHeight=($2==null)?0:$2-2;
}};
$1._resetRuntimeWidth=function(){this._style.width="";
if(this._innerStyle){this._innerStyle.width="";
}};
$1._resetRuntimeHeight=function(){this._style.height="";
if(this._innerStyle){this._innerStyle.height="";
}};
}$0.__initLayoutProperties($0);
{};
},
destruct:function(){var $0=this.getElement();
if($0){$0.qx_Widget=null;
}this._disposeFields("_isCreated",
"_inlineEvents",
"_element",
"_style",
"_borderElement",
"_innerStyle",
"_oldParent",
"_styleProperties",
"_htmlProperties",
"_htmlAttributes",
"__states",
"_jobQueue",
"_layoutChanges",
"__borderObject");
}});




/* ID: qx.html.Dimension */
qx.Class.define("qx.html.Dimension",
{statics:{getOuterWidth:function($0){return qx.html.Dimension.getBoxWidth($0)+qx.html.Style.getMarginLeft($0)+qx.html.Style.getMarginRight($0);
},
getOuterHeight:function($0){return qx.html.Dimension.getBoxHeight($0)+qx.html.Style.getMarginTop($0)+qx.html.Style.getMarginBottom($0);
},
getBoxWidthForZeroHeight:function($0){var $1=$0.offsetHeight;
if($1==0){var $2=$0.style.height;
$0.style.height="1px";
}var $3=$0.offsetWidth;
if($1==0){$0.style.height=$2;
}return $3;
},
getBoxHeightForZeroWidth:function($0){var $1=$0.offsetWidth;
if($1==0){var $2=$0.style.width;
$0.style.width="1px";
}var $3=$0.offsetHeight;
if($1==0){$0.style.width=$2;
}return $3;
},
getBoxWidth:function($0){return $0.offsetWidth;
},
getBoxHeight:function($0){return $0.offsetHeight;
},
getAreaWidth:qx.core.Variant.select("qx.client",
{"gecko":function($0){if($0.clientWidth!=0&&$0.clientWidth!=(qx.html.Style.getBorderLeft($0)+qx.html.Style.getBorderRight($0))){return $0.clientWidth;
}else{return qx.html.Dimension.getBoxWidth($0)-qx.html.Dimension.getInsetLeft($0)-qx.html.Dimension.getInsetRight($0);
}},
"default":function($0){return $0.clientWidth!=0?$0.clientWidth:(qx.html.Dimension.getBoxWidth($0)-qx.html.Dimension.getInsetLeft($0)-qx.html.Dimension.getInsetRight($0));
}}),
getAreaHeight:qx.core.Variant.select("qx.client",
{"gecko":function($0){if($0.clientHeight!=0&&$0.clientHeight!=(qx.html.Style.getBorderTop($0)+qx.html.Style.getBorderBottom($0))){return $0.clientHeight;
}else{return qx.html.Dimension.getBoxHeight($0)-qx.html.Dimension.getInsetTop($0)-qx.html.Dimension.getInsetBottom($0);
}},
"default":function($0){return $0.clientHeight!=0?$0.clientHeight:(qx.html.Dimension.getBoxHeight($0)-qx.html.Dimension.getInsetTop($0)-qx.html.Dimension.getInsetBottom($0));
}}),
getInnerWidth:function($0){return qx.html.Dimension.getAreaWidth($0)-qx.html.Style.getPaddingLeft($0)-qx.html.Style.getPaddingRight($0);
},
getInnerHeight:function($0){return qx.html.Dimension.getAreaHeight($0)-qx.html.Style.getPaddingTop($0)-qx.html.Style.getPaddingBottom($0);
},
getInsetLeft:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.clientLeft;
},
"default":function($0){return qx.html.Style.getBorderLeft($0);
}}),
getInsetTop:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.clientTop;
},
"default":function($0){return qx.html.Style.getBorderTop($0);
}}),
getInsetRight:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(qx.html.Style.getStyleProperty($0,
"overflowY")=="hidden"||$0.clientWidth==0){return qx.html.Style.getBorderRight($0);
}return Math.max(0,
$0.offsetWidth-$0.clientLeft-$0.clientWidth);
},
"default":function($0){if($0.clientWidth==0){var $1=qx.html.Style.getStyleProperty($0,
"overflow");
var $2=$1=="scroll"||$1=="-moz-scrollbars-vertical"?16:0;
return Math.max(0,
qx.html.Style.getBorderRight($0)+$2);
}return Math.max(0,
$0.offsetWidth-$0.clientWidth-qx.html.Style.getBorderLeft($0));
}}),
getInsetBottom:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(qx.html.Style.getStyleProperty($0,
"overflowX")=="hidden"||$0.clientHeight==0){return qx.html.Style.getBorderBottom($0);
}return Math.max(0,
$0.offsetHeight-$0.clientTop-$0.clientHeight);
},
"default":function($0){if($0.clientHeight==0){var $1=qx.html.Style.getStyleProperty($0,
"overflow");
var $2=$1=="scroll"||$1=="-moz-scrollbars-horizontal"?16:0;
return Math.max(0,
qx.html.Style.getBorderBottom($0)+$2);
}return Math.max(0,
$0.offsetHeight-$0.clientHeight-qx.html.Style.getBorderTop($0));
}}),
getScrollBarSizeLeft:function($0){return 0;
},
getScrollBarSizeTop:function($0){return 0;
},
getScrollBarSizeRight:function($0){return qx.html.Dimension.getInsetRight($0)-qx.html.Style.getBorderRight($0);
},
getScrollBarSizeBottom:function($0){return qx.html.Dimension.getInsetBottom($0)-qx.html.Style.getBorderBottom($0);
},
getScrollBarVisibleX:function($0){return qx.html.Dimension.getScrollBarSizeRight($0)>0;
},
getScrollBarVisibleY:function($0){return qx.html.Dimension.getScrollBarSizeBottom($0)>0;
}}});




/* ID: qx.html.Style */
qx.Class.define("qx.html.Style",
{statics:{getStylePropertySure:qx.lang.Object.select((document.defaultView&&document.defaultView.getComputedStyle)?"hasComputed":"noComputed",
{"hasComputed":function($0,
$1){return !$0?null:$0.ownerDocument?$0.ownerDocument.defaultView.getComputedStyle($0,
"")[$1]:$0.style[$1];
},
"noComputed":qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){try{if(!$0){return null;
}
if($0.parentNode&&$0.currentStyle){return $0.currentStyle[$1];
}else{var $2=$0.runtimeStyle[$1];
if($2!=null&&typeof $2!="undefined"&&$2!=""){return $2;
}return $0.style[$1];
}}catch(ex){throw new Error("Could not evaluate computed style: "+$0+"["+$1+"]: "+ex);
}},
"default":function($0,
$1){return !$0?null:$0.style[$1];
}})}),
getStyleProperty:qx.lang.Object.select((document.defaultView&&document.defaultView.getComputedStyle)?"hasComputed":"noComputed",
{"hasComputed":function($0,
$1){try{return $0.ownerDocument.defaultView.getComputedStyle($0,
"")[$1];
}catch(ex){throw new Error("Could not evaluate computed style: "+$0+"["+$1+"]: "+ex);
}},
"noComputed":qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){try{return $0.currentStyle[$1];
}catch(ex){throw new Error("Could not evaluate computed style: "+$0+"["+$1+"]: "+ex);
}},
"default":function($0,
$1){try{return $0.style[$1];
}catch(ex){throw new Error("Could not evaluate computed style: "+$0+"["+$1+"]");
}}})}),
getStyleSize:function($0,
$1){return parseInt(qx.html.Style.getStyleProperty($0,
$1))||0;
},
getMarginLeft:function($0){return qx.html.Style.getStyleSize($0,
"marginLeft");
},
getMarginTop:function($0){return qx.html.Style.getStyleSize($0,
"marginTop");
},
getMarginRight:function($0){return qx.html.Style.getStyleSize($0,
"marginRight");
},
getMarginBottom:function($0){return qx.html.Style.getStyleSize($0,
"marginBottom");
},
getPaddingLeft:function($0){return qx.html.Style.getStyleSize($0,
"paddingLeft");
},
getPaddingTop:function($0){return qx.html.Style.getStyleSize($0,
"paddingTop");
},
getPaddingRight:function($0){return qx.html.Style.getStyleSize($0,
"paddingRight");
},
getPaddingBottom:function($0){return qx.html.Style.getStyleSize($0,
"paddingBottom");
},
getBorderLeft:function($0){return qx.html.Style.getStyleProperty($0,
"borderLeftStyle")=="none"?0:qx.html.Style.getStyleSize($0,
"borderLeftWidth");
},
getBorderTop:function($0){return qx.html.Style.getStyleProperty($0,
"borderTopStyle")=="none"?0:qx.html.Style.getStyleSize($0,
"borderTopWidth");
},
getBorderRight:function($0){return qx.html.Style.getStyleProperty($0,
"borderRightStyle")=="none"?0:qx.html.Style.getStyleSize($0,
"borderRightWidth");
},
getBorderBottom:function($0){return qx.html.Style.getStyleProperty($0,
"borderBottomStyle")=="none"?0:qx.html.Style.getStyleSize($0,
"borderBottomWidth");
}}});




/* ID: qx.html.StyleSheet */
qx.Class.define("qx.html.StyleSheet",
{statics:{includeFile:function($0){var $1=document.createElement("link");
$1.type="text/css";
$1.rel="stylesheet";
$1.href=$0;
var $2=document.getElementsByTagName("head")[0];
$2.appendChild($1);
},
createElement:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0){var $1=document.createStyleSheet();
if($0){$1.cssText=$0;
}return $1;
},
"other":function($0){var $1=document.createElement("style");
$1.type="text/css";
$1.appendChild(document.createTextNode($0||"body {}"));
document.getElementsByTagName("head")[0].appendChild($1);
if($1.sheet){return $1.sheet;
}else{var $2=document.styleSheets;
for(var $3=$2.length-1;$3>=0;$3--){if($2[$3].ownerNode==$1){return $2[$3];
}}}throw "Error: Could not get a reference to the sheet object";
}}),
addRule:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0,
$1,
$2){$0.addRule($1,
$2);
},
"other":qx.lang.Object.select(qx.core.Client.getInstance().isSafari2()?"safari2":"other",
{"safari2+":function($0,
$1,
$2){if(!$0._qxRules){$0._qxRules={};
}
if(!$0._qxRules[$1]){var $3=document.createTextNode($1+"{"+$2+"}");
$0.ownerNode.appendChild($3);
$0._qxRules[$1]=$3;
}},
"other":function($0,
$1,
$2){$0.insertRule($1+"{"+$2+"}",
$0.cssRules.length);
}})}),
removeRule:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0,
$1){var $2=$0.rules;
var $3=$2.length;
for(var $4=$3-1;$4>=0;$4--){if($2[$4].selectorText==$1){$0.removeRule($4);
}}},
"other":qx.lang.Object.select(qx.core.Client.getInstance().isSafari2()?"safari2":"other",
{"safari2+":function($0,
$1){var $2=function(){qx.log.Logger.ROOT_LOGGER.warn("In Safari/Webkit you can only remove rules that are created using qx.html.StyleSheet.addRule");
};
if(!$0._qxRules){$2();
}var $3=$0._qxRules[$1];
if($3){$0.ownerNode.removeChild($3);
$0._qxRules[$1]=null;
}else{$2();
}},
"other":function($0,
$1){var $2=$0.cssRules;
var $3=$2.length;
for(var $4=$3-1;$4>=0;$4--){if($2[$4].selectorText==$1){$0.deleteRule($4);
}}}})}),
removeAllRules:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0){var $1=$0.rules;
var $2=$1.length;
for(var $3=$2-1;$3>=0;$3--){$0.removeRule($3);
}},
"other":qx.lang.Object.select(qx.core.Client.getInstance().isSafari2()?"safari2":"other",
{"safari2+":function($0){var $1=$0.ownerNode;
var $2=$1.childNodes;
while($2.length>0){$1.removeChild($2[0]);
}},
"other":function($0){var $1=$0.cssRules;
var $2=$1.length;
for(var $3=$2-1;$3>=0;$3--){$0.deleteRule($3);
}}})}),
addImport:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0,
$1){$0.addImport($1);
},
"other":qx.lang.Object.select(qx.core.Client.getInstance().isSafari2()?"safari2":"other",
{"safari2+":function($0,
$1){$0.ownerNode.appendChild(document.createTextNode('@import "'+$1+'";'));
},
"other":function($0,
$1){$0.insertRule('@import "'+$1+'";',
$0.cssRules.length);
}})}),
removeImport:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0,
$1){var $2=$0.imports;
var $3=$2.length;
for(var $4=$3-1;$4>=0;$4--){if($2[$4].href==$1){$0.removeImport($4);
}}},
"other":function($0,
$1){var $2=$0.cssRules;
var $3=$2.length;
for(var $4=$3-1;$4>=0;$4--){if($2[$4].href==$1){$0.deleteRule($4);
}}}}),
removeAllImports:qx.lang.Object.select(document.createStyleSheet?"ie4+":"other",
{"ie4+":function($0){var $1=$0.imports;
var $2=$1.length;
for(var $3=$2-1;$3>=0;$3--){$0.removeImport($3);
}},
"other":function($0){var $1=$0.cssRules;
var $2=$1.length;
for(var $3=$2-1;$3>=0;$3--){if($1[$3].type==$1[$3].IMPORT_RULE){$0.deleteRule($3);
}}}})}});




/* ID: qx.ui.core.Parent */
qx.Class.define("qx.ui.core.Parent",
{extend:qx.ui.core.Widget,
type:"abstract",
construct:function(){arguments.callee.base.call(this);
this._children=[];
this._layoutImpl=this._createLayoutImpl();
},
properties:{focusHandler:{check:"qx.event.handler.FocusHandler",
apply:"_applyFocusHandler",
nullable:true},
activeChild:{check:"qx.ui.core.Widget",
apply:"_applyActiveChild",
event:"changeActiveChild",
nullable:true},
focusedChild:{check:"qx.ui.core.Widget",
apply:"_applyFocusedChild",
event:"changeFocusedChild",
nullable:true},
visibleChildren:{_cached:true,
defaultValue:null}},
members:{isFocusRoot:function(){return this.getFocusHandler()!=null;
},
getFocusRoot:function(){if(this.isFocusRoot()){return this;
}
if(this._hasParent){return this.getParent().getFocusRoot();
}return null;
},
activateFocusRoot:function(){if(this._focusHandler){return;
}this._focusHandler=new qx.event.handler.FocusHandler(this);
this.setFocusHandler(this._focusHandler);
},
_onfocuskeyevent:function($0){this.getFocusHandler()._onkeyevent(this,
$0);
},
_applyFocusHandler:function($0,
$1){if($0){this.addEventListener("keypress",
this._onfocuskeyevent);
if(this.getTabIndex()<1){this.setTabIndex(1);
}this.setHideFocus(true);
this.setActiveChild(this);
}else{this.removeEventListener("keydown",
this._onfocuskeyevent);
this.removeEventListener("keypress",
this._onfocuskeyevent);
this.setTabIndex(-1);
this.setHideFocus(false);
}},
_applyActiveChild:function($0,
$1){},
_applyFocusedChild:function($0,
$1){var $2=$0!=null;
var $3=$1!=null;
if(qx.Class.isDefined("qx.ui.popup.PopupManager")&&$2){var $4=qx.ui.popup.PopupManager.getInstance();
if($4){$4.update($0);
}}
if($3){if($1.hasEventListeners("focusout")){var $5=new qx.event.type.FocusEvent("focusout",
$1);
if($2){$5.setRelatedTarget($0);
}$1.dispatchEvent($5);
$5.dispose();
}}
if($2){if($0.hasEventListeners("focusin")){var $5=new qx.event.type.FocusEvent("focusin",
$0);
if($3){$5.setRelatedTarget($1);
}$0.dispatchEvent($5);
$5.dispose();
}}
if($3){if(this.getActiveChild()==$1&&!$2){this.setActiveChild(null);
}$1.setFocused(false);
var $5=new qx.event.type.FocusEvent("blur",
$1);
if($2){$5.setRelatedTarget($0);
}$1.dispatchEvent($5);
if(qx.Class.isDefined("qx.ui.popup.ToolTipManager")){var $4=qx.ui.popup.ToolTipManager.getInstance();
if($4){$4.handleBlur($5);
}}$5.dispose();
}
if($2){this.setActiveChild($0);
$0.setFocused(true);
qx.event.handler.EventHandler.getInstance().setFocusRoot(this);
var $5=new qx.event.type.FocusEvent("focus",
$0);
if($3){$5.setRelatedTarget($1);
}$0.dispatchEvent($5);
if(qx.Class.isDefined("qx.ui.popup.ToolTipManager")){var $4=qx.ui.popup.ToolTipManager.getInstance();
if($4){$4.handleFocus($5);
}}$5.dispose();
}},
_layoutImpl:null,
_createLayoutImpl:function(){return null;
},
getLayoutImpl:function(){return this._layoutImpl;
},
getChildren:function(){return this._children;
},
getChildrenLength:function(){return this.getChildren().length;
},
hasChildren:function(){return this.getChildrenLength()>0;
},
isEmpty:function(){return this.getChildrenLength()==0;
},
indexOf:function($0){return this.getChildren().indexOf($0);
},
contains:function($0){switch($0){case null:return false;
case this:return true;
default:return this.contains($0.getParent());
}},
_computeVisibleChildren:function(){var $0=[];
var $1=this.getChildren();
if(!$1){return 0;
}var $2=$1.length;
for(var $3=0;$3<$2;$3++){var $4=$1[$3];
if($4._isDisplayable){$0.push($4);
}}return $0;
},
getVisibleChildrenLength:function(){return this.getVisibleChildren().length;
},
hasVisibleChildren:function(){return this.getVisibleChildrenLength()>0;
},
isVisibleEmpty:function(){return this.getVisibleChildrenLength()==0;
},
add:function($0){var $1;
for(var $2=0,
$3=arguments.length;$2<$3;$2++){$1=arguments[$2];
if(!($1 instanceof qx.ui.core.Parent)&&!($1 instanceof qx.ui.basic.Terminator)){throw new Error("Invalid Widget: "+$1);
}else{$1.setParent(this);
}}return this;
},
addAt:function($0,
$1){if($1==null||$1<0){throw new Error("Not a valid index for addAt(): "+$1);
}
if($0.getParent()==this){var $2=this.getChildren();
var $3=$2.indexOf($0);
if($3!=$1){if($3!=-1){qx.lang.Array.removeAt($2,
$3);
}qx.lang.Array.insertAt($2,
$0,
$1);
if(this._initialLayoutDone){this._invalidateVisibleChildren();
this.getLayoutImpl().updateChildrenOnMoveChild($0,
$1,
$3);
}}}else{$0._insertIndex=$1;
$0.setParent(this);
}},
addAtBegin:function($0){return this.addAt($0,
0);
},
addAtEnd:function($0){var $1=this.getChildrenLength();
return this.addAt($0,
$0.getParent()==this?$1-1:$1);
},
addBefore:function($0,
$1){var $2=this.getChildren();
var $3=$2.indexOf($1);
if($3==-1){throw new Error("Child to add before: "+$1+" is not inside this parent.");
}var $4=$2.indexOf($0);
if($4==-1||$4>$3){$3++;
}return this.addAt($0,
Math.max(0,
$3-1));
},
addAfter:function($0,
$1){var $2=this.getChildren();
var $3=$2.indexOf($1);
if($3==-1){throw new Error("Child to add after: "+$1+" is not inside this parent.");
}var $4=$2.indexOf($0);
if($4!=-1&&$4<$3){$3--;
}return this.addAt($0,
Math.min($2.length,
$3+1));
},
remove:function($0){var $1;
for(var $2=0,
$3=arguments.length;$2<$3;$2++){$1=arguments[$2];
if(!($1 instanceof qx.ui.core.Parent)&&!($1 instanceof qx.ui.basic.Terminator)){throw new Error("Invalid Widget: "+$1);
}else if($1.getParent()==this){$1.setParent(null);
}}},
removeAt:function($0){var $1=this.getChildren()[$0];
if($1){delete $1._insertIndex;
$1.setParent(null);
}},
removeAll:function(){var $0=this.getChildren();
var $1=$0[0];
while($1){this.remove($1);
$1=$0[0];
}},
getFirstChild:function(){return qx.lang.Array.getFirst(this.getChildren())||null;
},
getFirstVisibleChild:function(){return qx.lang.Array.getFirst(this.getVisibleChildren())||null;
},
getFirstActiveChild:function($0){return qx.ui.core.Widget.getActiveSiblingHelper(null,
this,
1,
$0,
"first")||null;
},
getLastChild:function(){return qx.lang.Array.getLast(this.getChildren())||null;
},
getLastVisibleChild:function(){return qx.lang.Array.getLast(this.getVisibleChildren())||null;
},
getLastActiveChild:function($0){return qx.ui.core.Widget.getActiveSiblingHelper(null,
this,
-1,
$0,
"last")||null;
},
forEachChild:function($0){var $1=this.getChildren(),
$2,
$3=-1;
if(!$1){return;
}
while($2=$1[++$3]){$0.call($2,
$3);
}},
forEachVisibleChild:function($0){var $1=this.getVisibleChildren(),
$2,
$3=-1;
if(!$1){return;
}
while($2=$1[++$3]){$0.call($2,
$3);
}},
_beforeAppear:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._beforeAppear();
}});
},
_afterAppear:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._afterAppear();
}});
},
_beforeDisappear:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._beforeDisappear();
}});
},
_afterDisappear:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._afterDisappear();
}});
},
_beforeInsertDom:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._beforeInsertDom();
}});
},
_afterInsertDom:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._afterInsertDom();
}});
},
_beforeRemoveDom:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._beforeRemoveDom();
}});
},
_afterRemoveDom:function(){arguments.callee.base.call(this);
this.forEachVisibleChild(function(){if(this.isAppearRelevant()){this._afterRemoveDom();
}});
},
_handleDisplayableCustom:function($0,
$1,
$2){this.forEachChild(function(){this._handleDisplayable();
});
},
_addChildrenToStateQueue:function(){this.forEachVisibleChild(function(){this.addToStateQueue();
});
},
recursiveAddToStateQueue:function(){this.addToStateQueue();
this.forEachVisibleChild(function(){this.recursiveAddToStateQueue();
});
},
_recursiveAppearanceThemeUpdate:function($0,
$1){arguments.callee.base.call(this,
$0,
$1);
this.forEachVisibleChild(function(){this._recursiveAppearanceThemeUpdate($0,
$1);
});
},
_addChildToChildrenQueue:function($0){if(!$0._isInParentChildrenQueue&&!$0._isDisplayable){this.warn("Ignoring invisible child: "+$0);
}
if(!$0._isInParentChildrenQueue&&$0._isDisplayable){qx.ui.core.Widget.addToGlobalLayoutQueue(this);
if(!this._childrenQueue){this._childrenQueue={};
}this._childrenQueue[$0.toHashCode()]=$0;
}},
_removeChildFromChildrenQueue:function($0){if(this._childrenQueue&&$0._isInParentChildrenQueue){delete this._childrenQueue[$0.toHashCode()];
if(qx.lang.Object.isEmpty(this._childrenQueue)){qx.ui.core.Widget.removeFromGlobalLayoutQueue(this);
}}},
_flushChildrenQueue:function(){if(!qx.lang.Object.isEmpty(this._childrenQueue)){this.getLayoutImpl().flushChildrenQueue(this._childrenQueue);
delete this._childrenQueue;
}},
_addChildrenToLayoutQueue:function($0){this.forEachChild(function(){this.addToLayoutChanges($0);
});
},
_layoutChild:function($0){if(!$0._isDisplayable){return ;
}var $1=$0._layoutChanges;
try{if($0.renderBorder){if($1.borderTop||$1.borderRight||$1.borderBottom||$1.borderLeft){$0.renderBorder($1);
}}}catch(ex){this.error("Could not apply border to child "+$0,
ex);
}
try{if($0.renderPadding){if($1.paddingLeft||$1.paddingRight||$1.paddingTop||$1.paddingBottom){$0.renderPadding($1);
}}}catch(ex){this.error("Could not apply padding to child "+$0,
ex);
}try{this.getLayoutImpl().layoutChild($0,
$1);
}catch(ex){this.error("Could not layout child "+$0+" through layout handler",
ex);
}try{$0._layoutPost($1);
}catch(ex){this.error("Could not post layout child "+$0,
ex);
}try{if($1.initial){$0._initialLayoutDone=true;
qx.ui.core.Widget.addToGlobalDisplayQueue($0);
}}catch(ex){this.error("Could not handle display updates from layout flush for child "+$0,
ex);
}$0._layoutChanges={};
delete $0._isInParentLayoutQueue;
delete this._childrenQueue[$0.toHashCode()];
},
_layoutPost:qx.lang.Function.returnTrue,
_computePreferredInnerWidth:function(){return this.getLayoutImpl().computeChildrenNeededWidth();
},
_computePreferredInnerHeight:function(){return this.getLayoutImpl().computeChildrenNeededHeight();
},
_changeInnerWidth:function($0,
$1){var $2=this.getLayoutImpl();
if($2.invalidateChildrenFlexWidth){$2.invalidateChildrenFlexWidth();
}this.forEachVisibleChild(function(){if($2.updateChildOnInnerWidthChange(this)&&this._recomputeBoxWidth()){this._recomputeOuterWidth();
this._recomputeInnerWidth();
}});
},
_changeInnerHeight:function($0,
$1){var $2=this.getLayoutImpl();
if($2.invalidateChildrenFlexHeight){$2.invalidateChildrenFlexHeight();
}this.forEachVisibleChild(function(){if($2.updateChildOnInnerHeightChange(this)&&this._recomputeBoxHeight()){this._recomputeOuterHeight();
this._recomputeInnerHeight();
}});
},
getInnerWidthForChild:function($0){return this.getInnerWidth();
},
getInnerHeightForChild:function($0){return this.getInnerHeight();
},
_remappingChildTable:["add",
"remove",
"addAt",
"addAtBegin",
"addAtEnd",
"removeAt",
"addBefore",
"addAfter",
"removeAll"],
_remapStart:"return this._remappingChildTarget.",
_remapStop:".apply(this._remappingChildTarget, arguments)",
remapChildrenHandlingTo:function($0){var $1=this._remappingChildTable;
this._remappingChildTarget=$0;
for(var $2=0,
$3=$1.length,
$4;$2<$3;$2++){$4=$1[$2];
this[$4]=new Function(qx.ui.core.Parent.prototype._remapStart+$4+qx.ui.core.Parent.prototype._remapStop);
}}},
defer:function($0,
$1,
$2){if(qx.core.Variant.isSet("qx.client",
"opera")){$1._layoutChildOrig=$1._layoutChild;
$1._layoutChild=function($3){if(!$3._initialLayoutDone||!$3._layoutChanges.border){return this._layoutChildOrig($3);
}var $4=$3.getElement().style;
var $5=$4.display;
$4.display="none";
var $6=this._layoutChildOrig($3);
$4.display=$5;
return $6;
};
}},
destruct:function(){this._disposeObjectDeep("_children",
1);
this._disposeObjects("_layoutImpl",
"_focusHandler");
this._disposeFields("_childrenQueue",
"_childrenQueue",
"_remappingChildTable",
"_remappingChildTarget",
"_cachedVisibleChildren");
}});




/* ID: qx.event.type.FocusEvent */
qx.Class.define("qx.event.type.FocusEvent",
{extend:qx.event.type.Event,
construct:function($0,
$1){arguments.callee.base.call(this,
$0);
this.setTarget($1);
switch($0){case "focusin":case "focusout":this.setBubbles(true);
this.setPropagationStopped(false);
}}});




/* ID: qx.event.handler.EventHandler */
qx.Class.define("qx.event.handler.EventHandler",
{type:"singleton",
extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this.__onmouseevent=qx.lang.Function.bind(this._onmouseevent,
this);
this.__ondragevent=qx.lang.Function.bind(this._ondragevent,
this);
this.__onselectevent=qx.lang.Function.bind(this._onselectevent,
this);
this.__onwindowblur=qx.lang.Function.bind(this._onwindowblur,
this);
this.__onwindowfocus=qx.lang.Function.bind(this._onwindowfocus,
this);
this.__onwindowresize=qx.lang.Function.bind(this._onwindowresize,
this);
this._commands={};
},
events:{"error":"qx.event.type.DataEvent"},
statics:{mouseEventTypes:["mouseover",
"mousemove",
"mouseout",
"mousedown",
"mouseup",
"click",
"dblclick",
"contextmenu",
qx.core.Variant.isSet("qx.client",
"gecko")?"DOMMouseScroll":"mousewheel"],
keyEventTypes:["keydown",
"keypress",
"keyup"],
dragEventTypes:qx.core.Variant.select("qx.client",
{"gecko":["dragdrop",
"dragover",
"dragenter",
"dragexit",
"draggesture"],
"mshtml":["dragend",
"dragover",
"dragstart",
"drag",
"dragenter",
"dragleave"],
"default":["dragstart",
"dragdrop",
"dragover",
"drag",
"dragleave",
"dragenter",
"dragexit",
"draggesture"]}),
getDomTarget:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.target||$0.srcElement;
},
"webkit":function($0){var $1=$0.target||$0.srcElement;
if($1&&($1.nodeType==qx.dom.Node.TEXT)){$1=$1.parentNode;
}return $1;
},
"default":function($0){return $0.target;
}}),
stopDomEvent:function($0){if($0.preventDefault){$0.preventDefault();
}$0.returnValue=false;
},
getOriginalTargetObject:function($0){if($0==document.documentElement){$0=document.body;
}while($0!=null&&$0.qx_Widget==null){try{$0=$0.parentNode;
}catch(vDomEvent){$0=null;
}}return $0?$0.qx_Widget:null;
},
getOriginalTargetObjectFromEvent:function($0,
$1){var $2=qx.event.handler.EventHandler.getDomTarget($0);
if($1){var $3=$1.document;
if($2==$1||$2==$3||$2==$3.documentElement||$2==$3.body){return $3.body.qx_Widget;
}}return qx.event.handler.EventHandler.getOriginalTargetObject($2);
},
getRelatedOriginalTargetObjectFromEvent:function($0){return qx.event.handler.EventHandler.getOriginalTargetObject($0.relatedTarget||($0.type=="mouseover"?$0.fromElement:$0.toElement));
},
getTargetObject:function($0,
$1,
$2){if(!$1){var $1=qx.event.handler.EventHandler.getOriginalTargetObject($0);
if(!$1){return null;
}}while($1){if(!$2&&!$1.getEnabled()){return null;
}if(!$1.getAnonymous()){break;
}$1=$1.getParent();
}return $1;
},
getTargetObjectFromEvent:function($0){return qx.event.handler.EventHandler.getTargetObject(qx.event.handler.EventHandler.getDomTarget($0));
},
getRelatedTargetObjectFromEvent:function($0){var $1=$0.relatedTarget;
if(!$1){if($0.type=="mouseover"){$1=$0.fromElement;
}else{$1=$0.toElement;
}}return qx.event.handler.EventHandler.getTargetObject($1);
}},
properties:{allowClientContextMenu:{check:"Boolean",
init:false},
allowClientSelectAll:{check:"Boolean",
init:false},
captureWidget:{check:"qx.ui.core.Widget",
nullable:true,
apply:"_applyCaptureWidget"},
focusRoot:{check:"qx.ui.core.Parent",
nullable:true,
apply:"_applyFocusRoot"}},
members:{_lastMouseEventType:null,
_lastMouseDown:false,
_lastMouseEventDate:0,
_applyCaptureWidget:function($0,
$1){if($1){$1.setCapture(false);
}
if($0){$0.setCapture(true);
}},
_applyFocusRoot:function($0,
$1){if($1){$1.setFocusedChild(null);
}
if($0&&$0.getFocusedChild()==null){$0.setFocusedChild($0);
}},
addCommand:function($0){this._commands[$0.toHashCode()]=$0;
},
removeCommand:function($0){delete this._commands[$0.toHashCode()];
},
_checkKeyEventMatch:function($0){var $1;
for(var $2 in this._commands){$1=this._commands[$2];
if($1.getEnabled()&&$1.matchesKeyEvent($0)){if(!$1.execute($0.getTarget())){$0.preventDefault();
}break;
}}},
attachEvents:function(){this.attachEventTypes(qx.event.handler.EventHandler.mouseEventTypes,
this.__onmouseevent);
this.attachEventTypes(qx.event.handler.EventHandler.dragEventTypes,
this.__ondragevent);
qx.event.handler.KeyEventHandler.getInstance()._attachEvents();
qx.html.EventRegistration.addEventListener(window,
"blur",
this.__onwindowblur);
qx.html.EventRegistration.addEventListener(window,
"focus",
this.__onwindowfocus);
qx.html.EventRegistration.addEventListener(window,
"resize",
this.__onwindowresize);
document.body.onselect=document.onselectstart=document.onselectionchange=this.__onselectevent;
},
detachEvents:function(){this.detachEventTypes(qx.event.handler.EventHandler.mouseEventTypes,
this.__onmouseevent);
this.detachEventTypes(qx.event.handler.EventHandler.dragEventTypes,
this.__ondragevent);
qx.event.handler.KeyEventHandler.getInstance()._detachEvents();
qx.html.EventRegistration.removeEventListener(window,
"blur",
this.__onwindowblur);
qx.html.EventRegistration.removeEventListener(window,
"focus",
this.__onwindowfocus);
qx.html.EventRegistration.removeEventListener(window,
"resize",
this.__onwindowresize);
document.body.onselect=document.onselectstart=document.onselectionchange=null;
},
attachEventTypes:function($0,
$1){try{var $2=qx.core.Variant.isSet("qx.client",
"gecko")?window:document.body;
for(var $3=0,
$4=$0.length;$3<$4;$3++){qx.html.EventRegistration.addEventListener($2,
$0[$3],
$1);
}}catch(ex){throw new Error("qx.event.handler.EventHandler: Failed to attach window event types: "+$0+": "+ex);
}},
detachEventTypes:function($0,
$1){try{var $2=qx.core.Variant.isSet("qx.client",
"gecko")?window:document.body;
for(var $3=0,
$4=$0.length;$3<$4;$3++){qx.html.EventRegistration.removeEventListener($2,
$0[$3],
$1);
}}catch(ex){throw new Error("qx.event.handler.EventHandler: Failed to detach window event types: "+$0+": "+ex);
}},
_onkeyevent_post:function($0,
$1,
$2,
$3,
$4){var $5=qx.event.handler.EventHandler.getDomTarget($0);
var $6=this.getFocusRoot();
var $7=this.getCaptureWidget()||($6==null?null:$6.getActiveChild());
var $8=new qx.event.type.KeyEvent($1,
$0,
$5,
$7,
null,
$2,
$3,
$4);
if($1=="keydown"){this._checkKeyEventMatch($8);
}
if($7!=null&&$7.getEnabled()){switch($4){case "Escape":case "Tab":if(qx.Class.isDefined("qx.ui.menu.Manager")){qx.ui.menu.Manager.getInstance().update($7,
$1);
}break;
}if(!this.getAllowClientSelectAll()){if($0.ctrlKey&&$4=="A"){switch($5.tagName.toLowerCase()){case "input":case "textarea":case "iframe":break;
default:qx.event.handler.EventHandler.stopDomEvent($0);
}}}$7.dispatchEvent($8);
if(qx.Class.isDefined("qx.event.handler.DragAndDropHandler")){qx.event.handler.DragAndDropHandler.getInstance().handleKeyEvent($8);
}}$8.dispose();
},
_onmouseevent:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(!$0){$0=window.event;
}var $1=qx.event.handler.EventHandler.getDomTarget($0);
var $2=$0.type;
if($2=="mousemove"){if(this._mouseIsDown&&$0.button==0){this._onmouseevent_post($0,
"mouseup");
this._mouseIsDown=false;
}}else{if($2=="mousedown"){this._mouseIsDown=true;
}else if($2=="mouseup"){this._mouseIsDown=false;
}if($2=="mouseup"&&!this._lastMouseDown&&((new Date).valueOf()-this._lastMouseEventDate)<250){this._onmouseevent_post($0,
"mousedown");
}else if($2=="dblclick"&&this._lastMouseEventType=="mouseup"&&((new Date).valueOf()-this._lastMouseEventDate)<250){this._onmouseevent_post($0,
"click");
}
switch($2){case "mousedown":case "mouseup":case "click":case "dblclick":case "contextmenu":this._lastMouseEventType=$2;
this._lastMouseEventDate=(new Date).valueOf();
this._lastMouseDown=$2=="mousedown";
}}this._onmouseevent_post($0,
$2,
$1);
},
"default":function($0){var $1=qx.event.handler.EventHandler.getDomTarget($0);
var $2=$0.type;
switch($2){case "DOMMouseScroll":$2="mousewheel";
break;
case "click":case "dblclick":if($0.which!==1){return;
}}this._onmouseevent_post($0,
$2,
$1);
}}),
_onmouseevent_click_fix:qx.core.Variant.select("qx.client",
{"gecko":function($0,
$1,
$2){var $3=false;
switch($1){case "mousedown":this._lastMouseDownDomTarget=$0;
this._lastMouseDownDispatchTarget=$2;
break;
case "mouseup":if(this._lastMouseDownDispatchTarget===$2&&$0!==this._lastMouseDownDomTarget){$3=true;
}else{this._lastMouseDownDomTarget=null;
this._lastMouseDownDispatchTarget=null;
}}return $3;
},
"default":null}),
_onmouseevent_post:function($0,
$1,
$2){var $3,
$4,
$5,
$6,
$7,
$8,
$9,
$a;
$4=this.getCaptureWidget();
$7=qx.event.handler.EventHandler.getOriginalTargetObject($2);
if(!$4){$5=$6=qx.event.handler.EventHandler.getTargetObject(null,
$7,
true);
}else{$5=$4;
$6=qx.event.handler.EventHandler.getTargetObject(null,
$7,
true);
}if(!$6){return;
}$a=$6.getEnabled();
if(qx.core.Variant.isSet("qx.client",
"gecko")){$9=this._onmouseevent_click_fix($2,
$1,
$5);
}if($1=="contextmenu"&&!this.getAllowClientContextMenu()){qx.event.handler.EventHandler.stopDomEvent($0);
}if($a&&$1=="mousedown"){qx.event.handler.FocusHandler.mouseFocus=true;
var $b=$6.getFocusRoot();
if($b){this.setFocusRoot($b);
var $c=$6;
while(!$c.isFocusable()&&$c!=$b){$c=$c.getParent();
}$b.setFocusedChild($c);
$b.setActiveChild($6);
}}switch($1){case "mouseover":case "mouseout":$8=qx.event.handler.EventHandler.getRelatedTargetObjectFromEvent($0);
if($8==$6){return;
}}$3=new qx.event.type.MouseEvent($1,
$0,
$2,
$6,
$7,
$8);
qx.event.type.MouseEvent.storeEventState($3);
if($a){var $d=false;
$d=$5?$5.dispatchEvent($3):true;
this._onmouseevent_special_post($1,
$6,
$7,
$5,
$d,
$3,
$0);
}else{if($1=="mouseover"){if(qx.Class.isDefined("qx.ui.popup.ToolTipManager")){qx.ui.popup.ToolTipManager.getInstance().handleMouseOver($3);
}}}$3.dispose();
$3=null;
qx.ui.core.Widget.flushGlobalQueues();
if($9){this._onmouseevent_post($0,
"click",
this._lastMouseDownDomTarget);
this._lastMouseDownDomTarget=null;
this._lastMouseDownDispatchTarget=null;
}},
_onmouseevent_special_post:function($0,
$1,
$2,
$3,
$4,
$5,
$6){switch($0){case "mousedown":if(qx.Class.isDefined("qx.ui.popup.PopupManager")){qx.ui.popup.PopupManager.getInstance().update($1);
}
if(qx.Class.isDefined("qx.ui.menu.Manager")){qx.ui.menu.Manager.getInstance().update($1,
$0);
}
if(qx.Class.isDefined("qx.ui.embed.IframeManager")){qx.ui.embed.IframeManager.getInstance().handleMouseDown($5);
}break;
case "mouseup":if(qx.Class.isDefined("qx.ui.menu.Manager")){qx.ui.menu.Manager.getInstance().update($1,
$0);
}
if(qx.Class.isDefined("qx.ui.embed.IframeManager")){qx.ui.embed.IframeManager.getInstance().handleMouseUp($5);
}break;
case "mouseover":if(qx.Class.isDefined("qx.ui.popup.ToolTipManager")){qx.ui.popup.ToolTipManager.getInstance().handleMouseOver($5);
}break;
case "mouseout":if(qx.Class.isDefined("qx.ui.popup.ToolTipManager")){qx.ui.popup.ToolTipManager.getInstance().handleMouseOut($5);
}break;
}this._ignoreWindowBlur=$0==="mousedown";
if(qx.Class.isDefined("qx.event.handler.DragAndDropHandler")&&$1){qx.event.handler.DragAndDropHandler.getInstance().handleMouseEvent($5);
}},
_ondragevent:function($0){if(!$0){$0=window.event;
}qx.event.handler.EventHandler.stopDomEvent($0);
},
_onselectevent:function($0){if(!$0){$0=window.event;
}var $1=qx.event.handler.EventHandler.getOriginalTargetObjectFromEvent($0);
while($1){if($1.getSelectable()!=null){if(!$1.getSelectable()){qx.event.handler.EventHandler.stopDomEvent($0);
}break;
}$1=$1.getParent();
}},
_focused:false,
_onwindowblur:function($0){if(!this._focused||this._ignoreWindowBlur){return;
}this._focused=false;
this.setCaptureWidget(null);
if(qx.Class.isDefined("qx.ui.popup.PopupManager")){qx.ui.popup.PopupManager.getInstance().update();
}if(qx.Class.isDefined("qx.ui.menu.Manager")){qx.ui.menu.Manager.getInstance().update();
}if(qx.Class.isDefined("qx.event.handler.DragAndDropHandler")){qx.event.handler.DragAndDropHandler.getInstance().globalCancelDrag();
}qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowblur");
},
_onwindowfocus:function($0){if(this._focused){return;
}this._focused=true;
qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowfocus");
},
_onwindowresize:function($0){qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowresize");
}},
destruct:function(){this.detachEvents();
this._disposeObjectDeep("_commands",
1);
this._disposeFields("__onmouseevent",
"__ondragevent",
"__onselectevent",
"__onwindowblur",
"__onwindowfocus",
"__onwindowresize");
this._disposeFields("_lastMouseEventType",
"_lastMouseDown",
"_lastMouseEventDate",
"_lastMouseDownDomTarget",
"_lastMouseDownDispatchTarget");
}});




/* ID: qx.dom.Node */
qx.Class.define("qx.dom.Node",
{statics:{ELEMENT:1,
ATTRIBUTE:2,
TEXT:3,
CDATA_SECTION:4,
ENTITY_REFERENCE:5,
ENTITY:6,
PROCESSING_INSTRUCTION:7,
COMMENT:8,
DOCUMENT:9,
DOCUMENT_TYPE:10,
DOCUMENT_FRAGMENT:11,
NOTATION:12,
getDocument:function($0){if(this.isDocument($0)){return $0;
}return $0.ownerDocument||$0.document||null;
},
getWindow:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return this.getDocument($0).parentWindow;
},
"default":function($0){return this.getDocument($0).defaultView;
}}),
getDocumentElement:function($0){return this.getDocument($0).documentElement;
},
getBodyElement:function($0){return this.getDocument($0).body;
},
isElement:function($0){return !!($0&&$0.nodeType===qx.dom.Node.ELEMENT);
},
isDocument:function($0){return !!($0&&$0.nodeType===qx.dom.Node.DOCUMENT);
},
isText:function($0){return !!($0&&$0.nodeType===qx.dom.Node.TEXT);
},
isWindow:function($0){return $0.document&&this.getWindow($0.document)==$0;
},
getText:function($0){if(!$0||!$0.nodeType){return null;
}
switch($0.nodeType){case 1:var $1,
$2=[],
$3=$0.childNodes,
$4=$3.length;
for($1=0;$1<$4;$1++){$2[$1]=this.getText($3[$1]);
}return $2.join("");
case 2:return $0.nodeValue;
break;
case 3:return $0.nodeValue;
break;
}return null;
}}});




/* ID: qx.event.handler.KeyEventHandler */
qx.Class.define("qx.event.handler.KeyEventHandler",
{type:"singleton",
extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this.__onkeypress=qx.lang.Function.bind(this._onkeypress,
this);
this.__onkeyupdown=qx.lang.Function.bind(this._onkeyupdown,
this);
},
members:{_attachEvents:function(){var $0=qx.core.Variant.isSet("qx.client",
"gecko")?window:document.body;
qx.html.EventRegistration.addEventListener($0,
"keypress",
this.__onkeypress);
qx.html.EventRegistration.addEventListener($0,
"keyup",
this.__onkeyupdown);
qx.html.EventRegistration.addEventListener($0,
"keydown",
this.__onkeyupdown);
},
_detachEvents:function(){var $0=qx.core.Variant.isSet("qx.client",
"gecko")?window:document.body;
qx.html.EventRegistration.removeEventListener($0,
"keypress",
this.__onkeypress);
qx.html.EventRegistration.removeEventListener($0,
"keyup",
this.__onkeyupdown);
qx.html.EventRegistration.removeEventListener($0,
"keydown",
this.__onkeyupdown);
},
_onkeyupdown:qx.core.Variant.select("qx.client",
{"mshtml":function($0){$0=window.event||$0;
var $1=$0.keyCode;
var $2=0;
var $3=$0.type;
if(!(this._lastUpDownType[$1]=="keydown"&&$3=="keydown")){this._idealKeyHandler($1,
$2,
$3,
$0);
}if($3=="keydown"){if(this._isNonPrintableKeyCode($1)||
$1==
8||$1==9){this._idealKeyHandler($1,
$2,
"keypress",
$0);
}}this._lastUpDownType[$1]=$3;
},
"gecko":function($0){var $1=this._keyCodeFix[$0.keyCode]||$0.keyCode;
var $2=$0.charCode;
var $3=$0.type;
if(qx.core.Client.getInstance().runsOnWindows()){var $4=$1?this._keyCodeToIdentifier($1):this._charCodeToIdentifier($2);
if(!(this._lastUpDownType[$4]=="keypress"&&$3=="keydown")){this._idealKeyHandler($1,
$2,
$3,
$0);
}this._lastUpDownType[$4]=$3;
}else{this._idealKeyHandler($1,
$2,
$3,
$0);
}},
"webkit":function($0){var $1=0;
var $2=0;
var $3=$0.type;
if(qx.core.Client.getInstance().getVersion()<420){if(!this._lastCharCodeForType){this._lastCharCodeForType={};
}var $4=this._lastCharCodeForType[$3]>63000;
if($4){this._lastCharCodeForType[$3]=null;
return;
}this._lastCharCodeForType[$3]=$0.charCode;
}
if($3=="keyup"||$3=="keydown"){$1=this._charCode2KeyCode[$0.charCode]||$0.keyCode;
}else{if(this._charCode2KeyCode[$0.charCode]){$1=this._charCode2KeyCode[$0.charCode];
}else{$2=$0.charCode;
}}this._idealKeyHandler($1,
$2,
$3,
$0);
},
"opera":function($0){this._idealKeyHandler($0.keyCode,
0,
$0.type,
$0);
this._lastKeyCode=$0.keyCode;
},
"default":function(){throw new Error("Unsupported browser for key event handler!");
}}),
_onkeypress:qx.core.Variant.select("qx.client",
{"mshtml":function($0){var $0=window.event||$0;
if(this._charCode2KeyCode[$0.keyCode]){this._idealKeyHandler(this._charCode2KeyCode[$0.keyCode],
0,
$0.type,
$0);
}else{this._idealKeyHandler(0,
$0.keyCode,
$0.type,
$0);
}},
"gecko":function($0){var $1=this._keyCodeFix[$0.keyCode]||$0.keyCode;
var $2=$0.charCode;
var $3=$0.type;
if(qx.core.Client.getInstance().runsOnWindows()){var $4=$1?this._keyCodeToIdentifier($1):this._charCodeToIdentifier($2);
if(!(this._lastUpDownType[$4]=="keypress"&&$3=="keydown")){this._idealKeyHandler($1,
$2,
$3,
$0);
}this._lastUpDownType[$4]=$3;
}else{this._idealKeyHandler($1,
$2,
$3,
$0);
}},
"webkit":function($0){var $1=0;
var $2=0;
var $3=$0.type;
if(qx.core.Client.getInstance().getVersion()<420){if(!this._lastCharCodeForType){this._lastCharCodeForType={};
}var $4=this._lastCharCodeForType[$3]>63000;
if($4){this._lastCharCodeForType[$3]=null;
return;
}this._lastCharCodeForType[$3]=$0.charCode;
}
if($3=="keyup"||$3=="keydown"){$1=this._charCode2KeyCode[$0.charCode]||$0.keyCode;
}else{if(this._charCode2KeyCode[$0.charCode]){$1=this._charCode2KeyCode[$0.charCode];
}else{$2=$0.charCode;
}}this._idealKeyHandler($1,
$2,
$3,
$0);
},
"opera":function($0){var $1=$0.keyCode;
var $2=$0.type;
if($1!=this._lastKeyCode){this._idealKeyHandler(0,
this._lastKeyCode,
$2,
$0);
}else{if(this._keyCodeToIdentifierMap[$1]){this._idealKeyHandler($1,
0,
$2,
$0);
}else{this._idealKeyHandler(0,
$1,
$2,
$0);
}}this._lastKeyCode=$1;
},
"default":function(){throw new Error("Unsupported browser for key event handler!");
}}),
_specialCharCodeMap:{8:"Backspace",
9:"Tab",
13:"Enter",
27:"Escape",
32:"Space"},
_keyCodeToIdentifierMap:{16:"Shift",
17:"Control",
18:"Alt",
20:"CapsLock",
224:"Meta",
37:"Left",
38:"Up",
39:"Right",
40:"Down",
33:"PageUp",
34:"PageDown",
35:"End",
36:"Home",
45:"Insert",
46:"Delete",
112:"F1",
113:"F2",
114:"F3",
115:"F4",
116:"F5",
117:"F6",
118:"F7",
119:"F8",
120:"F9",
121:"F10",
122:"F11",
123:"F12",
144:"NumLock",
44:"PrintScreen",
145:"Scroll",
19:"Pause",
91:"Win",
93:"Apps"},
_numpadToCharCode:{96:"0".charCodeAt(0),
97:"1".charCodeAt(0),
98:"2".charCodeAt(0),
99:"3".charCodeAt(0),
100:"4".charCodeAt(0),
101:"5".charCodeAt(0),
102:"6".charCodeAt(0),
103:"7".charCodeAt(0),
104:"8".charCodeAt(0),
105:"9".charCodeAt(0),
106:"*".charCodeAt(0),
107:"+".charCodeAt(0),
109:"-".charCodeAt(0),
110:",".charCodeAt(0),
111:"/".charCodeAt(0)},
_charCodeA:"A".charCodeAt(0),
_charCodeZ:"Z".charCodeAt(0),
_charCode0:"0".charCodeAt(0),
_charCode9:"9".charCodeAt(0),
_isNonPrintableKeyCode:function($0){return this._keyCodeToIdentifierMap[$0]?true:false;
},
_isIdentifiableKeyCode:function($0){if($0>=this._charCodeA&&$0<=this._charCodeZ){return true;
}if($0>=this._charCode0&&$0<=this._charCode9){return true;
}if(this._specialCharCodeMap[$0]){return true;
}if(this._numpadToCharCode[$0]){return true;
}if(this._isNonPrintableKeyCode($0)){return true;
}return false;
},
isValidKeyIdentifier:function($0){if(this._identifierToKeyCodeMap[$0]){return true;
}
if($0.length!=1){return false;
}
if($0>="0"&&$0<="9"){return true;
}
if($0>="A"&&$0<="Z"){return true;
}
switch($0){case "+":case "-":case "*":case "/":return true;
default:return false;
}},
_keyCodeToIdentifier:function($0){if(this._isIdentifiableKeyCode($0)){var $1=this._numpadToCharCode[$0];
if($1){return String.fromCharCode($1);
}return (this._keyCodeToIdentifierMap[$0]||this._specialCharCodeMap[$0]||String.fromCharCode($0));
}else{return "Unidentified";
}},
_charCodeToIdentifier:function($0){return this._specialCharCodeMap[$0]||String.fromCharCode($0).toUpperCase();
},
_identifierToKeyCode:function($0){return this._identifierToKeyCodeMap[$0]||$0.charCodeAt(0);
},
_idealKeyHandler:function($0,
$1,
$2,
$3){if(!$0&&!$1){return;
}var $4;
if($0){$4=this._keyCodeToIdentifier($0);
qx.event.handler.EventHandler.getInstance()._onkeyevent_post($3,
$2,
$0,
$1,
$4);
}else{$4=this._charCodeToIdentifier($1);
qx.event.handler.EventHandler.getInstance()._onkeyevent_post($3,
"keypress",
$0,
$1,
$4);
qx.event.handler.EventHandler.getInstance()._onkeyevent_post($3,
"keyinput",
$0,
$1,
$4);
}}},
defer:function($0,
$1,
$2){if(!$1._identifierToKeyCodeMap){$1._identifierToKeyCodeMap={};
for(var $3 in $1._keyCodeToIdentifierMap){$1._identifierToKeyCodeMap[$1._keyCodeToIdentifierMap[$3]]=parseInt($3);
}
for(var $3 in $1._specialCharCodeMap){$1._identifierToKeyCodeMap[$1._specialCharCodeMap[$3]]=parseInt($3);
}}
if(qx.core.Variant.isSet("qx.client",
"mshtml")){$1._lastUpDownType={};
$1._charCode2KeyCode={13:13,
27:27};
}else if(qx.core.Variant.isSet("qx.client",
"gecko")){$1._lastUpDownType={};
$1._keyCodeFix={12:$1._identifierToKeyCode("NumLock")};
}else if(qx.core.Variant.isSet("qx.client",
"webkit")){$1._charCode2KeyCode={63289:$1._identifierToKeyCode("NumLock"),
63276:$1._identifierToKeyCode("PageUp"),
63277:$1._identifierToKeyCode("PageDown"),
63275:$1._identifierToKeyCode("End"),
63273:$1._identifierToKeyCode("Home"),
63234:$1._identifierToKeyCode("Left"),
63232:$1._identifierToKeyCode("Up"),
63235:$1._identifierToKeyCode("Right"),
63233:$1._identifierToKeyCode("Down"),
63272:$1._identifierToKeyCode("Delete"),
63302:$1._identifierToKeyCode("Insert"),
63236:$1._identifierToKeyCode("F1"),
63237:$1._identifierToKeyCode("F2"),
63238:$1._identifierToKeyCode("F3"),
63239:$1._identifierToKeyCode("F4"),
63240:$1._identifierToKeyCode("F5"),
63241:$1._identifierToKeyCode("F6"),
63242:$1._identifierToKeyCode("F7"),
63243:$1._identifierToKeyCode("F8"),
63244:$1._identifierToKeyCode("F9"),
63245:$1._identifierToKeyCode("F10"),
63246:$1._identifierToKeyCode("F11"),
63247:$1._identifierToKeyCode("F12"),
63248:$1._identifierToKeyCode("PrintScreen"),
3:$1._identifierToKeyCode("Enter"),
12:$1._identifierToKeyCode("NumLock"),
13:$1._identifierToKeyCode("Enter")};
}else if(qx.core.Variant.isSet("qx.client",
"opera")){$1._lastKeyCode=null;
}},
destruct:function(){this._detachEvents();
this._disposeFields("_lastUpDownType",
"_lastKeyCode");
}});




/* ID: qx.event.type.DomEvent */
qx.Class.define("qx.event.type.DomEvent",
{extend:qx.event.type.Event,
construct:function($0,
$1,
$2,
$3,
$4){arguments.callee.base.call(this,
$0);
this.setDomEvent($1);
this.setDomTarget($2);
this.setTarget($3);
this.setOriginalTarget($4);
},
statics:{SHIFT_MASK:1,
CTRL_MASK:2,
ALT_MASK:4,
META_MASK:8},
properties:{bubbles:{_fast:true,
defaultValue:true,
noCompute:true},
propagationStopped:{_fast:true,
defaultValue:false,
noCompute:true},
domEvent:{_fast:true,
setOnlyOnce:true,
noCompute:true},
domTarget:{_fast:true,
setOnlyOnce:true,
noCompute:true},
modifiers:{_cached:true,
defaultValue:null}},
members:{_computeModifiers:function(){var $0=0;
var $1=this.getDomEvent();
if($1.shiftKey)$0|=qx.event.type.DomEvent.SHIFT_MASK;
if($1.ctrlKey)$0|=qx.event.type.DomEvent.CTRL_MASK;
if($1.altKey)$0|=qx.event.type.DomEvent.ALT_MASK;
if($1.metaKey)$0|=qx.event.type.DomEvent.META_MASK;
return $0;
},
isCtrlPressed:function(){return this.getDomEvent().ctrlKey;
},
isShiftPressed:function(){return this.getDomEvent().shiftKey;
},
isAltPressed:function(){return this.getDomEvent().altKey;
},
isMetaPressed:function(){return this.getDomEvent().metaKey;
},
isCtrlOrCommandPressed:function(){if(qx.core.Client.getInstance().runsOnMacintosh()){return this.getDomEvent().metaKey;
}else{return this.getDomEvent().ctrlKey;
}},
setDefaultPrevented:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(!$0){return this.error("It is not possible to set preventDefault to false if it was true before!",
"setDefaultPrevented");
}this.getDomEvent().returnValue=false;
arguments.callee.base.call(this,
$0);
},
"default":function($0){if(!$0){return this.error("It is not possible to set preventDefault to false if it was true before!",
"setDefaultPrevented");
}this.getDomEvent().preventDefault();
this.getDomEvent().returnValue=false;
arguments.callee.base.call(this,
$0);
}})},
destruct:function(){this._disposeFields("_valueDomEvent",
"_valueDomTarget");
}});




/* ID: qx.event.type.KeyEvent */
qx.Class.define("qx.event.type.KeyEvent",
{extend:qx.event.type.DomEvent,
construct:function($0,
$1,
$2,
$3,
$4,
$5,
$6,
$7){arguments.callee.base.call(this,
$0,
$1,
$2,
$3,
$4);
this._keyCode=$5;
this.setCharCode($6);
this.setKeyIdentifier($7);
},
statics:{keys:{esc:27,
enter:13,
tab:9,
space:32,
up:38,
down:40,
left:37,
right:39,
shift:16,
ctrl:17,
alt:18,
f1:112,
f2:113,
f3:114,
f4:115,
f5:116,
f6:117,
f7:118,
f8:119,
f9:120,
f10:121,
f11:122,
f12:123,
print:124,
del:46,
backspace:8,
insert:45,
home:36,
end:35,
pageup:33,
pagedown:34,
numlock:144,
numpad_0:96,
numpad_1:97,
numpad_2:98,
numpad_3:99,
numpad_4:100,
numpad_5:101,
numpad_6:102,
numpad_7:103,
numpad_8:104,
numpad_9:105,
numpad_divide:111,
numpad_multiply:106,
numpad_minus:109,
numpad_plus:107},
codes:{}},
properties:{charCode:{_fast:true,
setOnlyOnce:true,
noCompute:true},
keyIdentifier:{_fast:true,
setOnlyOnce:true,
noCompute:true}},
members:{getKeyCode:function(){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"Please use getKeyIdentifier() instead.");
return this._keyCode;
}},
defer:function($0){for(var $1 in $0.keys){$0.codes[$0.keys[$1]]=$1;
}}});




/* ID: qx.event.type.MouseEvent */
qx.Class.define("qx.event.type.MouseEvent",
{extend:qx.event.type.DomEvent,
construct:function($0,
$1,
$2,
$3,
$4,
$5){arguments.callee.base.call(this,
$0,
$1,
$2,
$3,
$4);
if($5){this.setRelatedTarget($5);
}},
statics:{C_BUTTON_LEFT:"left",
C_BUTTON_MIDDLE:"middle",
C_BUTTON_RIGHT:"right",
C_BUTTON_NONE:"none",
_screenX:0,
_screenY:0,
_clientX:0,
_clientY:0,
_pageX:0,
_pageY:0,
_button:null,
buttons:qx.core.Variant.select("qx.client",
{"mshtml":{left:1,
right:2,
middle:4},
"default":{left:0,
right:2,
middle:1}}),
storeEventState:function($0){this._screenX=$0.getScreenX();
this._screenY=$0.getScreenY();
this._clientX=$0.getClientX();
this._clientY=$0.getClientY();
this._pageX=$0.getPageX();
this._pageY=$0.getPageY();
this._button=$0.getButton();
},
getScreenX:function(){return this._screenX;
},
getScreenY:function(){return this._screenY;
},
getClientX:function(){return this._clientX;
},
getClientY:function(){return this._clientY;
},
getPageX:function(){return this._pageX;
},
getPageY:function(){return this._pageY;
},
getButton:function(){return this._button;
}},
properties:{button:{_fast:true,
readOnly:true},
wheelDelta:{_fast:true,
readOnly:true}},
members:{getPageX:qx.core.Variant.select("qx.client",
{"mshtml":function(){return this.getDomEvent().clientX+qx.bom.Viewport.getScrollLeft(window);
},
"default":function(){return this.getDomEvent().pageX;
}}),
getPageY:qx.core.Variant.select("qx.client",
{"mshtml":function(){return this.getDomEvent().clientY+qx.bom.Viewport.getScrollTop(window);
},
"default":function(){return this.getDomEvent().pageY;
}}),
getClientX:function(){return this.getDomEvent().clientX;
},
getClientY:function(){return this.getDomEvent().clientY;
},
getScreenX:function(){return this.getDomEvent().screenX;
},
getScreenY:function(){return this.getDomEvent().screenY;
},
isLeftButtonPressed:qx.core.Variant.select("qx.client",
{"mshtml":function(){if(this.getType()=="click"){return true;
}else{return this.getButton()===qx.event.type.MouseEvent.C_BUTTON_LEFT;
}},
"default":function(){return this.getButton()===qx.event.type.MouseEvent.C_BUTTON_LEFT;
}}),
isMiddleButtonPressed:function(){return this.getButton()===qx.event.type.MouseEvent.C_BUTTON_MIDDLE;
},
isRightButtonPressed:function(){return this.getButton()===qx.event.type.MouseEvent.C_BUTTON_RIGHT;
},
__buttons:qx.core.Variant.select("qx.client",
{"mshtml":{1:"left",
2:"right",
4:"middle"},
"default":{0:"left",
2:"right",
1:"middle"}}),
_computeButton:function(){switch(this.getDomEvent().type){case "click":case "dblclick":return "left";
case "contextmenu":return "right";
default:return this.__buttons[this.getDomEvent().button]||"none";
}},
_computeWheelDelta:qx.core.Variant.select("qx.client",
{"default":function(){return this.getDomEvent().wheelDelta/120;
},
"gecko":function(){return -(this.getDomEvent().detail/3);
}})}});




/* ID: qx.util.manager.Object */
qx.Class.define("qx.util.manager.Object",
{extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this._objects={};
},
members:{add:function($0){if(this.getDisposed()){return;
}this._objects[$0.toHashCode()]=$0;
},
remove:function($0){if(this.getDisposed()){return false;
}delete this._objects[$0.toHashCode()];
},
has:function($0){return this._objects[$0.toHashCode()]!=null;
},
get:function($0){return this._objects[$0.toHashCode()];
},
getAll:function(){return this._objects;
},
enableAll:function(){for(var $0 in this._objects){this._objects[$0].setEnabled(true);
}},
disableAll:function(){for(var $0 in this._objects){this._objects[$0].setEnabled(false);
}}},
destruct:function(){this._disposeObjectDeep("_objects");
}});




/* ID: qx.ui.embed.IframeManager */
qx.Class.define("qx.ui.embed.IframeManager",
{type:"singleton",
extend:qx.util.manager.Object,
construct:function(){arguments.callee.base.call(this);
this._blocked={};
},
members:{handleMouseDown:function($0){var $1=this._blockData=qx.lang.Object.copy(this.getAll());
for(var $2 in $1){$1[$2].block();
}},
handleMouseUp:function($0){var $1=this._blockData;
for(var $2 in $1){$1[$2].release();
}}}});




/* ID: qx.ui.layout.CanvasLayout */
qx.Class.define("qx.ui.layout.CanvasLayout",
{extend:qx.ui.core.Parent,
construct:function(){arguments.callee.base.call(this);
},
members:{_createLayoutImpl:function(){return new qx.ui.layout.impl.CanvasLayoutImpl(this);
}}});




/* ID: qx.ui.layout.impl.LayoutImpl */
qx.Class.define("qx.ui.layout.impl.LayoutImpl",
{extend:qx.core.Object,
construct:function($0){arguments.callee.base.call(this);
this._widget=$0;
},
members:{getWidget:function(){return this._widget;
},
computeChildBoxWidth:function($0){return $0.getWidthValue()||$0._computeBoxWidthFallback();
},
computeChildBoxHeight:function($0){return $0.getHeightValue()||$0._computeBoxHeightFallback();
},
computeChildNeededWidth:function($0){var $1=$0._computedMinWidthTypePercent?null:$0.getMinWidthValue();
var $2=$0._computedMaxWidthTypePercent?null:$0.getMaxWidthValue();
var $3=($0._computedWidthTypePercent||$0._computedWidthTypeFlex?null:$0.getWidthValue())||$0.getPreferredBoxWidth()||0;
return qx.lang.Number.limit($3,
$1,
$2)+$0.getMarginLeft()+$0.getMarginRight();
},
computeChildNeededHeight:function($0){var $1=$0._computedMinHeightTypePercent?null:$0.getMinHeightValue();
var $2=$0._computedMaxHeightTypePercent?null:$0.getMaxHeightValue();
var $3=($0._computedHeightTypePercent||$0._computedHeightTypeFlex?null:$0.getHeightValue())||$0.getPreferredBoxHeight()||0;
return qx.lang.Number.limit($3,
$1,
$2)+$0.getMarginTop()+$0.getMarginBottom();
},
computeChildrenNeededWidth_max:function(){for(var $0=0,
$1=this.getWidget().getVisibleChildren(),
$2=$1.length,
$3=0;$0<$2;$0++){$3=Math.max($3,
$1[$0].getNeededWidth());
}return $3;
},
computeChildrenNeededHeight_max:function(){for(var $0=0,
$1=this.getWidget().getVisibleChildren(),
$2=$1.length,
$3=0;$0<$2;$0++){$3=Math.max($3,
$1[$0].getNeededHeight());
}return $3;
},
computeChildrenNeededWidth_sum:function(){for(var $0=0,
$1=this.getWidget().getVisibleChildren(),
$2=$1.length,
$3=0;$0<$2;$0++){$3+=$1[$0].getNeededWidth();
}return $3;
},
computeChildrenNeededHeight_sum:function(){for(var $0=0,
$1=this.getWidget().getVisibleChildren(),
$2=$1.length,
$3=0;$0<$2;$0++){$3+=$1[$0].getNeededHeight();
}return $3;
},
computeChildrenNeededWidth:null,
computeChildrenNeededHeight:null,
updateSelfOnChildOuterWidthChange:function($0){},
updateSelfOnChildOuterHeightChange:function($0){},
updateChildOnInnerWidthChange:function($0){},
updateChildOnInnerHeightChange:function($0){},
updateSelfOnJobQueueFlush:function($0){},
updateChildrenOnJobQueueFlush:function($0){},
updateChildrenOnAddChild:function($0,
$1){},
updateChildrenOnRemoveChild:function($0,
$1){},
updateChildrenOnMoveChild:function($0,
$1,
$2){},
flushChildrenQueue:function($0){var $1=this.getWidget();
for(var $2 in $0){$1._layoutChild($0[$2]);
}},
layoutChild:function($0,
$1){},
layoutChild_sizeLimitX:qx.core.Variant.select("qx.client",
{"mshtml":qx.lang.Function.returnTrue,
"default":function($0,
$1){if($1.minWidth){$0._computedMinWidthTypeNull?$0._resetRuntimeMinWidth():$0._renderRuntimeMinWidth($0.getMinWidthValue());
}else if($1.initial&&!$0._computedMinWidthTypeNull){$0._renderRuntimeMinWidth($0.getMinWidthValue());
}
if($1.maxWidth){$0._computedMaxWidthTypeNull?$0._resetRuntimeMaxWidth():$0._renderRuntimeMaxWidth($0.getMaxWidthValue());
}else if($1.initial&&!$0._computedMaxWidthTypeNull){$0._renderRuntimeMaxWidth($0.getMaxWidthValue());
}}}),
layoutChild_sizeLimitY:qx.core.Variant.select("qx.client",
{"mshtml":qx.lang.Function.returnTrue,
"default":function($0,
$1){if($1.minHeight){$0._computedMinHeightTypeNull?$0._resetRuntimeMinHeight():$0._renderRuntimeMinHeight($0.getMinHeightValue());
}else if($1.initial&&!$0._computedMinHeightTypeNull){$0._renderRuntimeMinHeight($0.getMinHeightValue());
}
if($1.maxHeight){$0._computedMaxHeightTypeNull?$0._resetRuntimeMaxHeight():$0._renderRuntimeMaxHeight($0.getMaxHeightValue());
}else if($1.initial&&!$0._computedMaxHeightTypeNull){$0._renderRuntimeMaxHeight($0.getMaxHeightValue());
}}}),
layoutChild_marginX:function($0,
$1){if($1.marginLeft||$1.initial){var $2=$0.getMarginLeft();
$2!=null?$0._renderRuntimeMarginLeft($2):$0._resetRuntimeMarginLeft();
}
if($1.marginRight||$1.initial){var $3=$0.getMarginRight();
$3!=null?$0._renderRuntimeMarginRight($3):$0._resetRuntimeMarginRight();
}},
layoutChild_marginY:function($0,
$1){if($1.marginTop||$1.initial){var $2=$0.getMarginTop();
$2!=null?$0._renderRuntimeMarginTop($2):$0._resetRuntimeMarginTop();
}
if($1.marginBottom||$1.initial){var $3=$0.getMarginBottom();
$3!=null?$0._renderRuntimeMarginBottom($3):$0._resetRuntimeMarginBottom();
}},
layoutChild_sizeX_essentialWrapper:function($0,
$1){return $0._isWidthEssential()?this.layoutChild_sizeX($0,
$1):$0._resetRuntimeWidth();
},
layoutChild_sizeY_essentialWrapper:function($0,
$1){return $0._isHeightEssential()?this.layoutChild_sizeY($0,
$1):$0._resetRuntimeHeight();
}},
defer:function($0,
$1){$1.computeChildrenNeededWidth=$1.computeChildrenNeededWidth_max;
$1.computeChildrenNeededHeight=$1.computeChildrenNeededHeight_max;
},
destruct:function(){this._disposeFields("_widget");
}});




/* ID: qx.lang.Number */
qx.Class.define("qx.lang.Number",
{statics:{isInRange:function($0,
$1,
$2){return $0>=$1&&$0<=$2;
},
isBetweenRange:function($0,
$1,
$2){return $0>$1&&$0<$2;
},
limit:function($0,
$1,
$2){if(typeof $2==="number"&&$0>$2){return $2;
}else if(typeof $1==="number"&&$0<$1){return $1;
}else{return $0;
}}}});




/* ID: qx.ui.layout.impl.CanvasLayoutImpl */
qx.Class.define("qx.ui.layout.impl.CanvasLayoutImpl",
{extend:qx.ui.layout.impl.LayoutImpl,
construct:function($0){arguments.callee.base.call(this,
$0);
},
members:{computeChildBoxWidth:function($0){var $1=null;
if($0._computedLeftTypeNull||$0._computedRightTypeNull){$1=$0.getWidthValue();
}else if($0._hasParent){$1=this.getWidget().getInnerWidth()-$0.getLeftValue()-$0.getRightValue();
}return $1||$0._computeBoxWidthFallback();
},
computeChildBoxHeight:function($0){var $1=null;
if($0._computedTopTypeNull||$0._computedBottomTypeNull){$1=$0.getHeightValue();
}else if($0._hasParent){$1=this.getWidget().getInnerHeight()-$0.getTopValue()-$0.getBottomValue();
}return $1||$0._computeBoxHeightFallback();
},
computeChildNeededWidth:function($0){var $1=$0._computedLeftTypePercent?null:$0.getLeftValue();
var $2=$0._computedRightTypePercent?null:$0.getRightValue();
var $3=$0._computedMinWidthTypePercent?null:$0.getMinWidthValue();
var $4=$0._computedMaxWidthTypePercent?null:$0.getMaxWidthValue();
if($1!=null&&$2!=null){var $5=$0.getPreferredBoxWidth()||0;
}else{var $5=($0._computedWidthTypePercent?null:$0.getWidthValue())||$0.getPreferredBoxWidth()||0;
}return qx.lang.Number.limit($5,
$3,
$4)+$1+$2+$0.getMarginLeft()+$0.getMarginRight();
},
computeChildNeededHeight:function($0){var $1=$0._computedTopTypePercent?null:$0.getTopValue();
var $2=$0._computedBottomTypePercent?null:$0.getBottomValue();
var $3=$0._computedMinHeightTypePercent?null:$0.getMinHeightValue();
var $4=$0._computedMaxHeightTypePercent?null:$0.getMaxHeightValue();
if($1!=null&&$2!=null){var $5=$0.getPreferredBoxHeight()||0;
}else{var $5=($0._computedHeightTypePercent?null:$0.getHeightValue())||$0.getPreferredBoxHeight()||0;
}return qx.lang.Number.limit($5,
$3,
$4)+$1+$2+$0.getMarginTop()+$0.getMarginBottom();
},
updateChildOnInnerWidthChange:function($0){var $1=$0._recomputePercentX();
var $2=$0._recomputeRangeX();
return $1||$2;
},
updateChildOnInnerHeightChange:function($0){var $1=$0._recomputePercentY();
var $2=$0._recomputeRangeY();
return $1||$2;
},
layoutChild:function($0,
$1){this.layoutChild_sizeX_essentialWrapper($0,
$1);
this.layoutChild_sizeY_essentialWrapper($0,
$1);
this.layoutChild_sizeLimitX($0,
$1);
this.layoutChild_sizeLimitY($0,
$1);
this.layoutChild_locationX($0,
$1);
this.layoutChild_locationY($0,
$1);
this.layoutChild_marginX($0,
$1);
this.layoutChild_marginY($0,
$1);
},
layoutChild_sizeX:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.width||$1.minWidth||$1.maxWidth||$1.left||$1.right){if($0._computedMinWidthTypeNull&&$0._computedWidthTypeNull&&$0._computedMaxWidthTypeNull&&!(!$0._computedLeftTypeNull&&!$0._computedRightTypeNull)){$0._resetRuntimeWidth();
}else{$0._renderRuntimeWidth($0.getBoxWidth());
}}},
"default":function($0,
$1){if($1.initial||$1.width){$0._computedWidthTypeNull?$0._resetRuntimeWidth():$0._renderRuntimeWidth($0.getWidthValue());
}}}),
layoutChild_sizeY:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.height||$1.minHeight||$1.maxHeight||$1.top||$1.bottom){if($0._computedMinHeightTypeNull&&$0._computedHeightTypeNull&&$0._computedMaxHeightTypeNull&&!(!$0._computedTopTypeNull&&!$0._computedBottomTypeNull)){$0._resetRuntimeHeight();
}else{$0._renderRuntimeHeight($0.getBoxHeight());
}}},
"default":function($0,
$1){if($1.initial||$1.height){$0._computedHeightTypeNull?$0._resetRuntimeHeight():$0._renderRuntimeHeight($0.getHeightValue());
}}}),
layoutChild_locationX:function($0,
$1){var $2=this.getWidget();
if($1.initial||$1.left||$1.parentPaddingLeft){$0._computedLeftTypeNull?$0._computedRightTypeNull&&$2.getPaddingLeft()>0?$0._renderRuntimeLeft($2.getPaddingLeft()):$0._resetRuntimeLeft():$0._renderRuntimeLeft($0.getLeftValue()+$2.getPaddingLeft());
}
if($1.initial||$1.right||$1.parentPaddingRight){$0._computedRightTypeNull?$0._computedLeftTypeNull&&$2.getPaddingRight()>0?$0._renderRuntimeRight($2.getPaddingRight()):$0._resetRuntimeRight():$0._renderRuntimeRight($0.getRightValue()+$2.getPaddingRight());
}},
layoutChild_locationY:function($0,
$1){var $2=this.getWidget();
if($1.initial||$1.top||$1.parentPaddingTop){$0._computedTopTypeNull?$0._computedBottomTypeNull&&$2.getPaddingTop()>0?$0._renderRuntimeTop($2.getPaddingTop()):$0._resetRuntimeTop():$0._renderRuntimeTop($0.getTopValue()+$2.getPaddingTop());
}
if($1.initial||$1.bottom||$1.parentPaddingBottom){$0._computedBottomTypeNull?$0._computedTopTypeNull&&$2.getPaddingBottom()>0?$0._renderRuntimeBottom($2.getPaddingBottom()):$0._resetRuntimeBottom():$0._renderRuntimeBottom($0.getBottomValue()+$2.getPaddingBottom());
}}}});




/* ID: qx.ui.core.ClientDocument */
qx.Class.define("qx.ui.core.ClientDocument",
{type:"singleton",
extend:qx.ui.layout.CanvasLayout,
construct:function(){arguments.callee.base.call(this);
this._window=window;
this._document=window.document;
this.setElement(this._document.body);
this._document.body.style.position="";
if(qx.core.Variant.isSet("qx.client",
"mshtml")&&(qx.core.Client.getInstance().getMajor()<7)){try{document.execCommand("BackgroundImageCache",
false,
true);
}catch(err){}}this._cachedInnerWidth=this._document.body.offsetWidth;
this._cachedInnerHeight=this._document.body.offsetHeight;
this.addEventListener("windowresize",
this._onwindowresize);
this._modalWidgets=[];
this._modalNativeWindow=null;
this.activateFocusRoot();
this.initHideFocus();
this.initSelectable();
qx.event.handler.EventHandler.getInstance().setFocusRoot(this);
},
events:{"focus":"qx.event.type.Event",
"windowblur":"qx.event.type.Event",
"windowfocus":"qx.event.type.Event",
"windowresize":"qx.event.type.Event"},
properties:{appearance:{refine:true,
init:"client-document"},
enableElementFocus:{refine:true,
init:false},
enabled:{refine:true,
init:true},
selectable:{refine:true,
init:false},
hideFocus:{refine:true,
init:true},
globalCursor:{check:"String",
nullable:true,
themeable:true,
apply:"_applyGlobalCursor",
event:"changeGlobalCursor"}},
members:{_applyParent:qx.lang.Function.returnTrue,
getTopLevelWidget:qx.lang.Function.returnThis,
getWindowElement:function(){return this._window;
},
getDocumentElement:function(){return this._document;
},
getParent:qx.lang.Function.returnNull,
getToolTip:qx.lang.Function.returnNull,
isMaterialized:qx.lang.Function.returnTrue,
isSeeable:qx.lang.Function.returnTrue,
_isDisplayable:true,
_hasParent:false,
_initialLayoutDone:true,
_getBlocker:function(){if(!this._blocker){this._blocker=new qx.ui.core.ClientDocumentBlocker;
this._blocker.addEventListener("mousedown",
this.blockHelper,
this);
this._blocker.addEventListener("mouseup",
this.blockHelper,
this);
this.add(this._blocker);
}return this._blocker;
},
blockHelper:function($0){if(this._modalNativeWindow){if(!this._modalNativeWindow.isClosed()){this._modalNativeWindow.focus();
}else{this.debug("Window seems to be closed already! => Releasing Blocker");
this.release(this._modalNativeWindow);
}}},
block:function($0){this._getBlocker().show();
if(qx.Class.isDefined("qx.ui.window.Window")&&$0 instanceof qx.ui.window.Window){this._modalWidgets.push($0);
var $1=$0.getZIndex();
this._getBlocker().setZIndex($1);
$0.setZIndex($1+1);
}else if(qx.Class.isDefined("qx.client.NativeWindow")&&$0 instanceof qx.client.NativeWindow){this._modalNativeWindow=$0;
this._getBlocker().setZIndex(1e7);
}},
release:function($0){if($0){if(qx.Class.isDefined("qx.client.NativeWindow")&&$0 instanceof qx.client.NativeWindow){this._modalNativeWindow=null;
}else{qx.lang.Array.remove(this._modalWidgets,
$0);
}}var $1=this._modalWidgets.length;
if($1==0){this._getBlocker().hide();
}else{var $2=this._modalWidgets[$1-1];
var $3=$2.getZIndex();
this._getBlocker().setZIndex($3);
$2.setZIndex($3+1);
}},
createStyleElement:function($0){return qx.html.StyleSheet.createElement($0);
},
addCssRule:function($0,
$1,
$2){return qx.html.StyleSheet.addRule($0,
$1,
$2);
},
removeCssRule:function($0,
$1){return qx.html.StyleSheet.removeRule($0,
$1);
},
removeAllCssRules:function($0){return qx.html.StyleSheet.removeAllRules($0);
},
_applyGlobalCursor:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){if($0=="pointer"){$0="hand";
}
if($1=="pointer"){$1="hand";
}var $2,
$3;
var $4=this._cursorElements;
if($4){for(var $5=0,
$6=$4.length;$5<$6;$5++){$2=$4[$5];
if($2.style.cursor==$1){$2.style.cursor=$2._oldCursor;
$2._oldCursor=null;
}}}var $7=document.all;
var $4=this._cursorElements=[];
if($0!=null&&$0!=""&&$0!="auto"){for(var $5=0,
$6=$7.length;$5<$6;$5++){$2=$7[$5];
$3=$2.style.cursor;
if($3!=null&&$3!=""&&$3!="auto"){$2._oldCursor=$3;
$2.style.cursor=$0;
$4.push($2);
}}document.body.style.cursor=$0;
}else{document.body.style.cursor="";
}},
"default":function($0,
$1){if(!this._globalCursorStyleSheet){this._globalCursorStyleSheet=this.createStyleElement();
}this.removeCssRule(this._globalCursorStyleSheet,
"*");
if($0){this.addCssRule(this._globalCursorStyleSheet,
"*",
"cursor:"+$0+" !important");
}}}),
_onwindowresize:function($0){if(qx.Class.isDefined("qx.ui.popup.PopupManager")){qx.ui.popup.PopupManager.getInstance().update();
}this._recomputeInnerWidth();
this._recomputeInnerHeight();
qx.ui.core.Widget.flushGlobalQueues();
},
_computeInnerWidth:function(){return this._document.body.offsetWidth;
},
_computeInnerHeight:function(){return this._document.body.offsetHeight;
}},
settings:{"qx.enableApplicationLayout":true,
"qx.boxModelCorrection":true},
defer:function(){if(qx.core.Setting.get("qx.boxModelCorrection")){var $0=qx.core.Client.getInstance().getEngineBoxSizingAttributes();
var $1=$0.join(":border-box;")+":border-box;";
var $2=$0.join(":content-box;")+":content-box;";
qx.html.StyleSheet.createElement("html,body { margin:0;border:0;padding:0; } "+"html { border:0 none; } "+"*{"+$1+"} "+"img{"+$2+"}");
}
if(qx.core.Setting.get("qx.enableApplicationLayout")){qx.html.StyleSheet.createElement("html,body{width:100%;height:100%;overflow:hidden;}");
}},
destruct:function(){this._disposeObjects("_blocker");
this._disposeFields("_window",
"_document",
"_modalWidgets",
"_modalNativeWindow",
"_globalCursorStyleSheet");
}});




/* ID: qx.ui.basic.Terminator */
qx.Class.define("qx.ui.basic.Terminator",
{extend:qx.ui.core.Widget,
members:{renderPadding:function($0){if($0.paddingLeft){this._renderRuntimePaddingLeft(this.getPaddingLeft());
}
if($0.paddingRight){this._renderRuntimePaddingRight(this.getPaddingRight());
}
if($0.paddingTop){this._renderRuntimePaddingTop(this.getPaddingTop());
}
if($0.paddingBottom){this._renderRuntimePaddingBottom(this.getPaddingBottom());
}},
_renderContent:function(){if(this._computedWidthTypePixel){this._cachedPreferredInnerWidth=null;
}else{this._invalidatePreferredInnerWidth();
}if(this._computedHeightTypePixel){this._cachedPreferredInnerHeight=null;
}else{this._invalidatePreferredInnerHeight();
}if(this._initialLayoutDone){this.addToJobQueue("load");
}},
_layoutPost:function($0){if($0.initial||$0.load||$0.width||$0.height){this._postApply();
}},
_postApply:qx.lang.Function.returnTrue,
_computeBoxWidthFallback:function(){return this.getPreferredBoxWidth();
},
_computeBoxHeightFallback:function(){return this.getPreferredBoxHeight();
},
_computePreferredInnerWidth:qx.lang.Function.returnZero,
_computePreferredInnerHeight:qx.lang.Function.returnZero,
_isWidthEssential:function(){if(!this._computedLeftTypeNull&&!this._computedRightTypeNull){return true;
}
if(!this._computedWidthTypeNull&&!this._computedWidthTypeAuto){return true;
}
if(!this._computedMinWidthTypeNull&&!this._computedMinWidthTypeAuto){return true;
}
if(!this._computedMaxWidthTypeNull&&!this._computedMaxWidthTypeAuto){return true;
}
if(this._borderElement){return true;
}return false;
},
_isHeightEssential:function(){if(!this._computedTopTypeNull&&!this._computedBottomTypeNull){return true;
}
if(!this._computedHeightTypeNull&&!this._computedHeightTypeAuto){return true;
}
if(!this._computedMinHeightTypeNull&&!this._computedMinHeightTypeAuto){return true;
}
if(!this._computedMaxHeightTypeNull&&!this._computedMaxHeightTypeAuto){return true;
}
if(this._borderElement){return true;
}return false;
}}});




/* ID: qx.ui.core.ClientDocumentBlocker */
qx.Class.define("qx.ui.core.ClientDocumentBlocker",
{extend:qx.ui.basic.Terminator,
construct:function(){arguments.callee.base.call(this);
this.initTop();
this.initLeft();
this.initWidth();
this.initHeight();
this.initZIndex();
},
properties:{appearance:{refine:true,
init:"client-document-blocker"},
zIndex:{refine:true,
init:1e8},
top:{refine:true,
init:0},
left:{refine:true,
init:0},
width:{refine:true,
init:"100%"},
height:{refine:true,
init:"100%"},
display:{refine:true,
init:false}},
members:{getFocusRoot:function(){return null;
}}});




/* ID: qx.theme.manager.Appearance */
qx.Class.define("qx.theme.manager.Appearance",
{type:"singleton",
extend:qx.util.manager.Object,
construct:function(){arguments.callee.base.call(this);
this.__cache={};
this.__stateMap={};
this.__stateMapLength=1;
},
properties:{appearanceTheme:{check:"Theme",
nullable:true,
apply:"_applyAppearanceTheme",
event:"changeAppearanceTheme"}},
members:{_applyAppearanceTheme:function($0,
$1){this._currentTheme=$0;
this._oldTheme=$1;
if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncAppearanceTheme();
}},
syncAppearanceTheme:function(){if(!this._currentTheme&&!this._oldTheme){return;
}
if(this._currentTheme){this.__cache[this._currentTheme.name]={};
}var $0=qx.core.Init.getInstance().getApplication();
if($0&&$0.getUiReady()){qx.ui.core.ClientDocument.getInstance()._recursiveAppearanceThemeUpdate(this._currentTheme,
this._oldTheme);
}
if(this._oldTheme){delete this.__cache[this._oldTheme.name];
}delete this._currentTheme;
delete this._oldTheme;
},
styleFrom:function($0,
$1){var $2=this.getAppearanceTheme();
if(!$2){return;
}return this.styleFromTheme($2,
$0,
$1);
},
styleFromTheme:function($0,
$1,
$2){var $3=$0.appearances[$1];
if(!$3){{};
return null;
}if(!$3.style){if($3.include){return this.styleFromTheme($0,
$3.include,
$2);
}else{return null;
}}var $4=this.__stateMap;
var $5=[$1];
for(var $6 in $2){if(!$4[$6]){$4[$6]=this.__stateMapLength++;
}$5[$4[$6]]=true;
}var $7=$5.join();
var $8=this.__cache[$0.name];
if($8&&$8[$7]!==undefined){return $8[$7];
}var $9;
if($3.include||$3.base){var $a=$3.style($2);
var $b;
if($3.include){$b=this.styleFromTheme($0,
$3.include,
$2);
}$9={};
if($3.base){var $c=this.styleFromTheme($3.base,
$1,
$2);
if($3.include){for(var $d in $c){if($b[$d]===undefined&&$a[$d]===undefined){$9[$d]=$c[$d];
}}}else{for(var $d in $c){if($a[$d]===undefined){$9[$d]=$c[$d];
}}}}if($3.include){for(var $d in $b){if($a[$d]===undefined){$9[$d]=$b[$d];
}}}for(var $d in $a){$9[$d]=$a[$d];
}}else{$9=$3.style($2);
}if($8){$8[$7]=$9||null;
}return $9||null;
}},
destruct:function(){this._disposeFields("__cache",
"__stateMap");
}});




/* ID: qx.theme.manager.Meta */
qx.Class.define("qx.theme.manager.Meta",
{type:"singleton",
extend:qx.core.Target,
properties:{theme:{check:"Theme",
nullable:true,
apply:"_applyTheme",
event:"changeTheme"},
autoSync:{check:"Boolean",
init:true,
apply:"_applyAutoSync"}},
members:{_applyTheme:function($0,
$1){var $2=null;
var $3=null;
var $4=null;
var $5=null;
var $6=null;
var $7=null;
if($0){$2=$0.meta.color||null;
$3=$0.meta.border||null;
$4=$0.meta.font||null;
$5=$0.meta.widget||null;
$6=$0.meta.icon||null;
$7=$0.meta.appearance||null;
}
if($1){this.setAutoSync(false);
}var $8=qx.theme.manager.Color.getInstance();
var $9=qx.theme.manager.Border.getInstance();
var $a=qx.theme.manager.Font.getInstance();
var $b=qx.theme.manager.Icon.getInstance();
var $c=qx.theme.manager.Widget.getInstance();
var $d=qx.theme.manager.Appearance.getInstance();
$8.setColorTheme($2);
$9.setBorderTheme($3);
$a.setFontTheme($4);
$c.setWidgetTheme($5);
$b.setIconTheme($6);
$d.setAppearanceTheme($7);
if($1){this.setAutoSync(true);
}},
_applyAutoSync:function($0,
$1){if($0){qx.theme.manager.Appearance.getInstance().syncAppearanceTheme();
qx.theme.manager.Icon.getInstance().syncIconTheme();
qx.theme.manager.Widget.getInstance().syncWidgetTheme();
qx.theme.manager.Font.getInstance().syncFontTheme();
qx.theme.manager.Border.getInstance().syncBorderTheme();
qx.theme.manager.Color.getInstance().syncColorTheme();
}},
initialize:function(){var $0=qx.core.Setting;
var $1,
$2;
$1=$0.get("qx.theme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The meta theme to use is not available: "+$1);
}this.setTheme($2);
}$1=$0.get("qx.colorTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The color theme to use is not available: "+$1);
}qx.theme.manager.Color.getInstance().setColorTheme($2);
}$1=$0.get("qx.borderTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The border theme to use is not available: "+$1);
}qx.theme.manager.Border.getInstance().setBorderTheme($2);
}$1=$0.get("qx.fontTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The font theme to use is not available: "+$1);
}qx.theme.manager.Font.getInstance().setFontTheme($2);
}$1=$0.get("qx.widgetTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The widget theme to use is not available: "+$1);
}qx.theme.manager.Widget.getInstance().setWidgetTheme($2);
}$1=$0.get("qx.iconTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The icon theme to use is not available: "+$1);
}qx.theme.manager.Icon.getInstance().setIconTheme($2);
}$1=$0.get("qx.appearanceTheme");
if($1){$2=qx.Theme.getByName($1);
if(!$2){throw new Error("The appearance theme to use is not available: "+$1);
}qx.theme.manager.Appearance.getInstance().setAppearanceTheme($2);
}},
__queryThemes:function($0){var $1=qx.Theme.getAll();
var $2;
var $3=[];
for(var $4 in $1){$2=$1[$4];
if($2[$0]){$3.push($2);
}}return $3;
},
getMetaThemes:function(){return this.__queryThemes("meta");
},
getColorThemes:function(){return this.__queryThemes("colors");
},
getBorderThemes:function(){return this.__queryThemes("borders");
},
getFontThemes:function(){return this.__queryThemes("fonts");
},
getWidgetThemes:function(){return this.__queryThemes("widgets");
},
getIconThemes:function(){return this.__queryThemes("icons");
},
getAppearanceThemes:function(){return this.__queryThemes("appearances");
}},
settings:{"qx.theme":"qx.theme.ClassicRoyale",
"qx.colorTheme":null,
"qx.borderTheme":null,
"qx.fontTheme":null,
"qx.widgetTheme":null,
"qx.appearanceTheme":null,
"qx.iconTheme":null}});




/* ID: qx.theme.manager.Color */
qx.Class.define("qx.theme.manager.Color",
{type:"singleton",
extend:qx.util.manager.Value,
properties:{colorTheme:{check:"Theme",
nullable:true,
apply:"_applyColorTheme",
event:"changeColorTheme"}},
members:{_applyColorTheme:function($0){var $1=this._dynamic={};
if($0){var $2=$0.colors;
var $3=qx.util.ColorUtil;
var $4;
for(var $5 in $2){$4=$2[$5];
if(typeof $4==="string"){if(!$3.isCssString($4)){throw new Error("Could not parse color: "+$4);
}}else if($4 instanceof Array){$4=$3.rgbToRgbString($4);
}else{throw new Error("Could not parse color: "+$4);
}$1[$5]=$4;
}}
if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncColorTheme();
}},
syncColorTheme:function(){this._updateObjects();
}}});




/* ID: qx.util.ColorUtil */
qx.Class.define("qx.util.ColorUtil",
{statics:{REGEXP:{hex3:/^#([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})$/,
hex6:/^#([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})([0-9a-fA-F]{1})$/,
rgb:/^rgb\(\s*([0-9]{1,3}\.{0,1}[0-9]*)\s*,\s*([0-9]{1,3}\.{0,1}[0-9]*)\s*,\s*([0-9]{1,3}\.{0,1}[0-9]*)\s*\)$/},
SYSTEM:{activeborder:true,
activecaption:true,
appworkspace:true,
background:true,
buttonface:true,
buttonhighlight:true,
buttonshadow:true,
buttontext:true,
captiontext:true,
graytext:true,
highlight:true,
highlighttext:true,
inactiveborder:true,
inactivecaption:true,
inactivecaptiontext:true,
infobackground:true,
infotext:true,
menu:true,
menutext:true,
scrollbar:true,
threeddarkshadow:true,
threedface:true,
threedhighlight:true,
threedlightshadow:true,
threedshadow:true,
window:true,
windowframe:true,
windowtext:true},
NAMED:{black:[0,
0,
0],
silver:[192,
192,
192],
gray:[128,
128,
128],
white:[255,
255,
255],
maroon:[128,
0,
0],
red:[255,
0,
0],
purple:[128,
0,
128],
fuchsia:[255,
0,
255],
green:[0,
128,
0],
lime:[0,
255,
0],
olive:[128,
128,
0],
yellow:[255,
255,
0],
navy:[0,
0,
128],
blue:[0,
0,
255],
teal:[0,
128,
128],
aqua:[0,
255,
255],
transparent:[-1,
-1,
-1],
grey:[128,
128,
128],
magenta:[255,
0,
255],
orange:[255,
165,
0],
brown:[165,
42,
42]},
isNamedColor:function($0){return this.NAMED[$0]!==undefined;
},
isSystemColor:function($0){return this.SYSTEM[$0]!==undefined;
},
isThemedColor:function($0){return qx.theme.manager.Color.getInstance().isDynamic($0);
},
stringToRgb:function($0){if(this.isThemedColor($0)){var $0=qx.theme.manager.Color.getInstance().resolveDynamic($0);
}
if(this.isNamedColor($0)){return this.NAMED[$0];
}else if(this.isSystemColor($0)){throw new Error("Could not convert system colors to RGB: "+$0);
}else if(this.isRgbString($0)){return this.__rgbStringToRgb();
}else if(this.isHex3String($0)){return this.__hex3StringToRgb();
}else if(this.isHex6String($0)){return this.__hex6StringToRgb();
}throw new Error("Could not parse color: "+$0);
},
cssStringToRgb:function($0){if(this.isNamedColor($0)){return this.NAMED[$0];
}else if(this.isSystemColor($0)){throw new Error("Could not convert system colors to RGB: "+$0);
}else if(this.isRgbString($0)){return this.__rgbStringToRgb();
}else if(this.isHex3String($0)){return this.__hex3StringToRgb();
}else if(this.isHex6String($0)){return this.__hex6StringToRgb();
}throw new Error("Could not parse color: "+$0);
},
stringToRgbString:function($0){return this.rgbToRgbString(this.stringToRgb($0));
},
rgbToRgbString:function($0){return "rgb("+$0[0]+","+$0[1]+","+$0[2]+")";
},
rgbToHexString:function($0){return (qx.lang.String.pad($0[0].toString(16).toUpperCase(),
2)+qx.lang.String.pad($0[1].toString(16).toUpperCase(),
2)+qx.lang.String.pad($0[2].toString(16).toUpperCase(),
2));
},
isValid:function($0){return this.isThemedColor($0)||this.isCssString($0);
},
isCssString:function($0){return this.isSystemColor($0)||this.isNamedColor($0)||this.isHex3String($0)||this.isHex6String($0)||this.isRgbString($0);
},
isHex3String:function($0){return this.REGEXP.hex3.test($0);
},
isHex6String:function($0){return this.REGEXP.hex6.test($0);
},
isRgbString:function($0){return this.REGEXP.rgb.test($0);
},
__rgbStringToRgb:function(){var $0=parseInt(RegExp.$1);
var $1=parseInt(RegExp.$2);
var $2=parseInt(RegExp.$3);
return [$0,
$1,
$2];
},
__hex3StringToRgb:function(){var $0=parseInt(RegExp.$1,
16)*17;
var $1=parseInt(RegExp.$2,
16)*17;
var $2=parseInt(RegExp.$3,
16)*17;
return [$0,
$1,
$2];
},
__hex6StringToRgb:function(){var $0=(parseInt(RegExp.$1,
16)*16)+parseInt(RegExp.$2,
16);
var $1=(parseInt(RegExp.$3,
16)*16)+parseInt(RegExp.$4,
16);
var $2=(parseInt(RegExp.$5,
16)*16)+parseInt(RegExp.$6,
16);
return [$0,
$1,
$2];
},
hex3StringToRgb:function($0){if(this.isHex3String($0)){return this.__hex3StringToRgb($0);
}throw new Error("Invalid hex3 value: "+$0);
},
hex6StringToRgb:function($0){if(this.isHex6String($0)){return this.__hex6StringToRgb($0);
}throw new Error("Invalid hex6 value: "+$0);
},
hexStringToRgb:function($0){if(this.isHex3String($0)){return this.__hex3StringToRgb($0);
}
if(this.isHex6String($0)){return this.__hex6StringToRgb($0);
}throw new Error("Invalid hex value: "+$0);
},
rgbToHsb:function($0){var $1,
$2,
$3;
var $4=$0[0];
var $5=$0[1];
var $6=$0[2];
var $7=($4>$5)?$4:$5;
if($6>$7){$7=$6;
}var $8=($4<$5)?$4:$5;
if($6<$8){$8=$6;
}$3=$7/255.0;
if($7!=0){$2=($7-$8)/$7;
}else{$2=0;
}
if($2==0){$1=0;
}else{var $9=($7-$4)/($7-$8);
var $a=($7-$5)/($7-$8);
var $b=($7-$6)/($7-$8);
if($4==$7){$1=$b-$a;
}else if($5==$7){$1=2.0+$9-$b;
}else{$1=4.0+$a-$9;
}$1=$1/6.0;
if($1<0){$1=$1+1.0;
}}return [Math.round($1*360),
Math.round($2*100),
Math.round($3*100)];
},
hsbToRgb:function($0){var $1,
$2,
$3,
$4,
$5;
var $6=$0[0]/360;
var $7=$0[1]/100;
var $8=$0[2]/100;
if($6>=1.0){$6%=1.0;
}
if($7>1.0){$7=1.0;
}
if($8>1.0){$8=1.0;
}var $9=Math.floor(255*$8);
var $a={};
if($7==0.0){$a.red=$a.green=$a.blue=$9;
}else{$6*=6.0;
$1=Math.floor($6);
$2=$6-$1;
$3=Math.floor($9*(1.0-$7));
$4=Math.floor($9*(1.0-($7*$2)));
$5=Math.floor($9*(1.0-($7*(1.0-$2))));
switch($1){case 0:$a.red=$9;
$a.green=$5;
$a.blue=$3;
break;
case 1:$a.red=$4;
$a.green=$9;
$a.blue=$3;
break;
case 2:$a.red=$3;
$a.green=$9;
$a.blue=$5;
break;
case 3:$a.red=$3;
$a.green=$4;
$a.blue=$9;
break;
case 4:$a.red=$5;
$a.green=$3;
$a.blue=$9;
break;
case 5:$a.red=$9;
$a.green=$3;
$a.blue=$4;
break;
}}return $a;
},
randomColor:function(){var $0=Math.round(Math.random()*255);
var $1=Math.round(Math.random()*255);
var $2=Math.round(Math.random()*255);
return this.rgbToRgbString([$0,
$1,
$2]);
}}});




/* ID: qx.theme.manager.Border */
qx.Class.define("qx.theme.manager.Border",
{type:"singleton",
extend:qx.util.manager.Value,
properties:{borderTheme:{check:"Theme",
nullable:true,
apply:"_applyBorderTheme",
event:"changeBorderTheme"}},
members:{resolveDynamic:function($0){return $0 instanceof qx.ui.core.Border?$0:this._dynamic[$0];
},
isDynamic:function($0){return $0&&($0 instanceof qx.ui.core.Border||this._dynamic[$0]!==undefined);
},
syncBorderTheme:function(){this._updateObjects();
},
updateObjectsEdge:function($0,
$1){var $2=this._registry;
var $3=this._dynamic;
var $4;
for(var $5 in $2){$4=$2[$5];
if($4.value===$0||$3[$4.value]===$0){$4.callback.call($4.object,
$0,
$1);
}}},
_applyBorderTheme:function($0){var $1=this._dynamic;
for(var $2 in $1){if($1[$2].themed){$1[$2].dispose();
delete $1[$2];
}}
if($0){var $3=$0.borders;
var $4=qx.ui.core.Border;
for(var $2 in $3){$1[$2]=(new $4).set($3[$2]);
$1[$2].themed=true;
}}
if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncBorderTheme();
}}}});




/* ID: qx.ui.core.Border */
qx.Class.define("qx.ui.core.Border",
{extend:qx.core.Object,
construct:function($0,
$1,
$2){arguments.callee.base.call(this);
if($0!==undefined){this.setWidth($0);
}
if($1!==undefined){this.setStyle($1);
}
if($2!==undefined){this.setColor($2);
}},
statics:{fromString:function($0){var $1=new qx.ui.core.Border;
var $2=$0.split(/\s+/);
var $3,
$4;
for(var $5=0,
$6=$2.length;$5<$6;$5++){$3=$2[$5];
switch($3){case "groove":case "ridge":case "inset":case "outset":case "solid":case "dotted":case "dashed":case "double":case "none":$1.setStyle($3);
break;
default:$4=parseInt($3);
if($4===$3||qx.lang.String.contains($3,
"px")){$1.setWidth($4);
}else{$1.setColor($3);
}break;
}}return $1;
},
fromConfig:function($0){var $1=new qx.ui.core.Border;
$1.set($0);
return $1;
},
resetTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
if($1){$1.borderTopWidth=$1.borderTopStyle=$1.borderTopColor=$1.MozBorderTopColors="";
}},
"default":function($0){var $1=$0._style;
if($1){$1.borderTopWidth=$1.borderTopStyle=$1.borderTopColor="";
}$1=$0._innerStyle;
if($1){$1.borderTopWidth=$1.borderTopStyle=$1.borderTopColor="";
}}}),
resetRight:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
if($1){$1.borderRightWidth=$1.borderRightStyle=$1.borderRightColor=$1.MozBorderRightColors="";
}},
"default":function($0){var $1=$0._style;
if($1){$1.borderRightWidth=$1.borderRightStyle=$1.borderRightColor="";
}$1=$0._innerStyle;
if($1){$1.borderRightWidth=$1.borderRightStyle=$1.borderRightColor="";
}}}),
resetBottom:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
if($1){$1.borderBottomWidth=$1.borderBottomStyle=$1.borderBottomColor=$1.MozBorderBottomColors="";
}},
"default":function($0){var $1=$0._style;
if($1){$1.borderBottomWidth=$1.borderBottomStyle=$1.borderBottomColor="";
}$1=$0._innerStyle;
if($1){$1.borderBottomWidth=$1.borderBottomStyle=$1.borderBottomColor="";
}}}),
resetLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
if($1){$1.borderLeftWidth=$1.borderLeftStyle=$1.borderLeftColor=$1.MozBorderLeftColors="";
}},
"default":function($0){var $1=$0._style;
if($1){$1.borderLeftWidth=$1.borderLeftStyle=$1.borderLeftColor="";
}$1=$0._innerStyle;
if($1){$1.borderLeftWidth=$1.borderLeftStyle=$1.borderLeftColor="";
}}})},
properties:{widthTop:{check:"Number",
init:0,
apply:"_applyWidthTop"},
widthRight:{check:"Number",
init:0,
apply:"_applyWidthRight"},
widthBottom:{check:"Number",
init:0,
apply:"_applyWidthBottom"},
widthLeft:{check:"Number",
init:0,
apply:"_applyWidthLeft"},
styleTop:{nullable:true,
check:["solid",
"dotted",
"dashed",
"double",
"outset",
"inset",
"ridge",
"groove"],
init:"solid",
apply:"_applyStyleTop"},
styleRight:{nullable:true,
check:["solid",
"dotted",
"dashed",
"double",
"outset",
"inset",
"ridge",
"groove"],
init:"solid",
apply:"_applyStyleRight"},
styleBottom:{nullable:true,
check:["solid",
"dotted",
"dashed",
"double",
"outset",
"inset",
"ridge",
"groove"],
init:"solid",
apply:"_applyStyleBottom"},
styleLeft:{nullable:true,
check:["solid",
"dotted",
"dashed",
"double",
"outset",
"inset",
"ridge",
"groove"],
init:"solid",
apply:"_applyStyleLeft"},
colorTop:{nullable:true,
check:"Color",
apply:"_applyColorTop"},
colorRight:{nullable:true,
check:"Color",
apply:"_applyColorRight"},
colorBottom:{nullable:true,
check:"Color",
apply:"_applyColorBottom"},
colorLeft:{nullable:true,
check:"Color",
apply:"_applyColorLeft"},
colorInnerTop:{nullable:true,
check:"Color",
apply:"_applyColorInnerTop"},
colorInnerRight:{nullable:true,
check:"Color",
apply:"_applyColorInnerRight"},
colorInnerBottom:{nullable:true,
check:"Color",
apply:"_applyColorInnerBottom"},
colorInnerLeft:{nullable:true,
check:"Color",
apply:"_applyColorInnerLeft"},
left:{group:["widthLeft",
"styleLeft",
"colorLeft"]},
right:{group:["widthRight",
"styleRight",
"colorRight"]},
top:{group:["widthTop",
"styleTop",
"colorTop"]},
bottom:{group:["widthBottom",
"styleBottom",
"colorBottom"]},
width:{group:["widthTop",
"widthRight",
"widthBottom",
"widthLeft"],
mode:"shorthand"},
style:{group:["styleTop",
"styleRight",
"styleBottom",
"styleLeft"],
mode:"shorthand"},
color:{group:["colorTop",
"colorRight",
"colorBottom",
"colorLeft"],
mode:"shorthand"},
innerColor:{group:["colorInnerTop",
"colorInnerRight",
"colorInnerBottom",
"colorInnerLeft"],
mode:"shorthand"}},
members:{_applyWidthTop:function($0,
$1){this.__widthTop=$0==null?"0px":$0+"px";
this.__computeComplexTop();
this.__informManager("top");
},
_applyWidthRight:function($0,
$1){this.__widthRight=$0==null?"0px":$0+"px";
this.__computeComplexRight();
this.__informManager("right");
},
_applyWidthBottom:function($0,
$1){this.__widthBottom=$0==null?"0px":$0+"px";
this.__computeComplexBottom();
this.__informManager("bottom");
},
_applyWidthLeft:function($0,
$1){this.__widthLeft=$0==null?"0px":$0+"px";
this.__computeComplexLeft();
this.__informManager("left");
},
_applyColorTop:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorTop,
this,
$0);
},
_applyColorRight:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorRight,
this,
$0);
},
_applyColorBottom:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorBottom,
this,
$0);
},
_applyColorLeft:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorLeft,
this,
$0);
},
_applyColorInnerTop:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorInnerTop,
this,
$0);
},
_applyColorInnerRight:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorInnerRight,
this,
$0);
},
_applyColorInnerBottom:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorInnerBottom,
this,
$0);
},
_applyColorInnerLeft:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._changeColorInnerLeft,
this,
$0);
},
_applyStyleTop:function(){this.__informManager("top");
},
_applyStyleRight:function(){this.__informManager("right");
},
_applyStyleBottom:function(){this.__informManager("bottom");
},
_applyStyleLeft:function(){this.__informManager("left");
},
_changeColorTop:function($0){this.__colorTop=$0;
this.__computeComplexTop();
this.__informManager("top");
},
_changeColorInnerTop:function($0){this.__colorInnerTop=$0;
this.__computeComplexTop();
this.__informManager("top");
},
_changeColorRight:function($0){this.__colorRight=$0;
this.__computeComplexRight();
this.__informManager("right");
},
_changeColorInnerRight:function($0){this.__colorInnerRight=$0;
this.__computeComplexRight();
this.__informManager("right");
},
_changeColorBottom:function($0){this.__colorBottom=$0;
this.__computeComplexBottom();
this.__informManager("bottom");
},
_changeColorInnerBottom:function($0){this.__colorInnerBottom=$0;
this.__computeComplexBottom();
this.__informManager("bottom");
},
_changeColorLeft:function($0){this.__colorLeft=$0;
this.__computeComplexLeft();
this.__informManager("left");
},
_changeColorInnerLeft:function($0){this.__colorInnerLeft=$0;
this.__computeComplexLeft();
this.__informManager("left");
},
__computeComplexTop:function(){this.__complexTop=this.getWidthTop()===2&&this.__colorInnerTop!=null&&this.__colorTop!=this.__colorInnerTop;
},
__computeComplexRight:function(){this.__complexRight=this.getWidthRight()===2&&this.__colorInnerRight!=null&&this.__colorRight!=this.__colorInnerRight;
},
__computeComplexBottom:function(){this.__complexBottom=this.getWidthBottom()===2&&this.__colorInnerBottom!=null&&this.__colorBottom!=this.__colorInnerBottom;
},
__computeComplexLeft:function(){this.__complexLeft=this.getWidthLeft()===2&&this.__colorInnerLeft!=null&&this.__colorLeft!=this.__colorInnerLeft;
},
__informManager:function($0){qx.theme.manager.Border.getInstance().updateObjectsEdge(this,
$0);
},
renderTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
$1.borderTopWidth=this.__widthTop||"0px";
$1.borderTopColor=this.__colorTop||"";
if(this.__complexTop){$1.borderTopStyle="solid";
$1.MozBorderTopColors=this.__colorTop+" "+this.__colorInnerTop;
}else{$1.borderTopStyle=this.getStyleTop()||"none";
$1.MozBorderTopColors="";
}},
"default":function($0){var $1=$0._style;
var $2=$0._innerStyle;
if(this.__complexTop){if(!$2){$0.prepareEnhancedBorder();
$2=$0._innerStyle;
}$1.borderTopWidth=$2.borderTopWidth="1px";
$1.borderTopStyle=$2.borderTopStyle="solid";
$1.borderTopColor=this.__colorTop;
$2.borderTopColor=this.__colorInnerTop;
}else{$1.borderTopWidth=this.__widthTop||"0px";
$1.borderTopStyle=this.getStyleTop()||"none";
$1.borderTopColor=this.__colorTop||"";
if($2){$2.borderTopWidth=$2.borderTopStyle=$2.borderTopColor="";
}}}}),
renderRight:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
$1.borderRightWidth=this.__widthRight||"0px";
$1.borderRightColor=this.__colorRight||"";
if(this.__complexRight){$1.borderRightStyle="solid";
$1.MozBorderRightColors=this.__colorRight+" "+this.__colorInnerRight;
}else{$1.borderRightStyle=this.getStyleRight()||"none";
$1.MozBorderRightColors="";
}},
"default":function($0){var $1=$0._style;
var $2=$0._innerStyle;
if(this.__complexRight){if(!$2){$0.prepareEnhancedBorder();
$2=$0._innerStyle;
}$1.borderRightWidth=$2.borderRightWidth="1px";
$1.borderRightStyle=$2.borderRightStyle="solid";
$1.borderRightColor=this.__colorRight;
$2.borderRightColor=this.__colorInnerRight;
}else{$1.borderRightWidth=this.__widthRight||"0px";
$1.borderRightStyle=this.getStyleRight()||"none";
$1.borderRightColor=this.__colorRight||"";
if($2){$2.borderRightWidth=$2.borderRightStyle=$2.borderRightColor="";
}}}}),
renderBottom:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
$1.borderBottomWidth=this.__widthBottom||"0px";
$1.borderBottomColor=this.__colorBottom||"";
if(this.__complexBottom){$1.borderBottomStyle="solid";
$1.MozBorderBottomColors=this.__colorBottom+" "+this.__colorInnerBottom;
}else{$1.borderBottomStyle=this.getStyleBottom()||"none";
$1.MozBorderBottomColors="";
}},
"default":function($0){var $1=$0._style;
var $2=$0._innerStyle;
if(this.__complexBottom){if(!$2){$0.prepareEnhancedBorder();
$2=$0._innerStyle;
}$1.borderBottomWidth=$2.borderBottomWidth="1px";
$1.borderBottomStyle=$2.borderBottomStyle="solid";
$1.borderBottomColor=this.__colorBottom;
$2.borderBottomColor=this.__colorInnerBottom;
}else{$1.borderBottomWidth=this.__widthBottom||"0px";
$1.borderBottomStyle=this.getStyleBottom()||"none";
$1.borderBottomColor=this.__colorBottom||"";
if($2){$2.borderBottomWidth=$2.borderBottomStyle=$2.borderBottomColor="";
}}}}),
renderLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0._style;
$1.borderLeftWidth=this.__widthLeft||"0px";
$1.borderLeftColor=this.__colorLeft||"";
if(this.__complexLeft){$1.borderLeftStyle="solid";
$1.MozBorderLeftColors=this.__colorLeft+" "+this.__colorInnerLeft;
}else{$1.borderLeftStyle=this.getStyleLeft()||"none";
$1.MozBorderLeftColors="";
}},
"default":function($0){var $1=$0._style;
var $2=$0._innerStyle;
if(this.__complexLeft){if(!$2){$0.prepareEnhancedBorder();
$2=$0._innerStyle;
}$1.borderLeftWidth=$2.borderLeftWidth="1px";
$1.borderLeftStyle=$2.borderLeftStyle="solid";
$1.borderLeftColor=this.__colorLeft;
$2.borderLeftColor=this.__colorInnerLeft;
}else{$1.borderLeftWidth=this.__widthLeft||"0px";
$1.borderLeftStyle=this.getStyleLeft()||"none";
$1.borderLeftColor=this.__colorLeft||"";
if($2){$2.borderLeftWidth=$2.borderLeftStyle=$2.borderLeftColor="";
}}}})}});




/* ID: qx.theme.manager.Font */
qx.Class.define("qx.theme.manager.Font",
{type:"singleton",
extend:qx.util.manager.Value,
properties:{fontTheme:{check:"Theme",
nullable:true,
apply:"_applyFontTheme",
event:"changeFontTheme"}},
members:{resolveDynamic:function($0){return $0 instanceof qx.ui.core.Font?$0:this._dynamic[$0];
},
isDynamic:function($0){return $0&&($0 instanceof qx.ui.core.Font||this._dynamic[$0]!==undefined);
},
syncFontTheme:function(){this._updateObjects();
},
_applyFontTheme:function($0){var $1=this._dynamic;
for(var $2 in $1){if($1[$2].themed){$1[$2].dispose();
delete $1[$2];
}}
if($0){var $3=$0.fonts;
var $4=qx.ui.core.Font;
for(var $2 in $3){$1[$2]=(new $4).set($3[$2]);
$1[$2].themed=true;
}}
if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncFontTheme();
}}}});




/* ID: qx.ui.core.Font */
qx.Class.define("qx.ui.core.Font",
{extend:qx.core.Object,
construct:function($0,
$1){arguments.callee.base.call(this);
if($0!==undefined){this.setSize($0);
}
if($1!==undefined){this.setFamily($1);
}},
statics:{fromString:function($0){var $1=new qx.ui.core.Font;
var $2=$0.split(/\s+/);
var $3=[];
var $4;
for(var $5=0;$5<$2.length;$5++){switch($4=$2[$5]){case "bold":$1.setBold(true);
break;
case "italic":$1.setItalic(true);
break;
case "underline":$1.setDecoration("underline");
break;
default:var $6=parseInt($4);
if($6==$4||qx.lang.String.contains($4,
"px")){$1.setSize($6);
}else{$3.push($4);
}break;
}}
if($3.length>0){$1.setFamily($3);
}return $1;
},
fromConfig:function($0){var $1=new qx.ui.core.Font;
$1.set($0);
return $1;
},
reset:function($0){$0.removeStyleProperty("fontFamily");
$0.removeStyleProperty("fontSize");
$0.removeStyleProperty("fontWeight");
$0.removeStyleProperty("fontStyle");
$0.removeStyleProperty("textDecoration");
},
resetElement:function($0){var $1=$0.style;
$1.fontFamily="";
$1.fontSize="";
$1.fontWeight="";
$1.fontStyle="";
$1.textDecoration="";
},
resetStyle:function($0){$0.fontFamily="";
$0.fontSize="";
$0.fontWeight="";
$0.fontStyle="";
$0.textDecoration="";
}},
properties:{size:{check:"Integer",
nullable:true,
apply:"_applySize"},
family:{check:"Array",
nullable:true,
apply:"_applyFamily"},
bold:{check:"Boolean",
nullable:true,
apply:"_applyBold"},
italic:{check:"Boolean",
nullable:true,
apply:"_applyItalic"},
decoration:{check:["underline",
"line-through",
"overline"],
nullable:true,
apply:"_applyDecoration"}},
members:{__size:null,
__family:null,
__bold:null,
__italic:null,
__decoration:null,
_applySize:function($0,
$1){this.__size=$0===null?null:$0+"px";
},
_applyFamily:function($0,
$1){var $2="";
for(var $3=0,
$4=$0.length;$3<$4;$3++){if($0[$3].indexOf(" ")>0){$2+='"'+$0[$3]+'"';
}else{$2+=$0[$3];
}
if($3!=$4-1){$2+=",";
}}this.__family=$2;
},
_applyBold:function($0,
$1){this.__bold=$0===null?null:$0?"bold":"normal";
},
_applyItalic:function($0,
$1){this.__italic=$0===null?null:$0?"italic":"normal";
},
_applyDecoration:function($0,
$1){this.__decoration=$0===null?null:$0;
},
render:function($0){$0.setStyleProperty("fontFamily",
this.__family);
$0.setStyleProperty("fontSize",
this.__size);
$0.setStyleProperty("fontWeight",
this.__bold);
$0.setStyleProperty("fontStyle",
this.__italic);
$0.setStyleProperty("textDecoration",
this.__decoration);
},
renderStyle:function($0){$0.fontFamily=this.__family||"";
$0.fontSize=this.__size||"";
$0.fontWeight=this.__bold||"";
$0.fontStyle=this.__italic||"";
$0.textDecoration=this.__decoration||"";
},
renderElement:function($0){var $1=$0.style;
$1.fontFamily=this.__family||"";
$1.fontSize=this.__size||"";
$1.fontWeight=this.__bold||"";
$1.fontStyle=this.__italic||"";
$1.textDecoration=this.__decoration||"";
},
generateStyle:function(){return (this.__family?"font-family:"+this.__family.replace(/\"/g,
"'")+";":"")+(this.__size?"font-size:"+this.__size+";":"")+(this.__weight?"font-weight:"+this.__weight+";":"")+(this.__style?"font-style:"+this.__style+";":"")+(this.__decoration?"text-decoration:"+this.__decoration+";":"");
}}});




/* ID: qx.theme.manager.Icon */
qx.Class.define("qx.theme.manager.Icon",
{type:"singleton",
extend:qx.core.Target,
properties:{iconTheme:{check:"Theme",
nullable:true,
apply:"_applyIconTheme",
event:"changeIconTheme"}},
members:{_applyIconTheme:function($0,
$1){if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncIconTheme();
}},
syncIconTheme:function(){var $0=this.getIconTheme();
var $1=qx.io.Alias.getInstance();
$0?$1.add("icon",
$0.icons.uri):$1.remove("icon");
}}});




/* ID: qx.theme.manager.Widget */
qx.Class.define("qx.theme.manager.Widget",
{type:"singleton",
extend:qx.core.Target,
properties:{widgetTheme:{check:"Theme",
nullable:true,
apply:"_applyWidgetTheme",
event:"changeWidgetTheme"}},
members:{_applyWidgetTheme:function($0,
$1){if(qx.theme.manager.Meta.getInstance().getAutoSync()){this.syncWidgetTheme();
}},
syncWidgetTheme:function(){var $0=this.getWidgetTheme();
var $1=qx.io.Alias.getInstance();
$0?$1.add("widget",
$0.widgets.uri):$1.remove("widget");
}}});




/* ID: qx.event.handler.FocusHandler */
qx.Class.define("qx.event.handler.FocusHandler",
{extend:qx.core.Target,
construct:function($0){arguments.callee.base.call(this);
if($0!=null){this._attachedWidget=$0;
}},
statics:{mouseFocus:false},
members:{getAttachedWidget:function(){return this._attachedWidget;
},
_onkeyevent:function($0,
$1){if($1.getKeyIdentifier()!="Tab"){return;
}$1.stopPropagation();
$1.preventDefault();
qx.event.handler.FocusHandler.mouseFocus=false;
var $2=this.getAttachedWidget().getFocusedChild();
if(!$1.isShiftPressed()){var $3=$2?this.getWidgetAfter($0,
$2):this.getFirstWidget($0);
}else{var $3=$2?this.getWidgetBefore($0,
$2):this.getLastWidget($0);
}if($3){$3.setFocused(true);
$3._ontabfocus();
}},
compareTabOrder:function($0,
$1){if($0==$1){return 0;
}var $2=$0.getTabIndex();
var $3=$1.getTabIndex();
if($2!=$3){return $2-$3;
}var $4=qx.html.Location.getPageBoxTop($0.getElement());
var $5=qx.html.Location.getPageBoxTop($1.getElement());
if($4!=$5){return $4-$5;
}var $6=qx.html.Location.getPageBoxLeft($0.getElement());
var $7=qx.html.Location.getPageBoxLeft($1.getElement());
if($6!=$7){return $6-$7;
}var $8=$0.getZIndex();
var $9=$1.getZIndex();
if($8!=$9){return $8-$9;
}return 0;
},
getFirstWidget:function($0){return this._getFirst($0,
null);
},
getLastWidget:function($0){return this._getLast($0,
null);
},
getWidgetAfter:function($0,
$1){if($0==$1){return this.getFirstWidget($0);
}
if($1.getAnonymous()){$1=$1.getParent();
}
if($1==null){return [];
}var $2=[];
this._getAllAfter($0,
$1,
$2);
$2.sort(this.compareTabOrder);
return $2.length>0?$2[0]:this.getFirstWidget($0);
},
getWidgetBefore:function($0,
$1){if($0==$1){return this.getLastWidget($0);
}
if($1.getAnonymous()){$1=$1.getParent();
}
if($1==null){return [];
}var $2=[];
this._getAllBefore($0,
$1,
$2);
$2.sort(this.compareTabOrder);
var $3=$2.length;
return $3>0?$2[$3-1]:this.getLastWidget($0);
},
_getAllAfter:function($0,
$1,
$2){var $3=$0.getChildren();
var $4;
var $5=$3.length;
for(var $6=0;$6<$5;$6++){$4=$3[$6];
if(!($4 instanceof qx.ui.core.Parent)&&!($4 instanceof qx.ui.basic.Terminator)){continue;
}
if($4.isFocusable()&&$4.getTabIndex()>0&&this.compareTabOrder($1,
$4)<0){$2.push($3[$6]);
}
if(!$4.isFocusRoot()&&$4 instanceof qx.ui.core.Parent){this._getAllAfter($4,
$1,
$2);
}}},
_getAllBefore:function($0,
$1,
$2){var $3=$0.getChildren();
var $4;
var $5=$3.length;
for(var $6=0;$6<$5;$6++){$4=$3[$6];
if(!($4 instanceof qx.ui.core.Parent)&&!($4 instanceof qx.ui.basic.Terminator)){continue;
}
if($4.isFocusable()&&$4.getTabIndex()>0&&this.compareTabOrder($1,
$4)>0){$2.push($4);
}
if(!$4.isFocusRoot()&&$4 instanceof qx.ui.core.Parent){this._getAllBefore($4,
$1,
$2);
}}},
_getFirst:function($0,
$1){var $2=$0.getChildren();
var $3;
var $4=$2.length;
for(var $5=0;$5<$4;$5++){$3=$2[$5];
if(!($3 instanceof qx.ui.core.Parent)&&!($3 instanceof qx.ui.basic.Terminator)){continue;
}
if($3.isFocusable()&&$3.getTabIndex()>0){if($1==null||this.compareTabOrder($3,
$1)<0){$1=$3;
}}
if(!$3.isFocusRoot()&&$3 instanceof qx.ui.core.Parent){$1=this._getFirst($3,
$1);
}}return $1;
},
_getLast:function($0,
$1){var $2=$0.getChildren();
var $3;
var $4=$2.length;
for(var $5=0;$5<$4;$5++){$3=$2[$5];
if(!($3 instanceof qx.ui.core.Parent)&&!($3 instanceof qx.ui.basic.Terminator)){continue;
}
if($3.isFocusable()&&$3.getTabIndex()>0){if($1==null||this.compareTabOrder($3,
$1)>0){$1=$3;
}}
if(!$3.isFocusRoot()&&$3 instanceof qx.ui.core.Parent){$1=this._getLast($3,
$1);
}}return $1;
}},
destruct:function(){this._disposeFields("_attachedWidget");
}});




/* ID: qx.html.Location */
qx.Class.define("qx.html.Location",
{statics:{getPageOuterLeft:function($0){return qx.html.Location.getPageBoxLeft($0)-qx.html.Style.getMarginLeft($0);
},
getPageOuterTop:function($0){return qx.html.Location.getPageBoxTop($0)-qx.html.Style.getMarginTop($0);
},
getPageOuterRight:function($0){return qx.html.Location.getPageBoxRight($0)+qx.html.Style.getMarginRight($0);
},
getPageOuterBottom:function($0){return qx.html.Location.getPageBoxBottom($0)+qx.html.Style.getMarginBottom($0);
},
getClientOuterLeft:function($0){return qx.html.Location.getClientBoxLeft($0)-qx.html.Style.getMarginLeft($0);
},
getClientOuterTop:function($0){return qx.html.Location.getClientBoxTop($0)-qx.html.Style.getMarginTop($0);
},
getClientOuterRight:function($0){return qx.html.Location.getClientBoxRight($0)+qx.html.Style.getMarginRight($0);
},
getClientOuterBottom:function($0){return qx.html.Location.getClientBoxBottom($0)+qx.html.Style.getMarginBottom($0);
},
getClientBoxLeft:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.getBoundingClientRect().left;
},
"gecko":function($0){return qx.html.Location.getClientAreaLeft($0)-qx.html.Style.getBorderLeft($0);
},
"default":function($0){var $1=$0.offsetLeft;
while($0.tagName.toLowerCase()!="body"){$0=$0.offsetParent;
$1+=$0.offsetLeft-$0.scrollLeft;
}return $1;
}}),
getClientBoxTop:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.getBoundingClientRect().top;
},
"gecko":function($0){return qx.html.Location.getClientAreaTop($0)-qx.html.Style.getBorderTop($0);
},
"default":function($0){var $1=$0.offsetTop;
while($0.tagName.toLowerCase()!="body"){$0=$0.offsetParent;
$1+=$0.offsetTop-$0.scrollTop;
}return $1;
}}),
getClientBoxRight:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.getBoundingClientRect().right;
},
"default":function($0){return qx.html.Location.getClientBoxLeft($0)+qx.html.Dimension.getBoxWidth($0);
}}),
getClientBoxBottom:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.getBoundingClientRect().bottom;
},
"default":function($0){return qx.html.Location.getClientBoxTop($0)+qx.html.Dimension.getBoxHeight($0);
}}),
getPageBoxLeft:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return qx.html.Location.getClientBoxLeft($0)+qx.html.Scroll.getLeftSum($0);
},
"gecko":function($0){return qx.html.Location.getPageAreaLeft($0)-qx.html.Style.getBorderLeft($0);
},
"default":function($0){var $1=$0.offsetLeft;
while($0.tagName.toLowerCase()!="body"){$0=$0.offsetParent;
$1+=$0.offsetLeft;
}return $1;
}}),
getPageBoxTop:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return qx.html.Location.getClientBoxTop($0)+qx.html.Scroll.getTopSum($0);
},
"gecko":function($0){return qx.html.Location.getPageAreaTop($0)-qx.html.Style.getBorderTop($0);
},
"default":function($0){var $1=$0.offsetTop;
while($0.tagName.toLowerCase()!="body"){$0=$0.offsetParent;
$1+=$0.offsetTop;
}return $1;
}}),
getPageBoxRight:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return qx.html.Location.getClientBoxRight($0)+qx.html.Scroll.getLeftSum($0);
},
"default":function($0){return qx.html.Location.getPageBoxLeft($0)+qx.html.Dimension.getBoxWidth($0);
}}),
getPageBoxBottom:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return qx.html.Location.getClientBoxBottom($0)+qx.html.Scroll.getTopSum($0);
},
"default":function($0){return qx.html.Location.getPageBoxTop($0)+qx.html.Dimension.getBoxHeight($0);
}}),
getClientAreaLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getPageAreaLeft($0)-qx.html.Scroll.getLeftSum($0);
},
"default":function($0){return qx.html.Location.getClientBoxLeft($0)+qx.html.Style.getBorderLeft($0);
}}),
getClientAreaTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getPageAreaTop($0)-qx.html.Scroll.getTopSum($0);
},
"default":function($0){return qx.html.Location.getClientBoxTop($0)+qx.html.Style.getBorderTop($0);
}}),
getClientAreaRight:function($0){return qx.html.Location.getClientAreaLeft($0)+qx.html.Dimension.getAreaWidth($0);
},
getClientAreaBottom:function($0){return qx.html.Location.getClientAreaTop($0)+qx.html.Dimension.getAreaHeight($0);
},
getPageAreaLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){return $0.ownerDocument.getBoxObjectFor($0).x;
},
"default":function($0){return qx.html.Location.getPageBoxLeft($0)+qx.html.Style.getBorderLeft($0);
}}),
getPageAreaTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){return $0.ownerDocument.getBoxObjectFor($0).y;
},
"default":function($0){return qx.html.Location.getPageBoxTop($0)+qx.html.Style.getBorderTop($0);
}}),
getPageAreaRight:function($0){return qx.html.Location.getPageAreaLeft($0)+qx.html.Dimension.getAreaWidth($0);
},
getPageAreaBottom:function($0){return qx.html.Location.getPageAreaTop($0)+qx.html.Dimension.getAreaHeight($0);
},
getClientInnerLeft:function($0){return qx.html.Location.getClientAreaLeft($0)+qx.html.Style.getPaddingLeft($0);
},
getClientInnerTop:function($0){return qx.html.Location.getClientAreaTop($0)+qx.html.Style.getPaddingTop($0);
},
getClientInnerRight:function($0){return qx.html.Location.getClientInnerLeft($0)+qx.html.Dimension.getInnerWidth($0);
},
getClientInnerBottom:function($0){return qx.html.Location.getClientInnerTop($0)+qx.html.Dimension.getInnerHeight($0);
},
getPageInnerLeft:function($0){return qx.html.Location.getPageAreaLeft($0)+qx.html.Style.getPaddingLeft($0);
},
getPageInnerTop:function($0){return qx.html.Location.getPageAreaTop($0)+qx.html.Style.getPaddingTop($0);
},
getPageInnerRight:function($0){return qx.html.Location.getPageInnerLeft($0)+qx.html.Dimension.getInnerWidth($0);
},
getPageInnerBottom:function($0){return qx.html.Location.getPageInnerTop($0)+qx.html.Dimension.getInnerHeight($0);
},
getScreenBoxLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=0;
var $2=$0.parentNode;
while($2.nodeType==1){$1+=$2.scrollLeft;
$2=$2.parentNode;
}return $0.ownerDocument.getBoxObjectFor($0).screenX-$1;
},
"default":function($0){return qx.html.Location.getScreenDocumentLeft($0)+qx.html.Location.getPageBoxLeft($0);
}}),
getScreenBoxTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=0;
var $2=$0.parentNode;
while($2.nodeType==1){$1+=$2.scrollTop;
$2=$2.parentNode;
}return $0.ownerDocument.getBoxObjectFor($0).screenY-$1;
},
"default":function($0){return qx.html.Location.getScreenDocumentTop($0)+qx.html.Location.getPageBoxTop($0);
}}),
getScreenBoxRight:function($0){return qx.html.Location.getScreenBoxLeft($0)+qx.html.Dimension.getBoxWidth($0);
},
getScreenBoxBottom:function($0){return qx.html.Location.getScreenBoxTop($0)+qx.html.Dimension.getBoxHeight($0);
},
getScreenOuterLeft:function($0){return qx.html.Location.getScreenBoxLeft($0)-qx.html.Style.getMarginLeft($0);
},
getScreenOuterTop:function($0){return qx.html.Location.getScreenBoxTop($0)-qx.html.Style.getMarginTop($0);
},
getScreenOuterRight:function($0){return qx.html.Location.getScreenBoxRight($0)+qx.html.Style.getMarginRight($0);
},
getScreenOuterBottom:function($0){return qx.html.Location.getScreenBoxBottom($0)+qx.html.Style.getMarginBottom($0);
},
getScreenAreaLeft:function($0){return qx.html.Location.getScreenBoxLeft($0)+qx.html.Dimension.getInsetLeft($0);
},
getScreenAreaTop:function($0){return qx.html.Location.getScreenBoxTop($0)+qx.html.Dimension.getInsetTop($0);
},
getScreenAreaRight:function($0){return qx.html.Location.getScreenBoxRight($0)-qx.html.Dimension.getInsetRight($0);
},
getScreenAreaBottom:function($0){return qx.html.Location.getScreenBoxBottom($0)-qx.html.Dimension.getInsetBottom($0);
},
getScreenInnerLeft:function($0){return qx.html.Location.getScreenAreaLeft($0)+qx.html.Style.getPaddingLeft($0);
},
getScreenInnerTop:function($0){return qx.html.Location.getScreenAreaTop($0)+qx.html.Style.getPaddingTop($0);
},
getScreenInnerRight:function($0){return qx.html.Location.getScreenAreaRight($0)-qx.html.Style.getPaddingRight($0);
},
getScreenInnerBottom:function($0){return qx.html.Location.getScreenAreaBottom($0)-qx.html.Style.getPaddingBottom($0);
},
getScreenDocumentLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getScreenOuterLeft($0.ownerDocument.body);
},
"default":function($0){return $0.document.parentWindow.screenLeft;
}}),
getScreenDocumentTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getScreenOuterTop($0.ownerDocument.body);
},
"default":function($0){return $0.document.parentWindow.screenTop;
}}),
getScreenDocumentRight:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getScreenOuterRight($0.ownerDocument.body);
},
"default":function($0){}}),
getScreenDocumentBottom:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.html.Location.getScreenOuterBottom($0.ownerDocument.body);
},
"default":function($0){}})}});




/* ID: qx.html.Scroll */
qx.Class.define("qx.html.Scroll",
{statics:{getLeftSum:function($0){var $1=0;
var $2=$0.parentNode;
while($2.nodeType==1){$1+=$2.scrollLeft;
$2=$2.parentNode;
}return $1;
},
getTopSum:function($0){var $1=0;
var $2=$0.parentNode;
while($2.nodeType==1){$1+=$2.scrollTop;
$2=$2.parentNode;
}return $1;
}}});




/* ID: qx.io.image.Manager */
qx.Class.define("qx.io.image.Manager",
{type:"singleton",
extend:qx.core.Target,
construct:function(){arguments.callee.base.call(this);
this.__visible={};
this.__all={};
},
members:{add:function($0){var $1=this.__all;
if($1[$0]===undefined){$1[$0]=1;
}else{$1[$0]++;
}},
remove:function($0){var $1=this.__all;
if($1[$0]!==undefined){$1[$0]--;
}
if($1[$0]<=0){delete $1[$0];
}},
show:function($0){var $1=this.__visible;
if($1[$0]===undefined){$1[$0]=1;
}else{$1[$0]++;
}},
hide:function($0){var $1=this.__visible;
if($1[$0]!==undefined){$1[$0]--;
}
if($1[$0]<=0){delete $1[$0];
}},
getVisibleImages:function(){var $0=this.__visible;
var $1={};
for(var $2 in $0){if($0[$2]>0){$1[$2]=true;
}}return $1;
},
getHiddenImages:function(){var $0=this.__visible;
var $1=this.__all;
var $2={};
for(var $3 in $1){if($0[$3]===undefined){$2[$3]=true;
}}return $2;
}},
destruct:function(){this._disposeFields("__all",
"__visible");
}});




/* ID: qx.html.Offset */
qx.Class.define("qx.html.Offset",
{statics:{getLeft:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0.offsetLeft;
var $2=$0.parentNode;
var $3=qx.html.Style.getStyleProperty($0,
"position");
var $4=qx.html.Style.getStyleProperty($2,
"position");
if($3!="absolute"&&$3!="fixed"){$1-=qx.html.Style.getBorderLeft($2);
}if($4!="absolute"&&$4!="fixed"){while($2){$2=$2.parentNode;
if(!$2||typeof $2.tagName!=="string"){break;
}var $5=qx.html.Style.getStyleProperty($2,
"position");
if($5=="absolute"||$5=="fixed"){$1-=qx.html.Style.getBorderLeft($2)+qx.html.Style.getPaddingLeft($2);
break;
}}}return $1;
},
"default":function($0){return $0.offsetLeft;
}}),
getTop:qx.core.Variant.select("qx.client",
{"gecko":function($0){var $1=$0.offsetTop;
var $2=$0.parentNode;
var $3=qx.html.Style.getStyleProperty($0,
"position");
var $4=qx.html.Style.getStyleProperty($2,
"position");
if($3!="absolute"&&$3!="fixed"){$1-=qx.html.Style.getBorderTop($2);
}if($4!="absolute"&&$4!="fixed"){while($2){$2=$2.parentNode;
if(!$2||typeof $2.tagName!=="string"){break;
}var $5=qx.html.Style.getStyleProperty($2,
"position");
if($5=="absolute"||$5=="fixed"){$1-=qx.html.Style.getBorderTop($2)+qx.html.Style.getPaddingTop($2);
break;
}}}return $1;
},
"default":function($0){return $0.offsetTop;
}})}});




/* ID: qx.html.ScrollIntoView */
qx.Class.define("qx.html.ScrollIntoView",
{statics:{scrollX:function($0,
$1){var $2,
$3,
$4,
$5;
var $6=$0.parentNode;
var $7=$0.offsetLeft;
var $4=$0.offsetWidth;
while($6){switch(qx.html.Style.getStyleProperty($6,
"overflow")){case "scroll":case "auto":case "-moz-scrollbars-horizontal":$5=true;
break;
default:switch(qx.html.Style.getStyleProperty($6,
"overflowX")){case "scroll":case "auto":$5=true;
break;
default:$5=false;
}}
if($5){$2=$6.clientWidth;
$3=$6.scrollLeft;
if($1){$6.scrollLeft=$7;
}else if($1==false){$6.scrollLeft=$7+$4-$2;
}else if($4>$2||$7<$3){$6.scrollLeft=$7;
}else if(($7+$4)>($3+$2)){$6.scrollLeft=$7+$4-$2;
}$7=$6.offsetLeft;
$4=$6.offsetWidth;
}else{$7+=$6.offsetLeft;
}
if($6.tagName.toLowerCase()=="body"){break;
}$6=$6.offsetParent;
}return true;
},
scrollY:function($0,
$1){var $2,
$3,
$4,
$5;
var $6=$0.parentNode;
var $7=$0.offsetTop;
var $4=$0.offsetHeight;
while($6){switch(qx.html.Style.getStyleProperty($6,
"overflow")){case "scroll":case "auto":case "-moz-scrollbars-vertical":$5=true;
break;
default:switch(qx.html.Style.getStyleProperty($6,
"overflowY")){case "scroll":case "auto":$5=true;
break;
default:$5=false;
}}
if($5){$2=$6.clientHeight;
$3=$6.scrollTop;
if($1){$6.scrollTop=$7;
}else if($1==false){$6.scrollTop=$7+$4-$2;
}else if($4>$2||$7<$3){$6.scrollTop=$7;
}else if(($7+$4)>($3+$2)){$6.scrollTop=$7+$4-$2;
}$7=$6.offsetTop;
$4=$6.offsetHeight;
}else{$7+=$6.offsetTop;
}
if($6.tagName.toLowerCase()=="body"){break;
}$6=$6.offsetParent;
}return true;
}}});




/* ID: qx.io.image.PreloaderSystem */
qx.Class.define("qx.io.image.PreloaderSystem",
{extend:qx.core.Target,
construct:function($0,
$1,
$2){arguments.callee.base.call(this);
if($0 instanceof Array){this._list=qx.lang.Object.fromArray($0);
}else{this._list=$0;
}this._timer=new qx.client.Timer(qx.core.Setting.get("qx.preloaderTimeout"));
this._timer.addEventListener("interval",
this.__oninterval,
this);
if($1){this.addEventListener("completed",
$1,
$2||null);
}},
events:{"completed":"qx.event.type.Event"},
members:{_stopped:false,
start:function(){if(qx.lang.Object.isEmpty(this._list)){this.createDispatchEvent("completed");
return;
}
for(var $0 in this._list){var $1=qx.io.image.PreloaderManager.getInstance().create(qx.io.Alias.getInstance().resolve($0));
if($1.isErroneous()||$1.isLoaded()){delete this._list[$0];
}else{$1._origSource=$0;
$1.addEventListener("load",
this.__onload,
this);
$1.addEventListener("error",
this.__onerror,
this);
}}this._check();
},
__onload:function($0){if(this.getDisposed()){return;
}delete this._list[$0.getTarget()._origSource];
this._check();
},
__onerror:function($0){if(this.getDisposed()){return;
}delete this._list[$0.getTarget()._origSource];
this._check();
},
__oninterval:function($0){this.debug("Cannot preload: "+qx.lang.Object.getKeysAsString(this._list));
this._stopped=true;
this._timer.stop();
this.createDispatchEvent("completed");
},
_check:function(){if(this._stopped){return;
}if(qx.lang.Object.isEmpty(this._list)){this._timer.stop();
this.createDispatchEvent("completed");
}else{this._timer.restart();
}}},
settings:{"qx.preloaderTimeout":3000},
destruct:function(){if(this._timer){this._timer.removeEventListener("interval",
this.__oninterval,
this);
this._disposeObjects("_timer");
}this._disposeFields("_list");
}});




/* ID: qx.io.image.PreloaderManager */
qx.Class.define("qx.io.image.PreloaderManager",
{type:"singleton",
extend:qx.core.Object,
construct:function(){arguments.callee.base.call(this);
this._objects={};
},
members:{add:function($0){this._objects[$0.getUri()]=$0;
},
remove:function($0){delete this._objects[$0.getUri()];
},
has:function($0){return this._objects[$0]!=null;
},
get:function($0){return this._objects[$0];
},
create:function($0){if(this._objects[$0]){return this._objects[$0];
}return new qx.io.image.Preloader($0);
}},
destruct:function(){this._disposeFields("_objects");
}});




/* ID: qx.io.image.Preloader */
qx.Class.define("qx.io.image.Preloader",
{extend:qx.core.Target,
events:{"load":"qx.event.type.Event",
"error":"qx.event.type.Event"},
construct:function($0){if(qx.io.image.PreloaderManager.getInstance().has($0)){this.debug("Reuse qx.io.image.Preloader in old-style!");
this.debug("Please use qx.io.image.PreloaderManager.getInstance().create(source) instead!");
return qx.io.image.PreloaderManager.getInstance().get($0);
}arguments.callee.base.call(this);
this._element=new Image;
this._element.onload=qx.lang.Function.bind(this.__onload,
this);
this._element.onerror=qx.lang.Function.bind(this.__onerror,
this);
this._source=$0;
this._element.src=$0;
if(qx.core.Variant.isSet("qx.client",
"mshtml")){this._isPng=/\.png$/i.test(this._element.nameProp);
}qx.io.image.PreloaderManager.getInstance().add(this);
},
members:{_source:null,
_isLoaded:false,
_isErroneous:false,
getUri:function(){return this._source;
},
getSource:function(){return this._source;
},
isLoaded:function(){return this._isLoaded;
},
isErroneous:function(){return this._isErroneous;
},
_isPng:false,
getIsPng:function(){return this._isPng;
},
getWidth:qx.core.Variant.select("qx.client",
{"gecko":function(){return this._element.naturalWidth;
},
"default":function(){return this._element.width;
}}),
getHeight:qx.core.Variant.select("qx.client",
{"gecko":function(){return this._element.naturalHeight;
},
"default":function(){return this._element.height;
}}),
__onload:function(){if(this._isLoaded||this._isErroneous){return;
}this._isLoaded=true;
this._isErroneous=false;
if(this.hasEventListeners("load")){this.dispatchEvent(new qx.event.type.Event("load"),
true);
}},
__onerror:function(){if(this._isLoaded||this._isErroneous){return;
}this.debug("Could not load: "+this._source);
this._isLoaded=false;
this._isErroneous=true;
if(this.hasEventListeners("error")){this.dispatchEvent(new qx.event.type.Event("error"),
true);
}}},
destruct:function(){if(this._element){this._element.onload=this._element.onerror=null;
}this._disposeFields("_element",
"_isLoaded",
"_isErroneous",
"_isPng");
}});




/* ID: spagobi.test.TestApplication */
qx.Class.define("spagobi.test.TestApplication",
{extend:qx.application.Gui,
settings:{"spagobi.imageUri":"../img"},
members:{main:function(){arguments.callee.base.call(this);
var $0=qx.ui.core.ClientDocument.getInstance();
var $1=new qx.ui.pageview.buttonview.ButtonView;
$1.setEdge(20);
var $2=spagobi.commons.WidgetUtils.function_label("Engine Details");
var $3=new qx.ui.pageview.buttonview.Button("",
"");
$3.setLeft(790);
$3.setDisplay(false);
var $4=new qx.ui.form.Button("",
"icon/32/devices/video-display.png");
$4.setLeft(800);
var $5=new qx.ui.popup.ToolTip("Save",
"");
$4.setToolTip($5);
$5.setShowInterval(10);
function $6(){if($s.getValue()=="")alert('Label is a Mandatory field !!');
if($v.getValue()=="")alert('Name is a Mandatory field !!');
if($i.getDisplay()==true){if($i.getValue()=="")alert('Class is a Mandatory field !!');
}else if($l.getDisplay()==true){if($l.getValue()=="")alert('Url is a Mandatory field !!');
if($o.getValue()=="")alert('Driver name is a Mandatory field !!');
}}$4.addEventListener("execute",
$6);
var $7=new qx.ui.form.Button("",
"icon/32/apps/graphics-snapshot.png");
$7.setLeft(810);
var $8=new qx.ui.popup.ToolTip("Back",
"");
$7.setToolTip($8);
$8.setShowInterval(10);
function $9(){$e.setDisplay(false);
$2.setDisplay(false);
$4.setDisplay(false);
$7.setDisplay(false);
$a.setChecked(true);
}$7.addEventListener("execute",
$9);
var $a=new qx.ui.pageview.buttonview.Button("",
"");
$a.setLeft(790);
$a.setChecked(true);
$a.setDisplay(false);
var $b=new qx.ui.form.Button("Go to Page 1");
$b.setLeft(650);
$b.setTop(200);
var $c=new qx.ui.popup.ToolTip("Go",
"");
$b.setToolTip($c);
$c.setShowInterval(10);
function $d(){$3.setChecked(true);
$2.setDisplay(true);
$4.setDisplay(true);
$7.setDisplay(true);
}$b.addEventListener("execute",
$d);
var $e=new qx.ui.pageview.buttonview.Page($3);
var $f=new qx.ui.pageview.buttonview.Page($a);
$f.add($7);
$f.add($b);
$1.getBar().add($2,
$3,
$4,
$7,
$a);
$a.setChecked(true);
$2.setDisplay(false);
$4.setDisplay(false);
$7.setDisplay(false);
var $g,
$h,
$i,
$j,
$k,
$l,
$m,
$n,
$o,
$p,
$q;
var $r=spagobi.commons.WidgetUtils.function_label("Label",
undefined,
20,
10);
var $s=spagobi.commons.WidgetUtils.function_textfield(undefined,
20,
100,
20,
200,
20);
var $t=spagobi.commons.WidgetUtils.function_label("*",
undefined,
20,
302);
var $u=spagobi.commons.WidgetUtils.function_label("Name",
undefined,
50,
10);
var $v=spagobi.commons.WidgetUtils.function_textfield(undefined,
50,
100,
undefined,
200,
20);
var $w=spagobi.commons.WidgetUtils.function_label("*",
undefined,
50,
302);
var $x=spagobi.commons.WidgetUtils.function_label("Description",
undefined,
80,
10);
var $y=spagobi.commons.WidgetUtils.function_textfield(undefined,
80,
100,
undefined,
200,
20);
var $z=spagobi.commons.WidgetUtils.function_label("Document type",
undefined,
110,
10);
var $A=["Report",
"Map"];
var $B=spagobi.commons.WidgetUtils.function_combo($B,
110,
100,
$A);
var $C=spagobi.commons.WidgetUtils.function_label("Engine type",
undefined,
140,
10);
var $D=["Internal",
"External"];
var $E=spagobi.commons.WidgetUtils.function_combo($E,
140,
100,
$D);
$p=spagobi.commons.WidgetUtils.function_label("Data Source",
undefined,
230,
10);
var $F=["  ",
"Foodmart",
"SpagoBI",
"GeoData"];
$q=spagobi.commons.WidgetUtils.function_combo($q,
230,
100,
$F);
$q.setDisplay(false);
$g=spagobi.commons.WidgetUtils.function_label("Class");
$i=spagobi.commons.WidgetUtils.function_textfield(undefined,
undefined,
undefined,
undefined,
200,
20);
$h=spagobi.commons.WidgetUtils.function_label("*");
function_Int=function($G){$g=spagobi.commons.WidgetUtils.function_label(undefined,
$g,
230+$G,
10);
$i=spagobi.commons.WidgetUtils.function_textfield($i,
230+$G,
100,
undefined,
undefined,
undefined);
$h=spagobi.commons.WidgetUtils.function_label(undefined,
$h,
230+$G,
302);
$g.setDisplay(true);
$i.setDisplay(true);
$h.setDisplay(true);
$e.add($g,
$i,
$h);
};
$j=spagobi.commons.WidgetUtils.function_label("Url");
$l=spagobi.commons.WidgetUtils.function_textfield(undefined,
undefined,
undefined,
undefined,
200,
20);
$k=spagobi.commons.WidgetUtils.function_label("*");
$m=spagobi.commons.WidgetUtils.function_label("Driver Name");
$o=spagobi.commons.WidgetUtils.function_textfield(undefined,
undefined,
undefined,
undefined,
200,
20);
$n=spagobi.commons.WidgetUtils.function_label("*");
function_Ext=function($H){$j=spagobi.commons.WidgetUtils.function_label(undefined,
$j,
230+$H,
10);
$l=spagobi.commons.WidgetUtils.function_textfield($l,
230+$H,
100,
undefined,
undefined,
undefined);
$k=spagobi.commons.WidgetUtils.function_label(undefined,
$k,
230+$H,
302);
$m=spagobi.commons.WidgetUtils.function_label(undefined,
$m,
260+$H,
10);
$o=spagobi.commons.WidgetUtils.function_textfield($o,
260+$H,
100,
undefined,
undefined,
undefined);
$n=spagobi.commons.WidgetUtils.function_label(undefined,
$n,
260+$H,
302);
$j.setDisplay(true);
$l.setDisplay(true);
$k.setDisplay(true);
$m.setDisplay(true);
$o.setDisplay(true);
$n.setDisplay(true);
$e.add($j,
$l,
$k,
$m,
$o,
$n);
};
var $I=0;
function_Int($I);
function_Int_Ext=function($G,
$J){if($J.getValue()=="Internal"){if($j.getDisplay()==true){$j.setDisplay(false);
$l.setDisplay(false);
$k.setDisplay(false);
$m.setDisplay(false);
$o.setDisplay(false);
$n.setDisplay(false);
}function_Int($G);
}else if($J.getValue()=="External"){if($g.getDisplay()==true){$g.setDisplay(false);
$i.setDisplay(false);
$h.setDisplay(false);
}function_Ext($G);
}};
$E.addEventListener("changeValue",
function($J){var $G;
if($q.getDisplay()==true){$G=30;
function_Int_Ext($G,
$J);
}else{$G=0;
function_Int_Ext($G,
$J);
}});
var $K=spagobi.commons.WidgetUtils.function_checkbox($K,
"Use Data Set",
170,
10,
true);
var $L=spagobi.commons.WidgetUtils.function_checkbox($L,
"Use Data Source",
200,
10);
$L.addEventListener("changeChecked",
function($J){if($J.getValue()==true){$p.setDisplay(true);
$q.setDisplay(true);
$e.add($p,
$q);
var $H=30;
if($g.getDisplay()==true){$g=spagobi.commons.WidgetUtils.function_label(undefined,
$g,
230+$H,
10);
$i=spagobi.commons.WidgetUtils.function_textfield($i,
230+$H,
100,
undefined,
undefined,
undefined);
$h=spagobi.commons.WidgetUtils.function_label(undefined,
$h,
230+$H,
302);
}else if($j.getDisplay()==true){$j=spagobi.commons.WidgetUtils.function_label(undefined,
$j,
230+$H,
10);
$l=spagobi.commons.WidgetUtils.function_textfield($l,
230+$H,
100,
undefined,
undefined,
undefined);
$k=spagobi.commons.WidgetUtils.function_label(undefined,
$k,
230+$H,
302);
$m=spagobi.commons.WidgetUtils.function_label(undefined,
$m,
260+$H,
10);
$o=spagobi.commons.WidgetUtils.function_textfield($o,
260+$H,
100,
undefined,
undefined,
undefined);
$n=spagobi.commons.WidgetUtils.function_label(undefined,
$n,
260+$H,
302);
}}else{$p.setDisplay(false);
$q.setDisplay(false);
$H=0;
if($g.getDisplay()==true){$g=spagobi.commons.WidgetUtils.function_label(undefined,
$g,
230+$H,
10);
$i=spagobi.commons.WidgetUtils.function_textfield($i,
230+$H,
100,
undefined,
undefined,
undefined);
$h=spagobi.commons.WidgetUtils.function_label(undefined,
$h,
230+$H,
302);
}else if($j.getDisplay()==true){$j=spagobi.commons.WidgetUtils.function_label(undefined,
$j,
230+$H,
10);
$l=spagobi.commons.WidgetUtils.function_textfield($l,
230+$H,
100,
undefined,
undefined,
undefined);
$k=spagobi.commons.WidgetUtils.function_label(undefined,
$k,
230+$H,
302);
$m=spagobi.commons.WidgetUtils.function_label(undefined,
$m,
260+$H,
10);
$o=spagobi.commons.WidgetUtils.function_textfield($o,
260+$H,
100,
undefined,
undefined,
undefined);
$n=spagobi.commons.WidgetUtils.function_label(undefined,
$n,
260+$H,
302);
}}});
$e.add($r,
$s,
$t,
$u,
$v,
$w,
$x,
$y,
$z,
$B,
$C,
$E,
$K,
$L);
$1.getPane().add($e,
$f);
$0.add($1);
},
close:function(){arguments.callee.base.call(this);
},
terminate:function(){arguments.callee.base.call(this);
}}});




/* ID: qx.ui.layout.BoxLayout */
qx.Class.define("qx.ui.layout.BoxLayout",
{extend:qx.ui.core.Parent,
construct:function($0){arguments.callee.base.call(this);
if($0!=null){this.setOrientation($0);
}else{this.initOrientation();
}},
statics:{STR_REVERSED:"-reversed"},
properties:{orientation:{check:["horizontal",
"vertical"],
init:"horizontal",
apply:"_applyOrientation",
event:"changeOrientation"},
spacing:{check:"Integer",
init:0,
themeable:true,
apply:"_applySpacing",
event:"changeSpacing"},
horizontalChildrenAlign:{check:["left",
"center",
"right"],
init:"left",
themeable:true,
apply:"_applyHorizontalChildrenAlign"},
verticalChildrenAlign:{check:["top",
"middle",
"bottom"],
init:"top",
themeable:true,
apply:"_applyVerticalChildrenAlign"},
reverseChildrenOrder:{check:"Boolean",
init:false,
apply:"_applyReverseChildrenOrder"},
stretchChildrenOrthogonalAxis:{check:"Boolean",
init:true,
apply:"_applyStretchChildrenOrthogonalAxis"},
useAdvancedFlexAllocation:{check:"Boolean",
init:false,
apply:"_applyUseAdvancedFlexAllocation"},
accumulatedChildrenOuterWidth:{_cached:true,
defaultValue:null},
accumulatedChildrenOuterHeight:{_cached:true,
defaultValue:null}},
members:{_createLayoutImpl:function(){return this.getOrientation()=="vertical"?new qx.ui.layout.impl.VerticalBoxLayoutImpl(this):new qx.ui.layout.impl.HorizontalBoxLayoutImpl(this);
},
_layoutHorizontal:false,
_layoutVertical:false,
_layoutMode:"left",
isHorizontal:function(){return this._layoutHorizontal;
},
isVertical:function(){return this._layoutVertical;
},
getLayoutMode:function(){if(this._layoutMode==null){this._updateLayoutMode();
}return this._layoutMode;
},
_updateLayoutMode:function(){this._layoutMode=this._layoutVertical?this.getVerticalChildrenAlign():this.getHorizontalChildrenAlign();
if(this.getReverseChildrenOrder()){this._layoutMode+=qx.ui.layout.BoxLayout.STR_REVERSED;
}},
_invalidateLayoutMode:function(){this._layoutMode=null;
},
_applyOrientation:function($0,
$1){this._layoutHorizontal=$0=="horizontal";
this._layoutVertical=$0=="vertical";
if(this._layoutImpl){this._layoutImpl.dispose();
this._layoutImpl=null;
}
if($0){this._layoutImpl=this._createLayoutImpl();
}this._doLayoutOrder($0,
$1);
this.addToQueueRuntime("orientation");
},
_applySpacing:function($0,
$1){this._doLayout();
this.addToQueueRuntime("spacing");
},
_applyHorizontalChildrenAlign:function($0,
$1){this._doLayoutOrder();
this.addToQueueRuntime("horizontalChildrenAlign");
},
_applyVerticalChildrenAlign:function($0,
$1){this._doLayoutOrder();
this.addToQueueRuntime("verticalChildrenAlign");
},
_applyReverseChildrenOrder:function($0,
$1){this._doLayoutOrder();
this.addToQueueRuntime("reverseChildrenOrder");
},
_applyStretchChildrenOrthogonalAxis:function($0,
$1){this.addToQueueRuntime("stretchChildrenOrthogonalAxis");
},
_applyUseAdvancedFlexAllocation:function($0,
$1){this.addToQueueRuntime("useAdvancedFlexAllocation");
},
_doLayoutOrder:function(){this._invalidateLayoutMode();
this._doLayout();
},
_doLayout:function(){this._invalidatePreferredInnerDimensions();
this._invalidateAccumulatedChildrenOuterWidth();
this._invalidateAccumulatedChildrenOuterHeight();
},
_computeAccumulatedChildrenOuterWidth:function(){var $0=this.getVisibleChildren(),
$1,
$2=-1,
$3=this.getSpacing(),
$4=-$3;
while($1=$0[++$2]){$4+=$1.getOuterWidth()+$3;
}return $4;
},
_computeAccumulatedChildrenOuterHeight:function(){var $0=this.getVisibleChildren(),
$1,
$2=-1,
$3=this.getSpacing(),
$4=-$3;
while($1=$0[++$2]){$4+=$1.getOuterHeight()+$3;
}return $4;
},
_recomputeChildrenStretchingX:function(){var $0=this.getVisibleChildren(),
$1,
$2=-1;
while($1=$0[++$2]){if($1._recomputeStretchingX()&&$1._recomputeBoxWidth()){$1._recomputeOuterWidth();
}}},
_recomputeChildrenStretchingY:function(){var $0=this.getVisibleChildren(),
$1,
$2=-1;
while($1=$0[++$2]){if($1._recomputeStretchingY()&&$1._recomputeBoxHeight()){$1._recomputeOuterHeight();
}}}}});




/* ID: qx.ui.layout.impl.VerticalBoxLayoutImpl */
qx.Class.define("qx.ui.layout.impl.VerticalBoxLayoutImpl",
{extend:qx.ui.layout.impl.LayoutImpl,
properties:{enableFlexSupport:{check:"Boolean",
init:true}},
members:{computeChildBoxWidth:function($0){if(this.getWidget().getStretchChildrenOrthogonalAxis()&&$0._computedWidthTypeNull&&$0.getAllowStretchX()){return this.getWidget().getInnerWidth();
}return $0.getWidthValue()||$0._computeBoxWidthFallback();
},
computeChildBoxHeight:function($0){return $0.getHeightValue()||$0._computeBoxHeightFallback();
},
computeChildrenFlexHeight:function(){if(this._childrenFlexHeightComputed||!this.getEnableFlexSupport()){return;
}this._childrenFlexHeightComputed=true;
var $0=this.getWidget();
var $1=$0.getVisibleChildren();
var $2=$1.length;
var $3;
var $4=[];
var $5=$0.getInnerHeight();
var $6=$0.getSpacing()*($2-1);
var $7;
for($7=0;$7<$2;$7++){$3=$1[$7];
if($3._computedHeightTypeFlex){$4.push($3);
if($0._computedHeightTypeAuto){$6+=$3.getPreferredBoxHeight();
}}else{$6+=$3.getOuterHeight();
}}var $8=$5-$6;
var $9=$4.length;
var $a=0;
for($7=0;$7<$9;$7++){$a+=$4[$7]._computedHeightParsed;
}var $b=$8/$a;
if(!$0.getUseAdvancedFlexAllocation()){for($7=0;$7<$9;$7++){$3=$4[$7];
$3._computedHeightFlexValue=Math.round($3._computedHeightParsed*$b);
$6+=$3._computedHeightFlexValue;
}}else{var $c=0;
var $d,
$9,
$e,
$f,
$g,
$h;
for($7=0;$7<$9;$7++){$3=$4[$7];
$h=$3._computedHeightFlexValue=$3._computedHeightParsed*$b;
$c+=$h-qx.lang.Number.limit($h,
$3.getMinHeightValue(),
$3.getMaxHeightValue());
}$c=Math.round($c);
if($c==0){for($7=0;$7<$9;$7++){$3=$4[$7];
$3._computedHeightFlexValue=Math.round($3._computedHeightFlexValue);
$6+=$3._computedHeightFlexValue;
}}else{var $i=$c>0;
for($7=$9-1;$7>=0;$7--){$3=$4[$7];
if($i){$e=($3.getMaxHeightValue()||Infinity)-$3._computedHeightFlexValue;
if($e>0){$3._allocationLoops=Math.floor($e/$3._computedHeightParsed);
}else{qx.lang.Array.removeAt($4,
$7);
$3._computedHeightFlexValue=Math.round($3._computedHeightFlexValue);
$6+=Math.round($3._computedHeightFlexValue+$e);
}}else{$e=qx.util.Validation.isValidNumber($3.getMinHeightValue())?$3._computedHeightFlexValue-$3.getMinHeightValue():$3._computedHeightFlexValue;
if($e>0){$3._allocationLoops=Math.floor($e/$3._computedHeightParsed);
}else{qx.lang.Array.removeAt($4,
$7);
$3._computedHeightFlexValue=Math.round($3._computedHeightFlexValue);
$6+=Math.round($3._computedHeightFlexValue-$e);
}}}while($c!=0&&$9>0){$9=$4.length;
$d=Infinity;
$g=0;
for($7=0;$7<$9;$7++){$d=Math.min($d,
$4[$7]._allocationLoops);
$g+=$4[$7]._computedHeightParsed;
}$f=Math.min($g*$d,
$c);
$c-=$f;
for($7=$9-1;$7>=0;$7--){$3=$4[$7];
$3._computedHeightFlexValue+=$f/$g*$3._computedHeightParsed;
if($3._allocationLoops==$d){$3._computedHeightFlexValue=Math.round($3._computedHeightFlexValue);
$6+=$3._computedHeightFlexValue;
delete $3._allocationLoops;
qx.lang.Array.removeAt($4,
$7);
}else{if($c==0){$3._computedHeightFlexValue=Math.round($3._computedHeightFlexValue);
$6+=$3._computedHeightFlexValue;
delete $3._allocationLoops;
}else{$3._allocationLoops-=$d;
}}}}}}$3._computedHeightFlexValue+=$5-$6;
},
invalidateChildrenFlexHeight:function(){delete this._childrenFlexHeightComputed;
},
computeChildrenNeededHeight:function(){var $0=this.getWidget();
return qx.ui.layout.impl.LayoutImpl.prototype.computeChildrenNeededHeight_sum.call(this)+(($0.getVisibleChildrenLength()-1)*$0.getSpacing());
},
updateSelfOnChildOuterHeightChange:function($0){this.getWidget()._invalidateAccumulatedChildrenOuterHeight();
},
updateChildOnInnerWidthChange:function($0){var $1=$0._recomputePercentX();
var $2=$0._recomputeStretchingX();
if(($0.getHorizontalAlign()||this.getWidget().getHorizontalChildrenAlign())=="center"){$0.addToLayoutChanges("locationX");
}return $1||$2;
},
updateChildOnInnerHeightChange:function($0){if(this.getWidget().getVerticalChildrenAlign()=="middle"){$0.addToLayoutChanges("locationY");
}var $1=$0._recomputePercentY();
var $2=$0._recomputeFlexY();
return $1||$2;
},
updateSelfOnJobQueueFlush:function($0){if($0.addChild||$0.removeChild){this.getWidget()._invalidateAccumulatedChildrenOuterHeight();
}},
updateChildrenOnJobQueueFlush:function($0){var $1=false,
$2=false;
var $3=this.getWidget();
if($0.orientation){$1=$2=true;
}if($0.spacing||$0.orientation||$0.reverseChildrenOrder||$0.verticalChildrenAlign){$3._addChildrenToLayoutQueue("locationY");
}
if($0.horizontalChildrenAlign){$3._addChildrenToLayoutQueue("locationX");
}
if($0.stretchChildrenOrthogonalAxis){$1=true;
}if($1){$3._recomputeChildrenStretchingX();
$3._addChildrenToLayoutQueue("width");
}
if($2){$3._recomputeChildrenStretchingY();
$3._addChildrenToLayoutQueue("height");
}return true;
},
updateChildrenOnRemoveChild:function($0,
$1){var $2=this.getWidget(),
$3=$2.getVisibleChildren(),
$4=$3.length,
$5,
$6=-1;
if(this.getEnableFlexSupport()){for(var $6=0;$6<$4;$6++){$5=$3[$6];
if($5.getHasFlexY()){$1=Math.min($1,
$6);
break;
}}$6=-1;
}switch($2.getLayoutMode()){case "bottom":case "top-reversed":while(($5=$3[++$6])&&$6<$1){$5.addToLayoutChanges("locationY");
}break;
case "middle":case "middle-reversed":while($5=$3[++$6]){$5.addToLayoutChanges("locationY");
}break;
default:$6+=$1;
while($5=$3[++$6]){$5.addToLayoutChanges("locationY");
}}},
updateChildrenOnMoveChild:function($0,
$1,
$2){var $3=this.getWidget().getVisibleChildren();
var $4=Math.min($1,
$2);
var $5=Math.max($1,
$2)+1;
for(var $6=$4;$6<$5;$6++){$3[$6].addToLayoutChanges("locationY");
}},
flushChildrenQueue:function($0){var $1=this.getWidget(),
$2=$1.getVisibleChildren(),
$3=$2.length,
$4,
$5;
if(this.getEnableFlexSupport()){this.invalidateChildrenFlexHeight();
for($5=0;$5<$3;$5++){$4=$2[$5];
if($4.getHasFlexY()){$4._computedHeightValue=null;
if($4._recomputeBoxHeight()){$4._recomputeOuterHeight();
$4._recomputeInnerHeight();
}$0[$4.toHashCode()]=$4;
$4._layoutChanges.height=true;
}}}
switch($1.getLayoutMode()){case "bottom":case "top-reversed":for(var $5=$3-1;$5>=0&&!$0[$2[$5].toHashCode()];$5--){}for(var $6=0;$6<=$5;$6++){$1._layoutChild($4=$2[$6]);
}break;
case "middle":case "middle-reversed":$5=-1;
while($4=$2[++$5]){$1._layoutChild($4);
}break;
default:$5=-1;
var $7=false;
while($4=$2[++$5]){if($7||$0[$4.toHashCode()]){$1._layoutChild($4);
$7=true;
}}}},
layoutChild:function($0,
$1){this.layoutChild_sizeX($0,
$1);
this.layoutChild_sizeY($0,
$1);
this.layoutChild_sizeLimitX($0,
$1);
this.layoutChild_sizeLimitY($0,
$1);
this.layoutChild_locationX($0,
$1);
this.layoutChild_locationY($0,
$1);
this.layoutChild_marginX($0,
$1);
this.layoutChild_marginY($0,
$1);
},
layoutChild_sizeX:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.width||$1.minWidth||$1.maxWidth){if(($0._isWidthEssential()&&(!$0._computedWidthTypeNull||!$0._computedMinWidthTypeNull||!$0._computedMaxWidthTypeNull))||($0.getAllowStretchX()&&this.getWidget().getStretchChildrenOrthogonalAxis())){$0._renderRuntimeWidth($0.getBoxWidth());
}else{$0._resetRuntimeWidth();
}}},
"default":function($0,
$1){if($1.initial||$1.width){if($0._isWidthEssential()&&!$0._computedWidthTypeNull){$0._renderRuntimeWidth($0.getWidthValue());
}else{$0._resetRuntimeWidth();
}}}}),
layoutChild_sizeY:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.height||$1.minHeight||$1.maxHeight){if($0._isHeightEssential()&&(!$0._computedHeightTypeNull||!$0._computedMinHeightTypeNull||!$0._computedMaxHeightTypeNull)){$0._renderRuntimeHeight($0.getBoxHeight());
}else{$0._resetRuntimeHeight();
}}},
"default":function($0,
$1){if($1.initial||$1.height){if($0._isHeightEssential()&&!$0._computedHeightTypeNull){$0._renderRuntimeHeight($0.getHeightValue());
}else{$0._resetRuntimeHeight();
}}}}),
layoutChild_locationY:function($0,
$1){var $2=this.getWidget();
if($2.getFirstVisibleChild()==$0){switch($2.getLayoutMode()){case "bottom":case "top-reversed":var $3=$2.getPaddingBottom()+$2.getAccumulatedChildrenOuterHeight()-$0.getOuterHeight();
break;
case "middle":case "middle-reversed":var $3=$2.getPaddingTop()+Math.round(($2.getInnerHeight()-$2.getAccumulatedChildrenOuterHeight())/2);
break;
default:var $3=$2.getPaddingTop();
}}else{var $4=$0.getPreviousVisibleSibling();
switch($2.getLayoutMode()){case "bottom":case "top-reversed":var $3=$4._cachedLocationVertical-$0.getOuterHeight()-$2.getSpacing();
break;
default:var $3=$4._cachedLocationVertical+$4.getOuterHeight()+$2.getSpacing();
}}$0._cachedLocationVertical=$3;
switch(this.getWidget().getLayoutMode()){case "bottom":case "bottom-reversed":case "middle-reversed":$3+=!$0._computedBottomTypeNull?$0.getBottomValue():!$0._computedTopTypeNull?-($0.getTopValue()):0;
$0._resetRuntimeTop();
$0._renderRuntimeBottom($3);
break;
default:$3+=!$0._computedTopTypeNull?$0.getTopValue():!$0._computedBottomTypeNull?-($0.getBottomValue()):0;
$0._resetRuntimeBottom();
$0._renderRuntimeTop($3);
}},
layoutChild_locationX:function($0,
$1){var $2=this.getWidget();
if(qx.core.Variant.isSet("qx.client",
"gecko")){if($0.getAllowStretchX()&&$2.getStretchChildrenOrthogonalAxis()&&$0._computedWidthTypeNull){$0._renderRuntimeLeft($2.getPaddingLeft()||0);
$0._renderRuntimeRight($2.getPaddingRight()||0);
return;
}}var $3=$0.getHorizontalAlign()||$2.getHorizontalChildrenAlign();
var $4=$3=="center"?Math.round(($2.getInnerWidth()-$0.getOuterWidth())/2):0;
if($3=="right"){$4+=$2.getPaddingRight();
if(!$0._computedRightTypeNull){$4+=$0.getRightValue();
}else if(!$0._computedLeftTypeNull){$4-=$0.getLeftValue();
}$0._resetRuntimeLeft();
$0._renderRuntimeRight($4);
}else{$4+=$2.getPaddingLeft();
if(!$0._computedLeftTypeNull){$4+=$0.getLeftValue();
}else if(!$0._computedRightTypeNull){$4-=$0.getRightValue();
}$0._resetRuntimeRight();
$0._renderRuntimeLeft($4);
}}}});




/* ID: qx.util.Validation */
qx.Class.define("qx.util.Validation",
{statics:{isValid:function($0){switch(typeof $0){case "undefined":return false;
case "object":return $0!==null;
case "string":return $0!=="";
case "number":return !isNaN($0);
case "function":case "boolean":return true;
}return false;
},
isInvalid:function($0){switch(typeof $0){case "undefined":return true;
case "object":return $0===null;
case "string":return $0==="";
case "number":return isNaN($0);
case "function":case "boolean":return false;
}return true;
},
isValidNumber:function($0){return typeof $0==="number"&&!isNaN($0);
},
isInvalidNumber:function($0){return typeof $0!=="number"||isNaN($0);
},
isValidString:function($0){return typeof $0==="string"&&$0!=="";
},
isInvalidString:function($0){return typeof $0!=="string"||$0==="";
},
isValidArray:function($0){return typeof $0==="object"&&$0!==null&&$0 instanceof Array;
},
isInvalidArray:function($0){return typeof $0!=="object"||$0===null||!($0 instanceof Array);
},
isValidObject:function($0){return typeof $0==="object"&&$0!==null&&!($0 instanceof Array);
},
isInvalidObject:function($0){return typeof $0!=="object"||$0===null||$0 instanceof Array;
},
isValidNode:function($0){return typeof $0==="object"&&$0!==null;
},
isInvalidNode:function($0){return typeof $0!=="object"||$0===null;
},
isValidElement:function($0){return typeof $0==="object"&&$0!==null||$0.nodeType!==1;
},
isInvalidElement:function($0){return typeof $0!=="object"||$0===null||$0.nodeType!==1;
},
isValidFunction:function($0){return typeof $0==="function";
},
isInvalidFunction:function($0){return typeof $0!=="function";
},
isValidBoolean:function($0){return typeof $0==="boolean";
},
isInvalidBoolean:function($0){return typeof $0!=="boolean";
},
isValidStringOrNumber:function($0){switch(typeof $0){case "string":return $0!=="";
case "number":return !isNaN($0);
}return false;
},
isInvalidStringOrNumber:function($0){switch(typeof $0){case "string":return $0==="";
case "number":return isNaN($0);
}return false;
}}});




/* ID: qx.ui.layout.impl.HorizontalBoxLayoutImpl */
qx.Class.define("qx.ui.layout.impl.HorizontalBoxLayoutImpl",
{extend:qx.ui.layout.impl.LayoutImpl,
properties:{enableFlexSupport:{check:"Boolean",
init:true}},
members:{computeChildBoxWidth:function($0){return $0.getWidthValue()||$0._computeBoxWidthFallback();
},
computeChildBoxHeight:function($0){if(this.getWidget().getStretchChildrenOrthogonalAxis()&&$0._computedHeightTypeNull&&$0.getAllowStretchY()){return this.getWidget().getInnerHeight();
}return $0.getHeightValue()||$0._computeBoxHeightFallback();
},
computeChildrenFlexWidth:function(){if(this._childrenFlexWidthComputed||!this.getEnableFlexSupport()){return;
}this._childrenFlexWidthComputed=true;
var $0=this.getWidget();
var $1=$0.getVisibleChildren();
var $2=$1.length;
var $3;
var $4=[];
var $5=$0.getInnerWidth();
var $6=$0.getSpacing()*($2-1);
var $7;
for($7=0;$7<$2;$7++){$3=$1[$7];
if($3._computedWidthTypeFlex){$4.push($3);
if($0._computedWidthTypeAuto){$6+=$3.getPreferredBoxWidth();
}}else{$6+=$3.getOuterWidth();
}}var $8=$5-$6;
var $9=$4.length;
var $a=0;
for($7=0;$7<$9;$7++){$a+=$4[$7]._computedWidthParsed;
}var $b=$8/$a;
if(!$0.getUseAdvancedFlexAllocation()){for($7=0;$7<$9;$7++){$3=$4[$7];
$3._computedWidthFlexValue=Math.round($3._computedWidthParsed*$b);
$6+=$3._computedWidthFlexValue;
}}else{var $c=0;
var $d,
$9,
$e,
$f,
$g,
$h;
for($7=0;$7<$9;$7++){$3=$4[$7];
$h=$3._computedWidthFlexValue=$3._computedWidthParsed*$b;
$c+=$h-qx.lang.Number.limit($h,
$3.getMinWidthValue(),
$3.getMaxWidthValue());
}$c=Math.round($c);
if($c==0){for($7=0;$7<$9;$7++){$3=$4[$7];
$3._computedWidthFlexValue=Math.round($3._computedWidthFlexValue);
$6+=$3._computedWidthFlexValue;
}}else{var $i=$c>0;
for($7=$9-1;$7>=0;$7--){$3=$4[$7];
if($i){$e=($3.getMaxWidthValue()||Infinity)-$3._computedWidthFlexValue;
if($e>0){$3._allocationLoops=Math.floor($e/$3._computedWidthParsed);
}else{qx.lang.Array.removeAt($4,
$7);
$3._computedWidthFlexValue=Math.round($3._computedWidthFlexValue);
$6+=Math.round($3._computedWidthFlexValue+$e);
}}else{$e=qx.util.Validation.isValidNumber($3.getMinWidthValue())?$3._computedWidthFlexValue-$3.getMinWidthValue():$3._computedWidthFlexValue;
if($e>0){$3._allocationLoops=Math.floor($e/$3._computedWidthParsed);
}else{qx.lang.Array.removeAt($4,
$7);
$3._computedWidthFlexValue=Math.round($3._computedWidthFlexValue);
$6+=Math.round($3._computedWidthFlexValue-$e);
}}}while($c!=0&&$9>0){$9=$4.length;
$d=Infinity;
$g=0;
for($7=0;$7<$9;$7++){$d=Math.min($d,
$4[$7]._allocationLoops);
$g+=$4[$7]._computedWidthParsed;
}$f=Math.min($g*$d,
$c);
$c-=$f;
for($7=$9-1;$7>=0;$7--){$3=$4[$7];
$3._computedWidthFlexValue+=$f/$g*$3._computedWidthParsed;
if($3._allocationLoops==$d){$3._computedWidthFlexValue=Math.round($3._computedWidthFlexValue);
$6+=$3._computedWidthFlexValue;
delete $3._allocationLoops;
qx.lang.Array.removeAt($4,
$7);
}else{if($c==0){$3._computedWidthFlexValue=Math.round($3._computedWidthFlexValue);
$6+=$3._computedWidthFlexValue;
delete $3._allocationLoops;
}else{$3._allocationLoops-=$d;
}}}}}}$3._computedWidthFlexValue+=$5-$6;
},
invalidateChildrenFlexWidth:function(){delete this._childrenFlexWidthComputed;
},
computeChildrenNeededWidth:function(){var $0=this.getWidget();
return qx.ui.layout.impl.LayoutImpl.prototype.computeChildrenNeededWidth_sum.call(this)+(($0.getVisibleChildrenLength()-1)*$0.getSpacing());
},
updateSelfOnChildOuterWidthChange:function($0){this.getWidget()._invalidateAccumulatedChildrenOuterWidth();
},
updateChildOnInnerWidthChange:function($0){if(this.getWidget().getHorizontalChildrenAlign()=="center"){$0.addToLayoutChanges("locationX");
}var $1=$0._recomputePercentX();
var $2=$0._recomputeFlexX();
return $1||$2;
},
updateChildOnInnerHeightChange:function($0){var $1=$0._recomputePercentY();
var $2=$0._recomputeStretchingY();
if(($0.getVerticalAlign()||this.getWidget().getVerticalChildrenAlign())=="middle"){$0.addToLayoutChanges("locationY");
}return $1||$2;
},
updateSelfOnJobQueueFlush:function($0){if($0.addChild||$0.removeChild){this.getWidget()._invalidateAccumulatedChildrenOuterWidth();
}},
updateChildrenOnJobQueueFlush:function($0){var $1=false,
$2=false;
var $3=this.getWidget();
if($0.orientation){$1=$2=true;
}if($0.spacing||$0.orientation||$0.reverseChildrenOrder||$0.horizontalChildrenAlign){$3._addChildrenToLayoutQueue("locationX");
}
if($0.verticalChildrenAlign){$3._addChildrenToLayoutQueue("locationY");
}
if($0.stretchChildrenOrthogonalAxis){$2=true;
}if($1){$3._recomputeChildrenStretchingX();
$3._addChildrenToLayoutQueue("width");
}
if($2){$3._recomputeChildrenStretchingY();
$3._addChildrenToLayoutQueue("height");
}return true;
},
updateChildrenOnRemoveChild:function($0,
$1){var $2=this.getWidget(),
$3=$2.getVisibleChildren(),
$4=$3.length,
$5,
$6=-1;
if(this.getEnableFlexSupport()){for($6=0;$6<$4;$6++){$5=$3[$6];
if($5.getHasFlexX()){$1=Math.min($1,
$6);
break;
}}$6=-1;
}switch($2.getLayoutMode()){case "right":case "left-reversed":while(($5=$3[++$6])&&$6<$1){$5.addToLayoutChanges("locationX");
}break;
case "center":case "center-reversed":while($5=$3[++$6]){$5.addToLayoutChanges("locationX");
}break;
default:$6+=$1;
while($5=$3[++$6]){$5.addToLayoutChanges("locationX");
}}},
updateChildrenOnMoveChild:function($0,
$1,
$2){var $3=this.getWidget().getVisibleChildren();
var $4=Math.min($1,
$2);
var $5=Math.max($1,
$2)+1;
for(var $6=$4;$6<$5;$6++){$3[$6].addToLayoutChanges("locationX");
}},
flushChildrenQueue:function($0){var $1=this.getWidget(),
$2=$1.getVisibleChildren(),
$3=$2.length,
$4,
$5;
if(this.getEnableFlexSupport()){this.invalidateChildrenFlexWidth();
for($5=0;$5<$3;$5++){$4=$2[$5];
if($4.getHasFlexX()){$4._computedWidthValue=null;
if($4._recomputeBoxWidth()){$4._recomputeOuterWidth();
$4._recomputeInnerWidth();
}$0[$4.toHashCode()]=$4;
$4._layoutChanges.width=true;
}}}
switch($1.getLayoutMode()){case "right":case "left-reversed":for(var $5=$3-1;$5>=0&&!$0[$2[$5].toHashCode()];$5--){}for(var $6=0;$6<=$5;$6++){$1._layoutChild($4=$2[$6]);
}break;
case "center":case "center-reversed":$5=-1;
while($4=$2[++$5]){$1._layoutChild($4);
}break;
default:$5=-1;
var $7=false;
while($4=$2[++$5]){if($7||$0[$4.toHashCode()]){$1._layoutChild($4);
$7=true;
}}}},
layoutChild:function($0,
$1){this.layoutChild_sizeX($0,
$1);
this.layoutChild_sizeY($0,
$1);
this.layoutChild_sizeLimitX($0,
$1);
this.layoutChild_sizeLimitY($0,
$1);
this.layoutChild_locationX($0,
$1);
this.layoutChild_locationY($0,
$1);
this.layoutChild_marginX($0,
$1);
this.layoutChild_marginY($0,
$1);
},
layoutChild_sizeX:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.width||$1.minWidth||$1.maxWidth){if($0._isWidthEssential()&&(!$0._computedWidthTypeNull||!$0._computedMinWidthTypeNull||!$0._computedMaxWidthTypeNull)){$0._renderRuntimeWidth($0.getBoxWidth());
}else{$0._resetRuntimeWidth();
}}},
"default":function($0,
$1){if($1.initial||$1.width){if($0._isWidthEssential()&&!$0._computedWidthTypeNull){$0._renderRuntimeWidth($0.getWidthValue());
}else{$0._resetRuntimeWidth();
}}}}),
layoutChild_sizeY:qx.core.Variant.select("qx.client",
{"mshtml|opera|webkit":function($0,
$1){if($1.initial||$1.height||$1.minHeight||$1.maxHeight){if(($0._isHeightEssential()&&(!$0._computedHeightTypeNull||!$0._computedMinHeightTypeNull||!$0._computedMaxHeightTypeNull))||($0.getAllowStretchY()&&this.getWidget().getStretchChildrenOrthogonalAxis())){$0._renderRuntimeHeight($0.getBoxHeight());
}else{$0._resetRuntimeHeight();
}}},
"default":function($0,
$1){if($1.initial||$1.height){if($0._isHeightEssential()&&!$0._computedHeightTypeNull){$0._renderRuntimeHeight($0.getHeightValue());
}else{$0._resetRuntimeHeight();
}}}}),
layoutChild_locationX:function($0,
$1){var $2=this.getWidget();
if($2.getFirstVisibleChild()==$0){switch($2.getLayoutMode()){case "right":case "left-reversed":var $3=$2.getPaddingRight()+$2.getAccumulatedChildrenOuterWidth()-$0.getOuterWidth();
break;
case "center":case "center-reversed":var $3=$2.getPaddingLeft()+Math.round(($2.getInnerWidth()-$2.getAccumulatedChildrenOuterWidth())/2);
break;
default:var $3=$2.getPaddingLeft();
}}else{var $4=$0.getPreviousVisibleSibling();
switch($2.getLayoutMode()){case "right":case "left-reversed":var $3=$4._cachedLocationHorizontal-$0.getOuterWidth()-$2.getSpacing();
break;
default:var $3=$4._cachedLocationHorizontal+$4.getOuterWidth()+$2.getSpacing();
}}$0._cachedLocationHorizontal=$3;
switch($2.getLayoutMode()){case "right":case "right-reversed":case "center-reversed":$3+=!$0._computedRightTypeNull?$0.getRightValue():!$0._computedLeftTypeNull?-($0.getLeftValue()):0;
$0._resetRuntimeLeft();
$0._renderRuntimeRight($3);
break;
default:$3+=!$0._computedLeftTypeNull?$0.getLeftValue():!$0._computedRightTypeNull?-($0.getRightValue()):0;
$0._resetRuntimeRight();
$0._renderRuntimeLeft($3);
}},
layoutChild_locationY:function($0,
$1){var $2=this.getWidget();
if(qx.core.Variant.isSet("qx.client",
"gecko")){if($0.getAllowStretchY()&&$2.getStretchChildrenOrthogonalAxis()&&$0._computedHeightTypeNull){$0._renderRuntimeTop($2.getPaddingTop()||0);
$0._renderRuntimeBottom($2.getPaddingBottom()||0);
return;
}}var $3=$0.getVerticalAlign()||$2.getVerticalChildrenAlign();
var $4=$3=="middle"?Math.round(($2.getInnerHeight()-$0.getOuterHeight())/2):0;
if($3=="bottom"){$4+=$2.getPaddingBottom();
if(!$0._computedBottomTypeNull){$4+=$0.getBottomValue();
}else if(!$0._computedTopTypeNull){$4-=$0.getTopValue();
}$0._resetRuntimeTop();
$0._renderRuntimeBottom($4);
}else{$4+=$2.getPaddingTop();
if(!$0._computedTopTypeNull){$4+=$0.getTopValue();
}else if(!$0._computedBottomTypeNull){$4-=$0.getBottomValue();
}$0._resetRuntimeBottom();
$0._renderRuntimeTop($4);
}}}});




/* ID: qx.ui.pageview.AbstractPageView */
qx.Class.define("qx.ui.pageview.AbstractPageView",
{type:"abstract",
extend:qx.ui.layout.BoxLayout,
construct:function($0,
$1){arguments.callee.base.call(this);
this._bar=new $0;
this._pane=new $1;
this.add(this._bar,
this._pane);
},
members:{getPane:function(){return this._pane;
},
getBar:function(){return this._bar;
}},
destruct:function(){this._disposeObjects("_bar",
"_pane");
}});




/* ID: qx.ui.pageview.buttonview.ButtonView */
qx.Class.define("qx.ui.pageview.buttonview.ButtonView",
{extend:qx.ui.pageview.AbstractPageView,
construct:function(){arguments.callee.base.call(this,
qx.ui.pageview.buttonview.Bar,
qx.ui.pageview.buttonview.Pane);
this.initBarPosition();
},
properties:{appearance:{refine:true,
init:"button-view"},
barPosition:{init:"top",
check:["top",
"right",
"bottom",
"left"],
apply:"_applyBarPosition",
event:"changeBarPosition"}},
members:{_applyBarPosition:function($0,
$1){var $2=this._bar;
var $3=this._pane;
switch($0){case "top":$2.moveSelfToBegin();
$2.setHeight("auto");
$2.setWidth(null);
$2.setOrientation("horizontal");
$3.setWidth(null);
$3.setHeight("1*");
this.setOrientation("vertical");
break;
case "bottom":$2.moveSelfToEnd();
$2.setHeight("auto");
$2.setWidth(null);
$2.setOrientation("horizontal");
$3.setWidth(null);
$3.setHeight("1*");
this.setOrientation("vertical");
break;
case "left":$2.moveSelfToBegin();
$2.setWidth("auto");
$2.setHeight(null);
$2.setOrientation("vertical");
$3.setHeight(null);
$3.setWidth("1*");
this.setOrientation("horizontal");
break;
case "right":$2.moveSelfToEnd();
$2.setWidth("auto");
$2.setHeight(null);
$2.setOrientation("vertical");
$3.setHeight(null);
$3.setWidth("1*");
this.setOrientation("horizontal");
break;
}this._addChildrenToStateQueue();
$2._addChildrenToStateQueue();
}}});




/* ID: qx.ui.pageview.AbstractBar */
qx.Class.define("qx.ui.pageview.AbstractBar",
{type:"abstract",
extend:qx.ui.layout.BoxLayout,
construct:function(){arguments.callee.base.call(this);
this._manager=new qx.ui.selection.RadioManager;
this.addEventListener("mousewheel",
this._onmousewheel);
},
members:{getManager:function(){return this._manager;
},
_lastDate:(new Date(0)).valueOf(),
_onmousewheel:function($0){var $1=(new Date).valueOf();
if(($1-50)<this._lastDate){return;
}this._lastDate=$1;
var $2=this.getManager();
var $3=$2.getEnabledItems();
var $4=$3.indexOf($2.getSelected());
if(this.getWheelDelta($0)>0){var $5=$3[$4+1];
if(!$5){$5=$3[0];
}}else if($4>0){var $5=$3[$4-1];
if(!$5){$5=$3[0];
}}else{$5=$3[$3.length-1];
}$2.setSelected($5);
},
getWheelDelta:function($0){return $0.getWheelDelta();
}},
destruct:function(){this._disposeObjects("_manager");
}});




/* ID: qx.ui.selection.RadioManager */
qx.Class.define("qx.ui.selection.RadioManager",
{extend:qx.core.Target,
construct:function($0,
$1){arguments.callee.base.call(this);
this._items=[];
this.setName($0!=null?$0:qx.ui.selection.RadioManager.AUTO_NAME_PREFIX+this.toHashCode());
if($1!=null){this.add.apply(this,
$1);
}},
statics:{AUTO_NAME_PREFIX:"qx-radio-"},
properties:{selected:{nullable:true,
apply:"_applySelected",
event:"changeSelected",
check:"qx.core.Object"},
name:{check:"String",
nullable:true,
apply:"_applyName"}},
members:{getItems:function(){return this._items;
},
getEnabledItems:function(){var $0=[];
for(var $1=0,
$2=this._items,
$3=$2.length;$1<$3;$1++){if($2[$1].getEnabled()){$0.push($2[$1]);
}}return $0;
},
handleItemChecked:function($0,
$1){if($1){this.setSelected($0);
}else if(this.getSelected()==$0){this.setSelected(null);
}},
add:function($0){var $1=arguments;
var $2=$1.length;
var $3;
for(var $4=0;$4<$2;$4++){$3=$1[$4];
if(qx.lang.Array.contains(this._items,
$3)){return;
}this._items.push($3);
$3.setManager(this);
if($3.getChecked()){this.setSelected($3);
}$3.setName(this.getName());
}},
remove:function($0){qx.lang.Array.remove(this._items,
$0);
$0.setManager(null);
if($0.getChecked()){this.setSelected(null);
}},
_applySelected:function($0,
$1){if($1){$1.setChecked(false);
}
if($0){$0.setChecked(true);
}},
_applyName:function($0,
$1){for(var $2=0,
$3=this._items,
$4=$3.length;$2<$4;$2++){$3[$2].setName($0);
}},
selectNext:function($0){var $1=this._items.indexOf($0);
if($1==-1){return;
}var $2=0;
var $3=this._items.length;
$1=($1+1)%$3;
while($2<$3&&!this._items[$1].getEnabled()){$1=($1+1)%$3;
$2++;
}this._selectByIndex($1);
},
selectPrevious:function($0){var $1=this._items.indexOf($0);
if($1==-1){return;
}var $2=0;
var $3=this._items.length;
$1=($1-1+$3)%$3;
while($2<$3&&!this._items[$1].getEnabled()){$1=($1-1+$3)%$3;
$2++;
}this._selectByIndex($1);
},
_selectByIndex:function($0){if(this._items[$0].getEnabled()){this.setSelected(this._items[$0]);
this._items[$0].setFocused(true);
}}},
destruct:function(){this._disposeObjectDeep("_items",
1);
}});




/* ID: qx.ui.pageview.buttonview.Bar */
qx.Class.define("qx.ui.pageview.buttonview.Bar",
{extend:qx.ui.pageview.AbstractBar,
properties:{appearance:{refine:true,
init:"button-view-bar"}},
members:{getWheelDelta:function($0){var $1=$0.getWheelDelta();
switch(this.getParent().getBarPosition()){case "left":case "right":$1*=-1;
}return $1;
},
_renderAppearance:function(){if(this.getParent()){var $0=this.getParent().getBarPosition();
$0==="left"?this.addState("barLeft"):this.removeState("barLeft");
$0==="right"?this.addState("barRight"):this.removeState("barRight");
$0==="top"?this.addState("barTop"):this.removeState("barTop");
$0==="bottom"?this.addState("barBottom"):this.removeState("barBottom");
}arguments.callee.base.call(this);
}}});




/* ID: qx.ui.pageview.AbstractPane */
qx.Class.define("qx.ui.pageview.AbstractPane",
{type:"abstract",
extend:qx.ui.layout.CanvasLayout});




/* ID: qx.ui.pageview.buttonview.Pane */
qx.Class.define("qx.ui.pageview.buttonview.Pane",
{extend:qx.ui.pageview.AbstractPane,
properties:{appearance:{refine:true,
init:"button-view-pane"}},
members:{_renderAppearance:function(){if(this._hasParent){var $0=this.getParent().getBarPosition();
$0==="top"||$0==="bottom"?this.addState("barHorizontal"):this.removeState("barHorizontal");
$0==="left"||$0==="right"?this.addState("barVertical"):this.removeState("barVertical");
}arguments.callee.base.call(this);
}}});




/* ID: spagobi.commons.WidgetUtils */
qx.Class.define("spagobi.commons.WidgetUtils",
{type:"static",
statics:{createLabel:function($0){var $1={text:'',
top:0,
left:0,
width:80};
var $2=new qx.ui.basic.Label();
$2.set($1);
$2.set($0);
return $2;
},
createTextField:function($0){var $1={top:0,
left:0,
maxLength:50,
width:0,
height:0};
var $2=new qx.ui.form.TextField();
$2.set($1);
$2.set($0);
return $2;
},
createComboBox:function($0){var $1={top:0,
left:0,
items:[],
listeners:[]};
$0=spagobi.commons.CoreUtils.apply($1,
$0);
var $2=new qx.ui.form.ComboBox();
$2.set({top:$0.top,
left:$0.left});
for(var $3=0;$3<$0.items.length;$3++){var $4=new qx.ui.form.ListItem($0.items[$3]);
$2.add($4);
}
for(var $3=0;$3<$0.listeners.length;$3++){$2.addEventListener($0.listeners[$3].event,
$0.listeners[$3].handler);
}$2.setSelected($2.getList().getFirstChild());
return $2;
},
createCheckBox:function($0){var $1={checked:false,
top:0,
left:0};
$0=spagobi.commons.CoreUtils.apply($1,
$0);
var $2=new qx.ui.form.CheckBox();
$2.set($0);
return $2;
},
createInputTextField:function($0){var $1={top:0,
left:0,
text:'',
maxLength:50,
width:0,
height:0,
mandatory:false};
$0=spagobi.commons.CoreUtils.apply($1,
$0);
var $2=this.createLabel({text:$0.text,
top:$0.top,
left:$0.left});
var $3=this.createTextField({top:$0.top,
left:$0.left+30,
maxLength:$0.maxLength,
width:$0.width,
height:$0.height});
var $4=new qx.ui.basic.Atom();
$4.add($2,
$3);
if($0.mandatory){var $5=this.createLabel({text:'*',
top:$0.top,
left:$0.left+35});
$4.add($5);
}return $4;
},
createInputComboBox:function($0){var $1={top:0,
left:0,
text:'',
items:[],
listeners:[]};
$0=spagobi.commons.CoreUtils.apply($1,
$0);
var $2=this.createLabel({text:$0.text,
top:$0.top,
left:$0.left});
var $3=this.createComboBox({top:$0.top,
left:$0.left+30,
items:$0.items,
listeners:$0.listeners});
var $4=new qx.ui.basic.Atom();
$4.add($2,
$3);
return $4;
},
createInputCheckBox:function($0){var $1={top:0,
left:0,
text:'',
checked:false};
$0=spagobi.commons.CoreUtils.apply($1,
$0);
var $2=this.createLabel({text:$0.text,
top:$0.top,
left:$0.left});
var $3=this.createCheckBox({checked:$0.checked,
top:$0.top,
left:$0.left+30});
var $4=new qx.ui.basic.Atom();
$4.add($2,
$3);
return $4;
},
function_label:function($0,
$1,
$2,
$3){if($0==undefined)$0="";
if($2==undefined)$2=0;
if($3==undefined)$3=0;
if($1!=undefined){$1.set({top:$2,
left:$3});
return $1;
}else{var $4=new qx.ui.basic.Label($0);
$4.set({top:$2,
left:$3});
return $4;
}},
function_textfield:function($0,
$1,
$2,
$3,
$4,
$5){if($1==undefined)$1=0;
if($2==undefined)$2=0;
if($3==undefined)$3=50;
if($4==undefined)$4=0;
if($5==undefined)$5=0;
if($0!=undefined){$0.set({top:$1,
left:$2});
return $0;
}else{var $6=new qx.ui.form.TextField();
$6.set({top:$1,
left:$2});
$6.setMaxLength($3);
$6.setDimension($4,
$5);
return $6;
}},
function_combo:function($0,
$1,
$2,
$3){if($1==undefined)$1=0;
if($2==undefined)$2=0;
if($3==undefined)$3=null;
{var $4=new qx.ui.form.ComboBox;
$4.set({top:$1,
left:$2});
var $5=$3;
for(var $6=0;$6<$5.length;$6++){item1=new qx.ui.form.ListItem($5[$6]);
$4.add(item1);
}$4.setSelected($4.getList().getFirstChild());
return $4;
};
},
function_checkbox:function($0,
$1,
$2,
$3,
$4){if($1==undefined)checked="";
if($2==undefined)$2=0;
if($3==undefined)$3=0;
if($4==undefined)$4=false;
{var $5=new qx.ui.form.CheckBox($1);
with($5){setTop($2);
setLeft($3);
setChecked($4);
}return $5;
};
}}});




/* ID: qx.ui.basic.Label */
qx.Class.define("qx.ui.basic.Label",
{extend:qx.ui.basic.Terminator,
construct:function($0,
$1,
$2){arguments.callee.base.call(this);
if($2!=null){this.setMode($2);
}
if($0!=null){this.setText($0);
}
if($1!=null){this.setMnemonic($1);
}this.initWidth();
this.initHeight();
this.initSelectable();
this.initCursor();
this.initWrap();
},
statics:{_getMeasureNode:function(){var $0=this._measureNode;
if(!$0){$0=document.createElement("div");
var $1=$0.style;
$1.width=$1.height="auto";
$1.visibility="hidden";
$1.position="absolute";
$1.zIndex="-1";
document.body.appendChild($0);
this._measureNode=$0;
}return $0;
}},
properties:{appearance:{refine:true,
init:"label"},
width:{refine:true,
init:"auto"},
height:{refine:true,
init:"auto"},
allowStretchX:{refine:true,
init:false},
allowStretchY:{refine:true,
init:false},
selectable:{refine:true,
init:false},
cursor:{refine:true,
init:"default"},
text:{apply:"_applyText",
init:"",
dispose:true,
event:"changeText",
check:"Label"},
wrap:{check:"Boolean",
init:false,
nullable:true,
apply:"_applyWrap"},
textAlign:{check:["left",
"center",
"right",
"justify"],
nullable:true,
themeable:true,
apply:"_applyTextAlign"},
textOverflow:{check:"Boolean",
init:true,
apply:"_applyText"},
mode:{check:["html",
"text",
"auto"],
init:"auto",
apply:"_applyText"},
mnemonic:{check:"String",
nullable:true,
apply:"_applyMnemonic"}},
members:{_content:"",
_isHtml:false,
setHtml:function($0){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"please use setText() instead.");
this.setText($0);
},
getHtml:function(){qx.log.Logger.deprecatedMethodWarning(arguments.callee,
"please use getText() instead.");
return this.getText();
},
_applyTextAlign:function($0,
$1){$0===null?this.removeStyleProperty("textAlign"):this.setStyleProperty("textAlign",
$0);
},
_applyFont:function($0,
$1){qx.theme.manager.Font.getInstance().connect(this._styleFont,
this,
$0);
},
_styleFont:function($0){this._invalidatePreferredInnerDimensions();
$0?$0.render(this):qx.ui.core.Font.reset(this);
},
_applyTextColor:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._styleTextColor,
this,
$0);
},
_styleTextColor:function($0){$0?this.setStyleProperty("color",
$0):this.removeStyleProperty("color");
},
_applyWrap:function($0,
$1){$0==null?this.removeStyleProperty("whiteSpace"):this.setStyleProperty("whiteSpace",
$0?"normal":"nowrap");
},
_applyText:function($0,
$1){qx.locale.Manager.getInstance().connect(this._syncText,
this,
this.getText());
},
_syncText:function($0){var $1=this.getMode();
if($1==="auto"){$1=qx.util.Validation.isValidString($0)&&$0.match(/<.*>/)?"html":"text";
}
switch($1){case "text":var $2=qx.html.String.escape($0).replace(/(^ | $)/g,
"&nbsp;").replace(/  /g,
"&nbsp;&nbsp;");
this._isHtml=$2!==$0;
this._content=$2;
break;
case "html":this._isHtml=true;
this._content=$0;
break;
}
if(this._isCreated){this._renderContent();
}},
_applyMnemonic:function($0,
$1){this._mnemonicTest=$0?new RegExp("^(((<([^>]|"+$0+")+>)|(&([^;]|"+$0+")+;)|[^&"+$0+"])*)("+$0+")",
"i"):null;
if(this._isCreated){this._renderContent();
}},
_computeObjectNeededDimensions:function(){var $0=arguments.callee.self._getMeasureNode();
var $1=$0.style;
var $2=this._styleProperties;
$1.fontFamily=$2.fontFamily||"";
$1.fontSize=$2.fontSize||"";
$1.fontWeight=$2.fontWeight||"";
$1.fontStyle=$2.fontStyle||"";
if(this._isHtml){$0.innerHTML=this._content;
}else{$0.innerHTML="";
qx.dom.Element.setTextContent($0,
this._content);
}this._cachedPreferredInnerWidth=$0.scrollWidth;
this._cachedPreferredInnerHeight=$0.scrollHeight;
},
_computePreferredInnerWidth:function(){this._computeObjectNeededDimensions();
return this._cachedPreferredInnerWidth;
},
_computePreferredInnerHeight:function(){this._computeObjectNeededDimensions();
return this._cachedPreferredInnerHeight;
},
__patchTextOverflow:function($0,
$1){return ("<div style='float:left;width:"+($1-14)+"px;overflow:hidden;white-space:nowrap'>"+$0+"</div><span style='float:left'>&hellip;</span>");
},
_postApply:function(){var $0=this._content;
var $1=this._getTargetNode();
if($0==null){$1.innerHTML="";
return;
}
if(this.getMnemonic()){if(this._mnemonicTest.test($0)){$0=RegExp.$1+"<span style=\"text-decoration:underline\">"+RegExp.$7+"</span>"+RegExp.rightContext;
this._isHtml=true;
}else{$0+=" ("+this.getMnemonic()+")";
}}var $2=$1.style;
if(this.getTextOverflow()&&!this.getWrap()){if(this.getInnerWidth()<this.getPreferredInnerWidth()){$2.overflow="hidden";
if(qx.core.Variant.isSet("qx.client",
"mshtml|webkit")){$2.textOverflow="ellipsis";
}else if(qx.core.Variant.isSet("qx.client",
"opera")){$2.OTextOverflow="ellipsis";
}else{$0=this.__patchTextOverflow($0,
this.getInnerWidth());
this._isHtml=true;
}}else{$2.overflow="";
if(qx.core.Variant.isSet("qx.client",
"mshtml|webkit")){$2.textOverflow="";
}else if(qx.core.Variant.isSet("qx.client",
"opera")){$2.OTextOverflow="";
}}}
if(this._isHtml){$1.innerHTML=$0;
}else{$1.innerHTML="";
qx.dom.Element.setTextContent($1,
$0);
}}}});




/* ID: qx.locale.Manager */
qx.Class.define("qx.locale.Manager",
{type:"singleton",
extend:qx.util.manager.Value,
construct:function(){arguments.callee.base.call(this);
this._translationCatalog={};
this.setLocale(qx.core.Client.getInstance().getLocale()||this._defaultLocale);
},
statics:{tr:function($0,
$1){var $2=qx.lang.Array.fromArguments(arguments);
$2.splice(0,
1);
return new qx.locale.LocalizedString($0,
$2);
},
trn:function($0,
$1,
$2,
$3){var $4=qx.lang.Array.fromArguments(arguments);
$4.splice(0,
3);
if($2>1){return new qx.locale.LocalizedString($1,
$4);
}else{return new qx.locale.LocalizedString($0,
$4);
}},
trc:function($0,
$1,
$2){var $3=qx.lang.Array.fromArguments(arguments);
$3.splice(0,
2);
return new qx.locale.LocalizedString($1,
$3);
},
marktr:function($0){return $0;
}},
properties:{locale:{check:"String",
nullable:true,
apply:"_applyLocale",
event:"changeLocale"}},
members:{_defaultLocale:"C",
getLanguage:function(){return this._language;
},
getTerritory:function(){return this.getLocale().split("_")[1]||"";
},
getAvailableLocales:function(){var $0=[];
for(var $1 in this._translationCatalog){if($1!=this._defaultLocale){$0.push($1);
}}return $0;
},
_extractLanguage:function($0){var $1;
var $2=$0.indexOf("_");
if($2==-1){$1=$0;
}else{$1=$0.substring(0,
$2);
}return $1;
},
_applyLocale:function($0,
$1){this._locale=$0;
this._language=this._extractLanguage($0);
this._updateObjects();
},
addTranslation:function($0,
$1){if(this._translationCatalog[$0]){for(var $2 in $1){this._translationCatalog[$0][$2]=$1[$2];
}}else{this._translationCatalog[$0]=$1;
}},
addTranslationFromClass:function($0,
$1){this.addTranslation($0.substring($0.lastIndexOf(".")+1),
$1);
},
translate:function($0,
$1,
$2){var $3;
if($2){var $4=this._extractLanguage($2);
}else{$2=this._locale;
$4=this._language;
}
if(!$3&&this._translationCatalog[$2]){$3=this._translationCatalog[$2][$0];
}
if(!$3&&this._translationCatalog[$4]){$3=this._translationCatalog[$4][$0];
}
if(!$3&&this._translationCatalog[this._defaultLocale]){$3=this._translationCatalog[this._defaultLocale][$0];
}
if(!$3){$3=$0;
}
if($1.length>0){$3=qx.lang.String.format($3,
$1);
}return $3;
},
isDynamic:function($0){return $0 instanceof qx.locale.LocalizedString;
},
resolveDynamic:function($0){return $0.toString();
}},
destruct:function(){this._disposeFields("_translationCatalog");
}});




/* ID: qx.locale.LocalizedString */
qx.Class.define("qx.locale.LocalizedString",
{extend:qx.core.Object,
construct:function($0,
$1,
$2){arguments.callee.base.call(this);
this.setId($0);
this._locale=$2;
var $3=[];
for(var $4=0;$4<$1.length;$4++){var $5=$1[$4];
if($5 instanceof qx.locale.LocalizedString){$3.push($5);
}else{$3.push($5+"");
}}this.setArgs($3);
},
properties:{id:{check:"String",
nullable:true},
args:{nullable:true,
dispose:true}},
members:{_autoDispose:false,
toString:function(){return qx.locale.Manager.getInstance().translate(this.getId(),
this.getArgs(),
this._locale);
}}});




/* ID: qx.dom.Element */
qx.Class.define("qx.dom.Element",
{statics:{cleanWhitespace:function($0){for(var $1=0;$1<$0.childNodes.length;$1++){var $2=$0.childNodes[$1];
if($2.nodeType==qx.dom.Node.TEXT&&!/\S/.test($2.nodeValue)){$0.removeChild($2);
}}},
isEmpty:function($0){return $0.innerHTML.match(/^\s*$/);
},
getTextContent:qx.lang.Object.select(qx.core.Client.getInstance().supportsTextContent()?"textContent":qx.core.Client.getInstance().supportsInnerText()?"innerText":"default",
{innerText:function($0){return $0.innerText||$0.text;
},
textContent:function($0){return $0.textContent;
},
"default":function(){throw new Error("This browser does not support any form of text content handling!");
}}),
setTextContent:qx.lang.Object.select(qx.core.Client.getInstance().supportsTextContent()?"textContent":qx.core.Client.getInstance().supportsInnerText()?"innerText":"default",
{innerText:function($0,
$1){$0.innerText=$1;
},
textContent:function($0,
$1){$0.textContent=$1;
},
"default":function(){throw new Error("This browser does not support any form of text content handling!");
}})}});




/* ID: qx.ui.form.TextField */
qx.Class.define("qx.ui.form.TextField",
{extend:qx.ui.basic.Terminator,
construct:function($0){arguments.callee.base.call(this);
if($0!=null){this.setValue($0);
}this.initHideFocus();
this.initWidth();
this.initHeight();
this.initTabIndex();
this.initSpellCheck();
this.__oninput=qx.lang.Function.bindEvent(this._oninputDom,
this);
this.addEventListener("blur",
this._onblur);
this.addEventListener("focus",
this._onfocus);
this.addEventListener("input",
this._oninput);
},
statics:{createRegExpValidator:function($0){return function($1){return $0.test($1);
};
}},
events:{"input":"qx.event.type.DataEvent"},
properties:{allowStretchX:{refine:true,
init:true},
allowStretchY:{refine:true,
init:false},
appearance:{refine:true,
init:"text-field"},
tabIndex:{refine:true,
init:1},
hideFocus:{refine:true,
init:true},
width:{refine:true,
init:"auto"},
height:{refine:true,
init:"auto"},
selectable:{refine:true,
init:true},
value:{init:"",
nullable:true,
event:"changeValue",
apply:"_applyValue",
dispose:true},
textAlign:{check:["left",
"center",
"right",
"justify"],
nullable:true,
themeable:true,
apply:"_applyTextAlign"},
spellCheck:{check:"Boolean",
init:false,
apply:"_applySpellCheck"},
liveUpdate:{check:"Boolean",
init:false},
maxLength:{check:"Integer",
apply:"_applyMaxLength",
nullable:true},
readOnly:{check:"Boolean",
apply:"_applyReadOnly",
init:false},
validator:{check:"Function",
event:"changeValidator",
nullable:true}},
members:{_inputTag:"input",
_inputType:"text",
_inputOverflow:"hidden",
_applyElement:function($0,
$1){arguments.callee.base.call(this,
$0,
$1);
if($0){var $2=this._inputElement=document.createElement(this._inputTag);
if(this._inputType){$2.type=this._inputType;
}$2.autoComplete="off";
$2.setAttribute("autoComplete",
"off");
$2.disabled=this.getEnabled()===false;
$2.readOnly=this.getReadOnly();
$2.value=this.getValue()?this.getValue():"";
if(this.getMaxLength()!=null){$2.maxLength=this.getMaxLength();
}var $3=$2.style;
$3.padding=$3.margin=0;
$3.border="0 none";
$3.background="transparent";
$3.overflow=this._inputOverflow;
$3.outline="none";
$3.resize="none";
$3.WebkitAppearance="none";
$3.MozAppearance="none";
if(qx.core.Variant.isSet("qx.client",
"gecko|opera|webkit")){$3.margin="1px 0";
}this._renderFont();
this._renderTextColor();
this._renderTextAlign();
this._renderCursor();
this._renderSpellCheck();
if(qx.core.Variant.isSet("qx.client",
"mshtml")){$2.onpropertychange=this.__oninput;
}else{$2.addEventListener("input",
this.__oninput,
false);
}$0.appendChild($2);
}},
_postApply:function(){this._syncFieldWidth();
this._syncFieldHeight();
},
_changeInnerWidth:function($0,
$1){this._syncFieldWidth();
},
_changeInnerHeight:function($0,
$1){this._syncFieldHeight();
},
_syncFieldWidth:function(){this._inputElement.style.width=this.getInnerWidth()+"px";
},
_syncFieldHeight:function(){this._inputElement.style.height=(this.getInnerHeight()-2)+"px";
},
_applyCursor:function($0,
$1){if(this._inputElement){this._renderCursor();
}},
_renderCursor:function(){var $0=this._inputElement.style;
var $1=this.getCursor();
if($1){if($1=="pointer"&&qx.core.Client.getInstance().isMshtml()){$0.cursor="hand";
}else{$0.cursor=$1;
}}else{$0.cursor="";
}},
_applyTextAlign:function($0,
$1){if(this._inputElement){this._renderTextAlign();
}},
_renderTextAlign:function(){this._inputElement.style.textAlign=this.getTextAlign()||"";
},
_applySpellCheck:function($0,
$1){if(this._inputElement){this._renderSpellCheck();
}},
_renderSpellCheck:function(){this._inputElement.spellcheck=this.getSpellCheck();
},
_applyEnabled:function($0,
$1){if(this._inputElement){this._inputElement.disabled=$0===false;
}return arguments.callee.base.call(this,
$0,
$1);
},
_applyValue:function($0,
$1){this._inValueProperty=true;
if(this._inputElement){if($0===null){$0="";
}
if(this._inputElement.value!==$0){this._inputElement.value=$0;
}}delete this._inValueProperty;
},
_applyMaxLength:function($0,
$1){if(this._inputElement){this._inputElement.maxLength=$0==null?"":$0;
}},
_applyReadOnly:function($0,
$1){if(this._inputElement){this._inputElement.readOnly=$0;
}
if($0){this.addState("readonly");
}else{this.removeState("readonly");
}},
_applyTextColor:function($0,
$1){qx.theme.manager.Color.getInstance().connect(this._styleTextColor,
this,
$0);
},
_styleTextColor:function($0){this.__textColor=$0;
this._renderTextColor();
},
_renderTextColor:function(){var $0=this._inputElement;
if($0){$0.style.color=this.__textColor||"";
}},
_applyFont:function($0,
$1){qx.theme.manager.Font.getInstance().connect(this._styleFont,
this,
$0);
},
_styleFont:function($0){this.__font=$0;
this._renderFont();
},
_renderFont:function(){var $0=this._inputElement;
if($0){var $1=this.__font;
$1?$1.renderElement($0):qx.ui.core.Font.resetElement($0);
}},
_visualizeFocus:function(){arguments.callee.base.call(this);
if(!qx.event.handler.FocusHandler.mouseFocus&&this.getEnableElementFocus()){try{this._inputElement.focus();
}catch(ex){}}},
_visualizeBlur:function(){arguments.callee.base.call(this);
if(!qx.event.handler.FocusHandler.mouseFocus){try{this._inputElement.blur();
}catch(ex){}}},
getComputedValue:function(){if(this._inputElement){return this._inputElement.value;
}return this.getValue();
},
getInputElement:function(){return this._inputElement||null;
},
isValid:function(){var $0=this.getValidator();
return !$0||$0(this.getValue());
},
isComputedValid:function(){var $0=this.getValidator();
return !$0||$0(this.getComputedValue());
},
_computePreferredInnerWidth:function(){return 120;
},
_computePreferredInnerHeight:function(){return 16;
},
_ieFirstInputFix:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._inValueProperty=true;
this._inputElement.value=this.getValue()===null?"":this.getValue();
this._firstInputFixApplied=true;
delete this._inValueProperty;
},
"default":null}),
_afterAppear:qx.core.Variant.select("qx.client",
{"mshtml":function(){arguments.callee.base.call(this);
if(!this._firstInputFixApplied&&this._inputElement){qx.client.Timer.once(this._ieFirstInputFix,
this,
1);
}},
"default":function(){arguments.callee.base.call(this);
}}),
_firstInputFixApplied:false,
_textOnFocus:null,
_oninputDom:qx.core.Variant.select("qx.client",
{"mshtml":function($0){if(!this._inValueProperty&&$0.propertyName==="value"){this.createDispatchDataEvent("input",
this.getComputedValue());
}},
"default":function($0){this.createDispatchDataEvent("input",
this.getComputedValue());
}}),
_ontabfocus:function(){this.selectAll();
},
_onfocus:function(){this._textOnFocus=this.getComputedValue();
},
_onblur:function(){var $0=this.getComputedValue().toString();
if(this._textOnFocus!=$0){this.setValue($0);
}this.setSelectionLength(0);
},
_oninput:function(){if(!this.isLiveUpdate()){return;
}var $0=this.getComputedValue().toString();
this.setValue($0);
},
__getRange:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._visualPropertyCheck();
return this._inputElement.createTextRange();
},
"default":null}),
__getSelectionRange:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._visualPropertyCheck();
return this.getTopLevelWidget().getDocumentElement().selection.createRange();
},
"default":null}),
setSelectionStart:qx.core.Variant.select("qx.client",
{"mshtml":function($0){this._visualPropertyCheck();
var $1=this._inputElement.value;
var $2=0;
while($2<$0){$2=$1.indexOf("\r\n",
$2);
if($2==-1){break;
}$0--;
$2++;
}var $3=this.__getRange();
$3.collapse();
$3.move("character",
$0);
$3.select();
},
"default":function($0){this._visualPropertyCheck();
this._inputElement.selectionStart=$0;
}}),
getSelectionStart:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._visualPropertyCheck();
var $0=this.__getSelectionRange();
if(!this._inputElement.contains($0.parentElement())){return -1;
}var $1=this.__getRange();
var $2=this._inputElement.value.length;
$1.moveToBookmark($0.getBookmark());
$1.moveEnd('character',
$2);
return $2-$1.text.length;
},
"default":function(){this._visualPropertyCheck();
return this._inputElement.selectionStart;
}}),
setSelectionLength:qx.core.Variant.select("qx.client",
{"mshtml":function($0){this._visualPropertyCheck();
var $1=this.__getSelectionRange();
if(!this._inputElement.contains($1.parentElement())){return;
}$1.collapse();
$1.moveEnd("character",
$0);
$1.select();
},
"default":function($0){this._visualPropertyCheck();
var $1=this._inputElement;
if(qx.util.Validation.isValidString($1.value)&&this.getVisibility()){$1.selectionEnd=$1.selectionStart+$0;
}}}),
getSelectionLength:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._visualPropertyCheck();
var $0=this.__getSelectionRange();
if(!this._inputElement.contains($0.parentElement())){return 0;
}return $0.text.length;
},
"default":function(){this._visualPropertyCheck();
var $0=this._inputElement;
return $0.selectionEnd-$0.selectionStart;
}}),
setSelectionText:qx.core.Variant.select("qx.client",
{"mshtml":function($0){this._visualPropertyCheck();
var $1=this.getSelectionStart();
var $2=this.__getSelectionRange();
if(!this._inputElement.contains($2.parentElement())){return;
}$2.text=$0;
this.setValue(this._inputElement.value);
this.setSelectionStart($1);
this.setSelectionLength($0.length);
},
"default":function($0){this._visualPropertyCheck();
var $1=this._inputElement;
var $2=$1.value;
var $3=$1.selectionStart;
var $4=$2.substr(0,
$3);
var $5=$2.substr($1.selectionEnd);
var $6=$1.value=$4+$0+$5;
$1.selectionStart=$3;
$1.selectionEnd=$3+$0.length;
this.setValue($6);
}}),
getSelectionText:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._visualPropertyCheck();
var $0=this.__getSelectionRange();
if(!this._inputElement.contains($0.parentElement())){return "";
}return $0.text;
},
"default":function(){this._visualPropertyCheck();
return this._inputElement.value.substr(this.getSelectionStart(),
this.getSelectionLength());
}}),
selectAll:function(){this._visualPropertyCheck();
if(this.getValue()!=null){this.setSelectionStart(0);
this.setSelectionLength(this._inputElement.value.length);
}this._inputElement.select();
this._inputElement.focus();
},
selectFromTo:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){this._visualPropertyCheck();
this.setSelectionStart($0);
this.setSelectionLength($1-$0);
},
"default":function($0,
$1){this._visualPropertyCheck();
var $2=this._inputElement;
$2.selectionStart=$0;
$2.selectionEnd=$1;
}})},
destruct:function(){if(this._inputElement){if(qx.core.Variant.isSet("qx.client",
"mshtml")){this._inputElement.onpropertychange=null;
}else{this._inputElement.removeEventListener("input",
this.__oninput,
false);
}}this._disposeFields("_inputElement",
"__font",
"__oninput");
}});




/* ID: spagobi.commons.CoreUtils */
qx.Class.define("spagobi.commons.CoreUtils",
{type:"static",
statics:{apply:function($0,
$1,
$2){if($2){apply($0,
$2);
}
if($0&&$1&&typeof $1=='object'){for(var $3 in $1){$0[$3]=$1[$3];
}}return $0;
}}});




/* ID: qx.ui.layout.HorizontalBoxLayout */
qx.Class.define("qx.ui.layout.HorizontalBoxLayout",
{extend:qx.ui.layout.BoxLayout});




/* ID: qx.ui.form.ComboBox */
qx.Class.define("qx.ui.form.ComboBox",
{extend:qx.ui.layout.HorizontalBoxLayout,
construct:function(){arguments.callee.base.call(this);
var $0=this._list=new qx.ui.form.List;
$0.setAppearance("combo-box-list");
$0.setTabIndex(-1);
$0.setEdge(0);
var $1=this._manager=this._list.getManager();
$1.setMultiSelection(false);
$1.setDragSelection(false);
var $2=this._popup=new qx.ui.popup.Popup;
$2.setAppearance("combo-box-popup");
$2.setRestrictToPageLeft(-100000);
$2.setRestrictToPageRight(-100000);
$2.setAutoHide(false);
$2.setHeight("auto");
$2.add($0);
var $3=this._field=new qx.ui.form.TextField;
$3.setAppearance("combo-box-text-field");
$3.setTabIndex(-1);
$3.setWidth("1*");
$3.setAllowStretchY(true);
$3.setHeight(null);
this.add($3);
var $4=this._button=new qx.ui.basic.Atom;
$4.setAppearance("combo-box-button");
$4.setAllowStretchY(true);
$4.setTabIndex(-1);
$4.setHeight(null);
this.add($4);
this.addEventListener("mousedown",
this._onmousedown);
this.addEventListener("mouseup",
this._onmouseup);
this.addEventListener("click",
this._onclick);
this.addEventListener("mouseover",
this._onmouseover);
this.addEventListener("mousewheel",
this._onmousewheel);
this.addEventListener("keydown",
this._onkeydown);
this.addEventListener("keypress",
this._onkeypress);
this.addEventListener("keyinput",
this._onkeyinput);
this.addEventListener("beforeDisappear",
this._onbeforedisappear);
this._popup.addEventListener("appear",
this._onpopupappear,
this);
this._field.addEventListener("input",
this._oninput,
this);
qx.locale.Manager.getInstance().addEventListener("changeLocale",
this._onlocalechange,
this);
var $5=qx.ui.core.ClientDocument.getInstance();
$5.addEventListener("windowblur",
this._testClosePopup,
this);
this.remapChildrenHandlingTo($0);
this.initEditable();
this.initTabIndex();
this.initWidth();
this.initHeight();
this.initMinWidth();
},
events:{"beforeInitialOpen":"qx.event.type.Event"},
properties:{appearance:{refine:true,
init:"combo-box"},
allowStretchY:{refine:true,
init:false},
width:{refine:true,
init:120},
height:{refine:true,
init:"auto"},
minWidth:{refine:true,
init:40},
tabIndex:{refine:true,
init:1},
editable:{check:"Boolean",
apply:"_applyEditable",
event:"changeEditable",
init:false},
selected:{check:"qx.ui.form.ListItem",
nullable:true,
apply:"_applySelected",
event:"changeSelected"},
value:{check:"String",
nullable:true,
apply:"_applyValue",
event:"changeValue"},
pagingInterval:{check:"Integer",
init:10}},
members:{getManager:function(){return this._manager;
},
getPopup:function(){return this._popup;
},
getList:function(){return this._list;
},
getField:function(){return this._field;
},
getButton:function(){return this._button;
},
_applySelected:function($0,
$1){this._fromSelected=true;
if(!this._fromValue){this.setValue($0?$0.getLabel().toString():"");
}this._manager.setLeadItem($0);
this._manager.setAnchorItem($0);
if($0){this._manager.setSelectedItem($0);
}else{this._manager.deselectAll();
}delete this._fromSelected;
},
_applyValue:function($0,
$1){this._fromValue=true;
if(!this._fromInput){if(this._field.getValue()==$0){this._field.setValue(null);
}this._field.setValue($0);
}if(!this._fromSelected){var $2=this._list.findStringExact($0);
if($2!=null&&!$2.getEnabled()){$2=null;
}this.setSelected($2);
}delete this._fromValue;
},
_applyEditable:function($0,
$1){var $2=this._field;
$2.setReadOnly(!$0);
$2.setCursor($0?null:"default");
$2.setSelectable($0);
},
_oldSelected:null,
_openPopup:function(){var $0=this._popup;
var $1=this.getElement();
if(!$0.isCreated()){this.createDispatchEvent("beforeInitialOpen");
}
if(this._list.getChildrenLength()==0){return;
}$0.positionRelativeTo($1,
1,
qx.html.Dimension.getBoxHeight($1));
$0.setWidth(this.getBoxWidth()-2);
$0.setParent(this.getTopLevelWidget());
$0.show();
this._oldSelected=this.getSelected();
this.setCapture(true);
},
_closePopup:function(){this._popup.hide();
this.setCapture(false);
},
_testClosePopup:function(){if(this._popup.isSeeable()){this._closePopup();
}},
_togglePopup:function(){this._popup.isSeeable()?this._closePopup():this._openPopup();
},
_onpopupappear:function($0){var $1=this.getSelected();
if($1){$1.scrollIntoView();
}},
_oninput:function($0){this._fromInput=true;
this.setValue(this._field.getComputedValue());
if(this.getPopup().isSeeable()&&this.getSelected()){this.getSelected().scrollIntoView();
}delete this._fromInput;
},
_onbeforedisappear:function($0){this._testClosePopup();
},
_onlocalechange:function($0){var $1=this.getSelected();
this._applySelected($1,
$1);
},
_onmousedown:function($0){if(!$0.isLeftButtonPressed()){return;
}var $1=$0.getTarget();
switch($1){case this._field:if(this.getEditable()){break;
}case this._button:this._button.addState("pressed");
this._togglePopup();
this.setCapture(true);
break;
default:break;
}$0.stopPropagation();
},
_onclick:function($0){if(!$0.isLeftButtonPressed()){return;
}var $1=$0.getTarget();
switch($1){case this._field:case this._button:case this:case this._list:break;
default:if($1 instanceof qx.ui.form.ListItem&&$1.getParent()==this._list){this._list._onmousedown($0);
this.setSelected(this._list.getSelectedItem());
this._closePopup();
this.setFocused(true);
}else if(this._popup.isSeeable()){this._popup.hide();
this.setCapture(false);
}}},
_onmouseup:function($0){this._button.removeState("pressed");
if(!this._popup.isSeeable()){this.setCapture(false);
}},
_onmouseover:function($0){var $1=$0.getTarget();
if($1 instanceof qx.ui.form.ListItem){var $2=this._manager;
$2.deselectAll();
$2.setLeadItem($1);
$2.setAnchorItem($1);
$2.setSelectedItem($1);
}},
_onmousewheel:function($0){if(!this._popup.isSeeable()){var $1;
var $2=this.getSelected();
if($0.getWheelDelta()<0){$1=$2?this._manager.getNext($2):this._manager.getFirst();
}else{$1=$2?this._manager.getPrevious($2):this._manager.getLast();
}
if($1){this.setSelected($1);
}}else{var $3=$0.getTarget();
if($3!=this&&$3.getParent()!=this._list){this._popup.hide();
this.setCapture(false);
}}},
_onkeydown:function($0){var $1=this._manager;
var $2=this._popup.isSeeable();
switch($0.getKeyIdentifier()){case "Enter":if($2){this.setSelected(this._manager.getSelectedItem());
this._closePopup();
this.setFocused(true);
}else{this._openPopup();
}return;
case "Escape":if($2){$1.setLeadItem(this._oldSelected);
$1.setAnchorItem(this._oldSelected);
$1.setSelectedItem(this._oldSelected);
this._field.setValue(this._oldSelected?this._oldSelected.getLabel():"");
this._closePopup();
this.setFocused(true);
}return;
case "Down":if($0.isAltPressed()){this._togglePopup();
return;
}break;
}},
_onkeypress:function($0){var $1=this._popup.isSeeable();
var $2=this._manager;
switch($0.getKeyIdentifier()){case "PageUp":if(!$1){var $3;
var $4=this.getSelected();
if($4){var $5=this.getPagingInterval();
do{$3=$4;
}while(--$5&&($4=$2.getPrevious($3)));
}else{$3=$2.getLast();
}this.setSelected($3);
return;
}break;
case "PageDown":if(!$1){var $6;
var $4=this.getSelected();
if($4){var $5=this.getPagingInterval();
do{$6=$4;
}while(--$5&&($4=$2.getNext($6)));
}else{$6=$2.getFirst();
}this.setSelected($6||null);
return;
}break;
}if(!this.isEditable()||$1){this._list._onkeypress($0);
var $7=this._manager.getSelectedItem();
if(!$1){this.setSelected($7);
}else if($7){this._field.setValue($7.getLabel());
}}},
_onkeyinput:function($0){var $1=this._popup.isSeeable();
if(!this.isEditable()||$1){this._list._onkeyinput($0);
var $2=this._manager.getSelectedItem();
if(!$1){this.setSelected($2);
}else if($2){this._field.setValue($2.getLabel());
}}},
_visualizeBlur:function(){this.getField()._visualizeBlur();
this.removeState("focused");
},
_visualizeFocus:function(){this.getField()._visualizeFocus();
this.getField().selectAll();
this.addState("focused");
}},
destruct:function(){if(this._popup&&!qx.core.Object.inGlobalDispose()){this._popup.setParent(null);
}var $0=qx.ui.core.ClientDocument.getInstance();
$0.removeEventListener("windowblur",
this._testClosePopup,
this);
var $1=qx.locale.Manager.getInstance();
$1.removeEventListener("changeLocale",
this._onlocalechange,
this);
this._disposeObjects("_popup",
"_list",
"_manager",
"_field",
"_button");
}});




/* ID: qx.ui.layout.VerticalBoxLayout */
qx.Class.define("qx.ui.layout.VerticalBoxLayout",
{extend:qx.ui.layout.BoxLayout,
properties:{orientation:{refine:true,
init:"vertical"}}});




/* ID: qx.ui.form.List */
qx.Class.define("qx.ui.form.List",
{extend:qx.ui.layout.VerticalBoxLayout,
construct:function(){arguments.callee.base.call(this);
this._manager=new qx.ui.selection.SelectionManager(this);
this.addEventListener("mouseover",
this._onmouseover);
this.addEventListener("mousedown",
this._onmousedown);
this.addEventListener("mouseup",
this._onmouseup);
this.addEventListener("click",
this._onclick);
this.addEventListener("dblclick",
this._ondblclick);
this.addEventListener("keydown",
this._onkeydown);
this.addEventListener("keypress",
this._onkeypress);
this.addEventListener("keyinput",
this._onkeyinput);
this.initOverflow();
this.initTabIndex();
},
properties:{appearance:{refine:true,
init:"list"},
overflow:{refine:true,
init:"hidden"},
tabIndex:{refine:true,
init:1},
enableInlineFind:{check:"Boolean",
init:true},
markLeadingItem:{check:"Boolean",
init:false}},
members:{_pressedString:"",
getManager:function(){return this._manager;
},
getListItemTarget:function($0){while($0!=null&&$0.getParent()!=this){$0=$0.getParent();
}return $0;
},
getSelectedItem:function(){return this.getSelectedItems()[0]||null;
},
getSelectedItems:function(){return this._manager.getSelectedItems();
},
_onmouseover:function($0){var $1=this.getListItemTarget($0.getTarget());
if($1){this._manager.handleMouseOver($1,
$0);
}},
_onmousedown:function($0){var $1=this.getListItemTarget($0.getTarget());
if($1){this._manager.handleMouseDown($1,
$0);
}},
_onmouseup:function($0){var $1=this.getListItemTarget($0.getTarget());
if($1){this._manager.handleMouseUp($1,
$0);
}},
_onclick:function($0){var $1=this.getListItemTarget($0.getTarget());
if($1){this._manager.handleClick($1,
$0);
}},
_ondblclick:function($0){var $1=this.getListItemTarget($0.getTarget());
if($1){this._manager.handleDblClick($1,
$0);
}},
_onkeydown:function($0){if($0.getKeyIdentifier()=="Enter"&&!$0.isAltPressed()){var $1=this.getSelectedItems();
for(var $2=0;$2<$1.length;$2++){$1[$2].createDispatchEvent("action");
}}},
_onkeypress:function($0){this._manager.handleKeyPress($0);
},
_lastKeyPress:0,
_onkeyinput:function($0){if(!this.getEnableInlineFind()){return;
}if(((new Date).valueOf()-this._lastKeyPress)>1000){this._pressedString="";
}this._pressedString+=String.fromCharCode($0.getCharCode());
var $1=this.findString(this._pressedString,
null);
if($1){var $2=this._manager._getChangeValue();
var $3=this._manager.getFireChange();
this._manager.setFireChange(false);
this._manager._deselectAll();
this._manager.setItemSelected($1,
true);
this._manager.setAnchorItem($1);
this._manager.setLeadItem($1);
$1.scrollIntoView();
this._manager.setFireChange($3);
if($3&&this._manager._hasChanged($2)){this._manager._dispatchChange();
}}this._lastKeyPress=(new Date).valueOf();
$0.preventDefault();
},
_findItem:function($0,
$1,
$2){var $3=this.getChildren();
if($1==null){$1=$3.indexOf(this.getSelectedItem());
if($1==-1){$1=0;
}}var $4="matches"+$2;
for(var $5=$1;$5<$3.length;$5++){if($3[$5][$4]($0)){return $3[$5];
}}for(var $5=0;$5<$1;$5++){if($3[$5][$4]($0)){return $3[$5];
}}return null;
},
findString:function($0,
$1){return this._findItem($0,
$1||0,
"String");
},
findStringExact:function($0,
$1){return this._findItem($0,
$1||0,
"StringExact");
},
findValue:function($0,
$1){return this._findItem($0,
$1||0,
"Value");
},
findValueExact:function($0,
$1){return this._findItem($0,
$1||0,
"ValueExact");
},
_sortItemsCompare:function($0,
$1){return $0.key<$1.key?-1:$0.key==$1.key?0:1;
},
sortItemsByString:function($0){var $1=[];
var $2=this.getChildren();
for(var $3=0,
$4=$2.length;$3<$4;$3++){$1[$3]={key:$2[$3].getLabel(),
item:$2[$3]};
}$1.sort(this._sortItemsCompare);
if($0){$1.reverse();
}
for(var $3=0;$3<$4;$3++){this.addAt($1[$3].item,
$3);
}},
sortItemsByValue:function($0){var $1=[];
var $2=this.getChildren();
for(var $3=0,
$4=$2.length;$3<$4;$3++){$1[$3]={key:$2[$3].getValue(),
item:$2[$3]};
}$1.sort(this._sortItemsCompare);
if($0){$1.reverse();
}
for(var $3=0;$3<$4;$3++){this.addAt($1[$3].item,
$3);
}}},
destruct:function(){this._disposeObjects("_manager");
}});




/* ID: qx.ui.selection.SelectionManager */
qx.Class.define("qx.ui.selection.SelectionManager",
{extend:qx.core.Target,
construct:function($0){arguments.callee.base.call(this);
this._selectedItems=new qx.ui.selection.Selection(this);
if($0!=null){this.setBoundedWidget($0);
}},
events:{"changeSelection":"qx.event.type.DataEvent"},
properties:{boundedWidget:{check:"qx.ui.core.Widget",
nullable:true},
multiSelection:{check:"Boolean",
init:true},
dragSelection:{check:"Boolean",
init:true},
canDeselect:{check:"Boolean",
init:true},
fireChange:{check:"Boolean",
init:true},
anchorItem:{check:"Object",
nullable:true,
apply:"_applyAnchorItem",
event:"changeAnchorItem"},
leadItem:{check:"Object",
nullable:true,
apply:"_applyLeadItem",
event:"changeLeadItem"},
multiColumnSupport:{check:"Boolean",
init:false}},
members:{_applyAnchorItem:function($0,
$1){if($1){this.renderItemAnchorState($1,
false);
}
if($0){this.renderItemAnchorState($0,
true);
}},
_applyLeadItem:function($0,
$1){if($1){this.renderItemLeadState($1,
false);
}
if($0){this.renderItemLeadState($0,
true);
}},
_getFirst:function(){return this.getBoundedWidget().getFirstVisibleChild();
},
_getLast:function(){return this.getBoundedWidget().getLastVisibleChild();
},
getFirst:function(){var $0=this._getFirst();
if($0){return $0.getEnabled()?$0:this.getNext($0);
}},
getLast:function(){var $0=this._getLast();
if($0){return $0.getEnabled()?$0:this.getPrevious($0);
}},
getItems:function(){return this.getBoundedWidget().getChildren();
},
getNextSibling:function($0){return $0.getNextSibling();
},
getPreviousSibling:function($0){return $0.getPreviousSibling();
},
getNext:function($0){while($0){$0=this.getNextSibling($0);
if(!$0){break;
}
if(this.getItemEnabled($0)){return $0;
}}return null;
},
getPrevious:function($0){while($0){$0=this.getPreviousSibling($0);
if(!$0){break;
}
if(this.getItemEnabled($0)){return $0;
}}return null;
},
isBefore:function($0,
$1){var $2=this.getItems();
return $2.indexOf($0)<$2.indexOf($1);
},
isEqual:function($0,
$1){return $0==$1;
},
getItemHashCode:function($0){return $0.toHashCode();
},
scrollItemIntoView:function($0,
$1){$0.scrollIntoView($1);
},
getItemLeft:function($0){return $0.getOffsetLeft();
},
getItemTop:function($0){return $0.getOffsetTop();
},
getItemWidth:function($0){return $0.getOffsetWidth();
},
getItemHeight:function($0){return $0.getOffsetHeight();
},
getItemEnabled:function($0){return $0.getEnabled();
},
renderItemSelectionState:function($0,
$1){$1?$0.addState("selected"):$0.removeState("selected");
if($0.handleStateChange){$0.handleStateChange();
}},
renderItemAnchorState:function($0,
$1){$1?$0.addState("anchor"):$0.removeState("anchor");
if($0.handleStateChange!=null){$0.handleStateChange();
}},
renderItemLeadState:function($0,
$1){$1?$0.addState("lead"):$0.removeState("lead");
if($0.handleStateChange!=null){$0.handleStateChange();
}},
getItemSelected:function($0){return this._selectedItems.contains($0);
},
setItemSelected:function($0,
$1){switch(this.getMultiSelection()){case true:if(!this.getItemEnabled($0)){return;
}if(this.getItemSelected($0)==$1){return;
}this.renderItemSelectionState($0,
$1);
$1?this._selectedItems.add($0):this._selectedItems.remove($0);
this._dispatchChange();
break;
case false:var $2=this.getSelectedItems()[0];
if($1){var $3=$2;
if(this.isEqual($0,
$3)){return;
}if($3!=null){this.renderItemSelectionState($3,
false);
}this.renderItemSelectionState($0,
true);
this._selectedItems.removeAll();
this._selectedItems.add($0);
this._dispatchChange();
}else{if(!this.isEqual($2,
$0)){this.renderItemSelectionState($0,
false);
this._selectedItems.removeAll();
this._dispatchChange();
}}break;
}},
getSelectedItems:function(){return this._selectedItems.toArray();
},
getSelectedItem:function(){return this._selectedItems.getFirst();
},
setSelectedItems:function($0){var $1=this._getChangeValue();
var $2=this.getFireChange();
this.setFireChange(false);
this._deselectAll();
var $3;
var $4=$0.length;
for(var $5=0;$5<$4;$5++){$3=$0[$5];
if(!this.getItemEnabled($3)){continue;
}this._selectedItems.add($3);
this.renderItemSelectionState($3,
true);
}this.setFireChange($2);
if($2&&this._hasChanged($1)){this._dispatchChange();
}},
setSelectedItem:function($0){if(!$0){return;
}
if(!this.getItemEnabled($0)){return;
}var $1=this._getChangeValue();
var $2=this.getFireChange();
this.setFireChange(false);
this._deselectAll();
this._selectedItems.add($0);
this.renderItemSelectionState($0,
true);
this.setFireChange($2);
if($2&&this._hasChanged($1)){this._dispatchChange();
}},
selectAll:function(){var $0=this._getChangeValue();
var $1=this.getFireChange();
this.setFireChange(false);
this._selectAll();
this.setFireChange($1);
if($1&&this._hasChanged($0)){this._dispatchChange();
}},
_selectAll:function(){if(!this.getMultiSelection()){return;
}var $0;
var $1=this.getItems();
var $2=$1.length;
this._selectedItems.removeAll();
for(var $3=0;$3<$2;$3++){$0=$1[$3];
if(!this.getItemEnabled($0)){continue;
}this._selectedItems.add($0);
this.renderItemSelectionState($0,
true);
}return true;
},
deselectAll:function(){var $0=this._getChangeValue();
var $1=this.getFireChange();
this.setFireChange(false);
this._deselectAll();
this.setFireChange($1);
if($1&&this._hasChanged($0))this._dispatchChange();
},
_deselectAll:function(){var $0=this._selectedItems.toArray();
for(var $1=0;$1<$0.length;$1++){this.renderItemSelectionState($0[$1],
false);
}this._selectedItems.removeAll();
return true;
},
selectItemRange:function($0,
$1){var $2=this._getChangeValue();
var $3=this.getFireChange();
this.setFireChange(false);
this._selectItemRange($0,
$1,
true);
this.setFireChange($3);
if($3&&this._hasChanged($2)){this._dispatchChange();
}},
_selectItemRange:function($0,
$1,
$2){if(this.isBefore($1,
$0)){return this._selectItemRange($1,
$0,
$2);
}if($2){this._deselectAll();
}var $3=$0;
while($3!=null){if(this.getItemEnabled($3)){this._selectedItems.add($3);
this.renderItemSelectionState($3,
true);
}if(this.isEqual($3,
$1)){break;
}$3=this.getNext($3);
}return true;
},
_deselectItemRange:function($0,
$1){if(this.isBefore($1,
$0)){return this._deselectItemRange($1,
$0);
}var $2=$0;
while($2!=null){this._selectedItems.remove($2);
this.renderItemSelectionState($2,
false);
if(this.isEqual($2,
$1)){break;
}$2=this.getNext($2);
}},
_activeDragSession:false,
handleMouseDown:function($0,
$1){$1.stopPropagation();
if(!$1.isLeftButtonPressed()&&!$1.isRightButtonPressed()){return;
}if($1.isRightButtonPressed()&&this.getItemSelected($0)){return;
}if($1.isShiftPressed()||this.getDragSelection()||(!this.getItemSelected($0)&&!$1.isCtrlPressed())){this._onmouseevent($0,
$1);
}else{this.setLeadItem($0);
}this._activeDragSession=this.getDragSelection();
if(this._activeDragSession){this.getBoundedWidget().addEventListener("mouseup",
this._ondragup,
this);
this.getBoundedWidget().setCapture(true);
}},
_ondragup:function($0){this.getBoundedWidget().removeEventListener("mouseup",
this._ondragup,
this);
this.getBoundedWidget().setCapture(false);
this._activeDragSession=false;
},
handleMouseUp:function($0,
$1){if(!$1.isLeftButtonPressed()){return;
}
if($1.isCtrlPressed()||this.getItemSelected($0)&&!this._activeDragSession){this._onmouseevent($0,
$1);
}
if(this._activeDragSession){this._activeDragSession=false;
this.getBoundedWidget().setCapture(false);
}},
handleMouseOver:function($0,
$1){if(!this.getDragSelection()||!this._activeDragSession){return;
}this._onmouseevent($0,
$1,
true);
},
handleClick:function($0,
$1){},
handleDblClick:function($0,
$1){},
_onmouseevent:function($0,
$1,
$2){if(!this.getItemEnabled($0)){return;
}var $3=this._getChangeValue();
var $4=this.getLeadItem();
var $5=this.getFireChange();
this.setFireChange(false);
var $6=this.getSelectedItems();
var $7=$6.length;
this.setLeadItem($0);
var $8=this.getAnchorItem();
var $9=$1.isCtrlPressed();
var $a=$1.isShiftPressed();
if(!$8||$7==0||($9&&!$a&&this.getMultiSelection()&&!this.getDragSelection())){this.setAnchorItem($0);
$8=$0;
}if((!$9&&!$a&&!this._activeDragSession||!this.getMultiSelection())){if(!this.getItemEnabled($0)){return;
}this._deselectAll();
this.setAnchorItem($0);
if(this._activeDragSession){this.scrollItemIntoView((this.getBoundedWidget().getScrollTop()>(this.getItemTop($0)-1)?this.getPrevious($0):this.getNext($0))||$0);
}
if(!this.getItemSelected($0)){this.renderItemSelectionState($0,
true);
}this._selectedItems.add($0);
this._addToCurrentSelection=true;
}else if(this._activeDragSession&&$2){if($4){this._deselectItemRange($8,
$4);
}if(this.isBefore($8,
$0)){if(this._addToCurrentSelection){this._selectItemRange($8,
$0,
false);
}else{this._deselectItemRange($8,
$0);
}}else{if(this._addToCurrentSelection){this._selectItemRange($0,
$8,
false);
}else{this._deselectItemRange($0,
$8);
}}this.scrollItemIntoView((this.getBoundedWidget().getScrollTop()>(this.getItemTop($0)-1)?this.getPrevious($0):this.getNext($0))||$0);
}else if(this.getMultiSelection()&&$9&&!$a){if(!this._activeDragSession){this._addToCurrentSelection=!(this.getCanDeselect()&&this.getItemSelected($0));
}this.setItemSelected($0,
this._addToCurrentSelection);
this.setAnchorItem($0);
}else if(this.getMultiSelection()&&$9&&$a){if(!this._activeDragSession){this._addToCurrentSelection=!(this.getCanDeselect()&&this.getItemSelected($0));
}
if(this._addToCurrentSelection){this._selectItemRange($8,
$0,
false);
}else{this._deselectItemRange($8,
$0);
}}else if(this.getMultiSelection()&&!$9&&$a){if(this.getCanDeselect()){this._selectItemRange($8,
$0,
true);
}else{if($4){this._deselectItemRange($8,
$4);
}this._selectItemRange($8,
$0,
false);
}}this.setFireChange($5);
if($5&&this._hasChanged($3)){this._dispatchChange();
}},
handleKeyDown:function($0){this.warn("qx.ui.selection.SelectionManager.handleKeyDown is deprecated! "+"Use keypress insted and bind it to the onkeypress event.");
this.handleKeyPress($0);
},
handleKeyPress:function($0){var $1=this._getChangeValue();
var $2=this.getFireChange();
this.setFireChange(false);
if($0.getKeyIdentifier()=="A"&&$0.isCtrlPressed()){if(this.getMultiSelection()){this._selectAll();
this.setLeadItem(this.getFirst());
}}else{var $3=this.getAnchorItem();
var $4=this.getItemToSelect($0);
if($4&&this.getItemEnabled($4)){this.setLeadItem($4);
this.scrollItemIntoView($4);
$0.preventDefault();
if($0.isShiftPressed()&&this.getMultiSelection()){if($3==null){this.setAnchorItem($4);
}this._selectItemRange(this.getAnchorItem(),
$4,
true);
}else if(!$0.isCtrlPressed()){this._deselectAll();
this.renderItemSelectionState($4,
true);
this._selectedItems.add($4);
this.setAnchorItem($4);
}else if($0.getKeyIdentifier()=="Space"){if(this._selectedItems.contains($4)){this.renderItemSelectionState($4,
false);
this._selectedItems.remove($4);
this.setAnchorItem(this._selectedItems.getFirst());
}else{if(!$0.isCtrlPressed()||!this.getMultiSelection()){this._deselectAll();
}this.renderItemSelectionState($4,
true);
this._selectedItems.add($4);
this.setAnchorItem($4);
}}}}this.setFireChange($2);
if($2&&this._hasChanged($1)){this._dispatchChange();
}},
getItemToSelect:function($0){if($0.isAltPressed()){return null;
}switch($0.getKeyIdentifier()){case "Home":return this.getHome(this.getLeadItem());
case "End":return this.getEnd(this.getLeadItem());
case "Down":return this.getDown(this.getLeadItem());
case "Up":return this.getUp(this.getLeadItem());
case "Left":return this.getLeft(this.getLeadItem());
case "Right":return this.getRight(this.getLeadItem());
case "PageUp":return this.getPageUp(this.getLeadItem())||this.getHome(this.getLeadItem());
case "PageDown":return this.getPageDown(this.getLeadItem())||this.getEnd(this.getLeadItem());
case "Space":if($0.isCtrlPressed()){return this.getLeadItem();
}}return null;
},
_dispatchChange:function(){if(!this.getFireChange()){return;
}
if(this.hasEventListeners("changeSelection")){this.dispatchEvent(new qx.event.type.DataEvent("changeSelection",
this.getSelectedItems()),
true);
}},
_hasChanged:function($0){return $0!=this._getChangeValue();
},
_getChangeValue:function(){return this._selectedItems.getChangeValue();
},
getHome:function(){return this.getFirst();
},
getEnd:function(){return this.getLast();
},
getDown:function($0){if(!$0){return this.getFirst();
}return this.getMultiColumnSupport()?(this.getUnder($0)||this.getLast()):this.getNext($0);
},
getUp:function($0){if(!$0){return this.getLast();
}return this.getMultiColumnSupport()?(this.getAbove($0)||this.getFirst()):this.getPrevious($0);
},
getLeft:function($0){if(!this.getMultiColumnSupport()){return null;
}return !$0?this.getLast():this.getPrevious($0);
},
getRight:function($0){if(!this.getMultiColumnSupport()){return null;
}return !$0?this.getFirst():this.getNext($0);
},
getAbove:function($0){throw new Error("getAbove(): Not implemented yet");
},
getUnder:function($0){throw new Error("getUnder(): Not implemented yet");
},
getPageUp:function($0){var $1=this.getBoundedWidget();
var $2=$1.getScrollTop();
var $3=$1.getClientHeight();
var $4=this.getLeadItem();
if(!$4){$4=this.getFirst();
}var $5=0;
while($5<2){while($4&&(this.getItemTop($4)-this.getItemHeight($4)>=$2)){$4=this.getUp($4);
}if($4==null){break;
}if($4!=this.getLeadItem()){this.scrollItemIntoView($4,
true);
break;
}$1.setScrollTop($2-$3-this.getItemHeight($4));
$2=$1.getScrollTop();
$5++;
}return $4;
},
getPageDown:function($0){var $1=this.getBoundedWidget();
var $2=$1.getScrollTop();
var $3=$1.getClientHeight();
var $4=this.getLeadItem();
if(!$4){$4=this.getFirst();
}var $5=0;
while($5<2){while($4&&((this.getItemTop($4)+(2*this.getItemHeight($4)))<=($2+$3))){$4=this.getDown($4);
}if($4==null){break;
}if($4!=this.getLeadItem()){break;
}$1.setScrollTop($2+$3-2*this.getItemHeight($4));
$2=$1.getScrollTop();
$5++;
}return $4;
}},
destruct:function(){this._disposeObjects("_selectedItems");
}});




/* ID: qx.ui.selection.Selection */
qx.Class.define("qx.ui.selection.Selection",
{extend:qx.core.Object,
construct:function($0){arguments.callee.base.call(this);
this.__manager=$0;
this.removeAll();
},
members:{add:function($0){this.__storage[this.getItemHashCode($0)]=$0;
},
remove:function($0){delete this.__storage[this.getItemHashCode($0)];
},
removeAll:function(){this.__storage={};
},
contains:function($0){return this.getItemHashCode($0) in this.__storage;
},
toArray:function(){var $0=[];
for(var $1 in this.__storage){$0.push(this.__storage[$1]);
}return $0;
},
getFirst:function(){for(var $0 in this.__storage){return this.__storage[$0];
}return null;
},
getChangeValue:function(){var $0=[];
for(var $1 in this.__storage){$0.push($1);
}$0.sort();
return $0.join(";");
},
getItemHashCode:function($0){return this.__manager.getItemHashCode($0);
},
isEmpty:function(){return qx.lang.Object.isEmpty(this.__storage);
}},
destruct:function(){this._disposeFields("__storage",
"__manager");
}});




/* ID: qx.ui.popup.Popup */
qx.Class.define("qx.ui.popup.Popup",
{extend:qx.ui.layout.CanvasLayout,
construct:function(){arguments.callee.base.call(this);
this.setZIndex(this._minZIndex);
if(this._isFocusRoot){this.activateFocusRoot();
}this.initHeight();
this.initWidth();
},
properties:{appearance:{refine:true,
init:"popup"},
width:{refine:true,
init:"auto"},
height:{refine:true,
init:"auto"},
display:{refine:true,
init:false},
autoHide:{check:"Boolean",
init:true},
centered:{check:"Boolean",
init:false},
restrictToPageOnOpen:{check:"Boolean",
init:true},
restrictToPageLeft:{check:"Integer",
init:0},
restrictToPageRight:{check:"Integer",
init:0},
restrictToPageTop:{check:"Integer",
init:0},
restrictToPageBottom:{check:"Integer",
init:0}},
members:{_isFocusRoot:true,
_showTimeStamp:(new Date(0)).valueOf(),
_hideTimeStamp:(new Date(0)).valueOf(),
_beforeAppear:function(){arguments.callee.base.call(this);
if(this.getRestrictToPageOnOpen()){this._wantedLeft=this.getLeft();
if(this._wantedLeft!=null){this.setLeft(10000);
if(this.getElement()!=null){this.getElement().style.left=10000;
}}}qx.ui.popup.PopupManager.getInstance().add(this);
qx.ui.popup.PopupManager.getInstance().update(this);
this._showTimeStamp=(new Date).valueOf();
this.bringToFront();
},
_beforeDisappear:function(){arguments.callee.base.call(this);
qx.ui.popup.PopupManager.getInstance().remove(this);
this._hideTimeStamp=(new Date).valueOf();
},
_afterAppear:function(){arguments.callee.base.call(this);
if(this.getRestrictToPageOnOpen()){var $0=qx.ui.core.ClientDocument.getInstance();
var $1=$0.getClientWidth();
var $2=$0.getClientHeight();
var $3=qx.bom.Viewport.getScrollTop();
var $4=qx.bom.Viewport.getScrollLeft();
var $5=this.getRestrictToPageLeft()+$4;
var $6=this.getRestrictToPageRight()-$4;
var $7=this.getRestrictToPageTop()+$3;
var $8=this.getRestrictToPageBottom()-$3;
var $9=(this._wantedLeft==null)?this.getLeft():this._wantedLeft;
var $a=this.getTop();
var $b=this.getBoxWidth();
var $c=this.getBoxHeight();
var $d=this.getLeft();
var $e=$a;
if($9+$b>$1-$6){$9=$1-$6-$b;
}
if($a+$c>$2-$8){$a=$2-$8-$c;
}
if($9<$5){$9=$5;
}
if($a<$7){$a=$7;
}
if($9!=$d||$a!=$e){var $f=this;
window.setTimeout(function(){$f.setLeft($9);
$f.setTop($a);
},
0);
}}},
_makeActive:function(){this.getFocusRoot().setActiveChild(this);
},
_makeInactive:function(){var $0=this.getFocusRoot();
var $1=$0.getActiveChild();
if($1==this){$0.setActiveChild($0);
}},
_minZIndex:1e6,
bringToFront:function(){this.setZIndex(this._minZIndex+1000000);
this._sendTo();
},
sendToBack:function(){this.setZIndex(this._minZIndex+1);
this._sendTo();
},
_sendTo:function(){var $0=qx.lang.Object.getValues(qx.ui.popup.PopupManager.getInstance().getAll());
if(qx.Class.isDefined("qx.ui.menu.Manager")){var $1=qx.lang.Object.getValues(qx.ui.menu.Manager.getInstance().getAll());
var $2=$0.concat($1).sort(qx.util.Compare.byZIndex);
}else{var $2=$0.sort(qx.util.Compare.byZIndex);
}var $3=$2.length;
var $4=this._minZIndex;
for(var $5=0;$5<$3;$5++){$2[$5].setZIndex($4++);
}},
getShowTimeStamp:function(){return this._showTimeStamp;
},
getHideTimeStamp:function(){return this._hideTimeStamp;
},
positionRelativeTo:function($0,
$1,
$2){if($0 instanceof qx.ui.core.Widget){$0=$0.getElement();
}
if($0){var $3=qx.bom.element.Location.get($0);
this.setLocation($3.left+($1||0),
$3.top+($2||0));
}else{this.warn('Missing reference element');
}},
centerToBrowser:function(){var $0=qx.ui.core.ClientDocument.getInstance();
var $1=($0.getClientWidth()-this.getBoxWidth())/2;
var $2=($0.getClientHeight()-this.getBoxHeight())/2;
this.setLeft($1<0?0:$1);
this.setTop($2<0?0:$2);
}},
destruct:function(){qx.ui.popup.PopupManager.getInstance().remove(this);
this._disposeFields("_showTimeStamp",
"_hideTimeStamp");
}});




/* ID: qx.ui.popup.PopupManager */
qx.Class.define("qx.ui.popup.PopupManager",
{type:"singleton",
extend:qx.util.manager.Object,
construct:function(){arguments.callee.base.call(this);
},
members:{update:function($0){if(!($0 instanceof qx.ui.core.Widget)){$0=null;
}var $1,
$2;
var $3=this.getAll();
for($2 in $3){$1=$3[$2];
if(!$1.getAutoHide()||$0==$1||$1.contains($0)){continue;
}
if(qx.Class.isDefined("qx.ui.popup.ToolTip")&&$0 instanceof qx.ui.popup.ToolTip&&!($1 instanceof qx.ui.popup.ToolTip)){continue;
}$1.hide();
}}}});




/* ID: qx.util.Compare */
qx.Class.define("qx.util.Compare",
{statics:{byString:function($0,
$1){return $0==$1?0:$0>$1?1:-1;
},
byStringCaseInsensitive:function($0,
$1){return qx.util.Compare.byString($0.toLowerCase(),
$1.toLowerCase());
},
byStringUmlautsShort:function($0,
$1){return qx.util.Compare.byString(qx.util.Normalization.umlautsShort($0),
qx.util.Normalization.umlautsShort($1));
},
byStringUmlautsShortCaseInsensitive:function($0,
$1){return qx.util.Compare.byString(qx.util.Normalization.umlautsShort($0).toLowerCase(),
qx.util.Normalization.umlautsShort($1).toLowerCase());
},
byStringUmlautsLong:function($0,
$1){return qx.util.Compare.byString(qx.util.Normalization.umlautsLong($0),
qx.util.Normalization.umlautsLong($1));
},
byStringUmlautsLongCaseInsensitive:function($0,
$1){return qx.util.Compare.byString(qx.util.Normalization.umlautsLong($0).toLowerCase(),
qx.util.Normalization.umlautsLong($1).toLowerCase());
},
byFloat:function($0,
$1){return $0-$1;
},
byIntegerString:function($0,
$1){return parseInt($0)-parseInt($1);
},
byFloatString:function($0,
$1){return parseFloat($0)-parseFloat($1);
},
byIPv4:function($0,
$1){var $2=$0.split(".",
4);
var $3=$1.split(".",
4);
for(var $4=0;$4<3;$4++){$0=parseInt($2[$4]);
$1=parseInt($3[$4]);
if($0!=$1){return $0-$1;
}}return parseInt($2[3])-parseInt($3[3]);
},
byZIndex:function($0,
$1){return $0.getZIndex()-$1.getZIndex();
}},
defer:function($0){$0.byInteger=$0.byNumber=$0.byFloat;
$0.byNumberString=$0.byFloatString;
}});




/* ID: qx.util.Normalization */
qx.Class.define("qx.util.Normalization",
{statics:{__umlautsRegExp:new RegExp("[\xE4\xF6\xFC\xDF\xC4\xD6\xDC]",
"g"),
__umlautsShortData:{"\xC4":"A",
"\xD6":"O",
"\xDC":"U",
"\xE4":"a",
"\xF6":"o",
"\xFC":"u",
"\xDF":"s"},
__umlautsShort:function($0){return qx.util.Normalization.__umlautsShortData[$0];
},
umlautsShort:function($0){return $0.replace(qx.util.Normalization.__umlautsRegExp,
qx.lang.Function.bind(this.__umlautsShort,
this));
},
__umlautsLongData:{"\xC4":"Ae",
"\xD6":"Oe",
"\xDC":"Ue",
"\xE4":"ae",
"\xF6":"oe",
"\xFC":"ue",
"\xDF":"ss"},
__umlautsLong:function($0){return qx.util.Normalization.__umlautsLongData[$0];
},
umlautsLong:function($0){return $0.replace(qx.util.Normalization.__umlautsRegExp,
qx.lang.Function.bind(this.__umlautsLong,
this));
}}});




/* ID: qx.bom.element.Location */
qx.Class.define("qx.bom.element.Location",
{statics:{__style:function($0,
$1){return qx.bom.element.Style.get($0,
$1,
qx.bom.element.Style.COMPUTED_MODE,
false);
},
__num:function($0,
$1){return parseInt(qx.bom.element.Style.get($0,
$1,
qx.bom.element.Style.COMPUTED_MODE,
false))||0;
},
__computeScroll:function($0){var $1=0,
$2=0;
if(qx.core.Variant.isSet("qx.client",
"mshtml")&&$0.getBoundingClientRect){var $3=qx.dom.Node.getWindow($0);
$1-=qx.bom.Viewport.getScrollLeft($3);
$2-=qx.bom.Viewport.getScrollTop($3);
}else{var $4=qx.dom.Node.getDocument($0).body;
$0=$0.parentNode;
while($0&&$0!=$4){$1+=$0.scrollLeft;
$2+=$0.scrollTop;
$0=$0.parentNode;
}}return {left:$1,
top:$2};
},
__computeBody:qx.core.Variant.select("qx.client",
{"mshtml":function($0){var $1=qx.dom.Node.getDocument($0);
var $2=$1.body;
var $3=$2.offsetLeft;
var $4=$2.offsetTop;
$3-=this.__num($2,
"borderLeftWidth");
$4-=this.__num($2,
"borderTopWidth");
if($1.compatMode==="CSS1Compat"){$3+=this.__num($2,
"marginLeft");
$4+=this.__num($2,
"marginTop");
}return {left:$3,
top:$4};
},
"webkit":function($0){var $1=qx.dom.Node.getDocument($0);
var $2=$1.body;
var $3=$2.offsetLeft;
var $4=$2.offsetTop;
$3+=this.__num($2,
"borderLeftWidth");
$4+=this.__num($2,
"borderTopWidth");
if($1.compatMode==="CSS1Compat"){$3+=this.__num($2,
"marginLeft");
$4+=this.__num($2,
"marginTop");
}return {left:$3,
top:$4};
},
"gecko":function($0){var $1=qx.dom.Node.getDocument($0).body;
var $2=$1.offsetLeft;
var $3=$1.offsetTop;
if(qx.bom.element.Dimension.getBoxSizing($1)!=="border-box"){$2+=this.__num($1,
"borderLeftWidth");
$3+=this.__num($1,
"borderTopWidth");
if(!$0.getBoundingClientRect){var $4;
while($0){if(this.__style($0,
"position")==="absolute"||this.__style($0,
"position")==="fixed"){$4=true;
break;
}$0=$0.offsetParent;
}
if(!$4){$2+=this.__num($1,
"borderLeftWidth");
$3+=this.__num($1,
"borderTopWidth");
}}}return {left:$2,
top:$3};
},
"default":function($0){var $1=qx.dom.Node.getDocument($0).body;
var $2=$1.offsetLeft;
var $3=$1.offsetTop;
return {left:$2,
top:$3};
}}),
__computeOffset:qx.core.Variant.select("qx.client",
{"mshtml|webkit":function($0){var $1=qx.dom.Node.getDocument($0);
if($0.getBoundingClientRect){var $2=$0.getBoundingClientRect();
var $3=$2.left;
var $4=$2.top;
if($1.compatMode==="CSS1Compat"){$3-=this.__num($0,
"borderLeftWidth");
$4-=this.__num($0,
"borderTopWidth");
}}else{var $3=$0.offsetLeft;
var $4=$0.offsetTop;
$0=$0.offsetParent;
var $5=$1.body;
while($0&&$0!=$5){$3+=$0.offsetLeft;
$4+=$0.offsetTop;
$3+=this.__num($0,
"borderLeftWidth");
$4+=this.__num($0,
"borderTopWidth");
$0=$0.offsetParent;
}}return {left:$3,
top:$4};
},
"gecko":function($0){if($0.getBoundingClientRect){var $1=$0.getBoundingClientRect();
var $2=Math.round($1.left);
var $3=Math.round($1.top);
}else{var $2=0;
var $3=0;
var $4=qx.dom.Node.getDocument($0).body;
var $5=qx.bom.element.Dimension;
if($5.getBoxSizing($0)!=="border-box"){$2-=this.__num($0,
"borderLeftWidth");
$3-=this.__num($0,
"borderTopWidth");
}
while($0&&$0!==$4){$2+=$0.offsetLeft;
$3+=$0.offsetTop;
if($5.getBoxSizing($0)!=="border-box"){$2+=this.__num($0,
"borderLeftWidth");
$3+=this.__num($0,
"borderTopWidth");
}if($0.parentNode&&this.__style($0.parentNode,
"overflow")!="visible"){$2+=this.__num($0.parentNode,
"borderLeftWidth");
$3+=this.__num($0.parentNode,
"borderTopWidth");
}$0=$0.offsetParent;
}}return {left:$2,
top:$3};
},
"default":function($0){var $1=0;
var $2=0;
var $3=qx.dom.Node.getDocument($0).body;
while($0&&$0!==$3){$1+=$0.offsetLeft;
$2+=$0.offsetTop;
$0=$0.offsetParent;
}return {left:$1,
top:$2};
}}),
get:function($0,
$1){var $2=this.__computeBody($0);
if($0.tagName=="BODY"){var $3=$2.left;
var $4=$2.top;
}else{var $5=this.__computeOffset($0);
var $6=this.__computeScroll($0);
var $3=$5.left+$2.left-$6.left;
var $4=$5.top+$2.top-$6.top;
}var $7=$3+$0.offsetWidth;
var $8=$4+$0.offsetHeight;
if($1){switch($1){case "margin":$3-=this.__num($0,
"marginLeft");
$4-=this.__num($0,
"marginTop");
$7+=this.__num($0,
"marginRight");
$8+=this.__num($0,
"marginBottom");
break;
case "box":break;
case "padding":$3+=this.__num($0,
"paddingLeft");
$4+=this.__num($0,
"paddingTop");
$7-=this.__num($0,
"paddingRight");
$8-=this.__num($0,
"paddingBottom");
case "scroll":$3-=$0.scrollLeft;
$4-=$0.scrollTop;
$7+=$0.scrollLeft;
$8+=$0.scrollTop;
case "border":$3+=this.__num($0,
"borderLeftWidth");
$4+=this.__num($0,
"borderTopWidth");
$7-=this.__num($0,
"borderRightWidth");
$8-=this.__num($0,
"borderBottomWidth");
break;
default:throw new Error("Invalid mode for location detection: "+$1);
}}return {left:$3,
top:$4,
right:$7,
bottom:$8};
},
getLeft:function($0,
$1){return this.get($0,
$1).left;
},
getTop:function($0,
$1){return this.get($0,
$1).top;
},
getRight:function($0,
$1){return this.get($0,
$1).right;
},
getBottom:function($0,
$1){return this.get($0,
$1).bottom;
},
getRelative:function($0,
$1,
$2,
$3){var $4=this.get($0,
$2);
var $5=this.get($1,
$3);
return {left:$4.left-$5.left,
top:$4.top-$5.top,
right:$4.right-$5.right,
bottom:$4.bottom-$5.bottom};
}}});




/* ID: qx.bom.element.Style */
qx.Class.define("qx.bom.element.Style",
{statics:{__hints:{names:{"float":qx.core.Variant.isSet("qx.client",
"mshtml")?"styleFloat":"cssFloat",
"boxSizing":qx.core.Variant.isSet("qx.client",
"gecko")?"mozBoxSizing":"boxSizing"},
mshtmlPixel:{width:"pixelWidth",
height:"pixelHeight",
left:"pixelLeft",
right:"pixelRight",
top:"pixelTop",
bottom:"pixelBottom"},
special:{clip:true,
cursor:true,
opacity:true,
overflowX:true,
overflowY:true}},
setCss:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){$0.style.cssText=$1;
},
"default":function($0,
$1){$0.setAttribute("style",
$1);
}}),
getCss:qx.core.Variant.select("qx.client",
{"mshtml":function($0){return $0.style.cssText.toLowerCase();
},
"default":function($0){return $0.getAttribute("style");
}}),
COMPUTED_MODE:1,
CASCADED_MODE:2,
LOCAL_MODE:3,
set:function($0,
$1,
$2,
$3){var $4=this.__hints;
$1=$4.names[$1]||$1;
$0.style[$1]=$2||"";
},
reset:function($0,
$1,
$2){var $3=this.__hints;
$1=$3.names[$1]||$1;
$0.style[$1]="";
},
get:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1,
$2,
$3){var $4=this.__hints;
$1=$4.names[$1]||$1;
switch($2){case this.LOCAL_MODE:return $0.style[$1]||"";
case this.CASCADED_MODE:return $0.currentStyle[$1];
default:var $5=$0.currentStyle[$1];
if(/^-?[\.\d]+(px)?$/i.test($5)){return $5;
}var $6=$4.mshtmlPixel[$1];
if($6){var $7=$0.style[$1];
$0.style[$1]=$5||0;
var $8=$0.style[$6]+"px";
$0.style[$1]=$7;
return $8;
}if(/^-?[\.\d]+(em|pt|%)?$/i.test($5)){throw new Error("Untranslated computed property value: "+$1+". Only pixel values work well across different clients.");
}return $5;
}},
"default":function($0,
$1,
$2,
$3){var $4=this.__hints;
$1=$4.names[$1]||$1;
switch($2){case this.LOCAL_MODE:return $0.style[$1];
case this.CASCADED_MODE:if($0.currentStyle){return $0.currentStyle[$1];
}throw new Error("Cascaded styles are not supported in this browser!");
default:var $5=qx.dom.Node.getDocument($0);
var $6=$5.defaultView.getComputedStyle($0,
null);
return $6?$6[$1]:null;
}}})}});




/* ID: qx.bom.element.Dimension */
qx.Class.define("qx.bom.element.Dimension",
{statics:{getWidth:function($0){return $0.offsetWidth;
},
getHeight:function($0){return $0.offsetHeight;
},
getClientWidth:function($0){return $0.clientWidth;
},
getClientHeight:function($0){return $0.clientHeight;
},
getScrollWidth:function($0){return $0.scrollWidth;
},
getScrollHeight:function($0){return $0.scrollHeight;
},
__nativeBorderBox:{tags:{button:true,
select:true},
types:{search:true,
button:true,
submit:true,
reset:true,
checkbox:true,
radio:true}},
__usesNativeBorderBox:function($0){var $1=this.__nativeBorderBox;
return $1.tags[$0.tagName.toLowerCase()]||$1.types[$0.type];
},
setBoxSizing:qx.core.Variant.select("qx.client",
{"gecko":function($0,
$1){$0.style.MozBoxSizing=$1||"";
},
"mshtml":function($0,
$1){},
"default":function($0,
$1){$0.style.boxSizing=$1||"";
}}),
getBoxSizing:qx.core.Variant.select("qx.client",
{"gecko":function($0){return qx.bom.element.Style.get($0,
"MozBoxSizing",
qx.bom.element.Style.COMPUTED_MODE,
false);
},
"mshtml":function($0){if(qx.bom.Document.isStandardMode(qx.dom.Node.getDocument($0))){if(!this.__usesNativeBorderBox($0)){return "content-box";
}}return "border-box";
},
"default":function($0){return qx.bom.element.Style.get($0,
"boxSizing",
qx.bom.element.Style.COMPUTED_MODE,
false);
}})}});




/* ID: qx.bom.Document */
qx.Class.define("qx.bom.Document",
{statics:{isQuirksMode:function($0){return ($0||window).document.compatMode!=="CSS1Compat";
},
isStandardMode:function($0){return ($0||window).document.compatMode==="CSS1Compat";
},
getWidth:function($0){var $1=($0||window).document;
var $2=qx.bom.Viewport.getWidth($0);
var $3=$1.compatMode==="CSS1Compat"?$1.documentElement.scrollWidth:$1.body.scrollWidth;
return Math.max($3,
$2);
},
getHeight:function($0){var $1=($0||window).document;
var $2=qx.bom.Viewport.getHeight($0);
var $3=$1.compatMode==="CSS1Compat"?$1.documentElement.scrollHeight:$1.body.scrollHeight;
return Math.max($3,
$2);
}}});




/* ID: qx.ui.basic.Atom */
qx.Class.define("qx.ui.basic.Atom",
{extend:qx.ui.layout.BoxLayout,
construct:function($0,
$1,
$2,
$3,
$4){arguments.callee.base.call(this);
this.getLayoutImpl().setEnableFlexSupport(false);
if($0!==undefined){this.setLabel($0);
}if(qx.Class.isDefined("qx.ui.embed.Flash")&&$4!=null&&$2!=null&&$3!=null&&qx.ui.embed.Flash.getPlayerVersion().getMajor()>0){this._flashMode=true;
this.setIcon($4);
}else if($1!=null){this.setIcon($1);
}
if($1||$4){if($2!=null){this.setIconWidth($2);
}
if($3!=null){this.setIconHeight($3);
}}this.initWidth();
this.initHeight();
},
properties:{orientation:{refine:true,
init:"horizontal"},
allowStretchX:{refine:true,
init:false},
allowStretchY:{refine:true,
init:false},
appearance:{refine:true,
init:"atom"},
stretchChildrenOrthogonalAxis:{refine:true,
init:false},
width:{refine:true,
init:"auto"},
height:{refine:true,
init:"auto"},
horizontalChildrenAlign:{refine:true,
init:"center"},
verticalChildrenAlign:{refine:true,
init:"middle"},
spacing:{refine:true,
init:4},
label:{apply:"_applyLabel",
nullable:true,
dispose:true,
check:"Label"},
icon:{check:"String",
apply:"_applyIcon",
nullable:true,
themeable:true},
disabledIcon:{check:"String",
apply:"_applyDisabledIcon",
nullable:true,
themeable:true},
show:{init:"both",
check:["both",
"label",
"icon",
"none"],
themeable:true,
nullable:true,
inheritable:true,
apply:"_applyShow",
event:"changeShow"},
iconPosition:{init:"left",
check:["top",
"right",
"bottom",
"left"],
themeable:true,
apply:"_applyIconPosition"},
iconWidth:{check:"Integer",
themeable:true,
apply:"_applyIconWidth",
nullable:true},
iconHeight:{check:"Integer",
themeable:true,
apply:"_applyIconHeight",
nullable:true}},
members:{_flashMode:false,
_labelObject:null,
_iconObject:null,
_createLabel:function(){var $0=this._labelObject=new qx.ui.basic.Label(this.getLabel());
$0.setAnonymous(true);
$0.setCursor("default");
this.addAt($0,
this._iconObject?1:0);
},
_createIcon:function(){if(this._flashMode&&qx.Class.isDefined("qx.ui.embed.Flash")){var $0=this._iconObject=new qx.ui.embed.Flash(this.getIcon());
}else{var $0=this._iconObject=new qx.ui.basic.Image();
}$0.setAnonymous(true);
var $1=this.getIconWidth();
if($1!==null){this._iconObject.setWidth($1);
}var $2=this.getIconWidth();
if($2!==null){this._iconObject.setHeight($2);
}this._updateIcon();
this.addAt($0,
0);
},
_updateIcon:function(){var $0=this.getIcon();
if(this._iconObject&&this.getIcon&&this.getDisabledIcon){var $1=this.getDisabledIcon();
if($1){if(this.getEnabled()){$0?this._iconObject.setSource($0):this._iconObject.resetSource();
}else{$1?this._iconObject.setSource($1):this._iconObject.resetSource();
}this._iconObject.setEnabled(true);
}else{$0?this._iconObject.setSource($0):this._iconObject.resetSource();
this._iconObject.resetEnabled();
}}},
getLabelObject:function(){return this._labelObject;
},
getIconObject:function(){return this._iconObject;
},
_applyIconPosition:function($0,
$1){switch($0){case "top":case "bottom":this.setOrientation("vertical");
this.setReverseChildrenOrder($0=="bottom");
break;
default:this.setOrientation("horizontal");
this.setReverseChildrenOrder($0=="right");
break;
}},
_applyShow:function($0,
$1){this._handleIcon();
this._handleLabel();
},
_applyLabel:function($0,
$1){if(this._labelObject){$0?this._labelObject.setText($0):this._labelObject.resetText();
}this._handleLabel();
},
_applyIcon:function($0,
$1){this._updateIcon();
this._handleIcon();
},
_applyDisabledIcon:function($0,
$1){this._updateIcon();
this._handleIcon();
},
_applyIconWidth:function($0,
$1){if(this._iconObject){this._iconObject.setWidth($0);
}},
_applyIconHeight:function($0,
$1){if(this._iconObject){this._iconObject.setHeight($0);
}},
_iconIsVisible:false,
_labelIsVisible:false,
_handleLabel:function(){switch(this.getShow()){case "label":case "both":case "inherit":this._labelIsVisible=!!this.getLabel();
break;
default:this._labelIsVisible=false;
}
if(this._labelIsVisible){this._labelObject?this._labelObject.setDisplay(true):this._createLabel();
}else if(this._labelObject){this._labelObject.setDisplay(false);
}},
_handleIcon:function(){switch(this.getShow()){case "icon":case "both":case "inherit":this._iconIsVisible=!!this.getIcon();
break;
default:this._iconIsVisible=false;
}
if(this._iconIsVisible){this._iconObject?this._iconObject.setDisplay(true):this._createIcon();
}else if(this._iconObject){this._iconObject.setDisplay(false);
}}},
destruct:function(){this._disposeObjects("_iconObject",
"_labelObject");
}});




/* ID: qx.ui.basic.Image */
qx.Class.define("qx.ui.basic.Image",
{extend:qx.ui.basic.Terminator,
construct:function($0,
$1,
$2){arguments.callee.base.call(this);
this._blank=qx.io.Alias.getInstance().resolve("static/image/blank.gif");
if($0!=null){this.setSource($0);
}if($1!=null){this.setWidth($1);
}else{this.initWidth();
}
if($2!=null){this.setHeight($2);
}else{this.initHeight();
}this.initSelectable();
},
events:{"error":"qx.event.type.Event"},
properties:{allowStretchX:{refine:true,
init:false},
allowStretchY:{refine:true,
init:false},
selectable:{refine:true,
init:false},
width:{refine:true,
init:"auto"},
height:{refine:true,
init:"auto"},
appearance:{refine:true,
init:"image"},
source:{check:"String",
apply:"_applySource",
event:"changeSource",
nullable:true,
themeable:true},
preloader:{check:"qx.io.image.Preloader",
apply:"_applyPreloader",
nullable:true},
loaded:{check:"Boolean",
init:false,
apply:"_applyLoaded"},
resizeToInner:{check:"Boolean",
init:false}},
members:{_onload:function(){this.setLoaded(true);
},
_onerror:function(){this.warn("Could not load: "+this.getSource());
this.setLoaded(false);
if(this.hasEventListeners("error")){this.dispatchEvent(new qx.event.type.Event("error"),
true);
}},
_beforeAppear:function(){var $0=this.getSource();
if($0){qx.io.image.Manager.getInstance().show($0);
this._registeredAsVisible=true;
}return arguments.callee.base.call(this);
},
_beforeDisappear:function(){var $0=this.getSource();
if($0&&this._registeredAsVisible){qx.io.image.Manager.getInstance().hide($0);
delete this._registeredAsVisible;
}return arguments.callee.base.call(this);
},
_applySource:function($0,
$1){var $2=qx.io.image.Manager.getInstance();
if($1){$2.remove($1);
if(this._registeredAsVisible){$2.hide($1);
delete this._registeredAsVisible;
}}
if($0){$2.add($0);
if(this.isSeeable()){this._registeredAsVisible=true;
$2.show($0);
}}
if(this.isCreated()){this._connect();
}},
_connect:function(){var $0=qx.io.Alias.getInstance();
$0.connect(this._syncSource,
this,
this.getSource());
},
_syncSource:function($0){if($0===null){this.setPreloader(null);
}else{var $1=qx.io.image.PreloaderManager.getInstance().create($0);
this.setPreloader($1);
}},
_applyPreloader:function($0,
$1){if($1){$1.removeEventListener("load",
this._onload,
this);
$1.removeEventListener("error",
this._onerror,
this);
}
if($0){this.setLoaded(false);
if($0.isErroneous()){this._onerror();
}else if($0.isLoaded()){this.setLoaded(true);
}else{$0.addEventListener("load",
this._onload,
this);
$0.addEventListener("error",
this._onerror,
this);
}}else{this.setLoaded(false);
}},
_applyLoaded:function($0,
$1){if($0&&this.isCreated()){this._renderContent();
}else if(!$0){this._invalidatePreferredInnerWidth();
this._invalidatePreferredInnerHeight();
}},
_applyElement:function($0,
$1){if($0){if(!this._image){try{if(qx.core.Variant.isSet("qx.client",
"webkit")){this._image=document.createElement("img");
}else{this._image=new Image;
}this._image.style.border="0 none";
this._image.style.verticalAlign="top";
this._image.alt="";
this._image.title="";
}catch(ex){this.error("Failed while creating image #1",
ex);
}
if(qx.core.Variant.isSet("qx.client",
"gecko|opera|webkit")){this._styleEnabled();
}}$0.appendChild(this._image);
}arguments.callee.base.call(this,
$0,
$1);
if($0&&this.getSource()){this._connect();
}},
_postApply:function(){this._postApplyDimensions();
this._updateContent();
},
_applyEnabled:function($0,
$1){if(this._image){this._styleEnabled();
}return arguments.callee.base.call(this,
$0,
$1);
},
_updateContent:qx.core.Variant.select("qx.client",
{"mshtml":function(){var $0=this._image;
var $1=this.getPreloader();
var $2=$1&&$1.isLoaded()?$1.getSource():this._blank;
if($1&&$1.getIsPng()&&this.getEnabled()){$0.src=this._blank;
$0.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+$2+"',sizingMethod='scale')";
}else{$0.src=$2;
$0.style.filter=this.getEnabled()?"":"Gray() Alpha(Opacity=30)";
}},
"default":function(){var $0=this.getPreloader();
var $1=$0&&$0.isLoaded()?$0.getSource():this._blank;
this._image.src=$1;
}}),
_resetContent:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._image.src=this._blank;
this._image.style.filter="";
},
"default":function(){this._image.src=this._blank;
}}),
_styleEnabled:qx.core.Variant.select("qx.client",
{"mshtml":function(){this._updateContent();
},
"default":function(){if(this._image){var $0=this.getEnabled()===false?0.3:"";
var $1=this._image.style;
$1.opacity=$1.KhtmlOpacity=$1.MozOpacity=$0;
}}}),
_computePreferredInnerWidth:function(){var $0=this.getPreloader();
return $0?$0.getWidth():0;
},
_computePreferredInnerHeight:function(){var $0=this.getPreloader();
return $0?$0.getHeight():0;
},
_renderContent:function(){arguments.callee.base.call(this);
qx.ui.core.Widget.flushGlobalQueues();
},
_postApplyDimensions:qx.core.Variant.select("qx.client",
{"mshtml":function(){try{var $0=this._image.style;
if(this.getResizeToInner()){$0.pixelWidth=this.getInnerWidth();
$0.pixelHeight=this.getInnerHeight();
}else{$0.pixelWidth=this.getPreferredInnerWidth();
$0.pixelHeight=this.getPreferredInnerHeight();
}}catch(ex){this.error("postApplyDimensions failed",
ex);
}},
"default":function(){try{var $0=this._image;
if(this.getResizeToInner()){$0.width=this.getInnerWidth();
$0.height=this.getInnerHeight();
}else{$0.width=this.getPreferredInnerWidth();
$0.height=this.getPreferredInnerHeight();
}}catch(ex){this.error("postApplyDimensions failed",
ex);
}}}),
_changeInnerWidth:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){if(this.getResizeToInner()){this._image.style.pixelWidth=$0;
}},
"default":function($0,
$1){if(this.getResizeToInner()){this._image.width=$0;
}}}),
_changeInnerHeight:qx.core.Variant.select("qx.client",
{"mshtml":function($0,
$1){if(this.getResizeToInner()){this._image.style.pixelHeight=$0;
}},
"default":function($0,
$1){if(this.getResizeToInner()){this._image.height=$0;
}}})},
destruct:function(){if(this._image){this._image.style.filter="";
}this._disposeFields("_image");
}});




/* ID: qx.ui.form.ListItem */
qx.Class.define("qx.ui.form.ListItem",
{extend:qx.ui.basic.Atom,
construct:function($0,
$1,
$2){arguments.callee.base.call(this,
$0,
$1);
if($2!=null){this.setValue($2);
}this.addEventListener("dblclick",
this._ondblclick);
this.initMinWidth();
},
events:{"action":"qx.event.type.Event"},
properties:{appearance:{refine:true,
init:"list-item"},
minWidth:{refine:true,
init:"auto"},
width:{refine:true,
init:null},
allowStretchX:{refine:true,
init:true},
value:{check:"String",
event:"changeValue"}},
members:{handleStateChange:function(){if(this.hasState("lead")){this.setStyleProperty("MozOutline",
"1px dotted invert");
this.setStyleProperty("outline",
"1px dotted invert");
}else{this.removeStyleProperty("MozOutline");
this.setStyleProperty("outline",
"0px none");
}},
_applyStateStyleFocus:function($0){},
matchesString:function($0){$0=String($0);
return $0!=""&&this.getLabel().toString().toLowerCase().indexOf($0.toLowerCase())==0;
},
matchesStringExact:function($0){$0=String($0);
return $0!=""&&this.getLabel().toString().toLowerCase()==String($0).toLowerCase();
},
matchesValue:function($0){$0=String($0);
return $0!=""&&this.getValue().toLowerCase().indexOf($0.toLowerCase())==0;
},
matchesValueExact:function($0){$0=String($0);
return $0!=""&&this.getValue().toLowerCase()==String($0).toLowerCase();
},
_ondblclick:function($0){var $1=this.getCommand();
if($1){$1.execute();
}}}});




/* ID: qx.ui.form.CheckBox */
qx.Class.define("qx.ui.form.CheckBox",
{extend:qx.ui.basic.Atom,
construct:function($0,
$1,
$2,
$3){arguments.callee.base.call(this,
$0);
this.initTabIndex();
this._createIcon();
if($1!=null){this.setValue($1);
}
if($2!=null){this.setName($2);
}
if($3!=null){this.setChecked($3);
}else{this.initChecked();
}this.addEventListener("click",
this._onclick);
this.addEventListener("keydown",
this._onkeydown);
this.addEventListener("keyup",
this._onkeyup);
},
properties:{appearance:{refine:true,
init:"check-box"},
tabIndex:{refine:true,
init:1},
name:{check:"String",
event:"changeName"},
value:{check:"String",
event:"changeValue"},
checked:{check:"Boolean",
apply:"_applyChecked",
init:false,
event:"changeChecked"}},
members:{INPUT_TYPE:"checkbox",
_createIcon:function(){var $0=this._iconObject=new qx.ui.form.InputCheckSymbol;
$0.setType(this.INPUT_TYPE);
$0.setChecked(this.getChecked());
$0.setAnonymous(true);
this.addAtBegin($0);
},
_applyChecked:function($0,
$1){if(this._iconObject){this._iconObject.setChecked($0);
}},
_applyIcon:null,
_applyDisabledIcon:null,
_handleIcon:function(){switch(this.getShow()){case "icon":case "both":this._iconIsVisible=true;
break;
default:this._iconIsVisible=false;
}
if(this._iconIsVisible){this._iconObject?this._iconObject.setDisplay(true):this._createIcon();
}else if(this._iconObject){this._iconObject.setDisplay(false);
}},
_onclick:function($0){this.toggleChecked();
},
_onkeydown:function($0){if($0.getKeyIdentifier()=="Enter"&&!$0.isAltPressed()){this.toggleChecked();
}},
_onkeyup:function($0){if($0.getKeyIdentifier()=="Space"){this.toggleChecked();
}}}});




/* ID: qx.ui.form.InputCheckSymbol */
qx.Class.define("qx.ui.form.InputCheckSymbol",
{extend:qx.ui.basic.Terminator,
construct:function(){arguments.callee.base.call(this);
this.setSelectable(false);
if(qx.core.Variant.isSet("qx.client",
"mshtml")){this.setWidth(13);
this.setHeight(13);
}else if(qx.core.Variant.isSet("qx.client",
"gecko")){this.setMargin(0);
}this.initTabIndex();
this.setChecked(false);
},
properties:{tabIndex:{refine:true,
init:-1},
name:{check:"String",
init:null,
nullable:true,
apply:"_applyName"},
value:{init:null,
nullable:true,
apply:"_applyValue"},
type:{init:null,
nullable:true,
apply:"_applyType"},
checked:{check:"Boolean",
init:false,
apply:"_applyChecked"}},
members:{_createElementImpl:function(){this.setElement(this.getTopLevelWidget().getDocumentElement().createElement("input"));
},
_applyName:function($0,
$1){return this.setHtmlProperty("name",
$0);
},
_applyValue:function($0,
$1){return this.setHtmlProperty("value",
$0);
},
_applyType:function($0,
$1){return this.setHtmlProperty("type",
$0);
},
_applyChecked:function($0,
$1){return this.setHtmlProperty("checked",
$0);
},
getPreferredBoxWidth:function(){return 13;
},
getPreferredBoxHeight:function(){return 13;
},
_afterAppear:qx.core.Variant.select("qx.client",
{"mshtml":function(){arguments.callee.base.call(this);
var $0=this.getElement();
$0.checked=this.getChecked();
if(this.getEnabled()===false){$0.disabled=true;
}},
"default":qx.lang.Function.returnTrue}),
_applyEnabled:function($0,
$1){$0===false?this.setHtmlProperty("disabled",
"disabled"):this.removeHtmlProperty("disabled");
return arguments.callee.base.call(this,
$0,
$1);
}},
defer:function($0,
$1){$1.getBoxWidth=$1.getPreferredBoxWidth;
$1.getBoxHeight=$1.getPreferredBoxHeight;
$1.getInnerWidth=$1.getPreferredBoxWidth;
$1.getInnerHeight=$1.getPreferredBoxHeight;
}});




/* ID: qx.ui.pageview.AbstractButton */
qx.Class.define("qx.ui.pageview.AbstractButton",
{type:"abstract",
extend:qx.ui.basic.Atom,
construct:function($0,
$1,
$2,
$3,
$4){arguments.callee.base.call(this,
$0,
$1,
$2,
$3,
$4);
this.initChecked();
this.initTabIndex();
this.addEventListener("mouseover",
this._onmouseover);
this.addEventListener("mouseout",
this._onmouseout);
this.addEventListener("mousedown",
this._onmousedown);
this.addEventListener("keydown",
this._onkeydown);
this.addEventListener("keypress",
this._onkeypress);
},
properties:{tabIndex:{refine:true,
init:1},
checked:{check:"Boolean",
init:false,
apply:"_applyChecked",
event:"changeChecked"},
page:{check:"qx.ui.pageview.AbstractPage",
apply:"_applyPage",
nullable:true},
manager:{check:"qx.ui.selection.RadioManager",
nullable:true,
apply:"_applyManager"},
name:{check:"String",
apply:"_applyName"}},
members:{getView:function(){var $0=this.getParent();
return $0?$0.getParent():null;
},
_applyManager:function($0,
$1){if($1){$1.remove(this);
}
if($0){$0.add(this);
}},
_applyParent:function($0,
$1){arguments.callee.base.call(this,
$0,
$1);
if($1){$1.getManager().remove(this);
}
if($0){$0.getManager().add(this);
}},
_applyPage:function($0,
$1){if($1){$1.setButton(null);
}
if($0){$0.setButton(this);
this.getChecked()?$0.show():$0.hide();
}},
_applyChecked:function($0,
$1){if(this._hasParent){var $2=this.getManager();
if($2){$2.handleItemChecked(this,
$0);
}}$0?this.addState("checked"):this.removeState("checked");
var $3=this.getPage();
if($3){this.getChecked()?$3.show():$3.hide();
}},
_applyName:function($0,
$1){if(this.getManager()){this.getManager().setName($0);
}},
_onmousedown:function($0){this.setChecked(true);
},
_onmouseover:function($0){this.addState("over");
},
_onmouseout:function($0){this.removeState("over");
},
_onkeydown:function($0){},
_onkeypress:function($0){}}});




/* ID: qx.ui.pageview.buttonview.Button */
qx.Class.define("qx.ui.pageview.buttonview.Button",
{extend:qx.ui.pageview.AbstractButton,
properties:{allowStretchX:{refine:true,
init:true},
allowStretchY:{refine:true,
init:true},
appearance:{refine:true,
init:"button-view-button"}},
members:{_onkeypress:function($0){switch(this.getView().getBarPosition()){case "top":case "bottom":switch($0.getKeyIdentifier()){case "Left":var $1=true;
break;
case "Right":var $1=false;
break;
default:return;
}break;
case "left":case "right":switch($0.getKeyIdentifier()){case "Up":var $1=true;
break;
case "Down":var $1=false;
break;
default:return;
}break;
default:return;
}var $2=($1?(this.isFirstChild()?this.getParent().getLastChild():this.getPreviousSibling()):(this.isLastChild()?this.getParent().getFirstChild():this.getNextSibling()));
$2.setFocused(true);
$2.setChecked(true);
},
_renderAppearance:function(){if(this.getParent()){var $0=this.getView().getBarPosition();
$0==="left"?this.addState("barLeft"):this.removeState("barLeft");
$0==="right"?this.addState("barRight"):this.removeState("barRight");
$0==="top"?this.addState("barTop"):this.removeState("barTop");
$0==="bottom"?this.addState("barBottom"):this.removeState("barBottom");
}arguments.callee.base.call(this);
}}});




/* ID: qx.ui.form.Button */
qx.Class.define("qx.ui.form.Button",
{extend:qx.ui.basic.Atom,
construct:function($0,
$1,
$2,
$3,
$4){arguments.callee.base.call(this,
$0,
$1,
$2,
$3,
$4);
this.initTabIndex();
this.addEventListener("mouseover",
this._onmouseover);
this.addEventListener("mouseout",
this._onmouseout);
this.addEventListener("mousedown",
this._onmousedown);
this.addEventListener("mouseup",
this._onmouseup);
this.addEventListener("keydown",
this._onkeydown);
this.addEventListener("keyup",
this._onkeyup);
},
properties:{appearance:{refine:true,
init:"button"},
tabIndex:{refine:true,
init:1}},
members:{_onmouseover:function($0){if($0.getTarget()!=this){return;
}
if(this.hasState("abandoned")){this.removeState("abandoned");
this.addState("pressed");
}this.addState("over");
},
_onmouseout:function($0){if($0.getTarget()!=this){return;
}this.removeState("over");
if(this.hasState("pressed")){this.setCapture(true);
this.removeState("pressed");
this.addState("abandoned");
}},
_onmousedown:function($0){if($0.getTarget()!=this||!$0.isLeftButtonPressed()){return;
}this.removeState("abandoned");
this.addState("pressed");
},
_onmouseup:function($0){this.setCapture(false);
var $1=this.hasState("pressed");
var $2=this.hasState("abandoned");
if($1){this.removeState("pressed");
}
if($2){this.removeState("abandoned");
}
if(!$2){this.addState("over");
if($1){this.execute();
}}},
_onkeydown:function($0){switch($0.getKeyIdentifier()){case "Enter":case "Space":this.removeState("abandoned");
this.addState("pressed");
$0.stopPropagation();
}},
_onkeyup:function($0){switch($0.getKeyIdentifier()){case "Enter":case "Space":if(this.hasState("pressed")){this.removeState("abandoned");
this.removeState("pressed");
this.execute();
$0.stopPropagation();
}}}}});




/* ID: qx.ui.popup.PopupAtom */
qx.Class.define("qx.ui.popup.PopupAtom",
{extend:qx.ui.popup.Popup,
construct:function($0,
$1){arguments.callee.base.call(this);
this._atom=new qx.ui.basic.Atom($0,
$1);
this._atom.setParent(this);
},
members:{_isFocusRoot:false,
getAtom:function(){return this._atom;
}},
destruct:function(){this._disposeObjects("_atom");
}});




/* ID: qx.ui.popup.ToolTip */
qx.Class.define("qx.ui.popup.ToolTip",
{extend:qx.ui.popup.PopupAtom,
construct:function($0,
$1){arguments.callee.base.call(this,
$0,
$1);
this.setStyleProperty("filter",
"progid:DXImageTransform.Microsoft.Shadow(color='Gray', Direction=135, Strength=4)");
this._showTimer=new qx.client.Timer(this.getShowInterval());
this._showTimer.addEventListener("interval",
this._onshowtimer,
this);
this._hideTimer=new qx.client.Timer(this.getHideInterval());
this._hideTimer.addEventListener("interval",
this._onhidetimer,
this);
this.addEventListener("mouseover",
this._onmouseover);
this.addEventListener("mouseout",
this._onmouseover);
},
properties:{appearance:{refine:true,
init:"tool-tip"},
hideOnHover:{check:"Boolean",
init:true},
mousePointerOffsetX:{check:"Integer",
init:1},
mousePointerOffsetY:{check:"Integer",
init:20},
showInterval:{check:"Integer",
init:1000,
apply:"_applyShowInterval"},
hideInterval:{check:"Integer",
init:4000,
apply:"_applyHideInterval"},
boundToWidget:{check:"qx.ui.core.Widget",
apply:"_applyBoundToWidget"}},
members:{_minZIndex:1e7,
_applyHideInterval:function($0,
$1){this._hideTimer.setInterval($0);
},
_applyShowInterval:function($0,
$1){this._showTimer.setInterval($0);
},
_applyBoundToWidget:function($0,
$1){if($0){this.setParent($0.getTopLevelWidget());
}else if($1){this.setParent(null);
}},
_beforeAppear:function(){arguments.callee.base.call(this);
this._stopShowTimer();
this._startHideTimer();
},
_beforeDisappear:function(){arguments.callee.base.call(this);
this._stopHideTimer();
},
_afterAppear:function(){arguments.callee.base.call(this);
if(this.getRestrictToPageOnOpen()){var $0=qx.ui.core.ClientDocument.getInstance();
var $1=$0.getClientWidth();
var $2=$0.getClientHeight();
var $3=parseInt(this.getRestrictToPageLeft());
var $4=parseInt(this.getRestrictToPageRight());
var $5=parseInt(this.getRestrictToPageTop());
var $6=parseInt(this.getRestrictToPageBottom());
var $7=(this._wantedLeft==null)?this.getLeft():this._wantedLeft;
var $8=this.getTop();
var $9=this.getBoxWidth();
var $a=this.getBoxHeight();
var $b=qx.event.type.MouseEvent.getPageX();
var $c=qx.event.type.MouseEvent.getPageY();
var $d=this.getLeft();
var $e=$8;
if($7+$9>$1-$4){$7=$1-$4-$9;
}
if($8+$a>$2-$6){$8=$2-$6-$a;
}
if($7<$3){$7=$3;
}
if($8<$5){$8=$5;
}if($7<=$b&&$b<=$7+$9&&$8<=$c&&$c<=$8+$a){var $f=$c-$8;
var $g=$f-$a;
var $h=$b-$7;
var $i=$h-$9;
var $j=Math.max(0,
$5-($8+$g));
var $k=Math.max(0,
$8+$a+$f-($2-$6));
var $l=Math.max(0,
$3-($7+$i));
var $m=Math.max(0,
$7+$9+$h-($1-$4));
var $n=[[0,
$g,
$j],
[0,
$f,
$k],
[$i,
0,
$l],
[$h,
0,
$m]];
$n.sort(function($o,
$p){return $o[2]-$p[2]||(Math.abs($o[0])+Math.abs($o[1]))-(Math.abs($p[0])+Math.abs($p[1]));
});
var $q=$n[0];
$7=$7+$q[0];
$8=$8+$q[1];
}
if($7!=$d||$8!=$e){var $r=this;
window.setTimeout(function(){$r.setLeft($7);
$r.setTop($8);
},
0);
}}},
_startShowTimer:function(){if(!this._showTimer.getEnabled()){this._showTimer.start();
}},
_startHideTimer:function(){if(!this._hideTimer.getEnabled()){this._hideTimer.start();
}},
_stopShowTimer:function(){if(this._showTimer.getEnabled()){this._showTimer.stop();
}},
_stopHideTimer:function(){if(this._hideTimer.getEnabled()){this._hideTimer.stop();
}},
_onmouseover:function($0){if(this.getHideOnHover()){this.hide();
}},
_onshowtimer:function($0){this.setLeft(qx.event.type.MouseEvent.getPageX()+this.getMousePointerOffsetX());
this.setTop(qx.event.type.MouseEvent.getPageY()+this.getMousePointerOffsetY());
this.show();
},
_onhidetimer:function($0){return this.hide();
}},
destruct:function(){var $0=qx.ui.popup.ToolTipManager.getInstance();
$0.remove(this);
if($0.getCurrentToolTip()==this){$0.resetCurrentToolTip();
}this._disposeObjects("_showTimer",
"_hideTimer");
}});




/* ID: qx.ui.popup.ToolTipManager */
qx.Class.define("qx.ui.popup.ToolTipManager",
{type:"singleton",
extend:qx.util.manager.Object,
properties:{currentToolTip:{check:"qx.ui.popup.ToolTip",
nullable:true,
apply:"_applyCurrentToolTip"}},
members:{_applyCurrentToolTip:function($0,
$1){if($1&&$1.contains($0)){return;
}if($1&&!$1.isDisposed()){$1.hide();
$1._stopShowTimer();
$1._stopHideTimer();
}if($0){$0._startShowTimer();
}},
handleMouseOver:function($0){var $1=$0.getTarget();
var $2;
if(!($1 instanceof qx.ui.core.Widget)&&$1.nodeType==1){$1=qx.event.handler.EventHandler.getTargetObject($1);
}while($1!=null&&!($2=$1.getToolTip())){$1=$1.getParent();
}if($2!=null){$2.setBoundToWidget($1);
}this.setCurrentToolTip($2);
},
handleMouseOut:function($0){var $1=$0.getTarget();
var $2=$0.getRelatedTarget();
var $3=this.getCurrentToolTip();
if($3&&($2==$3||$3.contains($2))){return;
}if($2&&$1&&$1.contains($2)){return;
}if($3&&!$2){this.setCurrentToolTip(null);
}},
handleFocus:function($0){var $1=$0.getTarget();
var $2=$1.getToolTip();
if($2!=null){$2.setBoundToWidget($1);
this.setCurrentToolTip($2);
}},
handleBlur:function($0){var $1=$0.getTarget();
if(!$1){return;
}var $2=this.getCurrentToolTip();
if($2&&$2==$1.getToolTip()){this.setCurrentToolTip(null);
}}}});




/* ID: qx.ui.pageview.AbstractPage */
qx.Class.define("qx.ui.pageview.AbstractPage",
{type:"abstract",
extend:qx.ui.layout.CanvasLayout,
construct:function($0){arguments.callee.base.call(this);
if($0!==undefined){this.setButton($0);
}this.initTop();
this.initRight();
this.initBottom();
this.initLeft();
},
properties:{top:{refine:true,
init:0},
right:{refine:true,
init:0},
bottom:{refine:true,
init:0},
left:{refine:true,
init:0},
display:{refine:true,
init:false},
button:{check:"qx.ui.pageview.AbstractButton",
apply:"_applyButton"}},
members:{_applyButton:function($0,
$1){if($1){$1.setPage(null);
}
if($0){$0.setPage(this);
}}}});




/* ID: qx.ui.pageview.buttonview.Page */
qx.Class.define("qx.ui.pageview.buttonview.Page",
{extend:qx.ui.pageview.AbstractPage,
properties:{appearance:{refine:true,
init:"button-view-page"}}});




/* ID: qx.locale.Locale */
qx.Class.define("qx.locale.Locale",
{statics:{define:function($0,
$1){qx.Class.createNamespace($0,
$1);
qx.locale.Manager.getInstance().addTranslationFromClass($0,
$1);
}}});




/* ID: qx.locale.data.C */
qx.locale.Locale.define("qx.locale.data.C",
{cldr_alternateQuotationEnd:"”",
cldr_alternateQuotationStart:"“",
cldr_am:"am",
cldr_date_format_full:"EEEE, MMMM d, yyyy",
cldr_date_format_long:"MMMM d, yyyy",
cldr_date_format_medium:"MMM d, yyyy",
cldr_date_format_short:"M/d/yy",
cldr_date_time_format_HHmm:"HH:mm",
cldr_date_time_format_HHmmss:"HH:mm:ss",
cldr_date_time_format_MMMMd:"MMMM d",
cldr_date_time_format_Md:"M/d",
cldr_date_time_format_mmss:"mm:ss",
cldr_date_time_format_yyMM:"MM/yy",
cldr_date_time_format_yyQQQQ:"QQQQ yy",
cldr_date_time_format_yyyyMMM:"MMM yyyy",
cldr_day_abbreviated_fri:"Fri",
cldr_day_abbreviated_mon:"Mon",
cldr_day_abbreviated_sat:"Sat",
cldr_day_abbreviated_sun:"Sun",
cldr_day_abbreviated_thu:"Thu",
cldr_day_abbreviated_tue:"Tue",
cldr_day_abbreviated_wed:"Wed",
cldr_day_narrow_fri:"F",
cldr_day_narrow_mon:"M",
cldr_day_narrow_sat:"S",
cldr_day_narrow_sun:"S",
cldr_day_narrow_thu:"T",
cldr_day_narrow_tue:"T",
cldr_day_narrow_wed:"W",
cldr_day_wide_fri:"Friday",
cldr_day_wide_mon:"Monday",
cldr_day_wide_sat:"Saturday",
cldr_day_wide_sun:"Sunday",
cldr_day_wide_thu:"Thursday",
cldr_day_wide_tue:"Tuesday",
cldr_day_wide_wed:"Wednesday",
cldr_month_abbreviated_1:"Jan",
cldr_month_abbreviated_10:"Oct",
cldr_month_abbreviated_11:"Nov",
cldr_month_abbreviated_12:"Dec",
cldr_month_abbreviated_2:"Feb",
cldr_month_abbreviated_3:"Mar",
cldr_month_abbreviated_4:"Apr",
cldr_month_abbreviated_5:"May",
cldr_month_abbreviated_6:"Jun",
cldr_month_abbreviated_7:"Jul",
cldr_month_abbreviated_8:"Aug",
cldr_month_abbreviated_9:"Sep",
cldr_month_narrow_1:"J",
cldr_month_narrow_10:"O",
cldr_month_narrow_11:"N",
cldr_month_narrow_12:"D",
cldr_month_narrow_2:"F",
cldr_month_narrow_3:"M",
cldr_month_narrow_4:"A",
cldr_month_narrow_5:"M",
cldr_month_narrow_6:"J",
cldr_month_narrow_7:"J",
cldr_month_narrow_8:"A",
cldr_month_narrow_9:"S",
cldr_month_wide_1:"January",
cldr_month_wide_10:"October",
cldr_month_wide_11:"November",
cldr_month_wide_12:"December",
cldr_month_wide_2:"February",
cldr_month_wide_3:"March",
cldr_month_wide_4:"April",
cldr_month_wide_5:"May",
cldr_month_wide_6:"June",
cldr_month_wide_7:"July",
cldr_month_wide_8:"August",
cldr_month_wide_9:"September",
cldr_number_decimal_separator:".",
cldr_number_group_separator:",",
cldr_pm:"pm",
cldr_quotationEnd:"’",
cldr_quotationStart:"‘",
cldr_time_format_full:"h:mm:ss a v",
cldr_time_format_long:"h:mm:ss a z",
cldr_time_format_medium:"h:mm:ss a",
cldr_time_format_short:"h:mm a"});

