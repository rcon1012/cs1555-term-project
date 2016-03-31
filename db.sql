-- File sets up the db schema

DROP TABLE Profiles CASCADE CONSTRAINTS;
DROP TABLE Friends CASCADE CONSTRAINTS;
DROP TABLE Groups CASCADE CONSTRAINTS;
DROP TABLE Members CASCADE CONSTRAINTS;
DROP TABLE Messages CASCADE CONSTRAINTS;
DROP TABLE Conversations CASCADE CONSTRAINTS;
DROP TABLE Recipients CASCADE CONSTRAINTS;

CREATE TABLE Profiles (
    user_ID     NUMBER(10) PRIMARY KEY,
    fname       VARCHAR2(100),
    lname       VARCHAR2(100),
    email       VARCHAR2(100),
    dob         TIMESTAMP,
    last_on     TIMESTAMP
);

-- user_id: id of the person in question
-- frined_id: id of friend of person in question
-- status: flag to denote pending(0) or established(1)
CREATE TABLE Friends (
    user_id     NUMBER(10) NOT NULL,
    friend_id   NUMBER(10) NOT NULL,
    status      NUMBER(1) NOT NULL,
    established TIMESTAMP,
    CONSTRAINT Profile_UID_FK FOREIGN KEY (user_id) REFERENCES Profiles(user_id),
    CONSTRAINT Profile_FID_FK FOREIGN KEY (friend_id) REFERENCES Profiles(user_id),
    CONSTRAINT Friends_PK PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE Groups (
    group_id    NUMBER(10) PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL,
    description VARCHAR2(1024),
    capacity    NUMBER(10)
);

CREATE TABLE Members (
    group_id    NUMBER(10) NOT NULL,
    user_id     NUMBER(10) NOT NULL,
    CONSTRAINT Members_PK PRIMARY KEY (group_id, user_id)
);

CREATE TABLE Conversations (
    conv_id     NUMBER(10) NOT NULL,
    user_id   NUMBER(10) NOT NULL,
    CONSTRAINT Conversations_PK PRIMARY KEY (conv_id, user_id)
);

-- TODO: Add check for 100 length limit on msg_text
CREATE TABLE Messages (
    msg_id      NUMBER(10) PRIMARY KEY,
    subject     VARCHAR2(100),
    sender_id   NUMBER(10) NOT NULL,
    time_sent   TIMESTAMP,
    time_read   TIMESTAMP,
    conv_id     NUMBER(10) NOT NULL,
    msg_text    VARCHAR2(1024),
    spam        NUMBER(1),
    CONSTRAINT Messages_FK_Profiles FOREIGN KEY (sender_id) REFERENCES Profiles(user_id),
    CONSTRAINT Messages_FK_Conversations FOREIGN KEY (conv_id, sender_id) REFERENCES Conversations(conv_id, user_id)
);

CREATE TABLE Recipients (
    msg_id      NUMBER(10) NOT NULL,
    user_id     NUMBER(10) NOT NULL,
    CONSTRAINT Recipients_PK PRIMARY KEY (msg_id, user_id),
    CONSTRAINT Recipients_FK_Profiles FOREIGN KEY (user_id) REFERENCES Profiles(user_id),
    CONSTRAINT Recipients_FK_Messages FOREIGN KEY (msg_id) REFERENCES Messages(msg_id)
);
