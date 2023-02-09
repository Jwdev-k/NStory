
-- Table structure for table `account`

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
  `creationDate` varbinary(19) NOT NULL COMMENT '가입일',
  `isEnable` int(1) NOT NULL COMMENT '활성화 여부',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_un` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
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
  `name` varchar(100) NOT NULL COMMENT '유저닉네임',
  `time` varchar(19) NOT NULL DEFAULT 'current_timestamp()' COMMENT '작성시간',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recordlog`
--

--
-- Dumping routines for database 'nstory'
--
