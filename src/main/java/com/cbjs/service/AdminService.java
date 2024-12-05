package com.cbjs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cbjs.entity.PasswordConfig;
import com.cbjs.entity.PasswordGenerator;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminService {
    private String serializeToBase64(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    private static Object deserializeFromBase64(String s) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(s)))) {
            return ois.readObject();
        }
    }

    public ResponseEntity<Map<String, String>> generateConfig(HttpServletRequest request) {
        try {
            PasswordConfig config = new PasswordConfig();
            config.setLength(
                    request.getParameter("length") != null ? String.valueOf(request.getParameter("length")) : "12");
            config.setIncludeUppercase(
                    request.getParameter("uppercase") != null ? Boolean.parseBoolean(request.getParameter("uppercase"))
                            : true);
            config.setIncludeLowercase(
                    request.getParameter("lowercase") != null ? Boolean.parseBoolean(request.getParameter("lowercase"))
                            : true);
            config.setIncludeNumbers(
                    request.getParameter("numbers") != null ? Boolean.parseBoolean(request.getParameter("numbers"))
                            : true);
            config.setIncludeSpecialChars(request.getParameter("specialChars") != null
                    ? Boolean.parseBoolean(request.getParameter("specialChars"))
                    : true);

            return ResponseEntity.ok(Map.of("config", serializeToBase64(config)));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> generatePassword(HttpServletRequest request) {
        try {
            String configParam = request.getParameter("config");
            if (configParam == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing config parameter"));
            }

            PasswordConfig config = (PasswordConfig) deserializeFromBase64(configParam);
            PasswordGenerator generator = new PasswordGenerator(config);

            return ResponseEntity.ok(Map.of("password", generator.generate()));
        } catch (IOException | ClassNotFoundException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> getAdminSecret(HttpServletRequest request) {
        String secret = "FLAG3_HOLDER";
        return ResponseEntity.ok()
                .body(Map.of("secret", secret));
    }

}
