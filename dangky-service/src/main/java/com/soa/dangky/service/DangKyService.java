package com.soa.dangky.service;

import com.soa.dangky.client.DeTaiClient;
import com.soa.dangky.client.SinhVienClient;
import com.soa.dangky.dto.ApiResponse;
import com.soa.dangky.dto.DangKyRequestDTO;
import com.soa.dangky.dto.DangKyResponseDTO;
import com.soa.dangky.dto.DeTaiDTO;
import com.soa.dangky.dto.SinhVienDTO;
import com.soa.dangky.entity.DangKy;
import com.soa.dangky.repository.DangKyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DangKyService {

    private final DangKyRepository dangKyRepository;
    private final SinhVienClient sinhVienClient;
    private final DeTaiClient deTaiClient;

    public DangKyService(DangKyRepository dangKyRepository, SinhVienClient sinhVienClient, DeTaiClient deTaiClient) {
        this.dangKyRepository = dangKyRepository;
        this.sinhVienClient = sinhVienClient;
        this.deTaiClient = deTaiClient;
    }

    public List<DangKyResponseDTO> getAllDangKy() {
        return dangKyRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DangKyResponseDTO getDangKyById(Long id) {
        return dangKyRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public DangKyResponseDTO register(DangKyRequestDTO request) {
        // 1. Xác thực sinh viên tồn tại qua SinhVien-Service
        ApiResponse<SinhVienDTO> svResponse = sinhVienClient.getSinhVienByMaSv(request.getMaSv());
        if (svResponse == null || svResponse.getData() == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên: " + request.getMaSv());
        }

        // 2. Xác thực đề tài tồn tại qua DeTai-Service
        ApiResponse<DeTaiDTO> dtResponse = deTaiClient.getDeTaiByMaDeTai(request.getMaDeTai());
        if (dtResponse == null || dtResponse.getData() == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài: " + request.getMaDeTai());
        }

        // 3. Lưu thông tin đăng ký
        DangKy entity = new DangKy(
                null,
                request.getMaSv(),
                request.getMaDeTai(),
                LocalDateTime.now(),
                request.getGhiChu()
        );

        return toDTO(dangKyRepository.save(entity));
    }

    @Transactional
    public void deleteDangKy(Long id) {
        if (!dangKyRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy đăng ký với ID: " + id);
        }
        dangKyRepository.deleteById(id);
    }

    private DangKyResponseDTO toDTO(DangKy entity) {
        String ngayDangKyStr = entity.getNgayDangKy() != null
                ? entity.getNgayDangKy().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;

        return new DangKyResponseDTO(
                entity.getId(),
                entity.getMaSv(),
                entity.getMaDeTai(),
                ngayDangKyStr,
                entity.getGhiChu()
        );
    }
}
