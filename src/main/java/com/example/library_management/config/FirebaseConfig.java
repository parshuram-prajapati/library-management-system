package com.example.library_management.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            String base64 = System.getenv("FIREBASE_CREDENTIALS_BASE64");

            if (base64 == null || base64.isEmpty()) {
                throw new IllegalStateException("Firebase credentials not found");
            }

            byte[] decoded = Base64.getDecoder().decode(base64);

            GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decoded));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("🔥 Firebase initialized successfully (Base64)");

        } catch (Exception e) {
            throw new RuntimeException("Firebase init failed", e);
        }
    }
}
