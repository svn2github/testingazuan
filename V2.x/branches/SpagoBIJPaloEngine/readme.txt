Il template del documento JPalo è del tipo:
	 <olap connection="Mondrian" 
      	 account="admin" 
           view="Sales"
    	   cube="Sales">
	 </olap>
	 
Se nel template si specifica il nome della vista mediante attributo "view", il motore
cercherà di aprire una vista esistente su JPalo.
Se viene specificato 
"cube" = <nome del cubo> , 
"connection" = <nome della connessione al server mondrian>	 
"account" = <login name dell'account da utilizzare per loggarsi al server mondrian>
allora la vista viene creata dinamicamente se non esiste, o aperta quella esistente 
per quel cubo.