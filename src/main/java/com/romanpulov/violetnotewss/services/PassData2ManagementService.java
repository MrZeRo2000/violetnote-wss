package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Service.PassData2ReaderServiceV2;
import com.romanpulov.violetnotecore.Service.PassData2WriterServiceV2;
import com.romanpulov.violetnotewss.model.PasswordProvider;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PassData2ManagementService extends AbstractPassDataManagementService<PassData2> {

    @Override
    protected PassData2 readPassDataFromStream(PasswordProvider passwordProvider, InputStream inputStream) throws AESCryptException, DataReadWriteException, IOException {
        return PassData2ReaderServiceV2.fromStream(inputStream, passwordProvider.getPassword());
    }

    @Override
    protected void writePassDataToStream(PasswordProvider passwordProvider, OutputStream outputStream, PassData2 data) throws AESCryptException, DataReadWriteException, IOException {
        PassData2WriterServiceV2.toStream(outputStream, passwordProvider.getPassword(), data);
    }
}
