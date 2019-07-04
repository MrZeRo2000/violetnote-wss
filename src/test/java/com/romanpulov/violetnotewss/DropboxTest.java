package com.romanpulov.violetnotewss;

import com.romanpulov.violetnotewss.services.DropboxService;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class DropboxTest {

    @Test
    public void downloadFileTest() throws Exception {
        final String testFileName = "data/1_downloaded.vnf";
        final Path testFilePath = new File(testFileName).toPath();

        Files.delete(testFilePath);
        assertThat(Files.exists(testFilePath)).isFalse();

        DropboxService dropboxService = new DropboxService();
        dropboxService.downloadFile("IC-VZ7nhRZAAAAAAAAAAZTIxapCGU5yAnXt0o4PF1vjycHkNTXfOmrqQktgQil5Y", "/Temp/1.vnf", testFileName);

        assertThat(Files.exists(testFilePath)).isTrue();
    }
}
