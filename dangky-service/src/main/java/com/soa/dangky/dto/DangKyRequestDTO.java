package com.soa.dangky.dto;

public class DangKyRequestDTO {
    private String maSv;
    private String maDeTai;
    private String ghiChu;

    public DangKyRequestDTO() {
    }

    public DangKyRequestDTO(String maSv, String maDeTai, String ghiChu) {
        this.maSv = maSv;
        this.maDeTai = maDeTai;
        this.ghiChu = ghiChu;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
