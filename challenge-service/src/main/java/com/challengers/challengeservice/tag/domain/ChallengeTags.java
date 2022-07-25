package com.challengers.challengeservice.tag.domain;

import com.challengers.challengeservice.challengetag.domain.ChallengeTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class ChallengeTags {
	@OneToMany(mappedBy = "challenge", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ChallengeTag> challengeTags = new ArrayList<>();

	public static ChallengeTags empty() {
		return new ChallengeTags();
	}

	public List<Tag> getTags() {
		return challengeTags.stream()
			.map(ChallengeTag::getTag)
			.collect(Collectors.toList());
	}

	public List<String> getStringTags() {
		return challengeTags.stream()
				.map(challengeTag -> challengeTag.getTag().getName())
				.collect(Collectors.toList());
	}


	public void addChallengeTag(ChallengeTag challengeTag) {
		if (!isContainChallengeTag(challengeTag))
			challengeTags.add(challengeTag);
	}

	private boolean isContainChallengeTag(ChallengeTag other) {
		return challengeTags.stream()
			.anyMatch(challengeTag -> challengeTag.equals(other));
	}
}
