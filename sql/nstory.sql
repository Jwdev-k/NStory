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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=444 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=10032 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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

-- Dump completed on 2023-05-11 10:02:14
