INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('MODEL_ROOT', 'Model root type', 'GQM_ROOT', 'GQM root', '');
INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('MODEL_ROOT', 'Model root type', 'GENERIC_ROOT', 'Generic root', '');

INSERT INTO SBI_DOMAINS (DOMAIN_CD, DOMAIN_NM, VALUE_CD, VALUE_NM, VALUE_DS) VALUES ('MODEL_NODE', 'Model node type', 'GENERIC_NODE', 'Generic Node', '');

INSERT INTO SBI_KPI_MODEL_ATTR (KPI_MODEL_ATTR_TYPE_ID, KPI_MODEL_ATTR_CD, KPI_MODEL_ATTR_NM, KPI_MODEL_ATTR_DESCR) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_ROOT'), 'NAME', 'NAME', 'Name of generic root');
INSERT INTO SBI_KPI_MODEL_ATTR (KPI_MODEL_ATTR_TYPE_ID, KPI_MODEL_ATTR_CD, KPI_MODEL_ATTR_NM, KPI_MODEL_ATTR_DESCR) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_ROOT'), 'DESCRIPTION', 'DESCRIPTION', 'Description of generic root');
INSERT INTO SBI_KPI_MODEL_ATTR (KPI_MODEL_ATTR_TYPE_ID, KPI_MODEL_ATTR_CD, KPI_MODEL_ATTR_NM, KPI_MODEL_ATTR_DESCR) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GQM_ROOT'), 'NAME', 'NAME', 'Name of GQM root');
INSERT INTO SBI_KPI_MODEL_ATTR (KPI_MODEL_ATTR_TYPE_ID, KPI_MODEL_ATTR_CD, KPI_MODEL_ATTR_NM, KPI_MODEL_ATTR_DESCR) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GQM_ROOT'), 'DESCRIPTION', 'DESCRIPTION', 'Description of GQM root');

INSERT INTO SBI_KPI_MODEL_ATTR (KPI_MODEL_ATTR_TYPE_ID, KPI_MODEL_ATTR_CD, KPI_MODEL_ATTR_NM, KPI_MODEL_ATTR_DESCR) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_NODE'), 'NAME', 'NAME', 'Name of generic node');

INSERT INTO SBI_KPI_MODEL (KPI_MODEL_TYPE_ID, KPI_MODEL_CD, KPI_MODEL_NM, KPI_MODEL_DESC) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_ROOT'), 'FIRST_ROOT', 'FIRST_ROOT', 'test first root');

INSERT INTO SBI_KPI_MODEL (KPI_MODEL_TYPE_ID, KPI_MODEL_CD, KPI_MODEL_NM, KPI_MODEL_DESC) VALUES ((SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GQM_ROOT'), 'SECOND_ROOT', 'SECOND_ROOT', 'test second root');

INSERT INTO SBI_KPI_MODEL_ATTR_VAL (KPI_MODEL_ATTR_ID, KPI_MODEL_ID, VALUE) VALUES ((SELECT KPI_MODEL_ATTR_ID FROM sbi_kpi_model_attr WHERE KPI_MODEL_ATTR_TYPE_ID = (SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_ROOT') AND KPI_MODEL_ATTR_CD='NAME'),(SELECT KPI_MODEL_ID FROM sbi_kpi_model WHERE KPI_MODEL_CD='FIRST_ROOT'),'Name first root');

INSERT INTO SBI_KPI_MODEL_ATTR_VAL (KPI_MODEL_ATTR_ID, KPI_MODEL_ID, VALUE) VALUES ((SELECT KPI_MODEL_ATTR_ID FROM sbi_kpi_model_attr WHERE KPI_MODEL_ATTR_TYPE_ID = (SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GENERIC_ROOT') AND KPI_MODEL_ATTR_CD='DESCRIPTION'),(SELECT KPI_MODEL_ID FROM sbi_kpi_model WHERE KPI_MODEL_CD='FIRST_ROOT'),'Description first root');

INSERT INTO SBI_KPI_MODEL_ATTR_VAL (KPI_MODEL_ATTR_ID, KPI_MODEL_ID, VALUE) VALUES ((SELECT KPI_MODEL_ATTR_ID FROM sbi_kpi_model_attr WHERE KPI_MODEL_ATTR_TYPE_ID = (SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GQM_ROOT') AND KPI_MODEL_ATTR_CD='NAME'),(SELECT KPI_MODEL_ID FROM sbi_kpi_model WHERE KPI_MODEL_CD='SECOND_ROOT'),'Name second root');

INSERT INTO SBI_KPI_MODEL_ATTR_VAL (KPI_MODEL_ATTR_ID, KPI_MODEL_ID, VALUE) VALUES ((SELECT KPI_MODEL_ATTR_ID FROM sbi_kpi_model_attr WHERE KPI_MODEL_ATTR_TYPE_ID = (SELECT VALUE_ID FROM sbi_domains WHERE VALUE_CD='GQM_ROOT') AND KPI_MODEL_ATTR_CD='DESCRIPTION'),(SELECT KPI_MODEL_ID FROM sbi_kpi_model WHERE KPI_MODEL_CD='SECOND_ROOT'),'Description second root');
