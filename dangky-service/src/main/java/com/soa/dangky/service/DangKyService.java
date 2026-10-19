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

    public List<DangKyResponseDTO> getDangKyByMaSv(String maSv) {
        return dangKyRepository.findByMaSv(maSv).stream()
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
        ApiResponse<SinhVienDTO> svResponse = sinhVienClient.getSinhVienByMaSv(request.getMaSv());
        if (svResponse == null || svResponse.getData() == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên: " + request.getMaSv());
        }

        if (!"DANG_HOC".equals(svResponse.getData().getTrangThai())) {
            throw new IllegalArgumentException("Sinh viên " + request.getMaSv()
                    + " đang ở trạng thái " + svResponse.getData().getTrangThai()
                    + ", không thể đăng ký");
        }

        DangKy existingDangKy = dangKyRepository.findFirstByMaSv(request.getMaSv())
                .orElse(null);
        if (existingDangKy != null) {
            throw new IllegalArgumentException("Sinh viên " + request.getMaSv()
                    + " đã đăng ký đề tài " + existingDangKy.getMaDeTai()
                    + " rồi. Mỗi sinh viên chỉ được đăng ký tối đa 1 đề tài.");
        }

        ApiResponse<DeTaiDTO> dtResponse = deTaiClient.getDeTaiByMaDeTai(request.getMaDeTai());
        if (dtResponse == null || dtResponse.getData() == null) {
            throw new IllegalArgumentException("Không tìm thấy đề tài: " + request.getMaDeTai());
        }

        if (dangKyRepository.existsByMaSvAndMaDeTai(request.getMaSv(), request.getMaDeTai())) {
            throw new IllegalArgumentException("Sinh viên " + request.getMaSv()
                    + " đã đăng ký đề tài " + request.getMaDeTai() + " rồi");
        }


        ApiResponse<DeTaiDTO> slotResponse = deTaiClient.incrementSlot(request.getMaDeTai());
        if (slotResponse == null || slotResponse.getData() == null) {
            throw new IllegalArgumentException("Đề tài " + request.getMaDeTai() + " đã hết chỗ");
        }

        DangKy entity = new DangKy(
                null,
                request.getMaSv(),
                request.getMaDeTai(),
                LocalDateTime.now(),
                request.getGhiChu()
        );

        try {
            DangKy saved = dangKyRepository.save(entity);
            return toDTO(saved);
        } catch (Exception e) {
            try {
                deTaiClient.updateSlot(request.getMaDeTai(), "DECREASE");
            } catch (Exception ex) {
            }
            throw new IllegalArgumentException("Đăng ký thất bại: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteDangKy(Long id) {
        DangKy entity = dangKyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đăng ký với ID: " + id));

        dangKyRepository.deleteById(id);

        // Hoàn slot trên DeTai-Service
        try {
            deTaiClient.updateSlot(entity.getMaDeTai(), "DECREASE");
        } catch (Exception e) {
            // Ghi log, không chặn việc hủy đăng ký
        }
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
