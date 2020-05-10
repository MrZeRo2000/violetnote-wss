package com.romanpulov.violetnotewss.exception;

public class PassDataFileNotFoundException extends Exception {

    private static String composeMessage(String fileName) {
        return fileName == null ? "File not found" : "File not found: " + fileName;
    }

    public PassDataFileNotFoundException(String fileName) {
        super(PassDataFileNotFoundException.composeMessage(fileName));
    }

    public PassDataFileNotFoundException(String fileName, Throwable cause) {
        super(PassDataFileNotFoundException.composeMessage(fileName), cause);
    }

    public PassDataFileNotFoundException() {
        this(null);
    }
}
