package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV1;
import com.romanpulov.violetnotewss.exception.PassDataFileReadException;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotewss.exception.PassDataFileWriteException;
import com.romanpulov.violetnotewss.model.PasswordProvider;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PassDataManagementService extends AbstractPassDataManagementService<PassData> {

    @Override
    protected PassData readPassDataInternal(PasswordProvider passwordProvider, File f)
            throws PassDataFileReadException {
        try (InputStream inputStream = new FileInputStream(f)) {
            FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, passwordProvider.getPassword());
            return readerV1.readFile();
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                throw new PassDataFileReadException("Data file read error: " + e.getMessage());
            } else
                throw new PassDataFileReadException("Data decryption error: " + e.getMessage());
        }
    }

    @Override
    protected void savePassDataInternal(PasswordProvider passwordProvider, File f, PassData passData)
            throws PassDataFileWriteException {
        try (OutputStream outputStream = new FileOutputStream(f)) {
            FilePassDataWriterV1 writerV1 = new FilePassDataWriterV1(outputStream, passwordProvider.getPassword(), passData);
            writerV1.writeFile();
        } catch (AESCryptException | IOException | DataReadWriteException e) {
            e.printStackTrace();
            throw new PassDataFileWriteException("Data file write error:" + e.getMessage());
        }
    }
}
