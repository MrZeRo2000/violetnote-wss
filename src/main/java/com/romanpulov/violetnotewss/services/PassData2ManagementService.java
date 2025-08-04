package com.romanpulov.violetnotewss.services;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Service.PassData2ReaderServiceV2;
import com.romanpulov.violetnotecore.Service.PassData2WriterServiceV2;
import com.romanpulov.violetnotewss.model.PasswordProvider;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

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

    @Override
    protected PassData2 createNewPassData() {
        PassNote2 passNote2 = new PassNote2();
        passNote2.setSystem("New System");
        passNote2.setUser("New User");
        passNote2.setPassword("New Password");

        PassCategory2 passCategory2 = new PassCategory2();
        passCategory2.setCategoryName("New Category");
        passCategory2.setNoteList(List.of(passNote2));

        PassData2 passData = new PassData2();
        passData.setCategoryList(List.of(passCategory2));

        return passData;
    }
}
