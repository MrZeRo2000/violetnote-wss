package com.romanpulov.violetnotewss.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotewss.model.PassNoteMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(PassNote.class, PassNoteMixin.class);
        return mapper;
    }
}
