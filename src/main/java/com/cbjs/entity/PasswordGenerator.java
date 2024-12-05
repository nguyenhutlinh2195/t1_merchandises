package com.cbjs.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class PasswordGenerator implements Serializable {
    private static final long serialVersionUID = 1L;

    private String length;
    private String charset;

    public PasswordGenerator(PasswordConfig config) {
        this.length = config.getLength();
        this.charset = config.buildCharset();
    }

    public String generate() {
        try {
            String[] cmd = { "/bin/sh", "-c",
                    String.format("head /dev/urandom | tr -dc '%s' | head -c %s", this.charset, this.length) };
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            Future<String> future = Executors.newSingleThreadExecutor().submit(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                return reader.readLine();
            });

            try {
                return future.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                process.destroy();
                throw new RuntimeException("Generating password timedout");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating password");
        }
    }
}