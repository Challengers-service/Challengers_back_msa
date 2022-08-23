package com.challengers.pointservice.point.controller;

import com.challengers.pointservice.common.documentation.Documentation;
import com.challengers.pointservice.point.domain.PointTransactionType;
import com.challengers.pointservice.point.dto.PointTransactionResponse;
import com.challengers.pointservice.point.dto.PointResponse;
import com.challengers.pointservice.point.global.dto.PointUpdateRequest;
import com.challengers.pointservice.point.service.PointService;
import com.challengers.pointservice.testtool.StringToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointController.class)
public class PointControllerTest extends Documentation {
    @MockBean PointService pointService;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("나의 현재 포인트를 조회한다.")
    void getMyPoint() throws Exception {
        when(pointService.getMyPoint(any())).thenReturn(new PointResponse(1000L));

        mockMvc.perform(get("/api/point")
                    .header("Authorization", StringToken.getToken())
                    .header("userId",1L))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.getMyPoint());
    }

    @Test
    @DisplayName("나의 포인트 내역을 조회한다.")
    void getMyPointHistory() throws Exception {
        PageImpl<PointTransactionResponse> page = new PageImpl<>(
                Arrays.asList(
                    new PointTransactionResponse(-1000L, LocalDateTime.now(), PointTransactionType.DEPOSIT, 200L),
                    new PointTransactionResponse(100L, LocalDateTime.now(), PointTransactionType.ATTENDANCE, 300L)
                )
                , PageRequest.of(0,6),2);

        when(pointService.getMyPointTransaction(any(),any())).thenReturn(page);

        mockMvc.perform(get("/api/point/transaction")
                    .header("Authorization", StringToken.getToken())
                    .header("userId",1L))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.getMyPointTransaction());
    }

    @Test
    @DisplayName("나의 포인트를 업데이트 한다.")
    void updateMyPoint() throws Exception {
        PointUpdateRequest request = new PointUpdateRequest(100L, "ATTENDANCE");

        doNothing().when(pointService).updatePoint(any(),any());

        mockMvc.perform(put("/api/point")
                        .header("Authorization", StringToken.getToken())
                        .header("userId",1L)
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.updateMyPoint());
    }

    @Test
    @DisplayName("나의 포인트 정보를 최초 1회 생성한다.")
    void createPointInfo() throws Exception {
        mockMvc.perform(post("/api/point")
                        .header("Authorization", StringToken.getToken())
                        .header("userId",1L))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.createPointInfo());
    }

    @Test
    @DisplayName("나의 포인트 정보를 모두 삭제한다.")
    void removePointInfo() throws Exception {
        mockMvc.perform(delete("/api/point")
                        .header("Authorization", StringToken.getToken())
                        .header("userId",1L))
                .andExpect(status().isOk())
                .andDo(PointDocumentation.removePointInfo());
    }
}
