-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.21-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema genova
--

CREATE DATABASE IF NOT EXISTS genova;
USE genova;

--
-- Definition of table `elezioni`
--

DROP TABLE IF EXISTS `elezioni`;
CREATE TABLE `elezioni` (
  `sez_codmun` int(10) unsigned NOT NULL auto_increment,
  `votanti` int(10) unsigned NOT NULL,
  `votivalidi` int(10) unsigned NOT NULL,
  `percentuale` double NOT NULL,
  PRIMARY KEY  (`sez_codmun`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `elezioni`
--

/*!40000 ALTER TABLE `elezioni` DISABLE KEYS */;
INSERT INTO `elezioni` (`sez_codmun`,`votanti`,`votivalidi`,`percentuale`) VALUES 
 (1,5000,5000,28.474745),
 (2,398,300,31.38383),
 (3,4589,4567,40),
 (4,2345,2310,12.28282),
 (5,7000,6980,30.29292),
 (6,3456,3400,32.338383),
 (7,1000,1000,32.838383),
 (8,546,500,37),
 (9,4506,4500,33.8228);
/*!40000 ALTER TABLE `elezioni` ENABLE KEYS */;


--
-- Definition of table `seggi`
--

DROP TABLE IF EXISTS `seggi`;
CREATE TABLE `seggi` (
  `tot_scrutinati` int(10) unsigned NOT NULL default '0',
  `tot_seggi` int(10) unsigned NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `seggi`
--

/*!40000 ALTER TABLE `seggi` DISABLE KEYS */;
INSERT INTO `seggi` (`tot_scrutinati`,`tot_seggi`) VALUES 
 (45,789);
/*!40000 ALTER TABLE `seggi` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
