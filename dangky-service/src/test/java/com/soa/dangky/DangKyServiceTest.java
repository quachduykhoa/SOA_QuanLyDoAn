package com.soa.dangky;

import com.soa.dangky.client.DeTaiClient;
import com.soa.dangky.client.SinhVienClient;
import com.soa.dangky.dto.*;
import com.soa.dangky.entity.DangKy;
import com.soa.dangky.repository.DangKyRepository;
import com.soa.dangky.service.DangKyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DangKyServiceTest {

    @Mock
    private DangKyRepository dangKyRepository;

    @Mock
    private SinhVienClient sinhVienClient;

    @Mock
    private DeTaiClient deTaiClient;

    private DangKyService dangKyService;

    @BeforeEach
    void setUp() {
        dangKyService = new DangKyService(dangKyRepository, sinhVienClient, deTaiClient);
    }

    // ── CRUD cơ bản ──────────────────────────────────────────────

    @Test
    @DisplayName("CRUD: Lấy danh sách toàn bộ lượt đăng ký")
    void testGetAllDangKy() {
        DangKy dk = new DangKy(1L, "SV01", "DT01", LocalDateTime.now(), "Ghi chú");
        when(dangKyRepository.findAll()).thenReturn(List.of(dk));

        List<DangKyResponseDTO> list = dangKyService.getAllDangKy();

        assertEquals(1, list.size());
        assertEquals("SV01", list.get(0).getMaSv());
        assertEquals("DT01", list.get(0).getMaDeTai());
    }

    @Test
    @DisplayName("CRUD: Lấy đăng ký theo ID - Tìm thấy")
    void testGetDangKyById_Found() {
        DangKy dk = new DangKy(1L, "SV01", "DT01", LocalDateTime.now(), "Ghi chú");
        when(dangKyRepository.findById(1L)).thenReturn(Optional.of(dk));

        DangKyResponseDTO result = dangKyService.getDangKyById(1L);

        assertNotNull(result);
        assertEquals("SV01", result.getMaSv());
    }

    @Test
    @DisplayName("CRUD: Lấy đăng ký theo ID - Không tìm thấy trả về null")
    void testGetDangKyById_NotFound() {
        when(dangKyRepository.findById(99L)).thenReturn(Optional.empty());

        DangKyResponseDTO result = dangKyService.getDangKyById(99L);

        assertNull(result);
    }

    @Test
    @DisplayName("SOA Flow: Đăng ký thành công - SV tồn tại, ĐT tồn tại, lưu DB thành công")
    void testRegister_Success() {
        DangKyRequestDTO request = new DangKyRequestDTO("SV01", "DT01", "Nguyện vọng 1");

        SinhVienDTO svDTO = new SinhVienDTO(1L, "SV01", "Nguyễn Văn A", "2002-01-01", "Hà Nội", "CNTT", 3.5, "DANG_HOC");
        when(sinhVienClient.getSinhVienByMaSv("SV01")).thenReturn(ApiResponse.success("OK", svDTO));

        DeTaiDTO dtDTO = new DeTaiDTO(1L, "DT01", "Đề tài 1", "Mô tả", "TS. Hùng");
        when(deTaiClient.getDeTaiByMaDeTai("DT01")).thenReturn(ApiResponse.success("OK", dtDTO));

        when(dangKyRepository.save(any(DangKy.class))).thenAnswer(i -> {
            DangKy dk = i.getArgument(0);
            dk.setId(100L);
            return dk;
        });

        DangKyResponseDTO response = dangKyService.register(request);

        assertNotNull(response);
        assertEquals("SV01", response.getMaSv());
        assertEquals("DT01", response.getMaDeTai());
        verify(dangKyRepository, times(1)).save(any(DangKy.class));
    }

    @Test
    @DisplayName("SOA Flow: Sinh viên không tồn tại thì ném ngoại lệ")
    void testRegister_SinhVienNotFound_Throws() {
        DangKyRequestDTO request = new DangKyRequestDTO("SV99", "DT01", "Ghi chú");
        when(sinhVienClient.getSinhVienByMaSv("SV99")).thenReturn(ApiResponse.error("Không tìm thấy"));

        assertThrows(IllegalArgumentException.class, () -> dangKyService.register(request));
        verifyNoInteractions(deTaiClient);
        verify(dangKyRepository, never()).save(any());
    }

    @Test
    @DisplayName("SOA Flow: Đề tài không tồn tại thì ném ngoại lệ")
    void testRegister_DeTaiNotFound_Throws() {
        DangKyRequestDTO request = new DangKyRequestDTO("SV01", "DT99", "Ghi chú");

        SinhVienDTO svDTO = new SinhVienDTO(1L, "SV01", "Nguyễn Văn A", "2002-01-01", "Hà Nội", "CNTT", 3.5, "DANG_HOC");
        when(sinhVienClient.getSinhVienByMaSv("SV01")).thenReturn(ApiResponse.success("OK", svDTO));
        when(deTaiClient.getDeTaiByMaDeTai("DT99")).thenReturn(ApiResponse.error("Không tìm thấy"));

        assertThrows(IllegalArgumentException.class, () -> dangKyService.register(request));
        verify(dangKyRepository, never()).save(any());
    }

    @Test
    @DisplayName("CRUD: Xóa đăng ký thành công")
    void testDeleteDangKy_Success() {
        when(dangKyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(dangKyRepository).deleteById(1L);

        assertDoesNotThrow(() -> dangKyService.deleteDangKy(1L));
        verify(dangKyRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("CRUD: Xóa đăng ký không tồn tại thì ném ngoại lệ")
    void testDeleteDangKy_NotFound_Throws() {
        when(dangKyRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> dangKyService.deleteDangKy(99L));
        verify(dangKyRepository, never()).deleteById(any());
    }
}
