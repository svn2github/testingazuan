INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'INPUT_TYPE', 'Input mode and values', 'QUERY', 'Query statement', 'Query statement to load list of values to choose in' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'INPUT_TYPE', 'Input mode and values', 'SCRIPT', 'Script to load values', 'Script to load values' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'INPUT_TYPE', 'Input mode and values', 'FIX_LOV', 'Fixed list of values', 'Predefined and hard coded list of values to choose in' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'INPUT_TYPE', 'Input mode and values', 'JAVA_CLASS', 'Java class', 'Java class to load list of values to choose in' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'REPORT', 'Report', 'Basic business intelligence objects type' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'OLAP', 'On-line analytical processing', 'Dimensional analysis of significant facts' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'DATA_MINING', 'Data mining model', 'Model to find out hidden information in data' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'DASH', 'Dashboard', 'Dashboard to monitor perfomance indicators' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'DATAMART', 'Datamart Model', 'Logical definition of a datamart to inquiry' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'MAP', 'Map', 'Basic business intelligence objects type' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'BOOKLET', 'Booklet', 'Booklet' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'OFFICE_DOC', 'Office document', 'Office document' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'BIOBJ_TYPE', 'BI Object types', 'ETL', 'ETL process', 'ETL process' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'VALUE_TYPE', 'Input value types to check', 'BOOL', 'Boolean', 'Boolean input value type' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'VALUE_TYPE', 'Input value types to check', 'SINGLE', 'Single value', 'Only single value allowed' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'VALUE_TYPE', 'Input value types to check', 'MULTI', 'Multivalue', 'Many values are allowed' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PAR_TYPE', 'Parameter type', 'DATE', 'Date', 'Parameter expects date values' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PAR_TYPE', 'Parameter type', 'NUM', 'Number', 'Parameter expects numerical values' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PAR_TYPE', 'Parameter type', 'STRING', 'String', 'Parameter expects textual values' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'ROLE_TYPE', 'User role type', 'USER', 'Functional role', 'Functional role for end users' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'ROLE_TYPE', 'User role type', 'ADMIN', 'Administrative role', 'Administrative role for developer users'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ROLE_TYPE', 'Developer role type', 'DEV_ROLE', 'Developer role', 'Developer role for developer users');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('ROLE_TYPE', 'Tester role type', 'TEST_ROLE', 'Tester role', 'Tester role for tester users');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'STATE', 'Object state', 'SUSP', 'Suspended', 'Document temporanlly not in use'  
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'FUNCT_TYPE', 'Functionality', 'LOW_FUNCT', 'Low level functionality', 'BI Functionality joined to cate ries tree' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'FUNCT_TYPE', 'Functionality', 'USER_FUNCT', 'User functionality', 'BI Functionality joined to cate ries tree'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'STATE', 'Object state', 'DEV', 'Development', 'Document in development phase' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'STATE', 'Object state', 'TEST', 'Test', 'Document in testing phase' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'STATE', 'Object state', 'REL', 'Released', 'Document released for end users use' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'DATE', 'Date', 'Date' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'REGEXP', 'Regexp', 'Regular Expression ' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'MAXLENGTH', 'Max Length', 'Max Length' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'RANGE', 'Range', 'Range' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'DECIMALS', 'Decimal', 'Decimal' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'CHECK', 'Check', 'MINLENGTH', 'Min Length', 'Min Length' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'INTERNET ADDRESS', 'Internet Address', 'Internet Address' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'NUMERIC', 'Numeric', 'Numeric' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'ALFANUMERIC', 'Alfanumeric', 'Alfanumeric' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'LETTERSTRING', 'Letter String', 'Letter' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Check', 'MANDATORY', 'Mandatory', 'Mandatory' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'FISCALCODE', 'Fiscal Code', 'Fiscal Code' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'PRED_CHECK', 'Pred Check', 'EMAIL', 'E-Mail', 'E-Mail' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'ENGINE_TYPE', 'Engine types', 'EXT', 'External Engine', 'Business intelligence external engine of Spa BI platform' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'ENGINE_TYPE', 'Engine types', 'INT', 'Internal Engine', 'Business intelligence internal engine of Spa BI platform' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'SELECTION_TYPE','Selection modality of parameter values','LIST','List values selection','Single-value selection from a list' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'SELECTION_TYPE','Selection modality of parameter values','CHECK_LIST','CheckList values selection','Multi-value selection from a checklist' 
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'SELECTION_TYPE','Selection modality of parameter values','COMBOBOX','ComboBox values selection','Single value selection from a combobox'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'DEFAULT','','-1' ;
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'ORACLE','Oracle (any version)','org.hibernate.dialect.OracleDialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'ORACLE 9i/10g','Oracle (Oracle 9i/10g)', 'org.hibernate.dialect.Oracle9Dialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'SQLSERVER','SQL Server ', 'org.hibernate.dialect.SQLServerDialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'HQL','HQL ', 'org.hibernate.dialect.HSQLDialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'MYSQL','MySql ', 'org.hibernate.dialect.MySQLInnoDBDialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'POSTGRESQL','PostgreSQL ', 'org.hibernate.dialect.PostgreSQLDialect'
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) SELECT 'DIALECT_HIB', 'Predefined hibernate dialect', 'INGRES','Ingres ', 'org.hibernate.dialect.IngresDialect'

--INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES (0, 'Dashboard Internal Engine', 'Dashboard Internal Engine', '', '', '', '', '', 'DashboardInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT') go 'it.eng.spa bi.engines.dashboard.Spa BIDashboardInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='DASH') go) 
INSERT SBI_ENGINES
(ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR,
 OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE)
SELECT 0, 'Dashboard Internal Engine', 'Dashboard Internal Engine', '', '',
'', '', '', 'DashboardInternalEng',
S1.VALUE_ID, 'it.eng.spa bi.engines.dashboard.Spa BIDashboardInternalEngine', S2.VALUE_ID
from SBI_DOMAINS S1, SBI_DOMAINS S2
where S1.DOMAIN_CD='ENGINE_TYPE' and S1.VALUE_CD='INT'
and S2.DOMAIN_CD='BIOBJ_TYPE' and S2.VALUE_CD='DASH'
--INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE)
-- VALUES (0, 'Booklet Internal Engine', 'Booklet Internal Engine', '', '', '', '', '', 'BookletInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT') go 'it.eng.spa bi.booklets.engines.Spa BIBookletInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='BOOKLET') go) 
INSERT SBI_ENGINES
(ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR,
 OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE)
SELECT 0, 'Booklet Internal Engine', 'Booklet Internal Engine', '', '',
'', '', '', 'BookletInternalEng',
S1.VALUE_ID, 'it.eng.spa bi.booklets.engines.Spa BIBookletInternalEngine', S2.VALUE_ID
from SBI_DOMAINS S1, SBI_DOMAINS S2
where S1.DOMAIN_CD='ENGINE_TYPE' and S1.VALUE_CD='INT'
and S2.DOMAIN_CD='BIOBJ_TYPE' and S2.VALUE_CD='BOOKLET'
--INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) VALUES 
--(0, 'Office Document Internal Engine', 'Office Document Internal Engine', '', '', '', '', '', 'OfficeInternalEng', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT') go 'it.eng.spa bi.engines.officeDocuments.Spa BIOfficeDocumentInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='OFFICE_DOC') go) 
INSERT SBI_ENGINES
(ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR,
 OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE)
SELECT 0, 'Office Document Internal Engine', 'Office Document Internal Engine', '', '',
'', '', '', 'OfficeInternalEng',
S1.VALUE_ID, 'it.eng.spa bi.engines.officeDocuments.Spa BIOfficeDocumentInternalEngine', S2.VALUE_ID
from SBI_DOMAINS S1, SBI_DOMAINS S2
where S1.DOMAIN_CD='ENGINE_TYPE' and S1.VALUE_CD='INT'
and S2.DOMAIN_CD='BIOBJ_TYPE' and S2.VALUE_CD='OFFICE_DOC'
--INSERT INTO SBI_ENGINES (ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR, OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE) 
--VALUES (0, 'Dashboard Composition Internal Engine', 'Dashboard Composition Internal Engine', '', '', '', '', '', 'DashboardCompIE', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='ENGINE_TYPE' and VALUE_CD='INT') go 'it.eng.spa bi.engines.dashboardscomposition.Spa BIDashboardsCompositionInternalEngine', (select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='BIOBJ_TYPE' and VALUE_CD='DASH') go) 
INSERT SBI_ENGINES
(ENCRYPT, NAME, DESCR, MAIN_URL, SECN_URL, OBJ_UPL_DIR,
 OBJ_USE_DIR, DRIVER_NM, LABEL, ENGINE_TYPE, CLASS_NM, BIOBJ_TYPE)
