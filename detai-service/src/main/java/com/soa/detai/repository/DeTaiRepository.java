package com.soa.detai.repository;

import com.soa.detai.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeTaiRepository extends JpaRepository<DeTai, Long> {
    Optional<DeTai> findByMaDeTai(String maDeTai);
    boolean existsByMaDeTai(String maDeTai);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE DeTai d " +
            "SET d.soLuongDaDangKy = d.soLuongDaDangKy + 1 " +
            "WHERE d.maDeTai = :maDeTai " +
            "  AND d.soLuongDaDangKy < d.soLuongToiDa")
    int incrementSlotIfAvailable(@Param("maDeTai") String maDeTai);
}
