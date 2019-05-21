
DROP TABLE IF EXISTS `funcinfo`;

CREATE TABLE `funcinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `funcCode` varchar(50) DEFAULT NULL,
  `funcName` varchar(50) NOT NULL,
  `funcDesc` varchar(50) DEFAULT NULL,
  `superCode` varchar(20) DEFAULT NULL,
  `levels` int(2) NOT NULL,
  `funcSort` int(2) NOT NULL,
  `statu` varchar(2) NOT NULL,
  `funcDo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8;

/*Data for the table `funcinfo` */

insert  into `funcinfo`(`id`,`funcCode`,`funcName`,`funcDesc`,`superCode`,`levels`,`funcSort`,`statu`,`funcDo`) values (43,'sysAdmin','设备列表','设备列表','super',1,1,'1',''),(44,'sysInfo','设备列表','设备列表','sysAdmin',2,1,'1',''),(46,'roleInfo','设备列表','设备列表','sysInfo',2,2,'1','dyconfig/projectInfo/doProjectInfo.do?method=queryProjectInfoXml');

/*Table structure for table `monitorinfo` */

DROP TABLE IF EXISTS `monitorinfo`;

CREATE TABLE `monitorinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `cost_time` int(11) NOT NULL,
  `function` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `function_href` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reason` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29367 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `monitorinfo` */

/*Table structure for table `rolefuncinfo` */

DROP TABLE IF EXISTS `rolefuncinfo`;

CREATE TABLE `rolefuncinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `roleCode` varchar(25) NOT NULL,
  `funcCode` varchar(50) NOT NULL,
  `userCode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8;

/*Data for the table `rolefuncinfo` */

insert  into `rolefuncinfo`(`id`,`roleCode`,`funcCode`,`userCode`) values (3,'1','sysAdmin','admin'),(4,'1','sysInfo','admin'),(5,'1','roleInfo','admin'),(6,'1','adminInfo','admin'),(56,'1','projectManage','admin'),(57,'1','projectMassage','admin'),(58,'1','projectInfo','admin'),(256,'2','projectManage','manager'),(257,'2','projectMassage','manager'),(258,'2','projectInfo','manager'),(259,'1','watchManage','admin'),(260,'1','watchMassage','admin'),(261,'1','watchInfo','admin'),(262,'2','watchManage','manager'),(263,'2','watchMassage','manager'),(264,'2','watchInfo','manager'),(265,'1','gouka','admin'),(266,'1','addbalanceSuccess','admin'),(267,'1','shangYou1','admin'),(268,'1','shangYou2','admin'),(269,'1','shangYou3','admin'),(270,'2','gouka','manager'),(271,'2','addbalanceSuccess','manager');

/*Table structure for table `roleinfo` */

DROP TABLE IF EXISTS `roleinfo`;

CREATE TABLE `roleinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(25) NOT NULL,
  `roleDesc` varchar(50) DEFAULT NULL,
  `roleCode` varchar(25) NOT NULL,
  `roleType` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `roleinfo` */

insert  into `roleinfo`(`id`,`roleName`,`roleDesc`,`roleCode`,`roleType`) values (1,'超级管理员','admin','admin','超级管理员'),(2,'客户','manager','manager','客户'),(3,'1','1','1','1');

/*Table structure for table `sysloginfo` */

DROP TABLE IF EXISTS `sysloginfo`;

CREATE TABLE `sysloginfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userName` varchar(25) NOT NULL,
  `logDate` datetime NOT NULL,
  `logs` varchar(1024) NOT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `outDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1419 DEFAULT CHARSET=utf8;

/*Data for the table `sysloginfo` */

insert  into `sysloginfo`(`id`,`userName`,`logDate`,`logs`,`ip`,`outDate`) values (1407,'admin','2019-05-16 16:58:09','登录系统','0:0:0:0:0:0:0:1',NULL),(1408,'admin','2019-05-17 13:37:51','登录系统','0:0:0:0:0:0:0:1','2019-05-17 13:43:31'),(1409,'admin','2019-05-17 13:43:36','登录系统','0:0:0:0:0:0:0:1','2019-05-17 14:19:31'),(1410,'admin','2019-05-17 14:19:36','登录系统','0:0:0:0:0:0:0:1','2019-05-17 15:22:11'),(1411,'admin','2019-05-17 15:22:13','登录系统','0:0:0:0:0:0:0:1','2019-05-17 15:25:24'),(1412,'admin','2019-05-17 15:25:27','登录系统','0:0:0:0:0:0:0:1','2019-05-17 15:35:29'),(1413,'admin','2019-05-17 15:35:32','登录系统','0:0:0:0:0:0:0:1','2019-05-17 15:42:12'),(1414,'admin','2019-05-17 15:42:15','登录系统','0:0:0:0:0:0:0:1','2019-05-17 16:19:06'),(1415,'admin','2019-05-17 16:19:10','登录系统','0:0:0:0:0:0:0:1','2019-05-17 16:38:21'),(1416,'admin','2019-05-17 16:38:24','登录系统','0:0:0:0:0:0:0:1','2019-05-17 17:09:42'),(1417,'admin','2019-05-17 17:09:46','登录系统','0:0:0:0:0:0:0:1','2019-05-17 17:17:46'),(1418,'admin','2019-05-17 17:17:49','登录系统','0:0:0:0:0:0:0:1',NULL);

/*Table structure for table `userinfo` */

DROP TABLE IF EXISTS `userinfo`;

CREATE TABLE `userinfo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userCode` varchar(25) NOT NULL DEFAULT '',
  `userName` varchar(50) NOT NULL,
  `passWrd` varchar(50) NOT NULL,
  `passWrd1` varchar(50) NOT NULL,
  `tag` int(2) NOT NULL DEFAULT '0',
  `createDate` datetime NOT NULL DEFAULT '1800-00-00 00:00:00',
  `updateDate` datetime DEFAULT '1800-00-00 00:00:00',
  `groupCode` varchar(11) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `addUser` varchar(20) DEFAULT NULL,
  `phoneNo` varchar(18) DEFAULT '',
  `apply_status` char(1) DEFAULT '0',
  `company_id` varchar(200) DEFAULT '0',
  `isInApply` char(1) DEFAULT '1',
  `applyReason` varchar(200) DEFAULT '',
  `project_id` varchar(200) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;

/*Data for the table `userinfo` */

insert  into `userinfo`(`id`,`userCode`,`userName`,`passWrd`,`passWrd1`,`tag`,`createDate`,`updateDate`,`groupCode`,`remark`,`code`,`addUser`,`phoneNo`,`apply_status`,`company_id`,`isInApply`,`applyReason`,`project_id`) values (70,'admin','admin','fcea920f7412b5da7be0cf42b8c93759','1234567',1,'2014-06-23 12:00:00','2018-10-19 12:30:14','1','超级管理员','admin','admin','13548965896','2','0','1','','0'),(90,'abc','abc','e10adc3949ba59abbe56e057f20f883e','123456',1,'2019-04-19 14:18:45','2019-04-19 14:25:53','2','123321','manager','admin','','0','0','1','','0'),(91,'121212','121212','e10adc3949ba59abbe56e057f20f883e','123456',1,'2019-04-26 11:08:39','2019-04-26 11:08:39','2','客户','manager','admin','','0','0','1','','0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
