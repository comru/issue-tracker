-- begin TRACKER_PROJECT
create table TRACKER_PROJECT (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME varchar(10) not null,
    NAME varchar(255) not null,
    CATEGORY_ID varchar(36),
    --
    primary key (ID)
)^
-- end TRACKER_PROJECT
-- begin TRACKER_ISSUE
create table TRACKER_ISSUE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PROJECT_ID varchar(36) not null,
    NUMBER_ bigint not null,
    SUMMARY varchar(1000) not null,
    DESCRIPTION longvarchar,
    ASSIGNEE_ID varchar(36) not null,
    CREATED_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end TRACKER_ISSUE
-- begin TRACKER_PROJECT_USER_LINK
create table TRACKER_PROJECT_USER_LINK (
    PROJECT_ID varchar(36) not null,
    USER_ID varchar(36) not null,
    primary key (PROJECT_ID, USER_ID)
)^
-- end TRACKER_PROJECT_USER_LINK
