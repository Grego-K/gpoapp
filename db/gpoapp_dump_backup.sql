CREATE DATABASE  IF NOT EXISTS `gpoappdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gpoappdb`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: gpoappdb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `price_at_order` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `status` enum('CANCELLED','COMPLETED','PENDING') NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpoicjpwhmt0vm5lveep38j5sk` (`uuid`),
  KEY `FKdia0yhlyoiyjqwn7afeox2h6y` (`product_id`),
  KEY `FKitd0598xtxfyrro0df4ey8kdd` (`user_id`),
  CONSTRAINT `FKdia0yhlyoiyjqwn7afeox2h6y` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKitd0598xtxfyrro0df4ey8kdd` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `base_price` decimal(10,2) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `gpo_price` decimal(10,2) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `stock_quantity` int NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlj2l930qm0jyr4tl2xdtd2p01` (`uuid`),
  KEY `FKrk2vqdfq7yhpy5qgive31cpfi` (`supplier_id`),
  CONSTRAINT `FKrk2vqdfq7yhpy5qgive31cpfi` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regions`
--

DROP TABLE IF EXISTS `regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `regions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regions`
--

LOCK TABLES `regions` WRITE;
/*!40000 ALTER TABLE `regions` DISABLE KEYS */;
INSERT INTO `regions` VALUES (1,'Ανατολικής Μακεδονίας και Θράκης'),(2,'Αττικής'),(3,'Βορείου Αιγαίου'),(4,'Δυτικής Ελλάδας'),(5,'Δυτικής Μακεδονίας'),(6,'Ηπείρου'),(7,'Θεσσαλίας'),(8,'Ιονίων Νήσων'),(9,'Κεντρικής Μακεδονίας'),(10,'Κρήτης'),(11,'Νοτίου Αιγαίου'),(12,'Πελοποννήσου'),(13,'Στερεάς Ελλάδας');
/*!40000 ALTER TABLE `regions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `company_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `vat` varchar(9) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgiglhi58obbp51hyy9bqv4cmg` (`email`),
  UNIQUE KEY `UK8oubt750tenpr659ekymjic0c` (`vat`),
  UNIQUE KEY `UK87amtgywhhhbyqivrexf5hy9i` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `vat` varchar(9) NOT NULL,
  `region_id` bigint DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role` enum('ADMIN','PHARMACIST','SUPPLIER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6j5t70rd2eub907qysjvvd76n` (`email`),
  UNIQUE KEY `UK837vccj16f2ejnb52bmogtmf9` (`vat`),
  UNIQUE KEY `UKlgkd7iin2rkv9xkrkvdf6do2v` (`username`),
  UNIQUE KEY `UK6gyaymdekihum67hqjkipaqjs` (`uuid`),
  KEY `FKr6kg6k977rpc683msjvfp1s02` (`region_id`),
  CONSTRAINT `FKr6kg6k977rpc683msjvfp1s02` FOREIGN KEY (`region_id`) REFERENCES `regions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-01-13 22:31:00.799123','2026-01-13 22:31:00.799123','test@email.com','user','userSurename','6980127450','987e7f0a-04db-44a6-95b0-92cd177e15f8','138564499',13,'$2a$13$hRr4n0kPwqUUGnsX39JvR.IkxKoue8eUWLgu9kGdzHlIzu7Pz9YJO','testusername','PHARMACIST');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-14 13:56:40
