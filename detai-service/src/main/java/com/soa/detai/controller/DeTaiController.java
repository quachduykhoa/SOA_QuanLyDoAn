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

    // 5. Cập nhật slot (POST cho Feign - Feign khong ho tro PATCH; PATCH goi qua Gateway/Postman)
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/{maDeTai}/update-slot")
    public ResponseEntity<ApiResponse<DeTaiDTO>> updateSlot(@PathVariable String maDeTai,
                                                            @RequestParam String action) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Cập nhật slot thành công",
                    deTaiService.updateSlot(maDeTai, action)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 6. Tăng slot an toàn (POST cho Feign - Feign khong ho tro PATCH; PATCH goi qua Gateway/Postman)
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/{maDeTai}/increment-slot")
    public ResponseEntity<ApiResponse<DeTaiDTO>> incrementSlot(@PathVariable String maDeTai) {
        try {
            int updated = deTaiService.incrementSlotIfAvailable(maDeTai);
            if (updated == 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Đề tài đã hết chỗ"));
            }
            return ResponseEntity.ok(ApiResponse.success("Tăng slot thành công",
                    deTaiService.getDeTaiByMaDeTai(maDeTai)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 7. Cập nhật thông tin đề tài theo mã
    @PutMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> updateDeTai(@PathVariable String maDeTai,
                                                             @RequestBody DeTaiDTO request) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Cập nhật đề tài thành công",
                    deTaiService.updateDeTai(maDeTai, request)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
