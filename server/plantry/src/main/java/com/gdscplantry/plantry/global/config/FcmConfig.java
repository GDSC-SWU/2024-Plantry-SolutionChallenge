package com.gdscplantry.plantry.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@Slf4j
public class FcmConfig {
    @Value("${PROFILE}")
    private String profile;

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        String JSON_PATH = "/firebase/firebase_account_file.json";
        InputStream refreshToken;
        if (profile.contains("local")) {
            // For local
            ClassPathResource resource = new ClassPathResource(JSON_PATH);
            refreshToken = resource.getInputStream();
        } else {
            // For prod
            Path filePath = Paths.get(JSON_PATH);
            refreshToken = Files.newInputStream(filePath);
        }

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
            for (FirebaseApp app : firebaseAppList) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                    firebaseApp = app;
            }
        } else {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        }

        assert firebaseApp != null;
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
