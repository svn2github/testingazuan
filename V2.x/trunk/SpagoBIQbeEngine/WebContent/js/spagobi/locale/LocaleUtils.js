Ext.ns("Sbi.locale");
alert('Sbi.locale');

Sbi.locale.dummyFormatter = function(v){return v;};
Sbi.locale.formatters = {
	number: Sbi.locale.dummyFormatter,		
	string: Sbi.locale.dummyFormatter,		
	date: Sbi.locale.dummyFormatter,		
	boolean: Sbi.locale.dummyFormatter
};

alert('->' + Sbi.qbe.commons.Format);

if(Sbi.qbe.commons.Format){
	Sbi.locale.formatters.number  = Sbi.qbe.commons.Format.numberRenderer(Sbi.locale.formats['number']),		
	Sbi.locale.formatters.string  = Sbi.qbe.commons.Format.stringRenderer(Sbi.locale.formats['string']),		
	Sbi.locale.formatters.date    = Sbi.qbe.commons.Format.dateRenderer(Sbi.locale.formats['date']),		
	Sbi.locale.formatters.boolean = Sbi.qbe.commons.Format.booleanRenderer(Sbi.locale.formats['boolean'])
};


Sbi.locale.localize = function(key) {
	if(!Sbi.locale.ln) return key;
	return Sbi.locale.ln[key] || key;
};

// alias
LN = Sbi.locale.localize;
FORMATTERS = Sbi.locale.formatters;




