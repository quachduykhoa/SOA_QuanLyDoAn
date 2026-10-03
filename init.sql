-- ====================================================================
-- Script tự động khởi tạo 3 cơ sở dữ liệu độc lập cho hệ thống SOA
-- Áp dụng mô hình Database-per-Service
-- ====================================================================

CREATE DATABASE IF NOT EXISTS sinhvien_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS detai_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS dangky_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
