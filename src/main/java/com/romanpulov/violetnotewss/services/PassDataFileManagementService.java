package com.romanpulov.violetnotewss.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PassDataFileManagementService {
    public final static String PASS_DATA_FILE_NAME_PARAM_NAME = "pass-data-file-name";
    public final static String PASS_DATA_DROPBOX_FILE_NAME_PARAM_NAME = "pass-data-dropbox_file-name";
    public final static String PASS_DATA_DOWNLOADED_FILE_NAME_PARAM_NAME = "pass-data-downloaded_file-name";

    private final ServletContext context;

    public PassDataFileManagementService(@Autowired ServletContext context) {
        this.context = context;
    }

    public String getPassDataFileName() {
        return context.getInitParameter(PASS_DATA_FILE_NAME_PARAM_NAME);
    }

    public String getDropboxFileName() {
        return context.getInitParameter(PASS_DATA_DROPBOX_FILE_NAME_PARAM_NAME);
    }

    public String getDownloadedFileName() {
        String fileName = context.getInitParameter(PASS_DATA_DOWNLOADED_FILE_NAME_PARAM_NAME);
        if (fileName == null) {
            throw new RuntimeException(String.format("Parameter %s not found", PASS_DATA_DOWNLOADED_FILE_NAME_PARAM_NAME));
        }

        String defaultFilePath = System.getProperty("java.io.tmpdir");

        String filePath = System.getProperty("catalina.base");
        if (filePath != null) {
            filePath = filePath + "/temp";
            if (!Files.exists(Paths.get(filePath))) {
                filePath = defaultFilePath;
            }
        } else {
            filePath = defaultFilePath;
        }

        return Paths.get(filePath, fileName).toString();
    }
}
