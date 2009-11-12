This is an sql file to use to upgrade your SpagoBI 2.3.0 INGRES database to a SpagoBI 2.4.0 INGRES database.

Execute this script in your INGRES query browser.  

Pay attention to CREATION_DATE and LAST_CHANGE_DATE : they should have timestamp value as default, but Ingres goes in exception when it tries to do this. Make this setting manually.