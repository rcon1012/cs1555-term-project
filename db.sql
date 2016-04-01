-- Authors: Zach Gannon, Jon Povirk, Ryan Conley
-- Pitt ID: zjg7, jmp180, rgc11
-- Project: CS1555 - Milestone 1

-- File sets up the db schema

DROP TABLE Profiles CASCADE CONSTRAINTS;
DROP TABLE Friends CASCADE CONSTRAINTS;
DROP TABLE Groups CASCADE CONSTRAINTS;
DROP TABLE Members CASCADE CONSTRAINTS;
DROP TABLE Messages CASCADE CONSTRAINTS;

CREATE TABLE Profiles (
    user_id     NUMBER(10) PRIMARY KEY,
    fname       VARCHAR2(100),
    lname       VARCHAR2(100),
    email       VARCHAR2(100),
    dob         TIMESTAMP,
    last_on     TIMESTAMP
);

-- user_id: id of the person in question
-- frined_id: id of friend of person in question
-- status: flag to denote no relationship(0), pending(1) or established(2)
-- TODO: Add check for existing F(A, B) before adding F(B, A),
-- where F(uid, fid) = establish friendship of uid and fid. We want F to
-- be bilateral, which is accomplished by this check.
CREATE TABLE Friends (
    friend1_id  NUMBER(10) NOT NULL,
    friend2_id  NUMBER(10) NOT NULL,
    status      NUMBER(1) NOT NULL,
    established TIMESTAMP,
    CONSTRAINT Profile_UID_FK FOREIGN KEY (friend1_id) REFERENCES Profiles(user_id),
    CONSTRAINT Profile_FID_FK FOREIGN KEY (friend2_id) REFERENCES Profiles(user_id),
    CONSTRAINT Friends_PK PRIMARY KEY (friend1_id, friend2_id)
);

CREATE TABLE Groups (
    group_id    NUMBER(10) PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL,
    description VARCHAR2(1024),
    capacity    NUMBER(10) NOT NULL
);

CREATE TABLE Members (
    group_id    NUMBER(10),
    user_id     NUMBER(10),
    CONSTRAINT Members_PK PRIMARY KEY (group_id, user_id),
    CONSTRAINT Members_Groups_FK FOREIGN KEY (group_id) REFERENCES Groups(group_id),
    CONSTRAINT Members_Profiles_FK FOREIGN KEY (user_id) REFERENCES Profiles(user_id)
);

-- type: flag to denote sent to single user(1), or the whole group(2)
-- recip: single user or group id, depending upon type
CREATE TABLE Messages (
    msg_id      NUMBER(10) PRIMARY KEY,
    subject     VARCHAR2(100),
    sender_id   NUMBER(10) NOT NULL,
    recip_id    NUMBER(10) NOT NULL,
    time_sent   TIMESTAMP,
    conv_id     NUMBER(10) NOT NULL,
    msg_text    VARCHAR2(100),
    type        NUMBER(1),
    CONSTRAINT Messages_FK_Profiles FOREIGN KEY (sender_id) REFERENCES Profiles(user_id)
);
