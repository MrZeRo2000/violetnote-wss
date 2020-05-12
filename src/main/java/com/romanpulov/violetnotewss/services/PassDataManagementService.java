package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotewss.exception.PassDataFileNotFoundException;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;
import com.romanpulov.violetnotewss.model.PasswordProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
