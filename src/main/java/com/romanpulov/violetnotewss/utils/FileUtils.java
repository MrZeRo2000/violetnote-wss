package com.romanpulov.violetnotewss.utils;

import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static boolean fileExists(String fileName) {
        return Files.exists(Paths.get(fileName));
    }

    public static boolean fileValid(String fileName) throws PassDataFileWriteException {
        if (FileUtils.fileExists(fileName)) {
            return true;
        } else {
            Path filePath = Paths.get(fileName);
            try {
                Path newFilePath = Files.createFile(filePath);
                Files.delete(newFilePath);

                return true;
            } catch (IOException e) {
                throw new PassDataFileWriteException(String.format("Error creating file: %s", e.getMessage()));
            }
        }
    }
}
