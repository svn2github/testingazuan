CREATE SEQUENCE SBI_CHECKS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_DOMAINS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_ENGINES_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_EXT_ROLES_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_FUNCTIONS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_LOV_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_OBJECTS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_PARAMETERS_SEQ INCREMENT 1 START 1 ;
CREATE SEQUENCE SBI_PARUSE_SEQ INCREMENT 1 START 1 ;

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
       MAIN_URL             VARCHAR(400) NOT NULL,
       SECN_URL             VARCHAR(400) NULL,
       OBJ_UPL_DIR          VARCHAR(400) NULL,
       OBJ_USE_DIR          VARCHAR(400) NULL,
       DRIVER_NM            VARCHAR(400) NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       CONSTRAINT XPKSBI_ENGINES 
              PRIMARY KEY (ENGINE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_ENGINES ON SBI_ENGINES
(
       LABEL                          
);


CREATE TABLE SBI_EXT_ROLES (
       EXT_ROLE_ID          INTEGER DEFAULT nextval('SBI_EXT_ROLES_SEQ') NOT NULL,
       NAME                 VARCHAR(40) NULL,
       DESCR                VARCHAR(160) NULL,
       CODE                 VARCHAR(20) NULL,
       ROLE_TYPE_CD         VARCHAR(20) NOT NULL,
       ROLE_TYPE_ID         INTEGER NOT NULL,
       CONSTRAINT XPKSBI_EXT_ROLES 
              PRIMARY KEY (EXT_ROLE_ID)
);

CREATE UNIQUE INDEX XAK1SBI_EXT_ROLES ON SBI_EXT_ROLES
(
       CODE                       
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
       LOV_PROVIDER         VARCHAR(2000) NULL,
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
       PAR_ID               INTEGER NOT NULL,
       BIOBJ_ID             INTEGER NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       REQ_FL               SMALLINT NULL,
       MOD_FL               SMALLINT NULL,
       VIEW_FL              SMALLINT NULL,
       MULT_FL              SMALLINT NULL,
       PROG                 INTEGER NOT NULL,
       PARURL_NM            VARCHAR(20) NULL,
       CONSTRAINT XPKSBI_OBJ_PAR 
              PRIMARY KEY (BIOBJ_ID, PAR_ID, PROG)
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
       CONSTRAINT XPKSBI_PARAMETERS 
              PRIMARY KEY (PAR_ID)
);

CREATE UNIQUE INDEX XAK1SBI_PARAMETERS ON SBI_PARAMETERS
(
       LABEL                      
);


CREATE TABLE SBI_PARUSE (
       USE_ID               INTEGER DEFAULT nextval('SBI_PARUSE_SEQ') NOT NULL,
       LOV_ID               INTEGER NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       PAR_ID               INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
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