SELECT 0, 'Dashboard Composition Internal Engine', 'Dashboard Composition Internal Engine', '', '',
'', '', '', 'DashboardCompIE',
S1.VALUE_ID, 'it.eng.spa bi.engines.dashboardscomposition.Spa BIDashboardsCompositionInternalEngine', S2.VALUE_ID
from SBI_DOMAINS S1, SBI_DOMAINS S2
where S1.DOMAIN_CD='ENGINE_TYPE' and S1.VALUE_CD='INT'
and S2.DOMAIN_CD='BIOBJ_TYPE' and S2.VALUE_CD='DASH'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='INTERNET ADDRESS') 
'INTERNET ADDRESS', NULL, NULL, 'CK-FIX-01','Internet Address', 'Control if parameter is an Internet Address') 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'INTERNET ADDRESS', NULL, NULL,
'CK-FIX-01','Internet Address', 'Control if parameter is an Internet Address'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='INTERNET ADDRESS'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='NUMERIC') 
'NUMERIC', NULL, NULL, 'CK-FIX-02','Numeric', 'Control if  a parameter is Numeric') go 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'NUMERIC', NULL, NULL,
'CK-FIX-02','Numeric', 'Control if  a parameter is Numeric'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='NUMERIC'
/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='ALFANUMERIC') 
'ALFANUMERIC', NULL, NULL, 'CK-FIX-03','Alfanumeric', 'Control if  a parameter is Alfanumeric') go 
*/

INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'ALFANUMERIC', NULL, NULL,
'CK-FIX-03','Alfanumeric', 'Control if  a parameter is Alfanumeric'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='ALFANUMERIC'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='LETTERSTRING') 
'LETTERSTRING', NULL, NULL, 'CK-FIX-04','Letter String', 'Control if a parameter is a letter string') go 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'LETTERSTRING', NULL, NULL,
'CK-FIX-04','Letter String', 'Control if a parameter is a letter string'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='LETTERSTRING'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='MANDATORY') 
'MANDATORY', NULL, NULL, 'CK-FIX-05','Mandatory', 'Control if the parameter is present') go 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'MANDATORY', NULL, NULL,
'CK-FIX-05','Mandatory', 'Control if the parameter is present'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='MANDATORY'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='FISCALCODE') go 
'FISCALCODE', NULL, NULL, 'CK-FIX-06','Fiscal Code', 'Control if parameter is a Fiscal Code') go 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'FISCALCODE', NULL, NULL,
'CK-FIX-06','Fiscal Code', 'Control if parameter is a Fiscal Code'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='FISCALCODE'

/*INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='PRED_CHECK' and VALUE_CD='EMAIL') go
'EMAIL', NULL, NULL, 'CK-FIX-07','E-Mail', 'Control if parameter is a E-Mail') go 
*/
INSERT INTO SBI_CHECKS
(VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR)
SELECT D.VALUE_ID, 'EMAIL', NULL, NULL,
'CK-FIX-07','E-Mail', 'Control if parameter is a E-Mail'
from SBI_DOMAINS D where DOMAIN_CD='PRED_CHECK' and VALUE_CD='EMAIL'


