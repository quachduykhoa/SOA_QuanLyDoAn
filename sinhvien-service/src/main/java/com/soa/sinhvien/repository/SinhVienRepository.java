package com.soa.sinhvien.repository;

import com.soa.sinhvien.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    Optional<SinhVien> findByMaSv(String maSv);
    boolean existsByMaSv(String maSv);
}
