#-- 测试采用 mysql 数据库
DROP  TABLE IF EXISTS ORDERS;
CREATE TABLE `ORDERS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` int(11) default 0,
  `PRODUCT_NAME` varchar(50) NOT NULL,
  `orderNumber` varchar(50) NOT NULL,
  `AMOUNT` decimal(18,2) NOT NULL,
  `CREATED_DATETIME` datetime ,
  `LAST_MODIFIED_DATETIME` datetime ,
  `STATUS` varchar(20) NOT NULL,
  `ITEM_NAMES` varchar(100),
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
