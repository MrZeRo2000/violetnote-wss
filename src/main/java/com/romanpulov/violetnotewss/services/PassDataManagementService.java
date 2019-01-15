package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotewss.model.PassDataInfo;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;

@Service
public class PassDataManagementService {
    public final static String PASS_DATA_FILE_NAME_PARAM_NAME = "pass-data-file-name";

    private ServletContext context;

    public PassDataManagementService(@Autowired ServletContext context) {
        this.context = context;
    }

    public String getPassDataFileName() {
        return context.getInitParameter(PassDataManagementService.PASS_DATA_FILE_NAME_PARAM_NAME);
    }
    public PassData readPassData(PassDataInfo passDataInfo) throws PassDataFileNotFoundException, PassDataFileReadException {
        return readPassData(passDataInfo, getPassDataFileName());
    }

    public PassData readPassData(PassDataInfo passDataInfo, String fileName) throws PassDataFileNotFoundException, PassDataFileReadException {

        if (fileName == null) {
            throw new PassDataFileNotFoundException();
        }

        File file = new File(fileName);
        if (!file.exists()) {
            throw new PassDataFileNotFoundException(fileName);
        }



        return null;
    }
}
