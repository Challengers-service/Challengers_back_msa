package com.challengers.challengeservice.challenge.dto;

import com.challengers.challengeservice.challenge.domain.Challenge;
import com.challengers.challengeservice.challenge.domain.CheckFrequencyType;
import com.challengers.challengeservice.tag.dto.TagResponse;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ChallengeDetailResponse {
    private Long id;

    private Long hostId;

    private String name;
    private String imageUrl;
    private String photoDescription;
    private String challengeRule;
    private CheckFrequencyType checkFrequencyType;
    private Integer checkTimesPerRound;
    private String category;
    private String startDate;
    private String endDate;
    private int depositPoint;
    private String introduction;
    private int userCountLimit;
    private String status;
    private List<TagResponse> tags;
    private List<String> examplePhotos;
    private String createdDate;

    private int userCount;
    private Float starRatingAvg;
    private int reviewCount;
    private boolean cart;
    private long reward;

    public static ChallengeDetailResponse of(Challenge challenge, int userCount, float starRatingAvg,
                                             int reviewCount, boolean cart, long reward) {
        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getHostId(),
                challenge.getName(),
                challenge.getImageUrl(),
                challenge.getPhotoDescription(),
                challenge.getChallengeRule(),
                challenge.getCheckFrequencyType(),
                challenge.getCheckTimesPerRound(),
                challenge.getCategory().toString(),
                challenge.getStartDate().toString(),
                challenge.getEndDate().toString(),
                challenge.getDepositPoint(),
                challenge.getIntroduction(),
                challenge.getUserCountLimit(),
                challenge.getStatus().toString(),
                TagResponse.listOf(challenge.getChallengeTags().getTags()),
                challenge.getExamplePhotoUrls(),
                challenge.getCreatedDate().toLocalDate().toString(),
                userCount,
                starRatingAvg,
                reviewCount,
                cart,
                reward
        );
    }
}
