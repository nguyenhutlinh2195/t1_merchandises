package com.cbjs.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class PasswordConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String length;
    private boolean includeUppercase;
    private boolean includeLowercase;
    private boolean includeNumbers;
    private boolean includeSpecialChars;

    public String buildCharset() {
        StringBuilder charset = new StringBuilder();
        if (includeUppercase) charset.append("A-Z");
        if (includeLowercase) charset.append("a-z");
        if (includeNumbers) charset.append("0-9");
        if (includeSpecialChars) charset.append("!@#\\$%\\^&\\*\\(\\)");
        return charset.toString();
    }
}