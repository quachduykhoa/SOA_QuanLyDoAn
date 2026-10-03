package com.soa.sinhvien;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.sinhvien.controller.SinhVienController;
import com.soa.sinhvien.dto.SinhVienDTO;
import com.soa.sinhvien.service.SinhVienService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SinhVienControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        SinhVienService stubService = new SinhVienService(null) {
            @Override
            public List<SinhVienDTO> getAllSinhVien() {
                return List.of(new SinhVienDTO(1L, "SV001", "Nguyen Van A", "2002-01-01", "Ha Noi", "CNTT", 3.2, "DANG_HOC"));
            }

            @Override
            public SinhVienDTO createSinhVien(SinhVienDTO dto) {
                return new SinhVienDTO(2L, dto.getMaSv(), dto.getHoTen(), dto.getNgaySinh(), dto.getQueQuan(), dto.getChuyenNganh(), dto.getGpa(), dto.getTrangThai());
            }

            @Override
            public SinhVienDTO getSinhVienByMaSv(String maSv) {
                if ("SV001".equals(maSv)) {
                    return new SinhVienDTO(1L, "SV001", "Nguyen Van A", "2002-01-01", "Ha Noi", "CNTT", 3.2, "DANG_HOC");
                }
                return null;
            }
        };

        SinhVienController controller = new SinhVienController(stubService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/sinhvien trả về 200 OK và danh sách sinh viên")
    void testGetAllSinhVien() throws Exception {
        mockMvc.perform(get("/api/v1/sinhvien"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].maSv").value("SV001"))
                .andExpect(jsonPath("$.data[0].hoTen").value("Nguyen Van A"));
    }

    @Test
    @DisplayName("API Contract: POST /api/v1/sinhvien tạo mới sinh viên thành công trả về 201 Created")
    void testCreateSinhVien() throws Exception {
        SinhVienDTO requestDto = new SinhVienDTO(null, "SV002", "Tran Thi B", "2002-05-10", "Da Nang", "CNTT", 3.5, "DANG_HOC");

        mockMvc.perform(post("/api/v1/sinhvien")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maSv").value("SV002"));
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/sinhvien/{maSv} trả về 200 OK khi tìm thấy sinh viên")
    void testGetSinhVienByMaSv_Success() throws Exception {
        mockMvc.perform(get("/api/v1/sinhvien/SV001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maSv").value("SV001"))
                .andExpect(jsonPath("$.data.hoTen").value("Nguyen Van A"));
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/sinhvien/{maSv} trả về 404 NOT FOUND khi không tìm thấy")
    void testGetSinhVienByMaSv_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/sinhvien/SV999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
