package com.marketcollection.common.auth.dto;

import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.member.MemberStatus;
import com.marketcollection.domain.member.Role;
import com.marketcollection.domain.member.SocialType;
import com.marketcollection.domain.member.Member;
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
        return ofGoogle(userNameAttributeName, attributes);
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

    public Member toEntity() {
        return Member.builder()
                .socialType(SocialType.GOOGLE)
                .email(email)
                .memberName(name)
                .address(new Address())
                .role(Role.USER)
                .memberStatus(MemberStatus.ACTIVE)
                .build();
    }
}
