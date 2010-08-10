CREATE SEQUENCE SBI_CHECKS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_DOMAINS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_ENGINES_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_EXT_ROLES_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_FUNCTIONS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_LOV_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_OBJECTS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_PARAMETERS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_PARUSE_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_OBJ_PAR_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_EVENTS_LOG_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_AUDIT_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_MAPS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_FEATURES_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_VIEWPOINTS_SEQ INCREMENT 1 START 1 ;


CREATE TABLE SBI_CHECKS (
       CHECK_ID             INTEGER DEFAULT nextval('SBI_CHECKS_SEQ') NOT NULL,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       VALUE_TYPE_CD        VARCHAR(20) NOT NULL,
       VALUE_TYPE_ID        INTEGER NOT NULL,
       VALUE_1              VARCHAR(400) NULL,
       VALUE_2              VARCHAR(400) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       CONSTRAINT XPKSBI_CHECKS 
              PRIMARY KEY (CHECK_ID)
);

CREATE UNIQUE INDEX XAK1SBI_CHECKS ON SBI_CHECKS
(
       LABEL                      
);


CREATE TABLE SBI_DOMAINS (
       VALUE_ID             INTEGER DEFAULT nextval('SBI_DOMAINS_SEQ') NOT NULL,
       VALUE_CD             VARCHAR(20) NULL,
       VALUE_NM             VARCHAR(40) NULL,
       DOMAIN_CD            VARCHAR(20) NULL,
       DOMAIN_NM            VARCHAR(40) NULL,
       VALUE_DS             VARCHAR(160) NULL,
       CONSTRAINT XPKSBI_DOMAINS 
              PRIMARY KEY (VALUE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_DOMAINS ON SBI_DOMAINS
(
       VALUE_CD                   ,
       DOMAIN_CD                  
);


CREATE TABLE SBI_ENGINES (
       ENGINE_ID            INTEGER DEFAULT nextval('SBI_ENGINES_SEQ') NOT NULL,
       ENCRYPT              SMALLINT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       MAIN_URL             VARCHAR(400) NULL,
       SECN_URL             VARCHAR(400) NULL,
       OBJ_UPL_DIR          VARCHAR(400) NULL,
       OBJ_USE_DIR          VARCHAR(400) NULL,
       DRIVER_NM            VARCHAR(400) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       ENGINE_TYPE          INTEGER NOT NULL,
       CLASS_NM             VARCHAR(400) NULL,
       BIOBJ_TYPE           INTEGER NOT NULL,    
       CONSTRAINT XPKSBI_ENGINES 
              PRIMARY KEY (ENGINE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_ENGINES ON SBI_ENGINES
(
       LABEL                          
);


CREATE TABLE SBI_EXT_ROLES (
       EXT_ROLE_ID          INTEGER DEFAULT nextval('SBI_EXT_ROLES_SEQ') NOT NULL,
       NAME                 VARCHAR(100) NULL,
       DESCR                VARCHAR(160) NULL,
       CODE                 VARCHAR(20) NULL,
       ROLE_TYPE_CD         VARCHAR(20) NOT NULL,
       ROLE_TYPE_ID         INTEGER NOT NULL,
       CONSTRAINT XPKSBI_EXT_ROLES 
              PRIMARY KEY (EXT_ROLE_ID)
);

CREATE TABLE SBI_FUNC_ROLE (
       ROLE_ID              INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       STATE_CD             VARCHAR(20) NULL,
       STATE_ID             INTEGER NOT NULL,
       CONSTRAINT XPKSBI_FUNC_ROLE 
              PRIMARY KEY (FUNCT_ID, STATE_ID, ROLE_ID)
);


CREATE TABLE SBI_FUNCTIONS (
       FUNCT_ID             INTEGER DEFAULT nextval('SBI_FUNCTIONS_SEQ') NOT NULL,
       FUNCT_TYPE_CD        VARCHAR(20) NOT NULL,
       PARENT_FUNCT_ID      INTEGER NULL,
       NAME                 VARCHAR(40) NULL,
       DESCR                VARCHAR(160) NULL,
       PATH                 VARCHAR(400) NULL,
       CODE                 VARCHAR(20) NOT NULL,
       PROG					INTEGER NOT NULL,
       FUNCT_TYPE_ID        INTEGER NOT NULL,
       CONSTRAINT XPKSBI_FUNCTIONS 
              PRIMARY KEY (FUNCT_ID)
);

CREATE UNIQUE INDEX XAK1SBI_FUNCTIONS ON SBI_FUNCTIONS
(
       CODE                       
);


CREATE TABLE SBI_LOV (
       LOV_ID               INTEGER DEFAULT nextval('SBI_LOV_SEQ') NOT NULL,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       INPUT_TYPE_CD        VARCHAR(20) NOT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       LOV_PROVIDER         VARCHAR(4000) NULL,
       INPUT_TYPE_ID        INTEGER NOT NULL,
       PROFILE_ATTR         VARCHAR(20) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       CONSTRAINT XPKSBI_LOV 
              PRIMARY KEY (LOV_ID)
);

CREATE UNIQUE INDEX XAK1SBI_LOV ON SBI_LOV
(
       LABEL                      
);


CREATE TABLE SBI_OBJ_FUNC (
       BIOBJ_ID             INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       CONSTRAINT XPKSBI_OBJ_FUNC 
              PRIMARY KEY (BIOBJ_ID, FUNCT_ID)
);


CREATE TABLE SBI_OBJ_PAR (
       OBJ_PAR_ID           INTEGER DEFAULT nextval('SBI_OBJ_PAR_SEQ') NOT NULL,
       PAR_ID               INTEGER NOT NULL,
       BIOBJ_ID             INTEGER NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       REQ_FL               SMALLINT NULL,
       MOD_FL               SMALLINT NULL,
       VIEW_FL              SMALLINT NULL,
       MULT_FL              SMALLINT NULL,
       PROG                 INTEGER NOT NULL,
       PARURL_NM            VARCHAR(20) NULL,
       PRIORITY             INTEGER NULL,
       CONSTRAINT XPKSBI_OBJ_PAR 
              PRIMARY KEY (OBJ_PAR_ID)
);


CREATE TABLE SBI_OBJ_STATE (
       BIOBJ_ID             INTEGER NOT NULL,
       STATE_ID             INTEGER NOT NULL,
       END_DT               DATE NULL,
       START_DT             DATE NOT NULL,
       NOTE                 VARCHAR(300) NULL,
       CONSTRAINT XPKSBI_OBJ_STATE 
              PRIMARY KEY (BIOBJ_ID, STATE_ID, START_DT)
);


CREATE TABLE SBI_OBJECTS (
       BIOBJ_ID             INTEGER DEFAULT nextval('SBI_OBJECTS_SEQ') NOT NULL,
       ENGINE_ID            INTEGER NOT NULL,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       ENCRYPT              SMALLINT NULL,
       PATH                 VARCHAR(400) NULL,
       REL_NAME             VARCHAR(400) NULL,
       STATE_ID             INTEGER NOT NULL,
       STATE_CD             VARCHAR(20) NOT NULL,
       BIOBJ_TYPE_CD        VARCHAR(20) NOT NULL,
       BIOBJ_TYPE_ID        INTEGER NOT NULL,
       SCHED_FL             SMALLINT NULL,
       EXEC_MODE_ID         INTEGER NULL,
       STATE_CONS_ID        INTEGER NULL,
       EXEC_MODE_CD         VARCHAR(20) NULL,
       STATE_CONS_CD        VARCHAR(20) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       VISIBLE              SMALLINT NOT NULL,
       UUID                 VARCHAR(40) NOT NULL,
       CONSTRAINT XPKSBI_OBJECTS 
              PRIMARY KEY (BIOBJ_ID)
);

CREATE UNIQUE INDEX XAK1SBI_OBJECTS ON SBI_OBJECTS
(
       LABEL                      
);


CREATE TABLE SBI_PARAMETERS (
       PAR_ID               INTEGER DEFAULT nextval('SBI_PARAMETERS_SEQ') NOT NULL,
       DESCR                VARCHAR(160) NULL,
       LENGTH               SMALLINT NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       PAR_TYPE_CD          VARCHAR(20) NOT NULL,
       MASK                 VARCHAR(20) NULL,
       PAR_TYPE_ID          INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       FUNCTIONAL_FLAG		SMALLINT NOT NULL DEFAULT 1,
       CONSTRAINT XPKSBI_PARAMETERS 
              PRIMARY KEY (PAR_ID)
);

CREATE UNIQUE INDEX XAK1SBI_PARAMETERS ON SBI_PARAMETERS
(
       LABEL                      
);


CREATE TABLE SBI_PARUSE (
       USE_ID               INTEGER DEFAULT nextval('SBI_PARUSE_SEQ') NOT NULL,
       LOV_ID               INTEGER NULL,
       LABEL                VARCHAR(20) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       PAR_ID               INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       MAN_IN               INTEGER NOT NULL,
       SELECTION_TYPE  VARCHAR(20) DEFAULT 'LIST',
       MULTIVALUE_FLAG  INTEGER DEFAULT 0,
       CONSTRAINT XPKSBI_PARUSE 
              PRIMARY KEY (USE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_PARUSE ON SBI_PARUSE
(
       PAR_ID                     ,
       LABEL                      
);


CREATE TABLE SBI_PARUSE_CK (
       CHECK_ID             INTEGER NOT NULL,
       USE_ID               INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       CONSTRAINT XPKSBI_PARUSE_CK 
              PRIMARY KEY (USE_ID, CHECK_ID)
);


CREATE TABLE SBI_PARUSE_DET (
       EXT_ROLE_ID          INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       USE_ID               INTEGER NOT NULL,
       HIDDEN_FL            SMALLINT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       CONSTRAINT XPKSBI_PARUSE_DET 
              PRIMARY KEY (USE_ID, EXT_ROLE_ID)
);


CREATE TABLE SBI_SUBREPORTS (
       MASTER_RPT_ID        INTEGER NOT NULL ,
       SUB_RPT_ID           INTEGER NOT NULL,
       CONSTRAINT XPKSBI_SUBREPORTS 
              PRIMARY KEY (MASTER_RPT_ID, SUB_RPT_ID)
);


CREATE TABLE SBI_OBJ_PARUSE (
       OBJ_PAR_ID           INTEGER NOT NULL,
       USE_ID               INTEGER NOT NULL,
       OBJ_PAR_FATHER_ID    INTEGER NOT NULL,
       FILTER_OPERATION     VARCHAR(20) NOT NULL,
       PROG 				INTEGER NOT NULL,
       FILTER_COLUMN        VARCHAR(30) NOT NULL,
       PRE_CONDITION 		VARCHAR(10),
       POST_CONDITION 		VARCHAR(10),
       LOGIC_OPERATOR 		VARCHAR(10),
       CONSTRAINT XPKSBI_OBJ_PARUSE 
              PRIMARY KEY (OBJ_PAR_ID,USE_ID,OBJ_PAR_FATHER_ID,FILTER_OPERATION)
);


CREATE TABLE SBI_EVENTS (
       ID                   INTEGER NOT NULL,
       USER_EVENT           VARCHAR(40) NOT NULL,
       CONSTRAINT XPKSBI_EVENTS 
              PRIMARY KEY (ID)
);


CREATE TABLE SBI_EVENTS_LOG (
       ID                   INTEGER DEFAULT nextval('SBI_EVENTS_LOG_SEQ') NOT NULL,
       USER_EVENT           VARCHAR(40) NOT NULL,
       EVENT_DATE           TIMESTAMP DEFAULT current_timestamp NOT NULL,
       DESCR                VARCHAR(1000) NOT NULL,
       PARAMS               VARCHAR(1000),
       HANDLER 	VARCHAR(400) NOT NULL DEFAULT 'it.eng.spagobi.events.handlers.DefaultEventPresentationHandler',
       CONSTRAINT XPKSBI_EVENTS_LOG 
              PRIMARY KEY (ID)
);



CREATE TABLE SBI_EVENTS_ROLES (
       EVENT_ID            INTEGER NOT NULL,
       ROLE_ID             INTEGER NOT NULL,
       CONSTRAINT XPKSBI_EVENTS_ROLES 
              PRIMARY KEY (EVENT_ID,ROLE_ID)
);


CREATE TABLE SBI_AUDIT ( 
		ID 					INTEGER DEFAULT nextval('SBI_AUDIT_SEQ') NOT NULL,
		USERNAME 			VARCHAR(40) NOT NULL,
		USERGROUP 			VARCHAR(100),
		DOC_REF 			INTEGER,
		DOC_ID 				INTEGER,
		DOC_LABEL 			VARCHAR(20) NOT NULL,
		DOC_NAME 			VARCHAR(40) NOT NULL,
		DOC_TYPE 			VARCHAR(20) NOT NULL,
		DOC_STATE 			VARCHAR(20) NOT NULL,
		DOC_PARAMETERS 		VARCHAR(400),
		ENGINE_REF 			INTEGER,
		ENGINE_ID 			INTEGER,
		ENGINE_LABEL 		VARCHAR(20) NOT NULL,
		ENGINE_NAME 		VARCHAR(40) NOT NULL,
		ENGINE_TYPE 		VARCHAR(20) NOT NULL,
		ENGINE_URL 			VARCHAR(400),
		ENGINE_DRIVER 		VARCHAR(400),
		ENGINE_CLASS 		VARCHAR(400),
		REQUEST_TIME 		TIMESTAMP NOT NULL,
		EXECUTION_START 	TIMESTAMP,
		EXECUTION_END 		TIMESTAMP,
		EXECUTION_TIME 		INTEGER,
		EXECUTION_STATE 	VARCHAR(20),
		ERROR				SMALLINT,
		ERROR_MESSAGE 		VARCHAR(400),
		ERROR_CODE 			VARCHAR(20),
		EXECUTION_MODALITY 	VARCHAR(40),
		CONSTRAINT XPKSBI_AUDIT 
              PRIMARY KEY (ID)
);
	
CREATE TABLE SBI_GEO_MAPS (
       MAP_ID               INTEGER DEFAULT nextval('SBI_MAPS_SEQ') NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       URL					VARCHAR(400) NOT NULL,
       FORMAT 				VARCHAR(40) NULL,
       CONSTRAINT XPKSBI_GEO_MAPS 
              PRIMARY KEY (MAP_ID)
);

CREATE UNIQUE INDEX XAK1SBI_GEO_MAPS ON SBI_GEO_MAPS
(
       NAME
);	

CREATE TABLE SBI_GEO_FEATURES (
       FEATURE_ID           INTEGER DEFAULT nextval('SBI_FEATURES_SEQ') NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       TYPE					VARCHAR(40)  NULL,
       CONSTRAINT XPKSBI_GEO_FEATURES 
              PRIMARY KEY (FEATURE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_GEO_FEATURES ON SBI_GEO_FEATURES
(
       NAME
);	

CREATE TABLE SBI_GEO_MAP_FEATURES (
       MAP_ID            INTEGER NOT NULL,
       FEATURE_ID        INTEGER NOT NULL,
       SVG_GROUP         VARCHAR(40),
       VISIBLE_FLAG		 VARCHAR(1),
       CONSTRAINT XPKGEO_MAP_FEATURES
              PRIMARY KEY (MAP_ID,FEATURE_ID)
);		

CREATE TABLE SBI_VIEWPOINTS (
		VP_ID 				INTEGER DEFAULT nextval('SBI_VIEWPOINTS_SEQ') NOT NULL,
		BIOBJ_ID 			INTEGER NOT NULL, 
		VP_NAME 			VARCHAR(40) NOT NULL,
		VP_OWNER 			VARCHAR(40),
		VP_DESC 			VARCHAR(160),
		VP_SCOPE 			VARCHAR (20) NOT NULL, 
		VP_VALUE_PARAMS 	VARCHAR(400), 
		VP_CREATION_DATE 	DATE NOT NULL,
       CONSTRAINT XPKSBI_VIEWPOINTS 
              PRIMARY KEY (VP_ID)
);

CREATE UNIQUE INDEX XAK1SBI_VIEWPOINTS ON SBI_VIEWPOINTS
(
       VP_NAME
);

ALTER TABLE SBI_AUDIT
       ADD CONSTRAINT FK_sbi_audit_1 
              FOREIGN KEY (DOC_REF)
                             REFERENCES SBI_OBJECTS ON DELETE SET NULL ;
                             
ALTER TABLE SBI_AUDIT
       ADD CONSTRAINT FK_sbi_audit_2 
              FOREIGN KEY (ENGINE_REF)
                             REFERENCES SBI_ENGINES ON DELETE SET NULL ;


ALTER TABLE SBI_CHECKS
       ADD  CONSTRAINT FK_sbi_checks_1
              FOREIGN KEY (VALUE_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_EXT_ROLES
       ADD  CONSTRAINT FK_sbi_ext_roles_1
              FOREIGN KEY (ROLE_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_FUNC_ROLE
       ADD  CONSTRAINT FK_sbi_func_role_2
              FOREIGN KEY (STATE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_FUNC_ROLE
       ADD  CONSTRAINT FK_sbi_func_role_3
              FOREIGN KEY (FUNCT_ID)
                             REFERENCES SBI_FUNCTIONS ;


ALTER TABLE SBI_FUNC_ROLE
       ADD  CONSTRAINT FK_sbi_func_role_1
              FOREIGN KEY (ROLE_ID)
                             REFERENCES SBI_EXT_ROLES  ;


ALTER TABLE SBI_FUNCTIONS
       ADD  CONSTRAINT FK_sbi_functions_2
              FOREIGN KEY (PARENT_FUNCT_ID)
                             REFERENCES SBI_FUNCTIONS  ;


ALTER TABLE SBI_FUNCTIONS
       ADD  CONSTRAINT FK_sbi_functions_1
              FOREIGN KEY (FUNCT_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_LOV
       ADD  CONSTRAINT FK_sbi_lov_1
              FOREIGN KEY (INPUT_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJ_FUNC
       ADD  CONSTRAINT FK_sbi_obj_func_1
              FOREIGN KEY (FUNCT_ID)
                             REFERENCES SBI_FUNCTIONS  ;


ALTER TABLE SBI_OBJ_FUNC
       ADD  CONSTRAINT FK_sbi_obj_func_2
              FOREIGN KEY (BIOBJ_ID)
                             REFERENCES SBI_OBJECTS  ;


ALTER TABLE SBI_OBJ_PAR
       ADD  CONSTRAINT FK_sbi_obj_par_1
              FOREIGN KEY (BIOBJ_ID)
                             REFERENCES SBI_OBJECTS  ;


ALTER TABLE SBI_OBJ_PAR
       ADD  CONSTRAINT FK_sbi_obj_par_2
              FOREIGN KEY (PAR_ID)
                             REFERENCES SBI_PARAMETERS  ;


ALTER TABLE SBI_OBJ_STATE
       ADD  CONSTRAINT FK_sbi_obj_state_2
              FOREIGN KEY (STATE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJ_STATE
       ADD  CONSTRAINT FK_sbi_obj_state_1
              FOREIGN KEY (BIOBJ_ID)
                             REFERENCES SBI_OBJECTS  ;


ALTER TABLE SBI_OBJECTS
       ADD  CONSTRAINT FK_sbi_objects_1
              FOREIGN KEY (STATE_CONS_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJECTS
       ADD  CONSTRAINT FK_sbi_objects_4
              FOREIGN KEY (EXEC_MODE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJECTS
       ADD  CONSTRAINT FK_sbi_objects_3
              FOREIGN KEY (BIOBJ_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJECTS
       ADD  CONSTRAINT FK_sbi_objects_2
              FOREIGN KEY (STATE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_OBJECTS
       ADD  CONSTRAINT FK_sbi_objects_5
              FOREIGN KEY (ENGINE_ID)
                             REFERENCES SBI_ENGINES  ;


ALTER TABLE SBI_PARAMETERS
       ADD  CONSTRAINT FK_sbi_parameters_1
              FOREIGN KEY (PAR_TYPE_ID)
                             REFERENCES SBI_DOMAINS  ;


ALTER TABLE SBI_PARUSE
       ADD  CONSTRAINT FK_sbi_paruse_2
              FOREIGN KEY (LOV_ID)
                             REFERENCES SBI_LOV  ;


ALTER TABLE SBI_PARUSE
       ADD  CONSTRAINT FK_sbi_paruse_1
              FOREIGN KEY (PAR_ID)
                             REFERENCES SBI_PARAMETERS  ;


ALTER TABLE SBI_PARUSE_CK
       ADD  CONSTRAINT FK_sbi_paruse_ck_2
              FOREIGN KEY (CHECK_ID)
                             REFERENCES SBI_CHECKS  ;


ALTER TABLE SBI_PARUSE_CK
       ADD  CONSTRAINT FK_sbi_paruse_ck_1
              FOREIGN KEY (USE_ID)
                             REFERENCES SBI_PARUSE  ;


ALTER TABLE SBI_PARUSE_DET
       ADD  CONSTRAINT FK_sbi_paruse_det_1
              FOREIGN KEY (USE_ID)
                             REFERENCES SBI_PARUSE  ;


ALTER TABLE SBI_PARUSE_DET
       ADD  CONSTRAINT FK_sbi_paruse_det_2
              FOREIGN KEY (EXT_ROLE_ID)
                             REFERENCES SBI_EXT_ROLES ;


ALTER TABLE SBI_OBJ_PARUSE
       ADD  CONSTRAINT FK_sbi_obj_paruse_1
              FOREIGN KEY (OBJ_PAR_ID)
                             REFERENCES SBI_OBJ_PAR ;


ALTER TABLE SBI_OBJ_PARUSE
       ADD  CONSTRAINT FK_sbi_obj_paruse_2
              FOREIGN KEY (USE_ID)
                             REFERENCES SBI_PARUSE ;                             
  

ALTER TABLE SBI_OBJ_PARUSE
       ADD  CONSTRAINT FK_sbi_obj_paruse_3
              FOREIGN KEY (OBJ_PAR_FATHER_ID)
                             REFERENCES SBI_OBJ_PAR ;      
                             

ALTER TABLE SBI_ENGINES
       ADD  CONSTRAINT FK_sbi_engines_1
              FOREIGN KEY (BIOBJ_TYPE)
                             REFERENCES SBI_DOMAINS ;  

ALTER TABLE SBI_ENGINES
       ADD  CONSTRAINT FK_sbi_engines_2
              FOREIGN KEY (ENGINE_TYPE)
                             REFERENCES SBI_DOMAINS ;                       

ALTER TABLE SBI_EVENTS_ROLES
       ADD  CONSTRAINT FK_sbi_events_roles_1
              FOREIGN KEY (ROLE_ID)
                             REFERENCES SBI_EXT_ROLES ;
                             
ALTER TABLE SBI_EVENTS_ROLES
       ADD  CONSTRAINT FK_sbi_events_roles_2
              FOREIGN KEY (EVENT_ID)
                             REFERENCES SBI_EVENTS_LOG ;

ALTER TABLE SBI_SUBREPORTS
       ADD  CONSTRAINT FK_sbi_subreports_1
              FOREIGN KEY (MASTER_RPT_ID)
                             REFERENCES SBI_OBJECTS ;

ALTER TABLE SBI_SUBREPORTS
       ADD  CONSTRAINT FK_sbi_subreports_2
              FOREIGN KEY (SUB_RPT_ID)
                             REFERENCES SBI_OBJECTS ;
                             
ALTER TABLE SBI_GEO_MAP_FEATURES
       ADD CONSTRAINT FK_geo_map_features1
              FOREIGN KEY (MAP_ID)
                             REFERENCES SBI_GEO_MAPS; 
                             
ALTER TABLE SBI_GEO_MAP_FEATURES
       ADD CONSTRAINT FK_geo_map_features2
              FOREIGN KEY (FEATURE_ID)
                             REFERENCES SBI_GEO_FEATURES;                             
                             
ALTER TABLE SBI_VIEWPOINTS
       ADD  CONSTRAINT FK_sbi_viewpoints_1
              FOREIGN KEY (BIOBJ_ID)
                             REFERENCES SBI_OBJECTS  ;                                                          