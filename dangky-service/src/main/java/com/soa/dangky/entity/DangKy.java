package com.soa.dangky.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DANGKY")
public class DangKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sv", nullable = false, length = 20)
    private String maSv;

    @Column(name = "ma_detai", nullable = false, length = 20)
    private String maDeTai;

    @Column(name = "ngay_dang_ky", nullable = false)
    private LocalDateTime ngayDangKy;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    public DangKy() {
    }

    public DangKy(Long id, String maSv, String maDeTai, LocalDateTime ngayDangKy, String ghiChu) {
        this.id = id;
        this.maSv = maSv;
        this.maDeTai = maDeTai;
        this.ngayDangKy = ngayDangKy;
        this.ghiChu = ghiChu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public String getMaDeTai() {
        return maDeTai;
    }

    public void setMaDeTai(String maDeTai) {
        this.maDeTai = maDeTai;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
