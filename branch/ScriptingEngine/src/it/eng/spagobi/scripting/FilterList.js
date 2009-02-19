var x;
var names = list.toArray();
var validNames = new Array();

for(x in names) {
	if(names[x].startsWith('A')) {
		validNames[names[x]] = names[x];
	}
}

// be sure that andrea is always in list :-)
validNames['Andrea'] = 'Andrea';

list.clear();
for(name in validNames) {
	list.add(name);
}

