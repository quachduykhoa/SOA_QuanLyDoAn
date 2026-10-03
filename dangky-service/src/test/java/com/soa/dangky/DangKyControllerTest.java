package com.soa.dangky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.dangky.controller.DangKyController;
import com.soa.dangky.dto.DangKyRequestDTO;
import com.soa.dangky.dto.DangKyResponseDTO;
import com.soa.dangky.service.DangKyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DangKyControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        DangKyService stubService = new DangKyService(null, null, null) {
            @Override
            public List<DangKyResponseDTO> getAllDangKy() {
                return List.of(new DangKyResponseDTO(1L, "SV001", "DT01", "2026-09-30T18:00:00", "Ghi chú"));
            }

            @Override
            public DangKyResponseDTO getDangKyById(Long id) {
                if (id == 1L) {
                    return new DangKyResponseDTO(1L, "SV001", "DT01", "2026-09-30T18:00:00", "Ghi chú");
                }
                return null;
            }

            @Override
            public DangKyResponseDTO register(DangKyRequestDTO request) {
                return new DangKyResponseDTO(1L, request.getMaSv(), request.getMaDeTai(), "2026-09-30T18:00:00", request.getGhiChu());
            }

            @Override
            public void deleteDangKy(Long id) {
                if (id == 99L) {
                    throw new IllegalArgumentException("Không tìm thấy đăng ký với ID: " + id);
                }
            }
        };

        DangKyController controller = new DangKyController(stubService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("API: GET /api/v1/dangky trả về danh sách đăng ký")
    void testGetAllDangKy() throws Exception {
        mockMvc.perform(get("/api/v1/dangky"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].maSv").value("SV001"))
                .andExpect(jsonPath("$.data[0].maDeTai").value("DT01"));
    }

    @Test
    @DisplayName("API: GET /api/v1/dangky/{id} tìm thấy trả về 200")
    void testGetDangKyById_Found() throws Exception {
        mockMvc.perform(get("/api/v1/dangky/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maSv").value("SV001"));
    }

    @Test
    @DisplayName("API: GET /api/v1/dangky/{id} không tìm thấy trả về 404")
    void testGetDangKyById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/dangky/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("API: POST /api/v1/dangky đăng ký thành công trả về 201 Created")
    void testRegister() throws Exception {
        DangKyRequestDTO request = new DangKyRequestDTO("SV001", "DT01", "Ghi chú đăng ký");

        mockMvc.perform(post("/api/v1/dangky")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maSv").value("SV001"))
                .andExpect(jsonPath("$.data.maDeTai").value("DT01"));
    }

    @Test
    @DisplayName("API: DELETE /api/v1/dangky/{id} xóa thành công trả về 200")
    void testDeleteDangKy_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/dangky/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
