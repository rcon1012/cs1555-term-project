-- Authors: Zach Gannon, Jon Povirk, Ryan Conley
-- Pitt ID: zjg7, jmp180, rgc11
-- Project: CS1555 - Milestone 1

-- File sets up the db schema

DROP TABLE Profiles CASCADE CONSTRAINTS;
DROP TABLE Friends CASCADE CONSTRAINTS;
DROP TABLE Groups CASCADE CONSTRAINTS;
DROP TABLE Members CASCADE CONSTRAINTS;
DROP TABLE Messages CASCADE CONSTRAINTS;


-- Table manages user profiles and metadata.
-- Assume user's first name, last name, and email are 100 characters or less and can be null.
--  user_id     PK, user's assigned id
--  fname       user's first name
--  lname       user's last name
--  email       user's email address
--  dob         user's date of birth
--  last_on     user's last log-on timestamp
CREATE TABLE Profiles (
    user_id     NUMBER(10) PRIMARY KEY,
    fname       VARCHAR2(100),
    lname       VARCHAR2(100),
    email       VARCHAR2(100),
    dob         TIMESTAMP,
    last_on     TIMESTAMP
);

-- Table manages friendship relationship between user profiles.
-- We assume users can befriend themselves.
--  friend1_id  PK(1, 0), Requester's assigned id
--  friend2_id  PK(0, 1), Reciever's assigned id
--  status      Flag denoting friendship pending (0) or established (1)
--  established Timestamp denoting the time of establishement
CREATE TABLE Friends (
    friend1_id  NUMBER(10) NOT NULL,
    friend2_id  NUMBER(10) NOT NULL,
    status      NUMBER(1) NOT NULL,
    established TIMESTAMP,
    CONSTRAINT Profile_UID_FK FOREIGN KEY (friend1_id) REFERENCES Profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT Profile_FID_FK FOREIGN KEY (friend2_id) REFERENCES Profiles(user_id) ON DELETE CASCADE,
    CONSTRAINT Friends_PK PRIMARY KEY (friend1_id, friend2_id),
    CONSTRAINT Friends_Status_Check CHECK(status BETWEEN 0 AND 1)
);


-- Table manages group existence and metadata.
--  group_id    PK, Group's assigned id
--  name        Group's name
--  description Group's description
--  capacity    Group's maximum number of allowed members
CREATE TABLE Groups (
    group_id    NUMBER(10) PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL,
    description VARCHAR2(1024),
    capacity    NUMBER(10) NOT NULL
);

-- Table manages group membership of user profiles.
--  group_id    PK(1, 0), Group id to which the member belongs
--  user_id     PK(0, 1), USer id of the member
CREATE TABLE Members (
    group_id    NUMBER(10),
    user_id     NUMBER(10),
    CONSTRAINT Members_PK PRIMARY KEY (group_id, user_id),
    CONSTRAINT Members_Groups_FK FOREIGN KEY (group_id) REFERENCES Groups(group_id) ON DELETE CASCADE,
    CONSTRAINT Members_Profiles_FK FOREIGN KEY (user_id) REFERENCES Profiles(user_id) ON DELETE CASCADE
);

-- Table manages messages sent from users to other users or entire groups.
-- Here we assume all messages will be 100 chars or less, as described by the 
-- milestone description, therefore no trigger or check exists for such test.
-- Also assume messages will be kept in the database after user or group deletion
--  msg_id      Message's assigned id
--  subject     Message's subject
--  sender_id   Sending user's assigned id
--  recip_id    Recieving user or group assinged id, depends on type
--  time_sent   Message's timestamp of being sent
--  msg_text    Message's body of text
--  type        Flag to denote sent to single user(1), or the whole group(2)
CREATE TABLE Messages (
    msg_id      NUMBER(10) PRIMARY KEY,
    subject     VARCHAR2(100),
    sender_id   NUMBER(10) NOT NULL,
    recip_id    NUMBER(10) NOT NULL,
    time_sent   TIMESTAMP NOT NULL,
    msg_text    VARCHAR2(100),
    type        NUMBER(1) NOT NULL,
    CONSTRAINT Messages_FK_Profiles FOREIGN KEY (sender_id) REFERENCES Profiles(user_id),
    CONSTRAINT Messages_Type_Check CHECK (type BETWEEN 1 AND 2)
);

-- NOTE: the triggers below are subject to change come the next milestone for the project

-- Trigger to ensure users can only send messages to groups they belong to
CREATE OR REPLACE TRIGGER Membership_Trigger
BEFORE INSERT OR UPDATE ON Messages
FOR EACH ROW
WHEN (NEW.type = 2)
DECLARE
    cnt NUMBER;
BEGIN
    SELECT COUNT(Members.user_id)
    INTO cnt
    FROM Members
    WHERE Members.user_id = :NEW.sender_id AND Members.group_id = :NEW.recip_id;

    IF (cnt < 1) THEN
        RAISE_APPLICATION_ERROR( -20001, 'Not a member of the group!' );
    END IF;
END;
/

-- Trigger to ensure messages are sent to valid users
CREATE OR REPLACE TRIGGER User_Message_Trigger
BEFORE INSERT OR UPDATE ON Messages
FOR EACH ROW
WHEN (NEW.type = 1)
DECLARE
    cnt NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO cnt
    FROM Profiles
    WHERE Profiles.user_id = :NEW.recip_id;

    IF (cnt < 1) THEN
        RAISE_APPLICATION_ERROR( -20002, 'User does not exist' );
    END IF;
END;
/

-- Trigger to ensure groups do not exceed their capacity with membership
CREATE OR REPLACE TRIGGER Group_Capacity_Trigger
BEFORE INSERT OR UPDATE ON Members
FOR EACH ROW
DECLARE
    cnt NUMBER;
    cap NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO cnt
    FROM Members
    WHERE Members.group_id = :NEW.group_id;

    SELECT capacity
    INTO cap
    FROM Groups
    WHERE Groups.group_id = :NEW.group_id;

    IF (cnt > cap - 1) THEN
        RAISE_APPLICATION_ERROR( -20003, 'Group capacity met.' );
    END IF;
END;
/



