-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: nstory
-- ------------------------------------------------------
-- Server version	5.5.5-10.9.3-MariaDB

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
  `profileImg` mediumblob DEFAULT NULL COMMENT '프로필 이미지',
  `role` varchar(10) NOT NULL COMMENT '권한레벨',
  `creationDate` varchar(19) NOT NULL COMMENT '가입일',
  `lastLoginDate` varchar(19) DEFAULT NULL,
  `level` int(100) DEFAULT 1,
  `exp` int(11) DEFAULT 0,
  `nCoin` int(11) DEFAULT 0,
  `isEnable` tinyint(1) NOT NULL COMMENT '활성화 여부',
  `isOAuth` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_un` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `cid` int(11) NOT NULL AUTO_INCREMENT COMMENT '댓글 아이디',
  `id` int(11) NOT NULL COMMENT '게시판 아이디',
  `email` varchar(50) NOT NULL COMMENT '이메일',
  `name` varchar(100) NOT NULL COMMENT '작성자이름',
  `contents` varchar(300) NOT NULL COMMENT '내용',
  `time` varchar(19) NOT NULL COMMENT '작성시간',
  `isEnable` tinyint(1) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `comment_FK` (`id`),
  KEY `comment_FK_1` (`email`),
  CONSTRAINT `comment_FK` FOREIGN KEY (`id`) REFERENCES `whiteboard` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_FK_1` FOREIGN KEY (`email`) REFERENCES `account` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=223 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

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
INSERT INTO nstory.exptable (`level`,minExp,maxExp) VALUES
	 (1,0,300),
	 (2,300,600),
	 (3,600,900),
	 (4,900,1200),
	 (5,1200,1500),
	 (6,1500,1800),
	 (7,1800,2100),
	 (8,2100,2400),
	 (9,2400,2700),
	 (10,2700,3000);
INSERT INTO nstory.exptable (`level`,minExp,maxExp) VALUES
	 (11,3000,3500),
	 (12,3500,4000),
	 (13,4000,4500),
	 (14,4500,5000),
	 (15,5000,5500),
	 (16,5500,6000),
	 (17,6000,6500),
	 (18,6500,7000),
	 (19,7000,7500),
	 (20,7500,8000);
INSERT INTO nstory.exptable (`level`,minExp,maxExp) VALUES
	 (21,8000,9000),
	 (22,9000,10000),
	 (23,10000,11000),
	 (24,11000,12000),
	 (25,12000,13000),
	 (26,13000,14000),
	 (27,14000,15000),
	 (28,16000,17000),
	 (29,18000,19000),
	 (30,20000,21000);
INSERT INTO nstory.exptable (`level`,minExp,maxExp) VALUES
	 (31,21000,23000),
	 (32,23000,25000),
	 (33,25000,27000),
	 (34,27000,29000),
	 (35,29000,31000),
	 (36,31000,33000),
	 (37,33000,35000),
	 (38,35000,37000),
	 (39,37000,39000),
	 (40,39000,41000);
INSERT INTO nstory.exptable (`level`,minExp,maxExp) VALUES
	 (41,41000,44000),
	 (42,44000,47000),
	 (43,47000,50000),
	 (44,50000,53000),
	 (45,53000,56000),
	 (46,56000,59000),
	 (47,59000,62000),
	 (48,62000,65000),
	 (49,65000,68000),
	 (50,68000,71000);
