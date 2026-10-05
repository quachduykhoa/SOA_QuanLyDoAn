package com.soa.dangky.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.dangky.dto.ApiResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Xử lý ngoại lệ liên dịch vụ (OpenFeign) trả về HTTP đúng chuẩn nghiệp vụ.
 * Khi sinhvien-service / detai-service trả 404 (mã không tồn tại), Feign ném
 * FeignException.NotFound thay vì trả về null - cần bắt riêng để map về 400.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Void>> handleFeignException(FeignException e) {
        if (e.status() == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(extractMessage(e)));
        }
        log.error("Lỗi gọi liên dịch vụ qua OpenFeign: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.error("Không thể kết nối tới dịch vụ xác thực bên ngoài"));
    }

    /**
     * Đọc trường "message" từ body lỗi JSON mà dịch vụ bên kia trả về
     * (ví dụ: "Không tìm thấy sinh viên với mã: SV999").
     */
    private String extractMessage(FeignException e) {
        byte[] content = e.content();
        if (content != null && content.length > 0) {
            try {
                JsonNode node = objectMapper.readTree(content);
                if (node.hasNonNull("message")) {
                    return node.get("message").asText();
                }
            } catch (Exception ignored) {
                // body không phải JSON hợp lệ -> dùng thông báo mặc định
            }
        }
        return "Không tìm thấy tài nguyên được yêu cầu";
    }
}
