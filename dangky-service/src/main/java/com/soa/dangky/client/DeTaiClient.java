package com.soa.dangky.client;

import com.soa.dangky.dto.ApiResponse;
import com.soa.dangky.dto.DeTaiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "detai-service", url = "${services.detai.url:http://localhost:8082}")
public interface DeTaiClient {

    @GetMapping("/api/v1/detai/{maDeTai}")
    ApiResponse<DeTaiDTO> getDeTaiByMaDeTai(@PathVariable("maDeTai") String maDeTai);
}
