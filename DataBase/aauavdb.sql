-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 05, 2013 at 06:10 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `aauavdb`
--
CREATE DATABASE `aauavdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `aauavdb`;

-- --------------------------------------------------------

--
-- Table structure for table `droneinfo`
--

CREATE TABLE IF NOT EXISTS `droneinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `droneid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `Height` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `speed` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `startpointx` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `startpointy` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `currentpointx` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `currentpointy` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `endpointx` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `endpointy` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `distance` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `distancemade` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `dbattery` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `pbattery` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `dtype` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `droneid` (`droneid`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `droneinfo`
--

INSERT INTO `droneinfo` (`id`, `droneid`, `city`, `Height`, `speed`, `startpointx`, `startpointy`, `currentpointx`, `currentpointy`, `endpointx`, `endpointy`, `distance`, `distancemade`, `dbattery`, `pbattery`, `dtype`, `status`) VALUES
(2, '123456', 'Reshon_Lezion', '65', '80', '31.456214', '32.456214', '31.960637', '34.807949', '37.456214', '32.456214', '500', '54', '30', '40', 'police', 'on'),
(4, '839411021', 'Mazkeret Batya', '87.0', '3', '31.852505008194466', '34.85281586322232', '31.852520872753413', '34.852860691476984', '0.0', '0.0', '0.0', '0.0', '0', '71', 'gamer', 'on'),
(8, '272898643', 'Ramat Gan', '53.0', '0', '32.089528004887256', '34.80195814024025', '32.08963118044852', '34.80198876641601', '32.090359842111376', '34.80318043380976', '138.50494384765625', '138.50494384765625', '0', '66', 'gamer', 'on'),
(9, '397470748', 'null', '0.0', '0', '0.0', '0.0', '0.0', '0.0', '31.853823211810724', '34.854514226317406', '0.0', '0.0', '0', '61', 'gamer', 'on'),
(10, '360464890', 'null', '0.0', '0', '0.0', '0.0', '0.0', '0.0', '31.853823211810724', '34.854514226317406', '5086438.5', '5086438.5', '0', '57', 'gamer', 'on');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
