package com.soa.sinhvien.controller;

import com.soa.sinhvien.dto.ApiResponse;
import com.soa.sinhvien.dto.SinhVienDTO;
import com.soa.sinhvien.service.SinhVienService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sinhvien")
public class SinhVienController {

    private final SinhVienService sinhVienService;

    public SinhVienController(SinhVienService sinhVienService) {
        this.sinhVienService = sinhVienService;
    }

    // 1. Lấy toàn bộ danh sách sinh viên
    @GetMapping
    public ResponseEntity<ApiResponse<List<SinhVienDTO>>> getAllSinhVien() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sinh viên thành công", sinhVienService.getAllSinhVien()));
    }

    // 2. Thêm mới sinh viên
    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienDTO>> createSinhVien(@RequestBody SinhVienDTO request) {
        try {
            SinhVienDTO created = sinhVienService.createSinhVien(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Thêm mới sinh viên thành công", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 3. Lấy thông tin chi tiết sinh viên theo mã (CRUD cơ bản)
    @GetMapping("/{maSv}")
    public ResponseEntity<ApiResponse<SinhVienDTO>> getSinhVienByMaSv(@PathVariable String maSv) {
        SinhVienDTO sv = sinhVienService.getSinhVienByMaSv(maSv);
        if (sv == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Không tìm thấy sinh viên với mã: " + maSv));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sinh viên thành công", sv));
    }
}
