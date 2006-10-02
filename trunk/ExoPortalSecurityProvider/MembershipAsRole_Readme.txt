
This packages contains some classes useful for the security integration with the eXo portal.
The classes which name contains 'MembershipAsRole' are also useful to configure a datawarehouse 
data filtering, based on the user grants and the SpagoBI jpivot engine . 

These classes allow you to import the eXo user groups as SpagoBI user functionalities. 
Each functionality imported represent a granted dimension element.
Each olap document associated to the jpivot engine can be configured (into its template) to analize the
user functionalities and sent them to the jpivot engine.
The jpivot engine, based on the user functionality received, filters the datawarehouse data, 
presenting to the user only the information allowed.

Below is reported a list of the main classes concepts: 

The SpagoBI roles are the eXo memberships
The eXo groups become functionalities of the user profile (based on the security configuration)

--------------------------------------------------------------------------------
Example:

User Exo:
user biadmin --> group: /Filiali/Zona1/Filiale2 with membership sbiuser 
SpagoBI profile:
user name: biadmin 
roles: sbiuser
functionalities: data_access:/Filiali/Zona1/Filiale2  (based on the security configuration)
--------------------------------------------------------------------------------

Inside the spagobi.xml configuration file the security part must be configured as below:

 <!--  SPAGOBI SECURITY PROVIDER CONFIGURATION  Default Security Provider is ExoPortalSecurity Provider-->
	<SECURITY>
		<PORTAL-SECURITY-CLASS>	it.eng.spagobi.security.ExoMembershipAsRoleSecurityProviderImpl</PORTAL-SECURITY-CLASS>
		<USER-PROFILE-FACTORY-CLASS>it.eng.spagobi.security.ExoMembershipAsRoleUserProfileFactoryImpl</USER-PROFILE-FACTORY-CLASS>
		<FUNCTIONALITIES-LOADER>
				<GROUP-TRANSFORMERS>
						<TRANSFORMER startwith="/Filiali" prefix="data_access:" dimension="Filiali" />
				</GROUP-TRANSFORMERS>	
		</FUNCTIONALITIES-LOADER>
		<ROLE-NAME-PATTERN-FILTER>sbi.*</ROLE-NAME-PATTERN-FILTER>
		<NAME_PORTAL_APPLICATION>ecm</NAME_PORTAL_APPLICATION>
	</SECURITY>

The element ROLE-NAME-PATTERN-FILTER contains a regular expression useful to filter the eXo memberships.
The memberships that don't match the regular expression are not imported as SpagoBI Roles.

The element GROUP-TRANSFORMERS contains one or more TRANSFORMER elements. Each transformer element is a role 
for the functionalities assignment based on the user groups. The attributes of the transformer are:
  - startwith: the user group names which start with the value of the attribute are assigned as 
    user functionalities
  - prefix: the value of the attribute is put before the name of the user group
  - dimension: the first token of the user group is substitute with the value of the attribute. This behaviour 
    is useful because often the first token has not the same name of the datawarehouse dimension


-----------------------------------------------------
Example:
exo group 
/Zone Territoriali/Zona1/filiale2
transformer configuration:
<TRANSFORMER startwith="/Zone Territoriali" prefix="data_access:" dimension="Filiali" />
resulting profile functionality:
data_access:/Filiali/Zona1/filiale2
------------------------------------------------------

When a new olap document is defined its template must have the following sintax:

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

The entire element DATA-ACCESS can be ommitted but in that case no one filter is applied.
The element GRANTED-DIMENSIONS can contain more DIMENSION elements. Each one of this elements means that
the driver must add, to the olap engine request, the parameters for the data access control for the 
dimension specified.

When the driver is invoked:
- it recovers the list of granted dimensions from the template
- for each one granted dimension tag it recovers the dimension name
- it recover all the functionalities from the user profile
- All the functionalities that don't start with the string data_access:/<<dimension name>> are discarded
  Example of valid functionality --> data_access:/Filiali
- The fucntionalities not discarded are sent to the engine 

The engine implements the logic which allow to filter the datawarehouse data based on the parameters passed 
by the driver. 


	
