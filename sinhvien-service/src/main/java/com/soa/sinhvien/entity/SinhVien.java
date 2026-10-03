package com.soa.sinhvien.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SINHVIEN")
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sv", unique = true, nullable = false, length = 20)
    private String maSv;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "ngay_sinh", length = 20)
    private String ngaySinh;

    @Column(name = "que_quan", length = 100)
    private String queQuan;

    @Column(name = "chuyen_nganh", nullable = false, length = 100)
    private String chuyenNganh;

    @Column(name = "gpa", nullable = false)
    private Double gpa;

    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai; // "DANG_HOC", "DINH_CHI"

    public SinhVien() {
    }

    public SinhVien(Long id, String maSv, String hoTen, String ngaySinh, String queQuan, String chuyenNganh, Double gpa, String trangThai) {
        this.id = id;
        this.maSv = maSv;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.queQuan = queQuan;
        this.chuyenNganh = chuyenNganh;
        this.gpa = gpa;
        this.trangThai = trangThai;
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

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
