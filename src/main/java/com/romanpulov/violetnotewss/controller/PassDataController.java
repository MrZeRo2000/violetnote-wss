package com.romanpulov.violetnotewss.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotewss.model.*;
import com.romanpulov.violetnotewss.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/passdata")
public class PassDataController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    interface PassNoteMixin {
        @JsonIgnore()
        Map<String, String> getNoteAttr();
    }

    @Configuration
    public static class JacksonConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.addMixIn(PassNote.class, PassNoteMixin.class);
            return mapper;
        }
    }

    private final PassDataFileManagementService passDataFileManagementService;
    private final PassDataManagementService passDataManagementService;
    private final DropboxService dropboxService;

    public PassDataController(
            PassDataFileManagementService passDataFileManagementService,
            PassDataManagementService passDataManagementService,
            DropboxService dropboxService
            ) {
        this.passDataFileManagementService = passDataFileManagementService;
        this.passDataManagementService = passDataManagementService;
        this.dropboxService = dropboxService;
    }

    @RequestMapping("/filename")
    public String getFileName() {
        String fileName = passDataFileManagementService.getPassDataFileName();
        File f = new File(fileName);
        return "FileName:" + fileName + ", file exists:" + f.exists();
    }

    @RequestMapping(
            path = "/fileinfo",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public PassDataFileInfo getDataFileInfo() {
        String fileName = passDataFileManagementService.getPassDataFileName();
        File f = new File(fileName);
        return new PassDataFileInfo(fileName, f.exists());
    }

    @RequestMapping(
            path = "/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public String checkPasswordPost(@RequestBody PassDataInfo passDataInfo) {
        return passDataInfo.password;
    }

    @RequestMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public PassData readDataPost(@RequestBody PassDataInfo passDataInfo)
        throws PassDataFileNotFoundException, PassDataFileReadException
    {
        return passDataManagementService.readPassData(passDataInfo, passDataFileManagementService.getPassDataFileName());
    }

    @RequestMapping(
            path = "/dropbox",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public PassData readDataDropboxPost(@RequestBody PassDataAuthInfo passDataAuthInfo)
            throws PassDataFileNotFoundException, PassDataFileReadException, IOException
    {
        String dropboxFileName = passDataFileManagementService.getDropboxFileName();
        String downloadedFileName = passDataFileManagementService.getDownloadedFileName();

        logger.debug(String.format("Downloading from dropbox: %s to %s", dropboxFileName, downloadedFileName));

        dropboxService.downloadFile(
                passDataAuthInfo.authKey,
                dropboxFileName,
                downloadedFileName
        );

        logger.debug(String.format("Reading from %s", downloadedFileName));

        return passDataManagementService.readPassData(passDataAuthInfo, downloadedFileName);
    }

    @RequestMapping(
            path = "/dropbox/download",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public OkResponse readDataDropboxDownloadPost(@RequestBody PassDataAuthInfo passDataAuthInfo)
            throws IOException
    {
        String dropboxFileName = passDataFileManagementService.getDropboxFileName();
        String downloadedFileName = passDataFileManagementService.getDownloadedFileName();

        logger.debug(String.format("Downloading from dropbox: %s to %s", dropboxFileName, downloadedFileName));

        dropboxService.downloadFile(
                passDataAuthInfo.authKey,
                dropboxFileName,
                downloadedFileName
        );

        return new OkResponse("Ok");
    }

    @RequestMapping(
            path = "/dropbox/read",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public PassData readDataDropboxReadPost(@RequestBody PassDataAuthInfo passDataAuthInfo)
            throws PassDataFileNotFoundException, PassDataFileReadException, IOException
    {
        String downloadedFileName = passDataFileManagementService.getDownloadedFileName();

        if (!Files.exists(Paths.get(downloadedFileName))) {
            throw new IOException(String.format("File %s not found", downloadedFileName));
        }

        logger.debug(String.format("Reading from: %s", downloadedFileName));

        return passDataManagementService.readPassData(passDataAuthInfo, downloadedFileName);
    }

    @RequestMapping(
            path = "/dropbox/checkedread",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    public PassData readDataDropboxCheckedReadPost(@RequestBody PassDataAuthInfo passDataAuthInfo)
            throws PassDataFileNotFoundException, PassDataFileReadException, IOException
    {
        String downloadedFileName = passDataFileManagementService.getDownloadedFileName();

        if (!Files.exists(Paths.get(downloadedFileName))) {
            logger.debug(String.format("File %s not found, reading", downloadedFileName));

            String dropboxFileName = passDataFileManagementService.getDropboxFileName();

            dropboxService.downloadFile(
                    passDataAuthInfo.authKey,
                    dropboxFileName,
                    downloadedFileName
            );
        }

        logger.debug(String.format("Reading from: %s", downloadedFileName));

        return passDataManagementService.readPassData(passDataAuthInfo, downloadedFileName);
    }

    @ExceptionHandler({PassDataFileNotFoundException.class, PassDataFileReadException.class})
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.OK);
    }
}
