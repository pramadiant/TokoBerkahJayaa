-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.4.3 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for db_toko_berkah
CREATE DATABASE IF NOT EXISTS `db_toko_berkah` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_toko_berkah`;

-- Dumping structure for table db_toko_berkah.tb_barang
CREATE TABLE IF NOT EXISTS `tb_barang` (
  `id_barang` varchar(10) NOT NULL,
  `id_kategori` int NOT NULL,
  `nama_barang` varchar(100) NOT NULL,
  `satuan` varchar(20) NOT NULL,
  `harga_beli` double NOT NULL DEFAULT '0',
  `harga_jual` double NOT NULL,
  `stok` int NOT NULL,
  PRIMARY KEY (`id_barang`),
  KEY `id_kategori` (`id_kategori`),
  CONSTRAINT `tb_barang_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `tb_kategori` (`id_kategori`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_barang: ~37 rows (approximately)
INSERT INTO `tb_barang` (`id_barang`, `id_kategori`, `nama_barang`, `satuan`, `harga_beli`, `harga_jual`, `stok`) VALUES
	('BRG001', 4, 'Aqua Air Mineral Botol 600ml', 'Botol', 2200, 3500, 120),
	('BRG002', 3, 'Indomie Mi Goreng Spesial 85g', 'Bungkus', 2600, 3100, 250),
	('BRG003', 7, 'Chitato Sapi Panggang 68g', 'Pcs', 9000, 11500, 45),
	('BRG004', 6, 'Bimoli Minyak Goreng Pouch 2L', 'Pcs', 32500, 36500, 30),
	('BRG005', 6, 'Beras Setra Ramos 5Kg', 'Kg', 66000, 74000, 15),
	('BRG006', 7, 'Qtela Keripik Singkong Balado 185g', 'Pcs', 14000, 16500, 25),
	('BRG007', 5, 'Pepsodent Pencegah Gigi Berlubang 190g', 'Pcs', 11500, 14500, 60),
	('BRG008', 2, 'Facial Tissue Non Perfumed 250s', 'Pcs', 10000, 13500, 40),
	('BRG009', 2, 'Sunlight Jeruk Nipis Sabun Cuci Piring 755ml', 'Pcs', 14500, 17500, 35),
	('BRG010', 7, 'SilverQueen Chocolate Cashew 62g', 'Pcs', 13000, 16500, 50),
	('BRG011', 4, 'Bear Brand Susu Steril 189ml', 'Botol', 9000, 10500, 75),
	('BRG012', 1, 'Tolak Angin Cair Sido Muncul 15ml', 'Pcs', 3000, 4000, 90),
	('BRG013', 2, 'Rinso Anti Noda Deterjen Bubuk 770g', 'Pcs', 21000, 25000, 27),
	('BRG014', 7, 'Kopiko Coffee Candy Blister 24g', 'Pcs', 3500, 4500, 100),
	('BRG015', 4, 'Ultra Milk Susu UHT Coklat 250ml', 'Botol', 4500, 6000, 90),
	('BRG016', 5, 'Biore Body Foam Guard Pouch 450ml', 'Pcs', 22000, 28500, 32),
	('BRG017', 3, 'Sari Roti Roti Tawar Spesial 400g', 'Pcs', 13500, 16000, 18),
	('BRG018', 4, 'Teh Pucuk Harum 350ml', 'Botol', 2000, 3500, 150),
	('BRG019', 3, 'Pop Mie Rasa Ayam Bawang 75g', 'Bungkus', 3800, 4500, 80),
	('BRG020', 7, 'Cadbury Dairy Milk 62g', 'Pcs', 12500, 15000, 40),
	('BRG021', 5, 'Lifebuoy Sabun Cair Total 10 450ml', 'Pcs', 20000, 25000, 50),
	('BRG022', 6, 'Gula Pasir Lokal 1kg', 'Pcs', 14500, 16000, 100),
	('BRG023', 6, 'Segitiga Biru Tepung Terigu 1kg', 'Pcs', 11000, 13000, 40),
	('BRG024', 4, 'Pocari Sweat Botol 500ml', 'Botol', 6000, 7500, 70),
	('BRG025', 7, 'Oreo Biscuit Vanilla 133g', 'Pcs', 7500, 9500, 65),
	('BRG026', 5, 'Close Up Pasta Gigi Fresh Green 160g', 'Pcs', 13000, 16500, 45),
	('BRG027', 2, 'Baygon Aerosol Citrus 600ml', 'Pcs', 33000, 39000, 20),
	('BRG028', 1, 'Panadol Extra 10 Kaplet', 'Pcs', 9000, 11500, 49),
	('BRG029', 6, 'ABC Kecap Manis Botol 135ml', 'Botol', 7000, 8500, 60),
	('BRG030', 7, 'Roma Kelapa Biskuit 300g', 'Pcs', 8500, 10500, 55),
	('BRG031', 4, 'Cimory Yogurt Drink Blueberry 250ml', 'Botol', 7000, 9000, 40),
	('BRG032', 2, 'Mama Lemon Jeruk Nipis 680ml', 'Pcs', 11000, 13500, 35),
	('BRG033', 5, 'Rexona Men Deodorant Roll On 50ml', 'Pcs', 16000, 21000, 30),
	('BRG034', 7, 'Kraft Oreo Sandwich Minis Chocolate 67g', 'Pcs', 6000, 7500, 45),
	('BRG035', 3, 'Sedap Mie Instan Soto 75g', 'Bungkus', 2500, 3000, 200),
	('BRG036', 1, 'Madu TJ ', 'Botol', 12000, 18000, 22),
	('BRG037', 8, 'Roti keras', 'Dus', 20000, 30000, 30);

-- Dumping structure for table db_toko_berkah.tb_customer
CREATE TABLE IF NOT EXISTS `tb_customer` (
  `id_customer` varchar(10) NOT NULL,
  `nama_customer` varchar(100) NOT NULL,
  `alamat` text,
  `telepon` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id_customer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_customer: ~2 rows (approximately)
INSERT INTO `tb_customer` (`id_customer`, `nama_customer`, `alamat`, `telepon`) VALUES
	('CUST001', 'Budi Gunawan', 'Viktor', '087721831931'),
	('CUST002', 'Adi Pramadiato ', 'Viktor Pamulang', '087832223290');

-- Dumping structure for table db_toko_berkah.tb_detail_penjualan
CREATE TABLE IF NOT EXISTS `tb_detail_penjualan` (
  `id_detail` int NOT NULL AUTO_INCREMENT,
  `id_jual` int NOT NULL,
  `id_barang` varchar(10) NOT NULL,
  `harga_satuan` double NOT NULL,
  `jumlah_beli` int NOT NULL,
  `subtotal` double NOT NULL,
  PRIMARY KEY (`id_detail`),
  KEY `id_jual` (`id_jual`),
  KEY `id_barang` (`id_barang`),
  CONSTRAINT `tb_detail_penjualan_ibfk_1` FOREIGN KEY (`id_jual`) REFERENCES `tb_penjualan` (`id_jual`) ON DELETE CASCADE,
  CONSTRAINT `tb_detail_penjualan_ibfk_2` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id_barang`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_detail_penjualan: ~5 rows (approximately)
INSERT INTO `tb_detail_penjualan` (`id_detail`, `id_jual`, `id_barang`, `harga_satuan`, `jumlah_beli`, `subtotal`) VALUES
	(1, 1, 'BRG012', 4000, 10, 40000),
	(2, 2, 'BRG012', 4000, 20, 80000),
	(3, 2, 'BRG028', 11500, 1, 11500),
	(4, 2, 'BRG036', 18000, 1, 18000),
	(5, 2, 'BRG013', 25000, 1, 25000);

-- Dumping structure for table db_toko_berkah.tb_kategori
CREATE TABLE IF NOT EXISTS `tb_kategori` (
  `id_kategori` int NOT NULL AUTO_INCREMENT,
  `nama_kategori` varchar(50) NOT NULL,
  PRIMARY KEY (`id_kategori`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_kategori: ~8 rows (approximately)
INSERT INTO `tb_kategori` (`id_kategori`, `nama_kategori`) VALUES
	(1, 'Farmasi'),
	(2, 'Kebutuhan Rumah'),
	(3, 'Makanan'),
	(4, 'Minuman'),
	(5, 'Personal Care'),
	(6, 'Sembako'),
	(7, 'Snack'),
	(8, 'Cemilan');

-- Dumping structure for table db_toko_berkah.tb_penjualan
CREATE TABLE IF NOT EXISTS `tb_penjualan` (
  `id_jual` int NOT NULL AUTO_INCREMENT,
  `no_faktur` varchar(20) NOT NULL,
  `tgl_transaksi` date NOT NULL,
  `id_customer` varchar(10) DEFAULT NULL,
  `total_bayar` double NOT NULL,
  `uang_bayar` double NOT NULL DEFAULT '0',
  `kembalian` double NOT NULL DEFAULT '0',
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_jual`),
  KEY `id_customer` (`id_customer`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `tb_penjualan_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `tb_customer` (`id_customer`),
  CONSTRAINT `tb_penjualan_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `tb_user` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_penjualan: ~2 rows (approximately)
INSERT INTO `tb_penjualan` (`id_jual`, `no_faktur`, `tgl_transaksi`, `id_customer`, `total_bayar`, `uang_bayar`, `kembalian`, `id_user`) VALUES
	(1, 'FKTR001', '2026-06-07', 'CUST001', 40000, 0, 0, 2),
	(2, 'FKTR002', '2026-06-08', 'CUST001', 134500, 0, 0, 2);

-- Dumping structure for table db_toko_berkah.tb_user
CREATE TABLE IF NOT EXISTS `tb_user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `level` enum('Admin','Petugas') NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table db_toko_berkah.tb_user: ~3 rows (approximately)
INSERT INTO `tb_user` (`id_user`, `username`, `password`, `nama_lengkap`, `level`) VALUES
	(1, 'admin', '123', 'Administrator Utama', 'Admin'),
	(2, 'kasir1', '123', 'Bambang Kasir', 'Petugas'),
	(3, 'kasir2', '1234', 'Ratna Rahayu', 'Petugas');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
