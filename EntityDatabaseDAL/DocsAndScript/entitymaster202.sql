DROP DATABASE IF EXISTS `entitydb`;
CREATE DATABASE `entitydb` /*!40100 DEFAULT CHARACTER SET latin1 */;

DROP TABLE IF EXISTS `entitydb`.`entitytype`;
CREATE TABLE  `entitydb`.`entitytype` (
  `EntityTypeID` int(11) NOT NULL,
  `EntityTypeDesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`EntityTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`entitybase`;
CREATE TABLE  `entitydb`.`entitybase` (
  `EntityID` varchar(40) NOT NULL,
  `EntityTypeID` int(11) DEFAULT NULL,
  `EntityAccessStatus` varchar(20) DEFAULT NULL,
  `EntityParentEdit` varchar(20) DEFAULT NULL,
  `OwnerID` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`entitymetadata`;
CREATE TABLE  `entitydb`.`entitymetadata` (
  `EntityID` varchar(40) NOT NULL,
  `MetadataID` int(11) NOT NULL,
  PRIMARY KEY (`EntityID`,`MetadataID`),
  CONSTRAINT `FK_EM_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`entityrelationship`;
DROP TABLE IF EXISTS `entitydb`.`entityrole`;
CREATE TABLE  `entitydb`.`entityrole` (
  `EntityRoleID` int(11) NOT NULL,
  `EntityRoleDesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`EntityRoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE  `entitydb`.`entityrelationship` (
  `ParentEntityID` varchar(40) NOT NULL,
  `ChildEntityID` varchar(40) NOT NULL,
  `EntityRoleID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ParentEntityID`,`ChildEntityID`),
  KEY `FK_ER_CHILE` (`ChildEntityID`),
  KEY `FK_ER_EROLEID` (`EntityRoleID`),
  CONSTRAINT `FK_ER_CHILE` FOREIGN KEY (`ChildEntityID`) REFERENCES `entitybase` (`EntityID`),
  CONSTRAINT `FK_ER_EROLEID` FOREIGN KEY (`EntityRoleID`) REFERENCES `entityrole` (`EntityRoleID`),
  CONSTRAINT `FK_ER_PARENT` FOREIGN KEY (`ParentEntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`entitytext`;
CREATE TABLE  `entitydb`.`entitytext` (
  `EntityID` varchar(40) NOT NULL,
  `EntityText` varchar(20) DEFAULT NULL,
  `EntityTextTypeID` int(11) NOT NULL,
  PRIMARY KEY (`EntityID`),
  CONSTRAINT `FK_ET_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`entitytexttype`;
CREATE TABLE  `entitydb`.`entitytexttype` (
  `EntityTextTypeID` int(11) NOT NULL,
  `EntityTextTypeDesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`EntityTextTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`event`;
CREATE TABLE  `entitydb`.`event` (
  `EntityID` varchar(40) NOT NULL,
  `EventStartDate` datetime NOT NULL,
  `EventEndDate` datetime DEFAULT NULL,
  PRIMARY KEY (`EntityID`),
  CONSTRAINT `FK_EVENT_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`group`;
CREATE TABLE  `entitydb`.`group` (
  `GroupID` int(11) NOT NULL,
  `GroupTypeID` int(11) NOT NULL,
  `GroupAccessStatus` varchar(20) DEFAULT NULL,
  `GroupUserPerm` varchar(20) DEFAULT NULL,
  `GroupGroupPerm` varchar(20) DEFAULT NULL,
  `GroupAllPerm` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`GroupID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`grouprolelist`;
CREATE TABLE  `entitydb`.`grouprolelist` (
  `GroupRoleID` int(11) NOT NULL,
  `RoleDesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`GroupRoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`grouproleperm`;
CREATE TABLE  `entitydb`.`grouproleperm` (
  `EntityTypeID` int(11) NOT NULL,
  `UserGroupRoleID` int(11) NOT NULL,
  `UserRolePerm` varchar(20) DEFAULT NULL,
  `UserRolePermAllow` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EntityTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`grouptype`;
CREATE TABLE  `entitydb`.`grouptype` (
  `GroupTypeID` int(11) NOT NULL,
  `GroupTypeDesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`GroupTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`location`;
CREATE TABLE  `entitydb`.`location` (
  `EntityID` varchar(40) NOT NULL,
  `LocationName` varchar(50) DEFAULT NULL,
  `LocationStreetAddress` varchar(50) DEFAULT NULL,
  `LocationZipCode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`EntityID`),
  CONSTRAINT `FK_LOCATION_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`people`;
CREATE TABLE  `entitydb`.`people` (
  `EntityID` varchar(40) NOT NULL,
  `PeopleFirstName` varchar(50) DEFAULT NULL,
  `PeopleLastName` varchar(50) DEFAULT NULL,
  `PeopleEmail` varchar(100) DEFAULT NULL,
  `PeoplePhone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EntityID`),
  CONSTRAINT `FK_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`systemevent`;
CREATE TABLE  `entitydb`.`systemevent` (
  `SystemEventID` int(11) NOT NULL,
  `SystemEventDesc` varchar(100) DEFAULT NULL,
  `ResourceID` int(11) DEFAULT NULL,
  `SystemEventDate` date DEFAULT NULL,
  `SystemEventTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SystemEventCompleted` char(1) DEFAULT 'N',
  PRIMARY KEY (`SystemEventID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`users`;
CREATE TABLE  `entitydb`.`users` (
  `EntityID` varchar(40) NOT NULL,
  `UserEmail` varchar(100) DEFAULT NULL,
  `UserSalt` varchar(20) DEFAULT NULL,
  `UserPasswordHash` varchar(64) DEFAULT NULL,
  `UserOauthToken` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EntityID`),
  CONSTRAINT `FK_USERENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `people` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`systemlog`;
CREATE TABLE  `entitydb`.`systemlog` (
  `SystemLogID` int(11) NOT NULL,
  `UserID` varchar(40) DEFAULT NULL,
  `SystemLogAction` varchar(20) DEFAULT NULL,
  `SystemLogActionDate` date DEFAULT NULL,
  `EntityID` varchar(40) DEFAULT NULL,
  `SystemLogMisc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`SystemLogID`),
  KEY `FK_SL_ENTITYID` (`EntityID`),
  KEY `FK_SL_USERID` (`UserID`),
  CONSTRAINT `FK_SL_ENTITYID` FOREIGN KEY (`EntityID`) REFERENCES `entitybase` (`EntityID`),
  CONSTRAINT `FK_SL_USERID` FOREIGN KEY (`UserID`) REFERENCES `users` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `entitydb`.`systemperms`;
CREATE TABLE  `entitydb`.`systemperms` (
  `EntityTypeID` int(11) NOT NULL,
  `SystemPermsOwner` varchar(20) DEFAULT NULL,
  `SystemPermsGroup` varchar(20) DEFAULT NULL,
  `SystemPermsOthers` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EntityTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `entitydb`.`usergroupmembership`;
CREATE TABLE  `entitydb`.`usergroupmembership` (
  `UserID` varchar(40) NOT NULL,
  `GroupID` int(11) NOT NULL,
  `UserGroupRoleID` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserID`,`GroupID`),
  KEY `FK_UGM_GROUPID` (`GroupID`),
  CONSTRAINT `FK_UGM_GROUPID` FOREIGN KEY (`GroupID`) REFERENCES `group` (`GroupID`),
  CONSTRAINT `FK_UGM_USERID` FOREIGN KEY (`UserID`) REFERENCES `users` (`EntityID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;