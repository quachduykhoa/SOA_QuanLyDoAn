package com.soa.dangky.client;

import com.soa.dangky.dto.ApiResponse;
import com.soa.dangky.dto.DeTaiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "detai-service", url = "${services.detai.url:http://localhost:8082}")
public interface DeTaiClient {

    @GetMapping("/api/v1/detai/{maDeTai}")
    ApiResponse<DeTaiDTO> getDeTaiByMaDeTai(@PathVariable("maDeTai") String maDeTai);

    // POST vi Feign khong ho tro PATCH
    @PostMapping("/api/v1/detai/{maDeTai}/update-slot")
    ApiResponse<DeTaiDTO> updateSlot(@PathVariable("maDeTai") String maDeTai,
                                     @RequestParam("action") String action);

    // POST vi Feign khong ho tro PATCH (giong updateSlot o tren)
    @PostMapping("/api/v1/detai/{maDeTai}/increment-slot")
    ApiResponse<DeTaiDTO> incrementSlot(@PathVariable("maDeTai") String maDeTai);
}
