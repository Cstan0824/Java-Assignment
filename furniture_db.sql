-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 12, 2024 at 04:40 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `furniture_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `Admin_ID` varchar(100) NOT NULL,
  `Admin_Name` varchar(100) NOT NULL,
  `Admin_Password` varchar(255) NOT NULL,
  `Admin_Email` varchar(100) NOT NULL,
  `OTP_Code` varchar(6) DEFAULT NULL,
  `OTP_Expiry` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `Admin_Reg_Date` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`Admin_ID`, `Admin_Name`, `Admin_Password`, `Admin_Email`, `OTP_Code`, `OTP_Expiry`, `Admin_Reg_Date`) VALUES
('A00001', 'SUPERADMIN', 'Java@123', 'tarumtmoviesociety@gmail.com', '572283', '2024-09-12 14:54:12.000000', '2024-09-02'),
('A00002', 'Jane Smith', 'password456', 'jane.smithFake@gmail.com', '654321', '2024-09-12 13:52:44.205624', '2024-08-28'),
('A00003', 'Aliau The Monkey', 'monkeyLiau#05', 'monkeyliau123@gmail.com', NULL, '2024-09-12 13:57:23.274058', '2024-09-02'),
('A00004', 'Orang Utan Liau', 'orangUtan#123', 'notMonkeybutLiau@gmail.com', NULL, '2024-09-12 13:56:55.413536', '2024-09-02'),
('A00005', 'Cstan Not Pro', 'cstan@Noob', 'Cstan@gmail.com', NULL, '2024-09-12 14:00:12.385670', '2024-08-28');

-- --------------------------------------------------------

--
-- Table structure for table `autoreplenishment`
--

CREATE TABLE `autoreplenishment` (
  `AutoReplenishment_ID` int(11) NOT NULL,
  `Item_ID` int(11) DEFAULT NULL,
  `Item_Threshold` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `autoreplenishment`
--

INSERT INTO `autoreplenishment` (`AutoReplenishment_ID`, `Item_ID`, `Item_Threshold`) VALUES
(1, 1, 200),
(2, 7, 250),
(3, 12, 300),
(4, 19, 205),
(5, 21, 120),
(6, 28, 300),
(7, 32, 100),
(8, 36, 200),
(9, 44, 150),
(10, 49, 200),
(11, 13, 250),
(12, 3, 150),
(13, 22, 250),
(14, 10, 260);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `Item_ID` int(11) NOT NULL,
  `Item_Category_ID` int(11) DEFAULT NULL,
  `Vendor_ID` varchar(100) DEFAULT NULL,
  `Item_Name` varchar(100) DEFAULT NULL,
  `Item_Desc` text DEFAULT NULL,
  `Item_Quantity` int(11) DEFAULT NULL,
  `Item_Price` decimal(10,2) DEFAULT NULL,
  `Item_Location` varchar(255) DEFAULT NULL,
  `Item_Created_By` varchar(100) DEFAULT NULL,
  `Item_Modified_By` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`Item_ID`, `Item_Category_ID`, `Vendor_ID`, `Item_Name`, `Item_Desc`, `Item_Quantity`, `Item_Price`, `Item_Location`, `Item_Created_By`, `Item_Modified_By`) VALUES
(1, 3, 'V00003', 'FrostMaster Refrigerator', 'Energy-efficient fridge with cooling control', 270, '2499.99', 'Aisle 1', 'A00001', 'A00001'),
(2, 3, 'V00003', 'CleanStream Dishwasher', 'Quiet dishwasher with customizable wash cycles', 300, '1999.99', 'Aisle 2', 'A00001', 'A00001'),
(3, 3, 'V00003', 'QuickWave Microwave', 'Microwave with pre-programmed cooking modes', 300, '399.99', 'Aisle 3', 'A00001', 'A00001'),
(4, 3, 'V00003', 'ProBake Oven', 'Multifunction oven with precise temp. control', 300, '1499.99', 'Aisle 4', 'A00001', 'A00001'),
(5, 3, 'V00003', 'BrewPro Coffee Maker', 'Programmable coffee maker for a perfect brew', 300, '299.99', 'Aisle 5', 'A00001', 'A00001'),
(6, 8, 'V00008', 'VisionMax Smart TV', '4K smart TV with vibrant colors and apps', 300, '3999.99', 'Aisle 1', 'A00001', 'A00001'),
(7, 8, 'V00008', 'EchoWave Speaker System', 'Wireless speakers with multi-room syncing', 300, '999.99', 'Aisle 2', 'A00001', 'A00001'),
(8, 8, 'V00008', 'PowerStation Laptop Charger', 'Fast charging portable charger with ports', 300, '149.99', 'Aisle 3', 'A00001', 'A00001'),
(9, 8, 'V00008', 'ChargeNest Smartphone Dock', 'Upright charging dock for desk or bedside', 300, '79.99', 'Aisle 4', 'A00001', 'A00001'),
(10, 8, 'V00008', 'TabletX Pro', 'Lightweight tablet with powerful processor', 300, '1999.99', 'Aisle 5', 'A00001', 'A00001'),
(11, 2, 'V00002', 'GlowEase Bedside Lamp', 'Dimmable bedside lamp with warm lighting', 300, '199.99', 'Aisle 6', 'A00001', 'A00001'),
(12, 2, 'V00002', 'RiseAlert Alarm Clock', 'Alarm clock with sunrise simulation feature', 2100, '149.99', 'Aisle 7', 'A00001', 'A00001'),
(13, 2, 'V00002', 'CozyHeat Electric Blanket', 'Plush blanket with adjustable heat settings', 300, '249.99', 'Aisle 8', 'A00001', 'A00001'),
(14, 2, 'V00002', 'PureBreathe Air Purifier', 'Compact air purifier with HEPA filter', 300, '499.99', 'Aisle 9', 'A00001', 'A00001'),
(15, 2, 'V00002', 'HydraMist Humidifier', 'Humidifier adding moisture for better sleep', 300, '299.99', 'Aisle 10', 'A00001', 'A00001'),
(16, 1, 'V00001', 'SpinPro Washing Machine', 'Efficient washer with large load capacity', 300, '1699.99', 'Aisle 11', 'A00001', 'A00001'),
(17, 1, 'V00001', 'DryFlow Dryer', 'Quick and energy-efficient clothes dryer', 300, '1399.99', 'Aisle 12', 'A00001', 'A00001'),
(18, 1, 'V00001', 'SweepMax Vacuum Cleaner', 'Vacuum for carpets and hard floors', 300, '799.99', 'Aisle 13', 'A00001', 'A00001'),
(19, 1, 'V00001', 'IronSmooth Pro', 'Steam iron with adjustable heat settings', 300, '149.99', 'Aisle 14', 'A00001', 'A00001'),
(20, 1, 'V00001', 'SteamJet Cleaner', 'Compact steam cleaner for various surfaces', 300, '499.99', 'Aisle 15', 'A00001', 'A00001'),
(21, 9, 'V00009', 'LumiGlow Smart Bulbs', 'Smart bulbs with app and voice control', 300, '89.99', 'Aisle 16', 'A00001', 'A00001'),
(22, 9, 'V00009', 'BrightWay LED Floor Lamp', 'Adjustable floor lamp with LED lighting', 300, '299.99', 'Aisle 17', 'A00001', 'A00001'),
(23, 9, 'V00009', 'SmartPlug Connect', 'Remote-control smart plug for appliances', 300, '59.99', 'Aisle 18', 'A00001', 'A00001'),
(24, 9, 'V00009', 'MotionSense Wall Light', 'Motion-activated light for added security', 300, '199.99', 'Aisle 19', 'A00001', 'A00001'),
(25, 9, 'V00009', 'DimmerLux Wall Lamp', 'Touch dimmer wall lamp with soft lighting', 300, '149.99', 'Aisle 20', 'A00001', 'A00001'),
(26, 4, 'V00004', 'SonicClean Electric Toothbrush', 'Rechargeable toothbrush with multiple modes', 300, '299.99', 'Aisle 21', 'A00001', 'A00001'),
(27, 4, 'V00004', 'BlowWave Hair Dryer', 'Fast-drying hair dryer with ionic tech', 300, '249.99', 'Aisle 22', 'A00001', 'A00001'),
(28, 4, 'V00004', 'WarmDry Heated Towel Rack', 'Wall-mounted rack for warm towels', 1500, '599.99', 'Aisle 23', 'A00001', 'A00001'),
(29, 4, 'V00004', 'AquaHeat Bathroom Heater', 'Compact heater for quick bathroom warm-up', 300, '399.99', 'Aisle 24', 'A00001', 'A00001'),
(30, 4, 'V00004', 'SmoothTrim Electric Shaver', 'Precision shaver with multiple attachments', 300, '199.99', 'Aisle 25', 'A00001', 'A00001'),
(31, 10, 'V00010', 'CoolBreeze Air Conditioner', 'Energy-efficient AC with smart controls', 300, '2499.99', 'Aisle 26', 'A00001', 'A00001'),
(32, 10, 'V00010', 'HeatWave Electric Heater', 'Portable heater with adjustable temperature', 298, '599.99', 'Aisle 27', 'A00001', 'A00001'),
(33, 10, 'V00010', 'DryAir Dehumidifier', 'Compact dehumidifier for excess moisture', 300, '799.99', 'Aisle 28', 'A00001', 'A00001'),
(34, 10, 'V00010', 'WindFlow Ceiling Fan', 'Quiet ceiling fan with reversible blades', 300, '999.99', 'Aisle 29', 'A00001', 'A00001'),
(35, 10, 'V00010', 'FreshAir Air Purifier', 'Advanced air purifier with multi-stage filter', 300, '1299.99', 'Aisle 30', 'A00001', 'A00001'),
(36, 5, 'V00005', 'ShoeNest Rack', 'Adjustable shoe rack for entryway', 284, '199.99', 'Aisle 31', 'A00001', 'A00001'),
(37, 5, 'V00005', 'ClosetMaster Organizers', 'Modular closet organizers for storage', 300, '399.99', 'Aisle 32', 'A00001', 'A00001'),
(38, 5, 'V00005', 'StackSmart Bins', 'Stackable storage bins with clear lids', 300, '99.99', 'Aisle 33', 'A00001', 'A00001'),
(39, 5, 'V00005', 'UnderNest Bed Storage', 'Rolling storage boxes for under-bed use', 300, '149.99', 'Aisle 34', 'A00001', 'A00001'),
(40, 5, 'V00005', 'ShelvPro Units', 'Adjustable shelving units for organization', 300, '599.99', 'Aisle 35', 'A00001', 'A00001'),
(41, 6, 'V00006', 'FocusLite Desk Lamp', 'Adjustable desk lamp for brightness control', 300, '129.99', 'Aisle 36', 'A00001', 'A00001'),
(42, 6, 'V00006', 'PrintPro Wireless Printer', 'Wireless printer for easy printing', 300, '799.99', 'Aisle 37', 'A00001', 'A00001'),
(43, 6, 'V00006', 'ErgoFlex Office Chair', 'Ergonomic office chair with lumbar support', 300, '999.99', 'Aisle 38', 'A00001', 'A00001'),
(44, 6, 'V00006', 'LiftMate Laptop Stand', 'Adjustable laptop stand for ergonomic viewing', 300, '149.99', 'Aisle 39', 'A00001', 'A00001'),
(45, 6, 'V00006', 'PowerGuard Surge Protector', 'Surge protector with multiple outlets', 300, '79.99', 'Aisle 40', 'A00001', 'A00001'),
(46, 7, 'V00007', 'GrillMaster Electric Grill', 'Compact electric grill for outdoor BBQs', 300, '999.99', 'Aisle 41', 'A00001', 'A00001'),
(47, 7, 'V00007', 'HeatWave Patio Heater', 'Freestanding outdoor heater with adjustable heat', 300, '1499.99', 'Aisle 42', 'A00001', 'A00001'),
(48, 7, 'V00007', 'LawnEdge Mower', 'Electric lawn mower with adjustable height', 300, '1999.99', 'Aisle 43', 'A00001', 'A00001'),
(49, 7, 'V00007', 'SolarBright Outdoor Lighting', 'Solar-powered lights for ambient outdoor lighting', 300, '399.99', 'Aisle 44', 'A00001', 'A00001'),
(50, 7, 'V00007', 'AquaStream Hose Reel', 'Retractable garden hose reel for easy watering', 300, '299.99', 'Aisle 45', 'A00001', 'A00001');

-- --------------------------------------------------------

--
-- Table structure for table `item_category`
--

CREATE TABLE `item_category` (
  `Item_Category_ID` int(11) NOT NULL,
  `Item_Type` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item_category`
