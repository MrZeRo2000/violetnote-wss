package com.romanpulov.violetnotewss.services;

import com.romanpulov.jutilscore.io.FileUtils;
import com.romanpulov.violetnotecore.Processor.XMLPassDataWriter;
import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PasswordProvider;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PassDataManagementService {

    public PassData readPassData(PasswordProvider passwordProvider, String fileName)
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

        try (InputStream input = AESCryptService.generateCryptInputStream(new FileInputStream(file), passwordProvider.getPassword())) {
            return (new XMLPassDataReader()).readStream(input);
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new PassDataFileReadException("Data file read error: " + e.getMessage());
            } else
                throw new PassDataFileReadException("Data decryption error: " + e.getMessage());
        }
    }

    public void savePassData(PasswordProvider passwordProvider, String fileName, PassData passData)
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

    public void newPassData(PasswordProvider passwordProvider, String fileName, PassData passData)
            throws PassDataFileWriteException {
        File f = new File(fileName);

        // the new file should not exist
        if (f.exists()) {
            throw new PassDataFileWriteException("Error writing new file: the file already exists: " + f.getPath());
        }

        // saving file
        savePassDataInternal(passwordProvider, f, passData);
    }

    private void savePassDataInternal(PasswordProvider passwordProvider, File f, PassData passData)
            throws PassDataFileWriteException {
        try (OutputStream output = AESCryptService.generateCryptOutputStream(new FileOutputStream(f), passwordProvider.getPassword())) {

            (new XMLPassDataWriter(passData)).writeStream(output);
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            throw new PassDataFileWriteException("Data file write error:" + e.getMessage());
        }
    }
}
