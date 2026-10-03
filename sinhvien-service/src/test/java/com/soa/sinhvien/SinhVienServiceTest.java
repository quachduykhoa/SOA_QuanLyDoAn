package com.soa.sinhvien;

import com.soa.sinhvien.dto.SinhVienDTO;
import com.soa.sinhvien.entity.SinhVien;
import com.soa.sinhvien.repository.SinhVienRepository;
import com.soa.sinhvien.service.SinhVienService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SinhVienServiceTest {

    @Mock
    private SinhVienRepository sinhVienRepository;

    private SinhVienService sinhVienService;

    @BeforeEach
    void setUp() {
        sinhVienService = new SinhVienService(sinhVienRepository);
    }

    @Test
    void testGetAllSinhVien() {
        SinhVien sv = new SinhVien(1L, "SV01", "Nguyễn Văn A", "01/01/2002", "Hà Nội", "CNTT", 3.2, "DANG_HOC");
        when(sinhVienRepository.findAll()).thenReturn(List.of(sv));

        List<SinhVienDTO> list = sinhVienService.getAllSinhVien();

        assertEquals(1, list.size());
        assertEquals("SV01", list.get(0).getMaSv());
    }

    @Test
    void testGetSinhVienByMaSv_Found() {
        SinhVien sv = new SinhVien(1L, "SV01", "Nguyễn Văn A", "01/01/2002", "Hà Nội", "CNTT", 3.2, "DANG_HOC");
        when(sinhVienRepository.findByMaSv("SV01")).thenReturn(Optional.of(sv));

        SinhVienDTO result = sinhVienService.getSinhVienByMaSv("SV01");

        assertNotNull(result);
        assertEquals("SV01", result.getMaSv());
        assertEquals("Nguyễn Văn A", result.getHoTen());
    }

    @Test
    void testGetSinhVienByMaSv_NotFound() {
        when(sinhVienRepository.findByMaSv("SV99")).thenReturn(Optional.empty());

        SinhVienDTO result = sinhVienService.getSinhVienByMaSv("SV99");

        assertNull(result);
    }

    @Test
    void testCreateSinhVien_Success() {
        SinhVienDTO dto = new SinhVienDTO(null, "SV02", "Trần Thị B", "02/02/2002", "Đà Nẵng", "CNTT", 3.5, "DANG_HOC");
        when(sinhVienRepository.existsByMaSv("SV02")).thenReturn(false);
        when(sinhVienRepository.save(any(SinhVien.class))).thenAnswer(i -> {
            SinhVien sv = i.getArgument(0);
            sv.setId(2L);
            return sv;
        });

        SinhVienDTO created = sinhVienService.createSinhVien(dto);

        assertNotNull(created);
        assertEquals(2L, created.getId());
        assertEquals("SV02", created.getMaSv());
    }

    @Test
    void testCreateSinhVien_DuplicateMaSv_Throws() {
        SinhVienDTO dto = new SinhVienDTO(null, "SV01", "Nguyễn Văn A", null, null, null, null, null);
        when(sinhVienRepository.existsByMaSv("SV01")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> sinhVienService.createSinhVien(dto));
    }
}
