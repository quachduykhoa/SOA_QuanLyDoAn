-- Xóa bảng cũ nếu có
--DROP TABLE IF EXISTS DANGKY;
--DROP TABLE IF EXISTS SINHVIEN;
--DROP TABLE IF EXISTS DETAI;

-- ==========================================
-- BẢNG SINH VIÊN
-- ==========================================
CREATE TABLE SINHVIEN (
    maSV INTEGER PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL
);

-- ==========================================
-- BẢNG ĐỀ TÀI
-- ==========================================
CREATE TABLE DETAI (
    maDeTai INTEGER PRIMARY KEY,
    tenDeTai VARCHAR(255) NOT NULL
);

-- ==========================================
-- BẢNG ĐĂNG KÝ
-- ==========================================
CREATE TABLE DANGKY (
    maSV INTEGER NOT NULL,
    maDeTai INTEGER NOT NULL,

    PRIMARY KEY (maSV, maDeTai),

    CONSTRAINT fk_dangky_sinhvien
        FOREIGN KEY (maSV)
        REFERENCES SINHVIEN(maSV),

    CONSTRAINT fk_dangky_detai
        FOREIGN KEY (maDeTai)
        REFERENCES DETAI(maDeTai)
);

-- ==========================================
-- DỮ LIỆU MẪU
-- ==========================================
INSERT INTO SINHVIEN (maSV, hoTen, email)
VALUES
(1, 'Nguyen Van A', 'a@example.com'),
(2, 'Tran Thi B', 'b@example.com');

INSERT INTO DETAI (maDeTai, tenDeTai)
VALUES
(1, 'Xay dung he thong quan ly do an'),
(2, 'Xay dung ung dung Web voi Spring Boot');