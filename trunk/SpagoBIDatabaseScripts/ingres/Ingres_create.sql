CREATE SEQUENCE CHECK_ID_SEQ;
CREATE TABLE SBI_CHECKS (
       CHECK_ID               INTEGER       NOT NULL with default next value for  CHECK_ID_SEQ,      
       DESCR                  VARCHAR(160)  NULL,
       LABEL                  VARCHAR(20)   NOT NULL,
       VALUE_TYPE_CD          VARCHAR(20)   NOT NULL,
       VALUE_TYPE_ID          INTEGER       NOT NULL,
       VALUE_1                VARCHAR(400)  NULL,
       VALUE_2                VARCHAR(400)  NULL,
       NAME                   VARCHAR(40)   NOT NULL,
       UNIQUE (LABEL),
       PRIMARY KEY (CHECK_ID)
);

CREATE SEQUENCE SBI_DOMAINS_SEQ START WITH 200;
CREATE TABLE SBI_DOMAINS (
       VALUE_ID             INTEGER    NOT NULL with default next value for  SBI_DOMAINS_SEQ,
       VALUE_CD             VARCHAR(20) NOT NULL,
       VALUE_NM             VARCHAR(40) NULL,
       DOMAIN_CD            VARCHAR(20) NOT NULL,
       DOMAIN_NM            VARCHAR(40) NULL,
       VALUE_DS             VARCHAR(160) NULL,
       UNIQUE (VALUE_CD, DOMAIN_CD),
       PRIMARY KEY (VALUE_ID)
);

CREATE SEQUENCE SBI_ENGINES_SEQ;
CREATE TABLE SBI_ENGINES (
       ENGINE_ID            INTEGER    NOT NULL with default next value for  SBI_ENGINES_SEQ,
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
       UNIQUE (LABEL),
       PRIMARY KEY (ENGINE_ID)
);

CREATE SEQUENCE SBI_EXT_ROLES_SEQ;
CREATE TABLE SBI_EXT_ROLES (
       EXT_ROLE_ID          INTEGER    NOT NULL with default next value for  SBI_EXT_ROLES_SEQ,
       NAME                 VARCHAR(100) NULL,
       DESCR                VARCHAR(160) NULL,
       CODE                 VARCHAR(20) NULL,
       ROLE_TYPE_CD         VARCHAR(20) NOT NULL,
       ROLE_TYPE_ID         INTEGER NOT NULL,       
       PRIMARY KEY (EXT_ROLE_ID)
);

CREATE TABLE SBI_FUNC_ROLE (
       ROLE_ID              INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       STATE_CD             VARCHAR(20) NULL,
       STATE_ID             INTEGER NOT NULL,
       PRIMARY KEY (FUNCT_ID, STATE_ID, ROLE_ID)
);

CREATE SEQUENCE SBI_FUNCTIONS_SEQ;
CREATE TABLE SBI_FUNCTIONS (
       FUNCT_ID             INTEGER NOT NULL with default next value for  SBI_FUNCTIONS_SEQ,
       FUNCT_TYPE_CD        VARCHAR(20) NOT NULL,
       PARENT_FUNCT_ID      INTEGER NULL,
       NAME                 VARCHAR(40) NULL,
       DESCR                VARCHAR(160) NULL,
       PATH                 VARCHAR(400) NULL,
       CODE                 VARCHAR(20) NOT NULL,
       PROG 				        INTEGER NOT NULL,
       FUNCT_TYPE_ID        INTEGER NOT NULL,
       UNIQUE (CODE),
       PRIMARY KEY (FUNCT_ID)
);

CREATE SEQUENCE SBI_LOV_SEQ;
CREATE TABLE SBI_LOV (
       LOV_ID               INTEGER NOT NULL with default next value for  SBI_LOV_SEQ,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       INPUT_TYPE_CD        VARCHAR(20) NOT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       LOV_PROVIDER         VARCHAR(4000) NULL,
       INPUT_TYPE_ID        INTEGER NOT NULL,
       PROFILE_ATTR         VARCHAR(20) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       UNIQUE (LABEL),
       PRIMARY KEY (LOV_ID)
);

CREATE TABLE SBI_OBJ_FUNC (
       BIOBJ_ID             INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       PRIMARY KEY (BIOBJ_ID, FUNCT_ID)
);

