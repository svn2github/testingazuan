This is an sql file to use to upgrade your SpagoBI 2.2.0 INGRES database to a SpagoBI 2.3.0 INGRES database.

Execute this script in your INGRES query browser.  

Pay attention to MULTI_SCHEMA and BUILD_QBE_QUERY : they should have a value's default, but Ingres goes in exception when it tries to do this. Make this setting manually.