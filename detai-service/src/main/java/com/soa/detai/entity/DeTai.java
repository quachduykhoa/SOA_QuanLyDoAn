package com.soa.detai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DETAI")
public class DeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_detai", unique = true, nullable = false, length = 20)
    private String maDeTai;

    @Column(name = "ten_detai", nullable = false, length = 255)
    private String tenDeTai;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gvhd", nullable = false, length = 100)
    private String gvhd;

    public DeTai() {
    }

    public DeTai(Long id, String maDeTai, String tenDeTai, String moTa, String gvhd) {
        this.id = id;
        this.maDeTai = maDeTai;
        this.tenDeTai = tenDeTai;
        this.moTa = moTa;
        this.gvhd = gvhd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaDeTai() {
        return maDeTai;
    }

    public void setMaDeTai(String maDeTai) {
        this.maDeTai = maDeTai;
    }

    public String getTenDeTai() {
        return tenDeTai;
    }

    public void setTenDeTai(String tenDeTai) {
        this.tenDeTai = tenDeTai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getGvhd() {
        return gvhd;
    }

    public void setGvhd(String gvhd) {
        this.gvhd = gvhd;
    }
}
