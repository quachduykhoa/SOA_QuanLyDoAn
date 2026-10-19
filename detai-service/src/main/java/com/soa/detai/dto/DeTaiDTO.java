package com.soa.detai.dto;

public class DeTaiDTO {
    private Long id;
    private String maDeTai;
    private String tenDeTai;
    private String moTa;
    private String gvhd;
    private Integer soLuongToiDa;
    private Integer soLuongDaDangKy;

    public DeTaiDTO() {
    }

    public DeTaiDTO(Long id, String maDeTai, String tenDeTai, String moTa, String gvhd) {
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

    public Integer getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(Integer soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public Integer getSoLuongDaDangKy() {
        return soLuongDaDangKy;
    }

    public void setSoLuongDaDangKy(Integer soLuongDaDangKy) {
        this.soLuongDaDangKy = soLuongDaDangKy;
    }
}
