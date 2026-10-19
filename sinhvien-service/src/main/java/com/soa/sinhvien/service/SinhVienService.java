package com.soa.sinhvien.service;

import com.soa.sinhvien.dto.SinhVienDTO;
import com.soa.sinhvien.entity.SinhVien;
import com.soa.sinhvien.repository.SinhVienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SinhVienService {

    private final SinhVienRepository sinhVienRepository;

    public SinhVienService(SinhVienRepository sinhVienRepository) {
        this.sinhVienRepository = sinhVienRepository;
    }

    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SinhVienDTO createSinhVien(SinhVienDTO dto) {
        if (sinhVienRepository.existsByMaSv(dto.getMaSv())) {
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại: " + dto.getMaSv());
        }

        SinhVien entity = new SinhVien(
                null,
                dto.getMaSv(),
                dto.getHoTen(),
                dto.getNgaySinh(),
                dto.getQueQuan(),
                dto.getChuyenNganh(),
                dto.getGpa() != null ? dto.getGpa() : 3.0,
                dto.getTrangThai() != null ? dto.getTrangThai() : "DANG_HOC"
        );

        return toDTO(sinhVienRepository.save(entity));
    }

    public SinhVienDTO getSinhVienByMaSv(String maSv) {
        return sinhVienRepository.findByMaSv(maSv)
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public SinhVienDTO updateSinhVien(String maSv, SinhVienDTO dto) {
        SinhVien entity = sinhVienRepository.findByMaSv(maSv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên với mã: " + maSv));

        if (dto.getHoTen() != null) {
            entity.setHoTen(dto.getHoTen());
        }
        if (dto.getNgaySinh() != null) {
            entity.setNgaySinh(dto.getNgaySinh());
        }
        if (dto.getQueQuan() != null) {
            entity.setQueQuan(dto.getQueQuan());
        }
        if (dto.getChuyenNganh() != null) {
            entity.setChuyenNganh(dto.getChuyenNganh());
        }
        if (dto.getGpa() != null) {
            entity.setGpa(dto.getGpa());
        }
        if (dto.getTrangThai() != null) {
            entity.setTrangThai(dto.getTrangThai());
        }

        return toDTO(sinhVienRepository.save(entity));
    }

    private SinhVienDTO toDTO(SinhVien entity) {
        return new SinhVienDTO(
                entity.getId(),
                entity.getMaSv(),
                entity.getHoTen(),
                entity.getNgaySinh(),
                entity.getQueQuan(),
                entity.getChuyenNganh(),
                entity.getGpa(),
                entity.getTrangThai()
        );
    }
}
