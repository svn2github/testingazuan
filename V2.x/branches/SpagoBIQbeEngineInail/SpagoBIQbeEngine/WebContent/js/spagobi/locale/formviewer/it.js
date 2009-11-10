Ext.ns("Sbi.locale");
Sbi.locale.ln = Sbi.locale.ln || new Array();

Sbi.locale.formats = {
		/*
		number: {
			decimalSeparator: ',',
			decimalPrecision: 2,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '€',
			nullValue: ''
		},
		*/
		float: {
			decimalSeparator: ',',
			decimalPrecision: 2,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '€',
			nullValue: ''
		},
		int: {
			decimalSeparator: ',',
			decimalPrecision: 0,
			groupingSeparator: '.',
			groupingSize: 3,
			//currencySymbol: '€',
			nullValue: ''
		},
		string: {
			trim: true,
    		maxLength: null,
    		ellipsis: true,
    		changeCase: null, // null | 'capitalize' | 'uppercase' | 'lowercase'
    		//prefix: '',
    		//suffix: '',
    		nullValue: ''
		},
		
		date: {
			dateFormat: 'd/m/Y',
    		nullValue: ''
		},
		
		boolean: {
			trueSymbol: 'vero',
    		falseSymbol: 'false',
    		nullValue: ''
		}
};

//===================================================================
//VIEWER PANEL
//===================================================================
Sbi.locale.ln['sbi.formviewer.formviewerpage.title'] = 'DATA BROWER';


//===================================================================
//FILTERS PANEL
//===================================================================
Sbi.locale.ln['sbi.formviewer.staticclosedfilterspanel.title'] = 'Filtri statici chiusi';
Sbi.locale.ln['sbi.formviewer.staticopenfilterspanel.title'] = 'Filtri statici aperti';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.title'] = 'Filtri dinamici';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.variable'] = 'Variabile';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.value'] = 'Valore';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.fromvalue'] = 'Dal valore';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.tovalue'] = 'Al valore';
Sbi.locale.ln['sbi.formviewer.groupingvariablespanel.title'] = 'Variabili di raggruppamento';
Sbi.locale.ln['sbi.formviewer.groupingvariablespanel.variable-1'] = 'Prima variabile';
Sbi.locale.ln['sbi.formviewer.groupingvariablespanel.variable-2'] = 'Seconda variabile';
