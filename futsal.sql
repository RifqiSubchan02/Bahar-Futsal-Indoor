-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 02, 2020 at 08:22 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `futsal`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` varchar(6) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `j_kelamin` varchar(10) NOT NULL,
  `tgl_lahir` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `gambar` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `password`, `nama`, `j_kelamin`, `tgl_lahir`, `email`, `no_telp`, `alamat`, `gambar`) VALUES
('927991', '6870', 'Herman', 'Laki-laki', '02 Maret 1997', 'rifqisubchan@gmail.com', '999', 'Depok', 'Default_Pic_Admin.png'),
('928150', '3563', 'Rifqi', 'Laki-laki', '02 Juni 1998', 'rifqisubchan@gmail.com', '085695186393', 'Depok', 'messi.jpg'),
('986915', '1533', 'wanda', 'Perempuan', '03 April 1998', 'rifqisubchan@gmail.com', '777', 'bogor', 'Default_Pic_Admin.png');

-- --------------------------------------------------------

--
-- Table structure for table `admin_absensi`
--

CREATE TABLE `admin_absensi` (
  `tanggal` varchar(10) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `keterangan` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin_absensi`
--

INSERT INTO `admin_absensi` (`tanggal`, `nama`, `keterangan`) VALUES
('2020-08-03', 'Herman', 'Absen'),
('2020-08-03', 'Rifqi', 'Hadir'),
('2020-08-03', 'wanda', 'Absen'),
('2020-08-05', 'Herman', 'Absen'),
('2020-08-05', 'Rifqi', 'Hadir'),
('2020-08-05', 'wanda', 'Absen'),
('2020-08-06', 'Herman', 'Absen'),
('2020-08-06', 'Rifqi', 'Hadir'),
('2020-08-06', 'wanda', 'Absen'),
('2020-08-08', 'Herman', 'Absen'),
('2020-08-08', 'Rifqi', 'Hadir'),
('2020-08-08', 'wanda', 'Absen'),
('2020-08-09', 'Herman', 'Absen'),
('2020-08-09', 'Rifqi', 'Hadir'),
('2020-08-09', 'wanda', 'Absen'),
('2020-08-10', 'Herman', 'Absen'),
('2020-08-10', 'Rifqi', 'Hadir'),
('2020-08-10', 'wanda', 'Absen'),
('2020-08-11', 'Herman', 'Absen'),
('2020-08-11', 'Rifqi', 'Hadir'),
('2020-08-11', 'wanda', 'Absen'),
('2020-08-12', 'Herman', 'Absen'),
('2020-08-12', 'Rifqi', 'Hadir'),
('2020-08-12', 'wanda', 'Absen'),
('2020-08-16', 'Herman', 'Absen'),
('2020-08-16', 'Rifqi', 'Hadir'),
('2020-08-16', 'wanda', 'Absen'),
('2020-08-18', 'Herman', 'Absen'),
('2020-08-18', 'Rifqi', 'Hadir'),
('2020-08-18', 'wanda', 'Absen'),
('2020-08-20', 'Herman', 'Absen'),
('2020-08-20', 'Rifqi', 'Hadir'),
('2020-08-20', 'wanda', 'Absen'),
('2020-08-21', 'Herman', 'Absen'),
('2020-08-21', 'Rifqi', 'Hadir'),
('2020-08-21', 'wanda', 'Absen'),
('2020-09-09', 'Herman', 'Absen'),
('2020-09-09', 'Rifqi', 'Hadir'),
('2020-09-09', 'wanda', 'Absen'),
('2020-10-28', 'Herman', 'Absen'),
('2020-10-28', 'Rifqi', 'Hadir'),
('2020-10-28', 'wanda', 'Absen'),
('2020-10-29', 'Herman', 'Absen'),
('2020-10-29', 'Rifqi', 'Hadir'),
('2020-10-29', 'wanda', 'Absen'),
('2020-11-02', 'Herman', 'Absen'),
('2020-11-02', 'Rifqi', 'Hadir'),
('2020-11-02', 'wanda', 'Absen'),
('2020-11-03', 'Herman', 'Absen'),
('2020-11-03', 'Rifqi', 'Hadir'),
('2020-11-03', 'wanda', 'Absen');

-- --------------------------------------------------------

--
-- Table structure for table `data_sewa`
--