CREATE SEQUENCE SBI_OBJ_PAR_SEQ;
CREATE TABLE SBI_OBJ_PAR (
       OBJ_PAR_ID           INTEGER NOT NULL with default next value for  SBI_OBJ_PAR_SEQ,
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
       PRIMARY KEY (OBJ_PAR_ID)
);

CREATE TABLE SBI_OBJ_STATE (
       BIOBJ_ID             INTEGER NOT NULL,
       STATE_ID             INTEGER NOT NULL,
       END_DT               timestamp(3) NULL,
       START_DT             timestamp(3) NOT NULL,
       NOTE                 VARCHAR(300) NULL,
       PRIMARY KEY (BIOBJ_ID, STATE_ID, START_DT)
);

CREATE SEQUENCE SBI_OBJECTS_SEQ;
CREATE TABLE SBI_OBJECTS (
       BIOBJ_ID             INTEGER NOT NULL with default next value for  SBI_OBJECTS_SEQ,
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
       UNIQUE (LABEL),
       PRIMARY KEY (BIOBJ_ID)
);

CREATE SEQUENCE SBI_PARAMETERS_SEQ;
CREATE TABLE SBI_PARAMETERS (
       PAR_ID               INTEGER NOT NULL with default next value for  SBI_PARAMETERS_SEQ,
       DESCR                VARCHAR(160) NULL,
       LENGTH               SMALLINT NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       PAR_TYPE_CD          VARCHAR(20) NOT NULL,
       MASK                 VARCHAR(20) NULL,
       PAR_TYPE_ID          INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       FUNCTIONAL_FLAG		SMALLINT NOT NULL DEFAULT 1,
       UNIQUE (LABEL),
       PRIMARY KEY (PAR_ID)
);

CREATE SEQUENCE SBI_PARUSE_SEQ;
CREATE TABLE SBI_PARUSE (
       USE_ID               INTEGER NOT NULL with default next value for  SBI_PARUSE_SEQ,
       LOV_ID               INTEGER NULL,
       LABEL                VARCHAR(20) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       PAR_ID               INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       MAN_IN               INTEGER NOT NULL,
       SELECTION_TYPE  VARCHAR(20) DEFAULT 'LIST',
       MULTIVALUE_FLAG  INTEGER DEFAULT 0,
       UNIQUE (PAR_ID, LABEL),
       PRIMARY KEY (USE_ID)
);

CREATE TABLE SBI_PARUSE_CK (
       CHECK_ID             INTEGER NOT NULL,
       USE_ID               INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       PRIMARY KEY (USE_ID, CHECK_ID)
);


CREATE TABLE SBI_PARUSE_DET (
       EXT_ROLE_ID          INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       USE_ID               INTEGER NOT NULL,
       HIDDEN_FL            SMALLINT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       PRIMARY KEY (USE_ID, EXT_ROLE_ID)
);

CREATE TABLE SBI_SUBREPORTS (
       MASTER_RPT_ID        INTEGER NOT NULL,
       SUB_RPT_ID           INTEGER NOT NULL,
       PRIMARY KEY (MASTER_RPT_ID, SUB_RPT_ID)
);     

CREATE TABLE SBI_OBJ_PARUSE (
	OBJ_PAR_ID          INTEGER NOT NULL,
	USE_ID              INTEGER NOT NULL,
	OBJ_PAR_FATHER_ID   INTEGER NOT NULL,
	FILTER_OPERATION    VARCHAR(20) NOT NULL,
	PROG INTEGER NOT NULL,
	FILTER_COLUMN       VARCHAR(30) NOT NULL,
	PRE_CONDITION VARCHAR(10),
  POST_CONDITION VARCHAR(10),
  LOGIC_OPERATOR VARCHAR(10),
  PRIMARY KEY(OBJ_PAR_ID,USE_ID,OBJ_PAR_FATHER_ID,FILTER_OPERATION)
);      

CREATE SEQUENCE SBI_EVENTS_SEQ;
CREATE TABLE SBI_EVENTS (
	ID                  INTEGER NOT NULL with default next value for  SBI_EVENTS_SEQ,
  USER_EVENT          VARCHAR(40) NOT NULL,
  PRIMARY KEY(ID)
); 

