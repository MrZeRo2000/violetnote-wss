package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PassDataGetRequest;
import com.romanpulov.violetnotewss.model.PassDataPersistRequest;
import com.romanpulov.violetnotewss.services.PassDataManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/passdata")
public class PassDataControllerV2 {

    private final PassDataManagementService passDataManagementService;

    public PassDataControllerV2(PassDataManagementService passDataManagementService) {
        this.passDataManagementService = passDataManagementService;
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
        passDataManagementService.savePassData(persistRequest, persistRequest.getFileName(), persistRequest.getPassData());
        // read again and return
        return ResponseEntity.ok(passDataManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }

    @PostMapping("/new")
    ResponseEntity<PassData> newPassData(@RequestBody PassDataPersistRequest persistRequest)
            throws PassDataFileNotFoundException, PassDataFileReadException, PassDataFileWriteException
    {
        // write the data
        passDataManagementService.newPassData(persistRequest, persistRequest.getFileName(), persistRequest.getPassData());
        // read and return
        return ResponseEntity.ok(passDataManagementService.readPassData(persistRequest, persistRequest.getFileName()));
    }
}
