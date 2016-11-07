-- MySQL dump 10.13  Distrib 5.1.42, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: simlims
-- ------------------------------------------------------
-- Server version	5.1.42

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
-- Table structure for table `Group`
--

DROP TABLE IF EXISTS `Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Group` (
  `groupId` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`groupId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Group`
--

LOCK TABLES `Group` WRITE;
/*!40000 ALTER TABLE `Group` DISABLE KEYS */;
/*!40000 ALTER TABLE `Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Note`
--

DROP TABLE IF EXISTS `Note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Note` (
  `noteId` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `internalOnly` bit(1) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `owner_userId` bigint(20) DEFAULT NULL,
  `request_requestId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`noteId`),
  KEY `FK2524124140968C` (`owner_userId`),
  KEY `FK252412E8B554FA` (`request_requestId`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Note`
--

LOCK TABLES `Note` WRITE;
/*!40000 ALTER TABLE `Note` DISABLE KEYS */;
/*!40000 ALTER TABLE `Note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `projectId` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `securityProfile_profileId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`projectId`),
  KEY `FK50C8E2F960F9CBA8` (`securityProfile_profileId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Request`
--

DROP TABLE IF EXISTS `Request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Request` (
  `requestId` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `executionCount` int(11) NOT NULL,
  `lastExecutionDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `protocolUniqueIdentifier` varchar(255) DEFAULT NULL,
  `project_projectId` bigint(20) DEFAULT NULL,
  `securityProfile_profileId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`requestId`),
  KEY `FKA4878A6F60F9CBA8` (`securityProfile_profileId`),
  KEY `FKA4878A6F25FFBF98` (`project_projectId`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
/*!40000 ALTER TABLE `Request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SecurityProfile`
--

DROP TABLE IF EXISTS `SecurityProfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SecurityProfile` (
  `profileId` bigint(20) NOT NULL AUTO_INCREMENT,
  `allowAllInternal` bit(1) NOT NULL,
  `owner_userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`profileId`),
  KEY `FK18AEBA294140968C` (`owner_userId`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SecurityProfile`
--

LOCK TABLES `SecurityProfile` WRITE;
/*!40000 ALTER TABLE `SecurityProfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `SecurityProfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `admin` bit(1) NOT NULL,
  `external` bit(1) NOT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `internal` bit(1) NOT NULL,
  `loginName` varchar(255) DEFAULT NULL,
  `roles` tinyblob,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'','','\0','Administrator','','admin','¬í\0ur\0[Ljava.lang.String;­ÒVçé{G\0\0xp\0\0\0t\0PROTOCOL_HelloWorld_1_1t\0ACTIVITY_Example3_1t\0ACTIVITY_Example1_1t\0ACTIVITY_Example2_1','admin','holland@eaglegenomics.com'),(2,'','\0','\0','Demo internal user','','internal','¬í\0ur\0[Ljava.lang.String;­ÒVçé{G\0\0xp\0\0\0\0','internal','holland@eaglegenomics.com'),(3,'','\0','','Demo external user','\0','external','¬í\0ur\0[Ljava.lang.String;­ÒVçé{G\0\0xp\0\0\0t\0PROTOCOL_HelloWorld_1_1t\0ACTIVITY_Example3_1t\0ACTIVITY_Example1_1t\0ACTIVITY_Example2_1','external','holland@eaglegenomics.com');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Group`
--

DROP TABLE IF EXISTS `User_Group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User_Group` (
  `users_userId` bigint(20) NOT NULL,
  `groups_groupId` bigint(20) NOT NULL,
  KEY `FKE7B7ED0B94349B7F` (`groups_groupId`),
  KEY `FKE7B7ED0B749D8197` (`users_userId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Group`
--

LOCK TABLES `User_Group` WRITE;
/*!40000 ALTER TABLE `User_Group` DISABLE KEYS */;
/*!40000 ALTER TABLE `User_Group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_failed`
--

DROP TABLE IF EXISTS `simlims_act_failed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_failed` (
  `errorMsg` longtext NOT NULL,
  `actInputId` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_failed`
--

LOCK TABLES `simlims_act_failed` WRITE;
/*!40000 ALTER TABLE `simlims_act_failed` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_failed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_input`
--

DROP TABLE IF EXISTS `simlims_act_input`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_input` (
  `creationDate` date NOT NULL,
  `dataRefId` bigint(20) NOT NULL,
  `dataRefClass` varchar(255) DEFAULT NULL,
  `priority` varchar(255) NOT NULL,
  `actInputId` bigint(20) NOT NULL AUTO_INCREMENT,
  `actAlias` varchar(255) NOT NULL,
  `lockDate` date DEFAULT NULL,
  `lockUserLoginName` varchar(255) DEFAULT NULL,
  `processed` tinyint(1) DEFAULT '0',
  `actUniqueId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`actInputId`),
  KEY `inputIDX1` (`actInputId`,`processed`,`lockDate`)
) ENGINE=MyISAM AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_input`
--

LOCK TABLES `simlims_act_input` WRITE;
/*!40000 ALTER TABLE `simlims_act_input` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_input` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_input_entry`
--

DROP TABLE IF EXISTS `simlims_act_input_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_input_entry` (
  `dataIndex` varchar(255) NOT NULL,
  `requestId` bigint(20) NOT NULL,
  `executionCount` int(11) NOT NULL,
  `actInputId` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_input_entry`
--

LOCK TABLES `simlims_act_input_entry` WRITE;
/*!40000 ALTER TABLE `simlims_act_input_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_input_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_output_map`
--

DROP TABLE IF EXISTS `simlims_act_output_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_output_map` (
  `actInputId` bigint(20) DEFAULT NULL,
  `actOutputId` bigint(20) DEFAULT NULL,
  `actResultId` bigint(20) DEFAULT NULL,
  `inputIdx` varchar(255) DEFAULT NULL,
  `outputIdx` varchar(255) DEFAULT NULL,
  KEY `OUTMAP1` (`actInputId`,`inputIdx`),
  KEY `OUTMAP2` (`actOutputId`,`outputIdx`),
  KEY `OUTMAP3` (`actResultId`,`outputIdx`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_output_map`
--

LOCK TABLES `simlims_act_output_map` WRITE;
/*!40000 ALTER TABLE `simlims_act_output_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_output_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_result`
--

DROP TABLE IF EXISTS `simlims_act_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_result` (
  `creationDate` date NOT NULL,
  `dataRefId` bigint(20) NOT NULL,
  `dataRefClass` varchar(255) DEFAULT NULL,
  `priority` varchar(255) NOT NULL,
  `actAlias` varchar(255) NOT NULL,
  `actResultId` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`actResultId`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_result`
--

LOCK TABLES `simlims_act_result` WRITE;
/*!40000 ALTER TABLE `simlims_act_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_result_entry`
--

DROP TABLE IF EXISTS `simlims_act_result_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_result_entry` (
  `actResultId` bigint(20) DEFAULT NULL,
  `dataIndex` varchar(255) NOT NULL,
  `requestId` bigint(20) NOT NULL,
  `executionCount` int(11) NOT NULL,
  KEY `resultIDX1` (`actResultId`,`requestId`,`executionCount`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_result_entry`
--

LOCK TABLES `simlims_act_result_entry` WRITE;
/*!40000 ALTER TABLE `simlims_act_result_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_result_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_act_skipped`
--

DROP TABLE IF EXISTS `simlims_act_skipped`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_act_skipped` (
  `actInputId` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_act_skipped`
--

LOCK TABLES `simlims_act_skipped` WRITE;
/*!40000 ALTER TABLE `simlims_act_skipped` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_act_skipped` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simlims_dataref`
--

DROP TABLE IF EXISTS `simlims_dataref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simlims_dataref` (
  `ref_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_class` varchar(255) NOT NULL,
  `ref_data` longblob NOT NULL,
  PRIMARY KEY (`ref_id`)
) ENGINE=MyISAM AUTO_INCREMENT=70 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simlims_dataref`
--

LOCK TABLES `simlims_dataref` WRITE;
/*!40000 ALTER TABLE `simlims_dataref` DISABLE KEYS */;
/*!40000 ALTER TABLE `simlims_dataref` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-01-20 11:47:58
