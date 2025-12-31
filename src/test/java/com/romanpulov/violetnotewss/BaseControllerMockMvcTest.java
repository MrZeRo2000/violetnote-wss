package com.romanpulov.violetnotewss;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BaseControllerMockMvcTest {

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Exception;
    }

    protected MockMvc mvc;

    protected List<String> logResult = new ArrayList<>();

    protected void addResult (MvcResult mvcResult) throws UnsupportedEncodingException {
        logResult.add(mvcResult.getRequest().getRequestURI());
        logResult.add("Request:");
        try {
            mvcResult.getRequest().setCharacterEncoding(StandardCharsets.UTF_8.name());
            logResult.add(mvcResult.getRequest().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            logResult.add("Unable to log request");
        }
        logResult.add("Response:");
        mvcResult.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name());
        logResult.add(mvcResult.getResponse().getContentAsString());
        logResult.add("==========================");
    }


    protected JsonMapper mapper = new JsonMapper();

    protected String json;

    protected final WebApplicationContext context;

    public BaseControllerMockMvcTest(WebApplicationContext context) {
        this.context = context;
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    protected void runLogged(ThrowableRunnable function, String logFileName) throws Exception {
        try {
            function.run();
        } finally {
            Path f = Paths.get(getTempDir() + logFileName);
            Files.write(f, logResult, StandardCharsets.UTF_8);
        }
    }

    protected String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    protected String prepareTempDirFolder(String path) throws Exception {
        String testFileFolder = getTempDir() + path;
        Path testFileFolderPath = Paths.get(testFileFolder);
        if (Files.exists(testFileFolderPath)) {
            Files.list(testFileFolderPath).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
        } else {
            Files.createDirectory(testFileFolderPath);
        }

        return testFileFolder;
    }
}
