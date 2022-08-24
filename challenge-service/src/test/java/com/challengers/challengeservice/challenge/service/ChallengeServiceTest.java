package com.challengers.challengeservice.challenge.service;

import com.challengers.challengeservice.cart.repository.CartRepository;
import com.challengers.challengeservice.challenge.domain.Category;
import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.domain.ChallengeStatus;
import com.challengers.challengeservice.challenge.domain.CheckFrequencyType;
import com.challengers.challengeservice.challenge.dto.*;
import com.challengers.challengeservice.challenge.repository.ChallengeRepository;
import com.challengers.challengeservice.challengephoto.repository.ChallengePhotoRepository;
import com.challengers.challengeservice.common.AwsS3Uploader;
import com.challengers.challengeservice.global.client.PointClient;
import com.challengers.challengeservice.photocheck.repository.PhotoCheckRepository;
import com.challengers.challengeservice.tag.domain.Tag;
import com.challengers.challengeservice.tag.repository.TagRepository;
import com.challengers.challengeservice.userchallenge.domain.UserChallenge;
import com.challengers.challengeservice.userchallenge.repository.UserChallengeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {
    @Mock ChallengeRepository challengeRepository;
    @Mock TagRepository tagRepository;
    @Mock UserChallengeRepository userChallengeRepository;
    @Mock AwsS3Uploader awsS3Uploader;
    @Mock CartRepository cartRepository;
    @Mock PhotoCheckRepository photoCheckRepository;
    @Mock ChallengePhotoRepository challengePhotoRepository;
    @Mock PointClient pointClient;

    ChallengeService challengeService;

    ChallengeRequest challengeRequest;
    Challenge challenge;

    @BeforeEach
    void setUp() {
        challengeService = new ChallengeService(challengeRepository,tagRepository,userChallengeRepository,
                awsS3Uploader,cartRepository,photoCheckRepository,challengePhotoRepository, pointClient);

        challengeRequest = ChallengeRequest.builder()
                .name("미라클 모닝 - 아침 7시 기상")
                .image(new MockMultipartFile("테스트사진.png","테스트사진.png","image/png","saf".getBytes()))
                .challengeRule("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .checkFrequencyType("EVERY_DAY")
                .category("LIFE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .depositPoint(1000)
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .examplePhotos(new ArrayList<>(Arrays.asList(
                        new MockMultipartFile("예시사진1.png","예시사진1.png","image/png","asgas".getBytes()),
                        new MockMultipartFile("예시사진2.png","예시사진2.png","image/png","asgasagagas".getBytes())
                )))
                .tags(new ArrayList<>(Arrays.asList("미라클모닝", "기상")))
                .build();

        challenge = Challenge.builder()
                .id(1L)
                .hostId(1L)
                .name("미라클 모닝 - 아침 7시 기상")
                .imageUrl("https://imageUrl.png")
                .challengeRule("7시를 가르키는 시계와 본인이 같이 나오게 사진을 찍으시면 됩니다.")
                .checkFrequencyType(CheckFrequencyType.EVERY_DAY)
                .category(Category.LIFE)
                .starRating(3.5f)
                .depositPoint(1000)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .introduction("매일 아침 7시에 일어나면 하루가 개운합니다.")
                .userCountLimit(2000)
                .status(ChallengeStatus.IN_PROGRESS)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("챌린지 개설 성공")
    void create() {
        //given
        when(awsS3Uploader.uploadImage(any()))
                .thenReturn("https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e2bce0.PNG");
        when(tagRepository.findTagByName(any())).thenReturn(Optional.of(new Tag("임시 태그")));

        //when
        challengeService.create(challengeRequest, 1L);

        //then
        verify(challengeRepository).save(any());
    }

    @Test
    @DisplayName("챌린지 대표 이미지, 소개글 수정 성공")
    void update_all() {
        //given
        String updatedImageUrl = "https://challengers-bucket.s3.ap-northeast-2.amazonaws.com/1747f32c-e5083c5e20.PNG";
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                new MockMultipartFile("image","image".getBytes()), "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(awsS3Uploader.uploadImage(any())).thenReturn(updatedImageUrl);


        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHostId());

        //then
        assertThat(challenge.getImageUrl()).isEqualTo(updatedImageUrl);
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("챌린지 소개글 수정 성공")
    void update_introduction() {
        //given
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        //when
        challengeService.update(challengeUpdateRequest, challenge.getId(), challenge.getHostId());

        //then
        assertThat(challenge.getIntroduction()).isEqualTo(challengeUpdateRequest.getIntroduction());
    }

    @Test
    @DisplayName("챌린지 수정 실패 - 권한이 없습니다.")
    void update_unauthorized() {
        ChallengeUpdateRequest challengeUpdateRequest = new ChallengeUpdateRequest(
                null, "수정된 챌린지 소개글 입니다.");
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        assertThatThrownBy(()-> challengeService.update(challengeUpdateRequest, challenge.getId(),
                challenge.getHostId()+1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 삭제 성공")
    void delete() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(1L);
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,challenge.getHostId())));

        challengeService.delete(challenge.getId(),challenge.getHostId());

        verify(challengeRepository).delete(any());
    }


    @Test
    @DisplayName("챌린지 삭제 실패 - 참가자가 2명 이상일 경우")
    void delete_fail_proceeding() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.countByChallengeId(any())).thenReturn(2L);

        assertThatThrownBy(() -> challengeService.delete(challenge.getId(),challenge.getHostId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 상세 정보 조회")
    void findChallenge() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.findByChallengeIdAndStatus(any(),any())).thenReturn(new ArrayList<>());
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.empty());

        ChallengeDetailResponse response = challengeService.findChallenge(1L, 1L);

        assertThat(response).isEqualTo(ChallengeDetailResponse
                .of(challenge, false,0L));
    }

    @Test
    @DisplayName("챌린지 참여 성공")
    void join() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));

        challengeService.join(1L,1L);

        verify(userChallengeRepository).save(any());
    }

    @Test
    @DisplayName("챌린지 참여 실패 - 참여 인원이 초과되는 경우")
    void join_failed_due_to_overcrowding() {
        Challenge challengeFull = Challenge.builder()
                .id(1L)
                .userCount(3)
                .userCountLimit(3)
                .build();
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challengeFull));

        assertThatThrownBy(()->challengeService.join(1L,1L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 참여 실패 - 이미 해당 챌린지에 참여하고 있는 경우")
    void join_failed_because_already_participating_in_the_challenge() {
        when(challengeRepository.findById(any())).thenReturn(Optional.of(challenge));
        when(userChallengeRepository.findByUserIdAndChallengeId(any(),any()))
                .thenReturn(Optional.of(UserChallenge.create(challenge,challenge.getHostId())));

        assertThatThrownBy(()->challengeService.join(1L,1L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("챌린지 검색")
    void search() {
        PageImpl<Challenge> page = new PageImpl<>(Arrays.asList(challenge
        ), PageRequest.of(0,6),2);

        when(challengeRepository.search(any(),any())).thenReturn(page);
        when(userChallengeRepository.findByChallengeId(any())).thenReturn(new ArrayList<>());
        when(cartRepository.findByChallengeIdAndUserId(any(),any())).thenReturn(Optional.empty());

        Page<ChallengeResponse> response = challengeService.search(
                new ChallengeSearchCondition(null, null, null),
                PageRequest.of(0, 6),
                1L);

        for (ChallengeResponse challengeResponse : response) {
            assertThat(challengeResponse).isEqualTo(new ChallengeResponse(challenge,false,new ArrayList<>()));
        }
    }
}
