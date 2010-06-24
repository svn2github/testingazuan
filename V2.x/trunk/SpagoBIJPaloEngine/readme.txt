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
Se specificati tutti gli attributi (come nel caso di template generato con SpagoBI), allora
viene cercata la vista corrispondente al nome della vista "view" specificato nel template.
Se si invoca il motore per editare un template, viene aperto JPalo con navigator sulla vista 
specificata nel template con possibilità di creare una nuova vista ma senza Connections e Accounts e Roles.
Se si apre JPalo per eseguire un documento JPalo di SpagoBI, ed il documento è appena stato creato,
il motore apre la vista salvata in fase di definizione del documento. I salvataggi successivi della vista,
avvengono sotto forma di subobject legati al documento con nome della vista.
Quindi eseguendo un doc JPalo che ha già un subobject, viene eseguito il subobject.
 

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

paloviewapi: DbService (parametrizzato sia login/pwd dello user che connection a mondrian--> deploy_it.properties)

SpagoBIProject:
modificato JPaloDriver

NB: Le modifiche a DbService.java riguardano:
- user/password del JPalo User che sono parametrizzate (usate per autenticazione su JPalo, 
	e prese da deploy_it.properties). Vengono inserite nel db di jpalo e a questo user è associato
	l'account admin/admin, ruolo e gruppo admin.
- la connessione al server mondrian è parametrizzata sempre in deploy_it.properties 
- viene modificata l'associazione connection_to_mondrian - user_paramtrizzato - account_admin, 
	di modo che venga inserita di default in ciascuno dei db di jpalo e che sia già presente nella
	combo all'atto della creazione della view di jpalo