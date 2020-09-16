package com.romanpulov.violetnotewss.services;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PasswordProvider;

import java.io.File;

public abstract class AbstractPassDataManagementService<D> {

    protected abstract D readPassDataInternal(PasswordProvider passwordProvider, File f)
            throws PassDataFileReadException;

    protected abstract void savePassDataInternal(PasswordProvider passwordProvider, File f, D passData)
            throws PassDataFileWriteException;


    public D readPassData(PasswordProvider passwordProvider, String fileName)
            throws PassDataFileNotFoundException, PassDataFileReadException {

        if (fileName == null) {
            throw new PassDataFileNotFoundException();
        }

        File file = new File(fileName);
        if (!file.exists()) {
            throw new PassDataFileNotFoundException(fileName);
        }

        if ((passwordProvider == null) || (passwordProvider.isPasswordEmpty())) {
            throw new PassDataFileReadException("No password");
        }

        return readPassDataInternal(passwordProvider, file);
    }

    public void savePassData(PasswordProvider passwordProvider, String fileName, D passData)
            throws PassDataFileWriteException
    {
        File f = new File(fileName);

        // save as temp file first
        File tempFile = new File(FileUtils.getTempFileName(f.getPath()));

        // saving data to a temporary file
        savePassDataInternal(passwordProvider, tempFile, passData);

        //roll backup files
        if (!FileUtils.saveCopies(f.getPath()))
            throw new PassDataFileWriteException("Error saving copies of the file: " + f.getPath());

        //rename temp file
        if (!FileUtils.renameTempFile(tempFile.getPath())) {
            throw new PassDataFileWriteException("Error renaming temp file: " + tempFile.getPath());
        }
    }

    public void newPassData(PasswordProvider passwordProvider, String fileName, D passData)
            throws PassDataFileWriteException {
        File f = new File(fileName);

        // the new file should not exist
        if (f.exists()) {
            throw new PassDataFileWriteException("Error writing new file: the file already exists: " + f.getPath());
        }

        // saving file
        savePassDataInternal(passwordProvider, f, passData);
    }
}
