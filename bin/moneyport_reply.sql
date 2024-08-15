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
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text,
  `create_time` datetime(6) DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `reply_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmtfnx1efxiq3p9n6kh3org2yj` (`user_id`),
  CONSTRAINT `FKmtfnx1efxiq3p9n6kh3org2yj` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reply`
--

LOCK TABLES `reply` WRITE;
/*!40000 ALTER TABLE `reply` DISABLE KEYS */;
INSERT INTO `reply` VALUES (1,'wow，welcome！！！','2024-08-05 14:08:22.105298',2,NULL,3),(2,'hello！！','2024-08-05 14:08:27.283290',2,1,3),(3,'Obviously, waiting for the receiver.','2024-08-06 19:05:41.457488',4,NULL,20),(4,'Domestic stock market no money finally knows where to go','2024-08-06 19:06:12.976197',4,NULL,19),(5,'That price doesn\'t exist. That\'s where the merging stocks merge out. The next step in the financial sector is to make a new low and then merge stocks','2024-08-06 19:06:37.337978',4,3,19),(6,'once papa powell pivot ....\n\nstock market will skyrocket to the moon ....\n\nMTL?','2024-08-06 19:09:13.969051',5,NULL,20),(7,'10 yrs? Wow, did our networth at least doubled?\nif so hopefully can double again in the next 5 yrs then call it quits liao','2024-08-06 19:09:22.185235',5,NULL,20),(8,'definitely not for STI ... \n\nback to where we started .... ','2024-08-06 19:09:28.365484',5,NULL,20),(9,'Market was expecting Fed to cut rates in 2023, then Powell said that is unlikely the case. \n\nThat is the million dollar question, will interest rate stay high for prolonged period of time? ','2024-08-06 19:09:49.116444',5,NULL,19),(10,'Powell says so but no one believes him... ?\n\nMarket knows better than the decision maker. \n\nEdited January 3, 2023 by Volvobrick','2024-08-06 19:09:59.746511',5,6,19),(11,'I’ll Pee a bit here ? to leave my mark on this thread \n\nThank you all the sifus!','2024-08-06 19:10:09.085543',5,NULL,19),(12,'STI cmi one lah.\n\nits stable and gives some dividends so in times of swings it is good to buy and keep for safety.  But for long time growth, quite cmi.\n\nthats why when it dropped heavily during covid, i bought a lot, and when it went up 40%, i threw all out.  \n ','2024-08-06 19:12:26.612952',5,NULL,19),(13,'Hahahaha, I can only use cash \'cos using Windows phone.','2024-08-06 19:13:30.949992',6,NULL,19),(14,'I think in Japan this is nothing new. They make a lot of payment using their mobile phones. But in Sgp a bit difficult unless ppl pay less or get discount using the system. DBS should work towards this. Haha...','2024-08-06 19:13:47.177940',6,NULL,1),(15,'Starting Today 10 Feb, 2023, DBS will give 1st 100k paylah user $3 cashback for payment in Hawker Centre.\n\nThis morning, i paid $3.80 for kopi & eggs.\n\nI received $3 cashback','2024-08-06 19:13:59.650847',6,NULL,1),(16,'Regarding paylah app..\n\nMy phone number is linked to my posb savings account for paynow. If someone sends a payment via their paylah acct to my phone number, I get notified as it landed in my normal posb account. i get emailed.\n\nThen I went to install paylah app and link to my account. Suddenly I\'m not getting notified after someone send me a payment via their paylah. Then I found out it went into the paylah account. I had to manually transfer it back to my savings account. After I delete the paylah account, everything went back to normal.\n\nI\'m not sure what\'s the use of paylah, when everything works better with paynow.','2024-08-06 19:14:08.411842',6,NULL,1),(17,'wow','2024-08-06 19:15:22.748200',7,NULL,1),(18,'no money no bread\n\nno bread no talk \n \n\nwanted to support this homegrown but their stuff expensive leh...mee robus $5-6, fried dough stick $1.20 at food rep, kopi C $1.80...how to support? Same goes with Yakun...\n\nI can get these at kpt or hc for $3, $0.90 and $1.10 only \n\nI only patronize their franchised DTF because of Uni$...','2024-08-06 19:15:42.923069',7,NULL,19),(19,'WOW, good money indeed!','2024-08-06 19:18:48.231590',8,NULL,20),(20,'As an observer in April 2024, I can provide some general insights into NVIDIA stock. Keep in mind, however, that stock market conditions change rapidly, my information may not be up-to-date, and I cannot provide specific investment advice.','2024-08-06 19:19:43.087860',8,NULL,19),(21,'Historical Performance:\nNVIDIA\'s stock has performed very strongly over the past few years. The company\'s leadership in the artificial intelligence, graphics processor and data centre markets has driven significant growth in its share price.','2024-08-06 19:19:54.095565',8,NULL,19),(22,'Market Opportunity:\nDemand for NVIDIA\'s products is likely to continue to grow as technologies such as AI, autonomous driving, and meta-universes evolve.','2024-08-06 19:20:04.189959',8,NULL,19),(23,'Hi','2024-08-06 20:16:20.613328',6,NULL,20),(24,'Hi','2024-08-06 20:43:27.532100',6,NULL,20),(25,'Hi','2024-08-06 20:44:09.792155',6,NULL,20),(26,'wow','2024-08-06 20:44:39.873200',6,NULL,20),(27,'Hi','2024-08-06 21:00:26.787213',6,NULL,20),(28,'Hi','2024-08-06 21:02:34.221339',6,NULL,20),(29,'hi','2024-08-06 21:05:18.419996',6,NULL,20),(30,'hi','2024-08-06 21:05:44.863923',6,26,20),(31,'hhh','2024-08-06 21:12:43.443861',6,13,20),(32,'hhh','2024-08-06 21:16:51.892030',6,13,20),(33,'wow','2024-08-06 21:19:04.376657',6,13,20),(34,'hhh','2024-08-06 21:40:17.134407',6,13,20),(35,'hhh','2024-08-07 14:24:21.796183',6,13,20);
/*!40000 ALTER TABLE `reply` ENABLE KEYS */;
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
