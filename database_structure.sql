
SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `4511`;
CREATE DATABASE `4511`;
USE `4511`;

DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus` (
  `campusID` int NOT NULL AUTO_INCREMENT,
  `campusLocation` text COLLATE utf8mb4_general_ci NOT NULL,
  `campusName` text COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`campusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `damaged_records`;
CREATE TABLE `damaged_records` (
  `damged_recordID` int NOT NULL AUTO_INCREMENT,
  `equipmentID` int NOT NULL,
  `reportedDate` datetime NOT NULL,
  PRIMARY KEY (`damged_recordID`),
  KEY `equipmentID` (`equipmentID`),
  CONSTRAINT `damaged_records_ibfk_1` FOREIGN KEY (`equipmentID`) REFERENCES `equipments` (`equipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `delivery_record`;
CREATE TABLE `delivery_record` (
  `deliveryID` int NOT NULL AUTO_INCREMENT,
  `deliveryBy` int NOT NULL,
  `deliveryDate` datetime NOT NULL,
  PRIMARY KEY (`deliveryID`),
  KEY `deliveryBy` (`deliveryBy`),
  CONSTRAINT `delivery_record_ibfk_1` FOREIGN KEY (`deliveryBy`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `delivery_reservations`;
CREATE TABLE `delivery_reservations` (
  `delivery_reservationsID` int NOT NULL AUTO_INCREMENT,
  `deliveryID` int NOT NULL,
  `reservationsID` int NOT NULL,
  PRIMARY KEY (`delivery_reservationsID`),
  KEY `deliveryID` (`deliveryID`),
  KEY `reservationsID` (`reservationsID`),
  CONSTRAINT `delivery_reservations_ibfk_1` FOREIGN KEY (`deliveryID`) REFERENCES `delivery_record` (`deliveryID`),
  CONSTRAINT `delivery_reservations_ibfk_2` FOREIGN KEY (`reservationsID`) REFERENCES `reservations` (`reservationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `equipments`;
CREATE TABLE `equipments` (
  `equipmentid` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `borrowedTimes` int NOT NULL,
  `isStaffOnly` tinyint(1) NOT NULL,
  `isUnlisted` tinyint(1) NOT NULL,
  `currentLocation` int NOT NULL,
  `Status` text COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`equipmentid`),
  KEY `currentLocation` (`currentLocation`),
  CONSTRAINT `equipments_ibfk_1` FOREIGN KEY (`currentLocation`) REFERENCES `campus` (`campusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `reservation_itmes`;
CREATE TABLE `reservation_itmes` (
  `reservation_itmesid` int NOT NULL AUTO_INCREMENT,
  `reservationid` int NOT NULL,
  `equipmentid` int NOT NULL,
  PRIMARY KEY (`reservation_itmesid`),
  KEY `reservationid` (`reservationid`),
  KEY `equipmentid` (`equipmentid`),
  CONSTRAINT `reservation_itmes_ibfk_1` FOREIGN KEY (`reservationid`) REFERENCES `reservations` (`reservationid`),
  CONSTRAINT `reservation_itmes_ibfk_2` FOREIGN KEY (`equipmentid`) REFERENCES `equipments` (`equipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `reservations`;
CREATE TABLE `reservations` (
  `reservationid` int NOT NULL AUTO_INCREMENT,
  `userid` int NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `checkinTime` datetime DEFAULT NULL,
  `checkoutTime` datetime DEFAULT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  PRIMARY KEY (`reservationid`),
  KEY `userid` (`userid`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `userid` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phonenumber` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `firstname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `wishlist_items`;
CREATE TABLE `wishlist_items` (
  `wishlistid` int NOT NULL AUTO_INCREMENT,
  `userid` int NOT NULL,
  `equipmentid` int NOT NULL,
  PRIMARY KEY (`wishlistid`),
  KEY `userid` (`userid`),
  KEY `equipmentid` (`equipmentid`),
  CONSTRAINT `wishlist_items_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`),
  CONSTRAINT `wishlist_items_ibfk_2` FOREIGN KEY (`equipmentid`) REFERENCES `equipments` (`equipmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

