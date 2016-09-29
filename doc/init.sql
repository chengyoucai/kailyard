SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;
--
-- Table structure for table `ss_config`
--

DROP TABLE IF EXISTS `ss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_config` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONFIG_NAME` varchar(100) DEFAULT NULL COMMENT '参数名称',
  `CONFIG_VALUE` varchar(1000) DEFAULT NULL COMMENT '参数值',
  `DESCRIPTION` varchar(512) DEFAULT NULL COMMENT '参数描述',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_config`
--

LOCK TABLES `ss_config` WRITE;
/*!40000 ALTER TABLE `ss_config` DISABLE KEYS */;
INSERT INTO `ss_config` VALUES (1,'site_name','kailyard1','站点名字','2016-01-01 00:00:00',1,'2016-01-01 21:47:05',1,1);
/*!40000 ALTER TABLE `ss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_dict`
--

DROP TABLE IF EXISTS `ss_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_dict` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `DICT_TYPE` varchar(255) DEFAULT NULL COMMENT '字典分类',
  `DICT_VALUE` varchar(255) DEFAULT NULL COMMENT '字典值',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '描述',
  `SORT` int(11) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_dict`
--

LOCK TABLES `ss_dict` WRITE;
/*!40000 ALTER TABLE `ss_dict` DISABLE KEYS */;
INSERT INTO `ss_dict` VALUES (2,'132','123','123','',1,'2016-01-01 15:58:27',1,NULL,NULL,0);
/*!40000 ALTER TABLE `ss_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_menu`
--

DROP TABLE IF EXISTS `ss_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '菜单描述',
  `LINK` varchar(255) DEFAULT NULL COMMENT '菜单地址',
  `SORT` int(11) DEFAULT NULL COMMENT '菜单排序',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `ICON` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_menu`
--

LOCK TABLES `ss_menu` WRITE;
/*!40000 ALTER TABLE `ss_menu` DISABLE KEYS */;
INSERT INTO `ss_menu` VALUES (1,'系统管理',NULL,'',1,0,'icon-cog','2016-01-01 00:00:00',1,'2016-01-01 21:58:02',1,1),(2,'操作员管理',NULL,'sysUser/index',1,1,'','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(3,'角色管理',NULL,'role/index',2,1,'','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(4,'系统参数',NULL,'sysConfig/index',5,1,'','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(5,'菜单管理','菜单管理','menu/index',3,1,NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(6,'权限管理',NULL,'permissions/index',4,1,'','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(7,'数据字典',NULL,'dict/index',6,1,'','2016-01-01 15:00:10',1,'2016-01-01 15:48:25',1,1),(8,'定时任务管理',NULL,'scheduleJob/index',7,1,'','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,1);
/*!40000 ALTER TABLE `ss_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_permission`
--

DROP TABLE IF EXISTS `ss_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_permission` (
  `ID` bigint(12) NOT NULL AUTO_INCREMENT,
  `PERMISSION` varchar(100) NOT NULL DEFAULT '' COMMENT '权限代码',
  `MENU_ID` bigint(20) DEFAULT NULL COMMENT '所属菜单ID',
  `NAME` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '描述',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(255) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `index_permission` (`PERMISSION`),
  KEY `index_menu` (`MENU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_permission`
--

LOCK TABLES `ss_permission` WRITE;
/*!40000 ALTER TABLE `ss_permission` DISABLE KEYS */;
INSERT INTO `ss_permission` VALUES (1,'sys:user:list',2,'用户列表',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(2,'sys:user:add',2,'新增用户',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(3,'sys:user:del',2,'删除用户',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(4,'sys:user:edit',2,'修改用户',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(6,'sys:user:role:auth',2,'角色授权',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(7,'sys:role:list',3,'角色列表',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(8,'sys:role:add',3,'新增角色',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(9,'sys:role:del',3,'删除角色',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(10,'sys:role:edit',3,'修改角色',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(12,'sys:role:authority',3,'权限分配',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(13,'sys:menu:list',5,'菜单列表',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(14,'sys:menu:add',5,'新增菜单',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(15,'sys:menu:del',5,'删除菜单',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(16,'sys:menu:edit',5,'修改菜单',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(17,'sys:permissions:list',6,'权限列表',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(18,'sys:permissions:add',6,'增加权限',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(19,'sys:permissions:edit',6,'修改权限',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(20,'sys:permissions:del',6,'删除权限',NULL,'2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(31,'sys:config:list',4,'系统参数列表','','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0),(33,'sys:config:edit',4,'修改系统参数','','2016-01-01 00:00:00',1,'2016-01-01 20:21:59',1,2),(37,'sys:dict:list',7,'数据字典列表','','2016-01-01 15:00:47',1,NULL,NULL,0),(38,'sys:dict:add',7,'增加数据字典','','2016-01-01 15:01:28',1,NULL,NULL,0),(39,'sys:dict:edit',7,'修改数据字典','','2016-01-01 15:46:51',1,NULL,NULL,0),(40,'sys:dict:del',7,'删除数据字典','','2016-01-01 15:47:23',1,NULL,NULL,0),(41,'sys:schedule:list',8,'定时任务列表','','2016-09-08 12:14:51',1,'2016-09-08 12:22:59',1,3),(42,'sys:schedule:start',8,'启动定时任务','','2016-09-08 12:15:41',1,'2016-09-08 12:17:25',1,1),(43,'sys:schedule:stop',8,'停止定时任务','','2016-09-08 12:16:09',1,NULL,NULL,0),(44,'sys:schedule:update',8,'更新定时任务','','2016-09-08 12:16:32',1,NULL,NULL,0);
/*!40000 ALTER TABLE `ss_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_role`
--

DROP TABLE IF EXISTS `ss_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) DEFAULT NULL COMMENT '角色名称',
  `DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_role`
--

LOCK TABLES `ss_role` WRITE;
/*!40000 ALTER TABLE `ss_role` DISABLE KEYS */;
INSERT INTO `ss_role` VALUES (1,'系统管理员','系统管理员','2016-01-01 00:00:00',1,'2016-01-01 00:00:00',1,0);
/*!40000 ALTER TABLE `ss_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_role_menu_rel`
--

DROP TABLE IF EXISTS `ss_role_menu_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_role_menu_rel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MENU_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `ROLE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`ID`),
  KEY `i1` (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=370 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_role_menu_rel`
--

LOCK TABLES `ss_role_menu_rel` WRITE;
/*!40000 ALTER TABLE `ss_role_menu_rel` DISABLE KEYS */;
INSERT INTO `ss_role_menu_rel` VALUES (332,1,1,'2016-01-01 09:44:15',1),(333,0,1,'2016-01-01 09:44:15',1),(334,2,1,'2016-01-01 09:44:15',1),(335,1,1,'2016-01-01 09:44:15',1),(336,2,1,'2016-01-01 09:44:15',1),(337,2,1,'2016-01-01 09:44:15',1),(338,2,1,'2016-01-01 09:44:15',1),(339,2,1,'2016-01-01 09:44:15',1),(340,2,1,'2016-01-01 09:44:15',1),(341,3,1,'2016-01-01 09:44:15',1),(342,1,1,'2016-01-01 09:44:15',1),(343,3,1,'2016-01-01 09:44:15',1),(344,3,1,'2016-01-01 09:44:15',1),(345,3,1,'2016-01-01 09:44:15',1),(346,3,1,'2016-01-01 09:44:15',1),(347,3,1,'2016-01-01 09:44:15',1),(348,5,1,'2016-01-01 09:44:15',1),(349,1,1,'2016-01-01 09:44:15',1),(350,5,1,'2016-01-01 09:44:15',1),(351,5,1,'2016-01-01 09:44:15',1),(352,5,1,'2016-01-01 09:44:15',1),(353,5,1,'2016-01-01 09:44:15',1),(354,6,1,'2016-01-01 09:44:15',1),(355,1,1,'2016-01-01 09:44:15',1),(356,6,1,'2016-01-01 09:44:15',1),(357,6,1,'2016-01-01 09:44:15',1),(358,6,1,'2016-01-01 09:44:15',1),(359,6,1,'2016-01-01 09:44:15',1),(360,4,1,'2016-01-01 09:44:15',1),(361,1,1,'2016-01-01 09:44:15',1),(362,4,1,'2016-01-01 09:44:15',1),(363,4,1,'2016-01-01 09:44:15',1),(364,7,1,'2016-01-01 09:44:15',1),(365,1,1,'2016-01-01 09:44:15',1),(366,7,1,'2016-01-01 09:44:15',1),(367,7,1,'2016-01-01 09:44:15',1),(368,7,1,'2016-01-01 09:44:15',1),(369,7,1,'2016-01-01 09:44:15',1);
/*!40000 ALTER TABLE `ss_role_menu_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_role_permission_rel`
--

DROP TABLE IF EXISTS `ss_role_permission_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_role_permission_rel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PERMISSION_ID` bigint(20) DEFAULT NULL COMMENT '权限ID',
  `ROLE_ID` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`ID`),
  KEY `i1` (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_role_permission_rel`
--

LOCK TABLES `ss_role_permission_rel` WRITE;
/*!40000 ALTER TABLE `ss_role_permission_rel` DISABLE KEYS */;
INSERT INTO `ss_role_permission_rel` VALUES (236,1,1,'2016-01-01 09:44:15',1),(237,2,1,'2016-01-01 09:44:15',1),(238,3,1,'2016-01-01 09:44:15',1),(239,4,1,'2016-01-01 09:44:15',1),(240,6,1,'2016-01-01 09:44:15',1),(241,7,1,'2016-01-01 09:44:15',1),(242,8,1,'2016-01-01 09:44:15',1),(243,9,1,'2016-01-01 09:44:15',1),(244,10,1,'2016-01-01 09:44:15',1),(245,12,1,'2016-01-01 09:44:15',1),(246,13,1,'2016-01-01 09:44:15',1),(247,14,1,'2016-01-01 09:44:15',1),(248,15,1,'2016-01-01 09:44:15',1),(249,16,1,'2016-01-01 09:44:15',1),(250,17,1,'2016-01-01 09:44:15',1),(251,18,1,'2016-01-01 09:44:15',1),(252,19,1,'2016-01-01 09:44:15',1),(253,20,1,'2016-01-01 09:44:15',1),(254,31,1,'2016-01-01 09:44:15',1),(255,33,1,'2016-01-01 09:44:15',1),(256,37,1,'2016-01-01 09:44:15',1),(257,38,1,'2016-01-01 09:44:15',1),(258,39,1,'2016-01-01 09:44:15',1),(259,40,1,'2016-01-01 09:44:15',1);
/*!40000 ALTER TABLE `ss_role_permission_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_user`
--

DROP TABLE IF EXISTS `ss_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOGIN_NAME` varchar(50) DEFAULT NULL COMMENT '登录名',
  `NAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT 'email',
  `PASSWORD` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `SALT` varchar(32) DEFAULT NULL COMMENT '密码随机数',
  `ROLES` varchar(512) DEFAULT NULL COMMENT '用户角色',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  `VERSION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `index_loginname` (`LOGIN_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_user`
--

LOCK TABLES `ss_user` WRITE;
/*!40000 ALTER TABLE `ss_user` DISABLE KEYS */;
INSERT INTO `ss_user` VALUES (1,'admin','系统管理员',NULL,'4fef0c88b302566149aa55901c8cb9a414382619','2bc319c6378542ac','admin','2016-01-01 00:00:00',1,'2016-01-01 19:41:13',1,3),(3,'12345','123123','','66c6c9ed644a29ad2283772f912359d1e9ea5f74','e0788863e169fdcd','normal','2016-01-01 21:28:24',1,NULL,NULL,0);
/*!40000 ALTER TABLE `ss_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss_user_role_rel`
--

DROP TABLE IF EXISTS `ss_user_role_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_user_role_rel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss_user_role_rel`
--

LOCK TABLES `ss_user_role_rel` WRITE;
/*!40000 ALTER TABLE `ss_user_role_rel` DISABLE KEYS */;
INSERT INTO `ss_user_role_rel` VALUES (1,1,1,'2016-01-01 00:00:00',1);
/*!40000 ALTER TABLE `ss_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) DEFAULT NULL COMMENT '任务名称',
  `JOB_GROUP` varchar(32) DEFAULT NULL COMMENT '任务组',
  `JOB_CRON` varchar(32) DEFAULT NULL COMMENT '定时表达式',
  `JOB_CLASS` varchar(128) DEFAULT NULL COMMENT '定时任务执行类',
  `JOB_METHOD` varchar(64) DEFAULT NULL COMMENT '定时任务执行方法',
  `JOB_STATE` varchar(8) DEFAULT NULL COMMENT '定时任务状态',
  `LAST_RUN_TIME` datetime DEFAULT NULL COMMENT '最近一次执行时间',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_USER_ID` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `index_classmethod` (`JOB_CLASS`,`JOB_METHOD`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT

SET FOREIGN_KEY_CHECKS = 1;
