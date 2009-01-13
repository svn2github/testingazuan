-- DEFINITION
Alter table `SBI_KPI` add Foreign Key (`DS_ID`) references `SBI_DATA_SET` (`DS_ID`) on delete  restrict on update  restrict;
Alter table `SBI_THRESHOLD` add Foreign Key (`THRESHOLD_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_MEASURE_UNIT` add Foreign Key (`SCALE_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL` add Foreign Key (`KPI_MODEL_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_ATTR` add Foreign Key (`KPI_MODEL_ATTR_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_THRESHOLD_VALUE` add Foreign Key (`SEVERITY_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_ROLE` add Foreign Key (`EXT_ROLE_ID`) references `SBI_EXT_ROLES` (`EXT_ROLE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI` add Foreign Key (`id_kpi_parent`) references `SBI_KPI` (`KPI_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_ROLE` add Foreign Key (`KPI_ID`) references `SBI_KPI` (`KPI_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL` add Foreign Key (`KPI_ID`) references `SBI_KPI` (`KPI_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI` add Foreign Key (`id_measure_unit`) references `SBI_MEASURE_UNIT` (`id_measure_unit`) on delete  restrict on update  restrict;
Alter table `SBI_KPI` add Foreign Key (`THRESHOLD_ID`) references `SBI_THRESHOLD` (`THRESHOLD_ID`) on delete no action on update no action;
Alter table `SBI_THRESHOLD_VALUE` add Foreign Key (`THRESHOLD_ID`) references `SBI_THRESHOLD` (`THRESHOLD_ID`) on delete cascade on update no action;
Alter table `SBI_KPI_MODEL_ATTR_VAL` add Foreign Key (`KPI_MODEL_ID`) references `SBI_KPI_MODEL` (`KPI_MODEL_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL` add Foreign Key (`KPI_PARENT_MODEL_ID`) references `SBI_KPI_MODEL` (`KPI_MODEL_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_ATTR_VAL` add Foreign Key (`KPI_MODEL_ATTR_ID`) references `SBI_KPI_MODEL_ATTR` (`KPI_MODEL_ATTR_ID`) on delete  restrict on update  restrict;

-- INSTANCE
Alter table `SBI_RESOURCES` add Foreign Key (`RESOURCE_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE` add Foreign Key (`CHART_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE_HISTORY` add Foreign Key (`CHART_TYPE_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE` add Foreign Key (`id_measure_unit`) references `SBI_MEASURE_UNIT` (`id_measure_unit`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE_HISTORY` add Foreign Key (`id_measure_unit`) references `SBI_MEASURE_UNIT` (`id_measure_unit`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE` add Foreign Key (`THRESHOLD_ID`) references `SBI_THRESHOLD` (`THRESHOLD_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE_HISTORY` add Foreign Key (`THRESHOLD_ID`) references `SBI_THRESHOLD` (`THRESHOLD_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_INST` add Foreign Key (`KPI_MODEL_ID`) references `SBI_KPI_MODEL` (`KPI_MODEL_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_INST` add Foreign Key (`id_kpi_instance`) references `SBI_KPI_INSTANCE` (`id_kpi_instance`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_VALUE` add Foreign Key (`id_kpi_instance`) references `SBI_KPI_INSTANCE` (`id_kpi_instance`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_INSTANCE_HISTORY` add Foreign Key (`id_kpi_instance`) references `SBI_KPI_INSTANCE` (`id_kpi_instance`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_INST` add Foreign Key (`KPI_MODEL_INST_PARENT`) references `SBI_KPI_MODEL_INST` (`KPI_MODEL_INST`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_RESOURCES` add Foreign Key (`KPI_MODEL_INST`) references `SBI_KPI_MODEL_INST` (`KPI_MODEL_INST`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_MODEL_RESOURCES` add Foreign Key (`RESOURCE_ID`) references `SBI_RESOURCES` (`RESOURCE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_KPI_VALUE` add Foreign Key (`RESOURCE_ID`) references `SBI_RESOURCES` (`RESOURCE_ID`) on delete  restrict on update  restrict;
Alter TABLE `SBI_KPI_INST_PERIOD` ADD FOREIGN KEY (`PERIODICITY_ID`) REFERENCES `SBI_KPI_PERIODICITY` (`id_kpi_periodicity`) ON DELETE RESTRICT ON UPDATE RESTRICT;
Alter TABLE `SBI_KPI_INST_PERIOD` ADD FOREIGN KEY  (`KPI_INSTANCE_ID`) REFERENCES `SBI_KPI_INSTANCE` (`id_kpi_instance`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ALARM
Alter table `SBI_ALARM` add Foreign Key (`MODALITY_ID`) references `SBI_DOMAINS` (`VALUE_ID`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM` add Foreign Key (`DOCUMENT_ID`) references `SBI_OBJECTS` (`BIOBJ_ID`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM` add Foreign Key (`id_kpi_instance`) references `SBI_KPI_INSTANCE` (`id_kpi_instance`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM` add Foreign Key (`id_threshold_value`) references `SBI_THRESHOLD_VALUE` (`id_threshold_value`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM_EVENT` add Foreign Key (`ALARM_ID`) references `SBI_ALARM` (`ALARM_ID`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM_DISTRIBUTION` add Foreign Key (`ALARM_ID`) references `SBI_ALARM` (`ALARM_ID`) on delete  restrict on update  restrict;
Alter table `SBI_ALARM_DISTRIBUTION` add Foreign Key (`ALARM_CONTACT_ID`) references `SBI_ALARM_CONTACT` (`ALARM_CONTACT_ID`) on delete  restrict on update  restrict;
