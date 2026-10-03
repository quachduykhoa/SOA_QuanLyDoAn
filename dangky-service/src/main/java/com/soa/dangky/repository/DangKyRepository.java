package com.soa.dangky.repository;

import com.soa.dangky.entity.DangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy, Long> {
}
