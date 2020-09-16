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
    protected PassData readPassDataFromStream(PasswordProvider passwordProvider, InputStream inputStream)
            throws AESCryptException, DataReadWriteException, IOException {
        FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, passwordProvider.getPassword());
        return readerV1.readFile();
    }

    @Override
    protected void writePassDataToStream(PasswordProvider passwordProvider, OutputStream outputStream, PassData data)
            throws AESCryptException, DataReadWriteException, IOException {
        FilePassDataWriterV1 writerV1 = new FilePassDataWriterV1(outputStream, passwordProvider.getPassword(), data);
        writerV1.writeFile();
    }
}
