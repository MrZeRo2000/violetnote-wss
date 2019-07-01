package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotewss.model.DataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SameReturnValue")
    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        logger.info("Hello from logger");
        return "Hello from HelloController";
    }

    @RequestMapping(value = "/dataitem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataItem getDataItem(){
        DataItem dataItem = new DataItem();
        dataItem.setId(10);
        dataItem.setName("Name 1");
        return dataItem;
    }
}