/*!40000 ALTER TABLE `exptable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes_history`
--

DROP TABLE IF EXISTS `likes_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes_history` (
  `like_type` tinyint(1) NOT NULL COMMENT '좋아요1/싫어요0',
  `id` int(11) DEFAULT NULL COMMENT '게시물 번호 리스트',
  `email` varchar(50) NOT NULL COMMENT '이메일',
  KEY `whiteboard_likes_FK` (`email`),
  KEY `likes_history_FK` (`id`),
  CONSTRAINT `likes_history_FK` FOREIGN KEY (`id`) REFERENCES `whiteboard` (`id`) ON DELETE CASCADE,
  CONSTRAINT `whiteboard_likes_FK` FOREIGN KEY (`email`) REFERENCES `account` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes_history`
--

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
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recordlog`
--

--
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reply` (
  `rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '대댓글 아이디',
  `cid` int(11) NOT NULL COMMENT '댓글 아이디',
  `id` int(11) NOT NULL COMMENT '게시판 아이디',
  `email` varchar(50) NOT NULL COMMENT '이메일',
  `name` varchar(100) NOT NULL COMMENT '작성자 닉네임',
  `contents` varchar(300) NOT NULL COMMENT '내용',
  `time` varchar(19) NOT NULL COMMENT '작성시간',
  `isEnable` tinyint(1) NOT NULL,
  PRIMARY KEY (`rid`),
  KEY `reply_FK` (`cid`),
  KEY `reply_FK_1` (`email`),
  KEY `reply_id_fk` (`id`),
  CONSTRAINT `reply_FK` FOREIGN KEY (`cid`) REFERENCES `comment` (`cid`) ON DELETE CASCADE,
  CONSTRAINT `reply_FK_1` FOREIGN KEY (`email`) REFERENCES `account` (`email`) ON DELETE CASCADE,
  CONSTRAINT `reply_id_fk` FOREIGN KEY (`id`) REFERENCES `whiteboard` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reply`
--

--
-- Table structure for table `shop_items`
--

DROP TABLE IF EXISTS `shop_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop_items` (
  `itemId` int(11) NOT NULL AUTO_INCREMENT COMMENT '아이템아이디',
  `itemName` varchar(100) NOT NULL COMMENT '아이템이름',
  `itemPrice` int(11) NOT NULL COMMENT '아이템가격',
  `itemDsrp` varchar(100) DEFAULT NULL COMMENT '아이템설명',
  `itemImg` varchar(100) DEFAULT NULL COMMENT '아이템이미지이름',
  PRIMARY KEY (`itemId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_items`
--

LOCK TABLES `shop_items` WRITE;
/*!40000 ALTER TABLE `shop_items` DISABLE KEYS */;
INSERT INTO `shop_items` VALUES (1,'부스트',100,'EXP 2배 아이템','boost.png'),(2,'코인',100,'코인으로 교환한다.','coin.png'),(3,'연습티켓 (더블)',500,'300의 경험치를 획득한다.','ticket-double.png'),(4,'연습티켓 (트리플)',1000,'600의 경험치를 획득한다.','ticket-triple.png'),(5,'연습티켓 (프리미엄)',1500,'1200의 경험치를 획득한다.','ticket-quadruple.png');
/*!40000 ALTER TABLE `shop_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whiteboard`
--

DROP TABLE IF EXISTS `whiteboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `whiteboard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '번호',
  `bid` varchar(100) NOT NULL COMMENT '게시판이름',
  `title` varchar(50) NOT NULL COMMENT '제목',
  `contents` longtext NOT NULL COMMENT '내용',
  `author` varchar(100) NOT NULL COMMENT '작성자',
  `email` varchar(50) NOT NULL COMMENT '작성자 이메일',
  `creationDate` varchar(19) NOT NULL DEFAULT '1999-01-01',
  `views` int(11) NOT NULL DEFAULT 0 COMMENT '조회수',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '좋아요',
  `dislike_count` int(11) NOT NULL DEFAULT 0 COMMENT '싫어요',
  `isNotice` tinyint(1) NOT NULL DEFAULT 0,
  `isEnable` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `whiteboard_FK` (`email`),
  KEY `bid_pk` (`bid`),
  CONSTRAINT `bid_pk` FOREIGN KEY (`bid`) REFERENCES `whiteboard_list` (`bid`) ON DELETE CASCADE,
  CONSTRAINT `whiteboard_FK` FOREIGN KEY (`email`) REFERENCES `account` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10040 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whiteboard`
--


--
-- Table structure for table `whiteboard_list`
--

DROP TABLE IF EXISTS `whiteboard_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `whiteboard_list` (
  `bid` varchar(100) NOT NULL COMMENT '게시판 아이디',
  `kname` varchar(30) NOT NULL COMMENT '게시판 이름',
  `subname` varchar(100) DEFAULT NULL,
  `email` varchar(50) NOT NULL COMMENT '관리자 이메일',
  `mainImg` mediumblob DEFAULT NULL COMMENT '게시판 대표이미지',
  PRIMARY KEY (`bid`),
  KEY `whiteboard_list_FK` (`email`),
  CONSTRAINT `whiteboard_list_FK` FOREIGN KEY (`email`) REFERENCES `account` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whiteboard_list`
--

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

-- Dump completed on 2024-01-29 15:34:21
