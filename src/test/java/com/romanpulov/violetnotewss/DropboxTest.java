package com.romanpulov.violetnotewss;

import com.romanpulov.violetnotewss.services.DropboxService;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DropboxTest {

    @Test
    public void downloadFileTest() throws Exception {
        final String downloadedFileName = System.getProperty("java.io.tmpdir") + "test1_downloaded_test.vnf";
        final String testFileName = "data/test1.vnf";

        final Path downloadedFilePath = new File(downloadedFileName).toPath();

        try {
            Files.delete(downloadedFilePath);
        } catch (IOException e) {
            //ignore if file does not exist
        }
        assertThat(Files.exists(downloadedFilePath)).isFalse();

        DropboxService dropboxService = new DropboxService();
        dropboxService.downloadFile("IC-VZ7nhRZAAAAAAAAAAZTIxapCGU5yAnXt0o4PF1vjycHkNTXfOmrqQktgQil5Y", "/Temp/test1.vnf", downloadedFileName);

        assertThat(Files.exists(downloadedFilePath)).isTrue();

        byte[] downloadedFileContent = Files.readAllBytes(downloadedFilePath);
        byte[] testFileContent = Files.readAllBytes(new File(testFileName).toPath());
        assertThat(Arrays.equals(downloadedFileContent, testFileContent)).isTrue();
    }
}
