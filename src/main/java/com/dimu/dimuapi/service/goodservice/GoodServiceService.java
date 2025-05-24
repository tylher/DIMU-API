package com.dimu.dimuapi.service.goodservice;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.CreateGoodServiceDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface GoodServiceService {
    public ApiResponseDto createGoodService(CreateGoodServiceDto createGoodServiceDto);
}