--

INSERT INTO `item_category` (`Item_Category_ID`, `Item_Type`) VALUES
(1, 'Cleaning'),
(2, 'Bedroom'),
(3, 'Kitchen'),
(4, 'Bathroom'),
(5, 'Storage'),
(6, 'Office'),
(7, 'Gardening'),
(8, 'Electronics'),
(9, 'Lighting'),
(10, 'Air Quality');

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE `request` (
  `Request_ID` int(128) NOT NULL,
  `Retailer_ID` varchar(128) NOT NULL,
  `Retailer_Name` varchar(128) NOT NULL,
  `Retailer_Email` varchar(128) NOT NULL,
  `Retailer_Password` varchar(128) NOT NULL,
  `Retailer_Address` varchar(128) NOT NULL,
  `Status` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `request`
--

INSERT INTO `request` (`Request_ID`, `Retailer_ID`, `Retailer_Name`, `Retailer_Email`, `Retailer_Password`, `Retailer_Address`, `Status`) VALUES
(1, 'R00006', 'Millionare', 'blabla@gmail.com', '$$$$$$$$', 'Ba Sang Bakuteh', 'Approved'),
(2, 'R00008', 'Siti Harrison', 'harr@gmail.com', 'harrison@123', 'Johor Bahru, Selangor', 'Rejected'),
(3, 'R00009', 'Zwaho', 'chinjc-wm23@student.tarc.edu.my', 'java@123', 'TARUMT', 'Approved'),
(4, 'R00012', 'Kong Ji Shou', 'chinjunchen@gmail.com', 'kjs@2005', 'Tepi Sungai, Klang', 'Approved'),
(5, 'R00013', 'Low Zi Ling', 'lowzl-wm23@student.tarc.edu.my', 'zlinggg@DO', 'Puchong, Selangor', 'Pending'),
(6, 'R00014', 'Wong Shun Bin', 'wongsb-wm23@student.tarc.edu.my', 'jack@wong2002', 'Kepong', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `retailer`
--

CREATE TABLE `retailer` (
  `Retailer_ID` varchar(100) NOT NULL,
  `Retailer_Name` varchar(100) NOT NULL,
  `Retailer_Password` varchar(255) NOT NULL,
  `Retailer_Email` varchar(100) NOT NULL,
  `OTP_Code` varchar(6) DEFAULT NULL,
  `OTP_Expiry` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `Retailer_Reg_Date` date NOT NULL DEFAULT curdate(),
  `Retailer_Address` varchar(255) NOT NULL,
  `Retailer_Created_By` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `retailer`
--

INSERT INTO `retailer` (`Retailer_ID`, `Retailer_Name`, `Retailer_Password`, `Retailer_Email`, `OTP_Code`, `OTP_Expiry`, `Retailer_Reg_Date`, `Retailer_Address`, `Retailer_Created_By`) VALUES
('R00001', 'Jeremy Chin', 'Java@123', 'chinjc-wm23@student.tarc.edu.my', NULL,'2024-09-02 10:59:32.819874', '2024-09-12', 'TARUMT', 'A00001'),
('R00002', 'Angela Fisher', 'agf@1234', 'littlekathleen@yahoo.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Shah Alam', 'A00001'),
('R00003', 'Maureen Martinez', 'mauMar#2', 'edward79@yahoo.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Melaka', 'A00001'),
('R00004', 'Ryan Lowe', 'R004@pass', 'davidmoss@estes-mills.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Kuching', 'A00001'),
('R00005', 'Bryan Hanna', 'bryHan@123', 'kmay@yahoo.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Kota Kinabalu', 'A00001'),
('R00006', 'Stephen Terrell', 'step@ter', 'chelseawilson@gmail.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Petaling Jaya', 'A00001'),
('R00007', 'Doris Wilson', '123@Doris', 'umiller@gmail.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Subang Jaya', 'A00001'),
('R00008', 'Michael Crosby', 'crosby#1', 'markmassey@rocha-franklin.net', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Kuala Lumpur', 'A00001'),
('R00009', 'Andre Hicks', 'andre@005', 'ihale@yahoo.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Kuala Lumpur', 'A00001'),
('R00010', 'Anthony Harper', 'ant_har1', 'kathyschultz@willis.org', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'George Town', 'A00001'),
('R00011', 'Samuel Aguilar', 'sam#123', 'cynthia05@yahoo.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Johor Bahru', 'A00001'),
('R00012', 'Kong Ji Shou', '#chin123lol', 'chinjunchen@gmail.com', NULL, '2024-09-02 10:59:32.819874', '2024-09-12', 'Tepi Sungai, Klang', 'A00001');

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE `schedule` (
  `Schedule_ID` int(11) NOT NULL,
  `Doc_No` varchar(20) NOT NULL,
  `Vehicle_Plate` varchar(15) NOT NULL,
  `Time_Slot` time NOT NULL,
  `Schedule_Date` date NOT NULL,
  `Status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`Schedule_ID`, `Doc_No`, `Vehicle_Plate`, `Time_Slot`, `Schedule_Date`, `Status`) VALUES
(2, 'DO00004', 'WYS3378', '12:00:00', '2024-09-12', 1),
(3, 'DO00007', 'XYZ789', '10:00:00', '2024-09-13', 0),
(4, 'DO00009', 'BJW3938', '14:00:00', '2024-09-25', 0),
(5, 'DO00005', 'ABC123', '10:00:00', '2024-09-27', 0),
(6, 'DO00011', 'BJW3938', '16:00:00', '2024-09-30', 0);

-- --------------------------------------------------------

--
-- Table structure for table `sys_runno`
--

CREATE TABLE `sys_runno` (
  `Sys_Prefix` varchar(10) NOT NULL,
  `Sys_Next` int(11) NOT NULL,
  `Sys_Desc` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sys_runno`
--

INSERT INTO `sys_runno` (`Sys_Prefix`, `Sys_Next`, `Sys_Desc`) VALUES
('PO', 7, 'Purchase Order'),
('GRN', 8, 'Good Received Notes'),
('SO', 24, 'Sales Order'),
('DO', 13, 'Delivery Order'),
('A', 6, 'Admin'),
('R', 15, 'Retailer'),
('V', 11, 'Vendor');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `Item_ID` int(11) NOT NULL,
  `Doc_No` varchar(20) NOT NULL,
  `Source_Doc_No` varchar(20) DEFAULT NULL,
  `Transaction_Date` date NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Transaction_Mode` int(11) NOT NULL,
  `Transaction_Recipient` varchar(100) DEFAULT NULL,
  `Transaction_Created_By` varchar(100) DEFAULT NULL,
  `Transaction_Modified_By` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`Item_ID`, `Doc_No`, `Source_Doc_No`, `Transaction_Date`, `Quantity`, `Transaction_Mode`, `Transaction_Recipient`, `Transaction_Created_By`, `Transaction_Modified_By`) VALUES
(1, 'DO00004', 'SO00003', '2024-09-12', 15, 1, 'R00001', 'A00001', 'A00001'),
(1, 'DO00012', 'SO00022', '2024-09-12', 5, 1, 'R00012', 'A00001', 'A00001'),
(1, 'PO00005', NULL, '2024-09-12', 100, 0, 'V00003', 'A00001', 'A00001'),
(1, 'SO00003', 'SO00003', '2024-09-12', 15, 1, 'R00001', 'R00001', 'R00001'),
(1, 'SO00022', 'SO00022', '2024-09-12', 5, 1, 'R00012', 'R00012', 'R00012'),
(2, 'SO00011', 'SO00011', '2024-09-12', 10, 1, 'R00006', 'R00006', 'R00006'),
(3, 'SO00006', 'SO00006', '2024-09-12', 10, 1, 'R00002', 'R00002', 'R00002'),
(5, 'SO00011', 'SO00011', '2024-09-12', 20, 1, 'R00006', 'R00006', 'R00006'),
(6, 'DO00007', 'SO00012', '2024-09-12', 10, 1, 'R00007', 'A00001', 'A00001'),
(6, 'SO00012', 'SO00012', '2024-09-12', 10, 1, 'R00007', 'R00007', 'R00007'),
(6, 'SO00023', 'SO00023', '2024-09-12', 30, 1, 'R00012', 'R00012', 'R00012'),
(7, 'SO00006', 'SO00006', '2024-09-12', 5, 1, 'R00002', 'R00002', 'R00002'),
(8, 'DO00012', 'SO00022', '2024-09-12', 10, 1, 'R00012', 'A00001', 'A00001'),
(8, 'SO00009', 'SO00009', '2024-09-12', 10, 1, 'R00004', 'R00004', 'R00004'),
(8, 'SO00022', 'SO00022', '2024-09-12', 10, 1, 'R00012', 'R00012', 'R00012'),
(10, 'DO00007', 'SO00012', '2024-09-12', 100, 1, 'R00007', 'A00001', 'A00001'),
(10, 'SO00012', 'SO00012', '2024-09-12', 100, 1, 'R00007', 'R00007', 'R00007'),
(11, 'DO00008', 'SO00020', '2024-09-12', 10, 1, 'R00011', 'A00001', 'A00001'),
(11, 'SO00020', 'SO00020', '2024-09-12', 10, 1, 'R00011', 'R00011', 'R00011'),
(12, 'GRN00005', 'PO00003', '2024-09-12', 600, 0, 'V00002', 'A00001', 'A00001'),
(12, 'GRN00006', 'PO00004', '2024-09-12', 600, 0, 'V00002', 'A00001', 'A00001'),
(12, 'PO00003', NULL, '2024-09-12', 600, 0, 'V00002', 'System', 'System'),
(12, 'PO00004', NULL, '2024-09-12', 600, 0, 'V00002', 'System', 'System'),
(12, 'SO00006', 'SO00006', '2024-09-12', 100, 1, 'R00002', 'R00002', 'R00002'),
(12, 'SO00008', 'SO00008', '2024-09-12', 10, 1, 'R00003', 'R00003', 'R00003'),
(13, 'DO00005', 'SO00007', '2024-09-12', 20, 1, 'R00003', 'A00001', 'A00001'),
(13, 'SO00007', 'SO00007', '2024-09-12', 20, 1, 'R00003', 'R00003', 'R00003'),
(14, 'DO00010', 'SO00015', '2024-09-12', 5, 1, 'R00008', 'A00001', 'A00001'),
(14, 'SO00015', 'SO00015', '2024-09-12', 5, 1, 'R00008', 'R00008', 'R00008'),
(15, 'DO00006', 'SO00018', '2024-09-12', 90, 1, 'R00010', 'A00001', 'A00001'),
(15, 'SO00018', 'SO00018', '2024-09-12', 90, 1, 'R00010', 'R00010', 'R00010'),
(16, 'SO00011', 'SO00011', '2024-09-12', 100, 1, 'R00006', 'R00006', 'R00006'),
(17, 'SO00008', 'SO00008', '2024-09-12', 5, 1, 'R00003', 'R00003', 'R00003'),
(18, 'PO00005', NULL, '2024-09-12', 200, 0, 'V00001', 'A00001', 'A00001'),
(20, 'SO00013', 'SO00013', '2024-09-12', 50, 1, 'R00008', 'R00008', 'R00008'),
(21, 'DO00009', 'SO00016', '2024-09-12', 40, 1, 'R00009', 'A00001', 'A00001'),
(21, 'SO00010', 'SO00010', '2024-09-12', 5, 1, 'R00005', 'R00005', 'R00005'),
(21, 'SO00016', 'SO00016', '2024-09-12', 40, 1, 'R00009', 'R00009', 'R00009'),
(22, 'DO00010', 'SO00015', '2024-09-12', 15, 1, 'R00008', 'A00001', 'A00001'),
(22, 'PO00006', NULL, '2024-09-12', 120, 0, 'V00009', 'A00001', 'A00001'),
(22, 'SO00015', 'SO00015', '2024-09-12', 15, 1, 'R00008', 'R00008', 'R00008'),
(24, 'DO00005', 'SO00007', '2024-09-12', 10, 1, 'R00003', 'A00001', 'A00001'),
(24, 'SO00007', 'SO00007', '2024-09-12', 10, 1, 'R00003', 'R00003', 'R00003'),
(24, 'SO00010', 'SO00010', '2024-09-12', 5, 1, 'R00005', 'R00005', 'R00005'),
(25, 'DO00011', 'SO00021', '2024-09-12', 100, 1, 'R00012', 'A00001', 'A00001'),
(25, 'SO00021', 'SO00021', '2024-09-12', 100, 1, 'R00012', 'R00012', 'R00012'),
(27, 'DO00012', 'SO00022', '2024-09-12', 10, 1, 'R00012', 'A00001', 'A00001'),
(27, 'SO00010', 'SO00010', '2024-09-12', 1, 1, 'R00005', 'R00005', 'R00005'),
(27, 'SO00022', 'SO00022', '2024-09-12', 10, 1, 'R00012', 'R00012', 'R00012'),
(28, 'GRN00007', 'PO00005', '2024-09-12', 600, 0, 'V00004', 'A00001', 'A00001'),
(28, 'PO00005', NULL, '2024-09-12', 600, 0, 'V00004', 'System', 'System'),
(28, 'SO00017', 'SO00017', '2024-09-12', 3, 1, 'R00009', 'R00009', 'R00009'),
(29, 'SO00019', 'SO00019', '2024-09-12', 10, 1, 'R00010', 'R00010', 'R00010'),
(31, 'SO00009', 'SO00009', '2024-09-12', 5, 1, 'R00004', 'R00004', 'R00004'),
(32, 'DO00004', 'SO00003', '2024-09-12', 1, 1, 'R00001', 'A00001', 'A00001'),
(32, 'SO00003', 'SO00003', '2024-09-12', 1, 1, 'R00001', 'R00001', 'R00001'),
(32, 'SO00017', 'SO00017', '2024-09-12', 10, 1, 'R00009', 'R00009', 'R00009'),
(33, 'SO00014', 'SO00014', '2024-09-12', 10, 1, 'R00008', 'R00008', 'R00008'),
(36, 'DO00004', 'SO00003', '2024-09-12', 8, 1, 'R00001', 'A00001', 'A00001'),
(36, 'SO00003', 'SO00003', '2024-09-12', 8, 1, 'R00001', 'R00001', 'R00001'),
(36, 'SO00019', 'SO00019', '2024-09-12', 10, 1, 'R00010', 'R00010', 'R00010'),
(37, 'PO00006', NULL, '2024-09-12', 100, 0, 'V00005', 'A00001', 'A00001'),
(37, 'SO00019', 'SO00019', '2024-09-12', 2, 1, 'R00010', 'R00010', 'R00010'),
(38, 'SO00023', 'SO00023', '2024-09-12', 20, 1, 'R00012', 'R00012', 'R00012'),
(40, 'DO00008', 'SO00020', '2024-09-12', 5, 1, 'R00011', 'A00001', 'A00001'),
(40, 'DO00011', 'SO00021', '2024-09-12', 100, 1, 'R00012', 'A00001', 'A00001'),
(40, 'SO00020', 'SO00020', '2024-09-12', 5, 1, 'R00011', 'R00011', 'R00011'),
(40, 'SO00021', 'SO00021', '2024-09-12', 100, 1, 'R00012', 'R00012', 'R00012'),
(41, 'DO00007', 'SO00012', '2024-09-12', 50, 1, 'R00007', 'A00001', 'A00001'),
(41, 'SO00012', 'SO00012', '2024-09-12', 50, 1, 'R00007', 'R00007', 'R00007'),
(43, 'DO00007', 'SO00012', '2024-09-12', 20, 1, 'R00007', 'A00001', 'A00001'),
(43, 'DO00012', 'SO00022', '2024-09-12', 20, 1, 'R00012', 'A00001', 'A00001'),
(43, 'SO00012', 'SO00012', '2024-09-12', 20, 1, 'R00007', 'R00007', 'R00007'),
(43, 'SO00022', 'SO00022', '2024-09-12', 20, 1, 'R00012', 'R00012', 'R00012'),
(44, 'DO00006', 'SO00018', '2024-09-12', 10, 1, 'R00010', 'A00001', 'A00001'),
(44, 'SO00018', 'SO00018', '2024-09-12', 10, 1, 'R00010', 'R00010', 'R00010'),
(45, 'SO00006', 'SO00006', '2024-09-12', 100, 1, 'R00002', 'R00002', 'R00002'),
(46, 'DO00012', 'SO00022', '2024-09-12', 5, 1, 'R00012', 'A00001', 'A00001'),
(46, 'SO00022', 'SO00022', '2024-09-12', 5, 1, 'R00012', 'R00012', 'R00012'),
(47, 'DO00005', 'SO00007', '2024-09-12', 10, 1, 'R00003', 'A00001', 'A00001'),
(47, 'SO00007', 'SO00007', '2024-09-12', 10, 1, 'R00003', 'R00003', 'R00003'),
(48, 'DO00010', 'SO00015', '2024-09-12', 10, 1, 'R00008', 'A00001', 'A00001'),
(48, 'SO00015', 'SO00015', '2024-09-12', 10, 1, 'R00008', 'R00008', 'R00008'),
(49, 'SO00008', 'SO00008', '2024-09-12', 10, 1, 'R00003', 'R00003', 'R00003'),
(50, 'PO00006', NULL, '2024-09-12', 100, 0, 'V00007', 'A00001', 'A00005');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `Vehicle_Plate` varchar(15) NOT NULL,
  `Vehicle_Type` varchar(50) NOT NULL,
  `Driver` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`Vehicle_Plate`, `Vehicle_Type`, `Driver`) VALUES
('ABC123', 'Truck', 'Ali AhKau Muthu'),
('AGH7222', 'Swift', 'Leong YY'),
('BJW3938', 'AVANZA', 'Kong Ji Yu'),
('LMN456', 'SUV', 'Cstantan'),
('WYS3378', 'Almera', 'Gan Chin Chung'),
('XYZ789', 'Van', 'Liau Orang Utan');

-- --------------------------------------------------------

--
-- Table structure for table `vendor`
--

CREATE TABLE `vendor` (
  `Vendor_ID` varchar(100) NOT NULL,
  `Vendor_Name` varchar(100) NOT NULL,
  `Vendor_Address` varchar(255) NOT NULL,
  `Vendor_Email` varchar(100) NOT NULL,
  `Vendor_Created_By` varchar(100) NOT NULL,
  `Vendor_Modified_By` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vendor`
--

INSERT INTO `vendor` (`Vendor_ID`, `Vendor_Name`, `Vendor_Address`, `Vendor_Email`, `Vendor_Created_By`, `Vendor_Modified_By`) VALUES
('V00001', 'Cleaning Solutions', '21 Jalan Bersih, Kuala Lumpur', 'laundrysolutions@gmail.com', 'A00001', 'A00001'),
('V00002', 'Bedroom Comforts', '32 Jalan Tenang, Shah Alam', 'bedcomforts@gmail.com', 'A00001', 'A00001'),
('V00003', 'Kitchen Essentials', '43 Jalan Chef, George Town', 'kitchenessentials@gmail.com', 'A00001', 'A00001'),
('V00004', 'Bathroom Bliss', '54 Jalan Mandi, Petaling Jaya', 'bathroombliss@gmail.com', 'A00001', 'A00001'),
('V00005', 'Storage Pros', '65 Jalan Simpan, Subang Jaya', 'storagepros@gmail.com', 'A00001', 'A00001'),
('V00006', 'Office Solutions', '76 Jalan Kerja, Cyberjaya', 'officesolutions@gmail.com', 'A00001', 'A00001'),
('V00007', 'GreenGarden Supplies', '87 Jalan Taman, Johor Bahru', 'greengardens@gmail.com', 'A00001', 'A00001'),
('V00008', 'Electronics Hub', '98 Jalan Elektrik, Penang', 'electronicshub@gmail.com', 'A00001', 'A00001'),
('V00009', 'BrightLighting Co.', '109 Jalan Cahaya, Melaka', 'brightlighting@gmail.com', 'A00001', 'A00001'),
('V00010', 'AirPure Systems', '120 Jalan Segar, Ipoh', 'airpuresystems@gmail.com', 'A00001', 'A00001');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`Admin_ID`);

--
-- Indexes for table `autoreplenishment`
--
ALTER TABLE `autoreplenishment`
  ADD PRIMARY KEY (`AutoReplenishment_ID`),
  ADD KEY `Item_ID` (`Item_ID`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`Item_ID`),
  ADD KEY `Item_Category_ID` (`Item_Category_ID`),
  ADD KEY `Vendor_ID` (`Vendor_ID`);

--
-- Indexes for table `item_category`
--
ALTER TABLE `item_category`
  ADD PRIMARY KEY (`Item_Category_ID`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`Request_ID`);

--
-- Indexes for table `retailer`
--
ALTER TABLE `retailer`
  ADD PRIMARY KEY (`Retailer_ID`);

--
-- Indexes for table `schedule`
--
ALTER TABLE `schedule`
  ADD PRIMARY KEY (`Schedule_ID`),
  ADD KEY `Vehicle_Plate` (`Vehicle_Plate`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`Item_ID`,`Doc_No`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`Vehicle_Plate`);

--
-- Indexes for table `vendor`
--
ALTER TABLE `vendor`
  ADD PRIMARY KEY (`Vendor_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `autoreplenishment`
--
ALTER TABLE `autoreplenishment`
  MODIFY `AutoReplenishment_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `Item_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `item_category`
--
ALTER TABLE `item_category`
  MODIFY `Item_Category_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `Request_ID` int(128) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `Schedule_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `autoreplenishment`
--
ALTER TABLE `autoreplenishment`
  ADD CONSTRAINT `autoreplenishment_ibfk_1` FOREIGN KEY (`Item_ID`) REFERENCES `item` (`Item_ID`);

--
-- Constraints for table `item`
--
ALTER TABLE `item`
  ADD CONSTRAINT `item_ibfk_1` FOREIGN KEY (`Item_Category_ID`) REFERENCES `item_category` (`Item_Category_ID`),
  ADD CONSTRAINT `item_ibfk_2` FOREIGN KEY (`Vendor_ID`) REFERENCES `vendor` (`Vendor_ID`);

--
-- Constraints for table `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`Vehicle_Plate`) REFERENCES `vehicle` (`Vehicle_Plate`);

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`Item_ID`) REFERENCES `item` (`Item_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
