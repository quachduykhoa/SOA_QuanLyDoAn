package com.soa.dangky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.dangky.controller.DangKyController;
import com.soa.dangky.dto.DangKyRequestDTO;
import com.soa.dangky.dto.DangKyResponseDTO;
import com.soa.dangky.exception.GlobalExceptionHandler;
import com.soa.dangky.service.DangKyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DangKyControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // JWT giả lập cho tham số @AuthenticationPrincipal Jwt jwt của controller.
    // MockMvc standaloneSetup không chạy Spring Security -> ta tự cấp Jwt qua resolver bên dưới.
    private Jwt testJwt;

    private Jwt buildJwt(String role, String maSv) {
        var builder = Jwt.withTokenValue("test-token")
                .header("alg", "HS256")
                .claim("role", role)
                .claim("name", "Test User");
        if (maSv != null) {
            builder.claim("maSv", maSv);
        }
        return builder.build();
    }

    @BeforeEach
    void setUp() {
        // Mặc định là token ADMIN; test IDOR sẽ ghi đè testJwt trước khi perform
        testJwt = buildJwt("ADMIN", null);

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
            public void deleteDangKy(Long id, String role, String maSvHienTai) {
                if (id == 99L) {
                    throw new IllegalArgumentException("Không tìm thấy đăng ký với ID: " + id);
                }
            }
        };

        DangKyController controller = new DangKyController(stubService);

        // Resolver tự cấp JWT cho mọi tham số có kiểu Jwt (mô phỏng @AuthenticationPrincipal)
        HandlerMethodArgumentResolver jwtResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType() == Jwt.class;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest request, WebDataBinderFactory binderFactory) {
                return testJwt;
            }
        };

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())      // để AccessDeniedException -> 403
                .setCustomArgumentResolvers(jwtResolver)
                .build();
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

    @Test
    @DisplayName("API: POST /api/v1/dangky với token không khớp mã SV trả về 403 (chống IDOR)")
    void testRegister_TokenKhongKhopMaSV_TraVe403() throws Exception {
        testJwt = buildJwt("SINH_VIEN", "SV001");                          // token của SV001
        DangKyRequestDTO request = new DangKyRequestDTO("SV002", "DT01", "Ghi chú"); // nhưng đăng ký thay SV002

        mockMvc.perform(post("/api/v1/dangky")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }
}
