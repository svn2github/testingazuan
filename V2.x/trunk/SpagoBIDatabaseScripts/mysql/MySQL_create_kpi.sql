-- DEFINITION

Create table `SBI_KPI_ROLE` (
	`id_kpi_role` Int NOT NULL AUTO_INCREMENT,
	`KPI_ID` Int NOT NULL,
	`EXT_ROLE_ID` Int NOT NULL,
	UNIQUE (`id_kpi_role`),
 Primary Key (`id_kpi_role`)) ENGINE = InnoDB;

Create table `SBI_KPI` (
	`KPI_ID` Int NOT NULL AUTO_INCREMENT,
	`id_measure_unit` Int,
	`DS_ID` Int,
	`THRESHOLD_ID` Int,
	`id_kpi_parent` Int,
	`name` Varchar(400) NOT NULL,
	`document_label` Varchar(40),
	`code` Varchar(40),
	`metric` Varchar(1000),
	`description` Varchar(1000),
	`weight` Double,
	`flg_is_father` Char(1),
 Primary Key (`KPI_ID`)) ENGINE = InnoDB;

Create table `SBI_MEASURE_UNIT` (
	`id_measure_unit` Int NOT NULL AUTO_INCREMENT,
	`name` Varchar(400),
	`SCALE_TYPE_ID` Int NOT NULL,
	`SCALE_CD` Varchar(40),
	`SCALE_NM` Varchar(400),
 Primary Key (`id_measure_unit`)) ENGINE = InnoDB;

Create table `SBI_THRESHOLD` (
	`THRESHOLD_ID` Int NOT NULL AUTO_INCREMENT,
	`THRESHOLD_TYPE_ID` Int NOT NULL,
	`name` Varchar(400),
	`description` Varchar(1000),
 Primary Key (`THRESHOLD_ID`)) ENGINE = InnoDB;

Create table `SBI_THRESHOLD_VALUE` (
	`id_threshold_value` Int NOT NULL AUTO_INCREMENT,
	`THRESHOLD_ID` Int NOT NULL,
	`SEVERITY_ID` Int,
	`position` Int,
	`min_value` Double,
	`max_value` Double,
	`label` Varchar(20),
	`colour` Varchar(20),
 Primary Key (`id_threshold_value`)) ENGINE = InnoDB;

Create table `SBI_KPI_MODEL` (
	`KPI_MODEL_ID` Int NOT NULL AUTO_INCREMENT,
	`KPI_ID` Int,
	`KPI_MODEL_TYPE_ID` Int NOT NULL,
	`KPI_PARENT_MODEL_ID` Int,
	`KPI_MODEL_CD` Varchar(40),
	`KPI_MODEL_NM` Varchar(400),
	`KPI_MODEL_DESC` Varchar(1000),
 Primary Key (`KPI_MODEL_ID`)) ENGINE = InnoDB;

Create table `SBI_KPI_MODEL_ATTR` (
	`KPI_MODEL_ATTR_ID` Int NOT NULL AUTO_INCREMENT,
	`KPI_MODEL_ATTR_TYPE_ID` Int NOT NULL,
	`KPI_MODEL_ATTR_CD` Varchar(40),
	`KPI_MODEL_ATTR_NM` Varchar(400),
	`KPI_MODEL_ATTR_DESCR` Varchar(1000),
 Primary Key (`KPI_MODEL_ATTR_ID`)) ENGINE = InnoDB;

Create table `SBI_KPI_MODEL_ATTR_VAL` (
	`KPI_MODEL_ATTR_VAL_ID` Int NOT NULL AUTO_INCREMENT,
	`KPI_MODEL_ATTR_ID` Int NOT NULL,
	`KPI_MODEL_ID` Int NOT NULL,
	`VALUE` Varchar(2048),
	UNIQUE (`KPI_MODEL_ATTR_VAL_ID`),
 Primary Key (`KPI_MODEL_ATTR_VAL_ID`)) ENGINE = InnoDB;


-- INSTANCE

Create table `SBI_KPI_PERIODICITY` (
	`id_kpi_periodicity` Int NOT NULL AUTO_INCREMENT,
	`name` Varchar(400),
	`months` Int,
	`days` Int,
	`hours` Int,
	`minutes` Int,
	`chron_string` Varchar(20),
	`start_date` TIMESTAMP,
	UNIQUE (`id_kpi_periodicity`),
 Primary Key (`id_kpi_periodicity`)) ENGINE = InnoDB;

Create table `SBI_KPI_INSTANCE` (
	`id_kpi_instance` Int NOT NULL AUTO_INCREMENT,
	`KPI_ID` Int NOT NULL,
	`THRESHOLD_ID` Int,
	`CHART_TYPE_ID` Int,
	`id_measure_unit` Int,
	`weight` Double,
	`target` Double,
	`BEGIN_DT` Datetime,
 Primary Key (`id_kpi_instance`)) ENGINE = InnoDB;

Create table `SBI_KPI_INST_PERIOD` (
  `KPI_INST_PERIOD_ID` INTEGER NOT NULL AUTO_INCREMENT,
  `KPI_INSTANCE_ID` INTEGER NOT NULL,
  `PERIODICITY_ID` INTEGER NOT NULL,
  `DEFAULT` BOOLEAN ,
  PRIMARY KEY (`KPI_INST_PERIOD_ID`)
)
ENGINE = InnoDB;

