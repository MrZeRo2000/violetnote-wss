package com.romanpulov.violetnotewss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
        public abstract void run() throws Exception;
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

    protected ObjectMapper mapper = new ObjectMapper();
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
            Path f = Paths.get(System.getProperty("java.io.tmpdir") + logFileName);
            Files.write(f, logResult, StandardCharsets.UTF_8);
        }
    }
}
