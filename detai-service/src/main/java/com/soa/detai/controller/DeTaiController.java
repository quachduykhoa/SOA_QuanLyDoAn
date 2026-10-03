package com.soa.detai.controller;

import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTO;
import com.soa.detai.service.DeTaiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detai")
public class DeTaiController {

    private final DeTaiService deTaiService;

    public DeTaiController(DeTaiService deTaiService) {
        this.deTaiService = deTaiService;
    }

    // 1. Lấy toàn bộ danh sách đề tài
    @GetMapping
    public ResponseEntity<ApiResponse<List<DeTaiDTO>>> getAllDeTai() {
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đề tài thành công", deTaiService.getAllDeTai()));
    }

    // 2. Lấy thông tin chi tiết đề tài theo mã
    @GetMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> getDeTaiByMaDeTai(@PathVariable String maDeTai) {
        DeTaiDTO dt = deTaiService.getDeTaiByMaDeTai(maDeTai);
        if (dt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Không tìm thấy đề tài với mã: " + maDeTai));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đề tài thành công", dt));
    }

    // 3. Thêm mới đề tài
    @PostMapping
    public ResponseEntity<ApiResponse<DeTaiDTO>> createDeTai(@RequestBody DeTaiDTO request) {
        try {
            DeTaiDTO created = deTaiService.createDeTai(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Thêm mới đề tài thành công", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 4. Xóa đề tài theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDeTai(@PathVariable Long id) {
        try {
            deTaiService.deleteDeTai(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa đề tài thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
}
