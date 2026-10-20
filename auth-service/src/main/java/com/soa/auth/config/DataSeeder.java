package com.soa.auth.config;

import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Tự tạo tài khoản admin mặc định lúc service khởi động (nếu chưa tồn tại)
@Component
public class DataSeeder implements CommandLineRunner {

    private final TaiKhoanRepository repo;
    private final PasswordEncoder encoder;

    public DataSeeder(TaiKhoanRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!repo.existsByTenDangNhap("admin")) {
            TaiKhoan admin = new TaiKhoan();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(encoder.encode("admin123"));  // mã hóa BCrypt
            admin.setHoTen("Quan tri vien");
            admin.setVaiTro("ADMIN");
            repo.save(admin);
            System.out.println(">>> Da tao tai khoan admin macdinh (admin/admin123)");
        }
    }
}