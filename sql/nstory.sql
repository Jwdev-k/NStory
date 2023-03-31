-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: nstory
-- ------------------------------------------------------
-- Server version	5.5.5-10.6.4-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '구분아이디',
  `email` varchar(50) NOT NULL COMMENT '이메일',
  `password` varchar(60) NOT NULL COMMENT '패스워드',
  `name` varchar(100) NOT NULL COMMENT '닉네임',
  `comment` varchar(100) DEFAULT NULL COMMENT '소개글',
  `profileImg` blob DEFAULT NULL COMMENT '프로필 이미지',
  `role` varchar(10) NOT NULL COMMENT '권한레벨',
  `creationDate` varchar(19) NOT NULL COMMENT '가입일',
  `lastLoginDate` varchar(19) DEFAULT NULL,
  `level` int(100) DEFAULT 1,
  `exp` int(11) DEFAULT 0,
  `nCoin` int(11) DEFAULT 0,
  `isEnable` int(1) NOT NULL COMMENT '활성화 여부',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_un` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (3,'test@gmail.com','$2a$10$hC2lameVlxjhoAbwh/nDAesZhmslYvM1mka3ieyYmSS8O.0lGpzx.','Naka','',NULL,'ADMIN','2023-02-06 13:16:36','2023-02-23 01:33:56',4,1000,0,1),(5,'test2@gmail.com','$2a$10$mf9URqJAm6DLAC6bM4DLg.aPGjCvTY0srMAEX65.ozPiQtNRfVBVi','테스트유저','',NULL,'USER','2023-02-15 11:06:30','2023-02-21 21:30:37',1,100,0,1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exptable`
--

DROP TABLE IF EXISTS `exptable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exptable` (
  `level` int(11) NOT NULL COMMENT '레벨',
  `minExp` int(11) NOT NULL COMMENT '최소 레벨 경험치',
  `maxExp` int(11) NOT NULL COMMENT '최대 레벨 경험치',
  UNIQUE KEY `level_un` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exptable`
--

LOCK TABLES `exptable` WRITE;
/*!40000 ALTER TABLE `exptable` DISABLE KEYS */;
INSERT INTO `exptable` VALUES (1,0,300),(2,300,600),(3,600,900),(4,900,1200),(5,1200,1500),(6,1500,1800),(7,1800,2100),(8,2100,2400),(9,2400,2700),(10,2700,3000);
/*!40000 ALTER TABLE `exptable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recordlog`
--

DROP TABLE IF EXISTS `recordlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recordlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '구분아이디',
  `contents` varchar(100) NOT NULL COMMENT '내용',
  `email` varchar(50) NOT NULL COMMENT '이메일',
  `name` varchar(100) NOT NULL COMMENT '유저닉네임',
  `time` varchar(19) NOT NULL DEFAULT 'current_timestamp()' COMMENT '작성시간',
  PRIMARY KEY (`id`),
  KEY `Email_FK` (`email`),
  CONSTRAINT `Email_FK` FOREIGN KEY (`email`) REFERENCES `account` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recordlog`
--

CREATE TABLE `whiteboard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '번호',
  `title` varchar(50) NOT NULL COMMENT '제목',
  `contents` text NOT NULL COMMENT '내용',
  `author` varchar(100) NOT NULL COMMENT '작성자',
  `email` varchar(50) NOT NULL COMMENT '작성자 이메일',
  `creationDate` varchar(19) NOT NULL DEFAULT '1999-01-01',
  `isEnable` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `whiteboard_FK` (`email`),
  CONSTRAINT `whiteboard_FK` FOREIGN KEY (`email`) REFERENCES `account` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

--
-- Dumping routines for database 'nstory'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-23  1:40:46
