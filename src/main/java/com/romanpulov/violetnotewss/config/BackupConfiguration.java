package com.romanpulov.violetnotewss.config;

import com.romanpulov.jutilscore.storage.BackupUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BackupConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BackupUtils backupUtils(String fileName, String backupPath, String backupFileName) {
        return new BackupUtils(fileName, backupPath, backupFileName);
    }
}
