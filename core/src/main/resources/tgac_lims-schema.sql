-- MySQL dump 10.11
--
-- Host: localhost    Database: lims_dev
-- ------------------------------------------------------
-- Server version	5.0.77

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Experiment`
--

DROP TABLE IF EXISTS `Experiment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Experiment` (
  `experimentId` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `accession` varchar(50) default NULL,
  `title` varchar(255) NOT NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  `DTYPE` varchar(50) NOT NULL,
  `study_studyId` bigint(20) default NULL,
  PRIMARY KEY  (`experimentId`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Experiment_Run`
--

DROP TABLE IF EXISTS `Experiment_Run`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Experiment_Run` (
  `Experiment_experimentId` bigint(20) NOT NULL,
  `runs_runId` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Experiment_Sample`
--

DROP TABLE IF EXISTS `Experiment_Sample`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Experiment_Sample` (
  `Experiment_experimentId` bigint(20) NOT NULL,
  `samples_sampleId` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Group` (
  `groupId` bigint(20) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`groupId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Lane`
--

DROP TABLE IF EXISTS `Lane`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Lane` (
  `laneId` bigint(20) NOT NULL auto_increment,
  `laneNumber` tinyint(4) NOT NULL,
  `sampleId` varchar(50) default NULL,
  `concentration` double default NULL,
  `securityProfile_profileId` bigint(20) NOT NULL,
  `run_runId` bigint(20) NOT NULL,
  PRIMARY KEY  (`laneId`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Note`
--

DROP TABLE IF EXISTS `Note`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Note` (
  `noteId` bigint(20) NOT NULL auto_increment,
  `creationDate` datetime default NULL,
  `internalOnly` bit(1) NOT NULL,
  `text` varchar(255) default NULL,
  `owner_userId` bigint(20) default NULL,
  `request_requestId` bigint(20) default NULL,
  PRIMARY KEY  (`noteId`),
  KEY `FK2524124140968C` (`owner_userId`),
  KEY `FK252412E8B554FA` (`request_requestId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Platform`
--

DROP TABLE IF EXISTS `Platform`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Platform` (
  `platformId` bigint(20) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `instrumentModel` varchar(100) NOT NULL,
  `description` varchar(255) NOT NULL,
  `DTYPE` varchar(50) NOT NULL,
  PRIMARY KEY  (`platformId`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Project` (
  `projectId` bigint(20) NOT NULL auto_increment,
  `creationDate` datetime default NULL,
  `description` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  PRIMARY KEY  (`projectId`),
  KEY `FK50C8E2F960F9CBA8` (`securityProfile_profileId`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Project_Request`
--

DROP TABLE IF EXISTS `Project_Request`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Project_Request` (
  `Project_projectId` bigint(20) NOT NULL,
  `requests_requestId` bigint(20) NOT NULL,
  UNIQUE KEY `requests_requestId` (`requests_requestId`),
  KEY `FKDA6E0B2925FFBF98` (`Project_projectId`),
  KEY `FKDA6E0B29B36A83EF` (`requests_requestId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Project_Study`
--

DROP TABLE IF EXISTS `Project_Study`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Project_Study` (
  `Project_projectId` bigint(20) NOT NULL,
  `studies_studyId` bigint(20) NOT NULL,
  KEY `studyId` USING BTREE (`studies_studyId`),
  KEY `projectId` USING BTREE (`Project_projectId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Request`
--

DROP TABLE IF EXISTS `Request`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Request` (
  `requestId` bigint(20) NOT NULL auto_increment,
  `creationDate` datetime default NULL,
  `description` varchar(255) default NULL,
  `executionCount` int(11) NOT NULL,
  `lastExecutionDate` datetime default NULL,
  `name` varchar(255) default NULL,
  `protocolUniqueIdentifier` varchar(255) default NULL,
  `project_projectId` bigint(20) default NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  PRIMARY KEY  (`requestId`),
  KEY `FKA4878A6F60F9CBA8` (`securityProfile_profileId`),
  KEY `FKA4878A6F25FFBF98` (`project_projectId`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Request_Note`
--

DROP TABLE IF EXISTS `Request_Note`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Request_Note` (
  `Request_requestId` bigint(20) NOT NULL,
  `notes_noteId` bigint(20) NOT NULL,
  UNIQUE KEY `notes_noteId` (`notes_noteId`),
  KEY `FK57687FE2A7DC4D2C` (`notes_noteId`),
  KEY `FK57687FE2E8B554FA` (`Request_requestId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Run`
--

DROP TABLE IF EXISTS `Run`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Run` (
  `runId` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `accession` varchar(50) default NULL,
  `platformRunId` int(11) default NULL,
  `pairedEnd` tinyint(1) NOT NULL default '0',
  `cycles` smallint(6) default NULL,
  `filePath` varchar(255) default NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  `runType` varchar(50) NOT NULL,
  `platform_platformId` bigint(20) default NULL,
  `status_statusId` bigint(20) default NULL,
  PRIMARY KEY  (`runId`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Run_Lane`
--

DROP TABLE IF EXISTS `Run_Lane`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Run_Lane` (
  `Run_runId` bigint(20) NOT NULL,
  `lanes_laneId` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Sample`
--

DROP TABLE IF EXISTS `Sample`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Sample` (
  `sampleId` bigint(20) NOT NULL auto_increment,
  `accession` varchar(50) default NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  `DTYPE` varchar(50) NOT NULL,
  PRIMARY KEY  (`sampleId`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SecurityProfile`
--

DROP TABLE IF EXISTS `SecurityProfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SecurityProfile` (
  `profileId` bigint(20) NOT NULL auto_increment,
  `allowAllInternal` bit(1) NOT NULL,
  `owner_userId` bigint(20) default NULL,
  PRIMARY KEY  (`profileId`),
  KEY `FK18AEBA294140968C` (`owner_userId`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SecurityProfile_Group`
--

DROP TABLE IF EXISTS `SecurityProfile_Group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SecurityProfile_Group` (
  `SecurityProfile_profileId` bigint(20) NOT NULL,
  `writeGroups_groupId` bigint(20) default NULL,
  `readGroups_groupId` bigint(20) default NULL,
  KEY `FKC45520C93AFB0D3E` (`writeGroups_groupId`),
  KEY `FKC45520C960F9CBA8` (`SecurityProfile_profileId`),
  KEY `FKC45520C9467142B5` (`readGroups_groupId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SecurityProfile_User`
--

DROP TABLE IF EXISTS `SecurityProfile_User`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SecurityProfile_User` (
  `SecurityProfile_profileId` bigint(20) NOT NULL,
  `writeUsers_userId` bigint(20) default NULL,
  `readUsers_userId` bigint(20) default NULL,
  KEY `FKD4CF504160F9CBA8` (`SecurityProfile_profileId`),
  KEY `FKD4CF504125267E4D` (`readUsers_userId`),
  KEY `FKD4CF5041BD835716` (`writeUsers_userId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Status`
--

DROP TABLE IF EXISTS `Status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Status` (
  `statusId` bigint(20) NOT NULL auto_increment,
  `path` varchar(255) default NULL,
  `health` varchar(50) NOT NULL default 'Unknown',
  PRIMARY KEY  (`statusId`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Study`
--

DROP TABLE IF EXISTS `Study`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Study` (
  `studyId` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `accession` varchar(30) NOT NULL,
  `securityProfile_profileId` bigint(20) default NULL,
  `DTYPE` varchar(50) NOT NULL,
  `project_projectId` bigint(20) NOT NULL,
  PRIMARY KEY  (`studyId`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `Study_Experiment`
--

DROP TABLE IF EXISTS `Study_Experiment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `Study_Experiment` (
  `Study_studyId` bigint(20) NOT NULL,
  `experiments_experimentId` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `User` (
  `userId` bigint(20) NOT NULL auto_increment,
  `active` bit(1) NOT NULL,
  `admin` bit(1) NOT NULL,
  `external` bit(1) NOT NULL,
  `fullName` varchar(255) default NULL,
  `internal` bit(1) NOT NULL,
  `loginName` varchar(255) default NULL,
  `roles` tinyblob,
  `password` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  PRIMARY KEY  (`userId`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
INSERT INTO `User` VALUES  (1,0x01,0x01,0x00,'Administrator',0x01,'admin',0xACED0005757200135B4C6A6176612E6C616E672E537472696E673BADD256E7E91D7B4702000078700000000474001750524F544F434F4C5F48656C6C6F576F726C645F315F3174001341435449564954595F4578616D706C65335F3174001341435449564954595F4578616D706C65315F3174001341435449564954595F4578616D706C65325F31,'admin','admin@admin.com');

--
-- Table structure for table `User_Group`
--

DROP TABLE IF EXISTS `User_Group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `User_Group` (
  `users_userId` bigint(20) NOT NULL,
  `groups_groupId` bigint(20) NOT NULL,
  KEY `FKE7B7ED0B94349B7F` (`groups_groupId`),
  KEY `FKE7B7ED0B749D8197` (`users_userId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_failed`
--

DROP TABLE IF EXISTS `simlims_act_failed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_failed` (
  `errorMsg` longtext NOT NULL,
  `actInputId` bigint(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_input`
--

DROP TABLE IF EXISTS `simlims_act_input`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_input` (
  `creationDate` date NOT NULL,
  `dataRefId` bigint(20) NOT NULL,
  `dataRefClass` varchar(255) default NULL,
  `priority` varchar(255) NOT NULL,
  `actInputId` bigint(20) NOT NULL auto_increment,
  `actAlias` varchar(255) NOT NULL,
  `lockDate` date default NULL,
  `lockUserLoginName` varchar(255) default NULL,
  `processed` tinyint(1) default '0',
  PRIMARY KEY  (`actInputId`),
  KEY `inputIDX1` (`actInputId`,`processed`,`lockDate`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_input_entry`
--

DROP TABLE IF EXISTS `simlims_act_input_entry`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_input_entry` (
  `dataIndex` varchar(255) NOT NULL,
  `requestId` bigint(20) NOT NULL,
  `executionCount` int(11) NOT NULL,
  `actInputId` bigint(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_output_map`
--

DROP TABLE IF EXISTS `simlims_act_output_map`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_output_map` (
  `actInputId` bigint(20) default NULL,
  `actOutputId` bigint(20) default NULL,
  `actResultId` bigint(20) default NULL,
  `inputIdx` varchar(255) default NULL,
  `outputIdx` varchar(255) default NULL,
  KEY `OUTMAP1` (`actInputId`,`inputIdx`),
  KEY `OUTMAP2` (`actOutputId`,`outputIdx`),
  KEY `OUTMAP3` (`actResultId`,`outputIdx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_result`
--

DROP TABLE IF EXISTS `simlims_act_result`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_result` (
  `creationDate` date NOT NULL,
  `dataRefId` bigint(20) NOT NULL,
  `dataRefClass` varchar(255) default NULL,
  `priority` varchar(255) NOT NULL,
  `actAlias` varchar(255) NOT NULL,
  `actResultId` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`actResultId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_result_entry`
--

DROP TABLE IF EXISTS `simlims_act_result_entry`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_result_entry` (
  `actResultId` bigint(20) default NULL,
  `dataIndex` varchar(255) NOT NULL,
  `requestId` bigint(20) NOT NULL,
  `executionCount` int(11) NOT NULL,
  KEY `resultIDX1` (`actResultId`,`requestId`,`executionCount`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_act_skipped`
--

DROP TABLE IF EXISTS `simlims_act_skipped`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_act_skipped` (
  `actInputId` bigint(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simlims_dataref`
--

DROP TABLE IF EXISTS `simlims_dataref`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `simlims_dataref` (
  `ref_id` bigint(20) NOT NULL auto_increment,
  `ref_class` varchar(255) NOT NULL,
  `ref_data` longblob NOT NULL,
  PRIMARY KEY  (`ref_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-03-08 11:16:32
