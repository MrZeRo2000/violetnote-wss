package com.romanpulov.violetnotewss.config;

import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotewss.model.PassNoteMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration {

    @Bean
    JsonMapper jsonMapper(JsonMapper.Builder builder) {
        return builder.addMixIn(PassNote.class, PassNoteMixin.class).build();
    }
}
