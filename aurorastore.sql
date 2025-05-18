-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2025 at 07:35 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aurorastore`
--

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

CREATE TABLE `brands` (
  `brand_id` int(11) NOT NULL,
  `brand_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `brands`
--

INSERT INTO `brands` (`brand_id`, `brand_name`) VALUES
(1, 'NoTes'),
(2, 'INK'),
(3, 'Artistic'),
(4, 'Luxeria'),
(5, 'Ikea'),
(6, 'Decorative');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`) VALUES
(1, 'Sales'),
(2, 'New Arrivals'),
(3, 'Most Viewed');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `image` varchar(100) DEFAULT NULL,
  `product_price` decimal(10,2) NOT NULL,
  `product_description` varchar(100) NOT NULL,
  `product_quantity` int(11) NOT NULL,
  `product_status` enum('active','inactive') NOT NULL DEFAULT 'active',
  `category_id` int(11) NOT NULL,
  `brand_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `image`, `product_price`, `product_description`, `product_quantity`, `product_status`, `category_id`, `brand_id`) VALUES
(17, 'Eraser', 'eraser.png', 5.00, 'Smudge Proof guaranteed erasers.', 80, 'active', 2, 3),
(18, 'Pencil', 'pencil1.jpg', 10.00, 'Dark HB Pencils.', 100, 'active', 1, 6),
(19, 'Colour', 'colour.jpg', 5.00, '12 Colourful pencil colours.', 180, 'active', 1, 3),
(20, 'Holder', 'pencilholder.jpg', 300.00, 'Premium Pencil Holder for organizing study materials.', 40, 'active', 3, 5),
(21, 'Notebook', 'notebook.jpg', 50.00, '10 pieces of notebook.', 8, 'active', 3, 3);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `image` varchar(100) DEFAULT NULL,
  `contact_number` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_email`, `user_password`, `image`, `contact_number`, `created_at`, `role_id`) VALUES
(1, 'Admin', 'admin@gmail.com', 'Admin@123', NULL, '9767934708', '2025-05-17 15:36:48', 1),
(20, 'Jensen', 'jensen@gmail.com', 'yhBAdEwxvjdNW1Crzm9QKkTbs7TBsT9q3B7e/VfelJuYExLg8iLKbRcVrnTxg/IdRfuvjMDW', '1747539740466_JensenAckles.jpg', '9847583921', '2025-05-18 03:42:20', 2),
(21, 'Jared', 'jared@gmail.com', 'OW6O2lEIEABIkmf7KwFQNoV4ebDQJkkWAXcdCiTVyabGuW2ON4zp3u//0oj60MzJePw54Sg=', '1747540600993_jared.jpg', '9876543217', '2025-05-18 03:56:41', 2),
(22, 'Usha', 'usha@gmail.com', 'GSd7KRgYb5VPrONGWgFLwWnujHu+aSE0A1mHiBHm2DEolV7aFK8DphE2Or5yQfqOSfYf/A==', '1747542775225_ushaProfile.jpg', '9887766556', '2025-05-18 04:32:55', 2),
(23, 'Apekshya', 'apekshya@gmail.com', 'N4Ff60jBQK35r9xylgopUn8dhOLEQ/x2l7IOuKWApQfftbz6gGsofwOpSMdxRK7n5eS9rA==', '1747544072083_Apekshya.jpg', '9876543216', '2025-05-18 04:54:32', 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `role_id` int(11) NOT NULL,
  `role_type` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`role_id`, `role_type`) VALUES
(1, 'Admin'),
(2, 'Customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`brand_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `brand_id_fk` (`brand_id`),
  ADD KEY `category_id_fk` (`category_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `role_id_fk` (`role_id`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `brands`
--
ALTER TABLE `brands`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `brand_id_fk` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`),
  ADD CONSTRAINT `category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `user_roles` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
