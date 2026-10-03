package com.soa.detai;

import com.soa.detai.dto.DeTaiDTO;
import com.soa.detai.entity.DeTai;
import com.soa.detai.repository.DeTaiRepository;
import com.soa.detai.service.DeTaiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeTaiServiceTest {

    @Mock
    private DeTaiRepository deTaiRepository;

    private DeTaiService deTaiService;

    @BeforeEach
    void setUp() {
        deTaiService = new DeTaiService(deTaiRepository);
    }

    @Test
    @DisplayName("CRUD: Lấy danh sách toàn bộ đề tài")
    void testGetAllDeTai() {
        DeTai dt = new DeTai(1L, "DT01", "Nghiên cứu SOA", "Mô tả", "GV01");
        when(deTaiRepository.findAll()).thenReturn(List.of(dt));

        List<DeTaiDTO> list = deTaiService.getAllDeTai();

        assertEquals(1, list.size());
        assertEquals("DT01", list.get(0).getMaDeTai());
    }

    @Test
    @DisplayName("CRUD: Tìm đề tài theo mã - Thành công")
    void testGetDeTaiByMaDeTai_Success() {
        DeTai dt = new DeTai(1L, "DT01", "Nghiên cứu SOA", "Mô tả", "GV01");
        when(deTaiRepository.findByMaDeTai("DT01")).thenReturn(Optional.of(dt));

        DeTaiDTO result = deTaiService.getDeTaiByMaDeTai("DT01");

        assertNotNull(result);
        assertEquals("DT01", result.getMaDeTai());
        assertEquals("Nghiên cứu SOA", result.getTenDeTai());
    }

    @Test
    @DisplayName("CRUD: Tìm đề tài theo mã - Không tồn tại trả về null")
    void testGetDeTaiByMaDeTai_NotFound() {
        when(deTaiRepository.findByMaDeTai("DT99")).thenReturn(Optional.empty());

        DeTaiDTO result = deTaiService.getDeTaiByMaDeTai("DT99");

        assertNull(result);
    }

    @Test
    @DisplayName("CRUD: Tạo mới đề tài - Thành công")
    void testCreateDeTai_Success() {
        DeTaiDTO dto = new DeTaiDTO(null, "DT02", "Big Data", "Mô tả", "GV02");
        when(deTaiRepository.existsByMaDeTai("DT02")).thenReturn(false);
        when(deTaiRepository.save(any(DeTai.class))).thenAnswer(i -> {
            DeTai saved = i.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        DeTaiDTO result = deTaiService.createDeTai(dto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("DT02", result.getMaDeTai());
    }

    @Test
    @DisplayName("CRUD: Tạo mới đề tài - Trùng mã ném ngoại lệ")
    void testCreateDeTai_Duplicate_Throws() {
        DeTaiDTO dto = new DeTaiDTO(null, "DT01", "Big Data", "Mô tả", "GV02");
        when(deTaiRepository.existsByMaDeTai("DT01")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> deTaiService.createDeTai(dto));
        verify(deTaiRepository, never()).save(any());
    }

    @Test
    @DisplayName("CRUD: Xóa đề tài - Thành công")
    void testDeleteDeTai_Success() {
        when(deTaiRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deTaiRepository).deleteById(1L);

        assertDoesNotThrow(() -> deTaiService.deleteDeTai(1L));
        verify(deTaiRepository, times(1)).deleteById(1L);
    }
}
