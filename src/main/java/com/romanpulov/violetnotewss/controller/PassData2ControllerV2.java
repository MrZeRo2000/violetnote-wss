package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.mapper.PassData2DTOMapper;
import com.romanpulov.violetnotewss.model.PassData2PersistRequest;
import com.romanpulov.violetnotewss.model.PassDataFileInfo;
import com.romanpulov.violetnotewss.model.PassDataFileRequest;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import com.romanpulov.violetnotewss.services.PassData2ManagementService;
import com.romanpulov.violetnotewss.services.PassDataFileInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/passdata2")
public class PassData2ControllerV2 {

    private final PassData2ManagementService passData2ManagementService;
    private final PassData2DTOMapper passData2DTOMapper;

    public PassData2ControllerV2(PassData2ManagementService passData2ManagementService, PassData2DTOMapper passData2DTOMapper) {
        this.passData2ManagementService = passData2ManagementService;
        this.passData2DTOMapper = passData2DTOMapper;
    }

    @PostMapping("")
    ResponseEntity<PassData2> getPassData2(@RequestBody PassDataGetRequest getRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException {
        return ResponseEntity.ok(passData2ManagementService.readPassData(getRequest, getRequest.getFileName()));
    }

    @PostMapping("/edit")
    ResponseEntity<PassData2> editPassData(@RequestBody PassData2PersistRequest persistRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException, PassDataFileWriteException {
        // read previously saved data to make sure
        passData2ManagementService.readPassData(persistRequest, persistRequest.getFileName());
        // write the data
        passData2ManagementService.savePassData(persistRequest, persistRequest.getFileName(),
                passData2DTOMapper.dtoToCore(persistRequest.getPassData()));
        // read again and return
        return ResponseEntity.ok(passData2ManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }

    @PostMapping("/new")
    ResponseEntity<PassData2> newPassData(@RequestBody PassData2PersistRequest persistRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException, PassDataFileWriteException {
        // write the data
        passData2ManagementService.newPassData(persistRequest, persistRequest.getFileName(),
                passData2DTOMapper.dtoToCore(persistRequest.getPassData()));
        // read and return
        return ResponseEntity.ok(passData2ManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }

    @PostMapping("/fileinfo")
    ResponseEntity<PassDataFileInfo> getPassDataFileInfo(@RequestBody PassDataFileRequest fileRequest)
            throws PassDataFileWriteException{
        return ResponseEntity.ok(PassDataFileInfoService.getPassDataFileInfo(fileRequest.getFileName()));
    }
}
