package com.soa.dangky.client;

import com.soa.dangky.dto.ApiResponse;
import com.soa.dangky.dto.SinhVienDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sinhvien-service", url = "${services.sinhvien.url:http://localhost:8081}")
public interface SinhVienClient {

    @GetMapping("/api/v1/sinhvien/{maSv}")
    ApiResponse<SinhVienDTO> getSinhVienByMaSv(@PathVariable("maSv") String maSv);
}
