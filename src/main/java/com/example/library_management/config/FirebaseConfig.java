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
            String base64Creds = System.getenv("FIREBASE_CREDENTIALS_BASE64");

            if (base64Creds == null || base64Creds.isEmpty()) {
                System.out.println("⚠️ Firebase Base64 credentials not found, skipping Firebase init");
                return;
            }

            byte[] decodedBytes = Base64.getDecoder().decode(base64Creds);
            ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("🔥 Firebase initialized successfully (Base64)");

        } catch (Exception e) {
            System.out.println("❌ Firebase initialization failed: " + e.getMessage());
        }
    }
}
