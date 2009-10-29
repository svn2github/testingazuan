

var template = {

    // --------------------------------  pointer alla query base --------------------------------------------
    // puntatore ala query base
    // ------------------------------------------------------------------------------------------------------
    query:{
        qbeDocLabel: 'qbeOverFoodmart',
        queryName: 'sales'
    },
    
    // --------------------------------  selected fields ---------------------------------------------------
    // Specifica quali sono i campi, tra quelli presenti nella select della query di base, che possono essere inclusi 
    // liberamente dal'utente finale nel risultato (selectable:true) ed eventualmente su quali di questi 
    // è possibile definire un ordinamento (sortable:true)
    // -----------------------------------------------------------------------------------------------------
    fields: [
        { 
            field: 'it.eng.spagobi.SalesFact1998:productSales',
            sortable: false,
            selectable: true
        },
        
        {
            field: 'it.eng.spagobi.SalesFact1998::time(time_id):year',
            sortable: false,
            selectable: true
        },
        
        {
            field: 'it.eng.spagobi.SalesFact1998::store(store_id):name',
            sortable: false,
            selectable: true
        }
        
        // ecc ...
       
    ],
    // --------------------------------  fine selected fields ---------------------------------------------------
    
    
    // -------------------------------- inizio filtri statici chiusi ----------------------------------------------------
    // Raggruppamenti logici di filtri preconfezionati. E' possibile specificare quale deve essere la politica di
    // selezione dei filtri all'interno di un medesimo gruppo (singleSelection: true|false)
    // -----------------------------------------------------------------------------------------------------------
    staticClosedFilters: [
        // gruppo-1, filtri esclusivi (alternativi) 
        {
        	id: 'Stato',
            title: 'Stato',
            singleSelection: true,
            allowNoSelection: true,
            noSelectionText: 'Tutti gli stati',
            width: '50%',
            options: [
                {
                    text: 'Solo vendite negli USA',
                    leftOperandValue: 'it.eng.spagobi.SalesFact1998::customer(customer_id):country',
                    operator: 'EQUALS TO',
                    rightOperandValue: 'USA'
                },
                
                {
                    text: 'Solo vendite in Canada',
                    leftOperandValue: 'it.eng.spagobi.SalesFact1998::customer(customer_id):country',
                    operator: 'EQUALS TO',
                    rightOperandValue: 'Canada'
                }
            ]
        },
        
        // gruppo-2, filtro on/off
        {
        	id: 'Tipologia prodotto',
            title: 'Tipologia prodotto',
            singleSelection: false,
            width: '50%',
            options: [
                {
                    text: 'Solo cibi',
                    leftOperandValue: 'it.eng.spagobi.SalesFact1998::product(product_id):family',
                    operator: 'EQUALS TO',
                    rightOperandValue: 'Food'
                },
                
                {
                    text: 'solo bevande',
                    leftOperandValue: 'it.eng.spagobi.SalesFact1998::product(product_id):family',
                    operator: 'EQUALS TO',
                    rightOperandValue: 'Drink'
                }
            ]
        } 
    ], 
    // -------------------------------- fine filtri statici chiusi ----------------------------------------------
    
    
    
    // -------------------------------- inizio filtri statici aperti ----------------------------------------------
    // filtri in cui l'utente può liberamente scegliere uno o più valori (singleSelection: true|false) per 
    // l'operatore di destra da una lista generata per mezzo di una distinct sulla colonna definita come 
    // operatore di sinistra del filtro (field)
    // ----------------------------------------------------------------------------------------------------
    staticOpenFilters: [
        // single selection
        {
        	id: 'Mese',
            text: 'Mese',
            field: 'it.eng.spagobi.SalesFact1998::timeByDay(time_id):theMonth',
            operator: 'EQUALS',
            singleSelection: false,
            maxSelectedNumber: 3
        },
        {
        	id: 'Customer',
            text: 'Customer',
            field: 'it.eng.spagobi.Customer:fullname',
            operator: 'EQUALS',
            singleSelection: false,
            maxSelectedNumber: 3
        },
        // multi selection
        {
        	id: 'Brand',
            text: 'Brand',
            field: 'it.eng.spagobi.SalesFact1998::product(product_id):brandName',
            operator: 'EQUALS',
            singleSelection: true
        },
        {
        	id: 'Reparto',
            text: 'Reparto',
            field: 'it.eng.spagobi.SalesFact1998::store(store_id):department',
            operator: 'EQUALS',
            singleSelection: false,
            maxSelectedNumber: 3
        }
    ],
    // -------------------------------- fine filtri statici aperti ----------------------------------------------
    
    // -------------------------------- dinamici aperti -------------------------------------------------
    // filtri in cui l'utente può liberamente definire sia l'operando di destra che quello di sinistra.
    // Questo blocco di configurazione permette di specificarne il numero, la tipologia (operatore) ed
    // eventualemnte di imporre un limite sui campi selezionabili come operando di sinistra.
    // --------------------------------------------------------------------------------------------------
    dynamicFilters: [
        {
            operator: 'EQUALS',
            admissibleFields: [
               {field: 'it.eng.spagobi.SalesFact1998::time(time_id):quarter_num', text: 'Quadrimestre'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):month_num', text: 'Mese'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):week_num', text: 'Settimana'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):day_num', text: 'Giorno'}                
            ]
        },
        {
            operator: 'EQUALS',
            admissibleFields: [
               {field: 'it.eng.spagobi.SalesFact1998::time(time_id):quarter_num', text: 'Quadrimestre'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):month_num', text: 'Mese'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):week_num', text: 'Settimana'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):day_num', text: 'Giorno'}                
            ]
        },
        {
            operator: 'BETWEEN',
            admissibleFields: [
               {field: 'it.eng.spagobi.SalesFact1998::time(time_id):quarter_num', text: 'Quadrimestre'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):month_num', text: 'Mese'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):week_num', text: 'Settimana'},
        	   {field: 'it.eng.spagobi.SalesFact1998::time(time_id):day_num', text: 'Giorno'}                
            ]
        }
    ]
    // -------------------------------- fine dinamici aperti -------------------------------------------
};
