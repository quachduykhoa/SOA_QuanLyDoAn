package com.soa.auth.security;

import com.soa.auth.entity.TaiKhoan;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    // Nhận secret dạng Base64 từ application.properties
    public JwtService(@Value("${jwt.secret}") String secretBase64) {
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // >= 32 byte => HS256 hợp lệ
    }

    // Sinh token: subject = tên đăng nhập, kèm claims role/name/maSv
    public String generateToken(TaiKhoan tk) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600_000L); // 1 giờ
        return Jwts.builder()
                .subject(tk.getTenDangNhap())
                .claim("role", tk.getVaiTro())
                .claim("name", tk.getHoTen())
                .claim("maSv", tk.getMaSv())
                .issuer("soa-auth-service")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}