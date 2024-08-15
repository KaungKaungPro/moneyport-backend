-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: moneyport
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `a_class` tinyint DEFAULT NULL,
  `ipoyear` int NOT NULL,
  `last_trade_price` double NOT NULL,
  `open_price` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `currency` varchar(255) DEFAULT NULL,
  `stock_code` varchar(255) DEFAULT NULL,
  `stock_name` varchar(255) DEFAULT NULL,
  `sector` varchar(255) DEFAULT NULL,
  `a1_recommendation_id` bigint DEFAULT NULL,
  `a2_recommendation_id` bigint DEFAULT NULL,
  `a3_recommendation_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5t0k9ek08bfy7usbrmvcfvxm1` (`a1_recommendation_id`),
  KEY `FKsutxbxc4f9eve3b1pk97ll4is` (`a2_recommendation_id`),
  KEY `FKkyjwkceh3h3lat5ak9us720cd` (`a3_recommendation_id`),
  CONSTRAINT `FK5t0k9ek08bfy7usbrmvcfvxm1` FOREIGN KEY (`a1_recommendation_id`) REFERENCES `stock_recommendation` (`id`),
  CONSTRAINT `FKkyjwkceh3h3lat5ak9us720cd` FOREIGN KEY (`a3_recommendation_id`) REFERENCES `stock_recommendation` (`id`),
  CONSTRAINT `FKsutxbxc4f9eve3b1pk97ll4is` FOREIGN KEY (`a2_recommendation_id`) REFERENCES `stock_recommendation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (0,2004,53.59837694661416,160.64,1,'USD','JNJ','Johnson & Johnson','Healthcare',NULL,NULL,NULL),(0,2008,118.03092831082954,155.28,2,'USD','PG','Procter & Gamble','Consumer Goods',NULL,NULL,NULL),(0,2004,27.186748418552124,87.75,3,'USD','KOF','Coca Cola','Beverages',NULL,NULL,NULL),(0,2017,52.89955110250074,36.95,4,'USD','VZ','Verizon','Telecommunications',NULL,NULL,NULL),(0,2004,65.86987534372582,69.78,5,'USD','WMT','Walmart','Retail',NULL,NULL,NULL),(0,2004,162.40167266238922,252,6,'USD','MCD','MacDonald','Restaurants',NULL,NULL,NULL),(0,2004,120.56348733176002,172.75,7,'USD','PEP','PepsiCo','Beverages',NULL,NULL,NULL),(0,2022,407.96430450534126,367,8,'USD','ATT','AT&T','Telecommunications',NULL,NULL,NULL),(0,2004,0.06285967744657792,0.122,9,'EUR','UTG.MU','Unilever','Consumer Goods',NULL,NULL,NULL),(0,2004,118.11308530382985,117.33,10,'USD','XOM','ExxonMobile','Oil & Gas',NULL,NULL,NULL),(1,2000,2164.4369819726703,217.96,11,'USD','AAPL','Apple Inc','Technology',NULL,NULL,NULL),(1,2004,734.3020062793136,425.27,12,'USD','MSFT','Microsoft','Technology',NULL,NULL,NULL),(1,2001,13.995787078521126,170,13,'USD','GOOGL','Alphabet Inc','Technology',NULL,NULL,NULL),(1,2004,148.97844409953697,182.5,14,'USD','AMZN','Amazon','E-Commerce',NULL,NULL,NULL),(1,2012,144.62832118558242,465.7,15,'USD','META','Meta Platforms, Inc','Social Media',NULL,NULL,NULL),(1,2008,260.8408494906012,259.46,16,'USD','V','Visa','Financial Services',NULL,NULL,NULL),(1,2004,10.4804799369448,212.24,17,'USD','JPM','JPMorgan Chase','Banking',NULL,NULL,NULL),(1,2004,122.6733144400335,89.93,18,'USD','DIS','Disney','Entertainment',NULL,NULL,NULL),(1,2004,713.0365899236424,631.37,19,'USD','NFLX','Netflix','Streaming Services',NULL,NULL,NULL),(1,2004,14.603650035776294,47.88,20,'USD','CSCO','Cisco Systems','Networking Technology',NULL,NULL,NULL),(2,2010,0.8933006352898966,219.8,21,'USD','TSLA','Tesla','Automotive/Technology',NULL,NULL,NULL),(2,2004,26.948746628342235,113.06,22,'USD','NVDA','NVIDIA','Technology',NULL,NULL,NULL),(2,2004,0.10890097229433686,139.99,23,'USD','AMD','Advanced Micro Devices','Technology',NULL,NULL,NULL),(2,2021,64.3463847833039,242.93,24,'USD','COIN','Coinbase','Cryptocurrency',NULL,NULL,NULL),(2,2015,320.004871281561,60.18,25,'USD','SQ','Block, Inc','Financial Technology',NULL,NULL,NULL),(2,2020,17.58662387567588,27.18,26,'USD','PLTR','Palantir Technology','Software',NULL,NULL,NULL),(2,2018,0.03496350286382066,4.43,27,'USD','NIO','Nio Inc','Automotive',NULL,NULL,NULL),(2,2019,0.12824991565964106,6.25,28,'USD','BYND','Beyond Meat','Food Products',NULL,NULL,NULL),(2,2004,0.0582822669956389,2.49,29,'USD','PLUG','Plug Power','Hydrogen Fuel Cells',NULL,NULL,NULL),(2,2017,3.7922235161950395,7.45,30,'USD','SPCE','Virgin Galactic','Aerospace',NULL,NULL,NULL);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-11  4:55:00
