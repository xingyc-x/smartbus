SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `smartbus_user`
-- ----------------------------
DROP TABLE IF EXISTS `smartbus_user`;
CREATE TABLE `smartbus_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `capacity` int NOT NULL,
  company_id int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `company_id` (`company_id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

DROP TABLE IF EXISTS company;
CREATE TABLE company (
  id int NOT NULL AUTO_INCREMENT,
  company_name varchar(100) NOT NULL COMMENT '公司名称',
  company_info varchar(300) COMMENT '公司简介',
  PRIMARY KEY (id),
  UNIQUE KEY `company_name` (`company_name`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='公司表';




DROP TABLE IF EXISTS project_user_relation;
CREATE TABLE project_user_relation
(
id int NOT NULL AUTO_INCREMENT,
project_id int,
user_id int,
PRIMARY KEY (id),
KEY project_id (project_id) USING BTREE,
KEY user_id (user_id) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='项目用户关系表';





