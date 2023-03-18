package com.marketcollection.common.auth.dto;

import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.member.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private SocialType socialType;
    private String email;
    private String name;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, SocialType socialType, String email, String name) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.socialType = socialType;
        this.email = email;
        this.name = name;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        else if("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }

        return ofGoogle("id", attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .socialType(SocialType.GOOGLE)
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .socialType(SocialType.NAVER)
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return OAuthAttributes.builder()
                .socialType(SocialType.KAKAO)
                .email((String) account.get("email"))
                .name((String) profile.get("nickname"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .socialType(socialType)
                .email(email)
                .memberName(name)
                .address(new Address())
                .role(Role.USER)
                .grade(Grade.NORMAL)
                .memberStatus(MemberStatus.ACTIVE)
                .build();
    }
}
