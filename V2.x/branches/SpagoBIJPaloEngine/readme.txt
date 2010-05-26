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


Classi modificate in SpagoBIJPaloEngine:
JPaloEngineStartServlet (new)
JPaloEngineTemplate (new)
WPaloServiceImpl x apertura viste
CubeViewService  x salvataggio
BasePaloServiceServlet x sessione nuova jpalo
DisplayFlags (aggiunti hide)
AccountNavigatorView
AdminNavigatorView

Modifiche palo-gwt-core:
BasePaloServiceServlet
CubeViewController
CubeViewService
JPaloSavingUtil (new)

palo-xmla:
XMLAClient (errore url lowercase)