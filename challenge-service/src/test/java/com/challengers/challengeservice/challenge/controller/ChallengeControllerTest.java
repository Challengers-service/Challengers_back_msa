package com.challengers.challengeservice.challenge.controller;

import com.challengers.challengeservice.common.documentation.Documentation;
import com.challengers.challengeservice.challenge.domain.ChallengeStatus;
import com.challengers.challengeservice.challenge.domain.CheckFrequencyType;
import com.challengers.challengeservice.challenge.dto.ChallengeDetailResponse;
import com.challengers.challengeservice.challenge.dto.ChallengeRequest;
import com.challengers.challengeservice.challenge.dto.ChallengeResponse;
import com.challengers.challengeservice.challenge.dto.ChallengeUpdateRequest;
import com.challengers.challengeservice.challenge.service.ChallengeService;
import com.challengers.challengeservice.tag.dto.TagResponse;
import com.challengers.challengeservice.testtool.StringToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static com.challengers.challengeservice.testtool.UploadSupporter.uploadMockSupport;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChallengeController.class)
class ChallengeControllerTest extends Documentation {
    @MockBean ChallengeService challengeService;

    private ChallengeRequest challengeRequest;


    @BeforeEach
    void setUp() {
        challengeRequest = ChallengeRequest.builder()
                .name("미라클 모닝 - 아침 7시 기상")
                .image(new MockMultipartFile("테스트사진.png","테스트사진.png","image/png","image file".getBytes()))
                .photoDescription("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .challengeRule("중복된 사진을 올리면 안됩니다.")
                .checkFrequencyType("EVERY_DAY")
                .checkTimesPerRound(1)
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .depositPoint(1000)
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .userCountLimit(2000)
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("예시사진1.png","예시사진1.png","image/png","photo file1".getBytes()),
                        new MockMultipartFile("예시사진2.png","예시사진2.png","image/png","photo file2".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("미라클모닝", "기상")))
                .build();
    }

    @Test
    @DisplayName("챌린지 생성")
    void createChallenge() throws Exception{
        when(challengeService.create(any(),any())).thenReturn(1L);

        mockMvc.perform(uploadMockSupport(multipart("/api/challenge"),challengeRequest)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1")
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(ChallengeDocumentation.createChallenge());
    }

    @Test
    @DisplayName("챌린지 삭제")
    void deleteChallenge() throws Exception{
        doNothing().when(challengeService).delete(any(),any());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1"))
                .andExpect(status().isNoContent())
                .andDo(ChallengeDocumentation.deleteChallenge());
    }

    @Test
    @DisplayName("챌린지 상세 정보 조회")
    void findChallenge() throws Exception{
        ChallengeDetailResponse challengeDetailResponse = new ChallengeDetailResponse(1L, 1L, "챌린지 이름", "https://challengeImageUrl.png", "예시 사진 설명","챌린지 규칙", CheckFrequencyType.EVERY_DAY, 1,
                "EXERCISE","2022-06-21","2022-07-21",1000,"챌린지 소개글",3.5f,0,32,2000, ChallengeStatus.IN_PROGRESS.toString(),
                new ArrayList<>(Arrays.asList(new TagResponse(1L,"미라클모닝"), new TagResponse(2L, "기상"))),
                new ArrayList<>(Arrays.asList("https://examplePhotoUrl1.png","https://examplePhotoUrl2.png")), "2022-01-01", false, 100);
        when(challengeService.findChallenge(any(),any())).thenReturn(challengeDetailResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/challenge/{id}",1)
                .header("Authorization", StringToken.getToken())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findChallenge());
    }

    @Test
    @DisplayName("챌린지 참여")
    void joinChallenge() throws Exception{
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/challenge/join/{id}",1)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1"))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.joinChallenge());
    }

    @Test
    @DisplayName("챌린지 수정")
    void updateChallenge() throws Exception{
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                new MockMultipartFile("image file","image file".getBytes()),
                "수정된 챌린지 소개글 입니다.");

        mockMvc.perform(uploadMockSupport(RestDocumentationRequestBuilders.multipart("/api/challenge/{id}",1L), challengeUpdateRequest)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1")
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("PUT");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.updateChallenge());

        verify(challengeService).update(any(),any(),any());
    }

    @Test
    @DisplayName("참여 가능한 챌린지 조회")
    void findCanJoinChallenges() throws Exception{
        PageImpl<ChallengeResponse> page = new PageImpl<>(Arrays.asList(new ChallengeResponse(1L, "매일 아침 7시에 일어나기!", "LIFE",
                        new ArrayList<>(Arrays.asList("미라클 모닝", "기상")), "2022.07.02", 10, false,
                        new ArrayList<>(Arrays.asList(1L, 2L, 3L))),
                new ChallengeResponse(2L, "하루 물 2L 마시기", "LIFE",
                        new ArrayList<>(Arrays.asList("수분 섭취", "건강")), "2022.07.03", 14, true,
                        new ArrayList<>(Arrays.asList(1L, 2L)))),PageRequest.of(0,6, Sort.by("created_date")),2);

        when(challengeService.findReadyOrInProgressChallenges(any(),any())).thenReturn(page);
        mockMvc.perform(get("/api/challenge")
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(ChallengeDocumentation.findCanJoinChallenges());
    }

}