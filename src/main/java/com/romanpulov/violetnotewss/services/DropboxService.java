package com.romanpulov.violetnotewss.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

@Service
public class DropboxService {

    public void downloadFile(String authKey, String remoteFileName, String localFileName) throws IOException {
        RestTemplate restTemplate = new RestTemplate(Collections.singletonList(new ByteArrayHttpMessageConverter()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", authKey));
        headers.set("Dropbox-API-Arg", String.format("{\"path\":\"%s\"}", remoteFileName));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange("https://content.dropboxapi.com/2/files/download", HttpMethod.GET, entity, byte[].class);

        if ((response == null) || (response.getBody() == null)) {
            throw new IOException("Empty response from the server");
        } else {
            File f = new File(localFileName);
            Files.write(f.toPath(), response.getBody());
        }
    }
}
