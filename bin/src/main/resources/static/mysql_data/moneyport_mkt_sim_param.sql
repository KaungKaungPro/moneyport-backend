-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: moneyport
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `mkt_sim_param`
--

DROP TABLE IF EXISTS `mkt_sim_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mkt_sim_param` (
  `a1pvrlwr_bd` int NOT NULL,
  `a1pvrupr_bd` int NOT NULL,
  `a1tccap` int NOT NULL,
  `a1tvlwr_bd` int NOT NULL,
  `a1tvupr_bd` int NOT NULL,
  `a2pvrlwr_bd` int NOT NULL,
  `a2pvrupr_bd` int NOT NULL,
  `a2tccap` int NOT NULL,
  `a2tvlwr_bd` int NOT NULL,
  `a2tvupr_bd` int NOT NULL,
  `a3pvrlwr_bd` int NOT NULL,
  `a3pvrupr_bd` int NOT NULL,
  `a3tccap` int NOT NULL,
  `a3tvlwr_bd` int NOT NULL,
  `a3tvupr_bd` int NOT NULL,
  `param_date_effective` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_date_created` datetime(6) DEFAULT NULL,
  `param_created_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mkt_sim_param`
--

LOCK TABLES `mkt_sim_param` WRITE;
/*!40000 ALTER TABLE `mkt_sim_param` DISABLE KEYS */;
INSERT INTO `mkt_sim_param` VALUES (1,3,1,1,2,1,5,1,1,3,1,7,3,1,4,'2024-07-30',1,'2024-07-28 21:40:09.253171','admin');
/*!40000 ALTER TABLE `mkt_sim_param` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-31 15:29:25
