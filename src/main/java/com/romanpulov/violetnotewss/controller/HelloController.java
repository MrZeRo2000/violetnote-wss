package com.romanpulov.violetnotewss.controller;

import com.romanpulov.violetnotewss.model.DataItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.File;

@RestController
public class HelloController {

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
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
