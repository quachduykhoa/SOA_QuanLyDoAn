package com.soa.detai.service;

import com.soa.detai.dto.DeTaiDTO;
import com.soa.detai.entity.DeTai;
import com.soa.detai.repository.DeTaiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeTaiService {

    private final DeTaiRepository deTaiRepository;

    public DeTaiService(DeTaiRepository deTaiRepository) {
        this.deTaiRepository = deTaiRepository;
    }

    public List<DeTaiDTO> getAllDeTai() {
        return deTaiRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DeTaiDTO getDeTaiByMaDeTai(String maDeTai) {
        return deTaiRepository.findByMaDeTai(maDeTai)
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public DeTaiDTO createDeTai(DeTaiDTO dto) {
        if (deTaiRepository.existsByMaDeTai(dto.getMaDeTai())) {
            throw new IllegalArgumentException("Mã đề tài đã tồn tại: " + dto.getMaDeTai());
        }

        DeTai entity = new DeTai(
                null,
                dto.getMaDeTai(),
                dto.getTenDeTai(),
                dto.getMoTa(),
                dto.getGvhd()
        );
        entity.setSoLuongToiDa(dto.getSoLuongToiDa() != null ? dto.getSoLuongToiDa() : 1);
        entity.setSoLuongDaDangKy(0);

        return toDTO(deTaiRepository.save(entity));
    }

    @Transactional
    public DeTaiDTO updateSlot(String maDeTai, String action) {
        DeTai entity = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài với mã: " + maDeTai));

        if ("INCREASE".equalsIgnoreCase(action)) {
            if (entity.getSoLuongDaDangKy() >= entity.getSoLuongToiDa()) {
                throw new IllegalArgumentException("Đề tài đã hết chỗ, không thể tăng đăng ký");
            }
            entity.setSoLuongDaDangKy(entity.getSoLuongDaDangKy() + 1);
        } else if ("DECREASE".equalsIgnoreCase(action)) {
            if (entity.getSoLuongDaDangKy() > 0) {
                entity.setSoLuongDaDangKy(entity.getSoLuongDaDangKy() - 1);
            }
        } else {
            throw new IllegalArgumentException("Action không hợp lệ (INCREASE hoặc DECREASE): " + action);
        }
        return toDTO(deTaiRepository.save(entity));
    }

    @Transactional
    public DeTaiDTO updateDeTai(String maDeTai, DeTaiDTO dto) {
        DeTai entity = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài với mã: " + maDeTai));

        if (dto.getTenDeTai() != null) {
            entity.setTenDeTai(dto.getTenDeTai());
        }
        if (dto.getMoTa() != null) {
            entity.setMoTa(dto.getMoTa());
        }
        if (dto.getGvhd() != null) {
            entity.setGvhd(dto.getGvhd());
        }
        if (dto.getSoLuongToiDa() != null) {
            if (dto.getSoLuongToiDa() < entity.getSoLuongDaDangKy()) {
                throw new IllegalArgumentException("Số lượng tối đa không được nhỏ hơn số lượng đã đăng ký ("
                        + entity.getSoLuongDaDangKy() + ")");
            }
            entity.setSoLuongToiDa(dto.getSoLuongToiDa());
        }

        return toDTO(deTaiRepository.save(entity));
    }

    @Transactional
    public void deleteDeTai(Long id) {
        if (!deTaiRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy đề tài với ID: " + id);
        }
        deTaiRepository.deleteById(id);
    }

    @Transactional
    public int incrementSlotIfAvailable(String maDeTai) {
        return deTaiRepository.incrementSlotIfAvailable(maDeTai);
    }

    private DeTaiDTO toDTO(DeTai entity) {
        DeTaiDTO dto = new DeTaiDTO(
                entity.getId(),
                entity.getMaDeTai(),
                entity.getTenDeTai(),
                entity.getMoTa(),
                entity.getGvhd()
        );
        dto.setSoLuongToiDa(entity.getSoLuongToiDa());
        dto.setSoLuongDaDangKy(entity.getSoLuongDaDangKy());
        return dto;
    }
}
