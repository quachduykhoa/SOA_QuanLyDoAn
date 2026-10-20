package com.soa.auth.controller;

import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import com.soa.auth.security.JwtService;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final TaiKhoanRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, TaiKhoanRepository repo,
                          PasswordEncoder encoder, JwtService jwtService) {
        this.authManager = authManager;
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    // 1. Đăng nhập -> khớp tên + mật khẩu (BCrypt) -> trả JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.tenDangNhap(), req.matKhau()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Sai tên đăng nhập hoặc mật khẩu"));
        }

        TaiKhoan tk = repo.findByTenDangNhap(req.tenDangNhap()).orElseThrow();
        String token = jwtService.generateToken(tk);
        return ResponseEntity.ok(Map.of("success", true,
                "message", "Đăng nhập thành công",
                "data", new AuthResponse(token, tk.getVaiTro(), tk.getHoTen(), tk.getTenDangNhap())));
    }

    // 2. Đăng ký tài khoản sinh viên (vai trò cố định SINH_VIEN, không nhận từ client)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (repo.existsByTenDangNhap(req.tenDangNhap())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Tên đăng nhập đã tồn tại"));
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(req.tenDangNhap());
        tk.setMatKhau(encoder.encode(req.matKhau()));   // mã hóa BCrypt trước khi lưu
        tk.setHoTen(req.hoTen());
        tk.setVaiTro("SINH_VIEN");                      // KHÔNG cho client tự chọn vai trò
        tk.setMaSv(req.maSv());
        repo.save(tk);

        String token = jwtService.generateToken(tk);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true,
                        "message", "Đăng ký tài khoản thành công",
                        "data", new AuthResponse(token, tk.getVaiTro(), tk.getHoTen(), tk.getTenDangNhap())));
    }
}