CREATE TABLE `data_sewa` (
  `no_faktur` varchar(10) NOT NULL,
  `tanggal_pemesanan` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tanggal_pelunasan` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `id_member` varchar(6) NOT NULL DEFAULT '-',
  `id_admin` varchar(6) NOT NULL,
  `kd_diskon` varchar(4) NOT NULL DEFAULT '-'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_sewa`
--

INSERT INTO `data_sewa` (`no_faktur`, `tanggal_pemesanan`, `tanggal_pelunasan`, `id_member`, `id_admin`, `kd_diskon`) VALUES
('BFI19770', '2020-11-02 18:22:24', '2020-11-02 18:22:24', 'M-266', '928150', 'D-01'),
('BFI23404', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', '-'),
('BFI27941', '2020-11-02 18:22:24', '2020-11-02 18:22:24', 'M-351', '928150', 'D-01'),
('BFI40974', '2020-11-02 18:22:24', '2020-11-02 18:22:24', 'M-956', '928150', 'D-01'),
('BFI44966', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', 'D-02'),
('BFI47824', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', 'D-02'),
('BFI59835', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', '-'),
('BFI64629', '2020-11-02 18:22:24', '2020-11-02 18:22:24', 'M-956', '928150', 'D-01'),
('BFI71319', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', '-'),
('BFI81381', '2020-11-02 18:22:24', '2020-11-02 18:22:24', 'M-078', '928150', 'D-01'),
('BFI89683', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', 'D-02'),
('BFI91369', '2020-11-02 18:22:24', '2020-11-02 18:22:24', '-', '928150', 'D-02');

-- --------------------------------------------------------

--
-- Table structure for table `diskon`
--

CREATE TABLE `diskon` (
  `kd_diskon` varchar(4) NOT NULL,
  `harga_diskon` int(11) NOT NULL,
  `keterangan` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `diskon`
--

INSERT INTO `diskon` (`kd_diskon`, `harga_diskon`, `keterangan`) VALUES
('D-01', 10000, 'Diskon Member'),
('D-02', 3000, 'ID');

-- --------------------------------------------------------

--
-- Table structure for table `lapangan`
--

CREATE TABLE `lapangan` (
  `kd_lapangan` varchar(5) NOT NULL,
  `tipe` varchar(10) NOT NULL,
  `harga` int(10) NOT NULL,
  `gambar` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lapangan`
--

INSERT INTO `lapangan` (`kd_lapangan`, `tipe`, `harga`, `gambar`) VALUES
('L-01', 'Sintetis', 110000, '2499.jpg'),
('L-02', 'Sintetis', 110000, 'Lapangan Camp Nou.jpg'),
('L-03', 'Parquette', 100000, 'icons8-news-30.png'),
('L-04', 'Vinyls', 10000, '81993.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE `member` (
  `id` varchar(6) NOT NULL,
  `nama_tim` varchar(30) NOT NULL,
  `kapten` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `no_hp` varchar(15) NOT NULL,
  `periode` varchar(20) NOT NULL,
  `biaya` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`id`, `nama_tim`, `kapten`, `email`, `no_hp`, `periode`, `biaya`) VALUES
('M-078', 'ee', 'aa', 'rifqisubchan@gmail.com', '777', '2020-08-09', 260000),
('M-266', 'afk', 'ee', 'rifqisubchan@gmail.com', '888', '2020-08-06', 660000),
('M-351', 'AFK', 'Rifqi', 'rifqisubchan@gmail.com', '085695186393', '2020-09-01', 775000),
('M-956', 'Arsenal FC', 'Jack', 'rifqisubchan@gmail.com', '0856', '2020-09-05', 520000);

-- --------------------------------------------------------

--
-- Table structure for table `member_detail`
--

CREATE TABLE `member_detail` (
  `id` varchar(6) NOT NULL,
  `tgl_mulai` varchar(10) NOT NULL,
  `tgl_selesai` varchar(10) NOT NULL,
  `kd_lapangan` varchar(5) NOT NULL,
  `jam_sewa` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `member_detail`
--

INSERT INTO `member_detail` (`id`, `tgl_mulai`, `tgl_selesai`, `kd_lapangan`, `jam_sewa`) VALUES
('M-266', '2020-08-06', '2020-08-27', 'L-01', '16'),
('M-266', '2020-08-06', '2020-08-27', 'L-01', '8'),
('M-078', '2020-08-09', '2020-08-30', 'L-01', '8'),
('M-956', '2020-08-10', '2020-08-31', 'L-02', '10'),
('M-956', '2020-08-15', '2020-08-29', 'L-02', '8'),
('M-956', '2020-09-06', '2020-09-27', 'L-01', '8'),
('M-956', '2020-09-05', '2020-09-26', 'L-01', '8'),
('M-351', '2020-09-01', '2020-09-29', 'L-03', '10'),
('M-351', '2020-09-01', '2020-09-29', 'L-03', '17');

-- --------------------------------------------------------

--
-- Table structure for table `pelunasan_detail`
--

CREATE TABLE `pelunasan_detail` (
  `no_faktur` varchar(10) NOT NULL,
  `kd_lapangan` varchar(5) NOT NULL,
  `tgl_sewa` varchar(10) NOT NULL,
  `jam_sewa` varchar(2) NOT NULL,
  `nama_tim` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `nama_admin` varchar(30) NOT NULL,
  `harga` int(10) NOT NULL,
  `dp` int(10) NOT NULL,
  `sisa_bayar` int(10) NOT NULL,
  `diskon` int(10) NOT NULL,
  `total` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pelunasan_detail`
--

INSERT INTO `pelunasan_detail` (`no_faktur`, `kd_lapangan`, `tgl_sewa`, `jam_sewa`, `nama_tim`, `email`, `nama_admin`, `harga`, `dp`, `sisa_bayar`, `diskon`, `total`) VALUES
('BFI47824', 'L-01', '2020-08-06', '11', 'aa', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 55000, 3000, 52000),
('BFI91369', 'L-02', '2020-08-10', '13', 'aa', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 55000, 3000, 52000),
('BFI89683', 'L-02', '2020-08-10', '18', 'Unindra', 'rifqisubchan@gmail.com', 'Rifqi', 110000, 20000, 90000, 3000, 87000),
('catalans', 'L-02', '2020-11-02', '8', 'catalans', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 55000, 3000, 52000);

-- --------------------------------------------------------

--
-- Table structure for table `pemesanan_detail`
--

CREATE TABLE `pemesanan_detail` (
  `no_faktur` varchar(10) NOT NULL,
  `kd_lapangan` varchar(5) NOT NULL,
  `tgl_sewa` varchar(10) NOT NULL,
  `jam_sewa` varchar(2) NOT NULL,
  `nama_tim` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `nama_admin` varchar(30) NOT NULL,
  `harga` int(10) NOT NULL,
  `dp` int(10) NOT NULL,
  `keterangan` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pemesanan_detail`
--

INSERT INTO `pemesanan_detail` (`no_faktur`, `kd_lapangan`, `tgl_sewa`, `jam_sewa`, `nama_tim`, `email`, `nama_admin`, `harga`, `dp`, `keterangan`) VALUES
('BFI19770', 'L-01', '2020-08-06', '16', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 100000, 100000, 'Member'),
('BFI19770', 'L-01', '2020-08-13', '16', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 100000, 100000, 'Member'),
('BFI19770', 'L-01', '2020-08-20', '16', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 100000, 100000, 'Member'),
('BFI19770', 'L-01', '2020-08-27', '16', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 100000, 100000, 'Member'),
('BFI19770', 'L-01', '2020-08-06', '8', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI19770', 'L-01', '2020-08-13', '8', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI19770', 'L-01', '2020-08-20', '8', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI19770', 'L-01', '2020-08-27', '8', 'afk', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI47824', 'L-01', '2020-08-06', '11', 'aa', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Lunas'),
('BFI81381', 'L-01', '2020-08-09', '8', 'ee', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI81381', 'L-01', '2020-08-16', '8', 'ee', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI81381', 'L-01', '2020-08-23', '8', 'ee', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI81381', 'L-01', '2020-08-30', '8', 'ee', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI91369', 'L-02', '2020-08-10', '13', 'aa', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Lunas'),
('BFI91369', 'L-02', '2020-08-10', '14', 'aa', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI64629', 'L-02', '2020-08-10', '10', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-17', '10', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-24', '10', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-31', '10', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-15', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-22', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI64629', 'L-02', '2020-08-29', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-06', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-13', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-20', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-27', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-05', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-12', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-19', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI40974', 'L-01', '2020-09-26', '8', 'Arsenal FC', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI89683', 'L-02', '2020-08-10', '11', 'Unindra', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI89683', 'L-02', '2020-08-10', '12', 'Unindra', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI89683', 'L-02', '2020-08-10', '18', 'Unindra', 'rifqisubchan@gmail.com', 'Rifqi', 110000, 20000, 'Lunas'),
('BFI27941', 'L-03', '2020-09-01', '10', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI27941', 'L-03', '2020-09-08', '10', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI27941', 'L-03', '2020-09-15', '10', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI27941', 'L-03', '2020-09-22', '10', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI27941', 'L-03', '2020-09-29', '10', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 65000, 65000, 'Member'),
('BFI27941', 'L-03', '2020-09-01', '17', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 90000, 90000, 'Member'),
('BFI27941', 'L-03', '2020-09-08', '17', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 90000, 90000, 'Member'),
('BFI27941', 'L-03', '2020-09-15', '17', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 90000, 90000, 'Member'),
('BFI27941', 'L-03', '2020-09-22', '17', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 90000, 90000, 'Member'),
('BFI27941', 'L-03', '2020-09-29', '17', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 90000, 90000, 'Member'),
('BFI23404', 'L-01', '2020-08-12', '8', 'AFK', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI71319', 'L-01', '2020-08-12', '10', 'EE', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI44966', 'L-02', '2020-11-02', '8', 'catalans', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas'),
('BFI59835', 'L-01', '2020-11-02', '8', 'catalans', 'rifqisubchan@gmail.com', 'Rifqi', 75000, 20000, 'Belum lunas');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `data_sewa`
--
ALTER TABLE `data_sewa`
  ADD PRIMARY KEY (`no_faktur`);

--
-- Indexes for table `diskon`
--
ALTER TABLE `diskon`
  ADD PRIMARY KEY (`kd_diskon`);

--
-- Indexes for table `lapangan`
--
ALTER TABLE `lapangan`
  ADD PRIMARY KEY (`kd_lapangan`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
