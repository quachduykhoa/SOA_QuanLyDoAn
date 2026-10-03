package com.soa.dangky.controller;

import com.soa.dangky.dto.ApiResponse;
import com.soa.dangky.dto.DangKyRequestDTO;
import com.soa.dangky.dto.DangKyResponseDTO;
import com.soa.dangky.service.DangKyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dangky")
public class DangKyController {

    private final DangKyService dangKyService;

    public DangKyController(DangKyService dangKyService) {
        this.dangKyService = dangKyService;
    }

    // 1. Lấy toàn bộ danh sách đăng ký
    @GetMapping
    public ResponseEntity<ApiResponse<List<DangKyResponseDTO>>> getAllDangKy() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đăng ký thành công", dangKyService.getAllDangKy()));
    }

    // 2. Lấy chi tiết đăng ký theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DangKyResponseDTO>> getDangKyById(@PathVariable Long id) {
        DangKyResponseDTO dto = dangKyService.getDangKyById(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Không tìm thấy đăng ký với ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đăng ký thành công", dto));
    }

    // 3. Đăng ký đề tài (xác thực SV và ĐT qua Feign, sau đó lưu DB)
    @PostMapping
    public ResponseEntity<ApiResponse<DangKyResponseDTO>> register(@RequestBody DangKyRequestDTO request) {
        try {
            DangKyResponseDTO result = dangKyService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Đăng ký đề tài tốt nghiệp thành công!", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 4. Xóa đăng ký theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDangKy(@PathVariable Long id) {
        try {
            dangKyService.deleteDangKy(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa đăng ký thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
}
