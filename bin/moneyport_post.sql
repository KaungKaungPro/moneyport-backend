-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: moneyport
-- ------------------------------------------------------
-- Server version	8.0.36
use moneyport;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `postid` bigint NOT NULL AUTO_INCREMENT,
  `collect_count` bigint DEFAULT NULL,
  `like_count` bigint DEFAULT NULL,
  `is_pinned` bit(1) DEFAULT NULL,
  `pinned_order` int DEFAULT NULL,
  `post_content` text NOT NULL,
  `post_time` datetime(6) NOT NULL,
  `reply_count` bigint DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `userid` bigint DEFAULT NULL,
  PRIMARY KEY (`postid`),
  KEY `FKgaehntk3a9loa18trkih2gd0v` (`userid`),
  CONSTRAINT `FKgaehntk3a9loa18trkih2gd0v` FOREIGN KEY (`userid`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (2,0,41,_binary '\0',NULL,'nice to meet you all!','2024-08-05 14:08:09.273272',2,'First post on the forum',3),(3,0,5,NULL,NULL,'Welcome','2024-08-05 15:17:17.754037',0,'Hi, MoneyPort',1),(4,0,8,NULL,NULL,'Tonight, the United States stock market opened, although the three major indices mixed, but there are many highlights:\n\n　　1, a number of Chinese stocks staged a \"melting tide\": there are up and down are melting room more than, but also soared 900% melting 5 times Jiayin Jinke, soared more than 600% melting stable Sheng financial, soared 110% melting financial sector ......\n\n　　2, the U.S. stock Internet five giants in the plate collective record high.\n\n　　3, the night of the Federal Reserve resolution is coming, the three major indexes increased volatility.','2024-08-06 19:01:49.924544',3,'Chinese stocks go on a meltdown spree! Up to 900% surge What the hell happened?',1),(5,0,8,NULL,NULL,'let\'s start a new thread ...\n\nPart II was started in 2012 to 2022 (10 years cycle) ... ','2024-08-06 19:09:00.392144',7,'The Perfect Storm of the Stock Market III',20),(6,0,13,NULL,NULL,'Happen to found while using my ibanking. Do you all think this kind of mobile wallet will be feasible? Personally, i still like cash in hand.','2024-08-06 19:13:21.296772',17,'DBS Paylah',19),(7,0,4,NULL,NULL,'BreadTalk is synonymous with freshly baked $1 floss buns & an open concept bakery store with their signature white tongs & trays.','2024-08-06 19:15:08.040100',2,'BreadTalk Will Delist Company On 5 Jun After Posting $5.8 Million Loss In 2019',1),(8,0,24,_binary '',NULL,'??Choose Moneyport\'s stock to make more money!?? Contact: WhatsApp: +65 89421390','2024-08-06 19:18:25.870721',4,'???Choose our Gold Investment Programme???',20);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-07 14:43:43
