package com.example.library_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

@RestController
public class EmailJsController {

    @PostMapping("/api/emailjs")
    public ResponseEntity<?> saveEmailJs(@RequestBody Map<String, String> body) {
        try {
            String user = body.getOrDefault("user", "");
            String service = body.getOrDefault("service", "");
            String template = body.getOrDefault("template", "");

            File dir = new File("data"); if(!dir.exists()) dir.mkdirs();
            File f = new File(dir, "emailjs.properties");
            Properties p = new Properties();
            p.setProperty("user", user);
            p.setProperty("service", service);
            p.setProperty("template", template);
            try (FileOutputStream out = new FileOutputStream(f)) { p.store(out, "EmailJS config"); }
            return ResponseEntity.ok(Map.of("status","ok"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
