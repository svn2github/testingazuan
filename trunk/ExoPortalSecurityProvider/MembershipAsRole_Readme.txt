

I ruoli di spagobi sono le membership di exo.
I gruppi di exo diventano funzionalità del profilo utente (secondo la conf di sicurezza)

--------------------------------------------------------------------------------
Esempio:

Utente Exo:
utente biadmin --> group: /Filiali/Zona1/Filiale2 con membership sbiuser 
Profilo SpagoBI
nome utente: biadmin 
ruoli: sbiuser
funzionalità: data_access:/Filiali/Zona1/Filiale2  (previa configurazione di sicurezza)
--------------------------------------------------------------------------------

Tra le lib di spagobi vi deve essere la libreria di creazione UserProfile di Metodo

Dentro il file di configurazione spagobi.xml la parte di sicurezza deve essere configurata nel
modo seguente:

 <!--  SPAGOBI SECURITY PROVIDER CONFIGURATION  Default Security Provider is ExoPortalSecurity Provider-->
	<SECURITY>
		<PORTAL-SECURITY-CLASS>	it.eng.spagobi.security.MetodoSecurityProviderImpl</PORTAL-SECURITY-CLASS>
		<USER-PROFILE-FACTORY-CLASS>it.eng.spagobi.security.MetodoUserProfileFactoryImpl</USER-PROFILE-FACTORY-CLASS>
		<FUNCTIONALITIES-LOADER>
				<GROUP-TRANSFORMERS>
						<TRANSFORMER startwith="/Filiali" prefix="data_access:" dimension="Filiali" />
				</GROUP-TRANSFORMERS>	
		</FUNCTIONALITIES-LOADER>
		<ROLE-NAME-PATTERN-FILTER>sbi.*</ROLE-NAME-PATTERN-FILTER>
		<NAME_PORTAL_APPLICATION>ecm</NAME_PORTAL_APPLICATION>
	</SECURITY>
	
l'elemento 	ROLE-NAME-PATTERN-FILTER indica una regular expression che i nomi delle 
exo membership (ruoli in spagobi) devono rispettare per essere importate nel sistema
e nel profilo utente.

l'elemento GROUP-TRANSFORMERS può contenere più elementi TRANSFORMER in quali indicano:
i gruppi di exo associati all'utente che iniziano per 'startwith' diventano delle funzionalità
del profilo utente. Viene aggiunto un prefisso 'prefix' e il primo token del path viene 
sostituito con 'dimension'. (La sostituzione avviene perchè non è detto che il primo elemento del
path corrisponda al nome della dimensione del dwh)

-----------------------------------------------------
Esempio:
gruppo exo
/Zone Territoriali/Zona1/filiale2
conf transformer:
<TRANSFORMER startwith="/Zone Territoriali" prefix="data_access:" dimension="Filiali" />
Funzionalità profilo:
data_access:/Filiali/Zona1/filiale2
------------------------------------------------------

Quando si definisce un documento olap il suo template deve avere questa forma

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<olap>
  <cube reference='/WEB-INF/queries/LapamMart.xml' />
	<MDXquery>
	 select 
		{[Measures].[Importo], [Measures].[Pagato]} on columns, 
    {[Filiali].[Zona].Members} ON ROWS
    from [ServizioCoge]
	</MDXquery>
	<DATA-ACCESS>
	   <GRANTED-DIMENSIONS>
        <DIMENSION name="Filiali" grantSource="ProfileFunctionalities" />
     </GRANTED-DIMENSIONS>
	</DATA-ACCESS>
</olap>

l'elemento DATA-ACCESS può essere omesso ma in tal caso nessun filtro viene applicato
l'elemento GRANTED-DIMENSIONS può contenere più elementi DIMENSION. Ognuno di questi elementi
indica al driver che deve aggiungere parametri per il data access control sulla dimensione settata.
 
Quando il driver viene invocato:
- ricava la lista di dimension dal template
- per ogni dimension tag ne ricava il nome (Filiali)
- ricava tutte le funzioanlità dal profilo utente
- mantiene solo le funzionalità che iniziano con data_access:/name dimension 
  Esempio data_access:/Filiali
- Le funzionalità rimaste vengono inviate all'engine (dopo ooportuna trasformazione) 

L'engine possiede la logica per filtrare le dimensione del dwh in base ai parametri passati 
dal driver 


	
