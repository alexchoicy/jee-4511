
SET NAMES utf8;
SET time_zone = '+08:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `4511_dev`;
CREATE DATABASE `4511_dev` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `4511_dev`;

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
                            `category_id` int NOT NULL AUTO_INCREMENT,
                            `category_name` varchar(255) NOT NULL,
                            PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `damged_record`;
CREATE TABLE `damged_record` (
                                 `damged_record_id` int NOT NULL AUTO_INCREMENT,
                                 `description` varchar(255) NOT NULL,
                                 `reported_date` datetime NOT NULL,
                                 `related_reservation` int DEFAULT NULL,
                                 `recorded_by` int NOT NULL,
                                 `item_id` int NOT NULL,
                                 PRIMARY KEY (`damged_record_id`),
                                 KEY `item_id` (`item_id`),
                                 KEY `recorded_by` (`recorded_by`),
                                 CONSTRAINT `damged_record_ibfk_1` FOREIGN KEY (`recorded_by`) REFERENCES `user` (`user_id`),
                                 CONSTRAINT `damged_record_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `equipment_item` (`equipment_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `delivery_record`;
CREATE TABLE `delivery_record` (
                                   `delivery_id` int NOT NULL AUTO_INCREMENT,
                                   `delivery_by` int NOT NULL,
                                   `delivery_date` datetime NOT NULL,
                                   PRIMARY KEY (`delivery_id`),
                                   KEY `delivery_by` (`delivery_by`),
                                   CONSTRAINT `delivery_record_ibfk_1` FOREIGN KEY (`delivery_by`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `delivery_reservations`;
CREATE TABLE `delivery_reservations` (
                                         `delivery_reservations_id` int NOT NULL AUTO_INCREMENT,
                                         `delivery_id` int NOT NULL,
                                         `reservation_id` int NOT NULL,
                                         PRIMARY KEY (`delivery_reservations_id`),
                                         KEY `delivery_id` (`delivery_id`),
                                         KEY `reservation_id` (`reservation_id`),
                                         CONSTRAINT `delivery_reservations_ibfk_1` FOREIGN KEY (`delivery_id`) REFERENCES `delivery_record` (`delivery_id`),
                                         CONSTRAINT `delivery_reservations_ibfk_2` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
                             `equipment_id` int NOT NULL AUTO_INCREMENT,
                             `equipment_name` varchar(255) NOT NULL,
                             `description` varchar(255) NOT NULL,
                             `isStaffOnly` tinyint(1) NOT NULL,
                             `isListed` tinyint(1) NOT NULL,
                             `image_Path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `Detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `equipment_categories`;
CREATE TABLE `equipment_categories` (
                                        `categories_id` int NOT NULL AUTO_INCREMENT,
                                        `equipment_id` int NOT NULL,
                                        `category_id` int NOT NULL,
                                        PRIMARY KEY (`categories_id`),
                                        KEY `category_id` (`category_id`),
                                        KEY `equipment_id` (`equipment_id`),
                                        CONSTRAINT `equipment_categories_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
                                        CONSTRAINT `equipment_categories_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `equipment_item`;
CREATE TABLE `equipment_item` (
                                  `equipment_item_id` int NOT NULL AUTO_INCREMENT,
                                  `borrowed_time` int NOT NULL,
                                  `status` int NOT NULL,
                                  `equipment_id` int NOT NULL,
                                  `current_location` int NOT NULL,
                                  `serial_number` varchar(255) NOT NULL,
                                  PRIMARY KEY (`equipment_item_id`),
                                  KEY `current_location` (`current_location`),
                                  KEY `equipment_id` (`equipment_id`),
                                  KEY `status` (`status`),
                                  CONSTRAINT `equipment_item_ibfk_1` FOREIGN KEY (`status`) REFERENCES `equipment_status` (`equipment_status_id`),
                                  CONSTRAINT `equipment_item_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`),
                                  CONSTRAINT `equipment_item_ibfk_3` FOREIGN KEY (`current_location`) REFERENCES `location` (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `equipment_status`;
CREATE TABLE `equipment_status` (
                                    `equipment_status_id` int NOT NULL AUTO_INCREMENT,
                                    `status_name` varchar(255) NOT NULL,
                                    PRIMARY KEY (`equipment_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
                            `location_id` int NOT NULL AUTO_INCREMENT,
                            `location_name` varchar(255) NOT NULL,
                            `location_address` varchar(255) NOT NULL,
                            PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `reservation_items`;
CREATE TABLE `reservation_items` (
                                     `reservation_items_id` int NOT NULL AUTO_INCREMENT,
                                     `reservation_id` int NOT NULL,
                                     `equipment_id` int NOT NULL,
                                     PRIMARY KEY (`reservation_items_id`),
                                     KEY `equipment_id` (`equipment_id`),
                                     KEY `reservation_id` (`reservation_id`),
                                     CONSTRAINT `reservation_items_ibfk_1` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`),
                                     CONSTRAINT `reservation_items_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `reservation_status`;
CREATE TABLE `reservation_status` (
                                      `reservation_status_id` int NOT NULL AUTO_INCREMENT,
                                      `status_name` varchar(255) NOT NULL,
                                      PRIMARY KEY (`reservation_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `role_id` int NOT NULL AUTO_INCREMENT,
                        `role_name` varchar(255) NOT NULL,
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `user_id` int NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                        `first_name` varchar(255) NOT NULL,
                        `last_name` varchar(255) NOT NULL,
                        `role` int NOT NULL,
                        PRIMARY KEY (`user_id`),
                        KEY `role` (`role`),
                        CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `wishlist_items`;
CREATE TABLE `wishlist_items` (
                                  `wishlist_id` int NOT NULL AUTO_INCREMENT,
                                  `user_id` int NOT NULL,
                                  `equipment_id` int NOT NULL,
                                  PRIMARY KEY (`wishlist_id`),
                                  KEY `user_id` (`user_id`),
                                  KEY `equipment_id` (`equipment_id`),
                                  CONSTRAINT `wishlist_items_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
                                  CONSTRAINT `wishlist_items_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
