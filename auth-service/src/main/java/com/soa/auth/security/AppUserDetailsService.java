package com.soa.auth.security;

import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository repo;

    public AppUserDetailsService(TaiKhoanRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) throws UsernameNotFoundException {
        TaiKhoan tk = repo.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại: " + tenDangNhap));

        return User.withUsername(tk.getTenDangNhap())
                .password(tk.getMatKhau())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + tk.getVaiTro())))
                .build();
    }
}