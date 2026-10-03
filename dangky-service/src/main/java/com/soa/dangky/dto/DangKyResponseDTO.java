package com.soa.dangky.dto;

public class DangKyResponseDTO {
    private Long id;
    private String maSv;
    private String maDeTai;
    private String ngayDangKy;
    private String ghiChu;

    public DangKyResponseDTO() {
    }

    public DangKyResponseDTO(Long id, String maSv, String maDeTai, String ngayDangKy, String ghiChu) {
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

    public String getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(String ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
