package com.romanpulov.violetnotewss.services;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PasswordProvider;

import java.io.*;

public abstract class AbstractPassDataManagementService<D> {

    protected abstract D readPassDataFromStream(PasswordProvider passwordProvider, InputStream inputStream)
            throws AESCryptException, DataReadWriteException, IOException;

    protected abstract void writePassDataToStream(PasswordProvider passwordProvider, OutputStream outputStream, D data)
            throws AESCryptException, DataReadWriteException, IOException;

    protected D readPassDataFromFile(PasswordProvider passwordProvider, File f)
            throws PassDataFileReadException {
        try (InputStream inputStream = new FileInputStream(f)) {
            return readPassDataFromStream(passwordProvider, inputStream);
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new PassDataFileReadException("Data file read error: " + e.getMessage());
            } else
                throw new PassDataFileReadException("Data decryption error: " + e.getMessage());
        }
    };

    protected void savePassDataToFile(PasswordProvider passwordProvider, File f, D passData)
            throws PassDataFileWriteException {
        try (OutputStream outputStream = new FileOutputStream(f)) {
            writePassDataToStream(passwordProvider, outputStream, passData);
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            throw new PassDataFileWriteException("Data file write error:" + e.getMessage());
        }
    }

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

        return readPassDataFromFile(passwordProvider, file);
    }

    public void savePassData(PasswordProvider passwordProvider, String fileName, D passData)
            throws PassDataFileWriteException
    {
        File f = new File(fileName);

        // save as temp file first
        File tempFile = new File(FileUtils.getTempFileName(f.getPath()));

        // saving data to a temporary file
        savePassDataToFile(passwordProvider, tempFile, passData);

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
        savePassDataToFile(passwordProvider, f, passData);
    }
}
