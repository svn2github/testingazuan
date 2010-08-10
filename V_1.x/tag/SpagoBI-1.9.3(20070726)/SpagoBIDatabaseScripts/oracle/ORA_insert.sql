INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'QUERY', 'Query statement', 'Query statement to load list of values to choose in');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'SCRIPT', 'Script to load values', 'Script to load values');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'FIX_LOV', 'Fixed list of values', 'Predefined and hard coded list of values to choose in');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'JAVA_CLASS', 'Java class', 'Java class to load list of values to choose in');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'REPORT', 'Report', 'Basic business intelligence objects type');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'OLAP', 'On-line analytical processing', 'Dimensional analysis of significant facts');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'DATA_MINING', 'Data mining model', 'Model to find out hidden information in data');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'DASH', 'Dashboard', 'Dashboard to monitor perfomance indicators');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'DATAMART', 'Datamart Model', 'Logical definition of a datamart to inquiry');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'MAP', 'Map', 'Basic business intelligence objects type');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'BOOKLET', 'Booklet', 'Booklet');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'OFFICE_DOC', 'Office document', 'Office document');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'ETL', 'ETL process', 'ETL process');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('VALUE_TYPE', 'Input value types to check', 'BOOL', 'Boolean', 'Boolean input value type');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('VALUE_TYPE', 'Input value types to check', 'SINGLE', 'Single value', 'Only single value allowed');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('VALUE_TYPE', 'Input value types to check', 'MULTI', 'Multivalue', 'Many values are allowed');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PAR_TYPE', 'Parameter type', 'DATE', 'Date', 'Parameter expects date values');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PAR_TYPE', 'Parameter type', 'NUM', 'Number', 'Parameter expects numerical values');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PAR_TYPE', 'Parameter type', 'STRING', 'String', 'Parameter expects textual values');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ROLE_TYPE', 'User role type', 'FUNCT', 'Functional role', 'Functional role for end users');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ROLE_TYPE', 'User role type', 'ADMIN', 'Administrative role', 'Administrative role for developer users');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('STATE', 'Object state', 'SUSP', 'Suspended', 'Document temporanlly not in use');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ROLE_TYPE', 'User role type', 'PORTAL', 'External portal roles', 'Predefined roles in BI portal');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('FUNCT_TYPE', 'Functionality', 'LOW_FUNCT', 'Low level functionality', 'BI Functionality joined to categories tree');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('STATE', 'Object state', 'DEV', 'Development', 'Document in development phase');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('STATE', 'Object state', 'TEST', 'Test', 'Document in testing phase');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('STATE', 'Object state', 'REL', 'Released', 'Document released for end users use');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'DATE', 'Date', 'Date');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'REGEXP', 'Regexp', 'Regular Expression ');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'MAXLENGTH', 'Max Length', 'Max Length');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'RANGE', 'Range', 'Range');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'DECIMALS', 'Decimal', 'Decimal');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('CHECK', 'Check', 'MINLENGTH', 'Min Length', 'Min Length');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'INTERNET ADDRESS', 'Internet Address', 'Internet Address');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'NUMERIC', 'Numeric', 'Numeric');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'ALFANUMERIC', 'Alfanumeric', 'Alfanumeric');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'LETTERSTRING', 'Letter String', 'Letter');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Check', 'MANDATORY', 'Mandatory', 'Mandatory');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'FISCALCODE', 'Fiscal Code', 'Fiscal Code');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('PRED_CHECK', 'Pred Check', 'EMAIL', 'E-Mail', 'E-Mail');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ENGINE_TYPE', 'Engine types', 'EXT', 'External Engine', 'Business intelligence external engine of SpagoBI platform');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ENGINE_TYPE', 'Engine types', 'INT', 'Internal Engine', 'Business intelligence internal engine of SpagoBI platform');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('SELECTION_TYPE','Selection modality of parameter values','LIST','List values selection','Single-value selection from a list');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('SELECTION_TYPE','Selection modality of parameter values','CHECK_LIST','CheckList values selection','Multi-value selection from a checklist');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('SELECTION_TYPE','Selection modality of parameter values','COMBOBOX','ComboBox values selection','Single value selection from a combobox');

INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES (0, 'Dashboard Internal Engine', 'Dashboard Internal Engine', NULL, NULL, NULL, NULL, NULL, 'DashboardInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT'), 'it.eng.spagobi.engines.dashboard.SpagoBIDashboardInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='DASH'));
INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES (0, 'Booklet Internal Engine', 'Booklet Internal Engine',NULL,NULL,NULL,NULL,NULL, 'BookletInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT'), 'it.eng.spagobi.booklets.engines.SpagoBIBookletInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='BOOKLET'));
INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES (0, 'Office Document Internal Engine', 'Office Document Internal Engine', '', '', '', '', '', 'OfficeInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT'), 'it.eng.spagobi.engines.officeDocuments.SpagoBIOfficeDocumentInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='OFFICE_DOC'));
INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES (0, 'Dashboard Composition Internal Engine', 'Dashboard Composition Internal Engine', '', '', '', '', '', 'DashboardCompIE', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT'), 'it.eng.spagobi.engines.dashboardscomposition.SpagoBIDashboardsCompositionInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='DASH'));

INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='INTERNET ADDRESS'), 
'INTERNET ADDRESS', NULL, NULL, 'CK-FIX-01','Internet Address', 'Control if parameter is an Internet Address');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='NUMERIC'), 
'NUMERIC', NULL, NULL, 'CK-FIX-02','Numeric', 'Control if  a parameter is Numeric');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='ALFANUMERIC'), 
'ALFANUMERIC', NULL, NULL, 'CK-FIX-03','Alfanumeric', 'Control if  a parameter is Alfanumeric');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='LETTERSTRING'), 
'LETTERSTRING', NULL, NULL, 'CK-FIX-04','Letter String', 'Control if a parameter is a letter string');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='MANDATORY'), 
'MANDATORY', NULL, NULL, 'CK-FIX-05','Mandatory', 'Control if the parameter is present');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='FISCALCODE'), 
'FISCALCODE', NULL, NULL, 'CK-FIX-06','Fiscal Code', 'Control if parameter is a Fiscal Code');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='EMAIL'), 
'EMAIL', NULL, NULL, 'CK-FIX-07','E-Mail', 'Control if parameter is a E-Mail');

INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_MONTH_YEAR', 'Current month of the year', 'Current month of the year format mm', 
'<SCRIPTLOV><SCRIPT>
Date now = new Date();
int month = now.getMonth() + 1;
String monthStr = month.toString();
if (month < 10) monthStr = ''0'' + monthStr;
returnValue(monthStr);
</SCRIPT><VALUE-COLUMN>VALUE</VALUE-COLUMN><DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN><VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS><INVISIBLE-COLUMNS></INVISIBLE-COLUMNS></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT'), 'SCRIPT');
INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_YEAR', 'Current year', 'Current year format yyyy', 
'<SCRIPTLOV><SCRIPT>
Date now = new Date();
int year = now.getYear() + 1900;
String yearStr = year.toString();
returnValue(yearStr);
</SCRIPT><VALUE-COLUMN>VALUE</VALUE-COLUMN><DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN><VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS><INVISIBLE-COLUMNS></INVISIBLE-COLUMNS></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT'), 'SCRIPT');
INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_MONTH', 'Current month', 'Current month format mm/yyyy', 
'<SCRIPTLOV><SCRIPT>
Date now = new Date();
int month = now.getMonth() + 1;
String monthStr = month.toString();
if (month < 10) monthStr = ''0'' + monthStr;
int year = now.getYear() + 1900;
String toReturn = monthStr + ''/'' + year.toString();
returnValue(toReturn);
</SCRIPT><VALUE-COLUMN>VALUE</VALUE-COLUMN><DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN><VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS><INVISIBLE-COLUMNS></INVISIBLE-COLUMNS></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT'), 'SCRIPT');
INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_DATE', 'Current date', 'Current date format dd/mm/yyyy', 
'<SCRIPTLOV><SCRIPT>
Date now = new Date();
int day = now.getDate();
String dayStr = day.toString();
if (day < 10) dayStr = ''0'' + dayStr;
int month = now.getMonth() + 1;
String monthStr = month.toString();
if (month < 10) monthStr = ''0'' + monthStr;
int year = now.getYear() + 1900;
String toReturn = dayStr + ''/'' + monthStr + ''/'' + year.toString();
returnValue(toReturn);
</SCRIPT><VALUE-COLUMN>VALUE</VALUE-COLUMN><DESCRIPTION-COLUMN>VALUE</DESCRIPTION-COLUMN><VISIBLE-COLUMNS>VALUE</VISIBLE-COLUMNS><INVISIBLE-COLUMNS></INVISIBLE-COLUMNS></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT'), 'SCRIPT');
