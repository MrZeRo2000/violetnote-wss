package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PassDataFileInfo;
import com.romanpulov.violetnotewss.utils.FileUtils;

public class PassDataFileInfoService {

    public static PassDataFileInfo getPassDataFileInfo(String fileName)
            throws PassDataFileWriteException {
        boolean fileExists = FileUtils.fileExists(fileName);
        boolean fileValid = fileExists || FileUtils.fileValid(fileName);

        return new PassDataFileInfo(fileName, fileExists, fileValid);
    }
}
