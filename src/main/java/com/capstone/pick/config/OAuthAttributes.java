package com.capstone.pick.config;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes; // ex) { sub=1231344534523565757, name=홍길동, given_name=길동, family_name=홍, picture=https://xxx, email=xxx@gmail.com, email_verified=true, locale=ko}
    private String nameAttributeKey; // ex) google="sub", kakao="id", naver="response"
    private String name;
    private String email;
    private String username;
    private String age_range;
    private String birthday;
    private String gender;
    private String provider;


    // 해당 로그인인 서비스가 kakao 인지 google 인지 구분하여, 알맞게 매핑을 해주도록 합니다.
    // registrationId는 소셜로그인을 시도한 서비스 명("google","kakao","naver"..)이 되고,
    // userNameAttributeName은 해당 서비스의 map의 키 값이 되는 값이 됩니다. { google="sub", kakao="id", naver="response" }
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        }
        else if (registrationId.equals("naver")) {
            return ofNaver(userNameAttributeName,attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);

    }
//    카카오 예시
//    attributes = {
//        id=354353456,
//        connected_at=2023-03-12T18:03:55Z,
//        properties={
//               nickname=홍길동,
//               profile_image=http://k.kakaocdn.net/dn/nacb8/btrXJgjUuj3/lRytO3vswu30fIxJsaK4pK/img_640x640.jpg,
//               thumbnail_image=http://k.kakaocdn.net/dn/nacb8/btrXJgjUuj3/lRytO3vswu30fIxJsaK4pK/img_110x110.jpg
//        },
//        kakao_account={
//                profile_nickname_needs_agreement=false,
//                profile_image_needs_agreement=false,
//                profile={
//                     nickname=홍길동,
//                     thumbnail_image_url=http://k.kakaocdn.net/dn/nacb8/btrXJgjUuj3/lRytO3vswu30fIxJsaK4pK/img_110x110.jpg,
//                     profile_image_url=http://k.kakaocdn.net/dn/nacb8/btrXJgjUuj3/lRytO3vswu30fIxJsaK4pK/img_640x640.jpg,
//                     is_default_image=false
//                },
//                has_email=true,
//                email_needs_agreement=false,
//                is_email_valid=true,
//                is_email_verified=true,
//                email=test@nate.com,
//                has_age_range=true,
//                age_range_needs_agreement=false,
//                age_range=20~29,
//                has_birthday=true,
//                birthday_needs_agreement=false,
//                birthday=0101,
//                birthday_type=SOLAR,
//                has_gender=true,
//                gender_needs_agreement=false,
//                gender=male
//        }
//    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");   // 마찬가지로 profile(nickname 등) 정보가 담긴 값을 꺼낸다.

        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .name((String)profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .username(attributes.get(userNameAttributeName).toString())
                .age_range((String)kakao_account.get("age_range"))
                .birthday(kakao_account.get("birthday").toString())
                .gender((String)kakao_account.get("gender"))
                .provider("kakao")
                .build();
    }

//    네이버 예시
//    response = {
//         id=2pWHrMsdf78996Y36za-qdY-Isddskdsf34dfvc,
//         nickname=오옹,
//         profile_image=https://ssl.pstatic.net/static/pwe/address/img_profile.png,
//         age=20-29,
//         gender=M,
//         email=test@naver.com,
//         mobile=010-1234-5678,
//         mobile_e164=+821012345678,
//         name=홍길동,
//         birthday=01-10,
//         birthyear=2000
//        }
//    }
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");    // 네이버에서 받은 데이터에서 프로필 정보가 담긴 response 값을 꺼낸다.
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .username((String) response.get("id"))
                .age_range((String) response.get("age"))
                .birthday((String) response.get("birthday"))
                .gender((String) response.get("gender"))
                .provider("naver")
                .build();
    }

//  구글 예시
//  attributes = {
//     sub=1130477235475676341,
//     name=홍길동,
//     given_name=길동,
//     family_name=홍,
//     picture=https://lh3.googleusercontent.com/a/AGNmyxZNq_Qc9vxL0BCrzoe4AZqlQ0wixlyY9IudBxRK=s96-c,
//     email=test@gmail.com,
//     email_verified=true,
//     locale=ko
//     }
//   연령대, 성별, 생일 정보 받아올 수 없음.
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .username((String) attributes.get(userNameAttributeName))
                .provider("google")
                .build();
    }

}
