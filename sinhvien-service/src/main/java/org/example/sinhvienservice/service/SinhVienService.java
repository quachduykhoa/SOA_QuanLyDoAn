package org.example.sinhvienservice.service;
import org.example.sinhvienservice.entity.SinhVien;
import org.example.sinhvienservice.repository.SinhVienRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SinhVienService {

    private final SinhVienRepository sinhVienRepository;

    public SinhVienService(SinhVienRepository sinhVienRepository) {
        this.sinhVienRepository = sinhVienRepository;
    }

    public List<SinhVien> getAllSinhVien() {
        return sinhVienRepository.findAll();
    }

    public Optional<SinhVien> getSinhVienById(Integer id) {
        return sinhVienRepository.findById(id);
    }

    public SinhVien createSinhVien(SinhVien sinhVien) {
        return sinhVienRepository.save(sinhVien);
    }

    public Optional<SinhVien> updateSinhVien(Integer id, SinhVien sinhVien) {
        return sinhVienRepository.findById(id)
                .map(existingSinhVien -> {
                    existingSinhVien.setHoTen(sinhVien.getHoTen());
                    existingSinhVien.setEmail(sinhVien.getEmail());
                    return sinhVienRepository.save(existingSinhVien);
                });
    }

    public boolean deleteSinhVien(Integer id) {
        if (!sinhVienRepository.existsById(id)) {
            return false;
        }

        sinhVienRepository.deleteById(id);
        return true;
    }
}
