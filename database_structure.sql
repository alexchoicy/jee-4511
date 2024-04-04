
SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `4511`;
CREATE DATABASE `4511`;
USE `4511`;

DROP TABLE IF EXISTS `Roles`;
CREATE TABLE `Roles` (
                         `roleId` int NOT NULL,
                         `roleName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                         PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus` (
                          `campusId` int NOT NULL AUTO_INCREMENT,
                          `campusLocation` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                          `campusName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                          PRIMARY KEY (`campusId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `damaged_records`;
CREATE TABLE `damaged_records` (
                                   `damaged_recordId` int NOT NULL AUTO_INCREMENT,
                                   `equipmentId` int NOT NULL,
                                   `reportedDate` datetime NOT NULL,
                                   PRIMARY KEY (`damaged_recordId`),
                                   KEY `equipmentId` (`equipmentId`),
                                   CONSTRAINT `damaged_records_ibfk_1` FOREIGN KEY (`equipmentId`) REFERENCES `equipments` (`equipmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `delivery_record`;
CREATE TABLE `delivery_record` (
                                   `deliveryId` int NOT NULL AUTO_INCREMENT,
                                   `deliveryBy` int NOT NULL,
                                   `deliveryDate` datetime NOT NULL,
                                   PRIMARY KEY (`deliveryId`),
                                   KEY `deliveryBy` (`deliveryBy`),
                                   CONSTRAINT `delivery_record_ibfk_1` FOREIGN KEY (`deliveryBy`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `delivery_reservations`;
CREATE TABLE `delivery_reservations` (
                                         `delivery_reservationsId` int NOT NULL AUTO_INCREMENT,
                                         `deliveryId` int NOT NULL,
                                         `reservationsId` int NOT NULL,
                                         PRIMARY KEY (`delivery_reservationsId`),
                                         KEY `deliveryId` (`deliveryId`),
                                         KEY `reservationsId` (`reservationsId`),
                                         CONSTRAINT `delivery_reservations_ibfk_1` FOREIGN KEY (`deliveryId`) REFERENCES `delivery_record` (`deliveryId`),
                                         CONSTRAINT `delivery_reservations_ibfk_2` FOREIGN KEY (`reservationsId`) REFERENCES `reservations` (`reservationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `equipment_status`;
CREATE TABLE `equipment_status` (
                                    `equipment_status` int NOT NULL,
                                    `status_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                                    PRIMARY KEY (`equipment_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `equipments`;
CREATE TABLE `equipments` (
                              `equipmentId` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                              `borrowedTimes` int NOT NULL,
                              `isStaffOnly` tinyint(1) NOT NULL,
                              `isUnlisted` tinyint(1) NOT NULL,
                              `status` int NOT NULL,
                              `currentLocation` int NOT NULL,
                              PRIMARY KEY (`equipmentId`),
                              KEY `currentLocation` (`currentLocation`),
                              CONSTRAINT `equipments_ibfk_1` FOREIGN KEY (`currentLocation`) REFERENCES `campus` (`campusId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `reservation_itmes`;
CREATE TABLE `reservation_itmes` (
                                     `reservation_itemId` int NOT NULL AUTO_INCREMENT,
                                     `reservationId` int NOT NULL,
                                     `equipmentId` int NOT NULL,
                                     PRIMARY KEY (`reservation_itemId`),
                                     KEY `reservationId` (`reservationId`),
                                     KEY `equipmentId` (`equipmentId`),
                                     CONSTRAINT `reservation_itmes_ibfk_1` FOREIGN KEY (`reservationId`) REFERENCES `reservations` (`reservationId`),
                                     CONSTRAINT `reservation_itmes_ibfk_2` FOREIGN KEY (`equipmentId`) REFERENCES `equipments` (`equipmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `reservations`;
CREATE TABLE `reservations` (
                                `reservationId` int NOT NULL AUTO_INCREMENT,
                                `userId` int NOT NULL,
                                `location` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                                `startTime` datetime NOT NULL,
                                `endTime` datetime NOT NULL,
                                `checkinTime` datetime DEFAULT NULL,
                                `checkoutTime` datetime DEFAULT NULL,
                                `createdAt` datetime NOT NULL DEFAULT (now()),
                                PRIMARY KEY (`reservationId`),
                                KEY `userId` (`userId`),
                                CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `userId` int NOT NULL AUTO_INCREMENT,
                         `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                         `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                         `phoneNumber` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                         `firstName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                         `lastName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                         `role` int NOT NULL,
                         PRIMARY KEY (`userId`),
                         KEY `role` (`role`),
                         CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role`) REFERENCES `Roles` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `wishlist_items`;
CREATE TABLE `wishlist_items` (
                                  `wishlistId` int NOT NULL AUTO_INCREMENT,
                                  `userId` int NOT NULL,
                                  `equipmentId` int NOT NULL,
                                  PRIMARY KEY (`wishlistId`),
                                  KEY `userId` (`userId`),
                                  KEY `equipmentId` (`equipmentId`),
                                  CONSTRAINT `wishlist_items_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
                                  CONSTRAINT `wishlist_items_ibfk_2` FOREIGN KEY (`equipmentId`) REFERENCES `equipments` (`equipmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