/*INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_MONTH_YEAR', 'Current month of the year', 'Current month of the year format mm', 
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nreturnValue(monthStr) </SCRIPT></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT')  'SCRIPT') go 
*/INSERT INTO SBI_LOV(LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD)
SELECT
'CURRENT_MONTH_YEAR', 'Current month of the year','Current month of the year format mm',
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nreturnValue(monthStr) </SCRIPT></SCRIPTLOV>',
D.VALUE_ID,   'SCRIPT'
FROM SBI_DOMAINS D where D.DOMAIN_CD='INPUT_TYPE' and D.VALUE_CD='SCRIPT'
/*INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_YEAR', 'Current year', 'Current year format yyyy', 
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint year = now.getYear() + 1900 \r\nString yearStr = year.toString() \r\nreturnValue(yearStr) </SCRIPT></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT')  'SCRIPT') go 
*/
INSERT INTO SBI_LOV(LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD)
SELECT
'CURRENT_YEAR', 'Current year','Current year format yyyy',
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint year = now.getYear() + 1900 \r\nString yearStr = year.toString() \r\nreturnValue(yearStr) </SCRIPT></SCRIPTLOV>',
D.VALUE_ID,   'SCRIPT'
FROM SBI_DOMAINS D where D.DOMAIN_CD='INPUT_TYPE' and D.VALUE_CD='SCRIPT'
/*INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_MONTH', 'Current month', 'Current month format mm/yyyy', 
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nint year = now.getYear() + 1900 \r\nString toReturn = monthStr + ''/'' + year.toString() \r\nreturnValue(toReturn) </SCRIPT></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT')  'SCRIPT') go 
*/
INSERT INTO SBI_LOV(LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD)
SELECT
'CURRENT_MONTH', 'Current month','Current month format mm/yyyy',
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nint year = now.getYear() + 1900 \r\nString toReturn = monthStr + ''/'' + year.toString() \r\nreturnValue(toReturn) </SCRIPT></SCRIPTLOV>',
D.VALUE_ID,   'SCRIPT'
FROM SBI_DOMAINS D where D.DOMAIN_CD='INPUT_TYPE' and D.VALUE_CD='SCRIPT'
/*INSERT INTO SBI_LOV (LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD) VALUES 
('CURRENT_DATE', 'Current date', 'Current date format dd/mm/yyyy', 
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint day = now.getDate() \r\nString dayStr = day.toString() \r\nif (day < 10) dayStr = ''0'' + dayStr \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nint year = now.getYear() + 1900 \r\nString toReturn = dayStr + ''/'' + monthStr + ''/'' + year.toString() \r\nreturnValue(toReturn) </SCRIPT></SCRIPTLOV>', 
(select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='INPUT_TYPE' and VALUE_CD='SCRIPT')  'SCRIPT') go 
*/
INSERT INTO SBI_LOV(LABEL, NAME, DESCR, LOV_PROVIDER, INPUT_TYPE_ID, INPUT_TYPE_CD)
SELECT
'CURRENT_DATE', 'Current date','Current date format dd/mm/yyyy',
'<SCRIPTLOV><SCRIPT>Date now = new Date() \r\nint day = now.getDate() \r\nString dayStr = day.toString() \r\nif (day < 10) dayStr = ''0'' + dayStr \r\nint month = now.getMonth() + 1 \r\nString monthStr = month.toString() \r\nif (month < 10) monthStr = ''0'' + monthStr \r\nint year = now.getYear() + 1900 \r\nString toReturn = dayStr + ''/'' + monthStr + ''/'' + year.toString() \r\nreturnValue(toReturn) </SCRIPT></SCRIPTLOV>',
D.VALUE_ID,   'SCRIPT'
FROM SBI_DOMAINS D where D.DOMAIN_CD='INPUT_TYPE' and D.VALUE_CD='SCRIPT'

INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'EnginesManagement','EnginesManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'FunctionalitiesManagement','FunctionalitiesManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'LovsManagement','LovsManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'ConstraintManagement','ConstraintManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'ParameterManagement','ParameterManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentAdministration','DocumentAdministration'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentDevManagement','DocumentDevManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentTestManagement','DocumentTestManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentAdminManagement','DocumentAdminManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'ImportExportManagement','ImportExportManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'SchedulerManagement','SchedulerManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'EventsManagement','EventsManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'WorkspaceManagement','WorkspaceManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'WorklistManagement','WorklistManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'MapCatalogueManagement','MapCatalogueManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentManagement','DocumentManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'SyncronizeRolesManagement','SyncronizeRolesManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'ProfileAttributeManagement','ProfileAttributeManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DataSourceManagement','DataSourceManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentUserManagement','DocumentUserManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentDeleteManagement','DocumentDeleteManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentStateManagement','DocumentStateManagement'
INSERT INTO SBI_USER_FUNCTIONALITY (NAME, DESCRIPTION) SELECT 'DocumentDetailManagement','DocumentDetailManagement'

INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='EnginesManagement')) 
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='FunctionalitiesManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='LovsManagement'))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ConstraintManagement'))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ParameterManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentAdministration' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDevManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentTestManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentAdminManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ImportExportManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='SchedulerManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='EventsManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='WorkspaceManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='WorklistManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='MapCatalogueManagement'))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='SyncronizeRolesManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ProfileAttributeManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DataSourceManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentUserManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDeleteManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentStateManagement'))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='ADMIN',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDetailManagement' ))

INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='LovsManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ConstraintManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ParameterManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDevManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='EventsManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='WorkspaceManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='WorklistManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='MapCatalogueManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='ProfileAttributeManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDeleteManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentDetailManagement' ))
INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='DEV_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DataSourceManagement' ))

INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='TEST_ROLE',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentTestManagement' ));

INSERT INTO SBI_ROLE_TYPE_USER_FUNCTIONALITY SELECT((SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD='ROLE_TYPE' AND VALUE_CD='USER',SELECT USER_FUNCT_ID FROM SBI_USER_FUNCTIONALITY WHERE NAME='DocumentUserManagement' ));