CREATE SEQUENCE SBI_EVENTS_LOG_SEQ;
CREATE TABLE SBI_EVENTS_LOG (
	ID                   INTEGER NOT NULL with default next value for  SBI_EVENTS_LOG_SEQ,
	USER_EVENT          VARCHAR(40) NOT NULL,
	EVENT_DATE          TIMESTAMP(3)  NOT NULL,
	DESCR               VARCHAR(1000) NOT NULL,
	PARAMS              VARCHAR(1000),
	HANDLER 	          VARCHAR(400) NOT NULL DEFAULT 'it.eng.spagobi.events.handlers.DefaultEventPresentationHandler',
  PRIMARY KEY(ID)
);

CREATE TABLE SBI_EVENTS_ROLES (
       EVENT_ID            INTEGER NOT NULL,
       ROLE_ID             INTEGER NOT NULL,
       PRIMARY KEY (EVENT_ID, ROLE_ID)
);      

CREATE SEQUENCE SBI_AUDIT_SEQ;
CREATE TABLE SBI_AUDIT ( 
		ID 				  	  INTEGER NOT NULL with default next value for  SBI_AUDIT_SEQ,
		USERNAME 			  VARCHAR(40) NOT NULL,
		USERGROUP 			VARCHAR(100),
		DOC_REF 			  INTEGER,
		DOC_ID 				  INTEGER,
		DOC_LABEL 		  VARCHAR(20) NOT NULL,
		DOC_NAME 			  VARCHAR(40) NOT NULL,
		DOC_TYPE 			  VARCHAR(20) NOT NULL,
		DOC_STATE 		  VARCHAR(20) NOT NULL,
		DOC_PARAMETERS 	VARCHAR(400),
		ENGINE_REF 			INTEGER,
		ENGINE_ID 			INTEGER,
		ENGINE_LABEL 		VARCHAR(20) NOT NULL,
		ENGINE_NAME 		VARCHAR(40) NOT NULL,
		ENGINE_TYPE 		VARCHAR(20) NOT NULL,
		ENGINE_URL 			VARCHAR(400),
		ENGINE_DRIVER 	VARCHAR(400),
		ENGINE_CLASS 		VARCHAR(400),
		REQUEST_TIME 		TIMESTAMP NOT NULL,
		EXECUTION_START TIMESTAMP,
		EXECUTION_END 	TIMESTAMP,
		EXECUTION_TIME	INTEGER,
		EXECUTION_STATE VARCHAR(20),
		ERROR				    SMALLINT,
		ERROR_MESSAGE 	VARCHAR(400),
		ERROR_CODE 			VARCHAR(20),
		EXECUTION_MODALITY 	VARCHAR(20),
    PRIMARY KEY (ID)
);

CREATE SEQUENCE SBI_GEO_MAPS_SEQ;
CREATE TABLE SBI_GEO_MAPS (
       MAP_ID               INTEGER NOT NULL with default next value for  SBI_GEO_MAPS_SEQ,
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       URL					        VARCHAR(400) NOT NULL,
       FORMAT 				      VARCHAR(40) NULL,   
       UNIQUE (NAME),
       PRIMARY KEY (MAP_ID)
);

CREATE SEQUENCE SBI_GEO_FEATURES_SEQ;
CREATE TABLE SBI_GEO_FEATURES (
       FEATURE_ID           INTEGER NOT NULL with default next value for  SBI_GEO_FEATURES_SEQ, 
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       TYPE					        VARCHAR(40)  NULL,
       UNIQUE (NAME),
       PRIMARY KEY (FEATURE_ID)
);  

CREATE TABLE SBI_GEO_MAP_FEATURES (
       MAP_ID             INTEGER NOT NULL,
       FEATURE_ID         INTEGER NOT NULL,
       SVG_GROUP          VARCHAR(40),
       VISIBLE_FLAG		    VARCHAR(1),
      PRIMARY KEY (MAP_ID, FEATURE_ID)
);

CREATE SEQUENCE SBI_VIEWPOINTS_SEQ;
CREATE TABLE SBI_VIEWPOINTS (
		VP_ID 				INTEGER NOT NULL with default next value for  SBI_VIEWPOINTS_SEQ,     
		BIOBJ_ID 			INTEGER NOT NULL, 
		VP_NAME 			VARCHAR(40) NOT NULL,
	  VP_OWNER 			VARCHAR(40),
		VP_DESC 			VARCHAR(160),
		VP_SCOPE 			VARCHAR (20) NOT NULL, 
		VP_VALUE_PARAMS 	VARCHAR(400), 
		VP_CREATION_DATE 	TIMESTAMP  NOT NULL,
    UNIQUE (VP_NAME),
    PRIMARY KEY (VP_ID)
);
