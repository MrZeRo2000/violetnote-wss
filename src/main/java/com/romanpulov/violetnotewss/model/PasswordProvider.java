package com.romanpulov.violetnotewss.model;

public interface PasswordProvider {
    String getPassword();
    boolean isPasswordEmpty();
}
