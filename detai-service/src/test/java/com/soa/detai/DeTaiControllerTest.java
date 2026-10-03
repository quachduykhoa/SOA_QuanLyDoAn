package com.soa.detai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.detai.controller.DeTaiController;
import com.soa.detai.dto.DeTaiDTO;
import com.soa.detai.service.DeTaiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeTaiControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        DeTaiService stubService = new DeTaiService(null) {
            @Override
            public List<DeTaiDTO> getAllDeTai() {
                return List.of(new DeTaiDTO(1L, "DT01", "He thong SOA", "Mo ta", "TS. Tran Van B"));
            }

            @Override
            public DeTaiDTO createDeTai(DeTaiDTO dto) {
                return new DeTaiDTO(2L, dto.getMaDeTai(), dto.getTenDeTai(), dto.getMoTa(), dto.getGvhd());
            }

            @Override
            public DeTaiDTO getDeTaiByMaDeTai(String maDeTai) {
                if ("DT01".equals(maDeTai)) {
                    return new DeTaiDTO(1L, "DT01", "He thong SOA", "Mo ta", "TS. Tran Van B");
                }
                return null;
            }

            @Override
            public void deleteDeTai(Long id) {
                if (id == 999L) {
                    throw new IllegalArgumentException("Không tìm thấy");
                }
            }
        };

        DeTaiController controller = new DeTaiController(stubService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/detai trả về danh sách đề tài")
    void testGetAllDeTai() throws Exception {
        mockMvc.perform(get("/api/v1/detai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].maDeTai").value("DT01"))
                .andExpect(jsonPath("$.data[0].tenDeTai").value("He thong SOA"));
    }

    @Test
    @DisplayName("API Contract: POST /api/v1/detai tạo đề tài mới thành công trả về 201 Created")
    void testCreateDeTai() throws Exception {
        DeTaiDTO requestDto = new DeTaiDTO(null, "DT02", "Big Data", "Mo ta", "TS. Le Van C");

        mockMvc.perform(post("/api/v1/detai")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maDeTai").value("DT02"));
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/detai/{maDeTai} trả về 200 OK khi tìm thấy đề tài")
    void testGetDeTaiByMaDeTai_Success() throws Exception {
        mockMvc.perform(get("/api/v1/detai/DT01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.maDeTai").value("DT01"))
                .andExpect(jsonPath("$.data.tenDeTai").value("He thong SOA"));
    }

    @Test
    @DisplayName("API Contract: GET /api/v1/detai/{maDeTai} trả về 404 NOT FOUND khi không tìm thấy")
    void testGetDeTaiByMaDeTai_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/detai/DT99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("API Contract: DELETE /api/v1/detai/{id} xóa đề tài thành công trả về 200 OK")
    void testDeleteDeTai() throws Exception {
        mockMvc.perform(delete("/api/v1/detai/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
