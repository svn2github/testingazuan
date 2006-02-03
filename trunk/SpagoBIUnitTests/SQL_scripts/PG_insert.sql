INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'QUERY', 'Query statement', 'Query statement to load list of values to choose in');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'MAN_IN', 'Manual input', 'Not predefined values but manual input');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'SCRIPT', 'Script to load values', 'Script to load predefined values');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('INPUT_TYPE', 'Input mode and values', 'FIX_LOV', 'Fixed list of values', 'Predefined and hard coded list of values to choose in');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'REPORT', 'Report', 'Basic business intelligence objects type');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'OLAP', 'On-line analytical processing', 'Dimensional analysis of significant facts');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'DATA_MINING', 'Data mining model', 'Model to find out hidden information in data');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('BIOBJ_TYPE', 'BI Object types', 'DASH', 'DASHBOARD', 'Dashboard to monitor perfomance indicators');
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



INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='DATE'), 
'DATE', 'yyyy/MM/dd', NULL, 'CK-CUS-01','American Date', 'The format of american Date');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='DATE'), 
'DATE', 'dd/MM/yyyy', NULL, 'CK-CUS-02','Italian Date', 'Define the format of the italy date');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='MAXLENGTH'), 
'MAXLENGTH', '20', NULL, 'CK-CUS-03','MaxLenght20', 'MaxLenght20');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='MAXLENGTH'), 
'MAXLENGTH', '30', NULL, 'CK-CUS-04','MaxLenght30', 'Length 30 characters');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='RANGE'), 
'RANGE', '10', '20', 'CK-CUS-05','Range 10-20', 'Value between 10-20');
INSERT INTO SBI_CHECKS (VALUE_TYPE_ID, VALUE_TYPE_CD, VALUE_1, VALUE_2, LABEL, NAME, DESCR) VALUES 
((select VALUE_ID from SBI_DOMAINS where DOMAIN_CD='CHECK' and VALUE_CD='DECIMALS'), 
'DECIMALS', '2', NULL, 'CK-CUS-06','2 decimal places', '2 decimal places');

