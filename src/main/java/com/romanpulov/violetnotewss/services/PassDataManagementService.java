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

    public boolean savePassData(PasswordProvider passwordProvider, String fileName, PassData passData) {
        boolean result;

        File f = new File(fileName);

        // save as temp file first
        File tempFile = new File(FileUtils.getTempFileName(f.getPath()));
        result = savePassDataInternal(passwordProvider, tempFile, passData);
        if (!result)
            return false;

        //roll backup files
        result = FileUtils.saveCopies(f.getPath());
        if (!result)
            return false;

        //rename temp file
        result = FileUtils.renameTempFile(tempFile.getPath());
        return result;
    }

    private boolean savePassDataInternal(PasswordProvider passwordProvider, File f, PassData passData) {
        try (OutputStream output = AESCryptService.generateCryptOutputStream(new FileOutputStream(f), passwordProvider.getPassword())) {

            (new XMLPassDataWriter(passData)).writeStream(output);

            return true;
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
