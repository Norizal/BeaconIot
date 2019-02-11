-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: localhost    Database: by_iot
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'drivce uuid',
  `user_id` varchar(32) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'drivce name',
  `type` varchar(32) DEFAULT NULL,
  `mac` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'drivce mac',
  `description` varchar(2048) CHARACTER SET utf8 DEFAULT NULL COMMENT 'drivce description',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'last updated date',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created date',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gateway`
--

DROP TABLE IF EXISTS `gateway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gateway` (
  `uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'gateway uuid',
  `user_id` varchar(32) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'gateway name',
  `mac` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'gateway mac',
  `description` varchar(2048) CHARACTER SET utf8 DEFAULT NULL COMMENT 'gateway description',
  `status` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'last updated date',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created date',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation` (
  `uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'request uuid',
  `user_id` varchar(32) DEFAULT NULL,
  `gateway_mac` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'gateway mac',
  `operation` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'gateway operation',
  `response_code` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'response code',
  `response_message` varchar(2048) CHARACTER SET utf8 DEFAULT NULL COMMENT 'response message',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'last updated date',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created date',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'status uuid',
  `gateway_mac` varchar(256) CHARACTER SET utf8 NOT NULL COMMENT 'gateway mac',
  `mac` varchar(256) CHARACTER SET utf8 NOT NULL COMMENT 'drivce mac',
  `type` varchar(256) CHARACTER SET utf8 NOT NULL COMMENT 'drivce type',
  `ble_name` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT 'ble name',
  `ibeacon_uuid` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT 'ibeacon uuid',
  `ibeacon_major` int(11) DEFAULT NULL COMMENT 'ibeacon major',
  `ibeacon_minor` int(11) DEFAULT NULL COMMENT 'ibeacon minor',
  `ibeacon_tx_power` tinyint(4) DEFAULT NULL COMMENT 'ibeacon_tx_power',
  `rssi` tinyint(4) DEFAULT NULL COMMENT 'rssi',
  `battery` tinyint(4) DEFAULT NULL COMMENT 'battery',
  `temperature` float DEFAULT NULL COMMENT 'temperature',
  `humidity` float DEFAULT NULL COMMENT 'humidity',
  `raw_data` varchar(2048) CHARACTER SET utf8 DEFAULT NULL COMMENT 'raw data',
  `gateway_load` float DEFAULT '0',
  `gateway_free` float DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created date',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updated date',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  KEY `gateway_mac` (`gateway_mac`,`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `uuid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'account uuid',
  `user_id` varchar(32) NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'password',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `role` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT 'account type',
  `description` varchar(2048) CHARACTER SET utf8 DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'last operation date',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mobile_phone` varchar(32) DEFAULT NULL,
  `nationcode` varchar(24) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-25 14:16:07
