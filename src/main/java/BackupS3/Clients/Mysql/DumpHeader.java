package BackupS3.Clients.Mysql;

/**
 * Make dump header
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version  1.0
 */
public class DumpHeader {
    private final String host;
    private final String database;

    public DumpHeader(String host, String database) {
        this.host = host;
        this.database = database;
    }

    @Override
    public String toString() {
        return ""
                + "-- MySQL (Aleksei Vlasov <ru.adideas>)\r\n"
                + "--\r\n"
                + "-- Host: " + host + "    Database: " + database + "\r\n"
                + "-- ------------------------------------------------------\r\n\r\n"
                + "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\r\n"
                + "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\r\n"
                + "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\r\n"
                + "/*!50503 SET NAMES utf8mb4 */;\r\n"
                + "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\r\n"
                + "/*!40103 SET TIME_ZONE='+00:00' */;\r\n"
                + "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\r\n"
                + "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\r\n"
                + "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\r\n"
                + "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\r\n\r\n"
                + "CREATE DATABASE IF NOT EXISTS `" + database + "`;\r\n"
                + "USE `" + database + "`;\r\n";
    }
}
