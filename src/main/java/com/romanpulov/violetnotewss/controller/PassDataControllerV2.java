package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.mapper.PassDataDTOMapper;
import com.romanpulov.violetnotewss.mapper.PassNoteDTOMapper;
import com.romanpulov.violetnotewss.model.PassDataFileInfo;
import com.romanpulov.violetnotewss.model.PassDataFileRequest;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import com.romanpulov.violetnotewss.model.PassDataPersistRequest;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import com.romanpulov.violetnotewss.utils.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v2/passdata")
public class PassDataControllerV2 {

    private final PassDataManagementService passDataManagementService;
    private final PassDataDTOMapper passDataDTOMapper;

    public PassDataControllerV2(PassDataManagementService passDataManagementService, PassDataDTOMapper passDataDTOMapper) {
        this.passDataManagementService = passDataManagementService;
        this.passDataDTOMapper = passDataDTOMapper;
    }

    @PostMapping("")
    ResponseEntity<PassData> getPassData(@RequestBody PassDataGetRequest getRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException
    {
        return ResponseEntity.ok(passDataManagementService.readPassData(getRequest, getRequest.getFileName()));
    }

    @PostMapping("/edit")
    ResponseEntity<PassData> editPassData(@RequestBody PassDataPersistRequest persistRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException, PassDataFileWriteException
    {
        // read previously saved data to make sure
        passDataManagementService.readPassData(persistRequest, persistRequest.getFileName());
        // write the data
        passDataManagementService.savePassData(persistRequest, persistRequest.getFileName(),
                passDataDTOMapper.dtoToCore(persistRequest.getPassData()));
        // read again and return
        return ResponseEntity.ok(passDataManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }

    @PostMapping("/new")
    ResponseEntity<PassData> newPassData(@RequestBody PassDataPersistRequest persistRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException, PassDataFileWriteException
    {
        // write the data
        passDataManagementService.newPassData(persistRequest, persistRequest.getFileName(),
                passDataDTOMapper.dtoToCore(persistRequest.getPassData()));
        // read and return
        return ResponseEntity.ok(passDataManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }

    @PostMapping("/fileinfo")
    ResponseEntity<PassDataFileInfo> getPassDataFileInfo(@RequestBody PassDataFileRequest fileRequest)
            throws PassDataFileWriteException{
        String fileName = fileRequest.getFileName();
        boolean fileExists = FileUtils.fileExists(fileName);
        boolean fileValid = fileExists || FileUtils.fileValid(fileName);

        return ResponseEntity.ok(new PassDataFileInfo(fileName, fileExists, fileValid));
    }
}
