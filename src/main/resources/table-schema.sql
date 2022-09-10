CREATE TABLE STUDY
(
    id            BIGINT primary key,
    name          VARCHAR,
    interest_tags VARCHAR,
    introduce     VARCHAR
);

CREATE TABLE STUDY_MEMBER
(
    id           BIGINT primary key,
    study_id     BIGINT,
    user_id       VARCHAR,
    authority    VARCHAR
);

insert into STUDY (id, name, interest_tags, introduce)
values (1, 'test-1', 'java', '헬로월드');

insert into STUDY_MEMBER (id, study_id, user_id, authority)
values (1, 1, 'user1', 'OWNER');