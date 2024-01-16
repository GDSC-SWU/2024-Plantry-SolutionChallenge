package com.gdscplantry.plantry.global.util;

import com.gdscplantry.plantry.domain.User.domain.User;
import com.gdscplantry.plantry.domain.User.domain.UserRepository;
import com.gdscplantry.plantry.domain.User.error.UserErrorCode;
import com.gdscplantry.plantry.global.error.exception.AppException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthUtil {
    private final UserRepository userRepository;

    @Value("${GOOGLE_OAUTH_CLIENT_ID}")
    private String CLIENT_ID;

    public User authenticate(String idToken) throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken == null)
            throw new AppException(UserErrorCode.INVALID_ID_TOKEN);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        // Get profile information from payload
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");
        
        String nickname;
        Boolean isNotUnique;
        do {
            nickname = RandomUtil.getRandomNickname();
            isNotUnique = userRepository.existsByNickname(nickname);
        } while (isNotUnique);

        return User.builder()
                .email(email)
                .nickname(nickname)
                .profileImagePath(pictureUrl)
                .build();
    }
}
