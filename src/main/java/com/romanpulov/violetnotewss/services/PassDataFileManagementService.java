package com.romanpulov.violetnotewss.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
public class PassDataFileManagementService {
    public final static String PASS_DATA_FILE_NAME_PARAM_NAME = "pass-data-file-name";

    private final ServletContext context;

    public PassDataFileManagementService(@Autowired ServletContext context) {
        this.context = context;
    }

    public String getPassDataFileName() {
        return context.getInitParameter(PassDataFileManagementService.PASS_DATA_FILE_NAME_PARAM_NAME);
    }
}
