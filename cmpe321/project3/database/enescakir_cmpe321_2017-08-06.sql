# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.22)
# Database: enescakir_cmpe321
# Generation Time: 2017-08-06 13:49:52 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table administrators
# ------------------------------------------------------------

CREATE TABLE `administrators` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `activated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `administrators` WRITE;
/*!40000 ALTER TABLE `administrators` DISABLE KEYS */;

INSERT INTO `administrators` (`id`, `name`, `email`, `password`, `activated_at`)
VALUES
	(1,'Enes Çakır','enes@cakir.web.tr','$2y$10$E9ED7J6gqC7oCGTQI9EGNe8lxf/VOx9L5s10GT.y4k25IRJx9hRWy','2017-08-06 16:26:04'),
	(2,'Ahmet Mehmet','ahmet@mehmet.com','$2y$10$DD5kTCWXOVkRbGp6j4cWYOhqLY6y.2JuheqdYmTkAGOsqZpCryxx6',NULL);

/*!40000 ALTER TABLE `administrators` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table airplanes
# ------------------------------------------------------------

CREATE TABLE `airplanes` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `model` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `age` int(6) unsigned DEFAULT NULL,
  `vertical` int(6) unsigned DEFAULT NULL,
  `horizantal` int(6) unsigned DEFAULT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `airplanes_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL,
  CONSTRAINT `airplanes_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `airplanes` WRITE;
/*!40000 ALTER TABLE `airplanes` DISABLE KEYS */;

INSERT INTO `airplanes` (`id`, `model`, `age`, `vertical`, `horizantal`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'Boeing 737',32,4,15,1,'2017-08-06 16:33:12',NULL,NULL),
	(2,'Boeing 777',24,4,24,1,'2017-08-06 16:33:41',NULL,NULL),
	(3,'Boeing 787',10,4,30,1,'2017-08-06 16:33:56',NULL,NULL),
	(4,'Airbus A320',34,4,28,1,'2017-08-06 16:34:26',NULL,NULL),
	(5,'Airbus A380',13,4,20,1,'2017-08-06 16:34:40',NULL,NULL);

/*!40000 ALTER TABLE `airplanes` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table airports
# ------------------------------------------------------------

CREATE TABLE `airports` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `city` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `airports_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL,
  CONSTRAINT `airports_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `airports` WRITE;
/*!40000 ALTER TABLE `airports` DISABLE KEYS */;

INSERT INTO `airports` (`id`, `name`, `city`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'Esenboğa Airport','Ankara',1,'2017-08-06 16:30:03',NULL,NULL),
	(2,'Etimesgut Airport','Ankara',1,'2017-08-06 16:30:14',NULL,NULL),
	(3,'Erzurum Airport','Erzurum',1,'2017-08-06 16:30:41',NULL,NULL),
	(4,'İskenderun Airport','Hatay',1,'2017-08-06 16:30:52',NULL,NULL),
	(5,'Sabiha Gökçen Airport','İstanbul',1,'2017-08-06 16:31:02',NULL,NULL),
	(6,'Atatürk Airport','İstanbul',1,'2017-08-06 16:31:13',NULL,NULL),
	(7,'Süleyman Demirel Airport','Isparta',1,'2017-08-06 16:31:25',NULL,NULL),
	(8,'Ferit Melen Airport','Van',1,'2017-08-06 16:31:45',NULL,NULL);

/*!40000 ALTER TABLE `airports` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table customers
# ------------------------------------------------------------

CREATE TABLE `customers` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE SET NULL,
  CONSTRAINT `customers_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `employees` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;

INSERT INTO `customers` (`id`, `name`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'Banu Özdemir',NULL,NULL,NULL,NULL),
	(2,'Cansu Yavuz',NULL,NULL,NULL,NULL),
	(3,'Selvi Özer',NULL,NULL,NULL,NULL),
	(4,'Deniz Kaplan',NULL,NULL,NULL,NULL),
	(5,'Bekir Öztürk',NULL,NULL,NULL,NULL),
	(6,'Onur Kaya',NULL,NULL,NULL,NULL),
	(7,'Aykut Güler',NULL,NULL,NULL,NULL),
	(8,'Gül Kaya',NULL,NULL,NULL,NULL),
	(9,'Bekir Bulut',NULL,NULL,NULL,NULL),
	(10,'Ceyda Çetin',NULL,NULL,NULL,NULL),
	(11,'Edanur Köse',NULL,NULL,NULL,NULL),
	(12,'Kübra Polat',NULL,NULL,NULL,NULL);

/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table employees
# ------------------------------------------------------------

