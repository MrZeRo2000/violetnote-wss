package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotewss.config.GrProperties;
import com.romanpulov.violetnotewss.model.AppInfoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AppInfoController {
    private final GrProperties grProperties;

    public AppInfoController(GrProperties grProperties) {
        this.grProperties = grProperties;
    }

    @GetMapping(value="/app-info", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppInfoDTO> getAppInfo() {
        return ResponseEntity.ok(new AppInfoDTO(grProperties.getVersion()));
    }
}
