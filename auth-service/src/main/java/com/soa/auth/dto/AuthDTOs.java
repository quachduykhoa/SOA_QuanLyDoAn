package com.soa.auth.dto;

public class AuthDTOs {

    public record LoginRequest(String tenDangNhap, String matKhau) {}

    public record RegisterRequest(String tenDangNhap, String matKhau, String hoTen, String maSv) {}

    public record AuthResponse(String token, String vaiTro, String hoTen, String tenDangNhap) {}
}