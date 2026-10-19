package com.soa.dangky.repository;

import com.soa.dangky.entity.DangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy, Long> {
    List<DangKy> findByMaSv(String maSv);
    boolean existsByMaSvAndMaDeTai(String maSv, String maDeTai);

    Optional<DangKy> findFirstByMaSv(String maSv);
}
