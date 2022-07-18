package com.challengers.challengeservice.photocheck.controller;

import com.challengers.challengeservice.challengephoto.domain.ChallengePhoto;
import com.challengers.challengeservice.common.documentation.Documentation;
import com.challengers.challengeservice.photocheck.domain.PhotoCheck;
import com.challengers.challengeservice.photocheck.domain.PhotoCheckStatus;
import com.challengers.challengeservice.photocheck.dto.CheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckResponse;
import com.challengers.challengeservice.photocheck.service.PhotoCheckService;
import com.challengers.challengeservice.testtool.StringToken;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static com.challengers.challengeservice.testtool.UploadSupporter.uploadMockSupport;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhotoCheckController.class)
class PhotoCheckControllerTest extends Documentation {
    @MockBean PhotoCheckService photoCheckService;

    UserChallenge userChallenge;
    ChallengePhoto challengePhoto;
    PhotoCheck photoCheck;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userChallenge = UserChallenge.builder()
                .id(1L)
                .build();

        challengePhoto = ChallengePhoto.builder()
                .id(1L)
                .build();

        photoCheck = PhotoCheck.builder()
                .id(1L)
                .userChallenge(userChallenge)
                .challengePhoto(challengePhoto)
                .round(1)
                .status(PhotoCheckStatus.WAITING)
                .build();
    }

    @Test
    void getPhotoCheck() throws Exception {
        when(photoCheckService.findPhotoCheck(any())).thenReturn(PhotoCheckResponse.of(photoCheck));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/photo_check/{photo_check_id}",1L)
                .header("Authorization", StringToken.getToken()))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.getPhotoCheck());
    }

    @Test
    void addPhotoCheck() throws Exception {
        PhotoCheckRequest photoCheckRequest = new PhotoCheckRequest(1L,
                new MockMultipartFile("photo","photo".getBytes(StandardCharsets.UTF_8)));
        when(photoCheckService.addPhotoCheck(any(),any())).thenReturn(1L);

        mockMvc.perform(uploadMockSupport(multipart("/api/photo_check"),photoCheckRequest)
                .header("Authorization", StringToken.getToken())
                .header("userId", "1")
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(PhotoCheckDocumentation.addPhotoCheck());
    }

    @Test
    void pass() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L,2L,3L)));

        mockMvc.perform(post("/api/photo_check/pass")
                .header("Authorization", StringToken.getToken())
                .header("userId", "1")
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.pass());
    }

    @Test
    void fail() throws Exception {
        CheckRequest checkRequest = new CheckRequest(new ArrayList<>(Arrays.asList(1L)));

        mockMvc.perform(post("/api/photo_check/fail")
                .header("Authorization", StringToken.getToken())
                .header("userId", "1")
                .content(mapper.writeValueAsString(checkRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(PhotoCheckDocumentation.fail());
    }
}