Create table `SBI_KPI_INSTANCE_HISTORY` (
	`id_kpi_instance_history` Int NOT NULL AUTO_INCREMENT,
	`id_kpi_instance` Int NOT NULL,
	`THRESHOLD_ID` Int,
	`CHART_TYPE_ID` Int,
	`id_measure_unit` Int,
	`weight` Double,
	`target` Double,
	`BEGIN_DT` Datetime,
	`END_DT` Datetime,
	UNIQUE (`id_kpi_instance_history`),
 Primary Key (`id_kpi_instance_history`)) ENGINE = InnoDB;

Create table `SBI_KPI_VALUE` (
	`id_kpi_instance_value` Int NOT NULL AUTO_INCREMENT,
	`id_kpi_instance` Int NOT NULL,
	`RESOURCE_ID` Int NOT NULL,
	`VALUE` Varchar(40),
	`BEGIN_DT` Datetime,
	`END_DT` Datetime,
	UNIQUE (`id_kpi_instance_value`),
 Primary Key (`id_kpi_instance_value`)) ENGINE = InnoDB;

Create table `SBI_KPI_MODEL_INST` (
	`KPI_MODEL_INST` Int NOT NULL AUTO_INCREMENT,
	`KPI_MODEL_INST_PARENT` Int,
	`KPI_MODEL_ID` Int,
	`id_kpi_instance` Int,
	`name` Varchar(400),
	`description` Varchar(1000),
 Primary Key (`KPI_MODEL_INST`)) ENGINE = InnoDB;

Create table `SBI_RESOURCES` (
	`RESOURCE_ID` Int NOT NULL AUTO_INCREMENT,
	`RESOURCE_TYPE_ID` Int NOT NULL,
	`TABLE_NAME` Varchar(40),
	`COLUMN_NAME` Varchar(40),
	`RESOURCE_NAME` Varchar(40),
	`RESOURCE_DESCR` Varchar(400),
 Primary Key (`RESOURCE_ID`)) ENGINE = InnoDB;

Create table `SBI_KPI_MODEL_RESOURCES` (
	`KPI_MODEL_RESOURCES_ID` Int NOT NULL AUTO_INCREMENT,
	`RESOURCE_ID` Int NOT NULL,
	`KPI_MODEL_INST` Int NOT NULL,
 Primary Key (`KPI_MODEL_RESOURCES_ID`)) ENGINE = InnoDB;

-- ALARM

Create table `SBI_ALARM` (
	`ALARM_ID` Int NOT NULL AUTO_INCREMENT,
	`id_kpi_instance` Int NOT NULL,
	`MODALITY_ID` Int NOT NULL COMMENT 'VIA MAIL, SMS,ETC...',
	`DOCUMENT_ID` Int COMMENT 'DOCUMENTO PER ALLEGARE ALL''ALLARME',
	`LABEL` Varchar(50),
	`NAME` Varchar(50),
	`DESCR` Varchar(200),
	`TEXT` Varchar(1000) COMMENT 'TESTO STATICO DA ALLEGARE ALLA NOTIFICA DELL''ALLARME ',
	`URL` Varchar(20) COMMENT 'URL DA INSERIRE NELLA NOTIFICA',
	`SINGLE_EVENT` Char(1) COMMENT 'INDICA SE L''ALLARME DEVE ESSERE RIPETITIVO OPPURE NO, NEL PRIMO CASO L''UTENTE DOVRA'' SPEGNERE L''ALLARME',
	`AUTO_DISABLED` char(1) default NULL,
	`id_threshold_value` Int NOT NULL,
 Primary Key (`ALARM_ID`)) ENGINE = InnoDB
COMMENT = 'TABELLA DI DEFINIZIONE DEGLI ALLARMI';

Create table `SBI_ALARM_EVENT` (
	`ALARM_EVENT_ID` Int NOT NULL AUTO_INCREMENT,
	`ALARM_ID` Int NOT NULL,
	`EVENT_TS` Datetime COMMENT 'TS IN CUI è STATO INSERITO L''ALLARME',
	`ACTIVE` Char(1) COMMENT 'INDICA IL FATTO CHE L''ALLARME E'' ATTIVO, CIOE'' IL MOTORE LO DEVE NOTIFICARE',
	`KPI_VALUE` Varchar(50) COMMENT 'VALORE DEL KPI CALCOLATO',
	`THRESHOLD_VALUE` Varchar(50),
	`KPI_NAME` Varchar(100) COMMENT 'NOME DEL KPI UTILIZZATO PER CALCOLARE L''ALLARME',
	`RESOURCES` varchar(200) default NULL,
 Primary Key (`ALARM_EVENT_ID`)) ENGINE = InnoDB;

Create table `SBI_ALARM_CONTACT` (
	`ALARM_CONTACT_ID` Int NOT NULL AUTO_INCREMENT,
	`NAME` Varchar(100) NOT NULL,
	`EMAIL` Varchar(100),
	`MOBILE` Varchar(50),
	`RESOURCES` varchar(200) default NULL,
 Primary Key (`ALARM_CONTACT_ID`)) ENGINE = InnoDB;

Create table `SBI_ALARM_DISTRIBUTION` (
	`ALARM_CONTACT_ID` Int NOT NULL,
	`ALARM_ID` Int NOT NULL,
 Primary Key (`ALARM_CONTACT_ID`,`ALARM_ID`)) ENGINE = InnoDB;
