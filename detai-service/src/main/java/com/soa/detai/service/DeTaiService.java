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

        return toDTO(deTaiRepository.save(entity));
    }

    @Transactional
    public void deleteDeTai(Long id) {
        if (!deTaiRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy đề tài với ID: " + id);
        }
        deTaiRepository.deleteById(id);
    }

    private DeTaiDTO toDTO(DeTai entity) {
        return new DeTaiDTO(
                entity.getId(),
                entity.getMaDeTai(),
                entity.getTenDeTai(),
                entity.getMoTa(),
                entity.getGvhd()
        );
    }
}
