CREATE TABLE STUDY
(
    id            VARCHAR primary key,
    name          VARCHAR,
    interest_tags VARCHAR,
    introduce     VARCHAR
);

CREATE TABLE CREW
(
    id        VARCHAR primary key,
    study_id  VARCHAR,
    user_id   VARCHAR,
    authority VARCHAR
);

CREATE TABLE ATTEND
(
    id       VARCHAR primary key,
    study_id VARCHAR,
    user_id  VARCHAR,
    state    VARCHAR
);