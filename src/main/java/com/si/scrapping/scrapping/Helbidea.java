package com.si.scrapping.scrapping;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Helbidea {

    public String href;

    public String html;

    public HashMap < String, String > toMap() {
        public HashMap < String, String > hashMap()
        hash.put("href", this.href);
        hash.put("html", this.html);

        return hash;
    }
}