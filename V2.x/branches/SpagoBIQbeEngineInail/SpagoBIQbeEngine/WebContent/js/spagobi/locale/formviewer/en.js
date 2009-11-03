Ext.ns("Sbi.locale");
Sbi.locale.ln = Sbi.locale.ln || new Array();

Sbi.locale.formats = {
		/*
		number: {
			decimalSeparator: '.',
			decimalPrecision: 2,
			groupingSeparator: ',',
			groupingSize: 3,
			//currencySymbol: '$',
			nullValue: ''
		},
		*/
		
		float: {
			decimalSeparator: '.',
			decimalPrecision: 2,
			groupingSeparator: ',',
			groupingSize: 3,
			//currencySymbol: '$',
			nullValue: ''
		},
		int: {
			decimalSeparator: '.',
			decimalPrecision: 0,
			groupingSeparator: ',',
			groupingSize: 3,
			//currencySymbol: '$',
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
			dateFormat: 'm/Y/d',
    		nullValue: ''
		},
		
		boolean: {
			trueSymbol: 'true',
    		falseSymbol: 'false',
    		nullValue: ''
		}
};

//===================================================================
//FILTERS PANEL
//===================================================================
Sbi.locale.ln['sbi.formviewer.staticclosedfilterspanel.title'] = 'Static closed filters';
Sbi.locale.ln['sbi.formviewer.staticopenfilterspanel.title'] = 'Static open filters';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.title'] = 'Dynamic filters';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.variable'] = 'Variable';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.value'] = 'Value';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.fromvalue'] = 'From value';
Sbi.locale.ln['sbi.formviewer.dynamicfilterspanel.tovalue'] = 'To value';
