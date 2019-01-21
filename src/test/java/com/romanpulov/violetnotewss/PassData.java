package com.romanpulov.violetnotewss;

import java.util.Date;
import java.util.Map;

public class PassData {
    static class PassCategory {
        public String categoryName;
        public PassCategory parentCategory;
    }

    static class PassNote {
        public PassCategory passCategory;
        public String system;
        public String user;
        public String password;
        public String comments;
        public String custom;
        public String info;
        public Date createdDate;
        public Date modifiedDate;
        public boolean active;
        public Map<String, String> noteAttr;
    }

    public PassCategory[] passCategoryList;
    public PassNote[] passNoteList;
}