CREATE TABLE `employees` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL,
  CONSTRAINT `employees_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;

INSERT INTO `employees` (`id`, `name`, `email`, `password`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'Şule Korkmaz','korkmazsule97@example.com','$2y$10$KN19hg61re/2en3qHNjayeAxSfAU4ni6kiLYKYHI8guCbTFIVKgx6',1,'2017-08-06 16:28:43',NULL,NULL),
	(2,'Mustafa Aktaş','aktasmustafa@example.com','$2y$10$kMkMLdJryM0KBOd1xE7EQu/ENllmPg/ixk0cHViIlEW8pMCHIhCtm',1,'2017-08-06 16:29:01',NULL,NULL),
	(3,'Kadir Öztürk','kadir_ozturk@example.com','$2y$10$.OCEE242DIB4nm8H4/ztd.wJdjE8WegXXNIGUTwJprJfNNr/8tbRK',1,'2017-08-06 16:29:27',NULL,NULL);

/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table flights
# ------------------------------------------------------------

CREATE TABLE `flights` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `number` varchar(30) COLLATE utf8_turkish_ci NOT NULL,
  `departured_at` datetime DEFAULT NULL,
  `pilot_id` int(6) unsigned DEFAULT NULL,
  `departure_id` int(6) unsigned DEFAULT NULL,
  `destination_id` int(6) unsigned DEFAULT NULL,
  `airplane_id` int(6) unsigned DEFAULT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pilot_id` (`pilot_id`),
  KEY `departure_id` (`departure_id`),
  KEY `destination_id` (`destination_id`),
  KEY `airplane_id` (`airplane_id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `flights_ibfk_1` FOREIGN KEY (`pilot_id`) REFERENCES `pilots` (`id`) ON DELETE SET NULL,
  CONSTRAINT `flights_ibfk_2` FOREIGN KEY (`departure_id`) REFERENCES `airports` (`id`) ON DELETE CASCADE,
  CONSTRAINT `flights_ibfk_3` FOREIGN KEY (`destination_id`) REFERENCES `airports` (`id`) ON DELETE CASCADE,
  CONSTRAINT `flights_ibfk_4` FOREIGN KEY (`airplane_id`) REFERENCES `airplanes` (`id`) ON DELETE SET NULL,
  CONSTRAINT `flights_ibfk_5` FOREIGN KEY (`created_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL,
  CONSTRAINT `flights_ibfk_6` FOREIGN KEY (`updated_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `flights` WRITE;
/*!40000 ALTER TABLE `flights` DISABLE KEYS */;

INSERT INTO `flights` (`id`, `number`, `departured_at`, `pilot_id`, `departure_id`, `destination_id`, `airplane_id`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'TK101','2017-08-02 10:00:00',2,3,2,1,1,'2017-08-06 16:37:14',1,'2017-08-06 16:44:31'),
	(2,'TK108','2017-08-04 06:30:00',2,6,2,5,1,'2017-08-06 16:37:31',NULL,NULL),
	(3,'TK304','2017-08-10 00:25:00',4,4,6,2,1,'2017-08-06 16:37:50',NULL,NULL),
	(4,'TK182','2017-08-31 10:25:00',1,8,4,4,1,'2017-08-06 16:38:12',1,'2017-08-06 16:44:43'),
	(5,'TK205','2017-08-14 13:25:00',3,5,2,3,1,'2017-08-06 16:38:30',NULL,NULL);

/*!40000 ALTER TABLE `flights` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table pilots
# ------------------------------------------------------------

CREATE TABLE `pilots` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `age` int(6) unsigned DEFAULT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `pilots_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL,
  CONSTRAINT `pilots_ibfk_2` FOREIGN KEY (`updated_by`) REFERENCES `administrators` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `pilots` WRITE;
/*!40000 ALTER TABLE `pilots` DISABLE KEYS */;

INSERT INTO `pilots` (`id`, `name`, `age`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'Başak Şimşek',43,1,'2017-08-06 16:34:55',NULL,NULL),
	(2,'Halil Ünal',30,1,'2017-08-06 16:35:04',NULL,NULL),
	(3,'Ayça Köse',39,1,'2017-08-06 16:35:12',NULL,NULL),
	(4,'Ayhan Aslan',28,1,'2017-08-06 16:35:24',NULL,NULL);

/*!40000 ALTER TABLE `pilots` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reservations
# ------------------------------------------------------------

CREATE TABLE `reservations` (
  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
  `pnr` varchar(15) COLLATE utf8_turkish_ci NOT NULL,
  `seat` varchar(10) COLLATE utf8_turkish_ci NOT NULL,
  `flight_id` int(6) unsigned DEFAULT NULL,
  `employee_id` int(6) unsigned DEFAULT NULL,
  `customer_id` int(6) unsigned DEFAULT NULL,
  `customer_name` varchar(255) COLLATE utf8_turkish_ci DEFAULT NULL,
  `created_by` int(6) unsigned DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_by` int(6) unsigned DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `flight_id` (`flight_id`),
  KEY `customer_id` (`customer_id`),
  KEY `created_by` (`created_by`),
  KEY `employee_id` (`employee_id`),
  KEY `updated_by` (`updated_by`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reservations_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE SET NULL,
  CONSTRAINT `reservations_ibfk_4` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL,
  CONSTRAINT `reservations_ibfk_5` FOREIGN KEY (`updated_by`) REFERENCES `employees` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;

INSERT INTO `reservations` (`id`, `pnr`, `seat`, `flight_id`, `employee_id`, `customer_id`, `customer_name`, `created_by`, `created_at`, `updated_by`, `updated_at`)
VALUES
	(1,'CMZFG3U1HX','B4',5,3,1,NULL,3,'2017-08-06 16:45:58',NULL,NULL),
	(2,'UG7SGCLSTD','D4',5,3,2,NULL,3,'2017-08-06 16:46:07',NULL,NULL),
	(3,'B6PABL0KSO','C2',3,3,2,NULL,3,'2017-08-06 16:46:14',NULL,NULL),
	(4,'C5ABLRFFTW','D1',4,3,2,NULL,3,'2017-08-06 16:46:21',NULL,NULL),
	(5,'DNDKGTXCWQ','A1',5,3,3,NULL,3,'2017-08-06 16:46:30',NULL,NULL),
	(6,'F8W5I8QJSJ','D6',3,3,4,NULL,3,'2017-08-06 16:46:41',NULL,NULL),
	(7,'7FPIRAA6G3','A4',3,3,4,NULL,3,'2017-08-06 16:46:47',NULL,NULL),
	(8,'3TRHEILBKH','D2',5,3,5,NULL,3,'2017-08-06 16:46:56',NULL,NULL),
	(9,'SAQPF9XWSZ','D1',3,3,5,NULL,3,'2017-08-06 16:47:02',NULL,NULL),
	(10,'FAF4TWE4DU','A1',3,3,6,NULL,3,'2017-08-06 16:47:12',NULL,NULL),
	(11,'8HOZYSIJ4C','D4',3,3,7,NULL,3,'2017-08-06 16:47:23',NULL,NULL),
	(12,'RWDHVSQTOT','B2',3,1,8,NULL,1,'2017-08-06 16:47:45',NULL,NULL),
	(13,'TEU8IOENSI','A6',3,1,9,NULL,1,'2017-08-06 16:47:57',NULL,NULL),
	(14,'IAZ7AYZSHE','D3',3,1,10,NULL,1,'2017-08-06 16:48:06',NULL,NULL),
	(15,'59AIQ6BRZA','A3',3,1,11,NULL,1,'2017-08-06 16:48:15',NULL,NULL),
	(16,'L2FGBYEQM6','C3',3,1,12,NULL,1,'2017-08-06 16:48:23',NULL,NULL);

/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `check_customer` BEFORE INSERT ON `reservations` FOR EACH ROW BEGIN 
      IF EXISTS (SELECT * FROM customers WHERE name LIKE NEW.customer_name) THEN
  			SET NEW.customer_id = (SELECT id FROM customers WHERE name LIKE NEW.customer_name LIMIT 1);
        SET NEW.customer_name = NULL;
  		ELSE
  			INSERT INTO customers(name) VALUES (NEW.customer_name);
  			SET NEW.customer_id = (SELECT LAST_INSERT_ID());
  			SET NEW.customer_name = NULL;
  		END IF;
       END */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;



--
-- Dumping routines (PROCEDURE) for database 'enescakir_cmpe321'
--
DELIMITER ;;

# Dump of PROCEDURE full_seats
# ------------------------------------------------------------

/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `full_seats`(IN f_id VARCHAR(10))
BEGIN 
        SELECT seat FROM reservations WHERE flight_id = f_id;
       END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE future_flights
# ------------------------------------------------------------

/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `future_flights`(IN pid VARCHAR(10))
BEGIN 
        IF pid = "ALL" THEN
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at > NOW();
      	ELSE
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at > NOW() AND f.pilot_id = pid;
      	END IF;
       END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE past_flights
# ------------------------------------------------------------

/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `past_flights`(IN pid VARCHAR(10))
BEGIN 
        IF pid = "ALL" THEN
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
      		WHERE f.departured_at < NOW();
      	ELSE
          SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
          FROM flights as f
          JOIN airports as des ON des.id = f.destination_id
          JOIN airports as dep ON dep.id = f.departure_id
          JOIN pilots as p ON p.id = f.pilot_id
          JOIN airplanes as ap ON ap.id = f.airplane_id
          WHERE f.departured_at < NOW() AND f.pilot_id = pid;
      	END IF;
       END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
