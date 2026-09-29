package org.example.sinhvienservice.controller;
import org.example.sinhvienservice.entity.SinhVien;
import org.example.sinhvienservice.service.SinhVienService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sinhvien")
public class SinhVienController {

    private final SinhVienService sinhVienService;

    public SinhVienController(SinhVienService sinhVienService) {
        this.sinhVienService = sinhVienService;
    }

    // GET /api/sinhvien
    @GetMapping
    public List<SinhVien> getAllSinhVien() {
        return sinhVienService.getAllSinhVien();
    }

    // GET /api/sinhvien/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SinhVien> getSinhVienById(@PathVariable Integer id) {
        return sinhVienService.getSinhVienById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/sinhvien
    @PostMapping
    public SinhVien createSinhVien(@RequestBody SinhVien sinhVien) {
        return sinhVienService.createSinhVien(sinhVien);
    }

    // PUT /api/sinhvien/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SinhVien> updateSinhVien(
            @PathVariable Integer id,
            @RequestBody SinhVien sinhVien) {

        return sinhVienService.updateSinhVien(id, sinhVien)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/sinhvien/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSinhVien(@PathVariable Integer id) {

        if (sinhVienService.deleteSinhVien(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
